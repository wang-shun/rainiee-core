/*
 * 文 件 名:  CustomerQuery
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  heluzhu
 * 修改时间: 2016/5/18
 */
package com.dimeng.p2p.repeater.business.query;

import java.sql.Timestamp;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author heluzhu
 * @version [版本号, 2016/5/18]
 */
public abstract interface CustomerQuery {

    /**
     * 用户名
     * @return
     */
    public abstract String getUserName();

    /**
     * 真实姓名
     * @return
     */
    public abstract String getRealName();

    /**
     * 手机号码
     * @return
     */
    public abstract String getMobile();

    /**
     * 是否注册第三方
     * @return
     */
    public abstract String getIsThird();

    /**
     * 时间,大于等于查询.
     *
     * @return {@link Timestamp}null无效.
     */
    public abstract Timestamp getCreateTimeStart();

    /**
     * 时间,小于等于查询.
     *
     * @return {@link Timestamp}null无效.
     */
    public abstract Timestamp getCreateTimeEnd();

    /**
     * 业务员工号
     * @return
     */
    public abstract String getEmployNum();
}
