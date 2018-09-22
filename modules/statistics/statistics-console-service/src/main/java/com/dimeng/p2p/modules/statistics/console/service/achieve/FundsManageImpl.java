package com.dimeng.p2p.modules.statistics.console.service.achieve;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.modules.statistics.console.service.FundsManage;
import com.dimeng.p2p.modules.statistics.console.service.entity.QuarterFunds;
import com.dimeng.p2p.modules.statistics.console.service.entity.YearFunds;
import com.dimeng.util.StringHelper;
import com.dimeng.util.io.CVSWriter;

public class FundsManageImpl extends AbstractStatisticsService implements FundsManage
{
    
    public FundsManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public QuarterFunds[] getQuarterFunds(int year)
        throws Throwable
    {
        QuarterFunds[] quarterFunds = new QuarterFunds[4];
        for (int i = 0; i < 4; i++)
        {
            quarterFunds[i] = new QuarterFunds();
        }
        if (year > 0)
        {
            try (Connection connection = getConnection())
            {
                try (PreparedStatement ps =
                    connection.prepareStatement("SELECT F02,F03,F04,F05 FROM S70.T7037 WHERE F01=? ORDER BY F02 ASC"))
                {
                    ps.setInt(1, year);
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
    public YearFunds getYearFunds(int year)
        throws Throwable
    {
        YearFunds yearFunds = new YearFunds();
        if (year > 0)
        {
            try (Connection connection = getConnection())
            {
                try (PreparedStatement ps =
                    connection.prepareStatement("SELECT F03,F04,F05,F06,F07,F08,F09,F10,F11,F12,F13,F14,F15,F16,F17,F18,F19,F20,F21,F22,F23,F24,F25,F26 FROM S70.T7038 WHERE F01=?"))
                {
                    ps.setInt(1, year);
                    try (ResultSet resultSet = ps.executeQuery())
                    {
                        if (resultSet.next())
                        {
                            yearFunds.czsxf = resultSet.getBigDecimal(1);
                            yearFunds.txsxf = resultSet.getBigDecimal(2);
                            yearFunds.czcb = resultSet.getBigDecimal(3);
                            yearFunds.txcb = resultSet.getBigDecimal(4);
                            yearFunds.sfyzsxf = resultSet.getBigDecimal(5);
                            yearFunds.jkglf = resultSet.getBigDecimal(6);
                            yearFunds.yqglf = resultSet.getBigDecimal(7);
                            yearFunds.zqzrf = resultSet.getBigDecimal(8);
                            yearFunds.hdfy = resultSet.getBigDecimal(9);
                            yearFunds.lcglf = resultSet.getBigDecimal(10);
                            yearFunds.cxtgfy = resultSet.getBigDecimal(11);
                            yearFunds.yxtgfy = resultSet.getBigDecimal(12);
                            yearFunds.wyjsxf = resultSet.getBigDecimal(13);
                            yearFunds.bjdffh = resultSet.getBigDecimal(14);
                            yearFunds.lxdffh = resultSet.getBigDecimal(15);
                            yearFunds.fxdffh = resultSet.getBigDecimal(16);
                            yearFunds.bjdfzc = resultSet.getBigDecimal(17);
                            yearFunds.lxdfzc = resultSet.getBigDecimal(18);
                            yearFunds.fxdfzc = resultSet.getBigDecimal(19);
                            yearFunds.jxjlfy = resultSet.getBigDecimal(20);
                            yearFunds.tyjtzfy = resultSet.getBigDecimal(21);
                            yearFunds.hbjlfy = resultSet.getBigDecimal(22);
                            yearFunds.jlbjlfy = resultSet.getBigDecimal(23);
                            yearFunds.xxcz = resultSet.getBigDecimal(24);
                        }
                    }
                }
            }
        }
        return yearFunds;
    }
    
    @Override
    public int[] getStatisticedYear()
        throws Throwable
    {
        List<Integer> years = new ArrayList<>();
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement("SELECT DISTINCT(F01) FROM S70.T7037"))
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
            writer.write(quarterFunds[0].amountIn.toString());
            writer.write(quarterFunds[1].amountIn.toString());
            writer.write(quarterFunds[2].amountIn.toString());
            writer.write(quarterFunds[3].amountIn.toString());
            writer.write(inTotal.toString());
            writer.newLine();
            writer.write("支出(元)");
            writer.write(quarterFunds[0].amountOut.toString());
            writer.write(quarterFunds[1].amountOut.toString());
            writer.write(quarterFunds[2].amountOut.toString());
            writer.write(quarterFunds[3].amountOut.toString());
            writer.write(outTotal.toString());
            writer.newLine();
            writer.write("盈亏(元)");
            writer.write(quarterFunds[0].sum.toString());
            writer.write(quarterFunds[1].sum.toString());
            writer.write(quarterFunds[2].sum.toString());
            writer.write(quarterFunds[3].sum.toString());
            writer.write(sumTotal.toString());
        }
    }
    
    public static class FundsManageFactory implements ServiceFactory<FundsManage>
    {
        
        @Override
        public FundsManage newInstance(ServiceResource serviceResource)
        {
            return new FundsManageImpl(serviceResource);
        }
        
    }
    
    @Override
    public QuarterFunds[] getRosesStatistics(int year)
        throws Throwable
    {
        QuarterFunds[] quarterFunds = null;
        if (year > 0)
        {
            quarterFunds = new QuarterFunds[12];
            for (int i = 0; i < 12; i++)
            {
                quarterFunds[i] = new QuarterFunds();
            }
            try (Connection connection = getConnection())
            {
                try (PreparedStatement ps =
                    connection.prepareStatement("SELECT F02,F03,F04,F05 FROM S70.T7036 WHERE F01=? ORDER BY F02 ASC"))
                {
                    ps.setInt(1, year);
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
    public void exportRosesStatistics(QuarterFunds[] quarterFunds, OutputStream outputStream, String charset)
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
        BigDecimal inTotal = BigDecimal.ZERO;
        BigDecimal outTotal = BigDecimal.ZERO;
        BigDecimal sumTotal = BigDecimal.ZERO;
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(outputStream, charset)))
        {
            CVSWriter writer = new CVSWriter(out);
            writer.write("月份");
            writer.write("收入(元)");
            writer.write("支出(元)");
            writer.write("盈亏(元)");
            writer.newLine();
            for (int i = 0, j = quarterFunds.length; i < j; i++)
            {
                writer.write(quarterFunds[i].quarter + "月份");
                writer.write(quarterFunds[i].amountIn.toString());
                writer.write(quarterFunds[i].amountOut.toString());
                writer.write(quarterFunds[i].sum.toString());
                writer.newLine();
                inTotal = inTotal.add(quarterFunds[i].amountIn);
                outTotal = outTotal.add(quarterFunds[i].amountOut);
                sumTotal = sumTotal.add(quarterFunds[i].sum);
            }
            writer.write("总计(元)");
            writer.write(inTotal.toString());
            writer.write(outTotal.toString());
            writer.write(sumTotal.toString());
        }
        
    }
    
}
