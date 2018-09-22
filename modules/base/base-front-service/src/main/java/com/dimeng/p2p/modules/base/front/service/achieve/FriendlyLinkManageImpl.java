package com.dimeng.p2p.modules.base.front.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.S50.entities.T5014;
import com.dimeng.p2p.modules.base.front.service.FriendlyLinkManage;

public class FriendlyLinkManageImpl extends AbstractBaseService
		implements FriendlyLinkManage {

	public FriendlyLinkManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	@Override
	public T5014[] getAll() throws Throwable {
		try (Connection connection = getConnection()) {
			ArrayList<T5014> list = null;
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08 FROM S50.T5014 ORDER BY F03 ASC")) {
				try (ResultSet resultSet = pstmt.executeQuery()) {
					while (resultSet.next()) {
						T5014 record = new T5014();
						record.F01 = resultSet.getInt(1);
						record.F02 = resultSet.getInt(2);
						record.F03 = resultSet.getInt(3);
						record.F04 = resultSet.getString(4);
						record.F05 = resultSet.getString(5);
						record.F06 = resultSet.getInt(6);
						record.F07 = resultSet.getTimestamp(7);
						record.F08 = resultSet.getTimestamp(8);
						if (list == null) {
							list = new ArrayList<>();
						}
						list.add(record);
					}
				}
			}
			return ((list == null || list.size() == 0) ? null : list
					.toArray(new T5014[list.size()]));
		}
	}

	@Override
	public T5014 get(int F01) throws Throwable {
		if (F01 <= 0) {
			return null;
		}
		try (Connection connection = getConnection()) {
			T5014 record = null;
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08 FROM S50.T5014 WHERE T5014.F01 = ? LIMIT 1")) {
				pstmt.setInt(1, F01);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						record = new T5014();
						record.F01 = resultSet.getInt(1);
						record.F02 = resultSet.getInt(2);
						record.F03 = resultSet.getInt(3);
						record.F04 = resultSet.getString(4);
						record.F05 = resultSet.getString(5);
						record.F06 = resultSet.getInt(6);
						record.F07 = resultSet.getTimestamp(7);
						record.F08 = resultSet.getTimestamp(8);
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
            execute(connection, "UPDATE S50.T5014 SET F02 = F02 + 1 WHERE F01 = ? ", id);
        }
    }

}