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
import com.dimeng.p2p.S62.entities.T6214;
import com.dimeng.p2p.S62.enums.T6214_F04;
import com.dimeng.p2p.modules.base.console.service.DywsxManage;
import com.dimeng.util.StringHelper;

public class DywsxManageImpl extends AbstractInformationService implements
		DywsxManage {

	public static class TermManageFactory implements
			ServiceFactory<DywsxManage> {

		@Override
		public DywsxManage newInstance(ServiceResource serviceResource) {
			return new DywsxManageImpl(serviceResource);
		}

	}

	public DywsxManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	@Override
	public void add(T6214 t6214) throws Throwable {
		if (t6214 == null) {
			throw new ParameterException("参数值不能为空！");
		}

		try (Connection connection = getConnection()) {

			try (PreparedStatement pstmt = connection.prepareStatement(
					"INSERT INTO S62.T6214 SET F02 = ?, F03 = ?",
					PreparedStatement.RETURN_GENERATED_KEYS)) {
				pstmt.setInt(1, t6214.F02);
				pstmt.setString(2, t6214.F03);
				pstmt.execute();
			}
		}
	}

	@Override
	public PagingResult<T6214> search(int f02, String f03, Paging paging)
        throws Throwable
    {
        StringBuilder sql = new StringBuilder("SELECT F01, F02, F03,F04 FROM S62.T6214 WHERE 1=1 ");
        List<Object> parameters = new ArrayList<>();
        if (f02 > 0)
        {
            sql.append(" AND T6214.f02 = ? ");
            parameters.add(f02);
        }
        if (!StringHelper.isEmpty(f03))
        {
            sql.append("AND T6214.F03 LIKE ? ");
            parameters.add(getSQLConnectionProvider().allMatch(f03));
        }
        sql.append(" ORDER BY T6214.F01 DESC ");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<T6214>()
            {
                
                @Override
                public T6214[] parse(ResultSet rs)
                    throws SQLException
                {
                    ArrayList<T6214> list = null;
                    while (rs.next())
                    {
                        T6214 record = new T6214();
                        record.F01 = rs.getInt(1);
                        record.F02 = rs.getInt(2);
                        record.F03 = rs.getString(3);
                        record.F04 = T6214_F04.parse(rs.getString(4));
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return list == null || list.size() == 0 ? null : list.toArray(new T6214[list.size()]);
                    
                }
            }, paging, sql.toString(), parameters);
        }
    }

	@Override
	public void update(T6214 t6214) throws Throwable {
		if (t6214 == null) {
			throw new ParameterException("参数值不能为空！");
		}

		try (Connection connection = getConnection()) {
			try (PreparedStatement pstmt = connection
					.prepareStatement("UPDATE S62.T6214 SET F02 = ?, F03 = ? WHERE F01 = ?")) {
				pstmt.setInt(1, t6214.F02);
				pstmt.setString(2, t6214.F03);
				pstmt.setInt(3, t6214.F01);
				pstmt.execute();
			}
		}
	}

	@Override
	public T6214 get(int id) throws Throwable {
		if (id <= 0) {
			throw new ParameterException("参数值不能为空！");
		}
		T6214 record = null;

		try (Connection connection = getConnection()) {
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F01, F02, F03 FROM S62.T6214 WHERE T6214.F01 = ? LIMIT 1")) {
				pstmt.setInt(1, id);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						record = new T6214();
						record.F01 = resultSet.getInt(1);
						record.F02 = resultSet.getInt(2);
						record.F03 = resultSet.getString(3);
					}
				}
			}
		}
		return record;
	}

	@Override
    public void qyDywsx(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            execute(connection, "UPDATE S62.T6214 SET F04 = ? WHERE F01 = ? ", T6214_F04.QY.name(), id);
        }
        
    }

	@Override
    public void tyDywsx(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            execute(connection, "UPDATE S62.T6214 SET F04 = ? WHERE F01 = ?", T6214_F04.TY.name(), id);
        }
    }

}
