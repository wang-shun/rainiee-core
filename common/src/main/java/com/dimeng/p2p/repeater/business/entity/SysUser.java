package com.dimeng.p2p.repeater.business.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import com.dimeng.p2p.common.enums.SysAccountStatus;

/**
 * 系统用户
 * 
 * @author guopeng
 * 
 */
public class SysUser implements Serializable
{
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 后台账号表 ID
     */
    public int id;
    
    /**
     * 登录名
     */
    public String accountName;
    
    /**
     * 密码
     */
    public String password;
    
    /**
     * 真实姓名
     */
    public String name;
    
    /**
     * 状态(启用,停用)
     */
    public SysAccountStatus status;
    
    /**
     * 创建时间
     */
    public Timestamp createTime;
    
    /**
     * 最后登录时间
     */
    public Timestamp lastTime;
    
    /**
     * 登录IP
     */
    public String lastIp;
    
    /**
     * 角色Id
     */
    public int roleId;
    
    /**
     * 角色名称
     */
    public String roleName;
    
    /**
    * 联系方式：手机号码
    */
    public String phone;
    
    /** 
     * 职称
     */
    public String pos;
    
    /**
     * 业务员工号
     */
    public String employNum;
    
    /**
     * 所属部门
     */
    public String dept;
    
    /**
     * 客户数
     */
    public int customNum;
}
