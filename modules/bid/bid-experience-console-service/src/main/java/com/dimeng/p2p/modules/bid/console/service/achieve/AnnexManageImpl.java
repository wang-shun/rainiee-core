/**
 * 
 */
package com.dimeng.p2p.modules.bid.console.service.achieve;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.dimeng.framework.http.upload.UploadFile;
import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.p2p.S62.entities.T6212;
import com.dimeng.p2p.S62.entities.T6232;
import com.dimeng.p2p.S62.entities.T6233;
import com.dimeng.p2p.S62.enums.T6212_F04;
import com.dimeng.p2p.S62.enums.T6233_F10;
import com.dimeng.p2p.modules.bid.console.service.AnnexManage;
import com.dimeng.p2p.modules.bid.console.service.entity.MskAnnex;
import com.dimeng.p2p.modules.bid.console.service.entity.WzAnnex;
import com.dimeng.util.parser.EnumParser;

/**
 * @author guopeng
 * 
 */
public class AnnexManageImpl extends AbstractBidService implements AnnexManage {

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
	public WzAnnex[] searchFgk(int loanId) throws Throwable {
		try (Connection connection = getConnection()) {
			ArrayList<WzAnnex> list = null;
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10 FROM S62.T6233 WHERE T6233.F02 = ?")) {
				pstmt.setInt(1, loanId);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					while (resultSet.next()) {
						WzAnnex record = new WzAnnex();
						record.F01 = resultSet.getInt(1);
						record.F02 = resultSet.getInt(2);
						record.F03 = resultSet.getInt(3);
						record.F04 = resultSet.getString(4);
						record.F05 = resultSet.getInt(5);
						record.F06 = resultSet.getString(6);
						record.F07 = resultSet.getString(7);
						record.F08 = resultSet.getTimestamp(8);
						record.F09 = resultSet.getInt(9);
						record.annexName = getAnnexName(record.F03);
						record.name = getName(record.F09);
						record.F10 = EnumParser.parse(T6233_F10.class,
								resultSet.getString(10));
						if (list == null) {
							list = new ArrayList<>();
						}
						list.add(record);
					}
				}
			}
			return ((list == null || list.size() == 0) ? null : list
					.toArray(new WzAnnex[list.size()]));
		}
	}

	/**
	 * 查询上传人账号
	 * 
	 * @throws SQLException
	 */
	private String getName(int userId) throws SQLException {
		try (Connection connection = getConnection()) {
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F02 FROM S71.T7110 WHERE T7110.F01 = ? LIMIT 1")) {
				pstmt.setInt(1, userId);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						return resultSet.getString(1);
					}
				}
			}
		}
		return "";
	}

	/**
	 * 查询附件类型名称
	 * 
	 * @throws SQLException
	 */
	private String getAnnexName(int typeId) throws SQLException {
		try (Connection connection = getConnection()) {
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F02 FROM S62.T6212 WHERE T6212.F01 = ? LIMIT 1")) {
				pstmt.setInt(1, typeId);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						return resultSet.getString(1);
					}
				}
			}
		}
		return "";
	}

	@Override
	public MskAnnex[] searchGk(int loanId) throws Throwable {
		try (Connection connection = getConnection()) {
			ArrayList<MskAnnex> list = null;
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09 FROM S62.T6232 WHERE T6232.F02 = ?")) {
				pstmt.setInt(1, loanId);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					while (resultSet.next()) {
						MskAnnex record = new MskAnnex();
						record.F01 = resultSet.getInt(1);
						record.F02 = resultSet.getInt(2);
						record.F03 = resultSet.getInt(3);
						record.F04 = resultSet.getString(4);
						record.F05 = resultSet.getString(5);
						record.F06 = resultSet.getInt(6);
						record.F07 = resultSet.getString(7);
						record.F08 = resultSet.getTimestamp(8);
						record.F09 = resultSet.getInt(9);
						record.annexName = getAnnexName(record.F03);
						record.name = getName(record.F09);
						if (list == null) {
							list = new ArrayList<>();
						}
						list.add(record);
					}
				}
			}
			return ((list == null || list.size() == 0) ? null : list
					.toArray(new MskAnnex[list.size()]));
		}
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

	@Override
    public int addGk(T6232 entity, UploadFile file)
        throws Throwable
    {
        if (entity == null)
        {
            throw new ParameterException("参数不能为空");
        }
        
        int id = 0;
        try(InputStream inputStream = file.getInputStream()) {
			try (Connection connection = getConnection()) {
				try {
					serviceResource.openTransactions(connection);
					try (PreparedStatement pstmt =
								 connection.prepareStatement("INSERT INTO S62.T6232 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?",
										 PreparedStatement.RETURN_GENERATED_KEYS)) {
						pstmt.setInt(1, entity.F02);
						pstmt.setInt(2, entity.F03);
						pstmt.setString(3, entity.F04);
						pstmt.setString(4, entity.F05);
						pstmt.setInt(5, inputStream.available());
						pstmt.setString(6, file.getSuffix());
						pstmt.setTimestamp(7, getCurrentTimestamp(connection));
						pstmt.setInt(8, serviceResource.getSession().getAccountId());
						pstmt.execute();
						try (ResultSet resultSet = pstmt.getGeneratedKeys();) {
							if (resultSet.next()) {
								id = resultSet.getInt(1);
							}
						}
					}
					serviceResource.commit(connection);
				} catch (Exception e) {
					serviceResource.rollback(connection);
					throw e;
				}
				return id;
			}
		}
    }
	@Override
    public int addFgk(T6233 entity, UploadFile file)
        throws Throwable
    {
        if (entity == null)
        {
            throw new ParameterException("参数不能为空");
        }
        
        int id = 0;
        try(InputStream inputStream = file.getInputStream()) {
			try (Connection connection = getConnection()) {
				try {
					serviceResource.openTransactions(connection);
					try (PreparedStatement pstmt =
								 connection.prepareStatement("INSERT INTO S62.T6233 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?",
										 PreparedStatement.RETURN_GENERATED_KEYS)) {
						pstmt.setInt(1, entity.F02);
						pstmt.setInt(2, entity.F03);
						pstmt.setString(3, entity.F04);
						pstmt.setInt(4, inputStream.available());
						pstmt.setString(5, entity.F06);
						pstmt.setString(6, file.getSuffix());
						pstmt.setTimestamp(7, getCurrentTimestamp(connection));
						pstmt.setInt(8, serviceResource.getSession().getAccountId());
						pstmt.execute();
						try (ResultSet resultSet = pstmt.getGeneratedKeys();) {
							if (resultSet.next()) {
								id = resultSet.getInt(1);
							}
						}
					}
					serviceResource.commit(connection);
				} catch (Exception e) {
					serviceResource.rollback(connection);
					throw e;
				}
				return id;
			}
		}
    }

	@Override
    public void delGk(int... ids)
        throws Throwable
    {
        if (ids == null || ids.length <= 0)
        {
            throw new ParameterException("指定的记录不存在");
        }
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                String sql = "DELETE FROM S62.T6232 WHERE F01=?";
                for (int id : ids)
                {
                    if (id <= 0)
                    {
                        continue;
                    }
                    execute(connection, sql, id);
                }
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }

	@Override
    public void delFgk(int... ids)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                String sql = "DELETE FROM S62.T6233 WHERE F01=?";
                for (int id : ids)
                {
                    if (id <= 0)
                    {
                        continue;
                    }
                    execute(connection, sql, id);
                }
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }

	@Override
    public T6212[] searchAnnexType()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            return selectAll(connection, new ArrayParser<T6212>()
            {
                
                @Override
                public T6212[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    List<T6212> lists = new ArrayList<>();
                    while (resultSet.next())
                    {
                        T6212 t6212 = new T6212();
                        t6212.F01 = resultSet.getInt(1);
                        t6212.F02 = resultSet.getString(2);
                        t6212.F03 = resultSet.getInt(3);
                        t6212.F04 = EnumParser.parse(T6212_F04.class, resultSet.getString(4));
                        lists.add(t6212);
                    }
                    return lists.toArray(new T6212[lists.size()]);
                }
            }, "SELECT F01,F02,F03,F04 FROM S62.T6212 WHERE F04=? ORDER BY F03 ASC", T6212_F04.QY);
        }
    }

	@Override
	public boolean isGkExists(int loanId) throws Throwable {
		try (Connection connection = getConnection()) {
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F01 FROM S62.T6232 WHERE T6232.F02 = ? LIMIT 1")) {
				pstmt.setInt(1, loanId);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public boolean isFgkExists(int loanId) throws Throwable {
		try (Connection connection = getConnection()) {
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F01 FROM S62.T6233 WHERE T6233.F02 = ? LIMIT 1")) {
				pstmt.setInt(1, loanId);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
