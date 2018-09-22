/*
 * 文 件 名:  BadClaimTransferManage.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  God
 * 修改时间:  2016年6月14日
 */
package com.dimeng.p2p.repeater.claim;

import java.io.OutputStream;
import java.util.List;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.entities.T6161;
import com.dimeng.p2p.S62.entities.T6264;
import com.dimeng.p2p.repeater.claim.entity.BadAssets;
import com.dimeng.p2p.repeater.claim.entity.BadClaimDsh;
import com.dimeng.p2p.repeater.claim.entity.BadClaimShDetails;
import com.dimeng.p2p.repeater.claim.entity.BadClaimYzr;
import com.dimeng.p2p.repeater.claim.entity.BadClaimZr;
import com.dimeng.p2p.repeater.claim.entity.BadClaimZrDetails;
import com.dimeng.p2p.repeater.claim.query.DshQuery;
import com.dimeng.p2p.repeater.claim.query.DzrQuery;
import com.dimeng.p2p.repeater.claim.query.YzrQuery;

/**
 * <不良债权转让管理>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2016年6月14日]
 */
public interface BadClaimTransferManage extends Service
{
    
    /** 
     * 查询不良债权待转让记录
     * <功能详细描述>
     * @param dzrQuery
     * @param paging
     * @return PagingResult<BadClaimZr>
     * @throws Throwable
     */
    public abstract PagingResult<BadClaimZr> badClaimDzrSearch(DzrQuery dzrQuery, Paging paging)
        throws Throwable;
    
    /** 
     * 查询不良债权待转让详情
     * <功能详细描述>
     * @param bidId
     * @return BadClaimZrDetails
     * @throws Throwable
     */
    public abstract BadClaimZrDetails getBadClaimZrDetailsr(int bidId)
        throws Throwable;
    
    /** 
     * 不良债权转让
     * <功能详细描述>
     * @param bidId
     * @param period
     * @return BadClaimZrDetails
     * @throws Throwable
     */
    public abstract void transfer(int bidId, int period)
        throws Throwable;
    
    /** 
     * 查询不良债权待审核记录
     * <功能详细描述>
     * @param dshQuery
     * @param paging
     * @return PagingResult<BadClaimDsh>
     * @throws Throwable
     */
    public abstract PagingResult<BadClaimDsh> badClaimDshSearch(DshQuery dshQuery, Paging paging)
        throws Throwable;
    
    /** 
     * 查询T6264
     * <功能详细描述>
     * @param id
     * @return BadClaimZrDetails
     * @throws Throwable
     */
    public abstract T6264 getT6264ByF01(int id)
        throws Throwable;
    
    /** 
     * 不良债权审核
     * <功能详细描述>
     * @param t6264
     * @throws Throwable
     */
    public abstract void check(T6264 t6264)
        throws Throwable;
    
    /** 
     * 不良债权下架
     * <功能详细描述>
     * @param t6264
     * @throws Throwable
     */
    public abstract void ban(T6264 t6264)
        throws Throwable;
    
    /** 
     * 查询不良债权转让失败记录
     * <功能详细描述>
     * @param dshQuery
     * @param paging
     * @return PagingResult<BadClaimDsh>
     * @throws Throwable
     */
    public abstract PagingResult<BadClaimDsh> badClaimZrsbSearch(DshQuery dshQuery, Paging paging)
        throws Throwable;
    
    /** 
     * 查询不良债权已转让记录
     * <功能详细描述>
     * @param yzrQuery
     * @param paging
     * @return PagingResult<BadClaimYzr>
     * @throws Throwable
     */
    public abstract PagingResult<BadClaimYzr> badClaimYzrSearch(YzrQuery yzrQuery, Paging paging)
        throws Throwable;
    
    /** 
     * 查询所有债权接收方
     * <功能详细描述>
     * @return List<T6161>
     * @throws Throwable
     */
    public abstract List<T6161> getClaimReceiverList()
        throws Throwable;
    
    /**
     * <导出不良债权待转让>
     * <功能详细描述>
     * @param badClaimZrs
     * @param outputStream
     * @param charset
     * @throws Throwable
     */
    public abstract void exportBlzqDzr(BadClaimZr[] badClaimZrs, OutputStream outputStream, String charset)
        throws Throwable;
    
    /**
     * <导出不良债权待审核>
     * <功能详细描述>
     * @param badClaimDsh
     * @param outputStream
     * @param charset
     * @throws Throwable
     */
    public abstract void exportBlzqDsh(BadClaimDsh[] badClaimDshs, OutputStream outputStream, String charset)
        throws Throwable;
    
    /**
     * <导出不良债权转让中>
     * <功能详细描述>
     * @param badClaimDsh
     * @param outputStream
     * @param charset
     * @throws Throwable
     */
    public abstract void exportBlzqZrz(BadClaimDsh[] badClaimDshs, OutputStream outputStream, String charset)
        throws Throwable;
    
    /**
     * <导出不良债权已转让>
     * <功能详细描述>
     * @param badClaimYzr
     * @param outputStream
     * @param charset
     * @throws Throwable
     */
    public abstract void exportBlzqYzr(BadClaimYzr[] badClaimYzrs, OutputStream outputStream, String charset)
        throws Throwable;
    
    /**
     * <导出不良债权转让失败>
     * <功能详细描述>
     * @param badClaimDsh
     * @param outputStream
     * @param charset
     * @throws Throwable
     */
    public abstract void exportBlzqZrsb(BadClaimDsh[] badClaimDshs, OutputStream outputStream, String charset)
        throws Throwable;
    
    /**
     * <不良资产总金额>
     * <功能详细描述>
     * @param query
     * @return
     * @throws Throwable
     */
    public abstract BadAssets getBadAssetsTotal(YzrQuery query)
        throws Throwable;
    
    /** 
     * 查询不良债权审核详情
     * <功能详细描述>
     * @param id
     * @return BadClaimShDetails
     * @throws Throwable
     */
    public abstract BadClaimShDetails getBadClaimShDetailsr(int id)
        throws Throwable;
}
