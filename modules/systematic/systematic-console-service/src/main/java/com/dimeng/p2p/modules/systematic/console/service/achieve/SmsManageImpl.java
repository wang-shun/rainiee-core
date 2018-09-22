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

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S71.enums.T7162_F06;
import com.dimeng.p2p.common.SMSUtils;
import com.dimeng.p2p.common.enums.SendType;
import com.dimeng.p2p.modules.systematic.console.service.SmsManage;
import com.dimeng.p2p.modules.systematic.console.service.entity.Sms;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.EnumParser;

public class SmsManageImpl extends AbstractSystemService implements SmsManage {

	public SmsManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	@Override
	public int addSms(T7162_F06 sendType, String content, String... mobiles)
        throws Throwable
    {
        if (T7162_F06.SY == sendType)
        {
            // 查询所有用户手机号
            mobiles = getUserMobiles();
        }
        if (mobiles == null)
        {
            throw new ParameterException("没有指定手机号.");
        }
        /**
         * 短信推广
         */
        String t7162 = "INSERT INTO S71.T7162 SET F02=?,F03=?,F04=?,F05=?,F06=?";
        /**
         * 短信推广指定人
         */
        String t7163 = "INSERT INTO S71.T7163 SET F02=?,F03=?";
        int smsId = 0;
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                Timestamp now = new Timestamp(System.currentTimeMillis());
                List<String> mobileLists = new ArrayList<>();
                for (String s : mobiles)
                {
                    if (!StringHelper.isEmpty(s) && SMSUtils.isPhoneNumber(s))
                    {
                        mobileLists.add(s);
                    }
                }
                smsId =
                    insert(connection,
                        t7162,
                        content,
                        mobileLists.size(),
                        serviceResource.getSession().getAccountId(),
                        now,
                        sendType);
                for (String mobile : mobileLists)
                {
                    execute(connection, t7163, smsId, mobile);
                }
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
            return smsId;
        }
    }

	@Override
	public String[] getUserMobiles() throws Throwable {
		List<String> userMobiles = new ArrayList<>();
		String sql = "SELECT T6110.F04 FROM S61.T6110 LEFT JOIN S61.T6118 ON T6118.F01 = T6110.F01 WHERE T6118.F03 = 'TG'";
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection.prepareStatement(sql)) {
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						if (!StringHelper.isEmpty(rs.getString(1))) {
							userMobiles.add(rs.getString(1));
						}
					}
				}
			}
		}
		return userMobiles.toArray(new String[userMobiles.size()]);
	}

	@Override
	public PagingResult<Sms> serarch(String name, String beginTime,
 String endTime, Paging paging)
        throws Throwable
    {
        String sql =
            "SELECT T7162.F01,T7162.F02,T7162.F03,T7162.F05,T7162.F06,T7110.F02 AS ACCOUNTNAME FROM S71.T7162 INNER JOIN S71.T7110 ON T7162.F04=T7110.F01 WHERE 1=1";
        StringBuilder sb = new StringBuilder();
        sb.append(sql);
        List<Object> parameters = new ArrayList<>();
        if (!StringHelper.isEmpty(name))
        {
            sb.append(" AND T7110.F02 LIKE ?");
            parameters.add(getSQLConnectionProvider().allMatch(name));
        }
        if (!StringHelper.isEmpty(beginTime))
        {
            sb.append(" AND DATE(T7162.F05)>=?");
            parameters.add(beginTime);
        }
        if (!StringHelper.isEmpty(endTime))
        {
            sb.append(" AND DATE(T7162.F05)<=?");
            parameters.add(endTime);
        }
        sb.append(" ORDER BY T7162.F05 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<Sms>()
            {
                
                @Override
                public Sms[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    List<Sms> smses = new ArrayList<>();
                    while (resultSet.next())
                    {
                        Sms sms = new Sms();
                        sms.id = resultSet.getInt(1);
                        sms.content = resultSet.getString(2);
                        sms.count = resultSet.getInt(3);
                        sms.createTime = resultSet.getTimestamp(4);
                        sms.sendType = EnumParser.parse(SendType.class, resultSet.getString(5));
                        sms.name = resultSet.getString(6);
                        smses.add(sms);
                    }
                    return smses.toArray(new Sms[smses.size()]);
                }
            }, paging, sb.toString(), parameters);
        }
    }

	@Override
    public Sms get(int id)
        throws Throwable
    {
        if (id <= 0)
        {
            throw new ParameterException("指定的短信推广ID不存在.");
        }
        String sql =
            "SELECT T7162.F01,T7162.F02,T7162.F03,T7162.F05,T7162.F06,T7110.F02 AS ACCOUNTNAME FROM S71.T7162 INNER JOIN S71.T7110 ON T7162.F04=T7110.F01 WHERE T7162.F01=?";
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<Sms>()
            {
                
                @Override
                public Sms parse(ResultSet resultSet)
                    throws SQLException
                {
                    Sms sms = new Sms();
                    if (resultSet.next())
                    {
                        sms.id = resultSet.getInt(1);
                        sms.content = resultSet.getString(2);
                        sms.count = resultSet.getInt(3);
                        sms.createTime = resultSet.getTimestamp(4);
                        sms.sendType = EnumParser.parse(SendType.class, resultSet.getString(5));
                        sms.name = resultSet.getString(6);
                        sms.mobiles = getSendMobile(sms.id);
                    }
                    return sms;
                }
            }, sql, id);
        }
    }

    private String[] getSendMobile(int id)
    {
        if (id <= 0)
        {
            throw new ParameterException("指定的短信推广ID不存在.");
        }
        String sql = "SELECT F03 FROM S71.T7163 WHERE F02=?";
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
	public String[] importMobile(InputStream inputStream, String charset)
			throws Throwable {
		if (inputStream == null) {
			throw new ParameterException("读取文件流不存在.");
		}
		if (StringHelper.isEmpty(charset)) {
			charset = "GBK";
		}
		List<String> mobiles = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(
				inputStream, charset))) {
			String line = "";
			while ((line = reader.readLine()) != null) {
				mobiles.add(line);
			}
		}
		return mobiles.toArray(new String[mobiles.size()]);
	}

	@Override
	public void export(OutputStream outputStream, String charset, int id)
			throws Throwable {
		if (outputStream == null) {
			throw new ParameterException("写入文件流不存在.");
		}
		String[] mobiles = getSendMobile(id);
		if (StringHelper.isEmpty(charset)) {
			charset = "GBK";
		}
		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				outputStream, charset))) {
			for (String account : mobiles) {
				if (StringHelper.isEmpty(account)) {
					continue;
				}
				writer.write(account);
				writer.newLine();
			}
		}
	}

	@Override
	public int getCheckUserId(String mobile) throws Throwable {
		String sql = "SELECT F01 FROM S61.T6110 WHERE F04=?";
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection.prepareStatement(sql)) {
				ps.setString(1, mobile);
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
