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
import com.dimeng.p2p.S62.entities.T6212;
import com.dimeng.p2p.S62.enums.T6212_F04;
import com.dimeng.p2p.modules.base.console.service.BfjlxManage;
import com.dimeng.util.StringHelper;

public class BfjlxManageImpl extends AbstractInformationService implements
	BfjlxManage {

	public static class TermManageFactory implements
			ServiceFactory<BfjlxManage> {

		@Override
		public BfjlxManage newInstance(ServiceResource serviceResource) {
			return new BfjlxManageImpl(serviceResource);
		}

	}

	public BfjlxManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	@Override
	public void add(T6212 t6212) throws Throwable {
		if(t6212 == null){
			throw new ParameterException("参数值不能为空！");
		}
		
		try (Connection connection = getConnection()){
			
			try (PreparedStatement pstmt = connection.prepareStatement("INSERT INTO S62.T6212 SET F02 = ?, F03 = ?, F04 = ?",PreparedStatement.RETURN_GENERATED_KEYS)) {
		        pstmt.setString(1, t6212.F02);
		        pstmt.setInt(2, t6212.F03);
		        pstmt.setString(3, T6212_F04.QY.name());
		        pstmt.execute();
		    }
		}

	}

	@Override
	public PagingResult<T6212> search(String f02, T6212_F04 f04, Paging paging)
        throws Throwable
    {
        StringBuilder sql = new StringBuilder("SELECT F01, F02, F03, F04 FROM S62.T6212 WHERE 1=1 ");
        List<Object> parameters = new ArrayList<>();
        if (!StringHelper.isEmpty(f02))
        {
            sql.append(" AND T6212.f02 LIKE ? ");
            parameters.add(getSQLConnectionProvider().allMatch(f02));
        }
        if (f04 != null)
        {
            sql.append("AND T6212.F04 = ? ");
            parameters.add(f04);
        }
        sql.append(" ORDER BY T6212.F01 DESC ");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<T6212>()
            {
                
                @Override
                public T6212[] parse(ResultSet rs)
                    throws SQLException
                {
                    ArrayList<T6212> list = null;
                    while (rs.next())
                    {
                        T6212 record = new T6212();
                        record.F01 = rs.getInt(1);
                        record.F02 = rs.getString(2);
                        record.F03 = rs.getInt(3);
                        record.F04 = T6212_F04.parse(rs.getString(4));
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return list == null || list.size() == 0 ? null : list.toArray(new T6212[list.size()]);
                    
                }
            }, paging, sql.toString(), parameters);
        }
    }

	@Override
	public void update(T6212 t6212) throws Throwable {
		if(t6212 == null){
			throw new ParameterException("参数值不能为空！");
		}
		
		try (Connection connection = getConnection()){
			
			try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S62.T6212 SET F02 = ?, F03 = ? WHERE F01 = ?")) {
		        pstmt.setString(1,  t6212.F02);
		        pstmt.setInt(2,  t6212.F03);
		        pstmt.setInt(3,  t6212.F01);
		        pstmt.execute();
		    }
		}
	}

	@Override
	public T6212 get(int id) throws Throwable {
		if (id <= 0) {
			throw new ParameterException("参数值不能为空！");
		}
		T6212 record = null;
		
		try (Connection connection = getConnection()){
			try (PreparedStatement pstmt = connection.prepareStatement("SELECT F01, F02, F03, F04 FROM S62.T6212 WHERE T6212.F01 = ? LIMIT 1")) {
		        pstmt.setInt(1, id);
		        try(ResultSet resultSet = pstmt.executeQuery()) {
		            if(resultSet.next()) {
		                record = new T6212();
		                record.F01 = resultSet.getInt(1);
		                record.F02 = resultSet.getString(2);
		                record.F03 = resultSet.getInt(3);
		                record.F04 = T6212_F04.parse(resultSet.getString(4));
		            }
		        }
		    }
		}
		return record;
	}

	@Override
	public void update(int id, T6212_F04 f04) throws Throwable {
		if(id <= 0 || f04 == null){
			throw new ParameterException("参数值不能为空！");
		}
		
		try (Connection connection = getConnection()){
			
			try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S62.T6212 SET F04 = ? WHERE F01 = ?")) {
		        pstmt.setString(1,  f04.name());
		        pstmt.setInt(2,  id);
		        pstmt.execute();
		    }
		}
	}

}
