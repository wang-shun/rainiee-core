package com.dimeng.p2p.escrow.fuyou.executor;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.dimeng.framework.data.sql.SQLConnection;
import com.dimeng.framework.resource.Resource;
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
import com.dimeng.p2p.order.TenderOrderExecutor;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;

/**
 * 
 * 手动投资
 * <富友-预授权申请-优化版>
 * 
 * @author  heshiping
 * @version  [版本号, 2015年11月13日]
 */
@ResourceAnnotation
public class FYTenderOrderExecutor extends TenderOrderExecutor {
    
    public FYTenderOrderExecutor(ResourceProvider resourceProvider) {
        super(resourceProvider);
    }
    
    @Override
    public Class<? extends Resource> getIdentifiedType() {
        return FYTenderOrderExecutor.class;
    }
    
    @Override
    protected void doSubmit(SQLConnection connection, int orderId, Map<String, String> params) throws Throwable {
        super.doSubmit(connection, orderId, params);
        // 插入流水号
        String MCHNT_TXN_SSN = MchntTxnSsn.getMts(FuyouTypeEnum.SDTB.name());
        params.put("trxId", MCHNT_TXN_SSN);
        updateT6501(connection, MCHNT_TXN_SSN, orderId);
    }
    
    /**
     * {@inheritDoc}
     * 调用富友接口
     */
    @Override
    protected void callFace(SQLConnection connection, int orderId, Map<String, String> params) throws SQLException {
        ServiceProvider serviceProvider = resourceProvider.getResource(ServiceProvider.class);
        // 获取投资订单
        T6504 t6504 = selectT6504(connection, orderId);
        // 获取出账账户（投资人在托管方的账户）
        String tzrzh = getUserCustId(connection, t6504.F02);
        // 借款人ID
        int outId = selectT6230s(connection, t6504.F03);
        // 入账账户信息（借款人账户）
        String jkrzh = getUserCustId(connection, outId);
        if (tzrzh.equals(jkrzh)) {
            throw new LogicalException("不能投资自己的标!");
        }
        try (ServiceSession serviceSession = serviceProvider.createServiceSession()) {
            BigDecimal HBAMT = BigDecimal.ZERO;
            // 红包-订单ID<T6527>
            String hbOrdId = params.get("hbOrdId");
            // 判断是否有红包-并作查询
            if (!StringHelper.isEmpty(hbOrdId)) {
                HBAMT = selectT6527(connection, IntegerParser.parse(hbOrdId), t6504.F02);
            }
            // 商户代码
            logger.info("订单：" + orderId + "投资金额=" + t6504.F04 + " 红包=" + HBAMT);
            
            BidFaceManage manage = serviceSession.getService(BidFaceManage.class);
            
            Map<String, Object> map = manage.createBidFace(params.get("trxId"), tzrzh, jkrzh, PayUtil.getAmt(t6504.F04.subtract(HBAMT)));
            if (map != null && (FuyouRespCode.JYCG.getRespCode().equals(map.get("resp_code")))) {
                String contract_no = map.get("contract_no").toString();
                logger.info("向富友请求投资成功");
                params.put("contract_no", contract_no);
                params.put("out_cust_no", tzrzh);
                params.put("in_cust_no", jkrzh);
                params.put("mchnt_txn_ssn", map.get("mchnt_txn_ssn").toString());
                params.put("YN", "true");
                // 更新T6250增加合同号
                updateT6250(connection, orderId, contract_no);
            } else {
                params.put("YN", "false");
                logger.info("向第三方请求投资失败，请重新投资");
                throw new LogicalException("向第三方请求投资失败，请重新投资!");
            }
        } catch (Throwable e) {
            logger.error("向第三方请求投资失败，请重新投资", e);
            throw new LogicalException("向第三方请求投资失败，请重新投资!");
        }
    }
    
    /**
     * {@inheritDoc}
     * 回滚
     */
    @Override
    protected void rollbackFace(SQLConnection connection, int orderId, java.util.Map<String, String> params) throws Throwable {
        if (!Boolean.parseBoolean(params.get("YN"))) {
            return;
        }
        ServiceProvider serviceProvider = resourceProvider.getResource(ServiceProvider.class);
        
        try (ServiceSession serviceSession = serviceProvider.createServiceSession()) {
            // 流标
        	BidFaceManage manage = serviceSession.getService(BidFaceManage.class);
            Map<String, Object> map = null;
            for (int i = 0; i < 3; i++) {
                map = manage.createBidCancel(MchntTxnSsn.getMts(FuyouTypeEnum.TBLB.name()), params.get("out_cust_no"), params.get("in_cust_no"), params.get("contract_no"));
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
     * 红包
     * @param connection
     * @param F01 红包订单
     * @param F02 投资人ID
     * @return
     * @throws SQLException
     */
    private BigDecimal selectT6527(SQLConnection connection, int F01, int F02) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT F04 FROM S65.T6527 WHERE T6527.F01 = ? AND T6527.F02 = ?  LIMIT 1")) {
            ps.setInt(1, F01);
            ps.setInt(2, F02);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getBigDecimal(1);
                }
            }
        }
        return null;
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
