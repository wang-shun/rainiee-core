package com.dimeng.p2p.modules.account.console.experience.service.achieve;

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

import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.enums.T6103_F06;
import com.dimeng.p2p.S61.enums.T6103_F08;
import com.dimeng.p2p.S62.enums.T6231_F21;
import com.dimeng.p2p.S62.enums.T6285_F09;
import com.dimeng.p2p.modules.account.console.experience.service.ExperienceDetailManage;
import com.dimeng.p2p.modules.account.console.experience.service.entity.ExperienceDetail;
import com.dimeng.p2p.modules.account.console.experience.service.entity.ExperienceProfit;
import com.dimeng.p2p.modules.account.console.experience.service.entity.ExperienceTotalInfo;
import com.dimeng.p2p.modules.account.console.experience.service.entity.ExperienceTotalList;
import com.dimeng.p2p.modules.account.console.experience.service.query.ExperienceDetailQuery;
import com.dimeng.util.StringHelper;
import com.dimeng.util.io.CVSWriter;
import com.dimeng.util.parser.EnumParser;

public class ExperienceDetailImpl extends AbstractExperienceService implements ExperienceDetailManage
{
    
    public ExperienceDetailImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public PagingResult<ExperienceDetail> search(ExperienceDetailQuery query, Paging paging)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT T6110.F01,T6110.F02,T6230.F25,T6286.F04,T6230.F06,T6230.F08,T6103.F06,T6286.F05,T6103.F11,T6230.F01 FROM S62.T6286 INNER JOIN S62.T6230 ON T6286.F02=T6230.F01 INNER JOIN S61.T6110 ON T6286.F03=T6110.F01 INNER JOIN S61.T6103 ON T6286.F03=T6103.F02");
        // TODO 条件查询
        final List<ExperienceDetail> lists = new ArrayList<>();
        List<String> parameters = new ArrayList<>();
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<ExperienceDetail>()
            {
                
                @Override
                public ExperienceDetail[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    while (resultSet.next())
                    {
                        ExperienceDetail experienceDetail = new ExperienceDetail();
                        experienceDetail.userId = resultSet.getInt(1);
                        experienceDetail.accountName = resultSet.getString(2);
                        experienceDetail.bidNo = resultSet.getString(3);
                        experienceDetail.experience = resultSet.getBigDecimal(4);
                        experienceDetail.amount = resultSet.getBigDecimal(4);
                        experienceDetail.rate = resultSet.getBigDecimal(5);
                        experienceDetail.trem = resultSet.getInt(6);
                        experienceDetail.status = EnumParser.parse(T6103_F06.class, resultSet.getString(7));
                        experienceDetail.time = resultSet.getTimestamp(8);
                        experienceDetail.qxTime = resultSet.getDate(9);
                        experienceDetail.bidId = resultSet.getInt(10);
                        experienceDetail.profit = getSy(experienceDetail.userId, experienceDetail.bidId);
                        experienceDetail.name = getRealName(experienceDetail.userId);
                        lists.add(experienceDetail);
                    }
                    return lists.toArray(new ExperienceDetail[lists.size()]);
                }
            }, paging, sql.toString(), parameters);
        }
    }
    
    /**
     * 体验金详情-查看
     * @param id
     * @param paging
     * @return
     * @throws Throwable
     */
    @Override
    public PagingResult<ExperienceProfit> get(String id, int userId, Paging paging)
        throws Throwable
    {
        StringBuffer sql =
            new StringBuffer(
                "SELECT T6230.F25 AS F01,T6230.F06 AS F02,T6230.F09 AS F03,T6231.F21 AS F04,T6231.F22 AS F05,T6285.F08 AS F06,T6285.F07 AS F07,T6285.F09 AS F08,T6103.F07 AS F09, T6103.F16 AS F10 ");
        sql.append(" FROM S62.T6285 INNER JOIN S62.T6230 ON T6285.F02 = T6230.F01 INNER JOIN S62.T6231 ON T6230.F01 = T6231.F01 INNER JOIN S61.T6103 ON T6103.F01 = T6285.F12 WHERE T6285.F02 = ? AND T6285.F04 = ?");
        final List<ExperienceProfit> lists = new ArrayList<>();
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<ExperienceProfit>()
            {
                @Override
                public ExperienceProfit[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    while (resultSet.next())
                    {
                        ExperienceProfit record = new ExperienceProfit();
                        record.bidNo = resultSet.getString(1);
                        record.rate = resultSet.getBigDecimal(2);
                        record.num = resultSet.getInt(3);
                        record.dayOrMonth = T6231_F21.parse(resultSet.getString(4));
                        record.borrowDays = resultSet.getInt(5);
                        record.time = resultSet.getDate(6);
                        record.interest = resultSet.getBigDecimal(7);
                        record.status = T6285_F09.parse(resultSet.getString(8));
                        record.totalNum = resultSet.getInt(9);
                        record.incomeMethod = resultSet.getString(10);
                        lists.add(record);
                    }
                    return lists.toArray(new ExperienceProfit[lists.size()]);
                }
            }, paging, String.valueOf(sql), id, userId);
        }
    }
    
    @Override
    public void export(ExperienceDetail[] experienceDetails, OutputStream outputStream)
        throws Throwable
    {
        if (experienceDetails == null)
        {
            return;
        }
        if (outputStream == null)
        {
            return;
        }
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(outputStream, "GBK")))
        {
            CVSWriter writer = new CVSWriter(out);
            writer.write("序号");
            writer.write("用户名");
            writer.write("姓名");
            writer.write("标标号");
            writer.write("投资金额(元)");
            writer.write("体验金(元)");
            writer.write("年化利率");
            writer.write("项目期限");
            writer.write("状态");
            writer.write("投资时间");
            writer.write("收益(元)");
            writer.write("起息日");
            writer.newLine();
            int i = 1;
            for (ExperienceDetail experienceDetail : experienceDetails)
            {
                writer.write(i++);
                writer.write(experienceDetail.accountName);
                writer.write(experienceDetail.name);
                writer.write(experienceDetail.bidNo);
                writer.write(experienceDetail.amount);
                writer.write(experienceDetail.experience);
                writer.write(experienceDetail.rate);
                writer.write(experienceDetail.trem);
                writer.write(experienceDetail.status);
                writer.write(experienceDetail.time);
                writer.write(experienceDetail.profit);
                writer.write(experienceDetail.qxTime);
                writer.newLine();
            }
        }
    }
    
    /**
     * 查询用户真实姓名
     * 
     * @param userId
     * @return
     * @throws java.sql.SQLException
     * @throws Throwable
     */
    private String getRealName(int userId)
        throws SQLException
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement("SELECT F02 FROM S61.T6141 WHERE F01=?"))
            {
                ps.setInt(1, userId);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        return rs.getString(1);
                    }
                }
            }
        }
        return "";
    }
    
    /**
     * 计算收益
     * 
     * @param bidId
     * @return
     * @throws java.sql.SQLException
     */
    private BigDecimal getSy(int bidId, int userId)
        throws SQLException
    {
        BigDecimal amount = new BigDecimal(0);
        String sql = "SELECT IFNULL(SUM(F07 + F11),0) FROM S62.T6285 WHERE T6285.F04 = ? AND T6285.F02 = ?";
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql))
            {
                ps.setInt(1, userId);
                ps.setInt(2, bidId);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        amount = rs.getBigDecimal(1);
                    }
                }
            }
        }
        return amount;
    }
    
    /**
     * 统计体验金概要信息
     * @return ExperienceTotalInfo
     * @throws Throwable
     */
    @Override
    public ExperienceTotalInfo getExperienceTotal()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT (SELECT SUM(IFNULL(T6103.F03,0)) FROM S61.T6103) AS totalMoney,(SELECT SUM(IFNULL(T6103.F03,0)) FROM S61.T6103 WHERE T6103.F06 IN ('YTZ','YJQ')) AS userTotalMoney,(SELECT SUM(IFNULL(T6285.F07,0)) FROM S62.T6285) AS investMoney"))
            {
                try (ResultSet rs = ps.executeQuery())
                {
                    ExperienceTotalInfo info = null;
                    if (rs.next())
                    {
                        info = new ExperienceTotalInfo();
                        //总派出体验金
                        info.totalMoney = rs.getBigDecimal(1);
                        //已使用的体验金
                        info.totalUsedMoney = rs.getBigDecimal(2);
                        //体验金所得利息
                        info.returnMoney = rs.getBigDecimal(3);
                    }
                    return info;
                }
            }
        }
    }
    
    /**
     * 体验金列表
     * @param query  查询条件
     * @param paging 分页参数
     * @return PagingResult
     * @throws Throwable
     */
    @Override
    public PagingResult<ExperienceTotalList> searchTotalList(ExperienceDetailQuery query, Paging paging)
        throws Throwable
    {
        //拼接SQL，查询用户名、姓名、来源、派出金额、被邀请用户名、被邀请用户姓名、体验金开始时间、体验金失效时间、状态、借款人用户名、借款人姓名、借款项目、借款期、年化利率、可得利息、投资时间
        StringBuilder sql =
            new StringBuilder(
                "SELECT T6110.F02 AS F01,(SELECT F02 FROM S61.T6141 WHERE T6141.F01 = T6286.F03) AS F02,T6230.F25 AS F03,");
        sql.append(" (SELECT SUM(F04) FROM S62.T6250 WHERE T6250.F02 = T6230.F01 AND T6250.F03 = T6286.F03) AS F04,");
        sql.append(" SUM(T6286.F04) AS F05,T6230.F06 AS F06,T6231.F21 AS F07,T6231.F22 AS F08,T6230.F09 AS F09,T6103.F06 AS F10,T6286.F05 AS F11,T6103.F11 AS F12,");
        sql.append(" (SELECT SUM(F07) FROM S62.T6285 WHERE T6285.F02 = T6230.F01 AND T6285.F04 = T6286.F03) AS F13,T6230.F01 AS F14,T6103.F08 AS F15,T6110.F01 AS F16, ");
        sql.append(" S61.T6103.F07 AS F17, S61.T6103.F16 AS F18 FROM S62.T6230 ");
        sql.append(" INNER JOIN S62.T6286 ON T6230.F01 = T6286.F02 INNER JOIN S62.T6231 ON T6230.F01 = T6231.F01 INNER JOIN S61.T6103 ON T6286.F03 = T6103.F02 AND T6103.F01 = T6286.F10 AND T6103.F13 = T6230.F01");
        sql.append(" INNER JOIN S61.T6110 ON T6110.F01 = T6286.F03 WHERE T6103.F06 IN (?,?) ");
        //封装查询参数
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(T6103_F06.YTZ);
        parameters.add(T6103_F06.YJQ);
        searchTotalListParams(query, sql, parameters);
        sql.append(" GROUP BY T6230.F01,T6286.F03 ORDER BY T6286.F05 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<ExperienceTotalList>()
            {
                ArrayList<ExperienceTotalList> list = null;
                
                @Override
                public ExperienceTotalList[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    while (resultSet.next())
                    {
                        ExperienceTotalList info = new ExperienceTotalList();
                        info.userName = resultSet.getString(1);
                        info.userRealName = resultSet.getString(2);
                        info.bid = resultSet.getString(3);
                        info.investmoney = resultSet.getBigDecimal(4);
                        info.experienceMoney = resultSet.getBigDecimal(5);
                        info.jkNlv = resultSet.getBigDecimal(6);
                        info.borMethod = T6231_F21.parse(resultSet.getString(7));
                        info.jkDay = resultSet.getInt(8);
                        info.jkTime = resultSet.getInt(9);
                        info.status = T6103_F06.parse(resultSet.getString(10));
                        info.tbTime = resultSet.getTimestamp(11);
                        info.interestTime = resultSet.getTimestamp(12);
                        info.experienceInterest = resultSet.getBigDecimal(13);
                        info.id = resultSet.getString(14);
                        info.source = T6103_F08.parse(resultSet.getString(15));
                        info.userId = resultSet.getInt(16);
                        if("false".equals(resultSet.getString(18))){
                            info.expectedTerm = resultSet.getInt(17)+"天";
                        }else{
                            info.expectedTerm = resultSet.getInt(17)+"个月";
                        }
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(info);
                    }
                    return list == null || list.size() == 0 ? null : list.toArray(new ExperienceTotalList[list.size()]);
                }
            },
                paging,
                sql.toString(),
                parameters);
        }
    }
    
    @Override
    public ExperienceTotalList searchTotalAmount(ExperienceDetailQuery query)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder("SELECT IFNULL(SUM(t1.F01),0),IFNULL(SUM(t1.F02),0),IFNULL(SUM(t1.F03),0) FROM");
        sql.append(" (SELECT (SELECT SUM(F04) FROM S62.T6250 WHERE T6250.F02 = T6230.F01 AND T6250.F03 = T6286.F03) AS F01,");
        sql.append(" SUM(T6286.F04) AS F02,");
        sql.append(" (SELECT SUM(F07) FROM S62.T6285 WHERE T6285.F02 = T6230.F01 AND T6285.F04 = T6286.F03) AS F03 FROM S62.T6230 ");
        sql.append(" INNER JOIN S62.T6286 ON T6230.F01 = T6286.F02 INNER JOIN S62.T6231 ON T6230.F01 = T6231.F01 INNER JOIN S61.T6103 ON T6286.F03 = T6103.F02 AND T6103.F01 = T6286.F10 AND T6103.F13 = T6230.F01");
        sql.append(" INNER JOIN S61.T6110 ON T6110.F01 = T6286.F03 WHERE T6103.F06 IN (?,?) ");
        List<Object> parameters = new ArrayList<Object>();
        parameters.add(T6103_F06.YTZ);
        parameters.add(T6103_F06.YJQ);
        searchTotalListParams(query, sql, parameters);
        sql.append(" GROUP BY T6230.F01,T6286.F03 ");
        sql.append(" ORDER BY T6286.F05 DESC ) AS t1");
        // sql语句和查询参数处理
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<ExperienceTotalList>()
            {
                @Override
                public ExperienceTotalList parse(ResultSet resultSet)
                    throws SQLException
                {
                    ExperienceTotalList count = new ExperienceTotalList();
                    if (resultSet.next())
                    {
                        count.investmoney = resultSet.getBigDecimal(1);
                        count.experienceMoney = resultSet.getBigDecimal(2);
                        count.experienceInterest = resultSet.getBigDecimal(3);
                    }
                    return count;
                }
            }, sql.toString(), parameters);
        }
    }
    
    private void searchTotalListParams(ExperienceDetailQuery query, StringBuilder sql, List<Object> parameters)
        throws SQLException
    {
        SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
        if (query != null)
        {
            //体验金用户名查询条件
            if (!StringHelper.isEmpty(query.userName()))
            {
                sql.append(" AND T6110.F02 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(query.userName()));
            }
            //投资时间
            if (query.invalidStartTime() != null)
            {
                sql.append(" AND DATE(T6286.F05) >= ?");
                parameters.add(query.invalidStartTime());
            }
            if (query.invalidEndTime() != null)
            {
                sql.append(" AND DATE(T6286.F05) <= ?");
                parameters.add(query.invalidEndTime());
            }
            //利息生成开始时间
            if (query.lixiStartTime() != null)
            {
                sql.append(" AND DATE(T6103.F11) >= ?");
                parameters.add(query.lixiStartTime());
            }
            if (query.lixiEndTime() != null)
            {
                sql.append(" AND DATE(T6103.F11) <= ?");
                parameters.add(query.lixiEndTime());
            }
            //体验金状态
            if (query.status() != null)
            {
                sql.append(" AND T6103.F06 = ?");
                parameters.add(query.status().name());
            }
            //标编号
            if (!StringHelper.isEmpty(query.bid()))
            {
                sql.append(" AND T6230.F25 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(query.bid()));
            }
        }
    }
    
    /**
     * 导出
     * @param query 查询条件
     * @return ExperienceTotalList
     * @throws Throwable
     */
    @Override
    public ExperienceTotalList[] exportsExperienceLists(ExperienceDetailQuery query)
        throws Throwable
    {
        //拼接SQL，查询用户名、姓名、来源、派出金额、被邀请用户名、被邀请用户姓名、体验金开始时间、体验金失效时间、状态、借款人用户名、借款人姓名、借款项目、借款期、年化利率、可得利息、投资时间
        StringBuilder sql =
            new StringBuilder(
                "SELECT T6110.F02 AS F01,(SELECT F02 FROM S61.T6141 WHERE T6141.F01 = T6286.F03) AS F02,T6230.F25 AS F03,");
        sql.append("(SELECT SUM(F04) FROM S62.T6250 WHERE T6250.F02 = T6230.F01 AND T6250.F03 = T6286.F03) AS F04,");
        sql.append("SUM(T6286.F04) AS F05,T6230.F06 AS F06,T6231.F21 AS F07,T6231.F22 AS F08,T6230.F09 AS F09,T6103.F06 AS F10,T6286.F05 AS F11,T6103.F11 AS F12,");
        sql.append("(SELECT SUM(F07) FROM S62.T6285 WHERE T6285.F02 = T6230.F01 AND T6285.F04 = T6286.F03) AS F13,T6230.F01 AS F14,T6103.F08 AS F15 FROM S62.T6230 ");
        sql.append("INNER JOIN S62.T6286 ON T6230.F01 = T6286.F02 INNER JOIN S62.T6231 ON T6230.F01 = T6231.F01 INNER JOIN S61.T6103 ON T6286.F03 = T6103.F02 AND T6103.F01 IN (T6286.F10)");
        sql.append("INNER JOIN S61.T6110 ON T6110.F01 = T6286.F03 WHERE T6103.F06 IN (?,?) ");
        //封装查询参数
        List<Object> parameters = new ArrayList<>();
        parameters.add(T6103_F06.YTZ);
        parameters.add(T6103_F06.YJQ);
        if (query != null)
        {
            SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
            //体验金用户名查询条件
            if (!StringHelper.isEmpty(query.userName()))
            {
                sql.append(" AND T6110.F02 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(query.userName()));
            }
            //投资时间
            if (query.invalidStartTime() != null)
            {
                sql.append(" AND DATE(T6286.F05) >= ?");
                parameters.add(query.invalidStartTime());
            }
            if (query.invalidEndTime() != null)
            {
                sql.append(" AND DATE(T6286.F05) <= ?");
                parameters.add(query.invalidEndTime());
            }
            //利息生成开始时间
            if (query.lixiStartTime() != null)
            {
                sql.append(" AND DATE(T6103.F11) >= ?");
                parameters.add(query.lixiStartTime());
            }
            if (query.lixiEndTime() != null)
            {
                sql.append(" AND DATE(T6103.F11) <= ?");
                parameters.add(query.lixiEndTime());
            }
            //体验金状态
            if (query.status() != null)
            {
                sql.append(" AND T6103.F06 = ?");
                parameters.add(query.status().name());
            }
            //标编号
            if (!StringHelper.isEmpty(query.bid()))
            {
                sql.append(" AND T6230.F25 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(query.bid()));
            }
        }
        sql.append(" GROUP BY T6230.F01,T6286.F03 ORDER BY T6286.F05 DESC");
        try (Connection connection = getConnection())
        {
            return selectAll(connection, new ArrayParser<ExperienceTotalList>()
            {
                @Override
                public ExperienceTotalList[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<ExperienceTotalList> list = null;
                    while (resultSet.next())
                    {
                        ExperienceTotalList info = new ExperienceTotalList();
                        info.userName = resultSet.getString(1);
                        info.userRealName = resultSet.getString(2);
                        info.bid = resultSet.getString(3);
                        info.investmoney = resultSet.getBigDecimal(4);
                        info.experienceMoney = resultSet.getBigDecimal(5);
                        info.jkNlv = resultSet.getBigDecimal(6);
                        info.borMethod = T6231_F21.parse(resultSet.getString(7));
                        info.jkDay = resultSet.getInt(8);
                        info.jkTime = resultSet.getInt(9);
                        info.status = T6103_F06.parse(resultSet.getString(10));
                        info.tbTime = resultSet.getTimestamp(11);
                        info.interestTime = resultSet.getTimestamp(12);
                        info.experienceInterest = resultSet.getBigDecimal(13);
                        info.id = resultSet.getString(14);
                        info.source = T6103_F08.parse(resultSet.getString(15));
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(info);
                    }
                    return list == null || list.size() == 0 ? null : list.toArray(new ExperienceTotalList[list.size()]);
                }
                
            },
                String.valueOf(sql),
                parameters);
        }
    }
}
