package com.dimeng.p2p.escrow.fuyou.executor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.data.sql.SQLConnection;
import com.dimeng.framework.resource.AchieveVersion;
import com.dimeng.framework.resource.Resource;
import com.dimeng.framework.resource.ResourceAnnotation;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.OrderType;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S62.entities.T6230;
import com.dimeng.p2p.S62.entities.T6231;
import com.dimeng.p2p.S62.entities.T6238;
import com.dimeng.p2p.S62.entities.T6250;
import com.dimeng.p2p.S62.entities.T6292;
import com.dimeng.p2p.S62.enums.T6230_F10;
import com.dimeng.p2p.S62.enums.T6230_F11;
import com.dimeng.p2p.S62.enums.T6230_F12;
import com.dimeng.p2p.S62.enums.T6230_F13;
import com.dimeng.p2p.S62.enums.T6230_F14;
import com.dimeng.p2p.S62.enums.T6230_F15;
import com.dimeng.p2p.S62.enums.T6230_F16;
import com.dimeng.p2p.S62.enums.T6230_F17;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.S62.enums.T6230_F27;
import com.dimeng.p2p.S62.enums.T6231_F27;
import com.dimeng.p2p.S62.enums.T6250_F07;
import com.dimeng.p2p.S62.enums.T6292_F07;
import com.dimeng.p2p.S65.entities.T6501;
import com.dimeng.p2p.S65.entities.T6505;
import com.dimeng.p2p.S65.entities.T6517;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.S65.enums.T6501_F07;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouRespCode;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouTypeEnum;
import com.dimeng.p2p.escrow.fuyou.service.TransferManage;
import com.dimeng.p2p.escrow.fuyou.util.MchntTxnSsn;
import com.dimeng.p2p.escrow.fuyou.util.PayUtil;
import com.dimeng.p2p.order.TenderConfirmExecutor;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BigDecimalParser;
import com.dimeng.util.parser.EnumParser;

/**
 * 
 * 放款
 * <优化版-20160127>
 * 
 * @author  heshiping
 * @version  [版本号, 2015年11月25日]
 */
@AchieveVersion(version = 2160229)
@ResourceAnnotation
public class FYTenderConfirmExecutor extends TenderConfirmExecutor
{
    
    public FYTenderConfirmExecutor(ResourceProvider resourceProvider)
    {
        super(resourceProvider);
    }
    
    @Override
    public Class<? extends Resource> getIdentifiedType()
    {
        return FYTenderConfirmExecutor.class;
    }
    
