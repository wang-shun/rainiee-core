/*
 * 文 件 名:  ContractManageImpl.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoucl
 * 修改时间:  2016年6月29日
 */
package com.dimeng.p2p.service;

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
import com.dimeng.p2p.S62.entities.T6251;
import com.dimeng.p2p.S62.entities.T6253;
import com.dimeng.p2p.S62.entities.T6260;
import com.dimeng.p2p.S62.entities.T6262;
import com.dimeng.p2p.S62.entities.T6264;
import com.dimeng.p2p.S62.entities.T6265;
import com.dimeng.p2p.S62.entities.T6271;
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
import com.dimeng.p2p.S62.enums.T6251_F08;
import com.dimeng.p2p.S62.enums.T6260_F07;
import com.dimeng.p2p.S62.enums.T6264_F04;
import com.dimeng.p2p.S62.enums.T6271_F07;
import com.dimeng.p2p.S62.enums.T6271_F08;
import com.dimeng.p2p.S62.enums.T6271_F10;
import com.dimeng.p2p.common.ResourceProviderUtil;
import com.dimeng.p2p.common.entities.Bdxq;
import com.dimeng.p2p.common.entities.CjrList;
import com.dimeng.p2p.common.entities.Dzxy;
import com.dimeng.p2p.common.entities.DzxyBlzqzr;
import com.dimeng.p2p.common.entities.DzxyDf;
import com.dimeng.p2p.common.entities.DzxyDy;
import com.dimeng.p2p.common.entities.DzxyHkjh;
import com.dimeng.p2p.common.entities.DzxyZqzr;
import com.dimeng.p2p.common.entities.DzxyZrr;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.Formater;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.DateParser;
import com.dimeng.util.parser.EnumParser;
import com.dimeng.util.parser.TimestampParser;

/**
 * <合同>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2016年6月29日]
 */
public class ContractManageImpl extends AbstractP2PService implements ContractManage
{
    
