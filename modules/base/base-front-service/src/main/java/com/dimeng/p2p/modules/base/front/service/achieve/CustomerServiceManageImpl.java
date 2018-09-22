package com.dimeng.p2p.modules.base.front.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.S50.entities.T5012;
import com.dimeng.p2p.S50.enums.T5012_F03;
import com.dimeng.p2p.S50.enums.T5012_F11;
import com.dimeng.p2p.modules.base.front.service.CustomerServiceManage;

public class CustomerServiceManageImpl extends AbstractBaseService
		implements CustomerServiceManage {

	public CustomerServiceManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	@Override
	public T5012[] getAll(T5012_F03 customerServiceType) throws Throwable {
		if (customerServiceType == null) {
			return null;
		}
		try (Connection connection = getConnection()) {
			ArrayList<T5012> list = null;
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11 FROM S50.T5012 WHERE F11 = ? ORDER BY T5012.F04 ASC,T5012.F10 ASC")) {
				pstmt.setString(1, T5012_F11.QY.name());
				try (ResultSet resultSet = pstmt.executeQuery()) {
					while (resultSet.next()) {
						T5012 record = new T5012();
						record.F01 = resultSet.getInt(1);
						record.F02 = resultSet.getInt(2);
						record.F03 = T5012_F03.parse(resultSet.getString(3));
						record.F04 = resultSet.getInt(4);
						record.F05 = resultSet.getString(5);
						record.F06 = resultSet.getString(6);
						record.F07 = resultSet.getString(7);
						record.F08 = resultSet.getInt(8);
						record.F09 = resultSet.getTimestamp(9);
						record.F10 = resultSet.getTimestamp(10);
						if (list == null) {
							list = new ArrayList<>();
						}
						list.add(record);
					}
				}
			}
			return ((list == null || list.size() == 0) ? null : list
					.toArray(new T5012[list.size()]));
		}
	}

	@Override
	public T5012 get(int F01) throws Throwable {
		if (F01 <= 0) {
			return null;
		}
		try (Connection connection = getConnection()) {
			T5012 record = null;
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10 FROM S50.T5012 WHERE T5012.F01 = ? LIMIT 1")) {
				pstmt.setInt(1, F01);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						record = new T5012();
						record.F01 = resultSet.getInt(1);
						record.F02 = resultSet.getInt(2);
						record.F03 = T5012_F03.parse(resultSet.getString(3));
						record.F04 = resultSet.getInt(4);
						record.F05 = resultSet.getString(5);
						record.F06 = resultSet.getString(6);
						record.F07 = resultSet.getString(7);
						record.F08 = resultSet.getInt(8);
						record.F09 = resultSet.getTimestamp(9);
						record.F10 = resultSet.getTimestamp(10);
					}
				}
			}
			return record;
		}
	}

	@Override
    public void view(int id)
        throws Throwable
    {
        if (id <= 0)
        {
            return;
        }
        try (Connection connection = getConnection())
        {
            execute(connection, "UPDATE S50.T5012 SET F02 = F02 + 1 WHERE F01 = ? ", id);
        }
    }

}
