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
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F10;
import com.dimeng.p2p.S62.enums.T6252_F09;
import com.dimeng.p2p.modules.statistics.console.service.RechargeWithdrawManage;
import com.dimeng.p2p.modules.statistics.console.service.RepaymentStatisticsManage;
import com.dimeng.p2p.modules.statistics.console.service.entity.RepaymentStatisticsEntity;
import com.dimeng.p2p.modules.statistics.console.service.query.RepaymentStatisticsQuery;
import com.dimeng.util.StringHelper;
import com.dimeng.util.io.CVSWriter;
import com.dimeng.util.parser.DateParser;

public class RepaymentStatisticsManageImpl extends AbstractStatisticsService implements RepaymentStatisticsManage
{
    
    public RepaymentStatisticsManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public PagingResult<RepaymentStatisticsEntity> getRepaymentList(RepaymentStatisticsQuery query, Paging paging)
        throws Throwable
    {
        List<Object> parameters = new ArrayList<>();
        StringBuffer sqlBf = new StringBuffer();
        sqlBf.append(" SELECT * FROM ( SELECT	T6230.F25 AS F01,	T6230.F03 AS F02,	T6110.F02 AS F03,	T6110.F06 AS F04,	T6110.F10 AS F05, ");
        sqlBf.append(" (SELECT	F04	FROM S61.T6161 WHERE T6161.F01 = (SELECT F03 FROM S62.T6236 WHERE F02 = T6230.F01 LIMIT 1 )LIMIT 1) AS F06, ");
        sqlBf.append(" T6252.F08 AS F07,T6252.F09 AS F08,T6252.F05 AS F09,T6252.F07 AS F10,T6252.F10 AS F11,SK6110.F06 AS F12,SK6110.F02 AS F13, ");
        sqlBf.append(" (SELECT COUNT(1) FROM S62.T6250 WHERE T6250.F02 = T6230.F01 AND T6250.F03 = SK6110.F01) AS F14");
        sqlBf.append(" FROM	S62.T6230 ");
        sqlBf.append(" INNER JOIN S61.T6110 ON T6230.F02 = T6110.F01 ");
        sqlBf.append(" INNER JOIN S62.T6231 ON T6230.F01 = T6231.F01 ");
        sqlBf.append(" INNER JOIN S62.T6252 ON T6230.F01 = T6252.F02 ");
        sqlBf.append(" INNER JOIN S61.T6110 SK6110 ON T6252.F04 = SK6110.F01 ");
        sqlBf.append(" WHERE T6252.F05 IN(?,?,?,?,?) ) tmp where 1=1 ");
        parameters.add(FeeCode.TZ_BJ);
        parameters.add(FeeCode.TZ_LX);
        parameters.add(FeeCode.TZ_FX);
        parameters.add(FeeCode.TZ_WYJ);
        parameters.add(FeeCode.TZ_WYJ_SXF);
        sqlAndParameterProcess(query, sqlBf, parameters);
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, ARRAY_PARSER, paging, sqlBf.toString(), parameters);
        }
    }
    
    protected static final ArrayParser<RepaymentStatisticsEntity> ARRAY_PARSER =
        new ArrayParser<RepaymentStatisticsEntity>()
        {
            
            @Override
            public RepaymentStatisticsEntity[] parse(ResultSet resultSet)
                throws SQLException
            {
                List<RepaymentStatisticsEntity> list = new ArrayList<RepaymentStatisticsEntity>();
                while (resultSet.next())
                {
                    RepaymentStatisticsEntity entity = new RepaymentStatisticsEntity();
                    entity.id = resultSet.getString(1); // '借款ID',
                    entity.title = resultSet.getString(2);// '借款标题',
                    entity.account = resultSet.getString(3);// '借款账户',
                    String userType = resultSet.getString(4);// '用户类型',
                    String isAssure = resultSet.getString(5);// '是否担保方',
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
                    entity.guaranteeOrg = resultSet.getString(6);// '担保机构',
                    entity.shouldTheDate = resultSet.getTimestamp(7);// 合约还款日期
                    int subject = resultSet.getInt(9);// 科目
                    if (T6110_F06.FZRR.name().equals(resultSet.getString(12)))
                    {
                        if (FeeCode.TZ_WYJ_SXF == subject)
                        {
                            entity.state = T6252_F09.YH.getChineseName();// '状态',
                        }
                        else if (resultSet.getInt(14) <= 0)
                        {
                            entity.state = T6252_F09.DF.getChineseName();// '状态',
                        }
                        else
                        {
                            entity.state = T6252_F09.parse(resultSet.getString(8)).getChineseName();// '状态',
                        }
                        if (StringHelper.isEmpty(entity.guaranteeOrg) && !StringHelper.isEmpty(resultSet.getString(13))
                            && FeeCode.TZ_WYJ_SXF != subject && resultSet.getInt(14) <= 0)
                        {
                            entity.guaranteeOrg = "平台";
                        }
                    }
                    else
                    {
                        entity.state = T6252_F09.parse(resultSet.getString(8)).getChineseName();// '状态',
                    }
                    if (FeeCode.TZ_BJ == subject)
                    {
                        entity.subject = "本金";
                    }
                    else if (FeeCode.TZ_LX == subject)
                    {
                        entity.subject = "利息";
                    }
                    else if (FeeCode.TZ_FX == subject)
                    {
                        entity.subject = "逾期罚息";
                    }
                    else if (FeeCode.TZ_WYJ == subject)
                    {
                        entity.subject = "提前还款违约金";
                    }
                    else if (FeeCode.TZ_WYJ_SXF == subject)
                    {
                        entity.subject = "违约金手续费";
                    }
                    entity.repaymentPrice = resultSet.getBigDecimal(10);// '金额'
                    entity.actualDate = resultSet.getTimestamp(11);// '实际还款日期
                    list.add(entity);
                }
                return list.toArray(new RepaymentStatisticsEntity[list.size()]);
            }
        };
    
    @Override
    public void export(RepaymentStatisticsEntity[] recWits, BigDecimal total, OutputStream outputStream, String charset)
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
            writer.write("借款账户");
            writer.write("账户类型");
            writer.write("担保机构");
            writer.write("合约还款日期");
            writer.write("状态");
            writer.write("科目");
            writer.write("金额(元)");
            writer.write("实际还款日期");
            
            writer.newLine();
            int i = 0;
            for (RepaymentStatisticsEntity recWit : recWits)
            {
                i++;
                writer.write(i);
                writer.write(recWit.id);
                writer.write(recWit.account);
                writer.write(recWit.accountType);
                writer.write(recWit.guaranteeOrg);
                writer.write(DateParser.format(recWit.shouldTheDate));
                writer.write(recWit.state);
                writer.write(recWit.subject);
                writer.write(recWit.repaymentPrice);
                writer.write(recWit.actualDate);
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
    public BigDecimal getRepaymentTotal(RepaymentStatisticsQuery query)
        throws Throwable
    {
        StringBuffer sqlBf = new StringBuffer();
        sqlBf.append(" SELECT IFNULL(SUM(F10),0) FROM ( SELECT	T6230.F25 AS F01,	T6230.F03 AS F02,	T6110.F02 AS F03, ");
        sqlBf.append(" T6252.F08 AS F07,T6252.F09 AS F08,T6252.F05 AS F09,T6252.F07 AS F10,T6252.F10 AS F11,SK6110.F06 AS F12,SK6110.F02 AS F13,");
        sqlBf.append(" (SELECT COUNT(1) FROM S62.T6250 WHERE T6250.F02 = T6230.F01 AND T6250.F03 = SK6110.F01) AS F14");
        sqlBf.append(" FROM	S62.T6230 ");
        sqlBf.append(" INNER JOIN S61.T6110 ON T6230.F02 = T6110.F01 ");
        sqlBf.append(" INNER JOIN S62.T6231 ON T6230.F01 = T6231.F01 ");
        sqlBf.append(" INNER JOIN S62.T6252 ON T6230.F01 = T6252.F02 ");
        sqlBf.append(" INNER JOIN S61.T6110 SK6110 ON T6252.F04 = SK6110.F01 ");
        sqlBf.append(" WHERE T6252.F05 IN(?,?,?,?,?) ) tmp where 1=1 ");
        List<Object> parameters = new ArrayList<Object>();
        parameters.add(FeeCode.TZ_BJ);
        parameters.add(FeeCode.TZ_LX);
        parameters.add(FeeCode.TZ_FX);
        parameters.add(FeeCode.TZ_WYJ);
        parameters.add(FeeCode.TZ_WYJ_SXF);
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
    
    public void sqlAndParameterProcess(RepaymentStatisticsQuery query, StringBuffer sqlBf, List<Object> parameters)
        throws Throwable
    {
        if (query != null)
        {
            String id = query.getId();
            if (!StringHelper.isEmpty(id))
            {
                sqlBf.append(" AND tmp.F01 like ? ");
                parameters.add(getSQLConnectionProvider().allMatch(id));
            }
            
            String title = query.getLoanTitle();
            if (!StringHelper.isEmpty(title))
            {
                sqlBf.append(" AND tmp.F02 LIKE ?");
                parameters.add(getSQLConnectionProvider().allMatch(title));
            }
            
            String state = query.getState();
            if (!StringHelper.isEmpty(state))
            {
                //垫付根据还款记录中的收款人判断
                if (state.equals(T6252_F09.DF.name()))
                {
                    sqlBf.append(" AND tmp.F12 = 'FZRR' AND tmp.F14<=0 AND tmp.F09<>7007");
                }
                else
                {
                    sqlBf.append(" AND tmp.F08 = ?");
                    parameters.add(state);
                    sqlBf.append(" AND (tmp.F12 = 'ZRR' OR tmp.F09=7007 OR (tmp.F12 = 'FZRR' AND tmp.F14>0))");
                }
            }
            
            String subject = query.getSubject();
            if (!StringHelper.isEmpty(subject))
            {
                sqlBf.append(" AND tmp.F09 = ?");
                parameters.add(subject);
            }
            
            Timestamp timestamp = query.getShouldTheDateEnd();
            if (timestamp != null)
            {
                sqlBf.append(" AND DATE(tmp.F07) <=?");
                parameters.add(timestamp);
            }
            
            timestamp = query.getShouldTheDateStart();
            if (timestamp != null)
            {
                sqlBf.append(" AND DATE(tmp.F07) >=?");
                parameters.add(timestamp);
            }
            
            timestamp = query.getActualDateEnd();
            if (timestamp != null)
            {
                sqlBf.append(" AND DATE(tmp.F11) <=?");
                parameters.add(timestamp);
            }
            
            timestamp = query.getActualDateStart();
            if (timestamp != null)
            {
                sqlBf.append(" AND DATE(tmp.F11) >=?");
                parameters.add(timestamp);
            }
            
            String loanAccount = query.getLoanAccount();
            if (!StringHelper.isEmpty(loanAccount))
            {
                sqlBf.append(" AND tmp.F03 like ? ");
                parameters.add(getSQLConnectionProvider().allMatch(loanAccount));
            }
            
            sqlBf.append(" order by tmp.F11 DESC");
        }
    }
    
}