    public ContractManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public Map<String, Object> getClaimContentMap(int zqsqId, int uId)
        throws Throwable
    {
        Dzxy dzxy = null;
        DzxyZqzr dzxyZqzr = null;
        try (Connection connection = getConnection())
        {
            dzxy = getDzxy(connection, zqsqId, uId);
            if (null == dzxy)
            {
                logger.info("转让债权：" + zqsqId + "时，获取债权转让合同模板为空");
                return null;
            }
            dzxyZqzr = getDzxyZqzr(connection, zqsqId);
        }
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
    private DzxyDf getDzxyDf(int bId)
        throws Throwable
    {
        DzxyDf dzxyDf = new DzxyDf();
        try (Connection connection = getConnection())
        {
            dzxyDf.xy_no = "df-" + bId;
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
     * 垫付记录
     * <功能详细描述>
     * @param connection 
     * @param bid  T6253.F02即T6230的F01
     * @return
     * @throws Throwable
     */
    private T6253 selectT6253(Connection connection, int bid)
        throws Throwable
    {
        T6253 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11 FROM S62.T6253 WHERE T6253.F02 = ? LIMIT 1"))
        {
            pstmt.setInt(1, bid);
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
                    record.F08 = resultSet.getInt(8);
                    record.F09 = resultSet.getString(9);
                    record.F10 = resultSet.getString(10);
                    record.F11 = resultSet.getInt(11);
                }
            }
        }
        return record;
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
    
    private DzxyZqzr getDzxyZqzr(Connection connection, int zqzrjlId)
        throws Throwable
    {
        DzxyZqzr dzxyZqzr = new DzxyZqzr();
        dzxyZqzr.xy_no = "zqzr-" + zqzrjlId;
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
    
    private Bdxq getBid(int id)
        throws Throwable
    {
        Bdxq bdxq = null;
        try (Connection connection = getConnection())
        {
            bdxq = getBid(connection, id);
        }
        return bdxq;
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
                    record.F21 = EnumParser.parse(T6231_F21.class, resultSet.getString(17));
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
        return new BigDecimal(0);
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
    
    @Override
    public List<T6271> getClaimList(int zcbId)
        throws Throwable
    {
        List<T6271> list = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pst =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11, F12, F13 FROM S62.T6271 WHERE T6271.F13 = ?"))
            {
                pst.setInt(1, zcbId);
                try (ResultSet resultSet = pst.executeQuery())
                {
                    list = resultSetToT6271(resultSet);
                }
            }
            return list;
        }
    }
    
    @Override
    public void updateT6271PdfPathNo(T6271 t6271)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            String sql = "UPDATE S62.T6271 SET F04 = ?,F09 =? WHERE F01 =? ";
            try (PreparedStatement pstmt = connection.prepareStatement(sql))
            {
                pstmt.setString(1, t6271.F04);
                pstmt.setString(2, t6271.F09);
                pstmt.setInt(3, t6271.F01);
                pstmt.execute();
            }
        }
    }
    
    @Override
    public Map<String, Object> getAdvanceContentMap(int bId, int uId)
        throws Throwable
    {
        Dzxy dzxy = getAdvanceDzxy(bId, uId);
        if (null == dzxy)
        {
            logger.info("垫付标的：" + bId + "时，获取到垫付合同模板为空");
            return null;
        }
        DzxyDf dzxyDf = getDzxyDf(bId);
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
        valueMap.put("jf_dfje_CN", amountConvertCN(dzxyDf.t6253.F05));
        
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
    
    @Override
    public Map<String, Object> getValueMap(int loanId, int userId)
        throws Throwable
    {
        Bdxq bdxq = getBid(loanId);
        if (bdxq == null)
        {
            logger.info("获取标的信息失败");
            throw new NullPointerException("获取标的信息失败！");
        }
        
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
        //判断是否担保标的  ：担保标的使用四方合同模版，信用标、抵押标 使用三方合同模版
        if (bdxq.F11 == T6230_F11.S)
        {
            DzxyDy dzxyDy = getDzxyDy(loanId, userId);
            valueMap.put("xy_no", dzxyDy.xy_no);
            valueMap.put("jklist", dzxyDy.cjrList);
            valueMap.put("yf_loginName", dzxyDy.yf_loginName);
            if (null != dzxyDy.t6110)
            {
                valueMap.put("yf_userType", dzxyDy.t6110.F06.name());
                if (null != dzxyDy.t6141)
                {
                    valueMap.put("yf_cardx", dzxyDy.t6141.F07);
                    valueMap.put("yf_card", dzxyDy.t6141.F07);
                    valueMap.put("yf_realName", dzxyDy.t6141.F02);
                }
                if (null != dzxyDy.t6161)
                {
                    valueMap.put("yf_papersNum", dzxyDy.t6161.F20.equals("Y") ? dzxyDy.t6161.F19 : dzxyDy.t6161.F03);
                    valueMap.put("yf_companyName", dzxyDy.t6161.F04);
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
            DzxyDy dzxyDy = getDzxyXy(loanId, userId);
            valueMap.put("xy_no", dzxyDy.xy_no);
            valueMap.put("jklist", dzxyDy.cjrList);
            valueMap.put("yf_loginName", dzxyDy.yf_loginName);
            if (null != dzxyDy.t6110)
            {
                valueMap.put("yf_userType", dzxyDy.t6110.F06.name());
                if (null != dzxyDy.t6141)
                {
                    valueMap.put("yf_cardx", dzxyDy.t6141.F07);
                    valueMap.put("yf_card", dzxyDy.t6141.F07);
                    valueMap.put("yf_realName", dzxyDy.t6141.F02);
                }
                if (null != dzxyDy.t6161)
                {
                    valueMap.put("yf_papersNum", dzxyDy.t6161.F20.equals("Y") ? dzxyDy.t6161.F19 : dzxyDy.t6161.F03);
                    valueMap.put("yf_companyName", dzxyDy.t6161.F04);
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
    public DzxyDy getDzxyDy(int loanId, int tzUserId)
        throws Throwable
    {
        ConfigureProvider config = serviceResource.getResource(ConfigureProvider.class);
        try (Connection connection = getConnection())
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
            dzxyDy.xy_no = "dy-" + loanId;
            dzxyDy.yf_loginName = dzxyDy.t6110.F02;
            dzxyDy.hkjh = getHk(connection, loanId);
            dzxyDy.jk_money_dx = valuesToString(dzxyDy.bdxq.F05.subtract(dzxyDy.bdxq.F07));
            dzxyDy.wyj_rate = new BigDecimal(config.getProperty(SystemVariable.WYJ_RATE));
            dzxyDy.yq_rate = getYqRate(connection, loanId);
            dzxyDy.df_companyName = config.getProperty(SystemVariable.COMPANY_NAME);
            dzxyDy.df_address = config.getProperty(SystemVariable.COMPANY_ADDRESS);
            
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
    }
    
    @Override
    public DzxyDy getDzxyXy(int loanId, int tzUserId)
        throws Throwable
    {
        ConfigureProvider config = serviceResource.getResource(ConfigureProvider.class);
        try (Connection connection = getConnection())
        {
            DzxyDy dzxyDy = new DzxyDy();
            dzxyDy.bdxq = getBid(loanId);
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
            dzxyDy.xy_no = "xy-" + loanId;
            dzxyDy.yf_loginName = dzxyDy.t6110.F02;
            dzxyDy.hkjh = getHk(connection, loanId);
            dzxyDy.jk_money_dx = valuesToString(dzxyDy.bdxq.F05.subtract(dzxyDy.bdxq.F07));
            dzxyDy.wyj_rate = new BigDecimal(config.getProperty(SystemVariable.WYJ_RATE));
            dzxyDy.yq_rate = getYqRate(connection, loanId);
            dzxyDy.bf_companyName = config.getProperty(SystemVariable.COMPANY_NAME);
            dzxyDy.bf_address = config.getProperty(SystemVariable.COMPANY_ADDRESS);
            
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
     * 将金额数字转化成大写字符串
     * @param d
     * @return
     */
    public static String valuesToString(BigDecimal d)
    {
        String returnString = "";
        //保存数字大写数组
        String[] num = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};
        //十进制单位
        String[] unit = {"", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟"};
        //拆分int,double,long，等类型的传值
        //        String[] moneys = d.toString().split("\\.");
        String[] moneys = String.valueOf(d.setScale(2, BigDecimal.ROUND_HALF_UP)).split("\\.");
        //       System.out.println(moneys[0]);
        //将整数部分转化成字节
        char[] moneys1 = moneys[0].toCharArray();
        //new一个新数组用来保存金额每个数字对应的大写
        String[] str = new String[moneys1.length];
        //遍历字节数组找到每个数字的大写并保存
        for (int i = 0; i < moneys1.length; i++)
        {
            //保存数字大写
            str[i] = num[Integer.parseInt(String.valueOf(moneys1[i]))];
            if (!str[i].equals("零"))
            {
                if (i != 0 && !str[i - 1].equals("零"))
                {
                    //前一位数字非零又非第一位数字，则在对应的大写数组和单位数字直接去值组合
                    returnString = returnString + str[i] + unit[moneys1.length - i - 1];
                }
                else if (i != 0 && str[i - 1].equals("零") && (moneys1.length - i != 4 && moneys1.length - i != 8))
                {
                    //非第一位数字，但是前一位为零，并且满足不是在千*字倍上的则需要在前加上 "零“
                    returnString = returnString + "零" + str[i] + unit[moneys1.length - i - 1];
                }
                else if (i != 0 && str[i - 1].equals("零") && (moneys1.length - i == 4 || moneys1.length - i == 8))
                {
                    //非第一位数字，但是前一位为零，并且满足是在千*字倍上的则在对应的大写数组和单位数字直接去值组合
                    if (moneys1.length - i == 4 && returnString.endsWith("亿"))
                    {
                        //万位至亿位上全位零的时候，千位前加上"零"
                        returnString = returnString + "零" + str[i] + unit[moneys1.length - i - 1];
                    }
                    else
                    {
                        returnString = returnString + str[i] + unit[moneys1.length - i - 1];
                    }
                }
                else if (i == 0)
                {
                    //第一位数字不会出现为0，直接在对应的大写数组和单位数字直接去值组合
                    returnString = returnString + str[i] + unit[moneys1.length - i - 1];
                }
            }
            //在万位或者亿位上加上相应的万或亿单位
            if (moneys1.length > 4)
            {
                if (moneys1.length - i == 5)
                {
                    //万位加单位，如果万至亿之间没有数字则不需要加
                    if (!returnString.endsWith("亿"))
                    {
                        if (!returnString.endsWith("万"))
                        {
                            //万位上有数字不为零也不需要加
                            returnString = returnString + "万";
                        }
                    }
                }
                else if (moneys1.length - i == 9)
                {
                    //亿位加单位，如果亿位上数字不为零则不需要加
                    if (!returnString.endsWith("亿"))
                    {
                        returnString = returnString + "亿";
                    }
                }
            }
        }
        //小数点后处理
        Long dou = 0l;
        if (moneys.length > 1 && moneys[1] != null && !"".equals(moneys[1]))
        {
            dou = Long.valueOf(moneys[1]);
        }
        if (dou == 0)
        {
            //小数点后没有数或者为零
            returnString = returnString + "元整";
        }
        else
        {
            //小数点后保留的两位数值
            returnString = returnString + "元";
            Long j = dou / 10;
            Long f = dou % 10;
            returnString = returnString + num[j.intValue()] + "角";
            returnString = returnString + num[f.intValue()] + "分";
        }
        return returnString; //To change body of implemented methods use File | Settings | File Templates.
    }
    
    @Override
    public Dzxy getBlzqzr(int blzqzrjlId, int zqId, String type)
        throws Throwable
    {
        
        try (Connection connection = getConnection())
        {
            if ("front".equals(type))
            {
                int uId = serviceResource.getSession().getAccountId();
                if (!isBadCliamTheHolder(connection, uId, blzqzrjlId, zqId)
                    && !isBadCliamBuyers(connection, uId, blzqzrjlId))
                {
                    return null;
                }
            }
            Dzxy record = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT T5125.F03 AS F01, T5126.F03 AS F02 FROM S51.T5126 LEFT JOIN S51.T5125 ON T5125.F01 = T5126.F01 LEFT JOIN S62.T6267 ON T6267.F02 = T5126.F02 WHERE T6267.F01 = ? AND T5126.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, blzqzrjlId);
                pstmt.setInt(2, XyType.BLZQZRXY);
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
    
    @Override
    public DzxyBlzqzr getDzxyBlzqzr(int blzqzrjlId, int zqId, int userId, String type, String reqType)
        throws Throwable
    {
        DzxyBlzqzr dzxyBlzqzr = new DzxyBlzqzr();
        try (Connection connection = getConnection())
        {
            dzxyBlzqzr.xy_no = "blzqzr-" + blzqzrjlId;
            
            //不良债权转让记录
            T6265 t6265 = selectT6265(connection, blzqzrjlId);
            //不良债权转让申请
            T6264 t6264 = selectT6264(connection, t6265.F02);
            //标的主信息
            T6230 t6230 = getBid(connection, t6264.F03);
            //标的扩展信息
            T6231 t6231 = getExtra(connection, t6264.F03);
            
            //获取转让人列表
            dzxyBlzqzr.jf_zrr = searchZrrList(connection, blzqzrjlId, zqId, userId, type, reqType);
            dzxyBlzqzr.t6230 = t6230;
            dzxyBlzqzr.t6231 = t6231;
            
            dzxyBlzqzr.zqjz = t6265.F05;
            dzxyBlzqzr.zrjg = t6265.F06;
            dzxyBlzqzr.zrTime = t6265.F07;
            dzxyBlzqzr.dhPeriods = t6231.F03;
            dzxyBlzqzr.zPeriods = t6231.F02;
            
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT SUM(F03) FROM S62.T6266 WHERE T6266.F02 = ? LIMIT 1"))
            {
                pstmt.setInt(1, t6265.F01);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        dzxyBlzqzr.dhbx = resultSet.getBigDecimal(1);
                    }
                }
            }
            //购买人账户信息
            T6110 yft6110 = getUserInfo(connection, t6265.F03);
            //购买人基础信息
            T6141 yft6141 = selectT6141(connection, t6265.F03);
            //购买人基础信息
            T6161 yft6161 = selectT6161(connection, t6265.F03);
            
            dzxyBlzqzr.yf_companyName = yft6161.F04;
            if ("Y".equals(yft6161.F20))
            {
                dzxyBlzqzr.yf_papersNum = yft6161.F19;
            }
            else
            {
                dzxyBlzqzr.yf_papersNum = yft6161.F03;
            }
            dzxyBlzqzr.yf_loginName = yft6110.F02;
            dzxyBlzqzr.yf_realName = yft6141.F02;
            dzxyBlzqzr.yf_sfzh = yft6141.F07;
        }
        return dzxyBlzqzr;
    }
    
    @Override
    public Map<String, Object> getBadClaimContentMap(int blzqzrjlId, int zqId, int userId, String type, String reqType)
        throws Throwable
    {
        ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
        DzxyBlzqzr dzxyBlzqzr = getDzxyBlzqzr(blzqzrjlId, zqId, userId, type, reqType);
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
        //协议编号
        valueMap.put("xy_no", dzxyBlzqzr.xy_no);
        //转让人列表
        valueMap.put("zrrlist", dzxyBlzqzr.jf_zrr);
        //待还本息
        valueMap.put("dsbx", dzxyBlzqzr.dhbx);
        //待还期数
        valueMap.put("dhqs", dzxyBlzqzr.dhPeriods);
        //总期数
        valueMap.put("zqs", dzxyBlzqzr.zPeriods);
        //转让价格
        valueMap.put("zrjg", dzxyBlzqzr.zrjg);
        //转让时间
        valueMap.put("zrsj", dzxyBlzqzr.zrTime);
        //债权价值
        valueMap.put("zqjz", dzxyBlzqzr.zqjz);
        //真实姓名(乙方)
        valueMap.put("yf_zsxm", dzxyBlzqzr.yf_realName == null ? "" : dzxyBlzqzr.yf_realName);
        //身份证号(乙方)
        valueMap.put("yf_sfzh", dzxyBlzqzr.yf_sfzh == null ? "" : dzxyBlzqzr.yf_sfzh);
        //公司名称(乙方)
        valueMap.put("yf_companyName", dzxyBlzqzr.yf_companyName == null ? "" : dzxyBlzqzr.yf_companyName);
        //营业执照号/社会信用代码(乙方)
        valueMap.put("yf_papersNum", dzxyBlzqzr.yf_papersNum == null ? "" : dzxyBlzqzr.yf_papersNum);
        //用户名（乙方）
        valueMap.put("yf_yhm", dzxyBlzqzr.yf_loginName);
        //网站名称
        valueMap.put("site_name", configureProvider.format(SystemVariable.SITE_NAME));
        //网站域名
        valueMap.put("site_domain", configureProvider.format(SystemVariable.SITE_DOMAIN));
        //平台所属公司名称
        valueMap.put("company_name", configureProvider.format(SystemVariable.COMPANY_NAME));
        //平台所属公司地址
        valueMap.put("company_address", configureProvider.format(SystemVariable.COMPANY_ADDRESS));
        
        return valueMap;
    }
    
    /** 
     * 查询转让人列表
     * @param connection
     * @param blzqzrjlId
     * @param zqId
     * @param userId
     * @param type
     * @param reqType
     * @return
     * @throws Throwable
     */
    private List<DzxyZrr> searchZrrList(Connection connection, int blzqzrjlId, int zqId, int usesrId, String type,
        String reqType)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT SUM(T6266.F03) AS F01,T6110.F01 AS F02,T6110.F02 AS F03,T6110.F06 AS F04,T6110.F01 AS F05 FROM S62.T6266 LEFT JOIN S62.T6265 ON T6265.F01 = T6266.F02 LEFT JOIN S61.T6110 ON T6110.F01 = T6266.F04 WHERE T6266.F02 = ?  GROUP BY T6266.F06");
        
        List<DzxyZrr> list = null;
        try (PreparedStatement pstmt = connection.prepareStatement(sql.toString()))
        {
            pstmt.setInt(1, blzqzrjlId);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                while (resultSet.next())
                {
                    DzxyZrr record = new DzxyZrr();
                    record.zrAmount = resultSet.getBigDecimal(1);
                    int userId = resultSet.getInt(2);
                    record.loginName = resultSet.getString(3);
                    record.userType = resultSet.getString(4);
                    T6141 t6141 = selectT6141(connection, userId);
                    int tzrId = resultSet.getInt(5);
                    
                    //如果查看人为转出人
                    if ("ZCR".equals(type) && "front".equals(reqType))
                    {
                        //转让人只能看见自己的信息，其他转让人信息进行打码处理
                        if (tzrId == usesrId)
                        {
                            record.realName = t6141.F02;
                            record.sfzh = t6141.F07;
                            if (T6110_F06.FZRR.name().equals(record.userType))
                            {
                                T6161 t6161 = selectT6161(connection, userId);
                                record.companyName = t6161.F04;
                                record.papersNum = t6161.F20.equals("Y") ? t6161.F19 : t6161.F03;
                            }
                        }
                        else
                        {
                            record.sfzh = t6141.F06;
                            record.loginName = record.loginName.substring(0, 4) + "***";
                            record.realName = t6141.F02.substring(0, 1) + "**";
                            if (T6110_F06.FZRR.name().equals(record.userType))
                            {
                                T6161 t6161 = selectT6161(connection, userId);
                                record.companyName =
                                    t6161.F04.substring(0, 2) + "***"
                                        + t6161.F04.substring(t6161.F04.length() - 2, t6161.F04.length());
                                record.papersNum =
                                    t6161.F20.equals("Y") ? t6161.F19.substring(0, 4) + "***" : t6161.F03.substring(0,
                                        4) + "***";
                            }
                        }
                    }
                    else
                    {
                        record.realName = t6141.F02;
                        record.sfzh = t6141.F07;
                        if (T6110_F06.FZRR.name().equals(record.userType))
                        {
                            T6161 t6161 = selectT6161(connection, userId);
                            record.companyName = t6161.F04;
                            record.papersNum = t6161.F20.equals("Y") ? t6161.F19 : t6161.F03;
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
     * 不良债权转让记录
     * 
     * @param connection
     * @param F01
     * @return
     * @throws Throwable
     */
    private T6265 selectT6265(Connection connection, int F01)
        throws Throwable
    {
        T6265 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07 FROM S62.T6265 WHERE T6265.F01 = ? LIMIT 1"))
        {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6265();
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
    
    /**
     * 不良债权转让记录
     * 
     * @param connection
     * @param F01
     * @return
     * @throws Throwable
     */
    private T6264 selectT6264(Connection connection, int F01)
        throws Throwable
    {
        T6264 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10 FROM S62.T6264 WHERE T6264.F01 = ? LIMIT 1"))
        {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6264();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getString(2);
                    record.F03 = resultSet.getInt(3);
                    record.F04 = T6264_F04.parse(resultSet.getString(4));
                    record.F05 = resultSet.getInt(5);
                    record.F06 = resultSet.getInt(6);
                    record.F07 = resultSet.getTimestamp(7);
                    record.F08 = resultSet.getTimestamp(8);
                    record.F09 = resultSet.getBigDecimal(9);
                    record.F10 = resultSet.getBigDecimal(10);
                }
            }
        }
        return record;
    }
    
    /**
     * 根据债权转让申请ID判断该用户是否为这个债权的债权持有人
     * @param id 用户Id
     * @return
     */
    private boolean isBadCliamTheHolder(Connection connection, int id, int zqzrsqId, int zqId)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT T6265.F01 FROM S62.T6265 LEFT JOIN S62.T6266 ON T6265.F01 = T6266.F02 WHERE T6265.F01 = ? AND T6266.F04 = ? AND T6266.F06 = ? "))
        {
            pstmt.setInt(1, zqzrsqId);
            pstmt.setInt(2, id);
            pstmt.setInt(3, zqId);
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
    private boolean isBadCliamBuyers(Connection connection, int id, int zqzrsqId)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT T6265.F01 FROM S62.T6265 WHERE T6265.F01 = ? AND T6265.F03 = ?"))
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
    
    @Override
    public T6265 selectT6265(int F02)
        throws Throwable
    {
        T6265 record = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07 FROM S62.T6265 WHERE T6265.F02 = ? LIMIT 1"))
            {
                pstmt.setInt(1, F02);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new T6265();
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
        }
        return record;
    }
    
    @Override
    public List<T6271> getBadClaimList(int blzqzrId)
        throws Throwable
    {
        List<T6271> list = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11, F12, F13 FROM S62.T6271 WHERE T6271.F12 = ? "))
            {
                pstmt.setInt(1, blzqzrId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    list = resultSetToT6271(resultSet);
                }
            }
        }
        return list;
    }
    
    @Override
    public List<T6271> getDfList(int bId)
        throws Throwable
    {
        List<T6271> list = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pst =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11, F12, F13 FROM S62.T6271 WHERE T6271.F03 = ?"))
            {
                pst.setInt(1, bId);
                try (ResultSet resultSet = pst.executeQuery())
                {
                    list = resultSetToT6271(resultSet);
                }
            }
            return list;
        }
    }
    
    /**
     * 把T6271的结果集转换成List<T6271>
     * <功能详细描述>
     * @param resultSet
     * @return
     */
    private List<T6271> resultSetToT6271(ResultSet resultSet)
        throws Throwable
    {
        List<T6271> list = null;
        while (resultSet.next())
        {
            T6271 record = new T6271();
            record.F01 = resultSet.getInt(1);
            record.F02 = resultSet.getInt(2);
            record.F03 = resultSet.getInt(3);
            record.F04 = resultSet.getString(4);
            record.F05 = resultSet.getString(5);
            record.F06 = resultSet.getTimestamp(6);
            record.F07 = T6271_F07.parse(resultSet.getString(7));
            record.F08 = T6271_F08.parse(resultSet.getString(8));
            record.F09 = resultSet.getString(9);
            record.F10 = T6271_F10.parse(resultSet.getString(10));
            record.F11 = resultSet.getInt(11);
            record.F12 = resultSet.getInt(12);
            record.F13 = resultSet.getInt(13);
            if (list == null)
            {
                list = new ArrayList<>();
            }
            list.add(record);
        }
        return list;
    }
    
    @Override
    public String amountConvertCN(BigDecimal amount)
        throws Throwable
    {
        //汉语中数字大写
        final String[] CN_UPPER_NUMBER = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};
        //汉语中货币单位大写，这样的设计类似于占位符
        final String[] CN_UPPER_MONETRAY_UNIT =
            {"分", "角", "元", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "兆", "拾", "佰", "仟"};
        //特殊字符：整
        final String CN_FULL = "整";
        //特殊字符：负
        final String CN_NEGATIVE = "负";
        //金额的精度，默认值为2
        final int MONEY_PRECISION = 2;
        //特殊字符：零元整
        final String CN_ZEOR_FULL = "零元" + CN_FULL;
        StringBuffer sb = new StringBuffer();
        // -1, 0, or 1 as the value of this BigDecimal is negative, zero, or
        // positive.
        int signum = amount.signum();
        // 零元整的情况
        if (signum == 0)
        {
            return CN_ZEOR_FULL;
        }
        //这里会进行金额的四舍五入
        long number = amount.movePointRight(MONEY_PRECISION).setScale(0, 4).abs().longValue();
        // 得到小数点后两位值
        long scale = number % 100;
        int numUnit = 0;
        int numIndex = 0;
        boolean getZero = false;
        // 判断最后两位数，一共有四中情况：00 = 0, 01 = 1, 10, 11
        if (!(scale > 0))
        {
            numIndex = 2;
            number = number / 100;
            getZero = true;
        }
        if ((scale > 0) && (!(scale % 10 > 0)))
        {
            numIndex = 1;
            number = number / 10;
            getZero = true;
        }
        int zeroSize = 0;
        while (true)
        {
            if (number <= 0)
            {
                break;
            }
            // 每次获取到最后一个数
            numUnit = (int)(number % 10);
            if (numUnit > 0)
            {
                if ((numIndex == 9) && (zeroSize >= 3))
                {
                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[6]);
                }
                if ((numIndex == 13) && (zeroSize >= 3))
                {
                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[10]);
                }
                sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
                sb.insert(0, CN_UPPER_NUMBER[numUnit]);
                getZero = false;
                zeroSize = 0;
            }
            else
            {
                ++zeroSize;
                if (!(getZero))
                {
                    sb.insert(0, CN_UPPER_NUMBER[numUnit]);
                }
                if (numIndex == 2)
                {
                    if (number > 0)
                    {
                        sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
                    }
                }
                else if (((numIndex - 2) % 4 == 0) && (number % 1000 > 0))
                {
                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
                }
                getZero = true;
            }
            // 让number每次都去掉最后一个数
            number = number / 10;
            ++numIndex;
        }
        // 如果signum == -1，则说明输入的数字为负数，就在最前面追加特殊字符：负
        if (signum == -1)
        {
            sb.insert(0, CN_NEGATIVE);
        }
        // 输入的数字小数点后两位为"00"的情况，则要在最后追加特殊字符：整
        if (!(scale > 0))
        {
            sb.append(CN_FULL);
        }
        return sb.toString();
    }
}
