package com.dimeng.p2p.signature.fdd.achieve;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.resource.ResourceNotFoundException;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.P2PConst;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.S61.enums.T6110_F08;
import com.dimeng.p2p.S61.enums.T6110_F10;
import com.dimeng.p2p.S62.entities.T6230;
import com.dimeng.p2p.S62.entities.T6250;
import com.dimeng.p2p.S62.entities.T6273;
import com.dimeng.p2p.S62.enums.T6230_F10;
import com.dimeng.p2p.S62.enums.T6230_F11;
import com.dimeng.p2p.S62.enums.T6230_F12;
import com.dimeng.p2p.S62.enums.T6230_F13;
import com.dimeng.p2p.S62.enums.T6230_F14;
import com.dimeng.p2p.S62.enums.T6230_F15;
import com.dimeng.p2p.S62.enums.T6230_F16;
import com.dimeng.p2p.S62.enums.T6230_F17;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.S62.enums.T6230_F27;
import com.dimeng.p2p.S62.enums.T6250_F07;
import com.dimeng.p2p.S62.enums.T6250_F08;
import com.dimeng.p2p.S62.enums.T6273_F07;
import com.dimeng.p2p.S62.enums.T6273_F08;
import com.dimeng.p2p.S62.enums.T6273_F10;
import com.dimeng.p2p.S62.enums.T6273_F15;
import com.dimeng.p2p.service.AbstractP2PService;
import com.dimeng.p2p.signature.fdd.utils.FddClientV25;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.StringHelper;

/**
 * 通联账号支付系统服务基础虚类
 * 
 * 提供支付常量，数据链接
 * @author  dengwenwu
 * @version  [版本号, 2014年11月26日]
 */
public abstract class AbstractFddService extends AbstractP2PService
{
    //合同头部
    private static final String HT_HEADER = "BDF";
    
    //标的数字编号
    private static Integer BID_NO = 0;
    
    //标的数字编号位数
    private static Integer BIDNO_COUNT = 4;
    
    //分割线
    private static String TEXT_LINE = "-";
    
    //投资记录数字编码
    private static Integer RECORD_NO = 0;
    
    //投资记录数字编码位数
    private static Integer RECORDNO_COUNT = 4;
    
    public static int RECORDERRORNUM = 0;
    
    protected static ConfigureProvider configureProvider;
    
    public AbstractFddService(ServiceResource serviceResource)
    {
        super(serviceResource);
        configureProvider = serviceResource.getResource(ConfigureProvider.class);
        FddClientV25 fddClient = new FddClientV25();
        fddClient.init(configureProvider);
    }
    
    @Override
    protected Connection getConnection()
        throws ResourceNotFoundException, SQLException
    {
        return serviceResource.getDataConnectionProvider(SQLConnectionProvider.class, P2PConst.DB_MASTER_PROVIDER)
            .getConnection();
    }
    
    protected Connection getConnection(String db)
        throws ResourceNotFoundException, SQLException
    {
        return serviceResource.getDataConnectionProvider(SQLConnectionProvider.class, P2PConst.DB_MASTER_PROVIDER)
            .getConnection(db);
    }
    
    public static ConfigureProvider getConfigureProvider()
    {
        return configureProvider;
    }
    
    /**
     * 创建form表单公共方法
     * @param param 参数map
     * @param formUrl   form表单action提交地址
     * @return  form表单
     * @throws Throwable
     */
    protected String createSubmitForm(Map<String, String> param, String formUrl)
        throws Throwable
    {
        StringBuilder builder = new StringBuilder();
        builder.append("<form action='");
        builder.append(formUrl);
        builder.append("' method=\"post\">");
        for (String key : param.keySet())
        {
            builder.append("<input type=\"hidden\" name=\"");
            builder.append(key);
            builder.append("\" value='");
            builder.append(param.get(key));
            builder.append("' />");
        }
        builder.append("</form>");
        builder.append("<script type=\"text/javascript\">");
        builder.append("document.forms[0].submit();");
        builder.append("</script>");
        return builder.toString();
    }
    
    protected void doPrintWriter(HttpServletResponse response, String location, boolean printTag)
        throws Throwable
    {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        if (printTag)
            logger.info(String.format("发送法大大接口请求参数：%s", location));
        try
        {
            writer.print(location);
        }
        catch (Exception e)
        {
            logger.error(e.toString());
        }
        finally
        {
            writer.flush();
            writer.close();
        }
    }
    
