package com.dimeng.p2p.escrow.fuyou.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.OrderType;
import com.dimeng.p2p.S65.entities.T6501;
import com.dimeng.p2p.S65.entities.T6517;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.S65.enums.T6501_F11;
import com.dimeng.p2p.escrow.fuyou.entity.TransactionQueryDetailedEntity;
import com.dimeng.p2p.escrow.fuyou.entity.TransactionQueryResponseEntity;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouEnum;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouRespCode;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouTypeEnum;
import com.dimeng.p2p.escrow.fuyou.face.TransactionQueryFace;
import com.dimeng.p2p.escrow.fuyou.service.OrderManage;
import com.dimeng.p2p.escrow.fuyou.util.MchntTxnSsn;
import com.dimeng.p2p.escrow.fuyou.variables.FuyouVariable;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.DateParser;
import com.dimeng.util.parser.IntegerParser;

/**
 * 
 * 转账订单
 * 
 * @author  heshiping
 * @version  [版本号, 2016年1月28日]
 */
public class OrderManageImpl extends AbstractEscrowService implements OrderManage
{
    
    public OrderManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public ArrayList<T6501> getToSubmit(OrderType orderType)
        throws Throwable
    {
        ArrayList<T6501> arrayList = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F08, F10, F12 FROM S65.T6501 WHERE T6501.F03 in( ?,?,?) AND T6501.F02 = ? AND F11=? LIMIT 10 "))
            {
                pstmt.setString(1, T6501_F03.DTJ.name());
                pstmt.setString(2, T6501_F03.DQR.name());
                pstmt.setString(3, T6501_F03.SB.name());
                pstmt.setInt(4, orderType.orderType());
                pstmt.setString(5, T6501_F11.F.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        T6501 record = new T6501();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = T6501_F03.parse(resultSet.getString(3));
                        record.F04 = resultSet.getTimestamp(4);
                        record.F05 = resultSet.getTimestamp(5);
                        record.F06 = resultSet.getTimestamp(6);
                        record.F08 = resultSet.getInt(7);
                        record.F10 = resultSet.getString(8);
                        record.F12 = resultSet.getString(9);
                        if (arrayList == null)
                        {
                            arrayList = new ArrayList<T6501>();
                        }
                        arrayList.add(record);
                        
                    }
                }
            }
            return arrayList;
        }
    }
    
    @Override
    public boolean selectFuyou(ServiceSession serviceSession, T6501 t6501, Map<String, String> params)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            T6517 t6517 = selectT6517(connection, t6501.F01);
            String cust_no = null;
            switch (t6517.F06)
            {
            // 有效推广<推广首次充值>
                case FeeCode.TG_YX:
                    // 持续推广
                case FeeCode.TG_CX:
                    // 投资奖励 <奖励标>
                case FeeCode.TZ_TBJL:
                    cust_no = configureProvider.format(FuyouVariable.FUYOU_P2P_ACCOUNT_NAME);
                    break;
                // 成交服务费、借款管理费
                case FeeCode.CJFWF:
                    cust_no = selectT6119(connection, t6517.F03);
                    break;
            }
            //起始时间
            String start_day = new SimpleDateFormat("yyyyMMdd").format(t6501.F04);
            // 结束时间
            String end_day;
            if (t6501.F05 == null)
            {
                end_day = new SimpleDateFormat("yyyyMMdd").format(t6501.F04);
            }
            else
            {
                end_day = new SimpleDateFormat("yyyyMMdd").format(t6501.F05);
            }
            // 交易流水
            String txn_ssn = t6501.F10;
            // 交易查询接口地址
            //调用第三方平台
            TransactionQueryResponseEntity result =
                TransactionQueryFace.executeTransactionQuery(configureProvider.format(FuyouVariable.FUYOU_ACCOUNT_ID),
                    MchntTxnSsn.getMts(FuyouTypeEnum.JYCX.name()),
                    FuyouEnum.PWPC.name(),
                    start_day,
                    end_day,
                    txn_ssn,
                    cust_no,
                    "",
                    "",
                    "",
                    "",
                    configureProvider.format(FuyouVariable.FUYOU_TRADINGQUERY_URL),
                    serviceSession);
            String resultCode = result.respCode;
            int sb_time = IntegerParser.parse(configureProvider.getProperty(FuyouVariable.FUYOU_TRANSFER_SB_TIME));
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
                                updateT6501(connection, txn_ssn, t6501.F01, T6501_F03.DQR.name(), null);
                                return true;
                            }
                            else
                            {
                                // 说明第托管平台失败了，P2P重新生成流水号
                                logger.info("定时转账对账订单：" + t6501.F01 + " -重新生成流水号");
                                txn_ssn = MchntTxnSsn.getMts(FuyouTypeEnum.NEW.name());
                                // 如果失败订单创建大于设置的对帐处理不处理时间，订单列新为已对帐，再查询时，不查这种订单了
                            	if(new Date().getTime() - t6501.F04.getTime() > (1000 * 60 * 60) * sb_time && t6501.F03.name().equals(T6501_F03.SB.name())){
                            		 logger.info("定时转账对账订单：" + t6501.F01 + " ,转账对账失败,超过转账失败订单处理时间限制,原流水号 ="+t6501.F10);
                            		 updateT6501_F11(connection, txn_ssn, t6501.F01, T6501_F03.SB.name(),T6501_F11.S,t6501.F12+",查询对账返回:"+detail.getRspCdDesc());
                        		}else{
                        			// 小于设定的时间，可再次进行转账查询
                        			 updateT6501(connection, txn_ssn, t6501.F01, T6501_F03.DTJ.name(), null);
                        		}
                                return false;
                            }
                        }
                    }
                }
                // 如果失败订单创建大于设置的对帐处理不处理时间，订单列新为已对帐，再查询时，不查这种订单了
            	if(new Date().getTime() - t6501.F04.getTime() > (1000 * 60 * 60) * sb_time && t6501.F03.name().equals(T6501_F03.SB.name())){
            			logger.info("定时转账对账订单：" + t6501.F01 + " ,转账对账失败,超过转账失败订单处理时间限制,原流水号 ="+t6501.F10);
            		 updateT6501_F11(connection, txn_ssn, t6501.F01, T6501_F03.SB.name(),T6501_F11.S,t6501.F12+" ,查询对账返回码:"+result.respCode);
        		}else{
        			// 小于设定的时间，可再次进行转账查询
        			 updateT6501(connection, txn_ssn, t6501.F01, T6501_F03.DTJ.name(), null);
        		}
            }
            else
            {
                throw new LogicalException("定时转账提单对账异常!");
            }
        }
        return false;
    }
    
    private T6517 selectT6517(Connection connection, int F01)
        throws SQLException
    {
        T6517 t6517 = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06 FROM S65.T6517 WHERE T6517.F01 = ? "))
        {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    t6517 = new T6517();
                    t6517.F01 = resultSet.getInt(1);
                    t6517.F02 = resultSet.getBigDecimal(2);
                    t6517.F03 = resultSet.getInt(3);
                    t6517.F04 = resultSet.getInt(4);
                    t6517.F05 = resultSet.getString(5);
                    t6517.F06 = resultSet.getInt(6);
                }
            }
        }
        return t6517;
    }
    
    private T6501 selectT6501(Connection connection, int F01)
            throws SQLException
        {
    		T6501 record = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F08, F10,F11, F12 FROM S65.T6501 WHERE T6501.F01 = ? "))
            {
                pstmt.setInt(1, F01);
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
                         record.F08 = resultSet.getInt(7);
                         record.F10 = resultSet.getString(8);
                         record.F11 = T6501_F11.parse(resultSet.getString(9));
                         record.F12 = resultSet.getString(10);
                    }
                }
            }
            return record;
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
                connection.prepareStatement("UPDATE S65.T6501 SET F03 = ?, F10 = ?, F12 = ? WHERE F01 = ? "))
            {
                pstmt.setString(1, F03);
                pstmt.setString(2, F10);
                pstmt.setString(3, F12);
                pstmt.setInt(4, F01);
                pstmt.execute();
            }
        }
    }
    
    /**
     * 将订单改为已对帐
     * @param F10 流水号
     * @param F01 订单号
     * @param F03 状态
     * @throws SQLException
     */
    private void updateT6501_F11(Connection connection, String F10, int F01, String F03, T6501_F11 F11,String F12)
        throws SQLException
    {
      
            try (PreparedStatement pstmt =
                connection.prepareStatement("UPDATE S65.T6501 SET F03 = ?, F10 = ?, F11 = ?,F12=? WHERE F01 = ? "))
            {
                pstmt.setString(1, F03);
                pstmt.setString(2, F10);
                pstmt.setString(3, F11.name());
                pstmt.setString(4, F12);
                pstmt.setInt(5, F01);
                pstmt.execute();
            }
        
    }
    
}
