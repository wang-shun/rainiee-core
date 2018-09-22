package com.dimeng.p2p.modules.account.console.service.query;

import java.sql.Timestamp;

import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.S61.enums.T6110_F17;
import com.dimeng.p2p.S61.enums.T6110_F19;
import com.dimeng.p2p.common.enums.UserState;

/**
 * 企业用户列表查询
 *
 */
public abstract interface EnterpriseQuery
{
    
    /**
     * 用户ID，匹配查询.
     * 
     * @return {@code String}空值无效.
     */
    public abstract String getUserId();
    
    /**
     * 企业名称
     */
    public abstract String getName();
    
    /**
     * 联系人，模糊查询.
     *  
     * @return {@link String}空值无效.
     */
    public abstract String getContacts();
    
    /**
     * 联系人手机号码，模糊查询.
     * @return {@link String}空值无效.
     */
    public abstract String getContactsMobile();
    
    /**
     * 营业执照登记注册号，模糊查询.
     * * @return {@link String}空值无效.
     */
    public abstract String getLicenseNumber();
    
    /**
     * 组织机构号，模糊查询.
     * * @return {@link String}空值无效.
     */
    public abstract String getOrganizationNumber();
    
    /**
     * 企业税号，模糊查询.
     * * @return {@link String}空值无效.
     */
    public abstract String getDutyNumber();
    
    /**
     * 注册时间,大于等于查询.
     * 
     * @return {@link Timestamp}null无效.
     */
    public abstract Timestamp getCreateTimeStart();
    
    /**
     * 注册时间,小于等于查询.
     * 
     * @return {@link Timestamp}null无效.
     */
    public abstract Timestamp getCreateTimeEnd();
    
    /**
     * 状态(ZC正常;SD:锁定),匹配查询.
     * 
     * @return {@link UserState}空值无效.
     */
    public abstract T6110_F07 getUserState();
    
    /**
    * 是否允许投资（S：是；F：否）,匹配查询.
    * 
    * 空值无效.
    */
    public abstract T6110_F17 getInvestType();
    
    /**
     * 企业账号
     * <功能详细描述>
     * @return
     */
    public abstract String getAccount();
    
    /**
     * 是否允许购买不良债权（S：是；F：否）,匹配查询.
     * @return
     */
    public abstract T6110_F19 getBadClaimType();
}
