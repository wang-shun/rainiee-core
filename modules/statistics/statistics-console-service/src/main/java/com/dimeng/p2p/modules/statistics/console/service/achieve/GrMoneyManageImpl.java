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

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F13;
import com.dimeng.p2p.modules.statistics.console.service.GrMoneyManage;
import com.dimeng.p2p.modules.statistics.console.service.entity.GrMoneyEntity;
import com.dimeng.p2p.variables.defines.GuarantorVariavle;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.StringHelper;
import com.dimeng.util.io.CVSWriter;
import com.dimeng.util.parser.BooleanParser;

public class GrMoneyManageImpl extends AbstractStatisticsService implements GrMoneyManage
{
    
    public GrMoneyManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public PagingResult<GrMoneyEntity> search(GrMoneyEntity query, Paging paging)
        throws Throwable
    {
        ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
        final boolean isHasGuarant =
            BooleanParser.parse(configureProvider.getProperty(GuarantorVariavle.IS_HAS_GUARANTOR));
        final boolean tg = BooleanParser.parse(configureProvider.getProperty(SystemVariable.SFZJTG));
        final String escrowPrefix = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM (SELECT Y_t6110.F01 AS F01,Y_t6110.F02 AS F02,Y_t6110.F04 AS F03,Y_t6110.F05 AS F04,Y_t6110.F09 AS F05, Y_t6141.F07 AS F06,Y_t6141.F02 AS F07,");
        sql.append("(SELECT Y_T6101.F06 FROM S61.T6101 Y_T6101 WHERE Y_T6101.F03 = 'WLZH' AND Y_T6101.F02 = Y_t6110.F01 ) AS F08,");
        sql.append("(SELECT Y_T6101.F06 FROM S61.T6101 Y_T6101 WHERE Y_T6101.F03 = 'SDZH' AND Y_T6101.F02 = Y_t6110.F01 ) AS F09,");
        sql.append("(SELECT IFNULL(SUM(Y_T6252.F07),0) FROM S62.T6252 Y_T6252 WHERE Y_T6252.F04 = Y_t6110.F01 AND Y_T6252.F09='WH' AND Y_T6252.F05 IN (7001,7002,7004) AND NOT EXISTS (SELECT 1 FROM S62.T6236 WHERE T6236.F03 = Y_T6252.F04 AND T6236.F02 = Y_T6252.F02)) AS F10,");
        sql.append("(SELECT IFNULL(SUM(F07),0)  FROM S62.T6252 WHERE T6252.F03 = Y_t6110.F01 AND T6252.F09 IN ('WH','HKZ')) AS F11 ,");
        sql.append("(SELECT IFNULL(SUM(ZQZR.zqzryk), 0) FROM (SELECT IFNULL(SUM(T6262.F08), 0) zqzryk,T6262.F03 userId FROM S62.T6262 GROUP BY T6262.F03 UNION SELECT IFNULL(SUM(T6262.F09), 0) zqzryk,T6251.F04 userId FROM S62.T6262, S62.T6260, S62.T6251 WHERE T6251.F01 = T6260.F02 AND T6260.F01 = T6262.F02 GROUP BY T6251.F04) ZQZR WHERE ZQZR.userId=Y_t6110.F01) AS F12,");
        sql.append("(SELECT (SELECT IFNULL(SUM(T6102.F06), 0) F01 FROM S61.T6102, S61.T6101 WHERE T6102.F02 = T6101.F01 AND T6101.F02 = Y_t6110.F01 AND T6102.F03 = 1001)+(SELECT IFNULL(SUM(T7150.F04),0) F01 FROM S71.T7150 WHERE T7150.F05='YCZ' AND T7150.F02=Y_t6110.F01)) AS F13,");
        sql.append("(SELECT IFNULL(SUM(T6130.F04),0) FROM S61.T6130 WHERE T6130.F02 = Y_t6110.F01 AND T6130.F09 = 'YFK' ) AS F14, ");
        sql.append(" Y_t6110.F13 AS F15,Y_t6110.F06 AS F16, Y_t6110.F13 AS F17,");
        sql.append("(SELECT IFNULL(SUM(T6102.F07),0) FROM S61.T6102 INNER JOIN S61.T6101 ON T6102.F02 = T6101.F01 WHERE T6102.F03=1202 AND T6101.F03='WLZH' AND T6101.F02=Y_t6110.F01) tzglf,");
        sql.append("(SELECT IFNULL(SUM(T6252.F07),0) FROM S62.T6252 WHERE T6252.F09='YH' AND T6252.F05=7005 AND T6252.F04=Y_t6110.F01) AS wyj ");
        if (isHasGuarant)
        {
            sql.append(", (SELECT Y_T6101.F06 FROM S61.T6101 Y_T6101 WHERE Y_T6101.F03 = 'FXBZJZH' AND Y_T6101.F02 = Y_t6110.F01 ) AS F20, ");
            sql.append("(SELECT IFNULL(SUM(T6253.F05),0) FROM S62.T6253 WHERE T6253.F03 = Y_t6110.F01 ) AS F21, ");
            //sql.append("(SELECT IFNULL(SUM(T6253.F05 - T6253.F06),0) FROM S62.T6253 WHERE T6253.F03 = Y_t6110.F01 ) AS F20 ");
            sql.append("(SELECT IFNULL(SUM(T6252.F07),0) FROM S62.T6252 JOIN S62.T6230 ON T6252.F02=T6230.F01 WHERE EXISTS (SELECT T6236.F01 FROM S62.T6236 WHERE T6236.F02 = T6252.F02 AND T6236.F03 = Y_t6110.F01) AND T6252.F09 = 'WH' AND ((T6252.F05 = 7001 AND T6230.F12='BJQEDB') OR (T6252.F05 IN(7001,7002,7004) AND T6230.F12='BXQEDB'))) AS F22 ");
        }
        
        sql.append("FROM S61.T6110 Y_t6110 INNER JOIN S61.T6141 Y_t6141 ON  Y_t6141.F01=Y_t6110.F01) TEMP  WHERE TEMP.F16 = ? ");
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(T6110_F06.ZRR.name());
        sql.append(" AND TEMP.F17 = ?");
        parameters.add(T6110_F13.F);
        searchParams(query, sql, parameters);
        sql.append(" ORDER BY TEMP.F05 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<GrMoneyEntity>()
            {
                
                @Override
                public GrMoneyEntity[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<GrMoneyEntity> list = null;
                    while (resultSet.next())
                    {
                        GrMoneyEntity record = new GrMoneyEntity();
                        record.id = resultSet.getInt(1);
                        record.userName = resultSet.getString(2);
                        record.phone = resultSet.getString(3);
                        record.email = resultSet.getString(4);
                        record.startTime = resultSet.getTimestamp(5);
                        record.sfzh = resultSet.getString(6);
                        record.name = resultSet.getString(7);
                        record.balance = resultSet.getBigDecimal(8);
                        record.freezeFunds = resultSet.getBigDecimal(9);
                        record.tzzc = resultSet.getBigDecimal(10);
                        record.loanAmount = resultSet.getBigDecimal(11);
                        BigDecimal zqzryk = resultSet.getBigDecimal(12);
                        BigDecimal tzglf = resultSet.getBigDecimal(18);
                        BigDecimal wyj = resultSet.getBigDecimal(19);
                        BigDecimal lx =
                            getAmount(connection,
                                "SELECT IFNULL(SUM(CASE WHEN IFNULL(TBL_LX.F03,0) = 0 THEN IFNULL(TBL_LX.F02,0) ELSE IFNULL(TBL_LX.F02,0) + IFNULL(TBL_LX.F01,0) END),0) FROM (SELECT (SELECT SUM(T6252_WH.F07) FROM S62.T6252 T6252_WH WHERE T6252_WH.F11 = T6252.F11 AND T6252_WH.F06 = T6252.F06 AND T6252_WH.F09 = 'WH' AND T6252_WH.F05 = 7002) F01,(SELECT SUM(T6252_WH.F07) FROM S62.T6252 T6252_WH WHERE T6252_WH.F11 = T6252.F11 AND T6252_WH.F06 = T6252.F06 AND T6252_WH.F09 = 'YH' AND T6252_WH.F05 = 7002) F02,(SELECT T6253.F07 FROM S62.T6253 WHERE T6253.F02 = T6252.F02) F03 FROM S62.T6252 INNER JOIN S62.T6251 ON T6251.F01 = T6252.F11 WHERE T6252.F05 = 7002 AND T6252.F09 IN ('WH','YH') AND T6251.F04=? GROUP BY T6252.F11,T6252.F06) TBL_LX",
                                record.id,
                                false);
                        BigDecimal fx =
                            getAmount(connection,
                                "SELECT IFNULL(SUM(CASE WHEN IFNULL(TBL_LX.F03,0) = 0 THEN IFNULL(TBL_LX.F02,0) ELSE CASE WHEN TBL_LX.F04 = 'BJQEDB' THEN IFNULL(TBL_LX.F02,0) ELSE IFNULL(TBL_LX.F02,0) + IFNULL(TBL_LX.F01,0) END END),0) FROM (SELECT (SELECT SUM(F07) FROM S62.T6252 T6252_WH WHERE T6252_WH.F11 = T6252.F11 AND T6252_WH.F06 = T6252.F06 AND T6252_WH.F09 = 'WH' AND T6252_WH.F05 = 7004) F01,(SELECT SUM(F07) FROM S62.T6252 T6252_WH WHERE T6252_WH.F11 = T6252.F11 AND T6252_WH.F06 = T6252.F06 AND T6252_WH.F09 = 'YH' AND T6252_WH.F05 = 7004) F02,(SELECT T6253.F07 FROM S62.T6253 WHERE T6253.F02 = T6252.F02) F03,T6230.F12 F04 FROM S62.T6252 INNER JOIN S62.T6251 ON T6251.F01 = T6252.F11 INNER JOIN S62.T6230 ON T6230.F01 = T6252.F02 WHERE T6252.F05 = 7004 AND T6252.F09 IN ('WH','YH') AND T6251.F04=? AND T6252.F06 <= (IFNULL((SELECT F08 - 1 FROM S62.T6253 WHERE T6253.F02 = T6252.F02),(SELECT MAX(F06) FROM S62.T6252 T6252_QS WHERE T6252_QS.F02 = T6252.F02))) GROUP BY T6252.F11,T6252.F06 UNION SELECT '' AS F01,T6255.F03 AS F02,T6253.F07 AS F03,'' AS F07 FROM S62.T6255 LEFT JOIN S62.T6253 ON T6255.F02 = T6253.F01 WHERE T6255.F05 = 7004 AND T6255.F04=?) TBL_LX",
                                record.id,
                                true);
                        record.totalIncome = zqzryk.add(wyj).add(lx).add(fx).subtract(tzglf);
                        record.totalRecharge = resultSet.getBigDecimal(13);
                        record.totalWithdraw = resultSet.getBigDecimal(14);
                        if (tg && "yeepay".equals(escrowPrefix))
                        {
                            record.totalWithdraw =
                                getAmount(connection,
                                    "SELECT IFNULL(SUM(T6503.F03),0) AS F01 FROM S65.T6501 INNER JOIN S65.T6503 ON T6501.F01=T6503.F01 WHERE T6503.F02=? AND T6501.F02=10002 AND T6501.F03='CG'",
                                    record.id,
                                    false);
                        }
                        if (isHasGuarant)
                        {
                            record.riskAssureAmount = resultSet.getBigDecimal(20);
                            record.advanceAmount = resultSet.getBigDecimal(21);
                            record.advanceUnpaidAmount = resultSet.getBigDecimal(22);
                        }
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return ((list == null || list.size() == 0) ? null : list.toArray(new GrMoneyEntity[list.size()]));
                }
            },
                paging,
                sql.toString(),
                parameters);
        }
    }
    
    @Override
    public GrMoneyEntity searchAmount(GrMoneyEntity query)
        throws Throwable
    {
        ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
        final boolean isHasGuarant =
            BooleanParser.parse(configureProvider.getProperty(GuarantorVariavle.IS_HAS_GUARANTOR));
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT IFNULL(SUM(TEMP.F08),0), IFNULL(SUM(TEMP.F09),0), IFNULL(SUM(TEMP.F10),0), IFNULL(SUM(TEMP.F11),0),IFNULL(SUM(TEMP.riskReserveAmount), 0) FROM (SELECT Y_t6110.F01 AS F01,Y_t6110.F02 AS F02,Y_t6110.F04 AS F03,Y_t6110.F05 AS F04,Y_t6110.F09 AS F05, Y_t6141.F07 AS F06,Y_t6141.F02 AS F07,");
        sql.append("(SELECT Y_T6101.F06 FROM S61.T6101 Y_T6101 WHERE Y_T6101.F03 = 'WLZH' AND Y_T6101.F02 = Y_t6110.F01 ) AS F08,");
        sql.append("(SELECT Y_T6101.F06 FROM S61.T6101 Y_T6101 WHERE Y_T6101.F03 = 'SDZH' AND Y_T6101.F02 = Y_t6110.F01 ) AS F09,");
        sql.append("(SELECT IFNULL(SUM(Y_T6252.F07),0) FROM S62.T6252 Y_T6252 WHERE Y_T6252.F04 = Y_t6110.F01 AND Y_T6252.F09='WH' AND Y_T6252.F05 IN (7001,7002,7004) AND NOT EXISTS (SELECT 1 FROM S62.T6236 WHERE T6236.F03 = Y_T6252.F04 AND T6236.F02 = Y_T6252.F02)) AS F10,");
        sql.append("(SELECT IFNULL(SUM(F07),0)  FROM S62.T6252 WHERE T6252.F03 = Y_t6110.F01 AND T6252.F09 IN ('WH','HKZ')) AS F11 ,");
        sql.append("(SELECT ( SELECT IFNULL(SUM(T6102.F06), 0) FROM S61.T6102, S61.T6101 WHERE T6102.F02 = T6101.F01 AND T6101.F02 = Y_t6110.F01 AND T6102.F03 IN (7002, 7004, 7005)) - ( SELECT IFNULL(SUM(T6102.F07), 0) FROM S61.T6102, S61.T6101 WHERE T6102.F02 = T6101.F01 AND T6101.F02 = Y_t6110.F01 AND T6102.F03 = 1202 ) + (SELECT F02 FROM S62.T6263 WHERE T6263.F01 = Y_t6110.F01 LIMIT 1) ) AS F12 ,");
        sql.append("(SELECT IFNULL(SUM(T6102.F06), 0) FROM S61.T6102, S61.T6101 WHERE T6102.F02 = T6101.F01 AND T6101.F02 = Y_t6110.F01 AND T6102.F03 = 1001 ) AS F13,");
        sql.append("(SELECT IFNULL(SUM(T6102.F07), 0) FROM S61.T6102, S61.T6101 WHERE T6102.F02 = T6101.F01 AND T6101.F02 = Y_t6110.F01 AND T6102.F03 = 2001 ) AS F14, ");
        sql.append(" Y_t6110.F13 AS F15,Y_t6110.F06 AS F16, Y_t6110.F13 AS F17, ");
        sql.append("(SELECT Y_T6101.F06 FROM S61.T6101 Y_T6101 WHERE Y_T6101.F03 = 'FXBZJZH' AND Y_T6101.F02 = Y_t6110.F01 ) riskReserveAmount ");
        
        if (isHasGuarant)
        {
            sql.append(", (SELECT Y_T6101.F06 FROM S61.T6101 Y_T6101 WHERE Y_T6101.F03 = 'FXBZJZH' AND Y_T6101.F02 = Y_t6110.F01 ) AS F18, ");
            sql.append("(SELECT IFNULL(SUM(T6253.F05),0) FROM S62.T6253 WHERE T6253.F03 = Y_t6110.F01 ) AS F19, ");
            sql.append("(SELECT IFNULL(SUM(T6252.F07),0) FROM S62.T6252 WHERE T6252.F04 = Y_t6110.F01 AND EXISTS (SELECT T6236.F01 FROM S62.T6236 WHERE T6236.F02 = T6252.F02 AND T6236.F03 = Y_t6110.F01) AND T6252.F09 = 'WH' ) AS F20 ");
        }
        
        sql.append("FROM S61.T6110 Y_t6110 INNER JOIN S61.T6141 Y_t6141 ON  Y_t6141.F01=Y_t6110.F01) TEMP  WHERE TEMP.F16 = ? ");
        List<Object> parameters = new ArrayList<Object>();
        parameters.add(T6110_F06.ZRR.name());
        sql.append(" AND TEMP.F17 = ?");
        parameters.add(T6110_F13.F);
        searchParams(query, sql, parameters);
        // sql语句和查询参数处理
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<GrMoneyEntity>()
            {
                @Override
                public GrMoneyEntity parse(ResultSet resultSet)
                    throws SQLException
                {
                    GrMoneyEntity count = new GrMoneyEntity();
                    if (resultSet.next())
                    {
                        count.balance = resultSet.getBigDecimal(1);
                        count.freezeFunds = resultSet.getBigDecimal(2);
                        count.tzzc = resultSet.getBigDecimal(3);
                        count.loanAmount = resultSet.getBigDecimal(4);
                        count.riskAssureAmount = resultSet.getBigDecimal(5);
                    }
                    return count;
                }
            }, sql.toString(), parameters);
        }
    }
    
