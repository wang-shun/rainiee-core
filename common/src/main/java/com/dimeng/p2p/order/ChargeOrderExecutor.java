package com.dimeng.p2p.order;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
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
import com.dimeng.p2p.OrderType;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.entities.T6102;
import com.dimeng.p2p.S61.entities.T6105;
import com.dimeng.p2p.S61.entities.T6106;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S61.enums.T6106_F05;
import com.dimeng.p2p.S61.enums.T6123_F05;
import com.dimeng.p2p.S63.entities.T6356;
import com.dimeng.p2p.S63.enums.T6340_F03;
import com.dimeng.p2p.S63.enums.T6340_F04;
import com.dimeng.p2p.S63.enums.T6356_F03;
import com.dimeng.p2p.S63.enums.T6356_F04;
import com.dimeng.p2p.S65.entities.T6501;
import com.dimeng.p2p.S65.entities.T6502;
import com.dimeng.p2p.S65.entities.T6517;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.S65.enums.T6501_F07;
import com.dimeng.p2p.S65.enums.T6501_F11;
import com.dimeng.p2p.service.ActivityCommon;
import com.dimeng.p2p.variables.defines.LetterVariable;
import com.dimeng.p2p.variables.defines.MallVariavle;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.Formater;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BigDecimalParser;
import com.dimeng.util.parser.BooleanParser;
import com.dimeng.util.parser.DateTimeParser;
import com.dimeng.util.parser.IntegerParser;

@ResourceAnnotation
public class ChargeOrderExecutor extends AbstractOrderExecutor
{
    
    public ChargeOrderExecutor(ResourceProvider resourceProvider)
    {
        super(resourceProvider);
    }
    
    @Override
    public Class<? extends Resource> getIdentifiedType()
    {
        return ChargeOrderExecutor.class;
    }
    
