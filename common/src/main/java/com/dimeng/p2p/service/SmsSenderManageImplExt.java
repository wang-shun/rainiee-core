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
 * 发送短信，往1040表插入需要发送的数据，区别于框架的发送方法，多插入一个用户id
 * @author zengzhihua
 *
 */
public class SmsSenderManageImplExt  extends AbstractP2PService implements SmsSenderManageExt  {

    public SmsSenderManageImplExt(ServiceResource serviceResource) {
        super(serviceResource);
    }
    
    @Override
    public void send(int type, String message, String ip, String... receivers)
        throws Throwable
    {
        logger.info("调用重写的短信send方法 开始");
        if ((StringHelper.isEmpty(message)) || (receivers == null) || (receivers.length <= 0))
        {
            return;
        }
        int i = 0;
        List<String> phones = new ArrayList<String>();
        int userId = 0;
        try
        {
            userId = serviceResource.getSession().getAccountId();
        }
        catch (Exception e)
        {
        }
        
        for (String receiver : receivers)
        {
            ++i;
            if ((i % 100 != 0) && (i != receivers.length))
            {
                phones.add(receiver);
            }
            else
            {
                phones.add(receiver);
                if ((phones == null) || (phones.size() <= 0))
                {
                    continue;
                }
                try (Connection connection = getConnection())
                {
                    try
                    {
                        long msgId = 0;
                        try (PreparedStatement ps1040 =
                            connection.prepareStatement("INSERT INTO S10._1040(F02,F03,F04,F05,F07,F08) values(?,?,?,?,?,?)",
                                1))
                        {
                            ps1040.setInt(1, type);
                            ps1040.setString(2, message);
                            ps1040.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
                            ps1040.setString(4, "W");
                            ps1040.setInt(5, userId);
                            ps1040.setString(6, ip);
                            ps1040.execute();
                            try (ResultSet resultSet = ps1040.getGeneratedKeys())
                            {
                                if (resultSet.next())
                                {
                                    msgId = resultSet.getLong(1);
                                }
                                if (msgId > 0L)
                                {
                                    try (PreparedStatement ps1041 =
                                        connection.prepareStatement("INSERT INTO S10._1041(F01,F02) VALUES(?,?)"))
                                    {
                                        for (String phone : phones)
                                        {
                                            ps1041.setLong(1, msgId);
                                            ps1041.setString(2, phone);
                                            ps1041.addBatch();
                                        }
                                        ps1041.executeBatch();
                                    }
                                }
                            }
                        }
                    }
                    catch (Throwable e)
                    {
                        serviceResource.error("SmsSenderImplExt.send", e);
                        throw new Exception("SmsSenderImplExt.send Exception");
                    }
                    phones.clear();
                }
            }
        }
    }
}
