package com.dimeng.p2p.modules.base.front.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.S51.entities.T5124;
import com.dimeng.p2p.S51.enums.T5124_F05;
import com.dimeng.p2p.modules.base.front.service.CreditLevelManage;

public class CreditLevelManageImpl extends AbstractBaseService implements
		CreditLevelManage {

	public CreditLevelManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	@Override
	public T5124[] searchAll() throws Throwable {
		try (Connection connection = getConnection()) {
			ArrayList<T5124> list = null;
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F01, F02, F03, F04, F05 FROM S51.T5124 WHERE T5124.F05 = ? ORDER BY F03 DESC")) {
				pstmt.setString(1, T5124_F05.QY.name());
				try (ResultSet resultSet = pstmt.executeQuery()) {
					while (resultSet.next()) {
						T5124 record = new T5124();
						record.F01 = resultSet.getInt(1);
						record.F02 = resultSet.getString(2);
						record.F03 = resultSet.getInt(3);
						record.F04 = resultSet.getInt(4);
						record.F05 = T5124_F05.parse(resultSet.getString(5));
						if (list == null) {
							list = new ArrayList<>();
						}
						list.add(record);
					}
				}
			}
			return ((list == null || list.size() == 0) ? null : list
					.toArray(new T5124[list.size()]));
		}
	}

	@Override
	public String getXydj(int scroe) throws Throwable {
		try(Connection connection = getConnection()){
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT T5124.F02 FROM S51.T5124 WHERE T5124.F03 <= ? AND T5124.F04 >= ?")) {
				ps.setInt(1, scroe);
				ps.setInt(2, scroe);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						return  rs.getString(1);
					}
				}
			}

		}
		return null;
	}

	@Override
	public int getId(int scroe) throws Throwable {
		try(Connection connection = getConnection()){
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT T5124.F01 FROM S51.T5124 WHERE T5124.F03 <= ? AND T5124.F04 >= ?")) {
				ps.setInt(1, scroe);
				ps.setInt(2, scroe);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						return  rs.getInt(1);
					}
				}
			}

		}
		return 0;
	}

	@Override
	public String get(int id) throws Throwable {
		try(Connection connection = getConnection()){
	    try (PreparedStatement pstmt = connection.prepareStatement("SELECT F02 FROM S51.T5124 WHERE T5124.F01 = ? LIMIT 1")) {
	        pstmt.setInt(1, id);
	        try(ResultSet resultSet = pstmt.executeQuery()) {
	            if(resultSet.next()) {
	                return resultSet.getString(1);
	            }
	        }
	    }
	    return "";
	}
	}
}