    @Override
    protected void doConfirm(SQLConnection connection, int orderId, Map<String, String> params)
        throws Throwable
    {
        try
        {
            ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
            T6502 t6502 = selectT6502(connection, orderId); // 充值订单信息
            if (t6502 == null)
            {
                throw new ParameterException("订单不存在");
            }
            int accountId = t6502.F02;
            Timestamp t = getCurrentTimestamp(connection);
            int pid = getPTID(connection);
            
            // 平台往来账户信息
            T6101 cT6101 = selectT6101(connection, pid, T6101_F03.WLZH, true);
            if (cT6101 == null)
            {
                throw new LogicalException("平台往来账户不存在");
            }
            BigDecimal a = feeRate();
            
            if (t6502.F02 == pid)
            {
                // 插入平台账户资金流水
                cT6101.F06 = cT6101.F06.add(t6502.F03);
                T6102 cT6102 = new T6102();
                cT6102.F02 = cT6101.F01;
                cT6102.F03 = FeeCode.CZ;
                cT6102.F04 = cT6101.F01;
                cT6102.F05 = t;
                cT6102.F06 = t6502.F03;
                cT6102.F08 = cT6101.F06;
                cT6102.F09 = "平台充值";
                insertT6102(connection, cT6102);
                if (a != null && a.compareTo(BigDecimal.ZERO) > 0)
                {
                    // 充值成本
                    BigDecimal cb = fee(t6502, a, params);
                    if (cb.compareTo(BigDecimal.ZERO) > 0)
                    {
                        cT6101.F06 = cT6101.F06.subtract(cb);
                        cT6102.F03 = FeeCode.CZ_CB;
                        cT6102.F04 = cT6102.F01;
                        cT6102.F05 = t;
                        cT6102.F06 = BigDecimal.ZERO;
                        cT6102.F07 = cb;
                        cT6102.F08 = cT6101.F06;
                        cT6102.F09 = "平台充值成本";
                        insertT6102(connection, cT6102);
                    }
                }
                updateT6101(connection, cT6101.F06, cT6101.F01);
                return;
            }
            // 用户往来账户信息
            T6101 uT6101 = selectT6101(connection, accountId, T6101_F03.WLZH, true);
            if (uT6101 == null)
            {
                throw new LogicalException("用户往来账户不存在");
            }
            // 更新有效推广奖励统计
            try (PreparedStatement ps = connection.prepareStatement("SELECT F01 FROM S63.T6310 WHERE F01=? FOR UPDATE"))
            {
                ps.setInt(1, accountId);
                ps.execute();
            }
            try (PreparedStatement ps = connection.prepareStatement("SELECT F01 FROM S63.T6311 FOR UPDATE"))
            {
                ps.execute();
            }
            // 插入用户资金流水
            uT6101.F06 = uT6101.F06.add(t6502.F03);
            T6102 uT6102 = new T6102();
            uT6102.F02 = uT6101.F01;
            uT6102.F03 = FeeCode.CZ;
            uT6102.F04 = uT6101.F01;
            uT6102.F05 = t;
            uT6102.F06 = t6502.F03;
            uT6102.F08 = uT6101.F06;
            uT6102.F09 = "账户充值";
            insertT6102(connection, uT6102);
            if (t6502.F05.compareTo(BigDecimal.ZERO) > 0)
            {
                uT6101.F06 = uT6101.F06.subtract(t6502.F05);
                uT6102.F03 = FeeCode.CZ_SXF;
                uT6102.F04 = cT6101.F01;
                uT6102.F05 = t;
                uT6102.F06 = BigDecimal.ZERO;
                uT6102.F07 = t6502.F05;
                uT6102.F08 = uT6101.F06;
                uT6102.F09 = "充值手续费";
                insertT6102(connection, uT6102);
            }
            updateT6101(connection, uT6101.F06, uT6101.F01);
            // 插入平台账户资金流水
            T6102 cT6102 = new T6102();
            cT6102.F02 = cT6101.F01;
            if (t6502.F05.compareTo(BigDecimal.ZERO) > 0)
            {
                cT6101.F06 = cT6101.F06.add(t6502.F05);
                cT6102.F03 = FeeCode.CZ_SXF;
                cT6102.F04 = uT6101.F01;
                cT6102.F05 = t;
                cT6102.F06 = t6502.F05;
                cT6102.F08 = cT6101.F06;
                cT6102.F09 = "用户充值手续费";
                insertT6102(connection, cT6102);
            }
            // 获取支付公司收取的手续费率
            if (a != null && a.compareTo(BigDecimal.ZERO) > 0)
            {
                // 充值成本
                BigDecimal cb = fee(t6502, a, params);
                if (cb.compareTo(BigDecimal.ZERO) > 0)
                {
                    cT6101.F06 = cT6101.F06.subtract(cb);
                    cT6102.F03 = FeeCode.CZ_CB;
                    cT6102.F04 = cT6102.F01;
                    cT6102.F05 = t;
                    cT6102.F06 = BigDecimal.ZERO;
                    cT6102.F07 = cb;
                    cT6102.F08 = cT6101.F06;
                    cT6102.F09 = "充值成本";
                    insertT6102(connection, cT6102);
                }
            }
            // 更新平台往来账户
            updateT6101(connection, cT6101.F06, cT6101.F01);
            // 更新订单状态
            if (params != null && !StringHelper.isEmpty(params.get("paymentOrderId")))
            {
                updateT6502(connection, params.get("paymentOrderId"), orderId);
            }
            // 推广处理
            if (BooleanParser.parse(configureProvider.getProperty(SystemVariable.ACCOUNT_SFTG)))
            {
                expand(connection, t6502, uT6101, cT6101);
            }
            
            try (ServiceSession serviceSession =
                resourceProvider.getResource(ServiceProvider.class).createServiceSession())
            {
                //充值送红包和加息券
                ActivityCommon activityCommon = serviceSession.getService(ActivityCommon.class);
                //首次充值送红包
                activityCommon.sendActivity(connection,
                    accountId,
                    T6340_F03.redpacket.name(),
                    T6340_F04.firstrecharge.name(),
                    t6502.F03,
                    0);
                //首次充值送加息券
                activityCommon.sendActivity(connection,
                    accountId,
                    T6340_F03.interest.name(),
                    T6340_F04.firstrecharge.name(),
                    t6502.F03,
                    0);
                //首次充值送体验金
                activityCommon.sendActivity(connection,
                    accountId,
                    T6340_F03.experience.name(),
                    T6340_F04.firstrecharge.name(),
                    t6502.F03,
                    0);
                //单笔充值送红包
                activityCommon.sendActivity(connection,
                    accountId,
                    T6340_F03.redpacket.name(),
                    T6340_F04.recharge.name(),
                    t6502.F03,
                    0);
                //单笔充值送加息券
                activityCommon.sendActivity(connection,
                    accountId,
                    T6340_F03.interest.name(),
                    T6340_F04.recharge.name(),
                    t6502.F03,
                    0);
                //单笔充值送体验金
                activityCommon.sendActivity(connection,
                    accountId,
                    T6340_F03.experience.name(),
                    T6340_F04.recharge.name(),
                    t6502.F03,
                    0);
                //获取当前充值用户的推广人ID
                int exid = searchExtendId(connection, t6502.F02);
                if (exid > 0)
                {
                    //推荐用户首次充值奖励送红包
                    activityCommon.sendActivity(connection,
                        exid,
                        T6340_F03.redpacket.name(),
                        T6340_F04.tjsccz.name(),
                        t6502.F03,
                        accountId);
                    //推荐用户首次充值奖励送加息券
                    activityCommon.sendActivity(connection,
                        exid,
                        T6340_F03.interest.name(),
                        T6340_F04.tjsccz.name(),
                        t6502.F03,
                        accountId);
                    //推荐用户首次充值奖励送体验金
                    activityCommon.sendActivity(connection,
                        exid,
                        T6340_F03.experience.name(),
                        T6340_F04.tjsccz.name(),
                        t6502.F03,
                        accountId);
                }
            }
            
            boolean is_mall = Boolean.parseBoolean(configureProvider.getProperty(MallVariavle.IS_MALL));
            if (is_mall)
            {
                //充值赠送积分
                giveScore(connection, accountId, t6502.F03, t);
            }
            
            //充值成功，发站内信
            T6110 t6110 = selectT6110(connection, accountId);
            Envionment envionment = configureProvider.createEnvionment();
            envionment.set("datetime", DateTimeParser.format(t));
            envionment.set("name", t6110.F02);
            envionment.set("amount", Formater.formatAmount(t6502.F03));
            String content = configureProvider.format(LetterVariable.CHARGE_SUCCESS, envionment);
            sendLetter(connection, accountId, "充值成功", content);
            
        }
        catch (Exception e)
        {
            logger.error(e, e);
            throw e;
        }
    }
    
