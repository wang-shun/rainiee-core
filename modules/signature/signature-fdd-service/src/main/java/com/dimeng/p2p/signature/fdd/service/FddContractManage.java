/*
 * 文 件 名:  FddContractManage.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  guomianyun
 * 修改时间:  2017年10月24日
 */
package com.dimeng.p2p.signature.fdd.service;

import java.util.List;
import java.util.Map;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.S62.entities.T6273;
import com.dimeng.p2p.common.entities.Dzxy;

/**
 * 法大大生成合同
 * <功能详细描述>
 * 
 * @author  guomianyun
 * @version  [版本号, 2017年10月24日]
 */
public interface FddContractManage extends Service
{
    
    /**
     * 获取标的投资措款电子协议（三方，四方）
     * @return
     * @throws Throwable
     */
    public abstract Dzxy getBidContent(int loanId)
        throws Throwable;
    
    /**
     * 根据标的ID和用户ID获取合同中的参数
     * @param loanId
     * @param userId
     * @param htCode 合同编号
     * @return
     * @throws Throwable
     */
    public abstract Map<String, Object> getValueMap(int loanId, int userId, String htCode)
        throws Throwable;
    
    /**
     * 更新本地保存地址
     * <功能详细描述>
     * @param t6273
     * @throws Throwable
     */
    public void updateT6273PdfPathNo(T6273 t6273)
        throws Throwable;
    
    /**
     * 根据标ID添加垫付合同签章基本信息
     * 
     * @param bidId
     * @throws Throwable
     */
    public void insertT6273DF(int bidId)
        throws Throwable;
    
    /**
     * 生成垫付合同内容参数
     * @param zqsqId
     * @param uId
     * @return
     */
    public Map<String, Object> getAdvanceContentMap(int bId, int uId, String htCode)
        throws Throwable;
    
    /** 
     * 查询垫付合同保全列表
     * <功能详细描述>
     * @return bId 标的id，T6230.F01
     * @return List<T6271>
     * @throws Throwable
     */
    public abstract List<T6273> getDfList(int bId)
        throws Throwable;
}
