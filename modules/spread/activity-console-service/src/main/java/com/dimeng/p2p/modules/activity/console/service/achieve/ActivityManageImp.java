package com.dimeng.p2p.modules.activity.console.service.achieve;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.config.Envionment;
import com.dimeng.framework.config.entity.VariableBean;
import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.enums.T6103_F06;
import com.dimeng.p2p.S61.enums.T6103_F08;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S63.entities.T6340;
import com.dimeng.p2p.S63.entities.T6344;
import com.dimeng.p2p.S63.enums.T6340_F03;
import com.dimeng.p2p.S63.enums.T6340_F04;
import com.dimeng.p2p.S63.enums.T6340_F08;
import com.dimeng.p2p.S63.enums.T6340_F09;
import com.dimeng.p2p.S63.enums.T6342_F04;
import com.dimeng.p2p.S63.enums.T6344_F09;
import com.dimeng.p2p.common.SMSUtils;
import com.dimeng.p2p.modules.activity.console.service.ActivityManage;
import com.dimeng.p2p.modules.activity.console.service.entity.Activity;
import com.dimeng.p2p.modules.activity.console.service.entity.ActivityList;
import com.dimeng.p2p.modules.activity.console.service.entity.ActivityLog;
import com.dimeng.p2p.modules.activity.console.service.entity.ActivityQuery;
import com.dimeng.p2p.variables.defines.LetterVariable;
import com.dimeng.p2p.variables.defines.MsgVariavle;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.p2p.variables.defines.smses.SmsVaribles;
import com.dimeng.util.Formater;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BigDecimalParser;
import com.dimeng.util.parser.BooleanParser;
import com.dimeng.util.parser.DateParser;
import com.dimeng.util.parser.EnumParser;
import com.dimeng.util.parser.IntegerParser;

/**
 *
 * 活动管理
 *
 * @author heluzhu
 * @version [版本号, 2015年10月9日]
 */
public class ActivityManageImp extends AbstractActivityService implements ActivityManage
{
    
