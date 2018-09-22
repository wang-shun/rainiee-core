package com.dimeng.p2p.escrow.fuyou.executor;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S63.enums.T6340_F03;
import com.dimeng.p2p.S63.enums.T6340_F04;
import com.dimeng.p2p.S65.entities.T6501;
import com.dimeng.p2p.S65.entities.T6502;
import com.dimeng.p2p.S65.entities.T6517;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.S65.enums.T6501_F07;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouTypeEnum;
import com.dimeng.p2p.escrow.fuyou.util.MchntTxnSsn;
import com.dimeng.p2p.escrow.fuyou.variables.FuyouVariable;
import com.dimeng.p2p.order.ChargeOrderExecutor;
import com.dimeng.p2p.service.ActivityCommon;
import com.dimeng.p2p.variables.defines.LetterVariable;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.Formater;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BooleanParser;
import com.dimeng.util.parser.DateTimeParser;
import com.dimeng.util.parser.IntegerParser;

/**
 * 富友充值处理
 * <优化版-20160126>
 * 
 * @author heshiping
 * @date 2015.5.16
 */
@ResourceAnnotation
public class FYChargeOrderExecutor extends ChargeOrderExecutor {
    
    public FYChargeOrderExecutor(ResourceProvider resourceProvider) {
        super(resourceProvider);
    }
    
    @Override
    public Class<? extends Resource> getIdentifiedType() {
        return FYChargeOrderExecutor.class;
    }
    
