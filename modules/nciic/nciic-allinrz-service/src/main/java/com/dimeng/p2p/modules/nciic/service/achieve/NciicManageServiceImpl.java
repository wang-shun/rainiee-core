package com.dimeng.p2p.modules.nciic.service.achieve;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.resource.ResourceNotFoundException;
import com.dimeng.framework.service.AbstractService;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.P2PConst;
import com.dimeng.p2p.modules.nciic.service.INciicManageService;
import com.dimeng.p2p.modules.nciic.service.entity.IDVER;
import com.dimeng.p2p.modules.nciic.service.entity.INFO;
import com.dimeng.p2p.modules.nciic.service.entity.IdentityAuthEntity;
import com.dimeng.p2p.modules.nciic.service.entity.IdentityAuthQuery;
import com.dimeng.p2p.modules.nciic.service.entity.IdentityAuthQueryResult;
import com.dimeng.p2p.modules.nciic.service.entity.IdentityAuthResult;
import com.dimeng.p2p.modules.nciic.service.entity.VALIDRET;
import com.dimeng.p2p.modules.nciic.service.entity.VERQRY;
import com.dimeng.p2p.modules.nciic.service.util.XMLTools;
import com.dimeng.p2p.modules.nciic.varibles.AllinrzVaribles;

public class NciicManageServiceImpl extends AbstractService implements INciicManageService
{
    
    public NciicManageServiceImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    private static class Result
    {
        boolean idPassed = false;
        
        InputStream image = null;
        
        String errerMsg = null;
        
        int errorCode = 0;
        
        public boolean isPassed()
        {
            return idPassed;
        }
    }
    
    protected Connection getConnection()
        throws ResourceNotFoundException, SQLException
    {
        return serviceResource.getDataConnectionProvider(SQLConnectionProvider.class, P2PConst.DB_MASTER_PROVIDER)
            .getConnection();
    }
    
