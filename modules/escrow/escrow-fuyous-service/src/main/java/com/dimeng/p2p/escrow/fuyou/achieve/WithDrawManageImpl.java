package com.dimeng.p2p.escrow.fuyou.achieve;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.resource.ResourceNotFoundException;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.OrderType;
import com.dimeng.p2p.P2PConst;
import com.dimeng.p2p.PaymentInstitution;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.entities.T6102;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.entities.T6114;
import com.dimeng.p2p.S61.entities.T6130;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.S61.enums.T6110_F08;
import com.dimeng.p2p.S61.enums.T6110_F10;
import com.dimeng.p2p.S61.enums.T6130_F09;
import com.dimeng.p2p.S62.enums.T6252_F09;
import com.dimeng.p2p.S65.entities.T6501;
import com.dimeng.p2p.S65.entities.T6503;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.S65.enums.T6501_F07;
import com.dimeng.p2p.S65.enums.T6501_F11;
import com.dimeng.p2p.escrow.fuyou.cond.ChargeCond;
import com.dimeng.p2p.escrow.fuyou.cond.TxdzCond;
import com.dimeng.p2p.escrow.fuyou.entity.console.TxdzEntity;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouTypeEnum;
import com.dimeng.p2p.escrow.fuyou.service.WithDrawManage;
import com.dimeng.p2p.escrow.fuyou.util.MchntTxnSsn;
import com.dimeng.p2p.escrow.fuyou.util.PoundageUtil;
import com.dimeng.p2p.escrow.fuyou.variables.FuyouVariable;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BooleanParser;

/**
 * 
 * 提现业务管理实现类
 * <功能详细描述>
 * 
 * @author  heshiping
 * @version  [版本号, 2015年11月30日]
 */
public class WithDrawManageImpl extends AbstractEscrowService implements WithDrawManage
{
    
    public WithDrawManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public String selectT6119()
        throws Throwable
    {
        return getAccountId(serviceResource.getSession().getAccountId());
    }
    
