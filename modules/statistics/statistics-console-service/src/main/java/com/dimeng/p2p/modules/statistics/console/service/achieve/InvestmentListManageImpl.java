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
import java.util.List;

import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F10;
import com.dimeng.p2p.S62.enums.T6230_F10;
import com.dimeng.p2p.S62.enums.T6231_F21;
import com.dimeng.p2p.S62.enums.T6250_F09;
import com.dimeng.p2p.S62.enums.T6250_F11;
import com.dimeng.p2p.modules.statistics.console.service.InvestmentListManage;
import com.dimeng.p2p.modules.statistics.console.service.RechargeWithdrawManage;
import com.dimeng.p2p.modules.statistics.console.service.entity.InvestmentListEntity;
import com.dimeng.p2p.modules.statistics.console.service.query.InvestmentQuery;
import com.dimeng.util.Formater;
import com.dimeng.util.StringHelper;
import com.dimeng.util.io.CVSWriter;
import com.dimeng.util.parser.DateTimeParser;

public class InvestmentListManageImpl extends AbstractStatisticsService implements InvestmentListManage
{
    
    public InvestmentListManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public PagingResult<InvestmentListEntity> getInvestmentList(InvestmentQuery query, Paging paging)
        throws Throwable
    {
        List<Object> parameters = new ArrayList<>();
        StringBuffer sqlBf = new StringBuffer();
        sqlBf.append(" SELECT * FROM (SELECT T6230.F25 AS F01, ");
        sqlBf.append(" T6230.F03 AS F02,	T6230.F05 AS F03,	T6230.F06 AS F04,	T6230.F09 AS F05,	T6230.F10 AS F06,	T6231.F12 AS F07,	(CASE WHEN T6230.F20 = 'YZR' THEN T6231.F34 ELSE T6231.F13 END ) AS F08,	T6110.F02 AS F09,	T6110.F06 AS F10,	T6110.F10 AS F11,	T6231.F21 AS F12,	T6231.F22 AS F13, ");
        sqlBf.append(" ( SELECT F04 FROM S61.T6161 WHERE T6161.F01 = ( SELECT F03 FROM S62.T6236 WHERE F02 = T6230.F01 LIMIT 1 ) LIMIT 1) AS F14, ");
        sqlBf.append(" ( SELECT F02 FROM S61.T6110 WHERE T6110.F01 = T6250.F03 LIMIT 1) AS F15, ");
        sqlBf.append(" ( SELECT F02 FROM S61.T6141 WHERE F01 = T6250.F03 LIMIT 1) AS F16,  ");
        sqlBf.append(" T6250.F04 AS F17,  T6250.F06 AS F18,  T6250.F11 AS F19 , T6250.F09 AS F20 , tzr.F06 AS F21 , tzr.F10 AS F22, ");
        sqlBf.append(" ( SELECT F04 FROM S61.T6161 WHERE F01 = T6250.F03 LIMIT 1) AS F23");
        sqlBf.append(" FROM S62.T6230 ");
        sqlBf.append(" INNER JOIN S61.T6110 ON T6230.F02 = T6110.F01 ");
        sqlBf.append(" INNER JOIN S62.T6231 ON T6230.F01 = T6231.F01 ");
        sqlBf.append(" INNER JOIN S62.T6250 ON T6230.F01 = T6250.F02 ");
        sqlBf.append(" INNER JOIN S61.T6110 AS tzr ON T6250.F03 = tzr.F01");
        sqlBf.append(" WHERE T6230.F20 in('YJQ', 'HKZ', 'YDF','YZR' ) AND T6250.F07 = 'F' AND T6250.F08 = 'S') tmp where 1 = 1");
        String loanUserType = query.getLoanUserType();
        if (!StringHelper.isEmpty(loanUserType))
        {
            sqlBf.append(" AND tmp.F10 = ? ");
            parameters.add(loanUserType);
        }
        String investUserType = query.getInvestUserType();
        if (!StringHelper.isEmpty(investUserType))
        {
            if (investUserType.equals("ZRR"))
            {
                sqlBf.append(" AND tmp.F21 = 'ZRR' AND tmp.F22 = 'F'");
            }
            else if (investUserType.equals("FZRR"))
            {
                sqlBf.append(" AND tmp.F21 = 'FZRR' AND tmp.F22 = 'F'");
            }
            else if (investUserType.equals("FZRRJG"))
            {
                sqlBf.append(" AND tmp.F21 = 'FZRR' AND tmp.F22 = 'S'");
            }
        }
        sqlAndParameterProcess(query, sqlBf, parameters);
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, ARRAY_PARSER, paging, sqlBf.toString(), parameters);
        }
    }
    
    protected static final ArrayParser<InvestmentListEntity> ARRAY_PARSER = new ArrayParser<InvestmentListEntity>()
    {
        
        @Override
        public InvestmentListEntity[] parse(ResultSet resultSet)
            throws SQLException
        {
            List<InvestmentListEntity> list = new ArrayList<InvestmentListEntity>();
            while (resultSet.next())
            {
                InvestmentListEntity entity = new InvestmentListEntity();
                entity.id = resultSet.getString(1); // '借款ID',
                entity.title = resultSet.getString(2);// '借款标题',
                entity.loanPrice = resultSet.getBigDecimal(3);// '借款金额',
                entity.annualRate = resultSet.getBigDecimal(4);// '年化利率',
                String isDays = resultSet.getString(12);// '是否为按天借款',
                if (T6231_F21.F.name().equals(isDays))
                {
                    entity.timeLimit = resultSet.getInt(5) + "个月";// '期限',
                }
                else
                {
                    entity.timeLimit = resultSet.getInt(13) + "天";// '借款天数',
                                                                  // '期限',
                }
                entity.wayOfRepayment = T6230_F10.parse(resultSet.getString(6)).getChineseName();// '还款方式',
                entity.loanTime = resultSet.getTimestamp(7);// '放款时间',
                entity.endDate = resultSet.getTimestamp(8);// '完结日期',
                entity.account = resultSet.getString(9);// '借款账户',
                String userType = resultSet.getString(10);// '用户类型',
                String isAssure = resultSet.getString(11);// '是否担保方',
                if (T6110_F06.ZRR.name().equals(userType))
                {
                    entity.accountType = "个人账户";
                }
                else if (T6110_F06.FZRR.name().equals(userType) && T6110_F10.S.name().equals(isAssure))
                {
                    entity.accountType = "机构账户";
                }
                else if (T6110_F06.FZRR.name().equals(userType) && T6110_F10.F.name().equals(isAssure))
                {
                    entity.accountType = "企业账户";
                }
                entity.guaranteeOrg = resultSet.getString(14);// '担保机构',
                entity.investAccoun = resultSet.getString(15);// '投资账户',
                entity.investName = resultSet.getString(16);// '投资姓名',
                entity.investPrice = resultSet.getBigDecimal(17);// '投资本金',
                entity.investDate = resultSet.getTimestamp(18);// '投资时间'
                entity.source = T6250_F11.parse(resultSet.getString(19)).getChineseName();// '来源',
                entity.bidWay = "手动";
                if (T6250_F09.S.name().equals(resultSet.getString(20)))
                {
                    entity.bidWay = "自动";
                }
                String investUserType = resultSet.getString(21);// '投资用户类型',
                String isDbf = resultSet.getString(22);// '投资用户是否担保方',
                if (T6110_F06.ZRR.name().equals(investUserType))
                {
                    entity.investAccountType = "个人账户";
                }
                else if (T6110_F06.FZRR.name().equals(investUserType) && T6110_F10.S.name().equals(isDbf))
                {
                    entity.investAccountType = "机构账户";
                }
                else if (T6110_F06.FZRR.name().equals(investUserType) && T6110_F10.F.name().equals(isDbf))
                {
                    entity.investAccountType = "企业账户";
                    entity.investName = resultSet.getString(23);
                }
                list.add(entity);
            }
            return list.toArray(new InvestmentListEntity[list.size()]);
        }
    };
    
    @Override
    public void export(InvestmentListEntity[] recWits, BigDecimal total, OutputStream outputStream, String charset)
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
            writer.write("投资人账户");
            writer.write("投资账户类型");
            writer.write("投资人姓名");
            writer.write("投资时间");
            writer.write("放款时间");
            writer.write("完结日期");
            writer.write("标的ID");
            writer.write("借款账户");
            writer.write("借款账户类型");
            writer.write("借款金额(元)");
            /*writer.write("担保机构");*/
            writer.write("年化利率");
            writer.write("还款方式");
            writer.write("期限");
            writer.write("投资金额(元)");
            writer.write("投资来源");
            writer.write("投资方式");
            writer.newLine();
            int i = 0;
            for (InvestmentListEntity recWit : recWits)
            {
                i++;
                writer.write(i);
                writer.write(recWit.investAccoun);
                writer.write(recWit.investAccountType);
                writer.write(recWit.investName);
                writer.write(DateTimeParser.format(recWit.investDate) + "\t");
                writer.write(DateTimeParser.format(recWit.loanTime) + "\t");
                writer.write(DateTimeParser.format(recWit.endDate) + "\t");
                writer.write(recWit.id);
                writer.write(recWit.account);
                writer.write(recWit.accountType);
                writer.write(Formater.formatAmount(recWit.loanPrice));
                /*writer.write(recWit.guaranteeOrg);*/
                writer.write(Formater.formatRate(recWit.annualRate));
                writer.write(recWit.wayOfRepayment);
                writer.write(recWit.timeLimit);
                writer.write(Formater.formatAmount(recWit.investPrice));
                writer.write(recWit.source);
                writer.write(recWit.bidWay);
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
    public BigDecimal getInverstmentTotal(InvestmentQuery query)
        throws Throwable
    {
        StringBuffer sqlBf = new StringBuffer();
        sqlBf.append(" select IFNULL(SUM(F17),0) from (SELECT	T6230.F25 AS F01,	T6230.F03 AS F02,	T6231.F12 AS F07,	T6231.F13 AS F08, T6110.F06 AS F10,");
        sqlBf.append(" (		SELECT			F02		FROM			S61.T6110		WHERE			T6110.F01 = T6250.F03		LIMIT 1	) AS F15, ");
        sqlBf.append(" T6250.F04 AS F17, T6250.F06 AS F18,  T6250.F11 AS F19, T6250.F09 AS F20, tzr.F06 AS F21 , tzr.F10 AS F22  ");
        sqlBf.append(" FROM S62.T6230 ");
        sqlBf.append(" INNER JOIN S61.T6110 ON T6230.F02 = T6110.F01 ");
        sqlBf.append(" INNER JOIN S62.T6231 ON T6230.F01 = T6231.F01 ");
        sqlBf.append(" INNER JOIN S62.T6250 ON T6230.F01 = T6250.F02 ");
        sqlBf.append(" INNER JOIN S61.T6110 AS tzr ON T6250.F03 = tzr.F01 ");
        sqlBf.append(" WHERE T6230.F20 in('YJQ', 'HKZ', 'YDF','YZR') AND T6250.F07 = 'F' AND T6250.F08 = 'S')tmp where 1 = 1 ");
        List<Object> parameters = new ArrayList<Object>();
        String loanUserType = query.getLoanUserType();
        if (!StringHelper.isEmpty(loanUserType))
        {
            sqlBf.append(" AND tmp.F10 = ? ");
            parameters.add(loanUserType);
        }
        String investUserType = query.getInvestUserType();
        if (!StringHelper.isEmpty(investUserType))
        {
            if (investUserType.equals("ZRR"))
            {
                sqlBf.append(" AND tmp.F21 = 'ZRR' AND tmp.F22 = 'F'");
            }
            else if (investUserType.equals("FZRR"))
            {
                sqlBf.append(" AND tmp.F21 = 'FZRR' AND tmp.F22 = 'F'");
            }
            else if (investUserType.equals("FZRRJG"))
            {
                sqlBf.append(" AND tmp.F21 = 'FZRR' AND tmp.F22 = 'S'");
            }
        }
        // sql语句和查询参数处理
        sqlAndParameterProcess(query, sqlBf, parameters);
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<BigDecimal>()
            {
                @Override
                public BigDecimal parse(ResultSet resultSet)
                    throws SQLException
                {
                    BigDecimal total = BigDecimal.ZERO;
                    if (resultSet.next())
                    {
                        total = resultSet.getBigDecimal(1);
                    }
                    return total;
                }
            }, sqlBf.toString(), parameters);
        }
    }
    
    public void sqlAndParameterProcess(InvestmentQuery query, StringBuffer sqlBf, List<Object> parameters)
        throws Throwable
    {
        if (query != null)
        {
            String id = query.getId();
            if (!StringHelper.isEmpty(id))
            {
                sqlBf.append(" AND tmp.F01 LIKE ? ");
                parameters.add(getSQLConnectionProvider().allMatch(id));
            }
            
            String title = query.getLoanTitle();
            if (!StringHelper.isEmpty(title))
            {
                sqlBf.append(" AND tmp.F02 LIKE ?");
                parameters.add(getSQLConnectionProvider().allMatch(title));
            }
            
            String investAccoun = query.getInvestAccoun();
            if (!StringHelper.isEmpty(investAccoun))
            {
                sqlBf.append(" AND tmp.F15 LIKE ?");
                parameters.add(getSQLConnectionProvider().allMatch(investAccoun));
            }
            
            Timestamp timestamp = query.getLoanTimeStart();
            if (timestamp != null)
            {
                sqlBf.append(" AND DATE(tmp.F07) >=?");
                parameters.add(timestamp);
            }
            
            timestamp = query.getLoanTimeEnd();
            if (timestamp != null)
            {
                sqlBf.append(" AND DATE(tmp.F07) <=?");
                parameters.add(timestamp);
            }
            
            timestamp = query.getFinishTimeStart();
            if (timestamp != null)
            {
                sqlBf.append(" AND DATE(tmp.F08) >=?");
                parameters.add(timestamp);
            }
            
            timestamp = query.getFinishTimeEnd();
            if (timestamp != null)
            {
                sqlBf.append(" AND DATE(tmp.F08) <=?");
                parameters.add(timestamp);
            }
            
            BigDecimal investPriceTmp = query.getInvestPriceStart();
            if (investPriceTmp != null)
            {
                sqlBf.append(" AND tmp.F17 >=?");
                parameters.add(investPriceTmp);
            }
            
            investPriceTmp = query.getInvestPriceEnd();
            if (investPriceTmp != null)
            {
                sqlBf.append(" AND tmp.F17 <=?");
                parameters.add(investPriceTmp);
            }
            
            String source = query.getSource();
            if (!StringHelper.isEmpty(source))
            {
                sqlBf.append(" AND tmp.F19 =?");
                parameters.add(source);
            }
            
            String bidWay = query.getBidWay();
            if (!StringHelper.isEmpty(bidWay))
            {
                sqlBf.append(" AND tmp.F20 =?");
                parameters.add(bidWay);
            }
            
            sqlBf.append(" ORDER BY tmp.F18 DESC");
        }
    }
    
}
