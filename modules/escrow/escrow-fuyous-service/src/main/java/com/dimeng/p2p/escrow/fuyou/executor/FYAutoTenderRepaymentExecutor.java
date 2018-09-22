package com.dimeng.p2p.escrow.fuyou.executor;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.data.sql.SQLConnection;
import com.dimeng.framework.resource.AchieveVersion;
import com.dimeng.framework.resource.ResourceAnnotation;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.OrderType;
import com.dimeng.p2p.S62.entities.T6230;
import com.dimeng.p2p.S62.entities.T6238;
import com.dimeng.p2p.S62.entities.T6253;
import com.dimeng.p2p.S62.enums.T6230_F10;
import com.dimeng.p2p.S62.enums.T6230_F11;
import com.dimeng.p2p.S65.entities.T6501;
import com.dimeng.p2p.S65.entities.T6506;
import com.dimeng.p2p.S65.entities.T6517;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.S65.enums.T6501_F07;
import com.dimeng.p2p.S65.enums.T6501_F11;
import com.dimeng.p2p.escrow.fuyou.entity.TransactionQueryDetailedEntity;
import com.dimeng.p2p.escrow.fuyou.entity.TransactionQueryResponseEntity;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouEnum;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouRespCode;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouTypeEnum;
import com.dimeng.p2p.escrow.fuyou.face.TransactionQueryFace;
import com.dimeng.p2p.escrow.fuyou.service.TransferManage;
import com.dimeng.p2p.escrow.fuyou.util.BackCodeInfo;
import com.dimeng.p2p.escrow.fuyou.util.MchntTxnSsn;
import com.dimeng.p2p.escrow.fuyou.util.PayUtil;
import com.dimeng.p2p.escrow.fuyou.variables.FuyouVariable;
import com.dimeng.p2p.order.AutoTenderRepaymentExecutor;
import com.dimeng.util.StringHelper;

/**
 * 
 * 自动还款
 * <富友托管>
 * 
 * @author  heshiping
 * @version  [版本号, 2015年12月16日]
 */
@AchieveVersion(version = 20151216)
@ResourceAnnotation
public class FYAutoTenderRepaymentExecutor extends AutoTenderRepaymentExecutor {
    
    public FYAutoTenderRepaymentExecutor(ResourceProvider resourceProvider) {
        super(resourceProvider);
    }
    
    @Override
    protected void callFace(SQLConnection connection, int orderId, Map<String, String> params) throws SQLException {
        ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        ServiceProvider serviceProvider = resourceProvider.getResource(ServiceProvider.class);
        try (ServiceSession serviceSession = serviceProvider.createServiceSession()) {
            T6501 t6501 = selectT6501(connection, orderId);
            String czzh = getUserCustId(connection, t6501.F08);
            T6506 t6506 = selectT6506(connection, orderId);
            String PY = "YH";
            if (t6506.F02 == getPTID(connection)) {
                PY = "PT";
            }
            params.put("PY", PY);
            String busi_tp = FuyouEnum.PW03.name();
            if ("PT".equals(PY)) {
                busi_tp = FuyouEnum.PWPC.name();
            }
            if (!queryFuyou(configureProvider, serviceSession, connection, czzh, t6501, busi_tp, params)) {
                params.put("mchnt_txn_ssn", t6501.F10);
                T6230 t6230 = selectT6230F(connection, t6506.F03);
                if (!doFuyou(configureProvider, serviceProvider, connection, t6506, t6230, params)) {
                    logger.info("自动还款失败");
                    throw new LogicalException("自动还款失败!");
                }
            }
        } catch (Throwable e){
            logger.error("自动还款失败", e);
            throw new LogicalException("自动还款失败!");
        }
    }
    
