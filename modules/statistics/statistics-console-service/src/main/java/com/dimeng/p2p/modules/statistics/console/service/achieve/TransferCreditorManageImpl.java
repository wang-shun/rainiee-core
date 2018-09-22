package com.dimeng.p2p.modules.statistics.console.service.achieve;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
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
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F10;
import com.dimeng.p2p.S62.enums.T6262_F11;
import com.dimeng.p2p.modules.statistics.console.service.RechargeWithdrawManage;
import com.dimeng.p2p.modules.statistics.console.service.TransferCreditorManage;
import com.dimeng.p2p.modules.statistics.console.service.entity.TransferCreditorEntity;
import com.dimeng.p2p.modules.statistics.console.service.query.TransferCreditorQuery;
import com.dimeng.util.Formater;
import com.dimeng.util.StringHelper;
import com.dimeng.util.io.CVSWriter;

public class TransferCreditorManageImpl extends AbstractStatisticsService implements TransferCreditorManage
{
    
    public TransferCreditorManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    final ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
    
    @Override
    public PagingResult<TransferCreditorEntity> getCreditorList(TransferCreditorQuery query, Paging paging)
        throws Throwable
    {
        List<Object> parameters = new ArrayList<>();
        StringBuffer sqlBf = new StringBuffer();
        sqlBf.append(" SELECT * FROM (SELECT T6251.F02 AS F01, T6230.F25 AS F02, T6230.F03 AS F03, T6262.F03 AS F04, ");
        sqlBf.append(" ( SELECT F02 FROM S61.T6110 WHERE T6110.F01 = T6262.F03 LIMIT 1) AS F05, ");
        sqlBf.append(" ( SELECT F02 FROM S61.T6141 WHERE F01 = T6262.F03 LIMIT 1) AS F06,  ");
        sqlBf.append(" T6251.F04 AS F07,      ");
        sqlBf.append(" ( SELECT F02 FROM S61.T6110 WHERE T6110.F01 = T6251.F04 LIMIT 1) AS F08, ");
        sqlBf.append(" ( SELECT F02 FROM S61.T6141 WHERE F01 = T6251.F04 LIMIT 1) AS F09,  ");
        sqlBf.append(" T6260.F05 AS F10,   T6262.F06 AS F11, T6231.F02 AS F12, T6262.F10 AS F13, T6262.F04 AS F14, T6262.F07 AS F15, T6262.F05 AS F16, T6262.F09 AS F17, ");
        sqlBf.append(" ( SELECT IFNULL(SUM(T6252.F07),0) FROM S62.T6252 WHERE T6252.F02 = T6230.F01 AND T6252.F05 IN(7001,7002) AND T6252.F09='WH' AND T6252.F04 = T6262.F03) AS F18  ");
        sqlBf.append(" ,T6260.F08 AS F19,T6262.F11 AS F20,mrzh.F06 AS F21,mrzh.F10 AS F22,mczh.F06 AS F23,mczh.F10 AS F24,T6262.F08 AS F25 ");
        sqlBf.append(" FROM S62.T6262 ");
        sqlBf.append(" INNER JOIN S62.T6260 ON T6262.F02 = T6260.F01 ");
        sqlBf.append(" INNER JOIN S62.T6251 ON T6260.F02 = T6251.F01 ");
        sqlBf.append(" INNER JOIN S62.T6230 ON T6251.F03 = T6230.F01 ");
        sqlBf.append(" INNER JOIN S62.T6231 ON T6231.F01 = T6230.F01 ");
        sqlBf.append(" INNER JOIN S61.T6110 AS mrzh ON T6262.F03 = mrzh.F01 ");
        sqlBf.append(" INNER JOIN S61.T6110 AS mczh ON T6251.F04 = mczh.F01 ");
        sqlBf.append(" ) tmp where 1=1 ");
        sqlAndParameterProcess(query, sqlBf, parameters);
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, ARRAY_PARSER, paging, sqlBf.toString(), parameters);
        }
    }
    
    protected final ArrayParser<TransferCreditorEntity> ARRAY_PARSER = new ArrayParser<TransferCreditorEntity>()
    {
        
        @Override
        public TransferCreditorEntity[] parse(ResultSet resultSet)
            throws SQLException
        {
            List<TransferCreditorEntity> list = new ArrayList<TransferCreditorEntity>();
            while (resultSet.next())
            {
                TransferCreditorEntity entity = new TransferCreditorEntity();
                entity.creditorId = resultSet.getString(1); // 债权ID
                entity.loanId = resultSet.getString(2); // 借款ID
                entity.buyAccount = resultSet.getString(5);// 购买人账户
                entity.buyName = resultSet.getString(6); // 购买人姓名
                entity.sellAccount = resultSet.getString(8);// 卖出人账户
                entity.sellName = resultSet.getString(9); // 卖出人姓名
                entity.applyTime = resultSet.getTimestamp(10);// 债权转让申请时间
                entity.dealMoney = resultSet.getBigDecimal(11);// 债权转让手续费
                entity.surplusLimit = resultSet.getString(13) + "/" + resultSet.getString(12);//剩余期数
                entity.creditorWorth = resultSet.getBigDecimal(14);// 债权价值
                entity.buyTime = resultSet.getTimestamp(15);// 购买时间
                entity.lastMoney = resultSet.getBigDecimal(16);// 转让价格
                entity.transferEarn = resultSet.getBigDecimal(17);// 转让盈亏
                entity.dueInBX = resultSet.getBigDecimal(18);// 待收本息
                entity.dealRate = resultSet.getBigDecimal(19); //new BigDecimal(configureProvider.getProperty(SystemVariable.ZQZRGLF_RATE)); //交易费率
                entity.source = T6262_F11.parse(resultSet.getString(20)).getChineseName();
                String buyUserType = resultSet.getString(21);//买入账户类型
                String buyIsDbf = resultSet.getString(22);//买入账户是否为担保方
                if (T6110_F06.ZRR.name().equals(buyUserType))
                {
                    entity.buyUserType = "个人账户";
                }
                else if (T6110_F06.FZRR.name().equals(buyUserType) && T6110_F10.S.name().equals(buyIsDbf))
                {
                    entity.buyUserType = "机构账户";
                }
                else if (T6110_F06.FZRR.name().equals(buyUserType) && T6110_F10.F.name().equals(buyIsDbf))
                {
                    entity.buyUserType = "企业账户";
                }
                String sellUserType = resultSet.getString(23);//卖出账户类型
                String sellIsDbf = resultSet.getString(24);//卖出账户是否为担保方
                if (T6110_F06.ZRR.name().equals(sellUserType))
                {
                    entity.sellUserType = "个人账户";
                }
                else if (T6110_F06.FZRR.name().equals(sellUserType) && T6110_F10.S.name().equals(sellIsDbf))
                {
                    entity.sellUserType = "机构账户";
                }
                else if (T6110_F06.FZRR.name().equals(sellUserType) && T6110_F10.F.name().equals(sellIsDbf))
                {
                    entity.sellUserType = "企业账户";
                }
                entity.IntoEarn = resultSet.getBigDecimal(25);//转入盈亏
                list.add(entity);
            }
            return list.toArray(new TransferCreditorEntity[list.size()]);
        }
    };
    
    @Override
    public void export(TransferCreditorEntity[] recWits, Map total, OutputStream outputStream, String charset)
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
            writer.write("债权ID");
            writer.write("借款ID");
            writer.write("剩余期数");
            writer.write("卖出账户");
            writer.write("卖出账户类型");
            writer.write("卖出姓名");
            writer.write("申请卖出时间");
            writer.write("待收本息(元)");
            writer.write("债权价值(元)");
            writer.write("成交金额(元)");
            writer.write("交易费率");
            writer.write("交易费用(元)");
            writer.write("买入账户");
            writer.write("买入账户类型");
            writer.write("买入姓名");
            writer.write("购买时间");
            writer.write("转出盈亏(元)");
            writer.write("转入盈亏(元)");
            writer.write("来源");
            
            writer.newLine();
            int i = 0;
            for (TransferCreditorEntity recWit : recWits)
            {
                i++;
                writer.write(i);
                writer.write(recWit.creditorId);
                writer.write(recWit.loanId);
                writer.write(recWit.surplusLimit + "\t");
                writer.write(recWit.sellAccount);
                writer.write(recWit.sellUserType);
                writer.write(recWit.sellName);
                writer.write(recWit.applyTime);
                writer.write(recWit.dueInBX);
                writer.write(recWit.creditorWorth);
                writer.write(recWit.lastMoney);
                writer.write(Formater.formatRate(recWit.dealRate));
                writer.write(recWit.dealMoney);
                writer.write(recWit.buyAccount);
                writer.write(recWit.buyUserType);
                writer.write(recWit.buyName);
                writer.write(recWit.buyTime);
                writer.write(recWit.transferEarn);
                writer.write(recWit.IntoEarn);
                writer.write(recWit.source);
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
    public Map<String, String> getCreditorTotal(TransferCreditorQuery query)
        throws Throwable
    {
        StringBuffer sqlBf = new StringBuffer();
        sqlBf.append(" SELECT IFNULL(SUM(tmp.F16),0), IFNULL(SUM(tmp.F11),0), IFNULL(SUM(tmp.F25),0), IFNULL(SUM(tmp.F26),0) FROM (SELECT T6251.F02 AS F01, T6230.F25 AS F02, ");
        sqlBf.append(" ( SELECT F02 FROM S61.T6110 WHERE T6110.F01 = T6262.F03 LIMIT 1) AS F05, ");
        sqlBf.append(" ( SELECT F02 FROM S61.T6110 WHERE T6110.F01 = T6251.F04 LIMIT 1) AS F08, ");
        sqlBf.append(" T6260.F05 AS F10,   T6262.F06 AS F11,  T6262.F07 AS F15, T6262.F05 AS F16,T6262.F11 AS F20,mrzh.F06 AS F21,mrzh.F10 AS F22,mczh.F06 AS F23,mczh.F10 AS F24, T6262.F08 AS F25, T6262.F09 AS F26  ");
        sqlBf.append(" FROM S62.T6262 ");
        sqlBf.append(" INNER JOIN S62.T6260 ON T6262.F02 = T6260.F01 ");
        sqlBf.append(" INNER JOIN S62.T6251 ON T6260.F02 = T6251.F01 ");
        sqlBf.append(" INNER JOIN S62.T6230 ON T6251.F03 = T6230.F01 ");
        sqlBf.append(" INNER JOIN S62.T6231 ON T6231.F01 = T6230.F01 ");
        sqlBf.append(" INNER JOIN S61.T6110 AS mrzh ON T6262.F03 = mrzh.F01 ");
        sqlBf.append(" INNER JOIN S61.T6110 AS mczh ON T6251.F04 = mczh.F01 ");
        sqlBf.append(" ) tmp where 1=1 ");
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
                    Map map = new HashMap();
                    if (resultSet.next())
                    {
                        map.put("lastMoneyTotal", resultSet.getBigDecimal(1));
                        map.put("dealMoneyTotal", resultSet.getBigDecimal(2));
                        map.put("soldMoneyTotal", resultSet.getBigDecimal(3));
                        map.put("receiveMoneyTotal", resultSet.getBigDecimal(4));
                    }
                    return map;
                }
            }, sqlBf.toString(), parameters);
        }
    }
    
    public void sqlAndParameterProcess(TransferCreditorQuery query, StringBuffer sqlBf, List<Object> parameters)
        throws Throwable
    {
        if (query != null)
        {
            String loanId = query.getLoanId();
            if (!StringHelper.isEmpty(loanId))
            {
                sqlBf.append(" AND tmp.F02 like ? ");
                parameters.add(getSQLConnectionProvider().allMatch(loanId));
            }
            
            String creditorId = query.getCreditorId();
            if (!StringHelper.isEmpty(creditorId))
            {
                sqlBf.append(" AND tmp.F01 like ? ");
                parameters.add(getSQLConnectionProvider().allMatch(creditorId));
            }
            
            String sellAccount = query.getSellAccount();
            if (!StringHelper.isEmpty(sellAccount))
            {
                sqlBf.append(" AND tmp.F08 like ?");
                parameters.add(getSQLConnectionProvider().allMatch(sellAccount));
            }
            
            String buyAccount = query.getBuyAccount();
            if (!StringHelper.isEmpty(buyAccount))
            {
                sqlBf.append(" AND tmp.F05 like ?");
                parameters.add(getSQLConnectionProvider().allMatch(buyAccount));
            }
            
            Timestamp timestamp = query.getApplyTimeStart();
            if (timestamp != null)
            {
                sqlBf.append(" AND DATE(tmp.F10) >=?");
                parameters.add(timestamp);
            }
            
            timestamp = query.getApplyTimeEnd();
            if (timestamp != null)
            {
                sqlBf.append(" AND DATE(tmp.F10) <=?");
                parameters.add(timestamp);
            }
            
            timestamp = query.getBuyTimeStart();
            if (timestamp != null)
            {
                sqlBf.append(" AND DATE(tmp.F15) >=?");
                parameters.add(timestamp);
            }
            
            timestamp = query.getBuyTimeEnd();
            if (timestamp != null)
            {
                sqlBf.append(" AND DATE(tmp.F15) <=?");
                parameters.add(timestamp);
            }
            
            String source = query.getSource();
            if (!StringHelper.isEmpty(source))
            {
                sqlBf.append(" AND tmp.F20 =?");
                parameters.add(source);
            }
            
            String buyUserType = query.getBuyUserType();
            if (!StringHelper.isEmpty(buyUserType))
            {
                if (buyUserType.equals("ZRR"))
                {
                    sqlBf.append(" AND tmp.F21 = 'ZRR' AND tmp.F22 = 'F'");
                }
                else if (buyUserType.equals("FZRR"))
                {
                    sqlBf.append(" AND tmp.F21 = 'FZRR' AND tmp.F22 = 'F'");
                }
                else if (buyUserType.equals("FZRRJG"))
                {
                    sqlBf.append(" AND tmp.F21 = 'FZRR' AND tmp.F22 = 'S'");
                }
            }
            
            String sellUserType = query.getSellUserType();
            if (!StringHelper.isEmpty(sellUserType))
            {
                if (sellUserType.equals("ZRR"))
                {
                    sqlBf.append(" AND tmp.F23 = 'ZRR' AND tmp.F24 = 'F'");
                }
                else if (sellUserType.equals("FZRR"))
                {
                    sqlBf.append(" AND tmp.F23 = 'FZRR' AND tmp.F24 = 'F'");
                }
                else if (sellUserType.equals("FZRRJG"))
                {
                    sqlBf.append(" AND tmp.F23 = 'FZRR' AND tmp.F24 = 'S'");
                }
            }
            
            sqlBf.append(" ORDER BY tmp.F15 DESC");
        }
    }
    
}
