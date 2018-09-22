package com.dimeng.p2p.modules.base.console.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S62.entities.T6211;
import com.dimeng.p2p.S62.enums.T6211_F03;
import com.dimeng.p2p.modules.base.console.service.BlxManage;
import com.dimeng.util.StringHelper;

public class BlxManageImpl extends AbstractInformationService implements
	BlxManage {

	
	public BlxManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	@Override
	public void add(T6211 t6211) throws Throwable {
		if(t6211 == null){
			throw new ParameterException("参数值不能为空！");
		}
		
		try (Connection connection = getConnection()){
			
			try (PreparedStatement pstmt = connection.prepareStatement("INSERT INTO S62.T6211 SET F02 = ?, F03 = ?",PreparedStatement.RETURN_GENERATED_KEYS)) {
		        pstmt.setString(1, t6211.F02);
		        pstmt.setString(2, T6211_F03.QY.name());
		        pstmt.execute();
		    }
		}

	}

	@Override
	public PagingResult<T6211> search(String f02, T6211_F03 f03, Paging paging)
        throws Throwable
    {
        StringBuilder sql = new StringBuilder("SELECT F01, F02, F03 FROM S62.T6211 WHERE 1=1 ");
        List<Object> parameters = new ArrayList<>();
        if (!StringHelper.isEmpty(f02))
        {
            sql.append(" AND T6211.f02 LIKE ? ");
            parameters.add(getSQLConnectionProvider().allMatch(f02));
        }
        if (f03 != null)
        {
            sql.append("AND T6211.F03 = ? ");
            parameters.add(f03);
        }
        sql.append(" ORDER BY T6211.F01 DESC ");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<T6211>()
            {
                
                @Override
                public T6211[] parse(ResultSet rs)
                    throws SQLException
                {
                    ArrayList<T6211> list = null;
                    while (rs.next())
                    {
                        T6211 record = new T6211();
                        record.F01 = rs.getInt(1);
                        record.F02 = rs.getString(2);
                        record.F03 = T6211_F03.parse(rs.getString(3));
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return list == null || list.size() == 0 ? null : list.toArray(new T6211[list.size()]);
                    
                }
            }, paging, sql.toString(), parameters);
        }
    }

	@Override
	public void update(T6211 t6211) throws Throwable {
		if(t6211 == null){
			throw new ParameterException("参数值不能为空！");
		}
		
		try (Connection connection = getConnection()){
			
			try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S62.T6211 SET F02 = ? WHERE F01 = ?")) {
		        pstmt.setString(1,  t6211.F02);
		        pstmt.setInt(2,  t6211.F01);
		        pstmt.execute();
		    }
		}
	}

	@Override
	public T6211 get(int id) throws Throwable {
		if (id <= 0) {
			throw new ParameterException("参数值不能为空！");
		}
		T6211 record = null;
		
		try (Connection connection = getConnection()){
			try (PreparedStatement pstmt = connection.prepareStatement("SELECT F01, F02, F03 FROM S62.T6211 WHERE T6211.F01 = ? LIMIT 1")) {
		        pstmt.setInt(1, id);
		        try(ResultSet resultSet = pstmt.executeQuery()) {
		            if(resultSet.next()) {
		                record = new T6211();
		                record.F01 = resultSet.getInt(1);
		                record.F02 = resultSet.getString(2);
		                record.F03 = T6211_F03.parse(resultSet.getString(3));
		            }
		        }
		    }
		}
	    return record;
	}

	@Override
	public void update(int id, T6211_F03 f03) throws Throwable {
		if(id <= 0 || f03 == null){
			throw new ParameterException("参数值不能为空！");
		}
		
		try (Connection connection = getConnection()){
			
			try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S62.T6211 SET F03 = ? WHERE F01 = ?")) {
		        pstmt.setString(1,  f03.name());
		        pstmt.setInt(2,  id);
		        pstmt.execute();
		    }
		}
	}
	
	@Override
	public boolean isExist(T6211 entity) throws Throwable {
		StringBuilder sql = new StringBuilder("SELECT F01 FROM S62.T6211 WHERE F02=? ");
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
