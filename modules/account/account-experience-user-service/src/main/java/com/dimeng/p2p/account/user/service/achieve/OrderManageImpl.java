package com.dimeng.p2p.account.user.service.achieve;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.OrderType;
import com.dimeng.p2p.S65.entities.T6501;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.S65.enums.T6501_F07;
import com.dimeng.p2p.account.user.service.OrderManage;

public class OrderManageImpl extends AbstractAccountService implements OrderManage
{
    
    public OrderManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public PagingResult<T6501> search(int type, Timestamp startTime, Timestamp endTime, Paging paging)
        throws Throwable
    {
        ArrayList<Object> list = new ArrayList<>();
        StringBuilder sql =
            new StringBuilder(
                "SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F13 FROM S65.T6501 WHERE T6501.F08 = ? ");
        list.add(serviceResource.getSession().getAccountId());
        if (type > 0)
        {
            sql.append(" AND F02 = ? ");
            list.add(type);
        }
        if (startTime != null)
        {
            sql.append(" AND DATE(F04) >= ? ");
            list.add(startTime);
        }
        if (endTime != null)
        {
            sql.append(" AND DATE(F04) <= ? ");
            list.add(endTime);
        }
        sql.append(" ORDER BY F04 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<T6501>()
            {
                @Override
                public T6501[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<T6501> list = null;
                    while (resultSet.next())
                    {
                        T6501 record = new T6501();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = T6501_F03.parse(resultSet.getString(3));
                        record.F04 = resultSet.getTimestamp(4);
                        record.F05 = resultSet.getTimestamp(5);
                        record.F06 = resultSet.getTimestamp(6);
                        record.F07 = T6501_F07.parse(resultSet.getString(7));
                        record.F08 = resultSet.getInt(8);
                        record.F09 = resultSet.getInt(9);
                        record.F13 = resultSet.getBigDecimal(10);
                        record.status = record.F03.getChineseName();
                        record.orderTypeName = OrderType.getTypeName(record.F02);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return ((list == null || list.size() == 0) ? null : list.toArray(new T6501[list.size()]));
                }
            }, paging, sql.toString(), list);
        }
    }
    
}
