package com.dimeng.p2p.escrow.fuyou.executor;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Map;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.config.Envionment;
import com.dimeng.framework.data.sql.SQLConnection;
import com.dimeng.framework.resource.AchieveVersion;
import com.dimeng.framework.resource.Resource;
import com.dimeng.framework.resource.ResourceAnnotation;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.entities.T6102;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S65.entities.T6501;
import com.dimeng.p2p.S65.entities.T6502;
import com.dimeng.p2p.S65.entities.T6505;
import com.dimeng.p2p.S65.entities.T6517;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.S65.enums.T6501_F07;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouRespCode;
import com.dimeng.p2p.escrow.fuyou.service.TransferManage;
import com.dimeng.p2p.escrow.fuyou.util.BackCodeInfo;
import com.dimeng.p2p.escrow.fuyou.util.PayUtil;
import com.dimeng.p2p.escrow.fuyou.variables.FuyouVariable;
import com.dimeng.p2p.order.TransferExecutor;
import com.dimeng.p2p.variables.defines.LetterVariable;
import com.dimeng.util.parser.IntegerParser;

/**
 * 
 * 转账垫付器
 * <功能详细描述>
 * 
 * @author  heshiping
 * @version  [版本号, 2016年1月22日]
 */
@AchieveVersion(version = 20160122)
@ResourceAnnotation
public class FYTransferExecutor extends TransferExecutor {
    
    public FYTransferExecutor(ResourceProvider resourceProvider) {
        super(resourceProvider);
    }
    
    @Override
    public Class<? extends Resource> getIdentifiedType() {
        return FYTransferExecutor.class;
    }
    
    @Override
    protected void doConfirm(SQLConnection connection, int orderId, Map<String, String> params) throws Throwable {
        T6501 t6501 = selectT6501(connection, orderId);
        T6517 t6517 = selectT6517(connection, orderId);
        if (t6517 == null) {
            throw new LogicalException("转账订单不存在!");
        }
        //推广用户账号
        int tgyh = t6517.F04;
        T6101 tgrzh = selectT6101(connection, tgyh, T6101_F03.WLZH, true);
        int pid = getPTID(connection);
        if (pid <= 0)  {
            throw new LogicalException("平台账号不存在!");
        }
        // 平台往来账户信息
        T6101 ptwlzh = selectT6101(connection, pid, T6101_F03.WLZH, true);
        if (ptwlzh == null) {
            throw new LogicalException("平台往来账户不存在!");
        }
        params.put("mchnt_txn_ssn", t6501.F10);
        params.put("relevance", t6501.F12);
        switch (t6517.F06) {
        	// 有效推广<推广首次充值>
            case FeeCode.TG_YX:
                handleCharge(connection, t6517, tgrzh, ptwlzh, params);
                break;
            // 持续推广 <推广投资奖励>
            case FeeCode.TG_CX:
                handleInvest(connection, t6517, tgrzh, ptwlzh, params);
                break;
            // 投资奖励 <奖励标>
            case FeeCode.TZ_TBJL:
                handleJLB(connection, t6517, tgrzh, ptwlzh, params);
                break;
            // 成交服务费、借款管理费
            case FeeCode.CJFWF:
                handleCJFWF(connection, t6517, params);
                break;
            // 投资红包
            case FeeCode.TZ_TBHB:
            	handleTZHB(connection, t6517, params);
            	break;
            // 理财管理费/投资管理费
            case FeeCode.GLF:
            	handleCJFWF(connection, t6517, params);
            	break;
            // 理财管理费/投资管理费
            case FeeCode.ZQZR_SXF:
            	handleCJFWF(connection, t6517, params);
            	break;
        }
    }
    
    /**
     * 成交服务费
     * <成交服务费因扣用户为防止用户提现，而无法收取，在放款已完成平台资金扣除，所现只需完成第三方资金转账>
     * @param connection
     * @param t6517
     * @param params
     * @throws Throwable
     */
    private void handleCJFWF(SQLConnection connection, T6517 t6517, Map<String, String> params) throws Throwable {
        // 转账接口
        ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        // 接口调用
        params.put("amt", PayUtil.getAmt(t6517.F02));
        params.put("in_cust_no", configureProvider.format(FuyouVariable.FUYOU_P2P_ACCOUNT_NAME));
        params.put("out_cust_no", getUserCustId(connection, t6517.F03));
        if (!Boolean.parseBoolean(params.get("flag"))) {
            doSubmitFyou(connection, configureProvider, params);
        }
    }
    
