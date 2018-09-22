/*
 * 文 件 名:  ApplyGuarantorManage
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  heluzhu
 * 修改时间: 2016/6/13
 */
package com.dimeng.p2p.repeater.guarantor;

import java.math.BigDecimal;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S51.entities.T5122;
import com.dimeng.p2p.S61.entities.T6125;
import com.dimeng.p2p.repeater.guarantor.entity.DbRecordEntity;
import com.dimeng.p2p.repeater.guarantor.entity.GuarantorEntity;
import com.dimeng.p2p.repeater.guarantor.query.DbRecordQuery;
import com.dimeng.p2p.repeater.guarantor.query.GuarantorQuery;

/**
 * 申请担保方业务接口
 * <功能详细描述>
 *
 * @author heluzhu
 * @version [版本号, 2016/6/13]
 */
public interface ApplyGuarantorManage extends Service
{
    
    /**
     * 根据当前登录的用户id查询此用户担保方信息
     * @return
     * @throws Throwable
     */
    public T6125 getGuanterInfo()
        throws Throwable;
    
    /**
     * 申请担保人
     * @return
     * @throws Throwable
     */
    public String applyGuarantor()
        throws Throwable;
    
    /**
     * 用户担保列表
     * @param query
     * @param paging
     * @return
     * @throws Throwable
     */
    public PagingResult<GuarantorEntity> searchGuarantorInfos(GuarantorQuery query, Paging paging)
        throws Throwable;
    
    /**
     * 申请担保审核
     * @param id
     * @param status
     * @param desc
     * @throws Throwable
     */
    public void auditApply(int id, String status, String desc)
        throws Throwable;
    
    /**
     * 修改担保额度
     * @param id
     * @param amount
     * @throws Throwable
     */
    public void updateGuarantAmount(int id, BigDecimal amount)
        throws Throwable;
    
    /**
     * 申请担保人
     * @param id
     * @throws Throwable
     */
    public void cancelGuarantor(int id)
        throws Throwable;
    
    /**
     * 查询担保额度交易记录
     * @param query
     * @param paging
     * @return
     * @throws Throwable
     */
    public PagingResult<DbRecordEntity> searchAmountTrandRecords(DbRecordQuery query, Paging paging)
        throws Throwable;
    
    /**
     * 查询交易类型
     * @return
     * @throws Throwable
     */
    public T5122[] searchTypes()
        throws Throwable;
    
    /**
     * 根据担保码查询担保方是否存在
     * @param gCode 担保码
     * @param flag 担保方申请状态
     * @return ture：该担保码已存在；false：该担保码不存在
     * @throws Throwable
     */
    public int getGuarantId(String gCode, boolean flag)
        throws Throwable;
    
    /**
     * 判断用户是否网签
     * @return
     * @throws Throwable
     */
    public boolean isNetSign()
        throws Throwable;
    
    /** 操作类别
     *  日志内容
     * @param type
     * @param log
     * @throws Throwable
     */
    public void writeLog(String type, String log)
        throws Throwable;
    
    /**
     * 修改担保码
     * @param F03 担保码
     * @throws Throwable
     */
    public void updateT6125F03(String F03)
        throws Throwable;
}
