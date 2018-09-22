package com.dimeng.p2p.signature.fdd.achieve;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.dimeng.framework.resource.ResourceNotFoundException;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S62.entities.T6230;
import com.dimeng.p2p.S62.entities.T6250;
import com.dimeng.p2p.S62.entities.T6273;
import com.dimeng.p2p.S62.enums.T6273_F07;
import com.dimeng.p2p.S62.enums.T6273_F08;
import com.dimeng.p2p.S62.enums.T6273_F10;
import com.dimeng.p2p.S62.enums.T6273_F15;
import com.dimeng.p2p.signature.fdd.enums.FDDReturnStatus;
import com.dimeng.p2p.signature.fdd.service.IFddSignatureService;
import com.dimeng.p2p.signature.fdd.utils.FddClient;
import com.dimeng.p2p.signature.fdd.variables.FddVariable;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.p2p.variables.defines.URLVariable;
import com.dimeng.util.StringHelper;

/**
 * 法大大接口请求实现
 * 文  件  名：FddSignatureServiceImpl.java
 * 版        权：深圳市迪蒙网络科技有限公司
 * 类  描  述：
 * 修  改  人：ZhangXu
 * 修改时间：2016年12月20日
 */
public class FddSignatureServiceImpl extends AbstractFddService implements IFddSignatureService
{
    private static final Logger logger = Logger.getLogger(FddSignatureServiceImpl.class);
    
