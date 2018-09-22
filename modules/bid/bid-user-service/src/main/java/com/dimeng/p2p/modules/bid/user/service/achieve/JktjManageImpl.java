package com.dimeng.p2p.modules.bid.user.service.achieve;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.S62.enums.T6252_F09;
import com.dimeng.p2p.modules.bid.user.service.JktjManage;
import com.dimeng.p2p.modules.bid.user.service.entity.CreditTotal;
import com.dimeng.p2p.modules.bid.user.service.entity.MonthCreditTotal;
import com.dimeng.p2p.modules.bid.user.service.entity.NewCreditList;
import com.dimeng.p2p.modules.bid.user.service.entity.NewCreditTotal;
import com.dimeng.p2p.modules.bid.user.service.entity.NotPayCreditTotal;
import com.dimeng.p2p.modules.bid.user.service.entity.PayCreditTotal;
import com.dimeng.p2p.modules.bid.user.service.entity.YearCreditTotal;
import com.dimeng.p2p.modules.bid.user.service.query.NewCreditListQuery;
import com.dimeng.util.StringHelper;
import com.dimeng.util.io.CVSWriter;

public class JktjManageImpl extends AbstractBidManage implements JktjManage
{
    
    public JktjManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public CreditTotal getCreditTotal()
        throws Throwable
    {
        CreditTotal creditTotal = new CreditTotal();
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT IFNULL(SUM(F05-F07),0) FROM S62.T6230 WHERE T6230.F02 = ? AND T6230.F20 IN (?,?,?)"))
            {
                ps.setInt(1, serviceResource.getSession().getAccountId());
                ps.setString(2, T6230_F20.HKZ.name());
                ps.setString(3, T6230_F20.YJQ.name());
                ps.setString(4, T6230_F20.YDF.name());
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        creditTotal.total = rs.getBigDecimal(1);
                    }
                }
                creditTotal.totalPay =
                    getYjlxje(connection, FeeCode.TZ_BJ).add(getYjlxje(connection, FeeCode.TZ_LX))
                        .add(getYjlxje(connection, FeeCode.TZ_FX))
                        .add(getYjlxje(connection, FeeCode.TZ_WYJ))
                        .add(getYjlxje(connection, FeeCode.JK_GLF));
                creditTotal.accMoney = getYjlxje(connection, FeeCode.TZ_LX);
                creditTotal.arrMoney = getYjlxje(connection, FeeCode.TZ_FX);
                creditTotal.manageMoney = getYjlxje(connection, FeeCode.JK_GLF);
            }
        }
        return creditTotal;
    }
    
    /**
     * 根据类型查询总还款金额
     * @param loanId
     * @return
     * @throws Throwable
     */
    private BigDecimal getYjlxje(Connection connection, int feeCode)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT IFNULL(SUM(F07),0) FROM S62.T6252 WHERE F03 = ? AND F05  = ?"))
        {
            pstmt.setInt(1, serviceResource.getSession().getAccountId());
            pstmt.setInt(2, feeCode);
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
     * 根据类型和还款状态查询还款金额()
     * @param loanId
     * @return
     * @throws Throwable
     */
    private BigDecimal getYhlxje(Connection connection, int feeCode, T6252_F09 f09)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT IFNULL(SUM(F07),0) FROM S62.T6252 WHERE F03 = ? AND F05  = ? AND F09  = ?"))
        {
            pstmt.setInt(1, serviceResource.getSession().getAccountId());
            pstmt.setInt(2, feeCode);
            pstmt.setString(3, f09.name());
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
    public PayCreditTotal getPayCreditTotal()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            PayCreditTotal total = new PayCreditTotal();
            total.payBeforeMoney = getYhlxje(connection, FeeCode.TZ_WYJ, T6252_F09.YH);
            total.payMoney = getYhlxje(connection, FeeCode.TZ_BJ, T6252_F09.YH);
            total.payAccMoney = getYhlxje(connection, FeeCode.TZ_LX, T6252_F09.YH);
            total.payArrMoney = getYhlxje(connection, FeeCode.TZ_FX, T6252_F09.YH);
            total.payManageMoney = getYhlxje(connection, FeeCode.JK_GLF, T6252_F09.YH);
            total.payTotalMoney =
                total.payMoney.add(total.payAccMoney)
                    .add(total.payArrMoney)
                    .add(total.payBeforeMoney)
                    .add(total.payManageMoney);
            return total;
        }
        
    }
    
    @Override
    public YearCreditTotal[] getYearCreditTotal()
        throws Throwable
    {
        List<YearCreditTotal> totals = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT F02,F03,F04 FROM S61.T6191 WHERE F01=? ORDER BY F02 DESC, F03 ASC LIMIT 0,4"))
            {
                ps.setInt(1, serviceResource.getSession().getAccountId());
                try (ResultSet resultSet = ps.executeQuery())
                {
                    while (resultSet.next())
                    {
                        if (totals == null)
                        {
                            totals = new ArrayList<>();
                        }
                        YearCreditTotal total = new YearCreditTotal();
                        total.year = resultSet.getInt(1);
                        total.quarter = resultSet.getInt(2);
                        total.money = resultSet.getBigDecimal(3);
                        totals.add(total);
                    }
                }
            }
        }
        return totals == null ? null : totals.toArray(new YearCreditTotal[totals.size()]);
    }
    
    @Override
    public NotPayCreditTotal getNotPayCreditTotal()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            NotPayCreditTotal total = new NotPayCreditTotal();
            total.notPayMoney = getYhlxje(connection, FeeCode.TZ_BJ, T6252_F09.WH);
            total.notPayAccMoney = getYhlxje(connection, FeeCode.TZ_LX, T6252_F09.WH);
            total.notPayArrMoney = getYhlxje(connection, FeeCode.TZ_FX, T6252_F09.WH);
            total.notPayManageMoney = getYhlxje(connection, FeeCode.JK_GLF, T6252_F09.WH);
            total.notPayTotal =
                total.notPayMoney.add(total.notPayAccMoney).add(total.notPayArrMoney).add(total.notPayManageMoney);
            return total;
        }
    }
    
    @Override
    public MonthCreditTotal[] getMonthCreditTotal()
        throws Throwable
    {
        List<MonthCreditTotal> totals = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT F02,F03,F04 FROM S61.T6192 WHERE F01=? ORDER BY F02 DESC, F03 ASC LIMIT 0,6"))
            {
                ps.setInt(1, serviceResource.getSession().getAccountId());
                try (ResultSet resultSet = ps.executeQuery())
                {
                    if (resultSet.next())
                    {
                        if (totals == null)
                        {
                            totals = new ArrayList<>();
                        }
                        MonthCreditTotal total = new MonthCreditTotal();
                        total.year = resultSet.getInt(1);
                        total.month = resultSet.getInt(2);
                        total.money = resultSet.getBigDecimal(3);
                        totals.add(total);
                    }
                }
            }
        }
        return totals == null ? null : totals.toArray(new MonthCreditTotal[totals.size()]);
    }
    
    @Override
    public BigDecimal getLgyjk()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT IFNULL(SUM(T6230.F26),0)  AS F01 FROM S62.T6230 INNER JOIN S62.T6231 ON T6230.F01 = T6231.F01 WHERE T6230.F02 = ? AND T6230.F20 IN (?,?,?) AND ADDDATE(T6231.F12,INTERVAL 6 MONTH) > ? LIMIT 1"))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                pstmt.setString(2, T6230_F20.HKZ.name());
                pstmt.setString(3, T6230_F20.YJQ.name());
                pstmt.setString(4, T6230_F20.YDF.name());
                pstmt.setDate(5, getCurrentDate(connection));
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return resultSet.getBigDecimal(1);
                    }
                }
            }
        }
        return new BigDecimal(0);
    }
    
    @Override
    public BigDecimal getYnjk()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT IFNULL(SUM(T6230.F26),0)  AS F01 FROM S62.T6230 INNER JOIN S62.T6231 ON T6230.F01 = T6231.F01 WHERE T6230.F02 = ? AND T6230.F20 IN (?,?,?) AND ADDDATE(T6231.F12,INTERVAL 1 YEAR) > ? LIMIT 1"))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                pstmt.setString(2, T6230_F20.HKZ.name());
                pstmt.setString(3, T6230_F20.YJQ.name());
                pstmt.setString(4, T6230_F20.YDF.name());
                pstmt.setDate(5, getCurrentDate(connection));
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return resultSet.getBigDecimal(1);
                    }
                }
            }
        }
        return new BigDecimal(0);
    }
    
    @Override
    public PagingResult<NewCreditList> getNewCreditList(final NewCreditListQuery query, Paging paging)
        throws Throwable
    {
        
        StringBuffer sqlStr = new StringBuffer();
        sqlStr.append(" SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11, F12, F13, F14 FROM S70.T7204 WHERE F13 = ? ");
        
        List<Object> parameters = new ArrayList<>();
        parameters.add(serviceResource.getSession().getAccountId());
        if (query != null)
        {
            if (query.getYear() > 0)
            {
                sqlStr.append(" AND F01 = ? ");
                parameters.add(query.getYear());
            }
            
            if (query.getMonth() > 0)
            {
                sqlStr.append(" AND F02 = ? ");
                parameters.add(query.getMonth());
            }
        }
        sqlStr.append(" ORDER BY F01 DESC,F02 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<NewCreditList>()
            {
                @Override
                public NewCreditList[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    List<NewCreditList> list = new ArrayList<NewCreditList>();
                    while (resultSet.next())
                    {
                        NewCreditList entity = new NewCreditList();
                        entity.year = resultSet.getInt(1);
                        entity.month = resultSet.getInt(2);
                        entity.loanMoney = resultSet.getBigDecimal(3);
                        entity.loanInterest = resultSet.getBigDecimal(4);
                        entity.manageMoney = resultSet.getBigDecimal(5);
                        entity.payMoney = resultSet.getBigDecimal(6);
                        entity.payInterest = resultSet.getBigDecimal(7);
                        entity.payDefaultIns = resultSet.getBigDecimal(8);
                        entity.deditMoney = resultSet.getBigDecimal(9);
                        entity.notPayMoney = resultSet.getBigDecimal(10);
                        entity.notPayInterest = resultSet.getBigDecimal(11);
                        entity.notPayDefaultIns = resultSet.getBigDecimal(12);
                        entity.prepaymentFee = resultSet.getBigDecimal(14);
                        entity.payMoneyTotal =
                            entity.payMoney.add(entity.payInterest)
                                .add(entity.payDefaultIns)
                                .add(entity.deditMoney)
                                .add(entity.prepaymentFee);
                        entity.notPayMoneyTotal =
                            entity.notPayMoney.add(entity.notPayInterest).add(entity.notPayDefaultIns);
                        list.add(entity);
                    }
                    return list.toArray(new NewCreditList[list.size()]);
                }
            },
                paging,
                sqlStr.toString(),
                parameters);
        }
    }
    
    @Override
    public NewCreditTotal getNewCreditTotal(NewCreditListQuery query)
        throws Throwable
    {
        StringBuffer sqlStr = new StringBuffer();
        sqlStr.append(" SELECT IFNULL(SUM(F03),0), IFNULL(SUM(F04),0), IFNULL(SUM(F05),0), IFNULL(SUM(F06),0), IFNULL(SUM(F07),0), IFNULL(SUM(F08),0), IFNULL(SUM(F09),0), IFNULL(SUM(F10),0), IFNULL(SUM(F11),0), IFNULL(SUM(F12),0), IFNULL(SUM(F14),0) FROM S70.T7204 WHERE F13 = ? ");
        
        List<Object> parameters = new ArrayList<>();
        parameters.add(serviceResource.getSession().getAccountId());
        if (query != null)
        {
            if (query.getYear() > 0)
            {
                sqlStr.append(" AND F01 = ? ");
                parameters.add(query.getYear());
            }
            
            if (query.getMonth() > 0)
            {
                sqlStr.append(" AND F02 = ? ");
                parameters.add(query.getMonth());
            }
        }
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<NewCreditTotal>()
            {
                @Override
                public NewCreditTotal parse(ResultSet resultSet)
                    throws SQLException
                {
                    NewCreditTotal entity = new NewCreditTotal();
                    while (resultSet.next())
                    {
                        entity.loanTotal = resultSet.getBigDecimal(1);
                        entity.interestTotal = resultSet.getBigDecimal(2);
                        entity.manageTotal = resultSet.getBigDecimal(3);
                        
                        BigDecimal payMoney = resultSet.getBigDecimal(4);
                        BigDecimal payInterest = resultSet.getBigDecimal(5);
                        BigDecimal payDefaultIns = resultSet.getBigDecimal(6);
                        BigDecimal deditMoney = resultSet.getBigDecimal(7);
                        BigDecimal notPayMoney = resultSet.getBigDecimal(8);
                        BigDecimal notPayInterest = resultSet.getBigDecimal(9);
                        BigDecimal notPayDefaultIns = resultSet.getBigDecimal(10);
                        BigDecimal prepayFee = resultSet.getBigDecimal(11);
                        entity.payTotal = payMoney.add(payInterest).add(payDefaultIns).add(deditMoney).add(prepayFee);
                        entity.notPayTotal = notPayMoney.add(notPayInterest).add(notPayDefaultIns);
                    }
                    return entity;
                }
            }, sqlStr.toString(), parameters);
        }
    }
    
    @Override
    public void export(NewCreditList[] recWits, OutputStream outputStream, String charset)
        throws Throwable
    {
        if (outputStream == null)
        {
            return;
        }
        if (recWits == null || recWits.length <= 0)
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
            writer.write("年");
            writer.write("月");
            writer.write("借款金额(元)");
            writer.write("借款利息(元)");
            writer.write("借款管理费(元)");
            writer.write("已还本金(元)");
            writer.write("已还利息(元)");
            writer.write("已还逾期罚息(元)");
            writer.write("违约金(元)");
            writer.write("待还本金(元)");
            writer.write("待还利息(元)");
            writer.write("待还逾期罚息(元)");
            
            writer.newLine();
            int i = 0;
            for (NewCreditList recWit : recWits)
            {
                i++;
                writer.write(i);
                writer.write(recWit.year);
                writer.write(recWit.month);
                writer.write(recWit.loanMoney);
                writer.write(recWit.loanInterest);
                writer.write(recWit.manageMoney);
                writer.write(recWit.payMoney);
                writer.write(recWit.payInterest);
                writer.write(recWit.payDefaultIns);
                writer.write(recWit.deditMoney);
                writer.write(recWit.notPayMoney);
                writer.write(recWit.notPayInterest);
                writer.write(recWit.notPayDefaultIns);
                writer.newLine();
            }
        }
    }
}
