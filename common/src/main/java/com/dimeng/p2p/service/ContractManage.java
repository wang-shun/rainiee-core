/*
 * 文 件 名:  ContractManage.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoucl
 * 修改时间:  2016年6月29日
 */
package com.dimeng.p2p.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.S62.entities.T6265;
import com.dimeng.p2p.S62.entities.T6271;
import com.dimeng.p2p.common.entities.Dzxy;
import com.dimeng.p2p.common.entities.DzxyBlzqzr;
import com.dimeng.p2p.common.entities.DzxyDy;

/**
 * <合同>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2016年6月29日]
 */
public interface ContractManage extends Service
{
    
    /**
     * 生成债权转让合同内容参数
     * @param zqsqId 债权申请id
     * @param uId 用户id
     * @return
     */
    Map<String, Object> getClaimContentMap(int zqsqId, int uId)
        throws Throwable;
    
    /** 
     * 查询债权转让合同保全列表
     * <功能详细描述>
     * @return zcbId 债权申请id
     * @return List<T6271>
     * @throws Throwable
     */
    public abstract List<T6271> getClaimList(int zcbId)
        throws Throwable;
    
    /**
     * <一句话功能简述>更新合同保全列表数据
     * <功能详细描述>
     * @param t6271
     * @throws Throwable
     */
    void updateT6271PdfPathNo(T6271 t6271)
        throws Throwable;
    
    /**
     * 生成垫付合同内容参数
     * @param zqsqId
     * @param uId
     * @return
     */
    public Map<String, Object> getAdvanceContentMap(int bId, int uId)
        throws Throwable;
    
    /**
     * 获取标的电子协议
     * @return
     * @throws Throwable
     */
    public abstract Dzxy getBidContent(int loanId)
        throws Throwable;
    
    /**
     * 获取抵押担保标的信息
     * @param loandId
     * @return
     * @throws Throwable
     */
    public abstract DzxyDy getDzxyDy(int loanId, int tzUserId)
        throws Throwable;
    
    /**
     * 获取信用标的信息
     * @param loandId
     * @return
     * @throws Throwable
     */
    public abstract DzxyDy getDzxyXy(int loanId, int tzUserId)
        throws Throwable;
    
    /**
     * 根据标的ID和用户ID获取合同中的参数
     * @param loanId
     * @param userId
     * @return
     * @throws Throwable
     */
    public abstract Map<String, Object> getValueMap(int loanId, int userId)
        throws Throwable;
    
    /** 
     * 获取不良债权转让的电子协议
     * @param blzqzrjlId 转让记录id
     * @param zqId 债权id
     * @param reqType 访问类型：前台：front;后台：console
     * @return
     * @throws Throwable
     */
    public abstract Dzxy getBlzqzr(int blzqzrjlId, int zqId, String reqType)
        throws Throwable;
    
    /**
     * 获取不良债权转让申请的信息
     * @param blzqzrjlId 转让记录id
     * @param zqId 债权id
     * @param userId 用户id
     * @param type 查看类型：转出人：ZCR;受让人:SRR
     * @param reqType 访问类型：前台：front;后台:console
     * @return
     * @throws Throwable
     */
    public abstract DzxyBlzqzr getDzxyBlzqzr(int blzqzrjlId, int zqId, int userId, String type, String reqType)
        throws Throwable;
    
    /**
     * 组装不良债权转让协议数据
     * @param blzqzrjlId 转让记录id
     * @param zqId 债权id
     * @param userId 用户id
     * @param type 查看类型：转出人：ZCR;受让人:SRR
     * @param reqType 访问类型：前台：front;后台:console
     * @return
     * @throws Throwable
     */
    public abstract Map<String, Object> getBadClaimContentMap(int blzqzrjlId, int zqId, int userId, String type,
        String reqType)
        throws Throwable;
    
    /** 
     * 根据债权转让申请id获取转让记录
     * @param F02
     * @return
     * @throws Throwable
     */
    public T6265 selectT6265(int F02)
        throws Throwable;
    
    /** 
     * 根据不良债权转让id查询保全记录
     * @param blzqzrId
     * @return
     * @throws Throwable
     */
    public List<T6271> getBadClaimList(int blzqzrId)
        throws Throwable;
    
    /** 
     * 查询垫付合同保全列表
     * <功能详细描述>
     * @return bId 标的id，T6230.F01
     * @return List<T6271>
     * @throws Throwable
     */
    public abstract List<T6271> getDfList(int bId)
        throws Throwable;
    
    /**
     * 把输入的金额转换为汉语中人民币的大写
     * 
     * @param amount 输入的金额
     * @return 对应的汉语大写
     * @throws Throwable
     */
    public String amountConvertCN(BigDecimal amount)
        throws Throwable;
}