    public ActivityManageImp(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    public static class ActivityManageFactory implements ServiceFactory<ActivityManage>
    {
        @Override
        public ActivityManage newInstance(ServiceResource serviceResource)
        {
            return new ActivityManageImp(serviceResource);
        }
        
    }
    
    @Override
    public PagingResult<ActivityList> searchActivity(ActivityQuery query, Paging paging)
        throws Throwable
    {
        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT T6340.F01 AS F01, T6340.F02 AS F02, T6340.F03 AS F03, T6340.F04 AS F04, T6340.F05 AS F05, T6340.F06 AS F06, T6340.F07 AS F07, ");
        sql.append("(SELECT IFNULL(SUM(T6344.F03),0) FROM S63.T6344 WHERE T6344.F02 = T6340.F01) AS F08, ");
        sql.append("(SELECT IFNULL(SUM(T6344.F08),0) FROM S63.T6344 WHERE T6344.F02 = T6340.F01) AS F09, ");
        sql.append("(SELECT IFNULL(SUM(T6344.F05*T6344.F03), 0) FROM S63.T6344 WHERE T6344.F02 = T6340.F01 ) AS F10, ");
        sql.append("T6340.F08 AS F11, T6340.F12 AS F12, ");
        sql.append("(SELECT IFNULL(SUM(T6344.F05*T6344.F08), 0) FROM S63.T6344 WHERE T6344.F02 = T6340.F01) AS F13 ");
        sql.append("FROM S63.T6340 WHERE 1 = 1 ");
        ArrayList<Object> parameters = new ArrayList<>();
        if (query != null)
        {
            SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
            if (!StringHelper.isEmpty(query.code()))
            {
                sql.append(" AND S63.T6340.F02 like ? ");
                parameters.add(sqlConnectionProvider.allMatch(query.code()));
            }
            
            if (!StringHelper.isEmpty(query.name()))
            {
                sql.append(" AND S63.T6340.F05 like ? ");
                parameters.add(sqlConnectionProvider.allMatch(query.name()));
            }
            
            T6340_F03 jlType = query.jlType();
            if (jlType != null)
            {
                sql.append(" AND S63.T6340.F03 = ? ");
                parameters.add(jlType.name());
            }
            
            T6340_F04 hdType = query.hdType();
            if (hdType != null)
            {
                sql.append(" AND S63.T6340.F04 = ? ");
                parameters.add(hdType.name());
            }
            
            T6340_F08 status = query.status();
            if (status != null)
            {
                sql.append(" AND S63.T6340.F08 = ? ");
                parameters.add(status.name());
            }
            
            if (query.startsTime() != null)
            {
                sql.append(" AND DATE(S63.T6340.F06) >= ? ");
                parameters.add(query.startsTime());
            }
            
            if (query.starteTime() != null)
            {
                sql.append(" AND DATE(S63.T6340.F06) <= ? ");
                parameters.add(query.starteTime());
            }
            if (query.endsTime() != null)
            {
                sql.append(" AND DATE(S63.T6340.F07) >= ?");
                parameters.add(query.endsTime());
            }
            
            if (query.endeTime() != null)
            {
                sql.append(" AND DATE(S63.T6340.F07) <= ?");
                parameters.add(query.endeTime());
            }
            
        }
        sql.append(" ORDER BY T6340.F08 ASC,T6340.F11 DESC ");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<ActivityList>()
            {
                
                @Override
                public ActivityList[] parse(ResultSet rs)
                    throws SQLException
                {
                    ArrayList<ActivityList> list = new ArrayList<ActivityList>();
                    while (rs.next())
                    {
                        ActivityList info = new ActivityList();
                        info.F01 = rs.getInt(1);
                        info.F02 = rs.getString(2);
                        info.F03 = EnumParser.parse(T6340_F03.class, rs.getString(3));
                        info.F04 = EnumParser.parse(T6340_F04.class, rs.getString(4));
                        info.F05 = rs.getString(5);
                        info.F06 = rs.getTimestamp(6);
                        info.F07 = rs.getTimestamp(7);
                        info.F08 = rs.getInt(8);
                        info.F11 = rs.getInt(9);
                        info.F10 = rs.getBigDecimal(10);
                        info.F12 = EnumParser.parse(T6340_F08.class, rs.getString(11));
                        info.F13 = rs.getTimestamp(12);
                        info.F14 = rs.getBigDecimal(13);
                        list.add(info);
                    }
                    return list == null || list.size() == 0 ? null : list.toArray(new ActivityList[list.size()]);
                }
            }, paging, sql.toString(), parameters);
        }
    }
    
    @Override
    public void addActivity(Activity activity)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                Timestamp times = getCurrentTimestamp(connection);
                // 添加活动信息表T6340
                int t6340_id = insterT6340(activity, connection, times);
                // 添加活动规则信息表T6344
                int[] t6344_ids = insterT6344(activity, connection, t6340_id);
                if (T6340_F04.foruser.name().equals(activity.hdType()) && t6340_id > 0)
                {
                    String users[] = activity.userId().split("\\s+");
                    Set<Integer> userIds = new HashSet<Integer>();
                    for (String user : users)
                    {
                        userIds.add(getUserId(user, connection));
                    }
                    if (userIds == null || userIds.size() <= 0)
                    {
                        throw new ParameterException("用户不存在");
                    }
                    
                    Date date = getCurrentDate(connection);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    int syqx = IntegerParser.parse(activity.syqx()[0]); // 如果是“指定用户赠送，则只会存在一个使用期限”
                    if ("S".equals(activity.syqxType()[0]))
                    {
                        calendar.add(Calendar.MONTH, syqx);
                    }
                    else
                    {
                        calendar.add(Calendar.DATE, syqx - 1);
                    }
                    
                    if (T6340_F03.experience.name().equals(activity.jlType()))
                    {
                        // 体验金
                        String insertT6103 =
                            "INSERT INTO S61.T6103 SET F02 = ?,"
                                + " F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?, F12 = ?,F14 = ?,F16 = ?";
                        
                        // 获取配置器
                        ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
                        for (int userId : userIds)
                        {
                            insert(connection,
                                insertT6103,
                                userId,
                                activity.money()[0],
                                times,
                                new Timestamp(calendar.getTime().getTime()),
                                T6103_F06.WSY.name(),
                                activity.validDate(),
                                T6103_F08.PTZS.name(),
                                activity.remark(),
                                serviceResource.getSession().getAccountId(),
                                t6344_ids[0],
                                activity.validMethod());
                            sendNotice(connection,
                                userId,
                                Formater.formatAmount(BigDecimalParser.parse(activity.money()[0])),
                                "体验金奖励",
                                LetterVariable.SEND_EXPERIENCE,
                                MsgVariavle.SEND_EXPERIENCE);
                        }
                    }
                    else
                    {
                        String insertT6342 = "INSERT INTO S63.T6342 SET F02 = ?,F03 = ?,F04 = ?,F07 = ?,F08 = ?";
                        for (int userId : userIds)
                        {
                            insert(connection,
                                insertT6342,
                                userId,
                                t6344_ids[0],
                                T6342_F04.WSY.name(),
                                times,
                                new Timestamp(calendar.getTime().getTime()));
                            sendNotice(connection,
                                userId,
                                Formater.formatAmount(BigDecimalParser.parse(activity.money()[0])),
                                T6340_F03.valueOf(activity.jlType()).getChineseName() + "奖励",
                                T6340_F03.redpacket.name().equals(activity.jlType()) ? LetterVariable.SEND_REDPACKET
                                    : LetterVariable.SEND_INTEREST,
                                T6340_F03.redpacket.name().equals(activity.jlType()) ? MsgVariavle.SEND_REDPACKET
                                    : MsgVariavle.SEND_INTEREST);
                        }
                    }
                    
                    updateActivitySend(t6340_id, connection, userIds.size());
                }
                
                String addLogSql = "INSERT INTO S63.T6343 SET F02 = ?,F03 = ?,F04 = ?,F05 = ?";
                insert(connection, addLogSql, serviceResource.getSession().getAccountId(), t6340_id, times, "新增");
                if ("redpacket".equals(activity.jlType()))
                {
                    writeLog(connection, "操作日志", "新增活动(红包)");
                }
                else if ("interest".equals(activity.jlType()))
                {
                    writeLog(connection, "操作日志", "新增活动(加息券)");
                }
                else if ("experience".equals(activity.jlType()))
                {
                    writeLog(connection, "操作日志", "新增活动(体验金)");
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
    
    private void sendNotice(Connection connection, int userId, String amount, String title, VariableBean letterObj,
        VariableBean msgObj)
        throws Throwable
    {
        // 获取配置器
        ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
        String isUseYtx = configureProvider.getProperty(SmsVaribles.SMS_IS_USE_YTX);
        boolean isOpenSendAwardMsg =
            BooleanParser.parse(configureProvider.getProperty(SystemVariable.IS_OPEN_SEND_AWARD_MSG));
        String date_str = DateParser.format(new Date());
        // 发站内信
        T6110 t6110 = selectT6110(connection, userId);
        Envionment envionment = configureProvider.createEnvionment();
        envionment.set("name", t6110.F02);
        envionment.set("date", date_str);
        envionment.set("amount", amount);
        String content = configureProvider.format(letterObj, envionment);
        sendLetter(connection, userId, title, content);
        if (isOpenSendAwardMsg)
        {
            if ("1".equals(isUseYtx))
            {
                SMSUtils smsUtils = new SMSUtils(configureProvider);
                int type = smsUtils.getTempleId(msgObj.getDescription());
                sendMsg(connection, t6110.F04, title, type);
            }
            else
            {
                String msgContent = configureProvider.format(msgObj, envionment);
                sendMsg(connection, t6110.F04, msgContent);
            }
        }
    }
    
    /**
     * 添加活动信息表T6340
     *
     * @param activity
     * @param connection
     * @param times
     * @return
     * @throws Throwable
     */
    private int insterT6340(Activity activity, Connection connection, Timestamp times)
        throws Throwable
    {
        String insterT6340_sql =
            "INSERT INTO S63.T6340 SET F02=?,F03=?,F04=?,F05=?,F06=?,F07=?,F08=?,F09=?,F10=?,F11=?,F12=?";
        try (PreparedStatement ps = connection.prepareStatement(insterT6340_sql, Statement.RETURN_GENERATED_KEYS))
        {
            ps.setString(1, getCrid());
            ps.setString(2, activity.jlType());
            ps.setString(3, activity.hdType());
            ps.setString(4, activity.title());
            ps.setTimestamp(5, activity.startTime());
            ps.setTimestamp(6, activity.endTime());
            if (T6340_F04.foruser.name().equals(activity.hdType()))
            {
                ps.setString(7, T6340_F08.YXJ.name());
            }
            else
            {
                ps.setString(7, T6340_F08.DSJ.name());
            }
            ps.setString(8, activity.litj());
            ps.setString(9, activity.remark());
            ps.setTimestamp(10, times);
            ps.setTimestamp(11, times);
            ps.execute();
            int id = 0;
            try (ResultSet resultSet = ps.getGeneratedKeys())
            {
                if (resultSet.next())
                {
                    id = resultSet.getInt(1);
                }
            }
            return id;
        }
    }
    
    /**
     * 添加活动信息表T6344
     *
     * @param activity
     * @param connection
     * @return
     * @throws Throwable
     */
    private int[] insterT6344(Activity activity, Connection connection, int t6340_id)
        throws Throwable
    {
        String[] money = activity.money();
        int[] t6344_ids = new int[money.length];
        for (int i = 0; i < money.length; i++)
        {
            String insterT6340_sql = "INSERT INTO S63.T6344 SET F02=?,F03=?,F04=?,F05=?,F06=?,F07=?,F09=?,F10=?,F11=?";
            try (PreparedStatement ps = connection.prepareStatement(insterT6340_sql, Statement.RETURN_GENERATED_KEYS))
            {
                ps.setInt(1, t6340_id);
                ps.setInt(2, activity.num() == null ? 0 : IntegerParser.parse(activity.num()[i]));
                ps.setString(3, activity.syqx()[i]);
                ps.setBigDecimal(4, BigDecimalParser.parse(money[i]));

                if(T6340_F04.exchange.name().equals(activity.hdType()) || T6340_F04.integraldraw.name().equals(activity.hdType()))
                {
                    String rule = money[i]+activity.syqx()[i]+activity.leastInvest()[i];
                    ps.setBigDecimal(5, BigDecimalParser.parse(rule));
                }
                else
                {
                    ps.setBigDecimal(5,
                            activity.edu() == null ? BigDecimal.ZERO : BigDecimalParser.parse(activity.edu()[i]));
                }

                ps.setBigDecimal(6,
                        activity.leastInvest() == null ? BigDecimal.ZERO
                                : BigDecimalParser.parse(activity.leastInvest()[i]));
                ps.setString(7, activity.syqxType()[i]);
                ps.setString(8, activity.validDate());
                ps.setString(9, activity.validMethod());
                ps.execute();
                int id = 0;
                try (ResultSet resultSet = ps.getGeneratedKeys())
                {
                    if (resultSet.next())
                    {
                        id = resultSet.getInt(1);
                    }
                }
                t6344_ids[i] = id;
            }
        }
        return t6344_ids;
    }
    
    private int getUserId(String userName, Connection connection)
        throws Throwable
    {
        String sql = "SELECT F01 FROM S61.T6110 WHERE F02=? AND F06=? LIMIT 1";
        try (PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setString(1, userName);
            ps.setString(2, T6110_F06.ZRR.name());
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    return rs.getInt(1);
                }
            }
        }
        throw new ParameterException(userName + "用户不存在或者不符合赠送要求");
    }
    
    /**
     * 生成活动的编号
     *
     * @return
     * @throws Throwable
     */
    protected String getCrid()
        throws Throwable
    {
        String serNo = "H";
        try (Connection connection = getConnection())
        {
            
            try (PreparedStatement pstmt =
                connection.prepareStatement(" SELECT MAX(F02) FROM S63.T6340 WHERE date_format(T6340.F11,'%Y-%m')=date_format(CURDATE(),'%Y-%m')"))
            {
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        String no = resultSet.getString(1);
                        if (!StringHelper.isEmpty(no))
                        {
                            serNo +=
                                DateParser.format(new Date(), "yyMM")
                                    + String.format("%03d", Integer.parseInt(no.substring(no.length() - 3)) + 1);
                        }
                        else
                        {
                            serNo += DateParser.format(new Date(), "yyMM") + "001";
                        }
                    }
                }
            }
            
        }
        
        return serNo;
    }
    
    @Override
    public T6340 getActivity(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT T6340.F01 AS F01,T6340.F02 AS F02,T6340.F03 AS F03,T6340.F04 AS F04,T6340.F05 AS F05,T6340.F06 AS F06,T6340.F07 AS F07,T6340.F08 AS F08,T6340.F09 AS F09 ,T6340.F10 AS F10 ,T6340.F11 AS F11,T6340.F12 AS F12,T6340.F13 AS F13 FROM S63.T6340 WHERE T6340.F01 = ?"))
            {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery())
                {
                    T6340 t6340 = null;
                    if (rs.next())
                    {
                        t6340 = new T6340();
                        t6340.F01 = rs.getInt(1);
                        t6340.F02 = rs.getString(2);
                        t6340.F03 = EnumParser.parse(T6340_F03.class, rs.getString(3));
                        t6340.F04 = EnumParser.parse(T6340_F04.class, rs.getString(4));
                        t6340.F05 = rs.getString(5);
                        t6340.F06 = rs.getTimestamp(6);
                        t6340.F07 = rs.getTimestamp(7);
                        t6340.F08 = T6340_F08.parse(rs.getString(8));
                        t6340.F09 = T6340_F09.parse(rs.getString(9));
                        t6340.F10 = rs.getString(10);
                        t6340.F11 = rs.getTimestamp(11);
                        t6340.F12 = rs.getTimestamp(12);
                        t6340.F13 = rs.getString(13);
                        
                    }
                    return t6340;
                }
            }
        }
        
    }
    
    @Override
    public void updateActivity(int id, Activity activity)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                Timestamp times = getCurrentTimestamp(connection);
                try (PreparedStatement ps =
                    connection.prepareStatement("UPDATE S63.T6340 SET F05=?,F06=?,F07=?,F08=?,F09=?,F10=?,F12=? WHERE F01 = ?"))
                {
                    ps.setString(1, activity.title());
                    ps.setTimestamp(2, activity.startTime());
                    ps.setTimestamp(3, activity.endTime());
                    ps.setString(4, T6340_F08.DSJ.name());
                    ps.setString(5, activity.litj());
                    ps.setString(6, activity.remark());
                    ps.setTimestamp(7, times);
                    ps.setInt(8, id);
                    ps.executeUpdate();
                    
                }
                
                // 记录活动操作日志T6343
                String addLogSql = "INSERT INTO S63.T6343 SET F02 = ?,F03 = ?,F04 = ?,F05 = ?";
                insert(connection, addLogSql, serviceResource.getSession().getAccountId(), id, times, "修改");
                
                // 更新T6344规则信息表，先删除原有规则，再添加
                String delT6344Sql = "DELETE FROM S63.T6344 WHERE F02 = ?";
                execute(connection, delT6344Sql, id);
                
                // 添加T6344
                insterT6344(activity, connection, id);
                
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
    public void updateStatus(int id, String status)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                try (PreparedStatement ps =
                    connection.prepareStatement("UPDATE S63.T6340 SET F08= ?,F13= ? WHERE F01 = ?"))
                {
                    String reason = "";
                    String addLogSql = "INSERT INTO S63.T6343 SET F02 = ?,F03 = ?,F04 = ?,F05 = ?,F06 = ?";
                    if (status.equals(T6340_F08.JXZ.name()))
                    {
                        T6340 t6340 = getActivity(id);
                        String no = queryNo(t6340.F03.name(), t6340.F04.name(), connection);
                        if (!StringHelper.isEmpty(no))
                        {
                            reason = "同类型活动复盖";
                            updateOldAct(no, connection);
                            insert(connection,
                                addLogSql,
                                serviceResource.getSession().getAccountId(),
                                getId(connection, no),
                                getCurrentTimestamp(connection),
                                "下架",
                                reason);
                        }
                    }
                    String remark = "";
                    if (status.equals(T6340_F08.JXZ.name()))
                    {
                        remark = "上架";
                    }
                    else if (status.equals(T6340_F08.YSJ.name()))
                    {
                        remark = "预上架";
                    }
                    else if (status.equals(T6340_F08.YXJ.name()))
                    {
                        remark = "下架";
                        reason = "下架操作";
                    }
                    else if (status.equals(T6340_F08.YZF.name()))
                    {
                        remark = "作废";
                        reason = "作废操作";
                    }
                    
                    ps.setString(1, status);
                    ps.setString(2, reason);
                    ps.setInt(3, id);
                    ps.executeUpdate();
                    
                    insert(connection,
                        addLogSql,
                        serviceResource.getSession().getAccountId(),
                        id,
                        getCurrentTimestamp(connection),
                        remark,
                        reason);
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
    
    private void updateOldAct(String no, Connection connection)
        throws Throwable
    {
        try (PreparedStatement ps = connection.prepareStatement("UPDATE S63.T6340 SET F08 = ? WHERE F02 = ?"))
        {
            ps.setString(1, T6340_F08.YXJ.name());
            ps.setString(2, no);
            ps.executeUpdate();
        }
    }
    
    private int getId(Connection connection, String no)
        throws Throwable
    {
        try (PreparedStatement ps = connection.prepareStatement("SELECT F01 FROM S63.T6340 WHERE F02 = ?"))
        {
            ps.setString(1, no);
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    return rs.getInt(1);
                }
                return 0;
            }
        }
    }
    
    @Override
    public String[] importUsers(InputStream inputStream, String charset)
        throws Throwable
    {
        if (inputStream == null)
        {
            throw new ParameterException("读取文件流不存在.");
        }
        if (StringHelper.isEmpty(charset))
        {
            charset = "GBK";
        }
        List<String> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, charset)))
        {
            String line = "";
            while ((line = reader.readLine()) != null)
            {
                users.add(line);
            }
        }
        return users.toArray(new String[users.size()]);
    }
    
    @Override
    public ActivityLog[] activityLogs(int actId)
        throws Throwable
    {
        try (Connection connection = getConnection();
            PreparedStatement ps =
                connection.prepareStatement("SELECT T6343.F01 AS F01,T6343.F04 AS F02,T6343.F05 AS F03,T7110.F02 AS F04 FROM S63.T6343 LEFT JOIN S71.T7110 ON T6343.F02 = T7110.F01 WHERE T6343.F03 = ? ORDER BY T6343.F04 DESC"))
        {
            ps.setInt(1, actId);
            try (ResultSet rs = ps.executeQuery())
            {
                List<ActivityLog> list = null;
                while (rs.next())
                {
                    ActivityLog log = new ActivityLog();
                    log.F01 = rs.getInt(1);
                    log.F04 = rs.getTimestamp(2);
                    log.F05 = rs.getString(3);
                    log.F06 = rs.getString(4);
                    if (list == null)
                    {
                        list = new ArrayList<ActivityLog>();
                    }
                    list.add(log);
                }
                return list == null || list.size() == 0 ? null : list.toArray(new ActivityLog[list.size()]);
            }
        }
    }
    
    @Override
    public String queryExistNo(String jlType, String hdType)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            return queryNo(jlType, hdType, connection);
        }
    }
    
    private String queryNo(String jlType, String hdType, Connection connection)
        throws Throwable
    {
        try (PreparedStatement ps =
            connection.prepareStatement("SELECT T6340.F02 FROM S63.T6340 WHERE T6340.F03 = ? AND T6340.F04 = ? AND T6340.F08 = ? LIMIT 1 "))
        {
            ps.setString(1, jlType);
            ps.setString(2, hdType);
            ps.setString(3, T6340_F08.JXZ.name());
            String no = "";
            try (ResultSet rs = ps.executeQuery())
            {
                while (rs.next())
                {
                    no = rs.getString(1);
                    
                }
                return no;
            }
        }
    }
    
    @Override
    public String queryForUsers(int actId)
        throws Throwable
    {
        StringBuffer userNames = new StringBuffer("");
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT T6110.F02 FROM S63.T6342 LEFT JOIN S61.T6110 ON T6342.F02 = T6110.F01 WHERE T6342.F03 = (SELECT F01 FROM S63.T6344 WHERE T6344.F02 = ?) "))
            {
                ps.setInt(1, actId);
                
                try (ResultSet rs = ps.executeQuery())
                {
                    while (rs.next())
                    {
                        userNames.append(rs.getString(1) + ";");
                        
                    }
                    return userNames.toString();
                }
            }
        }
    }
    
    @Override
    public String queryExperienceForUsers(int actId)
        throws Throwable
    {
        StringBuffer userNames = new StringBuffer("");
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT T6110.F02 FROM S61.T6103 LEFT JOIN S61.T6110 ON T6103.F02 = T6110.F01 WHERE T6103.F14 = (SELECT F01 FROM S63.T6344 WHERE T6344.F02 = ?) "))
            {
                ps.setInt(1, actId);
                
                try (ResultSet rs = ps.executeQuery())
                {
                    while (rs.next())
                    {
                        userNames.append(rs.getString(1) + ";");
                        
                    }
                    return userNames.toString();
                }
            }
        }
    }
    
    @Override
    public T6344[] getT6344Arry(int actId)
        throws Throwable
    {
        try (Connection connection = getConnection();
            PreparedStatement ps =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11 FROM S63.T6344 WHERE F02 = ? ORDER BY F06"))
        {
            ps.setInt(1, actId);
            try (ResultSet rs = ps.executeQuery())
            {
                List<T6344> list = new ArrayList<T6344>();
                while (rs.next())
                {
                    T6344 t6344 = new T6344();
                    t6344.F01 = rs.getInt(1);
                    t6344.F02 = rs.getInt(2);
                    t6344.F03 = rs.getInt(3);
                    t6344.F04 = rs.getInt(4);
                    t6344.F05 = rs.getBigDecimal(5);
                    t6344.F06 = rs.getBigDecimal(6);
                    t6344.F07 = rs.getBigDecimal(7);
                    t6344.F08 = rs.getInt(8);
                    t6344.F09 = T6344_F09.parse(rs.getString(9));
                    t6344.F10 = rs.getInt(10);
                    t6344.F11 = rs.getString(11);
                    list.add(t6344);
                }
                return list.toArray(new T6344[list.size()]);
            }
        }
    }
    
    /**
     *
     * 指定用户更新已送数量
     */
    private void updateActivitySend(int activityId, Connection connection, int count)
        throws Throwable
    {
        String sql = "UPDATE S63.T6340 SET F13 = ? WHERE F01 = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setString(1, "指定用户自动下架");
            ps.setInt(2, activityId);
            ps.executeUpdate();
        }
        
        String updateT6344 = "UPDATE S63.T6344 SET F03 = ?, F08 = ? WHERE F02 = ?";
        try (PreparedStatement ps = connection.prepareStatement(updateT6344))
        {
            ps.setInt(1, count);
            ps.setInt(2, count);
            ps.setInt(3, activityId);
            ps.executeUpdate();
        }
    }
}
