package com.dimeng.p2p.repeater.preservation.entity;

import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.entities.T6141;
import com.dimeng.p2p.S61.entities.T6161;

/**
 * 
 * 网签协议
 * <功能详细描述>
 * 
 * @author  pengwei
 * @version  [版本号, 2016年6月20日]
 */
public class Wqxy {
	/**
	 * 协议编号
	 */
	public String xy_no;
	    
    /**
     * 网签用户信息
     */
    public T6110 t6110;
    
    /**
     * 网签用户基本信息
     */
    public T6141 t6141;
    
    /**
     * 网签用户公司信息
     */
    public T6161 t6161;
    
    /**
     * 用户名
     */
    public String userName;
    
    /**
     * 真是姓名
     */
    public String realName;
    
    /**
     * 证件号
     */
    public String IDNum;
}
