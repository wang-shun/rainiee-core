package com.dimeng.p2p.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.util.StringHelper;

/**
 * 发送邮件，往1046表插入需要发送的数据，区别于框架的发送方法，多插入一个用户id
 * @author zengzhihua
 *
 */
public class EmailSenderManageImplExt  extends AbstractP2PService implements EmailSenderManageExt {

	

	public EmailSenderManageImplExt(ServiceResource serviceResource) {
		super(serviceResource);
	}

	@Override
	public void send(int type, String subject, String content,
			String... addresses) throws Throwable {
		logger.info("调用重写的邮件send方法 开始");
		if ((StringHelper.isEmpty(subject)) || (StringHelper.isEmpty(content))
				|| (addresses == null) || (addresses.length <= 0)) {
			return;
		}
		int i = 0;
		int userId =0;
		try{
			userId = serviceResource.getSession().getAccountId();
		}catch(Exception e){
		}
		List<String> emails = new ArrayList<String>();
		for (String address : addresses) {
			++i;
			if ((i % 100 != 0) && (i != addresses.length)) {
				emails.add(address);
			} else {
				emails.add(address);
				if ((emails == null) || (emails.size() <= 0)) {
					continue;
				}
				try(Connection connection = getConnection())
				{
					try {
						long msgId = 0;
						try (PreparedStatement ps = connection
								.prepareStatement(
										"INSERT INTO S10._1046(F02,F03,F04,F05,F07,F08) VALUES(?,?,?,?,?,?)",
										1)) {
							ps.setString(1, subject);
							ps.setString(2, content);
							ps.setInt(3, type);
							ps.setTimestamp(4,
									new Timestamp(System.currentTimeMillis()));
							ps.setString(5, "W");
							ps.setInt(6, userId);
							ps.execute();
							ResultSet resultSet = ps.getGeneratedKeys();
							if (resultSet.next()) {
								msgId = resultSet.getLong(1);
							}
						}
						if (msgId > 0) {
							try (PreparedStatement ps = connection
									.prepareStatement("INSERT INTO S10._1047(F01,F02) VALUES(?,?)")) {
								for (String email : emails) {
									ps.setLong(1, msgId);
									ps.setString(2, email);
									ps.addBatch();
								}
								ps.executeBatch();
							}
						}
					} catch (Throwable e) {
						serviceResource.error("EmailSenderImplExt.send", e);
						throw new Exception("EmailSenderImplExt.send Exception");
					}
					emails.clear();
				}

			}
		}
	}
}
