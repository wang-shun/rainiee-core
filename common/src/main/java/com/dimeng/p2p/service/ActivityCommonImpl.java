package com.dimeng.p2p.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.config.Envionment;
import com.dimeng.framework.config.entity.VariableBean;
import com.dimeng.framework.resource.ResourceNotFoundException;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.OrderType;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.enums.T6103_F06;
import com.dimeng.p2p.S61.enums.T6103_F08;
import com.dimeng.p2p.S63.entities.T6342;
import com.dimeng.p2p.S63.enums.T6340_F03;
import com.dimeng.p2p.S63.enums.T6340_F04;
import com.dimeng.p2p.S63.enums.T6340_F08;
import com.dimeng.p2p.S63.enums.T6340_F09;
import com.dimeng.p2p.S63.enums.T6342_F04;
import com.dimeng.p2p.S63.enums.T6344_F09;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.common.SMSUtils;
import com.dimeng.p2p.common.entities.ActivityInfo;
import com.dimeng.p2p.variables.defines.LetterVariable;
import com.dimeng.p2p.variables.defines.MsgVariavle;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.p2p.variables.defines.smses.SmsVaribles;
import com.dimeng.util.Formater;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BooleanParser;
import com.dimeng.util.parser.DateParser;

/**
 * 
 * 活动管理
 * 1.注册送红包，加息券，体验金。生日投资。
 * 2.首次充值赠送,单笔充值赠送,推荐首次充值奖励。
 * 3.投资额度,推荐首次投资,推荐投资总额。
 * 4.生日登录。
 * 5.生日定时任务。
 * @version  [版本号, 2015年10月9日]
 */
public class ActivityCommonImpl extends AbstractP2PService implements ActivityCommon
{
    
