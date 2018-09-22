/*
 * 文 件 名:  BadClaimManage.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  huqinfu
 * 修改时间:  2016年7月4日
 */
package com.dimeng.p2p.repeater.claim;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.repeater.claim.entity.BuyBadClaimRecord;
import com.dimeng.p2p.repeater.claim.entity.BuyBadClaimRecordQuery;

/**
 * 
 * <不良债权转让管理接口>
 * 
 * @author  huqinfu
 * @version  [版本号, 2016年7月4日]
 */
public interface BadClaimManage extends Service
{
    /** 
     * <查询不良债权购买记录列表>
     * @param query
     * @param paging
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<BuyBadClaimRecord> getBuyBadClaimRecord(BuyBadClaimRecordQuery query, Paging paging)
        throws Throwable;
}
