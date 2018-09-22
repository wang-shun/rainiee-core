package com.dimeng.p2p.modules.bid.user.service.achieve;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.config.Envionment;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.XyType;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.entities.T6141;
import com.dimeng.p2p.S61.entities.T6161;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.S61.enums.T6110_F08;
import com.dimeng.p2p.S61.enums.T6110_F10;
import com.dimeng.p2p.S61.enums.T6141_F03;
import com.dimeng.p2p.S61.enums.T6141_F04;
import com.dimeng.p2p.S62.entities.T6230;
import com.dimeng.p2p.S62.entities.T6231;
import com.dimeng.p2p.S62.entities.T6251;
import com.dimeng.p2p.S62.entities.T6260;
import com.dimeng.p2p.S62.entities.T6262;
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
import com.dimeng.p2p.S62.enums.T6231_F21;
import com.dimeng.p2p.S62.enums.T6251_F08;
import com.dimeng.p2p.S62.enums.T6260_F07;
import com.dimeng.p2p.S62.enums.T6273_F07;
import com.dimeng.p2p.S62.enums.T6273_F08;
import com.dimeng.p2p.S62.enums.T6273_F10;
import com.dimeng.p2p.S62.enums.T6273_F15;
import com.dimeng.p2p.common.entities.Bdxq;
import com.dimeng.p2p.common.entities.Dzxy;
import com.dimeng.p2p.common.entities.DzxyZqzr;
import com.dimeng.p2p.modules.bid.user.service.ZqzrSignaManage;
import com.dimeng.p2p.modules.bid.user.service.entity.ZQZRinfo;
import com.dimeng.p2p.order.PdfFormationExecutor;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.Formater;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.TimestampParser;

public class ZqzrSignaManageImpl extends AbstractBidManage implements ZqzrSignaManage
{
    //合同头部
    private static final String HT_HEADER = "XSS";
    
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
    
    public ZqzrSignaManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public void setZqzrSigna(int orderId)
        throws Throwable
    {
        
        try (Connection connection = getConnection())
        {
            setZqzrSigna(connection, orderId);
        }
    }
    
    private void setZqzrSigna(Connection connection, int orderId)
        throws Throwable
    {
        
        ZQZRinfo info = selectZQZRinfo(connection, orderId);
        //生成电子签章
        //转让人
        T6273 t6273_zrr = new T6273();
        t6273_zrr.F02 = info.zqrId;
        t6273_zrr.F03 = info.bidId;
        t6273_zrr.F07 = T6273_F07.DQ;
        t6273_zrr.F08 = T6273_F08.ZQZRHT;
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S62.T6273 SET F02 = ?, F03 = ?, F07 = ?, F08 = ?, F10 = ?, F13 = ?, F14 = ?, F18 = ?, F15 = ?"))
        {
            pstmt.setInt(1, info.zqrId);
            pstmt.setInt(2, info.bidId);
            pstmt.setString(3, T6273_F07.DQ.name());
            pstmt.setString(4, T6273_F08.ZQZRHT.name());
            pstmt.setString(5, T6273_F10.ZCR.name());
            pstmt.setTimestamp(6, getCurrentTimestamp(connection));
            pstmt.setInt(7, info.recordId);
            pstmt.setInt(8, info.zqzrId);
            pstmt.setString(9, T6273_F15.DSQ.name());
            pstmt.execute();
        }
        t6273_zrr.F01 = selectT6273Id(info.bidId, info.zqrId, info.zqzrId, T6273_F10.ZCR, connection);
        doHtmlSavePdf(t6273_zrr, connection, orderId, info.zqsqId);
        
        //生成电子签章
        //债权人
        T6273 t6273_zqr = new T6273();
        t6273_zqr.F02 = info.srrId;
        t6273_zqr.F03 = info.bidId;
        t6273_zqr.F07 = T6273_F07.DQ;
        t6273_zqr.F08 = T6273_F08.ZQZRHT;
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S62.T6273 SET F02 = ?, F03 = ?, F07 = ?, F08 = ?, F10 = ?, F13 = ?, F14 = ?, F18 = ?, F15 = ?"))
        {
            pstmt.setInt(1, info.srrId);
            pstmt.setInt(2, info.bidId);
            pstmt.setString(3, T6273_F07.DQ.name());
            pstmt.setString(4, T6273_F08.ZQZRHT.name());
            pstmt.setString(5, T6273_F10.SRR.name());
            pstmt.setTimestamp(6, getCurrentTimestamp(connection));
            pstmt.setInt(7, info.recordId);
            pstmt.setInt(8, info.zqzrId);
            pstmt.setString(9, T6273_F15.DSQ.name());
            pstmt.execute();
        }
        t6273_zqr.F01 = selectT6273Id(info.bidId, info.srrId, info.zqzrId, T6273_F10.SRR, connection);
        doHtmlSavePdf(t6273_zqr, connection, orderId, info.zqsqId);
        RECORDERRORNUM = 0;
        updateRecordState(info.recordId, connection);
        
    }
    
