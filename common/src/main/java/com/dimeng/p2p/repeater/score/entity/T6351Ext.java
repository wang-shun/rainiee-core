package com.dimeng.p2p.repeater.score.entity;


import com.dimeng.p2p.S63.entities.T6351;
import com.dimeng.p2p.S63.enums.T6350_F07;

public class T6351Ext extends T6351 {
	
	/**
     * 注释内容
     */
    private static final long serialVersionUID = 8829006022385508006L;
    
    public String createName;
    
    /**
     * 商品类别名称
     */
    public String commTypeName;
    
    /**
     * 购买总数
     */
    public int buyTotal;
    
    /**
     * 商品类别 kind:实物，fee:话费
     */
    public T6350_F07 commTypeEnum;

    /**
     * 成交记录数
     */
    public int recordNum;

    /**
     * 规则说明
     */
    public String rule;

}