/*
 * 文 件 名:  ContractManageImpl.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  guomianyun
 * 修改时间:  2017年10月24日
 */
package com.dimeng.p2p.signature.fdd.achieve;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.config.Envionment;
import com.dimeng.framework.service.ServiceResource;
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
import com.dimeng.p2p.S62.entities.T6253;
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
import com.dimeng.p2p.S62.enums.T6236_F04;
import com.dimeng.p2p.S62.enums.T6273_F07;
import com.dimeng.p2p.S62.enums.T6273_F08;
import com.dimeng.p2p.S62.enums.T6273_F10;
import com.dimeng.p2p.S62.enums.T6273_F15;
import com.dimeng.p2p.common.CommonUtils;
import com.dimeng.p2p.common.ResourceProviderUtil;
import com.dimeng.p2p.common.entities.Bdxq;
import com.dimeng.p2p.common.entities.CjrList;
import com.dimeng.p2p.common.entities.Dzxy;
import com.dimeng.p2p.common.entities.DzxyDf;
import com.dimeng.p2p.common.entities.DzxyDy;
import com.dimeng.p2p.common.entities.DzxyHkjh;
import com.dimeng.p2p.signature.fdd.service.FddContractManage;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.Formater;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.DateParser;

/**
 * 法大大合同生成业务管理
 * <功能详细描述>
 * 
 * @author  guomianyun
 * @version  [版本号, 2017年10月24日]
 */
