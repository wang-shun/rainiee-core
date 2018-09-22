package com.dimeng.p2p.escrow.fuyou.executor;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Map;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.data.sql.SQLConnection;
import com.dimeng.framework.resource.Resource;
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
import com.dimeng.p2p.S65.entities.T6501;
import com.dimeng.p2p.S65.entities.T6506;
import com.dimeng.p2p.S65.entities.T6517;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.S65.enums.T6501_F07;
import com.dimeng.p2p.S65.enums.T6501_F11;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouRespCode;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouTypeEnum;
import com.dimeng.p2p.escrow.fuyou.service.TransferManage;
import com.dimeng.p2p.escrow.fuyou.util.BackCodeInfo;
import com.dimeng.p2p.escrow.fuyou.util.MchntTxnSsn;
import com.dimeng.p2p.escrow.fuyou.util.PayUtil;
import com.dimeng.p2p.escrow.fuyou.variables.FuyouVariable;
import com.dimeng.p2p.order.TenderRepaymentExecutor;
import com.dimeng.util.StringHelper;

/**
 * 
 * 还款执行类
 * <正常还款-富友托管>
 * 
 * @author  heshiping
 * @version  [版本号, 2015年11月27日]
 */
@ResourceAnnotation
public class FYRepaymentExecutor extends TenderRepaymentExecutor {
    
    public FYRepaymentExecutor(ResourceProvider resourceProvider) {
        super(resourceProvider);
    }
    
    @Override
    public Class<? extends Resource> getIdentifiedType() {
        return FYRepaymentExecutor.class;
    }
    
    @Override
    protected void doSubmit(SQLConnection connection, int orderId, Map<String, String> ps) throws Throwable {
        logger.info("还款时，转账或者划拨开始...");
        T6506 t6506 = selectT6506(connection, orderId);
        // 判断还款金额是否等于0，如果等于0就不调用接口，直接退出去
        if (t6506 != null && t6506.F06.compareTo(BigDecimal.ZERO) == 0) {
            ps.put("success", "true");
            logger.info("还款时，划拨成功 - 金额：" + t6506.F06 + " -还款记录ID：" + t6506.F01 + "-投资用户：" + t6506.F02);
            return;
        }
        // 标信息
        T6230 t6230 = selectT6230(connection, t6506.F03);
        boolean PY = false;
        if (t6506.F02 == getPTID(connection)){  
        	PY = true;
        }
        ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        ServiceProvider serviceProvider = resourceProvider.getResource(ServiceProvider.class);
        doFuyou(connection, configureProvider, serviceProvider, t6506, t6230, ps, PY);
    }
    
    /**
     * 手动还款
     * <调用接口>
     * @param connection
     * @param t6506 还款订单
     * @param t6230 标信息
     * @param t6252 还款信息
     * @param params
     * @return
     * @throws Throwable 
     */
    private void doFuyou(SQLConnection connection, ConfigureProvider configureProvider,
        ServiceProvider serviceProvider, T6506 t6506, T6230 t6230, Map<String, String> params, boolean PY) throws Throwable {
        // 出账账号(借款人账户)
        String out_cust_no = getUserCustId(connection, t6230.F02);
        String in_cust_no;
        String contract_no = "";
        if (PY) {
            in_cust_no = configureProvider.format(FuyouVariable.FUYOU_P2P_ACCOUNT_NAME);
            contract_no = "transferBmu";
        } else {
            in_cust_no = getUserCustId(connection, t6506.F02);
        }
        if (StringHelper.isEmpty(in_cust_no)) {
            throw new LogicalException("收款第三方账号查询有误！");
        }
        T6501 t6501 = selectT6501(connection, t6506.F01);
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
        // 合同号
        // 把借款者的钱划拨到投资者的账户上去(包括投资管理费)
        try (ServiceSession serviceSession = serviceProvider.createServiceSession()) {
        	TransferManage manage = serviceSession.getService(TransferManage.class);
            Map<String, Object> map = manage.createTransferMap(t6501.F10, out_cust_no, in_cust_no, amt, contract_no);
            // 验签通过。
            if (map != null && FuyouRespCode.JYCG.getRespCode().equals(map.get("resp_code"))) {
                params.put("success", "true");
                logger.info("订单号：" + t6506.F01 + "手动还款时，划拨成功。");
                if (fee.compareTo(BigDecimal.ZERO) > 0) { 
                    // 插入转账订单
                    T6501 zzt6501 = new T6501();
                    zzt6501.F02 = OrderType.TRANSFER.orderType();
                    zzt6501.F03 = T6501_F03.DTJ;
                    zzt6501.F07 = T6501_F07.XT;
                    zzt6501.F08 = t6230.F02;
                    zzt6501.F10 = MchntTxnSsn.getMts(FuyouTypeEnum.GLF.name());
                    zzt6501.F12 = String.format("散标还款投资管理费:%s，标题：%s", t6230.F25, t6230.F03);
                    zzt6501.F04 = getCurrentTimestamp(connection);
                    zzt6501.F13 = fee;
                    int ordId = insertT6501(connection, zzt6501);
                    T6517 t6517 = new T6517();
                    t6517.F01 = ordId;
                    t6517.F02 = fee;
                    t6517.F03 = t6230.F02;
                    t6517.F04 = getPTID(connection);
                    t6517.F05 = String.format("散标还款投资管理费:%s，标题：%s", t6230.F25, t6230.F03);
                    t6517.F06 = FeeCode.GLF;
                    insertT6517(connection, t6517);
                }
            } else {
            	this.updateT6252(connection, t6506, "WH");
                params.put("success", "false");
                String msg = BackCodeInfo.info(map.get("resp_code").toString());
                params.put("msg", msg);
                String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
                logger.info("手动还款时，划拨失败。 还款人ID：" + t6230.F02 + "，投资人ID：" + t6506.F02 + ",还款金额：" + t6506.F06 + ",还款时间："  + time + "备注" + msg);
            }
        } catch (Throwable e) {
            logger.error(e);
        }
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
