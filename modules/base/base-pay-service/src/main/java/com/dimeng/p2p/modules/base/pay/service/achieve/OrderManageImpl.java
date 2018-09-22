package com.dimeng.p2p.modules.base.pay.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.OrderType;
import com.dimeng.p2p.S65.entities.T6501;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.S65.enums.T6501_F07;
import com.dimeng.p2p.modules.base.pay.service.OrderManage;

public class OrderManageImpl extends AbstractBaseManage implements OrderManage {

	public OrderManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	@Override
	public T6501 getToSubmit() throws Throwable {
		try (Connection connection = getConnection()) {
			T6501 record = null;
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09 FROM S65.T6501 WHERE T6501.F03 = ? LIMIT 1")) {
				pstmt.setString(1, T6501_F03.DTJ.name());
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						record = new T6501();
						record.F01 = resultSet.getInt(1);
						record.F02 = resultSet.getInt(2);
						record.F03 = T6501_F03.parse(resultSet.getString(3));
						record.F04 = resultSet.getTimestamp(4);
						record.F05 = resultSet.getTimestamp(5);
						record.F06 = resultSet.getTimestamp(6);
						record.F07 = T6501_F07.parse(resultSet.getString(7));
						record.F08 = resultSet.getInt(8);
						record.F09 = resultSet.getInt(9);
					}
				}
			}
			return record;
		}
	}

	@Override
	public T6501 getToSubmit(OrderType orderType) throws Throwable {
		try (Connection connection = getConnection()) {
			T6501 record = null;
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09 FROM S65.T6501 WHERE T6501.F03 = ? AND T6501.F02 = ? LIMIT 1")) {
				pstmt.setString(1, T6501_F03.DTJ.name());
				pstmt.setInt(2, orderType.orderType());
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						record = new T6501();
						record.F01 = resultSet.getInt(1);
						record.F02 = resultSet.getInt(2);
						record.F03 = T6501_F03.parse(resultSet.getString(3));
						record.F04 = resultSet.getTimestamp(4);
						record.F05 = resultSet.getTimestamp(5);
						record.F06 = resultSet.getTimestamp(6);
						record.F07 = T6501_F07.parse(resultSet.getString(7));
						record.F08 = resultSet.getInt(8);
						record.F09 = resultSet.getInt(9);
					}
				}
			}
			return record;
		}
	}
}
