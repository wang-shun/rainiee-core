package com.dimeng.p2p.modules.account.console.experience.service.achieve;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
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
import com.dimeng.p2p.modules.account.console.experience.service.ExperienceStatisticsManage;
import com.dimeng.p2p.modules.account.console.experience.service.entity.ExperienceStaticTotal;
import com.dimeng.p2p.modules.account.console.experience.service.entity.ExperienceStatistics;
import com.dimeng.p2p.modules.account.console.experience.service.query.ExperienceStatisticsQuery;
import com.dimeng.util.StringHelper;
import com.dimeng.util.io.CVSWriter;

public class ExperienceStatisticsImpl extends AbstractExperienceService
		implements ExperienceStatisticsManage {

	public ExperienceStatisticsImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

    /**
     * 待还
     * @param query 查询条件
     * @param paging 分页
     * @return PagingResult
     * @throws Throwable
     */
	@Override
	public PagingResult<ExperienceStatistics> searchDh(
ExperienceStatisticsQuery query, Paging paging)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT T6230.F25 AS F01,T6110.F02 AS F02,(SELECT F02 FROM S61.T6141 WHERE T6141.F01 = T6285.F04) AS F03,T6103.F03 AS F04,T6285.F07 AS F05,T6285.F08 AS F06");
        sql.append(" FROM S62.T6285 INNER JOIN S62.T6230 ON T6285.F02 = T6230.F01 INNER JOIN S61.T6110 ON T6110.F01 = T6285.F04 INNER JOIN S61.T6103 ON T6103.F01 = T6285.F12  WHERE T6285.F09 <> 'YFH' ");
        List<Object> parameters = new ArrayList<>();
        searchDhParams(query, sql, parameters);
        sql.append(" ORDER BY T6285.F08");
        final List<ExperienceStatistics> lists = new ArrayList<>();
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<ExperienceStatistics>()
            {
                @Override
                public ExperienceStatistics[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    while (resultSet.next())
                    {
                        ExperienceStatistics eStatistics = new ExperienceStatistics();
                        eStatistics.bidNo = resultSet.getString(1);
                        eStatistics.accountName = resultSet.getString(2);
                        eStatistics.name = resultSet.getString(3);
                        eStatistics.experience = resultSet.getBigDecimal(4);
                        eStatistics.amount = resultSet.getBigDecimal(5);
                        eStatistics.time = resultSet.getDate(6);
                        lists.add(eStatistics);
                    }
                    return lists.toArray(new ExperienceStatistics[lists.size()]);
                }
            }, paging, sql.toString(), parameters);
        }
    }
    
    @Override
    public ExperienceStatistics searchDhAmount(ExperienceStatisticsQuery query)
        throws Throwable
    {
        StringBuilder sql = new StringBuilder("SELECT IFNULL(SUM(T6103.F03),0) AS F01,IFNULL(SUM(T6285.F07),0) AS F02");
        sql.append(" FROM S62.T6285 INNER JOIN S62.T6230 ON T6285.F02 = T6230.F01 INNER JOIN S61.T6110 ON T6110.F01 = T6285.F04 INNER JOIN S61.T6103 ON T6103.F01 = T6285.F12  WHERE T6285.F09 <> 'YFH' ");
        List<Object> parameters = new ArrayList<Object>();
        searchDhParams(query, sql, parameters);
        // sql语句和查询参数处理
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<ExperienceStatistics>()
            {
                @Override
                public ExperienceStatistics parse(ResultSet resultSet)
                    throws SQLException
                {
                    ExperienceStatistics count = new ExperienceStatistics();
                    if (resultSet.next())
                    {
                        count.experience = resultSet.getBigDecimal(1);
                        count.amount = resultSet.getBigDecimal(2);
                    }
                    return count;
                }
            }, sql.toString(), parameters);
        }
    }

    private void searchDhParams(ExperienceStatisticsQuery query, StringBuilder sql, List<Object> parameters)
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
            //还款时间
            if (query.repaymentStartTime() != null)
            {
                sql.append(" AND T6285.F08 >= ?");
                parameters.add(query.repaymentStartTime());
            }
            if (query.repaymentEndTime() != null)
            {
                sql.append(" AND T6285.F08 <= ?");
                parameters.add(query.repaymentEndTime());
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
     * 已还
     * @param query 条件
     * @param paging 分页
     * @return PagingResult
     * @throws Throwable
     */
	@Override
	public PagingResult<ExperienceStatistics> searchYh(
ExperienceStatisticsQuery query, Paging paging)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT T6230.F01 AS F01, T6230.F25 AS F02, T6110.F02 AS F03, T6103.F03 AS F04, T6285.F07 AS F05, T6285.F10 AS F06, T6110.F01 AS F07 FROM S62.T6285 INNER JOIN S62.T6230 ON T6285.F02 = T6230.F01 INNER JOIN S61.T6110 ON T6285.F04 = T6110.F01 INNER JOIN S61.T6103 ON T6103.F01 = T6285.F12 WHERE T6285.F09 = 'YFH'");
        List<Object> parameters = new ArrayList<>();
        searchYhParams(query, sql, parameters);
        sql.append(" GROUP BY T6285.F01 ORDER BY T6285.F10 DESC");
        final List<ExperienceStatistics> lists = new ArrayList<>();
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<ExperienceStatistics>()
            {
                @Override
                public ExperienceStatistics[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    while (resultSet.next())
                    {
                        ExperienceStatistics eStatistics = new ExperienceStatistics();
                        eStatistics.bidId = resultSet.getInt(1);
                        eStatistics.bidNo = resultSet.getString(2);
                        eStatistics.accountName = resultSet.getString(3);
                        eStatistics.experience = resultSet.getBigDecimal(4);
                        eStatistics.amount = resultSet.getBigDecimal(5);
                        eStatistics.fhTime = resultSet.getTimestamp(6);
                        eStatistics.name = getRealName(resultSet.getInt(7));
                        lists.add(eStatistics);
                    }
                    return lists.toArray(new ExperienceStatistics[lists.size()]);
                }
            }, paging, sql.toString(), parameters);
        }
    }
    
    @Override
    public ExperienceStatistics searchYhAmount(ExperienceStatisticsQuery query)
        throws Throwable
    {
        StringBuilder sql = new StringBuilder("SELECT IFNULL(SUM(T6103.F03),0) AS F01,IFNULL(SUM(T6285.F07),0) AS F02");
        sql.append(" FROM S62.T6285 INNER JOIN S62.T6230 ON T6285.F02 = T6230.F01 INNER JOIN S61.T6110 ON T6285.F04 = T6110.F01 INNER JOIN S61.T6103 ON T6103.F01 = T6285.F12 WHERE T6285.F09 = 'YFH' ");
        List<Object> parameters = new ArrayList<Object>();
        searchDhParams(query, sql, parameters);
        // sql语句和查询参数处理
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<ExperienceStatistics>()
            {
                @Override
                public ExperienceStatistics parse(ResultSet resultSet)
                    throws SQLException
                {
                    ExperienceStatistics count = new ExperienceStatistics();
                    if (resultSet.next())
                    {
                        count.experience = resultSet.getBigDecimal(1);
                        count.amount = resultSet.getBigDecimal(2);
                    }
                    return count;
                }
            }, sql.toString(), parameters);
        }
    }

    private void searchYhParams(ExperienceStatisticsQuery query, StringBuilder sql, List<Object> parameters)
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
            //还款时间
            if (query.repaymentStartTime() != null)
            {
                sql.append(" AND DATE(T6285.F10) >= ?");
                parameters.add(query.repaymentStartTime());
            }
            if (query.repaymentEndTime() != null)
            {
                sql.append(" AND DATE(T6285.F10) <= ?");
                parameters.add(query.repaymentEndTime());
            }
            //标编号
            if (!StringHelper.isEmpty(query.bid()))
            {
                sql.append(" AND T6230.F25 = ?");
                parameters.add(query.bid());
            }
        }
    }

    /**
     * 体验金统计
     * @return
     * @throws Throwable
     */
    @Override
    public ExperienceStaticTotal getExperienceTotal() throws Throwable {
        try(Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                         connection.prepareStatement("SELECT (SELECT SUM(F07) totalDh FROM S62.T6285 WHERE F09='WFH') totalDh,(SELECT SUM(F07) totalYh FROM S62.T6285 WHERE F09='YFH') totalYh from DUAL"))
            {
                try (ResultSet rs = ps.executeQuery()) {
                    ExperienceStaticTotal info = null;
                    if (rs.next()) {
                        info = new ExperienceStaticTotal();
                        //总派出体验金
                        info.totalDh = rs.getBigDecimal(1);
                        //已使用的体验金
                        info.totalYh = rs.getBigDecimal(2);
                    }
                    return info;
                }
            }
        }
    }

    /**
     * 导出待还
     * @param experienceStatistics 导出数据
     * @param outputStream 输出流
     * @throws Throwable
     */
    @Override
	public void exportexportDH(ExperienceStatistics[] experienceStatistics,
			OutputStream outputStream) throws Throwable {
        if (experienceStatistics == null) {
            return;
        }
        if (outputStream == null) {
            return;
        }
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                outputStream, "GBK"))) {
            CVSWriter writer = new CVSWriter(out);
            writer.write("序号");
            writer.write("标编号");
            writer.write("用户名");
            writer.write("姓名");
            writer.write("体验金(元)");
            writer.write("应还(元)");
            writer.write("还款日");
            writer.newLine();
            int i = 1;
            for (ExperienceStatistics experience : experienceStatistics) {
                writer.write(i++);
                writer.write(experience.bidNo);
                writer.write(experience.accountName);
                writer.write(experience.name);
                writer.write(experience.experience);
                writer.write(experience.amount);
                writer.write(experience.time);
                writer.newLine();
            }
        }
	}

    /**
     * 导出已还
     * @param experienceStatistics 导出数据
     * @param outputStream 输出流
     * @throws Throwable
     */
    @Override
    public void exportexportYH(ExperienceStatistics[] experienceStatistics,
                               OutputStream outputStream) throws Throwable {
        if (experienceStatistics == null) {
            return;
        }
        if (outputStream == null) {
            return;
        }
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                outputStream, "GBK"))) {
            CVSWriter writer = new CVSWriter(out);
            writer.write("序号");
            writer.write("标编号");
            writer.write("用户名");
            writer.write("姓名");
            writer.write("体验金(元)");
            writer.write("本期体验金利息(元)");
            writer.write("还款时间");
            writer.newLine();
            int i = 1;
            for (ExperienceStatistics experience : experienceStatistics) {
                writer.write(i++);
                writer.write(experience.bidNo);
                writer.write(experience.accountName);
                writer.write(experience.name);
                writer.write(experience.experience);
                writer.write(experience.amount);
                writer.write(experience.fhTime);
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
	private String getRealName(int userId) throws SQLException {
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT F02 FROM S61.T6141 WHERE F01=?")) {
				ps.setInt(1, userId);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						return rs.getString(1);
					}
				}
			}
		}
		return "";
	}
}
