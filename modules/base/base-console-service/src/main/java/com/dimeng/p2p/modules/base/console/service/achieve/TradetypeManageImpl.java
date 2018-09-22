package com.dimeng.p2p.modules.base.console.service.achieve;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.S51.entities.T5122;
import com.dimeng.p2p.S51.enums.T5122_F03;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F10;
import com.dimeng.p2p.common.enums.YesOrNo;
import com.dimeng.p2p.modules.base.console.service.TradeTypeManage;
import com.dimeng.p2p.modules.base.console.service.entity.TradetypeQuery;
import com.dimeng.p2p.variables.defines.GuarantorVariavle;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BooleanParser;
import com.dimeng.util.parser.EnumParser;

public class TradetypeManageImpl extends AbstractInformationService implements TradeTypeManage
{
    
    public TradetypeManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    public static class TradetypeManageFactory implements ServiceFactory<TradeTypeManage>
    {
        
        @Override
        public TradeTypeManage newInstance(ServiceResource serviceResource)
        {
            return new TradetypeManageImpl(serviceResource);
        }
    }
    
    protected static final String SELECT_ALL_SQL = "SELECT F01,F02,F03 FROM S51.T5122";
    
    @Override
    public PagingResult<T5122> search(TradetypeQuery query, Paging paging)
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
            T5122_F03 status = query.getStatus();
            if (status != null)
            {
                sql.append(" AND F03 = ?");
                parameters.add(status);
            }
        }
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, ARRAY_PARSER, paging, sql.toString(), parameters);
        }
    }
    
    protected static final ArrayParser<T5122> ARRAY_PARSER = new ArrayParser<T5122>()
    {
        @Override
        public T5122[] parse(ResultSet rs)
            throws SQLException
        {
            ArrayList<T5122> list = null;
            while (rs.next())
            {
                T5122 info = new T5122();
                info.F01 = rs.getInt(1);
                info.F02 = rs.getString(2);
                info.F03 = EnumParser.parse(T5122_F03.class, rs.getString(3));
                if (list == null)
                {
                    list = new ArrayList<>();
                }
                list.add(info);
            }
            return list == null ? null : list.toArray(new T5122[list.size()]);
        }
    };
    
    protected static final ItemParser<T5122> ITEM_PARSER = new ItemParser<T5122>()
    {
        @Override
        public T5122 parse(ResultSet rs)
            throws SQLException
        {
            T5122 info = null;
            if (rs.next())
            {
                info = new T5122();
                info.F01 = rs.getInt(1);
                info.F02 = rs.getString(2);
                info.F03 = EnumParser.parse(T5122_F03.class, rs.getString(3));
            }
            return info;
        }
    };
    
    @Override
    public int add(T5122 query)
        throws Throwable
    {
        String name = query.F02;
        if (StringHelper.isEmpty(name))
        {
            throw new ParameterException("类型名称为空.");
        }
        String sql = "INSERT INTO S51.T5122 SET F01 = ?,F02=?,F03=?";
        try (Connection connection = getConnection())
        {
            return insert(connection, sql, query.F01, name, T5122_F03.QY);
        }
    }
    
    @Override
    public void update(T5122 query)
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
        String sql = "UPDATE S51.T5122 SET F02=? WHERE F01 = ?";
        try (Connection connection = getConnection())
        {
            execute(connection, sql, name, id);
        }
    }
    
    @Override
    public T5122 get(int id)
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
    public void update(int id, T5122_F03 status)
        throws Throwable
    {
        if (id < 0)
        {
            throw new ParameterException("指定的内容不存在.");
        }
        try (Connection connection = getConnection())
        {
            execute(connection, "UPDATE S51.T5122 SET F03=? WHERE F01=?", status, id);
        }
    }
    
    @Override
    public T5122[] search(T6110_F06 userType, T6110_F10 t6110_F10, Object ojbect)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            ArrayList<T5122> list = null;
            List<Object> param = new ArrayList<Object>();
            StringBuffer sqlBuff = new StringBuffer("SELECT F01, F02 FROM S51.T5122 WHERE T5122.F03 = ? ");
            param.add(T5122_F03.QY.name());
            ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
            boolean isHasGuarant =
                BooleanParser.parse(configureProvider.getProperty(GuarantorVariavle.IS_HAS_GUARANTOR));
            if (isHasGuarant)
            {
                if (userType == T6110_F06.FZRR && t6110_F10 != null)
                {
                    sqlBuff =
                        new StringBuffer(
                            "SELECT F01, F02 FROM S51.T5122 WHERE T5122.F03 = ? AND ( T5122.F05 = ? OR T5122.F06 = ? )");
                    param.add(YesOrNo.yes.name());
                    param.add(YesOrNo.yes.name());
                    if(T6110_F10.S == t6110_F10)
                    {
                        sqlBuff.append(" AND T5122.F01 NOT IN (?,?,?)");
                        param.add(FeeCode.CJFWF);
                        param.add(FeeCode.JK);
                        param.add(FeeCode.TZ_WYJ_SXF);
                    }
                }
                else if (userType == T6110_F06.ZRR)
                {
                    sqlBuff =
                        new StringBuffer(
                            "SELECT F01, F02 FROM S51.T5122 WHERE T5122.F03 = ? AND ( T5122.F04 = ? OR T5122.F06 = ? )");
                    param.add(YesOrNo.yes.name());
                    param.add(YesOrNo.yes.name());
                }
                else
                {
                    //平台
                    sqlBuff.append("AND T5122.F07 = ? ");
                    param.add(YesOrNo.yes.name());
                }
            }
            else
            {
                if (userType == T6110_F06.ZRR)
                {
                    //个人
                    sqlBuff.append("AND T5122.F04 = ? ");
                }
                if (userType == T6110_F06.FZRR && t6110_F10 == T6110_F10.F)
                {
                    //企业
                    sqlBuff.append("AND T5122.F05 = ? ");
                }
                if (userType == T6110_F06.FZRR && t6110_F10 == T6110_F10.S)
                {
                    //机构
                    sqlBuff.append("AND T5122.F06 = ? ");
                }
                if (userType == T6110_F06.FZRR && t6110_F10 == null)
                {
                    //平台
                    sqlBuff.append("AND T5122.F07 = ? ");
                }
                if (userType == null && t6110_F10 == null)
                {
                    //信用类型
                    sqlBuff.append("AND T5122.F08 = ? ");
                }
                param.add(YesOrNo.yes.name());
            }
            return select(connection, new ItemParser<T5122[]>()
            {
                @Override
                public T5122[] parse(ResultSet rs)
                    throws SQLException
                {
                    ArrayList<T5122> list = new ArrayList<>();
                    while (rs.next())
                    {
                        T5122 record = new T5122();
                        record.F01 = rs.getInt(1);
                        record.F02 = rs.getString(2);
                        list.add(record);
                    }
                    return list.toArray(new T5122[list.size()]);
                }
            }, sqlBuff.toString(), param);
        }
    }
}
