/*
 * 文 件 名:  FddSignatureServiceV25.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  guomianyun
 * 修改时间:  2017年10月26日
 */
package com.dimeng.p2p.signature.fdd.service;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.resource.ResourceNotFoundException;
import com.dimeng.framework.service.Service;
import com.dimeng.p2p.S62.entities.T6230;
import com.dimeng.p2p.S62.entities.T6273;
import com.dimeng.p2p.S62.enums.T6273_F10;
import com.dimeng.p2p.S62.enums.T6273_F15;

/**
 * 法大大电子签章V2.5版
 * <功能详细描述>
 * 
 * @author  guomianyun
 * @version  [版本号, 2017年10月26日]
 */
public interface FddSignatureServiceV25 extends Service
{
    /**
     * 颁发CA证书
     * 调用场景：可以在接入平台用户注册信息实名审核后即时触发，或自行根据业务而定 
     * （但必须在调用文档签署接口前，以保证事先为客户颁发CA证书）
     * @return 法大大客户编号
     * @throws Exception
     */
    public abstract String syncPersonAuto(int accountId);
    
    /**
     * 法大大客户信息修改接口
     * @param userId 用户id
     * @param phoneNo 手机号
     * @return
     */
    public abstract String infoChange(int userId, String phoneNo);
    
    /**
     * 获取法大大客户号
     * @param accountId
     * @return
     * @throws Throwable
     */
    public abstract String getUserCustomerId(int accountId)
        throws Throwable;
    
    /**
     * 文档传输接口
     *      - 将文档上传到法大大平台
     * @param userId 投资用户Id
     * @param bidId 标的Id
     * @param orderId 投资成功订单Id
     * @param userType 用户角色类型 - T6273.F10
     */
    public abstract String uploadFile(int userId, int bidId, int orderId, T6273_F10 userType);
    
    /**
     * 文档传输接口
     *      - 将文档上传到法大大平台 ,不调自动签名
     * @param userId 投资用户Id
     * @param bidId 标的Id
     * @param orderId 投资成功订单Id
     * @param userType 用户角色类型 - T6273.F10
     * @throws Throwable 
     */
    public abstract String uploadFileNoSign(int userId, int bidId, int orderId, T6273_F10 userType)
        throws Throwable;
    
    /**
     * 文档签署接口 - 自动签章（投资人）
     * @param userId 用户id
     * @param bidId 标的id
     * @param orderId 投资成功id - T6250.F01
     * @param userType 用户角色类型
     * @throws SQLException 
     * @throws ResourceNotFoundException 
     */
    public abstract void extsignAuto(int userId, int bidId, int orderId, T6273_F10 userType)
        throws ResourceNotFoundException, SQLException;
    
    /**
     * 文档签署接口 - 手动签章（借款人）
     * @param userId 用户id
     * @param response
     */
    public abstract void extSign(int userId, int bidId, T6273_F10 userType, HttpServletResponse response);
    
    /**
     * 文档签署接口 - 手动签章APP（借款人）
     * @param userId 用户id
     * @returnUrl 页面回调地址
     * @param response
     */
    public abstract String extSignApp(int userId, int bidId, T6273_F10 userType, HttpServletResponse response,
        String returnUrl);
    
    /**
     * 合同归档接口
     * @param contract_id 合同编号
     * @return
     */
    public abstract String contractFiling(String contract_id);
    
    /**
     * 查看已签章文档接口
     * @param contract_id 合同编号
     */
    public abstract void viewContract(String contract_id);
    
    /**
     * 插入签章后文档地址
     * @param userId 用户id
     * @param bidId 标的id
     * @param orderId 投资成功id - T6250.F01
     * @param userType 用户角色 - T6273_F10
     * @param viewUrl   在线查看地址
     * @param uploadUrl 文档下载地址
     * @throws SQLException
     */
    public abstract void updateT6273ForUrl(int userId, int bidId, int orderId, T6273_F10 userType, String viewUrl,
        String uploadUrl)
        throws SQLException;
    
    /**
     * 修改签章状态
     * @param status   当前签章状态 - T6273_F15
     * @param userId    用户id
     * @param bidId     标的id
     * @param orderId   投资成功id - T6250.F01
     * @param userType  用户角色 - T6273_F10
     * @param isUpdateTime  是否更新当前签署时间
     * @throws SQLException
     */
    public abstract void updateT6273ForStatus(T6273_F15 status, int userId, int bidId, int orderId, T6273_F10 userType)
        throws SQLException;
    
    /**
     * 修改签章时交易时间和交易请求号
     * @param transaction_id
     */
    public abstract void updateT6273ForSign(String transaction_id, int userId, int bidId, int orderId,
        T6273_F10 userType)
        throws SQLException;
    
    /**
     * 法大大电子签章合同列表
     * @param userId    用户id
     * @param bidId     标的id
     * @param orderId   投资成功id - T6250.F01
     * @param userType  用户角色 - T6273_F10
     * @return
     * @throws Throwable
     */
    public abstract T6273 selectT6273(int userId, int bidId, int orderId, T6273_F10 userType)
        throws Throwable;
    
    /**
     * 根据标ID获取法大大电子签章投资合同列表信息
     * @return
     * @throws Throwable
     */
    public abstract List<T6273> selectT6273ByDidId(int bidId)
        throws Throwable;
    
    /**
     * 查询标的信息
     * @param F01 标ID
     * @return
     * @throws SQLException
     */
    public abstract T6230 selectT6230(int F01)
        throws SQLException;
    
    /**
     * 查询待签的合同
     */
    public abstract List<T6273> selectUnExtSign(Timestamp timestamp)
        throws SQLException;
    
    /**
     * 根据id查询该法大大签约合同信息
     * @param F01
     * @return
     * @throws SQLException
     */
    public abstract T6273 selectT6273(int F01)
        throws SQLException;
    
    /**
     * 获取用户id
     * @param userCustId 托管第三方账号
     * @return
     * @throws Throwable
     */
    public abstract String getUserId(String userCustId)
        throws Throwable;
    
    /**
     *  签署状态查询接口
     * <功能详细描述>
     * @param sid T6273.F01
     * @return
     * @throws Throwable
     */
    public String querySignStatus(int sid)
        throws Throwable;
    
    /**
     * 根据标ID添加签章基本信息
     * 包括投资 人和借款人的
     * @param bidId
     * @throws Throwable
     */
    public void insertT6273(int bidId)
        throws Throwable;
    
    /**
     * 签平台章
     * <功能详细描述>
     * @param userId
     * @param bidId
     * @param orderId
     * @param userType
     * @throws ResourceNotFoundException
     * @throws SQLException
     * @throws Throwable 
     */
    public void extsignAutoPT(int userId, int bidId, int orderId, T6273_F10 userType)
        throws Throwable;
    
    /**
     * 根据标ID删除该标的待签名信息
     * 如果该标有已签 或待归档或已归档，不能删除
     * @param bidId
     */
    public void deleteT6273byBidId(int bidId)
        throws Throwable;
}
