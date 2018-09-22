package com.dimeng.p2p.modules.bid.user.service.achieve;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S62.entities.T6231;
import com.dimeng.p2p.S62.entities.T6253;
import com.dimeng.p2p.S62.entities.T6260;
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
import com.dimeng.p2p.S62.enums.T6252_F09;
import com.dimeng.p2p.S63.enums.T6340_F03;
import com.dimeng.p2p.common.enums.QueryType;
import com.dimeng.p2p.modules.bid.user.service.WdzqManage;
import com.dimeng.p2p.modules.bid.user.service.entity.AssetsInfo;
import com.dimeng.p2p.modules.bid.user.service.entity.BackOff;
import com.dimeng.p2p.modules.bid.user.service.entity.BackOffList;
import com.dimeng.p2p.modules.bid.user.service.entity.Tbzdzq;
import com.dimeng.p2p.modules.bid.user.service.entity.ZqxxEntity;
import com.dimeng.p2p.modules.bid.user.service.query.BackOffQuery;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.DateParser;
import com.dimeng.util.parser.EnumParser;

public class WdzqManageImpl extends AbstractBidManage implements WdzqManage
{
    
    public WdzqManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public AssetsInfo getAssetsInfo()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            AssetsInfo info = new AssetsInfo();
            info.accMakeMoney = earnLx(connection);
            info.sellMakeMoney = earnZqzryk(connection);
            info.makeMoney = info.accMakeMoney.subtract(tzglf(connection));
            info.money = earnZc(connection);
            info.assetsNum = earnCount(connection);
            return info;
        }
    }
    
    private BigDecimal tzglf(Connection connection)
        throws Throwable
    {
        
        BigDecimal glf = new BigDecimal(0);
        StringBuilder sb =
            new StringBuilder(
                "SELECT IFNULL(SUM(T6102.F07),0) F01 FROM S61.T6102 INNER JOIN S61.T6101 ON T6102.F02 = T6101.F01 ");
        sb.append("WHERE T6102.F03=? AND T6101.F02=? AND T6101.F03=?");
        try (PreparedStatement ps = connection.prepareStatement(sb.toString());)
        {
            ps.setInt(1, FeeCode.GLF);
            ps.setInt(2, serviceResource.getSession().getAccountId());
            ps.setString(3, T6101_F03.WLZH.name());
            try (ResultSet rs = ps.executeQuery();)
            {
                if (rs.next())
                {
                    glf = rs.getBigDecimal(1);
                }
            }
        }
        return glf;
    }
    
    /**
     * 持有数量
     * 
     * @param userId
     * @return
     * @throws Throwable
     */
    private int earnCount(Connection connection)
        throws Throwable
    {
        int count = 0;
        try (PreparedStatement ps =
            connection.prepareStatement("SELECT COUNT(T6251.F01)  FROM S62.T6251,S62.T6230 WHERE T6230.F01 =T6251.F03 AND T6230.F20 IN (?,?) AND T6251.F04 = ? AND T6251.F07 >0");)
        {
            ps.setString(1, T6230_F20.HKZ.name());
            ps.setString(2, T6230_F20.TBZ.name());
            ps.setInt(3, serviceResource.getSession().getAccountId());
            try (ResultSet rs = ps.executeQuery();)
            {
                if (rs.next())
                {
                    count = rs.getInt(1);
                }
                
            }
        }
        return count;
    }
    
    /**
     * 资产
     * 
     * @param userId
     * @return
     * @throws Throwable
     */
    private BigDecimal earnZc(Connection connection)
        throws Throwable
    {
        BigDecimal zc = new BigDecimal(0);
        try (PreparedStatement ps =
            connection.prepareStatement("SELECT IFNULL(SUM(T6252.F07),0)  FROM S62.T6252 WHERE T6252.F04 = ? AND T6252.F09 = ? AND T6252.F05 = ? AND NOT EXISTS (SELECT 1 FROM S62.T6236 WHERE T6236.F03 = T6252.F04 AND T6236.F02 = T6252.F02)"))
        {
            ps.setInt(1, serviceResource.getSession().getAccountId());
            ps.setString(2, T6252_F09.WH.name());
            ps.setInt(3, FeeCode.TZ_BJ);
            try (ResultSet rs = ps.executeQuery();)
            {
                if (rs.next())
                {
                    zc = rs.getBigDecimal(1);
                }
                
            }
        }
        return zc;
    }
    
    /**
     * 利息加罚息
     * 
     * @param userId
     * @return
     * @throws Throwable
     */
    private BigDecimal earnLx(Connection connection)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT wyj.F01+lx.F01+fx.F01 FROM (SELECT IFNULL(SUM(T6252.F07),0) F01 FROM S62.T6252 WHERE T6252.F09='YH' AND T6252.F05=7005 AND T6252.F04=?) wyj,");
        sql.append("(SELECT IFNULL(SUM(CASE WHEN IFNULL(TBL_LX.F03,0) = 0 THEN IFNULL(TBL_LX.F02,0) ELSE IFNULL(TBL_LX.F02,0) + IFNULL(TBL_LX.F01,0) END),0) F01 FROM (SELECT (SELECT SUM(T6252_WH.F07) FROM S62.T6252 T6252_WH WHERE T6252_WH.F11 = T6252.F11 AND T6252_WH.F06 = T6252.F06 AND T6252_WH.F09 = 'WH' AND T6252_WH.F05 = 7002) F01,(SELECT SUM(T6252_WH.F07) FROM S62.T6252 T6252_WH WHERE T6252_WH.F11 = T6252.F11 AND T6252_WH.F06 = T6252.F06 AND T6252_WH.F09 = 'YH' AND T6252_WH.F05 = 7002) F02,(SELECT T6253.F07 FROM S62.T6253 WHERE T6253.F02 = T6252.F02) F03 FROM S62.T6252 INNER JOIN S62.T6251 ON T6251.F01 = T6252.F11 WHERE T6252.F05 = 7002 AND T6252.F09 IN ('WH','YH') AND T6251.F04=? GROUP BY T6252.F11,T6252.F06) TBL_LX) lx,");
        sql.append("(SELECT IFNULL(SUM(CASE WHEN IFNULL(TBL_LX.F03,0) = 0 THEN IFNULL(TBL_LX.F02,0) ELSE CASE WHEN TBL_LX.F04 = 'BJQEDB' THEN IFNULL(TBL_LX.F02,0) ELSE IFNULL(TBL_LX.F02,0) + IFNULL(TBL_LX.F01,0) END END),0) F01 FROM (SELECT (SELECT SUM(F07) FROM S62.T6252 T6252_WH WHERE T6252_WH.F11 = T6252.F11 AND T6252_WH.F06 = T6252.F06 AND T6252_WH.F09 = 'WH' AND T6252_WH.F05 = 7004) F01,(SELECT SUM(F07) FROM S62.T6252 T6252_WH WHERE T6252_WH.F11 = T6252.F11 AND T6252_WH.F06 = T6252.F06 AND T6252_WH.F09 = 'YH' AND T6252_WH.F05 = 7004) F02,(SELECT T6253.F07 FROM S62.T6253 WHERE T6253.F02 = T6252.F02) F03,T6230.F12 F04 FROM S62.T6252 INNER JOIN S62.T6251 ON T6251.F01 = T6252.F11 INNER JOIN S62.T6230 ON T6230.F01 = T6252.F02 WHERE T6252.F05 = 7004 AND T6252.F09 IN ('WH','YH') AND T6251.F04=? AND T6252.F06 <= (IFNULL((SELECT F08 - 1 FROM S62.T6253 WHERE T6253.F02 = T6252.F02),(SELECT MAX(F06) FROM S62.T6252 T6252_QS WHERE T6252_QS.F02 = T6252.F02))) GROUP BY T6252.F11,T6252.F06 UNION SELECT '' AS F01,T6255.F03 AS F02,T6253.F07 AS F03,'' AS F07 FROM S62.T6255 LEFT JOIN S62.T6253 ON T6255.F02 = T6253.F01 WHERE T6255.F05 = 7004 AND T6255.F04=?) TBL_LX) fx");
        BigDecimal amount = BigDecimal.ZERO;
        try (PreparedStatement ps = connection.prepareStatement(sql.toString());)
        {
            int userId = serviceResource.getSession().getAccountId();
            ps.setInt(1, userId);
            ps.setInt(2, userId);
            ps.setInt(3, userId);
            ps.setInt(4, userId);
            try (ResultSet rs = ps.executeQuery();)
            {
                if (rs.next())
                {
                    amount = rs.getBigDecimal(1);
                }
                
            }
        }
        return amount;
    }
    
    // 优选理财利息加罚息
    private BigDecimal yxlcLx(Connection connection)
        throws SQLException
    {
        BigDecimal lx = new BigDecimal(0);
        try (PreparedStatement ps =
            connection.prepareStatement("SELECT IFNULL(SUM(F07),0)	 FROM S64.T6412 WHERE F09 = 'YH' AND F04=? AND F05=7002");)
        {
            ps.setInt(1, serviceResource.getSession().getAccountId());
            try (ResultSet rs = ps.executeQuery();)
            {
                if (rs.next())
                {
                    lx = rs.getBigDecimal(1);
                }
                
            }
        }
        return lx;
    }
    
    /**
     * 违约金
     * 
     * @param userId
     * @return
     * @throws Throwable
     */
    // private BigDecimal earnWyj() throws Throwable {
    // BigDecimal wyj = new BigDecimal(0);
    // try (PreparedStatement ps = getConnection()
    // .prepareStatement(
    // "SELECT IFNULL(SUM(F07),0)  FROM S62.T6252 WHERE F04 = ? AND F09 = ? AND F05 = ?");)
    // {
    // ps.setInt(1, serviceResource.getSession().getAccountId());
    // ps.setString(2, T6252_F09.YH.name());
    // ps.setInt(3, FeeCode.TZ_WYJ);
    // try (ResultSet rs = ps.executeQuery();) {
    // if (rs.next()) {
    // wyj = rs.getBigDecimal(1);
    // }
    //
    // }
    // }
    // return wyj;
    // }
    
    /**
     * 线上债权转让盈亏
     * 
     * @param userId
     * @return
     * @throws Throwable
     */
    private BigDecimal earnZqzryk(Connection connection)
        throws Throwable
    {
        BigDecimal zryk = new BigDecimal(0);
        try (PreparedStatement ps =
            connection.prepareStatement("SELECT IFNULL(SUM(F08),0)  FROM S62.T6262 WHERE F03 = ?");)
        {
            ps.setInt(1, serviceResource.getSession().getAccountId());
            try (ResultSet rs = ps.executeQuery();)
            {
                if (rs.next())
                {
                    zryk = rs.getBigDecimal(1);
                }
                
            }
        }
        BigDecimal zcyk = new BigDecimal(0);
        try (PreparedStatement ps =
            connection.prepareStatement("SELECT IFNULL(SUM(T6262.F09),0)  FROM S62.T6262,S62.T6260,S62.T6251 WHERE T6251.F04 = ? AND  T6251.F01 = T6260.F02 AND T6260.F01 =T6262.F02");)
        {
            ps.setInt(1, serviceResource.getSession().getAccountId());
            try (ResultSet rs = ps.executeQuery();)
            {
                if (rs.next())
                {
                    zcyk = rs.getBigDecimal(1);
                }
                
            }
        }
        return zryk.add(zcyk);
    }
    
    public final static String SELECT_SQL =
        "SELECT T6251.F02 AS F01, T6251.F03 AS F02, T6251.F04 AS F03, T6251.F05 AS F04, T6251.F06 AS F05, T6251.F07 AS F06, T6251.F08 AS F07, T6251.F09 AS F08, T6251.F10 AS F09, T6230.F02 AS F10, T6230.F03 AS F11, T6230.F04 AS F12, T6230.F05 AS F13, T6230.F06 AS F14, T6230.F07 AS F15, T6230.F08 AS F16, T6230.F09 AS F17, T6230.F10 AS F18, T6230.F11 AS F19, T6230.F12 AS F20, T6230.F13 AS F21, T6230.F14 AS F22, T6230.F15 AS F23, T6230.F16 AS F24, T6230.F17 AS F25, T6230.F18 AS F26, T6230.F19 AS F27, T6230.F20 AS F28, T6230.F21 AS F29, T6230.F22 AS F30, T6230.F23 AS F31, T6230.F24 AS F32, T6230.F25 AS F33, T6230.F26 AS F34, T6251.F01 AS F35, T6251.F12 AS F36, ( SELECT T6344.F05 FROM S62.T6288 LEFT JOIN S63.T6342 ON T6288.F10 = T6342.F01 LEFT JOIN S63.T6344 ON T6344.F01 = T6342.F03 LEFT JOIN S63.T6340 ON T6344.F02 = T6340.F01 WHERE T6340.F03 = ? AND T6288.F09 = T6251.F11 ) AS F37 FROM S62.T6251 INNER JOIN S62.T6230 ON T6251.F03 = T6230.F01 INNER JOIN S62.T6231 ON T6231.F01 = T6230.F01";
    
    @Override
    public PagingResult<ZqxxEntity> getRecoverAssests(Paging paging)
        throws Throwable
    {
        int userId = serviceResource.getSession().getAccountId();
        StringBuilder sql = new StringBuilder(SELECT_SQL);
        ArrayList<Object> parameters = new ArrayList<>();
        sql.append(" WHERE T6230.F20 IN (?) AND T6251.F04 = ? AND T6251.F07 > 0  ORDER BY T6251.F09 DESC");
        parameters.add(T6340_F03.interest);
        parameters.add(T6230_F20.HKZ);
        // parameters.add(T6230_F20.TBZ);
        parameters.add(userId);
        try (final Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<ZqxxEntity>()
            {
                @Override
                public ZqxxEntity[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<ZqxxEntity> list = null;
                    while (resultSet.next())
                    {
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        ZqxxEntity record = new ZqxxEntity();
                        record.F01 = resultSet.getString(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getInt(3);
                        record.F04 = resultSet.getBigDecimal(4);
                        record.F05 = resultSet.getBigDecimal(5);
                        record.F06 = resultSet.getBigDecimal(6);
                        record.F07 = T6251_F08.parse(resultSet.getString(7));
                        record.F08 = resultSet.getDate(8);
                        record.F09 = resultSet.getDate(9);
                        record.F10 = resultSet.getInt(10);
                        record.F11 = resultSet.getString(11);
                        record.F12 = resultSet.getInt(12);
                        record.F13 = resultSet.getBigDecimal(13);
                        record.F14 = resultSet.getBigDecimal(14);
                        record.F15 = resultSet.getBigDecimal(15);
                        record.F16 = resultSet.getInt(16);
                        record.F17 = resultSet.getInt(17);
                        record.F18 = T6230_F10.parse(resultSet.getString(18));
                        record.F19 = T6230_F11.parse(resultSet.getString(19));
                        record.F20 = T6230_F12.parse(resultSet.getString(20));
                        record.F21 = T6230_F13.parse(resultSet.getString(21));
                        record.F22 = T6230_F14.parse(resultSet.getString(22));
                        record.F23 = T6230_F15.parse(resultSet.getString(23));
                        record.F24 = T6230_F16.parse(resultSet.getString(24));
                        record.F25 = T6230_F17.parse(resultSet.getString(25));
                        record.F26 = resultSet.getInt(26);
                        record.F27 = resultSet.getInt(27);
                        record.F28 = T6230_F20.parse(resultSet.getString(28));
                        record.F29 = resultSet.getString(29);
                        record.F30 = resultSet.getTimestamp(30);
                        record.F31 = resultSet.getInt(31);
                        record.F32 = resultSet.getTimestamp(32);
                        record.F33 = resultSet.getString(33);
                        record.F34 = resultSet.getBigDecimal(34);
                        record.zqid = resultSet.getInt(35);
                        record.zqzrOrderId = resultSet.getInt(36);
                        record.jxl = resultSet.getBigDecimal(37);
                        try
                        {
                            record.dsbx = getDsbx(connection, record.zqid);
                            // record.ysbx = getYsbx(record.F02);
                            T6231 t6231 = getkzxx(connection, record.F02);
                            record.hkqs = t6231.F02;
                            record.syqs = t6231.F03;
                            record.xghkr = t6231.F06;
                        }
                        catch (Throwable e)
                        {
                            logger.error(e, e);
                        }
                        list.add(record);
                    }
                    
                    return list == null || list.size() == 0 ? null : list.toArray(new ZqxxEntity[list.size()]);
                    
                }
            }, paging, sql.toString(), parameters);
        }
    }
    
    @Override
    public PagingResult<ZqxxEntity> getSettleAssests(Paging paging)
        throws Throwable
    {
        int userId = serviceResource.getSession().getAccountId();
        StringBuilder sql =
            new StringBuilder(
                "SELECT DISTINCT T6251.F02 AS F01, T6251.F03 AS F02, T6251.F04 AS F03, T6251.F05 AS F04, T6251.F06 AS F05, T6251.F07 AS F06, T6251.F08 AS F07, T6251.F09 AS F08, T6251.F10 AS F09, T6230.F02 AS F10, T6230.F03 AS F11, T6230.F04 AS F12, T6230.F05 AS F13, T6230.F06 AS F14, T6230.F07 AS F15, T6230.F08 AS F16, T6230.F09 AS F17, T6230.F10 AS F18, T6230.F11 AS F19, T6230.F12 AS F20, T6230.F13 AS F21, T6230.F14 AS F22, T6230.F15 AS F23, T6230.F16 AS F24, T6230.F17 AS F25, T6230.F18 AS F26, T6230.F19 AS F27, T6230.F20 AS F28, T6230.F21 AS F29, T6230.F22 AS F30, T6230.F23 AS F31, T6230.F24 AS F32, T6230.F25 AS F33, T6230.F26 AS F34, T6251.F01 AS F35, T6251.F12 AS F36, CASE T6230.F20 WHEN ? THEN T6231.F14 WHEN ? THEN T6231.F13 WHEN ? THEN T6231.F34 END AS F37, ( SELECT T6344.F05 FROM S62.T6288 LEFT JOIN S63.T6342 ON T6288.F10 = T6342.F01 LEFT JOIN S63.T6344 ON T6344.F01 = T6342.F03 LEFT JOIN S63.T6340 ON T6344.F02 = T6340.F01 WHERE T6340.F03 = ? AND T6288.F09 = T6251.F11 ) AS F38,(SELECT F02 FROM S62.T6266 WHERE T6266.F06 = T6251.F01 LIMIT 1) AS F39, T6251.F01 AS F40 FROM S62.T6251 INNER JOIN S62.T6230 ON T6251.F03 = T6230.F01 INNER JOIN S62.T6231 ON T6231.F01 = T6230.F01 ");
        sql.append(" INNER JOIN S62.T6250 ON T6250.F02 = T6230.F01 ");
        ArrayList<Object> parameters = new ArrayList<>();
        sql.append(" WHERE 1=1 AND T6230.F20 IN (?,?,?) AND T6251.F04 = ? AND NOT EXISTS(SELECT 1 FROM S62.T6253 WHERE T6253.F02 = T6230.F01 AND T6253.F03=? LIMIT 1) ORDER BY F37 DESC");
        parameters.add(T6230_F20.YDF);
        parameters.add(T6230_F20.YJQ);
        parameters.add(T6230_F20.YZR);
        parameters.add(T6340_F03.interest);
        parameters.add(T6230_F20.YDF);
        parameters.add(T6230_F20.YJQ);
        parameters.add(T6230_F20.YZR);
        parameters.add(userId);
        parameters.add(userId);
        try (final Connection connection = getConnection())
        {
            PagingResult<ZqxxEntity> saResult = selectPaging(connection, new ArrayParser<ZqxxEntity>()
            {
                @Override
                public ZqxxEntity[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<ZqxxEntity> list = null;
                    while (resultSet.next())
                    {
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        ZqxxEntity record = new ZqxxEntity();
                        record.F01 = resultSet.getString(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getInt(3);
                        record.F04 = resultSet.getBigDecimal(4);
                        record.F05 = resultSet.getBigDecimal(5);
                        record.F06 = resultSet.getBigDecimal(6);
                        record.F07 = T6251_F08.parse(resultSet.getString(7));
                        record.F08 = resultSet.getDate(8);
                        record.F09 = resultSet.getDate(9);
                        record.F10 = resultSet.getInt(10);
                        record.F11 = resultSet.getString(11);
                        record.F12 = resultSet.getInt(12);
                        record.F13 = resultSet.getBigDecimal(13);
                        record.F14 = resultSet.getBigDecimal(14);
                        record.F15 = resultSet.getBigDecimal(15);
                        record.F16 = resultSet.getInt(16);
                        record.F17 = resultSet.getInt(17);
                        record.F18 = T6230_F10.parse(resultSet.getString(18));
                        record.F19 = T6230_F11.parse(resultSet.getString(19));
                        record.F20 = T6230_F12.parse(resultSet.getString(20));
                        record.F21 = T6230_F13.parse(resultSet.getString(21));
                        record.F22 = T6230_F14.parse(resultSet.getString(22));
                        record.F23 = T6230_F15.parse(resultSet.getString(23));
                        record.F24 = T6230_F16.parse(resultSet.getString(24));
                        record.F25 = T6230_F17.parse(resultSet.getString(25));
                        record.F26 = resultSet.getInt(26);
                        record.F27 = resultSet.getInt(27);
                        record.F28 = T6230_F20.parse(resultSet.getString(28));
                        record.F29 = resultSet.getString(29);
                        record.F30 = resultSet.getTimestamp(30);
                        record.F31 = resultSet.getInt(31);
                        record.F32 = resultSet.getTimestamp(32);
                        record.F33 = resultSet.getString(33);
                        record.F34 = resultSet.getBigDecimal(34);
                        record.zqid = resultSet.getInt(35);
                        record.zqzrOrderId = resultSet.getInt(36);
                        record.jxl = resultSet.getBigDecimal(38);
                        record.blzqzrId = resultSet.getInt(39);
                        record.zqid = resultSet.getInt(40);
                        try
                        {
                            record.yzje = getLxFxWyj(connection, record);
                            record.hsje = getHsbj(connection, record.F02);
                        }
                        catch (Throwable e)
                        {
                            logger.error(e, e);
                        }
                        list.add(record);
                    }
                    
                    return list == null || list.size() == 0 ? null : list.toArray(new ZqxxEntity[list.size()]);
                    
                }
            }, paging, sql.toString(), parameters);
            
            if (saResult != null && saResult.getItemCount() > 0)
            {
                for (ZqxxEntity sa : saResult.getItems())
                {
                    if (sa.F28 == T6230_F20.YDF)
                    {
                        sa.jqsj = getkzxx(connection, sa.F02).F14;
                    }
                    else
                    {
                        sa.jqsj = getkzxx(connection, sa.F02).F13;
                    }
                }
            }
            return saResult;
        }
    }
    
    @Override
    public PagingResult<Tbzdzq> getLoanAssests(Paging paging)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            String sql =
                "SELECT T6230.F02 AS F01, T6230.F03 AS F02, T6230.F04 AS F03, T6230.F05 AS F04, T6230.F06 AS F05, T6230.F07 AS F06, T6230.F08 AS F07, T6230.F09 AS F08, T6230.F20 AS F09, T6230.F22 AS F10, T6230.F23 AS F11, T6230.F24 AS F12, T6250.F04 AS F13, T6250.F06 AS F14, T6230.F01 AS F15, T6230.F25 AS F16, T6231.F10 AS F17, T6231.F21 AS F18, T6231.F22 AS F19, ( SELECT T6344.F05 FROM S62.T6288 LEFT JOIN S63.T6342 ON T6288.F10 = T6342.F01 LEFT JOIN S63.T6344 ON T6344.F01 = T6342.F03 LEFT JOIN S63.T6340 ON T6344.F02 = T6340.F01 WHERE T6340.F03 = 'interest' AND T6288.F09 = T6250.F01 ) AS F20 FROM S62.T6230 INNER JOIN S62.T6231 ON T6230.F01 = T6231.F01 INNER JOIN S62.T6250 ON T6230.F01 = T6250.F02 WHERE T6230.F20 IN (?,?) AND T6250.F03 = ? ORDER BY T6250.F06 DESC ";
            return selectPaging(connection, new ArrayParser<Tbzdzq>()
            {
                @Override
                public Tbzdzq[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<Tbzdzq> list = null;
                    long dayInMill = 3600000 * 24;
                    long a = 0;
                    int day;
                    int hour;
                    int min;
                    while (resultSet.next())
                    {
                        Tbzdzq record = new Tbzdzq();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getString(2);
                        record.F03 = resultSet.getInt(3);
                        record.F04 = resultSet.getBigDecimal(4);
                        record.F05 = resultSet.getBigDecimal(5);
                        record.F06 = resultSet.getBigDecimal(6);
                        record.F07 = resultSet.getInt(7);
                        record.F08 = resultSet.getInt(8);
                        record.F09 = T6230_F20.parse(resultSet.getString(9));
                        record.F10 = resultSet.getTimestamp(10);
                        record.F11 = resultSet.getInt(11);
                        record.F12 = resultSet.getTimestamp(12);
                        record.F13 = resultSet.getBigDecimal(13);
                        record.F14 = resultSet.getTimestamp(14);
                        record.F15 = resultSet.getInt(15);
                        record.F16 = resultSet.getString(16);
                        record.shTime = resultSet.getTimestamp(17);
                        record.F21 = EnumParser.parse(T6231_F21.class, resultSet.getString(18));
                        record.F22 = resultSet.getInt(19);
                        record.jxl = resultSet.getBigDecimal(20);
                        a = dayInMill * record.F07 + record.F10.getTime() - System.currentTimeMillis();
                        if (a <= 0)
                        {
                            record.surTime = "00小时00分钟";
                        }
                        else
                        {
                            day = (int)Math.ceil(a / dayInMill);
                            hour = (int)Math.ceil((a - day * dayInMill) / 3600000);
                            min = (int)Math.ceil((a - day * dayInMill - hour * 3600000) / 60000);
                            record.surTime = String.format("%d天%d小时%d分钟", day, hour, min);
                        }
                        
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return list == null || list.size() == 0 ? null : list.toArray(new Tbzdzq[list.size()]);
                }
            }, paging, sql, T6230_F20.TBZ, T6230_F20.DFK, serviceResource.getSession().getAccountId());
        }
    }
    
    /**
     * 标扩展信息
     * 
     * @param loanId
     * @return
     * @throws Throwable
     */
    private T6231 getkzxx(Connection connection, int loanId)
        throws Throwable
    {
        T6231 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F02, F03, F04, F05, F06, F07, F08, F09, F10, F11, F12, F13, F14, F15, F16, F17, F18 FROM S62.T6231 WHERE T6231.F01 = ? LIMIT 1"))
        {
            pstmt.setInt(1, loanId);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6231();
                    record.F02 = resultSet.getInt(1);
                    record.F03 = resultSet.getInt(2);
                    record.F04 = resultSet.getBigDecimal(3);
                    record.F05 = resultSet.getBigDecimal(4);
                    record.F06 = resultSet.getDate(5);
                    record.F07 = resultSet.getInt(6);
                    record.F08 = resultSet.getString(7);
                    record.F09 = resultSet.getString(8);
                    record.F10 = resultSet.getTimestamp(9);
                    record.F11 = resultSet.getTimestamp(10);
                    record.F12 = resultSet.getTimestamp(11);
                    record.F13 = resultSet.getTimestamp(12);
                    record.F14 = resultSet.getTimestamp(13);
                    record.F15 = resultSet.getTimestamp(14);
                    record.F16 = resultSet.getString(15);
                    record.F17 = resultSet.getDate(16);
                    record.F18 = resultSet.getDate(17);
                }
            }
        }
        return record;
    }
    
    // 待收本息
    private BigDecimal getDsbx(Connection connection, int zqid)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT IFNULL(SUM(F07),0) FROM S62.T6252 WHERE T6252.F11 = ? AND T6252.F04 = ? AND T6252.F05 IN (?,?) AND T6252.F09 = ?"))
        {
            pstmt.setInt(1, zqid);
            pstmt.setInt(2, serviceResource.getSession().getAccountId());
            pstmt.setInt(3, FeeCode.TZ_LX);
            pstmt.setInt(4, FeeCode.TZ_BJ);
            pstmt.setString(5, T6252_F09.WH.name());
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
    
    /*
     * // 月收本息 private BigDecimal getYsbx(int jkbId) throws Throwable { try
     * (Connection connection = getConnection()) { try (PreparedStatement pstmt
     * = connection .prepareStatement(
     * "SELECT IFNULL(SUM(F07),0) FROM S62.T6252 WHERE T6252.F02 = ? AND T6252.F04 = ? AND T6252.F05 IN (?,?) AND T6252.F09 = ? AND F06 = (SELECT MAX(F06) FROM S62.T6252 WHERE T6252.F02 = ? AND T6252.F09 = ?)"
     * )) { pstmt.setInt(1, jkbId); pstmt.setInt(2,
     * serviceResource.getSession().getAccountId()); pstmt.setInt(3,
     * FeeCode.TZ_LX); pstmt.setInt(4, FeeCode.TZ_BJ); pstmt.setString(5,
     * T6252_F09.WH.name()); pstmt.setInt(6, jkbId); pstmt.setString(7,
     * T6252_F09.WH.name()); try (ResultSet resultSet = pstmt.executeQuery()) {
     * if (resultSet.next()) { return resultSet.getBigDecimal(1); } } } } return
     * new BigDecimal(0); }
     */
    
    // 回收本金
    private BigDecimal getHsbj(Connection connection, int jkbId)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT IFNULL(SUM(F07),0) FROM S62.T6252 WHERE T6252.F02 = ? AND T6252.F04 = ? AND T6252.F05 = ? AND T6252.F09 = ?"))
        {
            pstmt.setInt(1, jkbId);
            pstmt.setInt(2, serviceResource.getSession().getAccountId());
            pstmt.setInt(3, FeeCode.TZ_BJ);
            pstmt.setString(4, T6252_F09.YH.name());
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
    
    // 回收利息，罚息,违约金
    private BigDecimal getLxFxWyj(Connection connection, ZqxxEntity record)
        throws Throwable
    {
        BigDecimal sy = BigDecimal.ZERO;
        T6260 t6260 = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT T6260.F01 FROM S62.T6260 WHERE T6260.F02 = ?"))
        {
            pstmt.setInt(1, record.zqid);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    t6260 = new T6260();
                    t6260.F01 = resultSet.getInt(1);
                }
            }
        }
        // 查询垫付记录
        T6253 t6253 = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT T6253.F01,T6253.F08 FROM S62.T6253 WHERE T6253.F02 = ?"))
        {
            pstmt.setInt(1, record.F02);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    t6253 = new T6253();
                    t6253.F01 = resultSet.getInt(1);
                    t6253.F08 = resultSet.getInt(2);
                }
            }
        }
        
        StringBuffer sql = new StringBuffer();
        ArrayList<Object> parameters = new ArrayList<Object>();
        sql.append("SELECT IFNULL(SUM(F07),0) FROM S62.T6252 WHERE T6252.F11 = ? ");
        parameters.add(record.zqid);
        if (T6230_F20.YDF == record.F28 || T6230_F20.YJQ == record.F28 || T6230_F20.YZR == record.F28)
        {
            if (t6260 != null)
            {
                if (T6230_F20.YDF == record.F28 && T6230_F12.BJQEDB == record.F20)
                {
                    sql.append(" AND T6252.F09 = ? AND T6252.F04 = ?");
                    parameters.add(T6252_F09.YH.name());
                    parameters.add(serviceResource.getSession().getAccountId());
                }
                else
                {
                    sql.append(" AND T6252.F09 IN (?,?) AND T6252.F04 = ?");
                    parameters.add(T6252_F09.YH.name());
                    parameters.add(T6252_F09.WH.name());
                    parameters.add(serviceResource.getSession().getAccountId());
                }
            }
            else
            {
                if (T6230_F20.YDF == record.F28 && T6230_F12.BJQEDB == record.F20)
                {
                    sql.append(" AND T6252.F09 = ?");
                    parameters.add(T6252_F09.YH.name());
                }
                else
                {
                    sql.append(" AND T6252.F09 IN (?,?)");
                    parameters.add(T6252_F09.YH.name());
                    parameters.add(T6252_F09.WH.name());
                }
            }
        }
        else
        {
            sql.append(" AND T6252.F09 = ? AND T6252.F04 = ?");
            parameters.add(T6252_F09.YH.name());
            parameters.add(serviceResource.getSession().getAccountId());
        }
        if (t6253 != null)
        {
            sql.append(" AND T6252.F05 = ?");
            parameters.add(FeeCode.TZ_LX);
        }
        else
        {
            sql.append(" AND T6252.F05 IN (?,?,?)");
            parameters.add(FeeCode.TZ_LX);
            parameters.add(FeeCode.TZ_FX);
            parameters.add(FeeCode.TZ_WYJ);
        }
        sy = select(connection, new ItemParser<BigDecimal>()
        {
            @Override
            public BigDecimal parse(ResultSet resultSet)
                throws SQLException
            {
                if (resultSet.next())
                {
                    return resultSet.getBigDecimal(1);
                }
                return BigDecimal.ZERO;
            }
        }, sql.toString(), parameters);
        
        if (t6253 != null)
        {
            /*
             * BigDecimal fx = selectBigDecimal(connection,
             * "SELECT SUM(F07) FROM S62.T6252 WHERE T6252.F02 = ? AND T6252.F06 < ? AND T6252.F05 = ? AND T6252.F11 = ?"
             * , record.F02, t6253.F08, FeeCode.TZ_FX, record.zqid);
             */
            BigDecimal fx =
                selectBigDecimal(connection,
                    "SELECT SUM(F03) FROM S62.T6255 WHERE T6255.F02 = ? AND T6255.F04 = ? AND T6255.F05 = ? AND T6255.F06 = ? ",
                    t6253.F01,
                    serviceResource.getSession().getAccountId(),
                    FeeCode.TZ_FX,
                    record.zqid);
            //sy = sy.add(fx).subtract(tzglf(connection));
            sy = sy.add(fx);
        }
        return sy;
    }
    
    @Override
    public BackOff searchTotle()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            BackOff info = new BackOff();
            info.dsbx = searchTotleBx(connection);
            info.wlsgy = searchThreeBx(connection);
            info.wlygy = searchOneBx(connection);
            info.wlyn = searchYearBx(connection);
            return info;
        }
    }
    
    public final static String QUERY_TOP =
        "SELECT IFNULL(SUM((T6252.F07)), 0) FROM S62.T6252 WHERE T6252.F04 = ? AND T6252.F05 IN (?,?) AND T6252.F09 = ?  AND NOT EXISTS (SELECT 1 FROM S62.T6236 WHERE T6236.F03 = T6252.F04 AND T6236.F02 = T6252.F02)";
    
    private BigDecimal searchTotleBx(Connection connection)
        throws Throwable
    {
        BigDecimal info = new BigDecimal(0);
        try (PreparedStatement ps = connection.prepareStatement(QUERY_TOP))
        {
            ps.setInt(1, serviceResource.getSession().getAccountId());
            ps.setInt(2, FeeCode.TZ_BJ);
            ps.setInt(3, FeeCode.TZ_LX);
            ps.setString(4, T6252_F09.WH.name());
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    info = rs.getBigDecimal(1);
                }
            }
        }
        return info;
    }
    
    private BigDecimal searchOneBx(Connection connection)
        throws Throwable
    {
        StringBuilder sql = new StringBuilder(QUERY_TOP);
        sql.append("AND ADDDATE(CURDATE(),INTERVAL 1 MONTH) >= F08");
        BigDecimal info = new BigDecimal(0);
        try (PreparedStatement ps = connection.prepareStatement(sql.toString()))
        {
            ps.setInt(1, serviceResource.getSession().getAccountId());
            ps.setInt(2, FeeCode.TZ_BJ);
            ps.setInt(3, FeeCode.TZ_LX);
            ps.setString(4, T6252_F09.WH.name());
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    info = rs.getBigDecimal(1);
                }
            }
        }
        return info;
    }
    
    private BigDecimal searchThreeBx(Connection connection)
        throws Throwable
    {
        StringBuilder sql = new StringBuilder(QUERY_TOP);
        sql.append("AND ADDDATE(CURDATE(),INTERVAL 3 MONTH) >= F08");
        BigDecimal info = new BigDecimal(0);
        try (PreparedStatement ps = connection.prepareStatement(sql.toString()))
        {
            ps.setInt(1, serviceResource.getSession().getAccountId());
            ps.setInt(2, FeeCode.TZ_BJ);
            ps.setInt(3, FeeCode.TZ_LX);
            ps.setString(4, T6252_F09.WH.name());
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    info = rs.getBigDecimal(1);
                }
            }
        }
        return info;
    }
    
    private BigDecimal searchYearBx(Connection connection)
        throws Throwable
    {
        StringBuilder sql = new StringBuilder(QUERY_TOP);
        sql.append("AND ADDDATE(CURDATE(),INTERVAL 12 MONTH) >= F08");
        BigDecimal info = new BigDecimal(0);
        try (PreparedStatement ps = connection.prepareStatement(sql.toString()))
        {
            ps.setInt(1, serviceResource.getSession().getAccountId());
            ps.setInt(2, FeeCode.TZ_BJ);
            ps.setInt(3, FeeCode.TZ_LX);
            ps.setString(4, T6252_F09.WH.name());
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    info = rs.getBigDecimal(1);
                }
            }
        }
        return info;
    }
    
    @Override
    public PagingResult<BackOffList> searchList(BackOffQuery query, Paging paging)
        throws Throwable
    {
        String sql =
            "SELECT T6230.F01, (SELECT T6251.F02 FROM S62.T6251 WHERE T6251.F01 = T6252.F11), T6110.F02, SUM(T6252.F07), IFNULL(T6252.F10,T6252.F08), T6230.F04, IF(T6230.F20 = 'YDF' , 'YH', T6252.F09) AS F09,T6230.F11,T6230.F13,T6230.F14,T6252.F05 FROM S62.T6230, S62.T6252,S62.T6251, S61.T6110 WHERE T6251.F04 = ? AND T6252.F02 = T6230.F01 AND T6251.F01 = T6252.F11 AND T6110.F01 = T6230.F02 AND T6252.F05 IN (CASE WHEN T6230.F20 = 'YDF' AND T6230.F12 = 'BJQEDB' THEN '' ELSE ? END,?,CASE WHEN T6230.F20 = 'YDF' AND T6230.F12 = 'BJQEDB' THEN '' ELSE ? END) ";
        StringBuilder sbSql = new StringBuilder(sql);
        ArrayList<Object> parameters = new ArrayList<Object>();
        parameters.add(serviceResource.getSession().getAccountId());
        parameters.add(FeeCode.TZ_LX);
        parameters.add(FeeCode.TZ_BJ);
        parameters.add(FeeCode.TZ_WYJ);
        if (query != null)
        {
            QueryType queryType = query.getQueryType();
            if (queryType != null)
            {
                switch (queryType)
                {
                    case YS:
                    {
                        sbSql.append(" AND T6252.F09 IN (CASE WHEN T6230.F20 = 'YDF' THEN  ? ELSE '' END ,?)");
                        parameters.add(T6252_F09.WH.name());
                        parameters.add(T6252_F09.YH.name());
                        break;
                    }
                    case DS:
                    {
                        sbSql.append(" AND T6252.F09 = ? AND T6230.F20 <> 'YDF'");
                        parameters.add(T6252_F09.WH.toString());
                        break;
                    }
                    default:
                        break;
                }
            }
            Date startTime = query.getTimeStart();
            if (startTime != null)
            {
                if (QueryType.YS == queryType)
                {
                    sbSql.append(" AND DATE(T6252.F10) >= ?");
                }
                else
                {
                    sbSql.append(" AND DATE(T6252.F08) >= ?");
                }
                parameters.add(DateParser.format(startTime));
            }
            Date endTime = query.getTimeEnd();
            if (endTime != null)
            {
                if (QueryType.YS == queryType)
                {
                    sbSql.append(" AND DATE(T6252.F10) <= ?");
                }
                else
                {
                    sbSql.append(" AND DATE(T6252.F08) <= ?");
                }
                parameters.add(DateParser.format(endTime));
            }
            sbSql.append(" GROUP BY T6252.F02,T6252.F05,T6252.F06,T6252.F11");
            sbSql.append(" ORDER BY T6230.F01 DESC,T6252.F08 ASC");
        }
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<BackOffList>()
            {
                @Override
                public BackOffList[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<BackOffList> list = null;
                    while (resultSet.next())
                    {
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        BackOffList backOffList = new BackOffList();
                        backOffList.jkbId = resultSet.getInt(1);
                        backOffList.assestsId = resultSet.getString(2);
                        backOffList.creditor = resultSet.getString(3);
                        backOffList.money = resultSet.getBigDecimal(4);
                        backOffList.receiveDate = resultSet.getTimestamp(5);
                        backOffList.creditType = resultSet.getInt(6);
                        backOffList.dsStatus = EnumParser.parse(T6252_F09.class, resultSet.getString(7));
                        backOffList.F11 = EnumParser.parse(T6230_F11.class, resultSet.getString(8));
                        backOffList.F13 = EnumParser.parse(T6230_F13.class, resultSet.getString(9));
                        backOffList.F14 = EnumParser.parse(T6230_F14.class, resultSet.getString(10));
                        backOffList.backType = getBackType(resultSet.getInt(11));
                        list.add(backOffList);
                    }
                    
                    return list == null || list.size() == 0 ? null : list.toArray(new BackOffList[list.size()]);
                    
                }
            }, paging, sbSql.toString(), parameters);
        }
    }
    
    @Override
    public BigDecimal searchYsTotle()
        throws Throwable
    {
        BigDecimal info = new BigDecimal(0);
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(QUERY_TOP))
            {
                ps.setInt(1, serviceResource.getSession().getAccountId());
                ps.setInt(2, FeeCode.TZ_BJ);
                ps.setInt(3, FeeCode.TZ_LX);
                ps.setString(4, T6252_F09.YH.name());
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        info = rs.getBigDecimal(1);
                    }
                }
            }
        }
        return info;
    }
    
    @Override
    public BigDecimal searchList(String type, String strDate, String endDate)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            String sql =
                "SELECT IFNULL(SUM((T6252.F07)), 0) FROM S62.T6230, S62.T6252,S62.T6251, S61.T6110 WHERE T6251.F04 = ? AND T6252.F02 = T6230.F01 AND T6251.F01 = T6252.F11 AND T6110.F01 = T6230.F02 AND T6252.F05 IN (CASE WHEN T6230.F20 = 'YDF' AND T6230.F12 = 'BJQEDB' THEN '' ELSE ? END,?,CASE WHEN T6230.F20 = 'YDF' AND T6230.F12 = 'BJQEDB' THEN '' ELSE ? END) ";
            StringBuilder sbSql = new StringBuilder(sql);
            ArrayList<Object> parameters = new ArrayList<Object>();
            parameters.add(serviceResource.getSession().getAccountId());
            parameters.add(FeeCode.TZ_LX);
            parameters.add(FeeCode.TZ_BJ);
            parameters.add(FeeCode.TZ_WYJ);
            if (StringHelper.isEmpty(type))
            {
                type = "DS";
            }
            switch (type)
            {
                case "YS":
                {
                    sbSql.append(" AND T6252.F09 IN (CASE WHEN T6230.F20 = 'YDF' THEN  ? ELSE '' END ,?)");
                    parameters.add(T6252_F09.WH.name());
                    parameters.add(T6252_F09.YH.name());
                    break;
                }
                case "DS":
                {
                    sbSql.append(" AND T6252.F09 = ? AND T6230.F20 <> 'YDF'");
                    parameters.add(T6252_F09.WH.name());
                    break;
                }
                default:
                    break;
            }
            Date startTime = DateParser.parse(strDate, "yyyy-MM-dd");
            if (startTime != null)
            {
                if ("YS".equals(type))
                {
                    sbSql.append(" AND DATE(T6252.F10) >= ?");
                }
                else
                {
                    sbSql.append(" AND DATE(T6252.F08) >= ?");
                }
                parameters.add(DateParser.format(startTime));
            }
            Date endTime = DateParser.parse(endDate, "yyyy-MM-dd");
            if (endTime != null)
            {
                if ("YS".equals(type))
                {
                    sbSql.append(" AND DATE(T6252.F10) <= ?");
                }
                else
                {
                    sbSql.append(" AND DATE(T6252.F08) <= ?");
                }
                parameters.add(DateParser.format(endTime));
            }
            return select(connection, new ItemParser<BigDecimal>()
            {
                @Override
                public BigDecimal parse(ResultSet rs)
                    throws SQLException
                {
                    if (rs.next())
                    {
                        return rs.getBigDecimal(1);
                    }
                    return BigDecimal.ZERO;
                }
                
            }, sbSql.toString(), parameters);
        }
    }
    
    private String getBackType(int feeCode)
    {
        String rtnStr = "";
        switch (feeCode)
        {
            case FeeCode.TZ_BJ:
                rtnStr = "回收本金";
                break;
            case FeeCode.TZ_LX:
                rtnStr = "回收利息";
                break;
            case FeeCode.TZ_WYJ:
                rtnStr = "回收违约金";
                break;
            default:
                rtnStr = "不详";
                break;
        }
        return rtnStr;
    }
    
    @Override
    public String rendPaging(PagingResult<?> paging)
        throws Throwable
    {
        StringBuffer rtnPageStr = new StringBuffer();
        int currentPage = paging.getCurrentPage();
        
        rtnPageStr.append("<div class='paging'>总共");
        rtnPageStr.append("<span class='total highlight2 ml5 mr5'>");
        rtnPageStr.append(paging.getItemCount());
        rtnPageStr.append("</span>条记录 &nbsp;");
        if (currentPage == 1 && paging.getPageCount() > 1)
        {
            rtnPageStr.append("<a href=\"javascript:void(0);\" class='disabled prev'>&lt;</a>");
        }
        if (currentPage > 1)
        {
            rtnPageStr.append("<a href='javascript:void(0);' class='page-link prev'>&lt;</a>");
        }
        if (paging.getPageCount() > 1)
        {
            int total = 1;
            final int max = 5;
            int index = paging.getPageCount() - currentPage;
            if (index > 2)
            {
                index = 2;
            }
            else
            {
                index = index <= 0 ? (max - 1) : (max - index - 1);
            }
            int i;
            for (i = (currentPage - index); i <= paging.getPageCount() && total <= max; i++)
            {
                if (i <= 0)
                {
                    continue;
                }
                if (currentPage == i)
                {
                    rtnPageStr.append("<a href='javascript:void(0);' class='page-link cur'>");
                    rtnPageStr.append(i);
                    rtnPageStr.append("</a>");
                }
                else
                {
                    rtnPageStr.append("<a href='javascript:void(0);' class='page-link'>");
                    rtnPageStr.append(i);
                    rtnPageStr.append("</a>");
                }
                total++;
            }
            if (i < paging.getPageCount())
            {
                rtnPageStr.append("<span>...</span>");
                int idx = paging.getPageCount() - 2;
                if (i <= idx)
                {
                    rtnPageStr.append("<a href='javascript:void(0);' class='page-link'>");
                    rtnPageStr.append(idx);
                    rtnPageStr.append("</a>");
                }
                idx++;
                if (i <= idx)
                {
                    rtnPageStr.append("<a href='javascript:void(0);' class='page-link'>");
                    rtnPageStr.append(idx);
                    rtnPageStr.append("</a>");
                }
            }
        }
        if (currentPage < paging.getPageCount())
        {
            rtnPageStr.append("<a href='javascript:void(0);' class='page-link next'>&gt;</a>");
        }
        if (currentPage == paging.getPageCount() && paging.getPageCount() > 1)
        {
            rtnPageStr.append("<a href='javascript:void(0);' class=' disabled'>&gt;</a>");
        }
        
        if (paging.getPageCount() > 1)
        {
            rtnPageStr.append("到<input type=\"text\"  id=\"goPage\" class=\"page_input\" maxlength=\"7\">页<input type=\"button\"  class=\"btn_ok page-link cur\" value=\"确定\" onclick=\"pageSubmit(this);\" />");
        }
        
        rtnPageStr.append("</div>");
        
        return rtnPageStr.toString();
    }
}
