/*package com.dimeng.p2p.app.servlets.pay.service.fuyou.achieve;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dimeng.framework.config.Envionment;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.OrderType;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.entities.T6102;
import com.dimeng.p2p.S61.entities.T6119;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S61.enums.T6118_F02;
import com.dimeng.p2p.S61.enums.T6118_F03;
import com.dimeng.p2p.S61.enums.T6123_F05;
import com.dimeng.p2p.S65.entities.T6501;
import com.dimeng.p2p.S65.entities.T6502;
import com.dimeng.p2p.S65.entities.T6517;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.S65.enums.T6501_F07;
import com.dimeng.p2p.S71.entities.T7101;
import com.dimeng.p2p.app.servlets.pay.service.fuyou.service.ChargeManage;
import com.dimeng.p2p.escrow.fuyou.cond.ChargeCond;
import com.dimeng.p2p.escrow.fuyou.entity.ChargeOrder;
import com.dimeng.p2p.escrow.fuyou.entity.UserQueryEntity;
import com.dimeng.p2p.escrow.fuyou.entity.entities.Auth;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouTypeEnum;
import com.dimeng.p2p.escrow.fuyou.service.TransferManage;
import com.dimeng.p2p.escrow.fuyou.util.BackCodeInfo;
import com.dimeng.p2p.escrow.fuyou.util.MchntTxnSsn;
import com.dimeng.p2p.escrow.fuyou.variables.FuyouVariable;
import com.dimeng.p2p.variables.defines.LetterVariable;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.p2p.variables.defines.pays.PayVariavle;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BooleanParser;
import com.dimeng.util.parser.EnumParser;
import com.dimeng.util.parser.IntegerParser;
import com.google.gson.Gson;

*//**
 * 
 * 充值实现类
 * <富友托管>
 * 
 * @author  heshiping
 * @version  [版本号, 2015年11月10日]
 *//*
public class ChargeManageImpl extends AbstractEscrowService implements ChargeManage
{
    
    public ChargeManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public String addOrder(BigDecimal amount, int payCompanyCode, BigDecimal fee)
        throws Throwable
    {
        if (serviceResource.getSession() == null || !serviceResource.getSession().isAuthenticated())
        {
            throw new ParameterException("鉴权失败");
        }
        String mPhone = configureProvider.getProperty(PayVariavle.CHARGE_MUST_PHONE);
        String mRealName = configureProvider.getProperty(PayVariavle.CHARGE_MUST_NCIIC);
        // 认证信息
        Auth auth = getAutnInfo();
        if ("S".equals(mPhone) && !auth.phone)
        {
            throw new LogicalException("手机号未认证");
        }
        if ("S".equals(mRealName) && !auth.realName)
        {
            throw new LogicalException("未实名认证");
        }
        if (amount.compareTo(new BigDecimal(0)) <= 0 || payCompanyCode <= 0)
        {
            throw new ParameterException("金额或支付类型错误");
        }
        String min = configureProvider.getProperty(PayVariavle.CHARGE_MIN_AMOUNT);
        String max = configureProvider.getProperty(PayVariavle.CHARGE_MAX_AMOUNT);
        if (amount.compareTo(new BigDecimal(min)) < 0 || amount.compareTo(new BigDecimal(max)) > 0)
        {
            StringBuilder builder = new StringBuilder("充值金额必须大于");
            builder.append(min);
            builder.append("元小于");
            builder.append(max);
            builder.append("元");
            throw new LogicalException(builder.toString());
        }
        // 数据连接
        try (Connection connection = getConnection())
        {
            Throwable exception = null;
            // 充值订单
            ChargeOrder order = null;
            String mchnt_txn_ssn = null;
            try
            {
                // 开启事务
                connection.setAutoCommit(false);
                // 平台信息
                T7101 t7101 = selectT7101(connection);
                BigDecimal poundage = fee;
                // 订单表
                T6501 t6501 = new T6501();
                // 类型编码
                t6501.F02 = OrderType.CHARGE.orderType();
                // 状态,DTJ:待提交;YTJ:已提交;DQR:待确认;CG:成功;SB:失败;
                t6501.F03 = T6501_F03.DTJ;
                // 创建时间
                t6501.F04 = new Timestamp(System.currentTimeMillis());
                t6501.F05 = new Timestamp(System.currentTimeMillis());
                // 订单来源,XT:系统;YH:用户;HT:后台;
                t6501.F07 = T6501_F07.YH;
                // 用户ID
                t6501.F08 = serviceResource.getSession().getAccountId();
                // 平台ID
                t6501.F09 = t7101.F01;
                // 流水号
                t6501.F10 = MchntTxnSsn.getMts(FuyouTypeEnum.YHCZ.name());
                mchnt_txn_ssn = t6501.F10;
                int oId = 0;
                // 查询资金账户信息
                T6101 t6101 = selectT6101(connection, t7101.F01, T6101_F03.WLZH);
                if (t6101 == null)
                {
                    throw new LogicalException("用户往来账户不存在");
                }
                // 插入订单信息
                oId = insertT6501(connection, t6501);
                if (oId <= 0)
                {
                    throw new LogicalException("数据库异常");
                }
                // 充值订单表
                T6502 t6502 = new T6502();
                t6502.F01 = oId;
                t6502.F02 = serviceResource.getSession().getAccountId();
                // 充值金额
                t6502.F03 = amount;
                // 应收手续费
                t6502.F04 = poundage;
                // 实收手续费
                t6502.F05 = BigDecimal.ZERO;
                // 支付公司代号
                t6502.F07 = payCompanyCode;
                // 流水号 充值成功后增加此记录字段
                // t6502.F08 = serialNumber;
                // 插入充值订单信息
                insertT6502(connection, t6502);
                order = new ChargeOrder();
                order.id = oId;
                order.amount = amount;
                order.orderTime = t6501.F04;
                order.payCompanyCode = payCompanyCode;
                // order.serialNumber = serialNumber;
                // 提交事务
                connection.commit();
            }
            catch (Exception e)
            {
                connection.rollback();
                exception = e;
            }
            finally
            {
                if (connection != null)
                {
                    if (exception != null)
                    {
                        try
                        {
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                        catch (Throwable e)
                        {
                        }
                        exception.printStackTrace();
                    }
                    // 关闭事务
                    connection.setAutoCommit(true);
                }
            }
            if (exception != null)
            {
                throw exception;
            }
            return mchnt_txn_ssn;
        }
    }
    
    *//**
     * 插入充值订单信息
     * 
     * @param connection
     * @param entity
     *            充值订单表
     * @throws SQLException
     *//*
    protected void insertT6502(Connection connection, T6502 entity)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S65.T6502 SET F01 = ?, F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ? "))
        {
            pstmt.setInt(1, entity.F01);
            pstmt.setInt(2, entity.F02);
            pstmt.setBigDecimal(3, entity.F03);
            pstmt.setBigDecimal(4, entity.F04);
            pstmt.setBigDecimal(5, entity.F05);
            pstmt.setString(6, entity.F06);
            pstmt.setInt(7, entity.F07);
            // pstmt.setString(8, entity.F08); // 充值成功后加入
            pstmt.execute();
        }
    }
    
    *//**
     * 插入订单
     * 
     * @param connection
     * @param entity
     *            订单表信息
     * @return int 返回订单ID
     * @throws SQLException
     *//*
    protected int insertT6501(Connection connection, T6501 entity)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S65.T6501 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?, F10 = ? ",
                PreparedStatement.RETURN_GENERATED_KEYS))
        {
            pstmt.setInt(1, entity.F02);
            pstmt.setString(2, entity.F03.name());
            pstmt.setTimestamp(3, entity.F04);
            pstmt.setTimestamp(4, entity.F05);
            pstmt.setTimestamp(5, entity.F06);
            pstmt.setString(6, entity.F07.name());
            pstmt.setInt(7, entity.F08);
            pstmt.setInt(8, entity.F09);
            pstmt.setString(9, entity.F10);
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
    
    @Override
    public UserQueryEntity userChargeQuery(String mchnt_cd, String user_ids)
        throws Throwable
    {
        *//**
         * 加载用户信息查询-富友
         *//*
        UserQueryEntity userQueryEntity = new UserQueryEntity();
        // 商户代码
        userQueryEntity.setMchnt_cd(mchnt_cd);
        // 交易日期
        userQueryEntity.setMchnt_txn_dt(new SimpleDateFormat("yyyyMMdd").format((new Timestamp(
            System.currentTimeMillis()))));
        // 流水号
        userQueryEntity.setMchnt_txn_ssn(MchntTxnSsn.getMts(FuyouTypeEnum.YHXX.name()));
        // 待查询的登录帐户列表
        userQueryEntity.setUser_ids(user_ids);
        
        List<String> params = new ArrayList<>();
        params.add(userQueryEntity.getMchnt_cd());
        params.add(userQueryEntity.getMchnt_txn_dt());
        params.add(userQueryEntity.getUser_ids());
        
        logger.info(userQueryEntity.getMchnt_cd());
        logger.info(userQueryEntity.getMchnt_txn_dt());
        logger.info(userQueryEntity.getUser_ids());
        
        // 获取签名字符串
        String ChkValue = chkValue(params);
        
        logger.info("signature == " + ChkValue);
        // 签名数据
        userQueryEntity.setSignature(ChkValue);
        return userQueryEntity;
    }
    
    @Override
    public Auth getAutnInfo()
        throws Throwable
    {
        try (Connection connection = getConnection("S61"))
        {
            Auth auth = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F02, F03, F05 FROM S61.T6118 WHERE T6118.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        auth = new Auth();
                        auth.realName = T6118_F02.TG.name().equals(resultSet.getString(1));
                        auth.phone = T6118_F03.TG.name().equals(resultSet.getString(2));
                    }
                }
            }
            return auth;
        }
    }
    
    @Override
    public T7101 selectT7101(Connection connection)
        throws Throwable
    {
        // 查询平台信息
        T7101 t7101 = null;
        try (PreparedStatement ps = connection.prepareStatement("SELECT F01, F02, F03, F04 FROM S71.T7101 LIMIT 1"))
        {
            try (ResultSet resultSet = ps.executeQuery())
            {
                if (resultSet.next())
                {
                    t7101 = new T7101();
                    t7101.F01 = resultSet.getInt(1);
                    t7101.F02 = resultSet.getString(2);
                    t7101.F03 = resultSet.getString(3);
                    t7101.F04 = resultSet.getString(4);
                }
            }
        }
        return t7101;
    }
    
    public T6101 selectT6101(Connection connection, int F02, T6101_F03 F03)
        throws Throwable
    {
        T6101 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07 FROM S61.T6101 WHERE T6101.F02 = ? AND T6101.F03 = ?"))
        {
            pstmt.setInt(1, F02);
            pstmt.setString(2, F03.name());
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6101();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = T6101_F03.parse(resultSet.getString(3));
                    record.F04 = resultSet.getString(4);
                    record.F05 = resultSet.getString(5);
                    record.F06 = resultSet.getBigDecimal(6);
                    record.F07 = resultSet.getTimestamp(7);
                }
            }
            return record;
        }
    }
    
    @Override
    public Map<String, String> createChargeUrI(ChargeCond cond, BigDecimal fee)
        throws Throwable
    {
        Map<String, String> params = new HashMap<String, String>();
        params.put("mchnt_cd", cond.mchntCd());
        params.put("mchnt_txn_ssn", cond.mchntTxnSsn());
        params.put("login_id", cond.loginId());
        params.put("amt", cond.amt());
        params.put("page_notify_url", trimBlank(cond.pageNotifyUrl()));
        //        params.put("page_notify_url", trimBlank("http://112.95.233.249:5162/app/pay/service/fuyou/ret/chargeRet.htm"));
        //        params.put("page_notify_url", trimBlank("http://www.dimengwx.cc/40080/pay/service/fuyou/ret/chargeRet.htm"));
        params.put("back_notify_url", trimBlank(cond.backNotifyUrl()));
        //        params.put("back_notify_url", trimBlank("http://www.dimengwx.cc/40080/pay/service/fuyou/ret/chargeNotice.htm"));
        //        params.put("back_notify_url",
        //            trimBlank("http://112.95.233.249:5162/app/pay/service/fuyou/ret/chargeNotice.htm"));
        logger.info("充值信息==" + new Gson().toJson(params));
        String str = getSignature(params);
        logger.info("充值信息拼接=" + str);
        // 获取签名字符串
        String signature = encryptByRSA(str);
        logger.info("签名=" + signature);
        params.put("signature", signature);
        return params;
    }
    
    @Override
    public Map<String, String> selectT6501Success(String mchnt_txn_ssn)
        throws Throwable
    {
        Map<String, String> params = null;
        try (Connection connection = getConnection())
        {
            // 查询 订单ID
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F03 FROM S65.T6501 WHERE T6501.F10 = ? LIMIT 1 "))
            {
                pstmt.setString(1, mchnt_txn_ssn);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        params = new HashMap<String, String>();
                        params.put("orderId", String.valueOf(resultSet.getInt(1)));
                        params.put("state", resultSet.getString(2));
                        return params;
                    }
                }
            }
            return params;
        }
    }
    
    @Override
    public void updateOrderStatus(T6501_F03 t6501_F03, int orderId, String MCHNT_TXN_SSN)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            // 开启事物
            connection.setAutoCommit(false);
            // 锁定订单
            T6501 order = lock(connection, orderId);
            // 判断订单状态
            if (!T6501_F03.CG.name().equals(order.F03.name()) && !T6501_F03.DQR.name().equals(order.F03.name()))
            {
                // 更新订单状态为待确认+并插入流水号
                try (PreparedStatement pstmt =
                    connection.prepareStatement("UPDATE S65.T6501 SET F03 = ? , F10 = ? WHERE F01 = ?"))
                {
                    pstmt.setString(1, t6501_F03.name());
                    pstmt.setString(2, MCHNT_TXN_SSN);
                    pstmt.setInt(3, orderId);
                    pstmt.execute();
                }
            }
            // 提交事物，同时解除订单的锁定状态
            connection.commit();
            // 关闭事物
            connection.setAutoCommit(true);
        }
    }
    
    *//**
     * 锁定订单 <功能详细描述>
     * 
     * @param connection
     * @param orderId
     * @return
     * @throws SQLException
     *//*
    private T6501 lock(Connection connection, int orderId)
        throws SQLException
    {
        T6501 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07 FROM S65.T6501 WHERE T6501.F01 = ? FOR UPDATE"))
        {
            pstmt.setInt(1, orderId);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6501();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = T6501_F03.parse(resultSet.getString(3));
                    record.F04 = resultSet.getTimestamp(4);
                    record.F05 = resultSet.getTimestamp(5);
                    record.F06 = resultSet.getTimestamp(6);
                    record.F07 = EnumParser.parse(T6501_F07.class, resultSet.getString(7));
                }
            }
        }
        return record;
    }
    
    @Override
    public void updateT6502(String MCHNT_TXN_SSN, int orderId, BigDecimal mchnt_fee)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("UPDATE S65.T6502 SET F05 = ?, F08 = ? WHERE F01 = ?"))
            {
                pstmt.setBigDecimal(1, mchnt_fee);
                pstmt.setString(2, MCHNT_TXN_SSN);
                pstmt.setInt(3, orderId);
                pstmt.execute();
            }
        }
    }
    
    @Override
    public void expand(ServiceSession serviceSession, int orderId)
        throws Throwable
    {
        Timestamp t = new Timestamp(System.currentTimeMillis());
        // 数据连接
        try (Connection connection = getConnection())
        {
            Throwable exception = null;
            try
            {
                // 打开事务
                connection.setAutoCommit(false);
                T6502 t6502 = selectT6502(connection, orderId);
                // 计算奖励金额
                BigDecimal amt = null;
                // 用户ID
                int accountId = t6502.F02;
                // 推广人id
                int exId = 0;
                // 锁定用户首次充值T6311
                exId = selectT6311(connection, accountId);
                // 是否有推广人
                if (exId == 0)
                {
                    // 无邀人
                    return;
                }
                // 记录充值次数
                int chargeCount = selectChargeCount(connection, accountId);
                if (chargeCount > 1)
                {
                    // 返回0不是首次充值
                    return;
                }
                else
                {
                    if (t6502.F03.compareTo(new BigDecimal(configureProvider.getProperty(SystemVariable.TG_YXCZJS))) < 0)
                    {
                        // 第一次充值，未达奖励基数：记录首次金额及时间
                        updateT6311(connection, t6502.F03, t, accountId);
                        // 首次充值金额未达到标准
                        return;
                    }
                }
                // 第一次充值，达到奖励基数：记录首次金额及时间
                updateT6311(connection, t6502.F03, t, accountId);
                // 是否开启推广,true:开启,false,不开启
                String flag = configureProvider.getProperty(SystemVariable.ACCOUNT_SFTG);
                if (!"true".equals(flag))
                {
                    return;
                }
                // 收款账号(平台第三方账号)
                T6119 t6119 = null;
                // 推广人未托管不奖励
                boolean tg = BooleanParser.parse(configureProvider.getProperty(SystemVariable.SFZJTG));
                // 富友托管 
                if (!tg)
                {
                    t6119 = selectT6119(connection, exId);
                    if (StringHelper.isEmpty(t6119.F03))
                    {
                        // 邀人无第三方托管
                        return;
                    }
                }
                // 判断获取奖励次数是否超过上限(当月)
                int excount = selectUpperLimit(connection, exId);
                if (excount >= IntegerParser.parse(configureProvider.getProperty(SystemVariable.TG_YXTGSX)))
                {
                    return;
                }
                // 锁定推广人奖励记录
                selectT6310(connection, exId);
                // 计算奖励金额
                amt = new BigDecimal(configureProvider.getProperty(SystemVariable.TG_YXTGJL));
                // 流水号
                String mchnt_txn_ssn = MchntTxnSsn.getMts(FuyouTypeEnum.TGJL.name());
                // 付款账号(平台第三方账号)
                String out_cust_no = configureProvider.format(FuyouVariable.FUYOU_P2P_ACCOUNT_NAME);
                // 收款人账号
                String in_cust_no = t6119.F03;
                
                // ***P2P平台调用转账接口对推广增加奖励
                TransferManage manage = serviceSession.getService(TransferManage.class);
                Map<String, Object> map =
                    manage.createTransferMap(mchnt_txn_ssn, out_cust_no, in_cust_no, getAmt(amt), "transferBmu");
                
                // 判断奖励是否成功 —— 未成功不需处理业务
                if (map == null || !"0000".equals(map.get("resp_code")))
                {
                    logger.error("邀人受益转账失败 推广人ID：" + exId + ",交易时间:" + t + ",被推广人：" + accountId + ",首次充值金额："
                        + t6502.F03 + "，返回码：" + map.get("resp_code"));
                }
                // 执行业务操作
                Savepoint businessSavepoint = connection.setSavepoint();
                try
                {
                    // 推广受益处理
                    confirm(connection, t6502, amt, exId, t);
                    logger.info("邀人受益转账成功 推广人ID：" + exId + ",交易时间:" + t + ",被推广人：" + accountId + ",首次充值金额：" + t6502.F03
                        + "，返回码：" + map.get("resp_code"));
                }
                catch (Exception e)
                {
                    exception = e;
                    connection.rollback(businessSavepoint);
                }
            }
            catch (Exception e)
            {
                connection.rollback();
                exception = e;
            }
            finally
            {
                if (connection != null)
                {
                    if (exception != null)
                    {
                        try
                        {
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                        catch (Throwable e)
                        {
                        }
                        exception.printStackTrace();
                    }
                    // 关闭事务
                    connection.setAutoCommit(true);
                }
            }
            if (exception != null)
            {
                throw exception;
            }
        }
    }
    
    *//**
     * 推广受益处理
     * <功能详细描述>
     * @param connection
     * @param t6502
     * @param amt 推广金额
     * @param exId 受益人ID
     * @param t 时间
     * @throws Throwable
     *//*
    private void confirm(Connection connection, T6502 t6502, BigDecimal amt, int exId, Timestamp t)
        throws Throwable
    {
        T7101 t7010 = selectT7101(connection);
        if (t7010.F01 <= 0)
        {
            throw new LogicalException("平台账号不存在");
        }
        // 平台往来账户信息
        T6101 cT6101 = selectT6101(connection, t7010.F01, T6101_F03.WLZH);
        if (cT6101 == null)
        {
            throw new LogicalException("平台往来账户不存在");
        }
        
        updateT6101(amt, exId, T6101_F03.WLZH);
        T6101 eT6101 = selectT6101(connection, exId, T6101_F03.WLZH);
        T6102 eT6102 = new T6102();
        eT6102.F02 = eT6101.F01;
        eT6102.F03 = 9001;
        eT6102.F04 = cT6101.F01;
        eT6102.F05 = t;
        eT6102.F06 = amt;
        eT6102.F08 = eT6101.F06;
        eT6102.F09 = "有效推广奖励";
        // 增加资金流水记录
        insertT6102(connection, eT6102);
        // 站内信
        int letterId = insertT6123(connection, exId, "有效推广奖励", t, T6123_F05.WD);
        String tem = configureProvider.getProperty(LetterVariable.TG_YXJL);
        Envionment envionment = configureProvider.createEnvionment();
        envionment.set("cz", t6502.F03.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        envionment.set("jl", amt.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        insertT6124(connection, letterId, StringHelper.format(tem, envionment));
        // 平台账户扣除奖励金额
        updateT6101(amt.multiply(new BigDecimal(-1)), cT6101.F02, T6101_F03.WLZH);
        cT6101 = selectT6101(connection, cT6101.F02, T6101_F03.WLZH);
        T6102 ecT6102 = new T6102();
        ecT6102.F02 = cT6101.F01;
        ecT6102.F03 = 9001;
        ecT6102.F04 = eT6101.F01;
        ecT6102.F05 = t;
        ecT6102.F07 = amt;
        ecT6102.F08 = cT6101.F06;
        ecT6102.F09 = "有效推广奖励";
        // 增加订单记录
        insertT6102(connection, ecT6102);
        T6501 zzt6501 = new T6501();
        zzt6501.F02 = OrderType.TRANSFER.orderType();
        zzt6501.F03 = T6501_F03.DTJ;
        zzt6501.F07 = T6501_F07.XT;
        zzt6501.F08 = cT6101.F02;
        // 增加订单记录
        int ordId = insertT6501c(connection, zzt6501);
        T6517 t6517 = new T6517();
        t6517.F01 = ordId;
        t6517.F02 = amt;
        t6517.F03 = cT6101.F02;
        t6517.F04 = exId;
        t6517.F05 = "有效推广奖励";
        // 增加转账订单记录
        insertT6517(connection, t6517);
        // 更新推广人 T6310 推广
        try (PreparedStatement ps = connection.prepareStatement("UPDATE S63.T6310 SET F05=F05+? WHERE F01=?"))
        {
            ps.setBigDecimal(1, amt);
            ps.setInt(2, exId);
            ps.executeUpdate();
        }
        // 更新推广 充值奖励金额5元
        try (PreparedStatement ps = connection.prepareStatement("UPDATE S63.T6311 SET F05=? WHERE F03=?"))
        {
            ps.setBigDecimal(1, amt);
            ps.setInt(2, t6502.F02);
            ps.executeUpdate();
        }
    }
    
    *//**
     * 增加转账订单记录
     * 
     * @param t6517
     * @throws Throwable
     *//*
    private void insertT6517(Connection connection, T6517 t6517)
        throws Throwable
    {
        try (PreparedStatement ps =
            connection.prepareStatement("INSERT INTO S65.T6517 SET F01 = ?, F02 = ?, F03 = ?, F04 = ?, F05 = ?"))
        {
            ps.setInt(1, t6517.F01);
            ps.setBigDecimal(2, t6517.F02);
            ps.setInt(3, t6517.F03);
            ps.setInt(4, t6517.F04);
            ps.setString(5, t6517.F05);
            ps.execute();
        }
    }
    
    *//**
     * 用户站内信内容
     * 
     * @param F01
     * @param F02
     * @throws SQLException
     *//*
    private void insertT6124(Connection connection, int F01, String F02)
        throws SQLException
    {
        try
        {
            try (PreparedStatement pstmt = connection.prepareStatement("INSERT INTO S61.T6124 SET F01 = ?, F02 = ?"))
            {
                pstmt.setInt(1, F01);
                pstmt.setString(2, F02);
                pstmt.execute();
            }
        }
        catch (Exception e)
        {
            logger.error(e, e);
            throw e;
        }
    }
    
    *//**
     * 用户站内信
     * 
     * @param connection
     * @param F02
     * @param F03
     * @param F04
     * @param F05
     * @return
     * @throws SQLException
     *//*
    private int insertT6123(Connection connection, int F02, String F03, Timestamp F04, T6123_F05 F05)
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
    
    *//**
     * 增加订单记录
     * 
     * @param connection
     * @param t6501
     * @return int
     * @throws Throwable
     *//*
    private int insertT6501c(Connection connection, T6501 t6501)
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
    }
    
    *//**
     * 增加资流水记录
     * 
     * @param entity
     *            T6102订单信息
     * @return int返回插入成功ID
     * @throws SQLException
     *//*
    private int insertT6102(Connection connection, T6102 entity)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S61.T6102 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?",
                PreparedStatement.RETURN_GENERATED_KEYS))
        {
            pstmt.setInt(1, entity.F02);
            pstmt.setInt(2, entity.F03);
            pstmt.setInt(3, entity.F04);
            pstmt.setTimestamp(4, entity.F05);
            pstmt.setBigDecimal(5, entity.F06);
            pstmt.setBigDecimal(6, entity.F07);
            pstmt.setBigDecimal(7, entity.F08);
            pstmt.setString(8, entity.F09);
            pstmt.execute();
            try (ResultSet resultSet = pstmt.getGeneratedKeys())
            {
                if (resultSet.next())
                {
                    return resultSet.getInt(1);
                }
                return 0;
            }
        }
    }
    
    *//**
     * 更新资金账户
     * 
     * @param amt
     *            金额
     * @param exId
     *            推广ID
     * @param F03
     *            账户类型
     * @throws Throwable
     *//*
    private void updateT6101(BigDecimal amt, int exId, T6101_F03 F03)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("UPDATE S61.T6101 SET F06 = F06 + ?, F07 = ? WHERE F02 = ? AND F03 = ? "))
            {
                pstmt.setBigDecimal(1, amt);
                pstmt.setTimestamp(2, getCurrentTimestamp(connection));
                pstmt.setInt(3, exId);
                pstmt.setString(4, F03.name());
                pstmt.execute();
            }
        }
    }
    
    @Override
    public void updateT6501(int orderId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("UPDATE S65.T6501 SET F03 = ? , F06 = ? WHERE F01 = ?"))
            {
                pstmt.setString(1, T6501_F03.SB.toString());
                pstmt.setTimestamp(2, getCurrentTimestamp(connection));
                pstmt.setInt(3, orderId);
                pstmt.execute();
            }
        }
    }
    
    @Override
    public int selectT6311(Connection connection, int accountId)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F02 FROM S63.T6311 WHERE F03 = ? FOR UPDATE"))
        {
            pstmt.setInt(1, accountId);
            try (ResultSet resultSet = pstmt.executeQuery())
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
    
    @Override
    public int selectChargeCount(Connection connection, int accountId)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT COUNT(T6501.F01) FROM S65.T6501 INNER JOIN S65.T6502 ON T6501.F01 = T6502.F01 WHERE T6501.F03 = ? AND T6501.F02 = ? AND T6502.F02 = ? "))
        {
            pstmt.setString(1, T6501_F03.CG.name());
            pstmt.setInt(2, OrderType.CHARGE.orderType());
            pstmt.setInt(3, accountId);
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
    public T6119 selectT6119(Connection connection, int userId)
        throws Throwable
    {
        T6119 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03 FROM S61.T6119 WHERE T6119.F01 = ? LIMIT 1"))
        {
            pstmt.setInt(1, userId);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6119();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getString(3);
                }
            }
        }
        return record;
    }
    
    @Override
    public void updateT6311(Connection connection, BigDecimal amt, Timestamp t, int accountId)
        throws Throwable
    {
        try (PreparedStatement psc = connection.prepareStatement("UPDATE S63.T6311 SET F04 = ?, F06 = ? WHERE F03 = ?"))
        {
            psc.setBigDecimal(1, amt);
            psc.setTimestamp(2, t);
            psc.setInt(3, accountId);
            psc.execute();
        }
    }
    
    @Override
    public int selectUpperLimit(Connection connection, int exid)
        throws Throwable
    {
        try (PreparedStatement ps =
            connection.prepareStatement("SELECT COUNT(F02) FROM S63.T6311 WHERE F06 >= ? AND F06 <= ? AND F02=? AND F05>0 "))
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
                    return resultSet.getInt(1);
                }
                return 0;
            }
        }
    }
    
    @Override
    public boolean chargeRetDecode(Map<String, String> param)
        throws Exception
    {
        Map<String, String> paramMap = null;
        if (param != null)
        {
            paramMap = new HashMap<String, String>();
            //交易金额
            paramMap.put("amt", param.get("amt"));
            //交易用户
            paramMap.put("login_id", param.get("login_id"));
            //商户代码
            paramMap.put("mchnt_cd", param.get("mchnt_cd"));
            //请求流水号
            paramMap.put("mchnt_txn_ssn", param.get("mchnt_txn_ssn"));
            //网银充值验签中包含 rem（备注）字段
            if (param.containsKey("rem"))
            {
                paramMap.put("rem", param.get("rem"));
            }
            //响应码
            paramMap.put("resp_code", param.get("resp_code"));
            //响应消息
            //            if (param.containsKey("resp_desc"))
            //            {
            //                paramMap.put("resp_desc", param.get("resp_desc"));
            //            }
            //签名数据
            paramMap.put("signature", param.get("signature"));
        }
        String str = getSignature(paramMap);
        //        String str = getSignatureForRet(param);
        if (!verifyByRSA(str, paramMap.get("signature")))
        {
            logger.info("充值-验签失败-返信息==" + BackCodeInfo.info(param.get("resp_code")));
            return false;
        }
        return true;
    }
    
    @Override
    public void selectT6310(Connection connection, int exid)
        throws Throwable
    {
        try (PreparedStatement psc = connection.prepareStatement("SELECT F01 FROM S63.T6310 WHERE F01=? FOR UPDATE "))
        {
            psc.setInt(1, exid);
            psc.execute();
        }
    }
    
    @Override
    public T6502 selectT6502(Connection connection, int orderId)
        throws SQLException
    {
        T6502 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08 FROM S65.T6502 WHERE T6502.F01 = ? "))
        {
            pstmt.setInt(1, orderId);
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
}
*/