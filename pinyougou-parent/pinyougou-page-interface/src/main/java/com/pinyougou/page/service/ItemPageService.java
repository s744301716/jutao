package com.pinyougou.page.service;
/**
 * 商品详页接口
 * @author 沈嘉庆
 *
 */
public interface ItemPageService {

	public boolean genItemHtml (Long goodsId);
	
	/**
	 * 删除商品详细页
	 * @param goodsId
	 * @return
	 */
	public boolean deleteItemHtml(Long[] goodsIds);

}
