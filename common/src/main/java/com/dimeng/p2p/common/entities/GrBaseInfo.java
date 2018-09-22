/*
 * 文 件 名:  GrBaseInfo.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  God
 * 修改时间:  2016年5月10日
 */
package com.dimeng.p2p.common.entities;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6118_F02;
import com.dimeng.p2p.S61.enums.T6118_F05;

/**
 * <个人基础信息>
 * <验证用户是否实名验证，手机、交易密码、邮箱设置>
 * 
 * @author  zhoucl
 * @version  [版本号, 2016年5月10日]
 */
public class GrBaseInfo extends AbstractEntity
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 7310361947190291864L;
    
    /**
     * 手机号码
     */
    public String phone;
    
    /**
     * 邮箱
     */
    public String mail;
    
    /**
     * 用户类型
     */
    public T6110_F06 userType;
    
    /**
     * 身份认证状态
     */
    public T6118_F02 idCarStatus;
    
    /**
     * 交易密码
     */
    public T6118_F05 jyPwdStatus;
    
}