    protected void updateT6273ForUrl(Connection connection, int userId, int bidId, int orderId, T6273_F10 userType,
        String viewUrl, String uploadUrl)
        throws SQLException
    {
        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE S62.T6273 SET F07=?, F16=?, F17=? WHERE F02=? AND F03=? AND F10=? ");
        //非借款人时，订单筛选
        if (userType != T6273_F10.JKR)
        {
            //为投资人时，附加条件：投资成功订单
            if (userType == T6273_F10.TZR)
            {
                sql.append("AND T6273.F14 = ? ");
            }
            //为债权转让时，附加条件：债权订单
            else
            {
                sql.append("AND T6273.F18 = ? ");
            }
        }
        try (PreparedStatement ps = connection.prepareStatement(sql.toString()))
        {
            ps.setString(1, T6273_F07.YQ.name());
            ps.setString(2, viewUrl);
            ps.setString(3, uploadUrl);
            ps.setInt(4, userId);
            ps.setInt(5, bidId);
            ps.setString(6, userType.name());
            if (userType != T6273_F10.JKR)
            {
                //非借款人时，按投资/债权订单筛选
                ps.setInt(7, orderId);
            }
            ps.execute();
        }
    }
    
    /**
     * 生成合同编码
     * @param connection
     * @param ht_index 合同号索引
     * @param bidCode
     * @return
     * @throws Throwable
     */
    protected String getHtCode(String bidCode, int ht_index)
        throws Throwable
    {
        //      YDYL--20160001-标的编号-001 年份后的为标的数量递增，后三位为投资人数递增
        //  年份后的数量值四位，最大就是9999，对P2P项目估计数量值合适，对发消费标类型不合适，需重新讨论格式
        StringBuffer htCode = new StringBuffer();
        
        htCode.append(StringHelper.isEmpty(configureProvider.getProperty(SystemVariable.SIGNATURE_CONTRACT_PTNAME)) ? HT_HEADER
            : configureProvider.getProperty(SystemVariable.SIGNATURE_CONTRACT_PTNAME));
        htCode.append(TEXT_LINE);
        Calendar ca = Calendar.getInstance();
        htCode.append(ca.get(Calendar.YEAR));
        //或者当前标的数字编号
        /* BID_NO = getBidNo(connection, loanId);
         String bidNo = BID_NO.toString();
         for (int i = 0; i < BIDNO_COUNT - bidNo.trim().length(); i++)
         {
             htCode.append("0");
         }
         htCode.append(bidNo);*/
        htCode.append(TEXT_LINE);
        htCode.append(bidCode);
        
        htCode.append(TEXT_LINE);
        
        //        RECORD_NO = getRecordNo(connection, loanId);
        String recordNo = ht_index + "";
        for (int i = 0; i < RECORDNO_COUNT - recordNo.trim().length(); i++)
        {
            htCode.append("0");
        }
        htCode.append(ht_index);
        return htCode.toString();
    }
    
    /**
     * 查询投资记录
     * @return T6250
     * @throws SQLException 
     */
    protected ArrayList<T6250> getT6250s(int loanId, Connection connection)
        throws SQLException
    {
        ArrayList<T6250> t6250s = new ArrayList<T6250>();
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT F03, F01 FROM S62.T6250 WHERE F02 = ?"))
        {
            pstmt.setInt(1, loanId);
            try (ResultSet rs = pstmt.executeQuery())
            {
                
                while (rs.next())
                {
                    T6250 t6250 = null;
                    t6250 = new T6250();
                    t6250.F03 = rs.getInt(1);
                    t6250.F01 = rs.getInt(2);
                    t6250s.add(t6250);
                }
                return t6250s;
            }
        }
    }
    
