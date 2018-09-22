package com.dimeng.p2p.escrow.fuyou.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.escrow.fuyou.entity.ComUpdate;
import com.dimeng.p2p.escrow.fuyou.entity.UserUpdateEntity;
import com.dimeng.p2p.escrow.fuyou.service.UpdateInfoManage;
import com.dimeng.util.StringHelper;

public class UpdateInfoManageImpl extends AbstractEscrowService implements UpdateInfoManage
{
    
    public UpdateInfoManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    /**
     * 个人用户信息更新请求解析
     * @param request
     * @return
     * @throws Throwable
     */
    @Override
    public UserUpdateEntity userUpdateReqDecode(HttpServletRequest request)
        throws Throwable
    {
        // 接收充值返回参数
        UserUpdateEntity entity = new UserUpdateEntity();
        entity.setMchnt_cd(request.getParameter("mchnt_cd"));
        entity.setMchnt_txn_ssn(request.getParameter("mchnt_txn_ssn"));
        entity.setUser_id_from(request.getParameter("user_id_from"));
        entity.setMobile_no(request.getParameter("mobile_no"));
        entity.setCust_nm(request.getParameter("cust_nm"));
        entity.setCertif_id(request.getParameter("certif_id"));
        entity.setEmail(request.getParameter("email"));
        entity.setCity_id(request.getParameter("city_id"));
        entity.setParent_bank_id(request.getParameter("parent_bank_id"));
        entity.setBank_nm(request.getParameter("bank_nm"));
        entity.setCapAcntNo(request.getParameter("capAcntNo"));
        entity.setResp_code(request.getParameter("resp_code"));
        entity.setSignature(request.getParameter("signature"));
        logger.info("商户代码===" + entity.getMchnt_cd());
        logger.info("流水号===" + entity.getMchnt_txn_ssn());
        logger.info("用户在商户系统的标志" + entity.getUser_id_from());
        logger.info("手机号码" + entity.getMobile_no());
        logger.info("法人姓名" + entity.getCust_nm());
        logger.info("身份证号码" + entity.getCertif_id());
        logger.info("邮箱地址" + entity.getEmail());
        logger.info("开户行地区代码" + entity.getCity_id());
        logger.info("开户行行别" + entity.getParent_bank_id());
        logger.info("开户行支行名称" + entity.getBank_nm());
        logger.info("账号" + entity.getCapAcntNo());
        logger.info("返回码" + entity.getResp_code());
        logger.info("签名数据===" + entity.getSignature());
        if (entity.getSignature() == null)
        {
            logger.error("用户信息修改签名为空");
            return entity;
        }
        List<String> params = new ArrayList<>();
        params.add(entity.getBank_nm());
        params.add(entity.getCapAcntNo());
        params.add(entity.getCertif_id());
        params.add(entity.getCity_id());
        params.add(entity.getCust_nm());
        params.add(entity.getEmail());
        params.add(entity.getMchnt_cd());
        params.add(entity.getMchnt_txn_ssn());
        params.add(entity.getMobile_no());
        params.add(entity.getParent_bank_id());
        params.add(entity.getResp_code());
        params.add(entity.getUser_id_from());
        String str = forEncryptionStr(params);
        if (!verifyByRSA(str, entity.getSignature()))
        {
            logger.info("验签失败");
            return  null;
        }
        return entity;
    }
    
