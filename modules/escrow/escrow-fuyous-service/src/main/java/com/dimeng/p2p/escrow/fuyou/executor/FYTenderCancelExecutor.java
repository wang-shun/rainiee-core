package com.dimeng.p2p.escrow.fuyou.executor;

import java.sql.Connection;
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
import com.dimeng.p2p.S62.entities.T6250;
import com.dimeng.p2p.S62.enums.T6250_F07;
import com.dimeng.p2p.S65.entities.T6504;
import com.dimeng.p2p.S65.entities.T6508;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouRespCode;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouTypeEnum;
import com.dimeng.p2p.escrow.fuyou.service.BidFaceManage;
import com.dimeng.p2p.escrow.fuyou.util.MchntTxnSsn;
import com.dimeng.p2p.order.TenderCancelExecutor;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;

/**
 * 
 * 预授权撤销
 * <流标>
 * 
 * @author  heshiping
 * @version  [版本号, 2015年11月23日]
 */
@ResourceAnnotation
public class FYTenderCancelExecutor extends TenderCancelExecutor {
    
    public FYTenderCancelExecutor(ResourceProvider resourceProvider) {
        super(resourceProvider);
    }
    
    @Override
    public Class<? extends Resource> getIdentifiedType() {
        return FYTenderCancelExecutor.class;
    }
    
    @Override
    protected void doSubmit(SQLConnection connection, int orderId, Map<String, String> params)  throws Throwable {
        logger.info("流标-doSubmit start...");
        String expOrdId = params.get("experOrderId");
        if (!StringHelper.isEmpty(expOrdId)) {
            try {
                String[] arrayExperOrdId = expOrdId.split(",");
                for (String strExperOrd : arrayExperOrdId) {
                    if (IntegerParser.parse(strExperOrd) > 0) {
                        // 修改订单状态
                        updateSubmit(connection, T6501_F03.DQR, IntegerParser.parse(strExperOrd));
                    }
                }
            } catch (Exception e) {
                // 异常处理
                handleError(connection, orderId);
                // 修改订单状态
                updateSubmit(connection, T6501_F03.SB, IntegerParser.parse(expOrdId));
                // 记录日志
                logger.error(e, e);
            }
        }
        // 调用富友接口
        doSubmitFyou(connection, orderId, params);
    }
    
    /**
     * 调用富友接口
     * <流标>
     * @param connection
     * @param orderId
     * @throws Throwable 
     */
    private void doSubmitFyou(SQLConnection connection, int orderId, Map<String, String> params) throws Throwable {
        T6508 t6508 = selectT6508(connection, orderId);
        T6504 t6504 = selectT6504(connection, t6508.F03);
        // 借款人ID
        int outId = selectT6230s(connection, t6504.F03);
        if (outId == 0) {
            logger.info("流标-借款人ID——查询异常");
            throw new LogicalException("借款人ID—查询异常!");
        }
        // 投资信息
        T6250 t6250 = selectT6250(connection, t6504.F05);
        ServiceProvider serviceProvider = resourceProvider.getResource(ServiceProvider.class);
        try (ServiceSession serviceSession = serviceProvider.createServiceSession()) {
            // 流标
        	BidFaceManage manage = serviceSession.getService(BidFaceManage.class);
            Map<String, Object> map = manage.createBidCancel(MchntTxnSsn.getMts(FuyouTypeEnum.JKLB.name()), 
            		getUserCustId(connection, t6508.F02), getUserCustId(connection, outId), t6250.F10);                
            if (map != null && (FuyouRespCode.JYCG.getRespCode().equals(map.get("resp_code")) || FuyouRespCode.JYWC.getRespCode().equals(map.get("resp_code")))) {
                logger.info("预授权撤销成功");
            } else {
                logger.info("预授权撤销失败");
                throw new LogicalException("不放款或流标失败!");
            }
        }
    }
    
    /**
     * 查询t6250
     */
    @Override
    protected T6250 selectT6250(Connection connection, int F01)
        throws SQLException
    {
        T6250 record = null;
        try (PreparedStatement pstmt =  connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F10 FROM S62.T6250 WHERE T6250.F01 = ? LIMIT 1")) {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    record = new T6250();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getInt(3);
                    record.F04 = resultSet.getBigDecimal(4);
                    record.F05 = resultSet.getBigDecimal(5);
                    record.F06 = resultSet.getTimestamp(6);
                    record.F07 = T6250_F07.parse(resultSet.getString(7));
                    record.F10 = resultSet.getString(8);
                }
            }
        }
        return record;
    }
    
    /**
     * 投标信息
     * @param connection
     * @param F05
     * @return
     * @throws SQLException
     */
    private T6504 selectT6504(Connection connection, int F05) throws SQLException  {
        T6504 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05 FROM S65.T6504 WHERE T6504.F05 = ? LIMIT 1")) {
            pstmt.setInt(1, F05);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    record = new T6504();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getInt(3);
                    record.F04 = resultSet.getBigDecimal(4);
                    record.F05 = resultSet.getInt(5);
                }
            }
        }
        return record;
    }
}