    /**
     * 奖励标
     * @param connection
     * @param t6517
     * @param tgrzh
     * @param ptwlzh
     * @param params
     * @throws Throwable
     */
    private void handleJLB(SQLConnection connection, T6517 t6517, T6101 tgrzh, T6101 ptwlzh, Map<String, String> params) throws Throwable {
        Timestamp t = new Timestamp(System.currentTimeMillis());
        {
            // 给用户帐户资金额 添加当前发放的奖励 ；用户帐户金额+ 奖励金额 ，
            tgrzh.F06 = tgrzh.F06.add(t6517.F02);
            updateT6101(connection, tgrzh.F06, tgrzh.F01);
            // 资金流水
            T6102 t6102 = new T6102();
            t6102.F02 = tgrzh.F01;
            t6102.F03 = FeeCode.TZ_TBJL;
            t6102.F04 = ptwlzh.F01;
            t6102.F05 = t;
            t6102.F06 = t6517.F02;
            t6102.F08 = tgrzh.F06;
            t6102.F09 = t6517.F05;
            insertT6102(connection, t6102);
        }
        {
            // 扣除平台帐户发放奖励的金额 ；平台总额 - 当前奖励金额
            ptwlzh.F06 = ptwlzh.F06.subtract(t6517.F02);
            updateT6101(connection, ptwlzh.F06, ptwlzh.F01);
            T6102 t6102 = new T6102();
            t6102.F02 = ptwlzh.F01;
            t6102.F03 = FeeCode.TZ_TBJL;
            t6102.F04 = tgrzh.F01;
            t6102.F05 = t;
            t6102.F07 = t6517.F02;
            t6102.F08 = ptwlzh.F06;
            t6102.F09 = t6517.F05;
            insertT6102(connection, t6102);
        }
        // 转账接口
        ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        // 接口调用
        params.put("amt", PayUtil.getAmt(t6517.F02));
        params.put("in_cust_no", getUserCustId(connection, t6517.F04));
        params.put("out_cust_no", configureProvider.format(FuyouVariable.FUYOU_P2P_ACCOUNT_NAME));
        if (!Boolean.parseBoolean(params.get("flag"))) {
            doSubmitFyou(connection, configureProvider, params);
        }
    }
    
    /**
     * 推广持续奖励
     * @param configureProvider
     * @param connection
     * @param orderIdStr
     * @param t6517
     * @param tgrzh
     * @param ptwlzh
     * @throws Throwable
     */
    private void handleInvest(SQLConnection connection, T6517 t6517, T6101 tgrzh, T6101 ptwlzh, Map<String, String> params) throws Throwable {
        int orderId = IntegerParser.parse(params.get("relevance").replace("invest", ""));
        T6505 t6505 = selectT6505(connection, orderId);
        if (t6505 == null) {
            throw new LogicalException("放款订单不存在！");
        }
        
        try (PreparedStatement pstmt = connection.prepareStatement("INSERT INTO S63.T6312 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = CURRENT_TIMESTAMP()")) {
            pstmt.setInt(1, t6517.F04);
            pstmt.setInt(2, t6505.F02);
            pstmt.setBigDecimal(3, t6505.F05);
            pstmt.setBigDecimal(4, t6517.F02);
            pstmt.execute();
        }
        
        try (PreparedStatement ps = connection.prepareStatement("UPDATE S63.T6310 SET F04 = F04 + ? WHERE F01 = ?")) {
            ps.setBigDecimal(1, t6517.F02);
            ps.setInt(2, t6517.F04);
            ps.executeUpdate();
        }
        Timestamp t = new Timestamp(System.currentTimeMillis());
        tgrzh.F06 = tgrzh.F06.add(t6517.F02);
        {
            updateT6101(connection, tgrzh.F06, tgrzh.F01);
            T6102 t6102 = new T6102();
            t6102.F02 = tgrzh.F01;
            t6102.F03 = FeeCode.TG_CX;
            t6102.F04 = ptwlzh.F01;
            t6102.F05 = t;
            t6102.F06 = t6517.F02;
            t6102.F08 = tgrzh.F06;
            t6102.F09 = String.format("持续推广奖励");
            insertT6102(connection, t6102);
        }
        ptwlzh.F06 = ptwlzh.F06.subtract(t6517.F02);
        {
            updateT6101(connection, ptwlzh.F06, ptwlzh.F01);
            T6102 t6102 = new T6102();
            t6102.F02 = ptwlzh.F01;
            t6102.F03 = FeeCode.TG_CX;
            t6102.F04 = tgrzh.F01;
            t6102.F05 = t;
            t6102.F07 = t6517.F02;
            t6102.F08 = ptwlzh.F06;
            t6102.F09 = String.format("持续推广奖励");
            insertT6102(connection, t6102);
        }
        ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        //发站内信
        Envionment envionment = configureProvider.createEnvionment();
        envionment.set("tz", String.valueOf(t6505.F05));
        envionment.set("jl", String.valueOf(t6517.F02));
        String content = configureProvider.format(LetterVariable.TG_CXJL, envionment);
        sendLetter(connection, t6517.F04, "推广持续奖励", content);
        
        // 接口调用
        params.put("amt", PayUtil.getAmt(t6517.F02));
        params.put("in_cust_no", getUserCustId(connection, t6517.F04));
        params.put("out_cust_no", configureProvider.format(FuyouVariable.FUYOU_P2P_ACCOUNT_NAME));
        if (!Boolean.parseBoolean(params.get("flag"))) {
            doSubmitFyou(connection, configureProvider, params);
        }
    }
    
