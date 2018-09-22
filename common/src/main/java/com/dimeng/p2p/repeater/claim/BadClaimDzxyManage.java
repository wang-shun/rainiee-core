/*
 * 文 件 名:  BadClaimDzxyManage.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  huqinfu
 * 修改时间:  2016年6月20日
 */
package com.dimeng.p2p.repeater.claim;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S62.entities.T6264;
import com.dimeng.p2p.repeater.claim.entity.Blzqzrxy;
import com.dimeng.p2p.repeater.claim.query.BlzqzrxyQuery;

/**
 *
 * 不良债权转让电子协议
 * 
 * @author  huqinfu
 * @version  [版本号, 2016年6月20日]
 */
public interface BadClaimDzxyManage extends Service
{
    
    /**
     * 不良债权转让电子协议列表
     * @param paging
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<Blzqzrxy> search(BlzqzrxyQuery query, Paging paging)
        throws Throwable;
    
    /** 
     * 查询不良债权转让申请
     * @param F01
     * @return
     * @throws Throwable
     */
    public T6264 selectT6264(int F01)
        throws Throwable;
    
}
