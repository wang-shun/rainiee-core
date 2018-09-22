package com.dimeng.p2p.escrow.fuyou.executor;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.dimeng.framework.data.sql.SQLConnection;
import com.dimeng.framework.resource.AchieveVersion;
import com.dimeng.framework.resource.ResourceAnnotation;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.S65.entities.T6504;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouRespCode;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouTypeEnum;
import com.dimeng.p2p.escrow.fuyou.service.BidFaceManage;
import com.dimeng.p2p.escrow.fuyou.util.MchntTxnSsn;
import com.dimeng.p2p.escrow.fuyou.util.PayUtil;
import com.dimeng.p2p.order.AutoTenderOrderExecutor;

/**
 * 
 * 自动投资执行器
 * <优化-20160129>
 * 
 * @author  heshiping
 * @version  [版本号, 2015年12月15日]
 */
@AchieveVersion(version = 20151215)
@ResourceAnnotation
public class FYAutoTenderOrderExecutor extends AutoTenderOrderExecutor {
    
    public FYAutoTenderOrderExecutor(ResourceProvider resourceProvider) {
        super(resourceProvider);
    }
    
    @Override
    protected void doConfirm(SQLConnection connection, int orderId, Map<String, String> params) throws Throwable {
        params = new HashMap<String, String>();
        String MCHNT_TXN_SSN = MchntTxnSsn.getMts(FuyouTypeEnum.ZDTB.name());
        params.put("trxId", MCHNT_TXN_SSN);
        updateT6501(connection, MCHNT_TXN_SSN, orderId);
        super.doConfirm(connection, orderId, params);
    }
    
    /**
     * {@inheritDoc}
     * 接口调用
     */
    @Override
    protected void callFace(SQLConnection connection, int orderId, Map<String, String> params) throws SQLException {
        // 订单详情
        T6504 t6504 = selectT6504(connection, orderId);
        ServiceProvider serviceProvider = resourceProvider.getResource(ServiceProvider.class);
        try (ServiceSession serviceSession = serviceProvider.createServiceSession()) {
            // 商户代码
            String OUT_CUST_NO = getUserCustId(connection, t6504.F02);
            // 借款人ID
            String IN_CUST_NO = getUserCustId(connection, selectT6230s(connection, t6504.F03));
            logger.info("订单：" + orderId + "自动投资金额=" + t6504.F04);
            // 调用预授权接口进行投资操作
            BidFaceManage manage = serviceSession.getService(BidFaceManage.class);
            Map<String, Object> map = manage.createBidFace(params.get("trxId"), OUT_CUST_NO, IN_CUST_NO, PayUtil.getAmt(t6504.F04));
            if (map != null && (FuyouRespCode.JYCG.getRespCode().equals(map.get("resp_code")))) {
                String contract_no = map.get("contract_no").toString();
                logger.info("向富友请求自动投资成功" + contract_no);
                params.put("contract_no", contract_no);
                params.put("out_cust_no", OUT_CUST_NO);
                params.put("in_cust_no", IN_CUST_NO);
                params.put("mchnt_txn_ssn", map.get("mchnt_txn_ssn").toString());
                params.put("YN", "true");
                // 更新T6250增加合同号
                updateT6250(connection, orderId, contract_no);
            } else {
                logger.info("向第三方请求自动投资失败");
                throw new LogicalException("向第三方请求自动投资失败!");
            }
        } catch (IOException e){
            logger.error("向第三方请求自动投资失败!", e);
            throw new LogicalException("向第三方请求自动投资失败!");
        } catch (Throwable e) {
            throw new LogicalException("向第三方请求自动投资失败!", e);
        }
    }
    
    /**
     * {@inheritDoc}
     * 回滚
     */
    @Override
    protected void rollbackFace(SQLConnection connection, int orderId, Map<String, String> params) throws Throwable {
        if (!Boolean.parseBoolean(params.get("YN"))) {
            return;
        }
        ServiceProvider serviceProvider = resourceProvider.getResource(ServiceProvider.class);
        // 商户代码
        try (ServiceSession serviceSession = serviceProvider.createServiceSession()) {
            // 流标
        	BidFaceManage manage = serviceSession.getService(BidFaceManage.class);
            Map<String, Object> map = null;
            for (int i = 0; i < 3; i++) {
                map = manage.createBidCancel(MchntTxnSsn.getMts(FuyouTypeEnum.TBLB.name()),
                        params.get("out_cust_no"), params.get("in_cust_no"), params.get("contract_no"));
                if (map != null && (FuyouRespCode.JYCG.getRespCode().equals(map.get("resp_code")) || FuyouRespCode.JYWC.getRespCode().equals(map.get("resp_code")))) {
                    logger.info("预授权撤销-流标成功");
                    i = 3;
                } else {
                    logger.info("预授权撤销失败-第" + i + "次");
                }
            }
        }
    }
    
    /**
     * 投标成功增加预受权合同号
     * @param connection
     * @param F01
     * @param F10
     * @throws SQLException
     */
    private void updateT6250(SQLConnection connection, int F01, String F10) throws SQLException {
        try (PreparedStatement ps =
            connection.prepareStatement("UPDATE S62.T6250 SET F10 = ? WHERE T6250.F01 = ( SELECT T6504.F05 FROM S65.T6504 WHERE T6504.F01 = ? )")) {
            ps.setString(1, F10);
            ps.setInt(2, F01);
            ps.executeUpdate();
        }
    }
    
}
