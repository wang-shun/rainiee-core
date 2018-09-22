package com.dimeng.p2p.repeater.preservation;

import java.util.Map;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S62.entities.T6272;
import com.dimeng.p2p.common.entities.Dzxy;
import com.dimeng.p2p.repeater.preservation.entity.AgreementSave;
import com.dimeng.p2p.repeater.preservation.query.AgreementSaveQuery;

/**
 * 
 * 协议保全列表查询
 * <功能详细描述>
 * 
 * @author  pengwei
 * @version  [版本号, 2016年6月16日]
 */
public interface AgreementSaveManage extends Service {

    /**
     * 协议保全列表查询
     * <功能详细描述>
     * @param query
     * @param paging
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<AgreementSave> searchAgreementSaveList(AgreementSaveQuery query, Paging paging)
        throws Throwable;
    
    /**
     * 协议未保全列表查询
     * <功能详细描述>
     * @param query
     * @return
     * @throws Throwable
     */
    public abstract AgreementSave[] searchAgreementNotSaveList(AgreementSaveQuery query)
        throws Throwable;
    
    /**
     * 查询协议信息
     * <功能详细描述>
     * @param saveId
     * @return
     * @throws Throwable
     */
    public abstract T6272 getAgreementInfo(int saveId)
        throws Throwable;
    
    /**
     * 获取网签协议
     * <功能详细描述>
     * @return
     * @throws Throwable
     */
    public abstract Dzxy getSignContent()
        throws Throwable;
    
    /**
     * 查询网签协议保全里所需参数Map
     * <功能详细描述>
     * @param versionNum
     * @return
     * @throws Throwable
     */
    public abstract Map<String, Object> getValueMap(int userId)
        throws Throwable;
    
    /**
     * 更新网签协议内容
     * <功能详细描述>
     * @param t6272
     * @return
     * @throws Throwable
     */
    public abstract boolean updateAgreementContent(int id, String path)
        throws Throwable;
	
}
