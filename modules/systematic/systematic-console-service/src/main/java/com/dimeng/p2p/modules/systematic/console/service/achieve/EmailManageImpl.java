package com.dimeng.p2p.modules.systematic.console.service.achieve;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S71.enums.T7164_F07;
import com.dimeng.p2p.common.enums.SendType;
import com.dimeng.p2p.modules.systematic.console.service.EmailManage;
import com.dimeng.p2p.modules.systematic.console.service.entity.Email;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.EnumParser;

public class EmailManageImpl extends AbstractSystemService implements
		EmailManage {
	public static class EmailManageFactory implements
			ServiceFactory<EmailManage> {

		@Override
		public EmailManage newInstance(ServiceResource serviceResource) {
			return new EmailManageImpl(serviceResource);
		}
	}

	public EmailManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	@Override
	public int addEmail(T7164_F07 sendType, String title, String content,
			String... emails) throws Throwable {
		if (T7164_F07.SY == sendType) {
			// 查询所有用户邮箱
			emails = getUserEmails();
		}
        if (emails == null || emails.length == 0)
        {
			throw new ParameterException("没有指定邮箱账号信息.");
		}
		/**
		 * 邮件推广
		 */
		String t7164 = "INSERT INTO S71.T7164 SET F02=?,F03=?,F04=?,F05=?,F06=?,F07=?";
		/**
		 * 邮件推广指定人
		 */
		String t7165 = "INSERT INTO S71.T7165 SET F02=?,F03=?";
		List<String> emailLists = new ArrayList<>();
		for (String s : emails) {
			if (!StringHelper.isEmpty(s) && isEmail(s)) {
				emailLists.add(s);
			}
		}
        try (Connection connection = getConnection())
        {
            int emailId = 0;
            try
            {
                serviceResource.openTransactions(connection);
                emailId =
                    insert(connection, t7164, title, content, emailLists.size(), serviceResource.getSession()
                        .getAccountId(), new Timestamp(System.currentTimeMillis()), sendType);
                for (String email : emailLists)
                {
                    execute(connection, t7165, emailId, email);
                }
                serviceResource.commit(connection);
                return emailId;
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
	}

	private boolean isEmail(String email) throws Throwable {
		String m = "^\\w+([-\\.]\\w+)*@\\w+([\\.-]\\w+)*\\.\\w{2,4}$";
		if (email.matches(m)) {
			return true;
		}
		return false;
	}

	@Override
	public PagingResult<Email> search(String title, String name,
 String beginTime, String endTime, Paging paging)
        throws Throwable
    {
        String sql =
            "SELECT T7164.F01,T7164.F02,T7164.F03,T7164.F04,T7164.F06,T7164.F07,T7110.F02 AS ACCOUNTNAME FROM S71.T7164 INNER JOIN S71.T7110 ON T7164.F05=T7110.F01 WHERE 1=1";
        StringBuilder sb = new StringBuilder();
        sb.append(sql);
        List<Object> parameters = new ArrayList<>();
        if (!StringHelper.isEmpty(title))
        {
            sb.append(" AND T7164.F02 LIKE ?");
            parameters.add(getSQLConnectionProvider().allMatch(title));
        }
        if (!StringHelper.isEmpty(name))
        {
            sb.append(" AND T7110.F02 LIKE ?");
            parameters.add(getSQLConnectionProvider().allMatch(name));
        }
        if (!StringHelper.isEmpty(beginTime))
        {
            sb.append(" AND DATE(T7164.F06)>=?");
            parameters.add(beginTime);
        }
        if (!StringHelper.isEmpty(endTime))
        {
            sb.append(" AND DATE(T7164.F06)<=?");
            parameters.add(endTime);
        }
        sb.append(" ORDER BY T7164.F06 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<Email>()
            {
                
                @Override
                public Email[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    List<Email> emails = new ArrayList<>();
                    while (resultSet.next())
                    {
                        Email email = new Email();
                        email.id = resultSet.getInt(1);
                        email.title = resultSet.getString(2);
                        email.content = resultSet.getString(3);
                        email.count = resultSet.getInt(4);
                        email.createTime = resultSet.getTimestamp(5);
                        email.sendType = EnumParser.parse(SendType.class, resultSet.getString(6));
                        email.name = resultSet.getString(7);
                        emails.add(email);
                    }
                    return emails.toArray(new Email[emails.size()]);
                }
            }, paging, sb.toString(), parameters);
        }
    }

	@Override
	public String[] getUserEmails() throws Throwable {
		List<String> userEmails = new ArrayList<>();
		String sql = "SELECT F05 FROM S61.T6110";
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection.prepareStatement(sql)) {
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						if (!StringHelper.isEmpty(rs.getString(1))) {
							userEmails.add(rs.getString(1));
						}
					}
				}
			}
		}
		return userEmails.toArray(new String[userEmails.size()]);
	}

	@Override
    public Email get(int id)
        throws Throwable
    {
        if (id <= 0)
        {
            throw new ParameterException("指定的邮箱推广ID不存在.");
        }
        String sql =
            "SELECT T7164.F01,T7164.F02,T7164.F03,T7164.F04,T7164.F06,T7164.F07,T7110.F02 AS ACCOUNTNAME FROM S71.T7164 INNER JOIN S71.T7110 ON T7164.F05=T7110.F01 WHERE T7164.F01=?";
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<Email>()
            {
                
                @Override
                public Email parse(ResultSet resultSet)
                    throws SQLException
                {
                    Email email = new Email();
                    if (resultSet.next())
                    {
                        email.id = resultSet.getInt(1);
                        email.title = resultSet.getString(2);
                        email.content = resultSet.getString(3);
                        email.count = resultSet.getInt(4);
                        email.createTime = resultSet.getTimestamp(5);
                        email.sendType = EnumParser.parse(SendType.class, resultSet.getString(6));
                        email.name = resultSet.getString(7);
                        email.emails = getSendEmail(email.id);
                    }
                    return email;
                }
            }, sql, id);
        }
    }

    private String[] getSendEmail(int id)
    {
        if (id <= 0)
        {
            throw new ParameterException("指定的邮箱推广ID不存在.");
        }
        String sql = "SELECT F03 FROM S71.T7165 WHERE F02=?";
        try
        {
            try (Connection connection = getConnection())
            {
                return selectAll(connection, new ArrayParser<String>()
                {
                    
                    @Override
                    public String[] parse(ResultSet resultSet)
                        throws SQLException
                    {
                        List<String> lists = new ArrayList<>();
                        while (resultSet.next())
                        {
                            lists.add(resultSet.getString(1));
                        }
                        return lists.toArray(new String[lists.size()]);
                    }
                }, sql, id);
            }
        }
        catch (Throwable e)
        {
            logger.error(e, e);
        }
        return null;
    }

	@Override
	public String[] importEmail(InputStream inputStream, String charset)
			throws Throwable {
		if (inputStream == null) {
			throw new ParameterException("读取文件流不存在.");
		}
		if (StringHelper.isEmpty(charset)) {
			charset = "GBK";
		}
		List<String> emails = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(
				inputStream, charset))) {
			String line = "";
			while ((line = reader.readLine()) != null) {
				emails.add(line);
			}
		}
		return emails.toArray(new String[emails.size()]);
	}

	@Override
	public void export(OutputStream outputStream, String charset, int id)
			throws Throwable {
		if (outputStream == null) {
			throw new ParameterException("写入文件流不存在.");
		}
		if (StringHelper.isEmpty(charset)) {
			charset = "GBK";
		}
		String[] emails = getSendEmail(id);
		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				outputStream, charset))) {
			for (String email : emails) {
				if (StringHelper.isEmpty(email)) {
					continue;
				}
				writer.write(email);
				writer.newLine();
			}
		}
	}

	@Override
	public int getCheckUserId(String email) throws Throwable {
		String sql = "SELECT F01 FROM S61.T6110 WHERE F05=?";
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection.prepareStatement(sql)) {
				ps.setString(1, email);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						return rs.getInt(1);
					}
				}
			}
		}
		return 0;
	}
	
}
