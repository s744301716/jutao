package com.pinyougou.search.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.FilterQuery;
import org.springframework.data.solr.core.query.GroupOptions;
import org.springframework.data.solr.core.query.HighlightOptions;
import org.springframework.data.solr.core.query.HighlightQuery;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleFilterQuery;
import org.springframework.data.solr.core.query.SimpleHighlightQuery;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.GroupEntry;
import org.springframework.data.solr.core.query.result.GroupPage;
import org.springframework.data.solr.core.query.result.GroupResult;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightPage;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;

@Service(timeout = 4000)
public class ItemSearchServiceImpl implements ItemSearchService {

	@Autowired
	private SolrTemplate solrTemplate;

	@Override
	public Map<String, Object> search(Map searchMap) {
		Map map = new HashMap();
		// 关键字空格处理
		String keywords = (String) searchMap.get("keywords");
		searchMap.put("keywords", keywords.replace(" ", ""));

		// 1.查询列表
		map.putAll(searchList(searchMap));

		// 2.分组查询 商品分类列表
		List categoryList = searchCategroupPage(searchMap);
		map.put("categoryList", categoryList);
		// 3.查询品牌和规格列表
		// 3.查询品牌和规格列表
		String categoryName = (String) searchMap.get("category");
		if (!"".equals(categoryName)) {// 如果有分类名称
			map.putAll(searchBrandAndSpecList(categoryName));
		} else {// 如果没有分类名称，按照第一个查询
			if (categoryList.size() > 0) {
				map.putAll(searchBrandAndSpecList((String) categoryList.get(0)));
			}
		}

		return map;

	}

	/**
	 * 查询分类列表
	 * 
	 * @param searchMap
	 * @return
	 */

	public Map searchList(Map searchMap) {
		Map map = new HashMap();
		// 高亮初始化
		HighlightQuery query = new SimpleHighlightQuery();
		HighlightOptions highlightOptions = new HighlightOptions().addField("item_title");// 设置高亮的域
		highlightOptions.setSimplePrefix("<em style='color:red'>");// 高亮前缀
		highlightOptions.setSimplePostfix("</em>");// 高亮后缀
		query.setHighlightOptions(highlightOptions);// 设置高亮选项

		// 1.1 按照关键字查询
		Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
		query.addCriteria(criteria);

		// 1.2按分类筛选
		if (!"".equals(searchMap.get("category"))) {
			Criteria filterCriteria = new Criteria("item_category").is(searchMap.get("category"));
			FilterQuery filterQuery = new SimpleFilterQuery(filterCriteria);
			query.addFilterQuery(filterQuery);
		}

		// 1.3按品牌筛选
		if (!"".equals(searchMap.get("brand"))) {
			Criteria filterCriteria = new Criteria("item_brand").is(searchMap.get("brand"));
			FilterQuery filterQuery = new SimpleFilterQuery(filterCriteria);
			query.addFilterQuery(filterQuery);
		}

		// 1.4过滤规格
		if (searchMap.get("spec") != null) {
			Map<String, String> specMap = (Map) searchMap.get("spec");
			for (String key : specMap.keySet()) {
				Criteria filterCriteria = new Criteria("item_spec_" + key).is(specMap.get(key));
				FilterQuery filterQuery = new SimpleFilterQuery(filterCriteria);
				query.addFilterQuery(filterQuery);
			}
		}

		// 1.5 按价格过滤
		if (!"".equals(searchMap.get("price"))) {
			String priceStr = (String) searchMap.get("price");
			String[] price = priceStr.split("-");
			if (!price[0].equals("0")) { // 如果最低价格不等于0
				FilterQuery filterQuery = new SimpleFilterQuery();
				Criteria filterCriteria = new Criteria("item_price").greaterThanEqual(price[0]);
				filterQuery.addCriteria(filterCriteria);
				query.addFilterQuery(filterQuery);
			}
			if (!price[1].equals("*")) { // 如果最高价格不等于*
				FilterQuery filterQuery = new SimpleFilterQuery();
				Criteria filterCriteria = new Criteria("item_price").lessThanEqual(price[1]);
				filterQuery.addCriteria(filterCriteria);
				query.addFilterQuery(filterQuery);
			}
		}

		// 1.6 分页
		Integer pageNo = (Integer) searchMap.get("pageNo");// 获取页码
		if (pageNo == null) {
			pageNo = 1;
		}
		Integer pageSize = (Integer) searchMap.get("pageSize");
		if (pageSize == null) {
			pageSize = 20;
		}
		query.setOffset((pageNo - 1) * pageSize);// 起始索引
		query.setRows(pageSize);// 每页记录数

		
		//1.7 按价格排序
		String sortValue=(String)searchMap.get("sort");//升序ASC 降序DESC
		String sortField=(String)searchMap.get("sortField");
		if (sortValue!=null && sortField!=null) {
			if (sortValue.equals("ASC")) {
				Sort sort=new Sort(Sort.Direction.ASC,"item_"+sortField);
				query.addSort(sort);
			}
			if (sortValue.equals("DESC")) {
				Sort sort=new Sort(Sort.Direction.DESC,"item_"+sortField);
				query.addSort(sort);
			}
		}
		
		
		
		
		
		
		
		/* 获得高亮结果集 */
		// 高亮页对象
		HighlightPage<TbItem> page = solrTemplate.queryForHighlightPage(query, TbItem.class);
		for (HighlightEntry<TbItem> h : page.getHighlighted()) {// 循环高亮入口集合
			TbItem item = h.getEntity();// 获取原实体类
			if (h.getHighlights().size() > 0 && h.getHighlights().get(0).getSnipplets().size() > 0) {
				item.setTitle(h.getHighlights().get(0).getSnipplets().get(0));// 设置高亮的结果
			}
			// System.out.println(h);
		}
		map.put("rows", page.getContent());
		map.put("totalPages", page.getTotalPages()); // 总页数
		map.put("total", page.getTotalElements()); // 总记录数
		return map;
	}

