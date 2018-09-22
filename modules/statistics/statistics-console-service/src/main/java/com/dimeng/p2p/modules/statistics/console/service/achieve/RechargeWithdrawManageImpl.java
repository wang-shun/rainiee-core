package com.dimeng.p2p.modules.statistics.console.service.achieve;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.modules.statistics.console.service.RechargeWithdrawManage;
import com.dimeng.p2p.modules.statistics.console.service.entity.RecWit;
import com.dimeng.p2p.modules.statistics.console.service.entity.RecWitReport;
import com.dimeng.p2p.modules.statistics.console.service.entity.RecWitReportStatistics;
import com.dimeng.p2p.modules.statistics.console.service.entity.RecWitTotal;
import com.dimeng.p2p.modules.statistics.console.service.query.RecWitReportQuery;
import com.dimeng.util.StringHelper;
import com.dimeng.util.io.CVSWriter;

public class RechargeWithdrawManageImpl extends AbstractStatisticsService implements RechargeWithdrawManage
{
    
    public RechargeWithdrawManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public int[] getStatisticedYear()
        throws Throwable
    {
        List<Integer> years = new ArrayList<>();
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement("SELECT DISTINCT(F01) FROM S70.T7041"))
            {
                try (ResultSet resultSet = ps.executeQuery())
                {
                    while (resultSet.next())
                    {
                        years.add(resultSet.getInt(1));
                    }
                }
            }
        }
        int[] ys = new int[years.size()];
        for (int i = 0; i < years.size(); i++)
        {
            ys[i] = years.get(i);
        }
        return ys;
    }
    
    @Override
    public RecWit[] getRechargeWithdraws(int year)
        throws Throwable
    {
        RecWit[] rws = new RecWit[12];
        for (int i = 0; i < 12; i++)
        {
            rws[i] = new RecWit();
        }
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT F02,F03,F04,F05,F06 FROM S70.T7041 WHERE F01=?"))
            {
                ps.setInt(1, year);
                try (ResultSet resultSet = ps.executeQuery())
                {
                    while (resultSet.next())
                    {
                        int month = resultSet.getInt(1);
                        if (month >= 1 && month <= 12)
                        {
                            rws[month - 1].month = month;
                            rws[month - 1].recharge = resultSet.getBigDecimal(2);
                            rws[month - 1].withdraw = resultSet.getBigDecimal(3);
                            rws[month - 1].rechargeCount = resultSet.getInt(4);
                            rws[month - 1].withdrawCount = resultSet.getInt(5);
                        }
                    }
                }
            }
        }
        return rws;
    }
    
    @Override
    public RecWitTotal getRecWitTotal(int year)
        throws Throwable
    {
        RecWitTotal recWitTotal = new RecWitTotal();
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT SUM(F03),SUM(F04),SUM(F05),SUM(F06) FROM S70.T7041 WHERE F01=?"))
            {
                ps.setInt(1, year);
                try (ResultSet resultSet = ps.executeQuery())
                {
                    if (resultSet.next())
                    {
                        recWitTotal.recharge = resultSet.getBigDecimal(1);
                        recWitTotal.withdraw = resultSet.getBigDecimal(2);
                        recWitTotal.rechargeCount = resultSet.getInt(3);
                        recWitTotal.withdrawCount = resultSet.getInt(4);
                    }
                }
            }
        }
        return recWitTotal;
    }
    
    @Override
    public void export(RecWit[] recWits, RecWitTotal recWitTotal, OutputStream outputStream, String charset)
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
            writer.write("月份");
            writer.write("充值(元)");
            writer.write("提现(元)");
            writer.write("充值笔数");
            writer.write("提现笔数");
            writer.newLine();
            int i = 0;
            for (RecWit recWit : recWits)
            {
                i++;
                switch (i)
                {
                    case 1:
                        writer.write("一月份");
                        break;
                    case 2:
                        writer.write("二月份");
                        break;
                    case 3:
                        writer.write("三月份");
                        break;
                    case 4:
                        writer.write("四月份");
                        break;
                    case 5:
                        writer.write("五月份");
                        break;
                    case 6:
                        writer.write("六月份");
                        break;
                    case 7:
                        writer.write("七月份");
                        break;
                    case 8:
                        writer.write("八月份");
                        break;
                    case 9:
                        writer.write("九月份");
                        break;
                    case 10:
                        writer.write("十月份");
                        break;
                    case 11:
                        writer.write("十一月份");
                        break;
                    case 12:
                        writer.write("十二月份");
                        break;
                    default:
                        break;
                }
                writer.write(recWit.recharge.toString());
                writer.write(recWit.withdraw.toString());
                writer.write(Integer.toString(recWit.rechargeCount));
                writer.write(Integer.toString(recWit.withdrawCount));
                writer.newLine();
            }
            writer.write("总计");
            writer.write(recWitTotal.recharge.toString());
            writer.write(recWitTotal.withdraw.toString());
            writer.write(Integer.toString(recWitTotal.rechargeCount));
            writer.write(Integer.toString(recWitTotal.withdrawCount));
        }
    }
    
    public static class RechargeWithdrawManageFactory implements ServiceFactory<RechargeWithdrawManage>
    {
        
        @Override
        public RechargeWithdrawManage newInstance(ServiceResource serviceResource)
        {
            return new RechargeWithdrawManageImpl(serviceResource);
        }
        
    }
    
    /** {@inheritDoc} */
    
    @Override
    public PagingResult<RecWitReport> getRecWitReports(RecWitReportQuery query, Paging paging)
        throws Throwable
    {
        ArrayList<Object> parameters = new ArrayList<>();
        StringBuffer sql =
            new StringBuffer("SELECT COUNT(DISTINCT t.F01),IFNULL(SUM(t.F02),0),t.F03,t.F04,COUNT(1) F05,");
        sql.append("CASE t.F04 WHEN 'charge' THEN (SELECT IFNULL(SUM(T6502.F03),0) FROM S65.T6501 INNER JOIN S65.T6502 ON T6501.F01 = T6502.F01 WHERE DATE(T6501.F06)=t.F03 AND T6501.F03 = 'CG' AND T6502.F02 <> (SELECT F01 FROM S71.T7101 LIMIT 1)) ELSE 0 END AS F06,");
        sql.append("CASE t.F04 WHEN 'charge' THEN (SELECT IFNULL(SUM(T7150.F04),0) FROM S71.T7150 WHERE DATE(T7150.F10)=t.F03 AND T7150.F05='YCZ' AND T7150.F02 <> (SELECT F01 FROM S71.T7101 LIMIT 1)) ELSE 0 END AS F07  ");
        sql.append("FROM (SELECT T6502.F02 F01,T6502.F03 F02,DATE(T6501.F06) F03,'charge' F04  FROM S65.T6501 ");
        sql.append("INNER JOIN S65.T6502 ON T6501.F01 = T6502.F01 ");
        sql.append("WHERE T6501.F03 = 'CG' AND T6502.F02 <> (SELECT F01 FROM S71.T7101 LIMIT 1) ");
        sql.append("UNION ALL ");
        sql.append("SELECT T7150.F02 F01,T7150.F04 F02,DATE(T7150.F10) F03,'charge' F04  FROM S71.T7150 ");
        sql.append("WHERE T7150.F05='YCZ' AND T7150.F02 <> (SELECT F01 FROM S71.T7101 LIMIT 1) ");
        sql.append("UNION ALL ");
        sql.append("SELECT T6503.F02 F01,T6503.F03 F02,DATE(T6501.F06) F03,'withdraw' F04 FROM S65.T6503 ");
        sql.append("INNER JOIN S65.T6501 ON T6503.F01=T6501.F01 ");
        sql.append("WHERE T6501.F03 = 'CG' AND T6503.F02 <> (SELECT F01 FROM S71.T7101 LIMIT 1) ) t  WHERE 1=1 ");
        
        if (query != null)
        {
            Date time = query.getStartTime();
            if (time != null)
            {
                sql.append(" AND t.F03 >= ?");
                parameters.add(time);
            }
            time = query.getEndTime();
            if (time != null)
            {
                sql.append(" AND t.F03 <= ?");
                parameters.add(time);
            }
            
        }
        sql.append(" GROUP BY t.F03,t.F04");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<RecWitReport>()
            {
                
                @Override
                public RecWitReport[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<RecWitReport> list = null;
                    RecWitReport record = null;
                    while (resultSet.next())
                    {
                        record = new RecWitReport();
                        record.userCount = resultSet.getInt(1);
                        record.amount = resultSet.getBigDecimal(2);
                        record.date = resultSet.getDate(3);
                        record.type = resultSet.getString(4);
                        record.typeCount = resultSet.getInt(5);
                        record.onLineRecharge = resultSet.getBigDecimal(6);
                        record.offLineRecharge = resultSet.getBigDecimal(7);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return ((list == null || list.size() == 0) ? null : list.toArray(new RecWitReport[list.size()]));
                }
            }, paging, sql.toString(), parameters);
        }
    }
    
    /** {@inheritDoc} */
    
    @Override
    public RecWitReportStatistics getStatistics(RecWitReportQuery query)
        throws Throwable
    {
        
        ArrayList<Object> parameters = new ArrayList<>();
        StringBuffer sql = new StringBuffer("SELECT  IFNULL(SUM(t.F02),0),t.F04 ");
        sql.append("FROM (SELECT T6502.F03 F02,DATE(T6501.F06) F03,'xs' F04  FROM S65.T6501 ");
        sql.append("INNER JOIN S65.T6502 ON T6501.F01 = T6502.F01 ");
        sql.append("WHERE T6501.F03 = 'CG' AND T6502.F02 <> (SELECT F01 FROM S71.T7101 LIMIT 1) ");
        sql.append("UNION ALL ");
        sql.append("SELECT T7150.F04 F02,DATE(T7150.F10) F03,'xx' F04  FROM S71.T7150 ");
        sql.append("WHERE T7150.F05='YCZ' AND T7150.F02 <> (SELECT F01 FROM S71.T7101 LIMIT 1) ");
        sql.append("UNION ALL ");
        sql.append("SELECT T6503.F03 F02,DATE(T6501.F06) F03,'tx' F04 FROM S65.T6503 ");
        sql.append("INNER JOIN S65.T6501 ON T6503.F01=T6501.F01 ");
        sql.append("WHERE T6501.F03 = 'CG' AND T6503.F02 <> (SELECT F01 FROM S71.T7101 LIMIT 1) ) t WHERE 1=1 ");
        if (query != null)
        {
            Date time = query.getStartTime();
            if (time != null)
            {
                sql.append(" AND t.F03 >= ?");
                parameters.add(time);
            }
            time = query.getEndTime();
            if (time != null)
            {
                sql.append(" AND t.F03 <= ?");
                parameters.add(time);
            }
        }
        sql.append(" GROUP BY t.F04");
        
        // sql语句和查询参数处理
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<RecWitReportStatistics>()
            {
                @Override
                public RecWitReportStatistics parse(ResultSet resultSet)
                    throws SQLException
                {
                    RecWitReportStatistics statistics = new RecWitReportStatistics();
                    BigDecimal amount = BigDecimal.ZERO;
                    String type = "";
                    while (resultSet.next())
                    {
                        amount = resultSet.getBigDecimal(1);
                        type = resultSet.getString(2);
                        if ("xs".equals(type))
                        {
                            statistics.onLineRecharge = amount;
                        }
                        else if ("xx".equals(type))
                        {
                            statistics.offLineRecharge = amount;
                        }
                        else if ("tx".equals(type))
                        {
                            statistics.withdrawSum = amount;
                        }
                    }
                    statistics.rechargeSum = statistics.onLineRecharge.add(statistics.offLineRecharge);
                    return statistics;
                }
            }, sql.toString(), parameters);
        }
    }
    
    @Override
    public void exportStatistics(RecWitReport[] recWitReports, OutputStream outputStream, String charset)
        throws Throwable
    {
        if (outputStream == null)
        {
            return;
        }
        if (recWitReports == null)
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
            writer.write("日期");
            writer.write("类型");
            writer.write("总金额(元)");
            writer.write("笔数");
            writer.write("用户 ");
            writer.write("线上(元)");
            writer.write("线下(元)");
            writer.newLine();
            int index = 1;
            for (RecWitReport recWitReport : recWitReports)
            {
                writer.write(index++);
                writer.write(recWitReport.date);
                writer.write(recWitReport.type.equals(String.valueOf(FeeCode.TX)) ? "提现" : "充值");
                writer.write(recWitReport.amount);
                writer.write(recWitReport.typeCount);
                writer.write(recWitReport.userCount);
                writer.write(recWitReport.onLineRecharge);
                writer.write(recWitReport.offLineRecharge);
                writer.newLine();
            }
        }
    }
}