    /**
     * 推广奖励
     * <首次充值达标推广奖励>
     * @param connection
     * @param orderId
     * @param params
     */
    private void handleCharge(SQLConnection connection, T6517 t6517, T6101 tgrzh, T6101 cT6101,  Map<String, String> params) throws Throwable {
        try {      
        	Timestamp t = new Timestamp(System.currentTimeMillis());
            int exid = t6517.F04;
            BigDecimal exjl = t6517.F02;
            int orderId = IntegerParser.parse(params.get("relevance").replace("charge", ""));
            T6502 t6502 = selectT6502(connection, orderId);
            if (t6502 == null) {
                throw new LogicalException("充值订单不存在！");
            }
            int accountId = t6502.F02;
            try (PreparedStatement ps = connection.prepareStatement("UPDATE S63.T6311 SET F05=? WHERE F03=?")) {
                ps.setBigDecimal(1, exjl);
                ps.setInt(2, accountId);
                ps.execute();
            }
            try (PreparedStatement ps = connection.prepareStatement("UPDATE S63.T6310 SET F05=F05+? WHERE F01=?")) {
                ps.setBigDecimal(1, exjl);
                ps.setInt(2, exid);
                ps.executeUpdate();
            }
            
            {//更新推广用户往来账户余额
                tgrzh.F06 = tgrzh.F06.add(exjl);
                updateT6101(connection, tgrzh.F06, tgrzh.F01);
                T6102 eT6102 = new T6102();
                eT6102.F02 = tgrzh.F01;
                eT6102.F03 = FeeCode.TG_YX;
                eT6102.F04 = cT6101.F01;
                eT6102.F05 = t;
                eT6102.F06 = exjl;
                eT6102.F08 = tgrzh.F06;
                eT6102.F09 = "有效推广奖励";
                //插入推广用户交易流水
                insertT6102(connection, eT6102);
            }
            
            {// 平台账户扣除奖励金额
                cT6101.F06 = cT6101.F06.subtract(exjl);
                updateT6101(connection, cT6101.F06, cT6101.F01);
                cT6101 = selectT6101(connection, cT6101.F02, T6101_F03.WLZH, true);
                T6102 ecT6102 = new T6102();
                ecT6102.F02 = cT6101.F01;
                ecT6102.F03 = FeeCode.TG_YX;
                ecT6102.F04 = tgrzh.F01;
                ecT6102.F05 = t;
                ecT6102.F07 = exjl;
                ecT6102.F08 = cT6101.F06;
                ecT6102.F09 = "有效推广奖励";
                //插入平台交易流水
                insertT6102(connection, ecT6102);
            }
            if (BigDecimal.ZERO.compareTo(exjl) >= 0) {
                return;
            }
            ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
            // 站内信
            Envionment envionment = configureProvider.createEnvionment();
            envionment.set("cz", t6502.F03.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            envionment.set("jl", exjl.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            String content = configureProvider.format(LetterVariable.TG_YXJL, envionment);
            sendLetter(connection, exid, "有效推广奖励", content);
            
            // 转账接口
            params.put("out_cust_no", configureProvider.format(FuyouVariable.FUYOU_P2P_ACCOUNT_NAME));
            params.put("in_cust_no", getUserCustId(connection, t6517.F04));
            params.put("amt", PayUtil.getAmt(exjl));
            if (!Boolean.parseBoolean(params.get("flag"))) {
                doSubmitFyou(connection, configureProvider, params);
            }
        } catch (Exception e) {
            logger.error(e, e);
            throw e;
        }        
    }
    
    /**
     * 投资红包
     * @param connection
     * @param t6517
     * @param params
     * @throws Throwable
     */
    private void handleTZHB(SQLConnection connection, T6517 t6517, Map<String, String> params) throws Throwable {
        // 转账接口
        ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        // 接口调用
        params.put("amt", PayUtil.getAmt(t6517.F02));
        params.put("in_cust_no", getUserCustId(connection, t6517.F04));
        params.put("out_cust_no", configureProvider.format(FuyouVariable.FUYOU_P2P_ACCOUNT_NAME));
        if (!Boolean.parseBoolean(params.get("flag"))) {
            doSubmitFyou(connection, configureProvider, params);
        }
    }
    
    /**
     * 调用富友接口
     * <转账>
     * @param connection
     * @param orderId
     * @throws IOException 
     * @throws SQLException 
     */
    private void doSubmitFyou(Connection connection, ConfigureProvider configureProvider, Map<String, String> params) throws IOException, SQLException {
        ServiceProvider serviceProvider = resourceProvider.getResource(ServiceProvider.class);
        try (ServiceSession serviceSession = serviceProvider.createServiceSession()) {
            logger.info("转账流水：" + params.get("mchnt_txn_ssn") + "金额:" + params.get("amt"));
            TransferManage manage = serviceSession.getService(TransferManage.class);
            Map<String, Object> map = manage.createTransferMap(params.get("mchnt_txn_ssn"), params.get("out_cust_no"),
                    params.get("in_cust_no"), params.get("amt"), "transferBmu");
            if (map != null && (FuyouRespCode.JYCG.getRespCode().equals(map.get("resp_code")))) {
                logger.info("转账成功");
            } else {
                logger.info("转账成功失败" + BackCodeInfo.info(map.get("resp_code").toString()));
                throw new LogicalException("转账成功失败" + BackCodeInfo.info(map.get("resp_code").toString()) + "!");
            }
        } catch (Throwable e) {
            logger.error("转账成功失败", e);
            throw new LogicalException("转账成功失败!");
        }
    }
    
    private void updateT6101(Connection connection, BigDecimal F01, int F02) throws SQLException {
        try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S61.T6101 SET F06 = ?, F07 = now()  WHERE F01 = ?")) {
            pstmt.setBigDecimal(1, F01);
            pstmt.setInt(2, F02);
            pstmt.execute();
        }
    }
    
    /**
     * 转账订单表信息
     * @param connection
     * @param F01
     * @return
     * @throws Throwable
     */
    private T6517 selectT6517(SQLConnection connection, int F01) throws Throwable {
        T6517 t6517 = null;
        try (PreparedStatement ps = connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06 FROM S65.T6517 WHERE T6517.F01 = ?")) {
            ps.setInt(1, F01);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    t6517 = new T6517();
                    t6517.F01 = resultSet.getInt(1);
                    t6517.F02 = resultSet.getBigDecimal(2);
                    t6517.F03 = resultSet.getInt(3);
                    t6517.F04 = resultSet.getInt(4);
                    t6517.F05 = resultSet.getString(5);
                    t6517.F06 = resultSet.getInt(6);
                }
            }
        }
        return t6517;
    }
    
    /**
     * 充值订单信息
     * @param connection
     * @param orderId
     * @return
     * @throws Throwable
     */
    private T6502 selectT6502(SQLConnection connection, int orderId) throws Throwable {        
        T6502 record = null;
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT F01, F02, F03, F04, F05,F06, F07, F08 FROM S65.T6502 WHERE T6502.F01 = ? LIMIT 1")) {
            pstmt.setInt(1, orderId);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
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
    
    private T6505 selectT6505(SQLConnection connection, int orderId) throws Throwable {
        T6505 record = null;
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT F01, F02, F03, F04, F05 FROM S65.T6505 WHERE T6505.F01 = ? LIMIT 1")) {
            pstmt.setInt(1, orderId);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
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
    
    /**
     * 查询订单信息
     * @param connection
     * @param F01
     * @return
     * @throws SQLException
     */
    @Override
    protected T6501 selectT6501(Connection connection, int F01) throws Throwable {
        T6501 record = null;
        try {
            try (PreparedStatement pstmt = connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F12 FROM S65.T6501 WHERE T6501.F01 = ? ")) {
                pstmt.setInt(1, F01);
                try (ResultSet resultSet = pstmt.executeQuery()) {
                    if (resultSet.next()) {
                        record = new T6501();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = T6501_F03.parse(resultSet.getString(3));
                        record.F04 = resultSet.getTimestamp(4);
                        record.F05 = resultSet.getTimestamp(5);
                        record.F06 = resultSet.getTimestamp(6);
                        record.F07 = T6501_F07.parse(resultSet.getString(7));
                        record.F08 = resultSet.getInt(8);
                        record.F09 = resultSet.getInt(9);
                        record.F10 = resultSet.getString(10);
                        record.F12 = resultSet.getString(11);
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e, e);
            throw e;
        }
        return record;
    }
    
}
