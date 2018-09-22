package com.dimeng.p2p.modules.base.front.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.p2p.S50.entities.T5017;
import com.dimeng.p2p.S50.enums.T5017_F05;
import com.dimeng.p2p.common.enums.TermType;
import com.dimeng.p2p.modules.base.front.service.TermManage;

public class TermManageImpl extends AbstractBaseService implements
		TermManage {

	public TermManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	@Override
	public T5017 get(final TermType termType) throws Throwable {
		if (termType == null) {
			return null;
		}
		try (Connection connection = getConnection()) {
			return select(
					connection,
					new ItemParser<T5017>() {
						@Override
						public T5017 parse(ResultSet resultSet)
								throws SQLException {
							T5017 record = null;
							if (resultSet.next()) {
								record = new T5017();
								record.F01 = TermType.parse(resultSet
										.getString(1));
								record.F02 = resultSet.getString(2);
								record.F03 = resultSet.getInt(3);
								record.F04 = resultSet.getTimestamp(4);
								record.F05 = T5017_F05.parse(resultSet.getString(5));
							}
							return record;
						}
					},
					"SELECT F01, F02, F03, F04, F05 FROM S50.T5017 WHERE F01 = ? AND F05 = ?",
					termType, T5017_F05.QY.name());
		}
	}

	@Override
	public String[] getAttachments(TermType termType) throws Throwable {
		if (termType == null) {
			return null;
		}
		try (Connection connection = getConnection();
				PreparedStatement pstmt = connection
						.prepareStatement("SELECT F02 FROM S50.T5017_1 WHERE F01 = ?");) {
			pstmt.setString(1, termType.name());
			try (ResultSet resultSet = pstmt.executeQuery()) {
				ArrayList<String> list = null;
				while (resultSet.next()) {
					if (list == null) {
						list = new ArrayList<>();
					}
					list.add(resultSet.getString(1));
				}
				return list == null || list.size() == 0 ? null
						: new String[list.size()];
			}
		}
	}

	@Override
	public void view(TermType termType) throws Throwable {
		if (termType == null) {
			return;
		}
		try (Connection connection = getConnection()) {
			execute(connection,
					"UPDATE S50.T5017 SET F02 = F02 + 1 WHERE F01 = ? ",
					termType);
		}
	}
}
