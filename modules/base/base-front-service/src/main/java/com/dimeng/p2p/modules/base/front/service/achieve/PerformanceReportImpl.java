package com.dimeng.p2p.modules.base.front.service.achieve;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S50.entities.T5018;
import com.dimeng.p2p.S50.enums.T5018_F03;
import com.dimeng.p2p.modules.base.front.service.PerformanceReportManage;

public class PerformanceReportImpl extends AbstractBaseService implements
		PerformanceReportManage {

	public PerformanceReportImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	@Override
	public PagingResult<T5018> search(Paging paging) throws Throwable {
		try (Connection connection = getConnection()) {
			return selectPaging(
					connection,
					new ArrayParser<T5018>() {
						@Override
						public T5018[] parse(ResultSet resultSet)
								throws SQLException {
							ArrayList<T5018> list = null;
							while (resultSet.next()) {
								T5018 record = new T5018();
								record.F01 = resultSet.getInt(1);
								record.F02 = resultSet.getInt(2);
								record.F03 = T5018_F03.parse(resultSet
										.getString(3));
								record.F04 = resultSet.getInt(4);
								record.F05 = resultSet.getString(5);
								record.F06 = resultSet.getString(6);
								record.F07 = resultSet.getInt(7);
								record.F08 = resultSet.getTimestamp(8);
								record.F09 = resultSet.getTimestamp(9);
								if (list == null) {
									list = new ArrayList<>();
								}
								list.add(record);
							}
							return list == null || list.size() == 0 ? null
									: list.toArray(new T5018[list.size()]);
						}
					},
					paging,
					"SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09 FROM S50.T5018 WHERE F03 = ? ORDER BY F09 DESC",
					T5018_F03.YFB);
		}

	}

	@Override
	public T5018 get(int id) throws Throwable {
		try (Connection connection = getConnection()) {
			return select(connection, new ItemParser<T5018>() {
				@Override
				public T5018 parse(ResultSet resultSet) throws SQLException {
					T5018 record = null;
					if (resultSet.next()) {
						record = new T5018();
						record.F01 = resultSet.getInt(1);
						record.F02 = resultSet.getInt(2);
						record.F03 = T5018_F03.parse(resultSet.getString(3));
						record.F04 = resultSet.getInt(4);
						record.F05 = resultSet.getString(5);
						record.F06 = resultSet.getString(6);
						record.F07 = resultSet.getInt(7);
						record.F08 = resultSet.getTimestamp(8);
						record.F09 = resultSet.getTimestamp(9);
					}
					return record;
				}
			}, "SELECT F01,F02,F03,F04,F05,F06,F07,F08,F09 FROM S50.T5018 WHERE F01 = ? AND F03 = ? ",
					id, T5018_F03.YFB);
		}

	}

	@Override
	public void view(int id) throws Throwable {
		if (id <= 0) {
			return;
		}
		try (Connection connection = getConnection()) {
			execute(connection,
					"UPDATE S50.T5018 SET F04 = F04 + 1 WHERE F01 = ? AND F03 = ?",
					id, T5018_F03.YFB);
		}

	}

}