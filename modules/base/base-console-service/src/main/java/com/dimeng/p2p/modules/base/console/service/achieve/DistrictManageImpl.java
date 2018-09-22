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

import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S50.entities.T5019;
import com.dimeng.p2p.S50.enums.T5019_F11;
import com.dimeng.p2p.S50.enums.T5019_F13;
import com.dimeng.p2p.modules.base.console.service.DistrictManage;
import com.dimeng.p2p.modules.base.console.service.query.RegionQuery;
import com.dimeng.p2p.variables.P2PConst;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.EnumParser;

/**
 * @author guopeng
 * 
 */
public class DistrictManageImpl extends AbstractInformationService implements
		DistrictManage {

	public DistrictManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	public static class RegionManageFactory implements
			ServiceFactory<DistrictManage> {

		@Override
		public DistrictManage newInstance(ServiceResource serviceResource) {
			return new DistrictManageImpl(serviceResource);
		}

	}

	@Override
	public PagingResult<T5019> search(Paging paging, RegionQuery query)
        throws Throwable
    {
        String sql = "SELECT F01,F02,F03,F04,F05,F06,F07,F08,F09,F10,F11,F12,F13 FROM T5019 WHERE 1=1";
        StringBuilder sb = new StringBuilder();
        sb.append(sql);
        List<Object> parameters = new ArrayList<>();
        if (query != null)
        {
            String name = query.getName();
            if (!StringHelper.isEmpty(name))
            {
                sb.append(" AND F05 LIKE ?");
                parameters.add(getSQLConnectionProvider().allMatch(name));
            }
            T5019_F11 type = query.getType();
            if (type != null)
            {
                sb.append(" AND F11=?");
                parameters.add(type);
            }
            T5019_F13 status = query.getStatus();
            if (status != null)
            {
                sb.append(" AND F13=?");
                parameters.add(status);
            }
        }
        try (Connection connection = getConnection(P2PConst.DB_INFORMATION))
        {
            return selectPaging(connection, new ArrayParser<T5019>()
            {
                
                @Override
                public T5019[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    List<T5019> lists = new ArrayList<>();
                    while (resultSet.next())
                    {
                        T5019 t5019 = new T5019();
                        t5019.F01 = resultSet.getInt(1);
                        t5019.F02 = resultSet.getShort(2);
                        t5019.F03 = resultSet.getShort(3);
                        t5019.F04 = resultSet.getShort(4);
                        t5019.F05 = resultSet.getString(5);
                        t5019.F06 = resultSet.getString(6);
                        t5019.F07 = resultSet.getString(7);
                        t5019.F08 = resultSet.getString(8);
                        t5019.F09 = resultSet.getString(9);
                        t5019.F10 = resultSet.getString(10);
                        t5019.F11 = EnumParser.parse(T5019_F11.class, resultSet.getString(11));
                        t5019.F12 = resultSet.getString(12);
                        t5019.F13 = EnumParser.parse(T5019_F13.class, resultSet.getString(13));
                        lists.add(t5019);
                    }
                    return lists.toArray(new T5019[lists.size()]);
                }
            }, paging, sb.toString(), parameters);
        }
    }

	@Override
	public T5019[] getShi(int shengId) throws Throwable {
		if (shengId <= 0) {
			throw new ParameterException("参数错误");
		}
		String sql = "SELECT F01,F05 FROM T5019 WHERE F02=? AND F04=0 AND F11=? AND F13=?";
		List<T5019> lists = new ArrayList<>();
		try (Connection connection = getConnection(P2PConst.DB_INFORMATION)) {
			try (PreparedStatement ps = connection.prepareStatement(sql)) {
				ps.setInt(1, shengId / 10000);
				ps.setString(2, T5019_F11.SHI.name());
				ps.setString(3, T5019_F13.QY.name());
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						T5019 t5019 = new T5019();
						t5019.F01 = rs.getInt(1);
						t5019.F05 = rs.getString(2);
						lists.add(t5019);
					}
				}
			}
		}
		return lists.toArray(new T5019[lists.size()]);
	}

	@Override
	public T5019[] getSheng() throws Throwable {
		ArrayList<T5019> list = null;
		try (Connection connection = getConnection(P2PConst.DB_INFORMATION)) {
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F01, F05 FROM S50.T5019 WHERE T5019.F11 = ? AND F13=?")) {
				pstmt.setString(1, T5019_F11.SHENG.name());
				pstmt.setString(2, T5019_F13.QY.name());
				try (ResultSet resultSet = pstmt.executeQuery()) {
					while (resultSet.next()) {
						T5019 record = new T5019();
						record.F01 = resultSet.getInt(1);
						record.F05 = resultSet.getString(2);
						if (list == null) {
							list = new ArrayList<>();
						}
						list.add(record);
					}
				}
			}
		}
		return ((list == null || list.size() == 0) ? null : list
				.toArray(new T5019[list.size()]));
	}

	@Override
	public T5019[] getXian(int shiId) throws Throwable {
		if (shiId <= 0) {
			throw new ParameterException("参数错误");
		}
		String sql = "SELECT F01,F05 FROM T5019 WHERE F02=? AND F03=? AND F04<>0 AND F11=? AND F13=?";
		List<T5019> lists = new ArrayList<>();
		try (Connection connection = getConnection(P2PConst.DB_INFORMATION)) {
			try (PreparedStatement ps = connection.prepareStatement(sql)) {
				ps.setInt(1, shiId / 10000);
				ps.setInt(2, (shiId % 10000) / 100);
				ps.setString(3, T5019_F11.XIAN.name());
				ps.setString(4, T5019_F13.QY.name());
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						T5019 t5019 = new T5019();
						t5019.F01 = rs.getInt(1);
						t5019.F05 = rs.getString(2);
						lists.add(t5019);
					}
				}
			}
		}
		return lists.toArray(new T5019[lists.size()]);
	}

	@Override
    public void enable(int id)
        throws Throwable
    {
        if (id <= 0)
        {
            return;
        }
        T5019_F11 type = T5019_F11.XIAN;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement("SELECT F11 FROM S50.T5019 WHERE F01=?"))
            {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        type = EnumParser.parse(T5019_F11.class, rs.getString(1));
                    }
                }
            }
            try
            {
                if (type == T5019_F11.SHENG)
                {
                    String sql = "UPDATE S50.T5019 SET F13=? WHERE F02=?";
                    execute(connection, sql, T5019_F13.QY, id / 10000);
                }
                else if (type == T5019_F11.SHI)
                {
                    String sql = "UPDATE S50.T5019 SET F13=? WHERE F02=? AND F03=?";
                    execute(connection, sql, T5019_F13.QY, id / 10000, (id % 10000) / 100);
                }
                else if (type == T5019_F11.XIAN)
                {
                    String sql = "UPDATE S50.T5019 SET F13=? WHERE F01=?";
                    execute(connection, sql, T5019_F13.QY, id);
                }
            }
            catch (Exception e)
            {
                throw e;
            }
        }
    }

	@Override
    public void disable(int id)
        throws Throwable
    {
        if (id <= 0)
        {
            return;
        }
        T5019_F11 type = T5019_F11.XIAN;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement("SELECT F11 FROM S50.T5019 WHERE F01=?"))
            {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        type = EnumParser.parse(T5019_F11.class, rs.getString(1));
                    }
                }
            }
            try
            {
                if (type == T5019_F11.SHENG)
                {
                    String sql = "UPDATE S50.T5019 SET F13=? WHERE F02=?";
                    execute(connection, sql, T5019_F13.TY, id / 10000);
                }
                else if (type == T5019_F11.SHI)
                {
                    String sql = "UPDATE S50.T5019 SET F13=? WHERE F02=? AND F03=?";
                    execute(connection, sql, T5019_F13.TY, id / 10000, (id % 10000) / 100);
                }
                else if (type == T5019_F11.XIAN)
                {
                    String sql = "UPDATE S50.T5019 SET F13=? WHERE F01=?";
                    execute(connection, sql, T5019_F13.TY, id);
                }
            }
            catch (Exception e)
            {
                throw e;
            }
        }
    }

	@Override
    public void enableAll()
        throws Throwable
    {
        String sql = "UPDATE T5019 SET F13=?";
        try (Connection connection = getConnection(P2PConst.DB_INFORMATION))
        {
            execute(connection, sql, T5019_F13.QY);
        }
    }

	@Override
    public void disableAll()
        throws Throwable
    {
        String sql = "UPDATE T5019 SET F13=?";
        try (Connection connection = getConnection(P2PConst.DB_INFORMATION))
        {
            execute(connection, sql, T5019_F13.TY);
        }
    }

}