    public FddSignatureServiceImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public String getUserId(String userCustId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01 FROM S61.T6119 WHERE T6119.F03 = ? LIMIT 1"))
            {
                pstmt.setString(1, userCustId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return resultSet.getString(1);
                    }
                }
            }
        }
        return null;
    }
    
    @Override
    public String infoChange(int userId, String phoneNo)
    {
        try
        {
            try (Connection connection = getConnection())
            {
                logger.info("法大大【客户信息修改】---用户id:" + userId + "，手机号：" + phoneNo);
                T6110 t6110 = selectT6110(connection, userId);
                if (null != t6110)
                {
                    String customerId = t6110.F20; //客户编号
                    logger.info("法大大【客户信息修改】客户编号：" + customerId);
                    
                    //客户信息修改接口请求
                    String result = FddClient.invokeInfoChange("", phoneNo, customerId);
                    logger.info("---法大大【客户信息修改】返回结果：" + result);
                    
                    JSONObject outobj = JSONObject.fromObject(result);
                    String resultInfoChange = outobj.getString("result");
                    String code = outobj.getString("code");
                    if ("success".equals(resultInfoChange) && FDDReturnStatus.SUCCESS.code().equals(code))
                    {
                        logger.info("---法大大【客户信息修改】成功！！！");
                    }
                    else
                    {
                        throw new Exception("法大大【客户信息修改】失败！原因:" + outobj.getString("msg"));
                    }
                }
            }
        }
        catch (Exception e)
        {
            logger.error("法大大客户信息修改错误", e);
        }
        return null;
    }
    
    @Override
    public String syncPersonAuto(int accountId)
    {
        try
        {
            String realName = "";
            String identifyId = "";
            String mobile = "";
            try (Connection connection = getConnection())
            {
                try (PreparedStatement ps =
                    connection.prepareStatement("SELECT T1.F02, T1.F07, T2.F04 FROM S61.T6141 T1 "
                        + " INNER JOIN S61.T6110 T2 ON T2.F01=T1.F01 WHERE 1=1 AND T1.F01=?"))
                {
                    ps.setInt(1, accountId);
                    try (ResultSet resultSet = ps.executeQuery())
                    {
                        if (resultSet.next())
                        {
                            realName = resultSet.getString(1);
                            identifyId = StringHelper.decode(resultSet.getString(2));
                            mobile = resultSet.getString(3);
                        }
                    }
                }
                String returnStr = FddClient.invokeSyncPersonAuto(realName, "", identifyId, mobile);
                logger.info("法大大颁发CA证书返回结果：" + returnStr);
                
                JSONObject outobj = JSONObject.fromObject(returnStr);
                String result = outobj.getString("result");
                String code = outobj.getString("code");
                if ("success".equals(result) && FDDReturnStatus.SUCCESS.code().equals(code))
                {
                    String customerId = outobj.getString("customer_id");
                    try (PreparedStatement ps = connection.prepareStatement("UPDATE S61.T6110 SET F20=? WHERE F01=?"))
                    {
                        ps.setString(1, customerId);
                        ps.setInt(2, accountId);
                        ps.execute();
                    }
                    return customerId;
                }
            }
        }
        catch (Throwable t)
        {
            logger.error("法大大颁发CA证书错误", t);
        }
        return null;
    }
    
    /**
     * 10. 文档传输接口
     */
    @Override
    public String uploadFile(int userId, int bidId, int orderId, T6273_F10 userType)
    {
        String resultUploadDoc = "";
        try
        {
            try (Connection connection = getConnection())
            {
                //查询电子签章合同信息
                T6273 t6273 = selectT6273(connection, userId, bidId, orderId, userType);
                
                if (T6273_F15.DQM == t6273.F15)
                {
                    return "success";
                }
                T6230 t6230 = selectT6230(connection, bidId);
                
                //请求参数
                String filePath = t6273.F09; //合同本地存储路径
                if (StringUtils.isEmpty(filePath))
                {
                    throw new Exception("合同文件列表不存在！");
                }
                String contract_id = t6273.F04; //此合同编号必须与文档签署接口的合同编号保持一致,长度 <=32
                String doc_title = t6230.F03; //必选 合同标题
                String doc_url = ""; //可选 文档地址(公网地址) doc_url 和 file 两个参数必选一
                File file = new File(filePath); //可选 文档 File 文件 doc_url 和 file 两个参数必选一
                String doc_type = ".pdf"; //必选 文档类型
                logger.info("法大大【文档传输】合同编号：" + contract_id);
                
                //合同上传接口请求
                String result = FddClient.invokeUploadDocs(contract_id, doc_title, file, doc_url, doc_type);
                logger.info("---法大大【文档传输】返回结果：" + result);
                
                JSONObject outobj = JSONObject.fromObject(result);
                resultUploadDoc = outobj.getString("result");
                String code = outobj.getString("code");
                logger.info("---法大大【文档传输】" + outobj.getString("msg"));
                if ("success".equals(resultUploadDoc) && FDDReturnStatus.SUCCESS.code().equals(code))
                {
                    //合同上传成功则改状态为“待签名-DQM”，再调用自动签章
                    updateT6273ForStatus(connection, T6273_F15.DQM, userId, bidId, orderId, userType);
                    
                    if (userType != T6273_F10.JKR)
                    {
                        this.extsignAuto(connection, userId, bidId, orderId, userType);
                    }
                    return resultUploadDoc;
                }
                else
                {
                    throw new Exception("法大大【文档传输】失败！原因:" + outobj.getString("msg"));
                }
            }
        }
        catch (Throwable e)
        {
            logger.error("文档上传错误", e);
        }
        return resultUploadDoc;
    }
    
    /**
     * 6. 文档签署接口（自动签署模式）
     * @throws SQLException 
     * @throws ResourceNotFoundException 
     */
    @Override
    public void extsignAuto(int userId, int bidId, int orderId, T6273_F10 userType)
        throws ResourceNotFoundException, SQLException
    {
        try (Connection connection = getConnection())
        {
            extsignAuto(connection, userId, bidId, orderId, userType);
        }
    }
    
    /**
     * 6. 文档签署接口（自动签署模式）
     * @throws SQLException 
     * @throws ResourceNotFoundException 
     */
    private void extsignAuto(Connection connection, int userId, int bidId, int orderId, T6273_F10 userType)
    {
        try
        {
            //获取需要签名的文件列表
            T6273 t6273 = selectT6273(connection, userId, bidId, orderId, userType);
            
            T6230 t6230 = selectT6230(connection, bidId);
            if (null != t6273)
            {
                String dateStr = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                String transaction_id = "sign_" + orderId + "_" + dateStr; //必选 交易号,长度<=32 
                String batch_id = ""; //可选 授权交易号 此参数仅当客户角色为借款人时才需要传
                String contract_id = t6273.F04; //必选 合同编号,长度<=32
                String client_type = ""; //必选 客户类型 1 个人 2 企业
                logger.info("法大大【自动文档签署】请求订单号：" + transaction_id + "，合同编号：" + contract_id);
                
                T6110 t6110 = selectT6110(connection, userId);
                if (null != t6110)
                {
                    //判断客户类型
                    if (T6110_F06.ZRR == t6110.F06)
                    {
                        client_type = "1";
                    }
                    else
                    {
                        client_type = "2";
                    }
                }
                String client_role = "3"; //必选 客户角色 1 接入平台 2 担保公司 3 投资人 4 借款人
                String customer_id = t6110.F20; //必选 客户编号 客户编号
                String doc_title = t6230.F03; //必选 待签署文档标题 如“ xx 投资合同”
                String sign_keyword = t6273.F04; //必选 自动签约关键字 法大大按此关键字进行签章位置的定位
                String notify_url = ""; //可选 服务器异步通知，如果指定，当签章完成后，法大大将通过此 URL 通知接入方平台
                // 签个人章
                String retStr =
                    FddClient.invokeExtSignAuto(transaction_id,
                        customer_id,
                        batch_id,
                        client_type,
                        client_role,
                        contract_id,
                        doc_title,
                        sign_keyword,
                        notify_url);
                logger.info("---法大大【自动文档签署】返回结果：" + retStr);
                
                JSONObject outobj = JSONObject.fromObject(retStr);
                String result = outobj.getString("result");
                String code = outobj.getString("code");
                logger.info("---法大大【自动文档签署】" + outobj.getString("msg"));
                if ("success".equals(result) && FDDReturnStatus.SUCCESS.code().equals(code))
                {
                    // 签平台章
                    retStr = this.extsignAutoPT(connection, t6230, t6273, bidId, orderId);
                    if ("success".equals(result) && FDDReturnStatus.SUCCESS.code().equals(code))
                    {
                        //更新签章状态、交易时间、交易请求号
                        this.updateT6273ForStatus(connection, T6273_F15.DGD, userId, bidId, orderId, userType);
                        this.updateT6273ForSign(connection, transaction_id, userId, bidId, orderId, userType);
                        
                        //插入签章后在线查看已签文档的地址和文档下载地址
                        String download_url = outobj.getString("download_url");
                        String viewpdf_url = outobj.getString("viewpdf_url");
                        this.updateT6273ForUrl(connection, userId, bidId, orderId, userType, viewpdf_url, download_url);
                        
                        //调用合同归档
                        String resultStr = this.contractFiling(contract_id);
                        if (!StringUtils.isEmpty(resultStr))
                        {
                            if ("success".equals(resultStr))
                            {
                                //归档成功:则改状态为“已归档-YGD”，整个电子签章流程结束
                                this.updateT6273ForStatus(connection, T6273_F15.YGD, userId, bidId, orderId, userType);
                            }
                        }
                    }
                    else
                    {
                        throw new Exception("法大大自动签署文档失败-平台签章！原因:" + outobj.getString("msg"));
                    }
                }
                else
                {
                    throw new Exception("法大大自动文档签署失败！原因:" + outobj.getString("msg"));
                }
            }
            else
            {
                logger.error("该投资用户法大大合同列表无记录！");
            }
            
        }
        catch (Throwable e)
        {
            logger.error("自动文档签署错误", e);
        }
        
    }
    
    /**
     * 签平台章
     * <功能详细描述>
     * @param connection
     * @param t6230
     * @param t6273
     * @param bidId
     * @param orderId
     * @return
     */
    private String extsignAutoPT(Connection connection, T6230 t6230, T6273 t6273, int bidId, int orderId)
    {
        try
        {
            //获取需要签名的文件列表
            // T6273 t6273 = selectT6273(connection, userId, bidId, orderId, userType);
            
            // T6230 t6230 = selectT6230(connection, bidId);
            if (null != t6273)
            {
                String dateStr = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                
                String batch_id = ""; //可选 授权交易号 此参数仅当客户角色为借款人时才需要传
                String contract_id = t6273.F04; //必选 合同编号,长度<=32
                String client_type = ""; //必选 客户类型 1 个人 2 企业
                
                int ptId = this.getPTID(connection);
                String transaction_id = "sign_" + orderId + "_" + dateStr + "_" + ptId; //必选 交易号,长度<=32 
                logger.info("法大大【自动文档签署-平台签章】请求订单号：" + transaction_id + "，合同编号：" + contract_id);
                T6110 t6110 = selectT6110(connection, ptId);
                if (null != t6110)
                {
                    //判断客户类型
                    if (T6110_F06.ZRR == t6110.F06)
                    {
                        client_type = "1";
                    }
                    else
                    {
                        client_type = "2";
                    }
                }
                String client_role = "1"; //必选 客户角色 1 接入平台 2 担保公司 3 投资人 4 借款人
                String customer_id = t6110.F20; //必选 客户编号 客户编号
                String doc_title = t6230.F03; //必选 待签署文档标题 如“ xx 投资合同”
                String sign_keyword = configureProvider.getProperty(SystemVariable.COMPANY_NAME); //必选 自动签约关键字 法大大按此关键字进行签章位置的定位 ,平台所属公司名称
                String notify_url = ""; //可选 服务器异步通知，如果指定，当签章完成后，法大大将通过此 URL 通知接入方平台
                String retStr =
                    FddClient.invokeExtSignAuto(transaction_id,
                        customer_id,
                        batch_id,
                        client_type,
                        client_role,
                        contract_id,
                        doc_title,
                        sign_keyword,
                        notify_url);
                logger.info("---法大大【自动文档签署-平台签章】返回结果：" + retStr);
                return retStr;
            }
            else
            {
                logger.error("该投资用户法大大合同列表无记录！");
                return "";
            }
        }
        catch (Throwable e)
        {
            logger.error("自动文档签署错误-平台签章", e);
            throw new LogicalException("自动文档签署错误-平台签章");
        }
        
    }
    
    /**
     * 5. 文档签署接口（手动签署模式）
     */
    @Override
    public void extSign(int userId, int bidId, T6273_F10 userType, HttpServletResponse response)
    {
        try
        {
            try (Connection connection = getConnection())
            {
                //获取需要签名的文件列表
                T6273 t6273 = selectT6273(connection, userId, bidId, 0, userType);
                T6230 t6230 = selectT6230(connection, bidId);
                if (null != t6273)
                {
                    T6110 t6110 = selectT6110(connection, userId);
                    String client_type = ""; //必选 客户类型 1 个人 2 企业 默认为 1
                    if (null != t6110)
                    {
                        //判断客户类型
                        if (T6110_F06.ZRR == t6110.F06)
                        {
                            client_type = "1";
                        }
                        else
                        {
                            client_type = "2";
                        }
                    }
                    String dateStr = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
                    //规则：前缀_标Id_用户Id_时间戳
                    String transaction_id = "sign_" + bidId + "_" + userId + "_" + dateStr; //必选 交易号,长度<=32
                    String contract_id = t6273.F04; //必选 合同编号,长度<=32
                    String customer_id = t6110.F20; //必选 客户编号 客户编号
                    String doc_title = t6230.F03; //必选 待签署文档标题 如“xx投资合同”
                    String sign_keyword = t6273.F04; //必选 签约关键字 法大大按此关键字进行签章位置的定位
                    String return_url = configureProvider.format(URLVariable.USER_CREDIT);
                    String notify_url = configureProvider.format(FddVariable.FDD_EXTSIGN_NOTIFY);
                    logger.info("法大大【手动文档签署】请求订单号：" + transaction_id + "，合同编号：" + contract_id);
                    String reqUrl =
                        FddClient.invokeExtSign("post",
                            transaction_id,
                            contract_id,
                            return_url,
                            client_type,
                            customer_id,
                            doc_title,
                            notify_url,
                            sign_keyword);
                    //response.sendRedirect(reqUrl);
                    doPrintWriter(response, reqUrl, true);
                }
                else
                {
                    logger.error("该投资用户法大大合同列表无记录！");
                }
            }
        }
        catch (Throwable e)
        {
            logger.error("手动文档签署错误", e);
        }
    }
    
    /**
     * 11. 合同归档接口
     */
    @Override
    public String contractFiling(String contract_id)
    {
        String result = "";
        try
        {
            logger.info("---法大大【合同归档】开始.....合同编号：" + contract_id);
            String returnStr = FddClient.invokeContractFilling(contract_id);
            JSONObject outobj = JSONObject.fromObject(returnStr);
            result = outobj.getString("result");
            String code = outobj.getString("code");
            logger.info("---法大大【合同归档】" + outobj.getString("msg"));
            if ("success".equals(result) && FDDReturnStatus.SUCCESS.code().equals(code))
            {
                //归档成功:则改状态为“已归档-YGD”，整个电子签章流程结束
                logger.info("---法大大【合同归档】成功！法大大签章结束。");
            }
            else
            {
                throw new Exception("法大大【合同归档】失败！原因:" + outobj.getString("msg"));
            }
        }
        catch (Exception e)
        {
            logger.error("法大大合同归档错误", e);
        }
        return result;
    }
    
    @Override
    public void viewContract(String contract_id)
    {
        try
        {
            String returnStr = FddClient.invokeViewContract(contract_id);
            JSONObject outobj = JSONObject.fromObject(returnStr);
            String result = outobj.getString("result");
            String code = outobj.getString("code");
            logger.info("---法大大【查看合同】" + outobj.getString("msg"));
            if ("success".equals(result) && FDDReturnStatus.SUCCESS.code().equals(code))
            {
                logger.info("---法大大【查看合同】成功！合同编号：" + contract_id);
            }
            else
            {
                throw new Exception("法大大【查看合同】失败！原因:" + outobj.getString("msg"));
            }
        }
        catch (Exception e)
        {
            logger.error("法大大查看合同错误", e);
        }
    }
    
    @Override
    public String getUserCustomerId(int accountId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement("SELECT F20 FROM S61.T6110 WHERE F01=?"))
            {
                ps.setInt(1, accountId);
                try (ResultSet resultSet = ps.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return resultSet.getString(1);
                    }
                }
            }
        }
        return null;
    }
    
    @Override
    public void updateT6273ForUrl(int userId, int bidId, int orderId, T6273_F10 userType, String viewUrl,
        String uploadUrl)
        throws SQLException
    {
        try (Connection connection = getConnection())
        {
            updateT6273ForUrl(connection, userId, bidId, orderId, userType, viewUrl, uploadUrl);
        }
    }
    
    @Override
    public void updateT6273ForStatus(T6273_F15 status, int userId, int bidId, int orderId, T6273_F10 userType)
        throws SQLException
    {
        try (Connection connection = getConnection())
        {
            updateT6273ForStatus(connection, status, userId, bidId, orderId, userType);
        }
    }
    
    @Override
    public void updateT6273ForSign(String transaction_id, int userId, int bidId, int orderId, T6273_F10 userType)
        throws SQLException
    {
        try (Connection connection = getConnection())
        {
            updateT6273ForSign(connection, transaction_id, userId, bidId, orderId, userType);
        }
    }
    
    @Override
    public T6273 selectT6273(int userId, int bidId, int orderId, T6273_F10 userType)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            return this.selectT6273(connection, userId, bidId, orderId, userType);
        }
    }
    
    @Override
    public T6230 selectT6230(int F01)
        throws SQLException
    {
        try (Connection connection = getConnection())
        {
            return selectT6230(connection, F01);
        }
    }
    
    @Override
    public List<T6273> selectUnExtSign(Timestamp timestamp)
        throws SQLException
    {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT * FROM S62.T6273 ");
        sql.append(" WHERE F07=? AND F10 IN(?,?,?) ");
        //sql.append(" AND F14=?  AND F13 <= ? ");
        sql.append(" ORDER BY F01");
        
        List<T6273> results = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt = connection.prepareStatement(sql.toString()))
            {
                pstmt.setString(1, T6273_F07.DQ.name());
                pstmt.setString(2, T6273_F10.TZR.name());
                pstmt.setString(3, T6273_F10.SRR.name());
                pstmt.setString(4, T6273_F10.ZCR.name());
                //pstmt.setTimestamp(3, timestamp);
                
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        T6273 record = new T6273();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getInt(3);
                        record.F04 = resultSet.getString(4);
                        record.F05 = resultSet.getString(5);
                        record.F06 = resultSet.getTimestamp(6);
                        record.F07 = T6273_F07.parse(resultSet.getString(7));
                        record.F08 = T6273_F08.parse(resultSet.getString(8));
                        record.F09 = resultSet.getString(9);
                        record.F10 = T6273_F10.parse(resultSet.getString(10));
                        record.F11 = resultSet.getInt(11);
                        record.F12 = resultSet.getInt(12);
                        record.F13 = resultSet.getTimestamp(13);
                        record.F14 = resultSet.getInt(14);
                        record.F15 = T6273_F15.parse(resultSet.getString(15));
                        record.F16 = resultSet.getString(16);
                        record.F17 = resultSet.getString(17);
                        record.F18 = resultSet.getInt(18);
                        record.F19 = resultSet.getString(19);
                        if (results == null)
                        {
                            results = new ArrayList<T6273>();
                        }
                        results.add(record);
                    }
                }
            }
            
        }
        return results;
    }
    
    @Override
    public T6273 selectT6273(int F01)
        throws SQLException
    {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11, F12, F13, F14, F15, F16, F17, F18, F19,F20 FROM S62.T6273 WHERE F01=? ");
        T6273 record = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt = connection.prepareStatement(sql.toString()))
            {
                pstmt.setInt(1, F01);
                
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        record = new T6273();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getInt(3);
                        record.F04 = resultSet.getString(4);
                        record.F05 = resultSet.getString(5);
                        record.F06 = resultSet.getTimestamp(6);
                        record.F07 = T6273_F07.parse(resultSet.getString(7));
                        record.F08 = T6273_F08.parse(resultSet.getString(8));
                        record.F09 = resultSet.getString(9);
                        record.F10 = T6273_F10.parse(resultSet.getString(10));
                        record.F11 = resultSet.getInt(11);
                        record.F12 = resultSet.getInt(12);
                        record.F13 = resultSet.getTimestamp(13);
                        record.F14 = resultSet.getInt(14);
                        record.F15 = T6273_F15.parse(resultSet.getString(15));
                        record.F16 = resultSet.getString(16);
                        record.F17 = resultSet.getString(17);
                        record.F18 = resultSet.getInt(18);
                        record.F19 = resultSet.getString(19);
                        record.F20 = resultSet.getString(20);
                    }
                }
            }
        }
        return record;
    }
    
    @Override
    public String querySignStatus(int sid)
        throws Throwable
    {
        String result = "";
        try
        {
            logger.info("---法大大【查看合同签署状态】开始.....  待签合同信息ID , T6273.F01=" + sid);
            try (Connection connection = getConnection())
            {
                T6273 t6273 = selectT6273(sid);
                T6110 t6110 = selectT6110(connection, t6273.F02);
                
                // 后台查询不用验证用户
                /* if(t6273.F02 != userId){
                     throw new Exception("法大大【查看合同签署状态】失败！原因: 不能查非本人的合同" );
                 }*/
                String returnStr = FddClient.invokeQueryStatus(t6273.F04, t6110.F20);
                JSONObject outobj = JSONObject.fromObject(returnStr);
                result = outobj.getString("result");
                String code = outobj.getString("code");
                logger.info("---法大大【查看合同签署状态】" + outobj.getString("msg"));
                if ("success".equals(result) && FDDReturnStatus.SUCCESS.code().equals(code))
                {
                    //签署状态码 0: 待签 ， 1：已签
                    String sign_status = outobj.getString("sign_status");
                    if (sign_status.equals("1"))
                    {
                        // 合同下载地址
                        String download_url = outobj.getString("download_url");
                        //合同查看地址
                        String viewpdf_url = outobj.getString("viewpdf_url");
                        //交易号
                        String transaction_id = outobj.getString("transaction_id");
                        if (!StringHelper.isEmpty(transaction_id) && !StringHelper.isEmpty(t6273.F19)
                            && !transaction_id.equals(t6273.F19))
                        {
                            throw new Exception("法大大【查看合同签署状态】失败！原因:法大大交易号和平台的交易号不一致 , FDD transaction_id ="
                                + transaction_id + " , 平台交易号 = " + t6273.F19);
                        }
                        T6273_F15 status_sign = T6273_F15.DGD;
                        if (t6273.F15 == T6273_F15.YGD)
                        {
                            status_sign = T6273_F15.YGD;
                        }
                        if (!StringHelper.isEmpty(t6273.F19))
                        {
                            transaction_id = "";
                        }
                        this.updateT6273Info(connection,
                            T6273_F07.YQ,
                            status_sign,
                            t6273.F02,
                            sid,
                            download_url,
                            viewpdf_url,
                            transaction_id);
                    }
                    logger.info("---法大大【查看合同签署状态】成功！法大大签章结束。");
                }
                else
                {
                    throw new Exception("法大大【查看合同签署状态】失败！原因:" + outobj.getString("msg"));
                }
            }
        }
        catch (Exception e)
        {
            logger.error("查看合同签署状态", e);
        }
        return result;
    }
    
    @Override
    public void insertT6273(int bidId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                T6230 t6230 = selectT6230(connection, bidId);
                // T6250[] t6250s = selectAllT6250(connection, bidId);
                
                //生成电子签章
                //借款人和投资人
                StringBuilder sql = new StringBuilder();
                ArrayList<Object> parameters = new ArrayList<Object>();
                T6250[] t6250s = selectAllT6250(connection, t6230.F01);
                Timestamp time = getCurrentTimestamp(connection);
                int index = 1;
                sql.append("INSERT INTO S62.T6273 ( F02, F03 ,F04, F07 , F08 , F10 , F13 , F14 , F15 ) VALUES (?,?,?,?,?,?,?,?,?) ");
                //借款人
                parameters.add(t6230.F02);
                parameters.add(t6230.F01);
                parameters.add(getHtCode(t6230.F25, index));
                parameters.add(T6273_F07.DQ);
                parameters.add(T6273_F08.JKHT);
                parameters.add(T6273_F10.JKR);
                parameters.add(time);
                parameters.add("");
                parameters.add(T6273_F15.DSQ.name());
                // 投资人
                if (t6250s != null && t6250s.length > 0)
                {
                    for (T6250 t6250only : t6250s)
                    {
                        sql.append(",(?,?,?,?,?,?,?,?,?)");
                        index++;
                        parameters.add(t6250only.F03);
                        parameters.add(t6230.F01);
                        parameters.add(getHtCode(t6230.F25, index));
                        parameters.add(T6273_F07.DQ);
                        parameters.add(T6273_F08.JKHT);
                        parameters.add(T6273_F10.TZR);
                        parameters.add(time);
                        parameters.add(t6250only.F01);
                        parameters.add(T6273_F15.DSQ.name());
                        
                        //  t6273.F01 = selectT6273Id(t6230.F01, userId, t6250only.F01, connection);
                        //   doHtmlSavePdf(configureProvider,t6273, connection);
                    }
                }
                
                insert(connection, sql.toString(), parameters);
                serviceResource.commit(connection);
                /*  
                  StringBuilder sql = new StringBuilder();
                  ArrayList<Object> parameters = new ArrayList<Object>();
                  sql.append("INSERT INTO S62.T6271 (F02,F03,F07,F08,F10) VALUES (?,?,?,?,?)");
                  parameters.add(t6230.F02);
                  parameters.add(t6230.F01);
                  parameters.add(T6271_F07.WBQ);
                  parameters.add(T6271_F08.JKHT);
                  parameters.add(T6271_F10.JKR);
                  for (T6250 t6250 : t6250s)
                  {
                      sql.append(",(?,?,?,?,?)");
                      parameters.add(t6250.F03);
                      parameters.add(t6250.F02);
                      parameters.add(T6271_F07.WBQ);
                      parameters.add(T6271_F08.JKHT);
                      parameters.add(T6271_F10.TZR);
                  }
                  insert(connection, sql.toString(), parameters);
                  serviceResource.commit(connection);*/
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
        
    }
    
    @Override
    public List<T6273> selectT6273ByDidId(int bidId)
        throws Throwable
    {
        T6273 record = null;
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11, F12, F13, F14 , F15, F16, F17, F18, F19 FROM S62.T6273 ");
        sql.append("WHERE T6273.F03 = ?  AND F08=? ");
        List<T6273> results = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt = connection.prepareStatement(sql.toString()))
            {
                pstmt.setInt(1, bidId);
                pstmt.setString(2, T6273_F08.JKHT.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        record = new T6273();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getInt(3);
                        record.F04 = resultSet.getString(4);
                        record.F05 = resultSet.getString(5);
                        record.F06 = resultSet.getTimestamp(6);
                        record.F07 = T6273_F07.parse(resultSet.getString(7));
                        record.F08 = T6273_F08.parse(resultSet.getString(8));
                        record.F09 = resultSet.getString(9);
                        record.F10 = T6273_F10.parse(resultSet.getString(10));
                        record.F11 = resultSet.getInt(11);
                        record.F12 = resultSet.getInt(12);
                        record.F13 = resultSet.getTimestamp(13);
                        record.F14 = resultSet.getInt(14);
                        record.F15 = T6273_F15.parse(resultSet.getString(15));
                        record.F16 = resultSet.getString(16);
                        record.F17 = resultSet.getString(17);
                        record.F18 = resultSet.getInt(18);
                        record.F19 = resultSet.getString(19);
                        if (results == null)
                        {
                            results = new ArrayList<T6273>();
                        }
                        results.add(record);
                    }
                }
            }
        }
        return results;
    }
    
}
