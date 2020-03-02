package com.pinyougou.sellergoods.service;

import java.util.List;
import java.util.Map;

import com.pinyougou.pojo.TbBrand;

import entity.PageResult;

/**
 * 品牌接口
 * @author 沈嘉庆
 *
 */
public interface BrandService {
	public List<TbBrand> findAll();
	/**
	 * 品牌分页 
	 * @param pageNum 当前页面
	 * @param pageSize 每页记录数
	 * @return
	 */
	public PageResult findPage(int pageNum,int pageSize);
	
	/**
	 * 品牌新增
	 * @param brand
	 */
	public void add(TbBrand brand);
	/**
	 * 根据id查询
	 * @param id
	 * @return
	 */
	public TbBrand findOne(Long id);
	/**
	 * 品牌修改
	 * @param brand
	 */
	public void Update(TbBrand brand);
	/**
	 * 品牌删除
	 * @param ids
	 */
	public void delete(Long[] ids);
	
	/**
	 * 条件查询，品牌分页 
	 * @param pageNum 当前页面
	 * @param pageSize 每页记录数
	 * @return
	 */
	public PageResult findPage(TbBrand brand, int pageNum,int pageSize);
	
	/**
	 * 返回下拉列表
	 * @return
	 */
	public List<Map> selectOptionList();
}