    protected BigDecimal feeRate()
        throws Throwable
    {
        return BigDecimal.ZERO;
        /*ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        if (PaymentInstitution.ALLINPAY.getInstitutionCode() == type)
        {
            return BigDecimalParser.parse(configureProvider.getProperty(AllinpayVaribles.CHARGE_RATE_TL));
        }
        else if (PaymentInstitution.SHUANGQIAN.getInstitutionCode() == t6502.F07)
        {
            return BigDecimalParser.parse(configureProvider.getProperty(ShuangqianVaribles.CHARGE_RATE_SQ));
        }
        else if (PaymentInstitution.LIANLIANGATE.getInstitutionCode() == t6502.F07)
        {
            return BigDecimalParser.parse(configureProvider.getProperty(LianLianPayVariable.CHARGE_RATE_LIANLIAN));
        }
        else if (PaymentInstitution.DINPAY.getInstitutionCode() == t6502.F07)
        {
            return BigDecimalParser.parse(configureProvider.getProperty(DinpayVaribles.CHARGE_RATE_DINPAY));
        }
        else if (PaymentInstitution.SINAPAY.getInstitutionCode() == t6502.F07)
        {
            return BigDecimalParser.parse(configureProvider.getProperty(SinaGatePayVariavle.CHARGE_RATE_SINAGATE));
        }
        else if (PaymentInstitution.HUICHAOGATE.getInstitutionCode() == t6502.F07)
        {
            return BigDecimalParser.parse(configureProvider.getProperty(HuiChaoVariavle.CHARGE_RATE_HUICHAOGAT));
        }
        else if (PaymentInstitution.GFBPAY.getInstitutionCode() == t6502.F07)
        {
            return BigDecimalParser.parse(configureProvider.getProperty(GFBPayVariable.CHARGE_RATE_GFB));
        }
        else if (PaymentInstitution.BAOFU.getInstitutionCode() == t6502.F07)
        {
            return BigDecimalParser.parse(configureProvider.getProperty(BaoFuPayVariavle.CHARGE_RATE_BF));
        }
        else if (PaymentInstitution.BOCOM.getInstitutionCode() == t6502.F07)
        {
            return BigDecimalParser.parse(configureProvider.getProperty(BOCOMPayVariable.CHARGE_RATE_BOCOM));
        }
        else if (PaymentInstitution.CHINABANK.getInstitutionCode() == t6502.F07)
        {
            return BigDecimalParser.parse(configureProvider.getProperty(ChinaBankVaribles.CHARGE_RATE_CHINABANK));
        }
        else if (PaymentInstitution.KJTPAY.getInstitutionCode() == t6502.F07)
        {
            return BigDecimalParser.parse(configureProvider.getProperty(HaierVaribles.CHARGE_RATE_KJT));
        }
        else if (PaymentInstitution.HEEPAY.getInstitutionCode() == t6502.F07)
        {
            return BigDecimalParser.parse(configureProvider.getProperty(HeepayVaribles.CHARGE_RATE_HEEPAY));
        }
        else if (PaymentInstitution.YIFUPAY.getInstitutionCode() == t6502.F07)
        {
            return BigDecimalParser.parse(configureProvider.getProperty(YifuPayVariavle.YF_CHARGE_RATE));
        }
        else if (PaymentInstitution.ALLINWAPPAY.getInstitutionCode() == t6502.F07)
        {
            return BigDecimalParser.parse(configureProvider.getProperty(AllinpayVaribles.CHARGE_RATE_TL));
        }
        else
        {
            return BigDecimal.ZERO;
        }*/
    }
    