    @Override
    public String addOrderId(BigDecimal funds, int cardId, T6101_F03 f03)
        throws Throwable
    {
        // 最低提现金额
        BigDecimal min = new BigDecimal(configureProvider.getProperty(SystemVariable.WITHDRAW_MIN_FUNDS));
        // 最高提现金额
        BigDecimal max = new BigDecimal(configureProvider.getProperty(SystemVariable.WITHDRAW_MAX_FUNDS));
        BigDecimal zero = new BigDecimal(0);
        if (funds.compareTo(min) < 0 || funds.compareTo(max) > 0 || funds.compareTo(zero) <= 0)
        {
            throw new LogicalException("提现金额不能低于" + min + "元，不能高于" + max + "元!");
        }
        int accountId = serviceResource.getSession().getAccountId();
        try (Connection connection = getConnection())
        {
            //校验用户是否被拉入黑名单
            T6110 userInfo = getUserInfo(connection, accountId);
            if (T6110_F07.HMD == userInfo.F07)
            {
                throw new LogicalException("您的账户已被拉入黑名单，操作有所限制。如有疑问，请联系客服！");
            }
            
            // 查询是否有逾期未还的款
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT F01 FROM S62.T6252 WHERE DATEDIFF(NOW(),F08)>0 AND F09=? AND F03=?"))
            {
                ps.setString(1, T6252_F09.WH.name());
                ps.setInt(2, accountId);
                try (ResultSet resultSet = ps.executeQuery())
                {
                    if (resultSet.next())
                    {
                        throw new LogicalException("您有逾期未还的借款，请先还完再操作!");
                    }
                }
            }
            // 查询用户银行卡是否存在，并且是本人的
            String cardNumber = null;
            try (PreparedStatement ps = connection.prepareStatement("SELECT F07 FROM S61.T6114 WHERE F01=? AND F02=?"))
            {
                ps.setInt(1, cardId);
                ps.setInt(2, accountId);
                try (ResultSet resultSet = ps.executeQuery())
                {
                    if (resultSet.next())
                    {
                        cardNumber = resultSet.getString(1);
                    }
                }
            }
            if (StringHelper.isEmpty(cardNumber))
            {
                throw new LogicalException("银行卡不存在!");
            }
            // 计算提现手续费
            BigDecimal poundage = PoundageUtil.getWithDrawPoundage(funds, configureProvider);
            // 提现应付金额
            BigDecimal amount = funds;
            boolean txsxf_kcfs = BooleanParser.parse(configureProvider.format(SystemVariable.TXSXF_KCFS));
            if (!txsxf_kcfs)
            {
                amount = amount.add(poundage);
            }
            
            String mchnt_txn_ssn = MchntTxnSsn.getMts(FuyouTypeEnum.YHTX.name());
            try
            {
                // 开启事务
                serviceResource.openTransactions(connection);
                // 用户的往来账户
                T6101 wlzh = this.selectT6101(connection, accountId, T6101_F03.WLZH, false);
                if (amount.compareTo(wlzh.F06) > 0)
                {
                    throw new LogicalException("账户余额不足!");
                }
                // 用户的锁定账户
                T6101 sdzh = selectT6101(connection, accountId, T6101_F03.SDZH, false);
                if (sdzh == null)
                {
                    throw new LogicalException("锁定账号不存在!");
                }
                Timestamp times = getCurrentTimestamp(connection);
                T6501 t6501 = new T6501();
                t6501.F02 = OrderType.WITHDRAW.orderType();
                t6501.F03 = T6501_F03.DTJ;
                t6501.F04 = times;
                t6501.F05 = times;
                t6501.F07 = T6501_F07.YH;
                t6501.F08 = accountId;
                t6501.F09 = serviceResource.getSession().getAccountId();
                t6501.F10 = mchnt_txn_ssn;
                
                T6503 t6503 = new T6503();
                t6503.F02 = accountId;
                if ("ON".equals(configureProvider.format(FuyouVariable.FUYOU_WITHDRAW_ONOFF)))
                {
                    t6503.F03 = funds.subtract(poundage);
                    if (BigDecimal.ZERO.compareTo(t6503.F03) >= 0)
                    {
                        throw new LogicalException("提现金额不能小于扣款金额!");
                    }
                }
                else
                {
                    t6503.F03 = funds;
                }
                t6501.F13 = t6503.F03;
                // 插入订单
                int orderId = insertT6501(connection, t6501);
                t6503.F01 = orderId;
                t6503.F04 = poundage;
                t6503.F05 =
                    "ON".equals(configureProvider.format(FuyouVariable.FUYOU_WITHDRAW_ONOFF)) ? poundage
                        : BigDecimal.ZERO;
                t6503.F06 = StringHelper.decode(cardNumber);
                t6503.F07 = PaymentInstitution.FUYOU.getInstitutionCode();
                t6503.F09 = 0;
                // 下提现订单表
                insertT6503(connection, t6503);
                // 提交事务
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
            return mchnt_txn_ssn;
        }
    }
    
    protected void insertT6503(Connection connection, T6503 entity)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S65.T6503 SET F01 = ?, F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ? , F09 = ? "))
        {
            pstmt.setInt(1, entity.F01);
            pstmt.setInt(2, entity.F02);
            pstmt.setBigDecimal(3, entity.F03);
            pstmt.setBigDecimal(4, entity.F04);
            pstmt.setBigDecimal(5, entity.F05);
            pstmt.setString(6, entity.F06);
            pstmt.setInt(7, entity.F07);
            pstmt.setInt(8, entity.F09);
            pstmt.execute();
        }
    }
    
