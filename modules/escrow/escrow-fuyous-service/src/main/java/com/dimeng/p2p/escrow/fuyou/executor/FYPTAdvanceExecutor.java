package com.dimeng.p2p.escrow.fuyou.executor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Map;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.data.sql.SQLConnection;
import com.dimeng.framework.resource.Resource;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.entities.T6119;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S62.entities.T6251;
import com.dimeng.p2p.S65.entities.T6501;
import com.dimeng.p2p.S65.entities.T6514;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouRespCode;
import com.dimeng.p2p.escrow.fuyou.service.TransferManage;
import com.dimeng.p2p.escrow.fuyou.util.PayUtil;
import com.dimeng.p2p.escrow.fuyou.variables.FuyouVariable;
import com.dimeng.p2p.order.DFAdvanceExecutor;

/**
 * 
 * P2P平台垫付
 * <富友托管>
 * 
 * @author  heshiping
 * @version  [版本号, 2015年12月14日]
 */
public class FYPTAdvanceExecutor extends DFAdvanceExecutor {
    
    public FYPTAdvanceExecutor(ResourceProvider resourceProvider) {
        super(resourceProvider);
    }
    
    @Override
    public Class<? extends Resource> getIdentifiedType() {
        return FYPTAdvanceExecutor.class;
    }
    
    /**
     * 平台垫付转账给投资人
     */
    @Override
    protected void doSubmit(SQLConnection connection, int orderId, Map<String, String> ps) throws Throwable {
        // 根据订单查询
        T6514 t6514 = selectT6514(connection, orderId);
        if (t6514 == null) {
            throw new LogicalException("查询垫付记录异常，请稍后重新垫付!");
        }
        // 查询标债权记录
        T6251 t6251 = selectT6251(connection, t6514.F03);
        // 查询平台账户
        T6101 dfrzh = selectT6101(connection, t6514.F04, T6101_F03.WLZH, false);
        if (dfrzh == null) {
            throw new LogicalException("平台往来账户账户不存在!");
        }
        T6101 tzrzh = selectT6101(connection, t6251.F04, T6101_F03.WLZH, false);
        if (tzrzh == null) {
            throw new LogicalException("投资人往来账户不存在!");
        }
        if (dfrzh.F06.compareTo(t6514.F05) < 0) {
            throw new LogicalException("风险保证金余额不足，不能进行垫付！");
        }
        // 调用第三方划拨接口
        logger.info("不良资产处理时，转账开始...");
        // 根据订单ID查询订单
        T6501 t6501 = selectT6501(connection, orderId);
        ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        ServiceProvider serviceProvider = resourceProvider.getResource(ServiceProvider.class);
        try (ServiceSession serviceSession = serviceProvider.createServiceSession()) {
            T6119 rzzh = selectT6119(connection, tzrzh.F02);
            // 把借款人的提前还款手续费 转账到平台账户上去
            TransferManage manage = serviceSession.getService(TransferManage.class);
            Map<String, Object> map = manage.createTransferMap(
                    t6501.F10, configureProvider.format(FuyouVariable.FUYOU_P2P_ACCOUNT_NAME),
                    rzzh.F03, PayUtil.getAmt(t6514.F05), "transferBmu");
            // 实体不为空，说明验签通过。
            if (map != null && FuyouRespCode.JYCG.getRespCode().equals(map.get("resp_code"))) {
                ps.put("success", "true");
                logger.info("不良资产处理还款时，划拨成功。");
            } else {
                ps.put("success", "false");
                String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
                logger.info("还款时，划拨失败。 还款机构ID：" + t6514.F04 + "，投资人ID：" + t6251.F04 + ",还款金额：" + t6514.F05 + ",还款时间：" + time);
            }
        }
    }
    
    public T6119 selectT6119(Connection connection, int F01) throws SQLException {
        T6119 record = null;
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT F01, F02, F03 FROM S61.T6119 WHERE T6119.F01 = ? LIMIT 1")) {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    record = new T6119();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getString(3);
                }
            }
        }
        return record;
    }
    
}