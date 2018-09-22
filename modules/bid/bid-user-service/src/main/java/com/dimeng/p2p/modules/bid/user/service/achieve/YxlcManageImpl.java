package com.dimeng.p2p.modules.bid.user.service.achieve;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.S64.enums.T6410_F14;
import com.dimeng.p2p.S64.enums.T6412_F09;
import com.dimeng.p2p.common.enums.PlanState;
import com.dimeng.p2p.common.enums.RepayStatus;
import com.dimeng.p2p.modules.bid.user.service.YxlcManage;
import com.dimeng.p2p.modules.bid.user.service.entity.BestFinacingInfo;
import com.dimeng.p2p.modules.bid.user.service.entity.EndBestFinacing;
import com.dimeng.p2p.modules.bid.user.service.entity.InBestFinacing;
import com.dimeng.p2p.modules.bid.user.service.entity.SqzBestFinacing;
import com.dimeng.util.parser.EnumParser;

public class YxlcManageImpl extends AbstractBidManage implements YxlcManage
{
    
    public YxlcManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public BestFinacingInfo getBestFinacingInfo()
        throws Throwable
    {
        BestFinacingInfo info = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT F02,F03,F04,F05,F06 FROM S64.T6413 WHERE F01=?"))
            {
                ps.setInt(1, serviceResource.getSession().getAccountId());
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        info = new BestFinacingInfo();
                        info.makeMoney = rs.getBigDecimal(1);
                        info.accountMoney = rs.getBigDecimal(2);
                        info.rate = rs.getBigDecimal(3);
                        info.num = rs.getInt(4);
                        info.addMoney = rs.getBigDecimal(5);
                    }
                }
            }
            return info;
        }
    }
    
    @Override
    public PagingResult<SqzBestFinacing> getSqzBestFinacing(Paging paging)
        throws Throwable
    {
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(serviceResource.getSession().getAccountId());
        parameters.add(PlanState.YFB);
        String sql =
            "SELECT T6410.F02, T6411.F04, T6410.F05, T6410.F11, T6410.F07, T6410.F01,ADDDATE(T6410.F10,INTERVAL 1 DAY),T6410.F04 F40,T6410.F03"
                + "  FROM S64.T6411 LEFT JOIN S64.T6410 ON T6411.F02 = T6410.F01 WHERE T6411.F03 = ? AND T6410.F07  = ?  ORDER BY T6411.F06 DESC";
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<SqzBestFinacing>()
            {
                @Override
                public SqzBestFinacing[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<SqzBestFinacing> list = null;
                    while (resultSet.next())
                    {
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        SqzBestFinacing bestFinacing = new SqzBestFinacing();
                        bestFinacing.name = resultSet.getString(1);
                        bestFinacing.addMoney = resultSet.getBigDecimal(2);
                        bestFinacing.rate = resultSet.getDouble(3);
                        bestFinacing.jkTime = resultSet.getInt(4);
                        bestFinacing.status = EnumParser.parse(PlanState.class, resultSet.getString(5));
                        bestFinacing.planId = resultSet.getInt(6);
                        
                        long hm = 1000 * 3600 * 24;
                        long time = resultSet.getTimestamp(7).getTime() - System.currentTimeMillis();
                        long day = time / hm;
                        long hour = (time - day * hm) / (1000 * 3600);
                        long min = (time - day * hm - hour * 1000 * 3600) / (1000 * 60);
                        bestFinacing.sysj = day + "天" + hour + "小时" + min + "分";
                        
                        bestFinacing.jindu = (resultSet.getDouble(9) - resultSet.getDouble(8)) / resultSet.getDouble(9);
                        
                        list.add(bestFinacing);
                    }
                    
                    return list == null || list.size() == 0 ? null : list.toArray(new SqzBestFinacing[list.size()]);
                    
                }
            },
                paging,
                sql,
                parameters);
        }
    }
    
    @Override
    public PagingResult<InBestFinacing> getInBestFinacing(Paging paging)
        throws Throwable
    {
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(serviceResource.getSession().getAccountId());
        parameters.add(PlanState.YSX);
        String sql =
            "SELECT T6410.F02, T6411.F04, T6410.F05, T6410.F21, T6410.F11, T6410.F07, T6410.F01,T6410.F14 FROM S64.T6411 LEFT JOIN S64.T6410 ON T6411.F02 = T6410.F01 WHERE T6411.F03 = ? AND T6410.F07  = ?  ORDER BY T6411.F06 DESC";
        try (final Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<InBestFinacing>()
            {
                @Override
                public InBestFinacing[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<InBestFinacing> list = null;
                    while (resultSet.next())
                    {
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        InBestFinacing bestFinacing = new InBestFinacing();
                        bestFinacing.name = resultSet.getString(1);
                        bestFinacing.addMoney = resultSet.getBigDecimal(2);
                        bestFinacing.rate = resultSet.getDouble(3);
                        bestFinacing.nextDay = resultSet.getDate(4);
                        bestFinacing.jkTime = resultSet.getInt(5);
                        bestFinacing.status = EnumParser.parse(PlanState.class, resultSet.getString(6));
                        bestFinacing.planId = resultSet.getInt(7);
                        bestFinacing.type = EnumParser.parse(T6410_F14.class, resultSet.getString(8));
                        bestFinacing.monthMoney = getAmount(connection, bestFinacing.planId);
                        try
                        {
                            bestFinacing.notMoney =
                                getMoney(connection,bestFinacing.planId, T6412_F09.WH, FeeCode.TZ_BJ).add(getMoney(connection,bestFinacing.planId,
                                    T6412_F09.WH,
                                    FeeCode.TZ_LX));
                        }
                        catch (Throwable e)
                        {
                            logger.error(e, e);
                        }
                        list.add(bestFinacing);
                    }
                    
                    return list == null || list.size() == 0 ? null : list.toArray(new InBestFinacing[list.size()]);
                    
                }
            },
                paging,
                sql,
                parameters);
        }
    }
    
    public BigDecimal getAmount(Connection connection, int yxId)
        throws SQLException
    {
        BigDecimal amount = new BigDecimal(0);
        try (PreparedStatement ps =
            connection.prepareStatement("SELECT SUM(F07) FROM S64.T6412 WHERE F02=? AND F04=? AND (F05=? OR F05=?) AND F09='WH' AND F06=(SELECT MIN(F06) FROM S64.T6412 WHERE F02=? AND F04=? AND (F05=? OR F05=?) AND F09='WH')"))
        {
            ps.setInt(1, yxId);
            ps.setInt(2, serviceResource.getSession().getAccountId());
            ps.setInt(3, FeeCode.TZ_BJ);
            ps.setInt(4, FeeCode.TZ_LX);
            ps.setInt(5, yxId);
            ps.setInt(6, serviceResource.getSession().getAccountId());
            ps.setInt(7, FeeCode.TZ_BJ);
            ps.setInt(8, FeeCode.TZ_LX);
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    amount = rs.getBigDecimal(1);
                }
            }
        }
        return amount;
    }
    
    @Override
    public PagingResult<EndBestFinacing> getEndBestFinacing(Paging paging)
        throws Throwable
    {
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(serviceResource.getSession().getAccountId());
        parameters.add(PlanState.YJZ);
        String sql =
            "SELECT T6410.F02, T6411.F04, T6410.F05, T6410.F13 endTime,T6410.F01 FROM S64.T6410 INNER JOIN S64.T6411 ON T6410.F01 = T6411.F02 WHERE T6411.F03 = ? AND T6410.F07 = ? ORDER BY T6411.F05 DESC";
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<EndBestFinacing>()
            {
                @Override
                public EndBestFinacing[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<EndBestFinacing> list = null;
                    while (resultSet.next())
                    {
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        EndBestFinacing bestFinacing = new EndBestFinacing();
                        bestFinacing.name = resultSet.getString(1);
                        bestFinacing.addMoney = resultSet.getBigDecimal(2);
                        bestFinacing.rate = resultSet.getDouble(3);
                        bestFinacing.endTime = resultSet.getTimestamp(4);
                        bestFinacing.planId = resultSet.getInt(5);
                        try
                        {
                            bestFinacing.money = getMoney(connection,bestFinacing.planId, T6412_F09.YH, FeeCode.TZ_BJ);
                            bestFinacing.takeMoney = getMoney(connection,bestFinacing.planId, T6412_F09.YH, FeeCode.TZ_LX);
                        }
                        catch (Throwable e)
                        {
                            logger.error(e, e);
                        }
                        
                        list.add(bestFinacing);
                    }
                    
                    return list == null || list.size() == 0 ? null : list.toArray(new EndBestFinacing[list.size()]);
                }
            }, paging, sql, parameters);
        }
    }
    
    /**
     * 查询金额
     * 
     * @param yxlcId
     * @return
     * @throws Throwable
     */
    private BigDecimal getMoney(Connection connection, int yxlcId, T6412_F09 status, int feeCode)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT IFNULL(SUM(F07),0) FROM S64.T6412 WHERE T6412.F02 = ? AND T6412.F04 = ? AND T6412.F05 = ? AND T6412.F09 = ?"))
        {
            pstmt.setInt(1, yxlcId);
            pstmt.setInt(2, serviceResource.getSession().getAccountId());
            pstmt.setInt(3, feeCode);
            pstmt.setString(4, status.name());
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
    public InBestFinacing getSyTime(int planID)
        throws Throwable
    {
        InBestFinacing info = null;
        String sql =
            "SELECT COUNT(*) FROM (SELECT F01 FROM S64.T6412 WHERE F02 = ? AND F04=? AND F09 = ? GROUP BY F06) AS TAB";
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql))
            {
                ps.setInt(1, planID);
                ps.setInt(2, serviceResource.getSession().getAccountId());
                ps.setString(3, RepayStatus.WH.toString());
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        info = new InBestFinacing();
                        info.num = rs.getInt(1);
                    }
                }
            }
        }
        return info;
    }
    
}