    /**
     *  根据标的ID查询该标的的所有投资人
     * <功能详细描述>
     * @param connection 
     * @param F02
     * @return
     * @throws SQLException
     */
    protected T6250[] selectAllT6250(Connection connection, int F02)
        throws SQLException
    {
        List<T6250> list = new ArrayList<T6250>();
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01,F02,F03 FROM S62.T6250 WHERE F02 = ? AND F07 = ? AND F08=?  "))
        // GROUP BY F02,F03  , 每个投资人都一份合同，不组合
        {
            pstmt.setInt(1, F02);
            pstmt.setString(2, T6250_F07.F.name());
            pstmt.setString(3, T6250_F08.S.name());
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                while (resultSet.next())
                {
                    T6250 t6250 = new T6250();
                    t6250.F01 = resultSet.getInt(1);
                    t6250.F02 = resultSet.getInt(2);
                    t6250.F03 = resultSet.getInt(3);
                    list.add(t6250);
                }
            }
        }
        return list.toArray(new T6250[list.size()]);
    }
    
    /**
     * 改合同地址信息
     * <功能详细描述>
     * @param connection
     * @param status
     * @param status_sign
     * @param userId
     * @param sid
     * @param download_url
     * @param viewpdf_url
     * @throws SQLException
     */
    protected void updateT6273Info(Connection connection, T6273_F07 status, T6273_F15 status_sign, int userId, int sid,
        String download_url, String viewpdf_url, String transaction_id)
        throws SQLException
    {
        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE S62.T6273 ");
        sql.append("SET F07=? "); // 签名状态
        sql.append(", F15=? "); // 签章状态
        sql.append(", F16=? "); // 在线查看地址
        sql.append(", F17=? "); // 下载地址
        if (!StringHelper.isEmpty(transaction_id))
        {
            sql.append(", F19=? "); // 交易号
        }
        sql.append(" WHERE F01=? AND F02=?  ");
        
        try (PreparedStatement ps = connection.prepareStatement(sql.toString()))
        {
            ps.setString(1, status.name());
            ps.setString(2, status_sign.name());
            ps.setString(3, viewpdf_url);
            ps.setString(4, download_url);
            if (!StringHelper.isEmpty(transaction_id))
            {
                ps.setString(5, download_url);
                ps.setInt(6, sid);
                ps.setInt(7, userId);
            }
            else
            {
                ps.setInt(5, sid);
                ps.setInt(6, userId);
            }
            ps.execute();
        }
    }
    
    protected void updateT6273ForStatus(Connection connection, T6273_F15 status, int userId, int bidId, int orderId,
        T6273_F10 userType)
        throws SQLException
    {
        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE S62.T6273 ");
        sql.append("SET F15=? ");
        /* if (isUpdateTime)
         {
             sql.append("F06 = CURRENT_TIMESTAMP() ");
         }*/
        sql.append("WHERE F02=? AND F03=? AND F10=? ");
        //非借款人时，订单筛选
        if (userType != T6273_F10.JKR)
        {
            //为投资人时，附加条件：投资成功订单
            if (userType == T6273_F10.TZR)
            {
                sql.append("AND T6273.F14 = ? ");
            }
            //为债权转让时，附加条件：债权订单
            else
            {
                sql.append("AND T6273.F18 = ? ");
            }
        }
        try (PreparedStatement ps = connection.prepareStatement(sql.toString()))
        {
            ps.setString(1, status.name());
            ps.setInt(2, userId);
            ps.setInt(3, bidId);
            ps.setString(4, userType.name());
            if (userType != T6273_F10.JKR)
            {
                //非借款人时，按投资/债权订单筛选
                ps.setInt(5, orderId);
            }
            ps.execute();
        }
    }
    
    protected void updateT6273ForSign(Connection connection, String transaction_id, int userId, int bidId, int orderId,
        T6273_F10 userType)
        throws SQLException
    {
        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE S62.T6273 ");
        sql.append("SET F06 = CURRENT_TIMESTAMP() ");
        sql.append(", F19 = ? ");
        sql.append("WHERE F02=? AND F03=? AND F10=? ");
        //非借款人时，订单筛选
        if (userType != T6273_F10.JKR)
        {
            //为投资人时，附加条件：投资成功订单
            if (userType == T6273_F10.TZR)
            {
                sql.append("AND T6273.F14 = ? ");
            }
            //为债权转让时，附加条件：债权订单
            else
            {
                sql.append("AND T6273.F18 = ? ");
            }
        }
        try (PreparedStatement ps = connection.prepareStatement(sql.toString()))
        {
            ps.setString(1, transaction_id);
            ps.setInt(2, userId);
            ps.setInt(3, bidId);
            ps.setString(4, userType.name());
            if (userType != T6273_F10.JKR)
            {
                //非借款人时，按投资/债权订单筛选
                ps.setInt(5, orderId);
            }
            ps.execute();
        }
    }
    
    protected T6273 selectT6273(Connection connection, int userId, int bidId, int orderId, T6273_F10 userType)
        throws Throwable
    {
        T6273 record = null;
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11, F12, F13, F14 , F15, F16, F17, F18, F19 FROM S62.T6273 ");
        sql.append("WHERE T6273.F02 = ? AND T6273.F03 = ? AND T6273.F10 = ? ");
        //非借款人时，订单筛选
        if (userType != T6273_F10.JKR)
        {
            //为投资人时，附加条件：投资成功订单
            if (userType == T6273_F10.TZR)
            {
                sql.append("AND T6273.F14 = ? ");
            }
            //为债权转让时，附加条件：债权订单
            else
            {
                sql.append("AND T6273.F18 = ? ");
            }
        }
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql.toString()))
        {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, bidId);
            pstmt.setString(3, userType.name());
            if (userType != T6273_F10.JKR)
            {
                //非借款人时，按投资成功订单筛选
                pstmt.setInt(4, orderId);
            }
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
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
                }
            }
            
        }
        return record;
    }
    
    /**
     * 查询用户信息
     * @param connection
     * @param userId
     * @return
     * @throws SQLException
     */
    protected T6110 selectT6110(Connection connection, int userId)
        throws SQLException
    {
        T6110 record = null;
        try
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01,F02, F03, F04, F05, F06, F07, F08, F09, F10, F20"
                    + " FROM S61.T6110 WHERE T6110.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, userId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new T6110();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getString(2);
                        record.F03 = resultSet.getString(3);
                        record.F04 = resultSet.getString(4);
                        record.F05 = resultSet.getString(5);
                        record.F06 = T6110_F06.parse(resultSet.getString(6));
                        record.F07 = T6110_F07.parse(resultSet.getString(7));
                        record.F08 = T6110_F08.parse(resultSet.getString(8));
                        record.F09 = resultSet.getTimestamp(9);
                        record.F10 = T6110_F10.parse(resultSet.getString(10));
                        record.F20 = resultSet.getString(11);
                    }
                }
            }
        }
        catch (Exception e)
        {
            logger.error(e);
        }
        return record;
    }
    
    /**
     * 查询标的信息
     * <功能详细描述>
     * @param connection
     * @param F01
     * @return
     * @throws SQLException
     */
    protected T6230 selectT6230(Connection connection, int F01)
        throws SQLException
    {
        T6230 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11, F12, F13, F14, F15, F16, F17, F18, F19, F20, "
                + " F21, F22, F23, F24, F25, F26, F27 FROM S62.T6230 WHERE T6230.F01 = ? LIMIT 1"))
        {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6230();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getString(3);
                    record.F04 = resultSet.getInt(4);
                    record.F05 = resultSet.getBigDecimal(5);
                    record.F06 = resultSet.getBigDecimal(6);
                    record.F07 = resultSet.getBigDecimal(7);
                    record.F08 = resultSet.getInt(8);
                    record.F09 = resultSet.getInt(9);
                    record.F10 = T6230_F10.parse(resultSet.getString(10));
                    record.F11 = T6230_F11.parse(resultSet.getString(11));
                    record.F12 = T6230_F12.parse(resultSet.getString(12));
                    record.F13 = T6230_F13.parse(resultSet.getString(13));
                    record.F14 = T6230_F14.parse(resultSet.getString(14));
                    record.F15 = T6230_F15.parse(resultSet.getString(15));
                    record.F16 = T6230_F16.parse(resultSet.getString(16));
                    record.F17 = T6230_F17.parse(resultSet.getString(17));
                    record.F18 = resultSet.getInt(18);
                    record.F19 = resultSet.getInt(19);
                    record.F20 = T6230_F20.parse(resultSet.getString(20));
                    record.F21 = resultSet.getString(21);
                    record.F22 = resultSet.getTimestamp(22);
                    record.F23 = resultSet.getInt(23);
                    record.F24 = resultSet.getTimestamp(24);
                    record.F25 = resultSet.getString(25);
                    record.F26 = resultSet.getBigDecimal(26);
                    record.F27 = T6230_F27.parse(resultSet.getString(27));
                }
            }
        }
        return record;
    }
    
}