    /**
     * 购买成功修改投资合同状态
     * @param recordId
     * @param connection
     * @throws Throwable
     */
    protected void updateRecordState(int recordId, Connection connection)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S62.T6273 SET F07 = ? WHERE F14 = ? AND F08 = ?"))
        {
            pstmt.setString(1, T6273_F07.YQ.name());
            pstmt.setInt(2, recordId);
            pstmt.setString(3, T6273_F08.JKHT.name());
            pstmt.execute();
        }
    }
    
    /**
     * 生成pdf合同，并保存合同编号和保存路径
     * @param t6273
     * @param connection
     * @throws Throwable
     */
    protected void doHtmlSavePdf(T6273 t6273, Connection connection, int orderId, int zqsqId)
        throws Throwable
    {
        
        ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
        //生成债权转让合同PDF并保全
        try
        {
            String charset = serviceResource.getCharset();
            PdfFormationExecutor cpfe = serviceResource.getResource(PdfFormationExecutor.class);
            Map<String, Object> valueMap = null;
            StringBuffer sb = new StringBuffer();
            StringBuffer sbs = new StringBuffer();
            String pdfPath = null;
            
            valueMap = getClaimContentMap(configureProvider, zqsqId, t6273.F02, connection);
            if (null != valueMap)
            {
                sb.setLength(0);
                sb.append(configureProvider.getProperty(SystemVariable.CONTRACT_TEMPLATE_HTML_HEADER));
                sb.append((String)valueMap.get("dzxy_content"));
                sb.append(configureProvider.getProperty(SystemVariable.CONTRACT_TEMPLATE_HTML_FOOTER));
                String path =
                    cpfe.createHTML(valueMap,
                        "contract",
                        (String)valueMap.get("dzxy_xymc"),
                        sb.toString(),
                        charset,
                        (String)valueMap.get("xy_no"));
                if (!StringHelper.isEmpty(path))
                {
                    sbs.setLength(0);
                    sbs.append(configureProvider.getProperty(SystemVariable.SITE_REQUEST_PROTOCOL))
                        .append(configureProvider.getProperty(SystemVariable.SITE_DOMAIN))
                        .append(serviceResource.getContextPath())
                        .append("/");
                    pdfPath = cpfe.convertHtml2Pdf(path, sbs.toString(), charset);
                    t6273.F09 = pdfPath;
                    t6273.F04 = (String)valueMap.get("xy_no");
                    updateT6273PdfPathNo(t6273, connection);
                    logger.info("生成转让pdf合同文档成功！");
                }
            }
        }
        catch (Exception e)
        {
            logger.error("Zqzr.processPost()", e);
        }
        
    }
    
    public Map<String, Object> getClaimContentMap(ConfigureProvider configureProvider, int zqsqId, int uId,
        Connection connection)
        throws Throwable
    {
        Dzxy dzxy = null;
        DzxyZqzr dzxyZqzr = null;
        dzxy = getDzxy(connection, zqsqId, uId);
        if (null == dzxy)
        {
            logger.info("转让债权：" + zqsqId + "时，获取债权转让合同模板为空");
            return null;
        }
        dzxyZqzr = getDzxyZqzr(connection, configureProvider, zqsqId);
        
        final Envionment envionment = configureProvider.createEnvionment();
        
        Map<String, Object> valueMap = new HashMap<String, Object>()
        {
            
            private static final long serialVersionUID = 1L;
            
            @Override
            public Object get(Object key)
            {
                Object object = super.get(key);
                if (object == null)
                {
                    return envionment == null ? null : envionment.get(key.toString());
                }
                return object;
            }
        };
        valueMap.put("dzxy_content", dzxy.content);
        valueMap.put("dzxy_xymc", dzxy.xymc);
        valueMap.put("xy_no", dzxyZqzr.xy_no);
        valueMap.put("jf_yhlx", dzxyZqzr.t6110.F06.name());
        valueMap.put("jf_zsxm", dzxyZqzr.t6141.F02 == null ? "" : dzxyZqzr.t6141.F02);
        valueMap.put("jf_sfzh", dzxyZqzr.t6141.F07 == null ? "" : dzxyZqzr.t6141.F07);
        if (dzxyZqzr.t6161 != null)
        {
            valueMap.put("jf_companyName", dzxyZqzr.t6161.F04 == null ? "" : dzxyZqzr.t6161.F04);
            valueMap.put("jf_papersNum", dzxyZqzr.t6161.F20.equals("Y") ? dzxyZqzr.t6161.F19 : dzxyZqzr.t6161.F03);
        }
        valueMap.put("jf_yhm", dzxyZqzr.t6110.F02);
        
        valueMap.put("yf_yhlx", dzxyZqzr.buyT6110.F06.name());
        valueMap.put("yf_zsxm", dzxyZqzr.zqzr_yf_realName == null ? "" : dzxyZqzr.zqzr_yf_realName);
        valueMap.put("yf_sfzh", dzxyZqzr.zqzr_yf_sfzh == null ? "" : dzxyZqzr.zqzr_yf_sfzh);
        if (dzxyZqzr.buyT6161 != null)
        {
            valueMap.put("yf_companyName", dzxyZqzr.buyT6161.F04 == null ? "" : dzxyZqzr.buyT6161.F04);
            valueMap.put("yf_papersNum", dzxyZqzr.buyT6161.F20.equals("Y") ? dzxyZqzr.buyT6161.F19
                : dzxyZqzr.buyT6161.F03);
        }
        valueMap.put("yf_yhm", dzxyZqzr.zqzr_yf_loginName);
        
        valueMap.put("bid_title", dzxyZqzr.bdxq.F03);
        valueMap.put("bid_rate", dzxyZqzr.bdxq.F06);
        valueMap.put("bid_hkfs", dzxyZqzr.bdxq.F10.getChineseName());
        if (T6231_F21.S == dzxyZqzr.t6231.F21)
        {
            valueMap.put("bid_yzqqx", dzxyZqzr.bdxq.F09 + "天");
        }
        else
        {
            valueMap.put("bid_yzqqx", dzxyZqzr.bdxq.F09 + "个月");
        }
        //valueMap.put("bid_hkr", dzxyZqzr.t6231.F06);
        valueMap.put("bid_hkr", TimestampParser.format(dzxyZqzr.t6231.F06, "yyyy-MM-dd"));
        valueMap.put("bid_dhqs", dzxyZqzr.t6262.F10);
        valueMap.put("bid_zqs", dzxyZqzr.t6231.F02);
        if (dzxyZqzr.zqzr_bid_ychbxse.compareTo(BigDecimal.ZERO) > 0)
        {
            valueMap.put("bid_ychbxse", dzxyZqzr.zqzr_bid_ychbxse);
        }
        else
        {
            valueMap.put("bid_ychbxse", "");
        }
        
        valueMap.put("zqr_bjsr", Formater.formatAmount(dzxyZqzr.t6251.F05));
        valueMap.put("zqr_dsbx", dzxyZqzr.zqzr_zqr_dsbx);
        valueMap.put("zqr_tbsj", TimestampParser.format(dzxyZqzr.t6251.F09, "yyyy-MM-dd"));
        
        valueMap.put("bid_dqjz", Formater.formatAmount(dzxyZqzr.t6262.F04));
        
        valueMap.put("zqrz_zrjg", Formater.formatAmount(dzxyZqzr.t6260.F03));
        valueMap.put("zqrz_rate", Formater.formatAmount(dzxyZqzr.t6260.F08.multiply(dzxyZqzr.t6260.F03)));
        valueMap.put("zqrz_zrsj", TimestampParser.format(dzxyZqzr.t6262.F07, "yyyy-MM-dd"));
        
        valueMap.put("site_name", configureProvider.format(SystemVariable.SITE_NAME));
        valueMap.put("site_domain", configureProvider.format(SystemVariable.SITE_DOMAIN));
        valueMap.put("company_name", configureProvider.format(SystemVariable.COMPANY_NAME));
        return valueMap;
    }
    
    protected void updateT6273PdfPathNo(T6273 t6273, Connection connection)
        throws Throwable
    {
        String sql = "UPDATE S62.T6273 SET F04 = ?,F09 = ?, F20 = ? WHERE F01 = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql))
        {
            pstmt.setString(1, t6273.F04);
            pstmt.setString(2, t6273.F09);
            pstmt.setString(3, t6273.F04 + T6273_F08.ZQZRHT.getChineseName());
            pstmt.setInt(4, t6273.F01);
            pstmt.execute();
        }
    }
    
    private DzxyZqzr getDzxyZqzr(Connection connection, ConfigureProvider configureProvider, int zqzrjlId)
        throws Throwable
    {
        DzxyZqzr dzxyZqzr = new DzxyZqzr();
        T6230 t6230 = getBid(zqzrjlId, connection);
        
        String htCode = getHtCode(configureProvider, connection, t6230.F01, t6230.F25);
        System.out.println("生成合同编号成功:" + htCode);
        dzxyZqzr.xy_no = htCode;
        dzxyZqzr.t6262 = selectT6262(connection, zqzrjlId);
        dzxyZqzr.t6260 = selectT6260(connection, dzxyZqzr.t6262.F02);
        dzxyZqzr.t6251 = selectT6251(connection, dzxyZqzr.t6260.F02);
        dzxyZqzr.bdxq = getBid(connection, dzxyZqzr.t6251.F03);
        /**
         * Bug #19497【后台-统计管理-债权转让统计表】已转让成功的债权，借款人还款后该列表的“剩余期限”会变动
         */
        /* if(dzxyZqzr.bdxq.F10 == T6230_F10.MYFX){
            dzxyZqzr.t6262.F10 = getSyqs(dzxyZqzr.bdxq.F01,connection);
        }*/
        dzxyZqzr.t6231 = getExtra(connection, dzxyZqzr.t6251.F03);
        dzxyZqzr.t6110 = getUserInfo(connection, dzxyZqzr.t6251.F04);
        dzxyZqzr.t6141 = selectT6141(connection, dzxyZqzr.t6251.F04);
        dzxyZqzr.t6161 = selectT6161(connection, dzxyZqzr.t6251.F04);
        T6141 t6141 = selectT6141(connection, dzxyZqzr.t6262.F03);
        dzxyZqzr.buyT6110 = getUserInfo(connection, dzxyZqzr.t6262.F03);
        dzxyZqzr.buyT6161 = selectT6161(connection, dzxyZqzr.t6262.F03);
        dzxyZqzr.zqzr_yf_loginName = dzxyZqzr.buyT6110.F02;
        dzxyZqzr.zqzr_yf_realName = t6141.F02;
        dzxyZqzr.zqzr_yf_sfzh = t6141.F07;
        
        dzxyZqzr.zqzr_zqr_dsbx = getDsbx(zqzrjlId, connection);
        if (("等额本息").equals(dzxyZqzr.bdxq.F10.getChineseName()))
        {
            dzxyZqzr.zqzr_bid_ychbxse = getYchbx(connection, dzxyZqzr.t6251.F03, dzxyZqzr.t6262.F03);
        }
        else
        {
            dzxyZqzr.zqzr_bid_ychbxse = BigDecimal.ZERO;
        }
        
        return dzxyZqzr;
    }
    
    /**
     * 根据用户ID查询用户基本信息
     * 
     * @param connection
     * @param userID
     * @return
     * @throws Throwable
     */
    private T6141 selectT6141(Connection connection, int userID)
        throws Throwable
    {
        T6141 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08 FROM S61.T6141 WHERE T6141.F01 = ? LIMIT 1"))
        {
            pstmt.setInt(1, userID);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6141();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getString(2);
                    record.F03 = T6141_F03.parse(resultSet.getString(3));
                    record.F04 = T6141_F04.parse(resultSet.getString(4));
                    record.F05 = resultSet.getString(5);
                    record.F06 = resultSet.getString(6);
                    record.F07 = resultSet.getString(7);
                    record.F07 = StringHelper.decode(record.F07);
                    record.F08 = resultSet.getDate(8);
                }
            }
        }
        return record;
    }
    
    private T6161 selectT6161(Connection connection, int userID)
        throws Throwable
    {
        T6161 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11, F12, F13, F14, F15, F16, F17, F18, F19, F20 FROM S61.T6161 WHERE T6161.F01 = ? LIMIT 1"))
        {
            pstmt.setInt(1, userID);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6161();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getString(2);
                    record.F03 = resultSet.getString(3);
                    record.F04 = resultSet.getString(4);
                    record.F05 = resultSet.getString(5);
                    record.F06 = resultSet.getString(6);
                    record.F07 = resultSet.getInt(7);
                    record.F08 = resultSet.getBigDecimal(8);
                    record.F09 = resultSet.getString(9);
                    record.F10 = resultSet.getInt(10);
                    record.F11 = resultSet.getString(11);
                    record.F12 = resultSet.getString(12);
                    record.F13 = resultSet.getString(13);
                    if (record.F13 != null)
                    {
                        record.F13 = StringHelper.decode(record.F13);
                    }
                    record.F14 = resultSet.getBigDecimal(14);
                    record.F15 = resultSet.getBigDecimal(15);
                    record.F16 = resultSet.getString(16);
                    record.F17 = resultSet.getString(17);
                    record.F18 = resultSet.getString(18);
                    record.F19 = resultSet.getString(19);
                    record.F20 = resultSet.getString(20);
                }
            }
        }
        return record;
    }
    
    /**
     * 根据用户ID查询用户信息
     * 
     * @param userId
     * @return
     * @throws Throwable
     */
    private T6110 getUserInfo(Connection connection, int userId)
        throws Throwable
    {
        T6110 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10 FROM S61.T6110 WHERE T6110.F01 = ? LIMIT 1"))
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
                }
            }
        }
        return record;
    }
    
    /**
     * 获取标的扩展信息
     */
    private T6231 getExtra(Connection connection, int id)
        throws Throwable
    {
        T6231 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11, F12, F13, F14, F15, F16,F21,F22 FROM S62.T6231 WHERE T6231.F01 = ? LIMIT 1"))
        {
            pstmt.setInt(1, id);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6231();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getInt(3);
                    record.F04 = resultSet.getBigDecimal(4);
                    record.F05 = resultSet.getBigDecimal(5);
                    record.F06 = resultSet.getDate(6);
                    record.F07 = resultSet.getInt(7);
                    record.F08 = resultSet.getString(8);
                    record.F09 = resultSet.getString(9);
                    record.F10 = resultSet.getTimestamp(10);
                    record.F11 = resultSet.getTimestamp(11);
                    record.F12 = resultSet.getTimestamp(12);
                    record.F13 = resultSet.getTimestamp(13);
                    record.F14 = resultSet.getTimestamp(14);
                    record.F15 = resultSet.getTimestamp(15);
                    record.F16 = resultSet.getString(16);
                    record.F21 = T6231_F21.parse(resultSet.getString(17));
                    record.F22 = resultSet.getInt(18);
                }
            }
            return record;
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
    /* protected String getHtCode(ConfigureProvider configureProvider,String bidCode, int ht_index)
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
          BID_NO = getBidNo(connection, loanId);
          String bidNo = BID_NO.toString();
          for (int i = 0; i < BIDNO_COUNT - bidNo.trim().length(); i++)
          {
              htCode.append("0");
          }
          htCode.append(bidNo);
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
     }*/
    
    /**
     * 生成合同编码
     * @param connection
     * @param loanId
     * @param bidCode
     * @return
     * @throws Throwable
     */
    private String getHtCode(ConfigureProvider configureProvider, Connection connection, int loanId, String bidCode)
        throws Throwable
    {
        //      XSS--20160001-标的编号-001 年份后的为标的数量递增，后三位为投资人数递增
        StringBuffer htCode = new StringBuffer();
        htCode.append(StringHelper.isEmpty(configureProvider.getProperty(SystemVariable.SIGNATURE_CONTRACT_PTNAME)) ? HT_HEADER
            : configureProvider.getProperty(SystemVariable.SIGNATURE_CONTRACT_PTNAME));
        htCode.append(TEXT_LINE);
        Calendar a = Calendar.getInstance();
        htCode.append(a.get(Calendar.YEAR));
        //或者当前标的数字编号
        /*BID_NO = getBidNo(connection, loanId);
        String bidNo = BID_NO.toString();
        for(int i=0; i<BIDNO_COUNT-bidNo.trim().length(); i++){
        	htCode.append("0");
        }
        htCode.append(bidNo);*/
        htCode.append(TEXT_LINE);
        htCode.append(bidCode);
        
        htCode.append(TEXT_LINE);
        
        RECORD_NO = getRecordNo(connection, loanId);
        String recordNo = RECORD_NO.toString();
        for (int i = 0; i < RECORDNO_COUNT - recordNo.trim().length(); i++)
        {
            htCode.append("0");
        }
        htCode.append(RECORD_NO);
        return htCode.toString();
    }
    
    /**
     * 获取标的主信息
     * 
     * @param id
     * @return
     * @throws Throwable
     */
    private Bdxq getBid(Connection connection, int id)
        throws Throwable
    {
        Bdxq record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT T6230.F01, T6230.F02, T6230.F03, T6230.F04, T6230.F05, T6230.F06, T6230.F07, T6230.F08, T6230.F09, T6230.F10, T6230.F11, T6230.F12, T6230.F13, T6230.F14, T6230.F15, T6230.F16, T6230.F17, T6230.F18, T6230.F19, T6230.F20, T6230.F21, T6230.F22, T6230.F23, T6230.F24, T6230.F25, T6230.F26,ADDDATE(T6230.F22,INTERVAL T6230.F08 DAY), T6231.F21 F211, T6231.F22 F221 FROM S62.T6230, S62.T6231 WHERE S62.T6230.F01 = S62.T6231.F01 AND T6230.F01 = ? LIMIT 1"))
        {
            pstmt.setInt(1, id);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new Bdxq();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getString(3);
                    record.F04 = resultSet.getInt(4);
                    record.F05 = resultSet.getBigDecimal(5);
                    record.F06 = resultSet.getBigDecimal(6);
                    record.F07 = resultSet.getBigDecimal(7);
                    record.F08 = resultSet.getInt(8);
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
                    record.jsTime = resultSet.getTimestamp(27);
                    if (T6231_F21.parse(resultSet.getString(28)).equals(T6231_F21.F))
                    {
                        record.F09 = resultSet.getInt(9);
                    }
                    else
                    {
                        record.F09 = resultSet.getInt(29);
                    }
                }
            }
            return record;
        }
    }
    
    private T6230 getBid(int zqId, Connection connection)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT T6230.F01 AS F01, T6230.F25 AS F02 FROM S62.T6260 LEFT JOIN S62.T6251 ON T6260.F02 = T6251.F01 LEFT JOIN S62.T6230 ON T6230.F01 = T6251.F03 WHERE T6260.F01 =? LIMIT 1"))
        {
            pstmt.setInt(1, zqId);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    T6230 t6230 = new T6230();
                    t6230.F01 = resultSet.getInt(1);
                    t6230.F25 = resultSet.getString(2);
                    return t6230;
                }
            }
        }
        return null;
    }
    
    /**
     * 获取标的数字编号
     * @param connection
     * @param loanId
     * @return
     * @throws Throwable
     */
    private synchronized Integer getBidNo(Connection connection, int loanId)
        throws Throwable
    {
        int num = 0;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT RIGHT(LEFT(MAX(F04), 13), 4) FROM S62.T6273 WHERE F03=? LIMIT 1"))
        {
            pstmt.setInt(1, loanId);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    num = resultSet.getInt(1);
                }
            }
        }
        if (num <= 0)
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT RIGHT(LEFT(MAX(F04), 13), 4) FROM (SELECT F04 FROM S62.T6273 GROUP BY F03) T6273 WHERE LEFT(F04, 9) LIKE ?  LIMIT 1"))
            {
                Calendar ca = Calendar.getInstance();
                String parm = "%" + ca.get(Calendar.YEAR) + "%";
                pstmt.setString(1, parm);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        num = resultSet.getInt(1);
                    }
                }
            }
            num++;
        }
        return num;
    }
    
    /**
     * 获取标的合同数
     * @param connection
     * @param loanId
     * @return
     * @throws Throwable
     */
    private synchronized Integer getRecordNo(Connection connection, int loanId)
        throws Throwable
    {
        Integer num = 0;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT COUNT(*) FROM S62.T6273 WHERE F03=? LIMIT 1"))
        {
            pstmt.setInt(1, loanId);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    num = resultSet.getInt(1);
                }
            }
        }
        if (num == 0)
        {
            logger.error("=========================================返回投资人编号为空!");
            num = ++RECORDERRORNUM;
        }
        return num;
    }
    
    /**
     * 根据表的ID查询   月偿还本息
     * 
     * @param zqId
     * @param zqzr_yf_id
     * @return
     * @throws Throwable
     */
    private BigDecimal getYchbx(Connection connection, int zqId, int zqzr_yf_id)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT IFNULL(SUM(F07),0) FROM S62.T6252 WHERE F02 = ? AND T6252.F04 = ? AND T6252.F05 IN (?,?) GROUP BY T6252.F06 LIMIT 1"))
        {
            pstmt.setInt(1, zqId);
            pstmt.setInt(2, zqzr_yf_id);
            pstmt.setInt(3, FeeCode.TZ_LX);
            pstmt.setInt(4, FeeCode.TZ_BJ);
            
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    return resultSet.getBigDecimal(1);
                }
            }
        }
        return new BigDecimal(0);
    }
    
    protected Dzxy getBidContent(int loanId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT T5126.F03,T5125.F03 AS f03 FROM S62.T6239,S51.T5126,S51.T5125 WHERE T6239.F02 = T5126.F01 AND T6239.F03 = T5126.F02 AND T5126.F01 = T5125.F01 AND T6239.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, loanId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    Dzxy dzxy = null;
                    if (resultSet.next())
                    {
                        dzxy = new Dzxy();
                        dzxy.content = resultSet.getString(1);
                        dzxy.xymc = resultSet.getString(2);
                    }
                    return dzxy;
                }
            }
        }
    }
    
    /**
     * 线上债权转让记录
     * 
     * @param connection
     * @param F02
     * @return
     * @throws Throwable
     */
    private T6262 selectT6262(Connection connection, int F02)
        throws Throwable
    {
        T6262 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10 FROM S62.T6262 WHERE T6262.F02 = ? LIMIT 1"))
        {
            pstmt.setInt(1, F02);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6262();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getInt(3);
                    record.F04 = resultSet.getBigDecimal(4);
                    record.F05 = resultSet.getBigDecimal(5);
                    record.F06 = resultSet.getBigDecimal(6);
                    record.F07 = resultSet.getTimestamp(7);
                    record.F08 = resultSet.getBigDecimal(8);
                    record.F09 = resultSet.getBigDecimal(9);
                    record.F10 = resultSet.getInt(10);
                }
            }
        }
        return record;
    }
    
    /**
     * 线上债权转让申请记录
     * 
     * @param connection
     * @param zqzrsqId
     * @return
     * @throws Throwable
     */
    private T6260 selectT6260(Connection connection, int zqzrsqId)
        throws Throwable
    {
        T6260 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08 FROM S62.T6260 WHERE T6260.F01 = ? LIMIT 1"))
        {
            pstmt.setInt(1, zqzrsqId);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6260();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getBigDecimal(3);
                    record.F04 = resultSet.getBigDecimal(4);
                    record.F05 = resultSet.getTimestamp(5);
                    record.F06 = resultSet.getDate(6);
                    record.F07 = T6260_F07.parse(resultSet.getString(7));
                    record.F08 = resultSet.getBigDecimal(8);
                }
            }
        }
        return record;
    }
    
    /**
     * 债权ID
     * 
     * @param connection
     * @param zqId
     * @return
     * @throws Throwable
     */
    private T6251 selectT6251(Connection connection, int zqId)
        throws Throwable
    {
        T6251 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11 FROM S62.T6251 WHERE T6251.F01 = ? LIMIT 1"))
        {
            pstmt.setInt(1, zqId);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6251();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getString(2);
                    record.F03 = resultSet.getInt(3);
                    record.F04 = resultSet.getInt(4);
                    record.F05 = resultSet.getBigDecimal(5);
                    record.F06 = resultSet.getBigDecimal(6);
                    record.F07 = resultSet.getBigDecimal(7);
                    record.F08 = T6251_F08.parse(resultSet.getString(8));
                    record.F09 = resultSet.getDate(9);
                    record.F10 = resultSet.getDate(10);
                    record.F11 = resultSet.getInt(11);
                }
            }
        }
        return record;
    }
    
    /**
     * 
     * @param zqId
     * @return
     * @throws Throwable
     */
    private BigDecimal getDsbx(int zqId, Connection connection)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT SUM(F07) FROM S62.T6252 WHERE T6252.F11 = (SELECT T6251.F01 FROM S62.T6251 WHERE T6251.F12 = (SELECT T6507.F01 FROM S65.T6507 WHERE T6507.F02 = ? ORDER BY T6507.F01 desc limit 1)) AND T6252.F05 IN (?,?) "))
        {
            pstmt.setInt(1, zqId);
            pstmt.setInt(2, FeeCode.TZ_LX);
            pstmt.setInt(3, FeeCode.TZ_BJ);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    return resultSet.getBigDecimal(1);
                }
            }
        }
        return BigDecimal.ZERO;
    }
    
    /**
     * 查询债权转让详细信息
     * @param connection
     * @param F01
     * @return
     * @throws SQLException
     */
    private ZQZRinfo selectZQZRinfo(Connection connection, int F01)
        throws SQLException
    {
        ZQZRinfo record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT T6251.F04 AS F01, T6507.F03 AS F02, T6230.F01 AS F03, T6260.F02 AS F04, T6230.F25 AS F05, T6260.F01 AS F06, T6251.F11 AS F07 "
                + "FROM S65.T6507 LEFT JOIN S62.T6260 ON T6260.F01 = T6507.F02 LEFT JOIN S62.T6251 ON T6251.F01 = T6260.F02 LEFT JOIN S62.T6230 ON T6230.F01 = T6251.F03 WHERE T6507.F01 = ?"))
        {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new ZQZRinfo();
                    record.zqrId = resultSet.getInt(1);
                    record.srrId = resultSet.getInt(2);
                    record.bidId = resultSet.getInt(3);
                    record.zqzrId = resultSet.getInt(4);
                    record.bidCode = resultSet.getString(5);
                    record.zqsqId = resultSet.getInt(6);
                    record.recordId = resultSet.getInt(7);
                }
            }
        }
        return record;
    }
    
    protected int selectT6273Id(int loanId, int userId, int zqzrId, T6273_F10 t6273_f10, Connection connection)
        throws Throwable
    {
        String sql = "SELECT F01 FROM S62.T6273 WHERE F02 = ? AND F03 = ?";
        sql += " AND F18 = ? AND F10 = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setInt(1, userId);
            ps.setInt(2, loanId);
            ps.setInt(3, zqzrId);
            ps.setString(4, t6273_f10.name());
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }
    
    /**
     * 根据债权转让申请ID判断该用户是否为这个债权的债权持有人
     * @param id 用户Id
     * @return
     */
    private boolean isTheHolder(Connection connection, int id, int zqzrsqId)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT T6251.F01 FROM S62.T6251 WHERE T6251.F01=(SELECT T6260.F02 FROM S62.T6260 WHERE T6260.F01=?) AND T6251.F04=?"))
        {
            pstmt.setInt(1, zqzrsqId);
            pstmt.setInt(2, id);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    return true;
                }
                return false;
            }
        }
    }
    
    /**
     * 根据债权转让申请ID判断该用户是否为这个债权的购买人
     * @param id 用户Id
     * @return
     */
    private boolean isBuyers(Connection connection, int id, int zqzrsqId)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT T6262.F01 FROM S62.T6262 WHERE T6262.F02=? AND T6262.F03=?"))
        {
            pstmt.setInt(1, zqzrsqId);
            pstmt.setInt(2, id);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    return true;
                }
                return false;
            }
        }
    }
    
    private int selectint(Connection connection, int F01)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F02 FROM S62.T6261 WHERE T6261.F01 = ? LIMIT 1"))
        {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    return resultSet.getInt(1);
                }
            }
        }
        return 0;
    }
    
    private Dzxy getDzxy(Connection connection, int zqsqId, int uId)
        throws Throwable
    {
        
        if (uId > 0 && !isTheHolder(connection, uId, zqsqId) && !isBuyers(connection, uId, zqsqId))
        {
            return null;
        }
        Dzxy record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT T5125.F03 AS F01, T5126.F03 AS F02 FROM S51.T5126 LEFT JOIN S51.T5125 ON T5125.F01 = T5126.F01 WHERE T5126.F01 = ? AND T5126.F02 = ? LIMIT 1"))
        {
            pstmt.setInt(1, XyType.ZQZRXY);
            pstmt.setInt(2, selectint(connection, zqsqId));
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new Dzxy();
                    record.xymc = resultSet.getString(1);
                    record.content = resultSet.getString(2);
                }
            }
        }
        return record;
    }
    
    /**
     * 根据债权转让申请ID查债权转让订单ID
     * @param connection
     * @param zqzrId
     * @return
     * @throws SQLException
     */
    private int selectZQZRinfoZqzrId(Connection connection, int zqzrId)
        throws SQLException
    {
        ZQZRinfo record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT  T6501.F01  FROM S65.T6507  LEFT JOIN S65.T6501 ON  T6507.F01=T6501.F01 WHERE T6507.F02 = ? AND T6501.F03='CG' "))
        {
            pstmt.setInt(1, zqzrId);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    return resultSet.getInt(1);
                }
            }
        }
        return 0;
    }
    
    @Override
    public void setZqzrSignaZqzrId(int zqzrId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            int orderId = selectZQZRinfoZqzrId(connection, zqzrId);
            if (orderId > 0)
            {
                this.setZqzrSigna(connection, orderId);
            }
            else
            {
                throw new LogicalException("该债权申请未找订单或订单是失败状态");
            }
        }
    }
    
}
