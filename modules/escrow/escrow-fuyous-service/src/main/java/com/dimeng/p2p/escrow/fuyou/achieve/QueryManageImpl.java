package com.dimeng.p2p.escrow.fuyou.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.OrderType;
import com.dimeng.p2p.S61.entities.T6114;
import com.dimeng.p2p.S61.entities.T6130;
import com.dimeng.p2p.S61.enums.T6130_F09;
import com.dimeng.p2p.S62.entities.T6251;
import com.dimeng.p2p.S62.entities.T6265;
import com.dimeng.p2p.S62.entities.T6271;
import com.dimeng.p2p.S62.enums.T6251_F08;
import com.dimeng.p2p.S62.enums.T6271_F07;
import com.dimeng.p2p.S65.entities.T6501;
import com.dimeng.p2p.S65.entities.T6503;
import com.dimeng.p2p.S65.entities.T6507;
import com.dimeng.p2p.S65.entities.T6514;
import com.dimeng.p2p.S65.entities.T6529;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.S65.enums.T6501_F07;
import com.dimeng.p2p.S65.enums.T6501_F11;
import com.dimeng.p2p.S65.enums.T6529_F08;
import com.dimeng.p2p.common.ResourceProviderUtil;
import com.dimeng.p2p.common.entities.Dzxy;
import com.dimeng.p2p.escrow.fuyou.entity.TransactionQueryDetailedEntity;
import com.dimeng.p2p.escrow.fuyou.entity.TransactionQueryResponseEntity;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouEnum;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouRespCode;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouTypeEnum;
import com.dimeng.p2p.escrow.fuyou.executor.FYAdvanceExecutor;
import com.dimeng.p2p.escrow.fuyou.executor.FYPTAdvanceExecutor;
import com.dimeng.p2p.escrow.fuyou.executor.FYPrepaymentExecutor;
import com.dimeng.p2p.escrow.fuyou.executor.FYRepaymentExecutor;
import com.dimeng.p2p.escrow.fuyou.face.TransactionQueryFace;
import com.dimeng.p2p.escrow.fuyou.service.BidFaceManage;
import com.dimeng.p2p.escrow.fuyou.service.BidManage;
import com.dimeng.p2p.escrow.fuyou.service.QueryManage;
import com.dimeng.p2p.escrow.fuyou.service.TransferManage;
import com.dimeng.p2p.escrow.fuyou.util.MchntTxnSsn;
import com.dimeng.p2p.escrow.fuyou.variables.FuyouVariable;
import com.dimeng.p2p.order.DonationExecutor;
import com.dimeng.p2p.order.MallChangeExecutor;
import com.dimeng.p2p.order.MallRefundExecutor;
import com.dimeng.p2p.order.PdfFormationExecutor;
import com.dimeng.p2p.order.PreservationExecutor;
import com.dimeng.p2p.service.ContractManage;
import com.dimeng.p2p.user.servlets.thread.BadClaimContractPreservationThread;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;

/**
 * 
 * 相关查询
 * <功能详细描述>
 * 
 * @author  heshiping
 * @version  [版本号, 2015年12月25日]
 */
public class QueryManageImpl extends AbstractEscrowService implements QueryManage {
    
    public QueryManageImpl(ServiceResource serviceResource) {
        super(serviceResource);
    }
    
    @Override
    public boolean selectFuyou(ServiceSession serviceSession, T6501 t6501, Map<String, String> params) throws Throwable {
        try (Connection connection = getConnection()) {
            String busi_tp = null;
            String cust_no = null;
            switch (t6501.F02) {
            	// 投标
                case 20001:
                    busi_tp = FuyouEnum.PW13.name();
                    cust_no = selectT6119(connection, t6501.F08);
                    break;
                // 散标还款
                case 20004:
                    if (getPTID(connection) == selectT6506(connection, t6501.F01)) {
                        busi_tp = FuyouEnum.PWPC.name();
                    } else {
                        busi_tp = FuyouEnum.PW03.name();
                    }
                    cust_no = selectT6119(connection, t6501.F08);
                    break;
                // 提前还款
                case 30004:
                    // 违约金手续费
                    if (getPTID(connection) == selectT6521(connection, t6501.F01)) {
                        busi_tp = FuyouEnum.PWPC.name();
                        cust_no = configureProvider.format(FuyouVariable.FUYOU_P2P_ACCOUNT_NAME);
                        params.put("YH", "true");
                    } else {
                        busi_tp = FuyouEnum.PW03.name();
                        cust_no = selectT6119(connection, t6501.F08);
                        params.put("YH", "false");
                    }
                    break;
                // 垫付
                case 10004:
                    if (getPTID(connection) == selectT6514(connection, t6501.F01)) {
                        busi_tp = FuyouEnum.PWPC.name();
                        cust_no = configureProvider.format(FuyouVariable.FUYOU_P2P_ACCOUNT_NAME);
                        params.put("YH", "true");                        
                    } else {
                        busi_tp = FuyouEnum.PW03.name();
                        cust_no = selectT6119(connection, t6501.F08);
                        params.put("YH", "false");
                    }
                    break;
                // 债权对账
                case 20005:
                    busi_tp = FuyouEnum.PW03.name();
                    cust_no = selectT6119(connection, t6501.F08);
                    break;
                // 公益捐款对账
                case 50002:
                    busi_tp = FuyouEnum.PWPC.name();
                    cust_no = selectT6119(connection, t6501.F08);
                    break;
                case 50003:
                    busi_tp = FuyouEnum.PWPC.name();
                    cust_no = selectT6119(connection, t6501.F08);
                    break;
                case 50004:
                    busi_tp = FuyouEnum.PWPC.name();
                    cust_no = selectT6119(connection, t6501.F08);
                    break;
                //不良债权购买
                case 20015:
                    busi_tp = FuyouEnum.PW03.name();
                    cust_no = selectT6119(connection, t6501.F08);
                    break;
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
            String queryUrl = configureProvider.format(FuyouVariable.FUYOU_TRADINGQUERY_URL);
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
                    queryUrl,
                    serviceSession);
            String resultCode = result.respCode;
            // 定义返回结果详情对象
            List<TransactionQueryDetailedEntity> respDetailList = null;
            if (FuyouRespCode.JYCG.getRespCode().equals(resultCode)) {
                //结果详情
                respDetailList = result.detailedEntity;
                if (respDetailList != null && respDetailList.size() > 0) {
                    for (TransactionQueryDetailedEntity detail : respDetailList) {
                        // 获取流水号 
                        String serialNumStr = detail.getMchntSsn();
                        // 通过流水号 判断该笔交易是不是 要查询的那笔交易
                        /*if (!StringHelper.isEmpty(serialNumStr) && serialNumStr.equals(t6501.F10))
                        {*/
                        //                            if (FuyouRespCode.JYCG.getRespCode().equals(detail.getTxnRspCd()))
                            if (FuyouRespCode.JYCG.getRespCode().equals(resultCode))
                            {
                                if (t6501.F02 == OrderType.BID.orderType())
                                {
                                    params.put("contract_no", detail.getContractNo());
                                    bid20001(serviceSession, connection, t6501, params, true);
                                }
                                return true;
                            }
                            else
                            {
                                
                                if (t6501.F02 == OrderType.BID.orderType())
                                {
                                    params.put("contract_no", detail.getContractNo());
                                    bid20001(serviceSession, connection, t6501, params, false);
                                }
                                else if (t6501.F03 != T6501_F03.CG)
                                {
                                    // 说明第托管平台失败了，P2P重新生成流水号
                                    logger.info("手动对账订单：" + t6501.F01 + " -重新生成流水号");
                                    txn_ssn = MchntTxnSsn.getMts(FuyouTypeEnum.NEW.name());
                                    updateT6501(connection, txn_ssn, t6501.F01, T6501_F03.DTJ.name(), null);
                                }
                                else
                                {
                                    updateT6501(connection,
                                        T6501_F11.F.name(),
                                        t6501.F01,
                                        T6501_F03.CG.name(),
                                        "对账异常,请联系管理员");
                                }
                                params.put("isNo", "true");
                                return false;
                            }
                        }
                    //                    }
                }
                else
                {
                    if (t6501.F02 == OrderType.BID.orderType())
                    {
                        bid20001(serviceSession, connection, t6501, null, false);
                    }
                }
            }
            else
            {
                throw new LogicalException("手动对账异常!");
            }
        }
        params.put("isNo", "false");
        return false;
    }
    