public class FddContractManageImpl extends AbstractFddService implements FddContractManage
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
    private static Integer RECORDNO_COUNT = 3;
    
    public static int RECORDERRORNUM = 0;
    
    private static final Logger logger = Logger.getLogger(FddContractManageImpl.class);
    
    public FddContractManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public Dzxy getBidContent(int loanId)
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
    
    @Override
    public Map<String, Object> getValueMap(int loanId, int userId, String htCode)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            Bdxq bdxq = getBid(connection, loanId);
            if (bdxq == null)
            {
                logger.info("获取标的信息失败");
                throw new NullPointerException("获取标的信息失败！");
            }
            
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
            //判断是否担保标的  ：担保标的使用四方合同模版，信用标、抵押标 使用三方合同模版
            if (bdxq.F11 == T6230_F11.S)
            {
                
                DzxyDy dzxyDy = getDzxyDy(loanId, userId, htCode, connection);
                valueMap.put("xy_no", dzxyDy.xy_no);
                valueMap.put("jklist", dzxyDy.cjrList);
                valueMap.put("yf_loginName", dzxyDy.yf_loginName);
                if (null != dzxyDy.t6110)
                {
                    valueMap.put("yf_userType", dzxyDy.t6110.F06.name());
                    if (dzxyDy.t6110.F06.name().equals(T6110_F06.FZRR.name()))
                    {
                        valueMap.put("user_info", T6110_F06.FZRR);
                        valueMap.put("yf_cardx", StringHelper.isEmpty(dzxyDy.t6161.F03) ? dzxyDy.t6161.F19
                            : dzxyDy.t6161.F03);
                        valueMap.put("yf_realName", dzxyDy.t6161.F04);
                    }
                    if (dzxyDy.t6110.F06.name().equals(T6110_F06.ZRR.name()) && null != dzxyDy.t6141)
                    {
                        valueMap.put("user_info", T6110_F06.ZRR);
                        valueMap.put("yf_cardx", dzxyDy.t6141.F06);
                        valueMap.put("yf_card", dzxyDy.t6141.F07);
                        valueMap.put("yf_realName", dzxyDy.t6141.F02);
                    }
                }
                valueMap.put("bf_companyName", dzxyDy.bf_companyName == null ? "" : dzxyDy.bf_companyName);
                valueMap.put("bf_address", dzxyDy.bf_address == null ? "" : dzxyDy.bf_address);
                valueMap.put("df_companyName", dzxyDy.df_companyName == null ? "" : dzxyDy.df_companyName);
                valueMap.put("df_address", dzxyDy.df_address == null ? "" : dzxyDy.df_address);
                valueMap.put("yq_rate", dzxyDy.yq_rate);
                valueMap.put("wyj_rate", dzxyDy.wyj_rate);
                valueMap.put("jk_jkyt", dzxyDy.t6231.F08);
                valueMap.put("jk_money_xx", dzxyDy.bdxq.F05.subtract(dzxyDy.bdxq.F07));
                valueMap.put("jk_money_dx", dzxyDy.jk_money_dx);
                valueMap.put("jk_rate", Formater.formatRate(dzxyDy.bdxq.F06));
                /*Calendar calendar = Calendar.getInstance();
                calendar.setTime(dzxyDy.t6231.F12);*/
                if (T6231_F21.F == dzxyDy.t6231.F21)
                {
                    valueMap.put("jk_jkqx", dzxyDy.bdxq.F09 + "个月");
                    /*calendar.add(Calendar.MONTH, dzxyDy.bdxq.F09);
                    valueMap.put("jk_dqr", DateParser.format(calendar.getTime(), "yyyy-MM-dd"));*/
                }
                else
                {
                    valueMap.put("jk_jkqx", dzxyDy.t6231.F22 + "天");
                    /* calendar.add(Calendar.DAY_OF_MONTH, dzxyDy.t6231.F22);
                     valueMap.put("jk_dqr", DateParser.format(calendar.getTime(), "yyyy-MM-dd"));*/
                }
                valueMap.put("jk_hkqs", dzxyDy.t6231.F02);
                valueMap.put("jk_hkr", dzxyDy.jk_hkr);
                valueMap.put("jk_ksr", DateParser.format(dzxyDy.t6231.F12, "yyyy-MM-dd"));
                valueMap.put("hklist", dzxyDy.hkjh);
                valueMap.put("site_name", configureProvider.format(SystemVariable.SITE_NAME));
                valueMap.put("site_domain", configureProvider.format(SystemVariable.SITE_DOMAIN));
            }
            else
            {
                DzxyDy dzxyDy = getDzxyXy(loanId, userId, htCode, connection);
                valueMap.put("xy_no", dzxyDy.xy_no);
                valueMap.put("jklist", dzxyDy.cjrList);
                valueMap.put("yf_loginName", dzxyDy.yf_loginName);
                if (null != dzxyDy.t6110)
                {
                    valueMap.put("yf_userType", dzxyDy.t6110.F06.name());
                    if (dzxyDy.t6110.F06.name().equals(T6110_F06.FZRR.name()))
                    {
                        valueMap.put("user_info", T6110_F06.FZRR);
                        valueMap.put("yf_cardx", StringHelper.isEmpty(dzxyDy.t6161.F03) ? dzxyDy.t6161.F19
                            : dzxyDy.t6161.F03);
                        valueMap.put("yf_realName", dzxyDy.t6161.F04);
                    }
                    if (dzxyDy.t6110.F06.name().equals(T6110_F06.ZRR.name()) && null != dzxyDy.t6141)
                    {
                        valueMap.put("user_info", T6110_F06.ZRR);
                        valueMap.put("yf_cardx", dzxyDy.t6141.F06);
                        valueMap.put("yf_card", dzxyDy.t6141.F07);
                        valueMap.put("yf_realName", dzxyDy.t6141.F02);
                    }
                }
                valueMap.put("bf_companyName", dzxyDy.bf_companyName == null ? "" : dzxyDy.bf_companyName);
                valueMap.put("bf_address", dzxyDy.bf_address == null ? "" : dzxyDy.bf_address);
                valueMap.put("yq_rate", dzxyDy.yq_rate);
                valueMap.put("wyj_rate", dzxyDy.wyj_rate);
                valueMap.put("jk_jkyt", dzxyDy.t6231.F08);
                valueMap.put("jk_money_xx", dzxyDy.bdxq.F05.subtract(dzxyDy.bdxq.F07));
                valueMap.put("jk_money_dx", dzxyDy.jk_money_dx);
                valueMap.put("jk_rate", Formater.formatRate(dzxyDy.bdxq.F06));
                /* Calendar calendar = Calendar.getInstance();
                 calendar.setTime(dzxyDy.t6231.F12);*/
                if (T6231_F21.F == dzxyDy.t6231.F21)
                {
                    valueMap.put("jk_jkqx", dzxyDy.bdxq.F09 + "个月");
                    /* calendar.add(Calendar.MONTH, dzxyDy.bdxq.F09);
                     valueMap.put("jk_dqr", DateParser.format(calendar.getTime(), "yyyy-MM-dd"));*/
                }
                else
                {
                    valueMap.put("jk_jkqx", dzxyDy.t6231.F22 + "天");
                    /*calendar.add(Calendar.DAY_OF_MONTH, dzxyDy.t6231.F22);
                    valueMap.put("jk_dqr", DateParser.format(calendar.getTime(), "yyyy-MM-dd"));*/
                }
                valueMap.put("jk_hkqs", dzxyDy.t6231.F02);
                valueMap.put("jk_hkr", dzxyDy.jk_hkr);
                valueMap.put("jk_ksr", DateParser.format(dzxyDy.t6231.F12, "yyyy-MM-dd"));
                valueMap.put("hklist", dzxyDy.hkjh);
                valueMap.put("site_name", configureProvider.format(SystemVariable.SITE_NAME));
                valueMap.put("site_domain", configureProvider.format(SystemVariable.SITE_DOMAIN));
            }
            return valueMap;
        }
    }
    
    @Override
    public void updateT6273PdfPathNo(T6273 t6273)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            String sql = "UPDATE S62.T6273 SET F04 = ?,F09 = ?, F20 = ? WHERE F01 = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql))
            {
                pstmt.setString(1, t6273.F04);
                pstmt.setString(2, t6273.F09);
                pstmt.setString(3, t6273.F04 + T6273_F08.JKHT.getChineseName());
                pstmt.setInt(4, t6273.F01);
                pstmt.execute();
            }
        }
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
    
    /**
     * 担保标
     * @param loanId
     * @param tzUserId
     * @param connection
     * @return
     * @throws Throwable
     */
    public DzxyDy getDzxyDy(int loanId, int tzUserId, String htCode, Connection connection)
        throws Throwable
    {
        
        DzxyDy dzxyDy = new DzxyDy();
        dzxyDy.bdxq = getBid(connection, loanId);
        int jkrId = dzxyDy.bdxq.F02;
        dzxyDy.t6231 = getExtra(connection, loanId);
        dzxyDy.t6110 = getUserInfo(connection, jkrId);
        dzxyDy.t6161 = selectT6161(connection, jkrId);
        dzxyDy.t6141 = selectT6141(connection, jkrId);
        if (tzUserId == 0)
        {
            tzUserId = jkrId;
        }
        dzxyDy.cjrList = searchCjrList(connection, loanId, dzxyDy.bdxq.F09, jkrId, tzUserId);
        
        //String htCode = getHtCode( connection, loanId, dzxyDy.bdxq.F25);
        dzxyDy.xy_no = htCode;
        dzxyDy.yf_loginName = dzxyDy.t6110.F02;
        dzxyDy.hkjh = getHk(connection, loanId);
        dzxyDy.jk_money_dx = CommonUtils.amountConvertCN(dzxyDy.bdxq.F05.subtract(dzxyDy.bdxq.F07));
        dzxyDy.wyj_rate = new BigDecimal(configureProvider.getProperty(SystemVariable.WYJ_RATE));
        dzxyDy.yq_rate = getYqRate(connection, loanId);
        dzxyDy.df_companyName = configureProvider.getProperty(SystemVariable.COMPANY_NAME);
        dzxyDy.df_address = configureProvider.getProperty(SystemVariable.COMPANY_ADDRESS);
        
        //还款日
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT DAY(?) "))
        {
            pstmt.setDate(1, dzxyDy.t6231.F06);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    dzxyDy.jk_hkr = resultSet.getInt(1);
                }
            }
        }
        //担保公司
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT T6161.F04 AS F01, T6164.F03 AS F02 FROM S62.T6236 INNER JOIN S61.T6161 ON T6236.F03 = T6161.F01 INNER JOIN S61.T6164 ON T6161.F01 = T6164.F01 WHERE T6236.F02 = ? AND T6236.F04 = ? LIMIT 1"))
        {
            pstmt.setInt(1, loanId);
            pstmt.setString(2, T6236_F04.S.name());
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    dzxyDy.bf_companyName = resultSet.getString(1);
                    dzxyDy.bf_address = resultSet.getString(2);
                }
            }
        }
        
        return dzxyDy;
    }
    
    /**
     * 信用标
     * @param loanId
     * @param tzUserId
     * @param connection
     * @return
     * @throws Throwable
     */
    public DzxyDy getDzxyXy(int loanId, int tzUserId, String htCode, Connection connection)
        throws Throwable
    {
        
        DzxyDy dzxyDy = new DzxyDy();
        dzxyDy.bdxq = getBid(connection, loanId);
        int jkrId = dzxyDy.bdxq.F02;
        dzxyDy.t6231 = getExtra(connection, loanId);
        dzxyDy.t6110 = getUserInfo(connection, jkrId);
        dzxyDy.t6141 = selectT6141(connection, jkrId);
        dzxyDy.t6161 = selectT6161(connection, jkrId);
        if (tzUserId == 0)
        {
            tzUserId = jkrId;
        }
        dzxyDy.cjrList = searchCjrList(connection, loanId, dzxyDy.bdxq.F09, jkrId, tzUserId);
        
        //String htCode = getHtCode( connection, loanId, dzxyDy.bdxq.F25);
        System.out.println("htCode = " + htCode);
        dzxyDy.xy_no = htCode;
        dzxyDy.yf_loginName = dzxyDy.t6110.F02;
        dzxyDy.hkjh = getHk(connection, loanId);
        dzxyDy.jk_money_dx = CommonUtils.amountConvertCN(dzxyDy.bdxq.F05.subtract(dzxyDy.bdxq.F07));
        dzxyDy.wyj_rate = new BigDecimal(configureProvider.getProperty(SystemVariable.WYJ_RATE));
        dzxyDy.yq_rate = getYqRate(connection, loanId);
        dzxyDy.bf_companyName = configureProvider.getProperty(SystemVariable.COMPANY_NAME);
        dzxyDy.bf_address = configureProvider.getProperty(SystemVariable.COMPANY_ADDRESS);
        
        //还款日
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT DAY(?) "))
        {
            pstmt.setDate(1, dzxyDy.t6231.F06);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    dzxyDy.jk_hkr = resultSet.getInt(1);
                }
            }
        }
        return dzxyDy;
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
     * 获取标的垫付合同数
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
            connection.prepareStatement("SELECT COUNT(*) FROM S62.T6273 WHERE F03=? AND F08=? LIMIT 1"))
        {
            pstmt.setInt(1, loanId);
            pstmt.setString(2, T6273_F08.DFHT.name());
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
        else
        {
            num = num + 1;
        }
        return num;
    }
    
    /**
     * 生成合同编码
     * @param connection
     * @param loanId
     * @param bidCode
     * @return
     * @throws Throwable
     */
    private String getDfHtCode(Connection connection, int loanId, String bidCode)
        throws Throwable
    {
        //      YDYL--2016-标的编号-DF-001 年份后的为标的数量递增，后三位为投资人数递增
        //  年份后的数量值四位，最大就是9999，对P2P项目估计数量值合适，对发消费标类型不合适，需重新讨论格式
        StringBuffer htCode = new StringBuffer();
        
        htCode.append(StringHelper.isEmpty(configureProvider.getProperty(SystemVariable.SIGNATURE_CONTRACT_PTNAME)) ? HT_HEADER
            : configureProvider.getProperty(SystemVariable.SIGNATURE_CONTRACT_PTNAME));
        htCode.append(TEXT_LINE);
        Calendar ca = Calendar.getInstance();
        htCode.append(ca.get(Calendar.YEAR));
        //或者当前标的数字编号
        /*BID_NO = getBidNo(connection, loanId);
        String bidNo = BID_NO.toString();
        for (int i = 0; i < BIDNO_COUNT - bidNo.trim().length(); i++)
        {
            htCode.append("0");
        }
        htCode.append(bidNo);*/
        htCode.append(TEXT_LINE);
        htCode.append(bidCode);
        
        htCode.append(TEXT_LINE);
        
        htCode.append("DF");
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
    
    protected int selectT6273Id(int loanId, int userId, int recordId, Connection connection)
        throws Throwable
    {
        String sql = "SELECT F01 FROM S62.T6273 WHERE F02 = ? AND F03 = ?";
        if (recordId != 0)
        {
            sql += " AND F14 = ?";
        }
        else
        {
            sql += " AND F10 = ?";
        }
        try (PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setInt(1, userId);
            ps.setInt(2, loanId);
            if (recordId != 0)
            {
                ps.setInt(3, recordId);
            }
            else
            {
                ps.setString(3, T6273_F10.JKR.name());
            }
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
     * 还款计划
     * @param id
     * @return
     * @throws Throwable
     */
    private DzxyHkjh[] getHk(Connection connection, int id)
        throws Throwable
    {
        ArrayList<DzxyHkjh> list = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT SUM(F07), F08,  ( SELECT T5122.F02 FROM S51.T5122 WHERE T6252.F05 = T5122.F01 ) FROM S62.T6252 WHERE T6252.F02 = ? AND T6252.F05 IN (?,?) GROUP BY T6252.F08,F05"))
        {
            pstmt.setInt(1, id);
            pstmt.setInt(2, FeeCode.TZ_BJ);
            pstmt.setInt(3, FeeCode.TZ_LX);
            //pstmt.setString(2, T6252_F09.WH.name());
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                while (resultSet.next())
                {
                    DzxyHkjh record = new DzxyHkjh();
                    record.hkje = resultSet.getBigDecimal(1);
                    record.hkrq = Formater.formatDate(resultSet.getDate(2));
                    record.hklx = resultSet.getString(3);
                    if (list == null)
                    {
                        list = new ArrayList<>();
                    }
                    list.add(record);
                }
            }
        }
        return ((list == null || list.size() == 0) ? null : list.toArray(new DzxyHkjh[list.size()]));
    }
    
    /**
     * 获取指定标的逾期罚息率
     * @param connection
     * @param F01
     * @return
     * @throws SQLException
     */
    private BigDecimal getYqRate(Connection connection, int F01)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F04 FROM S62.T6238 WHERE T6238.F01 = ? LIMIT 1"))
        {
            pstmt.setInt(1, F01);
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
     * 根据借款标ID查询出借人信息
     * @param loanId
     * @return
     * @throws Throwable
     */
    private List<CjrList> searchCjrList(Connection connection, int loanId, int jkqx, int jkUserId, int tzUserId)
        throws Throwable
    {
        String jkqxs = jkqx + "个月";
        T6231 t6231 = getExtra(connection, loanId);
        if (t6231.F21 == T6231_F21.S)
        {
            jkqxs = t6231.F22 + "天";
        }
        List<CjrList> list = null;
        //int loginId = serviceResource.getSession().getAccountId();
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT T6250.F04,T6250.F03,T6250.F01 FROM S62.T6250 WHERE T6250.F02 = ?"))
        {
            pstmt.setInt(1, loanId);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                while (resultSet.next())
                {
                    CjrList record = new CjrList();
                    record.cjje = resultSet.getBigDecimal(1);
                    record.jkqx = jkqxs;
                    int userId = resultSet.getInt(2);
                    T6110 t6110 = getUserInfo(connection, resultSet.getInt(2));
                    record.loginName = t6110.F02;
                    record.userType = t6110.F06.name();
                    record.qbys = getQbys(connection, resultSet.getInt(3));
                    T6141 t6141 = selectT6141(connection, userId);
                    record.card = t6141.F07;
                    if (userId == tzUserId || jkUserId == tzUserId)
                    {
                        record.realName = t6141.F02;
                        record.realCard = t6141.F07;
                    }
                    else
                    {
                        record.realName = t6141.F02.substring(0, 1) + "**";
                        record.card = t6141.F06;
                    }
                    T6161 t6161 = selectT6161(connection, userId);
                    if (userId == tzUserId || jkUserId == tzUserId)
                    {
                        record.loginName = t6110.F02;
                        record.userType = t6110.F06.name();
                        if (T6110_F06.FZRR.equals(t6110.F06))
                        {
                            record.companyName = t6161.F04;
                            record.papersNum = t6161.F20.equals("Y") ? t6161.F19 : t6161.F03;
                        }
                    }
                    else
                    {
                        record.loginName = t6110.F02.substring(0, 4) + "***";
                        record.userType = t6110.F06.name();
                        if (T6110_F06.FZRR.equals(t6110.F06))
                        {
                            record.companyName =
                                t6161.F04.substring(0, 2) + "***"
                                    + t6161.F04.substring(t6161.F04.length() - 2, t6161.F04.length());
                            record.papersNum =
                                t6161.F20.equals("Y") ? t6161.F19.substring(0, 4) + "***" : t6161.F03.substring(0, 4)
                                    + "***";
                        }
                    }
                    if (list == null)
                    {
                        list = new ArrayList<>();
                    }
                    list.add(record);
                }
            }
        }
        return list;
    }
    
    /**
     * 根据投资ID查询所有本息
     * @param loanId
     * @param loginId
     * @return
     * @throws Throwable
     */
    private BigDecimal getQbys(Connection connection, int tbId)
        throws Throwable
    {
        BigDecimal qbys = new BigDecimal(0);
        try (PreparedStatement ps =
            connection.prepareStatement("SELECT IFNULL(SUM(F07),0)  FROM S62.T6252 WHERE F11 IN "
                + "(SELECT T6251.F01 AS F01 FROM S62.T6251 INNER JOIN S62.T6250 ON T6251.F11 = T6250.F01 WHERE T6250.F01 = ?) AND F05 IN (?,?)");)
        {
            ps.setInt(1, tbId);
            ps.setInt(2, FeeCode.TZ_BJ);
            ps.setInt(3, FeeCode.TZ_LX);
            try (ResultSet rs = ps.executeQuery();)
            {
                if (rs.next())
                {
                    qbys = rs.getBigDecimal(1);
                }
            }
        }
        return qbys;
    }
    
    @Override
    public void insertT6273DF(int bidId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            T6253 t6253 = this.selectT6253(connection, bidId);
            T6230 t6230 = this.selectT6230(connection, bidId);
            if (null != t6253)
            {
                //生成电子签章
                //垫付人
                T6273 t6273_dfr = new T6273();
                t6273_dfr.F02 = t6253.F03;
                t6273_dfr.F03 = bidId;
                t6273_dfr.F07 = T6273_F07.DQ;
                t6273_dfr.F08 = T6273_F08.DFHT;
                try (PreparedStatement pstmt =
                    connection.prepareStatement("INSERT INTO S62.T6273 SET F02 = ?, F03 = ?,F04=?, F07 = ?, F08 = ?, F10 = ?, F13 = ?, F18 = ?, F15 = ?"))
                {
                    pstmt.setInt(1, t6273_dfr.F02);
                    pstmt.setInt(2, t6273_dfr.F03);
                    pstmt.setString(3, this.getDfHtCode(connection, bidId, t6230.F25));
                    pstmt.setString(4, T6273_F07.DQ.name());
                    pstmt.setString(5, T6273_F08.DFHT.name());
                    pstmt.setString(6, T6273_F10.DFR.name());
                    pstmt.setTimestamp(7, getCurrentTimestamp(connection));
                    pstmt.setInt(8, t6253.F01); //垫付ID
                    pstmt.setString(9, T6273_F15.DSQ.name());
                    pstmt.execute();
                }
                
                //生成电子签章
                //被垫付人
                try (PreparedStatement pstmt =
                    connection.prepareStatement("INSERT INTO S62.T6273 SET F02 = ?, F03 = ?,F04=?, F07 = ?, F08 = ?, F10 = ?, F13 = ?, F18 = ?, F15 = ?"))
                {
                    pstmt.setInt(1, t6253.F04);
                    pstmt.setInt(2, bidId);
                    pstmt.setString(3, this.getDfHtCode(connection, bidId, t6230.F25));
                    pstmt.setString(4, T6273_F07.DQ.name());
                    pstmt.setString(5, T6273_F08.DFHT.name());
                    pstmt.setString(6, T6273_F10.BDFR.name());
                    pstmt.setTimestamp(7, getCurrentTimestamp(connection));
                    pstmt.setInt(8, t6253.F01); //垫付ID
                    pstmt.setString(9, T6273_F15.DSQ.name());
                    pstmt.execute();
                }
                /*  t6273_zqr.F01 = selectT6273Id(info.bidId, info.srrId, info.zqzrId, T6273_F10.SRR, connection);
                  doHtmlSavePdf(t6273_zqr, connection, orderId, info.zqsqId);
                  */
                RECORDERRORNUM = 0;
            }
        }
    }
    
    /**
     * 根据标的查询垫付记录
     * <功能详细描述>
     * @param connection
     * @param F02
     * @return
     * @throws SQLException
     */
    protected T6253 selectT6253(Connection connection, int F02)
        throws SQLException
    {
        T6253 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07 FROM S62.T6253 WHERE T6253.F02 = ? LIMIT 1 "))
        {
            pstmt.setInt(1, F02);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6253();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getInt(3);
                    record.F04 = resultSet.getInt(4);
                    record.F05 = resultSet.getBigDecimal(5);
                    record.F06 = resultSet.getBigDecimal(6);
                    record.F07 = resultSet.getTimestamp(7);
                }
            }
        }
        return record;
    }
    
    //垫付合同信息
    @Override
    public Map<String, Object> getAdvanceContentMap(int bId, int uId, String htCode)
        throws Throwable
    {
        Dzxy dzxy = getAdvanceDzxy(bId, uId);
        if (null == dzxy)
        {
            logger.info("垫付标的：" + bId + "时，获取到垫付合同模板为空");
            return null;
        }
        DzxyDf dzxyDf = getDzxyDf(bId, htCode);
        ConfigureProvider configureProvider =
            ResourceProviderUtil.getResourceProvider().getResource(ConfigureProvider.class);
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
        valueMap.put("xy_no", dzxyDf.xy_no);
        valueMap.put("jf_yhlx", dzxyDf.t6110.F06.name());
        valueMap.put("jf_zsxm", dzxyDf.t6141.F02 == null ? "" : dzxyDf.t6141.F02);
        valueMap.put("jf_sfzh", dzxyDf.t6141.F07 == null ? "" : dzxyDf.t6141.F07);
        
        if (dzxyDf.t6161 != null)
        {
            valueMap.put("jf_companyName", dzxyDf.t6161.F04 == null ? "" : dzxyDf.t6161.F04);
            valueMap.put("jf_papersNum", dzxyDf.t6161.F20.equals("Y") ? dzxyDf.t6161.F19 : dzxyDf.t6161.F03);
        }
        valueMap.put("jf_yhm", dzxyDf.t6110.F02);
        valueMap.put("jf_dfje", Formater.formatAmount(dzxyDf.t6253.F05));
        //垫付金额中文
        valueMap.put("jf_dfje_CN", CommonUtils.amountConvertCN(dzxyDf.t6253.F05));
        
        valueMap.put("yf_yhlx", dzxyDf.bdft6110.F06.name());
        valueMap.put("yf_zsxm", dzxyDf.bdft6141.F02 == null ? "" : dzxyDf.bdft6141.F02);
        valueMap.put("yf_sfzh", dzxyDf.bdft6141.F07 == null ? "" : dzxyDf.bdft6141.F07);
        valueMap.put("yf_yhm", dzxyDf.bdft6110.F02);
        if (dzxyDf.bdft6161 != null)
        {
            valueMap.put("yf_companyName", dzxyDf.bdft6161.F04 == null ? "" : dzxyDf.bdft6161.F04);
            valueMap.put("yf_papersNum", dzxyDf.bdft6161.F20.equals("Y") ? dzxyDf.bdft6161.F19 : dzxyDf.bdft6161.F03);
        }
        valueMap.put("yf_yhm", dzxyDf.bdft6110.F02);
        
        valueMap.put("bid_title", dzxyDf.bdxq.F03);
        valueMap.put("df_date", dzxyDf.t6253.F07);
        valueMap.put("site_name", configureProvider.format(SystemVariable.SITE_NAME));
        valueMap.put("site_domain", configureProvider.format(SystemVariable.SITE_DOMAIN));
        valueMap.put("company_name", configureProvider.format(SystemVariable.COMPANY_NAME));
        return valueMap;
    }
    
    private Dzxy getAdvanceDzxy(int bId, int uId)
        throws Throwable
    {
        
        try (Connection connection = getConnection())
        {
            if (!isPayment(connection, bId))
            {
                return null;
            }
            Dzxy record = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT T5125.F03 AS F01, T5126.F03 AS F02 FROM S51.T5126 LEFT JOIN S51.T5125 ON T5125.F01 = T5126.F01 WHERE T5126.F01 = ? AND T5126.F02 = (SELECT F02 FROM S62.T6256 WHERE T6256.F01 = ?) ORDER BY T5126.F04 DESC LIMIT 1"))
            {
                pstmt.setInt(1, XyType.DFXY);
                pstmt.setInt(2, bId);
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
    }
    
    /**
     * 获取垫付协议内容信息
     * <功能详细描述>
     * @param bId
     * @return
     * @throws Throwable
     */
    private DzxyDf getDzxyDf(int bId, String htCode)
        throws Throwable
    {
        DzxyDf dzxyDf = new DzxyDf();
        try (Connection connection = getConnection())
        {
            dzxyDf.xy_no = htCode;
            dzxyDf.t6253 = selectT6253(connection, bId);
            dzxyDf.bdxq = getBid(connection, dzxyDf.t6253.F02);
            dzxyDf.t6110 = getUserInfo(connection, dzxyDf.t6253.F03);
            dzxyDf.t6141 = selectT6141(connection, dzxyDf.t6253.F03);
            dzxyDf.t6161 = selectT6161(connection, dzxyDf.t6253.F03);
            dzxyDf.bdft6110 = getUserInfo(connection, dzxyDf.t6253.F04);
            dzxyDf.bdft6141 = selectT6141(connection, dzxyDf.t6253.F04);
            dzxyDf.bdft6161 = selectT6161(connection, dzxyDf.t6253.F04);
        }
        return dzxyDf;
    }
    
    /**
     * 根据标的ID判断该用户是否垫付
     * @param id 用户Id
     * @param bid 标的ID T6230.F01
     * @return
     */
    private boolean isPayment(Connection connection, int bid)
        throws Throwable
    {
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT T6253.F01 FROM S62.T6253 WHERE T6253.F02=?"))
        {
            pstmt.setInt(1, bid);
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
    
    @Override
    public List<T6273> getDfList(int bidId)
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
                pstmt.setString(2, T6273_F08.DFHT.name());
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
