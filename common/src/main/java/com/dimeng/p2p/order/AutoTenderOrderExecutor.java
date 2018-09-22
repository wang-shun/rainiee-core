package com.dimeng.p2p.order;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.config.Envionment;
import com.dimeng.framework.data.sql.SQLConnection;
import com.dimeng.framework.resource.Resource;
import com.dimeng.framework.resource.ResourceAnnotation;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.entities.T6102;
import com.dimeng.p2p.S61.entities.T6105;
import com.dimeng.p2p.S61.entities.T6106;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S61.enums.T6102_F10;
import com.dimeng.p2p.S61.enums.T6106_F05;
import com.dimeng.p2p.S62.entities.T6230;
import com.dimeng.p2p.S62.entities.T6231;
import com.dimeng.p2p.S62.entities.T6238;
import com.dimeng.p2p.S62.entities.T6250;
import com.dimeng.p2p.S62.entities.T6251;
import com.dimeng.p2p.S62.entities.T6252;
import com.dimeng.p2p.S62.enums.T6230_F10;
import com.dimeng.p2p.S62.enums.T6230_F11;
import com.dimeng.p2p.S62.enums.T6230_F12;
import com.dimeng.p2p.S62.enums.T6230_F13;
import com.dimeng.p2p.S62.enums.T6230_F14;
import com.dimeng.p2p.S62.enums.T6230_F15;
import com.dimeng.p2p.S62.enums.T6230_F16;
import com.dimeng.p2p.S62.enums.T6230_F17;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.S62.enums.T6231_F21;
import com.dimeng.p2p.S62.enums.T6250_F07;
import com.dimeng.p2p.S62.enums.T6250_F09;
import com.dimeng.p2p.S62.enums.T6251_F08;
import com.dimeng.p2p.S62.enums.T6252_F09;
import com.dimeng.p2p.S63.entities.T6356;
import com.dimeng.p2p.S63.enums.T6356_F03;
import com.dimeng.p2p.S63.enums.T6356_F04;
import com.dimeng.p2p.S65.entities.T6504;
import com.dimeng.p2p.S65.entities.T6518;
import com.dimeng.p2p.S65.entities.T6527;
import com.dimeng.p2p.S65.enums.T6504_F06;
import com.dimeng.p2p.common.SMSUtils;
import com.dimeng.p2p.service.ActivityCommon;
import com.dimeng.p2p.variables.defines.LetterVariable;
import com.dimeng.p2p.variables.defines.MallVariavle;
import com.dimeng.p2p.variables.defines.MsgVariavle;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.p2p.variables.defines.URLVariable;
import com.dimeng.p2p.variables.defines.smses.SmsVaribles;
import com.dimeng.util.DateHelper;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BooleanParser;
import com.dimeng.util.parser.DateTimeParser;
import com.dimeng.util.parser.IntegerParser;

/**
 * 自动投资执行器
 * <功能详细描述>
 * 
 * @author  YINKE
 * @version  [版本号, 2015年11月9日]
 */
@ResourceAnnotation
public class AutoTenderOrderExecutor extends AbstractOrderExecutor
{
    
    public AutoTenderOrderExecutor(ResourceProvider resourceProvider)
    {
        super(resourceProvider);
    }
    
    @Override
    public Class<? extends Resource> getIdentifiedType()
    {
        return AutoTenderOrderExecutor.class;
    }
    