    /**
     * 法人信息更新请求解析
     * @param request
     * @return
     * @throws Throwable
     */
    public ComUpdate legalPerUpdateReqDecode(HttpServletRequest request)
        throws Throwable
    {
        
        // 接收充值返回参数
        ComUpdate entity = new ComUpdate();
        entity.setMchnt_cd(request.getParameter("mchnt_cd"));
        entity.setMchnt_txn_ssn(request.getParameter("mchnt_txn_ssn"));
        entity.setUser_id_from(request.getParameter("user_id_from"));
        entity.setMobile_no(request.getParameter("mobile_no"));
        entity.setCust_nm(request.getParameter("cust_nm"));
        entity.setArtif_nm(request.getParameter("artif_nm"));
        entity.setCertif_id(request.getParameter("certif_id"));
        entity.setEmail(request.getParameter("email"));
        entity.setCity_id(request.getParameter("city_id"));
        entity.setParent_bank_id(request.getParameter("parent_bank_id"));
        entity.setBank_nm(request.getParameter("bank_nm"));
        entity.setCapAcntNo(request.getParameter("capAcntNo"));
        entity.setResp_code(request.getParameter("resp_code"));
        entity.setSignature(request.getParameter("signature"));
        logger.info("商户代码===" + entity.getMchnt_cd());
        logger.info("流水号===" + entity.getMchnt_txn_ssn());
        logger.info("用户在商户系统的标志" + entity.getUser_id_from());
        logger.info("手机号码" + entity.getMobile_no());
        logger.info("企业名称" + entity.getCust_nm());
        logger.info("法人姓名" + entity.getArtif_nm());
        logger.info("身份证号码" + entity.getCertif_id());
        logger.info("邮箱地址" + entity.getEmail());
        logger.info("开户行地区代码" + entity.getCity_id());
        logger.info("开户行行别" + entity.getParent_bank_id());
        logger.info("开户行支行名称" + entity.getBank_nm());
        logger.info("账号" + entity.getCapAcntNo());
        logger.info("返回码" + entity.getResp_code());
        logger.info("签名数据===" + entity.getSignature());
        if (entity.getSignature() == null)
        {
            logger.error("用户信息修改签名为空");
            return entity;
        }
        List<String> params = new ArrayList<>();
        params.add(entity.getArtif_nm());
        params.add(entity.getBank_nm());
        params.add(entity.getCapAcntNo());
        params.add(entity.getCertif_id());
        params.add(entity.getCity_id());
        params.add(entity.getCust_nm());
        params.add(entity.getEmail());
        params.add(entity.getMchnt_cd());
        params.add(entity.getMchnt_txn_ssn());
        params.add(entity.getMobile_no());
        params.add(entity.getParent_bank_id());
        params.add(entity.getResp_code());
        params.add(entity.getUser_id_from());
        String str = forEncryptionStr(params);
        if (!verifyByRSA(str, entity.getSignature()))
        {
            logger.info("验签失败");
            return null;
        }
        return entity;
    }
    
