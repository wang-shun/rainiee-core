package com.dimeng.p2p.modules.base.front.service.achieve;

import java.awt.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.dimeng.framework.http.upload.FileStore;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S50.entities.T5013;
import com.dimeng.p2p.S50.enums.T5010_F04;
import com.dimeng.p2p.modules.base.front.service.PartnerManage;
import com.dimeng.p2p.modules.base.front.service.entity.T5013_EXT;
import com.dimeng.util.Formater;

public class PartnerManageImpl extends AbstractBaseService implements
		PartnerManage {

	public PartnerManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	@Override
	public T5013[] getAll() throws Throwable {
	    try (Connection connection = getConnection()) {
            ArrayList<T5013> list = null;
            try (PreparedStatement pstmt = connection
                .prepareStatement("SELECT F01 FROM S50.T5010 WHERE F02='HZJG' AND F04=? LIMIT 1")) {
                pstmt.setString(1, T5010_F04.QY.name());
                try (ResultSet resultSet = pstmt.executeQuery()) {
                    while (resultSet.next()) {
                        try (PreparedStatement ps = connection
                            .prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11 FROM S50.T5013")) {
                            try (ResultSet rs = ps.executeQuery()) {
                                while (rs.next()) {
                                    T5013 record = new T5013();
                                    record.F01 = rs.getInt(1);
                                    record.F02 = rs.getInt(2);
                                    record.F03 = rs.getInt(3);
                                    record.F04 = rs.getString(4);
                                    record.F05 = rs.getString(5);
                                    record.F06 = rs.getString(6);
                                    record.F07 = rs.getString(7);
                                    record.F08 = rs.getString(8);
                                    record.F09 = rs.getInt(9);
                                    record.F10 = rs.getTimestamp(10);
                                    record.F11 = rs.getTimestamp(11);
                                    if (list == null) {
                                        list = new ArrayList<>();
                                    }
                                    list.add(record);
                                }
                            }
                        }
                        return ((list == null || list.size() == 0) ? null : list
                            .toArray(new T5013[list.size()]));
                    }
                    return null;
                }
            }
        }
	 
	}

	@Override
	public PagingResult<T5013> getPagedList(Paging paging) throws Throwable {
		try (Connection connection = getConnection()) {
			return selectPaging(
					connection,
					new ArrayParser<T5013>() {

						@Override
						public T5013[] parse(ResultSet resultSet)
								throws SQLException {
							ArrayList<T5013> list = null;
							while (resultSet.next()) {
								T5013 record = new T5013();
								record.F01 = resultSet.getInt(1);
								record.F02 = resultSet.getInt(2);
								record.F03 = resultSet.getInt(3);
								record.F04 = resultSet.getString(4);
								record.F05 = resultSet.getString(5);
								record.F06 = resultSet.getString(6);
								record.F07 = resultSet.getString(7);
								record.F08 = resultSet.getString(8);
								record.F09 = resultSet.getInt(9);
								record.F10 = resultSet.getTimestamp(10);
								record.F11 = resultSet.getTimestamp(11);
								if (list == null) {
									list = new ArrayList<>();
								}
								list.add(record);
							}
							return list == null || list.size() == 0 ? null
									: list.toArray(new T5013[list.size()]);
						}
					},
					paging,
					"SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11 FROM S50.T5013 ORDER BY F02 DESC");
		}
	}

	@Override
	public T5013 get(int F01) throws Throwable {
		if (F01 <= 0) {
			return null;
		}
		try (Connection connection = getConnection()) {
			T5013 record = null;
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11 FROM S50.T5013 WHERE T5013.F01 = ? LIMIT 1")) {
				pstmt.setInt(1, F01);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						record = new T5013();
						record.F01 = resultSet.getInt(1);
						record.F02 = resultSet.getInt(2);
						record.F03 = resultSet.getInt(3);
						record.F04 = resultSet.getString(4);
						record.F05 = resultSet.getString(5);
						record.F06 = resultSet.getString(6);
						record.F07 = resultSet.getString(7);
						record.F08 = resultSet.getString(8);
						record.F09 = resultSet.getInt(9);
						record.F10 = resultSet.getTimestamp(10);
						record.F11 = resultSet.getTimestamp(11);
					}
				}
			}
			return record;
		}
	}

	@Override
	public void view(int id) throws Throwable {
		if (id <= 0) {
			return;
		}
		try (Connection connection = getConnection()) {
			execute(connection,
					"UPDATE S50.T5013 SET F03 = F03 + 1 WHERE F01 = ?", id);
		}

	}

	@Override
	public PagingResult<T5013_EXT> gett5013ExtList(Paging paging, final FileStore fileStore) throws Throwable {
		try (Connection connection = getConnection()) {
			return selectPaging(
					connection,
					new ArrayParser<T5013_EXT>() {

						@Override
						public T5013_EXT[] parse(ResultSet resultSet)
								throws SQLException {
							ArrayList<T5013_EXT> list = null;
							while (resultSet.next()) {
								T5013_EXT record = new T5013_EXT();
								record.F01 = resultSet.getInt(1);
								record.F02 = resultSet.getInt(2);
								record.F03 = resultSet.getInt(3);
								record.F04 = resultSet.getString(4);
								record.F05 = resultSet.getString(5);
								record.F06 = resultSet.getString(6);
								record.F07 = resultSet.getString(7);
								record.F08 = resultSet.getString(8);
								record.F09 = resultSet.getInt(9);
								record.F10 = resultSet.getTimestamp(10);
								record.F11 = resultSet.getTimestamp(11);
								record.pic = fileStore.getURL(record.F06);
								record.lastUPdateTime = Formater.formatDate(record.F10);
								if (list == null) {
									list = new ArrayList<>();
								}
								list.add(record);
							}
							return list == null || list.size() == 0 ? null : list.toArray(new T5013_EXT[list.size()]);
						}
					},
					paging,
					"SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11 FROM S50.T5013 ORDER BY F02 DESC");
		}
	}

}