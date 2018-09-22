/**
 * 
 */
package com.dimeng.p2p.modules.base.console.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S51.entities.T5123;
import com.dimeng.p2p.S51.enums.T5123_F03;
import com.dimeng.p2p.S51.enums.T5123_F04;
import com.dimeng.p2p.S51.enums.T5123_F06;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F10;
import com.dimeng.p2p.modules.base.console.service.CreditCertificationManage;
import com.dimeng.p2p.modules.base.console.service.entity.AttestationQuery;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.EnumParser;

/**
 * @author guopeng
 * 
 */
public class CreditCerificationManageImpl extends AbstractInformationService implements CreditCertificationManage
{
    
    public CreditCerificationManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    public static class CreditCertificationManageFactory implements ServiceFactory<CreditCertificationManage>
    {
        
        @Override
        public CreditCertificationManage newInstance(ServiceResource serviceResource)
        {
            return new CreditCerificationManageImpl(serviceResource);
        }
    }
    
    protected static final String SELECT_ALL_SQL = "SELECT F01,F02,F03,F04,F05,F06 FROM S51.T5123";
    
    @Override
    public PagingResult<T5123> search(AttestationQuery query, Paging paging)
        throws Throwable
    {
        StringBuilder sql = new StringBuilder(SELECT_ALL_SQL);
        sql.append(" WHERE 1=1 ");
        List<Object> parameters = new ArrayList<>();
        if (query != null)
        {
            String name = query.getName();
            SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
            if (!StringHelper.isEmpty(name))
            {
                sql.append(" AND F02 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(name));
            }
            T5123_F03 type = query.getType();
            if (type != null)
            {
                sql.append(" AND F03 = ?");
                parameters.add(type);
            }
            T5123_F04 status = query.getStatus();
            if (status != null)
            {
                sql.append(" AND F04 = ?");
                parameters.add(status);
            }
            T5123_F06 userType = query.getUserType();
            if (userType != null)
            {
                sql.append(" AND F06 = ?");
                parameters.add(userType);
            }
            sql.append(" ORDER BY F01 DESC");
        }
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, ARRAY_PARSER, paging, sql.toString(), parameters);
        }
    }
    
    protected static final ArrayParser<T5123> ARRAY_PARSER = new ArrayParser<T5123>()
    {
        @Override
        public T5123[] parse(ResultSet rs)
            throws SQLException
        {
            ArrayList<T5123> list = null;
            while (rs.next())
            {
                T5123 info = new T5123();
                info.F01 = rs.getInt(1);
                info.F02 = rs.getString(2);
                info.F03 = EnumParser.parse(T5123_F03.class, rs.getString(3));
                info.F04 = EnumParser.parse(T5123_F04.class, rs.getString(4));
                info.F05 = rs.getInt(5);
                info.F06 = EnumParser.parse(T5123_F06.class, rs.getString(6));
                if (list == null)
                {
                    list = new ArrayList<>();
                }
                list.add(info);
            }
            return list == null ? null : list.toArray(new T5123[list.size()]);
        }
    };
    
    protected static final ItemParser<T5123> ITEM_PARSER = new ItemParser<T5123>()
    {
        @Override
        public T5123 parse(ResultSet rs)
            throws SQLException
        {
            T5123 info = null;
            if (rs.next())
            {
                info = new T5123();
                info.F01 = rs.getInt(1);
                info.F02 = rs.getString(2);
                info.F03 = EnumParser.parse(T5123_F03.class, rs.getString(3));
                info.F04 = EnumParser.parse(T5123_F04.class, rs.getString(4));
                info.F05 = rs.getInt(5);
                info.F06 = EnumParser.parse(T5123_F06.class, rs.getString(6));
            }
            return info;
        }
    };
    
    @Override
    public int add(T5123 query)
        throws Throwable
    {
        String name = query.F02;
        if (StringHelper.isEmpty(name))
        {
            throw new ParameterException("类型名称为空.");
        }
        T5123_F03 type = query.F03;
        if (type == null)
        {
            throw new ParameterException("必要认证类型为空.");
        }
        T5123_F06 userType = query.F06;
        if (userType == null)
        {
            throw new ParameterException("用户类型为空.");
        }
        String sql = "INSERT INTO S51.T5123 SET F02=?,F03=?,F04=?,F05=?,F06=?";
        int id = 0;
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                id = insert(connection, sql, name, type.name(), T5123_F04.QY.name(), query.F05, query.F06);
                try (PreparedStatement pstmt =
                    connection.prepareStatement("SELECT F01 FROM S61.T6110 WHERE F06 = ? AND F10 = ? "))
                {
                    pstmt.setString(1, userType == T5123_F06.GR ? T6110_F06.ZRR.name() : T6110_F06.FZRR.name());
                    pstmt.setString(2, T6110_F10.F.name());
                    try (ResultSet resultSet = pstmt.executeQuery())
                    {
                        while (resultSet.next())
                        {
                            execute(connection, "INSERT INTO S61.T6120 SET F01 = ?, F02 = ?", resultSet.getInt(1), id);
                        }
                    }
                }
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
        return id;
    }
    
    @Override
    public void update(T5123 query)
        throws Throwable
    {
        int id = query.F01;
        if (id < 0)
        {
            throw new ParameterException("指定的内容不存在.");
        }
        String name = query.F02;
        if (StringHelper.isEmpty(name))
        {
            throw new ParameterException("类型名称为空.");
        }
        T5123_F03 type = query.F03;
        if (type == null)
        {
            throw new ParameterException("必要认证类型为空.");
        }
        /*T5123_F06 userType = query.F06;
        if (userType == null)
        {
            throw new ParameterException("用户类型为空.");
        }*/
        
        String sql = "UPDATE S51.T5123 SET F02=?,F03=?,F05=? WHERE F01 = ?";
        try (Connection connection = getConnection())
        {
            execute(connection, sql, name, type.name(), query.F05, id);
        }
    }
    
    @Override
    public T5123 get(int id)
        throws Throwable
    {
        if (id < 0)
        {
            throw new ParameterException("指定的内容不存在.");
        }
        List<Object> parameters = new ArrayList<>();
        StringBuilder sql = new StringBuilder(SELECT_ALL_SQL);
        sql.append(" WHERE F01 = ? ");
        parameters.add(id);
        try (Connection connection = getConnection())
        {
            return select(connection, ITEM_PARSER, sql.toString(), parameters);
        }
    }
    
    @Override
    public void update(int id, T5123_F04 state)
        throws Throwable
    {
        if (id < 0)
        {
            throw new ParameterException("指定的内容不存在.");
        }
        if (state == null)
        {
            throw new ParameterException("认证状态为空.");
        }
        String sql = "UPDATE S51.T5123 SET F04=? WHERE F01 = ?";
        try (Connection connection = getConnection())
        {
            execute(connection, sql, state.name(), id);
        }
    }
    
    @Override
    public boolean isExist(T5123 entity)
        throws Throwable
    {
        StringBuilder sql = new StringBuilder("SELECT F01 FROM S51.T5123 WHERE F02=? ");
        if (entity.F01 != 0)
        {
            sql.append(" AND F01<> ");
            sql.append(entity.F01);
        }
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql.toString()))
            {
                ps.setString(1, entity.F02);
                try (ResultSet rs = ps.executeQuery())
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
    
}
