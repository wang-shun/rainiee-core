package com.dimeng.p2p.modules.statistics.console.service.achieve;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.enums.T6110_F10;
import com.dimeng.p2p.modules.statistics.console.service.DFStatisticsManage;
import com.dimeng.p2p.modules.statistics.console.service.RechargeWithdrawManage;
import com.dimeng.p2p.modules.statistics.console.service.entity.DFStatisticsEntity;
import com.dimeng.p2p.modules.statistics.console.service.query.DFStatisticsQuery;
import com.dimeng.p2p.variables.defines.GuarantorVariavle;
import com.dimeng.util.StringHelper;
import com.dimeng.util.io.CVSWriter;
import com.dimeng.util.parser.BooleanParser;
import com.dimeng.util.parser.TimestampParser;

public class DFStatisticsManageImpl extends AbstractStatisticsService implements DFStatisticsManage
{
    
    public DFStatisticsManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public PagingResult<DFStatisticsEntity> getDFList(DFStatisticsQuery query, Paging paging)
        throws Throwable
    {
        ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
        boolean isHasGuarant = BooleanParser.parse(configureProvider.getProperty(GuarantorVariavle.IS_HAS_GUARANTOR));
        List<Object> parameters = new ArrayList<>();
        StringBuffer sqlBf = new StringBuffer();
        sqlBf.append(" SELECT * FROM ( ");
        sqlBf.append(" SELECT  T6230.F25 AS F01,T6253.F03 AS F02,T6253.F05 AS F03,T6253.F06 AS F04,T6253.F07 AS F05, T6231.F02 AS F06, T6231.F03 AS F07, T6110.F02 AS F08, ");
        if (!isHasGuarant)
        {
            sqlBf.append(" (SELECT F04 FROM S61.T6161 WHERE T6161.F01 = T6253.F03) AS F09 ");
        }
        else
        {
            sqlBf.append(" CASE T6110.F06 WHEN 'ZRR' THEN (SELECT F02 FROM S61.T6141 WHERE T6141.F01 = T6253.F03) ELSE (SELECT F04 FROM S61.T6161 WHERE T6161.F01 = T6253.F03) END AS F09");
        }
        sqlBf.append(" ,T6110.F10 AS F10,jkr.F02 AS F11 FROM S62.T6253");
        sqlBf.append(" INNER JOIN S62.T6230 ON T6253.F02 = T6230.F01");
        sqlBf.append(" INNER JOIN S62.T6231 ON T6253.F02 = T6231.F01");
        sqlBf.append(" INNER JOIN S61.T6110 jkr ON T6230.F02 = jkr.F01");
        sqlBf.append(" INNER JOIN S61.T6110 ON T6253.F03 = T6110.F01) tmp WHERE 1=1 ");
        if (!isHasGuarant)
        {
            sqlBf.append(" AND tmp.F10 = ? ");
            parameters.add(T6110_F10.S.name());
        }
        sqlAndParameterProcess(query, sqlBf, parameters);
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, ARRAY_PARSER, paging, sqlBf.toString(), parameters);
        }
    }
    
    protected static final ArrayParser<DFStatisticsEntity> ARRAY_PARSER = new ArrayParser<DFStatisticsEntity>()
    {
        
        @Override
        public DFStatisticsEntity[] parse(ResultSet resultSet)
            throws SQLException
        {
            List<DFStatisticsEntity> list = new ArrayList<DFStatisticsEntity>();
            while (resultSet.next())
            {
                DFStatisticsEntity entity = new DFStatisticsEntity();
                entity.loanId = resultSet.getString(1); // 借款ID
                entity.actualMoney = resultSet.getBigDecimal(3);// 实际垫付金额
                entity.reMoney = resultSet.getBigDecimal(4);// 返还金额
                entity.dfTime = resultSet.getTimestamp(5);// 垫付时间
                entity.periods = resultSet.getString(7) + "/" + resultSet.getString(6);// '担保机构',
                entity.dfAccount = resultSet.getString(8);//垫付账户名账号名
                entity.dfAccountName = resultSet.getString(9);// 垫付机构名称
                entity.dfEarn = entity.reMoney.subtract(entity.actualMoney);//垫付盈亏
                entity.jkrAccount = resultSet.getString(11);// 借款人账号
                list.add(entity);
            }
            return list.toArray(new DFStatisticsEntity[list.size()]);
        }
    };
    
    @Override
    public void export(DFStatisticsEntity[] recWits, OutputStream outputStream, String charset)
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
            writer.write("借款ID");
            writer.write("剩余期数");
            writer.write("借款人账号");
            writer.write("垫付账户名");
            writer.write("垫付方名称");
            writer.write("实际垫付金额(元)");
            writer.write("垫付时间");
            writer.write("返还金额(元)");
            writer.write("垫付盈亏(元)");
            
            writer.newLine();
            int i = 0;
            for (DFStatisticsEntity recWit : recWits)
            {
                i++;
                writer.write(i);
                writer.write(recWit.loanId);
                writer.write(recWit.periods + "\t");
                writer.write(recWit.jkrAccount);
                writer.write(recWit.dfAccount);
                writer.write(recWit.dfAccountName);
                writer.write(recWit.actualMoney);
                writer.write(TimestampParser.format(recWit.dfTime));
                writer.write(recWit.reMoney);
                writer.write(recWit.dfEarn);
                writer.newLine();
            }
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
    
    @Override
    public Map<String, String> getDFTotal(DFStatisticsQuery query)
        throws Throwable
    {
        StringBuffer sqlBf = new StringBuffer();
        ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
        boolean isHasGuarant = BooleanParser.parse(configureProvider.getProperty(GuarantorVariavle.IS_HAS_GUARANTOR));
        sqlBf.append(" SELECT IFNULL(SUM(tmp.F03),0), IFNULL(SUM(tmp.F04),0)  FROM ( ");
        sqlBf.append(" SELECT  T6230.F25 AS F01,T6253.F03 AS F02,T6253.F05 AS F03,T6253.F06 AS F04,T6253.F07 AS F05, T6231.F02 AS F06, T6231.F03 AS F07, T6110.F02 AS F08, ");
        sqlBf.append(" CASE T6110.F06 WHEN 'ZRR' THEN (SELECT F02 FROM S61.T6141 WHERE T6141.F01 = T6253.F03) ELSE (SELECT F04 FROM S61.T6161 WHERE T6161.F01 = T6253.F03) END AS F09, T6110.F10 AS F10 ");
        sqlBf.append(" FROM S62.T6253 ");
        sqlBf.append(" INNER JOIN S62.T6230 ON T6253.F02 = T6230.F01   ");
        sqlBf.append(" INNER JOIN S62.T6231 ON T6253.F02 = T6231.F01  ");
        sqlBf.append(" INNER JOIN S61.T6110 ON T6253.F03 = T6110.F01) tmp WHERE 1=1 ");
        List<Object> parameters = new ArrayList<Object>();
        if (!isHasGuarant)
        {
            sqlBf.append(" AND tmp.F10 = ? ");
            parameters.add(T6110_F10.S.name());
        }
        // sql语句和查询参数处理
        sqlAndParameterProcess(query, sqlBf, parameters);
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<Map>()
            {
                @Override
                public Map parse(ResultSet resultSet)
                    throws SQLException
                {
                    //BigDecimal total = BigDecimal.ZERO;
                    Map map = new HashMap();
                    if (resultSet.next())
                    {
                        BigDecimal actualMoneySum = resultSet.getBigDecimal(1);
                        BigDecimal reMoneySum = resultSet.getBigDecimal(2);
                        BigDecimal dfEarnSum = reMoneySum.subtract(actualMoneySum);
                        map.put("actualMoneySum", actualMoneySum);
                        map.put("reMoneySum", reMoneySum);
                        map.put("dfEarnSum", dfEarnSum);
                    }
                    return map;
                }
            }, sqlBf.toString(), parameters);
        }
    }
    
    public void sqlAndParameterProcess(DFStatisticsQuery query, StringBuffer sqlBf, List<Object> parameters)
        throws Throwable
    {
        if (query != null)
        {
            String loanId = query.getLoanId();
            if (!StringHelper.isEmpty(loanId))
            {
                sqlBf.append(" AND tmp.F01 LIKE ? ");
                parameters.add(getSQLConnectionProvider().allMatch(loanId));
            }
            
            String dfAccountName = query.getDfAccountName();
            if (!StringHelper.isEmpty(dfAccountName))
            {
                sqlBf.append(" AND tmp.F09 LIKE ?");
                parameters.add(getSQLConnectionProvider().allMatch(dfAccountName));
            }
            
            String dfAccount = query.getDfAccount();
            if (!StringHelper.isEmpty(dfAccount))
            {
                sqlBf.append(" AND tmp.F08 LIKE ?");
                parameters.add(getSQLConnectionProvider().allMatch(dfAccount));
            }
            
            Timestamp timestamp = query.getDfTimeEnd();
            if (timestamp != null)
            {
                sqlBf.append(" AND DATE(tmp.F05) <=?");
                parameters.add(timestamp);
            }
            
            timestamp = query.getDfTimeStart();
            if (timestamp != null)
            {
                sqlBf.append(" AND DATE(tmp.F05) >=?");
                parameters.add(timestamp);
            }
            
            String jkAccount = query.getJKAccount();
            if (!StringHelper.isEmpty(jkAccount))
            {
                sqlBf.append(" AND tmp.F11 LIKE ?");
                parameters.add(getSQLConnectionProvider().allMatch(jkAccount));
            }
            
            sqlBf.append(" order by tmp.F05 DESC ");
        }
    }
    
    @Override
    public PagingResult<DFStatisticsEntity> getPTDFList(DFStatisticsQuery query, Paging paging)
        throws Throwable
    {
        List<Object> parameters = new ArrayList<>();
        StringBuffer sqlBf = new StringBuffer();
        sqlBf.append("SELECT * FROM (SELECT T6230.F25 AS F01, T6253.F03 AS F02, T6253.F05 AS F03,"
            + " T6253.F06 AS F04, T6253.F07 AS F05, T6231.F02 AS F06, T6231.F03 AS F07, T6110.F02 AS F08, "
            + "T6253.F05 AS F09, T6253.F11 AS F10,(SELECT F02 FROM S61.T6110 WHERE T6230.F02 = T6110.F01)  AS F11 FROM S62.T6253 INNER JOIN S62.T6230 ON T6253.F02 = T6230.F01 "
            + "INNER JOIN S62.T6231 ON T6253.F02 = T6231.F01 INNER JOIN S61.T6110 ON T6253.F03 = T6110.F01 WHERE T6230.F11 = 'F' )"
            + " tmp WHERE 1 = 1");
        sqlAndParameterProcess(query, sqlBf, parameters);
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, _ARRAY_PARSER, paging, sqlBf.toString(), parameters);
        }
    }
    
    protected static final ArrayParser<DFStatisticsEntity> _ARRAY_PARSER = new ArrayParser<DFStatisticsEntity>()
    {
        
        @Override
        public DFStatisticsEntity[] parse(ResultSet resultSet)
            throws SQLException
        {
            List<DFStatisticsEntity> list = new ArrayList<DFStatisticsEntity>();
            while (resultSet.next())
            {
                DFStatisticsEntity entity = new DFStatisticsEntity();
                entity.loanId = resultSet.getString(1); // 标的ID
                entity.actualMoney = resultSet.getBigDecimal(3);// 实际垫付金额
                entity.reMoney = resultSet.getBigDecimal(4);// 返还金额
                entity.dfTime = resultSet.getTimestamp(5);// 垫付时间
                entity.dfEarn = entity.reMoney.subtract(entity.actualMoney);//垫付盈亏
                entity.periods = (resultSet.getInt(6) - resultSet.getInt(10) + 1) + "/" + resultSet.getInt(6);
                entity.jkrAccount = resultSet.getString(11);
                entity.dfAccountName = resultSet.getString(8);//
                list.add(entity);
            }
            return list.toArray(new DFStatisticsEntity[list.size()]);
        }
    };
    
    @Override
    public Map<String, String> getPTDFTotal(DFStatisticsQuery query)
        throws Throwable
    {
        StringBuffer sqlBf = new StringBuffer();
        sqlBf.append(" SELECT IFNULL(SUM(tmp.F03),0), IFNULL(SUM(tmp.F04),0)  FROM ( ");
        sqlBf.append("SELECT T6230.F25 AS F01, T6253.F03 AS F02, T6253.F05 AS F03,");
        sqlBf.append(" T6253.F06 AS F04, T6253.F07 AS F05, T6231.F02 AS F06, T6231.F03 AS F07, T6110.F02 AS F08, ");
        sqlBf.append("T6253.F05 AS F09, T6253.F11 AS F10,(SELECT F02 FROM S61.T6110 WHERE T6230.F02 = T6110.F01)  AS F11 FROM S62.T6253 INNER JOIN S62.T6230 ON T6253.F02 = T6230.F01 ");
        sqlBf.append("INNER JOIN S62.T6231 ON T6253.F02 = T6231.F01 INNER JOIN S61.T6110 ON T6253.F03 = T6110.F01 WHERE T6230.F11 = 'F' )");
        sqlBf.append(" tmp WHERE 1 = 1");
        List<Object> parameters = new ArrayList<Object>();
        // sql语句和查询参数处理
        sqlAndParameterProcess(query, sqlBf, parameters);
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<Map>()
            {
                @Override
                public Map parse(ResultSet resultSet)
                    throws SQLException
                {
                    //BigDecimal total = BigDecimal.ZERO;
                    Map map = new HashMap();
                    if (resultSet.next())
                    {
                        BigDecimal actualMoneySum = resultSet.getBigDecimal(1);
                        BigDecimal reMoneySum = resultSet.getBigDecimal(2);
                        BigDecimal dfEarnSum = reMoneySum.subtract(actualMoneySum);
                        map.put("actualMoneySum", actualMoneySum);
                        map.put("reMoneySum", reMoneySum);
                        map.put("dfEarnSum", dfEarnSum);
                    }
                    return map;
                }
            }, sqlBf.toString(), parameters);
        }
    }
    
    @Override
    public void exportPtdf(DFStatisticsEntity[] recWits, OutputStream outputStream, String charset)
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
            writer.write("借款ID");
            writer.write("期数");
            writer.write("借款用户名");
            writer.write("垫付方");
            writer.write("实际垫付金额(元)");
            writer.write("垫付时间");
            writer.write("返还金额(元)");
            writer.write("垫付盈亏(元)");
            
            writer.newLine();
            int i = 0;
            for (DFStatisticsEntity recWit : recWits)
            {
                i++;
                writer.write(i);
                writer.write(recWit.loanId);
                writer.write(recWit.periods + "\t");
                writer.write(recWit.jkrAccount);
                writer.write(recWit.dfAccountName);
                writer.write(recWit.actualMoney);
                writer.write(TimestampParser.format(recWit.dfTime));
                writer.write(recWit.reMoney);
                writer.write(recWit.dfEarn);
                writer.newLine();
            }
        }
    }
}
