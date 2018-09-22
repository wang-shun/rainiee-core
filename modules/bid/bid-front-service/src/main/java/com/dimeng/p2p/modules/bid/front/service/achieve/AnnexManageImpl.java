/**
 * 
 */
package com.dimeng.p2p.modules.bid.front.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S62.entities.T6232;
import com.dimeng.p2p.S62.entities.T6233;
import com.dimeng.p2p.modules.bid.front.service.AnnexManage;

public class AnnexManageImpl extends AbstractBidManage implements AnnexManage {

	public static class AnnexManageFactory implements
			ServiceFactory<AnnexManage> {

		@Override
		public AnnexManage newInstance(ServiceResource serviceResource) {
			return new AnnexManageImpl(serviceResource);
		}

	}

	public AnnexManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}


	@Override
	public T6233 getFgk(int id) throws Throwable {
		if (id <= 0) {
			throw new ParameterException("指定的记录不存在");
		}
		T6233 record = null;
		try (Connection connection = getConnection()) {
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09 FROM S62.T6233 WHERE T6233.F01 = ? LIMIT 1")) {
				pstmt.setInt(1, id);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						record = new T6233();
						record.F01 = resultSet.getInt(1);
						record.F02 = resultSet.getInt(2);
						record.F03 = resultSet.getInt(3);
						record.F04 = resultSet.getString(4);
						record.F05 = resultSet.getInt(5);
						record.F06 = resultSet.getString(6);
						record.F07 = resultSet.getString(7);
						record.F08 = resultSet.getTimestamp(8);
						record.F09 = resultSet.getInt(9);
					}
				}
			}
		}
		return record;
	}

	@Override
	public T6232 getGk(int id) throws Throwable {
		if (id <= 0) {
			throw new ParameterException("指定的记录不存在");
		}
		T6232 record = null;
		try (Connection connection = getConnection()) {
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09 FROM S62.T6232 WHERE T6232.F01 = ? LIMIT 1")) {
				pstmt.setInt(1, id);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						record = new T6232();
						record.F01 = resultSet.getInt(1);
						record.F02 = resultSet.getInt(2);
						record.F03 = resultSet.getInt(3);
						record.F04 = resultSet.getString(4);
						record.F05 = resultSet.getString(5);
						record.F06 = resultSet.getInt(6);
						record.F07 = resultSet.getString(7);
						record.F08 = resultSet.getTimestamp(8);
						record.F09 = resultSet.getInt(9);
					}
				}
			}
		}
		return record;
	}


	
}