    public ActivityCommonImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    /**
     * 赠送活动
     * 1.注册送红包，加息券，体验金。生日投资。
     * 2.首次充值赠送,单笔充值赠送,推荐首次充值奖励。
     * 3.投资额度,推荐首次投资,推荐投资总额。
     * @throws Throwable 
     * 
     */
    @Override
    public void sendActivity(Connection connection, int userId, String rewardType, String activityType,
        BigDecimal amount, int tjId)
        throws Throwable
    {
        boolean isFzrr = isFzrr(connection, userId);
        if (isFzrr)
        {
            //企业,机构用户不送活动
            logger.info("企业机构不参与红包加息券的赠送活动!");
            return;
        }
        
        List<ActivityInfo> activityInfos = getActivityByType(connection, rewardType, activityType);
        if (activityInfos != null && activityInfos.size() != 0)
        {
            
            //首次充值赠送红包加息券
            if (T6340_F04.firstrecharge.name().equalsIgnoreCase(activityType))
            {
                //判断用户是否为首次充值
                int chargeCount = selectChargeCount(T6501_F03.CG, OrderType.CHARGE.orderType(), userId, connection);
                if (chargeCount > 0)
                {
                    return;
                }
            }
            
            //推荐用户首次投资。
            if (T6340_F04.tjsctz.name().equalsIgnoreCase(activityType))
            {
                //判断用户是否为首次投资，首次投资判断是大于0。
                int chargeCount = selectBidCount(connection, T6501_F03.CG, OrderType.BID.orderType(), tjId);
                if (chargeCount > 0)
                {
                    return;
                }
            }
            
            //被推荐用户首次充值。
            if (T6340_F04.tjsccz.name().equalsIgnoreCase(activityType))
            {
                //判断用户是否为首次充值
                int chargeCount = selectChargeCount(T6501_F03.CG, OrderType.CHARGE.orderType(), tjId, connection);
                if (chargeCount > 0)
                {
                    return;
                }
            }
            
            //推荐用户投资总额
            if (T6340_F04.tjtzze.name().equalsIgnoreCase(activityType))
            {
                amount =
                    tjUserTzSum(tjId, connection, activityInfos.get(0).startTime, activityInfos.get(0).endTime).subtract(tjRedPacketSum(tjId,
                        connection,
                        activityInfos.get(0).startTime,
                        activityInfos.get(0).endTime))
                        .add(amount);
            }
            
            ActivityInfo activityInfo = null;
            
            //获取匹配的规则。
            if (T6340_F04.recharge.name().equals(activityType) || T6340_F04.firstrecharge.name().equals(activityType)
                || T6340_F04.tjsccz.name().equals(activityType) || T6340_F04.investlimit.name().equals(activityType)
                || T6340_F04.tjtzze.name().equals(activityType) || T6340_F04.tjsctz.name().equals(activityType))
            {
                //根据金额排序
                Collections.sort(activityInfos);
                for (ActivityInfo activity : activityInfos)
                {
                    if (amount.compareTo(activity.money) >= 0)
                    {
                        activityInfo = activity;
                        break;
                    }
                }
                
                //没有满足规则
                if (activityInfo == null)
                {
                    return;
                }
            }
            else
            {
                //注册送红包，加息券，体验金直接取活动规则
                activityInfo = activityInfos.get(0);
            }
            
            //生日,判断是否是投资活动
            if (T6340_F04.birthday.name().equals(activityType)
                && !T6340_F09.invest.name().equals(activityInfo.birthdayType.name()))
            {
                return;
            }
            
            //生日,判断是否是当天生日
            if (T6340_F04.birthday.name().equals(activityType))
            {
                boolean checkBirthday = checkBirthday(connection, userId, activityInfo);
                
                if (!checkBirthday)
                {
                    return;
                }
            }
            
            //推荐用户投资总额是否已送
            if (T6340_F04.tjtzze.name().equalsIgnoreCase(activityType))
            {
                int ysCount = ysCountByUser(userId, activityInfo.ruleId, tjId, connection);
                //用户已经送了
                if (ysCount > 0)
                {
                    return;
                }
            }
            
            //根据主键锁定行记录
            activityInfo = getActivityLock(connection, activityInfo);
            //是否送完
            boolean sendEnd = activityInfo.sendedNumber < activityInfo.sendAmount;
            if (sendEnd)
            {
                //注册送体验金
                if (T6340_F03.experience.name().equals(rewardType))
                {
                    activityInfo.activityType = T6340_F04.parse(activityType);
                    insertExperience(connection, userId, activityInfo);
                    //更新库存
                    updateActivitySend(connection, activityInfo.ruleId, 1);
                }
                else
                {
                    insertT6342(connection, userId, activityInfo, tjId);
                    //更新库存
                    updateActivitySend(connection, activityInfo.ruleId, 1);
                }
                sendNotice(connection,
                    userId,
                    Formater.formatAmount(activityInfo.amount),
                    T6340_F03.valueOf(rewardType).getChineseName() + "奖励",
                    T6340_F03.redpacket.name().equals(rewardType) ? LetterVariable.SEND_REDPACKET
                        : (T6340_F03.interest.name().equals(rewardType) ? LetterVariable.SEND_INTEREST
                            : LetterVariable.SEND_EXPERIENCE),
                    T6340_F03.redpacket.name().equals(rewardType) ? MsgVariavle.SEND_REDPACKET
                        : (T6340_F03.interest.name().equals(rewardType) ? MsgVariavle.SEND_INTEREST
                            : MsgVariavle.SEND_EXPERIENCE));
                logger.info(" 用户id：" + userId + ",活动名称《" + activityInfo.activityName + "》赠送成功。");
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
    *
    * 投资送活动。
    */
    @Override
    public void sendRedAndRest(BigDecimal amount, int userId, Connection connection)
        throws Throwable
    {
        //投资额度赠送
        sendActivity(connection, userId, T6340_F03.redpacket.name(), T6340_F04.investlimit.name(), amount, 0);
        sendActivity(connection, userId, T6340_F03.interest.name(), T6340_F04.investlimit.name(), amount, 0);
        sendActivity(connection, userId, T6340_F03.experience.name(), T6340_F04.investlimit.name(), amount, 0);
        
        //生日投资
        sendActivity(connection, userId, T6340_F03.redpacket.name(), T6340_F04.birthday.name(), amount, 0);
        sendActivity(connection, userId, T6340_F03.interest.name(), T6340_F04.birthday.name(), amount, 0);
        sendActivity(connection, userId, T6340_F03.experience.name(), T6340_F04.birthday.name(), amount, 0);
        
        // 邀请码
        String yqm = null;
        try (PreparedStatement ps = connection.prepareStatement("SELECT F03 FROM S61.T6111 WHERE T6111.F01 = ?"))
        {
            ps.setInt(1, userId);
            try (ResultSet resultSet = ps.executeQuery())
            {
                if (resultSet.next())
                {
                    yqm = resultSet.getString(1);
                }
            }
        }
        if (StringHelper.isEmpty(yqm))
        {
            return;
        }
        int tgyh = 0; // 推广用户
        try (PreparedStatement ps = connection.prepareStatement("select F01 from S61.T6111 where T6111.F02 = ?"))
        {
            ps.setString(1, yqm);
            try (ResultSet resultSet = ps.executeQuery())
            {
                if (resultSet.next())
                {
                    tgyh = resultSet.getInt(1);
                }
            }
        }
        if (tgyh <= 0)
        {
            return;
        }
        
        //推荐首次投资奖励
        sendActivity(connection, tgyh, T6340_F03.redpacket.name(), T6340_F04.tjsctz.name(), amount, userId);
        sendActivity(connection, tgyh, T6340_F03.interest.name(), T6340_F04.tjsctz.name(), amount, userId);
        sendActivity(connection, tgyh, T6340_F03.experience.name(), T6340_F04.tjsctz.name(), amount, userId);
        
        //推荐投资总额奖励
        sendActivity(connection, tgyh, T6340_F03.redpacket.name(), T6340_F04.tjtzze.name(), amount, userId);
        sendActivity(connection, tgyh, T6340_F03.interest.name(), T6340_F04.tjtzze.name(), amount, userId);
        sendActivity(connection, tgyh, T6340_F03.experience.name(), T6340_F04.tjtzze.name(), amount, userId);
    }
    
    /**
     * 赠送活动
     * 1.生日登录赠送红包，加息券（无connection）。
     * @throws Throwable 
     * 
     */
    @Override
    public void sendActivity(int userId, String rewardType, String activityType)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try
            {
                boolean isFzrr = isFzrr(connection, userId);
                //企业,机构用户登录不送活动
                if (isFzrr)
                {
                    logger.info("非自然人不参与红包加息券赠送活动！");
                    return;
                }
                serviceResource.openTransactions(connection);
                List<ActivityInfo> activityInfos = getActivityByType(connection, rewardType, activityType);
                if (activityInfos != null && activityInfos.size() != 0)
                {
                    //生日登录赠送红包，加息券。
                    ActivityInfo activityInfo = activityInfos.get(0);
                    //生日,判断是否是登录活动
                    if (!(T6340_F04.birthday.name().equals(activityType) && T6340_F09.login.name()
                        .equals(activityInfo.birthdayType.name())))
                    {
                        serviceResource.rollback(connection);
                        return;
                    }
                    
                    //生日,判断是否是当天生日
                    if (T6340_F04.birthday.name().equals(activityType))
                    {
                        boolean checkBirthday = checkBirthday(connection, userId, activityInfo);
                        
                        if (!checkBirthday)
                        {
                            serviceResource.rollback(connection);
                            return;
                        }
                    }
                    
                    //根据主键锁定行记录
                    activityInfo = getActivityLock(connection, activityInfo);
                    //是否送完
                    boolean sendEnd = activityInfo.sendedNumber < activityInfo.sendAmount;
                    if (sendEnd)
                    {
                        //体验金
                        if (T6340_F03.experience.name().equals(rewardType))
                        {
                            insertExperience(connection, userId, activityInfo);
                            //更新库存
                            updateActivitySend(connection, activityInfo.ruleId, 1);
                        }
                        else
                        {
                            insertT6342(connection, userId, activityInfo, 0);
                            //更新库存
                            updateActivitySend(connection, activityInfo.ruleId, 1);
                        }
                        
                        T6110 t6110 = selectT6110(connection, userId);
                        sendActivityMsg(connection, activityInfo, t6110);
                        logger.info(" 用户id：" + userId + ",活动名称《" + activityInfo.activityName + "》赠送成功。");
                    }
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
    
    /**
     * 赠送活动(定时任务)
     * 1.生日赠送红包，加息券（无connection）。
     * @throws Throwable 
     * 
     */
    @Override
    public void sendActivity()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try
            {
                logger.info("开始执行【生日用户赠送红包、加息券】任务，开始时间：" + new Date());
                
                List<T6110> t6110s = getBirthDayUsers(connection);
                if (t6110s != null && t6110s.size() <= 0)
                {
                    return;
                }
                serviceResource.openTransactions(connection);
                List<ActivityInfo> activityInfos = new ArrayList<ActivityInfo>();
                List<ActivityInfo> activityInfosRed =
                    getActivityByType(connection, T6340_F03.redpacket.name(), T6340_F04.birthday.name());
                List<ActivityInfo> activityInfosInt =
                    getActivityByType(connection, T6340_F03.interest.name(), T6340_F04.birthday.name());
                List<ActivityInfo> activityInfosExp =
                    getActivityByType(connection, T6340_F03.experience.name(), T6340_F04.birthday.name());
                activityInfos.addAll(activityInfosRed);
                activityInfos.addAll(activityInfosInt);
                activityInfos.addAll(activityInfosExp);
                if (activityInfos != null && activityInfos.size() != 0)
                {
                    for (ActivityInfo activityInfo : activityInfos)
                    {
                        
                        //根据主键锁定行记录
                        activityInfo = getActivityLock(connection, activityInfo);
                        
                        for (T6110 t6110 : t6110s)
                        {
                            int ysCount = ysCountByUser(t6110.F01, activityInfo.ruleId, connection, activityInfo);
                            //用户已经送了
                            if (ysCount > 0)
                            {
                                continue;
                            }
                            
                            //是否送完
                            boolean sendEnd = activityInfo.sendedNumber < activityInfo.sendAmount;
                            if (sendEnd)
                            {
                                //体验金
                                if (T6340_F03.experience.name().equals(activityInfo.rewardType))
                                {
                                    insertExperience(connection, t6110.F01, activityInfo);
                                    //更新库存
                                    updateActivitySend(connection, activityInfo.ruleId, 1);
                                }
                                else
                                {
                                    insertT6342(connection, t6110.F01, activityInfo, 0);
                                    //更新库存
                                    updateActivitySend(connection, activityInfo.ruleId, 1);
                                }
                                
                                logger.info(" 用户id：" + t6110.F01 + ",活动名称《" + activityInfo.activityName + "》赠送成功。");
                                
                                activityInfo.sendedNumber = getActivityInfo(connection, activityInfo.ruleId);
                                //给用户发短信
                                sendActivityMsg(connection, activityInfo, t6110);
                                /*sendNotice(connection,t6110.F01,activityInfo.amount.toString(),activityInfo.rewardType.getChineseName()+"奖励",
                                        T6340_F03.redpacket.name().equals(activityInfo.rewardType) ? LetterVariable.SEND_REDPACKET : LetterVariable.SEND_INTEREST,
                                        T6340_F03.redpacket.name().equals(activityInfo.rewardType) ? MsgVariavle.SEND_REDPACKET : MsgVariavle.SEND_INTEREST);*/
                            }
                            
                        }
                    }
                }
                logger.info("结束执行【生日用户赠送红包、加息券】任务，结束时间：" + new Date());
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    /**
     * 校验生日赠送
     * 
     * @param connection
     * @param userId
     * @param activityInfo
     * @return
     * @throws Throwable
     */
    private boolean checkBirthday(Connection connection, int userId, ActivityInfo activityInfo)
        throws Throwable
    {
        Date birth = getBirthdayDate(userId, connection);
        if (null == birth)
        {
            return false;
        }
        String birthday = birth.toString();
        String day = getCurrentDate(connection).toString();
        boolean isBirthday = birthday.substring(5, birthday.length()).equals(day.substring(5, day.length()));
        if (!isBirthday)
        {
            return false;
        }
        int ysCount = ysCountByUser(userId, activityInfo.ruleId, connection, activityInfo);
        //用户已经送了
        if (ysCount > 0)
        {
            return false;
        }
        return true;
    }
    
    /**
     * 发送短信
     * 
     * @param connection
     * @param activityInfo
     * @param t6110
     * @throws IOException
     * @throws Throwable
     */
    private void sendActivityMsg(Connection connection, ActivityInfo activityInfo, T6110 t6110)
        throws IOException, Throwable
    {
        ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
        Envionment envionment = configureProvider.createEnvionment();
        envionment.set("name", t6110.F02);
        envionment.set("actType", activityInfo.rewardType.getChineseName());
        String content = configureProvider.format(LetterVariable.SEND_ACTIVITY, envionment);
        sendLetter(connection, t6110.F01, "生日" + activityInfo.rewardType.getChineseName() + "奖励", content);
        String msgcontent = configureProvider.format(MsgVariavle.SEND_ACTIVITY, envionment);
        String isUseYtx = configureProvider.getProperty(SmsVaribles.SMS_IS_USE_YTX);
        if ("1".equals(isUseYtx))
        {
            SMSUtils smsUtils = new SMSUtils(configureProvider);
            int type = smsUtils.getTempleId(MsgVariavle.SEND_ACTIVITY.getDescription());
            sendMsg(connection, t6110.F04, smsUtils.getSendContent(t6110.F02, activityInfo.rewardType.name()), type);
            
        }
        else
        {
            sendMsg(connection, t6110.F04, msgcontent);
        }
        
    }
    
    /**
     * 赠送活动
     * 
     * @param connection
     * @param userId
     * @param activityInfo
     * @throws Throwable
     */
    private void insertT6342(Connection connection, int userId, ActivityInfo activityInfo, int tjId)
        throws Throwable
    {
        T6342 t6342 = new T6342();
        t6342.F02 = userId;
        t6342.F03 = activityInfo.ruleId;
        t6342.F04 = T6342_F04.WSY;
        t6342.F07 = getCurrentTimestamp(connection);
        t6342.F08 = getDeadline(activityInfo.expDay, activityInfo.isMonth, connection);
        t6342.F09 = tjId;
        //添加活动信息
        addT6342(t6342, connection);
    }
    
    /**
     * 查询活动对象已送数量
     * @return
     * @throws Throwable
     */
    private int getActivityInfo(Connection connection, int ruleId)
        throws Throwable
    {
        try (PreparedStatement pstm = connection.prepareStatement("SELECT F08 FROM S63.T6344 WHERE F01=?"))
        {
            pstm.setInt(1, ruleId);
            try (ResultSet resultSet = pstm.executeQuery())
            {
                if (resultSet.next())
                {
                    return resultSet.getInt(1);
                }
            }
        }
        return 0;
    }
    
    /**
     * 注册送体验金
     *
     * @param userId
     * @return
     */
    private int insertExperience(Connection connection, int userId, ActivityInfo activityInfo)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S61.T6103 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, "
                + "F06 = ?, F07 = ? ,F08 = ?, F09 = ? , F10 = ?, F12 = ?, F14 = ?,F16 = ?",
                PreparedStatement.RETURN_GENERATED_KEYS))
        {
            // 获取配置器
            ConfigureProvider configPro = serviceResource.getResource(ConfigureProvider.class);
            Timestamp time = getCurrentTimestamp(connection);
            pstmt.setInt(1, userId);
            pstmt.setBigDecimal(2, activityInfo.amount);
            pstmt.setTimestamp(3, time);
            pstmt.setTimestamp(4, getDeadline(activityInfo.expDay, activityInfo.isMonth, connection));
            pstmt.setString(5, T6103_F06.WSY.name());
            pstmt.setInt(6, activityInfo.validDate);
            pstmt.setString(7, T6103_F08.ZC.name());
            pstmt.setString(8, activityInfo.activityType.getChineseName());
            pstmt.setTimestamp(9, time);
            pstmt.setInt(10, getPTID(connection));
            pstmt.setInt(11, activityInfo.ruleId);
            pstmt.setString(12, activityInfo.validMethod);
            // 执行SQL
            pstmt.execute();
            // 获取自增ID
            try (ResultSet resultSet = pstmt.getGeneratedKeys())
            {
                if (resultSet.next())
                {
                    return resultSet.getInt(1);
                }
                else
                {
                    return 0;
                }
            }
        }
    }
    
    /**
    *
    * 更新已送数量
    */
    private void updateActivitySend(Connection connection, int ruleId, int count)
        throws Throwable
    {
        String sql = "UPDATE S63.T6344 SET F08 = F08 + ? WHERE F01 = ? ";
        try (PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setInt(1, count);
            ps.setInt(2, ruleId);
            ps.executeUpdate();
        }
    }
    
    /**
    *
    * 查询活动记录
    */
    private List<ActivityInfo> getActivityByType(Connection connection, String rewardType, String activityType)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT t6340.F01 AS F01,t6340.F02 AS F02,t6340.F05 AS F03,t6340.F06 AS F04,t6340.F07 AS F05,");
        sql.append(" t6340.F08 AS F06,t6340.F09 AS F07,t6344.F01 AS F8,t6344.F03 AS F9,t6344.F05 AS F10,t6344.F06 AS F11,t6344.F08 AS F12,t6344.F04 AS F13,t6344.F09 AS F14,");
        sql.append(" t6344.F10 AS F15,t6344.F11 AS F16, t6340.F04 AS F17");
        sql.append(" FROM S63.T6344 t6344 LEFT JOIN S63.T6340 t6340 ON t6344.F02 = t6340.F01");
        sql.append(" WHERE t6340.F08 = 'JXZ' AND DATE(t6340.F06) <= ? AND DATE(t6340.F07) >= ? AND");
        sql.append(" t6340.F03 = ? AND t6340.F04 = ? ");
        List<ActivityInfo> activityInfos = new ArrayList<ActivityInfo>();
        try (PreparedStatement ps = connection.prepareStatement(sql.toString()))
        {
            java.sql.Date currentDate = getCurrentDate(connection);
            ps.setDate(1, currentDate);
            ps.setDate(2, currentDate);
            ps.setString(3, rewardType);
            ps.setString(4, activityType);
            try (ResultSet rs = ps.executeQuery())
            {
                ActivityInfo activityInfo = null;
                while (rs.next())
                {
                    activityInfo = new ActivityInfo();
                    activityInfo.activityId = rs.getInt(1);
                    activityInfo.activityCode = rs.getString(2);
                    activityInfo.activityName = rs.getString(3);
                    activityInfo.startTime = rs.getTimestamp(4);
                    activityInfo.endTime = rs.getTimestamp(5);
                    activityInfo.activityState = T6340_F08.parse(rs.getString(6));
                    activityInfo.birthdayType = T6340_F09.parse(rs.getString(7));
                    activityInfo.ruleId = rs.getInt(8);
                    activityInfo.sendAmount = rs.getInt(9);
                    activityInfo.amount = rs.getBigDecimal(10);
                    activityInfo.money = rs.getBigDecimal(11);
                    activityInfo.sendedNumber = rs.getInt(12);
                    activityInfo.expDay = rs.getInt(13);
                    activityInfo.isMonth = T6344_F09.parse(rs.getString(14));
                    activityInfo.rewardType = T6340_F03.parse(rewardType);
                    activityInfo.validDate = rs.getInt(15);
                    activityInfo.validMethod = rs.getString(16);
                    activityInfo.activityType = T6340_F04.parse(rs.getString(17));
                    activityInfos.add(activityInfo);
                }
            }
        }
        return activityInfos;
    }
    
    /**
    *
    * 推荐用户累计投资
    */
    private BigDecimal tjUserTzSum(int userId, Connection connection, Timestamp start, Timestamp end)
        throws Throwable
    {
        String sql =
            "SELECT IFNULL(SUM(t6504.F04),0) FROM S65.T6504 t6504 LEFT JOIN S65.T6501 t6501 ON t6504.F01 = t6501.F01 WHERE t6504.F02 = ? AND t6501.F03 = ? AND DATE(t6501.F05) >= ? AND DATE(t6501.F05) <= ? ";
        BigDecimal sum = BigDecimal.ZERO;
        try (PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setInt(1, userId);
            ps.setString(2, T6501_F03.CG.name());
            ps.setTimestamp(3, start);
            ps.setTimestamp(4, end);
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    sum = rs.getBigDecimal(1);
                }
            }
        }
        return sum;
    }
    
