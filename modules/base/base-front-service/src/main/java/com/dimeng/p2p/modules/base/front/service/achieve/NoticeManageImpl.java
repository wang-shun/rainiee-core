package com.dimeng.p2p.modules.base.front.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S50.entities.T5015;
import com.dimeng.p2p.S50.enums.T5015_F02;
import com.dimeng.p2p.S50.enums.T5015_F04;
import com.dimeng.p2p.modules.base.front.service.NoticeManage;
import com.dimeng.p2p.modules.base.front.service.entity.Notice;
import com.dimeng.p2p.modules.base.front.service.entity.T5015_EXT;
import com.dimeng.util.Formater;

public class NoticeManageImpl extends AbstractBaseService implements
		NoticeManage {

	public NoticeManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	@Override
	public PagingResult<T5015> search(Paging paging) throws Throwable {
		try (Connection connection = getConnection()) {
			return selectPaging(
					connection,
					new ArrayParser<T5015>() {
						@Override
						public T5015[] parse(ResultSet resultSet)
								throws SQLException {
							List<T5015> list = null;
							while (resultSet.next()) {
								T5015 record = new T5015();
								record.F01 = resultSet.getInt(1);
								record.F02 = T5015_F02.parse(resultSet
										.getString(2));
								record.F03 = resultSet.getInt(3);
								record.F04 = T5015_F04.parse(resultSet
										.getString(4));
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
									: list.toArray(new T5015[list.size()]);
						}
					},
					paging,
					"SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09 FROM S50.T5015 WHERE F04=? ORDER BY F09 DESC ",
					T5015_F04.YFB);
		}
	}

	@Override
	public T5015 get(final int id) throws Throwable {
		if (id <= 0) {
			return null;
		}
		try (Connection connection = getConnection()) {
			return select(
					connection,
					new ItemParser<T5015>() {
						@Override
						public T5015 parse(ResultSet resultSet)
								throws SQLException {
							T5015 record = null;
							if (resultSet.next()) {
								record = new T5015();
								record.F01 = resultSet.getInt(1);
								record.F02 = T5015_F02.parse(resultSet
										.getString(2));
								record.F03 = resultSet.getInt(3);
								record.F04 = T5015_F04.parse(resultSet
										.getString(4));
								record.F05 = resultSet.getString(5);
								record.F06 = resultSet.getString(6);
								record.F07 = resultSet.getInt(7);
								record.F08 = resultSet.getTimestamp(8);
								record.F09 = resultSet.getTimestamp(9);
							}
							return record;
						}
					},
					"SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09 FROM S50.T5015 WHERE F01 = ? AND F04 = ?",
					id, T5015_F04.YFB);
		}

	}

	@Override
	public void view(int id) throws Throwable {
		if (id <= 0) {
			return;
		}
		try (Connection connection = getConnection()) {
			execute(connection,
					"UPDATE S50.T5015 SET F03 = F03 + 1 WHERE F01 = ? AND F04 = ?",
					id, T5015_F04.YFB);
		}

	}
	
	@Override
	public Notice getNotice() throws Throwable {
		Notice notice = null;
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT F01,F05,F09 FROM S50.T5015 WHERE F04='YFB' ORDER BY F09 DESC LIMIT 1 ")) {
				try (ResultSet resultSet = ps.executeQuery()) {
					if (resultSet.next()) {
						notice = new Notice();
						notice.id = resultSet.getInt(1);
						notice.title = resultSet.getString(2);
						notice.time = resultSet.getTimestamp(3);
					}
				}
			}
		}
		return notice;
	}

	@Override
	public PagingResult<T5015_EXT> searchExt(Paging paging) throws Throwable {
		try (Connection connection = getConnection()) {
			return selectPaging(
					connection,
					new ArrayParser<T5015_EXT>() {
						@Override
						public T5015_EXT[] parse(ResultSet resultSet)
								throws SQLException {
							List<T5015_EXT> list = null;
							while (resultSet.next()) {
								T5015_EXT record = new T5015_EXT();
								record.F01 = resultSet.getInt(1);
								record.F02 = T5015_F02.parse(resultSet
										.getString(2));
								record.F03 = resultSet.getInt(3);
								record.F04 = T5015_F04.parse(resultSet
										.getString(4));
								record.F05 = resultSet.getString(5);
								record.F06 = resultSet.getString(6);
								record.F07 = resultSet.getInt(7);
								record.F08 = resultSet.getTimestamp(8);
								record.F09 = resultSet.getTimestamp(9);
								record.t5015_F02 = record.F02.getChineseName();
								record.lastUPdateTime = Formater.formatDate(record.F09);
								if (list == null) {
									list = new ArrayList<>();
								}
								list.add(record);
							}
							return list == null || list.size() == 0 ? null
									: list.toArray(new T5015_EXT[list.size()]);
						}
					},
					paging,
					"SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09 FROM S50.T5015 WHERE F04=? ORDER BY F09 DESC ",
					T5015_F04.YFB);
		}
	}
}
