package com.dimeng.p2p.escrow.fuyou.achieve;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.OrderType;
import com.dimeng.p2p.PaymentInstitution;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.entities.T6102;
import com.dimeng.p2p.S61.entities.T6114;
import com.dimeng.p2p.S61.entities.T6130;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S61.enums.T6130_F09;
import com.dimeng.p2p.S65.entities.T6501;
import com.dimeng.p2p.S65.entities.T6502;
import com.dimeng.p2p.S65.entities.T6503;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.S65.enums.T6501_F07;
import com.dimeng.p2p.S65.enums.T6501_F11;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouTypeEnum;
import com.dimeng.p2p.escrow.fuyou.service.InformManage;
import com.dimeng.p2p.escrow.fuyou.util.MchntTxnSsn;
import com.dimeng.util.StringHelper;

/**
 * 
 * 富友通知业务处理类
 * <功能详细描述>
 * 
 * @author  heshiping
 * @version  [版本号, 2015年12月1日]
 */
public class InformManageImpl extends AbstractEscrowService implements InformManage
{
    
    public InformManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public boolean informReturnDecoder(Map<String, String> params)
        throws Throwable
    {
        Map<String, String> paramMap = null;
        
        if (params != null)
        {
            paramMap = new HashMap<String, String>();
            //充值金额
            paramMap.put("amt", params.get("amt"));
            //商户代码
            paramMap.put("mchnt_cd", params.get("mchnt_cd"));
            //交易日期
            paramMap.put("mchnt_txn_dt", params.get("mchnt_txn_dt"));
            //请求流水号
            paramMap.put("mchnt_txn_ssn", params.get("mchnt_txn_ssn"));
            //手机号码
            paramMap.put("mobile_no", params.get("mobile_no"));
            //备注
            paramMap.put("remark", params.get("remark"));
            //签名数据
            paramMap.put("signature", params.get("signature"));
        }
        
        return getReturnParams(paramMap);
    }
    
    @Override
    public String encryptByRSA(Map<String, String> params)
        throws Throwable
    {
        return plainRSA(params);
    }
    