    /**
     * {@inheritDoc}
     * 放款接口调用
     */
    @Override
    protected void callFace(SQLConnection connection, int orderId, Map<String, String> params)
        throws SQLException
    {
        ServiceProvider serviceProvider = resourceProvider.getResource(ServiceProvider.class);
        try (ServiceSession serviceSession = serviceProvider.createServiceSession())
        {
            // 放款订单
            T6505 t6505 = selectT6505(connection, orderId);
            // 标信息(借款用户ID)
            int outId = selectT6230s(connection, t6505.F03);
            // 获取投标记录
            T6250 t6250 = this.selectT6250(connection, t6505.F04, T6250_F07.F);
            T6501 t6501 = selectT6501(connection, orderId);
            // 红包
            T6292 t6292 = selectT6292(connection, t6505.F04, T6292_F07.S);
            BigDecimal hbAmt = BigDecimal.ZERO;
            if (t6292 != null)
            {
                hbAmt = t6292.F04;
            }
            // 商户代码
            logger.info("放款订单：" + orderId + "投资金额=" + t6505.F05 + " 红包=" + hbAmt);
            TransferManage manage = serviceSession.getService(TransferManage.class);
            Map<String, Object> map =
                manage.createTransferMap(t6501.F10,
                    getUserCustId(connection, t6505.F02),
                    getUserCustId(connection, outId),
                    PayUtil.getAmt(t6505.F05.subtract(hbAmt)),
                    t6250.F10);
            
            if (map != null
                && (FuyouRespCode.JYCG.getRespCode().equals(map.get("resp_code")) || FuyouRespCode.JYWC.getRespCode()
                    .equals(map.get("resp_code"))))
            {
                logger.info("放款成功-订单：" + orderId);
                params.put("resp_code", map.get("resp_code").toString());
                params.put("success", "true");
                
                // 查询标信息
                T6230 t6230 = selectT6230F(connection, t6505.F03);
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
                    logger.info(t6230.F01 + "：标-放款成功生成服务费");
                    // 收成交服务费
                    T6238 t6238 = selectT6238(connection, t6230.F01);
                    // 判断是否已全部放款完成
                    if (t6238 != null && t6238.F02.compareTo(BigDecimal.ZERO) > 0)
                    {
                        // 查询标扩展信息
                        //                            T6231 t6231 = selectT6231(connection, t6505.F03);
                        // 标成交服务费
                        BigDecimal fwf = getCJFee(connection, t6238, t6230);
                        // 插入转账订单
                        T6501 zzt6501 = new T6501();
                        zzt6501.F02 = OrderType.TRANSFER.orderType();
                        zzt6501.F03 = T6501_F03.DTJ;
                        zzt6501.F07 = T6501_F07.XT;
                        zzt6501.F08 = t6230.F02;
                        zzt6501.F10 = MchntTxnSsn.getMts(FuyouTypeEnum.CJFF.name());
                        zzt6501.F12 = String.format("散标成交服务费:%s，标题：%s", t6230.F25, t6230.F03);
                        zzt6501.F04 = getCurrentTimestamp(connection);
                        zzt6501.F13 = fwf;
                        int ordId = insertT6501(connection, zzt6501);
                        T6517 t6517 = new T6517();
                        t6517.F01 = ordId;
                        t6517.F02 = fwf;
                        t6517.F03 = t6230.F02;
                        t6517.F04 = getPTID(connection);
                        t6517.F05 = String.format("散标成交服务费:%s，标题：%s", t6230.F25, t6230.F03);
                        t6517.F06 = FeeCode.CJFWF;
                        insertT6517(connection, t6517);
                    }
                }
                if (t6292 != null)
                {
                    // 插入转账订单
                    T6501 zzt6501 = new T6501();
                    zzt6501.F02 = OrderType.TRANSFER.orderType();
                    zzt6501.F03 = T6501_F03.DTJ;
                    zzt6501.F07 = T6501_F07.XT;
                    zzt6501.F08 = getPTID(connection);
                    zzt6501.F10 = MchntTxnSsn.getMts(FuyouTypeEnum.HBJL.name());
                    zzt6501.F12 = String.format("散标投资红包放款:%s，标题：%s", t6230.F25, t6230.F03);
                    zzt6501.F04 = getCurrentTimestamp(connection);
                    zzt6501.F13 = t6292.F04;
                    int ordId = insertT6501(connection, zzt6501);
                    T6517 t6517 = new T6517();
                    t6517.F01 = ordId;
                    t6517.F02 = t6292.F04;
                    t6517.F03 = getPTID(connection);
                    t6517.F04 = t6230.F02;
                    t6517.F05 = String.format("散标投资红包放款:%s，标题：%s", t6230.F25, t6230.F03);
                    t6517.F06 = FeeCode.TZ_TBHB;
                    insertT6517(connection, t6517);
                }
            }
            else
            {
                params.put("success", "false");
                logger.info("放款失败请稍后试");
                throw new LogicalException("放款失败请稍后试!");
            }
        }
        catch (Throwable e)
        {
            logger.error("放款失败请稍后试", e);
            throw new LogicalException("放款或流标失败!");
        }
    }
    
    /**
     * {@inheritDoc}
     * 持续推广奖励
     */
    @Override
    protected void tzjl(Connection connection, T6230 t6230, T6505 t6505, T6101 wlzh, T6101 ptwlzh)
        throws Throwable
    {
        ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        // 标的单笔投资金额
        BigDecimal t6505_f05 = t6505.F05;
        BigDecimal baseReward = BigDecimalParser.parse(configureProvider.getProperty(SystemVariable.TG_TZJS));
        
        if (BigDecimal.ZERO.compareTo(baseReward) >= 0 || t6505_f05.compareTo(baseReward) < 0)
        {
            logger.info("持续推广奖励金额未达标");
            return;
        }
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
        // 推广用户
        int tgyh = 0;
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
        // 投资人第三方托管账户
        String thirdParyUserName = getUserCustId((SQLConnection)connection, tgyh);
        if (StringHelper.isEmpty(thirdParyUserName))
        {
            resourceProvider.log("用户没有注册资金托管账户，发放奖励失败");
            return;
        }
        T6101 tgrzh = null;
        if (tgyh == t6230.F02)
        {
            tgrzh = wlzh;
        }
        else
        {
            tgrzh = selectT6101(connection, tgyh, T6101_F03.WLZH, false);
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
        // 金额需大于0
        if (jlje.compareTo(BigDecimal.ZERO) > 0)
        {
            // 插入转账订单
            T6501 zzt6501 = new T6501();
            zzt6501.F02 = OrderType.TRANSFER.orderType();
            zzt6501.F03 = T6501_F03.DTJ;
            zzt6501.F07 = T6501_F07.XT;
            zzt6501.F08 = ptwlzh.F02;
            zzt6501.F10 = MchntTxnSsn.getMts(FuyouTypeEnum.TGCX.name());
            zzt6501.F12 = "invest" + t6505.F01;
            zzt6501.F04 = getCurrentTimestamp(connection);
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
    }
    
    /**
     * {@inheritDoc}
     * 奖励标
     */
    @Override
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
            // 插入转账订单
            if (jlje.compareTo(BigDecimal.ZERO) <= 0)
            {
                return;
            }
            T6501 t6501 = new T6501();
            t6501.F02 = OrderType.TRANSFER.orderType();
            t6501.F03 = T6501_F03.DTJ;
            t6501.F07 = T6501_F07.XT;
            t6501.F08 = ptwlzh.F02;
            t6501.F10 = MchntTxnSsn.getMts(FuyouTypeEnum.TZJL.name());
            t6501.F12 = String.format("散标投资奖励:%s", t6230.F25);
            t6501.F04 = getCurrentTimestamp(connection);
            t6501.F13 = jlje;
            int ordId = insertT6501(connection, t6501);
            T6517 t6517 = new T6517();
            t6517.F01 = ordId;
            t6517.F02 = jlje;
            t6517.F03 = ptwlzh.F02;
            t6517.F04 = tzrwlzh.F02;
            t6517.F05 = String.format("散标投资奖励:%s", t6230.F25);
            t6517.F06 = FeeCode.TZ_TBJL;
            insertT6517(connection, t6517);
        }
    }
    
    @Override
    protected T6250 selectT6250(Connection connection, int F01, T6250_F07 F07)
        throws SQLException
    {
        T6250 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F10 FROM S62.T6250 WHERE T6250.F01 = ? AND T6250.F07 = ? LIMIT 1"))
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
                    record.F10 = resultSet.getString(8);
                }
            }
        }
        return record;
    }
    
    private T6230 selectT6230F(Connection connection, int F01)
        throws SQLException
    {
        T6230 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11, F12, F13, F14, F15, F16, F17, F18, F19, F20, F21, F22, F23, F24, F25, F26, F27 FROM S62.T6230 WHERE T6230.F01 = ? "))
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
}
