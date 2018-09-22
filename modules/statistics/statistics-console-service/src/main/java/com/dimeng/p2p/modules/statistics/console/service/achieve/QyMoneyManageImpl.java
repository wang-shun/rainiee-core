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
import com.dimeng.p2p.modules.statistics.console.service.QyMoneyManage;
import com.dimeng.p2p.modules.statistics.console.service.entity.PropertyStatisticsEntity;
import com.dimeng.p2p.variables.defines.GuarantorVariavle;
import com.dimeng.util.Formater;
import com.dimeng.util.StringHelper;
import com.dimeng.util.io.CVSWriter;
import com.dimeng.util.parser.BooleanParser;

public class QyMoneyManageImpl extends AbstractStatisticsService implements QyMoneyManage
{
    
    public QyMoneyManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public PagingResult<PropertyStatisticsEntity> search(PropertyStatisticsEntity query, Paging paging)
        throws Throwable
    {
        StringBuilder sql = new StringBuilder();
        ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
        final boolean isHasGuarant =
            BooleanParser.parse(configureProvider.getProperty(GuarantorVariavle.IS_HAS_GUARANTOR));
        sql.append("SELECT * FROM ( SELECT Y_t6110.F01 AS F01,Y_t6110.F02 AS F02,Y_t6110.F04 AS F03,Y_t6110.F05 AS F04,Y_t6110.F09 AS F05, Y_t6161.F04 AS qyName,Y_t6161.F13 AS F06,");
        sql.append("(SELECT Y_T6101.F06 FROM S61.T6101 Y_T6101 WHERE Y_T6101.F03 = 'WLZH' AND Y_T6101.F02 = Y_t6110.F01 ) balance,");
        sql.append("(SELECT Y_T6101.F06 FROM S61.T6101 Y_T6101 WHERE  Y_T6101.F03 = 'SDZH' AND Y_T6101.F02 = Y_t6110.F01 ) freezeFunds,");
        sql.append("(SELECT IFNULL(SUM(Y_T6252.F07),0) FROM S62.T6252 Y_T6252 WHERE Y_T6252.F04 = Y_t6110.F01 AND Y_T6252.F09='WH' AND Y_T6252.F05 IN (7001,7002,7004) AND NOT EXISTS (SELECT 1 FROM S62.T6236 WHERE T6236.F03 = Y_T6252.F04 AND T6236.F02 = Y_T6252.F02)) tzzc,");
        sql.append("(SELECT IFNULL(SUM(F07),0)  FROM S62.T6252 WHERE T6252.F03 = Y_t6110.F01 AND T6252.F09 IN ('WH','HKZ')) loanAmount, ");
        sql.append("(SELECT SUM(T6502.F03) FROM S65.T6502 INNER JOIN S65.T6501 ON T6502.F01 = T6501.F01 WHERE T6501.F03='CG' AND T6502.F02 = Y_t6110.F01) onlinePayAmount,");
        sql.append("(SELECT SUM(F04) FROM S71.T7150 WHERE F05='YCZ' AND F02 = Y_t6110.F01) offlinePayAmount,");
        sql.append("(SELECT IFNULL(SUM(T6503.F03),0) FROM S65.T6503 INNER JOIN S65.T6501 ON T6501.F01=T6503.F01 WHERE T6501.F03='CG' AND T6503.F02 = Y_t6110.F01) withdrawAmount,");
        //sql.append("(SELECT IFNULL(SUM(T6102.F06), 0) FROM S61.T6102 WHERE T6102.F03 IN ('7002','7004','7005') AND T6102.F02 IN (SELECT T6101.F01 FROM S61.T6101 WHERE T6101.F02 = Y_t6110.F01 AND T6101.F03 = 'WLZH')) earnLx,");
        sql.append("(SELECT IFNULL(SUM(ZQZR.zqzryk),0) FROM (SELECT IFNULL(SUM(T6262.F08), 0) zqzryk,T6262.F03 userId FROM S62.T6262 GROUP BY T6262.F03 UNION SELECT IFNULL(SUM(T6262.F09), 0) zqzryk,T6251.F04 userId FROM S62.T6262, S62.T6260, S62.T6251 WHERE T6251.F01 = T6260.F02 AND T6260.F01 = T6262.F02 GROUP BY T6251.F04) ZQZR WHERE ZQZR.userId=Y_t6110.F01) AS zqzryk,");
        sql.append("(SELECT IFNULL(SUM(T6102.F07),0) FROM S61.T6102 INNER JOIN S61.T6101 ON T6102.F02 = T6101.F01 WHERE T6102.F03=1202 AND T6101.F03='WLZH' AND T6101.F02=Y_t6110.F01) tzglf,");
        sql.append("(SELECT IFNULL(SUM(T6252.F07),0) FROM S62.T6252 WHERE T6252.F09='YH' AND T6252.F05=7005 AND T6252.F04=Y_t6110.F01) AS wyj ");
        if (isHasGuarant)
        {
            sql.append(", (SELECT Y_T6101.F06 FROM S61.T6101 Y_T6101 WHERE Y_T6101.F03 = 'FXBZJZH' AND Y_T6101.F02 = Y_t6110.F01 ) riskReserveAmount, ");
            sql.append("(SELECT IFNULL(SUM(T6253.F05),0) FROM S62.T6253 WHERE T6253.F03 = Y_t6110.F01 ) advanceAmount, ");
            //sql.append("(SELECT IFNULL(SUM(T6253.F05 - T6253.F06),0) FROM S62.T6253 WHERE T6253.F03 = Y_t6110.F01 ) AS advanceUnpaidAmount ");
            sql.append("(SELECT IFNULL(SUM(T6252.F07),0) FROM S62.T6252 JOIN S62.T6230 ON T6252.F02=T6230.F01 WHERE EXISTS (SELECT T6236.F01 FROM S62.T6236 WHERE T6236.F02 = T6252.F02 AND T6236.F03 = Y_t6110.F01) AND T6252.F09 = 'WH' AND ((T6252.F05 = 7001 AND T6230.F12='BJQEDB') OR (T6252.F05 IN(7001,7002,7004) AND T6230.F12='BXQEDB'))) advanceUnpaidAmount ");
        }
        sql.append("FROM S61.T6110 Y_t6110,S61.T6161 Y_t6161  WHERE Y_t6161.F01=Y_t6110.F01 AND Y_t6110.F06 = 'FZRR' AND Y_t6110.F13 = 'F' AND Y_t6110.F10 = 'F' ");
        ArrayList<Object> parameters = new ArrayList<>();
        sql.append(" ORDER BY Y_t6110.F09 DESC ) temp WHERE 1 = 1");
        searchParams(query, sql, parameters);
        
        BigDecimal balanceMin = query.balanceMin;
        if (balanceMin != null)
        {
            sql.append(" AND temp.balance >= ?");
            parameters.add(balanceMin);
        }
        BigDecimal balanceMax = query.balanceMax;
        if (balanceMax != null)
        {
            sql.append(" AND temp.balance <= ?");
            parameters.add(balanceMax);
        }
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<PropertyStatisticsEntity>()
            {
                
                @Override
                public PropertyStatisticsEntity[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<PropertyStatisticsEntity> list = null;
                    while (resultSet.next())
                    {
                        PropertyStatisticsEntity record = new PropertyStatisticsEntity();
                        record.id = resultSet.getInt(1);
                        record.userName = resultSet.getString(2);
                        record.phone = resultSet.getString(3);
                        record.email = resultSet.getString(4);
                        record.startTime = resultSet.getTimestamp(5);
                        record.businessName = resultSet.getString(6);
                        record.idCardNo = resultSet.getString(7);
                        record.balance = resultSet.getBigDecimal(8);
                        record.freezeFunds = resultSet.getBigDecimal(9);
                        record.tzzc = resultSet.getBigDecimal(10);
                        record.loanAmount = resultSet.getBigDecimal(11);
                        BigDecimal onlinePayAmount = resultSet.getBigDecimal(12);
                        BigDecimal offlinePayAmount = resultSet.getBigDecimal(13);
                        record.payAmount = onlinePayAmount.add(offlinePayAmount);
                        record.withdrawAmount = resultSet.getBigDecimal(14);
                        BigDecimal zqzryk = resultSet.getBigDecimal(15);
                        BigDecimal tzglf = resultSet.getBigDecimal(16);
                        BigDecimal wyj = resultSet.getBigDecimal(17);
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
                        record.earningsAmount = zqzryk.add(wyj).add(lx).add(fx).subtract(tzglf);
                        if (isHasGuarant)
                        {
                            record.riskAssureAmount = resultSet.getBigDecimal(18);
                            record.advanceAmount = resultSet.getBigDecimal(19);
                            record.advanceUnpaidAmount = resultSet.getBigDecimal(20);
                        }
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return ((list == null || list.size() == 0) ? null
                        : list.toArray(new PropertyStatisticsEntity[list.size()]));
                }
            },
                paging,
                sql.toString(),
                parameters);
        }
    }
    
    @Override
    public PropertyStatisticsEntity searchAmount(PropertyStatisticsEntity query)
        throws Throwable
    {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT IFNULL(SUM(temp.balance),0),IFNULL(SUM(temp.freezeFunds),0),IFNULL(SUM(temp.tzzc),0),IFNULL(SUM(temp.loanAmount),0),IFNULL(SUM(temp.riskReserveAmount), 0) FROM");
        sql.append("(SELECT (SELECT Y_T6101.F06 FROM S61.T6101 Y_T6101 WHERE Y_T6101.F03 = 'WLZH' AND Y_T6101.F02 = Y_t6110.F01 ) balance,");
        sql.append("(SELECT Y_T6101.F06 FROM S61.T6101 Y_T6101 WHERE  Y_T6101.F03 = 'SDZH' AND Y_T6101.F02 = Y_t6110.F01 ) freezeFunds,");
        sql.append("(SELECT IFNULL(SUM(Y_T6252.F07),0) FROM S62.T6252 Y_T6252 WHERE Y_T6252.F04 = Y_t6110.F01 AND Y_T6252.F09='WH' AND Y_T6252.F05 IN (7001,7002,7004) AND NOT EXISTS (SELECT 1 FROM S62.T6236 WHERE T6236.F03 = Y_T6252.F04 AND T6236.F02 = Y_T6252.F02)) tzzc,");
        sql.append("(SELECT IFNULL(SUM(F07),0)  FROM S62.T6252 WHERE T6252.F03 = Y_t6110.F01 AND T6252.F09 IN ('WH','HKZ')) loanAmount, ");
        sql.append("(SELECT Y_T6101.F06 FROM S61.T6101 Y_T6101 WHERE Y_T6101.F03 = 'FXBZJZH' AND Y_T6101.F02 = Y_t6110.F01 ) riskReserveAmount ");
        sql.append("FROM S61.T6110 Y_t6110,S61.T6161 Y_t6161  WHERE Y_t6161.F01=Y_t6110.F01 AND Y_t6110.F06 = 'FZRR'");
        List<Object> parameters = new ArrayList<Object>();
        SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
        String userName = query.userName;
        if (!StringHelper.isEmpty(userName))
        {
            sql.append(" AND Y_t6110.F02 LIKE ?");
            parameters.add(sqlConnectionProvider.allMatch(userName));
        }
        String businessName = query.businessName;
        if (!StringHelper.isEmpty(businessName))
        {
            sql.append(" AND Y_t6161.F04 LIKE ?");
            parameters.add(sqlConnectionProvider.allMatch(businessName));
        }
        String phone = query.phone;
        if (!StringHelper.isEmpty(phone))
        {
            sql.append(" AND Y_t6110.F04 LIKE ?");
            parameters.add(sqlConnectionProvider.allMatch(phone));
        }
        
        String email = query.email;
        if (!StringHelper.isEmpty(email))
        {
            sql.append(" AND Y_t6110.F05 LIKE ?");
            parameters.add(sqlConnectionProvider.allMatch(email));
        }
        
        sql.append(" AND Y_t6110.F13 = 'F' AND Y_t6110.F10 = 'F') AS temp WHERE 1=1");
        
        BigDecimal balanceMin = query.balanceMin;
        if (balanceMin != null)
        {
            sql.append(" AND temp.balance >= ?");
            parameters.add(balanceMin);
        }
        BigDecimal balanceMax = query.balanceMax;
        if (balanceMax != null)
        {
            sql.append(" AND temp.balance <= ?");
            parameters.add(balanceMax);
        }
        
        // sql语句和查询参数处理
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<PropertyStatisticsEntity>()
            {
                @Override
                public PropertyStatisticsEntity parse(ResultSet resultSet)
                    throws SQLException
                {
                    PropertyStatisticsEntity count = new PropertyStatisticsEntity();
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
    
    @Override
    public void export(PropertyStatisticsEntity[] qyMoneyEntitys, OutputStream outputStream, String charset)
        throws Throwable
    {
        if (outputStream == null)
        {
            return;
        }
        if (qyMoneyEntitys == null)
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
            writer.write("企业账户");
            writer.write("企业名称");
            writer.write("法人身份证");
            writer.write("法人手机号码");
            writer.write("法人邮箱地址 ");
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
            for (PropertyStatisticsEntity qyMoneyEntity : qyMoneyEntitys)
            {
                String sfzh = StringHelper.decode(qyMoneyEntity.idCardNo);
                if (sfzh != null)
                {
                    sfzh = StringHelper.decode(qyMoneyEntity.idCardNo) + "\t";
                }
                writer.write(index++);
                writer.write(qyMoneyEntity.userName);
                writer.write(qyMoneyEntity.businessName);
                writer.write(sfzh);
                writer.write(qyMoneyEntity.phone);
                writer.write(qyMoneyEntity.email);
                writer.write(qyMoneyEntity.balance);
                writer.write(qyMoneyEntity.freezeFunds);
                writer.write((isHasGuarant ? Formater.formatAmount(qyMoneyEntity.balance.add(qyMoneyEntity.freezeFunds)
                    .add(qyMoneyEntity.riskAssureAmount))
                    : Formater.formatAmount(qyMoneyEntity.balance.add(qyMoneyEntity.freezeFunds))));
                
                if (isHasGuarant)
                {
                    writer.write(qyMoneyEntity.riskAssureAmount);
                    writer.write(qyMoneyEntity.advanceAmount);
                    writer.write(qyMoneyEntity.advanceUnpaidAmount);
                }
                writer.write(qyMoneyEntity.tzzc);
                writer.write(qyMoneyEntity.loanAmount);
                writer.write(qyMoneyEntity.earningsAmount);
                writer.write(qyMoneyEntity.payAmount);
                writer.write(qyMoneyEntity.withdrawAmount);
                writer.write(qyMoneyEntity.startTime);
                writer.newLine();
            }
        }
    }
    
    private void searchParams(PropertyStatisticsEntity query, StringBuilder sql, List<Object> parameters)
        throws SQLException
    {
        SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
        String userName = query.userName;
        if (!StringHelper.isEmpty(userName))
        {
            sql.append(" AND temp.F02 LIKE ?");
            parameters.add(sqlConnectionProvider.allMatch(userName));
        }
        String businessName = query.businessName;
        if (!StringHelper.isEmpty(businessName))
        {
            sql.append(" AND temp.qyName LIKE ?");
            parameters.add(sqlConnectionProvider.allMatch(businessName));
        }
        String phone = query.phone;
        if (!StringHelper.isEmpty(phone))
        {
            sql.append(" AND temp.F03 LIKE ?");
            parameters.add(sqlConnectionProvider.allMatch(phone));
        }
        
        String email = query.email;
        if (!StringHelper.isEmpty(email))
        {
            sql.append(" AND temp.F04 LIKE ?");
            parameters.add(sqlConnectionProvider.allMatch(email));
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