    private BigDecimal fee(T6502 t6502, BigDecimal bg, Map<String, String> params)
    {
        if (params != null && !StringHelper.isEmpty(params.get("feeAmt")))
        {
            return BigDecimalParser.parse(params.get("feeAmt")).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        else
        {
            return t6502.F03.multiply(bg).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
    }
    
    /**
     * 推广处理
     * 
     * @param amount
     *            充值金额
     * @param id
     *            充值订单id
     * @throws Throwable
     */
    protected void expand(Connection connection, T6502 t6502, T6101 uT6101, T6101 cT6101)
        throws Throwable
    {
        ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        Timestamp t = new Timestamp(System.currentTimeMillis());
        BigDecimal exjl = null;
        int accountId = t6502.F02;
        int exid = 0;// 推广人id
        // 充值次数
        int chargeCount = selectChargeCount(connection, T6501_F03.CG, OrderType.CHARGE.orderType(), accountId);
        
        if (chargeCount > 0)
        {
            return;
        }
        
        exid = searchExtendId(connection, accountId);
        //无推广人 返回
        if (exid <= 0)
        {
            return;
        }
        
        try (PreparedStatement ps = connection.prepareStatement("UPDATE S63.T6311 SET F04 = ?, F06 = ? WHERE F03 = ?"))
        {
            ps.setBigDecimal(1, t6502.F03);
            ps.setTimestamp(2, t);
            ps.setInt(3, accountId);
            ps.execute();
        }
        
        if (t6502.F03.compareTo(new BigDecimal(configureProvider.getProperty(SystemVariable.TG_YXCZJS))) < 0)
        {
            return;
        }
        
        // 推广人未托管不奖励
        boolean tg = BooleanParser.parse(configureProvider.getProperty(SystemVariable.SFZJTG));
        if (tg)
        {
            try (PreparedStatement ps = connection.prepareStatement("SELECT F01 FROM S61.T6119 WHERE F01 = ?"))
            {
                ps.setInt(1, exid);
                try (ResultSet resultSet = ps.executeQuery())
                {
                    if (!resultSet.next())
                    {
                        return;
                    }
                }
            }
        }
        // 判断获取奖励次数是否超过上限(当月)
        int excount = 0;
        try (PreparedStatement ps =
            connection.prepareStatement("SELECT COUNT(F02) FROM S63.T6311 WHERE F06 >= ? AND F06 <= ? AND F02=? AND F05>0"))
        {
            Calendar monthCal = Calendar.getInstance();
            monthCal.setTime(new Date());
            monthCal.set(Calendar.DATE, monthCal.getActualMinimum(Calendar.DATE));
            monthCal.set(Calendar.HOUR_OF_DAY, 0);
            monthCal.set(Calendar.MINUTE, 0);
            monthCal.set(Calendar.SECOND, 0);
            ps.setDate(1, new java.sql.Date(monthCal.getTimeInMillis()));
            
            monthCal.set(Calendar.DATE, monthCal.getActualMaximum(Calendar.DATE));
            monthCal.set(Calendar.HOUR_OF_DAY, 23);
            monthCal.set(Calendar.MINUTE, 59);
            monthCal.set(Calendar.SECOND, 59);
            ps.setDate(2, new java.sql.Date(monthCal.getTimeInMillis()));
            
            ps.setInt(3, exid);
            try (ResultSet resultSet = ps.executeQuery())
            {
                if (resultSet.next())
                {
                    excount = resultSet.getInt(1);
                }
            }
        }
        if (excount >= IntegerParser.parse(configureProvider.getProperty(SystemVariable.TG_YXTGSX)))
        {
            return;
        }
        // 计算奖励金额
        exjl = new BigDecimal(configureProvider.getProperty(SystemVariable.TG_YXTGJL));
        try (PreparedStatement ps = connection.prepareStatement("UPDATE S63.T6311 SET F05=? WHERE F03=?"))
        {
            ps.setBigDecimal(1, exjl);
            ps.setInt(2, accountId);
            ps.execute();
        }
        try (PreparedStatement ps = connection.prepareStatement("UPDATE S63.T6310 SET F05=F05+? WHERE F01=?"))
        {
            ps.setBigDecimal(1, exjl);
            ps.setInt(2, exid);
            ps.executeUpdate();
        }
        
        if (!BooleanParser.parse(configureProvider.getProperty(SystemVariable.SFZJTG)))
        {
            updateT6101(connection, exjl, exid, T6101_F03.WLZH);
            T6101 eT6101 = selectT6101(connection, exid, T6101_F03.WLZH, true);
            T6102 eT6102 = new T6102();
            eT6102.F02 = eT6101.F01;
            eT6102.F03 = FeeCode.TG_YX;
            eT6102.F04 = cT6101.F01;
            eT6102.F05 = t;
            eT6102.F06 = exjl;
            eT6102.F08 = eT6101.F06;
            eT6102.F09 = "有效推广奖励";
            insertT6102(connection, eT6102);
            
            // 平台账户扣除奖励金额
            updateT6101(connection, exjl.multiply(new BigDecimal(-1)), cT6101.F02, T6101_F03.WLZH);
            cT6101 = selectT6101(connection, cT6101.F02, T6101_F03.WLZH, true);
            T6102 ecT6102 = new T6102();
            ecT6102.F02 = cT6101.F01;
            ecT6102.F03 = FeeCode.TG_YX;
            ecT6102.F04 = eT6101.F01;
            ecT6102.F05 = t;
            ecT6102.F07 = exjl;
            ecT6102.F08 = cT6101.F06;
            ecT6102.F09 = "有效推广奖励";
            insertT6102(connection, ecT6102);
        }
        else
        {
            // 插入转账订单
            T6501 zzt6501 = new T6501();
            zzt6501.F02 = OrderType.TRANSFER.orderType();
            zzt6501.F03 = T6501_F03.DTJ;
            zzt6501.F07 = T6501_F07.XT;
            zzt6501.F08 = cT6101.F02;
            zzt6501.F04 = getCurrentTimestamp(connection);
            zzt6501.F13 = exjl;
            int ordId = insertT6501(connection, zzt6501);
            T6517 t6517 = new T6517();
            t6517.F01 = ordId;
            t6517.F02 = exjl;
            t6517.F03 = cT6101.F02;
            t6517.F04 = exid;
            t6517.F05 = "有效推广奖励";
            t6517.F06 = FeeCode.TG_YX;
            insertT6517(connection, t6517);
        }
        
        // 站内信
        int letterId = insertT6123(connection, exid, "有效推广奖励", t, T6123_F05.WD);
        String tem = configureProvider.getProperty(LetterVariable.TG_YXJL);
        Envionment envionment = configureProvider.createEnvionment();
        envionment.set("cz", t6502.F03.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        envionment.set("jl", exjl.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        insertT6124(connection, letterId, StringHelper.format(tem, envionment));
    }
    
    /**
     * 根据用户ID查询推广人
     * @param accountIdd
     * @return
     * @throws SQLException
     */
    protected int searchExtendId(Connection connection, int accountId)
        throws SQLException
    {
        // 充值推广奖励
        String code = null;// 邀请码
        try (PreparedStatement ps = connection.prepareStatement("SELECT F03 FROM S61.T6111 WHERE F01=?"))
        {
            ps.setInt(1, accountId);
            try (ResultSet resultSet = ps.executeQuery())
            {
                if (resultSet.next())
                {
                    code = resultSet.getString(1);
                }
            }
        }
        if (StringHelper.isEmpty(code))
        {
            return 0;
        }
        try (PreparedStatement ps = connection.prepareStatement("SELECT F01 FROM S61.T6111 WHERE F02=? LIMIT 1"))
        {
            ps.setString(1, code);
            try (ResultSet resultSet = ps.executeQuery())
            {
                if (resultSet.next())
                {
                    return resultSet.getInt(1);
                }
            }
        }
        return 0;
    }
    
    @Override
    protected void doSubmit(SQLConnection connection, int orderId, Map<String, String> params)
        throws Throwable
    {
        
    }
    
    protected void insertT6517(Connection connection, T6517 t6517)
        throws Throwable
    {
        try (PreparedStatement ps =
            connection.prepareStatement("INSERT INTO S65.T6517 SET F01 = ?, F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?"))
        {
            ps.setInt(1, t6517.F01);
            ps.setBigDecimal(2, t6517.F02);
            ps.setInt(3, t6517.F03);
            ps.setInt(4, t6517.F04);
            ps.setString(5, t6517.F05);
            ps.setInt(6, t6517.F06);
            ps.execute();
        }
    }
    
    /*protected int insertT6501(Connection connection, T6501 t6501)
        throws Throwable
    {
        try (PreparedStatement ps =
            connection.prepareStatement("INSERT INTO S65.T6501 SET F02 = ?, F03 = ?, F04 = ?, F07 = ?, F08 = ? ",
                Statement.RETURN_GENERATED_KEYS))
        {
            ps.setInt(1, t6501.F02);
            ps.setString(2, t6501.F03.name());
            ps.setTimestamp(3, getCurrentTimestamp(connection));
            ps.setString(4, t6501.F07.name());
            ps.setInt(5, t6501.F08);
            ps.execute();
            try (ResultSet resultSet = ps.getGeneratedKeys())
            {
                if (resultSet.next())
                {
                    return resultSet.getInt(1);
                }
            }
        }
        return 0;
    }*/
    
    protected int insertT6501(Connection connection, T6501 entity)
        throws Throwable
    {
        int orderId = 0;
        StringBuilder sql =
            new StringBuilder(
                "INSERT INTO S65.T6501 SET F02 = ?,F03 = ?,F04 = ?,F05 = ?,F06 = ?,F07 = ?,F08 = ?,F10 = ?,F11 = ?,F12 = ?,F13 = ?");
        if (entity.F09 != null)
        {
            sql.append(",F09 = ?");
        }
        try (PreparedStatement pstmt =
            connection.prepareStatement(sql.toString(), PreparedStatement.RETURN_GENERATED_KEYS))
        {
            pstmt.setInt(1, entity.F02);
            pstmt.setString(2, entity.F03.name());
            pstmt.setTimestamp(3, entity.F04);
            pstmt.setTimestamp(4, entity.F05);
            pstmt.setTimestamp(5, entity.F06);
            pstmt.setString(6, entity.F07.name());
            pstmt.setInt(7, entity.F08);
            pstmt.setString(8, entity.F10);
            pstmt.setString(9, entity.F11 == null ? T6501_F11.F.name() : entity.F11.name());
            pstmt.setString(10, entity.F12);
            pstmt.setBigDecimal(11, entity.F13);
            if (entity.F09 != null)
            {
                pstmt.setInt(12, entity.F09);
            }
            pstmt.execute();
            try (ResultSet resultSet = pstmt.getGeneratedKeys();)
            {
                if (resultSet.next())
                {
                    orderId = resultSet.getInt(1);
                }
            }
        }
        if (orderId == 0)
        {
            logger.error("ChargeOrderExecutor.insertT6501():数据库异常");
            throw new SQLException("数据库异常");
        }
        return orderId;
    }
    
    protected T6502 selectT6502(Connection connection, int F01)
        throws SQLException
    {
        T6502 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08 FROM S65.T6502 WHERE T6502.F01 = ? FOR UPDATE"))
        {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6502();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getBigDecimal(3);
                    record.F04 = resultSet.getBigDecimal(4);
                    record.F05 = resultSet.getBigDecimal(5);
                    record.F06 = resultSet.getString(6);
                    record.F07 = resultSet.getInt(7);
                    record.F08 = resultSet.getString(8);
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
    
    protected void updateT6101(Connection connection, BigDecimal F01, int F02, T6101_F03 F03)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S61.T6101 SET F06 = F06 + ?, F07 = ? WHERE F02 = ? AND F03 = ?"))
        {
            pstmt.setBigDecimal(1, F01);
            pstmt.setTimestamp(2, getCurrentTimestamp(connection));
            pstmt.setInt(3, F02);
            pstmt.setString(4, F03.name());
            pstmt.execute();
        }
    }
    
    private void updateT6502(Connection connection, String F01, int F02)
        throws SQLException
    {
        try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S65.T6502 SET F08 = ? WHERE F01 = ?"))
        {
            pstmt.setString(1, F01);
            pstmt.setInt(2, F02);
            pstmt.execute();
        }
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
    protected int selectChargeCount(Connection connection, T6501_F03 F03, int F02, int F07)
        throws SQLException
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
    
    protected int insertT6123(Connection connection, int F02, String F03, Timestamp F04, T6123_F05 F05)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S61.T6123 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?",
                PreparedStatement.RETURN_GENERATED_KEYS))
        {
            pstmt.setInt(1, F02);
            pstmt.setString(2, F03);
            pstmt.setTimestamp(3, F04);
            pstmt.setString(4, F05.name());
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
    
    protected void giveScore(Connection connection, Integer userId, BigDecimal amount, Timestamp now)
    {
        try
        {
            T6106 t6106 = new T6106();
            t6106.F02 = userId;
            T6356 t6356 = getT6356(connection);
            if (null == t6356)
            {
                throw new ParameterException("积分规则不存在");
            }
            String score = t6356.F02;
            if (StringHelper.isEmpty(score) || "0".equals(score))
            {
                throw new ParameterException("积分值错误：" + score);
            }
            t6106.F05 = T6106_F05.charge;
            setT6106F03(t6106, amount, score);
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
     * <充值积分规则设置表>
     * @return T6356
     * @throws SQLException 
     */
    protected T6356 getT6356(Connection connection)
        throws SQLException
    {
        
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01,F02,F03,F04,F05,F06 FROM S63.T6356 WHERE F03=? AND F04=? LIMIT 1"))
        {
            pstmt.setString(1, T6106_F05.charge.name());
            pstmt.setString(2, T6356_F04.on.name());
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
     * <设置充值积分值>
     * <功能详细描述>
     * @param t6106
     * @param amount
     * @param score
     */
    protected void setT6106F03(T6106 t6106, BigDecimal amount, String score)
        throws Throwable
    {
        int amountInt = amount.intValue();
        String[] scores = score.split(",");
        int amountMark = IntegerParser.parse(scores[0]);
        if (amountMark > amountInt)
        {
            throw new ParameterException("不符合充值赠送积分规则,amount:" + amount);
        }
        t6106.F03 = amountInt / amountMark * IntegerParser.parse(scores[1]);
    }
    
    protected void saveT6106(T6106 t6106, Connection connection)
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
    
    protected void updateT6105(T6106 t6106, Connection connection, Timestamp now)
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
    protected void addScoreAccount(Connection connection, int F02, Timestamp now)
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
