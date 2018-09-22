package com.dimeng.p2p.S62.entities
;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S62.enums.T6211_F03;

/** 
 * 核心企业与标的映射
 */
public class CompanyMapping extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 自增ID
     */
    public int id;

    /** 
     * 标ID
     */
    public int bid;

    /** 
     * 核心企业对应用户id
     */
    public int userId;
    
    
    public String nounUser;
    
    
    
    

}
