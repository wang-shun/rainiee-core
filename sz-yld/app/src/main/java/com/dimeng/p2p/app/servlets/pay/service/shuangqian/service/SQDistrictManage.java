/*
 * 文 件 名:  SQDistrictManage.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2016年1月11日
 */
package com.dimeng.p2p.app.servlets.pay.service.shuangqian.service;

import java.sql.SQLException;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.S50.entities.T5019;

/**
 * 双乾托管行政区管理接口
 * 
 * @author  zhoulantao
 * @version  [版本号, 2016年1月11日]
 */
public abstract interface SQDistrictManage  extends Service
{
    /**
     * 获取省列表
     * 
     * @return 省份列表信息
     * @throws SQLException 异常信息
     */
    public T5019[] getSheng() throws SQLException;
    
    /**
     * 获取市列表
     * 
     * @param provinceId 省份ID
     * @return 市列表信息
     * @throws SQLException 异常信息
     */
    public T5019[] getShi(int provinceId) throws SQLException;
}