	private List searchCategroupPage(Map searchMap) {
		List list = new ArrayList();

		Query query = new SimpleQuery("*:*");
		// 按照关键字查询 相当于sql语句中的where
		Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
		query.addCriteria(criteria);
		// 设置分组选项
		GroupOptions groupOptions = new GroupOptions().addGroupByField("item_category");// 相当于sql语句中的 group by
		query.setGroupOptions(groupOptions);

		// 获取分组页
		GroupPage<TbItem> page = solrTemplate.queryForGroupPage(query, TbItem.class);
		// 获取分组结果对象
		GroupResult<TbItem> groupResult = page.getGroupResult("item_category");
		// 获取分组入口页
		Page<GroupEntry<TbItem>> groupEntries = groupResult.getGroupEntries();
		// 获取分组入口集合
		List<GroupEntry<TbItem>> content = groupEntries.getContent();

		for (GroupEntry<TbItem> groupEntry : content) {

			list.add(groupEntry.getGroupValue());// 将分组的结果添加到返回值中
		}

		return list;
	}

	@Autowired
	private RedisTemplate redisTemplate;

	/**
	 * 根据商品分类名称查询品牌和规格列表
	 * 
	 * @param category
	 *            分类名称
	 * @return
	 */
	private Map searchBrandAndSpecList(String category) {
		Map map = new HashMap();
		Long typeId = (Long) redisTemplate.boundHashOps("itemCat").get(category);// 获取模板ID
		if (typeId != null) {
			// 根据模板ID查询品牌列表
			List brandList = (List) redisTemplate.boundHashOps("brandList").get(typeId);
			map.put("brandList", brandList);// 返回值添加品牌列表
			// 根据模板ID查询规格列表
			List specList = (List) redisTemplate.boundHashOps("specList").get(typeId);
			map.put("specList", specList);
		}
		return map;

	}

	@Override
	public void importList(List list) {
		solrTemplate.saveBeans(list);	
		solrTemplate.commit();

	}

	@Override
	public void deleteByGoodsIds(List goodsIdList) {
		System.out.println("删除商品ID"+goodsIdList);
		Query query=new SimpleQuery();		
		Criteria criteria=new Criteria("item_goodsid").in(goodsIdList);
		query.addCriteria(criteria);
		solrTemplate.delete(query);
		solrTemplate.commit();

		
	}

}
