/*
 * 文 件 名:  BadClaimDzxyManageImpl.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  huqinfu
 * 修改时间:  2016年6月20日
 */
package com.dimeng.p2p.modules.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S62.entities.T6264;
import com.dimeng.p2p.S62.enums.T6264_F04;
import com.dimeng.p2p.modules.service.AbstractBadClaimService;
import com.dimeng.p2p.repeater.claim.BadClaimDzxyManage;
import com.dimeng.p2p.repeater.claim.entity.Blzqzrxy;
import com.dimeng.p2p.repeater.claim.query.BlzqzrxyQuery;
import com.dimeng.util.StringHelper;

/**
 * 不良债权转让电子协议实现类
 * 
 * @author  huqinfu
 * @version  [版本号, 2016年6月20日]
 */
public class BadClaimDzxyManageImpl extends AbstractBadClaimService implements BadClaimDzxyManage
{
    
    /** <默认构造函数>
     */
    public BadClaimDzxyManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public PagingResult<Blzqzrxy> search(BlzqzrxyQuery query, Paging paging)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT T6265.F01 AS F01, T6265.F02 AS F02, T6265.F03 AS F03, T6265.F04 AS F04, T6265.F05 AS F05, T6265.F06 AS F06, T6265.F07 AS F07, T6110.F02 AS F08, T6230.F03 AS F09 ");
        sql.append(" FROM S62.T6265 INNER JOIN S61.T6110 ON T6110.F01 = T6265.F03");
        sql.append(" LEFT JOIN S62.T6264 ON T6264.F01 = T6265.F02 LEFT JOIN S62.T6230 ON T6230.F01 = T6264.F03 WHERE 1=1");
        ArrayList<Object> parameters = new ArrayList<>();
        if (query != null)
        {
            String account = query.getName();
            if (!StringHelper.isEmpty(account))
            {
                sql.append(" AND T6110.F02 LIKE ?");
                parameters.add(getSQLConnectionProvider().allMatch(account));
            }
            Timestamp datetime = query.getCreateTimeStart();
            if (datetime != null)
            {
                sql.append(" AND DATE(T6265.F07) >=?");
                parameters.add(datetime);
            }
            Timestamp datetimeEnd = query.getCreateTimeEnd();
            if (datetimeEnd != null)
            {
                sql.append(" AND DATE(T6265.F07) <=?");
                parameters.add(datetimeEnd);
            }
        }
        sql.append(" ORDER BY T6265.F07 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<Blzqzrxy>()
            {
                @Override
                public Blzqzrxy[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<Blzqzrxy> list = null;
                    while (resultSet.next())
                    {
                        Blzqzrxy record = new Blzqzrxy();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getInt(3);
                        record.F04 = resultSet.getInt(4);
                        record.F05 = resultSet.getBigDecimal(5);
                        record.F06 = resultSet.getBigDecimal(6);
                        record.F07 = resultSet.getTimestamp(7);
                        record.zrName = resultSet.getString(8);
                        record.loanTitle = resultSet.getString(9);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    
                    return ((list == null || list.size() == 0) ? null : list.toArray(new Blzqzrxy[list.size()]));
                }
            }, paging, sql.toString(), parameters);
        }
    }
    
    @Override
    public T6264 selectT6264(int F01)
        throws Throwable
    {
        T6264 record = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10 FROM S62.T6264 WHERE T6264.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, F01);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new T6264();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getString(2);
                        record.F03 = resultSet.getInt(3);
                        record.F04 = T6264_F04.parse(resultSet.getString(4));
                        record.F05 = resultSet.getInt(5);
                        record.F06 = resultSet.getInt(6);
                        record.F07 = resultSet.getTimestamp(7);
                        record.F08 = resultSet.getTimestamp(8);
                        record.F09 = resultSet.getBigDecimal(9);
                        record.F10 = resultSet.getBigDecimal(10);
                    }
                }
            }
        }
        return record;
    }
    
}
