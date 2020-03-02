package com.pinyougou.pojoroup;

import java.io.Serializable;
import java.util.List;

import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.pojo.TbSpecificationOption;

/**
 * 规格组合实体类
 * @author 沈嘉庆
 *
 */
public class Specification implements Serializable{
	private TbSpecification specification;
	
	private List<TbSpecificationOption> specificationOptions;

	public TbSpecification getSpecification() {
		return specification;
	}

	public void setSpecification(TbSpecification specification) {
		this.specification = specification;
	}

	public List<TbSpecificationOption> getSpecificationOptions() {
		return specificationOptions;
	}

	public void setSpecificationOptions(List<TbSpecificationOption> specificationOptions) {
		this.specificationOptions = specificationOptions;
	}
	public Specification() {}
	public Specification(TbSpecification specification, List<TbSpecificationOption> specificationOptions) {
		super();
		this.specification = specification;
		this.specificationOptions = specificationOptions;
	}
	
}
