package com.dimeng.p2p.modules.statistics.console.service;

import java.io.OutputStream;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.modules.statistics.console.service.entity.PropertyStatisticsEntity;


/**
 * 
 * 机构资产统计
 * <功能详细描述>
 * 
 * @author  pengwei
 * @version  [版本号, 2016年6月15日]
 */
public interface JgMoneyManage extends Service {

    PagingResult<PropertyStatisticsEntity> search(PropertyStatisticsEntity query, Paging paging)
			throws Throwable;
    
    public abstract PropertyStatisticsEntity searchAmount(PropertyStatisticsEntity query)
        throws Throwable;
    
    public void export(PropertyStatisticsEntity[] grMoneyEntitys, OutputStream outputStream, String charset)
		        throws Throwable;
	
}
