package com.dimeng.p2p.modules.systematic.console.service.achieve;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S61.enums.T6110_F10;
import com.dimeng.p2p.S61.enums.T6130_F09;
import com.dimeng.p2p.S62.enums.T6230_F11;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.S62.enums.T6250_F07;
import com.dimeng.p2p.S62.enums.T6250_F08;
import com.dimeng.p2p.S62.enums.T6252_F09;
import com.dimeng.p2p.S62.enums.T6264_F04;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.S71.enums.T7150_F05;
import com.dimeng.p2p.modules.systematic.console.service.MoneyStatisticManage;
import com.dimeng.p2p.modules.systematic.console.service.entity.MoneyStatisticalEntity;
import com.dimeng.p2p.variables.defines.BadClaimVariavle;
import com.dimeng.util.StringHelper;
import com.dimeng.util.io.CVSWriter;
import com.dimeng.util.parser.IntegerParser;

/**
 * 
 * 资金统计总览实现类
 * <功能详细描述>
 * 
 * @author  pengwei
 * @version  [版本号, 2015年11月10日]
 */
public class MoneyStatisticManageImpl extends AbstractSystemService implements MoneyStatisticManage
{
    
    public MoneyStatisticManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public BigDecimal accountBalance()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT SUM(F06) FROM S61.T6101 WHERE T6101.F02 <> (SELECT F01 FROM S71.T7101 LIMIT 1) LIMIT 1"))
            {
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return resultSet.getBigDecimal(1);
                    }
                }
            }
        }
        return BigDecimal.ZERO;
    }
    
    @Override
    public BigDecimal usableBalance(T6101_F03 f03)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT SUM(F06) FROM S61.T6101 WHERE T6101.F02 <> (SELECT F01 FROM S71.T7101 LIMIT 1) AND T6101.F03 = ? LIMIT 1"))
            {
                pstmt.setString(1, f03.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return resultSet.getBigDecimal(1);
                    }
                }
            }
        }
        return BigDecimal.ZERO;
    }
    
    @Override
    public BigDecimal getTxsxf()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT SUM(F07) FROM S61.T6130 WHERE F09=? AND T6130.F02 <> (SELECT T7101.F01 FROM S71.T7101)"))
            {
                ps.setString(1, T6130_F09.YFK.name());
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        return rs.getBigDecimal(1);
                    }
                }
            }
        }
        return BigDecimal.ZERO;
    }
    
    @Override
    public BigDecimal getXxcz()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT SUM(F04) FROM S71.T7150 WHERE F05=? AND F02 <> (SELECT T7101.F01 FROM S71.T7101)"))
            {
                ps.setString(1, T7150_F05.YCZ.name());
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        return rs.getBigDecimal(1);
                    }
                }
            }
        }
        return BigDecimal.ZERO;
    }
    
    @Override
    public BigDecimal getXscz()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT SUM(T6502.F03) FROM S65.T6502 INNER JOIN S65.T6501 ON T6502.F01 = T6501.F01 WHERE T6501.F03=? AND T6502.F02 <> (SELECT T7101.F01 FROM S71.T7101)"))
            {
                ps.setString(1, T6501_F03.CG.name());
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        return rs.getBigDecimal(1);
                    }
                }
            }
        }
        return BigDecimal.ZERO;
    }
    
    @Override
    public BigDecimal getCzsxf()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT SUM(T6502.F05) FROM S65.T6502 INNER JOIN S65.T6501 ON T6501.F01=T6502.F01 WHERE T6501.F03=? AND T6502.F02 <> (SELECT T7101.F01 FROM S71.T7101)"))
            {
                ps.setString(1, T6501_F03.CG.name());
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        return rs.getBigDecimal(1);
                    }
                }
            }
        }
        return BigDecimal.ZERO;
    }
    
    @Override
    public BigDecimal getPtzhzjtj(int f03)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT SUM(F06) FROM S61.T6102 WHERE T6102.F03 = ?"))
            {
                ps.setInt(1, f03);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        return rs.getBigDecimal(1);
                    }
                }
            }
        }
        return BigDecimal.ZERO;
    }
    
    @Override
    public BigDecimal getWyj()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement("SELECT SUM(F06) FROM S61.T6102 WHERE T6102.F03=?"))
            {
                //ps.setInt(1, FeeCode.TZ_WYJ);
                ps.setInt(1, FeeCode.TZ_WYJ_SXF);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        return rs.getBigDecimal(1);
                    }
                }
            }
        }
        return BigDecimal.ZERO;
    }
    
    @Override
    public BigDecimal getLjtzje()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT SUM(T6250.F04) FROM S62.T6250 JOIN S62.T6230 ON T6250.F02=T6230.F01 WHERE T6250.F07 = ? AND T6250.F08 = ? AND T6230.F20 IN(?,?,?,?)"))
            {
                ps.setString(1, T6250_F07.F.name());
                ps.setString(2, T6250_F08.S.name());
                ps.setString(3, T6230_F20.YJQ.name());
                ps.setString(4, T6230_F20.HKZ.name());
                ps.setString(5, T6230_F20.YDF.name());
                ps.setString(6, T6230_F20.YZR.name());
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        return rs.getBigDecimal(1);
                    }
                }
            }
        }
        return BigDecimal.ZERO;
    }
    
    @Override
    public BigDecimal getLjtzzsy()
        throws Throwable
    {
        return getSbtzzsy().add(getZqzrykze());
    }
    
    @Override
    public BigDecimal getSbtzzsy()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            
            StringBuffer sb =
                new StringBuffer(
                    "SELECT (SELECT IFNULL(SUM(CASE WHEN IFNULL(TBL_LX.F03,0) = 0 THEN IFNULL(TBL_LX.F02,0) ELSE IFNULL(TBL_LX.F02,0) + IFNULL(TBL_LX.F01,0) END),0) FROM (SELECT (SELECT SUM(T6252_WH.F07) FROM S62.T6252 T6252_WH WHERE T6252_WH.F11 = T6252.F11 AND T6252_WH.F06 = T6252.F06 AND T6252_WH.F09 = 'WH' AND T6252_WH.F05 = 7002) F01,(SELECT SUM(T6252_WH.F07) FROM S62.T6252 T6252_WH WHERE T6252_WH.F11 = T6252.F11 AND T6252_WH.F06 = T6252.F06 AND T6252_WH.F09 = 'YH' AND T6252_WH.F05 = 7002) F02,(SELECT T6253.F07 FROM S62.T6253 WHERE T6253.F02 = T6252.F02) F03 FROM S62.T6252 INNER JOIN S62.T6251 ON T6251.F01 = T6252.F11 WHERE T6252.F05 = 7002 AND T6252.F09 IN ('WH','YH') GROUP BY T6252.F11,T6252.F06) TBL_LX)");
            sb.append("+(SELECT IFNULL(SUM(CASE WHEN IFNULL(TBL_LX.F03,0) = 0 THEN IFNULL(TBL_LX.F02,0) ELSE CASE WHEN TBL_LX.F04 = 'BJQEDB' THEN IFNULL(TBL_LX.F02,0) ELSE IFNULL(TBL_LX.F02,0) + IFNULL(TBL_LX.F01,0) END END),0) FROM (SELECT (SELECT SUM(F07) FROM S62.T6252 T6252_WH WHERE T6252_WH.F11 = T6252.F11 AND T6252_WH.F06 = T6252.F06 AND T6252_WH.F09 = 'WH' AND T6252_WH.F05 = 7004) F01,(SELECT SUM(F07) FROM S62.T6252 T6252_WH WHERE T6252_WH.F11 = T6252.F11 AND T6252_WH.F06 = T6252.F06 AND T6252_WH.F09 = 'YH' AND T6252_WH.F05 = 7004) F02,(SELECT T6253.F07 FROM S62.T6253 WHERE T6253.F02 = T6252.F02) F03,T6230.F12 F04 FROM S62.T6252 INNER JOIN S62.T6251 ON T6251.F01 = T6252.F11 INNER JOIN S62.T6230 ON T6230.F01 = T6252.F02 WHERE T6252.F05 = 7004 AND T6252.F09 IN ('WH','YH') AND T6252.F06 <= (IFNULL((SELECT F08 - 1 FROM S62.T6253 WHERE T6253.F02 = T6252.F02),(SELECT MAX(F06) FROM S62.T6252 T6252_QS WHERE T6252_QS.F02 = T6252.F02))) GROUP BY T6252.F11,T6252.F06 UNION SELECT '' AS F01,T6255.F03 AS F02,T6253.F07 AS F03,'' AS F07 FROM S62.T6255 LEFT JOIN S62.T6253 ON T6255.F02 = T6253.F01 WHERE T6255.F05 = 7004) TBL_LX)");
            sb.append("+(SELECT IFNULL(SUM(T6252.F07),0) FROM S62.T6252 WHERE T6252.F09='YH' AND T6252.F05=7005)");
            sb.append("-(SELECT IFNULL(SUM(T6102.F07),0) FROM S61.T6102 INNER JOIN S61.T6101 ON T6102.F02 = T6101.F01 WHERE T6102.F03=1202 AND T6101.F03='WLZH')");
            try (PreparedStatement ps = connection.prepareStatement(sb.toString()))
            {
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        return rs.getBigDecimal(1);
                    }
                }
            }
        }
        return BigDecimal.ZERO;
    }
    
    @Override
    public BigDecimal getZqzrykze()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement("SELECT SUM(F08)+SUM(F09) FROM S62.T6262"))
            {
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        return rs.getBigDecimal(1);
                    }
                }
            }
        }
        return BigDecimal.ZERO;
    }
    
    @Override
    public BigDecimal getJkyhzje()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT SUM(F07) FROM S62.T6252 WHERE T6252.F09 IN (?,?)"))
            {
                ps.setString(1, T6252_F09.TQH.name());
                ps.setString(2, T6252_F09.YH.name());
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        return rs.getBigDecimal(1);
                    }
                }
            }
        }
        return BigDecimal.ZERO;
    }
    
    @Override
    public BigDecimal getJkzchkze()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT SUM(F07) FROM S62.T6252 WHERE T6252.F08 = DATE(T6252.F10)"))
            {
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        return rs.getBigDecimal(1);
                    }
                }
            }
        }
        return BigDecimal.ZERO;
    }
    
    @Override
    public BigDecimal getJkwhk()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT SUM(F07) FROM S62.T6252 WHERE T6252.F09 IN (?,?)"))
            {
                ps.setString(1, T6252_F09.WH.name());
                ps.setString(2, T6252_F09.HKZ.name());
                //ps.setString(3, T6252_F09.DF.name());//垫付是免利息的
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        return rs.getBigDecimal(1);
                    }
                }
            }
        }
        return BigDecimal.ZERO;
    }
    
    @Override
    public BigDecimal getJkyqwh()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            //逾期未还还要加上条件F08应还日期<当前日期
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT SUM(F07) FROM S62.T6252 WHERE T6252.F09 = ? AND T6252.F08 < ?"))
            {
                ps.setString(1, T6252_F09.WH.name());
                ps.setDate(2, getCurrentDate(connection));
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        return rs.getBigDecimal(1);
                    }
                }
            }
        }
        return BigDecimal.ZERO;
    }
    
    @Override
    public BigDecimal getYqjgdf()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT SUM(T6253.F05) FROM S62.T6253 INNER JOIN S62.T6230 ON T6253.F02 = T6230.F01 WHERE T6230.F11 = ?"))
            {
                ps.setString(1, T6230_F11.S.name());
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        return rs.getBigDecimal(1);
                    }
                }
            }
        }
        return BigDecimal.ZERO;
    }
    
    @Override
    public BigDecimal getYqjgdfyh()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT SUM(T6253.F06) FROM S62.T6253 INNER JOIN S62.T6230 ON T6253.F02 = T6230.F01 WHERE T6230.F11 = ?"))
            {
                ps.setString(1, T6230_F11.S.name());
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        return rs.getBigDecimal(1);
                    }
                }
            }
        }
        return BigDecimal.ZERO;
    }
    
    @Override
    public BigDecimal getYqptdf()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT SUM(T6253.F05) FROM S62.T6253 INNER JOIN S62.T6230 ON T6253.F02 = T6230.F01 WHERE T6230.F11 = ?"))
            {
                ps.setString(1, T6110_F10.F.name());
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        return rs.getBigDecimal(1);
                    }
                }
            }
        }
        return BigDecimal.ZERO;
    }
    
    @Override
    public BigDecimal getYqptdfyh()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT SUM(T6253.F06) FROM S62.T6253 INNER JOIN S62.T6230 ON T6253.F02 = T6230.F01 WHERE T6230.F11 = ?"))
            {
                ps.setString(1, T6110_F10.F.name());
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        return rs.getBigDecimal(1);
                    }
                }
            }
        }
        return BigDecimal.ZERO;
    }
    
    /**
     * 今日用户总充值
     * @return BigDecimal
     * @throws Throwable
     */
    @Override
    public BigDecimal getTodayCharge()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT SUM(xs.F03+xx.F04) FROM (SELECT IFNULL(SUM(T6502.F03),0) F03 "
                    + "FROM S65.T6502 INNER JOIN S65.T6501 ON T6501.F01=T6502.F01 WHERE T6501.F03=? AND TO_DAYS(T6501.F06) = TO_DAYS(NOW()) AND T6502.F02 <> (SELECT T7101.F01 FROM S71.T7101)) xs,"
                    + "(SELECT IFNULL(SUM(F04),0) F04 FROM S71.T7150 WHERE F05=? AND TO_DAYS(F10) = TO_DAYS(NOW()) AND F02 <> (SELECT T7101.F01 FROM S71.T7101)) xx"))
            {
                ps.setString(1, T6501_F03.CG.name());
                ps.setString(2, T7150_F05.YCZ.name());
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        return rs.getBigDecimal(1);
                    }
                }
            }
        }
        return BigDecimal.ZERO;
    }
    
    /**
     * 今日用户总提现
     * @return BigDecimal
     * @throws Throwable
     */
    @Override
    public BigDecimal getTodayWithdraw()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT IFNULL(SUM(T6503.F03),0) FROM S65.T6503 INNER JOIN S65.T6501 ON T6501.F01=T6503.F01 WHERE T6501.F03=? AND TO_DAYS(T6501.F06) = TO_DAYS(NOW()) AND T6503.F02 <> (SELECT T7101.F01 FROM S71.T7101)"))
            {
                ps.setString(1, T6501_F03.CG.name());
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        return rs.getBigDecimal(1);
                    }
                }
            }
        }
        return BigDecimal.ZERO;
    }
    
    @Override
    public BigDecimal getYhtxze()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT IFNULL(SUM(T6503.F03),0) FROM S65.T6503 INNER JOIN S65.T6501 ON T6501.F01=T6503.F01 WHERE T6501.F03=? AND T6503.F02 <> (SELECT T7101.F01 FROM S71.T7101)"))
            {
                ps.setString(1, T6501_F03.CG.name());
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        return rs.getBigDecimal(1);
                    }
                }
            }
        }
        return BigDecimal.ZERO;
    }
    
    @Override
    public void export(String platformTotalIncome, MoneyStatisticalEntity moneyStatisticalEntity,
        OutputStream outputStream, String charset)
        throws Throwable
    {
        if (outputStream == null)
        {
            return;
        }
        if (moneyStatisticalEntity == null)
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
            writer.write("用户账户资金统计");
            writer.newLine();
            writer.write("账户余额总额(元)");
            writer.write("可用余额总额(元)");
            writer.write("机构风险保证金(元)");
            writer.write("冻结金额总额(元)");
            writer.newLine();
            writer.write(moneyStatisticalEntity.getAccountBalance());
            writer.write(moneyStatisticalEntity.getUsableBalance());
            writer.write(moneyStatisticalEntity.getMargin());
            writer.write(moneyStatisticalEntity.getAmountFrozen());
            writer.newLine();
            writer.newLine();
            writer.write("充值/提现资金统计");
            writer.newLine();
            writer.write("用户充值手续费总额(元)");
            writer.write("用户充值总额(元)");
            writer.write("线上充值总额(元)");
            writer.write("线下充值总额(元)");
            writer.write("今日用户总充值(元)");
            writer.write("今日用户总提现(元)");
            writer.write("用户提现总额(元)");
            writer.write("用户提现手续费总额(元)");
            writer.newLine();
            writer.write(moneyStatisticalEntity.getCzsxf());
            writer.write(moneyStatisticalEntity.getOnlinePay().add(moneyStatisticalEntity.getOfflinePay()));
            writer.write(moneyStatisticalEntity.getOnlinePay());
            writer.write(moneyStatisticalEntity.getOfflinePay());
            writer.write(moneyStatisticalEntity.getTodayCharge());
            writer.write(moneyStatisticalEntity.getTodayWithdraw());
            writer.write(moneyStatisticalEntity.getYhtxze());
            writer.write(moneyStatisticalEntity.getTxsxf());
            writer.newLine();
            writer.newLine();
            writer.write("平台账户资金统计");
            writer.newLine();
            writer.write("平台总收益(元)");
            writer.write("理财管理费总额(元)");
            writer.write("违约金手续费总额(元)");
            writer.write("成交服务费总额(元)");
            writer.write("债权转让手续费总额(元)");
            writer.newLine();
            writer.write(platformTotalIncome);
            writer.write(moneyStatisticalEntity.getLcglf());
            writer.write(moneyStatisticalEntity.getWyjsxf());
            writer.write(moneyStatisticalEntity.getCjfwf());
            writer.write(moneyStatisticalEntity.getZqzrsxf());
            writer.newLine();
            writer.newLine();
            writer.write("投资/借款资金统计");
            writer.newLine();
            writer.write("累计投资总额(元)");
            writer.write("累计投资总收益(元)");
            writer.write("散标投资总收益(元)");
            writer.write("债权转让盈亏总额(元)");
            writer.write("借款已还款总额(元)");
            writer.write("借款正常还款总额(元)");
            writer.write("借款未还款总额(元)");
            writer.write("借款逾期未还款总额(元)");
            writer.newLine();
            writer.write(moneyStatisticalEntity.getLjtzje());
            writer.write(moneyStatisticalEntity.getLjtzzsy());
            writer.write(moneyStatisticalEntity.getSbtzzsy());
            writer.write(moneyStatisticalEntity.getZqzrykze());
            writer.write(moneyStatisticalEntity.getJkyhzje());
            writer.write(moneyStatisticalEntity.getJkzchkz());
            writer.write(moneyStatisticalEntity.getJkwhk());
            writer.write(moneyStatisticalEntity.getJkyqwh());
            writer.newLine();
            writer.newLine();
            writer.write("垫付资金统计");
            writer.newLine();
            writer.write("逾期垫付总额(元)");
            writer.write("逾期垫付未还款总额(元)");
            writer.write("逾期垫付已还款总额(元)");
            writer.newLine();
            writer.write(moneyStatisticalEntity.getYqjgdf());
            writer.write(moneyStatisticalEntity.getYqjgdf().subtract(moneyStatisticalEntity.getYqjgdfyh()));
            writer.write(moneyStatisticalEntity.getYqjgdfyh());
            writer.newLine();
            writer.newLine();
            writer.write("不良资产处理统计");
            writer.newLine();
            writer.write("待转让债权价值总金额(元)");
            writer.write("转让中债权价值总金额(元)");
            writer.write("已转让债权价值总金额(元)");
            writer.newLine();
            writer.write(moneyStatisticalEntity.getDzrzqze());
            writer.write(moneyStatisticalEntity.getZrzzqze());
            writer.write(moneyStatisticalEntity.getYzrzqze());
            writer.newLine();
        }
        
    }
    
    @Override
    public BigDecimal getDzrzqze()
        throws Throwable
    {
        final StringBuilder sql =
            new StringBuilder(
                "SELECT IFNULL(SUM(zq.F01),0) FROM (SELECT (SELECT IFNULL(SUM(T6252.F07),0) FROM S62.T6252 WHERE T6252.F02 = T6230.F01 AND T6252.F09 ='WH' AND T6252.F05 IN ( '7001', '7002', '7003', '7004' )) F01");
        sql.append(" FROM S62.T6252 a JOIN S62.T6230 ON a.F02=T6230.F01 JOIN S61.T6110 ON T6230.F02 = T6110.F01");
        sql.append(" JOIN (SELECT MIN(b.F01) F01 FROM S62.T6252 b WHERE b.F08 < ? AND b.F09 = 'WH' AND ? >= DATE_ADD(b.F08, INTERVAL ? DAY ) AND b.F05 IN ( '7001', '7002', '7003', '7004' ) GROUP BY b.F02) bb");
        sql.append(" ON a.F01=bb.F01 WHERE a.F02 NOT IN (SELECT T6264.F03 FROM S62.T6264 WHERE T6264.F04 IN ('DSH','ZRZ')) AND T6230.F20 <> 'YDF') zq");
        try (Connection connection = getConnection())
        {
            Date date = getCurrentDate(connection);
            try (PreparedStatement ps = connection.prepareStatement(sql.toString()))
            {
                ps.setDate(1, date);
                ps.setDate(2, date);
                ps.setInt(3, IntegerParser.parse(serviceResource.getResource(ConfigureProvider.class)
                    .getProperty(BadClaimVariavle.BLZQZR_YQ_DAY)));
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        return rs.getBigDecimal(1);
                    }
                }
            }
        }
        return BigDecimal.ZERO;
    }
    
    @Override
    public BigDecimal getZrzzqze()
        throws Throwable
    {
        final StringBuilder sql =
            new StringBuilder(
                "SELECT IFNULL(SUM(zq.F01),0) FROM (SELECT (SELECT IFNULL(SUM(T6252.F07),0) FROM S62.T6252 WHERE T6252.F02 = T6230.F01 AND T6252.F09 ='WH' AND T6252.F05 IN ( '7001', '7002', '7003', '7004' )) F01");
        sql.append(" FROM S62.T6252 a JOIN S62.T6230 ON a.F02=T6230.F01");
        sql.append(" JOIN (SELECT MIN(b.F01) F01 FROM S62.T6252 b WHERE b.F08 < ? AND b.F09 = 'WH' AND b.F05 IN ( '7001', '7002', '7003', '7004' ) GROUP BY b.F02) bb ON a.F01=bb.F01");
        sql.append(" JOIN S62.T6264 ON a.F02=T6264.F03 WHERE T6264.F04 = ?) zq");
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql.toString()))
            {
                ps.setDate(1, getCurrentDate(connection));
                ps.setString(2, T6264_F04.ZRZ.name());
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        return rs.getBigDecimal(1);
                    }
                }
            }
        }
        return BigDecimal.ZERO;
    }
    
    @Override
    public BigDecimal getYzrzqze()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT IFNULL(SUM(T6264.F09),0) FROM S62.T6264 WHERE T6264.F04=?"))
            {
                ps.setString(1, T6264_F04.YZR.name());
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        return rs.getBigDecimal(1);
                    }
                }
            }
        }
        return BigDecimal.ZERO;
    }
    
    @Override
    public BigDecimal getYqjgdfwh()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT IFNULL(SUM(T6252.F07),0) FROM S62.T6252 JOIN S62.T6230 ON T6252.F02=T6230.F01 JOIN S62.T6236 ON T6230.F01=T6236.F02 WHERE T6252.F09=? AND T6230.F20=?"))
            {
                ps.setString(1, T6252_F09.WH.name());
                ps.setString(2, T6230_F20.YDF.name());
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        return rs.getBigDecimal(1);
                    }
                }
            }
        }
        return BigDecimal.ZERO;
    }
    
}
