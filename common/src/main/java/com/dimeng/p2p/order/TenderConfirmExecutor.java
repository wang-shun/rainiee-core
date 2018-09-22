package com.dimeng.p2p.order;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.config.Envionment;
import com.dimeng.framework.data.sql.SQLConnection;
import com.dimeng.framework.resource.Resource;
import com.dimeng.framework.resource.ResourceAnnotation;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.OrderType;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.entities.T6102;
import com.dimeng.p2p.S61.entities.T6103;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S61.enums.T6103_F06;
import com.dimeng.p2p.S62.entities.*;
import com.dimeng.p2p.S62.enums.*;
import com.dimeng.p2p.S63.entities.T6342;
import com.dimeng.p2p.S63.enums.T6342_F04;
import com.dimeng.p2p.S65.entities.*;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.S65.enums.T6501_F07;
import com.dimeng.p2p.S65.enums.T6501_F11;
import com.dimeng.p2p.common.SMSUtils;
import com.dimeng.p2p.common.enums.AutoSetStatus;
import com.dimeng.p2p.variables.defines.LetterVariable;
import com.dimeng.p2p.variables.defines.MsgVariavle;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.p2p.variables.defines.URLVariable;
import com.dimeng.p2p.variables.defines.smses.SmsVaribles;
import com.dimeng.util.DateHelper;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * 放款
 */
@ResourceAnnotation
public class TenderConfirmExecutor extends AbstractOrderExecutor
{
    
    public TenderConfirmExecutor(ResourceProvider resourceProvider)
    {
        super(resourceProvider);
    }
    
    @Override
    public Class<? extends Resource> getIdentifiedType()
    {
        return TenderConfirmExecutor.class;
    }
    