    /**
     * 查询垫付人ID
     * <机构或平台>
     * @param connection
     * @param F01
     * @return
     * @throws SQLException
     */
    private int selectT6514(Connection connection, int F01)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F04 FROM S65.T6514 WHERE T6514.F01 = ? LIMIT 1"))
        {
            pstmt.setInt(1, F01);
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
    
    /**
     * 投资用户ID
     * <机构或平台>
     * @param connection
     * @param F01
     * @return
     * @throws SQLException
     */
    private int selectT6521(Connection connection, int F01)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F02 FROM S65.T6521 WHERE T6521.F01 = ? LIMIT 1"))
        {
            pstmt.setInt(1, F01);
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
    
    /**
     * 查询投资人ID
     * <T6506-针对平台ID,区分转账与划拨>
     * @param connection
     * @param F01
     * @return
     * @throws SQLException
     */
    private int selectT6506(Connection connection, int F01)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F02 FROM S65.T6506 WHERE T6506.F01 = ? LIMIT 1"))
        {
            pstmt.setInt(1, F01);
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
    public T6501 selectT6501(int orderId)
        throws Throwable
    {
        T6501 t6501 = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM S65.T6501 WHERE F01 = ? "))
            {
                pstmt.setInt(1, orderId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        t6501 = new T6501();
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
                        t6501.F12 = resultSet.getString("F12");
                        return t6501;
                    }
                }
            }
        }
        return t6501;
    }
    
    @Override
    public void updateT6501(int F01, String F11, String F12, String F03)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            if (StringHelper.isEmpty(F03))
            {
                try (PreparedStatement pstmt =
                    connection.prepareStatement("UPDATE S65.T6501 SET F11 = ?, F12 = ? WHERE F01 = ?"))
                {
                    pstmt.setString(1, F11);
                    pstmt.setString(2, F12);
                    pstmt.setInt(3, F01);
                    pstmt.execute();
                }
            }
            else
            {
                try (PreparedStatement pstmt =
                    connection.prepareStatement("UPDATE S65.T6501 SET F03 = ?, F11 = ?, F12 = ? WHERE F01 = ?"))
                {
                    pstmt.setString(1, F03);
                    pstmt.setString(2, F11);
                    pstmt.setString(3, F12);
                    pstmt.setInt(4, F01);
                    pstmt.execute();
                }
            }
            
        }
    }
    
    @Override
    public String selectT6119(int F01)
        throws Throwable
    {
        return getAccountId(F01);
    }
    
    /**
     * 散标投标对账处理
     * <20001-散标投标>
     * @param serviceSession
     * @param connection
     * @param t6501
     * @param params
     * @throws Throwable 
     */
    private void bid20001(ServiceSession serviceSession, Connection connection, T6501 t6501,
        Map<String, String> params, boolean flag)
        throws Throwable
    {
        if (flag)
        {
            if (t6501.F03 != T6501_F03.CG)
            {
                // 流标单
                if (t6501.F02 == OrderType.BID.orderType())
                {
                    bidCancel(serviceSession, connection, t6501.F01, params);
                }
            }
            else
            {
                updateT6501(connection, T6501_F11.S.name(), t6501.F01, T6501_F03.CG.name(), "确认,投资成功");
                
            }
        }
        else if (t6501.F03 == T6501_F03.CG)
        {
            // 未查到-说明失败
            updateT6501(connection, T6501_F11.F.name(), t6501.F01, T6501_F03.CG.name(), "对账异常,请联系管理员");
        }
        else
        {
            // 未查到-说明失败
            updateT6501(connection, T6501_F11.S.name(), t6501.F01, T6501_F03.SB.name(), "投资失败-订单未提交第三方");
        }
    }
    
    /**
     * 手动对账[对平台投标失败单手动对账，防止第三方成功，而平台未更新]——>流标
     * <流标>
     * @param connection
     * @param orderId
     * @param params
     * @throws Throwable 
     */
    private void bidCancel(ServiceSession serviceSession, Connection connection, int orderId, Map<String, String> params) throws Throwable {
        boolean flag = selectBid(connection, orderId, params);
        if (!flag) {
            logger.info("投标手动对账详细信息查异常");
            throw new LogicalException("投资手动对账详细信息查异常!");
        }
        if (!StringHelper.isEmpty(params.get("flag"))) {
            // 因已生成 T6250记录了
            logger.info("该订单已成功处理ID：" + orderId);
            updateT6501(connection, T6501_F11.S.name(), orderId, T6501_F03.CG.name(), "投资成功");
        }
        // 流标
        BidFaceManage manage = serviceSession.getService(BidFaceManage.class);
        Map<String, Object> map = null;
        for (int i = 0; i < 3; i++) {
            map = manage.createBidCancel(
                    MchntTxnSsn.getMts(FuyouTypeEnum.TBLB.name()), params.get("out_cust_no"), params.get("in_cust_no"), params.get("contract_no"));
            if (map != null && (FuyouRespCode.JYCG.getRespCode().equals(map.get("resp_code")) || FuyouRespCode.JYWC.getRespCode().equals(map.get("resp_code")))) {
                logger.info("手动对账-预授权撤销-流标成功ID:" + orderId);
                updateT6501(connection, T6501_F11.S.name(), orderId, T6501_F03.SB.name(), "投标更新失败-流标处理-" + map.get("resp_code"));
                break;
            } else {
                logger.info("预授权撤销失败-第" + i + "次");
            }
        }
    }
    
    /**
     * 查询投标信息
     * @param connection
     * @param F01
     * @param param
     * @return
     * @throws Throwable
     */
    private boolean selectBid(Connection connection, int F01, Map<String, String> param) throws Throwable {
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
     * 将订单改为待确认状态
     * @param F10 流水号
     * @param F01 订单号
     * @param F03 状态
     * @throws SQLException
     */
    private void updateT6501(Connection connection, String F10, int F01, String F03, String F12)
        throws SQLException
    {
        if (StringHelper.isEmpty(F12))
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
        else
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("UPDATE S65.T6501 SET F03 = ?, F11 = ?, F12 = ? WHERE F01 = ? "))
            {
                pstmt.setString(1, F03);
                pstmt.setString(2, F10);
                pstmt.setString(3, F12);
                pstmt.setInt(4, F01);
                pstmt.execute();
            }
        }
    }
    
    @Override
    public void bid20004(ServiceSession serviceSession, T6501 t6501, Map<String, String> params, boolean flag,
        FYRepaymentExecutor executor)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            if (flag)
            {
                switch (t6501.F03.name())
                {
                    case "DTJ":
                        updateT6501(connection, T6501_F11.F.name(), t6501.F01, T6501_F03.DQR.name(), "手动对账-还款第三方成功");
                        executor.confirm(t6501.F01, params);
                        isNoT6501(connection, t6501.F01, "手动对账-还款成功");
                        break;
                    case "DQR":
                        // 第三方成功，平台未更新
                        executor.confirm(t6501.F01, params);
                        logger.info("手动对账还款-确认成功ID:" + t6501.F01);
                        updateT6501(connection, T6501_F11.S.name(), t6501.F01, T6501_F03.CG.name(), "手动对账-还款成功");
                        break;
                    case "CG":
                        updateT6501(connection, T6501_F11.S.name(), t6501.F01, T6501_F03.CG.name(), "确认,手动还款成功");
                        break;
                    case "SB":
                        updateT6501(connection, T6501_F11.F.name(), t6501.F01, T6501_F03.DQR.name(), "手动对账-还款第三方成功");
                        executor.confirm(t6501.F01, params);
                        isNoT6501(connection, t6501.F01, "手动对账-还款第三方成功");
                        break;
                }
            }
            else
            {
                switch (t6501.F03.name())
                {
                    case "DTJ":
                        executor.submit(t6501.F01, params);
                        if (!"true".equals(params.get("success")))
                        {
                            logger.info("手动对账-还款失败ID:" + t6501.F01);
                            updateT6501(connection,
                                T6501_F11.F.name(),
                                t6501.F01,
                                T6501_F03.SB.name(),
                                params.get("msg"));
                            break;
                        }
                        logger.info("手动对账还款-成功ID:" + t6501.F01);
                        executor.confirm(t6501.F01, params);
                        isNoT6501(connection, t6501.F01, "手动对账-还款成功");
                        break;
                    case "SB":
                        updateT6501(connection, T6501_F11.F.name(), t6501.F01, T6501_F03.DTJ.name(), "手动对账-重新还款");
                        executor.submit(t6501.F01, params);
                        if (!"true".equals(params.get("success")))
                        {
                            logger.info("手动对账-还款失败ID:" + t6501.F01);
                            updateT6501(connection,
                                T6501_F11.F.name(),
                                t6501.F01,
                                T6501_F03.SB.name(),
                                params.get("msg"));
                            break;
                        }
                        logger.info("手动对账还款-成功ID:" + t6501.F01);
                        executor.confirm(t6501.F01, params);
                        isNoT6501(connection, t6501.F01, "手动对账-还款成功");
                        break;
                    case "CG":
                        updateT6501(connection,
                            T6501_F11.F.name(),
                            t6501.F01,
                            T6501_F03.CG.name(),
                            "对账异常,请与第三方对账人工核实，第三方查询无此记录");
                        break;
                }
            }
        }
    }
    
    @Override
    public void bid30004(ServiceSession serviceSession, T6501 t6501, Map<String, String> params, boolean flag,
        FYPrepaymentExecutor executor)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            if (flag)
            {
                switch (t6501.F03.name())
                {
                    case "DTJ":
                        updateT6501(connection, T6501_F11.F.name(), t6501.F01, T6501_F03.DQR.name(), "手动对账-提前还款失败单");
                        executor.confirm(t6501.F01, params);
                        isNoT6501(connection, t6501.F01, "手动对账-提前还款成功");
                        break;
                    case "DQR":
                        // 第三方成功，平台未更新
                        executor.confirm(t6501.F01, params);
                        logger.info("手动对账提前还款-确认成功ID:" + t6501.F01);
                        isNoT6501(connection, t6501.F01, "手动对账-提前还款成功");
                        break;
                    case "CG":
                        updateT6501(connection, T6501_F11.S.name(), t6501.F01, T6501_F03.CG.name(), "确认,提前还款成功");
                        break;
                    case "SB":
                        updateT6501(connection, T6501_F11.F.name(), t6501.F01, T6501_F03.DQR.name(), "手动对账-提前还款第三方成功");
                        executor.confirm(t6501.F01, params);
                        isNoT6501(connection, t6501.F01, "手动对账-提前还款第三方成功");
                        break;
                }
            }
            else
            {
                switch (t6501.F03.name())
                {
                    case "DTJ":
                        executor.submit(t6501.F01, params);
                        if (!"true".equals(params.get("success")))
                        {
                            logger.info("手动对账-提前还款失败ID:" + t6501.F01);
                            updateT6501(connection,
                                T6501_F11.F.name(),
                                t6501.F01,
                                T6501_F03.SB.name(),
                                params.get("msg"));
                            break;
                        }
                        logger.info("手动对账提前还款-成功ID:" + t6501.F01);
                        executor.confirm(t6501.F01, params);
                        isNoT6501(connection, t6501.F01, "手动对账-提前还款成功");
                        break;
                    case "SB":
                        updateT6501(connection, T6501_F11.F.name(), t6501.F01, T6501_F03.DTJ.name(), "手动对账-重新提前还款");
                        executor.submit(t6501.F01, params);
                        if (!"true".equals(params.get("success")))
                        {
                            logger.info("手动对账-提前还款失败ID:" + t6501.F01);
                            updateT6501(connection,
                                T6501_F11.F.name(),
                                t6501.F01,
                                T6501_F03.SB.name(),
                                params.get("msg"));
                            break;
                        }
                        logger.info("手动对账提前还款-成功ID:" + t6501.F01);
                        executor.confirm(t6501.F01, params);
                        isNoT6501(connection, t6501.F01, "手动对账-还款成功");
                        break;
                    case "CG":
                        updateT6501(connection,
                            T6501_F11.F.name(),
                            t6501.F01,
                            T6501_F03.CG.name(),
                            "对账异常,请与第三方对账人工核实，第三方查询无此记录");
                        break;
                }
            }
        }
    }
    
    @Override
    public void bid10004PT(ServiceSession serviceSession, T6501 t6501, Map<String, String> params, boolean flag,
        FYPTAdvanceExecutor executor, HttpServletRequest request)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            if (flag)
            {
                switch (t6501.F03.name())
                {
                    case "DTJ":
                        updateT6501(connection, T6501_F11.F.name(), t6501.F01, T6501_F03.DQR.name(), "手动对账-不良资产处理失败单");
                        executor.confirm(t6501.F01, params);
                        isNoT6501(connection, t6501.F01, "手动对账-提前还款成功");
                        break;
                    case "DQR":
                        // 第三方成功，平台未更新
                        executor.confirm(t6501.F01, params);
                        logger.info("手动对账平台对账-确认成功ID:" + t6501.F01);
                        updateT6501(connection, T6501_F11.S.name(), t6501.F01, T6501_F03.CG.name(), "手动对账-不良资产处理成功");
                        /**
                         * 不良资产合同保全
                         */
                        saveAdvanceContract(request, serviceSession, t6501.F01);
                        break;
                    case "CG":
                        updateT6501(connection, T6501_F11.S.name(), t6501.F01, T6501_F03.CG.name(), "确认,不良资产处理成功");
                        break;
                    case "SB":
                        updateT6501(connection, T6501_F11.F.name(), t6501.F01, T6501_F03.DQR.name(), "手动对账-不良资产处理第三方成功");
                        executor.confirm(t6501.F01, params);
                        isNoT6501(connection, t6501.F01, "手动对账-不良资产处理第三方成功");
                        break;
                }
            }
            else
            {
                switch (t6501.F03.name())
                {
                    case "DTJ":
                        executor.submit(t6501.F01, params);
                        if (!"true".equals(params.get("success")))
                        {
                            logger.info("手动对账-不良资产处理失败ID:" + t6501.F01);
                            updateT6501(connection,
                                T6501_F11.F.name(),
                                t6501.F01,
                                T6501_F03.SB.name(),
                                params.get("msg"));
                            break;
                        }
                        logger.info("手动对账不良资产处理-成功ID:" + t6501.F01);
                        executor.confirm(t6501.F01, params);
                        isNoT6501(connection, t6501.F01, "手动对账-不良资产处理成功");
                        /**
                         * 不良资产合同保全
                         */
                        saveAdvanceContract(request, serviceSession, t6501.F01);
                        break;
                    case "SB":
                        updateT6501(connection, T6501_F11.F.name(), t6501.F01, T6501_F03.DTJ.name(), "手动对账-重新不良资产处理");
                        executor.submit(t6501.F01, params);
                        if (!"true".equals(params.get("success")))
                        {
                            logger.info("手动对账-不良资产处理失败ID:" + t6501.F01);
                            updateT6501(connection,
                                T6501_F11.F.name(),
                                t6501.F01,
                                T6501_F03.SB.name(),
                                params.get("msg"));
                            break;
                        }
                        logger.info("手动对账不良资产处理-成功ID:" + t6501.F01);
                        executor.confirm(t6501.F01, params);
                        isNoT6501(connection, t6501.F01, "手动对账-还款成功");
                        break;
                    case "CG":
                        updateT6501(connection,
                            T6501_F11.F.name(),
                            t6501.F01,
                            T6501_F03.CG.name(),
                            "对账异常,请与第三方对账人工核实，第三方查询无此记录");
                        break;
                }
            }
        }
    }
    
    @Override
    public void bid10004JG(ServiceSession serviceSession, T6501 t6501, Map<String, String> params, boolean flag,
        FYAdvanceExecutor executor, HttpServletRequest request)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            if (flag)
            {
                switch (t6501.F03.name())
                {
                    case "DTJ":
                        updateT6501(connection, T6501_F11.F.name(), t6501.F01, T6501_F03.DQR.name(), "手动对账-机构垫付失败单");
                        executor.confirm(t6501.F01, params);
                        isNoT6501(connection, t6501.F01, "手动对账-提前还款成功");
                        break;
                    case "DQR":
                        // 第三方成功，平台未更新
                        executor.confirm(t6501.F01, params);
                        logger.info("手动对账机构垫付-确认成功ID:" + t6501.F01);
                        isNoT6501(connection, t6501.F01, "手动对账-机构垫付成功");
                        /**
                         * 垫付合同保全
                         */
                        handlerAdvance(t6501.F01, params, serviceSession, request);
                        break;
                    case "CG":
                        updateT6501(connection, T6501_F11.S.name(), t6501.F01, T6501_F03.CG.name(), "确认,机构垫付成功");
                        break;
                    case "SB":
                        updateT6501(connection, T6501_F11.F.name(), t6501.F01, T6501_F03.DQR.name(), "手动对账-机构垫付第三方成功");
                        executor.confirm(t6501.F01, params);
                        isNoT6501(connection, t6501.F01, "手动对账-提前还款成功");
                        break;
                }
            }
            else
            {
                switch (t6501.F03.name())
                {
                    case "DTJ":
                        executor.submit(t6501.F01, params);
                        if (!"true".equals(params.get("success")))
                        {
                            logger.info("手动对账-机构垫付失败ID:" + t6501.F01);
                            updateT6501(connection,
                                T6501_F11.F.name(),
                                t6501.F01,
                                T6501_F03.SB.name(),
                                params.get("msg"));
                            break;
                        }
                        logger.info("手动对账提前还款-成功ID:" + t6501.F01);
                        executor.confirm(t6501.F01, params);
                        updateT6501(connection, T6501_F11.S.name(), t6501.F01, T6501_F03.CG.name(), "手动对账-机构垫付成功");
                        /**
                         * 垫付合同保全
                         */
                        handlerAdvance(t6501.F01, params, serviceSession, request);
                        break;
                    case "SB":
                        updateT6501(connection, T6501_F11.F.name(), t6501.F01, T6501_F03.DTJ.name(), "手动对账-重新机构垫付");
                        executor.submit(t6501.F01, params);
                        if (!"true".equals(params.get("success")))
                        {
                            logger.info("手动对账-机构垫付失败ID:" + t6501.F01);
                            updateT6501(connection,
                                T6501_F11.F.name(),
                                t6501.F01,
                                T6501_F03.SB.name(),
                                params.get("msg"));
                            break;
                        }
                        logger.info("手动对账机构垫付-成功ID:" + t6501.F01);
                        executor.confirm(t6501.F01, params);
                        isNoT6501(connection, t6501.F01, "手动对账-还款成功");
                        break;
                    case "CG":
                        updateT6501(connection,
                            T6501_F11.F.name(),
                            t6501.F01,
                            T6501_F03.CG.name(),
                            "对账异常,请与第三方对账人工核实，第三方查询无此记录");
                        break;
                }
            }
        }
    }
    
    @Override
    public void bid20005(ServiceSession serviceSession, T6501 t6501, Map<String, String> params, boolean flag,
        HttpServletRequest request)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            // 第三方是否成功
            if (flag)
            {
                switch (t6501.F03.name())
                {
                    case "CG":
                        updateT6501(connection, T6501_F11.S.name(), t6501.F01, T6501_F03.CG.name(), "确认,债权转让成功");
                        /**
                         * 债权转让合同保全
                         */
                        assignmentOfDebtAdvance(serviceSession, connection, t6501.F01, request);
                        break;
                    case "DQR":
                    case "DTJ":
                    case "SB":
                        T6507 t6507 = selectT6507(connection, t6501.F01);
                        // 第三方成功，平台未更新——作撤销处理【转账处理】
                        boolean back = selectFuyou20005(serviceSession, t6501, t6507.F01, params);
                        if (back)
                        {
                            updateT6501(connection,
                                T6501_F11.S.name(),
                                t6501.F01,
                                T6501_F03.SB.name(),
                                "手动对账-债权购买失败-第三方已退款成功");
                        }
                        else
                        { // 退款单是否已有
                            if (Boolean.parseBoolean(params.get("isNOs")))
                            {
                                params.put("mssn", t6501.F10.concat("BACK1"));
                                params.put("mssns", t6501.F10.concat("BACKF1"));
                            }
                            else
                            {
                                params.put("mssn", t6501.F10.concat("BACK"));
                                params.put("mssns", t6501.F10.concat("BACKF"));
                            }
                            params.put("out_cust_no", getSellCustId(connection, t6507.F01));
                            params.put("in_cust_no", selectT6119(connection, t6507.F03));
                            params.put("amt", getAmt(t6507.F05.subtract(t6507.F06)));
                            params.put("mchnt_amt", getAmt(t6507.F06));
                            doSubmitFyouBack(serviceSession, connection, t6501.F01, params);
                            updateT6501(connection,
                                T6501_F11.S.name(),
                                t6501.F01,
                                T6501_F03.SB.name(),
                                "手动对账-债权购买失败-第三方已退款成功");
                        }
                        break;
                }
            }
            else
            {
                switch (t6501.F03.name())
                {
                    case "DTJ":
                    case "SB":
                    case "DQR":
                        updateT6501(connection, T6501_F11.S.name(), t6501.F01, T6501_F03.SB.name(), "手动对账-债权购买失败");
                        break;
                    case "CG":
                        updateT6501(connection,
                            T6501_F11.F.name(),
                            t6501.F01,
                            T6501_F03.CG.name(),
                            "对账异常,请与第三方对账人工核实，第三方查询无此记录");
                        break;
                }
            }
        }
        
    }
    
    @Override
    public void bid50002(ServiceSession serviceSession, T6501 t6501, Map<String, String> params, boolean flag,
        DonationExecutor executor)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            // 第三方是否成功
            if (flag)
            {
                switch (t6501.F03.name())
                {
                    case "CG":
                        updateT6501(connection, T6501_F11.S.name(), t6501.F01, T6501_F03.CG.name(), "确认,捐款成功");
                        break;
                    case "DQR":
                    case "DTJ":
                    case "SB":
                        updateT6501(connection, T6501_F11.F.name(), t6501.F01, T6501_F03.DQR.name(), "第三方成功，更改为-DQR");
                        executor.confirm(t6501.F01, null);
                        isNoT6501(connection, t6501.F01, "手动对账,捐款更新成功");
                        break;
                }
            }
            else
            {
                switch (t6501.F03.name())
                {
                    case "DTJ":
                    case "SB":
                    case "DQR":
                        updateT6501(connection, T6501_F11.S.name(), t6501.F01, T6501_F03.SB.name(), "手动对账-捐款失败");
                        break;
                    case "CG":
                        updateT6501(connection,
                            T6501_F11.F.name(),
                            t6501.F01,
                            T6501_F03.CG.name(),
                            "对账异常,请与第三方对账人工核实，第三方查询无此记录");
                        break;
                }
            }
        }
        
    }
    
    /**
     * 订单是确认订单是否成功
     * <功能详细描述>
     * @param connection
     * @param orderId
     * @throws SQLException 
     */
    private void isNoT6501(Connection connection, int orderId, String mse)
        throws SQLException
    {
        T6501_F03 t6501_F03 = null;
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT F03 FROM S65.T6501 WHERE T6501.F01 = ? "))
        {
            pstmt.setInt(1, orderId);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    t6501_F03 = T6501_F03.parse(resultSet.getString(1));
                }
            }
            if (T6501_F03.CG == t6501_F03)
            {
                try (PreparedStatement pstmtc =
                    connection.prepareStatement("UPDATE S65.T6501 SET F11 = ?, F12 = ? WHERE F01 = ? "))
                {
                    pstmtc.setString(1, T6501_F11.S.name());
                    pstmtc.setString(2, mse);
                    pstmtc.setInt(3, orderId);
                    pstmtc.execute();
                }
            }
        }
    }
    
    /**
     * 债权转让合同保全
     * @param serviceSession
     * @throws Throwable 
     */
    public void assignmentOfDebtAdvance(ServiceSession serviceSession, Connection connection, int orderId,
        HttpServletRequest request)
        throws Throwable
    {
        ResourceProvider resourceProvider = ResourceProviderUtil.getResourceProvider();
        ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        //生成债权转让合同PDF并保全
        if (Boolean.parseBoolean(configureProvider.getProperty(SystemVariable.IS_SAVE_TRANSFER_CONTRACT)))
        {
            try
            {
                T6507 t6507 = selectT6507(connection, orderId);
                ContractManage manage = serviceSession.getService(ContractManage.class);
                List<T6271> t6271List = manage.getClaimList(t6507.F02);
                if (null != t6271List)
                {
                    String charset = resourceProvider.getCharset();
                    PdfFormationExecutor cpfe = resourceProvider.getResource(PdfFormationExecutor.class);
                    /*调用第三方合同保全执行器*/
                    PreservationExecutor preservationExecutor =
                        resourceProvider.getResource(PreservationExecutor.class);
                    Map<String, Object> valueMap = null;
                    StringBuffer sb = new StringBuffer();
                    StringBuffer sbs = new StringBuffer();
                    for (T6271 t6271 : t6271List)
                    {
                        valueMap = manage.getClaimContentMap(t6507.F02, t6271.F02);
                        if (null != valueMap)
                        {
                            sb.setLength(0);
                            sb.append(configureProvider.getProperty(SystemVariable.CONTRACT_TEMPLATE_HTML_HEADER));
                            sb.append((String)valueMap.get("dzxy_content"));
                            sb.append(configureProvider.getProperty(SystemVariable.CONTRACT_TEMPLATE_HTML_FOOTER));
                            String path =
                                cpfe.createHTML(valueMap,
                                    "contract",
                                    (String)valueMap.get("dzxy_xymc"),
                                    sb.toString(),
                                    charset,
                                    (String)valueMap.get("xy_no"));
                            if (!StringHelper.isEmpty(path))
                            {
                                sbs.setLength(0);
                                sbs.append(configureProvider.getProperty(SystemVariable.SITE_REQUEST_PROTOCOL))
                                    .append(configureProvider.getProperty(SystemVariable.SITE_DOMAIN))
                                    .append(request.getContextPath())
                                    .append("/");
                                String pdfPath = cpfe.convertHtml2Pdf(path, sbs.toString(), charset);
                                t6271.F04 = (String)valueMap.get("xy_no");
                                t6271.F09 = pdfPath;
                                manage.updateT6271PdfPathNo(t6271);
                                logger.info("生成pdf合同文档成功！");
                                preservationExecutor.contractPreservation(t6271.F01);
                            }
                        }
                    }
                }
            }
            catch (Exception e)
            {
                logger.error("Zqzr.processPost()", e);
            }
        }
    }
    
    /**
     * 垫付保全
     * @param orderId
     * @param bidQueryEntity
     * @param params
     * @param configureProvider
     * @param serviceSession
     * @param request
     * @throws Throwable
     */
    private void handlerAdvance(int orderId, Map<String, String> params, ServiceSession serviceSession,
        HttpServletRequest request)
        throws Throwable
    {
        
        ResourceProvider resourceProvider = ResourceProviderUtil.getResourceProvider();
        ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        //生成债权转让合同PDF并保全
        if (Boolean.parseBoolean(configureProvider.getProperty(SystemVariable.IS_SAVE_ADVANCE_CONTRACT)))
        {
            ContractManage cManage = serviceSession.getService(ContractManage.class);
            BidManage bidManage = serviceSession.getService(BidManage.class);
            T6514 t6514 = bidManage.selectT6514(orderId);
            
            if (t6514 == null)
            {
                logger.info("垫付记录为空！");
                return;
            }
            List<T6271> t6271List = cManage.getDfList(t6514.F02);
            if (null != t6271List && t6271List.size() > 0)
            {
                StringBuffer sbContract = new StringBuffer();
                StringBuffer sbContractSave = new StringBuffer();
                
                int userId = t6514.F04;
                ContractManage contractMng = serviceSession.getService(ContractManage.class);
                Map<String, Object> valueMap = contractMng.getAdvanceContentMap(t6514.F02, userId);
                if (null != valueMap)
                {
                    sbContract.setLength(0);
                    sbContract.append(configureProvider.getProperty(SystemVariable.CONTRACT_TEMPLATE_HTML_HEADER));
                    sbContract.append((String)valueMap.get("dzxy_content"));
                    sbContract.append(configureProvider.getProperty(SystemVariable.CONTRACT_TEMPLATE_HTML_FOOTER));
                    PdfFormationExecutor cpfe = resourceProvider.getResource(PdfFormationExecutor.class);
                    String charset = resourceProvider.getCharset();
                    sbContractSave.setLength(0);
                    sbContractSave.append(configureProvider.getProperty(SystemVariable.SITE_REQUEST_PROTOCOL))
                        .append(configureProvider.getProperty(SystemVariable.SITE_DOMAIN))
                        .append(request.getContextPath())
                        .append("/");
                    PreservationExecutor preservationExecutor =
                        resourceProvider.getResource(PreservationExecutor.class);
                    for (T6271 t6271 : t6271List)
                    {
                        if (T6271_F07.YBQ.equals(t6271.F07))
                        {
                            logger.info("该记录已保全，循环执行未保全的订单！");
                            continue;
                        }
                        String dfrPath =
                            cpfe.createHTML(valueMap,
                                "contract",
                                (String)valueMap.get("dzxy_xymc"),
                                sbContract.toString(),
                                charset,
                                (String)valueMap.get("xy_no"));
                        if (!StringHelper.isEmpty(dfrPath))
                        {
                            String dfrContractPath = cpfe.convertHtml2Pdf(dfrPath, sbContractSave.toString(), charset);
                            T6271 dfrT6271 = new T6271();
                            dfrT6271.F01 = t6271.F01;
                            dfrT6271.F04 = (String)valueMap.get("xy_no");
                            dfrT6271.F09 = dfrContractPath;
                            contractMng.updateT6271PdfPathNo(dfrT6271);
                            logger.info("生成垫付pdf合同文档成功！");/*调用第三方合同保全执行器*/
                            preservationExecutor.contractPreservation(dfrT6271.F01);
                        }
                    }
                }
            }
        }
        
    }
    
    /**
     * 不良债权转让生成合同保全
     * @param request
     * @param configureProvider
     * @param serviceSession
     * @param id
     * @throws Throwable 
     */
    public void saveAdvanceContract(HttpServletRequest request, ServiceSession serviceSession, int id)
        throws Throwable
    {
        ResourceProvider resourceProvider = ResourceProviderUtil.getResourceProvider();
        ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        //生成不良债权转让合同PDF并保全
        if (Boolean.parseBoolean(configureProvider.getProperty(SystemVariable.IS_SAVE_ADVANCE_CONTRACT)))
        {
            int blzqId = IntegerParser.parse(id);
            ContractManage contractManage = serviceSession.getService(ContractManage.class);
            T6265 t6265 = contractManage.selectT6265(blzqId);
            if (t6265 != null)
            {
                Dzxy dzxy = contractManage.getBlzqzr(t6265.F01, 0, "");
                if (dzxy != null)
                {
                    int blzqzrId = t6265.F01;
                    //执行合同保全线程
                    BadClaimContractPreservationThread bccpThread = new BadClaimContractPreservationThread(blzqzrId);
                    new Thread(bccpThread).start();
                }
            }
        }
    }
    
    /**
     * 更新失败 回滚
     * <债权转用>
     * @param connection
     * @param orderId
     * @throws Throwable 
     */
    private void doSubmitFyouBack(ServiceSession serviceSession, Connection connection, int orderId, Map<String, String> params) throws Throwable {
        if (params.get("in_cust_no").equals(params.get("out_cust_no"))) {
            throw new LogicalException("自己不能购买自己转让的债权!");
        }
        
        TransferManage manage = serviceSession.getService(TransferManage.class);
        Map<String, Object> map = manage.createTransferMap(
                params.get("mssn"), params.get("in_cust_no"), params.get("out_cust_no"), params.get("amt"), "");
        if (map != null && (FuyouRespCode.JYCG.getRespCode().equals(map.get("resp_code")) || FuyouRespCode.LSCF.getRespCode().equals(map.get("resp_code")))) {
            // 手续退还
            doSubmitFyouBackFee(connection, orderId, params, serviceSession);
            logger.info("债权购买失败后退单-成功-订单：" + orderId);
        } else {
            logger.info("债权购买失败后退单,失败");
            throw new LogicalException("债权购买失败后退单，失败!");
        }
    }
    
    /**
     * 更新失败 回滚 手续费
     * <债权转用>
     * @param connection
     * @param orderId
     * @throws Throwable 
     */
    private void doSubmitFyouBackFee(Connection connection, int orderId, Map<String, String> params, ServiceSession serviceSession) throws Throwable {
    	TransferManage manage = serviceSession.getService(TransferManage.class);
        Map<String, Object> map = manage.createTransferMap(params.get("mssns"),
                configureProvider.format(FuyouVariable.FUYOU_P2P_ACCOUNT_NAME),
                params.get("out_cust_no"), params.get("mchnt_amt"), "transferBmu");
        if (map != null && FuyouRespCode.JYCG.getRespCode().equals(map.get("resp_code"))) {
            logger.info("债权购买失败后退单-成功还手续费-订单：" + orderId);
        } else {
            logger.info("债权购买失败后退单-手续费,失败");
            throw new LogicalException("债权购买失败后退单-手续费，失败!");
        }
    }
    
    /**
     * 查询债权转让订单
     * @param connection
     * @param F01
     * @return
     * @throws SQLException
     */
    private T6507 selectT6507(Connection connection, int F01)
        throws SQLException
    {
        T6507 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06 FROM S65.T6507 WHERE T6507.F01 = ? FOR UPDATE"))
        {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6507();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getInt(3);
                    record.F04 = resultSet.getBigDecimal(4);
                    record.F05 = resultSet.getBigDecimal(5);
                    record.F06 = resultSet.getBigDecimal(6);
                }
            }
        }
        return record;
    }
    
    /**
     * 查询不良债权购买订单
     * @param connection
     * @param F01
     * @return
     * @throws SQLException
     */
    private T6529 selectT6529(Connection connection, int F01)
        throws SQLException
    {
        T6529 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08 FROM S65.T6529 WHERE T6529.F01 = ? FOR UPDATE"))
        {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6529();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getInt(3);
                    record.F04 = resultSet.getInt(4);
                    record.F05 = resultSet.getInt(5);
                    record.F06 = resultSet.getBigDecimal(6);
                    record.F07 = resultSet.getInt(7);
                    record.F08 = T6529_F08.parse(resultSet.getString(8));
                }
            }
        }
        return record;
    }
    
    /** 
     * 查询标的债权记录
     * @param connection
     * @param F01
     * @return
     * @throws SQLException
     */
    protected T6251 selectT6251(Connection connection, int F01)
        throws SQLException
    {
        T6251 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11 FROM S62.T6251 WHERE T6251.F01 = ? FOR UPDATE"))
        {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6251();
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
                    record.F11 = resultSet.getInt(11);
                }
            }
        }
        return record;
    }
    
    /**
     * 查询是否已退款
     * @param serviceSession
     * @param t6501
     * @param t6507
     * @param params
     * @return
     * @throws Throwable
     */
    public boolean selectFuyou20005(ServiceSession serviceSession, T6501 t6501, int F01, Map<String, String> params)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            //调用第三方平台
            TransactionQueryResponseEntity result =
                TransactionQueryFace.executeTransactionQuery(configureProvider.format(FuyouVariable.FUYOU_ACCOUNT_ID),
                    MchntTxnSsn.getMts(FuyouTypeEnum.JYCX.name()),
                    FuyouEnum.PW03.name(),
                    new SimpleDateFormat("yyyyMMdd").format(t6501.F04),
                    new SimpleDateFormat("yyyyMMdd").format(t6501.F04),
                    t6501.F10.concat("BACK"),
                    getSellCustId(connection, F01),
                    "",
                    "",
                    "",
                    "",
                    configureProvider.format(FuyouVariable.FUYOU_TRADINGQUERY_URL),
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
                        if (!StringHelper.isEmpty(serialNumStr) && serialNumStr.equals(t6501.F10.concat("BACK")))
                        {
                            if (FuyouRespCode.JYCG.getRespCode().equals(detail.getTxnRspCd()))
                            {
                                return true;
                            }
                            else
                            {
                                params.put("isNOs", "true");
                                return false;
                            }
                        }
                    }
                }
            }
            else
            {
                throw new LogicalException("对账异常!");
            }
        }
        params.put("isNOs", "false");
        return false;
    }
    
    /**
     * 债权人第三方账号
     * <功能详细描述>
     * @param orderId
     * @return
     * @throws Throwable
     */
    public String getSellCustId(Connection connection, int orderId)
        throws Throwable
    {
        // 获取用户商户号
        StringBuffer sb = new StringBuffer("SELECT T6119.F03 FROM S65.T6507 ");
        sb.append(" LEFT JOIN S62.T6260 ON T6260.F01=T6507.F02");
        sb.append(" LEFT JOIN S62.T6251 ON T6251.F01=T6260.F02");
        sb.append(" LEFT JOIN S61.T6119 ON T6119.F01=T6251.F04");
        sb.append(" WHERE T6507.F01 = ? LIMIT 1 ");
        try (PreparedStatement pstmt = connection.prepareStatement(sb.toString()))
        {
            pstmt.setInt(1, orderId);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    return resultSet.getString(1);
                }
            }
        }
        return "0";
    }
    
    @Override
    public void updateT6501TxSb(int orderId, String F12)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
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
                t6130.F09 = T6130_F09.TXSB;
                // 插入提现申请
                insertT6130(connection, t6130, adminId);
                
                try (PreparedStatement pstmt =
                    connection.prepareStatement("UPDATE S65.T6501 SET F03 = ?, F11 = ?, F12 = ? WHERE F01 = ? "))
                {
                    pstmt.setString(1, T6501_F03.SB.name());
                    pstmt.setString(2, T6501_F11.S.name());
                    pstmt.setString(3, F12);
                    pstmt.setInt(4, orderId);
                    pstmt.execute();
                }
                
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    private void insertT6130(Connection connection, T6130 entity, int accountId)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S61.T6130 SET F02 = ?, F03 = ?, F04 = ?, F06 = ?, F07 = ?, F08 = CURRENT_TIMESTAMP(), F09 = ?, F13=?, F14 = CURRENT_TIMESTAMP(), F15 = ?,F17 = ?,F18 = ? "))
        {
            pstmt.setInt(1, entity.F02);
            pstmt.setInt(2, entity.F03);
            pstmt.setBigDecimal(3, entity.F04);
            pstmt.setBigDecimal(4, entity.F06);
            pstmt.setBigDecimal(5, entity.F07);
            pstmt.setString(6, entity.F09.name());
            pstmt.setInt(7, accountId);
            pstmt.setString(8, "订单未提交第三方");
            Map<String, Object> result = getEmpInfo(accountId, connection);
            pstmt.setString(9, (String)result.get("empNum"));
            pstmt.setString(10, (String)result.get("empStatus"));
            pstmt.execute();
        }
    }
    
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
    
    @Override
    public void bid50003(ServiceSession serviceSession, T6501 t6501, Map<String, String> params, boolean flag,
        MallChangeExecutor executor)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            // 第三方是否成功
            if (flag)
            {
                switch (t6501.F03.name())
                {
                    case "CG":
                        updateT6501(connection, T6501_F11.S.name(), t6501.F01, T6501_F03.CG.name(), "确认,商品购买成功！");
                        break;
                    case "DQR":
                    case "DTJ":
                    case "SB":
                        updateT6501(connection, T6501_F11.F.name(), t6501.F01, T6501_F03.DQR.name(), "第三方成功，更改为-DQR！");
                        executor.confirm(t6501.F01, null);
                        isNoT6501(connection, t6501.F01, "手动对账,商品购买更新成功！");
                        break;
                }
            }
            else
            {
                switch (t6501.F03.name())
                {
                    case "DTJ":
                    case "SB":
                    case "DQR":
                        updateT6501(connection, T6501_F11.S.name(), t6501.F01, T6501_F03.SB.name(), "手动对账-商品购买失败！");
                        break;
                    case "CG":
                        updateT6501(connection,
                            T6501_F11.F.name(),
                            t6501.F01,
                            T6501_F03.CG.name(),
                            "对账异常,请与第三方对账人工核实，第三方查询无此记录！");
                        break;
                }
            }
        }
    }
    
    @Override
    public void bid50004(ServiceSession serviceSession, T6501 t6501, Map<String, String> params, boolean flag,
        MallRefundExecutor executor)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            // 第三方是否成功
            if (flag)
            {
                switch (t6501.F03.name())
                {
                    case "CG":
                        updateT6501(connection, T6501_F11.S.name(), t6501.F01, T6501_F03.CG.name(), "确认,商品退款成功！");
                        break;
                    case "DQR":
                    case "DTJ":
                    case "SB":
                        updateT6501(connection, T6501_F11.F.name(), t6501.F01, T6501_F03.DQR.name(), "第三方成功，更改为-DQR！");
                        executor.confirm(t6501.F01, null);
                        isNoT6501(connection, t6501.F01, "手动对账,商品退款更新成功！");
                        break;
                }
            }
            else
            {
                switch (t6501.F03.name())
                {
                    case "DTJ":
                    case "SB":
                    case "DQR":
                        updateT6501(connection, T6501_F11.S.name(), t6501.F01, T6501_F03.SB.name(), "手动对账-商品退款失败！");
                        break;
                    case "CG":
                        updateT6501(connection,
                            T6501_F11.F.name(),
                            t6501.F01,
                            T6501_F03.CG.name(),
                            "对账异常,请与第三方对账人工核实，第三方查询无此记录！");
                        break;
                }
            }
        }
    }
    
    @Override
    public String bid20015(ServiceSession serviceSession, T6501 t6501, Map<String, String> params, boolean flag)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            // 第三方是否成功
            if (flag)
            {
                switch (t6501.F03.name())
                {
                    case "CG":
                        updateT6501(connection, T6501_F11.S.name(), t6501.F01, T6501_F03.CG.name(), "确认,不良债权购买成功");
                        break;
                    case "DQR":
                    case "DTJ":
                    case "SB":
                        T6529 t6529 = selectT6529(connection, t6501.F01);
                        T6251 t6251 = selectT6251(connection, t6529.F05);
                        // 第三方成功，平台未更新——作撤销处理【转账处理】
                        try
                        {
                            boolean back = selectFuyou20005(serviceSession, t6501, t6529.F01, params);
                            if (back)
                            {
                                updateT6501(connection,
                                    T6501_F11.S.name(),
                                    t6501.F01,
                                    T6501_F03.SB.name(),
                                    "手动对账-不良债权购买失败-第三方已退款成功");
                            }
                            else
                            { // 退款单是否已有
                                if (Boolean.parseBoolean(params.get("isNOs")))
                                {
                                    params.put("mssn", t6501.F10.concat("BACK1"));
                                    params.put("mssns", t6501.F10.concat("BACKF1"));
                                }
                                else
                                {
                                    params.put("mssn", t6501.F10.concat("BACK"));
                                    params.put("mssns", t6501.F10.concat("BACKF"));
                                }
                                params.put("out_cust_no", selectT6119(connection, t6529.F04));
                                params.put("in_cust_no", selectT6119(connection, t6251.F04));
                                params.put("amt", getAmt(t6529.F06));
                                params.put("mchnt_amt", "0");
                                doSubmitFyouBack(serviceSession, connection, t6501.F01, params);
                                updateT6501(connection,
                                    T6501_F11.S.name(),
                                    t6501.F01,
                                    T6501_F03.SB.name(),
                                    "手动对账-不良债权购买失败-第三方已退款成功");
                            }
                        }
                        catch (Exception e)
                        {
                            updateT6501(connection,
                                T6501_F11.S.name(),
                                t6501.F01,
                                T6501_F03.SB.name(),
                                "手动对账-不良债权购买失败-第三方失败");
                            return "对账失败";
                        }
                        break;
                }
            }
            else
            {
                switch (t6501.F03.name())
                {
                    case "DTJ":
                    case "SB":
                    case "DQR":
                        updateT6501(connection, T6501_F11.S.name(), t6501.F01, T6501_F03.SB.name(), "手动对账-不良债权购买失败");
                        break;
                    case "CG":
                        updateT6501(connection,
                            T6501_F11.F.name(),
                            t6501.F01,
                            T6501_F03.CG.name(),
                            "对账异常,请与第三方对账人工核实，第三方查询无此记录");
                        break;
                }
            }
        }
        return "SUCCESS";
        
    }
    
}
