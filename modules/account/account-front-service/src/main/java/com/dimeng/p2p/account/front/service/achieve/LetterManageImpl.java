package com.dimeng.p2p.account.front.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.entities.T6123;
import com.dimeng.p2p.S61.enums.T6123_F05;
import com.dimeng.p2p.account.front.service.LetterManage;

public class LetterManageImpl extends AbstractAccountService implements
		LetterManage {

	public LetterManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	public static class LetterManageFactory implements
			ServiceFactory<LetterManage> {
		@Override
		public LetterManage newInstance(ServiceResource serviceResource) {
			return new LetterManageImpl(serviceResource);
		}
	}

	@Override
	public int getUnReadCount() throws Throwable {
		try (Connection connection = getConnection()) {
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT COUNT(F01) FROM S61.T6123 WHERE T6123.F02 = ? AND T6123.F05 = ? LIMIT 1")) {
				pstmt.setInt(1, serviceResource.getSession().getAccountId());
				pstmt.setString(2, T6123_F05.WD.name());
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						return resultSet.getInt(1);
					}
				}
			}
		}
		return 0;
	}

	@Override
	public PagingResult<T6123> seach(T6123_F05 letterType, Paging paging)
        throws Throwable
    {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT F01, F02, F03, F04 FROM S61.T6123 WHERE T6123.F02 = ?  ");
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(serviceResource.getSession().getAccountId());
        if (letterType == null)
        {
            sql.append(" AND (T6123.F05 = ? OR T6123.F05 = ?) ");
            parameters.add(T6123_F05.WD.name());
            parameters.add(T6123_F05.YD.name());
        }
        else
        {
            sql.append(" AND T6123.F05 = ?  ");
            parameters.add(letterType.name());
        }
        sql.append(" ORDER BY T6123.F01 DESC ");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<T6123>()
            {
                
                @Override
                public T6123[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<T6123> list = null;
                    while (resultSet.next())
                    {
                        T6123 record = new T6123();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getString(3);
                        record.F04 = resultSet.getTimestamp(4);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return ((list == null || list.size() == 0) ? null : list.toArray(new T6123[list.size()]));
                }
            }, paging, sql.toString(), parameters);
        }
    }

	@Override
	public String get(int letterID) throws Throwable {
		try (Connection connection = getConnection()) {
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F02 FROM S61.T6124 WHERE T6124.F01 = ? LIMIT 1")) {
				pstmt.setInt(1, letterID);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						return resultSet.getString(1);
					}
				}
			}
		}
		return null;
	}

    /*@Override
    protected void writeLog(String arg0, String arg1) throws Throwable {
    	// TODO Auto-generated method stub
    }*/

	@Override
    public void del(int... letterID)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                for (int id : letterID)
                {
                    execute(connection, "UPDATE S61.T6123 SET F05 = ? WHERE F01 = ? ", T6123_F05.SC.name(), id);
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

}
