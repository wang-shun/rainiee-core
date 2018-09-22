package com.dimeng.p2p.modules.base.front.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.dimeng.framework.resource.ResourceNotFoundException;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S50.entities.T5019;
import com.dimeng.p2p.S50.enums.T5019_F11;
import com.dimeng.p2p.S50.enums.T5019_F13;
import com.dimeng.p2p.modules.base.front.service.DistrictManage;
import com.dimeng.p2p.variables.P2PConst;
import com.dimeng.util.StringHelper;

public class DistrictManageImpl extends AbstractBaseService implements
		DistrictManage {

	public DistrictManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}
	
	protected static final ArrayParser<T5019> ARRAY_PARSER = new ArrayParser<T5019>() {

		@Override
		public T5019[] parse(ResultSet rs) throws SQLException {
			ArrayList<T5019> list = null;
			while (rs.next()) {
				T5019 t5019 = new T5019();
				t5019.F01 = rs.getInt(1);
				t5019.F02 = rs.getShort(2);
				t5019.F03 = rs.getShort(3);
				t5019.F04 = rs.getShort(4);
				t5019.F05 = rs.getString(5);
				t5019.F06 = rs.getString(6);
				t5019.F07 = rs.getString(7);
				t5019.F08 = rs.getString(8);
				
				if (list == null) {
					list = new ArrayList<>();
				}
				list.add(t5019);
			}
			return list == null ? null : list.toArray(new T5019[list.size()]);
		}
	};

	@Override
	public T5019[] getShi(int shengId) throws Throwable {
		if (shengId <= 0) {
			throw new ParameterException("参数错误");
		}
		String sql = "SELECT F01,F05 FROM S50.T5019 WHERE F02=? AND F04=0 AND F11=? AND F13=?";
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
		String sql = "SELECT F01,F05 FROM S50.T5019 WHERE F02=? AND F03=? AND F04<>0 AND F11=? AND F13=?";
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
	public T5019 getShengName(int id) throws Throwable {
		try (Connection connection = getConnection()) {
			T5019 record = null;
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F01, F06, F07, F08 FROM S50.T5019 WHERE T5019.F01 = ? LIMIT 1")) {
				pstmt.setInt(1, id);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						record = new T5019();
						record.F01 = resultSet.getInt(1);
						record.F06 = resultSet.getString(2);
						record.F07 = resultSet.getString(3);
						record.F08 = resultSet.getString(4);
					}
				}
			}
			return record;
		}

	}


	/**
	 * 通过id的信息
	 * @param areaId
	 * @return
	 * @throws Throwable
	 */
	@Override
	public T5019 getArea(int areaId) throws Throwable{
		try (Connection connection = getConnection()) {
			T5019 record = null;
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F01, F06, F07, F08 FROM S50.T5019 WHERE T5019.F01 = ? LIMIT 1")) {
				pstmt.setInt(1, areaId);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						record = new T5019();
						record.F01 = resultSet.getInt(1);
						record.F06 = resultSet.getString(2);
						record.F07 = resultSet.getString(3);
						record.F08 = resultSet.getString(4);
					}
				}
			}
			return record;
		}
	}
	/**
	 * 支持中文名字模糊查询，支持首字母查询
	 * @param name
	 * @return
	 * @throws SQLException 
	 * @throws ResourceNotFoundException 
	 */
	@Override
	public PagingResult<T5019> searchXIAN(String name,Paging paging) throws ResourceNotFoundException,
 SQLException
    {
        if (StringHelper.isEmpty(name))
        {
            return null;
        }
        String sql =
            "SELECT F01, F02, F03, F04, F05, F06, F07, F08 FROM S50.T5019 where (F12 LIKE ? or F08 LIKE ? or F07 LIKE ? or F06 LIKE ? ) and  F11 = 'XIAN'";
        List<Object> paras = new ArrayList<Object>();
        paras.add("%" + name.toUpperCase() + "%");
        paras.add("%" + name + "%");
        paras.add("%" + name + "%");
        paras.add("%" + name + "%");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, ARRAY_PARSER, paging, sql.toString(), paras);
        }
    }
}