package com.dimeng.p2p.modules.base.console.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S71.entities.T7166;
import com.dimeng.p2p.S71.enums.T7166_F03;
import com.dimeng.p2p.S71.enums.T7166_F04;
import com.dimeng.p2p.S71.enums.T7166_F07;
import com.dimeng.p2p.modules.base.console.service.KhghManage;
import com.dimeng.p2p.modules.base.console.service.entity.KhghEntity;
import com.dimeng.util.StringHelper;

public class KhghManageImpl extends AbstractInformationService implements KhghManage {

	public KhghManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}
	
	public static class KhghManageFactory implements ServiceFactory<KhghManage> {
		@Override
		public KhghManage newInstance(ServiceResource serviceResource) {
			return new KhghManageImpl(serviceResource);
		}
	}
	
	private static final ArrayParser<KhghEntity> GLOBAL_ARRAY_PARSER = new ArrayParser<KhghEntity>() {
		@Override
		public KhghEntity[] parse(ResultSet rs) throws SQLException {
			java.util.List<KhghEntity> list = new ArrayList<>();
			KhghEntity record;
			while (rs.next()) {
				record = new KhghEntity();
				record.F01 = rs.getInt(1);
                record.F02 = rs.getInt(2);
                record.F03 = T7166_F03.parse(rs.getString(3));
                record.F04 = T7166_F04.parse(rs.getString(4));
                record.F05 = rs.getString(5);
                record.F06 = rs.getString(6);
                record.F07 = T7166_F07.parse(rs.getString(7));
                record.F08 = rs.getInt(8);
                record.F09 = rs.getDate(9);
                record.F10 = rs.getTimestamp(10);
                record.T7110_F02 = rs.getString(11);
                list.add(record);
			}
			return list.toArray(new KhghEntity[list.size()]);
		}
	};

	@Override
    public PagingResult<KhghEntity> search(String title, String beginTime, String endTime, Paging paging)
        throws Throwable
    {
        if (paging == null)
            return null;
        StringBuffer sql = new StringBuffer();
        List<Object> parameters = new ArrayList<>();
        sql.append(" SELECT A.F01, A.F02, A.F03, A.F04, A.F05, A.F06, A.F07, A.F08, A.F09, A.F10, B.F02 AS T7110_F02 FROM S71.T7166 AS A INNER JOIN S71.T7110 AS B ON (A.F08 = B.F01) where 1=1 ");
        if (!StringHelper.isEmpty(title))
        {
            sql.append(" AND A.F05 LIKE ?");
            parameters.add(getSQLConnectionProvider().allMatch(title));
        }
        if (!StringHelper.isEmpty(beginTime))
        {
            sql.append(" AND DATE(A.F09)>=?");
            parameters.add(beginTime);
        }
        if (!StringHelper.isEmpty(endTime))
        {
            sql.append(" AND DATE(A.F09)<=?");
            parameters.add(endTime);
        }
        
        sql.append(" ORDER BY A.F01 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, GLOBAL_ARRAY_PARSER, paging, sql.toString(), parameters);
        }
    }

	@Override
	public T7166 get(int id) throws Throwable {
		if(id <= 0) {
			throw new ParameterException("参数ID值非法");
		}
		T7166 record = null;
		try(Connection connection = getConnection())
		{
			try (PreparedStatement pstmt
						 = connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10 FROM S71.T7166 WHERE T7166.F01 = ? LIMIT 1")) {
				pstmt.setInt(1, id);
				try(ResultSet resultSet = pstmt.executeQuery()) {
					if(resultSet.next()) {
						record = new T7166();
						record.F01 = resultSet.getInt(1);
						record.F02 = resultSet.getInt(2);
						record.F03 = T7166_F03.parse(resultSet.getString(3));
						record.F04 = T7166_F04.parse(resultSet.getString(4));
						record.F05 = resultSet.getString(5);
						record.F06 = resultSet.getString(6);
						record.F07 = T7166_F07.parse(resultSet.getString(7));
						record.F08 = resultSet.getInt(8);
						record.F09 = resultSet.getDate(9);
						record.F10 = resultSet.getTimestamp(10);
					}
				}
			}
			return record;
		}
	}

	@Override
	public void update(T7166 t7166) throws Throwable {
		if(t7166 == null || t7166.F01 <= 0) {
			return;
		}
		try(Connection connection = getConnection())
		{
			try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S71.T7166 SET F03 = ?, F04 = ?, F05 = ?, F06 = ?, F09 = ? WHERE F01 = ?")) {
				pstmt.setString(1, t7166.F03.name());
				pstmt.setString(2, t7166.F04.name());
				pstmt.setString(3, t7166.F05);
				pstmt.setString(4, t7166.F06);
				if(t7166.F03 == T7166_F03.SR) {
					pstmt.setNull(5, Types.DATE);
				} else {
					pstmt.setDate(5, t7166.F09);
				}
				pstmt.setInt(6, t7166.F01);
				pstmt.execute();
			}
		}
	}

	@Override
	public void delete(int id) throws Throwable {
		if(id <= 0) {
			return;
		}
		try(Connection connection = getConnection())
		{
			try (PreparedStatement pstmt
						 = connection.prepareStatement("DELETE FROM S71.T7166 WHERE T7166.F01 = ?")) {
				pstmt.setInt(1, id);
				pstmt.execute();
			}
		}

	}

	@Override
	public void changeStatus(int id, T7166_F07 t7166_F07) throws Throwable {
		if(id <= 0) {
			return;
		}
		try(Connection connection = getConnection())
		{
			try (PreparedStatement pstmt
						 = connection.prepareStatement("UPDATE S71.T7166 SET F07 = ? WHERE F01 = ?")) {
				pstmt.setString(1, t7166_F07.name());
				pstmt.setInt(2, id);
				pstmt.execute();
			}
		}
	}

	@Override
    public int add(T7166 entity)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("INSERT INTO S71.T7166 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?, F10 = ?",
                    PreparedStatement.RETURN_GENERATED_KEYS))
            {
                pstmt.setInt(1, entity.F02);
                pstmt.setString(2, entity.F03.name());
                pstmt.setString(3, entity.F04.name());
                pstmt.setString(4, entity.F05);
                pstmt.setString(5, entity.F06);
                pstmt.setString(6, entity.F07.name());
                pstmt.setInt(7, entity.F08);
                if (entity.F03 == T7166_F03.SR)
                {
                    pstmt.setNull(8, Types.DATE);
                }
                else
                {
                    pstmt.setDate(8, entity.F09);
                }
                pstmt.setTimestamp(9, getCurrentTimestamp(connection));
                pstmt.execute();
                try (ResultSet resultSet = pstmt.getGeneratedKeys();)
                {
                    if (resultSet.next())
                    {
                        return resultSet.getInt(1);
                    }
                    return 0;
                }
            }
        }
    }

}
