package com.dimeng.p2p.escrow.fuyou.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.OrderType;
import com.dimeng.p2p.S62.entities.T6252;
import com.dimeng.p2p.S62.enums.T6250_F08;
import com.dimeng.p2p.S62.enums.T6252_F09;
import com.dimeng.p2p.S65.entities.T6501;
import com.dimeng.p2p.S65.entities.T6505;
import com.dimeng.p2p.S65.entities.T6506;
import com.dimeng.p2p.S65.entities.T6521;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.S65.enums.T6501_F07;
import com.dimeng.p2p.S65.enums.T6501_F11;
import com.dimeng.p2p.S65.enums.T6514_F07;
import com.dimeng.p2p.escrow.fuyou.entity.TransactionQueryDetailedEntity;
import com.dimeng.p2p.escrow.fuyou.entity.TransactionQueryResponseEntity;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouEnum;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouRespCode;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouTypeEnum;
import com.dimeng.p2p.escrow.fuyou.executor.FYAdvanceExecutor;
import com.dimeng.p2p.escrow.fuyou.executor.FYPTAdvanceExecutor;
import com.dimeng.p2p.escrow.fuyou.executor.FYPrepaymentExecutor;
import com.dimeng.p2p.escrow.fuyou.executor.FYRepaymentExecutor;
import com.dimeng.p2p.escrow.fuyou.executor.FYTenderConfirmExecutor;
import com.dimeng.p2p.escrow.fuyou.face.TransactionQueryFace;
import com.dimeng.p2p.escrow.fuyou.service.AutoManage;
import com.dimeng.p2p.escrow.fuyou.service.BidFaceManage;
import com.dimeng.p2p.escrow.fuyou.util.MchntTxnSsn;
import com.dimeng.p2p.escrow.fuyou.variables.FuyouVariable;
import com.dimeng.util.StringHelper;

/**
 * 
 * 定时自动任务实现类
 * 
 * @author  heshiping
 * @version  [版本号, 2015年12月17日]
 */
public class AutoManageImpl extends AbstractEscrowService implements AutoManage
{
    
