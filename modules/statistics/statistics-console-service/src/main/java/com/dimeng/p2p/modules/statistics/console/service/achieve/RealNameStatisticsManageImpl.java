/*
 * 文 件 名:  RealNameStatisticsManageImpl.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  God
 * 修改时间:  2016年4月5日
 */
package com.dimeng.p2p.modules.statistics.console.service.achieve;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.enums.T6198_F04;
import com.dimeng.p2p.modules.statistics.console.service.RealNameStatisticsManage;
import com.dimeng.p2p.modules.statistics.console.service.entity.RealNameStatisticsEntity;
import com.dimeng.p2p.modules.statistics.console.service.query.RealNameStatisticsQuery;
import com.dimeng.util.StringHelper;
import com.dimeng.util.io.CVSWriter;
import com.dimeng.util.parser.TimestampParser;

/**
 * <<实名认证统计>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2016年4月5日]
 */
public class RealNameStatisticsManageImpl extends AbstractStatisticsService implements RealNameStatisticsManage
{
    
    public RealNameStatisticsManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public PagingResult<RealNameStatisticsEntity> queryRealNameStatisticsList(RealNameStatisticsQuery query,
        Paging paging)
        throws Throwable
    {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT T6198.F01 F01,T6110.F02 F02,T6141.F02 F03,T6198.F03 F04,T6198.F04 F05,T6198.F05 F06,T6198.F06 F07,T6198.F07 F08 FROM S61.T6198 JOIN S61.T6110 ON T6198.F02=T6110.F01 JOIN S61.T6141 ON T6141.F01=T6110.F01 WHERE 1=1 ");
        ArrayList<Object> parameters = new ArrayList<>();
        SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
        String queryResult = query.getUserName();
        if (!StringHelper.isEmpty(queryResult))
        {
            sql.append(" AND T6110.F02 LIKE ?");
            parameters.add(sqlConnectionProvider.allMatch(queryResult));
        }
        queryResult = query.getRealName();
        if (!StringHelper.isEmpty(queryResult))
        {
            sql.append(" AND T6141.F02 LIKE ?");
            parameters.add(sqlConnectionProvider.allMatch(queryResult));
        }
        queryResult = query.getAuthSource();
        if (!StringHelper.isEmpty(queryResult))
        {
            sql.append(" AND T6198.F04 = ?");
            parameters.add(queryResult);
        }
        Timestamp time = query.getAuthPassTimeStart();
        if (time != null)
        {
            sql.append(" AND DATE(T6198.F06) >= ?");
            parameters.add(time);
        }
        time = query.getAuthPassTimeEnd();
        if (time != null)
        {
            sql.append(" AND DATE(T6198.F06) <= ?");
            parameters.add(time);
        }
        sql.append(" ORDER BY T6198.F06 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<RealNameStatisticsEntity>()
            {
                @Override
                public RealNameStatisticsEntity[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<RealNameStatisticsEntity> list = null;
                    while (resultSet.next())
                    {
                        RealNameStatisticsEntity entity = new RealNameStatisticsEntity();
                        entity.F01 = resultSet.getInt(1);
                        entity.userName = resultSet.getString(2);
                        entity.realName = resultSet.getString(3);
                        entity.F03 = resultSet.getInt(4);
                        entity.F04 = T6198_F04.parse(resultSet.getString(5));
                        entity.F05 = resultSet.getInt(6);
                        entity.F06 = resultSet.getTimestamp(7);
                        entity.F07 = resultSet.getTimestamp(8);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(entity);
                    }
                    return ((list == null || list.size() == 0) ? null
                        : list.toArray(new RealNameStatisticsEntity[list.size()]));
                }
            },
                paging,
                sql.toString(),
                parameters);
        }
    }
    
    @Override
    public void exportRealNameStatistics(RealNameStatisticsEntity[] entitys, OutputStream outputStream, String charset)
        throws Throwable
    {
        if (outputStream == null)
        {
            return;
        }
        if (entitys == null || entitys.length <= 0)
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
            writer.write("用户名");
            writer.write("真实姓名");
            writer.write("错误认证次数");
            writer.write("认证来源");
            writer.write("认证通过时间");
            writer.write("累计登录次数");
            writer.write("最后登录时间");
            writer.newLine();
            int i = 0;
            int total = 0;
            int totalLoginTimes = 0;
            for (RealNameStatisticsEntity entity : entitys)
            {
                total += entity.F03;
                totalLoginTimes += entity.F05;
                i++;
                writer.write(i);
                writer.write(entity.userName);
                writer.write(entity.realName);
                writer.write(entity.F03);
                writer.write(entity.F04.getChineseName());
                writer.write(TimestampParser.format(entity.F06) + "\t");
                writer.write(entity.F05);
                writer.write(TimestampParser.format(entity.F07) + "\t");
                writer.newLine();
            }
            
            writer.write("合计");
            writer.write("");
            writer.write("");
            writer.write(total);
            writer.write("");
            writer.write("");
            writer.write(totalLoginTimes);
            writer.write("");
            writer.newLine();
        }
    }
    
}
