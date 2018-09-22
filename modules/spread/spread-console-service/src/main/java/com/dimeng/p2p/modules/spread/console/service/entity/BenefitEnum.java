package com.dimeng.p2p.modules.spread.console.service.entity;
/**
 *是否受益 
 *
 */
public enum BenefitEnum {
	F("未受益人"),
	S("受益人");
	
	private String name;
	
	BenefitEnum(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
}
