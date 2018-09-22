package com.dimeng.p2p.modules.base.console.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.http.upload.FileStore;
import com.dimeng.framework.http.upload.UploadFile;
import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S50.enums.T5012_F03;
import com.dimeng.p2p.S50.enums.T5012_F11;
import com.dimeng.p2p.modules.base.console.service.CustomerServiceManage;
import com.dimeng.p2p.modules.base.console.service.entity.CustomerService;
import com.dimeng.p2p.modules.base.console.service.entity.CustomerServiceRecord;
import com.dimeng.p2p.modules.base.console.service.query.CustomerServiceQuery;
import com.dimeng.p2p.variables.FileType;
import com.dimeng.util.StringHelper;

public class CustomerServiceManageImpl extends AbstractInformationService implements CustomerServiceManage
{
    
    public static class CustomerServiceManageFactory implements ServiceFactory<CustomerServiceManage>
    {
        
        @Override
        public CustomerServiceManage newInstance(ServiceResource serviceResource)
        {
            return new CustomerServiceManageImpl(serviceResource);
        }
        
    }
    
    public CustomerServiceManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    protected static final ArrayParser<CustomerServiceRecord> ARRAY_PARSER = new ArrayParser<CustomerServiceRecord>()
    {
        
        @Override
        public CustomerServiceRecord[] parse(ResultSet resultSet)
            throws SQLException
        {
            ArrayList<CustomerServiceRecord> list = null;
            while (resultSet.next())
            {
                CustomerServiceRecord record = new CustomerServiceRecord();
                record.id = resultSet.getInt(1);
                record.viewTimes = resultSet.getInt(2);
                record.type = T5012_F03.valueOf(resultSet.getString(3));
                record.sortIndex = resultSet.getInt(4);
                record.name = resultSet.getString(5);
                record.number = resultSet.getString(6);
                record.imageCode = resultSet.getString(7);
                record.publisherId = resultSet.getInt(8);
                record.createTime = resultSet.getTimestamp(9);
                record.updateTime = resultSet.getTimestamp(10);
                record.publisherName = resultSet.getString(11);
                record.status = T5012_F11.parse(resultSet.getString(12));
                if (list == null)
                {
                    list = new ArrayList<>();
                }
                list.add(record);
            }
            return list == null || list.size() == 0 ? null : list.toArray(new CustomerServiceRecord[0]);
        }
    };
    
    protected static final ItemParser<CustomerServiceRecord> ITEM_PARSER = new ItemParser<CustomerServiceRecord>()
    {
        
        @Override
        public CustomerServiceRecord parse(ResultSet resultSet)
            throws SQLException
        {
            CustomerServiceRecord record = null;
            if (resultSet.next())
            {
                record = new CustomerServiceRecord();
                record.id = resultSet.getInt(1);
                record.viewTimes = resultSet.getInt(2);
                record.type = T5012_F03.parse(resultSet.getString(3));
                record.sortIndex = resultSet.getInt(4);
                record.name = resultSet.getString(5);
                record.number = resultSet.getString(6);
                record.imageCode = resultSet.getString(7);
                record.publisherId = resultSet.getInt(8);
                record.createTime = resultSet.getTimestamp(9);
                record.updateTime = resultSet.getTimestamp(10);
                record.publisherName = resultSet.getString(11);
            }
            return record;
        }
    };
    
    protected static final String SELECT_ALL_SQL =
        "SELECT T5012.F01,T5012.F02,T5012.F03,T5012.F04,T5012.F05,T5012.F06,T5012.F07,T5012.F08,T5012.F09,T5012.F10 AS PX,T7110.F04 AS F11,T5012.F11 AS F12 FROM T5012 INNER JOIN S71.T7110 ON T5012.F08 = T7110.F01 ";
    