    private void searchParams(GrMoneyEntity query, StringBuilder sql, List<Object> parameters)
        throws SQLException
    {
        SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
        if (!StringHelper.isEmpty(query.name))
        {
            sql.append(" AND TEMP.F07 LIKE ?");
            parameters.add(sqlConnectionProvider.allMatch(query.name));
        }
        if (!StringHelper.isEmpty(query.userName))
        {
            sql.append(" AND TEMP.F02 LIKE ?");
            parameters.add(sqlConnectionProvider.allMatch(query.userName));
        }
        if (!StringHelper.isEmpty(query.phone))
        {
            sql.append(" AND TEMP.F03 LIKE ?");
            parameters.add(sqlConnectionProvider.allMatch(query.phone));
        }
        
        if (!StringHelper.isEmpty(query.email))
        {
            sql.append(" AND TEMP.F04 LIKE ?");
            parameters.add(sqlConnectionProvider.allMatch(query.email));
        }
        if (query.begin != null)
        {
            sql.append(" AND TEMP.F08 >= ?");
            parameters.add(query.begin);
        }
        if (query.end != null)
        {
            sql.append(" AND TEMP.F08 <= ?");
            parameters.add(query.end);
        }
    }
    
    @Override
    public void export(GrMoneyEntity[] grMoneyEntitys, OutputStream outputStream, String charset)
        throws Throwable
    {
        if (outputStream == null)
        {
            return;
        }
        if (grMoneyEntitys == null)
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
            writer.write("用户名称");
            writer.write("真实姓名");
            writer.write("手机号码");
            writer.write("身份证");
            writer.write("邮箱 ");
            writer.write("可用金额(元)");
            writer.write("冻结金额(元)");
            writer.write("账户余额(元)");
            ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
            final boolean isHasGuarant =
                BooleanParser.parse(configureProvider.getProperty(GuarantorVariavle.IS_HAS_GUARANTOR));
            if (isHasGuarant)
            {
                writer.write("风险保证金余额(元)");
                writer.write("垫付总金额(元)");
                writer.write("垫付待还总金额(元)");
            }
            writer.write("理财资产(元)");
            writer.write("借款负债(元)");
            writer.write("总收益(元)");
            writer.write("总充值(元)");
            writer.write("总提现(元)");
            writer.write("注册时间");
            writer.newLine();
            int index = 1;
            for (GrMoneyEntity grMoneyEntity : grMoneyEntitys)
            {
                String sfzh = StringHelper.decode(grMoneyEntity.sfzh);
                if (sfzh != null)
                {
                    sfzh = StringHelper.decode(grMoneyEntity.sfzh) + "\t";
                }
                writer.write(index++);
                writer.write(grMoneyEntity.userName);
                writer.write(grMoneyEntity.name);
                writer.write(grMoneyEntity.phone);
                writer.write(sfzh);
                writer.write(grMoneyEntity.email);
                writer.write(grMoneyEntity.balance);
                writer.write(grMoneyEntity.freezeFunds);
                writer.write((isHasGuarant ? grMoneyEntity.balance.add(grMoneyEntity.freezeFunds)
                    .add(grMoneyEntity.riskAssureAmount) : grMoneyEntity.balance.add(grMoneyEntity.freezeFunds)));
                if (isHasGuarant)
                {
                    writer.write(grMoneyEntity.riskAssureAmount);
                    writer.write(grMoneyEntity.advanceAmount);
                    writer.write(grMoneyEntity.advanceUnpaidAmount);
                }
                writer.write(grMoneyEntity.tzzc);
                writer.write(grMoneyEntity.loanAmount);
                writer.write(grMoneyEntity.totalIncome);
                writer.write(grMoneyEntity.totalRecharge);
                writer.write(grMoneyEntity.totalWithdraw);
                writer.write(grMoneyEntity.startTime);
                writer.newLine();
            }
        }
    }
    
    private BigDecimal getAmount(Connection connection, String sql, int userId, boolean isDoublePram)
        throws SQLException
    {
        try (PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setInt(1, userId);
            if (isDoublePram)
            {
                ps.setInt(2, userId);
            }
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    return rs.getBigDecimal(1);
                }
            }
        }
        return BigDecimal.ZERO;
    }
}
