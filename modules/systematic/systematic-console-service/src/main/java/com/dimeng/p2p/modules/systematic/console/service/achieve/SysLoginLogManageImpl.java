package com.dimeng.p2p.modules.systematic.console.service.achieve;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.common.enums.ConsoleLogType;
import com.dimeng.p2p.common.enums.LoginStatus;
import com.dimeng.p2p.modules.systematic.console.service.SysLoginLogManage;
import com.dimeng.p2p.modules.systematic.console.service.entity.OperLog;
import com.dimeng.p2p.modules.systematic.console.service.entity.SysLog;
import com.dimeng.p2p.modules.systematic.console.service.query.OperLogQuery;
import com.dimeng.p2p.modules.systematic.console.service.query.SysLogQuery;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.EnumParser;

public class SysLoginLogManageImpl extends AbstractSystemService implements
		SysLoginLogManage {

	public static class SysLoginLogManageFactory implements
			ServiceFactory<SysLoginLogManage> {

		@Override
		public SysLoginLogManage newInstance(ServiceResource serviceResource) {
			return new SysLoginLogManageImpl(serviceResource);
		}
	}

	public SysLoginLogManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	@Override
	public PagingResult<SysLog> seacrch(SysLogQuery query, Paging paging)
        throws Throwable
    {
        String sql = "SELECT F01,F02,F03,F04,F05,F06 FROM S71.T7120 WHERE 1=1";
        StringBuilder sb = new StringBuilder();
        sb.append(sql);
        List<Object> parameters = new ArrayList<>();
        if (query != null)
        {
            String accountName = query.getAccountName();
            Timestamp createTimeStart = query.getCreateTimeStart();
            Timestamp createTimeEnd = query.getCreateTimeEnd();
            if (!StringHelper.isEmpty(accountName))
            {
                sb.append(" AND F02 LIKE ?");
                parameters.add(getSQLConnectionProvider().allMatch(accountName));
            }
            if (createTimeStart != null)
            {
                sb.append(" AND DATE(F03)>=?");
                parameters.add(createTimeStart);
            }
            if (createTimeEnd != null)
            {
                sb.append(" AND DATE(F03)<=?");
                parameters.add(createTimeEnd);
            }
        }
        sb.append(" ORDER BY F03 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<SysLog>()
            {
                
                @Override
                public SysLog[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    List<SysLog> lists = new ArrayList<>();
                    while (resultSet.next())
                    {
                        SysLog sysLog = new SysLog();
                        sysLog.id = resultSet.getInt(1);
                        sysLog.accountName = resultSet.getString(2);
                        sysLog.lastTime = resultSet.getTimestamp(3);
                        sysLog.lastIp = resultSet.getString(4);
                        sysLog.isSuccess = EnumParser.parse(LoginStatus.class, resultSet.getString(5));
                        lists.add(sysLog);
                    }
                    return lists.toArray(new SysLog[lists.size()]);
                }
            }, paging, sb.toString(), parameters);
        }
    }

	@Override
	public PagingResult<OperLog> search(OperLogQuery query, Paging paging)
        throws Throwable
    {
        StringBuilder sb =
            new StringBuilder(
                "SELECT T7120.F01 AS F01, T7120.F02 AS F02, T7120.F03 AS F03, T7120.F04 AS F04, T7120.F05 AS F05, T7120.F06 AS F06, T7110.F02 AS F07 FROM S71.T7120 INNER JOIN S71.T7110 ON T7120.F02 = T7110.F01");
        List<Object> parameters = new ArrayList<>();
        if (query != null)
        {
            String name = query.getName();
            ConsoleLogType type = query.getType();
            Timestamp createTimeStart = query.getCreateTimeStart();
            Timestamp createTimeEnd = query.getCreateTimeEnd();
            if (type != null)
            {
                sb.append(" AND T7120.F04=? ");
                parameters.add(type.getName());
            }
            if (!StringHelper.isEmpty(name))
            {
                sb.append(" AND T7110.F02 LIKE ?");
                parameters.add(getSQLConnectionProvider().allMatch(name));
            }
            if (createTimeStart != null)
            {
                sb.append(" AND DATE(T7120.F03)>=?");
                parameters.add(createTimeStart);
            }
            if (createTimeEnd != null)
            {
                sb.append(" AND DATE(T7120.F03)<=?");
                parameters.add(createTimeEnd);
            }
        }
        sb.append(" ORDER BY T7120.F03 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<OperLog>()
            {
                
                @Override
                public OperLog[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    List<OperLog> lists = new ArrayList<>();
                    while (resultSet.next())
                    {
                        OperLog record = new OperLog();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getTimestamp(3);
                        record.F04 = resultSet.getString(4);
                        record.F05 = resultSet.getString(5);
                        record.F06 = resultSet.getString(6);
                        record.name = resultSet.getString(7);
                        lists.add(record);
                    }
                    return lists.toArray(new OperLog[lists.size()]);
                }
            }, paging, sb.toString(), parameters);
        }
    }
}