    @Override
    protected void doConfirm(SQLConnection connection, int orderId, Map<String, String> params)
        throws Throwable
    {
        try
        {
            // 查询订单详情
            T6505 t6505 = selectT6505(connection, orderId);
            if (t6505 == null)
            {
                throw new LogicalException("订单信息不存在");
            }
            // 查询标信息
            T6230 t6230 = selectT6230(connection, t6505.F03);
            if (t6230 == null)
            {
                throw new LogicalException("投信息不存在");
            }
            if (t6230.F20 != T6230_F20.DFK)
            {
                throw new LogicalException("不是待放款状态,不能放款");
            }
            // 查询投资记录
            T6250 t6250 = selectT6250(connection, t6505.F04, T6250_F07.F);
            if (t6250 == null)
            {
                throw new LogicalException("投资记录不存在");
            }
            // 锁定投资人锁定账户
            T6101 sdzh = selectT6101(connection, t6505.F02, T6101_F03.SDZH, true);
            if (sdzh == null)
            {
                throw new LogicalException("投资人锁定账户不存在");
            }
            // 锁定投资人往来账户
            T6101 tzrwlzh = selectT6101(connection, t6505.F02, T6101_F03.WLZH, true);
            if (tzrwlzh == null)
            {
                throw new LogicalException("投资人往来账户不存在");
            }
            // 锁定借款人往来账户
            T6101 wlzh = null;
            if (t6230.F27 == T6230_F27.S)
            {
                T6240 t6240 = selectT6240(connection, t6230.F01);
                wlzh = selectT6101(connection, t6240.F02, T6101_F03.WLZH, true);
            }
            else
            {
                wlzh = selectT6101(connection, t6230.F02, T6101_F03.WLZH, true);
            }
            if (wlzh == null)
            {
                throw new LogicalException("借款人往来账户不存在");
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
            
            // 红包
            T6292 t6292 = selectT6292(connection, t6505.F04, T6292_F07.F);
            BigDecimal sjAmount = t6505.F05;
            if (t6292 != null)
            {
                sjAmount = sjAmount.subtract(t6292.F04);
                sjAmount = sjAmount.compareTo(BigDecimal.ZERO) == -1 ? BigDecimal.ZERO : sjAmount;
                // 红包投资
                sendRedPacket(connection, t6292, wlzh, ptwlzh, t6230);
            }
            
            String comment = String.format("散标投资放款:%s，标题：%s", t6230.F25, t6230.F03);
            
            {
                
                sdzh.F06 = sdzh.F06.subtract(sjAmount);
                if (sdzh.F06.compareTo(BigDecimal.ZERO) < 0)
                {
                    throw new LogicalException("投资人锁定账户余额不足,投资人ID：" + sdzh.F02);
                }
                updateT6101(connection, sdzh.F06, sdzh.F01);
                // 资金流水
                T6102 t6102 = new T6102();
                t6102.F02 = sdzh.F01;
                t6102.F03 = FeeCode.TZ;
                t6102.F04 = wlzh.F01;
                t6102.F07 = sjAmount;
                t6102.F08 = sdzh.F06;
                t6102.F09 = comment;
                insertT6102(connection, t6102);
            }
            {
                // 增加借款人往来账户资金
                wlzh.F06 = wlzh.F06.add(sjAmount);
                updateT6101(connection, wlzh.F06, wlzh.F01);
                // 资金流水
                T6102 t6102 = new T6102();
                t6102.F02 = wlzh.F01;
                t6102.F03 = FeeCode.JK;
                t6102.F04 = sdzh.F01;
                // 借款人资金不要减去红包
                t6102.F06 = t6505.F05;
                t6102.F08 = wlzh.F06;
                t6102.F09 = comment;
                insertT6102(connection, t6102);
            }
            int creditorId = 0;
            Date currentDate = getCurrentDate(connection);
            {
                // 插入债权
                T6251 t6251 = new T6251();
                t6251.F02 = zqCode(t6250.F01);
                t6251.F03 = t6230.F01;
                t6251.F04 = t6505.F02;
                t6251.F05 = t6250.F04;
                t6251.F06 = t6250.F04;
                t6251.F07 = t6250.F04;
                t6251.F08 = T6251_F08.F;
                t6251.F09 = currentDate;
                t6251.F10 = new Date(currentDate.getTime() + t6230.F19 * DateHelper.DAY_IN_MILLISECONDS);
                t6251.F11 = t6250.F01;
                creditorId = insertT6251(connection, t6251);
            }
            // 更新投资记录为已放款
            updateT6250(connection, T6250_F08.S, t6250.F01);
            // 更新借款人的自动投资状态,借款用户在获得借款时会自动关闭自动投资，以避免借款被用作自动投资资金。
            updateT6280(connection, t6230.F02);
            // 投资奖励
            ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
            if (BooleanParser.parse(configureProvider.getProperty(SystemVariable.ACCOUNT_SFTG)))
            {
                tzjl(connection, t6230, t6505, wlzh, ptwlzh);
            }
            
            // 如果奖励标，则赠送投资奖励
            giveBidReward(connection, t6505, t6230, sdzh, tzrwlzh, wlzh, ptwlzh);
            
            {
                // 判断是否全部投资均已成功放款,是则更新标状态为已流标
                int count = 0;
                try (PreparedStatement pstmt =
                    connection.prepareStatement("SELECT COUNT(*) FROM S62.T6250 WHERE F02 = ? AND F07 = 'F' AND F08 = 'F'"))
                {
                    pstmt.setInt(1, t6230.F01);
                    try (ResultSet resultSet = pstmt.executeQuery())
                    {
                        if (resultSet.next())
                        {
                            count = resultSet.getInt(1);
                        }
                    }
                }
                
                if (count == 0)
                {
                    resourceProvider.log("=============放款完成");
                    // 生成还款计划
                    hkjh(connection, t6230);
                    // 更新标状态为还款中
                    updateT6230(connection, t6230.F01);
                    // 更新标扩展信息，放款时间
                    updateT6231(connection, t6230.F01);
                    // 收成交服务费
                    T6238 t6238 = selectT6238(connection, t6230.F01);
                    if (t6238 != null && t6238.F02.compareTo(BigDecimal.ZERO) > 0)
                    {
                        resourceProvider.log("==============计算成交服务费");
                        BigDecimal fwf = this.getCJFee(connection, t6238, t6230);
                        {
                            // 平台资金增加
                            ptwlzh.F06 = ptwlzh.F06.add(fwf);
                            updateT6101(connection, ptwlzh.F06, ptwlzh.F01);
                            T6102 t6102 = new T6102();
                            t6102.F02 = ptwlzh.F01;
                            t6102.F03 = FeeCode.CJFWF;
                            t6102.F04 = wlzh.F01;
                            t6102.F06 = fwf;
                            t6102.F08 = ptwlzh.F06;
                            t6102.F09 = String.format("散标成交服务费:%s，标题：%s", t6230.F25, t6230.F03);
                            insertT6102(connection, t6102);
                        }
                        {
                            // 借款人账户减少
                            wlzh.F06 = wlzh.F06.subtract(fwf);
                            updateT6101(connection, wlzh.F06, wlzh.F01);
                            T6102 t6102 = new T6102();
                            t6102.F02 = wlzh.F01;
                            t6102.F03 = FeeCode.CJFWF;
                            t6102.F04 = ptwlzh.F01;
                            t6102.F07 = fwf;
                            t6102.F08 = wlzh.F06;
                            t6102.F09 = String.format("散标成交服务费:%s，标题：%s", t6230.F25, t6230.F03);
                            insertT6102(connection, t6102);
                        }
                    }
                    
                    // 根据标的id查询T体验金订单
                    List<T6519> arrayExperOrder = selectExperOrdIds(connection, t6230.F01);
                    
                    if (null != arrayExperOrder)
                    {
                        for (T6519 t6519 : arrayExperOrder)
                        {
                            // 查询体验金投资记录
                            T6286 t6286 = selectT6286(connection, t6519.F04, T6250_F07.F);
                            if (t6286 == null)
                            {
                                continue;
                            }
                            // 锁定投资人体验金账户
                            T6103 t6103 = selectT6103(connection, t6519.F02, t6230.F01, t6286.F10);
                            if (t6103 == null)
                            {
                                continue;
                            }
                            // 更新投资记录为已放款
                            updateT6286(connection, t6286.F01);
                            // 更新体验金利息开始时间
                            updateT6103(connection, t6286.F10);
                            // 生成体验金利息返还计划
                            hkjh(connection, t6230, t6519, t6103);
                            // 修改订单状态
                            updateSubmit(connection, T6501_F03.CG, t6519.F01);
                        }
                    }
                    if (t6230.F07.compareTo(BigDecimal.ZERO) > 0)
                    {
                        rebackAmount(connection, t6230);
                    }
                }
            }

            // 放款成功，发站内信
            T6110 t6110 = selectT6110(connection, t6230.F02);
            Envionment envionment = configureProvider.createEnvionment();
            envionment.set("datetime", DateTimeParser.format(t6230.F22));
            envionment.set("title", t6230.F03);
            envionment.set("lookUrl", configureProvider.format(URLVariable.USER_CREDIT));
            String content = configureProvider.format(LetterVariable.LOAN_SUCCESS, envionment);
            sendLetter(connection, t6230.F02, "放款成功", content);

            String isUseYtx = configureProvider.getProperty(SmsVaribles.SMS_IS_USE_YTX);
            if ("1".equals(isUseYtx))
            {
                SMSUtils smsUtils = new SMSUtils(configureProvider);
                int type = smsUtils.getTempleId(MsgVariavle.LOAN_SUCCESS.getDescription());
                sendMsg(connection, t6110.F04, t6230.F03, type);
            }
            else
            {
                sendMsg(connection, t6110.F04, configureProvider.format(MsgVariavle.LOAN_SUCCESS, envionment));
            }
            // 托管接口调用
            callFace(connection, orderId, params);
            // 加息券订单Id
            {
                // 查询加息券放款订单详情
                /* 根据投资用户Id+标的ID 检查放款订单中用户是否使用加息券投对应的标的 */
                T6524 t6524 = selectT6524(connection, t6505.F02, t6505.F03);
                if (t6524 == null)
                {
                    return;
                }
                // 查询加息券投资记录
                T6288 t6288 = selectT6288(connection, t6524.F04, T6288_F07.F);
                if (t6288 == null)
                {
                    return;
                }
                T6342 t6342 = selectT6342(connection, t6288.F10, T6342_F04.YSY);
                if (t6342 == null)
                {
                    return;
                }
                // 更新加息券投资记录为已放款
                updateT6288(connection, t6288.F01);
                // 生成加息券利息返还计划
                hkjh(connection, t6230, t6524, t6288.F10, creditorId);
                // 修改加息券订单状态为成功
                updateSubmit(connection, T6501_F03.CG, t6524.F01);
            }
            

        }
        catch (Exception e)
        {
            logger.error(e, e);
            throw e;
        }
    }
    
    /**
     * 未满标放款返还多余的信用额度和担保额度
     * @param connection
     * @param t6230
     * @throws Throwable
     */
    protected void rebackAmount(Connection connection, T6230 t6230)
        throws Throwable
    {
        BigDecimal amount = t6230.F07;
        BigDecimal totalAmount = BigDecimal.ZERO;
        Timestamp newTime = getCurrentTimestamp(connection);
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F03 FROM S61.T6116 WHERE T6116.F01 = ? FOR UPDATE"))
        {
            pstmt.setInt(1, t6230.F02);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    totalAmount = resultSet.getBigDecimal(1);
                }
            }
        }
        try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S61.T6116 SET F03 = ? WHERE F01 = ?"))
        {
            pstmt.setBigDecimal(1, totalAmount.add(amount));
            pstmt.setInt(2, t6230.F02);
            pstmt.execute();
        }
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S61.T6117 SET F02 = ?, F03 = ?, F04 = ?,F05 = ?, F07 = ?, F08 = ?"))
        {
            pstmt.setInt(1, t6230.F02);
            pstmt.setInt(2, FeeCode.XY_FK_FH);
            pstmt.setTimestamp(3, newTime);
            pstmt.setBigDecimal(4, amount);
            pstmt.setBigDecimal(5, totalAmount.add(amount));
            pstmt.setString(6, "未满标放款，多余的信用额度返还");
            pstmt.execute();
        }
        if (t6230.F11 == T6230_F11.S)
        {
            int dbUserId = 0;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT T6236.F03 FROM S62.T6236 WHERE T6236.F02 = ? AND T6236.F04 = 'S' LIMIT 1"))
            {
                pstmt.setInt(1, t6230.F01);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        dbUserId = resultSet.getInt(1);
                    }
                }
            }
            BigDecimal dbTotalAmount = new BigDecimal(0);
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F04 FROM S61.T6125 WHERE T6125.F01 = (SELECT T1.F01 FROM S61.T6125 T1 WHERE T1.F02 = ? LIMIT 1) FOR UPDATE"))
            {
                pstmt.setInt(1, dbUserId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        dbTotalAmount = resultSet.getBigDecimal(1);
                    }
                }
            }
            try (PreparedStatement pstmt =
                connection.prepareStatement("UPDATE S61.T6125 SET F04 = ? WHERE F02 = ?"))
            {
                pstmt.setBigDecimal(1, dbTotalAmount.add(amount));
                pstmt.setInt(2, dbUserId);
                pstmt.execute();
            }
            try (PreparedStatement pstmt =
                connection.prepareStatement("INSERT INTO S61.T6126 SET F02 = ?, F03 = ?, F04 = ?,F05 = ?, F07 = ?, F08 = ?"))
            {
                pstmt.setInt(1, dbUserId);
                pstmt.setInt(2, FeeCode.DB_FK_FH);
                pstmt.setTimestamp(3, newTime);
                pstmt.setBigDecimal(4, amount);
                pstmt.setBigDecimal(5, dbTotalAmount.add(amount));
                pstmt.setString(6, "未满标放款，多余的担保额度返还");
                pstmt.execute();
            }

        }
        
    }
    
    /**
     * 根据标的id查询所有体验金放款订单
     * @param connection
     * @param bidId
     * @return
     * @throws Throwable
     */
    protected List<T6519> selectExperOrdIds(Connection connection, int bidId)
        throws Throwable
    {
        List<T6519> records = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM S65.T6519 WHERE F03 = ?"))
        {
            ps.setInt(1, bidId);
            try (ResultSet resultSet = ps.executeQuery())
            {
                while (resultSet.next())
                {
                    T6519 record = new T6519();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getInt(3);
                    record.F04 = resultSet.getInt(4);
                    record.F05 = resultSet.getBigDecimal(5);
                    records.add(record);
                }
                return records;
                
            }
        }
    }
    
    /**
    * 红包投资
    */
    protected void sendRedPacket(Connection connection, T6292 t6292, T6101 wlzh, T6101 ptwlzh, T6230 t6230)
        throws Throwable
    {
        updateT6292(connection, T6292_F07.S, t6292.F01);
        BigDecimal hbje = t6292.F04;
        {
            // 给借款用户帐户资金额
            wlzh.F06 = wlzh.F06.add(hbje);
            updateT6101(connection, wlzh.F06, wlzh.F01);
            // 红包借款人不用资金流水
        }
        {
            // 扣除平台帐户发放红包的金额 ；平台总额 - 当前奖励金额
            ptwlzh.F06 = ptwlzh.F06.subtract(hbje);
            updateT6101(connection, ptwlzh.F06, ptwlzh.F01);
            T6102 t6102 = new T6102();
            t6102.F02 = ptwlzh.F01;
            t6102.F03 = FeeCode.TZ_TBHB;
            t6102.F04 = wlzh.F01;
            t6102.F07 = hbje;
            t6102.F08 = ptwlzh.F06;
            t6102.F09 = String.format("散标投资红包:%s", t6230.F25);
            insertT6102(connection, t6102);
        }
    }
    
    /**
    * 如果奖励标，则赠送投资奖励
    * 
    * @param connection
    * @param t6505
    * @param t6230
    * @param sdzh
    * @param tzrwlzh
    * @param wlzh
    * @param ptwlzh
    * @throws Throwable
    */
    protected void giveBidReward(SQLConnection connection, T6505 t6505, T6230 t6230, T6101 sdzh, T6101 tzrwlzh,
        T6101 wlzh, T6101 ptwlzh)
        throws Throwable
    {
        // 查询标扩展信息
        T6231 t6231 = selectT6231(connection, t6505.F03);
        // 奖励标发放奖励金
        if (t6231 != null && T6231_F27.S.name().equals(t6231.F27.name()))
        {
            BigDecimal jlje = t6505.F05.multiply(t6231.F28).setScale(2, RoundingMode.HALF_UP);
            
            // 如果是托管，插入6501、6517
            ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
            if (BooleanParser.parse(configureProvider.getProperty(SystemVariable.SFZJTG)))
            {
                doInsertT6517(connection, t6230, ptwlzh, tzrwlzh, jlje);
            }
            else
            {
                //网关方法。托管资金流水在转账订单成功后由托管处理。
                doGiveBidReward(connection, t6230, sdzh, tzrwlzh, wlzh, ptwlzh, jlje);
            }
        }
    }
    
    /**
     * 由于各托管有独立插入的需求，故抽取该方法
     * <功能详细描述>
     * @param connection
     * @param t6230
     * @param ptwlzh
     * @param tzrwlzh
     * @param jlje
     * @throws Throwable
     */
    protected void doInsertT6517(SQLConnection connection, T6230 t6230, T6101 ptwlzh, T6101 tzrwlzh, BigDecimal jlje)
        throws Throwable
    {// 插入转账订单
        if (jlje.compareTo(BigDecimal.ZERO) <= 0)
            return;
        T6501 t6501 = new T6501();
        t6501.F02 = OrderType.TRANSFER.orderType();
        t6501.F03 = T6501_F03.DTJ;
        t6501.F07 = T6501_F07.XT;
        t6501.F08 = ptwlzh.F02;
        t6501.F04 = getCurrentTimestamp(connection);
        t6501.F13 = jlje;
        int ordId = insertT6501(connection, t6501);
        T6517 t6517 = new T6517();
        t6517.F01 = ordId;
        t6517.F02 = jlje;
        t6517.F03 = ptwlzh.F02;
        t6517.F04 = tzrwlzh.F02;
        t6517.F05 = "投标奖励";
        t6517.F06 = FeeCode.TZ_TBJL;
        insertT6517(connection, t6517);
    }
    
    /**
     * 针对奖励标执行奖励发放
     * <功能详细描述>
     */
    protected void doGiveBidReward(SQLConnection connection, T6230 t6230, T6101 sdzh, T6101 tzrwlzh, T6101 wlzh,
        T6101 ptwlzh, BigDecimal jlje)
        throws Throwable
    {
        {
            // 给用户帐户资金额 添加当前发放的奖励 ；用户帐户金额+ 奖励金额 ，
            tzrwlzh.F06 = tzrwlzh.F06.add(jlje);
            updateT6101(connection, tzrwlzh.F06, tzrwlzh.F01);
            // 资金流水
            T6102 t6102 = new T6102();
            t6102.F02 = tzrwlzh.F01;
            t6102.F03 = FeeCode.TZ_TBJL;
            t6102.F04 = ptwlzh.F01;
            t6102.F06 = jlje;
            t6102.F08 = tzrwlzh.F06;
            t6102.F09 = String.format("散标投资奖励:%s", t6230.F25);
            insertT6102(connection, t6102);
        }
        {
            // 扣除平台帐户发放奖励的金额 ；平台总额 - 当前奖励金额
            ptwlzh.F06 = ptwlzh.F06.subtract(jlje);
            updateT6101(connection, ptwlzh.F06, ptwlzh.F01);
            T6102 t6102 = new T6102();
            t6102.F02 = ptwlzh.F01;
            t6102.F03 = FeeCode.TZ_TBJL;
            t6102.F04 = tzrwlzh.F01;
            t6102.F07 = jlje;
            t6102.F08 = ptwlzh.F06;
            t6102.F09 = String.format("散标投资奖励:%s", t6230.F25);
            insertT6102(connection, t6102);
        }
        
    }
    
    // 投资奖励
    protected void tzjl(Connection connection, T6230 t6230, T6505 t6505, T6101 wlzh, T6101 ptwlzh)
        throws Throwable
    {
        ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        // 邀请码
        String yqm = null;
        try (PreparedStatement ps = connection.prepareStatement("SELECT F03 FROM S61.T6111 WHERE T6111.F01 = ?"))
        {
            ps.setInt(1, t6505.F02);
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
        
        // 标的单笔投资金额
        BigDecimal t6505_f05 = t6505.F05;
        BigDecimal baseReward = BigDecimalParser.parse(configureProvider.getProperty(SystemVariable.TG_TZJS));
        if (BigDecimal.ZERO.compareTo(baseReward) >= 0 || t6505_f05.compareTo(baseReward) < 0)
        {
            return;
        }
        // 推广人未托管不奖励
        boolean tg = BooleanParser.parse(configureProvider.getProperty(SystemVariable.SFZJTG));
        if (tg)
        {
            try (PreparedStatement ps = connection.prepareStatement("SELECT F01 FROM S61.T6119 WHERE F01 = ?"))
            {
                ps.setInt(1, tgyh);
                try (ResultSet resultSet = ps.executeQuery())
                {
                    if (!resultSet.next())
                    {
                        return;
                    }
                }
            }
        }
        T6101 tgrzh = null;
        if (tgyh == t6230.F02)
        {
            tgrzh = wlzh;
        }
        else
        {
            tgrzh = selectT6101(connection, tgyh, T6101_F03.WLZH, true);
        }
        if (tgrzh == null)
        {
            return;
        }
        // 标的奖励金额: (单笔投资金额 / 投资基数) * 奖励基数
        BigDecimal jlje =
            t6505_f05.divide(baseReward, 0, BigDecimal.ROUND_FLOOR)
                .multiply(BigDecimalParser.parse(configureProvider.getProperty(SystemVariable.TG_TZJL)));
        if (jlje == null)
        {
            return;
        }
        try (PreparedStatement ps = connection.prepareStatement("UPDATE S63.T6310 SET F04 = F04 + ? WHERE F01 = ?"))
        {
            ps.setBigDecimal(1, jlje);
            ps.setInt(2, tgyh);
            ps.executeUpdate();
        }
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S63.T6312 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?"))
        {
            pstmt.setInt(1, tgyh);
            pstmt.setInt(2, t6505.F02);
            pstmt.setBigDecimal(3, t6505.F05);
            pstmt.setBigDecimal(4, jlje);
            pstmt.setTimestamp(5, getCurrentTimestamp(connection));
            pstmt.execute();
        }
        //网关模式直接插资金流水，托管模式插T6517转账订单
        if (!BooleanParser.parse(configureProvider.getProperty(SystemVariable.SFZJTG)))
        {
            tgrzh.F06 = tgrzh.F06.add(jlje);
            {
                updateT6101(connection, tgrzh.F06, tgrzh.F01);
                T6102 t6102 = new T6102();
                t6102.F02 = tgrzh.F01;
                t6102.F03 = FeeCode.TG_CX;
                t6102.F04 = ptwlzh.F01;
                t6102.F06 = jlje;
                t6102.F08 = tgrzh.F06;
                t6102.F09 = String.format("持续推广奖励");
                insertT6102(connection, t6102);
            }
            ptwlzh.F06 = ptwlzh.F06.subtract(jlje);
            {
                updateT6101(connection, ptwlzh.F06, ptwlzh.F01);
                T6102 t6102 = new T6102();
                t6102.F02 = ptwlzh.F01;
                t6102.F03 = FeeCode.TG_CX;
                t6102.F04 = tgrzh.F01;
                t6102.F07 = jlje;
                t6102.F08 = ptwlzh.F06;
                t6102.F09 = String.format("持续推广奖励");
                insertT6102(connection, t6102);
            }
        }
        else
        {
            // 插入转账订单
            T6501 zzt6501 = new T6501();
            zzt6501.F02 = OrderType.TRANSFER.orderType();
            zzt6501.F03 = T6501_F03.DTJ;
            zzt6501.F07 = T6501_F07.XT;
            zzt6501.F08 = ptwlzh.F02;
            zzt6501.F04 = getCurrentTimestamp(connection);
            zzt6501.F09 = getPTID(connection);
            zzt6501.F13 = jlje;
            int ordId = insertT6501(connection, zzt6501);
            T6517 t6517 = new T6517();
            t6517.F01 = ordId;
            t6517.F02 = jlje;
            t6517.F03 = ptwlzh.F02;
            t6517.F04 = tgyh;
            t6517.F05 = "持续推广奖励";
            t6517.F06 = FeeCode.TG_CX;
            insertT6517(connection, t6517);
        }
        
        // 发站内信
        Envionment envionment = configureProvider.createEnvionment();
        envionment.set("tz", String.valueOf(t6505_f05));
        envionment.set("jl", String.valueOf(jlje));
        String content = configureProvider.format(LetterVariable.TG_CXJL, envionment);
        sendLetter(connection, tgyh, "推广持续奖励", content);
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
            logger.error("TenderConfirmExecutor.insertT6501():数据库异常");
            throw new SQLException("数据库异常");
        }
        return orderId;
    }
    
    protected T6240 selectT6240(Connection connection, int F01)
        throws SQLException
    {
        T6240 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07 FROM S62.T6240 WHERE T6240.F01 = ? LIMIT 1"))
        {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6240();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getInt(3);
                    record.F04 = resultSet.getBigDecimal(4);
                    record.F05 = resultSet.getString(5);
                    record.F06 = resultSet.getBigDecimal(6);
                    record.F07 = resultSet.getBigDecimal(7);
                }
            }
        }
        return record;
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
    
    protected void updateT6231(Connection connection, Date F01, int F02)
        throws Throwable
    {
        try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S62.T6231 SET F06 = ? WHERE F01 = ?"))
        {
            pstmt.setDate(1, F01);
            pstmt.setInt(2, F02);
            pstmt.execute();
        }
    }
    
    protected void updateT6231(Connection connection, int F02)
        throws Throwable
    {
        resourceProvider.log("==========更新放款时间，标id：" + F02);
        try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S62.T6231 SET F12 = ? WHERE F01 = ?"))
        {
            pstmt.setTimestamp(1, getCurrentTimestamp(connection));
            pstmt.setInt(2, F02);
            pstmt.execute();
        }
    }
    
    protected void updateT6230(Connection connection, int F02)
        throws SQLException
    {
        resourceProvider.log("===========修改标状态为还款中,标id：" + F02);
        try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S62.T6230 SET F20 = 'HKZ' WHERE F01 = ?"))
        {
            pstmt.setInt(1, F02);
            pstmt.execute();
        }
    }
    
    protected void updateT6250(Connection connection, T6250_F08 F01, int F02)
        throws Throwable
    {
        try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S62.T6250 SET F08 = ? WHERE F01 = ?"))
        {
            pstmt.setString(1, F01.name());
            pstmt.setInt(2, F02);
            pstmt.execute();
        }
    }
    
    protected void updateT6292(Connection connection, T6292_F07 F01, int F02)
        throws SQLException
    {
        try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S62.T6292 SET F07 = ? WHERE F01 = ?"))
        {
            pstmt.setString(1, F01.name());
            pstmt.setInt(2, F02);
            pstmt.execute();
        }
    }
    
    protected void hkjh(Connection connection, T6230 t6230)
        throws Throwable
    {
        resourceProvider.log("==============生成还款计划");
        final Date currentDate = getCurrentDate(connection); // 数据库当前日期
        final Date interestDate = new Date(currentDate.getTime() + Long.valueOf(t6230.F19) * 86400000);
        final Date endDate = new Date(DateHelper.rollMonth(interestDate.getTime(), t6230.F09));
        T6251[] t6251s = selectT6251s(connection, t6230.F01);
        if (t6230.F17 == T6230_F17.ZRY)
        {// 自然月
            switch (t6230.F10)
            {
                case DEBX:
                {
                    for (T6251 t6251 : t6251s)
                    {
                        insertT6252s(connection, calZRY_DEBX(connection, t6230, t6251, interestDate, endDate));
                    }
                    break;
                }
                case MYFX:
                {
                    for (T6251 t6251 : t6251s)
                    {
                        insertT6252s(connection, calZRY_MYFX(connection, t6230, t6251, interestDate, endDate));
                    }
                    break;
                }
                case YCFQ:
                {
                    for (T6251 t6251 : t6251s)
                    {
                        insertT6252s(connection, calYCFQ(connection, t6230, t6251, interestDate, endDate));
                    }
                    break;
                }
                case DEBJ:
                {
                    for (T6251 t6251 : t6251s)
                    {
                        insertT6252s(connection, calZRY_DEBJ(connection, t6230, t6251, interestDate, endDate));
                    }
                    break;
                }
                default:
                    throw new LogicalException("不支持的还款方式");
            }
        }
        else if (t6230.F17 == T6230_F17.GDR)
        {// 固定付息日
            switch (t6230.F10)
            {
                case DEBX:
                {
                    for (T6251 t6251 : t6251s)
                    {
                        insertT6252s(connection, calGDR_DEBX(connection, t6230, t6251, interestDate, endDate));
                    }
                    break;
                }
                case MYFX:
                {
                    for (T6251 t6251 : t6251s)
                    {
                        insertT6252s(connection, calGDR_MYFX(connection, t6230, t6251, interestDate, endDate));
                    }
                    break;
                }
                case YCFQ:
                {
                    for (T6251 t6251 : t6251s)
                    {
                        insertT6252s(connection, calYCFQ(connection, t6230, t6251, interestDate, endDate));
                    }
                    break;
                }
                case DEBJ:
                {
                    for (T6251 t6251 : t6251s)
                    {
                        insertT6252s(connection, calGDR_DEBJ(connection, t6230, t6251, interestDate, endDate));
                    }
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
    
    private T6251[] selectT6251s(Connection connection, int id)
        throws Throwable
    {
        ArrayList<T6251> list = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10 FROM S62.T6251 WHERE T6251.F03 = ? AND F08 = 'F' FOR UPDATE"))
        {
            pstmt.setInt(1, id);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                while (resultSet.next())
                {
                    T6251 record = new T6251();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getString(2);
                    record.F03 = resultSet.getInt(3);
                    record.F04 = resultSet.getInt(4);
                    record.F05 = resultSet.getBigDecimal(5);
                    record.F06 = resultSet.getBigDecimal(6);
                    record.F07 = resultSet.getBigDecimal(7);
                    record.F08 = T6251_F08.parse(resultSet.getString(8));
                    record.F09 = resultSet.getDate(9);
                    record.F10 = resultSet.getDate(10);
                    if (list == null)
                    {
                        list = new ArrayList<>();
                    }
                    list.add(record);
                }
            }
        }
        return ((list == null || list.size() == 0) ? null : list.toArray(new T6251[list.size()]));
    }
    
    private ArrayList<T6252> calGDR_DEBJ(Connection connection, T6230 t6230, T6251 t6251, Date interestDate,
        Date endDate)
        throws Throwable
    {
        resourceProvider.log("====生成还款计划=====固定日等额本金====开始");
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
                    t6252.F07 = interest.setScale(2, BigDecimal.ROUND_HALF_UP);
                    t6252.F08 = new Date(ca.getTimeInMillis());
                    t6252.F09 = T6252_F09.WH;
                    t6252.F11 = t6251.F01;
                    t6252s.add(t6252);
                }
                updateT6231(connection, new Date(ca.getTimeInMillis()), t6230.F01);
                continue;
            }
            if (i == terms)
            {
                // total = total.subtract(monthAmount);
                bj = x.subtract(total).setScale(2, BigDecimal.ROUND_HALF_UP);
                interest =
                    bj.multiply(y)
                        .multiply(new BigDecimal(l))
                        .divide(new BigDecimal(f).add(new BigDecimal(l)), DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
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
                    t6252.F07 = interest.setScale(2, BigDecimal.ROUND_HALF_UP);
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
        resourceProvider.log("====生成还款计划=====固定日等额本金====结束");
        return t6252s;
    }
    
    private ArrayList<T6252> calGDR_MYFX(Connection connection, T6230 t6230, T6251 t6251, final Date interestDate,
        final Date endDate)
        throws Throwable
    {
        resourceProvider.log("====生成还款计划=====固定日每月付息到期还本====开始");
        ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
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
        BigDecimal yearDays =
            new BigDecimal(IntegerParser.parse(configureProvider.getProperty(SystemVariable.REPAY_DAYS_OF_YEAR), 360));
        // 首期付息天数
        int firstTermDays = 0;
        Date payDate;
        if (inserestStartDay < t6230.F18)
        {
            calendar.set(Calendar.DAY_OF_MONTH, t6230.F18);
            payDate = new Date(calendar.getTimeInMillis());
            firstTermDays = t6230.F18 - inserestStartDay;
        }
        else
        {
            calendar.set(Calendar.DAY_OF_MONTH, t6230.F18);
            payDate = new Date(DateHelper.rollMonth(calendar.getTimeInMillis(), 1));
            firstTermDays = 30 - inserestStartDay + t6230.F18;
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
        // 首期利息
        BigDecimal firstTermInterest = BigDecimal.ZERO;
        // 月利率
        BigDecimal monthRate = t6230.F06.divide(new BigDecimal(12), DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
        // 生成还款记录
        ArrayList<T6252> t6252s = new ArrayList<>();
        for (int i = 1; i <= term; i++)
        {
            // 利息
            BigDecimal interest = new BigDecimal(0);//BigDecimal.ZERO;
            if (i == term)
            {
                payDate = endDate;
                interest =
                    t6251.F07.multiply(monthRate)
                        .subtract(firstTermInterest)
                        .setScale(DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
            }
            else if (i == 1)
            {
                updateT6231(connection, payDate, t6230.F01);
                interest =
                    t6251.F07.multiply(t6230.F06)
                        .multiply(new BigDecimal(firstTermDays))
                        .divide(yearDays, DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
                firstTermInterest = interest;
            }
            else
            {
                interest = t6251.F07.multiply(monthRate).setScale(DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
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
                t6252.F07 = interest;
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
            payDate = new Date(DateHelper.rollMonth(payDate.getTime(), 1));
        }
        updateT6231(connection, term, t6230.F01);
        resourceProvider.log("====生成还款计划=====固定日每月付息到期还本====结束");
        return t6252s;
    }
    
    private ArrayList<T6252> calGDR_DEBX(Connection connection, T6230 t6230, T6251 t6251, Date interestDate,
        Date endDate)
        throws Throwable
    {
        resourceProvider.log("====生成还款计划=====固定日等额本息====开始");
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
        ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        // 借款金额
        BigDecimal y = t6251.F07;
        // 日利率
        BigDecimal z =
            t6230.F06.divide(new BigDecimal(
                IntegerParser.parse(configureProvider.format(SystemVariable.REPAY_DAYS_OF_YEAR))),
                DECIMAL_SCALE,
                BigDecimal.ROUND_HALF_UP);
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
        resourceProvider.log("====生成还款计划=====固定日等额本息====结束");
        return t6252s;
    }
    
    private ArrayList<T6252> calZRY_DEBJ(Connection connection, T6230 t6230, T6251 t6251, Date interestDate,
        Date endDate)
        throws Throwable
    {
        resourceProvider.log("====生成还款计划=====自然月等额本息====开始");
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
        resourceProvider.log("====生成还款计划=====自然月等额本息====结束");
        return t6252s;
    }
    
    private ArrayList<T6252> calYCFQ_BY_DAYS(Connection connection, T6230 t6230, T6251 t6251, Date interestDate,
        Date endDate, int days)
        throws Throwable
    {
        ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        // 更新下个还款日
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
                    .multiply(new BigDecimal(days))
                    .divide(new BigDecimal(configureProvider.getProperty(SystemVariable.REPAY_DAYS_OF_YEAR)),
                        DECIMAL_SCALE,
                        BigDecimal.ROUND_HALF_UP);
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
    
    private ArrayList<T6252> calYCFQ(Connection connection, T6230 t6230, T6251 t6251, Date interestDate, Date endDate)
        throws Throwable
    {
        resourceProvider.log("====生成还款计划=====自然月本息一次性付清====开始");
        int days = 0; // 借款天数
        T6231_F21 dayOrMonth = T6231_F21.F; // 借款方式
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F21, F22 FROM S62.T6231 WHERE F01 = ? LIMIT 1 FOR UPDATE"))
        {
            pstmt.setInt(1, t6230.F01);
            try (ResultSet rs = pstmt.executeQuery())
            {
                if (rs.next())
                {
                    dayOrMonth = T6231_F21.parse(rs.getString(1));
                    days = IntegerParser.parse(rs.getInt(2));
                }
            }
        }
        
        // 分支逻辑：按天计算还款计划
        if (T6231_F21.S == dayOrMonth)
        {
            if (days <= 0)
            {
                throw new LogicalException("按天借款，其天数必须大于0");
            }
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(endDate.getTime());
            cal.add(Calendar.DATE, days);
            endDate = new java.sql.Date(cal.getTimeInMillis());
            return calYCFQ_BY_DAYS(connection, t6230, t6251, interestDate, endDate, days);
        }
        
        // 更新下个还款日
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
        resourceProvider.log("====生成还款计划=====自然月本息一次性付清====结束");
        return t6252s;
    }
    
    private ArrayList<T6252> calZRY_MYFX(Connection connection, T6230 t6230, T6251 t6251, Date interestDate,
        Date endDate)
        throws Throwable
    {
        resourceProvider.log("====生成还款计划=====自然月每月付息到期还本====开始");
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
        // 除去最后一期的总利息
        BigDecimal totalExlastLx =
            (t6251.F07.multiply(t6230.F06).divide(monthes, DECIMAL_SCALE, BigDecimal.ROUND_DOWN).multiply(new BigDecimal(
                t6230.F09))).setScale(2, BigDecimal.ROUND_HALF_UP);
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
                t6252.F07 = t6251.F07.multiply(t6230.F06).divide(monthes, 2, BigDecimal.ROUND_DOWN);
                t6252.F08 = date;
                t6252.F09 = T6252_F09.WH;
                t6252.F10 = null;
                t6252.F11 = t6251.F01;
                if (term < t6230.F09)
                {
                    totalExlastLx = totalExlastLx.subtract(t6252.F07);
                }
                if (term == t6230.F09)
                {
                    t6252.F07 = totalExlastLx;
                }
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
        resourceProvider.log("====生成还款计划=====自然月每月付息到期还本====结束");
        return t6252s;
    }
    
    private ArrayList<T6252> calZRY_DEBX(Connection connection, T6230 t6230, T6251 t6251, Date interestDate,
        Date endDate)
        throws Throwable
    {
        resourceProvider.log("====生成还款计划=====自然月等额本息====开始");
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
                    t6252.F07 =
                        monthPayTotal.subtract(remainTotal).compareTo(BigDecimal.ZERO) > 0 ? monthPayTotal.subtract(remainTotal)
                            : BigDecimal.ZERO;
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
        resourceProvider.log("====生成还款计划=====自然月等额本息====结束");
        return t6252s;
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
    
    private BigDecimal debx(BigDecimal total, BigDecimal monthRate, int terms)
    {
        BigDecimal tmp = monthRate.add(new BigDecimal(1)).pow(terms);
        return total.multiply(monthRate)
            .multiply(tmp)
            .divide(tmp.subtract(new BigDecimal(1)), 2, BigDecimal.ROUND_HALF_UP);
    }
    
    protected int insertT6251(Connection connection, T6251 entity)
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
    
    protected T6250 selectT6250(Connection connection, int F01, T6250_F07 F07)
        throws SQLException
    {
        T6250 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07 FROM S62.T6250 WHERE T6250.F01 = ? AND T6250.F07 = ? LIMIT 1"))
        {
            pstmt.setInt(1, F01);
            pstmt.setString(2, F07.name());
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6250();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getInt(3);
                    record.F04 = resultSet.getBigDecimal(4);
                    record.F05 = resultSet.getBigDecimal(5);
                    record.F06 = resultSet.getTimestamp(6);
                    record.F07 = T6250_F07.parse(resultSet.getString(7));
                }
            }
        }
        return record;
    }
    
    protected T6292 selectT6292(Connection connection, int F01, T6292_F07 F07)
        throws SQLException
    {
        T6292 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F03, F04,F10 FROM S62.T6292 WHERE T6292.F09 = ? AND T6292.F07 = ? LIMIT 1"))
        {
            pstmt.setInt(1, F01);
            pstmt.setString(2, F07.name());
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6292();
                    record.F01 = resultSet.getInt(1);
                    record.F03 = resultSet.getInt(2);
                    record.F04 = resultSet.getBigDecimal(3);
                    record.F10 = resultSet.getInt(4);
                }
            }
        }
        return record;
    }
    
    protected String zqCode(int recordId)
    {
        DecimalFormat decimalFormat = new DecimalFormat("0000000000");
        return ("Z" + decimalFormat.format(recordId));
    }
    
    protected T6230 selectT6230(Connection connection, int F01)
        throws SQLException
    {
        T6230 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11, F12, F13, F14, F15, F16, F17, F18, F19, F20, F21, F22, F23, F24, F25, F26, F27 FROM S62.T6230 WHERE T6230.F01 = ? FOR UPDATE"))
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
                    record.F27 = EnumParser.parse(T6230_F27.class, resultSet.getString(27));
                }
            }
        }
        return record;
    }
    
    protected T6505 selectT6505(Connection connection, int F01)
        throws SQLException
    {
        T6505 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05 FROM S65.T6505 WHERE T6505.F01 = ? LIMIT 1"))
        {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6505();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getInt(3);
                    record.F04 = resultSet.getInt(4);
                    record.F05 = resultSet.getBigDecimal(5);
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
    
    protected void updateT6280(Connection connection, int F01)
        throws SQLException
    {
        try (PreparedStatement ps = connection.prepareStatement("UPDATE S62.T6280 SET F10 = ? WHERE F12 = ?"))
        {
            ps.setString(1, AutoSetStatus.TY.name());
            ps.setInt(2, F01);
            ps.execute();
        }
    }
    
    protected BigDecimal getCJFee(Connection connection, T6238 t6238, T6230 t6230)
        throws Throwable
    {
        BigDecimal wgCjFee = BigDecimal.ZERO;
        wgCjFee = t6238.F02.multiply(t6230.F05.subtract(t6230.F07)).setScale(2, RoundingMode.HALF_UP);
        return wgCjFee;
    }
    
    @Override
    protected Timestamp getCurrentTimestamp(Connection connection)
        throws Throwable
    {
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT CURRENT_TIMESTAMP()"))
        {
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    return resultSet.getTimestamp(1);
                }
            }
        }
        return null;
    }
    
    /**
    * 按天生成还款记录
    * 
    * @return
    */
    private List<T6285> calTyjDay(Connection connection, T6230 t6230, T6519 t6519, int experDay, int F01)
        throws Throwable
    {
        //更新收益期
        updateT6103(connection, F01, experDay, "false");
        
        // 数据库当前日期
        final Date currentDate = getCurrentDate(connection);
        
        // 起息日
        Date endDate = new Date(currentDate.getTime() + Long.valueOf(t6230.F19) * 86400000);
        
        //Date endDate = new Date(DateHelper.rollMonth(interestDate.getTime(), t6230.F09));
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(endDate.getTime());
        cal.add(Calendar.DATE, experDay);
        endDate = new java.sql.Date(cal.getTimeInMillis());
        
        // 利息返还数组
        List<T6285> entities = new ArrayList<T6285>();
        
        ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        
        T6285 t6285 = new T6285();
        t6285.F02 = t6519.F03;
        t6285.F03 = getPTID(connection);
        t6285.F04 = t6519.F02;
        t6285.F05 = FeeCode.TYJTZ;
        t6285.F06 = 1;
        t6285.F07 =
            t6519.F05.multiply(t6230.F06)
                .multiply(new BigDecimal(experDay))
                .divide(new BigDecimal(configureProvider.getProperty(SystemVariable.REPAY_DAYS_OF_YEAR)),
                    DECIMAL_SCALE,
                    BigDecimal.ROUND_HALF_UP);
        t6285.F11 = BigDecimal.ZERO;
        t6285.F08 = endDate;
        t6285.F09 = T6285_F09.WFH;
        t6285.F10 = null;
        t6285.F12 = F01;
        entities.add(t6285);
        
        return entities;
    }
    
    /**
    * 按月生成还款记录
    * 
    * @return
    */
    private List<T6285> calTyjMonth(Connection connection, T6230 t6230, T6519 t6519, int experMonth, int F01)
        throws Throwable
    {
        //更新收益期
        updateT6103(connection, F01, experMonth, "true");
        
        // 利息返还数组
        List<T6285> entities = new ArrayList<T6285>();
        
        // 数据库当前日期
        final Date currentDate = getCurrentDate(connection);
        
        // 起息日
        final Date interestDate = new Date(currentDate.getTime() + Long.valueOf(t6230.F19) * 86400000);
        
        // 利息返还时间
        Date lxfhTime = null;
        
        // 月利率
        BigDecimal monthRate = t6230.F06.divide(new BigDecimal(12), DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
        
        // 当前时间
        Calendar calendar = Calendar.getInstance();
        
        BigDecimal lxTotal =
            t6519.F05.setScale(DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP)
                .multiply(monthRate)
                .multiply(new BigDecimal(experMonth));
        
        if (T6230_F10.YCFQ == t6230.F10)
        {
            T6285 t6285 = new T6285();
            t6285.F02 = t6519.F03;
            t6285.F03 = getPTID(connection);
            t6285.F04 = t6519.F02;
            t6285.F05 = FeeCode.TYJTZ;
            t6285.F06 = 1;
            t6285.F07 = lxTotal;
            t6285.F11 = BigDecimal.ZERO;
            t6285.F08 = new Date(DateHelper.rollMonth(interestDate.getTime(), experMonth));
            t6285.F09 = T6285_F09.WFH;
            t6285.F10 = null;
            t6285.F12 = F01;
            entities.add(t6285);
        }
        else
        {
            // 生成利息返还计划
            for (int i = 1; i <= experMonth; i++)
            {
                // 自然月
                if (t6230.F17 == T6230_F17.ZRY)
                {
                    Date date = new Date(DateHelper.rollMonth(interestDate.getTime(), i));
                    lxfhTime = date;
                }
                else if (t6230.F17 == T6230_F17.GDR)
                {// 固定付息日
                    final int inserestStartDay;// 起息日
                    {
                        calendar.setTime(interestDate);
                        inserestStartDay = calendar.get(Calendar.DAY_OF_MONTH);
                    }
                    if (inserestStartDay == t6230.F18)
                    {
                        // 自然月还款
                        Date date = new Date(DateHelper.rollMonth(interestDate.getTime(), i));
                        lxfhTime = date;
                    }
                    else
                    {
                        // 首期付款日
                        Calendar ca = Calendar.getInstance();
                        if (inserestStartDay > t6230.F18)
                        {
                            calendar.setTimeInMillis(System.currentTimeMillis());
                            calendar.add(Calendar.MONTH, i);
                            calendar.set(Calendar.DAY_OF_MONTH, t6230.F18);
                            ca.setTimeInMillis(calendar.getTimeInMillis());
                        }
                        else
                        {
                            calendar.setTimeInMillis(System.currentTimeMillis());
                            calendar.add(Calendar.MONTH, i - 1);
                            calendar.set(Calendar.DAY_OF_MONTH, t6230.F18);
                            ca.setTimeInMillis(calendar.getTimeInMillis());
                        }
                        lxfhTime = new Date(ca.getTimeInMillis());
                        ca.add(Calendar.MONTH, 1);
                    }
                }
                else
                {
                    throw new LogicalException("不支持的付息方式");
                }
                T6285 t6285 = new T6285();
                t6285.F02 = t6519.F03;
                t6285.F03 = getPTID(connection);
                t6285.F04 = t6519.F02;
                t6285.F05 = FeeCode.TYJTZ;
                t6285.F06 = i;
                t6285.F07 = t6519.F05.setScale(DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP).multiply(monthRate);
                t6285.F11 =
                    lxTotal.subtract(t6519.F05.setScale(DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP)
                        .multiply(monthRate)
                        .multiply(new BigDecimal(i)));
                t6285.F08 = lxfhTime;
                t6285.F09 = T6285_F09.WFH;
                t6285.F10 = null;
                t6285.F12 = F01;
                entities.add(t6285);
            }
        }
        return entities;
    }
    
    protected T6519 selectT6519(Connection connection, int F01)
        throws SQLException
    {
        T6519 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05 FROM S65.T6519 WHERE T6519.F01 = ? LIMIT 1"))
        {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6519();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getInt(3);
                    record.F04 = resultSet.getInt(4);
                    record.F05 = resultSet.getBigDecimal(5);
                }
            }
        }
        return record;
    }
    
    protected T6286 selectT6286(Connection connection, int F01, T6250_F07 F07)
        throws SQLException
    {
        T6286 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F10 FROM S62.T6286 WHERE T6286.F01 = ? AND T6286.F07 = ? LIMIT 1 FOR UPDATE"))
        {
            pstmt.setInt(1, F01);
            pstmt.setString(2, F07.name());
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6286();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getInt(3);
                    record.F04 = resultSet.getBigDecimal(4);
                    record.F05 = resultSet.getTimestamp(5);
                    record.F06 = T6286_F06.parse(resultSet.getString(6));
                    record.F07 = T6286_F07.parse(resultSet.getString(7));
                    record.F10 = resultSet.getInt(8);
                }
            }
        }
        return record;
    }
    
    /**
    * 根据用户ID、标ID，查看体验金 <功能详细描述>
    * 
    * @param connection
    * @param accountId
    * @param loanId
    * @return
    * @throws SQLException
    */
    protected T6103 selectT6103(Connection connection, int accountId, int loanId, int id)
        throws SQLException
    {
        T6103 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01,F07,F16 FROM S61.T6103 WHERE T6103.F01 = ? FOR UPDATE"))
        {
            pstmt.setInt(1, id);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6103();
                    record.F01 = resultSet.getInt(1);
                    record.F07 = resultSet.getInt(2);
                    record.F16 = resultSet.getString(3);
                }
            }
        }
        return record;
    }
    
    protected void updateT6286(Connection connection, int F01)
        throws SQLException
    {
        try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S62.T6286 SET F07 = ? WHERE F01 = ?"))
        {
            pstmt.setString(1, T6286_F07.S.name());
            pstmt.setInt(2, F01);
            pstmt.execute();
        }
    }
    
    protected void updateT6103(Connection connection, int id)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S61.T6103 SET T6103.F06= ?, T6103.F11 = ? WHERE T6103.F01 = ?"))
        {
            pstmt.setString(1, T6103_F06.YTZ.name());
            pstmt.setTimestamp(2, getCurrentTimestamp(connection));
            pstmt.setInt(3, id);
            pstmt.execute();
        }
    }
    
    protected void updateT6103(Connection connection, int id, int term, String dayType)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S61.T6103 SET T6103.F07= ?, T6103.F16 = ? WHERE T6103.F01 = ?"))
        {
            pstmt.setInt(1, term);
            pstmt.setString(2, dayType);
            pstmt.setInt(3, id);
            pstmt.execute();
        }
    }
    
    protected void hkjh(Connection connection, T6230 t6230, T6519 t6519, T6103 t6103)
        throws Throwable
    {
        
        // 先拿T6231信息判断是否按月，按月取T6230.F09，按天取T6231.F22
        
        // 体验金 天标规则：
        // 如果用户投的是天标，那么默认体验金的收益天数，就是标的天数。
        // 这个天数要和后台平台常量里面设置的 体验金月数，进行比较，
        // 如果这个天数，大于设置的月数，就按照设置的月数进行计算收益。
        // 如果这个天数，小与设置的月数，就按照这个天数进行计算收益。
        
        int days = 0; // 借款天数
        int exper = 0; // 体验金收益数
        T6231_F21 dayOrMonth = T6231_F21.F; // 借款方式
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F21, F22 FROM S62.T6231 WHERE F01 = ? LIMIT 1 FOR UPDATE"))
        {
            pstmt.setInt(1, t6230.F01);
            try (ResultSet rs = pstmt.executeQuery())
            {
                if (rs.next())
                {
                    dayOrMonth = T6231_F21.parse(rs.getString(1));
                    days = IntegerParser.parse(rs.getInt(2));
                }
            }
        }
        
        // 数据库当前日期
        final Date currentDate = getCurrentDate(connection);
        
        // 利息返还数组
        List<T6285> entities = new ArrayList<T6285>();
        
        Calendar dayCalendar = Calendar.getInstance();
        Calendar monthCalendar = Calendar.getInstance();
        dayCalendar.setTime(currentDate);
        monthCalendar.setTime(currentDate);
        // 体验金投资有效收益天数
        if ("false".equals(t6103.F16))
        {
            monthCalendar.add(Calendar.DATE, t6103.F07);
            
            if (T6231_F21.S == dayOrMonth)
            {
                dayCalendar.add(Calendar.DATE, days);
            }
            else
            {
                dayCalendar.add(Calendar.MONTH, t6230.F09);
            }
            // 借款周期大于收益期，按照收益期返还利息
            if (dayCalendar.compareTo(monthCalendar) > 0)
            {
                exper = t6103.F07;
            }
            // 借款周期小于收益期，按照借款周期来算
            else
            {
                if (T6231_F21.S == dayOrMonth)
                {
                    exper = days;
                }
                else
                {
                    exper = t6230.F09 * 30;
                }
            }
            entities = calTyjDay(connection, t6230, t6519, exper, t6103.F01);
        }
        else
        {
            //体验金投资有效收益月份数
            if (T6231_F21.S == dayOrMonth)
            {
                dayCalendar.add(Calendar.DATE, days);
                monthCalendar.add(Calendar.MONTH, t6103.F07);
                // 借款周期大于收益期，按照收益期返还利息
                if (dayCalendar.compareTo(monthCalendar) > 0)
                {
                    exper = t6103.F07 * 30;
                }
                // 借款周期小于收益期，按照借款周期来算
                else
                {
                    exper = days;
                }
                entities = calTyjDay(connection, t6230, t6519, exper, t6103.F01);
            }
            else
            {
                exper = t6230.F09 > t6103.F07 ? t6103.F07 : t6230.F09;
                entities = calTyjMonth(connection, t6230, t6519, exper, t6103.F01);
            }
        }
        
        // 录入返还计划
        insertT6285(connection, entities);
    }
    
    private void insertT6285(Connection connection, List<T6285> entities)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S62.T6285 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?, F10 = ?, F11 = ?,F12 = ?"))
        {
            for (T6285 entity : entities)
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
                pstmt.setBigDecimal(10, entity.F11);
                pstmt.setInt(11, entity.F12);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }
    }
    
    @Override
    protected void doSubmit(SQLConnection connection, int orderId, Map<String, String> params)
        throws Throwable
    {
        // 体验金订单确认
        String expOrdId = params.get("experOrderIds");
        if (!StringHelper.isEmpty(expOrdId))
        {
            String[] strExperOrderIds = expOrdId.split(",");
            for (String strExperOrder : strExperOrderIds)
            {
                if (IntegerParser.parse(strExperOrder) > 0)
                {
                    // 修改订单状态
                    updateSubmit(connection, T6501_F03.DQR, IntegerParser.parse(strExperOrder));
                }
            }
        }
    }
    
    /**
    * 查询标是否为奖励标
    * 
    * @param connection
    * @param F01
    * @return
    * @throws SQLException
    */
    protected T6231 selectT6231(Connection connection, int F01)
        throws SQLException
    {
        T6231 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F27,F28 FROM S62.T6231 WHERE T6231.F01 = ? FOR UPDATE"))
        {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6231();
                    record.F27 = T6231_F27.parse(resultSet.getString(1));
                    record.F28 = resultSet.getBigDecimal(2);
                }
            }
        }
        return record;
    }
    
    /**
    * 根据订单ID查询加息券放款订单 <功能详细描述>
    * 
    * @param connection
    * @param F02投资用户ID
    * @param F03标ID
    * @return
    * @throws SQLException
    */
    protected T6524 selectT6524(Connection connection, int F02, int F03)
        throws SQLException
    {
        T6524 record = null;
        try (PreparedStatement ps =
            connection.prepareStatement("SELECT F01,F02,F03,F04,F05,F06 FROM S65.T6524 WHERE T6524.F02=? AND T6524.F03=? LIMIT 1"))
        {
            ps.setInt(1, F02);
            ps.setInt(2, F03);
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    record = new T6524();
                    record.F01 = rs.getInt(1);
                    record.F02 = rs.getInt(2);
                    record.F03 = rs.getInt(3);
                    record.F04 = rs.getInt(4);
                    record.F05 = rs.getBigDecimal(5);
                    record.F06 = rs.getBigDecimal(6);
                }
            }
        }
        return record;
    }
    
    protected T6288 selectT6288(Connection connection, int F01, T6288_F07 F07)
        throws SQLException
    {
        T6288 record = null;
        try (PreparedStatement ps =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07,F08,F09, F10,F11 FROM S62.T6288 WHERE T6288.F01 = ? AND T6288.F07 = ? LIMIT 1 FOR UPDATE"))
        {
            ps.setInt(1, F01);
            ps.setString(2, F07.name());
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    record = new T6288();
                    record.F01 = rs.getInt(1);
                    record.F02 = rs.getInt(2);
                    record.F03 = rs.getInt(3);
                    record.F04 = rs.getBigDecimal(4);
                    record.F05 = rs.getTimestamp(5);
                    record.F06 = T6288_F06.parse(rs.getString(6));
                    record.F07 = T6288_F07.parse(rs.getString(7));
                    record.F08 = T6288_F08.parse(rs.getString(8));
                    record.F09 = rs.getInt(9);
                    record.F10 = rs.getInt(10);
                    record.F11 = rs.getBigDecimal(11);
                }
            }
        }
        return record;
    }
    
    protected T6342 selectT6342(Connection connection, int F01, T6342_F04 F04)
        throws SQLException
    {
        T6342 record = null;
        try (PreparedStatement ps =
            connection.prepareStatement("SELECT F01,F02,F03,F04,F05,F06,F07,F08 FROM S63.T6342 WHERE F01=? AND F04=? FOR UPDATE"))
        {
            ps.setInt(1, F01);
            ps.setString(2, F04.name());
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    record = new T6342();
                    record.F01 = rs.getInt(1);
                    record.F02 = rs.getInt(2);
                    record.F03 = rs.getInt(3);
                    record.F04 = T6342_F04.parse(rs.getString(4));
                    record.F05 = rs.getTimestamp(5);
                    record.F06 = rs.getInt(6);
                    record.F07 = rs.getTimestamp(7);
                    record.F08 = rs.getTimestamp(8);
                }
            }
        }
        return record;
    }
    
    /**
    * 更新加息券投资记录为已放款状态
    * 
    * @param connection
    * @param F01
    * @throws SQLException
    */
    protected void updateT6288(Connection connection, int F01)
        throws SQLException
    {
        try (PreparedStatement ps = connection.prepareStatement("UPDATE S62.T6288 SET F07 = ? WHERE F01 = ?"))
        {
            ps.setString(1, T6288_F07.S.name());
            ps.setInt(2, F01);
            ps.execute();
        }
    }
    
    /**
    * 批量生成加息券利息返还计划 <功能详细描述>
    * 
    * @param connection
    * @param list
    * @throws SQLException
    */
    protected void insertT6289(Connection connection, List<T6289> list)
        throws SQLException
    {
        try (PreparedStatement ps =
            connection.prepareStatement("INSERT INTO S62.T6289(F02,F03,F04,F05,F06,F07,F08,F09,F10,F11,F12,F13) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)"))
        {
            for (T6289 entity : list)
            {
                ps.setInt(1, entity.F02);
                ps.setInt(2, entity.F03);
                ps.setInt(3, entity.F04);
                ps.setInt(4, entity.F05);
                ps.setInt(5, entity.F06);
                ps.setBigDecimal(6, entity.F07);
                ps.setDate(7, entity.F08);
                ps.setString(8, entity.F09.name());
                ps.setTimestamp(9, entity.F10);
                ps.setBigDecimal(10, entity.F11);
                ps.setInt(11, entity.F12);
                ps.setInt(12, entity.F13);
                ps.addBatch();
            }
            ps.executeBatch();
        }
        
    }
    
    /**
    * 加息券生成还款计划 <功能详细描述>
    * 
    * @param connection
    * @param t6230
    * @param t6524
    * @param t6324
    * @throws Throwable
    */
    protected void hkjh(Connection connection, T6230 t6230, T6524 t6524, int couponId, int creditorId)
        throws Throwable
    {
        resourceProvider.log("================加息券生成利息返还计划===================开始");
        // 获取数据库当前时间
        final Date currentDate = getCurrentDate(connection);
        // 加息券起息日期
        final Date interestDate = new Date(currentDate.getTime() + Long.valueOf(t6230.F19) * 86400000);
        // 加息券结束日期
        final Date endDate = new Date(DateHelper.rollMonth(interestDate.getTime(), t6230.F09));
        List<T6289> list = new ArrayList<T6289>();
        if (t6230.F17 == T6230_F17.ZRY)
        {
            switch (t6230.F10)
            {
                case DEBX:
                {
                    list = calJXQ_ZRY_DEBX(connection, t6230, t6524, interestDate, endDate, couponId, creditorId);
                    break;
                }
                case MYFX:
                {
                    list = calJXQ_ZRY_MYFX(connection, t6230, t6524, interestDate, endDate, couponId, creditorId);
                    break;
                }
                case YCFQ:
                {
                    list = calJXQ_YCFQ(connection, t6230, t6524, interestDate, endDate, couponId, creditorId);
                    break;
                }
                case DEBJ:
                {
                    list = calJXQ_ZRY_DEBJ(connection, t6230, t6524, interestDate, endDate, couponId, creditorId);
                    break;
                }
                default:
                    throw new LogicalException("不支持的还款方式");
            }
        }
        else if (t6230.F17 == T6230_F17.GDR)
        {
            switch (t6230.F10)
            {
                case DEBX:
                {
                    list = calJXQ_GDR_DEBX(connection, t6230, t6524, interestDate, endDate, couponId, creditorId);
                    break;
                }
                case MYFX:
                {
                    list = calJXQ_GDR_MYFX(connection, t6230, t6524, interestDate, endDate, couponId, creditorId);
                    break;
                }
                case YCFQ:
                {
                    list = calJXQ_YCFQ(connection, t6230, t6524, interestDate, endDate, couponId, creditorId);
                    break;
                }
                case DEBJ:
                {
                    list = calJXQ_GDR_DEBJ(connection, t6230, t6524, interestDate, endDate, couponId, creditorId);
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
        insertT6289(connection, list);
        resourceProvider.log("================加息券生成利息返还计划===================结束");
    }
    
    /**
    * 加息券自然月等额本息生成利息返还计划 <功能详细描述>
    * 
    * @param connection
    * @param t6230
    * @param t6524
    * @param interestDate
    * @param endDate
    * @param couponId
    * @param creditorId
    * @return
    * @throws Throwable
    */
    protected List<T6289> calJXQ_ZRY_DEBX(Connection connection, T6230 t6230, T6524 t6524, Date interestDate,
        Date endDate, int couponId, int creditorId)
        throws Throwable
    {
        resourceProvider.log("=================加息券自然月【等额本息】生成利息返还计划==================开始");
        List<T6289> list = new ArrayList<T6289>();
        // 月利率
        BigDecimal monthRate =
            t6524.F05.setScale(DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP).divide(new BigDecimal(12),
                DECIMAL_SCALE,
                BigDecimal.ROUND_HALF_UP);
        BigDecimal remainTotal = t6524.F06;
        BigDecimal monthPayTotal = debx(t6524.F06, monthRate, t6230.F09);
        for (int i = 1; i <= t6230.F09; i++)
        {
            Date date = new Date(DateHelper.rollMonth(interestDate.getTime(), i));
            BigDecimal interest = remainTotal.multiply(monthRate).setScale(2, BigDecimal.ROUND_HALF_UP);
            {
                T6289 entity = new T6289();
                entity.F02 = t6524.F03;
                entity.F03 = getPTID(connection);
                entity.F04 = t6524.F02;
                entity.F05 = FeeCode.JX_REWARD;
                entity.F06 = i;
                if (t6230.F09 == i)
                {
                    entity.F07 =
                        monthPayTotal.subtract(remainTotal).compareTo(BigDecimal.ZERO) > 0 ? monthPayTotal.subtract(remainTotal)
                            : BigDecimal.ZERO;
                }
                else
                {
                    entity.F07 = interest;
                }
                entity.F08 = date;
                entity.F09 = T6289_F09.WFH;
                entity.F10 = null;
                entity.F12 = couponId;
                entity.F13 = creditorId;
                list.add(entity);
            }
            // 剩余本金 -【每月还款额-当月还款利息】
            remainTotal = remainTotal.subtract(monthPayTotal.subtract(interest));
        }
        resourceProvider.log("=================加息券自然月【等额本息】生成利息返还计划==================结束");
        return list;
    }
    
    /**
    * 加息券自然月每月付息生成利息返还计划 <功能详细描述>
    * 
    * @param connection
    * @param t6230
    * @param t6524
    * @param interestDate
    * @param endDate
    * @return
    * @throws Throwable
    */
    protected List<T6289> calJXQ_ZRY_MYFX(Connection connection, T6230 t6230, T6524 t6524, Date interestDate,
        Date endDate, int couponId, int creditorId)
        throws Throwable
    {
        resourceProvider.log("=================加息券自然月【每月付息】生成利息返还计划==================开始");
        List<T6289> list = new ArrayList<T6289>();
        BigDecimal monthsOfYear = new BigDecimal(12);
        // 计算总利息
        BigDecimal totalInterest =
            (t6524.F06.multiply(t6524.F05).divide(monthsOfYear, DECIMAL_SCALE, BigDecimal.ROUND_DOWN).multiply(new BigDecimal(
                t6230.F09))).setScale(2, BigDecimal.ROUND_HALF_UP);
        for (int i = 1; i <= t6230.F09; i++)
        {
            // 返还利息日期
            Date date = new Date(DateHelper.rollMonth(interestDate.getTime(), i));
            T6289 entity = new T6289();
            entity.F02 = t6524.F03;
            entity.F03 = getPTID(connection);
            entity.F04 = t6524.F02;
            entity.F05 = FeeCode.JX_REWARD;
            entity.F06 = i;
            entity.F07 = t6524.F06.multiply(t6524.F05).divide(monthsOfYear, 2, BigDecimal.ROUND_DOWN);
            entity.F08 = date;
            entity.F09 = T6289_F09.WFH;
            entity.F10 = null;
            if (i < t6230.F09)
            {
                totalInterest = totalInterest.subtract(entity.F07);
            }
            entity.F11 = totalInterest;
            entity.F12 = couponId; // 用户使用加息券Id
            entity.F13 = creditorId; // 债权ID
            list.add(entity);
        }
        resourceProvider.log("=================加息券自然月【每月付息】生成利息返还计划==================结束");
        return list;
    }
    
    /**
    * 加息券一次付清利息返还计划 <功能详细描述>
    * 
    * @param connection
    * @param t6230
    * @param t6524
    * @param interestDate
    * @param endDate
    * @param couponId
    * @param creditorId
    * @return
    * @throws Throwable
    */
    public List<T6289> calJXQ_YCFQ(Connection connection, T6230 t6230, T6524 t6524, Date interestDate, Date endDate,
        int couponId, int creditorId)
        throws Throwable
    {
        resourceProvider.log("=================加息券【一次付清】生成利息返还计划==================开始");
        List<T6289> list = new ArrayList<T6289>();
        int days = 0;
        T6231_F21 dayOrMonth = T6231_F21.F; // 借款方式
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F21, F22 FROM S62.T6231 WHERE F01 = ? LIMIT 1 FOR UPDATE"))
        {
            pstmt.setInt(1, t6230.F01);
            try (ResultSet rs = pstmt.executeQuery())
            {
                if (rs.next())
                {
                    dayOrMonth = T6231_F21.parse(rs.getString(1));
                    days = IntegerParser.parse(rs.getInt(2));
                }
            }
        }
        // 分支逻辑：按天计算还款计划
        if (T6231_F21.S == dayOrMonth)
        {
            if (days <= 0)
            {
                throw new LogicalException("按天借款，其天数必须大于0");
            }
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(endDate.getTime());
            cal.add(Calendar.DATE, days);
            endDate = new java.sql.Date(cal.getTimeInMillis());
            return calJXQYCFQ_BY_DAYS(connection, t6230, t6524, interestDate, endDate, days, couponId, creditorId);
        }
        {
            BigDecimal interest =
                t6524.F06.setScale(DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP)
                    .multiply(t6524.F05)
                    .multiply(new BigDecimal(t6230.F09))
                    .divide(new BigDecimal(12), DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
            T6289 entity = new T6289();
            entity.F02 = t6524.F03;
            entity.F03 = getPTID(connection);
            entity.F04 = t6524.F02;
            entity.F05 = FeeCode.JX_REWARD;
            entity.F06 = 1;
            entity.F07 = interest.setScale(2, BigDecimal.ROUND_HALF_UP);
            entity.F08 = endDate;
            entity.F09 = T6289_F09.WFH;
            entity.F10 = null;
            entity.F12 = couponId;
            entity.F13 = creditorId;
            list.add(entity);
        }
        resourceProvider.log("=================加息券【一次付清】生成利息返还计划==================结束");
        return list;
    }
    
    protected List<T6289> calJXQYCFQ_BY_DAYS(Connection connection, T6230 t6230, T6524 t6524, Date interestDate,
        Date endDate, int days, int couponId, int creditorId)
        throws Throwable
    {
        ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        List<T6289> list = new ArrayList<T6289>();
        BigDecimal interest =
            t6524.F06.setScale(9, BigDecimal.ROUND_HALF_UP)
                .multiply(t6524.F05)
                .multiply(new BigDecimal(days))
                .divide(new BigDecimal(configureProvider.getProperty(SystemVariable.REPAY_DAYS_OF_YEAR)),
                    DECIMAL_SCALE,
                    BigDecimal.ROUND_HALF_UP);
        T6289 entity = new T6289();
        entity.F02 = t6524.F03;
        entity.F03 = getPTID(connection);
        entity.F04 = t6524.F02;
        entity.F05 = FeeCode.JX_REWARD;
        entity.F06 = 1;
        entity.F07 = interest.setScale(2, BigDecimal.ROUND_HALF_UP);
        entity.F08 = endDate;
        entity.F09 = T6289_F09.WFH;
        entity.F10 = null;
        entity.F12 = couponId;
        entity.F13 = creditorId;
        list.add(entity);
        return list;
    }
    
    /**
    * 加息券自然月等额本息还款生成返还利息计划 <功能详细描述>
    * 
    * @param connection
    * @param t6230
    * @param t6524
    * @param interestDate
    * @param endDate
    * @param couponId
    * @param creditorId
    * @return
    * @throws Throwable
    */
    protected List<T6289> calJXQ_ZRY_DEBJ(Connection connection, T6230 t6230, T6524 t6524, Date interestDate,
        Date endDate, int couponId, int creditorId)
        throws Throwable
    {
        resourceProvider.log("=================加息券自然月【等额本金】生成利息返还计划==================开始");
        List<T6289> list = new ArrayList<T6289>();
        {
            Calendar c = Calendar.getInstance();
            c.setTime(interestDate);
            c.add(Calendar.MONTH, t6230.F09);
            //endDate = new Date(c.getTimeInMillis());
        }
        // 使用加息券投资金额
        BigDecimal x = t6524.F06;
        // 加息券月利率
        BigDecimal y = t6524.F05.divide(new BigDecimal(12), DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
        // 借款期限
        int terms = t6230.F09;
        // 月还本金【等额本金：每期本金=借款金额/总期数】
        BigDecimal monthAmount = x.divide(new BigDecimal(terms), 2, BigDecimal.ROUND_HALF_UP);
        // 还本金总额
        BigDecimal total = BigDecimal.ZERO;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(interestDate);
        for (int i = 1; i <= terms; i++)
        {
            calendar.add(Calendar.MONTH, 1);
            BigDecimal interest = BigDecimal.ZERO;
            if (i == terms)
            {
                BigDecimal bj = x.subtract(total).setScale(2, BigDecimal.ROUND_HALF_UP);
                interest = bj.multiply(y).setScale(2, BigDecimal.ROUND_HALF_UP);
                T6289 entity = new T6289();
                entity.F02 = t6524.F03;
                entity.F03 = getPTID(connection);
                entity.F04 = t6524.F02;
                entity.F05 = FeeCode.JX_REWARD;
                entity.F06 = i;
                entity.F07 = interest;
                entity.F08 = new Date(calendar.getTimeInMillis());
                entity.F09 = T6289_F09.WFH;
                entity.F10 = null;
                entity.F12 = couponId;
                entity.F13 = creditorId;
                list.add(entity);
                continue;
            }
            interest = x.subtract(total).multiply(y).setScale(2, BigDecimal.ROUND_HALF_UP);
            {
                T6289 entity = new T6289();
                entity.F02 = t6524.F03;
                entity.F03 = getPTID(connection);
                entity.F04 = t6524.F02;
                entity.F05 = FeeCode.JX_REWARD;
                entity.F06 = i;
                entity.F07 = interest;
                entity.F08 = new Date(calendar.getTimeInMillis());
                entity.F09 = T6289_F09.WFH;
                entity.F10 = null;
                entity.F12 = couponId;
                entity.F13 = creditorId;
                list.add(entity);
            }
            total = total.add(monthAmount);
        }
        resourceProvider.log("=================加息券自然月【等额本金】生成利息返还计划==================结束");
        return list;
    }
    
    /**
    * 加息券固定日等额本息还款方式生成利息返还计划 <功能详细描述>
    * 
    * @param connection
    * @param t6230
    * @param t6524
    * @param interestDate
    * @param endDate
    * @param couponId
    * @param creditorId
    * @return
    * @throws Throwable
    */
    protected List<T6289> calJXQ_GDR_DEBX(Connection connection, T6230 t6230, T6524 t6524, Date interestDate,
        Date endDate, int couponId, int creditorId)
        throws Throwable
    {
        resourceProvider.log("=================加息券固定日【等额本息】生成利息返还计划==================开始");
        ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        List<T6289> list = new ArrayList<T6289>();
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
            return calJXQ_ZRY_DEBX(connection, t6230, t6524, interestDate, endDate, couponId, creditorId);
        }
        if (t6230.F18 > 28)
        {
            throw new LogicalException("固定付息日不能大于28");
        }
        // 加息券月利率
        BigDecimal x = t6524.F05.divide(new BigDecimal(12), DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
        // 使用加息券投资金额
        BigDecimal y = t6524.F06;
        // 加息券日利率
        BigDecimal z =
            t6524.F05.divide(new BigDecimal(
                IntegerParser.parse(configureProvider.format(SystemVariable.REPAY_DAYS_OF_YEAR))),
                DECIMAL_SCALE,
                BigDecimal.ROUND_HALF_UP);
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
                { // 利息
                    T6289 entity = new T6289();
                    entity.F02 = t6524.F03;
                    entity.F03 = getPTID(connection);
                    entity.F04 = t6524.F02;
                    entity.F05 = FeeCode.JX_REWARD;
                    entity.F06 = i;
                    entity.F07 = interest;
                    entity.F08 = new Date(ca.getTimeInMillis());
                    entity.F09 = T6289_F09.WFH;
                    entity.F10 = null;
                    entity.F12 = couponId;
                    entity.F13 = creditorId;
                    list.add(entity);
                }
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
                { // 利息
                    T6289 entity = new T6289();
                    entity.F02 = t6524.F03;
                    entity.F03 = getPTID(connection);
                    entity.F04 = t6524.F02;
                    entity.F05 = FeeCode.JX_REWARD;
                    entity.F06 = i;
                    entity.F07 = interest;
                    entity.F08 = new Date(ca.getTimeInMillis());
                    entity.F09 = T6289_F09.WFH;
                    entity.F10 = null;
                    entity.F12 = couponId;
                    entity.F13 = creditorId;
                    list.add(entity);
                }
                continue;
            }
            interest = y.subtract(total).multiply(x).setScale(2, BigDecimal.ROUND_HALF_UP);
            bj = amount.subtract(interest).setScale(2, BigDecimal.ROUND_HALF_UP);
            total = total.add(bj);
            { // 利息
                T6289 entity = new T6289();
                entity.F02 = t6524.F03;
                entity.F03 = getPTID(connection);
                entity.F04 = t6524.F02;
                entity.F05 = FeeCode.JX_REWARD;
                entity.F06 = i;
                entity.F07 = interest;
                entity.F08 = new Date(ca.getTimeInMillis());
                entity.F09 = T6289_F09.WFH;
                entity.F10 = null;
                entity.F12 = couponId;
                entity.F13 = creditorId;
                list.add(entity);
            }
            ca.add(Calendar.MONTH, 1);
        }
        resourceProvider.log("=================加息券固定日【等额本息】生成利息返还计划==================结束");
        return list;
    }
    
    /**
    * 加息券固定日每月付息生成利息返还计划 <功能详细描述>
    * 
    * @param connection
    * @param t6230
    * @param t6524
    * @param interestDate
    * @param endDate
    * @param couponId
    * @param creditorId
    * @return
    * @throws Throwable
    */
    protected List<T6289> calJXQ_GDR_MYFX(Connection connection, T6230 t6230, T6524 t6524, Date interestDate,
        Date endDate, int couponId, int creditorId)
        throws Throwable
    {
        resourceProvider.log("=================加息券固定日【每月付息】生成利息返还计划==================开始");
        ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        // 生成还款记录
        List<T6289> list = new ArrayList<T6289>();
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
            return calJXQ_ZRY_MYFX(connection, t6230, t6524, interestDate, endDate, couponId, creditorId);
        }
        final int term = t6230.F09 + 1;// 总期数
        BigDecimal yearDays =
            new BigDecimal(IntegerParser.parse(configureProvider.getProperty(SystemVariable.REPAY_DAYS_OF_YEAR), 360));
        // 首期付息天数
        int firstTermDays = 0;
        Date payDate;
        if (inserestStartDay < t6230.F18)
        {
            calendar.set(Calendar.DAY_OF_MONTH, t6230.F18);
            payDate = new Date(calendar.getTimeInMillis());
            firstTermDays = t6230.F18 - inserestStartDay;
        }
        else
        {
            calendar.set(Calendar.DAY_OF_MONTH, t6230.F18);
            payDate = new Date(DateHelper.rollMonth(calendar.getTimeInMillis(), 1));
            firstTermDays = 30 - inserestStartDay + t6230.F18;
        }
        // 首期利息
        BigDecimal firstTermInterest = BigDecimal.ZERO;
        // 月利率
        BigDecimal monthRate = t6524.F05.divide(new BigDecimal(12), DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
        for (int i = 1; i <= term; i++)
        {
            // 利息
            BigDecimal interest = BigDecimal.ZERO;
            if (i == term)
            {
                payDate = endDate;
                interest =
                    t6524.F06.multiply(monthRate)
                        .subtract(firstTermInterest)
                        .setScale(DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
            }
            else if (i == 1)
            {
                interest =
                    t6524.F06.multiply(t6524.F05)
                        .multiply(new BigDecimal(firstTermDays))
                        .divide(yearDays, DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
                firstTermInterest = interest;
            }
            else
            {
                interest = t6524.F06.multiply(monthRate).setScale(DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
            }
            {
                // 利息
                T6289 entity = new T6289();
                entity.F02 = t6524.F03;
                entity.F03 = getPTID(connection);
                entity.F04 = t6524.F02;
                entity.F05 = FeeCode.JX_REWARD;
                entity.F06 = i;
                entity.F07 = interest.setScale(2, BigDecimal.ROUND_HALF_UP);
                entity.F08 = payDate;
                entity.F09 = T6289_F09.WFH;
                entity.F10 = null;
                entity.F12 = couponId;
                entity.F13 = creditorId;
                list.add(entity);
            }
            payDate = new Date(DateHelper.rollMonth(payDate.getTime(), 1));
        }
        
        resourceProvider.log("=================加息券固定日【每月付息】生成利息返还计划==================结束");
        return list;
    }
    
    protected List<T6289> calJXQ_GDR_DEBJ(Connection connection, T6230 t6230, T6524 t6524, Date interestDate,
        Date endDate, int couponId, int creditorId)
        throws Throwable
    {
        resourceProvider.log("=================加息券固定日【等额本金】生成利息返还计划==================开始");
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
            return calJXQ_ZRY_DEBJ(connection, t6230, t6524, interestDate, endDate, couponId, creditorId);
        }
        if (t6230.F18 > 28)
        {
            throw new LogicalException("固定付息日不能大于28");
        }
        // 使用加息券投资金额
        BigDecimal x = t6524.F06;
        // 加息券月利率
        BigDecimal y = t6524.F05.divide(new BigDecimal(12), DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
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
        BigDecimal total = BigDecimal.ZERO;
        BigDecimal monthAmount = x.divide(new BigDecimal(n), 2, BigDecimal.ROUND_HALF_UP);
        List<T6289> list = new ArrayList<T6289>();
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
                { // 利息
                    T6289 entity = new T6289();
                    entity.F02 = t6524.F03;
                    entity.F03 = getPTID(connection);
                    entity.F04 = t6524.F02;
                    entity.F05 = FeeCode.JX_REWARD;
                    entity.F06 = i;
                    entity.F07 = interest.setScale(2, BigDecimal.ROUND_HALF_UP);
                    entity.F08 = new Date(ca.getTimeInMillis());
                    entity.F09 = T6289_F09.WFH;
                    entity.F10 = null;
                    entity.F12 = couponId;
                    entity.F13 = creditorId;
                    list.add(entity);
                }
                continue;
            }
            if (i == terms)
            {
                // total = total.subtract(monthAmount);
                bj = x.subtract(total).setScale(2, BigDecimal.ROUND_HALF_UP);
                interest =
                    bj.multiply(y)
                        .multiply(new BigDecimal(l))
                        .divide(new BigDecimal(f).add(new BigDecimal(l)), DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
                { // 利息
                    T6289 entity = new T6289();
                    entity.F02 = t6524.F03;
                    entity.F03 = getPTID(connection);
                    entity.F04 = t6524.F02;
                    entity.F05 = FeeCode.JX_REWARD;
                    entity.F06 = i;
                    entity.F07 = interest.setScale(2, BigDecimal.ROUND_HALF_UP);
                    entity.F08 = new Date(ca.getTimeInMillis());
                    entity.F09 = T6289_F09.WFH;
                    entity.F10 = null;
                    entity.F12 = couponId;
                    entity.F13 = creditorId;
                    list.add(entity);
                }
                continue;
            }
            interest = x.subtract(total).multiply(y).setScale(2, BigDecimal.ROUND_HALF_UP);
            bj = monthAmount;
            total = total.add(bj);
            ca.add(Calendar.MONTH, 1);
            { // 利息
                T6289 entity = new T6289();
                entity.F02 = t6524.F03;
                entity.F03 = getPTID(connection);
                entity.F04 = t6524.F02;
                entity.F05 = FeeCode.JX_REWARD;
                entity.F06 = i;
                entity.F07 = interest;
                entity.F08 = new Date(ca.getTimeInMillis());
                entity.F09 = T6289_F09.WFH;
                entity.F10 = null;
                entity.F12 = couponId;
                entity.F13 = creditorId;
                list.add(entity);
            }
        }
        resourceProvider.log("=================加息券固定日【等额本金】生成利息返还计划==================结束");
        return list;
    }
}
