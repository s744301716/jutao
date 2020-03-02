package com.pinyougou.pojoroup;

import java.io.Serializable;
/**
 * 商品组合实体类
 * @author 沈嘉庆
 *
 */
import java.util.List;

import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.TbGoodsDesc;
import com.pinyougou.pojo.TbItem;
public class Goods implements Serializable{
	private TbGoods goods;
	private TbGoodsDesc goodsDesc;
	
	private List<TbItem> itemsList;

	public TbGoods getGoods() {
		return goods;
	}

	public void setGoods(TbGoods goods) {
		this.goods = goods;
	}

	public TbGoodsDesc getGoodsDesc() {
		return goodsDesc;
	}

	public void setGoodsDesc(TbGoodsDesc goodsDesc) {
		this.goodsDesc = goodsDesc;
	}

	public List<TbItem> getItemsList() {
		return itemsList;
	}

	public void setItemsList(List<TbItem> itemsList) {
		this.itemsList = itemsList;
	}
	public Goods() {}
	public Goods(TbGoods goods, TbGoodsDesc goodsDesc, List<TbItem> itemsList) {
		super();
		this.goods = goods;
		this.goodsDesc = goodsDesc;
		this.itemsList = itemsList;
	}
	
}