    /**
    *
    * 根据主键锁定要修改数量的行
    */
    private ActivityInfo getActivityLock(Connection connection, ActivityInfo activityInfo)
        throws Throwable
    {
        String sql = "SELECT F03,F08 FROM S63.T6344 WHERE F01 = ? FOR UPDATE";
        try (PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setInt(1, activityInfo.ruleId);
            try (ResultSet rs = ps.executeQuery())
            {
                while (rs.next())
                {
                    activityInfo.sendAmount = rs.getInt(1);
                    activityInfo.sendedNumber = rs.getInt(2);
                }
            }
        }
        return activityInfo;
    }
    
    /**
     * 判断是否是企业或机构
     * @param connection
     * @param userId
     * @return
     */
    private boolean isFzrr(Connection connection, int userId)
        throws Throwable
    {
        String sql = "SELECT F01 FROM S61.T6110 WHERE F01 = ? AND F06 = 'FZRR' ";
        boolean isFzrr = false;
        try (PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    isFzrr = true;
                }
            }
        }
        return isFzrr;
    }
    
    /**
     * 获取过期时间
     * @return
     */
    private Timestamp getDeadline(int yxq, T6344_F09 isMonth, Connection connection)
        throws Throwable
    {
        Date times = getCurrentDate(connection);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(times);
        if (T6344_F09.S.name().equals(isMonth.name()))
        {
            calendar.add(Calendar.MONTH, yxq);
        }
        else
        {
            calendar.add(Calendar.DATE, yxq - 1);
        }
        return new Timestamp(calendar.getTime().getTime());
    }
    
    /**
    *
    * 添加用户红包和加息券
    */
    private void addT6342(T6342 t6342, Connection connection)
        throws Throwable
    {
        String sql = "INSERT INTO S63.T6342 SET F02=?,F03=?,F04=?,F07=?,F08=?,F09=?";
        try (PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setInt(1, t6342.F02);
            ps.setInt(2, t6342.F03);
            ps.setString(3, t6342.F04.name());
            ps.setTimestamp(4, t6342.F07);
            ps.setTimestamp(5, t6342.F08);
            ps.setInt(6, t6342.F09);
            ps.executeUpdate();
        }
    }
    
    /**
     * 查询用户名ID
     *
     * @throws java.sql.SQLException
     * @throws ResourceNotFoundException
     * @throws Throwable
     */
    private int getUserId(String userName, Connection connection)
        throws Throwable
    {
        try (PreparedStatement ps =
            connection.prepareStatement("SELECT T6110.F01 FROM S61.T6110 T6110 WHERE T6110.F02 like ?"))
        {
            ps.setString(1, "%" + userName + "%");
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }
    
    /**
     * 获取当天过生日的用户集合
     * @return
     * @throws Throwable
     */
    private List<T6110> getBirthDayUsers(Connection connection)
        throws Throwable
    {
        T6110 t6110 = null;
        List<T6110> list = new ArrayList<T6110>();
        try (PreparedStatement pstm =
            connection.prepareStatement("SELECT T6110.F01, T6110.F02, T6110.F04 from S61.T6141 INNER JOIN S61.T6110 ON T6141.F01 = T6110.F01 where T6110.F06='ZRR' AND DATE_FORMAT(T6141.F08,'%m%d')= DATE_FORMAT(CURRENT_DATE(),'%m%d')"))
        {
            try (ResultSet resultSet = pstm.executeQuery())
            {
                while (resultSet.next())
                {
                    t6110 = new T6110();
                    t6110.F01 = resultSet.getInt(1);
                    t6110.F02 = resultSet.getString(2);
                    t6110.F04 = resultSet.getString(3);
                    list.add(t6110);
                }
            }
        }
        return list;
    }
    
    /**
    *
    * 单个用户送了几次
    */
    private int ysCountByUser(int userId, int ruleId, Connection connection, ActivityInfo activityInfo)
        throws Throwable
    {
        String sql = "SELECT count(1) FROM S63.T6342 WHERE F02 = ? AND F03 = ? ";
        if (activityInfo.activityType == T6340_F04.birthday)
        {
            sql = "SELECT COUNT(1) FROM S61.T6103 WHERE F02 = ? AND F14 = ? ";
        }
        int ysCount = 0;
        try (PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setInt(1, userId);
            ps.setInt(2, ruleId);
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    ysCount = rs.getInt(1);
                }
            }
        }
        return ysCount;
    }
    
    /**
    *
    * 查询生日
    */
    private Date getBirthdayDate(int userId, Connection connection)
        throws Throwable
    {
        String sql = "SELECT F08 FROM S61.T6141 WHERE F01 = ? LIMIT 1";
        Date date = null;
        try (PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    date = new Date();
                    date = rs.getDate(1);
                }
            }
        }
        return date;
    }
    
    /**
     * 查询充值次数
     *
     * @param connection
     * @param F03
     * @param F02
     * @return
     * @throws SQLException
     */
    private int selectChargeCount(T6501_F03 F03, int F02, int F07, Connection connection)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT COUNT(T6501.F01) FROM S65.T6501 INNER JOIN S65.T6502 ON T6501.F01 = T6502.F01 WHERE T6501.F03 = ? AND T6501.F02 = ? AND T6502.F02 = ? "))
        {
            pstmt.setString(1, F03.name());
            pstmt.setInt(2, F02);
            pstmt.setInt(3, F07);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    return resultSet.getInt(1);
                }
            }
        }
        return 0;
    }
    
    /**
    *
    * 推荐投资单个用户送了几次
    */
    private int ysCountByUser(int userId, int activityId, int tjId, Connection connection)
        throws Throwable
    {
        String sql = "SELECT count(1) FROM S63.T6342 WHERE F02 = ? AND F03 = ? AND F09 = ?";
        int ysCount = 0;
        try (PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setInt(1, userId);
            ps.setInt(2, activityId);
            ps.setInt(3, tjId);
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    ysCount = rs.getInt(1);
                }
            }
        }
        return ysCount;
    }
    
    /**
    *
    * 推荐用户累计使用红包
    */
    private BigDecimal tjRedPacketSum(int userId, Connection connection, Timestamp start, Timestamp end)
        throws Throwable
    {
        String sql =
            "SELECT IFNULL(SUM(t6527.F04),0) FROM S65.T6527 t6527 LEFT JOIN S65.T6501 t6501 ON t6527.F01 = t6501.F01 WHERE t6527.F02 = ? AND t6501.F03 = ? AND DATE(t6501.F05) >= ? AND DATE(t6501.F05) <= ? ";
        BigDecimal sum = BigDecimal.ZERO;
        try (PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setInt(1, userId);
            ps.setString(2, T6501_F03.CG.name());
            ps.setTimestamp(3, start);
            ps.setTimestamp(4, end);
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    sum = rs.getBigDecimal(1);
                }
            }
        }
        return sum;
    }
    
    /**
     * 查询投资次数
     *
     * @param connection
     * @param F03
     * @param F02
     * @return
     * @throws SQLException
     */
    private int selectBidCount(Connection connection, T6501_F03 F03, int F02, int F07)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT COUNT(T6501.F01) FROM S65.T6501 INNER JOIN S65.T6504 ON T6501.F01 = T6504.F01 WHERE T6501.F03 = ? AND T6501.F02 = ? AND T6504.F02 = ? "))
        {
            pstmt.setString(1, F03.name());
            pstmt.setInt(2, F02);
            pstmt.setInt(3, F07);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    return resultSet.getInt(1);
                }
            }
        }
        return 0;
    }
}