    @Override
    public Map<String, String> createWithdrawUrI(String loginId, BigDecimal funds, String mchntTxnSsn)
        throws Throwable
    {
        Map<String, String> params = new HashMap<String, String>();
        params.put("mchnt_cd", trimBlank(configureProvider.format(FuyouVariable.FUYOU_ACCOUNT_ID)));
        params.put("mchnt_txn_ssn", mchntTxnSsn);
        params.put("login_id", loginId);
        params.put("amt", getAmt(funds));
        params.put("page_notify_url", configureProvider.format(FuyouVariable.FUYOU_WITHDRAWRET_URL));
        params.put("back_notify_url", configureProvider.format(FuyouVariable.FUYOU_WITHDRANOTICE_URL));
        // 数据拼接
        String dataStr = getSignature(params);
        logger.info("提现信息：" + dataStr);
        String signature = encryptByRSA(dataStr);
        logger.info("签名结果：" + signature);
        params.put("signature", signature);
        return params;
    }
    
    @Override
    public Map<String, String> createWithdrawUrI(BigDecimal funds, ChargeCond cond)
        throws Throwable
    {
        Map<String, String> params = new HashMap<String, String>();
        params.put("mchnt_cd", cond.mchntCd());
        params.put("mchnt_txn_ssn", cond.mchntTxnSsn());
        params.put("login_id", cond.loginId());
        params.put("amt", getAmt(funds));
        params.put("page_notify_url", cond.pageNotifyUrl());
        params.put("back_notify_url", cond.backNotifyUrl());
        // 数据拼接
        String dataStr = getSignature(params);
        logger.info("提现信息：" + dataStr);
        String signature = encryptByRSA(dataStr);
        logger.info("签名结果：" + signature);
        params.put("signature", signature);
        return params;
    }
    
    @Override
    public boolean withdrawReturnDecoder(Map<String, String> params)
        throws Throwable
    {
        Map<String, String> paramMap = null;
        
        if (params != null)
        {
            paramMap = new HashMap<String, String>();
            //交易金额
            paramMap.put("amt", params.get("amt"));
            //交易用户
            paramMap.put("login_id", params.get("login_id"));
            //商户代码
            paramMap.put("mchnt_cd", params.get("mchnt_cd"));
            //请求流水号
            paramMap.put("mchnt_txn_ssn", params.get("mchnt_txn_ssn"));
            //响应码
            paramMap.put("resp_code", params.get("resp_code"));
            //签名数据
            paramMap.put("signature", params.get("signature"));
        }
        
        return getReturnParams(paramMap);
    }
    
    @Override
    public int updateOrder(Map<String, String> params)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            String mchnt_txn_ssn = params.get("mchnt_txn_ssn");
            int orderId = selectT6501(mchnt_txn_ssn);
            if (orderId == 0)
            {
                logger.info("流水号=" + mchnt_txn_ssn);
                throw new LogicalException("查询异常!");
            }
            
