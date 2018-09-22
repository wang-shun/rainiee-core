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
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S51.entities.T5124;
import com.dimeng.p2p.S51.enums.T5124_F05;
import com.dimeng.p2p.modules.base.console.service.CreditLevelManage;
import com.dimeng.p2p.modules.base.console.service.query.CreditLevelQuery;
import com.dimeng.util.StringHelper;

public class CreditLevelManageImpl extends AbstractInformationService implements
		CreditLevelManage {

	public static class TermManageFactory implements
			ServiceFactory<CreditLevelManage> {

		@Override
		public CreditLevelManage newInstance(ServiceResource serviceResource) {
			return new CreditLevelManageImpl(serviceResource);
		}

	}

	public CreditLevelManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	@Override
    public void add(T5124 creditLeve)
        throws Throwable
    {
        if (creditLeve == null)
        {
            throw new ParameterException("参数值不能为空！");
        }
        try (Connection connection = getConnection("S51"))
        {
            insert(connection,
                "INSERT INTO T5124 SET F02 = ?, F03 = ?, F04 = ?",
                creditLeve.F02,
                creditLeve.F03,
                creditLeve.F04);
        }
    }

	@Override
	public PagingResult<T5124> search(CreditLevelQuery creditLeve, Paging paging)
        throws Throwable
    {
        StringBuilder sql = new StringBuilder("SELECT F01, F02, F03, F04, F05 FROM T5124 WHERE 1=1 ");
        List<Object> parameters = new ArrayList<>();
        if (creditLeve != null)
        {
            String name = creditLeve.getName();
            if (!StringHelper.isEmpty(name))
            {
                sql.append(" AND T5124.F02 LIKE ? ");
                parameters.add(getSQLConnectionProvider().allMatch(name));
            }
            T5124_F05 status = creditLeve.getStatus();
            if (status != null)
            {
                sql.append("AND T5124.F05 = ? ");
                parameters.add(status);
            }
        }
        sql.append(" ORDER BY T5124.F01 DESC ");
        try (Connection connection = getConnection("S51"))
        {
            return selectPaging(connection, new ArrayParser<T5124>()
            {
                
                @Override
                public T5124[] parse(ResultSet rs)
                    throws SQLException
                {
                    ArrayList<T5124> list = null;
                    while (rs.next())
                    {
                        T5124 record = new T5124();
                        record.F01 = rs.getInt(1);
                        record.F02 = rs.getString(2);
                        record.F03 = rs.getInt(3);
                        record.F04 = rs.getInt(4);
                        record.F05 = T5124_F05.parse(rs.getString(5));
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return list == null || list.size() == 0 ? null : list.toArray(new T5124[list.size()]);
                    
                }
            }, paging, sql.toString(), parameters);
        }
    }

	@Override
    public void update(T5124 creditLeve)
        throws Throwable
    {
        if (creditLeve == null)
        {
            throw new ParameterException("参数值不能为空！");
        }
        try (Connection connection = getConnection())
        {
            execute(connection,
                "UPDATE S51.T5124 SET F02 = ?,F03 = ?,F04 = ? WHERE F01 = ? ",
                creditLeve.F02,
                creditLeve.F03,
                creditLeve.F04,
                creditLeve.F01);
        }
    }

	@Override
    public T5124 get(int id)
        throws Throwable
    {
        if (id <= 0)
        {
            throw new ParameterException("参数值不能为空！");
        }
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<T5124>()
            {
                
                @Override
                public T5124 parse(ResultSet rs)
                    throws SQLException
                {
                    T5124 record = null;
                    if (rs.next())
                    {
                        record = new T5124();
                        record.F01 = rs.getInt(1);
                        record.F02 = rs.getString(2);
                        record.F03 = rs.getInt(3);
                        record.F04 = rs.getInt(4);
                        record.F05 = T5124_F05.valueOf(rs.getString(5));
                    }
                    return record;
                }
                
            }, "SELECT F01,F02,F03,F04,F05 FROM S51.T5124 WHERE F01 = ? ", id);
        }
    }

	@Override
    public void update(int id, T5124_F05 state)
        throws Throwable
    {
        if (id <= 0 || state == null)
        {
            throw new ParameterException("参数值不能为空！");
        }
        try (Connection connection = getConnection("S51"))
        {
            execute(connection, "UPDATE T5124 SET F05 = ? WHERE F01 = ? ", state, id);
        }
    }

	@Override
	public boolean isExist(T5124 entity) throws Throwable {
		StringBuilder sql = new StringBuilder("SELECT F01 FROM S51.T5124 WHERE F02=? ");
		if(entity.F01 != 0){
			sql.append(" AND F01<> ");
			sql.append(entity.F01);
		}
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection
					.prepareStatement(sql.toString())) {
				ps.setString(1, entity.F02);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
