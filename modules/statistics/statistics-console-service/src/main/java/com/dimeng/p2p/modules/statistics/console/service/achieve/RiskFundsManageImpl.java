package com.dimeng.p2p.modules.statistics.console.service.achieve;

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

import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.p2p.S61.entities.T6161;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F10;
import com.dimeng.p2p.S61.enums.T6125_F05;
import com.dimeng.p2p.common.enums.OrganizationStatus;
import com.dimeng.p2p.modules.statistics.console.service.RiskFundsManage;
import com.dimeng.p2p.modules.statistics.console.service.entity.Organization;
import com.dimeng.p2p.modules.statistics.console.service.entity.QuarterFunds;
import com.dimeng.p2p.modules.statistics.console.service.entity.Sponsors;
import com.dimeng.p2p.modules.statistics.console.service.entity.YearRiskFunds;
import com.dimeng.util.Formater;
import com.dimeng.util.StringHelper;
import com.dimeng.util.io.CVSWriter;

public class RiskFundsManageImpl extends AbstractStatisticsService implements RiskFundsManage
{
    
    public RiskFundsManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public QuarterFunds[] getQuarterFunds(int orgId, int year)
        throws Throwable
    {
        QuarterFunds[] quarterFunds = new QuarterFunds[4];
        for (int i = 0; i < 4; i++)
        {
            quarterFunds[i] = new QuarterFunds();
        }
        if (year > 0)
        {
            String sql = null;
            List<Object> params = new ArrayList<>();
            if (-1 == orgId)
            {
                sql =
                    "SELECT F02,IFNULL(SUM(F03),0),IFNULL(SUM(F04),0),IFNULL(SUM(F05),0) FROM S70.T7039 WHERE F01=? GROUP BY F02 ORDER BY F02 ASC";
                params.add(year);
            }
            else if (orgId >= 0)
            {
                sql = "SELECT F02,F03,F04,F05 FROM S70.T7039 WHERE F01=? AND F07=? ORDER BY F02 ASC";
                params.add(year);
                params.add(orgId);
            }
            else
            {
                return null;
            }
            try (Connection connection = getConnection())
            {
                try (PreparedStatement ps = connection.prepareStatement(sql))
                {
                    serviceResource.setParameters(ps, params);
                    try (ResultSet resultSet = ps.executeQuery())
                    {
                        while (resultSet.next())
                        {
                            int quarter = resultSet.getInt(1);
                            quarterFunds[quarter - 1].quarter = quarter;
                            quarterFunds[quarter - 1].amountIn = resultSet.getBigDecimal(2);
                            quarterFunds[quarter - 1].amountOut = resultSet.getBigDecimal(3);
                            quarterFunds[quarter - 1].sum = resultSet.getBigDecimal(4);
                        }
                    }
                }
            }
        }
        return quarterFunds;
    }
    
    @Override
    public YearRiskFunds getYearRiskFunds(int orgId, int year)
        throws Throwable
    {
        YearRiskFunds yearRiskFunds = new YearRiskFunds();
        if (year > 0)
        {
            String sql = null;
            List<Object> params = new ArrayList<>();
            if (-1 == orgId)
            {
                sql =
                    "SELECT IFNULL(SUM(F03),0),IFNULL(SUM(F04),0),IFNULL(SUM(F05),0),IFNULL(SUM(F06),0),IFNULL(SUM(F07),0) FROM S70.T7040 WHERE F01=?";
                params.add(year);
            }
            else if (orgId >= 0)
            {
                sql = "SELECT F03,F04,F05,F06,F07 FROM S70.T7040 WHERE F01=? AND F08=?";
                params.add(year);
                params.add(orgId);
            }
            else
            {
                return null;
            }
            try (Connection connection = getConnection())
            {
                try (PreparedStatement ps = connection.prepareStatement(sql))
                {
                    serviceResource.setParameters(ps, params);
                    try (ResultSet resultSet = ps.executeQuery())
                    {
                        if (resultSet.next())
                        {
                            yearRiskFunds.df = resultSet.getBigDecimal(1);
                            yearRiskFunds.dffh = resultSet.getBigDecimal(2);
                            yearRiskFunds.jkcjfwf = resultSet.getBigDecimal(3);
                            yearRiskFunds.sdzjbzj = resultSet.getBigDecimal(4);
                            yearRiskFunds.sdkcbzj = resultSet.getBigDecimal(5);
                        }
                    }
                }
            }
        }
        return yearRiskFunds;
    }
    
