/*
 * 文 件 名:  CustomerDetail
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  heluzhu
 * 修改时间: 2016/5/18
 */
package com.dimeng.p2p.repeater.business.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 客户详情查询返回结果
 * <功能详细描述>
 *
 * @author heluzhu
 * @version [版本号, 2016/5/18]
 */
public class CustomerEntity {
    /**
     * 用户名
     */
    public String userName;

    /**
     * 真实姓名
     */
    public String realName;

    /**
     * 手机号码
     */
    public String mobile;

    /**
     * 可用余额
     */
    public BigDecimal usableAmount;

    /**
     * 借款负债
     */
    public BigDecimal loanAmount;

    /**
     * 注册时间
     */
    public Timestamp registeTime;
}