    private Result doCheck(String id, String name)
        throws Throwable
    {
        Result result = new Result();
        INFO info = new INFO();
        ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
        //交易代码
        info.setTRX_CODE("220001");
        //版本
        info.setVERSION("03");
        //数据格式
        info.setDATA_TYPE("2");
        //处理级别
        info.setLEVEL("5");
        info.setSIGNED_MSG("");
        //商户号
        String ALLINRZ_AIPG_MERCHANTID = configureProvider.format(AllinrzVaribles.ALLINRZ_AIPG_MERCHANTID);
        info.setMERCHANT_ID(ALLINRZ_AIPG_MERCHANTID);
        //用户名
        info.setUSER_NAME(configureProvider.format(AllinrzVaribles.ALLINRZ_AIPG_USERNAME));
        //密码
        info.setUSER_PASS(configureProvider.format(AllinrzVaribles.ALLINRZ_AIPG_USERPASSWORD));
        //公钥路径
        String pubKeyPath = configureProvider.format(AllinrzVaribles.ALLINRZ_TLTCERPATH_PATH);
        //私钥路径
        String prvKeyPath = configureProvider.format(AllinrzVaribles.ALLINRZ_PFXFILE_PATH);
        //私钥密码
        String prvKeyPassWord = configureProvider.format(AllinrzVaribles.ALLINRZ_PFX_PASSWORD);
        //接口地址
        String url = configureProvider.format(AllinrzVaribles.ALLINRZ_AIPG_URL);
        //交易批次号
        String req_sn =
            ALLINRZ_AIPG_MERCHANTID + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())
                + XMLTools.getSerialNumberSuffix();
        info.setREQ_SN(req_sn);
        IDVER idver = new IDVER();
        //姓名
        idver.setNAME(name);
        //身份证号
        idver.setIDNO(id);
        IdentityAuthEntity entity = new IdentityAuthEntity();
        entity.setiDVER(idver);
        entity.setiNFO(info);
        //构造xml字符串
        String xml =
            "<?xml version=\"1.0\" encoding=\"GBK\"?><AIPG>" + XMLTools.objToXml(entity, "INFO", true) + "</AIPG>";
        //向通联发送报文
        String retXml = XMLTools.sendToTlt(xml, false, url, prvKeyPath, pubKeyPath, prvKeyPassWord);
        //解析返回结果
        IdentityAuthResult entityResult = (IdentityAuthResult)XMLTools.xmlTOEntity(retXml, new IdentityAuthResult());
        if (entityResult != null && entityResult.getValidret() != null && entityResult.getInfo() != null)
        {
            VALIDRET validret = entityResult.getValidret();
            INFO iNFO = entityResult.getInfo();
            //判断是否提交成功,如果不成功，再查询一次
            if (!"0000".equals(iNFO.getRET_CODE()))
            {
                return setResultValue(result, "认证系统繁忙，请稍后重新认证", iNFO.getRET_CODE(), false);
                //判断身份验证是否通过
            }
            else if ("0000".equals(validret.getRET_CODE()))
            {
                return setResultValue(result, "", "0000", true);
            }
            else if (!"0000".equals(validret.getRET_CODE()))
            {
                logger.info("验证失败，返回码：" + validret.getRET_CODE());
                return setResultValue(result, validret.getERR_MSG(), iNFO.getRET_CODE(), false);
            }
        }
        else
        {
            VERQRY verqry = new VERQRY();
            verqry.setQSN(req_sn);
            verqry.setQTARGET("1");
            IdentityAuthQuery query = new IdentityAuthQuery();
            query.setiNFO(info);
            query.setvERQRY(verqry);
            //构造xml字符串
            String xmlSec =
                "<?xml version=\"1.0\" encoding=\"GBK\"?><AIPG>" + XMLTools.objToXml(query, "INFO", true) + "</AIPG>";
            //向通联发送报文
            String retXmlSec = XMLTools.sendToTlt(xmlSec, false, url, prvKeyPath, pubKeyPath, prvKeyPassWord);
            //解析返回结果
            IdentityAuthQueryResult queryResult =
                (IdentityAuthQueryResult)XMLTools.xmlTOEntity(retXml, new IdentityAuthQueryResult());
            if (queryResult != null && queryResult.getValidret() != null && queryResult.getInfo() != null)
            {
                VALIDRET validret = queryResult.getValidret();
                INFO iNFO = queryResult.getInfo();
                //判断是否成功
                if ("0000".equals(validret.getRET_CODE()))
                {
                    return setResultValue(result, "", "0000", true);
                }
                else if (!"0000".equals(validret.getRET_CODE()))
                {
                    logger.info("验证失败，返回码：" + validret.getRET_CODE());
                    return setResultValue(result, validret.getERR_MSG(), iNFO.getRET_CODE(), false);
                }
            }
            
        }
        return setResultValue(result, "认证系统繁忙，请稍后重新认证", "0", false);
    }
    
    /**
     * 设置返回结果
     * @param result
     * @param errMsg
     * @param errorCode
     * @param pass
     * @return
     */
    private Result setResultValue(Result result, String errMsg, String errorCode, boolean pass)
    {
        if (result == null)
        {
            result = new Result();
        }
        result.errerMsg = errMsg;
        result.errorCode = Integer.parseInt(errorCode);
        result.idPassed = pass;
        return result;
    }
    
    @Override
    public boolean check(String id, String name, String terminal, int accountId)
        throws Throwable
    {
        return check(id, name, false, terminal, accountId);
    }
    
    @Override
    public boolean check(String id, String name, boolean duplicatedName, String terminal, int accountId)
        throws Throwable
    {
        logger.info("check() start ");
        try (Connection connection = getConnection())
        {
            if (duplicatedName)
            {
                try (PreparedStatement pstmt =
                    connection.prepareStatement("SELECT F02 FROM S71.T7122 WHERE F01 = ? AND F03 = 'TG' LIMIT 1"))
                {
                    pstmt.setString(1, id);
                    try (ResultSet resultSet = pstmt.executeQuery())
                    {
                        if (resultSet.next())
                        {
                            return name.equalsIgnoreCase(resultSet.getString(1));
                        }
                    }
                }
            }
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F03 FROM S71.T7122 WHERE F01 = ? AND F02 = ?"))
            {
                pstmt.setString(1, id);
                pstmt.setString(2, name);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return "TG".equalsIgnoreCase(resultSet.getString(1));
                    }
                }
            }
            Result result = null;
            try
            {
                result = doCheck(id, name);
            }
            catch (Throwable t)
            {
                serviceResource.log(t);
            }
            serviceResource.openTransactions(connection);
            if (result != null && result.idPassed)
            {
                boolean passed = result.isPassed();
                try (PreparedStatement pstmt =
                    connection.prepareStatement("INSERT INTO S71.T7122 SET F01 = ?, F02 = ?, F03 = ?, F04 = ?, F06 = ?, F07 = ?"))
                {
                    pstmt.setString(1, id);
                    pstmt.setString(2, name);
                    pstmt.setString(3, passed ? "TG" : "SB");
                    if (result.image == null)
                    {
                        pstmt.setNull(4, Types.BLOB);
                    }
                    else
                    {
                        pstmt.setBlob(4, result.image);
                    }
                    pstmt.setInt(5, accountId);
                    pstmt.setString(6, terminal);
                    pstmt.execute();
                }
                
                try (PreparedStatement pstmt =
                    connection.prepareStatement("INSERT INTO S71.T7124 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F07 = ?"))
                {
                    pstmt.setString(1, id);
                    pstmt.setString(2, name);
                    pstmt.setString(3, passed ? "TG" : "SB");
                    pstmt.setInt(4, result.errorCode);
                    pstmt.setString(5, terminal);
                    pstmt.execute();
                }
                
                int userId = serviceResource.getSession().getAccountId();
                if (passed)
                {
                    updateT6198F06(connection, userId, terminal);
                }
                else
                {
                    updateT6198F03(connection, userId, terminal);
                }
                serviceResource.commit(connection);// 提交事务
                logger.info("check() end ");
                return passed;
            }
            return false;
        }
    }
    
    /**
     * 更新错误认证次数
     * @return
     * @throws Throwable
     */
    private void updateT6198F03(Connection connection, int userId, String terminal)
        throws Throwable
    {
        execute(connection, "UPDATE  S61.T6198 SET F03=F03+1,F04=? WHERE F02 = ?", terminal, userId);
    }
    
    /**
     * 更新认证通过时间
     * @return
     * @throws Throwable
     */
    private void updateT6198F06(Connection connection, int userId, String terminal)
        throws Throwable
    {
        execute(connection, "UPDATE  S61.T6198 SET F04=?,F06=now() WHERE F02 = ?", terminal, userId);
    }
}
