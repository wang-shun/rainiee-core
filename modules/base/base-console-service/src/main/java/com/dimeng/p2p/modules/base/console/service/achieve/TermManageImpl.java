package com.dimeng.p2p.modules.base.console.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S50.enums.T5017_F05;
import com.dimeng.p2p.common.enums.TermType;
import com.dimeng.p2p.modules.base.console.service.TermManage;
import com.dimeng.p2p.modules.base.console.service.entity.TermRecord;
import com.dimeng.p2p.modules.base.console.service.query.TermQuery;
import com.dimeng.util.StringHelper;

public class TermManageImpl extends AbstractInformationService implements TermManage
{
    
    public static class TermManageFactory implements ServiceFactory<TermManage>
    {
        
        @Override
        public TermManage newInstance(ServiceResource serviceResource)
        {
            return new TermManageImpl(serviceResource);
        }
        
    }
    
    public TermManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    protected static final ArrayParser<TermRecord> ARRAY_PARSER = new ArrayParser<TermRecord>()
    {
        
        @Override
        public TermRecord[] parse(ResultSet resultSet)
            throws SQLException
        {
            ArrayList<TermRecord> list = null;
            while (resultSet.next())
            {
                TermRecord record = new TermRecord();
                record.type = TermType.valueOf(resultSet.getString(1));
                record.content = resultSet.getString(2);
                record.publisherId = resultSet.getInt(3);
                record.updateTime = resultSet.getTimestamp(4);
                record.status = T5017_F05.parse(resultSet.getString(5));
                record.updaterName = resultSet.getString(6);
                if (list == null)
                {
                    list = new ArrayList<>();
                }
                list.add(record);
            }
            return list == null || list.size() == 0 ? null : list.toArray(new TermRecord[0]);
        }
    };
    
    protected static final ItemParser<TermRecord> ITEM_PARSER = new ItemParser<TermRecord>()
    {
        
        @Override
        public TermRecord parse(ResultSet resultSet)
            throws SQLException
        {
            TermRecord record = null;
            if (resultSet.next())
            {
                record = new TermRecord();
                record.type = TermType.valueOf(resultSet.getString(1));
                record.content = resultSet.getString(2);
                record.publisherId = resultSet.getInt(3);
                record.updateTime = resultSet.getTimestamp(4);
                record.status = T5017_F05.parse(resultSet.getString(5));
                record.updaterName = resultSet.getString(6);
            }
            return record;
        }
    };
    
    protected static final String SELECT_ALL_SQL =
        "SELECT T5017.F01,T5017.F02,T5017.F03,T5017.F04,T5017.F05,T7110.F04 AS F06 FROM S50.T5017 LEFT JOIN S71.T7110 ON T5017.F03 = T7110.F01 ";
    
    @Override
    public PagingResult<TermRecord> search(TermQuery query, Paging paging)
        throws Throwable
    {
        ArrayList<Object> parameters = new ArrayList<Object>();
        StringBuilder sql = new StringBuilder(SELECT_ALL_SQL);
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, ARRAY_PARSER, paging, sql.toString(), parameters);
        }
    }
    
    @Override
    public TermRecord get(TermType type)
        throws Throwable
    {
        if (type == null)
        {
            return null;
        }
        try (Connection connection = getConnection())
        {
            return select(connection, ITEM_PARSER, SELECT_ALL_SQL + " WHERE T5017.F01 = ?", type);
        }
    }
    
    @Override
    public void update(TermType type, String content)
        throws Throwable
    {
        if (StringHelper.isEmpty(content))
        {
            throw new ParameterException("协议内容不能为空");
        }
        try (Connection connection = getConnection())
        {
            execute(connection,
                "UPDATE S50.T5017 SET F02=?, F03=?, F04=? WHERE F01  =?",
                content,
                serviceResource.getSession().getAccountId(),
                    new Timestamp(System.currentTimeMillis()),
                type);
        }
    }

    @Override
    public void updateStatus(TermType type, String status)
            throws Throwable
    {
        if (StringHelper.isEmpty(status))
        {
            throw new ParameterException("状态不能为空");
        }
        try (Connection connection = getConnection())
        {
            execute(connection,
                    "UPDATE S50.T5017 SET F05=?, F03= ?, F04=? WHERE F01  =?",
                    status,
                    serviceResource.getSession().getAccountId(),
                    new Timestamp(System.currentTimeMillis()),
                    type);
        }
    }
    
    @Override
    public void add(TermType type, String content)
        throws Throwable
    {
        if (type == null)
        {
            throw new ParameterException("协议类型不能为空");
        }
        if (StringHelper.isEmpty(content))
        {
            throw new ParameterException("协议内容不能为空");
        }
        try (Connection connection = getConnection())
        {
            execute(connection,
                "INSERT INTO T5017 (F01,F02,F03,F04,F05,F06) VALUES (?, 0, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE F02 = VALUES(F02),F03 = VALUES(F03), F04 = VALUES(F04), F05 = VALUES(F05),F06 = VALUES(F06)",
                type,
                content,
                serviceResource.getSession().getAccountId(),
                getCurrentTimestamp(connection),
                getCurrentTimestamp(connection));
        }
    }
    
    @Override
    public void delete(String... ids)
        throws Throwable
    {
        if (ids == null || ids.length == 0)
        {
            return;
        }
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                // 协议附件
                try (PreparedStatement pstmt = connection.prepareStatement("DELETE FROM S50.T5017_1 WHERE F01 = ?"))
                {
                    for (String id : ids)
                    {
                        if (StringHelper.isEmpty(id))
                        {
                            continue;
                        }
                        pstmt.setString(1, id);
                        pstmt.addBatch();
                    }
                    pstmt.executeBatch();
                }
                // 删除协议
                try (PreparedStatement pstmt = connection.prepareStatement("DELETE FROM S50.T5017 WHERE F01 = ?"))
                {
                    for (String id : ids)
                    {
                        if (StringHelper.isEmpty(id))
                        {
                            continue;
                        }
                        pstmt.setString(1, id);
                        pstmt.addBatch();
                    }
                    pstmt.executeBatch();
                }
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
}
