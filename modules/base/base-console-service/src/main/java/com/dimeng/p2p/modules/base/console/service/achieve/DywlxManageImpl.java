package com.dimeng.p2p.modules.base.console.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S51.entities.T5131;
import com.dimeng.p2p.S51.enums.T5131_F02;
import com.dimeng.p2p.S62.entities.T6213;
import com.dimeng.p2p.S62.entities.T6214;
import com.dimeng.p2p.S62.enums.T6213_F03;
import com.dimeng.p2p.S62.enums.T6214_F04;
import com.dimeng.p2p.modules.base.console.service.DywlxManage;
import com.dimeng.util.StringHelper;

public class DywlxManageImpl extends AbstractInformationService implements DywlxManage
{
    
    public static class TermManageFactory implements ServiceFactory<DywlxManage>
    {
        
        @Override
        public DywlxManage newInstance(ServiceResource serviceResource)
        {
            return new DywlxManageImpl(serviceResource);
        }
        
    }
    
    public DywlxManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public void add(T6213 t6213)
        throws Throwable
    {
        if (t6213 == null)
        {
            throw new ParameterException("参数值不能为空！");
        }
        
        try (Connection connection = getConnection())
        {
            
            try (PreparedStatement pstmt =
                connection.prepareStatement("INSERT INTO S62.T6213 SET F02 = ?, F03 = ?",
                    PreparedStatement.RETURN_GENERATED_KEYS))
            {
                pstmt.setString(1, t6213.F02);
                pstmt.setString(2, T6213_F03.QY.name());
                pstmt.execute();
            }
        }
    }
    
    @Override
    public PagingResult<T6213> search(String f02, T6213_F03 f03, Paging paging)
        throws Throwable
    {
        StringBuilder sql = new StringBuilder("SELECT F01, F02, F03 FROM S62.T6213 WHERE 1=1 ");
        List<Object> parameters = new ArrayList<>();
        if (!StringHelper.isEmpty(f02))
        {
            sql.append(" AND T6213.F02 LIKE ? ");
            parameters.add(getSQLConnectionProvider().allMatch(f02));
        }
        if (f03 != null)
        {
            sql.append("AND T6213.F03 = ? ");
            parameters.add(f03);
        }
        sql.append(" ORDER BY T6213.F01 DESC ");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<T6213>()
            {
                
                @Override
                public T6213[] parse(ResultSet rs)
                    throws SQLException
                {
                    ArrayList<T6213> list = null;
                    while (rs.next())
                    {
                        T6213 record = new T6213();
                        record.F01 = rs.getInt(1);
                        record.F02 = rs.getString(2);
                        record.F03 = T6213_F03.parse(rs.getString(3));
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return list == null || list.size() == 0 ? null : list.toArray(new T6213[list.size()]);
                    
                }
            }, paging, sql.toString(), parameters);
        }
    }
    
    @Override
    public void update(T6213 t6213)
        throws Throwable
    {
        if (t6213 == null)
        {
            throw new ParameterException("参数值不能为空！");
        }
        
        try (Connection connection = getConnection())
        {
            
            try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S62.T6213 SET F02 = ? WHERE F01 = ?"))
            {
                pstmt.setString(1, t6213.F02);
                pstmt.setInt(2, t6213.F01);
                pstmt.execute();
            }
        }
        
    }
    
    @Override
    public T6213 get(int id)
        throws Throwable
    {
        if (id <= 0)
        {
            throw new ParameterException("参数值不能为空！");
        }
        
        T6213 record = null;
        
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03 FROM S62.T6213 WHERE T6213.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new T6213();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getString(2);
                        record.F03 = T6213_F03.parse(resultSet.getString(3));
                    }
                }
            }
        }
        return record;
        
    }
    
    @Override
    public void update(int id, T6213_F03 f03)
        throws Throwable
    {
        if (id <= 0 || f03 == null)
        {
            throw new ParameterException("参数值不能为空！");
        }
        try (Connection connection = getConnection())
        {
            
            try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S62.T6213 SET F03 = ? WHERE F01 = ?"))
            {
                pstmt.setString(1, f03.name());
                pstmt.setInt(2, id);
                pstmt.execute();
            }
        }
    }
    
    @Override
    public boolean isAllTy(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01 FROM S62.T6214 WHERE F02 = ? AND F04 = ? "))
            {
                pstmt.setInt(1, id);
                pstmt.setString(2, T6214_F04.QY.name());
                try (ResultSet rs = pstmt.executeQuery())
                {
                    if (rs.next())
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    @Override
    public T6213[] getDyws()
        throws Throwable
    {
        ArrayList<T6213> list = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02 FROM S62.T6213 WHERE T6213.F03 = ?"))
            {
                pstmt.setString(1, T6213_F03.QY.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        T6213 record = new T6213();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getString(2);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                }
            }
        }
        return ((list == null || list.size() == 0) ? null : list.toArray(new T6213[list.size()]));
    }
    
    @Override
    public T6214[] getDywsx(int typeId)
        throws SQLException
    {
        ArrayList<T6214> list = new ArrayList<>();
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT T6214.F01, T6214.F02, T6214.F03 FROM S62.T6214 WHERE T6214.F02 = ? AND T6214.F04 = ?"))
            {
                pstmt.setInt(1, typeId);
                pstmt.setString(2, T6214_F04.QY.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        T6214 record = new T6214();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getString(3);
                        list.add(record);
                    }
                }
            }
        }
        return list.toArray(new T6214[list.size()]);
    }
    
    @Override
    public String getNameById(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F02 FROM S62.T6213 WHERE T6213.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return resultSet.getString(1);
                    }
                }
            }
        }
        return "";
    }
    
    @Override
    public T5131 getPtdfType()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt = connection.prepareStatement("SELECT F01, F02 FROM S51.T5131 LIMIT 1"))
            {
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        T5131 record = new T5131();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = T5131_F02.parse(resultSet.getString(2));
                        return record;
                    }
                }
            }
        }
        return null;
    }
    
    @Override
    public void updateT5031(int id, T5131_F02 f02)
        throws Throwable
    {
        if (id <= 0 || f02 == null)
        {
            throw new ParameterException("参数值不能为空！");
        }
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                try (PreparedStatement pstmt =
                    connection.prepareStatement("UPDATE S51.T5131 SET F02 = ? WHERE F01 = ?"))
                {
                    pstmt.setString(1, f02.name());
                    pstmt.setInt(2, id);
                    pstmt.execute();
                }
                writeLog(connection, "操作日志", "不良资产处理方案设置修改");
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