    @Override
    public PagingResult<CustomerServiceRecord> search(CustomerServiceQuery query, Paging paging)
        throws Throwable
    {
        ArrayList<Object> parameters = new ArrayList<Object>();
        StringBuilder sql = new StringBuilder(SELECT_ALL_SQL);
        sql.append(" WHERE 1=1 ");
        if (query != null)
        {
            SQLConnectionProvider connectionProvider = getSQLConnectionProvider();
            String string = query.getName();
            if (!StringHelper.isEmpty(string))
            {
                sql.append(" AND T5012.F05 LIKE ?");
                parameters.add(connectionProvider.allMatch(string));
            }
            string = query.getPublisherName();
            if (!StringHelper.isEmpty(string))
            {
                sql.append(" AND T7110.F04 LIKE ?");
                parameters.add(connectionProvider.allMatch(string));
            }
            Timestamp timestamp = query.getCreateTimeStart();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T5012.F09) >= ?");
                parameters.add(timestamp);
            }
            timestamp = query.getCreateTimeEnd();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T5012.F09) <= ?");
                parameters.add(timestamp);
            }
            timestamp = query.getUpdateTimeStart();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T5012.F10) >= ?");
                parameters.add(timestamp);
            }
            timestamp = query.getUpdateTimeEnd();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T5012.F10) <= ?");
                parameters.add(timestamp);
            }
        }
        sql.append(" ORDER BY T5012.F04 DESC,T5012.F10 DESC ");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, ARRAY_PARSER, paging, sql.toString(), parameters);
        }
    }
    
    @Override
    public CustomerServiceRecord get(int id)
        throws Throwable
    {
        if (id <= 0)
        {
            return null;
        }
        try (Connection connection = getConnection())
        {
            return select(connection, ITEM_PARSER, SELECT_ALL_SQL + " WHERE T5012.F01 = ?", id);
        }
    }
    
    @Override
    public void delete(int... ids)
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
                try (PreparedStatement pstmt = connection.prepareStatement("DELETE FROM T5012 WHERE F01 = ?"))
                {
                    for (int id : ids)
                    {
                        if (id <= 0)
                        {
                            continue;
                        }
                        pstmt.setInt(1, id);
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
    
    @Override
    public int add(CustomerService customerService)
        throws Throwable
    {
        if (customerService == null)
        {
            throw new ParameterException("没有指定客服信息 ");
        }
        String name;
        String number;
        T5012_F03 type;
        UploadFile image = customerService.getImage();
        if (image == null)
        {
            throw new ParameterException("必须上传客服图片");
        }
        {
            name = customerService.getName();
            if (StringHelper.isEmpty(name))
            {
                throw new ParameterException("客服名称不能为空 ");
            }
            type = customerService.getType();
            if (type == null)
            {
                throw new ParameterException("客服类型不能为空 ");
            }
            String numberMsg = "";
            if (T5012_F03.QQ.name().equals(type))
            {
                numberMsg = "客服号不能为空 ";
            }
            else
            {
                numberMsg = "在线代码不能为空 ";
            }
            number = customerService.getNumber();
            if (StringHelper.isEmpty(number))
            {
                throw new ParameterException(numberMsg);
            }
            
        }
        FileStore fileStore = serviceResource.getResource(FileStore.class);
        String imageCode = fileStore.upload(FileType.CUSTOMER_SERVICE_IMAGE.ordinal(), image)[0];
        try (Connection connection = getConnection())
        {
            return insert(connection,
                "INSERT INTO T5012 SET F03 = ?,F04 = ?,F05 = ?,F06 = ?,F07 = ?,F08 = ?,F09 = ?",
                type,
                customerService.getSortIndex(),
                name,
                number,
                imageCode,
                serviceResource.getSession().getAccountId(),
                getCurrentTimestamp(connection));
        }
    }
    
    @Override
    public void update(int id, CustomerService customerService)
        throws Throwable
    {
        if (id <= 0 || customerService == null)
        {
            return;
        }
        String name = null;
        String number = null;
        T5012_F03 type = null;
        String imageCode = null;
        
        {
            name = customerService.getName();
            if (StringHelper.isEmpty(name))
            {
                throw new ParameterException("客服名称不能为空 ");
            }
            type = customerService.getType();
            if (type == null)
            {
                throw new ParameterException("客服类型不能为空 ");
            }
            else if (type == T5012_F03.QQ)
            {
                number = customerService.getNumber();
                if (StringHelper.isEmpty(number))
                {
                    throw new ParameterException("客服号不能为空 ");
                }
            }
            else if (type == T5012_F03.QQ_QY)
            {
                number = customerService.getQy();
                if (StringHelper.isEmpty(number))
                {
                    throw new ParameterException("在线代码不能为空 ");
                }
            }
            else
            {
                throw new ParameterException("客服类型不存在");
            }
            
        }
        UploadFile image = customerService.getImage();
        if (image != null)
        {
            FileStore fileStore = serviceResource.getResource(FileStore.class);
            imageCode = fileStore.upload(FileType.CUSTOMER_SERVICE_IMAGE.ordinal(), image)[0];
        }
        try (Connection connection = getConnection())
        {
            if (imageCode == null)
            {
                execute(connection,
                    "UPDATE T5012 SET F03 = ?,F04 = ?,F05 = ?,F06 = ? WHERE F01 = ? ",
                    type,
                    customerService.getSortIndex(),
                    name,
                    number,
                    id);
            }
            else
            {
                execute(connection,
                    "UPDATE T5012 SET F03 = ?,F04 = ?,F05 = ?,F06 = ?,F07 = ? WHERE F01 = ? ",
                    type,
                    customerService.getSortIndex(),
                    name,
                    number,
                    imageCode,
                    id);
            }
        }
    }
    
    @Override
    public void updateBatchOrder(String ids, int order)
        throws Throwable
    {
        StringBuilder sql = new StringBuilder("UPDATE S50.T5012 SET F04  = ? WHERE F01 IN (");
        List<Object> params = new ArrayList<Object>();
        params.add(order);
        sql.append(getBatchId(ids, params));
        sql.append(")");
        try (Connection connection = getConnection())
        {
            execute(connection, sql.toString(), params.toArray());
        }
    }
    
    @Override
    public void updateStatus(String id, T5012_F11 t5012)
        throws Throwable
    {
        StringBuilder sql = new StringBuilder("UPDATE S50.T5012 SET F11  = ? WHERE F01 = ?");
        try (Connection connection = getConnection())
        {
            execute(connection, sql.toString(), t5012.name(), id);
        }
    }
    
    @Override
    public Integer getQyCount(T5012_F11 t5012)
        throws Throwable
    {
        StringBuilder sql = new StringBuilder("SELECT COUNT(1) FROM S50.T5012 WHERE F11  = ?");
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<Integer>()
            {
                @Override
                public Integer parse(ResultSet resultSet)
                    throws SQLException
                {
                    if (resultSet.next())
                    {
                        return resultSet.getInt(1);
                    }
                    return 0;
                }
            }, sql.toString(), t5012.name());
        }
    }
    
}