    @Override
    protected void doConfirm(SQLConnection connection, int orderId, Map<String, String> params) throws Throwable {
        try {
            ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
            T6502 t6502 = selectT6502(connection, orderId); // 充值订单信息
            if (t6502 == null) {
                throw new ParameterException("订单不存在");
            }
            int accountId = t6502.F02;
            Timestamp t = getCurrentTimestamp(connection);
            int pid = getPTID(connection);
            
            // 平台往来账户信息
            T6101 cT6101 = selectT6101(connection, pid, T6101_F03.WLZH, true);
            if (cT6101 == null) {
                throw new LogicalException("平台往来账户不存在");
            }
            
            if (t6502.F02 == pid) {
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
                // 充值成本
                if (t6502.F05.compareTo(BigDecimal.ZERO) > 0) {
                    cT6101.F06 = cT6101.F06.subtract(t6502.F05);
                    cT6102.F03 = FeeCode.CZ_CB;
                    cT6102.F04 = cT6102.F01;
                    cT6102.F05 = t;
                    cT6102.F06 = BigDecimal.ZERO;
                    cT6102.F07 = t6502.F05;
                    cT6102.F08 = cT6101.F06;
                    cT6102.F09 = "平台充值成本";
                    insertT6102(connection, cT6102);
                }
                updateT6101(connection, cT6101.F06, cT6101.F01);
                return;
            }
            
            // 用户往来账户信息
            T6101 uT6101 = selectT6101(connection, accountId, T6101_F03.WLZH, true);
            if (uT6101 == null) {
                throw new LogicalException("用户往来账户不存在");
            }
            // 更新有效推广奖励统计
            try (PreparedStatement ps = connection.prepareStatement("SELECT F01 FROM S63.T6310 WHERE F01=? FOR UPDATE")) {
                ps.setInt(1, accountId);
                ps.execute();
            }
            try (PreparedStatement ps = connection.prepareStatement("SELECT F01 FROM S63.T6311 FOR UPDATE")) {
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
            if (t6502.F05.compareTo(BigDecimal.ZERO) > 0 && "ON".equals(configureProvider.format(FuyouVariable.FUYOU_CHAREFEE_ONOFF))) {
                uT6101.F06 = uT6101.F06.subtract(t6502.F05);
                uT6102.F03 = FeeCode.CZ_SXF;
                uT6102.F04 = uT6101.F01;
                uT6102.F05 = t;
                uT6102.F06 = BigDecimal.ZERO;
                uT6102.F07 = t6502.F05;
                uT6102.F08 = uT6101.F06;
                uT6102.F09 = "充值手续费";
                insertT6102(connection, uT6102);
            }  else {
				// 插入平台账户资金流水
				T6102 cT6102 = new T6102();
				cT6102.F02 = cT6101.F01;
				if (t6502.F04.compareTo(BigDecimal.ZERO) > 0) {
					cT6101.F06 = cT6101.F06.subtract(t6502.F04);
					cT6102.F03 = FeeCode.CZ_CB;
					cT6102.F04 = cT6101.F01;
					cT6102.F05 = t;
					cT6102.F06 = BigDecimal.ZERO;
					cT6102.F07 = t6502.F04;
					cT6102.F08 = cT6101.F06;
					cT6102.F09 = "充值成本";
					insertT6102(connection, cT6102);
					// 更新平台往来账户
					updateT6101(connection, cT6101.F06, cT6101.F01);
				}
			}
            updateT6101(connection, uT6101.F06, uT6101.F01);
            
            // 更新订单状态
            if (params != null && !StringHelper.isEmpty(params.get("mchnt_txn_ssn"))) {
                try (PreparedStatement pstmt =
                    connection.prepareStatement("UPDATE S65.T6502 SET F08 = ? WHERE F01 = ?")) {
                    pstmt.setString(1, params.get("mchnt_txn_ssn"));
                    pstmt.setInt(2, orderId);
                    pstmt.execute();
                }
            }
            // 推广处理
            if (BooleanParser.parse(configureProvider.getProperty(SystemVariable.ACCOUNT_SFTG))) {
                expand(connection, t6502, uT6101, cT6101);
            }
            
            try (ServiceSession serviceSession = resourceProvider.getResource(ServiceProvider.class).createServiceSession()) {
                //充值送红包和加息券
                ActivityCommon activityCommon = serviceSession.getService(ActivityCommon.class);
                activityCommon.sendActivity(connection, accountId, T6340_F03.redpacket.name(), T6340_F04.firstrecharge.name(), t6502.F03, 0);
                activityCommon.sendActivity(connection, accountId, T6340_F03.interest.name(),  T6340_F04.firstrecharge.name(), t6502.F03, 0);
                //首次充值送体验金
                activityCommon.sendActivity(connection, accountId,T6340_F03.experience.name(), T6340_F04.firstrecharge.name(), t6502.F03, 0);
                
                activityCommon.sendActivity(connection, accountId, T6340_F03.redpacket.name(), T6340_F04.recharge.name(),      t6502.F03, 0);
                activityCommon.sendActivity(connection, accountId, T6340_F03.interest.name(),  T6340_F04.recharge.name(),      t6502.F03, 0);
                //单笔充值送体验金
                activityCommon.sendActivity(connection, accountId,T6340_F03.experience.name(),T6340_F04.recharge.name(),t6502.F03, 0);
                int exid = searchExtendId(connection, t6502.F02);
                if (exid > 0) {
                    //推荐用户首次充值奖励送红包、加息券
                    activityCommon.sendActivity(connection, exid, T6340_F03.redpacket.name(), T6340_F04.tjsccz.name(), t6502.F03, accountId);
                    activityCommon.sendActivity(connection, exid, T6340_F03.interest.name(),  T6340_F04.tjsccz.name(), t6502.F03,  accountId);
                    //推荐用户首次充值奖励送体验金
                    activityCommon.sendActivity(connection, exid,T6340_F03.experience.name(), T6340_F04.tjsccz.name(),t6502.F03,accountId);
                }
            }
            
            //充值成功，发站内信
            T6110 t6110 = selectT6110(connection, accountId);
            Envionment envionment = configureProvider.createEnvionment();
            envionment.set("datetime", DateTimeParser.format(t));
            envionment.set("name", t6110.F02);
            envionment.set("amount", Formater.formatAmount(t6502.F03));
            String content = configureProvider.format(LetterVariable.CHARGE_SUCCESS, envionment);
            sendLetter(connection, accountId, "充值成功", content);            
        } catch (Exception e) {
            logger.error(e, e);
            throw e;
        }
    }
    
    @Override
    protected void expand(Connection connection, T6502 t6502, T6101 uT6101, T6101 cT6101)  throws Throwable {
        int accountId = t6502.F02;
        // 充值次数
        int chargeCount = selectChargeCount(connection, T6501_F03.CG, OrderType.CHARGE.orderType(), accountId);
        
        if (chargeCount > 0) {
            return;
        }
        ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        // 推广人id
        int exid = 0;
        Timestamp t = new Timestamp(System.currentTimeMillis());
        // 充值推广奖励
        String code = null;// 邀请码
        try (PreparedStatement ps = connection.prepareStatement("SELECT F03 FROM S61.T6111 WHERE F01=?")) {
            ps.setInt(1, accountId);
            try (ResultSet resultSet = ps.executeQuery()){
                if (resultSet.next()) {
                    code = resultSet.getString(1);
                }
            }
        }
        if (StringHelper.isEmpty(code)) {
            return;
        }
        try (PreparedStatement ps = connection.prepareStatement("SELECT F01 FROM S61.T6111 WHERE F02=? LIMIT 1")) {
            ps.setString(1, code);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    exid = resultSet.getInt(1);
                }
            }
        }
        if (exid <= 0) {
            return;
        }
        try (PreparedStatement ps = connection.prepareStatement("UPDATE S63.T6311 SET F04 = ?, F06 = ? WHERE F03 = ?")) {
            ps.setBigDecimal(1, t6502.F03);
            ps.setTimestamp(2, t);
            ps.setInt(3, accountId);
            ps.execute();
        }
        // 首次充值达标赠送推广奖励【定时任务转奖励资金】
        if (t6502.F03.compareTo(new BigDecimal(configureProvider.getProperty(SystemVariable.TG_YXCZJS))) < 0) {
            return;
        }
        try (PreparedStatement ps = connection.prepareStatement("SELECT F01 FROM S61.T6119 WHERE F01 = ?")) {
            ps.setInt(1, exid);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (!resultSet.next()) {
                    return;
                }
            }
        }
        // 判断获取奖励次数是否超过上限(当月)
        int excount = 0;
        try (PreparedStatement ps = connection.prepareStatement("SELECT COUNT(F02) FROM S63.T6311 WHERE F06 >= ? AND F06 <= ? AND F02=? AND F05>0"))  {
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
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    excount = resultSet.getInt(1);
                }
            }
        }
        if (excount >= IntegerParser.parse(configureProvider.getProperty(SystemVariable.TG_YXTGSX))) {
            return;
        }
        // 计算奖励金额
        BigDecimal exjl = new BigDecimal(configureProvider.getProperty(SystemVariable.TG_YXTGJL));
        // 金额需大于0
        if (exjl.compareTo(BigDecimal.ZERO) > 0) {
            // 插入转账订单
            T6501 zzt6501 = new T6501();
            zzt6501.F02 = OrderType.TRANSFER.orderType();
            zzt6501.F03 = T6501_F03.DTJ;
            zzt6501.F07 = T6501_F07.XT;
            zzt6501.F08 = cT6101.F02;
            zzt6501.F10 = MchntTxnSsn.getMts(FuyouTypeEnum.TGYX.name());
            zzt6501.F04 = getCurrentTimestamp(connection);
            zzt6501.F13 = exjl;
            //保存此时的充值订单ID，托管在执行发放充值奖励时需要使用
            zzt6501.F12 = "charge" + t6502.F01;
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
    }
    
}