    public AutoManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public List<T6501> searchFailedOrder(int orderType, Timestamp timestamp, int number)
        throws Throwable
    {
        
        List<T6501> results = null;
        String sql =
            "SELECT * FROM S65.T6501 WHERE F02 = ? AND F11 = ? AND F03 IN (?, ?, ?) AND F04 <= ? AND F10 != '' ORDER BY F01 LIMIT ? ";
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt = connection.prepareStatement(sql))
            {
                pstmt.setInt(1, orderType);
                pstmt.setString(2, T6501_F11.F.name());
                pstmt.setString(3, T6501_F03.DTJ.name());
                pstmt.setString(4, T6501_F03.DQR.name());
                pstmt.setString(5, T6501_F03.SB.name());
                pstmt.setTimestamp(6, timestamp);
                pstmt.setInt(7, number);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        T6501 t6501 = new T6501();
                        t6501.F01 = resultSet.getInt("F01");
                        t6501.F02 = resultSet.getInt("F02");
                        t6501.F03 = T6501_F03.parse(resultSet.getString("F03"));
                        t6501.F04 = resultSet.getTimestamp("F04");
                        t6501.F05 = resultSet.getTimestamp("F05");
                        t6501.F06 = resultSet.getTimestamp("F06");
                        t6501.F07 = T6501_F07.parse(resultSet.getString("F07"));
                        t6501.F08 = resultSet.getInt("F08");
                        t6501.F09 = resultSet.getInt("F09");
                        t6501.F10 = resultSet.getString("F10");
                        t6501.F11 = T6501_F11.parse(resultSet.getString("F11"));
                        if (results == null)
                        {
                            results = new ArrayList<T6501>();
                        }
                        results.add(t6501);
                    }
                }
            }
            
        }
        return results;
    }
    
    @Override
    public boolean selectFuyou(Connection connection, ServiceSession serviceSession, T6501 t6501, boolean flag)
        throws Throwable
    {
        String busi_tp;
        String cust_no;
        if (flag)
        {
            if (t6501.F08 == getPTID(connection))
            {
                busi_tp = FuyouEnum.PWPC.name();
                cust_no = configureProvider.format(FuyouVariable.FUYOU_P2P_ACCOUNT_NAME);
            }
            else
            {
                busi_tp = FuyouEnum.PW03.name();
                cust_no = selectT6119(connection, t6501.F08);
            }
        }
        else
        {
            busi_tp = FuyouEnum.PW03.name();
            cust_no = selectT6119(connection, t6501.F08);
        }
        // 流水号
        String mchnt_txn_ssn = MchntTxnSsn.getMts(FuyouTypeEnum.JYCX.name());
        //起始时间
        String start_day = new SimpleDateFormat("yyyyMMdd").format(t6501.F04);
        // 结束时间
        String end_day = new SimpleDateFormat("yyyyMMdd").format(t6501.F04);
        // 交易流水
        String txn_ssn = t6501.F10;
        // 交易查询接口地址
        String tradingQueryUrl = configureProvider.format(FuyouVariable.FUYOU_TRADINGQUERY_URL);
        //调用第三方平台
        TransactionQueryResponseEntity result =
            TransactionQueryFace.executeTransactionQuery(configureProvider.format(FuyouVariable.FUYOU_ACCOUNT_ID),
                mchnt_txn_ssn,
                busi_tp,
                start_day,
                end_day,
                txn_ssn,
                cust_no,
                "",
                "",
                "",
                "",
                tradingQueryUrl,
                serviceSession);
        String resultCode = result.respCode;
        // 定义返回结果详情对象
        List<TransactionQueryDetailedEntity> respDetailList = null;
        if (FuyouRespCode.JYCG.getRespCode().equals(resultCode))
        {
            //结果详情
            respDetailList = result.detailedEntity;
            if (respDetailList != null && respDetailList.size() > 0)
            {
                for (TransactionQueryDetailedEntity detail : respDetailList)
                {
                    // 获取流水号
                    String serialNumStr = detail.getMchntSsn();
                    // 通过流水号 判断该笔交易是不是 要查询的那笔交易
                    if (!StringHelper.isEmpty(serialNumStr) && serialNumStr.equals(t6501.F10))
                    {
                        if (FuyouRespCode.JYCG.getRespCode().equals(detail.getTxnRspCd()))
                        {
                            updateT6501(connection, txn_ssn, t6501.F01, T6501_F03.DQR.name());
                            return true;
                        }
                        else
                        {
                            // 说明第托管平台失败了，P2P重新生成流水号
                            logger.info("提前还款订单：" + t6501.F01 + " -重新生成流水号");
                            txn_ssn = MchntTxnSsn.getMts(FuyouTypeEnum.NEW.name());
                            updateT6501(connection, txn_ssn, t6501.F01, T6501_F03.DTJ.name());
                        }
                    }
                }
            }
            else
            {
                // 查询结果无，说明托管系统无此记录
                if (!T6501_F03.DTJ.equals(t6501.F03))
                {
                    // txn_ssn = MchntTxnSsn.getMts(FuyouTypeEnum.NEW.name());
                    updateT6501(connection, txn_ssn, t6501.F01, T6501_F03.DTJ.name());
                }
            }
        }
        else
        {
            throw new LogicalException("对账异常!");
        }
        return false;
    }
    
    @Override
    public void searchBidFailedOrder(ServiceSession serviceSession, Timestamp timestamp, int number)
        throws Throwable
    {
        List<T6501> list = searchFailedOrder(OrderType.BID.orderType(), timestamp, number);
        if (list == null)
        {
            return;
        }
        try (Connection connection = getConnection())
        {
            Map<String, String> params = new HashMap<String, String>();
            for (T6501 t6501 : list)
            {
                boolean flag = selectFuyouBid(serviceSession, connection, t6501, FuyouEnum.PW13.name(), params);
                if (flag)
                {
                    // 执行流单
                    bidCancel(serviceSession, connection, t6501.F01, params);
                }
                else
                {
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("UPDATE S65.T6501 SET F03 = ? , F11 = ?, F12 = ?  WHERE F01 = ? "))
                    {
                        pstmt.setString(1, T6501_F03.SB.name());
                        pstmt.setString(2, T6501_F11.S.name());
                        pstmt.setString(3, "自动对账,投资失败,第三方不存在该记录");
                        pstmt.setInt(4, t6501.F01);
                        pstmt.execute();
                    }
                }
            }
        }
    }
    
    /**
     * 对账[对平台投资失败单对账，防止第三方成功，而平台未更新]——>流标
     * <流标>
     * @param connection
     * @param orderId
     * @param params
     * @throws Throwable 
     */
    private void bidCancel(ServiceSession serviceSession, Connection connection, int orderId, Map<String, String> params) throws Throwable {
        boolean flag = selectBid(connection, orderId, params);
        if (!flag) {
            logger.info("投资对账详细信息查异常");
            throw new LogicalException("投资对账详细信息查异常!");
        }
        if (!StringHelper.isEmpty(params.get("flag"))) {
            // 因已生成 T6250记录了
            logger.info("该订单已成功处理ID：" + orderId);
            updateT6501Status(connection, T6501_F03.CG.name(), orderId);
        }
        // 流标
        BidFaceManage manage = serviceSession.getService(BidFaceManage.class);
        Map<String, Object> map = null;
        for (int i = 0; i < 3; i++) {
            map = manage.createBidCancel(
                    MchntTxnSsn.getMts(FuyouTypeEnum.TBLB.name()),
                    params.get("out_cust_no"),
                    params.get("in_cust_no"),
                    params.get("contract_no"));
            if (map != null  && ((FuyouRespCode.JYCG.getRespCode().equals(map.get("resp_code")) || FuyouRespCode.JYWC.getRespCode().equals(map.get("resp_code"))))) {
                logger.info("对账-预授权撤销-流标成功ID:" + orderId);
                try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S65.T6501 SET F03 = ?, F11 = ?, F12 = ?  WHERE F01 = ? ")) {
                    pstmt.setString(1, T6501_F03.SB.name());
                    pstmt.setString(2, T6501_F11.S.name());
                    pstmt.setString(3, "自动对账,投资失败,第三方成功,现对单标退款成功");
                    pstmt.setInt(4, orderId);
                    pstmt.execute();
                }
                break;
            } else {
                logger.info("预授权撤销失败-第" + i + "次");
            }
        }
    }
    
    /**
     * 查询投资信息
     * @param connection
     * @param F01
     * @param param
     * @return
     * @throws Throwable
     */
    private boolean selectBid(Connection connection, int F01, Map<String, String> param)
        throws Throwable
    {
        boolean flag = false;
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT T6501.F01, T6504.F02, T6504.F03, T6504.F04, T6504.F05, T6230.F02, T6230.F20 FROM S65.T6501 "
            + "LEFT JOIN S65.T6504 ON T6504.F01 = T6501.F01 LEFT JOIN S62.T6230 ON T6230.F01 = T6504.F03 WHERE T6501.F01 = ?");
        try (PreparedStatement pstmt = connection.prepareStatement(sb.toString()))
        {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    param.put("orderId", String.valueOf(resultSet.getInt(1)));
                    param.put("out_cust_no", selectT6119(connection, resultSet.getInt(2)));
                    param.put("bidId", String.valueOf(resultSet.getInt(3)));
                    param.put("amt", getAmt(resultSet.getBigDecimal(4)));
                    param.put("flag", String.valueOf(resultSet.getInt(5)));
                    param.put("in_cust_no", selectT6119(connection, resultSet.getInt(6)));
                    param.put("state", resultSet.getString(7));
                    flag = true;
                }
            }
        }
        return flag;
    }
    
    /**
     * 投资对账
     * <查询第三方>
     * @param serviceSession
     * @param t6501
     * @param busi_tp
     * @return
     * @throws Throwable
     */
    public boolean selectFuyouBid(ServiceSession serviceSession, Connection connection, T6501 t6501, String busi_tp,
        Map<String, String> params)
        throws Throwable
    {
        //调用第三方平台
        TransactionQueryResponseEntity result =
            TransactionQueryFace.executeTransactionQuery(configureProvider.format(FuyouVariable.FUYOU_ACCOUNT_ID),
                MchntTxnSsn.getMts(FuyouTypeEnum.JYCX.name()),
                busi_tp,
                new SimpleDateFormat("yyyyMMdd").format(t6501.F04),
                new SimpleDateFormat("yyyyMMdd").format(t6501.F04),
                t6501.F10,
                selectT6119(connection, t6501.F08),
                "",
                "",
                "",
                "",
                configureProvider.format(FuyouVariable.FUYOU_TRADINGQUERY_URL),
                serviceSession);
        // 定义返回结果详情对象
        List<TransactionQueryDetailedEntity> respDetailList = null;
        if (FuyouRespCode.JYCG.getRespCode().equals(result.respCode))
        {
            //结果详情
            respDetailList = result.detailedEntity;
            if (respDetailList != null && respDetailList.size() > 0)
            {
                for (TransactionQueryDetailedEntity detail : respDetailList)
                {
                    // 获取流水号
                    String serialNumStr = detail.getMchntSsn();
                    if (!StringHelper.isEmpty(serialNumStr) && serialNumStr.equals(t6501.F10))
                    {
                        if (FuyouRespCode.JYCG.getRespCode().equals(detail.getTxnRspCd()))
                        {
                            params.put("contract_no", detail.getContractNo());
                            return true;
                        }
                    }
                }
            }
        }
        else
        {
            throw new LogicalException("对账异常!");
        }
        return false;
    }
    
    /**
     * 将订单改为待确认状态
     * @param F10 流水号
     * @param F01 订单号
     * @param F03 状态
     * @throws SQLException
     */
    private void updateT6501(Connection connection, String F10, int F01, String F03)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S65.T6501 SET F03 = ?, F10 = ? WHERE F01 = ? "))
        {
            pstmt.setString(1, F03);
            pstmt.setString(2, F10);
            pstmt.setInt(3, F01);
            pstmt.execute();
        }
    }
    
    @Override
    public void updateT6250(T6250_F08 F08, int F01)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("UPDATE S62.T6250 SET T6250.F08 = ? WHERE T6250.F01 = ( SELECT T6505.F04 FROM S65.T6505 WHERE T6505.F01 = ? )"))
            {
                pstmt.setString(1, F08.name());
                pstmt.setInt(2, F01);
                pstmt.execute();
            }
        }
        
    }
    
    @Override
    public T6505 selectT6505(Connection connection, int F01)
        throws Throwable
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
    
    @Override
    public T6506 selectT6506(Connection connection, int F01)
        throws Throwable
    {
        T6506 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07 FROM S65.T6506 WHERE T6506.F01 = ? LIMIT 1"))
        {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6506();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getInt(3);
                    record.F04 = resultSet.getInt(4);
                    record.F05 = resultSet.getInt(5);
                    record.F06 = resultSet.getBigDecimal(6);
                    record.F07 = resultSet.getInt(7);
                }
            }
        }
        return record;
    }
    
    @Override
    public T6252 selectT6252(Connection connection, int F05, int F06, int F11)
        throws SQLException
    {
        T6252 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11 FROM S62.T6252 "
                + "WHERE T6252.F05 = ? AND T6252.F06 = ? AND T6252.F11 = ?"))
        {
            pstmt.setInt(1, F05);
            pstmt.setInt(2, F06);
            pstmt.setInt(3, F11);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6252();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getInt(3);
                    record.F04 = resultSet.getInt(4);
                    record.F05 = resultSet.getInt(5);
                    record.F06 = resultSet.getInt(6);
                    record.F07 = resultSet.getBigDecimal(7);
                    record.F08 = resultSet.getDate(8);
                    record.F09 = T6252_F09.parse(resultSet.getString(9));
                    record.F10 = resultSet.getTimestamp(10);
                    record.F11 = resultSet.getInt(11);
                }
            }
        }
        return record;
    }
    
    @Override
    public void updateT6501(Connection connection, int F01, String F03, String F11)
        throws Throwable
    {
        if (StringHelper.isEmpty(F11))
        {
            //修改订单状态
            updateT6501Status(connection, F03, F01);
        }
        else
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("UPDATE S65.T6501 SET F03 = ? , F11 = ?  WHERE F01 = ? "))
            {
                pstmt.setString(1, F03);
                pstmt.setString(2, F11);
                pstmt.setInt(3, F01);
                pstmt.execute();
            }
        }
    }
    
    @Override
    public String selectT6230(Connection connection, int F01)
        throws Throwable
    {
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT F20 FROM S62.T6230 WHERE T6230.F01 = ? "))
        {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    return resultSet.getString(1);
                }
            }
        }
        return null;
    }
    
    @Override
    public int selectT7101(Connection connection)
        throws Throwable
    {
        return getPTID(connection);
    }
    
    @Override
    public T6521 selectT6521(Connection connection, int F01)
        throws SQLException
    {
        T6521 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09 FROM S65.T6521 WHERE T6521.F01 = ? LIMIT 1"))
        {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6521();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getInt(3);
                    record.F04 = resultSet.getInt(4);
                    record.F05 = resultSet.getInt(5);
                    record.F06 = resultSet.getBigDecimal(6);
                    record.F07 = resultSet.getInt(7);
                    record.F08 = resultSet.getInt(8);
                    record.F09 = resultSet.getInt(9);
                }
            }
        }
        return record;
    }
    
    @Override
    public String selectT6514(Connection connection, int F01)
        throws SQLException
    {
        try (PreparedStatement ps = connection.prepareStatement("SELECT F07 FROM S65.T6514 WHERE F01=?"))
        {
            ps.setInt(1, F01);
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    return rs.getString(1);
                }
            }
        }
        return null;
    }
    
    @Override
    public void autoAdvanceDZ(FYPTAdvanceExecutor PTExecutor, FYAdvanceExecutor JGExecutor,
        ServiceSession serviceSession, T6501 t6501, Map<String, String> params)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            int PTID = selectT7101(connection);
            // 查询单垫付状态
            String F07 = selectT6514(connection, t6501.F01);
            if (StringHelper.isEmpty(F07))
            {
                logger.info("垫付订单明细记录不存在:" + t6501.F01);
                //  autoManage.updateT6501(t6501.F01, T6501_F03.SB.name(), T6501_F11.S.name());
                return;
            }
            if (T6514_F07.S.name().equals(F07))
            {
                logger.info(t6501.F01 + "已还-将此单改为成功");
                updateT6501(connection, t6501.F01, T6501_F03.CG.name(), T6501_F11.S.name());
                return;
            }
            boolean PJ;
            if (t6501.F08 == PTID)
            {
                logger.info("不良资产处理-对账ID:" + t6501.F01);
                PJ = true;
            }
            else
            {
                logger.info("机构垫付-对账ID:" + t6501.F01);
                PJ = false;
            }
            boolean flag = selectFuyou(connection, serviceSession, t6501, true);
            if (PJ)
            {
                // 平台垫付对账
                if (!flag)
                {
                    PTExecutor.submit(t6501.F01, params);
                    // 如果转账失败，则执行下一个
                    if (!"true".equals(params.get("success")))
                    {
                        logger.info("不良资产处理对账ID：" + t6501.F01 + "对账-再次提垫付失败");
                        return;
                    }
                    logger.info("不良资产处理对账ID：" + t6501.F01 + "对账-再次垫付成功");
                    
                }
                PTExecutor.confirm(t6501.F01, params);
                logger.info("不良资产处理对账ID：" + t6501.F01 + "对账-确认成功");
            }
            else
            {
                // 机构垫付对账
                if (!flag)
                {
                    JGExecutor.submit(t6501.F01, params);
                    // 如果转账失败，则执行下一个
                    if (!"true".equals(params.get("success")))
                    {
                        logger.info("机构垫付对账ID：" + t6501.F01 + "对账-再次垫付失败");
                        return;
                    }
                    logger.info("机构垫付对账ID：" + t6501.F01 + "对账-再次垫付成功");
                }
                JGExecutor.confirm(t6501.F01, params);
                logger.info("机构垫付对账ID：" + t6501.F01 + "对账-确认成功");
            }
        }
    }
    
    @Override
    public void autoLoanDZ(FYTenderConfirmExecutor executor, ServiceSession serviceSession, T6501 t6501,
        Map<String, String> params)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            
            boolean flag = selectFuyou(connection, serviceSession, t6501, false);
            if (flag)
            {
                // 查询放款单是否为放款状态
                T6505 t6505 = selectT6505(connection, t6501.F01);
                if (t6505 == null)
                {
                    logger.info("订单信息不存在-放款订单ID:" + t6501.F01);
                    updateT6501(connection, t6501.F01, T6501_F03.SB.name(), T6501_F11.S.name());
                    return;
                }
                // 查询标信息F20 状态
                String t6230_F20 = selectT6230(connection, t6505.F03);
                if (t6230_F20 == null)
                {
                    logger.info("投信息不存在-放款订单ID:" + t6501.F01);
                    updateT6501(connection, t6501.F01, T6501_F03.SB.name(), T6501_F11.S.name());
                    return;
                }
                if (!"DFK".equals(t6230_F20))
                {
                    logger.info("不是待放款状态,不能放款-说明订单已成功");
                    updateT6501(connection, t6501.F01, T6501_F03.CG.name(), T6501_F11.S.name());
                    return;
                }
                else
                {
                    updateT6501(connection, t6501.F01, T6501_F03.DQR.name(), null);
                }
            }
            else
            {
                executor.submit(t6501.F01, params);
                // 如果转账失败，则执行下一个
                if (!"true".equals(params.get("success")))
                {
                    logger.info("放款对账ID：" + t6501.F01 + "对账-再次放款失败");
                    //                        autoManage.updateT6250(T6250_F08.F, t6501.F01);
                    return;
                }
                logger.info("放款对账ID：" + t6501.F01 + "对账-再次放款成功");
            }
            executor.confirm(t6501.F01, params);
            logger.info("放款对账ID：" + t6501.F01 + "对账-确认成功");
        }
    }
    
    @Override
    public void autoPaymentDZ(FYRepaymentExecutor executor, ServiceSession serviceSession, T6501 t6501,
        Map<String, String> params)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            T6506 t6506 = selectT6506(connection, t6501.F01);
            if (t6506 == null)
            {
                logger.info("还款订单明细记录不存在");
                updateT6501(connection, t6501.F01, T6501_F03.SB.name(), T6501_F11.S.name());
                return;
            }
            // 查询还款记录
            T6252 t6252 = selectT6252(connection, t6506.F07, t6506.F05, t6506.F04);
            if (t6252 == null)
            {
                logger.info("还款记录不存在");
                updateT6501(connection, t6501.F01, T6501_F03.SB.name(), T6501_F11.S.name());
                return;
            }
            if (t6252.F09 == T6252_F09.YH)
            {
                logger.info(t6501.F01 + "已还-将此单改为成功");
                updateT6501(connection, t6501.F01, T6501_F03.CG.name(), T6501_F11.S.name());
                return;
            }
            boolean flag = selectFuyou(connection, serviceSession, t6501, true);
            if (!flag)
            {
                executor.submit(t6501.F01, params);
                // 如果转账失败，则执行下一个
                if (!"true".equals(params.get("success")))
                {
                    logger.info("手动还款对账ID：" + t6501.F01 + "对账-再次手动还款失败");
                    //   autoManage.updateT6250(T6250_F08.F, t6501.F01);
                    return;
                }
                logger.info("手动还款对账ID：" + t6501.F01 + "对账-再次手动还款成功");
                
            }
            executor.confirm(t6501.F01, params);
            logger.info("手动还款对账ID：" + t6501.F01 + "对账-确认成功");
        }
        
    }
    
    @Override
    public void antoPrepaymentDZ(FYPrepaymentExecutor executor, ServiceSession serviceSession, T6501 t6501,
        Map<String, String> params)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            // 订单查询
            T6521 t6521 = selectT6521(connection, t6501.F01);
            if (t6521 == null)
            {
                logger.info("提前还款订单明细记录不存在:" + t6501.F01);
                updateT6501(connection, t6501.F01, T6501_F03.SB.name(), T6501_F11.S.name());
                return;
            }
            
            if (FeeCode.TZ_BJ == t6521.F07 || FeeCode.TZ_LX == t6521.F07)
            {
                // 查询还款记录
                T6252 t6252 = selectT6252(connection, t6521.F07, t6521.F05, t6521.F04);
                if (t6252 == null)
                {
                    logger.info("还款记录不存在:" + t6501.F01);
                    updateT6501(connection, t6501.F01, T6501_F03.SB.name(), T6501_F11.S.name());
                    return;
                }
                if (t6252.F09 == T6252_F09.YH)
                {
                    logger.info(t6501.F01 + "已还-将此单改为成功");
                    updateT6501(connection, t6501.F01, T6501_F03.CG.name(), T6501_F11.S.name());
                    return;
                }
            }
            boolean flag = selectFuyou(connection, serviceSession, t6501, true);
            if (!flag)
            {
                executor.submit(t6501.F01, params);
                // 如果转账失败，则执行下一个
                if (!"true".equals(params.get("success")))
                {
                    logger.info("提前还款对账ID：" + t6501.F01 + "对账-再次提前还款失败");
                    //  autoManage.updateT6250(T6250_F08.F, t6501.F01);
                    return;
                }
                logger.info("提前还款对账ID：" + t6501.F01 + "对账-再次提前还款成功");
            }
            executor.confirm(t6501.F01, params);
            logger.info("提前还款对账ID：" + t6501.F01 + "对账-确认成功");
        }
        
    }
    
}
