/**
 * 
 */
package com.dimeng.p2p.modules.account.console.service.entity;

import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.S61.enums.T6110_F17;
import com.dimeng.p2p.common.enums.OverdueEnum;
import com.dimeng.p2p.common.enums.UserState;
import com.dimeng.p2p.common.enums.UserStatus;

public class Enterprise extends AbstractEntity
{
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 用户ID
     */
    public int userId;
    
    /**
     * 用户名
     */
    public String userName;
    
    /**
     * 真实姓名
     */
    public String realName;
    
    /**
     * 手机
     */
    public String msisdn;
    
    /**
     * 邮箱
     */
    public String mailbox;
    
    /**
     * 身份证
     */
    public String identityCard;
    
    /**
     * 注册时间
     */
    public Timestamp registrationTime;
    
    /**
     * 状态(ZC正常;SD:锁定)
     */
    public UserState userState;
    
    /**
     * 拉黑状态
     */
    public UserStatus userStatus;
    
    /**
     * 账号
     */
    public String account;
    
    /**
     * 密码
     */
    public String password;
    
    /**
     * 手机号
     */
    public String mobile;
    
    /**
     * 状态,ZC正常;SD:锁定
     */
    public String status;
    
    /**
     * 注册来源,ZC:注册;HT:后台;
     */
    public String source;
    
    /**
     * 逾期状态
     */
    public OverdueEnum sfyq;
    
    /**
     * 企业税号
     */
    public String dutyNumber;
    
    /**
     * 企业名称
     */
    public String name;
    
    /**
     * 企业联系地址
     */
    public String address;
    
    /**
     * 联系人
     */
    public String contacts;
    
    /**
     * 联系人手机号码
     */
    public String contactsMobile;
    
    /**
     * 营业执照登记注册号
     */
    public String licenseNumber;
    
    /**
     * 组织机构号
     */
    public String organizationNumber;
    
    /**
     * 邮箱地址
     */
    public String email;
    
    /** 
     * 用户ID,自增
     */
    public int F01;
    
    /** 
     * 用户登录账号
     */
    public String F02;
    
    /** 
     * 手机号码
     */
    public String F03;
    
    /** 
     * 邮箱
     */
    public String F04;
    
    /** 
     * 用户类型,ZRR:自然人;FZRR:非自然人
     */
    public T6110_F06 F05;
    
    /** 
     * 状态,QY启用;SD:锁定;HMD:黑名单;
     */
    public T6110_F07 F06;
    
    /** 
     * 注册时间
     */
    public Timestamp F07;
    
    /** 
     * 企业编号
     */
    public String F08;
    
    /** 
     * 营业执照登记注册号,唯一
     */
    public String F09;
    
    /** 
     * 企业名称
     */
    public String F10;
    
    /** 
     * 企业纳税号
     */
    public String F11;
    
    /** 
     * 组织机构代码
     */
    public String F12;
    
    /** 
     * 法人
     */
    public String F13;
    
    /** 
     * 法人身份证号,9-16位星号替换
     */
    public String F14;
    
    /**
     * 联系人
     */
    public String lxr;
    
    /**
     * 联系人电话
     */
    public String lxrPhone;
    
    /**
     * 社会信用代码
     */
    public String shxydm;
    
    /**
     * 是否社会信用代码
     */
    public String isShxydm;
    
    /** 
     * 是否允许投资：S：是；F：否;
     */
    public T6110_F17 investType;
    
    /**
     * 是否待审核
     */
    public String dshFlg;
    
}