    /**
     * 自动还款
     * <调用接口>
     * @param connection
     * @param t6506 还款订单
     * @param t6230 标信息
     * @param t6252 还款信息
     * @param params
     * @returnF
     * @throws SQLException
     * @throws IOException
     */
    private boolean doFuyou(ConfigureProvider configureProvider, ServiceProvider serviceProvider, SQLConnection connection, T6506 t6506, T6230 t6230, Map<String, String> params) throws SQLException, IOException {
        // 出账账号(借款人账户)
        String out_cust_no = getUserCustId(connection, t6230.F02);
        String in_cust_no;
        String contract_no = "";
        if ("PT".equals(params.get("PY"))) {
            in_cust_no = configureProvider.format(FuyouVariable.FUYOU_P2P_ACCOUNT_NAME);
            contract_no = "transferBmu";
        } else {
            in_cust_no = getUserCustId(connection, t6506.F02);
        }
        if (StringHelper.isEmpty(in_cust_no)) {
            throw new LogicalException("收款第三方账号查询有误！");
        }
        //流水号
        String mchnt_txn_ssn = params.get("mchnt_txn_ssn");
        BigDecimal fee = BigDecimal.ZERO;
        // 利息 | 逾期罚息 ~收取投资管理费
        if (t6506.F07 == FeeCode.TZ_LX || t6506.F07 == FeeCode.TZ_FX) {
            T6253 t6253 = selectT6253(connection, t6230.F01);
            // 垫付后的 逾期罚息 平台不作收取，全给垫付机构
            if (t6253 == null) {
                // 计算投资管理费
                T6238 t6238 = selectT6238(connection, t6230.F01);
                fee = t6506.F06.multiply(t6238.F03).setScale(2, BigDecimal.ROUND_HALF_UP);
            }
        }
        // 还金
        String amt = PayUtil.getAmt(t6506.F06.subtract(fee));
        // 把借款者的钱划拨到投资者的账户上去(包括投资管理费)
        try (ServiceSession serviceSession = serviceProvider.createServiceSession()) {
        	TransferManage manage = serviceSession.getService(TransferManage.class);
            Map<String, Object> map = manage.createTransferMap(mchnt_txn_ssn, out_cust_no, in_cust_no, amt, contract_no);
            // 验签通过。
            if (map != null && FuyouRespCode.JYCG.getRespCode().equals(map.get("resp_code"))) {
                params.put("success", "true");
                logger.info("自动还款时，划拨成功。");
                if (fee.compareTo(BigDecimal.ZERO) > 0) { 
                    // 插入转账订单
                    T6501 zzt6501 = new T6501();
                    zzt6501.F02 = OrderType.TRANSFER.orderType();
                    zzt6501.F03 = T6501_F03.DTJ;
                    zzt6501.F07 = T6501_F07.XT;
                    zzt6501.F08 = t6230.F02;
                    zzt6501.F10 = MchntTxnSsn.getMts(FuyouTypeEnum.GLF.name());
                    zzt6501.F12 = String.format("散标自动还款投资管理费:%s，标题：%s", t6230.F25, t6230.F03);
                    zzt6501.F04 = getCurrentTimestamp(connection);
                    zzt6501.F13 = fee;
                    int ordId = insertT6501(connection, zzt6501);
                    T6517 t6517 = new T6517();
                    t6517.F01 = ordId;
                    t6517.F02 = fee;
                    t6517.F03 = t6230.F02;
                    t6517.F04 = getPTID(connection);
                    t6517.F05 = String.format("散标自动还款投资管理费:%s，标题：%s", t6230.F25, t6230.F03);
                    t6517.F06 = FeeCode.GLF;
                    insertT6517(connection, t6517);
                }
                return true;
            } else {
            	this.updateT6252(connection, t6506, "WH");
                params.put("success", "false");
                String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
                logger.info("自动还款时，划拨失败。 还款人ID：" + t6230.F02 + "，投资人ID：" + t6506.F02 + ",还款金额：" + t6506.F06 + ",还款时间："  + time + "备注" + BackCodeInfo.info(map.get("resp_code").toString()));
            }
        } catch (Throwable e) {
            logger.error(e);
        }
        return false;        
    }
    
    protected int insertT6501(Connection connection, T6501 entity) throws Throwable {
        int orderId = 0;
        StringBuilder sql = new StringBuilder("INSERT INTO S65.T6501 SET F02 = ?,F03 = ?,F04 = ?,F05 = ?,F06 = ?,F07 = ?,F08 = ?,F10 = ?,F11 = ?,F12 = ?,F13 = ?");
        if (entity.F09 != null) {
            sql.append(",F09 = ?");
        }
        try (PreparedStatement pstmt = connection.prepareStatement(sql.toString(), PreparedStatement.RETURN_GENERATED_KEYS)) {
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
            if (entity.F09 != null) {
                pstmt.setInt(12, entity.F09);
            }
            pstmt.execute();
            try (ResultSet resultSet = pstmt.getGeneratedKeys()) {
                if (resultSet.next()) {
                    orderId = resultSet.getInt(1);
                }
            }
        }
        if (orderId == 0)  {
            logger.error("TenderConfirmExecutor.insertT6501():数据库异常");
            throw new SQLException("数据库异常");
        }
        return orderId;
    }
    
