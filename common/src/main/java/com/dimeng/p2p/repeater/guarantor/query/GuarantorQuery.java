/*
 * 文 件 名:  GuarantorQuery
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  heluzhu
 * 修改时间: 2016/6/13
 */
package com.dimeng.p2p.repeater.guarantor.query;

/**
 * 申请担保方查询条件实体
 * <功能详细描述>
 *
 * @author heluzhu
 * @version [版本号, 2016/6/13]
 */
public abstract interface GuarantorQuery {

    /**
     * 用户名
     * @return
     */
    public abstract String getUserName();

    /**
     * 姓名/企业名
     * @return
     */
    public abstract String getRealName();

    /**
     * 担保码
     * @return
     */
    public abstract String getCode();

    /**
     * 用户类型
     * @return
     */
    public abstract String getUserType();
}
