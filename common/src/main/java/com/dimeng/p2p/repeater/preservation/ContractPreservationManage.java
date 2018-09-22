/*
 * 文 件 名:  ContractPreservationManage.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2016年6月16日
 */
package com.dimeng.p2p.repeater.preservation;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S62.entities.T6271;
import com.dimeng.p2p.repeater.preservation.entity.ContractEntity;
import com.dimeng.p2p.repeater.preservation.query.ContractQuery;

/**
 * <一句话功能简述> 
 * <功能详细描述>
 * 
 * @author  xiaoqi
 * @version  [7.2.0, 2016年6月16日]
 */
public interface ContractPreservationManage extends Service
{
    /**
     * <一句话功能简述> 
     * <功能详细描述>
     * @param contractQuery
     * @return
     * @throws Throwable
     */
    PagingResult<ContractEntity> search(ContractQuery contractQuery, Paging paging)
        throws Throwable;
    
    /**
     * <一句话功能简述> 保存放款成后合同生成保全合同列表
     * <功能详细描述>
     * @param bidId 标的ID
     * @throws Throwable
     */
    void insertT6271(int bidId)
        throws Throwable;
    
    /**
     * <一句话功能简述>更新合同保全列表数据
     * <功能详细描述>
     * @param id
     * @throws Throwable
     */
    void updateT6271(T6271 t6271)
        throws Throwable;
    
    /**
     * <一句话功能简述>更新合同保全列表数据
     * <功能详细描述>
     * @param id
     * @throws Throwable
     */
    void updateT6271PdfPath(int id, String path)
        throws Throwable;
    
    /**
     * <一句话功能简述> 根据主键id获取记录
     * <功能详细描述>
     * @param id 主键ID
     * @return
     * @throws Throwable
     */
    T6271 getT6271(int id)
        throws Throwable;
    
    /**
     * <一句话功能简述> 一键保全，获取所有未保全的记录
     * <功能详细描述>
     * @return
     * @throws Throwable
     */
    T6271[] getAllUnPreservedRecord()
        throws Throwable;
    
    /**
     * <一句话功能简述> 根据标的ID查询未生成pdf文档的集合
     * <功能详细描述>
     * @param id
     * @return
     * @throws Throwable
     */
    T6271[] getT6271s(int id)
        throws Throwable;
    
    /** 操作类别
     *  日志内容
     * @param type
     * @param log
     * @throws Throwable
     */
    void writeLog(String type, String log)
        throws Throwable;
}