            try
            {
                // 开启事务
                serviceResource.openTransactions(connection);
                // 根据流水号成的订单号查询订单并锁住
                try (PreparedStatement ps =
                    connection.prepareStatement("SELECT F01 FROM S65.T6501 WHERE F01 = ? FOR UPDATE"))
                {
                    ps.setInt(1, orderId);
                    try (ResultSet resultSet = ps.executeQuery())
                    {
                        if (resultSet.next())
                        {
                            orderId = resultSet.getInt(1);
                        }
                        else
                        {
                            throw new LogicalException("查询提现订单异常!");
                        }
                    }
                }
                
                // 根据订单ID查询提现提单
                T6503 t6503 = selectT6503(connection, orderId);
                // 查询银行卡信息
                T6114 t6114 = selectT6114(connection, t6503.F02, StringHelper.encode(t6503.F06));
                int adminId = selectT7110(connection);
                T6130 t6130 = new T6130();
                t6130.F02 = t6503.F02;
                t6130.F03 = t6114.F01;
                t6130.F04 = t6503.F03;
                t6130.F06 = t6503.F04;
                t6130.F07 = t6503.F04;
                t6130.F09 = T6130_F09.YFK;
                // 插入提现申请
                int aId = insertT6130(connection, t6130, adminId);
                // 更新T6501，将待提交改为待确认
                try (PreparedStatement pstmt =
                    connection.prepareStatement("UPDATE S65.T6501 SET F03 = ?, F06 = CURRENT_TIMESTAMP(), F10 = ? WHERE F01=? "))
                {
                    pstmt.setString(1, T6501_F03.DQR.name());
                    pstmt.setString(2, mchnt_txn_ssn);
                    pstmt.setInt(3, orderId);
                    pstmt.execute();
                }
                
                // 更新T6503,更新流水号和提现申请记录
                try (PreparedStatement pstmts =
                    connection.prepareStatement("UPDATE S65.T6503 SET T6503.F08 = ?,T6503.F09= ? WHERE T6503.F01=?"))
                {
                    // 实收手续费《因富友新接口暂款返回 只能用应收为准》
                    pstmts.setString(1, mchnt_txn_ssn);
                    pstmts.setInt(2, aId);
                    pstmts.setInt(3, orderId);
                    pstmts.execute();
                }
                
                // 计算总金额
                BigDecimal amt = t6503.F03.add(t6503.F04);
                // 查询用户的往来账户和锁定账户（ 将金额从往来账户中转入锁定账户)
                T6101 wT6101 = selectT6101(connection, t6503.F02, T6101_F03.WLZH, true);
                if (wT6101 == null)
                {
                    throw new LogicalException("用户往来账户不存在!");
                }
                T6101 sT6101 = selectT6101(connection, t6503.F02, T6101_F03.SDZH, true);
                if (sT6101 == null)
                {
                    throw new LogicalException("用户锁定账户不存在!");
                }
                {
                    // 往来账户的余额为 ： 以前的余额减去提现的金额
                    wT6101.F06 = wT6101.F06.subtract(t6503.F03);
                    T6102 t6102 = new T6102();
                    t6102.F02 = wT6101.F01;
                    t6102.F03 = FeeCode.TX;
                    t6102.F04 = sT6101.F01;
                    t6102.F07 = t6503.F03;
                    t6102.F08 = wT6101.F06;
                    t6102.F09 = "提现";
                    // 插入提现金额的流水记录（往来账户）
                    insertT6102(connection, t6102);
                    // 如果提现包含 手续费，则插入提现手续费的流水记录（往来账户）
                    if (t6503.F05.compareTo(new BigDecimal(0)) > 0)
                    {
                        // 往来账户的余额为 ：以前的余额再减去手续费
                        wT6101.F06 = wT6101.F06.subtract(t6503.F05);
                        T6102 tt6102 = new T6102();
                        tt6102.F02 = wT6101.F01;
                        tt6102.F03 = FeeCode.TX_SXF;
                        tt6102.F04 = sT6101.F01;
                        tt6102.F07 = t6503.F05;
                        tt6102.F08 = wT6101.F06;
                        tt6102.F09 = "提现手续费";
                        insertT6102(connection, tt6102);
                    }
                    // 更新用户的往来账户金额
                    updateT6101(connection, wT6101.F06, wT6101.F01);
                }
                {
                    // 锁定账户的余额为：以前的余额加上提现金额
                    sT6101.F06 = sT6101.F06.add(t6503.F03);
                    T6102 t6102 = new T6102();
                    t6102.F02 = sT6101.F01;
                    t6102.F03 = FeeCode.TX;
                    t6102.F04 = wT6101.F01;
                    t6102.F06 = t6503.F03;
                    t6102.F08 = sT6101.F06;
                    t6102.F09 = "提现";
                    // 插入提现金额的流水记录（锁定账户)
                    insertT6102(connection, t6102);
                    // 如果提现包含 手续费，则插入提现手续费的流水记录（锁定账户）
                    if (t6503.F05.compareTo(new BigDecimal(0)) > 0)
                    {
                        // 锁定账户的余额为以前的余额再加上手续费
                        sT6101.F06 = sT6101.F06.add(t6503.F05);
                        T6102 tt6102 = new T6102();
                        tt6102.F02 = sT6101.F01;
                        tt6102.F03 = FeeCode.TX_SXF;
                        tt6102.F04 = wT6101.F01;
                        tt6102.F06 = t6503.F05;
                        tt6102.F08 = sT6101.F06;
                        tt6102.F09 = "提现手续费";
                        insertT6102(connection, tt6102);
                    }
                    // 更新用户的锁定账户金额
                    updateT6101(connection, sT6101.F06, sT6101.F01);
                }
                Timestamp times = getCurrentTimestamp(connection);
                // ****生成冻结订单
                T6501 t6501 = new T6501();
                t6501.F02 = OrderType.FREEZE.orderType();
                t6501.F03 = T6501_F03.CG;
                t6501.F04 = times;
                t6501.F05 = times;
                t6501.F07 = T6501_F07.YH;
                t6501.F10 = MchntTxnSsn.getMts(FuyouTypeEnum.TXTZ.name());
                t6501.F13 = amt;
                // 插入订单，类型为冻结订单类型
                t6501.F01 = insertT6501(connection, t6501);
                // 插入冻结订单
                insertT6515(connection, t6501.F01, t6503.F01, amt);
                // 提交事务
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
            return orderId;
        }
    }
    
    /**
     * 查询订单
     * <功能详细描述>
     * @param mchnt_txn_ssn
     * @return
     * @throws Throwable
     */
    private int selectT6501(String mchnt_txn_ssn)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt = connection.prepareStatement("SELECT F01 FROM S65.T6501 WHERE T6501.F10 = ?"))
            {
                pstmt.setString(1, mchnt_txn_ssn);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return resultSet.getInt(1);
                    }
                }
            }
            
        }
        return 0;
    }
    
    /**
     * 描述:插入冻结订单
     *  作者:wangshaohua 
     *  创建时间：2015年4月29日
     * 
     * @param connection
     * @param F01
     * @param F02
     * @param F03
     * @throws SQLException
     */
    private void insertT6515(Connection connection, int F01, int F02, BigDecimal F03)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S65.T6515 SET F01 = ?, F02 = ?, F03 = ?"))
        {
            pstmt.setInt(1, F01);
            pstmt.setInt(2, F02);
            pstmt.setBigDecimal(3, F03);
            pstmt.execute();
        }
    }
    
    /**
     * 描述:更新用户资金账户 作者:wangshaohua 创建时间：2015年4月29日
     * 
     * @param connection
     * @param F01
     * @param F02
     * @throws SQLException
     */
    private void updateT6101(Connection connection, BigDecimal F01, int F02)
        throws SQLException
    {
        try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S61.T6101 SET F06 = ? WHERE F01 = ?"))
        {
            pstmt.setBigDecimal(1, F01);
            pstmt.setInt(2, F02);
            pstmt.execute();
        }
    }
    
    /**
     * 描述:插入提现申请T6130 作者:wangshaohua 创建时间：2015年4月29日
     * 
     * @param connection
     * @param entity
     * @return
     * @throws SQLException
     */
    private int insertT6130(Connection connection, T6130 entity, int accountId)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S61.T6130 SET F02 = ?, F03 = ?, F04 = ?, F06 = ?, F07 = ?, F08 = CURRENT_TIMESTAMP(), F09 = ?, F13=?, F14 = CURRENT_TIMESTAMP(),F17 = ?,F18 = ? ",
                PreparedStatement.RETURN_GENERATED_KEYS))
        {
            pstmt.setInt(1, entity.F02);
            pstmt.setInt(2, entity.F03);
            pstmt.setBigDecimal(3, entity.F04);
            pstmt.setBigDecimal(4, entity.F06);
            pstmt.setBigDecimal(5, entity.F07);
            pstmt.setString(6, entity.F09.name());
            pstmt.setInt(7, accountId);
            Map<String, Object> result = getEmpInfo(accountId, connection);
            pstmt.setString(8, (String)result.get("empNum"));
            pstmt.setString(9, (String)result.get("empStatus"));
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
    
    /**
     * 描述:查询管理员ID 
     * 作者:wangshaohua 
     * 创建时间：2015年4月29日
     *  此处 admin 用户名不能承意更改，如改此方法必段得改
     * @param connection
     * @return
     * @throws Throwable
     */
    private int selectT7110(Connection connection)
        throws Throwable
    {
        int adminId = 1000;
        try (PreparedStatement ps = connection.prepareStatement("SELECT T7110.F01 FROM S71.T7110 LIMIT 1"))
        {
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    adminId = rs.getInt(1);
                }
            }
        }
        return adminId;
    }
    
    /**
     * 描述:查询银行卡信息 作者:wangshaohua 创建时间：2015年4月29日
     * 
     * @param connection
     * @param F02
     * @param F07
     * @return
     * @throws SQLException
     */
    private T6114 selectT6114(Connection connection, int F02, String F07)
        throws SQLException
    {
        T6114 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07 FROM S61.T6114 WHERE T6114.F02 = ? AND T6114.F07 = ? LIMIT 1"))
        {
            pstmt.setInt(1, F02);
            pstmt.setString(2, F07);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6114();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getInt(3);
                    record.F04 = resultSet.getInt(4);
                    record.F05 = resultSet.getString(5);
                    record.F06 = resultSet.getString(6);
                    record.F07 = resultSet.getString(7);
                }
            }
        }
        return record;
    }
    
    /**
     * 描述:查询提现订单 
     * 作者:wangshaohua 
     * 创建时间：2015年4月29日
     * 
     * @param connection
     * @param F01
     * @return
     * @throws SQLException
     */
    @Override
    public T6503 selectT6503(Connection connection2, int F01)
        throws SQLException
    {
        T6503 record = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09 FROM S65.T6503 WHERE T6503.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, F01);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new T6503();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getBigDecimal(3);
                        record.F04 = resultSet.getBigDecimal(4);
                        record.F05 = resultSet.getBigDecimal(5);
                        record.F06 = resultSet.getString(6);
                        record.F07 = resultSet.getInt(7);
                        record.F08 = resultSet.getString(8);
                        record.F09 = resultSet.getInt(9);
                    }
                }
            }
            return record;
        }
    }
    
    @Override
    public PagingResult<TxdzEntity> search(TxdzCond query, Paging paging)
        throws Throwable
    {
        try (Connection conn = getConnection())
        {
            StringBuilder sql =
                new StringBuilder(
                    "SELECT T6501.F01 AS F01,T6501.F03 AS F02,T6501.F04 F03,T6501.F05 AS F04,T6501.F06 AS F05,T6501.F10 AS F06,T6503.F03 AS F07,T6503.F08 AS F08,T6110.F02 AS F09 "
                        + ", T6501.F11 AS F10, T6501.F12 AS F11 FROM S65.T6501 INNER JOIN S65.T6503 ON T6501.F01=T6503.F01 INNER JOIN S61.T6110 ON T6503.F02=T6110.F01 "
                        + "WHERE 1 = 1 "
                        + "AND T6501.F03 IN('"
                        + T6501_F03.DQR
                        + "','"
                        + T6501_F03.DTJ
                        + "','"
                        + T6501_F03.SB
                        + "')"
                        + " AND (T6501.F02 = '"
                        + OrderType.WITHDRAW.orderType()
                        + "' OR T6501.F02 = '" + OrderType.PLATFORM_WITHDRAW.orderType() + "')");
            ArrayList<Object> parameters = new ArrayList<>();
            if (query != null)
            {
                String account = query.userName();
                if (!StringHelper.isEmpty(account))
                {
                    sql.append(" AND T6110.F02 LIKE ?");
                    parameters.add(getSQLConnectionProvider().allMatch(account));
                }
                String orderId = query.f01();
                if (!StringHelper.isEmpty(orderId))
                {
                    sql.append(" AND T6501.F01 LIKE ?");
                    parameters.add(getSQLConnectionProvider().allMatch(orderId));
                }
                String loanNo = query.f10();
                if (!StringHelper.isEmpty(loanNo))
                {
                    sql.append(" AND T6501.F10 LIKE ?");
                    parameters.add(getSQLConnectionProvider().allMatch(loanNo));
                }
                Timestamp datetime = query.getStartExpireDatetime();
                if (datetime != null)
                {
                    sql.append(" AND DATE(T6501.F04) >=?");
                    parameters.add(datetime);
                }
                datetime = query.getEndExpireDatetime();
                if (datetime != null)
                {
                    sql.append(" AND DATE(T6501.F04) <= ?");
                    parameters.add(datetime);
                }
                String state = query.state();
                if (!StringHelper.isEmpty(state))
                {
                    sql.append(" AND T6501.F03 = '" + state + "'");
                }
            }
            sql.append(" ORDER BY T6501.F01 DESC");
            return selectPaging(conn, new ArrayParser<TxdzEntity>()
            {
                
                @Override
                public TxdzEntity[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    List<TxdzEntity> lists = new ArrayList<>();
                    while (resultSet.next())
                    {
                        TxdzEntity record = new TxdzEntity();
                        record.F01 = resultSet.getInt(1);
                        record.F03 = T6501_F03.parse(resultSet.getString(2));
                        record.F04 = resultSet.getTimestamp(3);
                        record.F05 = resultSet.getTimestamp(4);
                        record.F06 = resultSet.getTimestamp(5);
                        record.F10 = resultSet.getString(6) != null ? resultSet.getString(6) : "";
                        record.amount = resultSet.getBigDecimal(7);
                        record.userName = resultSet.getString(9);
                        record.F11 = T6501_F11.parse(resultSet.getString(10));
                        record.F12 = resultSet.getString(11) != null ? resultSet.getString(11) : "";
                        lists.add(record);
                    }
                    return lists.toArray(new TxdzEntity[lists.size()]);
                }
            }, paging, sql.toString(), parameters);
        }
    }
    
    @Override
    protected SQLConnectionProvider getSQLConnectionProvider()
        throws ResourceNotFoundException, SQLException
    {
        return serviceResource.getDataConnectionProvider(SQLConnectionProvider.class, P2PConst.DB_MASTER_PROVIDER);
    }
    
    private T6110 getUserInfo(Connection connection, int userid)
        throws Exception
    {
        T6110 record = null;
        try
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F02, F03, F04, F05, F06, F07, F08, F09, F10 FROM S61.T6110 WHERE T6110.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, userid);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new T6110();
                        record.F02 = resultSet.getString(1);
                        record.F03 = resultSet.getString(2);
                        record.F04 = resultSet.getString(3);
                        record.F05 = resultSet.getString(4);
                        record.F06 = T6110_F06.parse(resultSet.getString(5));
                        record.F07 = T6110_F07.parse(resultSet.getString(6));
                        record.F08 = T6110_F08.parse(resultSet.getString(7));
                        record.F09 = resultSet.getTimestamp(8);
                        record.F10 = T6110_F10.parse(resultSet.getString(9));
                    }
                }
            }
        }
        catch (Exception e)
        {
            logger.error(e, e);
            throw e;
        }
        return record;
    }
    

    
    
    @Override
    public Map<String, String> createWithdrawFXUrl(String loginId, String mchntTxnSsn, BigDecimal TXFS)
        throws Throwable
    {
        
        Map<String, String> params = new HashMap<String, String>();
        params.put("mchnt_cd", trimBlank(configureProvider.format(FuyouVariable.FUYOU_ACCOUNT_ID)));
        params.put("mchnt_txn_ssn", mchntTxnSsn);
        params.put("login_id", loginId);
        params.put("cash_way", String.valueOf(TXFS));
        // params.put("busi_tp", "PV15");//提现：PV15   委托提现：PV65 非必填
        // 数据拼接
        String dataStr = getSignature(params);
        logger.info("设置用户提现模式请求参数信息：" + dataStr);
        String signature = encryptByRSA(dataStr);
        logger.info("设置用户提现模式签名结果：" + signature);
        params.put("signature", signature);
        return params;
        
    }

    @Override
    public BigDecimal getWithDrawPoundage(BigDecimal funds, BigDecimal TXFS)
        throws Throwable
    {
        // TODO Auto-generated method stub
        return null;
    }

}
