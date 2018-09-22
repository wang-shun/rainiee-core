package com.dimeng.p2p.modules.base.console.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S51.entities.T5127;
import com.dimeng.p2p.S51.enums.T5127_F02;
import com.dimeng.p2p.S51.enums.T5127_F03;
import com.dimeng.p2p.S51.enums.T5127_F06;
import com.dimeng.p2p.modules.base.console.service.RankManage;

/**
 * @author guopeng
 * 
 */
public class RankManageImpl extends AbstractInformationService implements
		RankManage {

	public RankManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	@Override
	public PagingResult<T5127> seach(T5127 entity, Paging paging)
        throws Throwable
    {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT F01, F02, F03, F04, F05, F06 FROM S51.T5127 WHERE 1 = 1 ");
        ArrayList<Object> parameters = new ArrayList<>();
        T5127_F02 type = entity.F02;
        if (type != null)
        {
            sql.append(" AND F02 = ? ");
            parameters.add(type.name());
        }
        T5127_F06 status = entity.F06;
        if (status != null)
        {
            sql.append(" AND F06 = ? ");
            parameters.add(status.name());
        }
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<T5127>()
            {
                
                @Override
                public T5127[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<T5127> list = null;
                    while (resultSet.next())
                    {
                        T5127 record = new T5127();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = T5127_F02.parse(resultSet.getString(2));
                        record.F03 = T5127_F03.parse(resultSet.getString(3));
                        record.F04 = resultSet.getBigDecimal(4);
                        record.F05 = resultSet.getBigDecimal(5);
                        record.F06 = T5127_F06.parse(resultSet.getString(6));
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return ((list == null || list.size() == 0) ? null : list.toArray(new T5127[list.size()]));
                }
            }, paging, sql.toString(), parameters);
        }
    }

	@Override
    public void add(T5127 entity)
        throws Throwable
    {
        int id = getID(entity);
        try (Connection connection = getConnection())
        {
            if (id > 0)
            {
                try (PreparedStatement pstmt =
                    connection.prepareStatement("UPDATE S51.T5127 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ? WHERE F01 = ?"))
                {
                    pstmt.setString(1, entity.F02.name());
                    pstmt.setString(2, entity.F03.name());
                    pstmt.setBigDecimal(3, entity.F04);
                    pstmt.setBigDecimal(4, entity.F05);
                    pstmt.setString(5, T5127_F06.QY.name());
                    pstmt.setInt(6, id);
                    pstmt.execute();
                }
            }
            else
            {
                try (PreparedStatement pstmt =
                    connection.prepareStatement("INSERT INTO S51.T5127 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?"))
                {
                    pstmt.setString(1, entity.F02.name());
                    pstmt.setString(2, entity.F03.name());
                    pstmt.setBigDecimal(3, entity.F04);
                    pstmt.setBigDecimal(4, entity.F05);
                    pstmt.setString(5, T5127_F06.QY.name());
                    pstmt.execute();
                }
            }
        }
    }

	@Override
	public void update(T5127 entity) throws Throwable {
		try(Connection connection = getConnection()){
			try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S51.T5127 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ? WHERE F01 = ?")) {
		        pstmt.setString(1,  entity.F02.name());
		        pstmt.setString(2,  entity.F03.name());
		        pstmt.setBigDecimal(3,  entity.F04);
		        pstmt.setBigDecimal(4,  entity.F05);
		        pstmt.setString(5,  entity.F06.name());
		        pstmt.setInt(6,  entity.F01);
		        pstmt.execute();
		    }
		}
	}
	
	//根据等级类型和等级得到id
	private int getID(T5127 entity) throws SQLException{
		try(Connection connection = getConnection()){
			try (PreparedStatement pstmt = connection.prepareStatement("SELECT F01 FROM S51.T5127 WHERE T5127.F02 = ? AND T5127.F03 = ? LIMIT 1")) {
		        pstmt.setString(1, entity.F02.name());
		        pstmt.setString(2, entity.F03.name());
		        try(ResultSet resultSet = pstmt.executeQuery()) {
		            if(resultSet.next()) {
		                return resultSet.getInt(1);
		            }
		        }
		    }
		}
		return 0;
	}

	@Override
	public T5127 get(int id) throws Throwable {
		try(Connection connection = getConnection()){
			T5127 record =  new T5127();
		    try (PreparedStatement pstmt = connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06 FROM S51.T5127 WHERE T5127.F01 = ? LIMIT 1")) {
		        pstmt.setInt(1, id);
		        try(ResultSet resultSet = pstmt.executeQuery()) {
		            if(resultSet.next()) {
		            	record.F01 = resultSet.getInt(1);
		                record.F02 = T5127_F02.parse(resultSet.getString(2));
		                record.F03 = T5127_F03.parse(resultSet.getString(3));
		                record.F04 = resultSet.getBigDecimal(4);
		                record.F05 = resultSet.getBigDecimal(5);
		                record.F06 = T5127_F06.parse(resultSet.getString(6));
		            }
		        }
		    }
		    return record;
		}
	}

	@Override
    public void updateQy(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            execute(connection, "UPDATE S51.T5127 SET F06 = ? WHERE F01 = ?", T5127_F06.QY.name(), id);
        }
        
    }

	@Override
    public void updateTy(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            execute(connection, "UPDATE S51.T5127 SET F06 = ? WHERE F01 = ?", T5127_F06.TY.name(), id);
        }
    }

}