    @Override
    protected void doConfirm(SQLConnection connection, int orderId, Map<String, String> params)
        throws Throwable
    {
        try
        {
            // 订单详情
            T6504 t6504 = selectT6504(connection, orderId);
            if (t6504 == null)
            {
                throw new LogicalException("订单明细记录不存在");
            }
            // 标的详情,锁定标
            T6230 t6230 = selectT6230(connection, t6504.F03);
            T6231 t6231 = selectT6231(connection, t6504.F03);
            if (t6230 == null || t6231 == null)
            {
                throw new LogicalException("标记录不存在");
            }
            ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
            boolean ajkt = BooleanParser.parse(configureProvider.getProperty(SystemVariable.BID_SFZJKT));
            if (!ajkt && t6504.F02 == t6230.F02)
            {
                throw new LogicalException("不可投本账号发的标");
            }
            //获取标的担保方ID（购买人不能购买自己担保的标）
            int assureId = selectT6236(connection, t6504.F03);
            if (t6504.F02 == assureId)
            {
                throw new LogicalException("不可投资自己担保的标");
            }
            if (t6230.F20 != T6230_F20.TBZ)
            {
                throw new LogicalException("不是投资中状态,不能投资");
            }
            if (t6504.F04.compareTo(t6230.F07) > 0)
            {
                if (params != null)
                {
                    resourceProvider.log(String.format("%s %s投资异常：投资金额大于可投金额，投资订单%s，冻结订单%s，冻结流水%s",
                        DateTimeParser.format(new java.util.Date()),
                        t6230.F01,
                        orderId,
                        params.get("freezeOrdId"),
                        params.get("freezeTrxId")));
                    
                }
                throw new LogicalException("投资金额大于可投金额");
            }
            Timestamp nowTime = getCurrentTimestamp(connection);
            // 校验最低起投金额
            // BigDecimal min =
            // BigDecimalParser.parse(configureProvider.getProperty(SystemVariable.MIN_BIDING_AMOUNT));
            BigDecimal min = t6231.F25;
            if (t6504.F04.compareTo(min) < 0)
            {
                throw new LogicalException("投资金额不能低于最低起投金额");
            }
            BigDecimal max = t6231.F26;
            if (t6504.F04.compareTo(max) > 0)
            {
                throw new LogicalException("投资金额不能大于最大投资金额");
            }
            t6230.F07 = t6230.F07.subtract(t6504.F04);
            if (t6230.F07.compareTo(BigDecimal.ZERO) > 0 && t6230.F07.compareTo(min) < 0)
            {
                throw new LogicalException("剩余可投金额不能低于最低起投金额");
            }
            int pid = getPTID(connection); // 平台用户id
            if (pid <= 0)
            {
                throw new LogicalException("平台账号不存在");
            }
            // 平台往来账户信息
            T6101 ptwlzh = selectT6101(connection, pid, T6101_F03.WLZH, true);
            if (ptwlzh == null)
            {
                throw new LogicalException("平台往来账户不存在");
            }
            if (params != null)
            {
                String trxId = params.get("trxId");
                int freezeOrdId = IntegerParser.parse(params.get("freezeOrdId"));
                String freezeTrxId = params.get("freezeTrxId");
                // 更新流水号
                updateT6501(connection, trxId, t6504.F01);
                if (freezeOrdId > 0)
                {
                    updateT6501(connection, freezeTrxId, freezeOrdId);
                }
            }
            // 扣减可投金额
            try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S62.T6230 SET F07 = ? WHERE F01 = ?"))
            {
                pstmt.setBigDecimal(1, t6230.F07);
                pstmt.setInt(2, t6504.F03);
                pstmt.execute();
            }
            // 锁定投资人资金账户
            T6101 czzh = selectT6101(connection, t6504.F02, T6101_F03.WLZH, true);
            if (czzh == null)
            {
                throw new LogicalException("投资人往来账户不存在");
            }
            // 锁定入账账户
            T6101 rzzh = null;
            String msg = null;
            int feeCode = 0;
            if (t6230.F15 == T6230_F15.S)
            {
                rzzh = selectT6101(connection, t6230.F02, T6101_F03.WLZH, true);
                msg = "借款人账户不存在";
                feeCode = FeeCode.JK;
            }
            else
            {
                rzzh = selectT6101(connection, t6504.F02, T6101_F03.SDZH, true);
                msg = "投资人锁定账户不存在";
                feeCode = FeeCode.TZ;
            }
            if (rzzh == null)
            {
                throw new LogicalException(msg);
            }
            BigDecimal sjAmount = t6504.F04;
            {
                // 扣减出账账户金额
                czzh.F06 = czzh.F06.subtract(sjAmount);
                if (czzh.F06.compareTo(BigDecimal.ZERO) < 0)
                {
                    throw new LogicalException("账户金额不足");
                }
                updateT6101(connection, czzh.F06, czzh.F01);
                // 资金流水
                T6102 t6102 = new T6102();
                t6102.F02 = czzh.F01;
                t6102.F03 = FeeCode.TZ;
                t6102.F04 = rzzh.F01;
                t6102.F07 = sjAmount;
                t6102.F08 = czzh.F06;
                t6102.F09 = String.format("散标投资:%s，标题：%s", t6230.F25, t6230.F03);
                insertT6102(connection, t6102);
            }
            {
                // 增加入账账户金额
                rzzh.F06 = rzzh.F06.add(sjAmount);
                updateT6101(connection, rzzh.F06, rzzh.F01);
                T6102 t6102 = new T6102();
                t6102.F02 = rzzh.F01;
                t6102.F03 = feeCode;
                t6102.F04 = czzh.F01;
                t6102.F06 = sjAmount;
                t6102.F08 = rzzh.F06;
                t6102.F09 = String.format("散标投资:%s，标题：%s", t6230.F25, t6230.F03);
                insertT6102(connection, t6102);
            }
            
            // 插入投资记录
            T6250 t6250 = new T6250();
            t6250.F02 = t6230.F01;
            t6250.F03 = t6504.F02;
            t6250.F04 = t6504.F04;
            t6250.F01 = t6504.F05;
            t6250.F09 = t6504.F06 == null ? T6250_F09.F : T6250_F09.parse(t6504.F06.name());
            // 判断计息金额与标总金额是否一致
            if (t6230.F05.compareTo(t6230.F26) == 0)
            {
                t6250.F05 = t6504.F04;
            }
            else
            {
                t6250.F05 = t6230.F26.multiply(t6504.F04).divide(t6230.F05, DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
            }
            t6250.F07 = T6250_F07.F;
            int rid = insertT6250(connection, t6250);
            try (PreparedStatement ps = connection.prepareStatement("UPDATE S65.T6504 SET F05 = ? WHERE F01 = ?"))
            {
                ps.setInt(1, rid);
                ps.setInt(2, t6504.F01);
                ps.executeUpdate();
            }
            if (t6230.F15 == T6230_F15.S)
            {
                // 自动放款
                Date currentDate = getCurrentDate(connection);
                T6251 t6251 = new T6251();
                {
                    // 插入债权
                    t6251.F02 = zqCode(rid);
                    t6251.F03 = t6230.F01;
                    t6251.F04 = t6504.F02;
                    t6251.F05 = t6250.F04;
                    t6251.F06 = t6250.F04;
                    t6251.F07 = t6250.F04;
                    t6251.F08 = T6251_F08.F;
                    t6251.F09 = currentDate;
                    t6251.F10 = new Date(currentDate.getTime() + t6230.F19 * DateHelper.DAY_IN_MILLISECONDS);
                    t6251.F11 = rid;
                    t6251.F01 = insertT6251(connection, t6251);
                }
                // 生成还款计划
                hkjh(connection, t6230, t6251);
                // 收成交服务费
                T6238 t6238 = selectT6238(connection, t6230.F01);
                if (t6238 != null && t6238.F02.compareTo(BigDecimal.ZERO) > 0)
                {
                    BigDecimal fwf = t6238.F02.multiply(t6504.F04);
                    // updateT6101(connection, wlzh.F06, wlzh.F01);
                    {
                        // 平台资金增加
                        ptwlzh.F06 = ptwlzh.F06.add(fwf);
                        updateT6101(connection, ptwlzh.F06, ptwlzh.F01);
                        T6102 t6102 = new T6102();
                        t6102.F02 = ptwlzh.F01;
                        t6102.F03 = FeeCode.CJFWF;
                        t6102.F04 = rzzh.F01;
                        t6102.F06 = fwf;
                        t6102.F08 = ptwlzh.F06;
                        t6102.F09 = String.format("散标成交服务费:%s", t6230.F25);
                        insertT6102(connection, t6102);
                    }
                    {
                        // 借款人账户减少
                        rzzh.F06 = rzzh.F06.subtract(fwf);
                        updateT6101(connection, rzzh.F06, rzzh.F01);
                        T6102 t6102 = new T6102();
                        t6102.F02 = rzzh.F01;
                        t6102.F03 = FeeCode.CJFWF;
                        t6102.F04 = ptwlzh.F01;
                        t6102.F07 = fwf;
                        t6102.F08 = rzzh.F06;
                        t6102.F09 = String.format("散标成交服务费:%s", t6230.F25);
                        insertT6102(connection, t6102);
                    }
                }
                try (PreparedStatement pstmt =
                    connection.prepareStatement("UPDATE S62.T6231 SET F12 = ? WHERE F01 = ?"))
                {
                    pstmt.setTimestamp(1, nowTime);
                    pstmt.setInt(2, t6230.F01);
                    pstmt.execute();
                }
                if (t6230.F07.compareTo(BigDecimal.ZERO) <= 0)
                {
                    // 满标
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("UPDATE S62.T6231 SET F11 = ? WHERE F01 = ?"))
                    {
                        pstmt.setTimestamp(1, nowTime);
                        pstmt.setInt(2, t6230.F01);
                        pstmt.execute();
                    }
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("UPDATE S62.T6230 SET F20 = ? WHERE F01 = ?"))
                    {
                        pstmt.setString(1, T6230_F20.HKZ.name());
                        pstmt.setInt(2, t6230.F01);
                        pstmt.execute();
                    }
                    //筹款成功(满标)，发站内信
                    sendLetterForLoanFilled(configureProvider, connection, t6230);
                }
            }
            else
            {
                if (t6230.F07.compareTo(BigDecimal.ZERO) <= 0)
                {
                    // 满标
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("UPDATE S62.T6231 SET F11 = ? WHERE F01 = ?"))
                    {
                        pstmt.setTimestamp(1, nowTime);
                        pstmt.setInt(2, t6230.F01);
                        pstmt.execute();
                    }
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("UPDATE S62.T6230 SET F20 = ? WHERE F01 = ?"))
                    {
                        pstmt.setString(1, T6230_F20.DFK.name());
                        pstmt.setInt(2, t6230.F01);
                        pstmt.execute();
                    }
                    //筹款成功(满标)，发站内信
                    sendLetterForLoanFilled(configureProvider, connection, t6230);
                }
            }
            // 发站内信
            T6110 t6110 = selectT6110(connection, t6504.F02);
            Envionment envionment = configureProvider.createEnvionment();
            envionment.set("title", t6230.F03);
            envionment.set("money", t6504.F04.toString());
            String content = configureProvider.format(LetterVariable.TZR_TBCG, envionment);
            sendLetter(connection, t6504.F02, "投资成功", content);
            
            String isUseYtx = configureProvider.getProperty(SmsVaribles.SMS_IS_USE_YTX);
            if ("1".equals(isUseYtx))
            {
                SMSUtils smsUtils = new SMSUtils(configureProvider);
                int type = smsUtils.getTempleId(MsgVariavle.TZR_TBCG.getDescription());
                sendMsg(connection, t6110.F04, t6230.F03, type);
            }
            else
            {
                String msgContent = configureProvider.format(MsgVariavle.TZR_TBCG, envionment);
                sendMsg(connection, t6110.F04, msgContent);
            }
            
            try (ServiceSession serviceSession =
                resourceProvider.getResource(ServiceProvider.class).createServiceSession())
            {
                // 送红包和加息券
                ActivityCommon activityCommon = serviceSession.getService(ActivityCommon.class);
                activityCommon.sendRedAndRest(sjAmount, t6504.F02, connection);
            }
            
            boolean is_mall = Boolean.parseBoolean(configureProvider.getProperty(MallVariavle.IS_MALL));
            if (is_mall)
            {
                //投资赠送积分
                giveScore(connection, t6504.F02, t6504.F04, T6106_F05.invest, nowTime, null);
                //邀请有效投资用户赠送积分
                giveScore(connection, t6504.F02, t6504.F04, T6106_F05.invite, nowTime, configureProvider);
            }
            
            // 托管调用接口
            callFace(connection, orderId, params);
        }
        catch (Exception e)
        {
            //  托管调用接口
            rollbackFace(connection, orderId, params);
            logger.error(e, e);
            throw e;
        }
    }

    /**
     * 查询担保机构
     * @param connection
     * @param F02
     * @return
     * @throws SQLException
     */
    private int selectT6236(Connection connection, int F02)
            throws SQLException
    {
        int F03 = 0;
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT F03 FROM S62.T6236 WHERE T6236.F02 = ?"))
        {
            pstmt.setInt(1, F02);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    F03 = resultSet.getInt(1);
                }
            }
        }
        return F03;
    }
    
    /**
     * 筹款成功(满标)，发站内信 <功能详细描述>
     * 
     * @param configureProvider
     * @param Connection
     * @param T6230
     * @throws Throwable
     */
    private void sendLetterForLoanFilled(ConfigureProvider configureProvider, Connection connection, T6230 t6230)
        throws Throwable
    {
        
        Envionment envionment = configureProvider.createEnvionment();
        envionment.set("datetime", DateTimeParser.format(t6230.F22));
        envionment.set("title", t6230.F03);
        envionment.set("lookUrl", configureProvider.format(URLVariable.USER_JKSQCX));
        String content = configureProvider.format(LetterVariable.LOAN_FILLED, envionment);
        sendLetter(connection, t6230.F02, "筹款成功(满标)", content);
    }
    
    protected void handleError(Connection connection, int orderId, Throwable throwable)
    {
        
    }
    
    protected T6238 selectT6238(Connection connection, int F01)
        throws SQLException
    {
        T6238 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04 FROM S62.T6238 WHERE T6238.F01 = ? LIMIT 1"))
        {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6238();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getBigDecimal(2);
                    record.F03 = resultSet.getBigDecimal(3);
                    record.F04 = resultSet.getBigDecimal(4);
                }
            }
        }
        return record;
    }
    
    private String zqCode(int recordId)
    {
        DecimalFormat decimalFormat = new DecimalFormat("0000000000");
        return ("Z" + decimalFormat.format(recordId));
    }
    
    private void hkjh(Connection connection, T6230 t6230, T6251 t6251)
        throws Throwable
    {
        final Date currentDate = getCurrentDate(connection); // 数据库当前日期
        // 起息日
        final Date interestDate = new Date(currentDate.getTime() + Long.valueOf(t6230.F19) * 86400000);
        // final Date endDate = new Date(DateHelper.rollMonth(
        // interestDate.getTime(), t6230.F09));
        Date endDate = new Date(DateHelper.rollMonth(t6230.F22.getTime(), t6230.F09));
        
        // 首期还款日期
        Date backMoneyDate = null;
        // 总期数
        int totalTerm = 0;
        if (t6230.F17 == T6230_F17.ZRY)
        {// 自然月
        
            // 首期还款日期
            backMoneyDate = new Date(t6230.F22.getTime());
            // 总期数
            totalTerm = t6230.F09;
            switch (t6230.F10)
            {
                case DEBX:
                {
                    // insertT6252s(connection, calZRY_DEBX(connection, t6230,
                    // t6251, interestDate, endDate));
                    insertT6252s(connection,
                        calculate_DEBX(connection, t6230, t6251, interestDate, endDate, backMoneyDate, totalTerm));
                    break;
                }
                case MYFX:
                {
                    // insertT6252s(connection, calZRY_MYFX(connection, t6230,
                    // t6251, interestDate, endDate));
                    insertT6252s(connection,
                        calculate_MYFX(connection, t6230, t6251, interestDate, endDate, backMoneyDate, totalTerm));
                    break;
                }
                case YCFQ:
                {
                    // insertT6252s(connection, calYCFQ(connection, t6230, t6251,
                    // interestDate, endDate));
                    insertT6252s(connection,
                        calculate_YCFQ(connection, t6230, t6251, interestDate, endDate, backMoneyDate, totalTerm));
                    break;
                }
                case DEBJ:
                {
                    // insertT6252s(connection, calZRY_DEBJ(connection, t6230,
                    // t6251, interestDate, endDate));
                    insertT6252s(connection,
                        calculate_DEBJ(connection, t6230, t6251, interestDate, endDate, backMoneyDate, totalTerm));
                    break;
                }
                default:
                    throw new LogicalException("不支持的还款方式");
            }
        }
        else if (t6230.F17 == T6230_F17.GDR)
        {// 固定付息日
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date(t6230.F22.getTime()));
            System.out.println(cal.get(Calendar.DAY_OF_MONTH) + "=======GDR=====" + t6230.F18);
            if (cal.get(Calendar.DAY_OF_MONTH) <= t6230.F18)
            {
                totalTerm = t6230.F09;
            }
            else
            {
                totalTerm = t6230.F09 + 1;
            }
            if (cal.get(Calendar.DAY_OF_MONTH) <= t6230.F18)
            {
                cal.set(Calendar.DAY_OF_MONTH, t6230.F18);
            }
            else
            {
                // cal.setTime(new
                // Date(DateHelper.rollMonth(t6230.F22.getTime(), 1)));
                cal.set(Calendar.DAY_OF_MONTH, t6230.F18);
            }
            
            backMoneyDate = new Date(cal.getTimeInMillis());
            switch (t6230.F10)
            {
                case DEBX:
                {
                    // insertT6252s(connection, calGDR_DEBX(connection, t6230,
                    // t6251, interestDate, endDate));
                    insertT6252s(connection,
                        calculate_DEBX(connection, t6230, t6251, interestDate, endDate, backMoneyDate, totalTerm));
                    break;
                }
                case MYFX:
                {
                    // insertT6252s(connection, calGDR_MYFX(connection, t6230,
                    // t6251, interestDate, endDate));
                    insertT6252s(connection,
                        calculate_MYFX(connection, t6230, t6251, interestDate, endDate, backMoneyDate, totalTerm));
                    break;
                }
                case YCFQ:
                {
                    // insertT6252s(connection, calYCFQ(connection, t6230, t6251,
                    // interestDate, endDate));
                    insertT6252s(connection,
                        calculate_YCFQ(connection, t6230, t6251, interestDate, endDate, backMoneyDate, totalTerm));
                    break;
                }
                case DEBJ:
                {
                    // insertT6252s(connection, calGDR_DEBJ(connection, t6230,
                    // t6251, interestDate, endDate));
                    insertT6252s(connection,
                        calculate_DEBJ(connection, t6230, t6251, interestDate, endDate, backMoneyDate, totalTerm));
                    break;
                }
                default:
                    throw new LogicalException("不支持的还款方式");
            }
        }
        else
        {
            throw new LogicalException("不支持的付息方式");
        }
    }
    
    @SuppressWarnings("unused")
    private ArrayList<T6252> calGDR_MYFX(Connection connection, T6230 t6230, T6251 t6251, final Date interestDate,
        final Date endDate)
        throws Throwable
    {
        Calendar calendar = Calendar.getInstance();
        final int inserestStartDay;// 起息日
        {
            calendar.setTime(interestDate);
            inserestStartDay = calendar.get(Calendar.DAY_OF_MONTH);
        }
        if (t6230.F18 > 28)
        {
            throw new LogicalException("固定付息日不能大于28");
        }
        if (inserestStartDay == t6230.F18)
        {
            // 自然月还款
            return calZRY_MYFX(connection, t6230, t6251, interestDate, endDate);
        }
        final int term = t6230.F09 + 1;// 总期数
        ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        BigDecimal yearDays =
            new BigDecimal(IntegerParser.parse(configureProvider.getProperty(SystemVariable.REPAY_DAYS_OF_YEAR), 360));
        Date prePayDate = interestDate;// 上次付息日
        Date payDate;
        if (inserestStartDay < t6230.F18)
        {
            calendar.setTime(interestDate);
            calendar.set(Calendar.DAY_OF_MONTH, t6230.F18);
            payDate = new Date(calendar.getTimeInMillis());
        }
        else
        {
            calendar.setTime(interestDate);
            calendar.set(Calendar.DAY_OF_MONTH, t6230.F18);
            payDate = new Date(DateHelper.rollMonth(calendar.getTimeInMillis(), 1));
        }
        // 更新扩展信息
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S62.T6231 SET F02 = ?, F03 = ?, F06 = ? WHERE F01 = ?"))
        {
            pstmt.setInt(1, term);
            pstmt.setInt(2, term);
            pstmt.setDate(3, payDate);
            pstmt.setInt(4, t6230.F01);
            pstmt.execute();
        }
        // 生成还款记录
        ArrayList<T6252> t6252s = new ArrayList<>();
        for (int i = 1; i <= term; i++)
        {
            if (i == term)
            {
                payDate = endDate;
            }
            int days =
                (int)Math.ceil((double)(payDate.getTime() - prePayDate.getTime()) / DateHelper.DAY_IN_MILLISECONDS);
            if (1 == term)
            {
                updateT6231(connection, payDate, t6230.F01);
            }
            {
                // 利息
                T6252 t6252 = new T6252();
                t6252.F02 = t6230.F01;
                t6252.F03 = t6230.F02;
                t6252.F04 = t6251.F04;
                t6252.F05 = FeeCode.TZ_LX;
                t6252.F06 = i;
                t6252.F07 = t6251.F07.setScale(DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
                t6252.F07 =
                    t6252.F07.multiply(t6230.F06)
                        .multiply(new BigDecimal(days))
                        .divide(yearDays, DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
                t6252.F08 = payDate;
                t6252.F09 = T6252_F09.WH;
                t6252.F10 = null;
                t6252.F11 = t6251.F01;
                t6252s.add(t6252);
            }
            if (i == term)
            {
                // 本金
                T6252 t6252 = new T6252();
                t6252.F02 = t6230.F01;
                t6252.F03 = t6230.F02;
                t6252.F04 = t6251.F04;
                t6252.F05 = FeeCode.TZ_BJ;
                t6252.F06 = i;
                t6252.F07 = t6251.F07;
                t6252.F08 = payDate;
                t6252.F09 = T6252_F09.WH;
                t6252.F10 = null;
                t6252.F11 = t6251.F01;
                t6252s.add(t6252);
            }
            prePayDate = payDate;
            payDate = new Date(DateHelper.rollMonth(payDate.getTime(), 1));
        }
        updateT6231(connection, term, t6230.F01);
        return t6252s;
    }
    
    private void updateT6231(Connection connection, int F01, int F02)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S62.T6231 SET F02 = ?, F03 = ? WHERE F01 = ?"))
        {
            pstmt.setInt(1, F01);
            pstmt.setInt(2, F01);
            pstmt.setInt(3, F02);
            pstmt.execute();
        }
    }
    
    private void insertT6252s(Connection connection, List<T6252> entities)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S62.T6252 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?, F10 = ?, F11 = ?"))
        {
            for (T6252 entity : entities)
            {
                pstmt.setInt(1, entity.F02);
                pstmt.setInt(2, entity.F03);
                pstmt.setInt(3, entity.F04);
                pstmt.setInt(4, entity.F05);
                pstmt.setInt(5, entity.F06);
                pstmt.setBigDecimal(6, entity.F07);
                pstmt.setDate(7, entity.F08);
                pstmt.setString(8, entity.F09.name());
                pstmt.setTimestamp(9, entity.F10);
                pstmt.setInt(10, entity.F11);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }
    }
    
    // 标扩展信息
    protected T6231 selectT6231(Connection connection, int F01)
        throws SQLException
    {
        T6231 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F21, F22, F25, F26 FROM S62.T6231 WHERE T6231.F01 = ? LIMIT 1"))
        {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6231();
                    record.F21 = T6231_F21.parse(resultSet.getString(1));
                    record.F22 = resultSet.getInt(2);
                    record.F25 = resultSet.getBigDecimal(3);
                    record.F26 = resultSet.getBigDecimal(4);
                }
            }
        }
        return record;
    }
    
    @SuppressWarnings("unused")
    private ArrayList<T6252> calYCFQ(Connection connection, T6230 t6230, T6251 t6251, Date interestDate, Date endDate)
        throws Throwable
    {
        T6231 t6231 = selectT6231(connection, t6230.F01);
        if (t6231.F21 == T6231_F21.S)
        {
            Calendar date = Calendar.getInstance();
            date.setTime(endDate);
            date.add(Calendar.DAY_OF_MONTH, t6231.F22);
            endDate = new Date(date.getTimeInMillis());
        }
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S62.T6231 SET F02 = ?, F03 = ?, F06 = ? WHERE F01 = ?"))
        {
            pstmt.setInt(1, 1);
            pstmt.setInt(2, 1);
            pstmt.setDate(3, endDate);
            pstmt.setInt(4, t6230.F01);
            pstmt.execute();
        }
        ArrayList<T6252> t6252s = new ArrayList<>();
        {
            // 利息
            T6252 t6252 = new T6252();
            t6252.F02 = t6230.F01;
            t6252.F03 = t6230.F02;
            t6252.F04 = t6251.F04;
            t6252.F05 = FeeCode.TZ_LX;
            t6252.F06 = 1;
            t6252.F07 = t6251.F07.setScale(9, BigDecimal.ROUND_HALF_UP);
            t6252.F07 =
                t6252.F07.multiply(t6230.F06)
                    .multiply(new BigDecimal(t6230.F09))
                    .divide(new BigDecimal(12), DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
            t6252.F08 = endDate;
            t6252.F09 = T6252_F09.WH;
            t6252.F10 = null;
            t6252.F11 = t6251.F01;
            t6252s.add(t6252);
        }
        {
            // 本金
            T6252 t6252 = new T6252();
            t6252.F02 = t6230.F01;
            t6252.F03 = t6230.F02;
            t6252.F04 = t6251.F04;
            t6252.F05 = FeeCode.TZ_BJ;
            t6252.F06 = 1;
            t6252.F07 = t6251.F07;
            t6252.F08 = endDate;
            t6252.F09 = T6252_F09.WH;
            t6252.F10 = null;
            t6252.F11 = t6251.F01;
            t6252s.add(t6252);
        }
        return t6252s;
    }
    
    private ArrayList<T6252> calZRY_DEBJ(Connection connection, T6230 t6230, T6251 t6251, Date interestDate,
        Date endDate)
        throws Throwable
    {
        {
            Calendar c = Calendar.getInstance();
            c.setTime(interestDate);
            c.add(Calendar.MONTH, t6230.F09);
            endDate = new Date(c.getTimeInMillis());
        }
        // 借款金额
        BigDecimal x = t6251.F07;
        // 月利率
        BigDecimal y = t6230.F06.divide(new BigDecimal(12), DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
        // 借款期限
        int terms = t6230.F09;
        // 月还本金
        BigDecimal monthAmount = x.divide(new BigDecimal(terms), 2, BigDecimal.ROUND_HALF_UP);
        // 还本金总额
        BigDecimal total = BigDecimal.ZERO;
        ArrayList<T6252> t6252s = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(interestDate);
        for (int i = 1; i <= terms; i++)
        {
            calendar.add(Calendar.MONTH, 1);
            BigDecimal interest = BigDecimal.ZERO;
            if (1 == i)
            {
                updateT6231(connection, new Date(calendar.getTimeInMillis()), t6230.F01);
            }
            if (i == terms)
            {
                BigDecimal bj = x.subtract(total).setScale(2, BigDecimal.ROUND_HALF_UP);
                interest = bj.multiply(y).setScale(2, BigDecimal.ROUND_HALF_UP);
                { // 本金
                    T6252 t6252 = new T6252();
                    t6252.F02 = t6230.F01;
                    t6252.F03 = t6230.F02;
                    t6252.F04 = t6251.F04;
                    t6252.F05 = FeeCode.TZ_BJ;
                    t6252.F06 = i;
                    t6252.F07 = bj;
                    t6252.F08 = endDate;
                    t6252.F09 = T6252_F09.WH;
                    t6252.F11 = t6251.F01;
                    t6252s.add(t6252);
                }
                { // 利息
                    T6252 t6252 = new T6252();
                    t6252.F02 = t6230.F01;
                    t6252.F03 = t6230.F02;
                    t6252.F04 = t6251.F04;
                    t6252.F05 = FeeCode.TZ_LX;
                    t6252.F06 = i;
                    t6252.F07 = interest;
                    t6252.F08 = endDate;
                    t6252.F09 = T6252_F09.WH;
                    t6252.F11 = t6251.F01;
                    t6252s.add(t6252);
                }
                continue;
            }
            interest = x.subtract(total).multiply(y).setScale(2, BigDecimal.ROUND_HALF_UP);
            { // 本金
                T6252 t6252 = new T6252();
                t6252.F02 = t6230.F01;
                t6252.F03 = t6230.F02;
                t6252.F04 = t6251.F04;
                t6252.F05 = FeeCode.TZ_BJ;
                t6252.F06 = i;
                t6252.F07 = monthAmount;
                t6252.F08 = new Date(calendar.getTimeInMillis());
                t6252.F09 = T6252_F09.WH;
                t6252.F11 = t6251.F01;
                t6252s.add(t6252);
            }
            { // 利息
                T6252 t6252 = new T6252();
                t6252.F02 = t6230.F01;
                t6252.F03 = t6230.F02;
                t6252.F04 = t6251.F04;
                t6252.F05 = FeeCode.TZ_LX;
                t6252.F06 = i;
                t6252.F07 = interest;
                t6252.F08 = new Date(calendar.getTimeInMillis());
                t6252.F09 = T6252_F09.WH;
                t6252.F11 = t6251.F01;
                t6252s.add(t6252);
            }
            total = total.add(monthAmount);
        }
        return t6252s;
    }
    
    @SuppressWarnings("unused")
    private ArrayList<T6252> calGDR_DEBX(Connection connection, T6230 t6230, T6251 t6251, Date interestDate,
        Date endDate)
        throws Throwable
    {
        Calendar calendar = Calendar.getInstance();
        final int inserestStartDay;// 起息日
        {
            calendar.setTime(interestDate);
            inserestStartDay = calendar.get(Calendar.DAY_OF_MONTH);
        }
        {
            Calendar c = Calendar.getInstance();
            c.setTime(interestDate);
            c.add(Calendar.MONTH, t6230.F09);
            endDate = new Date(c.getTimeInMillis());
        }
        if (inserestStartDay == t6230.F18)
        {
            // 自然月还款
            return calZRY_DEBX(connection, t6230, t6251, interestDate, endDate);
        }
        if (t6230.F18 > 28)
        {
            throw new LogicalException("固定付息日不能大于28");
        }
        // 月利率
        BigDecimal x = t6230.F06.divide(new BigDecimal(12), DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
        // 借款金额
        BigDecimal y = t6251.F07;
        ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        BigDecimal yearDays =
            new BigDecimal(IntegerParser.parse(configureProvider.getProperty(SystemVariable.REPAY_DAYS_OF_YEAR), 360));
        // 日利率
        BigDecimal z = t6230.F06.divide(yearDays, DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
        // 借款期限
        int n = t6230.F09;
        // 首期天数
        int f = 0;
        // 首期付款日
        Calendar ca = Calendar.getInstance();
        if (inserestStartDay > t6230.F18)
        {
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.add(Calendar.MONTH, 1);
            calendar.set(Calendar.DAY_OF_MONTH, t6230.F18);
            ca.setTimeInMillis(calendar.getTimeInMillis());
        }
        else
        {
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.DAY_OF_MONTH, t6230.F18);
            ca.setTimeInMillis(calendar.getTimeInMillis());
        }
        f = (int)Math.floor((calendar.getTimeInMillis() - interestDate.getTime()) / DateHelper.DAY_IN_MILLISECONDS);
        // 尾期天数
        calendar.setTime(endDate);
        if (calendar.get(Calendar.DAY_OF_MONTH) > t6230.F18)
        {
            calendar.set(Calendar.DAY_OF_MONTH, t6230.F18);
        }
        else
        {
            calendar.add(Calendar.MONTH, -1);
            calendar.set(Calendar.DAY_OF_MONTH, t6230.F18);
        }
        int l = (int)((endDate.getTime() - calendar.getTimeInMillis()) / (24 * 3600 * 1000));
        int terms = t6230.F09 + 1;
        ArrayList<T6252> t6252s = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;
        // 月供
        BigDecimal amount =
            x.multiply(y)
                .multiply(x.add(new BigDecimal(1)).pow(n))
                .divide(x.add(new BigDecimal(1)).pow(n).subtract(new BigDecimal(1)),
                    DECIMAL_SCALE,
                    BigDecimal.ROUND_HALF_UP);
        for (int i = 1; i <= terms; i++)
        {
            // 利息
            BigDecimal interest = BigDecimal.ZERO;
            // 本金
            BigDecimal bj = BigDecimal.ZERO;
            if (1 == i)
            {
                
                BigDecimal fAmount =
                    amount.multiply(new BigDecimal(f)).divide(new BigDecimal(f).add(new BigDecimal(l)),
                        DECIMAL_SCALE,
                        BigDecimal.ROUND_HALF_UP);
                interest = y.multiply(z).multiply(new BigDecimal(f)).setScale(2, BigDecimal.ROUND_HALF_UP);
                bj = fAmount.subtract(interest).setScale(2, BigDecimal.ROUND_HALF_UP);
                total = total.add(bj);
                { // 本金
                    T6252 t6252 = new T6252();
                    t6252.F02 = t6230.F01;
                    t6252.F03 = t6230.F02;
                    t6252.F04 = t6251.F04;
                    t6252.F05 = FeeCode.TZ_BJ;
                    t6252.F06 = i;
                    t6252.F07 = bj;
                    t6252.F08 = new Date(ca.getTimeInMillis());
                    t6252.F09 = T6252_F09.WH;
                    t6252.F11 = t6251.F01;
                    t6252s.add(t6252);
                }
                { // 利息
                    T6252 t6252 = new T6252();
                    t6252.F02 = t6230.F01;
                    t6252.F03 = t6230.F02;
                    t6252.F04 = t6251.F04;
                    t6252.F05 = FeeCode.TZ_LX;
                    t6252.F06 = i;
                    t6252.F07 = interest;
                    t6252.F08 = new Date(ca.getTimeInMillis());
                    t6252.F09 = T6252_F09.WH;
                    t6252.F11 = t6251.F01;
                    t6252s.add(t6252);
                }
                updateT6231(connection, new Date(ca.getTimeInMillis()), t6230.F01);
                ca.add(Calendar.MONTH, 1);
                continue;
            }
            else if (i == terms)
            {
                BigDecimal lAmount =
                    amount.multiply(new BigDecimal(l)).divide(new BigDecimal(f).add(new BigDecimal(l)),
                        DECIMAL_SCALE,
                        BigDecimal.ROUND_HALF_UP);
                bj = y.subtract(total).setScale(2, BigDecimal.ROUND_HALF_UP);
                interest = lAmount.subtract(bj).setScale(2, BigDecimal.ROUND_HALF_UP);
                total = total.add(bj);
                { // 本金
                    T6252 t6252 = new T6252();
                    t6252.F02 = t6230.F01;
                    t6252.F03 = t6230.F02;
                    t6252.F04 = t6251.F04;
                    t6252.F05 = FeeCode.TZ_BJ;
                    t6252.F06 = i;
                    t6252.F07 = bj;
                    t6252.F08 = endDate;
                    t6252.F09 = T6252_F09.WH;
                    t6252.F11 = t6251.F01;
                    t6252s.add(t6252);
                }
                { // 利息
                    T6252 t6252 = new T6252();
                    t6252.F02 = t6230.F01;
                    t6252.F03 = t6230.F02;
                    t6252.F04 = t6251.F04;
                    t6252.F05 = FeeCode.TZ_LX;
                    t6252.F06 = i;
                    t6252.F07 = interest;
                    t6252.F08 = endDate;
                    t6252.F09 = T6252_F09.WH;
                    t6252.F11 = t6251.F01;
                    t6252s.add(t6252);
                }
                
                continue;
            }
            interest = y.subtract(total).multiply(x).setScale(2, BigDecimal.ROUND_HALF_UP);
            bj = amount.subtract(interest).setScale(2, BigDecimal.ROUND_HALF_UP);
            total = total.add(bj);
            { // 本金
                T6252 t6252 = new T6252();
                t6252.F02 = t6230.F01;
                t6252.F03 = t6230.F02;
                t6252.F04 = t6251.F04;
                t6252.F05 = FeeCode.TZ_BJ;
                t6252.F06 = i;
                t6252.F07 = bj;
                t6252.F08 = new Date(ca.getTimeInMillis());
                t6252.F09 = T6252_F09.WH;
                t6252.F11 = t6251.F01;
                t6252s.add(t6252);
            }
            { // 利息
                T6252 t6252 = new T6252();
                t6252.F02 = t6230.F01;
                t6252.F03 = t6230.F02;
                t6252.F04 = t6251.F04;
                t6252.F05 = FeeCode.TZ_LX;
                t6252.F06 = i;
                t6252.F07 = interest;
                t6252.F08 = new Date(ca.getTimeInMillis());
                t6252.F09 = T6252_F09.WH;
                t6252.F11 = t6251.F01;
                t6252s.add(t6252);
            }
            ca.add(Calendar.MONTH, 1);
        }
        updateT6231(connection, terms, t6230.F01);
        return t6252s;
    }
    
    @SuppressWarnings("unused")
    private ArrayList<T6252> calGDR_DEBJ(Connection connection, T6230 t6230, T6251 t6251, Date interestDate,
        Date endDate)
        throws Throwable
    {
        Calendar calendar = Calendar.getInstance();
        final int inserestStartDay;// 起息日
        {
            calendar.setTime(interestDate);
            inserestStartDay = calendar.get(Calendar.DAY_OF_MONTH);
        }
        {
            Calendar c = Calendar.getInstance();
            c.setTime(interestDate);
            c.add(Calendar.MONTH, t6230.F09);
            endDate = new Date(c.getTimeInMillis());
        }
        if (inserestStartDay == t6230.F18)
        {
            // 自然月还款
            return calZRY_DEBJ(connection, t6230, t6251, interestDate, endDate);
        }
        if (t6230.F18 > 28)
        {
            throw new LogicalException("固定付息日不能大于28");
        }
        // 借款金额
        BigDecimal x = t6251.F07;
        // 月利率
        BigDecimal y = t6230.F06.divide(new BigDecimal(12), DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
        // 借款期限
        int terms = t6230.F09 + 1;
        // 还款期限
        int n = t6230.F09;
        // 首期天数
        int f = 0;
        // 首期付款日
        Calendar ca = Calendar.getInstance();
        if (inserestStartDay > t6230.F18)
        {
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.add(Calendar.MONTH, 1);
            calendar.set(Calendar.DAY_OF_MONTH, t6230.F18);
            ca.setTimeInMillis(calendar.getTimeInMillis());
        }
        else
        {
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.DAY_OF_MONTH, t6230.F18);
            ca.setTimeInMillis(calendar.getTimeInMillis());
        }
        f = (int)(calendar.getTimeInMillis() - interestDate.getTime()) / (24 * 3600 * 1000);
        // 尾期天数
        calendar.setTime(endDate);
        if (calendar.get(Calendar.DAY_OF_MONTH) > t6230.F18)
        {
            calendar.set(Calendar.DAY_OF_MONTH, t6230.F18);
        }
        else
        {
            calendar.add(Calendar.MONTH, -1);
            calendar.set(Calendar.DAY_OF_MONTH, t6230.F18);
        }
        int l = (int)((endDate.getTime() - calendar.getTimeInMillis()) / (24 * 3600 * 1000));
        BigDecimal total = BigDecimal.ZERO;
        BigDecimal monthAmount = x.divide(new BigDecimal(n), 2, BigDecimal.ROUND_HALF_UP);
        ArrayList<T6252> t6252s = new ArrayList<>();
        for (int i = 1; i <= terms; i++)
        {
            BigDecimal interest = BigDecimal.ZERO;
            BigDecimal bj = BigDecimal.ZERO;
            if (1 == i)
            {
                interest =
                    x.multiply(y)
                        .multiply(new BigDecimal(f))
                        .divide(new BigDecimal(f).add(new BigDecimal(l)), DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
                bj =
                    monthAmount.multiply(new BigDecimal(f)).divide(new BigDecimal(f).add(new BigDecimal(l)),
                        2,
                        BigDecimal.ROUND_HALF_UP);
                total = total.add(bj);
                { // 本金
                    T6252 t6252 = new T6252();
                    t6252.F02 = t6230.F01;
                    t6252.F03 = t6230.F02;
                    t6252.F04 = t6251.F04;
                    t6252.F05 = FeeCode.TZ_BJ;
                    t6252.F06 = i;
                    t6252.F07 = bj;
                    t6252.F08 = new Date(ca.getTimeInMillis());
                    t6252.F09 = T6252_F09.WH;
                    t6252.F11 = t6251.F01;
                    t6252s.add(t6252);
                }
                { // 利息
                    T6252 t6252 = new T6252();
                    t6252.F02 = t6230.F01;
                    t6252.F03 = t6230.F02;
                    t6252.F04 = t6251.F04;
                    t6252.F05 = FeeCode.TZ_LX;
                    t6252.F06 = i;
                    t6252.F07 = interest;
                    t6252.F08 = new Date(ca.getTimeInMillis());
                    t6252.F09 = T6252_F09.WH;
                    t6252.F11 = t6251.F01;
                    t6252s.add(t6252);
                }
                updateT6231(connection, endDate, t6230.F01);
                continue;
            }
            if (i == terms)
            {
                interest =
                    x.subtract(total)
                        .multiply(y)
                        .multiply(new BigDecimal(l))
                        .divide(new BigDecimal(f).add(new BigDecimal(l)), DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
                bj = x.subtract(total).setScale(2, BigDecimal.ROUND_HALF_UP);
                { // 本金
                    T6252 t6252 = new T6252();
                    t6252.F02 = t6230.F01;
                    t6252.F03 = t6230.F02;
                    t6252.F04 = t6251.F04;
                    t6252.F05 = FeeCode.TZ_BJ;
                    t6252.F06 = i;
                    t6252.F07 = bj;
                    t6252.F08 = endDate;
                    t6252.F09 = T6252_F09.WH;
                    t6252.F11 = t6251.F01;
                    t6252s.add(t6252);
                }
                { // 利息
                    T6252 t6252 = new T6252();
                    t6252.F02 = t6230.F01;
                    t6252.F03 = t6230.F02;
                    t6252.F04 = t6251.F04;
                    t6252.F05 = FeeCode.TZ_LX;
                    t6252.F06 = i;
                    t6252.F07 = interest;
                    t6252.F08 = endDate;
                    t6252.F09 = T6252_F09.WH;
                    t6252.F11 = t6251.F01;
                    t6252s.add(t6252);
                }
                continue;
            }
            interest = x.subtract(total).multiply(y).setScale(2, BigDecimal.ROUND_HALF_UP);
            bj = monthAmount;
            total = total.add(bj);
            ca.add(Calendar.MONTH, 1);
            { // 本金
                T6252 t6252 = new T6252();
                t6252.F02 = t6230.F01;
                t6252.F03 = t6230.F02;
                t6252.F04 = t6251.F04;
                t6252.F05 = FeeCode.TZ_BJ;
                t6252.F06 = i;
                t6252.F07 = bj;
                t6252.F08 = new Date(ca.getTimeInMillis());
                t6252.F09 = T6252_F09.WH;
                t6252.F11 = t6251.F01;
                t6252s.add(t6252);
            }
            { // 利息
                T6252 t6252 = new T6252();
                t6252.F02 = t6230.F01;
                t6252.F03 = t6230.F02;
                t6252.F04 = t6251.F04;
                t6252.F05 = FeeCode.TZ_LX;
                t6252.F06 = i;
                t6252.F07 = interest;
                t6252.F08 = new Date(ca.getTimeInMillis());
                t6252.F09 = T6252_F09.WH;
                t6252.F11 = t6251.F01;
                t6252s.add(t6252);
            }
        }
        updateT6231(connection, terms, t6230.F01);
        return t6252s;
    }
    
    /**
    * 本息到期一次付清
    * 
    * @param connection
    * @param t6230
    * @param t6251
    * @param interestDate
    *            起息日
    * @param endDate
    *            结束日
    * @param backMoneyDate
    *            首期还款日
    * @param totalTerm
    * @return
    * @throws Throwable
    */
    private ArrayList<T6252> calculate_YCFQ(Connection connection, T6230 t6230, T6251 t6251, Date interestDate,
        Date endDate, Date backMoneyDate, int totalTerm)
        throws Throwable
    {
        // 一年中的天数
        ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        BigDecimal yearDays =
            new BigDecimal(IntegerParser.parse(configureProvider.getProperty(SystemVariable.REPAY_DAYS_OF_YEAR), 360));
        
        BigDecimal lx = BigDecimal.ZERO;
        BigDecimal bj = BigDecimal.ZERO;
        ArrayList<T6252> t6252s = new ArrayList<>();
        
        T6231 t6231 = selectT6231(connection, t6230.F01);
        
        Calendar date = Calendar.getInstance();
        date.setTime(backMoneyDate);
        date.add(Calendar.DAY_OF_MONTH, t6231.F22);
        endDate = new Date(date.getTimeInMillis());
        
        int days = 0;
        days = (int)Math.floor((endDate.getTime() - interestDate.getTime()) / DateHelper.DAY_IN_MILLISECONDS);
        
        lx = t6251.F07.setScale(9, BigDecimal.ROUND_HALF_UP);
        lx =
            lx.multiply(t6230.F06)
                .multiply(new BigDecimal(days))
                .divide(yearDays, DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
        
        bj = t6251.F07;
        
        /*
        * if (t6231.F21 == T6231_F21.S) {//按天算 Calendar date =
        * Calendar.getInstance(); date.setTime(backMoneyDate);
        * date.add(Calendar.DAY_OF_MONTH, t6231.F22); endDate = new
        * Date(date.getTimeInMillis());
        * 
        * int days = 0; days = (int)Math.floor((endDate.getTime() -
        * interestDate.getTime()) / DateHelper.DAY_IN_MILLISECONDS);
        * 
        * lx = t6251.F07.setScale(9, BigDecimal.ROUND_HALF_UP); lx =
        * lx.multiply(t6230.F06) .multiply(new BigDecimal(days))
        * .divide(yearDays, DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
        * 
        * bj = t6251.F07;
        * 
        * } else { List<T6252> list = calculate_MYFX(connection, t6230, t6251,
        * interestDate, endDate, backMoneyDate, totalTerm);
        * 
        * if (list != null && list.size() > 0) { for (T6252 t6252 : list) { if
        * (FeeCode.TZ_LX == t6252.F05) { lx = lx.add(t6252.F07); } else if
        * (FeeCode.TZ_BJ == t6252.F05) { bj = bj.add(t6252.F07); } } } }
        */
        
        {
            // 利息
            T6252 t6252 = new T6252();
            t6252.F02 = t6230.F01;
            t6252.F03 = t6230.F02;
            t6252.F04 = t6251.F04;
            t6252.F05 = FeeCode.TZ_LX;
            t6252.F06 = 1;
            t6252.F07 = lx;
            t6252.F08 = endDate;
            t6252.F09 = T6252_F09.WH;
            t6252.F10 = null;
            t6252.F11 = t6251.F01;
            t6252s.add(t6252);
            System.out.println("date:" + endDate + "===lx:" + t6252.F07.doubleValue());
        }
        {
            // 本金
            T6252 t6252 = new T6252();
            t6252.F02 = t6230.F01;
            t6252.F03 = t6230.F02;
            t6252.F04 = t6251.F04;
            t6252.F05 = FeeCode.TZ_BJ;
            t6252.F06 = 1;
            t6252.F07 = bj;
            t6252.F08 = endDate;
            t6252.F09 = T6252_F09.WH;
            t6252.F10 = null;
            t6252.F11 = t6251.F01;
            t6252s.add(t6252);
            System.out.println("date:" + endDate + "===bj:" + t6252.F07.doubleValue());
        }
        return t6252s;
    }
    
    /**
    * 每日还款，到期本金
    * 
    * @param connection
    * @param t6230
    * @param t6251
    * @param interestDate
    *            起息日
    * @param endDate
    *            结束日
    * @param backMoneyDate
    *            首期还款日
    * @return
    * @throws Throwable
    */
    @SuppressWarnings("unused")
    private ArrayList<T6252> calculate_MYFX(Connection connection, T6230 t6230, T6251 t6251, Date interestDate,
        Date endDate, Date backMoneyDate, int totalTerm)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S62.T6231 SET F02 = ?, F03 = ?, F06 = ? WHERE F01 = ?"))
        {
            pstmt.setInt(1, t6230.F09);
            pstmt.setInt(2, t6230.F09);
            pstmt.setDate(3, new Date(DateHelper.rollMonth(interestDate.getTime(), 1)));
            pstmt.setInt(4, t6230.F01);
            pstmt.execute();
        }
        BigDecimal monthes = new BigDecimal(12);
        // 一年中的天数
        ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        BigDecimal yearDays =
            new BigDecimal(IntegerParser.parse(configureProvider.getProperty(SystemVariable.REPAY_DAYS_OF_YEAR), 360));
        
        ArrayList<T6252> t6252s = new ArrayList<>();
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(backMoneyDate);
        // 还款日
        int backMoneyDay = cal.get(Calendar.DAY_OF_MONTH);
        // 起息日
        cal.setTime(interestDate);
        int interestDay = cal.get(Calendar.DAY_OF_MONTH);
        // 结束日
        cal.setTime(endDate);
        int endDay = cal.get(Calendar.DAY_OF_MONTH);
        
        int term = 1;// 起息月还款期
        int days = 0;// 首期不满一月天数
        {
            // 相差月数，起息日大于计划还款日，相差月+1;
            int differMonth = (int)getDifferMonth(interestDate, backMoneyDate);
            if (differMonth < 0)
            {
                differMonth = 0;
                // throw new ParameterException("起息日错误！");
            }
            System.out.println("differMonth:" + differMonth);
            term = term + differMonth;
            System.out.println("totalTerm:" + totalTerm + ",term:" + term);
            
            if (backMoneyDay > interestDay)
            {
                days = backMoneyDay - interestDay;
            }
            if (backMoneyDay < interestDay)
            {
                cal = Calendar.getInstance();
                cal.setTime(interestDate);
                cal.add(Calendar.MONTH, 1);
                cal.set(Calendar.DAY_OF_MONTH, backMoneyDay);
                days =
                    (int)Math.floor((cal.getTimeInMillis() - interestDate.getTime()) / DateHelper.DAY_IN_MILLISECONDS);
            }
            
        }
        
        for (int i = 1; term <= totalTerm; term++, i++)
        {
            Date date = new Date(DateHelper.rollMonth(backMoneyDate.getTime(), term));
            Calendar calMonthDay = Calendar.getInstance();
            calMonthDay.setTime(new Date(date.getTime()));
            calMonthDay.add(Calendar.MONTH, -1);
            Date prefixDate = new Date(calMonthDay.getTimeInMillis());
            
            if (1 == i && days > 0)
            {// 计算首月不完整月利息
            
                updateT6231(connection, date, t6230.F01);
                
                // 利息
                T6252 t6252 = new T6252();
                t6252.F02 = t6230.F01;
                t6252.F03 = t6230.F02;
                t6252.F04 = t6251.F04;
                t6252.F05 = FeeCode.TZ_LX;
                t6252.F06 = term;
                t6252.F07 = t6251.F07.setScale(9, BigDecimal.ROUND_HALF_UP);
                t6252.F07 =
                    t6252.F07.multiply(t6230.F06)
                        .multiply(new BigDecimal(days))
                        .divide(yearDays, DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
                t6252.F08 = date;
                t6252.F09 = T6252_F09.WH;
                t6252.F10 = null;
                t6252.F11 = t6251.F01;
                System.out.println("t6230.F06:" + t6230.F06 + "|days:" + days + "|yearDays:" + yearDays);
                System.out.println(i + "date:" + date + "===lx:" + t6252.F07.doubleValue());
                t6252s.add(t6252);
            }
            else if (endDay != backMoneyDay && term == totalTerm)
            {// 不完整月的最后一期
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.MONTH, -1);
                days =
                    (int)Math.ceil((double)(endDate.getTime() - calendar.getTimeInMillis())
                        / DateHelper.DAY_IN_MILLISECONDS);
                System.out.println("t6230.F06:" + t6230.F06 + "|sdate:" + new Date(calendar.getTimeInMillis())
                    + "edate:" + endDate + "|days:" + days + "|yearDays:" + yearDays);
                date = new Date(endDate.getTime());
                // 利息
                T6252 t6252 = new T6252();
                t6252.F02 = t6230.F01;
                t6252.F03 = t6230.F02;
                t6252.F04 = t6251.F04;
                t6252.F05 = FeeCode.TZ_LX;
                t6252.F06 = term;
                t6252.F07 = t6251.F07.setScale(9, BigDecimal.ROUND_HALF_UP);
                t6252.F07 =
                    t6252.F07.multiply(t6230.F06)
                        .multiply(new BigDecimal(days))
                        .divide(yearDays, DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
                t6252.F08 = date;
                t6252.F09 = T6252_F09.WH;
                t6252.F10 = null;
                t6252.F11 = t6251.F01;
                System.out.println(i + "date:" + date + "|days:" + days + "===lx:" + t6252.F07.doubleValue());
                t6252s.add(t6252);
            }
            else
            {// 完整月
             // 一个月的总天数
                int monthDays =
                    (int)Math.floor((date.getTime() - prefixDate.getTime()) / DateHelper.DAY_IN_MILLISECONDS);
                // 利息
                T6252 t6252 = new T6252();
                t6252.F02 = t6230.F01;
                t6252.F03 = t6230.F02;
                t6252.F04 = t6251.F04;
                t6252.F05 = FeeCode.TZ_LX;
                t6252.F06 = term;
                t6252.F07 = t6251.F07.setScale(9, BigDecimal.ROUND_HALF_UP);
                t6252.F07 =
                    t6252.F07.multiply(t6230.F06)
                        .multiply(new BigDecimal(monthDays))
                        .divide(yearDays, DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
                t6252.F08 = date;
                t6252.F09 = T6252_F09.WH;
                t6252.F10 = null;
                t6252.F11 = t6251.F01;
                t6252s.add(t6252);
                System.out.println(i + "date:" + date + "|monthDays：" + monthDays + "===lx:" + t6252.F07.doubleValue());
            }
            
            if (term == totalTerm)
            {
                // 本金
                T6252 t6252 = new T6252();
                t6252.F02 = t6230.F01;
                t6252.F03 = t6230.F02;
                t6252.F04 = t6251.F04;
                t6252.F05 = FeeCode.TZ_BJ;
                t6252.F06 = term;
                t6252.F07 = t6251.F07;
                t6252.F08 = date;
                t6252.F09 = T6252_F09.WH;
                t6252.F10 = null;
                t6252.F11 = t6251.F01;
                t6252s.add(t6252);
                System.out.println(i + "date:" + date + "===bj:" + t6252.F07.doubleValue());
            }
            
        }
        updateT6231(connection, t6230.F09, t6230.F01);
        return t6252s;
    }
    
    /**
    * 等额本金
    * 
    * @param connection
    * @param t6230
    * @param t6251
    * @param interestDate
    * @param endDate
    * @param backMoneyDate
    * @param totalTerm
    * @return
    * @throws Throwable
    */
    @SuppressWarnings("unused")
    private ArrayList<T6252> calculate_DEBJ(Connection connection, T6230 t6230, T6251 t6251, Date interestDate,
        Date endDate, Date backMoneyDate, int totalTerm)
        throws Throwable
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(backMoneyDate);
        // 还款日
        int backMoneyDay = cal.get(Calendar.DAY_OF_MONTH);
        // 起息日
        cal.setTime(interestDate);
        int interestDay = cal.get(Calendar.DAY_OF_MONTH);
        // 结束日
        cal.setTime(endDate);
        int endDay = cal.get(Calendar.DAY_OF_MONTH);
        
        int term = 1;// 起息月还款期
        int days = 0;// 首期不满一月天数
        {
            // 相差月数，起息日大于计划还款日，相差月+1;
            int differMonth = (int)getDifferMonth(interestDate, backMoneyDate);
            if (differMonth < 0)
            {
                differMonth = 0;
                // throw new ParameterException("起息日错误！");
            }
            System.out.println("differMonth:" + differMonth);
            term = term + differMonth;
            System.out.println("totalTerm:" + totalTerm + ",term:" + term);
            
            if (backMoneyDay > interestDay)
            {
                days = backMoneyDay - interestDay;
            }
            if (backMoneyDay < interestDay)
            {
                cal = Calendar.getInstance();
                cal.setTime(interestDate);
                cal.add(Calendar.MONTH, 1);
                cal.set(Calendar.DAY_OF_MONTH, backMoneyDay);
                days =
                    (int)Math.floor((cal.getTimeInMillis() - interestDate.getTime()) / DateHelper.DAY_IN_MILLISECONDS);
            }
            
            System.out.println("interestDate:" + interestDate);
            System.out.println("fbDay:" + backMoneyDay);
            System.out.println("days:" + days);
        }
        
        // 一年中的天数
        ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        BigDecimal yearDays =
            new BigDecimal(IntegerParser.parse(configureProvider.getProperty(SystemVariable.REPAY_DAYS_OF_YEAR), 360));
        
        // 借款金额
        BigDecimal totalBj = t6251.F07;
        // 月利率
        BigDecimal monthRate = t6230.F06.divide(new BigDecimal(12), DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
        // 日利率
        BigDecimal dayRate = t6230.F06.divide(yearDays, DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
        // 月还本金
        BigDecimal monthBj = totalBj.divide(new BigDecimal(totalTerm - term + 1), 2, BigDecimal.ROUND_HALF_UP);
        // 已还本金总额
        BigDecimal total = BigDecimal.ZERO;
        ArrayList<T6252> t6252s = new ArrayList<>();
        for (int i = 1; term <= totalTerm; term++, i++)
        {
            Date date = new Date(DateHelper.rollMonth(backMoneyDate.getTime(), term));
            Calendar calMonthDay = Calendar.getInstance();
            calMonthDay.setTime(new java.util.Date(date.getTime()));
            calMonthDay.add(Calendar.MONTH, -1);
            Date prefixDate = new Date(calMonthDay.getTimeInMillis());
            BigDecimal interest = BigDecimal.ZERO;
            if (1 == i && days > 0)
            {// 不完整首月
            
                updateT6231(connection, new Date(date.getTime()), t6230.F01);
                
                BigDecimal bj = totalBj.subtract(total).setScale(2, BigDecimal.ROUND_HALF_UP);
                interest =
                    bj.multiply(t6230.F06)
                        .multiply(new BigDecimal(days))
                        .divide(yearDays, DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
                { // 本金
                    T6252 t6252 = new T6252();
                    t6252.F02 = t6230.F01;
                    t6252.F03 = t6230.F02;
                    t6252.F04 = t6251.F04;
                    t6252.F05 = FeeCode.TZ_BJ;
                    t6252.F06 = term;
                    t6252.F07 = monthBj;
                    t6252.F08 = date;
                    t6252.F09 = T6252_F09.WH;
                    t6252.F11 = t6251.F01;
                    t6252s.add(t6252);
                    System.out.println("date:" + date + "|monthDays：" + days + "===bj:" + t6252.F07.doubleValue());
                }
                { // 利息
                    T6252 t6252 = new T6252();
                    t6252.F02 = t6230.F01;
                    t6252.F03 = t6230.F02;
                    t6252.F04 = t6251.F04;
                    t6252.F05 = FeeCode.TZ_LX;
                    t6252.F06 = term;
                    t6252.F07 = interest;
                    t6252.F08 = date;
                    t6252.F09 = T6252_F09.WH;
                    t6252.F11 = t6251.F01;
                    t6252s.add(t6252);
                    System.out.println("date:" + date + "|monthDays：" + days + "===lx:" + t6252.F07.doubleValue());
                }
            }
            else if (endDay != backMoneyDay && i == totalTerm)
            {// 不完整最后一个月
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.MONTH, -1);
                days =
                    (int)Math.ceil((double)(endDate.getTime() - calendar.getTimeInMillis())
                        / DateHelper.DAY_IN_MILLISECONDS);
                System.out.println("t6230.F06:" + t6230.F06 + "|sdate:" + new Date(calendar.getTimeInMillis())
                    + "edate:" + endDate + "|days:" + days + "|yearDays:" + yearDays);
                date = new Date(endDate.getTime());
                
                BigDecimal bj = totalBj.subtract(total).setScale(2, BigDecimal.ROUND_HALF_UP);
                interest =
                    bj.multiply(t6230.F06)
                        .multiply(new BigDecimal(days))
                        .divide(yearDays, DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
                { // 本金
                    T6252 t6252 = new T6252();
                    t6252.F02 = t6230.F01;
                    t6252.F03 = t6230.F02;
                    t6252.F04 = t6251.F04;
                    t6252.F05 = FeeCode.TZ_BJ;
                    t6252.F06 = term;
                    t6252.F07 = monthBj;
                    t6252.F08 = date;
                    t6252.F09 = T6252_F09.WH;
                    t6252.F11 = t6251.F01;
                    t6252s.add(t6252);
                    System.out.println("date:" + date + "|monthDays：" + days + "===bj:" + t6252.F07.doubleValue());
                }
                { // 利息
                    T6252 t6252 = new T6252();
                    t6252.F02 = t6230.F01;
                    t6252.F03 = t6230.F02;
                    t6252.F04 = t6251.F04;
                    t6252.F05 = FeeCode.TZ_LX;
                    t6252.F06 = term;
                    t6252.F07 = interest;
                    t6252.F08 = date;
                    t6252.F09 = T6252_F09.WH;
                    t6252.F11 = t6251.F01;
                    t6252s.add(t6252);
                    System.out.println("date:" + date + "|monthDays：" + days + "===lx:" + t6252.F07.doubleValue());
                }
            }
            else
            {// 完整月计算
             // 一个月的总天数
                int monthDays =
                    (int)Math.floor((date.getTime() - prefixDate.getTime()) / DateHelper.DAY_IN_MILLISECONDS);
                BigDecimal bj = totalBj.subtract(total).setScale(2, BigDecimal.ROUND_HALF_UP);
                interest =
                    bj.multiply(t6230.F06)
                        .multiply(new BigDecimal(monthDays))
                        .divide(yearDays, DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
                
                { // 本金
                    T6252 t6252 = new T6252();
                    t6252.F02 = t6230.F01;
                    t6252.F03 = t6230.F02;
                    t6252.F04 = t6251.F04;
                    t6252.F05 = FeeCode.TZ_BJ;
                    t6252.F06 = term;
                    t6252.F07 = monthBj;
                    t6252.F08 = date;
                    t6252.F09 = T6252_F09.WH;
                    t6252.F11 = t6251.F01;
                    t6252s.add(t6252);
                    System.out.println("date:" + date + "|monthDays：" + monthDays + "===bj:" + t6252.F07.doubleValue());
                }
                { // 利息
                    T6252 t6252 = new T6252();
                    t6252.F02 = t6230.F01;
                    t6252.F03 = t6230.F02;
                    t6252.F04 = t6251.F04;
                    t6252.F05 = FeeCode.TZ_LX;
                    t6252.F06 = term;
                    t6252.F07 = interest;
                    t6252.F08 = date;
                    t6252.F09 = T6252_F09.WH;
                    t6252.F11 = t6251.F01;
                    t6252s.add(t6252);
                    System.out.println("date:" + date + "|monthDays：" + monthDays + "===lx:" + t6252.F07.doubleValue());
                }
            }
            total = total.add(monthBj);
        }
        return t6252s;
    }
    
    private ArrayList<T6252> calculate_DEBX(Connection connection, T6230 t6230, T6251 t6251, Date interestDate,
        Date endDate, Date backMoneyDate, int totalTerm)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S62.T6231 SET F02 = ?, F03 = ?, F06 = ? WHERE F01 = ?"))
        {
            pstmt.setInt(1, t6230.F09);
            pstmt.setInt(2, t6230.F09);
            pstmt.setDate(3, new Date(DateHelper.rollMonth(interestDate.getTime(), 1)));
            pstmt.setInt(4, t6230.F01);
            pstmt.execute();
        }
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(backMoneyDate);
        // 还款日
        int backMoneyDay = cal.get(Calendar.DAY_OF_MONTH);
        // 起息日
        cal.setTime(interestDate);
        int interestDay = cal.get(Calendar.DAY_OF_MONTH);
        // 结束日
        cal.setTime(endDate);
        int endDay = cal.get(Calendar.DAY_OF_MONTH);
        
        int term = 1;// 起息月还款期
        int days = 0;// 首期不满一月天数
        {
            // 相差月数，起息日大于计划还款日，相差月+1;
            int differMonth = (int)getDifferMonth(interestDate, backMoneyDate);
            if (differMonth < 0)
            {
                differMonth = 0;
                // throw new ParameterException("起息日错误！");
            }
            System.out.println("differMonth:" + differMonth);
            term = term + differMonth;
            System.out.println("totalTerm:" + totalTerm + ",term:" + term);
            
            if (backMoneyDay > interestDay)
            {
                days = backMoneyDay - interestDay;
            }
            if (backMoneyDay < interestDay)
            {
                cal = Calendar.getInstance();
                cal.setTime(interestDate);
                cal.add(Calendar.MONTH, 1);
                cal.set(Calendar.DAY_OF_MONTH, backMoneyDay);
                days =
                    (int)Math.floor((cal.getTimeInMillis() - interestDate.getTime()) / DateHelper.DAY_IN_MILLISECONDS);
            }
            
            System.out.println("interestDate:" + interestDate);
            System.out.println("fbDay:" + backMoneyDay);
            System.out.println("days:" + days);
        }
        
        // 一年中的天数
        ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        BigDecimal yearDays =
            new BigDecimal(IntegerParser.parse(configureProvider.getProperty(SystemVariable.REPAY_DAYS_OF_YEAR), 360));
        
        ArrayList<T6252> t6252s = new ArrayList<>();
        BigDecimal monthRate =
            t6230.F06.setScale(DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP).divide(new BigDecimal(12),
                DECIMAL_SCALE,
                BigDecimal.ROUND_HALF_UP);
        BigDecimal remainTotal = t6251.F07;
        BigDecimal monthPayTotal = debx(t6251.F07, monthRate, t6230.F09 - term + 1);
        
        for (int i = 1; term <= totalTerm; term++, i++)
        {
            Date date = new Date(DateHelper.rollMonth(backMoneyDate.getTime(), term));
            Calendar calMonthDay = Calendar.getInstance();
            calMonthDay.setTime(new java.util.Date(date.getTime()));
            calMonthDay.add(Calendar.MONTH, -1);
            Date prefixDate = new Date(calMonthDay.getTimeInMillis());
            BigDecimal interest = BigDecimal.ZERO;
            if (1 == i && days > 0)
            {// 不完整首月
            
                updateT6231(connection, new Date(date.getTime()), t6230.F01);
                
                interest =
                    remainTotal.multiply(t6230.F06)
                        .multiply(new BigDecimal(days))
                        .divide(yearDays, DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
                { // 本金
                    T6252 t6252 = new T6252();
                    t6252.F02 = t6230.F01;
                    t6252.F03 = t6230.F02;
                    t6252.F04 = t6251.F04;
                    t6252.F05 = FeeCode.TZ_BJ;
                    t6252.F06 = term;
                    t6252.F07 = monthPayTotal.subtract(interest);
                    remainTotal = remainTotal.subtract(t6252.F07);
                    t6252.F08 = date;
                    t6252.F09 = T6252_F09.WH;
                    t6252.F11 = t6251.F01;
                    t6252s.add(t6252);
                    System.out.println("date:" + date + "|monthDays：" + days + "===bj:" + t6252.F07.doubleValue());
                }
                { // 利息
                    T6252 t6252 = new T6252();
                    t6252.F02 = t6230.F01;
                    t6252.F03 = t6230.F02;
                    t6252.F04 = t6251.F04;
                    t6252.F05 = FeeCode.TZ_LX;
                    t6252.F06 = term;
                    t6252.F07 = interest;
                    t6252.F08 = date;
                    t6252.F09 = T6252_F09.WH;
                    t6252.F11 = t6251.F01;
                    t6252s.add(t6252);
                    System.out.println("date:" + date + "|monthDays：" + days + "===bx:" + t6252.F07.doubleValue());
                }
            }
            else if (endDay != backMoneyDay && i == totalTerm)
            {// 不完整最后一个月
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.MONTH, -1);
                days =
                    (int)Math.ceil((double)(endDate.getTime() - calendar.getTimeInMillis())
                        / DateHelper.DAY_IN_MILLISECONDS);
                System.out.println("t6230.F06:" + t6230.F06 + "|sdate:" + new Date(calendar.getTimeInMillis())
                    + "edate:" + endDate + "|days:" + days + "|yearDays:" + yearDays);
                date = new Date(endDate.getTime());
                
                interest =
                    remainTotal.multiply(t6230.F06)
                        .multiply(new BigDecimal(days))
                        .divide(yearDays, DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
                { // 本金
                    T6252 t6252 = new T6252();
                    t6252.F02 = t6230.F01;
                    t6252.F03 = t6230.F02;
                    t6252.F04 = t6251.F04;
                    t6252.F05 = FeeCode.TZ_BJ;
                    t6252.F06 = term;
                    t6252.F07 = monthPayTotal.subtract(interest);
                    remainTotal = remainTotal.subtract(t6252.F07);
                    t6252.F08 = date;
                    t6252.F09 = T6252_F09.WH;
                    t6252.F11 = t6251.F01;
                    t6252s.add(t6252);
                    System.out.println("date:" + date + "|monthDays：" + days + "===bj:" + t6252.F07.doubleValue());
                }
                { // 利息
                    T6252 t6252 = new T6252();
                    t6252.F02 = t6230.F01;
                    t6252.F03 = t6230.F02;
                    t6252.F04 = t6251.F04;
                    t6252.F05 = FeeCode.TZ_LX;
                    t6252.F06 = term;
                    t6252.F07 = interest;
                    t6252.F08 = date;
                    t6252.F09 = T6252_F09.WH;
                    t6252.F11 = t6251.F01;
                    t6252s.add(t6252);
                    System.out.println("date:" + date + "|monthDays：" + days + "===lx:" + t6252.F07.doubleValue());
                }
            }
            else
            {// 完整月
             // 一个月的总天数
                int monthDays =
                    (int)Math.floor((date.getTime() - prefixDate.getTime()) / DateHelper.DAY_IN_MILLISECONDS);
                interest =
                    remainTotal.multiply(t6230.F06)
                        .multiply(new BigDecimal(monthDays))
                        .divide(yearDays, DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
                { // 本金
                    T6252 t6252 = new T6252();
                    t6252.F02 = t6230.F01;
                    t6252.F03 = t6230.F02;
                    t6252.F04 = t6251.F04;
                    t6252.F05 = FeeCode.TZ_BJ;
                    t6252.F06 = term;
                    t6252.F07 = monthPayTotal.subtract(interest);
                    remainTotal = remainTotal.subtract(t6252.F07);
                    t6252.F08 = date;
                    t6252.F09 = T6252_F09.WH;
                    t6252.F11 = t6251.F01;
                    t6252s.add(t6252);
                    System.out.println("date:" + date + "|monthDays：" + monthDays + "===bj:" + t6252.F07.doubleValue());
                }
                { // 利息
                    T6252 t6252 = new T6252();
                    t6252.F02 = t6230.F01;
                    t6252.F03 = t6230.F02;
                    t6252.F04 = t6251.F04;
                    t6252.F05 = FeeCode.TZ_LX;
                    t6252.F06 = term;
                    t6252.F07 = interest;
                    t6252.F08 = date;
                    t6252.F09 = T6252_F09.WH;
                    t6252.F11 = t6251.F01;
                    t6252s.add(t6252);
                    System.out.println("date:" + date + "|monthDays：" + monthDays + "===lx:" + t6252.F07.doubleValue());
                }
            }
        }
        
        return t6252s;
    }
    
    protected void updateT6231(Connection connection, Date F01, int F02)
        throws SQLException
    {
        try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S62.T6231 SET F06 = ? WHERE F01 = ?"))
        {
            pstmt.setDate(1, F01);
            pstmt.setInt(2, F02);
            pstmt.execute();
        }
    }
    
    private ArrayList<T6252> calZRY_MYFX(Connection connection, T6230 t6230, T6251 t6251, Date interestDate,
        Date endDate)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S62.T6231 SET F02 = ?, F03 = ?, F06 = ? WHERE F01 = ?"))
        {
            pstmt.setInt(1, t6230.F09);
            pstmt.setInt(2, t6230.F09);
            pstmt.setDate(3, new Date(DateHelper.rollMonth(interestDate.getTime(), 1)));
            pstmt.setInt(4, t6230.F01);
            pstmt.execute();
        }
        ArrayList<T6252> t6252s = new ArrayList<>();
        BigDecimal monthes = new BigDecimal(12);
        for (int term = 1; term <= t6230.F09; term++)
        {
            Date date = new Date(DateHelper.rollMonth(interestDate.getTime(), term));
            if (1 == term)
            {
                updateT6231(connection, date, t6230.F01);
            }
            {
                // 利息
                T6252 t6252 = new T6252();
                t6252.F02 = t6230.F01;
                t6252.F03 = t6230.F02;
                t6252.F04 = t6251.F04;
                t6252.F05 = FeeCode.TZ_LX;
                t6252.F06 = term;
                t6252.F07 = t6251.F07.setScale(9, BigDecimal.ROUND_HALF_UP);
                t6252.F07 = t6252.F07.multiply(t6230.F06).divide(monthes, DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
                t6252.F08 = date;
                t6252.F09 = T6252_F09.WH;
                t6252.F10 = null;
                t6252.F11 = t6251.F01;
                t6252s.add(t6252);
            }
            if (term == t6230.F09)
            {
                // 本金
                T6252 t6252 = new T6252();
                t6252.F02 = t6230.F01;
                t6252.F03 = t6230.F02;
                t6252.F04 = t6251.F04;
                t6252.F05 = FeeCode.TZ_BJ;
                t6252.F06 = term;
                t6252.F07 = t6251.F07;
                t6252.F08 = date;
                t6252.F09 = T6252_F09.WH;
                t6252.F10 = null;
                t6252.F11 = t6251.F01;
                t6252s.add(t6252);
            }
        }
        updateT6231(connection, t6230.F09, t6230.F01);
        return t6252s;
    }
    
    private ArrayList<T6252> calZRY_DEBX(Connection connection, T6230 t6230, T6251 t6251, Date interestDate,
        Date endDate)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S62.T6231 SET F02 = ?, F03 = ?, F06 = ? WHERE F01 = ?"))
        {
            pstmt.setInt(1, t6230.F09);
            pstmt.setInt(2, t6230.F09);
            pstmt.setDate(3, new Date(DateHelper.rollMonth(interestDate.getTime(), 1)));
            pstmt.setInt(4, t6230.F01);
            pstmt.execute();
        }
        ArrayList<T6252> t6252s = new ArrayList<>();
        BigDecimal monthRate =
            t6230.F06.setScale(DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP).divide(new BigDecimal(12),
                DECIMAL_SCALE,
                BigDecimal.ROUND_HALF_UP);
        BigDecimal remainTotal = t6251.F07;
        BigDecimal monthPayTotal = debx(t6251.F07, monthRate, t6230.F09);
        for (int term = 1; term <= t6230.F09; term++)
        {
            Date date = new Date(DateHelper.rollMonth(interestDate.getTime(), term));
            BigDecimal interest = remainTotal.multiply(monthRate).setScale(2, BigDecimal.ROUND_HALF_UP);
            if (1 == term)
            {
                updateT6231(connection, date, t6230.F01);
            }
            {
                // 利息
                T6252 t6252 = new T6252();
                t6252.F02 = t6230.F01;
                t6252.F03 = t6230.F02;
                t6252.F04 = t6251.F04;
                t6252.F05 = FeeCode.TZ_LX;
                t6252.F06 = term;
                if (t6230.F09 == term)
                {
                    t6252.F07 = monthPayTotal.subtract(remainTotal);
                }
                else
                {
                    t6252.F07 = interest;
                }
                t6252.F08 = date;
                t6252.F09 = T6252_F09.WH;
                t6252.F10 = null;
                t6252.F11 = t6251.F01;
                t6252s.add(t6252);
            }
            {
                // 本金
                T6252 t6252 = new T6252();
                t6252.F02 = t6230.F01;
                t6252.F03 = t6230.F02;
                t6252.F04 = t6251.F04;
                t6252.F05 = FeeCode.TZ_BJ;
                t6252.F06 = term;
                if (t6230.F09 == term)
                {
                    t6252.F07 = remainTotal;
                }
                else
                {
                    t6252.F07 = monthPayTotal.subtract(interest);
                }
                remainTotal = remainTotal.subtract(t6252.F07);
                t6252.F08 = date;
                t6252.F09 = T6252_F09.WH;
                t6252.F10 = null;
                t6252.F11 = t6251.F01;
                t6252s.add(t6252);
            }
        }
        
        return t6252s;
    }
    
    private BigDecimal debx(BigDecimal total, BigDecimal monthRate, int terms)
    {
        BigDecimal tmp = monthRate.add(new BigDecimal(1)).pow(terms);
        return total.multiply(monthRate)
            .multiply(tmp)
            .divide(tmp.subtract(new BigDecimal(1)), 2, BigDecimal.ROUND_HALF_UP);
    }
    
    private Map<String, Object> getEmpInfo(int userId, Connection connection)
        throws Throwable
    {
        Map<String, Object> result = new HashMap<String, Object>();
        String sql =
            "SELECT T7110.F12 AS F01, T7110.F05 AS F02 FROM S61.T6110 LEFT JOIN S71.T7110 ON T6110.F14 = T7110.F12 WHERE T6110.F01 = ? LIMIT 1";
        try (PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    result.put("empNum", rs.getString(1));
                    result.put("empStatus", rs.getString(2));
                }
            }
        }
        
        if (result.get("empNum") == null || StringHelper.isEmpty((String)result.get("empNum")))
        {
            sql =
                "SELECT T7110.F12 AS F01, T7110.F05 AS F02 FROM S61.T6110 LEFT JOIN S71.T7110 ON T6110.F14 = T7110.F12 WHERE T6110.F01 IN (SELECT F01 FROM S61.T6111 T1 WHERE F02 = (SELECT T2.F03 FROM S61.T6111 T2 WHERE T2.F01 = ? LIMIT 1)) LIMIT 1 ";
            try (PreparedStatement ps = connection.prepareStatement(sql))
            {
                ps.setInt(1, userId);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        result.put("empNum", rs.getString(1));
                        result.put("empStatus", rs.getString(2));
                    }
                }
            }
        }
        return result;
    }
    
    private int insertT6250(Connection connection, T6250 entity)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S62.T6250 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F09 = ?, F12 = ?, F13 = ? ",
                PreparedStatement.RETURN_GENERATED_KEYS))
        {
            pstmt.setInt(1, entity.F02);
            pstmt.setInt(2, entity.F03);
            pstmt.setBigDecimal(3, entity.F04);
            pstmt.setBigDecimal(4, entity.F05);
            pstmt.setTimestamp(5, getCurrentTimestamp(connection));
            pstmt.setString(6, entity.F07.name());
            pstmt.setString(7, entity.F09.name());
            pstmt.setString(8, (String)getEmpInfo(entity.F03, connection).get("empNum"));
            pstmt.setString(9, (String)getEmpInfo(entity.F03, connection).get("empStatus"));
            pstmt.execute();
            try (ResultSet resultSet = pstmt.getGeneratedKeys();)
            {
                if (resultSet.next())
                {
                    return resultSet.getInt(1);
                }
                return 0;
            }
        }
    }
    
    private int insertT6251(Connection connection, T6251 entity)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S62.T6251 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?, F10 = ?, F11 = ?",
                PreparedStatement.RETURN_GENERATED_KEYS))
        {
            pstmt.setString(1, entity.F02);
            pstmt.setInt(2, entity.F03);
            pstmt.setInt(3, entity.F04);
            pstmt.setBigDecimal(4, entity.F05);
            pstmt.setBigDecimal(5, entity.F06);
            pstmt.setBigDecimal(6, entity.F07);
            pstmt.setString(7, entity.F08.name());
            pstmt.setDate(8, entity.F09);
            pstmt.setDate(9, entity.F10);
            pstmt.setInt(10, entity.F11);
            pstmt.execute();
            try (ResultSet resultSet = pstmt.getGeneratedKeys();)
            {
                if (resultSet.next())
                {
                    return resultSet.getInt(1);
                }
                return 0;
            }
        }
    }

    protected T6504 selectT6504(Connection connection, int F01)
        throws SQLException
    {
        T6504 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06 FROM S65.T6504 WHERE T6504.F01 = ? LIMIT 1"))
        {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6504();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getInt(3);
                    record.F04 = resultSet.getBigDecimal(4);
                    record.F05 = resultSet.getInt(5);
                    record.F06 = T6504_F06.parse(resultSet.getString(6));
                }
            }
        }
        return record;
    }
    
    protected void updateT6101(Connection connection, BigDecimal F01, int F02)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S61.T6101 SET F06 = ?, F07 = ? WHERE F01 = ?"))
        {
            pstmt.setBigDecimal(1, F01);
            pstmt.setTimestamp(2, getCurrentTimestamp(connection));
            pstmt.setInt(3, F02);
            pstmt.execute();
        }
    }
    
    protected T6230 selectT6230(Connection connection, int F01)
        throws SQLException
    {
        T6230 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11, F12, F13, F14, F15, F16, F17, F18, F19, F20, F21, F22, F23, F24, F25, F26 FROM S62.T6230 WHERE T6230.F01 = ? FOR UPDATE"))
        {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6230();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getString(3);
                    record.F04 = resultSet.getInt(4);
                    record.F05 = resultSet.getBigDecimal(5);
                    record.F06 = resultSet.getBigDecimal(6);
                    record.F07 = resultSet.getBigDecimal(7);
                    record.F08 = resultSet.getInt(8);
                    record.F09 = resultSet.getInt(9);
                    record.F10 = T6230_F10.parse(resultSet.getString(10));
                    record.F11 = T6230_F11.parse(resultSet.getString(11));
                    record.F12 = T6230_F12.parse(resultSet.getString(12));
                    record.F13 = T6230_F13.parse(resultSet.getString(13));
                    record.F14 = T6230_F14.parse(resultSet.getString(14));
                    record.F15 = T6230_F15.parse(resultSet.getString(15));
                    record.F16 = T6230_F16.parse(resultSet.getString(16));
                    record.F17 = T6230_F17.parse(resultSet.getString(17));
                    record.F18 = resultSet.getInt(18);
                    record.F19 = resultSet.getInt(19);
                    record.F20 = T6230_F20.parse(resultSet.getString(20));
                    record.F21 = resultSet.getString(21);
                    record.F22 = resultSet.getTimestamp(22);
                    record.F23 = resultSet.getInt(23);
                    record.F24 = resultSet.getTimestamp(24);
                    record.F25 = resultSet.getString(25);
                    record.F26 = resultSet.getBigDecimal(26);
                }
            }
        }
        return record;
    }
    
    /**
    * 得到两日期相差几个月
    * 
    * @param startDate
    * @param endDate
    * @return
    */
    private long getDifferMonth(Date startDate, Date endDate)
    {
        long monthday;
        Calendar starCal = Calendar.getInstance();
        starCal.setTime(startDate);
        
        int sYear = starCal.get(Calendar.YEAR);
        int sMonth = starCal.get(Calendar.MONTH);
        int sDay = starCal.get(Calendar.DATE);
        
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);
        int eYear = endCal.get(Calendar.YEAR);
        int eMonth = endCal.get(Calendar.MONTH);
        int eDay = endCal.get(Calendar.DATE);
        
        monthday = ((eYear - sYear) * 12 + (eMonth - sMonth));
        
        if (eMonth != sMonth && sDay > eDay)
        {
            monthday = monthday - 1;
        }
        
        return monthday;
    }
    
    protected T6518 selectT6518(Connection connection, int F06)
        throws SQLException
    {
        T6518 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05 FROM S65.T6518 WHERE T6518.F06 = ? LIMIT 1"))
        {
            pstmt.setInt(1, F06);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6518();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getInt(3);
                    record.F04 = resultSet.getBigDecimal(4);
                    record.F05 = resultSet.getInt(5);
                }
            }
        }
        return record;
    }
    
    /**
    * 
    * 红包订单
    */
    protected T6527 selectT6527(Connection connection, int F06)
        throws SQLException
    {
        T6527 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05 FROM S65.T6527 WHERE T6527.F06 = ? LIMIT 1"))
        {
            pstmt.setInt(1, F06);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6527();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getInt(3);
                    record.F04 = resultSet.getBigDecimal(4);
                    record.F05 = resultSet.getInt(5);
                }
            }
        }
        return record;
    }
    
    protected void giveScore(Connection connection, Integer userId, BigDecimal amount, T6106_F05 F05, Timestamp now,
        ConfigureProvider configureProvider)
    {
        try
        {
            T6106 t6106 = new T6106();
            t6106.F02 = userId;
            T6356 t6356 = getT6356(F05.name(), T6356_F04.on.name(), connection);
            if (null == t6356)
            {
                throw new ParameterException("积分规则不存在");
            }
            String score = t6356.F02;
            if (StringHelper.isEmpty(score) || "0".equals(score))
            {
                throw new ParameterException("积分值错误：" + score);
            }
            t6106.F05 = F05;
            switch (F05.name())
            {
            //投资
                case "invest":
                    if (null == amount)
                    {
                        throw new ParameterException("金额为空");
                    }
                    setT6106F03(t6106, amount, score);
                    break;
                //邀请有效投资用户积分
                case "invite":
                    if (null == amount)
                    {
                        throw new ParameterException("金额为空");
                    }
                    setT6106F03(t6106, amount, score, connection, configureProvider);
                    break;
                default:
                    throw new ParameterException("积分类型不存在");
            }
            t6106.F04 = now;
            t6106.F07 = now;
            //保存用户积分
            saveT6106(t6106, connection);
            //更新用户积分账户
            updateT6105(t6106, connection, now);
        }
        catch (Exception e)
        {
            logger.error(e, e);
        }
        catch (Throwable e)
        {
            logger.error(e, e);
        }
    }
    
    /**
     * <积分规则设置表>
     * @return T6356
     * @throws SQLException 
     */
    private T6356 getT6356(String F03, String F04, Connection connection)
        throws SQLException
    {
        
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01,F02,F03,F04,F05,F06 FROM S63.T6356 WHERE F03=? AND F04=? LIMIT 1"))
        {
            pstmt.setString(1, F03);
            pstmt.setString(2, F04);
            try (ResultSet rs = pstmt.executeQuery())
            {
                T6356 t6356 = null;
                if (rs.next())
                {
                    t6356 = new T6356();
                    t6356.F01 = rs.getInt(1);
                    t6356.F02 = rs.getString(2);
                    t6356.F03 = T6356_F03.parse(rs.getString(3));
                    t6356.F04 = T6356_F04.parse(rs.getString(4));
                    t6356.F05 = rs.getTimestamp(5);
                    t6356.F06 = rs.getTimestamp(5);
                }
                return t6356;
            }
        }
    }
    
    /**
     * <设置投资积分值>
     * <功能详细描述>
     * @param t6106
     * @param amount
     * @param score
     */
    private void setT6106F03(T6106 t6106, BigDecimal amount, String score)
        throws Throwable
    {
        int amountInt = amount.intValue();
        String[] scores = score.split(",");
        int amountMark = IntegerParser.parse(scores[0]);
        if (amountMark > amountInt)
        {
            throw new ParameterException("不符合赠送积分规则,赠送类型:" + t6106.F05.name() + ",amount:" + amount);
        }
        
        t6106.F03 = amountInt * IntegerParser.parse(scores[1]) / amountMark;
    }
    
    /**
     * <邀请有效投资用户积分>
     * <功能详细描述>
     * @param t6106
     * @param amount
     * @param score
     * @param connection
     */
    private void setT6106F03(T6106 t6106, BigDecimal amount, String score, Connection connection,
        ConfigureProvider configureProvider)
        throws Throwable
    {
        
        // 邀请码
        String yqm = null;
        try (PreparedStatement ps = connection.prepareStatement("SELECT F03 FROM S61.T6111 WHERE T6111.F01 = ?"))
        {
            ps.setInt(1, t6106.F02);
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
            throw new ParameterException("邀请码不存在");
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
            throw new ParameterException("推广用户不存在");
        }
        t6106.F02 = tgyh;
        t6106.F03 = IntegerParser.parse(score);
    }
    
    private void saveT6106(T6106 t6106, Connection connection)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S61.T6106 SET F02=?,F03=?,F04=?,F05=?,F07=?",
                PreparedStatement.RETURN_GENERATED_KEYS))
        {
            pstmt.setInt(1, t6106.F02);
            pstmt.setInt(2, t6106.F03);
            pstmt.setTimestamp(3, t6106.F04);
            pstmt.setString(4, t6106.F05.name());
            pstmt.setTimestamp(5, t6106.F07);
            pstmt.execute();
        }
    }
    
    private void updateT6105(T6106 t6106, Connection connection, Timestamp now)
        throws Throwable
    {
        if (t6106.F02 <= 0)
        {
            throw new ParameterException("赠送积分用户不存在");
        }
        addScoreAccount(connection, t6106.F02, now);
        T6105 t6105 = getT6105(t6106.F02, connection);
        if (null == t6105)
        {
            throw new ParameterException("用户积分账户不存在");
        }
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S61.T6105 SET F03=F03+?,F07=? WHERE F02 = ?"))
        {
            pstmt.setInt(1, t6106.F03);
            pstmt.setTimestamp(2, now);
            pstmt.setInt(3, t6106.F02);
            pstmt.execute();
        }
    }
    
    /**
     * <用户积分账户>
     * @return T6105
     */
    private void addScoreAccount(Connection connection, int F02, Timestamp now)
        throws Throwable
    {
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT F01 FROM S61.T6105 WHERE F02=?"))
        {
            pstmt.setInt(1, F02);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (!resultSet.next())
                {
                    try (PreparedStatement pstmt1 =
                        connection.prepareStatement("INSERT INTO S61.T6105 SET F02 = ?, F06 = ?, F07 = ?"))
                    {
                        pstmt1.setInt(1, F02);
                        pstmt1.setTimestamp(2, now);
                        pstmt1.setTimestamp(3, now);
                        pstmt1.execute();
                    }
                }
            }
        }
    }
    
    /**
     * <用户积分账户>
     * @return T6105
     * @throws SQLException 
     */
    private T6105 getT6105(int F02, Connection connection)
        throws SQLException
    {
        
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01,F02,F03,F04,F05,F06,F07 FROM S61.T6105 WHERE F01=(SELECT F01 FROM S61.T6105 WHERE F02=? LIMIT 1) FOR UPDATE"))
        {
            pstmt.setInt(1, F02);
            try (ResultSet rs = pstmt.executeQuery())
            {
                T6105 t6105 = null;
                if (rs.next())
                {
                    t6105 = new T6105();
                    t6105.F01 = rs.getInt(1);
                    t6105.F02 = rs.getInt(2);
                    t6105.F03 = rs.getInt(3);
                    t6105.F04 = rs.getInt(4);
                    t6105.F05 = rs.getInt(5);
                    t6105.F06 = rs.getTimestamp(6);
                    t6105.F07 = rs.getTimestamp(7);
                }
                return t6105;
            }
        }
    }
    
}