    @Override
    public int[] getStatisticedYear()
        throws Throwable
    {
        List<Integer> years = new ArrayList<>();
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement("SELECT DISTINCT(F01) FROM S70.T7039"))
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
    public T6161[] selectT6161()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            ArrayList<T6161> list = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT T6161.F01 AS F01, T6161.F04 AS F02 FROM S61.T6161 INNER JOIN S61.T6110 ON T6161.F01 = T6110.F01 AND T6161.F01 = T6110.F01 WHERE T6110.F10 = ?"))
            {
                pstmt.setString(1, T6110_F10.S.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        T6161 record = new T6161();
                        record.F01 = resultSet.getInt(1);
                        record.F04 = resultSet.getString(2);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return list == null || list.size() == 0 ? null : list.toArray(new T6161[list.size()]);
                }
            }
        }
    }
    
    @Override
    public Organization[] orgList()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            return selectAll(connection, new ArrayParser<Organization>()
            {
                
                @Override
                public Organization[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    List<Organization> orglist = new ArrayList<>();
                    while (resultSet.next())
                    {
                        Organization org = new Organization();
                        org.id = resultSet.getInt(1);
                        org.name = resultSet.getString(2);
                        orglist.add(org);
                    }
                    return orglist.toArray(new Organization[orglist.size()]);
                }
            }, "SELECT F01,F02 FROM S70.T7029 WHERE F05=?", OrganizationStatus.YX.name());
        }
    }
    
    @Override
    public void export(QuarterFunds[] quarterFunds, OutputStream outputStream, String charset)
        throws Throwable
    {
        if (outputStream == null)
        {
            return;
        }
        if (quarterFunds == null || quarterFunds.length <= 0)
        {
            return;
        }
        if (StringHelper.isEmpty(charset))
        {
            charset = "GBK";
        }
        BigDecimal inTotal = new BigDecimal(0);
        BigDecimal outTotal = new BigDecimal(0);
        BigDecimal sumTotal = new BigDecimal(0);
        for (QuarterFunds funds : quarterFunds)
        {
            inTotal = inTotal.add(funds.amountIn);
            outTotal = outTotal.add(funds.amountOut);
            sumTotal = sumTotal.add(funds.sum);
        }
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(outputStream, charset)))
        {
            CVSWriter writer = new CVSWriter(out);
            writer.write("交易类型");
            writer.write("一季度");
            writer.write("二季度");
            writer.write("三季度");
            writer.write("四季度");
            writer.write("合计");
            writer.newLine();
            writer.write("收入(元)");
            writer.write(Formater.formatAmount(quarterFunds[0].amountIn));
            writer.write(Formater.formatAmount(quarterFunds[1].amountIn));
            writer.write(Formater.formatAmount(quarterFunds[2].amountIn));
            writer.write(Formater.formatAmount(quarterFunds[3].amountIn));
            writer.write(Formater.formatAmount(inTotal));
            writer.newLine();
            writer.write("支出(元)");
            writer.write(Formater.formatAmount(quarterFunds[0].amountOut));
            writer.write(Formater.formatAmount(quarterFunds[1].amountOut));
            writer.write(Formater.formatAmount(quarterFunds[2].amountOut));
            writer.write(Formater.formatAmount(quarterFunds[3].amountOut));
            writer.write(Formater.formatAmount(outTotal));
            writer.newLine();
            writer.write("盈亏(元)");
            writer.write(Formater.formatAmount(quarterFunds[0].sum));
            writer.write(Formater.formatAmount(quarterFunds[1].sum));
            writer.write(Formater.formatAmount(quarterFunds[2].sum));
            writer.write(Formater.formatAmount(quarterFunds[3].sum));
            writer.write(Formater.formatAmount(sumTotal));
        }
    }
    
    public static class RiskFundsManageFactory implements ServiceFactory<RiskFundsManage>
    {
        
        @Override
        public RiskFundsManage newInstance(ServiceResource serviceResource)
        {
            return new RiskFundsManageImpl(serviceResource);
        }
        
    }
    
    @Override
    public Sponsors[] selectSponsors()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            ArrayList<Sponsors> list = null;
            Sponsors record = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT T6110.F01 F01,T6110.F02 F02,T6141.F02 F03 FROM S61.T6125 JOIN S61.T6110 ON T6125.F02=T6110.F01 JOIN S61.T6141 ON T6141.F01=T6110.F01 WHERE T6110.F06=? AND T6125.F05 IN(?,?)"))
            {
                pstmt.setString(1, T6110_F06.ZRR.name());
                pstmt.setString(2, T6125_F05.SQCG.name());
                pstmt.setString(3, T6125_F05.QXCG.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        record = new Sponsors();
                        record.sponsorsId = resultSet.getInt(1);
                        record.sponsorsName = resultSet.getString(2) + "/" + resultSet.getString(3);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    
                }
            }
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT T6161.F01 F01,T6161.F04 F02 FROM S61.T6161 JOIN S61.T6125 ON T6125.F02=T6161.F01 JOIN S61.T6110 ON T6110.F01=T6125.F02 WHERE T6125.F05 IN(?,?) AND T6110.F10=?"))
            {
                pstmt.setString(1, T6125_F05.SQCG.name());
                pstmt.setString(2, T6125_F05.QXCG.name());
                pstmt.setString(3, T6110_F10.F.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        record = new Sponsors();
                        record.sponsorsId = resultSet.getInt(1);
                        record.sponsorsName = resultSet.getString(2);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                }
            }
            return list == null || list.size() == 0 ? null : list.toArray(new Sponsors[list.size()]);
        }
    }
    
}
