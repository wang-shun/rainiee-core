package com.dimeng.p2p.account.user.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.enums.T6123_F05;
import com.dimeng.p2p.account.user.service.LetterManage;
import com.dimeng.p2p.account.user.service.entity.LetterEntity;
import com.dimeng.p2p.common.enums.LetterStatus;

public class LetterManageImpl extends AbstractAccountService implements
		LetterManage {

	public LetterManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	public static class LetterManageFactory implements
			ServiceFactory<LetterManage> {
		@Override
		public LetterManage newInstance(ServiceResource serviceResource) {
			return new LetterManageImpl(serviceResource);
		}
	}

	@Override
	public int getUnReadCount() throws Throwable {
		try (Connection connection = getConnection()) {
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT COUNT(F01) FROM S61.T6123 WHERE T6123.F02 = ? AND T6123.F05 = ? LIMIT 1")) {
				pstmt.setInt(1, serviceResource.getSession().getAccountId());
				pstmt.setString(2, T6123_F05.WD.name());
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						return resultSet.getInt(1);
					}
				}
			}
		}
		return 0;
	}

	@Override
	public PagingResult<LetterEntity> search(LetterStatus letterRead,
			Paging paging) throws Throwable {
		List<Object> params = new ArrayList<>();
		StringBuilder builder = new StringBuilder(
				"SELECT T6123.F01 AS F01, T6123.F03 AS F02, T6123.F04 AS F03, T6123.F05 AS F04, T6124.F02 AS F05 FROM S61.T6123 INNER JOIN S61.T6124 ON T6123.F01 = T6124.F01 WHERE T6123.F02 = ?");
		params.add(serviceResource.getSession().getAccountId());
		if (letterRead != null) {
			builder.append(" AND T6123.F05 = ?");
			params.add(letterRead.toString());
		} else {
			builder.append(" AND T6123.F05 <> ?");
			params.add(LetterStatus.SC.toString());
		}
        builder.append(" ORDER BY T6123.F05 ASC, T6123.F04 DESC");
		try (Connection connection = getConnection()) {
			return selectPaging(connection, new ArrayParser<LetterEntity>() {

				@Override
				public LetterEntity[] parse(ResultSet resultSet)
						throws SQLException {
					List<LetterEntity> letters = null;
					while (resultSet.next()) {
						if (letters == null) {
							letters = new ArrayList<>();
						}
						LetterEntity letter = new LetterEntity();
						letter.id = resultSet.getInt(1);
						letter.title = resultSet.getString(2);
						letter.sendTime = resultSet.getTimestamp(3);
						letter.status = LetterStatus.valueOf(resultSet
								.getString(4));
						letter.content = resultSet.getString(5);
						letters.add(letter);
					}
					return letters == null ? null : letters
							.toArray(new LetterEntity[letters.size()]);
				}
			}, paging, builder.toString(), params);
		}
	}

	@Override
	public String get(int letterID) throws Throwable {
		try (Connection connection = getConnection()) {
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F02 FROM S61.T6124 WHERE T6124.F01 = ? LIMIT 1")) {
				pstmt.setInt(1, letterID);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						return resultSet.getString(1);
					}
				}
			}
		}
		return null;
	}

	@Override
    public void delete(int... letterID)
        throws Throwable
    {
        if (letterID != null && letterID.length > 0)
        {
            StringBuffer strIds = new StringBuffer();
            for (int i = 0; i < letterID.length; i++)
            {
                strIds.append(letterID[i]);
                if (i < letterID.length - 1)
                {
                    strIds.append(",");
                }
            }
            StringBuilder sql = new StringBuilder("UPDATE S61.T6123 SET F05 = ? WHERE F01 in ( ");

			List<Object> params = new ArrayList<Object>();
			params.add(T6123_F05.SC.name());
			sql.append(getBatchId(strIds.toString(),params));
			sql.append(")");
			sql.append(" AND F02 = ?");
			params.add(String.valueOf(serviceResource.getSession().getAccountId()));
            try (Connection connection = getConnection())
            {
                execute(connection, sql.toString(), params.toArray());
            }
        }
    }

	
	@Override
    public void updateYd(int... letterID)
        throws Throwable
    {
        if (letterID != null && letterID.length > 0)
        {
            StringBuffer strIds = new StringBuffer();
            for (int i = 0; i < letterID.length; i++)
            {
                strIds.append(letterID[i]);
                if (i < letterID.length - 1)
                {
                    strIds.append(",");
                }
            }

			StringBuilder sql = new StringBuilder("UPDATE S61.T6123 SET F05 = ? WHERE F01 in ( ");
			List<Object> params = new ArrayList<Object>();
			params.add(T6123_F05.YD.name());
			sql.append(getBatchId(strIds.toString(),params));
			sql.append(")");
			sql.append(" AND F02 = ?");
			params.add(serviceResource.getSession().getAccountId());
			try (Connection connection = getConnection())
			{
				execute(connection, sql.toString(), params.toArray());
			}
        }
    }
	@Override
	public int getCount() throws Throwable {
		try (Connection connection = getConnection()) {
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT COUNT(F01) FROM S61.T6123 WHERE T6123.F02 = ? AND T6123.F05 <> ? LIMIT 1")) {
				pstmt.setInt(1, serviceResource.getSession().getAccountId());
				pstmt.setString(2, T6123_F05.SC.name());
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						return resultSet.getInt(1);
					}
				}
			}
		}
		return 0;
	}

	@Override
	public void updateToRead(int id) throws Throwable {
		if (id <= 0) {
			return;
		}
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection
					.prepareStatement("UPDATE S61.T6123 SET F05=? WHERE F01=?  and F02 = ? ")) {
				ps.setString(1, LetterStatus.YD.toString());
				ps.setInt(2, id);
				ps.setInt(3, serviceResource.getSession().getAccountId());
				ps.execute();
			}
		}
		
	}

	@Override
	public int getT6123(int letterID) throws Throwable {
		try (Connection connection = getConnection()) {
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F01 FROM S61.T6123 WHERE T6123.F01 = ? AND T6123.F02 = ? LIMIT 1")) {
				pstmt.setInt(1, letterID);
				pstmt.setInt(2, serviceResource.getSession().getAccountId());
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						return resultSet.getInt(1);
					}
				}
			}
		}
		return 0;
	}

}
