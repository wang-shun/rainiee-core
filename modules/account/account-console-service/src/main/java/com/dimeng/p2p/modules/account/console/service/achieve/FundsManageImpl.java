package com.dimeng.p2p.modules.account.console.service.achieve;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.dimeng.framework.resource.ResourceNotFoundException;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F10;
import com.dimeng.p2p.S62.enums.T6252_F09;
import com.dimeng.p2p.common.enums.FundAccountType;
import com.dimeng.p2p.modules.account.console.service.FundsManage;
import com.dimeng.p2p.modules.account.console.service.entity.AmountTotle;
import com.dimeng.p2p.modules.account.console.service.entity.FundsView;
import com.dimeng.p2p.modules.account.console.service.entity.ZjDetailView;
import com.dimeng.p2p.modules.account.console.service.query.FundsQuery;
import com.dimeng.p2p.modules.account.console.service.query.GrzjDetailQuery;
import com.dimeng.util.Formater;
import com.dimeng.util.StringHelper;
import com.dimeng.util.io.CVSWriter;
import com.dimeng.util.parser.DateParser;
import com.dimeng.util.parser.DateTimeParser;
import com.dimeng.util.parser.EnumParser;

public class FundsManageImpl extends AbstractUserService implements FundsManage
{
    
    public FundsManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public PagingResult<FundsView> search(FundsQuery query, Paging paging)
        throws Throwable
    {
        
        try (Connection connection = getConnection())
        {
            int userId = getPTID(connection);
            
            StringBuilder sql =
                new StringBuilder(
                    "SELECT T6101.F01 AS F01, T6101.F02 AS F02, T6101.F03 AS F03, T6101.F04 AS F04, T6101.F05 AS F05, T6101.F06 AS F06, T6101.F07 AS F07, T6110.F06 AS F08, T6110.F10 AS F09 FROM S61.T6101 INNER JOIN S61.T6110 ON T6101.F02 = T6110.F01 WHERE T6101.F03 IN(?,?)");
            PagingResult<FundsView> pagingResult = null;
            ArrayList<Object> parameters = new ArrayList<Object>();
            parameters.add(T6101_F03.WLZH);
            parameters.add(T6101_F03.SDZH);
            if (query != null)
            {
                FundAccountType userType = query.getFundAccountType();
                if (userType != null)
                {
                    if (userType == FundAccountType.GR)
                    {
                        sql.append(" AND T6110.F06 = ? AND T6110.F10 = ?");
                        parameters.add(T6110_F06.ZRR);
                        parameters.add(T6110_F10.F);
                    }
                    else if (userType == FundAccountType.QY)
                    {
                        sql.append(" AND T6110.F06 = ? AND T6110.F10 = ?");
                        parameters.add(T6110_F06.FZRR);
                        parameters.add(T6110_F10.F);
                    }
                    else if (userType == FundAccountType.JG)
                    {
                        sql.append(" AND T6110.F06 = ? AND T6110.F10 = ?");
                        parameters.add(T6110_F06.FZRR);
                        parameters.add(T6110_F10.S);
                    }
                    else if (userType == FundAccountType.PT)
                    {
                        sql.append(" AND T6101.F02 = ?");
                        parameters.add(userId);
                    }
                }
                String loginName = query.getLoginName();
                if (!StringHelper.isEmpty(loginName))
                {
                    sql.append(" AND T6110.F02 LIKE ?");
                    parameters.add(getSQLConnectionProvider().allMatch(loginName));
                }
                String userName = query.getUserName();
                if (!StringHelper.isEmpty(userName))
                {
                    sql.append(" AND T6101.F04 LIKE ?");
                    parameters.add(getSQLConnectionProvider().allMatch(userName));
                }
            }
            sql.append(" ORDER BY T6101.F07 DESC");
            pagingResult = selectPaging(connection, new ArrayParser<FundsView>()
            {
                
                @Override
                public FundsView[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<FundsView> list = new ArrayList<FundsView>();
                    while (resultSet.next())
                    {
                        FundsView record = new FundsView();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = T6101_F03.parse(resultSet.getString(3));
                        record.F04 = resultSet.getString(4);
                        record.F05 = resultSet.getString(5);
                        record.F06 = resultSet.getBigDecimal(6);
                        record.F07 = resultSet.getTimestamp(7);
                        T6110_F06 t6110_F06 = T6110_F06.parse(resultSet.getString(8));
                        record.isDbf = T6110_F10.parse(resultSet.getString(9));
                        if (t6110_F06 == T6110_F06.ZRR)
                        {
                            record.userType = "个人";
                        }
                        if (t6110_F06 == T6110_F06.FZRR && record.isDbf == T6110_F10.F)
                        {
                            record.userType = "企业";
                        }
                        if (t6110_F06 == T6110_F06.FZRR && record.isDbf == T6110_F10.S)
                        {
                            record.userType = "机构";
                        }
                        list.add(record);
                    }
                    return list.toArray(new FundsView[list.size()]);
                }
            }, paging, sql.toString(), parameters);
            for (FundsView fundsView : pagingResult.getItems())
            {
                if (fundsView == null)
                {
                    continue;
                }
                if (fundsView.F02 == userId)
                {
                    fundsView.userType = "平台";
                }
            }
            return pagingResult;
        }
    }
    