    @Override
    public Map<String, String> selectT6501(String mchnt_txn_ssn, boolean flag)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01,F03 FROM S65.T6501 WHERE T6501.F10 = ?"))
            {
                pstmt.setString(1, mchnt_txn_ssn);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("orderId", String.valueOf(resultSet.getInt(1)));
                        params.put("state", resultSet.getString(2));
                        if (flag && params.get("state").equals(T6501_F03.SB.name()))
                        {
                            try (PreparedStatement pstmts =
                                connection.prepareStatement("UPDATE S65.T6501 SET F03 = ? WHERE F10 = ?"))
                            {
                                pstmts.setString(1, T6501_F03.DTJ.name());
                                pstmts.setString(2, mchnt_txn_ssn);
                                pstmts.execute();
                            }
                        }
                        return params;
                    }
                }
            }
            
        }
        return null;
    }
    
    @Override
    public void addOrder(Map<String, String> params)
        throws Throwable
    {
        String mobile_no = params.get("mobile_no");
        String mchnt_txn_ssn = params.get("mchnt_txn_ssn");
        String amt = params.get("amt");
        BigDecimal amount = formatAmtRet(amt);
        // 数据连接
        try (Connection connection = getConnection())
        {
            try
            {
                int userId = selectT6119(connection, mobile_no);
                if (userId == 0)
                {
                    logger.info("POSS机充值用户ID不存在,手机号：" + mobile_no);
                    params.put("NO", "NO");
                    return;
                }
                // 开启事务
                serviceResource.openTransactions(connection);
                int ptId = getPTID(connection);
                BigDecimal poundage = BigDecimal.ZERO;
                Timestamp times = getCurrentTimestamp(connection);
                // 订单表
                T6501 t6501 = new T6501();
                // 类型编码
                t6501.F02 = OrderType.CHARGE.orderType();
                // 状态,DTJ:待提交;YTJ:已提交;DQR:待确认;CG:成功;SB:失败;
                t6501.F03 = T6501_F03.DTJ;
                t6501.F04 = times;
                t6501.F05 = times;
                // 订单来源,XT:系统;YH:用户;HT:后台;PS：poss机充值
                t6501.F07 = T6501_F07.PS;
                // 用户ID
                t6501.F08 = userId;
                // 平台ID
                t6501.F09 = ptId;
                // 流水号
                t6501.F10 = mchnt_txn_ssn;
                t6501.F12 = "poss机充值/委托充值";
                t6501.F13 = amount;
                int oId = 0;
                // 查询资金账户信息
                T6101 t6101 = selectT6101(connection, ptId, T6101_F03.WLZH, false);
                if (t6101 == null)
                {
                    throw new LogicalException("用户往来账户不存在!");
                }
                // 插入订单信息
                oId = insertT6501(connection, t6501);
                params.put("orderId", String.valueOf(oId));
                if (oId <= 0)
                {
                    throw new LogicalException("数据库异常!");
                }
                // 充值订单表
                T6502 t6502 = new T6502();
                t6502.F01 = oId;
                t6502.F02 = userId;
                // 充值金额
                t6502.F03 = amount;
                // 应收手续费
                t6502.F04 = poundage;
                // 实收手续费
                t6502.F05 = poundage;
                // 支付公司代号
                t6502.F07 = PaymentInstitution.FUYOU.getInstitutionCode();
                // 流水号 充值成功后增加此记录字段
                // t6502.F08 = serialNumber;
                // 插入充值订单信息
                insertT6502(connection, t6502);
                // 提交事务
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
    public void addOrderEntrust(Map<String, String> params)
        throws Throwable
    {
        String mobile_no = params.get("mobile_no");
        String mchnt_txn_ssn = params.get("mchnt_txn_ssn");
        String amt = params.get("amt");
        BigDecimal amount = formatAmtRet(amt);
        T6503 t6503 = null;
        // 数据连接
        try (Connection connection = getConnection())
        {
            // 查询用户银行卡是否存在，并且是本人的
            String cardNumber = null;
            int accountId = selectT6119(connection, mobile_no);
            try (PreparedStatement ps = connection.prepareStatement("SELECT F07 FROM S61.T6114 WHERE F02=?"))
            {
                ps.setInt(1, accountId);
                try (ResultSet resultSet = ps.executeQuery())
                {
                    if (resultSet.next())
                    {
                        cardNumber = resultSet.getString(1);
                    }
                }
            }
            if (accountId == 0)
            {
                logger.info("委托提现用户ID不存在,用户第三方账号：" + mobile_no + "流水号：" + mchnt_txn_ssn);
                params.put("NO", "NO");
                return;
            }
            if (StringHelper.isEmpty(cardNumber))
            {
                throw new LogicalException("银行卡不存在!");
            }
            try
            {
                // 开启事务
                serviceResource.openTransactions(connection);
                // 用户的锁定账户
                T6101 sdzh = selectT6101(connection, accountId, T6101_F03.SDZH, false);
                if (sdzh == null)
                {
                    throw new LogicalException("锁定账号不存在!");
                }
                
                int ptId = getPTID(connection);
                Timestamp times = getCurrentTimestamp(connection);
                T6501 t6501 = new T6501();
                t6501.F02 = OrderType.WITHDRAW.orderType();
                t6501.F03 = T6501_F03.DTJ;
                t6501.F04 = times;
                t6501.F05 = times;
                t6501.F07 = T6501_F07.WT;
                t6501.F08 = accountId;
                t6501.F09 = ptId;
                t6501.F10 = mchnt_txn_ssn;
                t6501.F12 = "委托提现";
                t6501.F13 = amount;
                // 插入订单
                int orderId = insertT6501(connection, t6501);
                params.put("orderId", String.valueOf(orderId));
                t6503 = new T6503();
                t6503.F01 = orderId;
                t6503.F02 = accountId;
                t6503.F03 = amount;
                t6503.F04 = BigDecimal.ZERO;
                t6503.F05 = BigDecimal.ZERO;
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
        }
    }
    
    /**
     * 描述:插入提现订单 
     * 作者:wangshaohua 
     * 创建时间：2015年4月28日
     * 
     * @param connection
     * @param entity
     * @throws SQLException
     */
    protected void insertT6503(Connection connection, T6503 entity)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S65.T6503 SET F01 = ?, F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ? , F09 = ?"))
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
    
    /**
     * 插入充值订单信息
     * 
     * @param connection
     * @param entity
     *            充值订单表
     * @throws SQLException
     */
    protected void insertT6502(Connection connection, T6502 entity)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S65.T6502 SET F01 = ?, F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F09 = ?, F10 = ? "))
        {
            pstmt.setInt(1, entity.F01);
            pstmt.setInt(2, entity.F02);
            pstmt.setBigDecimal(3, entity.F03);
            pstmt.setBigDecimal(4, entity.F04);
            pstmt.setBigDecimal(5, entity.F05);
            pstmt.setString(6, entity.F06);
            pstmt.setInt(7, entity.F07);
            // pstmt.setString(8, entity.F08); // 充值成功后加入
            Map<String, Object> result = getEmpInfo(entity.F02, connection);
            pstmt.setString(8, (String)result.get("empNum"));
            pstmt.setString(9, (String)result.get("empStatus"));
            pstmt.execute();
        }
    }
    
    /**
     * 插入订单
     * 
     * @param connection
     * @param entity
     *            订单表信息
     * @return int 返回订单ID
     * @throws SQLException
     */
    /*protected int insertT6501(Connection connection, T6501 entity)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S65.T6501 SET F02 = ?, F03 = ?, F04 = CURRENT_TIMESTAMP(), F05 = CURRENT_TIMESTAMP(), F06 = ?, F07 = ?, F08 = ?, F09 = ?, F10 = ? , F12 = ?",
                PreparedStatement.RETURN_GENERATED_KEYS))
        {
            pstmt.setInt(1, entity.F02);
            pstmt.setString(2, entity.F03.name());
            pstmt.setTimestamp(3, entity.F06);
            pstmt.setString(4, entity.F07.name());
            pstmt.setInt(5, entity.F08);
            pstmt.setInt(6, entity.F09);
            pstmt.setString(7, entity.F10);
            pstmt.setString(8, entity.F12);
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
    }*/
    
    /**
     * 用户ID查询
     * @param userCustId
     * @return
     * @throws Throwable
     */
    private int selectT6119(Connection connection, String mobile_no)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01 FROM S61.T6119 WHERE T6119.F03 = ? LIMIT 1"))
        {
            pstmt.setString(1, mobile_no);
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
    
    @Override
    public BigDecimal selectT6502(int orderId)
        throws Throwable
    {
        BigDecimal fee = BigDecimal.ZERO;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F04 FROM S65.T6502 WHERE T6502.F01 = ? "))
            {
                pstmt.setInt(1, orderId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        fee = resultSet.getBigDecimal(1);
                    }
                }
                if (fee == BigDecimal.ZERO)
                {
                    return fee;
                }
            }
//            try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S65.T6502 SET F05 = ? WHERE F01 = ?"))
//            {
//                pstmt.setBigDecimal(1, fee);
//                pstmt.setInt(2, orderId);
//                pstmt.execute();
//            }
        }
        return fee;
    }
    
    @Override
    public void updateT6503(int orderId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S65.T6503 SET F05 = F04 WHERE F01 = ?"))
            {
                pstmt.setInt(1, orderId);
                pstmt.execute();
            }
        }
    }
    
    @Override
    public void updateT6502(int orderId)
        throws Throwable
    {
        BigDecimal fee = BigDecimal.ZERO;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F04 FROM S65.T6502 WHERE T6502.F01 = ? "))
            {
                pstmt.setInt(1, orderId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        fee = resultSet.getBigDecimal(1);
                    }
                }
                if (fee == BigDecimal.ZERO)
                {
                    return;
                }
            }
            try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S65.T6502 SET F05 = ? WHERE F01 = ?"))
            {
                pstmt.setBigDecimal(1, fee);
                pstmt.setInt(2, orderId);
                pstmt.execute();
            }
        }
    }
    
    @Override
    public void updateT6502(String MCHNT_TXN_SSN, int orderId, BigDecimal mchnt_amt)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("UPDATE S65.T6502 SET F05 = ?, F08 = ? WHERE F01 = ?"))
            {
                pstmt.setBigDecimal(1, mchnt_amt);
                pstmt.setString(2, MCHNT_TXN_SSN);
                pstmt.setInt(3, orderId);
                pstmt.execute();
            }
        }
    }
    
    @Override
    public int updateOrder(Map<String, String> params)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            String mchnt_txn_ssn = params.get("mchnt_txn_ssn");
            int orderId = Integer.parseInt(params.get("orderId"));
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
                    connection.prepareStatement("UPDATE S65.T6503 SET T6503.F05 = ?, T6503.F08 = ?,T6503.F09= ? WHERE T6503.F01=?"))
                {
                    // 实收手续费《因富友新接口暂款返回 只能用应收为准》
                    pstmts.setBigDecimal(1, t6503.F04);
                    pstmts.setString(2, mchnt_txn_ssn);
                    pstmts.setInt(3, aId);
                    pstmts.setInt(4, orderId);
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
                    String source = params.get("WT") == null ? "提现" : "委托提现";
                    t6102.F09 = source;
                    // 插入提现金额的流水记录（往来账户）
                    insertT6102(connection, t6102);
                    // 如果提现包含 手续费，则插入提现手续费的流水记录（往来账户）
                    if (t6503.F04.compareTo(new BigDecimal(0)) > 0)
                    {
                        // 往来账户的余额为 ：以前的余额再减去手续费
                        wT6101.F06 = wT6101.F06.subtract(t6503.F04);
                        T6102 tt6102 = new T6102();
                        tt6102.F02 = wT6101.F01;
                        tt6102.F03 = FeeCode.TX_SXF;
                        tt6102.F04 = sT6101.F01;
                        tt6102.F07 = t6503.F04;
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
                    if (t6503.F04.compareTo(new BigDecimal(0)) > 0)
                    {
                        // 锁定账户的余额为以前的余额再加上手续费
                        sT6101.F06 = sT6101.F06.add(t6503.F04);
                        T6102 tt6102 = new T6102();
                        tt6102.F02 = sT6101.F01;
                        tt6102.F03 = FeeCode.TX_SXF;
                        tt6102.F04 = wT6101.F01;
                        tt6102.F06 = t6503.F04;
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
            connection.prepareStatement("INSERT INTO S61.T6130 SET F02 = ?, F03 = ?, F04 = ?, F06 = ?, F07 = ?, F08 = CURRENT_TIMESTAMP(), F09 = ?, F13=?, F14 = CURRENT_TIMESTAMP(),F17 = ?,F18 = ?",
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
     *  此处 admin 用户名不能承意更改，如改此方法必段得改 -hsp
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
    public T6503 selectT6503(Connection connection, int F01)
        throws SQLException
    {
        T6503 record = null;
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
            return record;
        }
    }
    
    @Override
    public boolean refundWithderaw(int orderId, String mchnt_txn_ssn)
        throws Throwable
    {
        boolean flag = true;
        try (Connection connection = getConnection())
        {
            try
            {
                // 开启事务
                serviceResource.openTransactions(connection);
                int accountId;
                try (PreparedStatement ps =
                    connection.prepareStatement("SELECT F08 FROM S65.T6501 WHERE F01 = ? FOR UPDATE"))
                {
                    ps.setInt(1, orderId);
                    try (ResultSet resultSet = ps.executeQuery())
                    {
                        if (resultSet.next())
                        {
                            accountId = resultSet.getInt(1);
                        }
                        else
                        {
                            throw new LogicalException("查询提现订单异常!");
                        }
                    }
                }
                // 根据订单ID查询提现提单
                T6503 t6503 = selectT6503(connection, orderId);
                // 用户往来账户信息
                T6101 uT6101 = selectT6101(connection, accountId, T6101_F03.WLZH, true);
                if (uT6101 == null)
                {
                    throw new LogicalException("用户往来账户不存在!");
                }
                // 插入用户资金流水
                uT6101.F06 = uT6101.F06.add(t6503.F03);
                T6102 uT6102 = new T6102();
                uT6102.F02 = uT6101.F01;
                uT6102.F03 = FeeCode.TX;
                uT6102.F04 = uT6101.F01;
                uT6102.F06 = t6503.F03;
                uT6102.F08 = uT6101.F06;
                uT6102.F09 = "提现退票";
                insertT6102(connection, uT6102);
                updateT6101(connection, uT6101.F06, uT6101.F01);
                if (t6503.F05.compareTo(BigDecimal.ZERO) > 0)
                {
                    // 插入用户资金流水
                    uT6101.F06 = uT6101.F06.add(t6503.F05);
                    T6102 uT6102f = new T6102();
                    uT6102f.F02 = uT6101.F01;
                    uT6102f.F03 = FeeCode.TX_SXF;
                    uT6102f.F04 = uT6101.F01;
                    uT6102f.F06 = t6503.F05;
                    uT6102f.F08 = uT6101.F06;
                    uT6102f.F09 = "提现退票手续费";
                    insertT6102(connection, uT6102f);
                    updateT6101(connection, uT6101.F06, uT6101.F01);
                    int pid = getPTID(connection);
                    // 平台往来账户信息
                    T6101 cT6101 = selectT6101(connection, pid, T6101_F03.WLZH, true);
                    if (cT6101 == null)
                    {
                        throw new LogicalException("平台往来账户不存在!");
                    }
                    // 插入平台账户资金流水
                    cT6101.F06 = cT6101.F06.subtract(t6503.F05);
                    T6102 cT6102 = new T6102();
                    cT6102.F02 = cT6101.F01;
                    cT6102.F03 = FeeCode.TX_SXF;
                    cT6102.F04 = cT6101.F01;
                    cT6102.F07 = t6503.F05;
                    cT6102.F08 = cT6101.F06;
                    cT6102.F09 = "提现退票手续费";
                    insertT6102(connection, cT6102);
                }
                updateT6501(orderId);
                // 提交事务
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                flag = false;
                serviceResource.rollback(connection);
                throw e;
            }
            return flag;
        }
    }
    
    /**
     * 提现退票
     * @param orderId
     * @throws Throwable
     */
    public void updateT6501(int orderId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("UPDATE S65.T6501 SET F03 = ?, F11 = ?, F12 = ? WHERE F01 = ?"))
            {
                pstmt.setString(1, T6501_F03.SB.name());
                pstmt.setString(2, T6501_F11.S.name());
                pstmt.setString(3, "提现退票");
                pstmt.setInt(4, orderId);
                pstmt.execute();
            }
        }
    }
}
