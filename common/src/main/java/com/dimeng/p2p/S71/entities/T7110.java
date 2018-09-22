package com.dimeng.p2p.S71.entities
;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S71.enums.T7110_F05;
import com.dimeng.p2p.S71.enums.T7110_F09;
import java.sql.Timestamp;

/** 
 * 后台账号
 */
public class T7110 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 自增ID
     */
    public int F01;

    /** 
     * 登陆名
     */
    public String F02;

    /** 
     * 密码
     */
    public String F03;

    /** 
     * 真实姓名
     */
    public String F04;

    /** 
     * 状态,QY:启用;TY:停用
     */
    public T7110_F05 F05;

    /** 
     * 创建时间
     */
    public Timestamp F06;

    /** 
     * 最后登陆时间
     */
    public Timestamp F07;

    /** 
     * 最后登陆IP
     */
    public String F08;

    /** 
     * 是否需要修改密码,S:是;F:否
     */
    public T7110_F09 F09;
    /** 
     * 联系方式
     */
    public String F10;
    /**
    * 职称
    */
    public String F11;
    /**
     * 业务员工号
     */
    public String F12;

    /**
     * 所属部门
     */
    public String F13;

}