    @SuppressWarnings("unused")
    private BigDecimal getYhsy(int userId)
        throws SQLException
    {
        BigDecimal amount = new BigDecimal(0);
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT IFNULL(SUM(F07),0) FROM S62.T6252 WHERE T6252.F05 IN (?,?,?) AND T6252.F09 = ? AND T6252.F04=?"))
            {
                pstmt.setInt(1, FeeCode.TZ_FX);
                pstmt.setInt(2, FeeCode.TZ_WYJ);
                pstmt.setInt(3, FeeCode.TZ_LX);
                pstmt.setString(4, T6252_F09.YH.name());
                pstmt.setInt(5, userId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        amount = resultSet.getBigDecimal(1);
                    }
                }
            }
        }
        return amount;
    }
    
    /**
     * 利息加罚息
     *
     * @param userId
     * @return
     * @throws Throwable
     */
    private BigDecimal earnLxById(int userId)
        throws SQLException
    {
        BigDecimal lx = new BigDecimal(0);
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT IFNULL(SUM(T6102.F06),0) from S61.T6102 WHERE T6102.F03 IN (?, ?, ?) AND "
                    + "T6102.F02 = (SELECT T6101.F01 FROM S61.T6101 WHERE T6101.F02 = ? AND T6101.F03 = ?)");)
            {
                ps.setInt(1, FeeCode.TZ_LX);
                ps.setInt(2, FeeCode.TZ_FX);
                ps.setInt(3, FeeCode.TZ_WYJ);
                ps.setInt(4, userId);
                ps.setString(5, T6101_F03.WLZH.name());
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
    }
    
    //优选理财利息加罚息
    @SuppressWarnings("unused")
    private BigDecimal yxlcLxByID(int userId)
        throws SQLException
    {
        BigDecimal lx = new BigDecimal(0);
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT IFNULL(SUM(F07),0)	 FROM S64.T6412 WHERE F09 = 'YH' AND F04=? AND F05=7002");)
            {
                ps.setInt(1, userId);
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
    }
    
    /**
     * 线上债权转让盈亏
     * @return BigDecimal
     * @throws Throwable
     */
    private BigDecimal earnZqzrykById(int userId)
        throws SQLException
    {
        //转入盈亏
        BigDecimal zryk = new BigDecimal(0);
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT IFNULL(SUM(T6262.F08),0)  FROM S62.T6262,S61.T6110 WHERE T6262.F03 = T6110.F01 AND T6110.F01=?");)
            {
                ps.setInt(1, userId);
                try (ResultSet rs = ps.executeQuery();)
                {
                    if (rs.next())
                    {
                        zryk = rs.getBigDecimal(1);
                    }
                    
                }
            }
            //转出盈亏
            BigDecimal zcyk = new BigDecimal(0);
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT IFNULL(SUM(T6262.F09),0)  FROM S62.T6262,S62.T6260,S62.T6251,S61.T6110 WHERE T6251.F04 = T6110.F01 AND  T6251.F01 = T6260.F02 AND T6260.F01 =T6262.F02 AND T6110.F01=?");)
            {
                ps.setInt(1, userId);
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
    }
    
    @Override
    public void export(FundsView[] fundsViews, OutputStream outputStream, String charset)
        throws Throwable
    {
        if (outputStream == null)
        {
            return;
        }
        if (fundsViews == null)
        {
            return;
        }
        if (StringHelper.isEmpty(charset))
        {
            charset = "GBK";
        }
        
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(outputStream, charset)))
        {
            CVSWriter writer = new CVSWriter(out);
            writer.write("序号");
            writer.write("资金账号");
            writer.write("资金账户类型");
            writer.write("用户名");
            writer.write("用户类型");
            writer.write("账户余额(元)");
            writer.write("最后更新时间");
            writer.newLine();
            int index = 1;
            for (FundsView fundsView : fundsViews)
            {
                if (fundsView == null)
                {
                    continue;
                }
                writer.write(index++);
                writer.write(fundsView.F04);
                writer.write(fundsView.F03.getChineseName());
                writer.write(fundsView.F05);
                writer.write(fundsView.userType);
                writer.write(Formater.formatAmount(fundsView.F06));
                writer.write(DateTimeParser.format(fundsView.F07) + "\t");
                writer.newLine();
            }
        }
    }
    
    @Override
    public void exportZj(FundsView[] fundsViews, OutputStream outputStream, String charset)
        throws Throwable
    {
        if (outputStream == null)
        {
            return;
        }
        if (fundsViews == null)
        {
            return;
        }
        if (StringHelper.isEmpty(charset))
        {
            charset = "GBK";
        }
        
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(outputStream, charset)))
        {
            CVSWriter writer = new CVSWriter(out);
            writer.write("序号");
            writer.write("资金账号");
            writer.write("资金账户类型");
            writer.write("用户名");
            writer.write("用户类型");
            writer.write("账户余额(元)");
            writer.write("用户收益(元)");
            writer.write("最后更新时间");
            writer.newLine();
            int index = 1;
            for (FundsView fundsView : fundsViews)
            {
                if (fundsView == null)
                {
                    continue;
                }
                writer.write(index++);
                writer.write(fundsView.F04);
                writer.write(fundsView.F03.getChineseName());
                writer.write(fundsView.F05);
                writer.write(fundsView.userType);
                writer.write(Formater.formatAmount(fundsView.F06));
                if (fundsView.F03 == T6101_F03.SDZH)
                {
                    writer.write("-");
                }
                else
                {
                    writer.write(Formater.formatAmount(fundsView.yhsy));
                }
                writer.write(DateTimeParser.format(fundsView.F07) + "\t");
                writer.newLine();
            }
        }
    }
    
    @Override
    public PagingResult<FundsView> bYSearch(FundsQuery query, Paging paging)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT T6101.F01 AS F01, T6101.F02 AS F02, T6101.F03 AS F03, T6101.F04 AS F04, T6101.F05 AS F05, T6101.F06 AS F06, T6101.F07 AS F07, T6110.F06 AS F08, T6110.F10 AS F09 FROM S61.T6101 INNER JOIN S61.T6110 ON T6101.F02 = T6110.F01 WHERE T6101.F03=?");
        PagingResult<FundsView> pagingResult = null;
        ArrayList<Object> parameters = new ArrayList<Object>();
        parameters.add(T6101_F03.FXBZJZH);
        if (query != null)
        {
            FundAccountType userType = query.getFundAccountType();
            if (userType != null)
            {
                if (userType == FundAccountType.GR)
                {
                    sql.append(" AND T6110.F06 = ? AND T6110.F10 = ?");
                    parameters.add(T6110_F06.ZRR);
                    parameters.add(T6110_F10.F);
                }
                else if (userType == FundAccountType.QY)
                {
                    sql.append(" AND T6110.F06 = ? AND T6110.F10 = ?");
                    parameters.add(T6110_F06.FZRR);
                    parameters.add(T6110_F10.F);
                }
                else if (userType == FundAccountType.JG)
                {
                    sql.append(" AND T6110.F06 = ? AND T6110.F10 = ?");
                    parameters.add(T6110_F06.FZRR);
                    parameters.add(T6110_F10.S);
                }
            }
            String loginName = query.getLoginName();
            if (!StringHelper.isEmpty(loginName))
            {
                sql.append(" AND T6110.F02 LIKE ?");
                parameters.add(getSQLConnectionProvider().allMatch(loginName));
            }
            String userName = query.getUserName();
            if (!StringHelper.isEmpty(userName))
            {
                sql.append(" AND T6101.F04 = ?");
                parameters.add(userName);
            }
        }
        sql.append(" ORDER BY T6101.F07 DESC");
        try (Connection connection = getConnection())
        {
            pagingResult = selectPaging(connection, new ArrayParser<FundsView>()
            {
                
                @Override
                public FundsView[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<FundsView> list = new ArrayList<FundsView>();
                    while (resultSet.next())
                    {
                        FundsView record = new FundsView();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = T6101_F03.parse(resultSet.getString(3));
                        record.F04 = resultSet.getString(4);
                        record.F05 = resultSet.getString(5);
                        record.F06 = resultSet.getBigDecimal(6);
                        record.F07 = resultSet.getTimestamp(7);
                        T6110_F06 t6110_F06 = T6110_F06.parse(resultSet.getString(8));
                        record.isDbf = T6110_F10.parse(resultSet.getString(9));
                        if (t6110_F06 == T6110_F06.ZRR)
                        {
                            record.userType = "个人";
                        }
                        if (t6110_F06 == T6110_F06.FZRR && record.isDbf == T6110_F10.F)
                        {
                            record.userType = "企业";
                        }
                        if (t6110_F06 == T6110_F06.FZRR && record.isDbf == T6110_F10.S)
                        {
                            record.userType = "机构";
                        }
                        list.add(record);
                    }
                    return list.toArray(new FundsView[list.size()]);
                }
            }, paging, sql.toString(), parameters);
            return pagingResult;
        }
    }
    
    @Override
    public AmountTotle getTotle()
        throws Throwable
    {
        AmountTotle amountTotle = new AmountTotle();
        try (Connection connection = getConnection())
        {
            amountTotle.djjeze = selectDjje(connection);
            amountTotle.jkfzze = selectJkfzze(connection);
            amountTotle.zhyeze = selectKyje(connection);
            //amountTotle.yhzsy = selectYhzsy(connection);
            //用户总收益：利息收益+债权转让盈亏
            amountTotle.yhzsy = earnLx().add(earnZqzryk());
            amountTotle.zqzcze = selectZqzcze(connection);
            amountTotle.ptzsy = selectPtzsy(connection);
            return amountTotle;
        }
    }
    
    // 冻结金额总额
    protected BigDecimal selectKyje(Connection connection)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT IFNULL(SUM(F06),0) FROM S61.T6101 WHERE T6101.F03 = ?"))
        {
            pstmt.setString(1, T6101_F03.WLZH.name());
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
    
    // 冻结总金额
    protected BigDecimal selectDjje(Connection connection)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT IFNULL(SUM(F06),0) FROM S61.T6101 WHERE T6101.F03 = ?"))
        {
            pstmt.setString(1, T6101_F03.SDZH.name());
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
    
    // 用户总收益
    protected BigDecimal selectYhzsy(Connection connection)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT IFNULL(SUM(F07),0) FROM S62.T6252 WHERE T6252.F05 IN (?,?,?) AND T6252.F09 = ?"))
        {
            pstmt.setInt(1, FeeCode.TZ_FX);
            pstmt.setInt(2, FeeCode.TZ_WYJ);
            pstmt.setInt(3, FeeCode.TZ_LX);
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
    
    // 平台总收益
    protected BigDecimal selectPtzsy(Connection connection)
        throws SQLException
    {
        int id = 0;
        try (PreparedStatement ps =
            connection.prepareStatement("SELECT T6101.F01 FROM S71.T7101 INNER JOIN S61.T6101 WHERE T7101.F01=T6101.F02 AND T6101.F03=? LIMIT 1"))
        {
            ps.setString(1, T6101_F03.WLZH.name());
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    id = rs.getInt(1);
                }
            }
        }
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT IFNULL(SUM(T6102.F06-T6102.F07),0) FROM S61.T6102 WHERE T6102.F02=?"))
        {
            pstmt.setInt(1, id);
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
    
    // 债权资产总额
    protected BigDecimal selectZqzcze(Connection connection)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT IFNULL(SUM(F07),0) FROM S62.T6252 WHERE T6252.F05 = ? AND T6252.F09 = ?"))
        {
            pstmt.setInt(1, FeeCode.TZ_BJ);
            pstmt.setString(2, T6252_F09.WH.name());
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
    
    // 借款负债总额
    protected BigDecimal selectJkfzze(Connection connection)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT IFNULL(SUM(F07),0) FROM S62.T6252 WHERE T6252.F05 IN (?,?,?) AND T6252.F09 = ?"))
        {
            pstmt.setInt(1, FeeCode.TZ_BJ);
            pstmt.setInt(2, FeeCode.TZ_FX);
            pstmt.setInt(3, FeeCode.TZ_LX);
            pstmt.setString(4, T6252_F09.WH.name());
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
    public PagingResult<FundsView> search(FundsQuery query, T6101_F03 zhlx, Paging paging)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            int userId = getPTID(connection);
            
            StringBuilder sql =
                new StringBuilder(
                    "SELECT T6101.F01 AS F01, T6101.F02 AS F02, T6101.F03 AS F03, T6101.F04 AS F04, T6101.F05 AS F05, T6101.F06 AS F06, T6101.F07 AS F07, T6110.F06 AS F08, T6110.F10 AS F09 FROM S61.T6101 INNER JOIN S61.T6110 ON T6101.F02 = T6110.F01 WHERE T6101.F03 IN(?,?)");
            PagingResult<FundsView> pagingResult = null;
            ArrayList<Object> parameters = new ArrayList<Object>();
            parameters.add(T6101_F03.WLZH);
            parameters.add(T6101_F03.SDZH);
            if (query != null)
            {
                FundAccountType userType = query.getFundAccountType();
                if (userType != null)
                {
                    if (userType == FundAccountType.GR)
                    {
                        sql.append(" AND T6110.F06 = ? AND T6110.F10 = ?");
                        parameters.add(T6110_F06.ZRR);
                        parameters.add(T6110_F10.F);
                    }
                    else if (userType == FundAccountType.QY)
                    {
                        sql.append(" AND T6110.F06 = ? AND T6110.F10 = ? AND T6101.F02 != ?");
                        parameters.add(T6110_F06.FZRR);
                        parameters.add(T6110_F10.F);
                        parameters.add(userId);
                    }
                    else if (userType == FundAccountType.JG)
                    {
                        sql.append(" AND T6110.F06 = ? AND T6110.F10 = ?");
                        parameters.add(T6110_F06.FZRR);
                        parameters.add(T6110_F10.S);
                    }
                    else if (userType == FundAccountType.PT)
                    {
                        sql.append(" AND T6101.F02 = ?");
                        parameters.add(userId);
                    }
                }
                String loginName = query.getLoginName();
                if (!StringHelper.isEmpty(loginName))
                {
                    sql.append(" AND T6110.F02 LIKE ?");
                    parameters.add(getSQLConnectionProvider().allMatch(loginName));
                }
                String userName = query.getUserName();
                if (!StringHelper.isEmpty(userName))
                {
                    sql.append(" AND T6101.F04 LIKE ?");
                    parameters.add(getSQLConnectionProvider().allMatch(userName));
                }
            }
            if (zhlx != null)
            {
                sql.append(" AND T6101.F03 = ?");
                parameters.add(zhlx);
            }
            
            sql.append(" ORDER BY T6101.F07 DESC");
            final int ptAccountId = userId;
            pagingResult = selectPaging(connection, new ArrayParser<FundsView>()
            {
                
                @Override
                public FundsView[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<FundsView> list = new ArrayList<FundsView>();
                    while (resultSet.next())
                    {
                        FundsView record = new FundsView();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = T6101_F03.parse(resultSet.getString(3));
                        record.F04 = resultSet.getString(4);
                        record.F05 = resultSet.getString(5);
                        record.F06 = resultSet.getBigDecimal(6);
                        record.F07 = resultSet.getTimestamp(7);
                        T6110_F06 t6110_F06 = T6110_F06.parse(resultSet.getString(8));
                        record.isDbf = T6110_F10.parse(resultSet.getString(9));
                        if (record.F01 == ptAccountId)
                        {
                            record.yhsy = selectPtzsy(connection);
                        }
                        else
                        {
                            if (T6101_F03.WLZH == record.F03 && t6110_F06 == T6110_F06.ZRR)
                            {
                                //record.yhsy = getYhsy(record.F02);
                                record.yhsy = earnLxById(record.F02).add(earnZqzrykById(record.F02));
                            }
                            else
                            {
                                record.yhsy = BigDecimal.ZERO;
                            }
                        }
                        if (t6110_F06 == T6110_F06.ZRR)
                        {
                            record.userType = "个人";
                        }
                        if (t6110_F06 == T6110_F06.FZRR && record.isDbf == T6110_F10.F)
                        {
                            record.userType = "企业";
                        }
                        if (t6110_F06 == T6110_F06.FZRR && record.isDbf == T6110_F10.S)
                        {
                            record.userType = "机构";
                        }
                        list.add(record);
                    }
                    return list.toArray(new FundsView[list.size()]);
                }
            }, paging, sql.toString(), parameters);
            for (FundsView fundsView : pagingResult.getItems())
            {
                if (fundsView == null)
                {
                    continue;
                }
                if (fundsView.F02 == userId)
                {
                    fundsView.userType = "平台";
                }
            }
            return pagingResult;
        }
    }
    
    /**
     * 利息加罚息
     * @return
     * @throws Throwable
     */
    private BigDecimal earnLx()
        throws Throwable
    {
        BigDecimal lx = new BigDecimal(0);
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT IFNULL(SUM(T6102.F06),0) from S61.T6102 WHERE T6102.F03 IN (?, ?, ?) AND "
                    + "T6102.F02 IN (SELECT T6101.F01 FROM S61.T6101,S61.T6110 WHERE T6101.F02 = T6110.F01 AND T6101.F03 = ? AND T6110.F06 = 'ZRR')");)
            {
                ps.setInt(1, FeeCode.TZ_LX);
                ps.setInt(2, FeeCode.TZ_FX);
                ps.setInt(3, FeeCode.TZ_WYJ);
                //ps.setInt(4, serviceResource.getSession().getAccountId());
                ps.setString(4, T6101_F03.WLZH.name());
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
    }
    
    //优选理财利息加罚息
    @SuppressWarnings("unused")
    private BigDecimal yxlcLx()
        throws SQLException
    {
        BigDecimal lx = new BigDecimal(0);
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT IFNULL(SUM(T6412.F07),0) FROM S64.T6412,S61.T6110 WHERE T6412.F09 = 'YH' AND T6412.F04=T6110.F01 AND T6412.F05=7002 AND T6110.F06='ZRR'"))
            {
                //ps.setInt(1, serviceResource.getSession().getAccountId());
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
    }
    
    /**
     * 线上债权转让盈亏
     * @return BigDecimal
     * @throws Throwable
     */
    private BigDecimal earnZqzryk()
        throws Throwable
    {
        //转入盈亏
        BigDecimal zryk = new BigDecimal(0);
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT IFNULL(SUM(T6262.F08),0)  FROM S62.T6262,S61.T6110 WHERE T6262.F03 = T6110.F01 AND T6110.F06='ZRR'");)
            {
                //ps.setInt(1, serviceResource.getSession().getAccountId());
                try (ResultSet rs = ps.executeQuery();)
                {
                    if (rs.next())
                    {
                        zryk = rs.getBigDecimal(1);
                    }
                    
                }
            }
            //转出盈亏
            BigDecimal zcyk = new BigDecimal(0);
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT IFNULL(SUM(T6262.F09),0)  FROM S62.T6262,S62.T6260,S62.T6251,S61.T6110 WHERE T6251.F04 = T6110.F01 AND  T6251.F01 = T6260.F02 AND T6260.F01 =T6262.F02 AND T6110.F06='ZRR'");)
            {
                //ps.setInt(1, serviceResource.getSession().getAccountId());
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
    }
    
    @Override
    public PagingResult<ZjDetailView> queryGrzjDetail(GrzjDetailQuery query, Paging paging)
        throws Throwable
    {
        boolean platFlg = query.getPlatDetailFlg();
        StringBuilder sql2 = null;
        if (platFlg)
        {
            sql2 = new StringBuilder("SELECT a.*,t.F02 AS F11,t2.F02 AS F12 FROM( ");
        }
        
        StringBuilder sql =
            new StringBuilder(
                "SELECT T6110.F02 AS F01,T6102.F05 AS F02,(SELECT T5122.F02 FROM S51.T5122 WHERE F01 = T6102.F03) AS F03,T6102.F06 AS F04,T6102.F07 AS F05,T6102.F08 AS F06,T6102.F09 AS F07,T6101.F03 AS F08,T6102.F04 AS F09,T6102.F01 AS F10 "
                    + " FROM S61.T6102 INNER JOIN S61.T6101 ON T6101.F01 = T6102.F02 INNER JOIN S61.T6110 ON T6101.F02 = T6110.F01 WHERE 1 = 1");
        List<Object> parameters = new ArrayList<Object>();
        contactQueryGrzjDetailSql(query, sql, parameters);
        try (Connection connection = getConnection())
        {
            if (platFlg)
            {
                sql2.append(sql);
                sql2.append(" ) a LEFT JOIN S61.T6101 t ON t.F01 = a.F09 LEFT JOIN S61.T6110 t2 ON t2.F01 = t.F02 ORDER BY a.F10 DESC");
                return selectPaging(connection, new ArrayParser<ZjDetailView>()
                {
                    @Override
                    public ZjDetailView[] parse(ResultSet resultSet)
                        throws SQLException
                    {
                        ArrayList<ZjDetailView> list = new ArrayList<ZjDetailView>();
                        while (resultSet.next())
                        {
                            ZjDetailView record = new ZjDetailView();
                            record.userName = resultSet.getString(1);
                            record.F05 = resultSet.getTimestamp(2);
                            record.tradingName = resultSet.getString(3);
                            record.F06 = resultSet.getBigDecimal(4);
                            record.F07 = resultSet.getBigDecimal(5);
                            record.F08 = resultSet.getBigDecimal(6);
                            record.F09 = resultSet.getString(7);
                            record.zhlx = EnumParser.parse(T6101_F03.class, resultSet.getString(8));
                            record.assUserName = resultSet.getString(12);
                            list.add(record);
                        }
                        return list.toArray(new ZjDetailView[list.size()]);
                    }
                }, paging, sql2.toString(), parameters);
            }
            else
            {
                sql.append("ORDER BY T6102.F01 DESC");
                
                return selectPaging(connection, new ArrayParser<ZjDetailView>()
                {
                    @Override
                    public ZjDetailView[] parse(ResultSet resultSet)
                        throws SQLException
                    {
                        ArrayList<ZjDetailView> list = new ArrayList<ZjDetailView>();
                        while (resultSet.next())
                        {
                            ZjDetailView record = new ZjDetailView();
                            record.userName = resultSet.getString(1);
                            record.F05 = resultSet.getTimestamp(2);
                            record.tradingName = resultSet.getString(3);
                            record.F06 = resultSet.getBigDecimal(4);
                            record.F07 = resultSet.getBigDecimal(5);
                            record.F08 = resultSet.getBigDecimal(6);
                            record.F09 = resultSet.getString(7);
                            record.zhlx = EnumParser.parse(T6101_F03.class, resultSet.getString(8));
                            list.add(record);
                        }
                        return list.toArray(new ZjDetailView[list.size()]);
                    }
                }, paging, sql.toString(), parameters);
            }
        }
    }
    
    @Override
    public void exportGrzjDetail(ZjDetailView[] paramArrayOfYFundRecord, OutputStream outputStream, String charset)
        throws Throwable
    {
        if (outputStream == null)
        {
            return;
        }
        if (paramArrayOfYFundRecord == null)
        {
            return;
        }
        if (StringHelper.isEmpty(charset))
        {
            charset = "GBK";
        }
        
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(outputStream, charset)))
        {
            CVSWriter writer = new CVSWriter(out);
            writer.write("序号");
            writer.write("用户名");
            writer.write("时间");
            writer.write("类型明细");
            writer.write("账户类型");
            writer.write("收入(元)");
            writer.write("支出(元)");
            writer.write("结余(元)");
            //writer.write("关联用户");
            writer.write("备注");
            writer.newLine();
            int index = 1;
            for (ZjDetailView grzjDetailView : paramArrayOfYFundRecord)
            {
                if (grzjDetailView == null)
                {
                    continue;
                }
                writer.write(index++);
                writer.write(grzjDetailView.userName);
                writer.write(DateTimeParser.format(grzjDetailView.F05) + "\t");
                writer.write(grzjDetailView.tradingName);
                writer.write(grzjDetailView.zhlx.getChineseName());
                writer.write(Formater.formatAmount(grzjDetailView.F06));
                writer.write(Formater.formatAmount(grzjDetailView.F07));
                writer.write(Formater.formatAmount(grzjDetailView.F08));
                //writer.write(StringHelper.isEmpty(grzjDetailView.assUserName)?"":grzjDetailView.assUserName);
                writer.write(grzjDetailView.F09);
                writer.newLine();
            }
        }
    }
    
    @Override
    public void exportPtzjDetail(ZjDetailView[] paramArrayOfYFundRecord, OutputStream outputStream, String charset)
        throws Throwable
    {
        if (outputStream == null)
        {
            return;
        }
        if (paramArrayOfYFundRecord == null)
        {
            return;
        }
        if (StringHelper.isEmpty(charset))
        {
            charset = "GBK";
        }
        
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(outputStream, charset)))
        {
            CVSWriter writer = new CVSWriter(out);
            writer.write("序号");
            writer.write("用户名");
            writer.write("时间");
            writer.write("类型明细");
            writer.write("账户类型");
            writer.write("收入(元)");
            writer.write("支出(元)");
            writer.write("结余(元)");
            writer.write("关联用户");
            writer.write("备注");
            writer.newLine();
            int index = 1;
            for (ZjDetailView grzjDetailView : paramArrayOfYFundRecord)
            {
                if (grzjDetailView == null)
                {
                    continue;
                }
                writer.write(index++);
                writer.write(grzjDetailView.userName);
                writer.write(DateTimeParser.format(grzjDetailView.F05) + "\t");
                writer.write(grzjDetailView.tradingName);
                writer.write(grzjDetailView.zhlx.getChineseName());
                writer.write(Formater.formatAmount(grzjDetailView.F06));
                writer.write(Formater.formatAmount(grzjDetailView.F07));
                writer.write(Formater.formatAmount(grzjDetailView.F08));
                writer.write(grzjDetailView.assUserName);
                writer.write(grzjDetailView.F09);
                writer.newLine();
            }
        }
    }
    
    private void contactQueryGrzjDetailSql(GrzjDetailQuery query, StringBuilder sql, List<Object> parameters)
        throws ResourceNotFoundException, SQLException
    {
        
        if (query != null)
        {
            sql.append(" AND T6110.F06 = ?");
            parameters.add(query.getUserType().name());
            sql.append(" AND T6110.F10 = ?");
            parameters.add(query.getUserDb().name());
            String loginName = query.getLoginName();
            if (!StringHelper.isEmpty(loginName))
            {
                sql.append(" AND T6110.F02 like ?");
                parameters.add(getSQLConnectionProvider().allMatch(loginName));
            }
            int tradingType = query.getTradingType();
            if (tradingType > 0)
            {
                sql.append(" AND T6102.F03 = ?");
                parameters.add(tradingType);
            }
            Date startTime = query.getStartDate();
            if (startTime != null)
            {
                sql.append(" AND DATE(T6102.F05)>=?");
                parameters.add(DateParser.format(startTime));
            }
            Date endTime = query.getEndDate();
            if (endTime != null)
            {
                sql.append(" AND DATE(T6102.F05)<=?");
                parameters.add(DateParser.format(endTime));
            }
            boolean platFlg = query.getPlatDetailFlg();
            if (!platFlg)
            {
                sql.append(" AND T6110.F01 <> (SELECT T7101.F01 FROM S71.T7101 LIMIT 1)");
            }
            else
            {
                sql.append(" AND T6110.F01 = (SELECT T7101.F01 FROM S71.T7101 LIMIT 1)");
            }
            
            if (query.getZhlx() != null)
            {
                sql.append(" AND T6101.F03 = ?");
                parameters.add(query.getZhlx().name());
            }
        }
    }
    
    @Override
    public ZjDetailView queryAmountCount(GrzjDetailQuery query)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT IFNULL(SUM(T6102.F06),0) AS F01,IFNULL(SUM(T6102.F07),0) AS F02"
                    + " FROM S61.T6102 INNER JOIN S61.T6101 ON T6101.F01 = T6102.F02 INNER JOIN S61.T6110 ON T6101.F02 = T6110.F01 WHERE 1 = 1");
        List<Object> parameters = new ArrayList<Object>();
        contactQueryGrzjDetailSql(query, sql, parameters);
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<ZjDetailView>()
            {
                @Override
                public ZjDetailView parse(ResultSet resultSet)
                    throws SQLException
                {
                    ZjDetailView grzjDetailView = new ZjDetailView();
                    if (resultSet.next())
                    {
                        grzjDetailView.F06 = resultSet.getBigDecimal(1);
                        grzjDetailView.F07 = resultSet.getBigDecimal(2);
                    }
                    return grzjDetailView;
                }
            }, sql.toString(), parameters);
        }
    }
}
