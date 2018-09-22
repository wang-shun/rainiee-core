package com.dimeng.p2p.modules.statistics.console.service;

import java.io.OutputStream;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.modules.statistics.console.service.entity.PropertyStatisticsEntity;


/**
 * 
 * 企业资产统计
 * <功能详细描述>
 * 
 * @author  pengwei
 * @version  [版本号, 2016年6月15日]
 */
public interface QyMoneyManage extends Service {
    /**
     * 查询企业资产统计列表
     * <根据查询条件查询出满足搜索条件的列表>
     * @param query
     * @param paging
     * @return
     * @throws Throwable
     */
    PagingResult<PropertyStatisticsEntity> search(PropertyStatisticsEntity query, Paging paging)
			throws Throwable;
    
    /**
     * 企业资产统计（可用总金额、冻结总金额、账户总金额、账户余额、理财总资产、借款总负债）
     * <根据查询条件统计出满足搜索条件的值>
     * @param query
     * @return
     * @throws Throwable
     */
    public abstract PropertyStatisticsEntity searchAmount(PropertyStatisticsEntity query)
        throws Throwable;
    
    /**
     * 导出满足收索条件的列
     * <功能详细描述>
     * @param qyMoneyEntitys
     * @param outputStream
     * @param charset
     * @throws Throwable
     */
    public void export(PropertyStatisticsEntity[] qyMoneyEntitys, OutputStream outputStream, String charset)
		        throws Throwable;
	
}