    /**
     * 根据用户id更新用户的银行卡信息
     */
    public void updateInfo(String mobile_no, String capAcntNo, String city_id, String parent_bank_id, String bank_nm,
        String cust_name, int isPerson)
        throws Throwable
    {
        if (StringHelper.isEmpty(mobile_no))
        {
            return;
        }
        try (Connection connection = getConnection())
        {
            
            // 第三方的登陆账号是否存在
            try (PreparedStatement pstmt1 =
                connection.prepareStatement("SELECT T6119.F01 FROM S61.T6119 WHERE T6119.F03= ? "))
            {
                pstmt1.setString(1, mobile_no);
                try (ResultSet rs1 = pstmt1.executeQuery())
                {
                    if (!rs1.next())
                    {
                        logger.info("第三方登陆账号信息不正确");
                        return;
                    }
                    
                    // 获取用户ID
                    int userId = rs1.getInt(1);
                    // 根据用户Id和银行卡号，检查该记录是否已经存在
                    try (PreparedStatement pstmt2 =
                        connection.prepareStatement("SELECT T6114.F01 FROM S61.T6114 WHERE T6114.F02= ? AND T6114.F07= ?"))
                    {
                        pstmt2.setInt(1, userId);
                        pstmt2.setString(2, StringHelper.encode(capAcntNo));
                        try (ResultSet rs2 = pstmt2.executeQuery())
                        {
                            // 如果能查出来，直接更新开户行，支行，地区就行了
                            if (rs2.next())
                            {
                                int cardId = rs2.getInt(1);
                                // 更新银行卡信息
                                try (PreparedStatement pstmt3 =
                                    connection.prepareStatement("UPDATE S61.T6114 SET "
                                        + "F03 = (SELECT F01 FROM S50.T5020 WHERE F04 = ? LIMIT 1), "
                                        + "F04 = (SELECT F01 FROM S50.T5019 WHERE F03 = ? LIMIT 1), F05 = ?,  "
                                        + " F08 = 'QY', F10 = 'TG' WHERE T6114.F01=?"))
                                {
                                    pstmt3.setString(1, parent_bank_id);
                                    pstmt3.setInt(2, Integer.parseInt(city_id));
                                    pstmt3.setString(3, bank_nm);
                                    pstmt3.setInt(4, cardId);
                                    pstmt3.execute();
                                }
                            }
                            else
                            {
                                // 将旧银行卡号停用
                                try (PreparedStatement pstmt4 =
                                    connection.prepareStatement("UPDATE S61.T6114 SET F08 ='TY' WHERE T6114.F02= ?"))
                                {
                                    pstmt4.setInt(1, userId);
                                    pstmt4.execute();
                                    // 插入新银行卡
                                    try (PreparedStatement pstmt5 =
                                        connection.prepareStatement("INSERT INTO S61.T6114 SET F02 = ?, "
                                            + "F03 = (SELECT F01 FROM S50.T5020 WHERE F04 = ? LIMIT 1), "
                                            + "F04 = (SELECT F01 FROM S50.T5019 WHERE F03 = ? LIMIT 1), F05 = ?, F06 = ?, "
                                            + "F07 = ?, F08 = 'QY', F09 = CURRENT_TIMESTAMP(), F10 = 'TG',F11= ? , F12=?"))
                                    {
                                        pstmt5.setInt(1, userId);
                                        pstmt5.setString(2, parent_bank_id);
                                        pstmt5.setInt(3, Integer.parseInt(city_id));
                                        pstmt5.setString(4, bank_nm);
                                        pstmt5.setString(5, capAcntNo.replace(capAcntNo.substring(4,
                                            capAcntNo.length() - 3), getTargetStr(capAcntNo.length() - 7)));
                                        pstmt5.setString(6, StringHelper.encode(capAcntNo));
                                        pstmt5.setString(7, cust_name);
                                        pstmt5.setInt(8, isPerson);
                                        pstmt5.execute();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    /**
     * 查询此卡是否成功设置
     * @param userIdStr
     * @param capAcntNo
     * @return
     * @throws Throwable
     */
    public boolean setCardNumSuccessed(String mobile_no, String capAcntNo, String city_id, String parent_bank_id,
        String bank_nm)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            // 根据用户Id和银行卡号，检查该记录是否已经存在
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT T6114.F01 FROM S61.T6114 WHERE T6114.F02= (SELECT T6119.F01 FROM S61.T6119 WHERE T6119.F03= ?) AND T6114.F03 = (SELECT T5020.F01 FROM S50.T5020 WHERE T5020.F04 = ? LIMIT 1) AND "
                    + "T6114.F04 = (SELECT T5019.F01 FROM S50.T5019 WHERE T5019.F03 = ? LIMIT 1) AND T6114.F05 = ? AND T6114.F07= ? AND T6114.F08 = 'QY' "))
            {
                pstmt.setString(1, mobile_no);
                pstmt.setString(2, parent_bank_id);
                pstmt.setInt(3, Integer.parseInt(city_id));
                pstmt.setString(4, bank_nm);
                pstmt.setString(5, StringHelper.encode(capAcntNo));
                try (ResultSet rs = pstmt.executeQuery())
                {
                    if (rs.next())
                    {
                        return true;
                    }
                    else
                    {
                        return false;
                    }
                }
            }
        }
    }
    
    /**
     * 获取一个*序列
     * 
     * @param length
     * @return
     */
    public String getTargetStr(int length)
    {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++)
        {
            sb.append("*");
        }
        return sb.toString();
    }
    
    /**
     * 加密得到签名
     * @param map
     * @return
     * @throws Throwable
     */
    @Override
    public String asynQuery(HashMap<String, String> params)
        throws Throwable
    {
        return plainRSA(params);
    }
    
}