    protected void insertT6517(Connection connection, T6517 t6517) throws Throwable {
        try (PreparedStatement ps =
            connection.prepareStatement("INSERT INTO S65.T6517 SET F01 = ?, F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?")) {
            ps.setInt(1, t6517.F01);
            ps.setBigDecimal(2, t6517.F02);
            ps.setInt(3, t6517.F03);
            ps.setInt(4, t6517.F04);
            ps.setString(5, t6517.F05);
            ps.setInt(6, t6517.F06);
            ps.execute();
        }
    }
    
    private T6230 selectT6230F(Connection connection, int F01) throws SQLException {
        T6230 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11, F25 FROM S62.T6230 WHERE T6230.F01 = ? ")) {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
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
                    record.F25 = resultSet.getString(12);
                }
            }
        }
        return record;
    }
    
    /**
     * 对账
     * <功能详细描述>
     * @param connection
     * @param czzh 出款人账号
     * @param t6501 旧订单
     * @param busi_tp 查询类型
     * @param params
     * @return
     * @throws Throwable
     */
    private boolean queryFuyou(ConfigureProvider configureProvider, ServiceSession serviceSession, SQLConnection connection, String czzh, T6501 t6501, String busi_tp, Map<String, String> params) throws Throwable {
        logger.info("自动还款-自动还款对账 ~~~");
        //起始时间
        String start_day = new SimpleDateFormat("yyyyMMdd").format(t6501.F04);
        // 结束时间
        String end_day;
        if (t6501.F05 != null) {
            end_day = new SimpleDateFormat("yyyyMMdd").format(t6501.F05);
        } else {
            end_day = new SimpleDateFormat("yyyyMMdd").format(t6501.F04);
        }
        
        //调用第三方平台
        TransactionQueryResponseEntity result =
            TransactionQueryFace.executeTransactionQuery(configureProvider.format(FuyouVariable.FUYOU_ACCOUNT_ID),
                MchntTxnSsn.getMts(FuyouTypeEnum.JYCX.name()),
                busi_tp,
                start_day,
                end_day,
                t6501.F10,
                czzh,
                "",
                "",
                "",
                "",
                configureProvider.format(FuyouVariable.FUYOU_TRADINGQUERY_URL),
                serviceSession);
        // 定义返回结果详情对象
        List<TransactionQueryDetailedEntity> respDetailList = null;
        if (FuyouRespCode.JYCG.getRespCode().equals(result.respCode)) {
            //结果详情
            respDetailList = result.detailedEntity;
            if (respDetailList != null && respDetailList.size() > 0) {
                for (TransactionQueryDetailedEntity detail : respDetailList) {
                    // 获取流水号
                    String serialNumStr = detail.getMchntSsn();
                    // 通过流水号 判断该笔交易是不是 要查询的那笔交易
                    if (!StringHelper.isEmpty(serialNumStr) && serialNumStr.equals(t6501.F10)) {
                        if (FuyouRespCode.JYCG.getRespCode().equals(detail.getTxnRspCd())) {
                            return true;
                        } else if ("5002".equals(detail.getTxnRspCd())) {
                            throw new LogicalException("对账验签失败!");
                        } else {
                            serialNumStr = MchntTxnSsn.getMts(FuyouTypeEnum.NEW.name());
                            params.put("mchnt_txn_ssn", serialNumStr);
                            updateT6501(connection, serialNumStr, t6501.F01);
                        }
                    }
                }
            }
        }
        return false;
    }
    
    protected void updateT6252(Connection connection, T6506 t6506, String status) throws Throwable {
        try (PreparedStatement ps = connection.prepareStatement("UPDATE S62.T6252 SET F09= ? WHERE F02 = ? AND F04 = ? AND F05 = ? AND F06 = ? AND F11 = ?")) {
        	ps.setString(1, status);
            ps.setInt(2, t6506.F03);
            ps.setInt(3, t6506.F02);
            ps.setInt(4, t6506.F07);
            ps.setInt(5, t6506.F05);
            ps.setInt(6, t6506.F04);
            ps.executeUpdate();
        }
    }
    
}
