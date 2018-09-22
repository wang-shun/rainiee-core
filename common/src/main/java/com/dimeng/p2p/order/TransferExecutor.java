package com.dimeng.p2p.order;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Map;

import com.dimeng.framework.data.sql.SQLConnection;
import com.dimeng.framework.resource.Resource;
import com.dimeng.framework.resource.ResourceAnnotation;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.entities.T6102;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S65.entities.T6517;

@ResourceAnnotation
public class TransferExecutor extends AbstractOrderExecutor
{
    
    public TransferExecutor(ResourceProvider resourceProvider)
    {
        super(resourceProvider);
    }
    
    @Override
    protected void doConfirm(SQLConnection connection, int orderId, Map<String, String> params)
        throws Throwable
    {
        
        T6517 t6517 = selectT6517(connection, orderId);
        
        // 入账资金账户
        T6101 rzT6101 = selectT6101(connection, t6517.F04, T6101_F03.WLZH, true);
        // 出账资金账户
        T6101 cz6101 = selectT6101(connection, t6517.F03, T6101_F03.WLZH, true);
        
        Timestamp dateTime = getCurrentTimestamp(connection);
        
        if (t6517.F06 == FeeCode.TZ_TBHB)
        {
            T6102 eT6102 = new T6102();
            // 插入借款人用户资金流水
            updateT6101(connection, t6517.F02, t6517.F04, T6101_F03.WLZH);
            eT6102.F02 = rzT6101.F01;
            eT6102.F03 = FeeCode.JK;
            eT6102.F04 = cz6101.F01;
            eT6102.F05 = dateTime;
            eT6102.F06 = t6517.F02;
            eT6102.F08 = rzT6101.F06.add(t6517.F02);
            eT6102.F09 = "红包投资";
            insertT6102(connection, eT6102);
        }
        else
        {
            // 插入借款人用户资金流水
            updateT6101(connection, t6517.F02, t6517.F04, T6101_F03.WLZH);
            T6102 eT6102 = new T6102();
            eT6102.F02 = rzT6101.F01;
            eT6102.F03 = t6517.F06;
            eT6102.F04 = cz6101.F01;
            eT6102.F05 = dateTime;
            eT6102.F06 = t6517.F02;
            eT6102.F08 = rzT6101.F06.add(t6517.F02);
            eT6102.F09 = t6517.F05;
            insertT6102(connection, eT6102);
        }
        // 平台账户扣除奖励金额
        updateT6101(connection, t6517.F02.multiply(new BigDecimal(-1)), t6517.F03, T6101_F03.WLZH);
        T6102 ecT6102 = new T6102();
        ecT6102.F02 = cz6101.F01;
        ecT6102.F03 = t6517.F06;
        ecT6102.F04 = rzT6101.F01;
        ecT6102.F05 = dateTime;
        ecT6102.F07 = t6517.F02;
        ecT6102.F08 = cz6101.F06.subtract(t6517.F02);
        ecT6102.F09 = t6517.F05;
        insertT6102(connection, ecT6102);
    }
    
    @Override
    public Class<? extends Resource> getIdentifiedType()
    {
        return TransferExecutor.class;
    }
    
    protected T6517 selectT6517(Connection connection, int orderId)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT t1.* from S65.T6517 t1,S65.T6501 t2 where t1.F01 = t2.F01 and t1.F01 = ?"))
        {
            pstmt.setInt(1, orderId);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    T6517 record = new T6517();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getBigDecimal(2);
                    record.F03 = resultSet.getInt(3);
                    record.F04 = resultSet.getInt(4);
                    record.F05 = resultSet.getString(5);
                    record.F06 = resultSet.getInt(6);
                    return record;
                }
            }
        }
        return null;
    }
    
    protected void updateT6101(Connection connection, BigDecimal F01, int F02, T6101_F03 F03)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S61.T6101 SET F06 = F06 + ?, F07 = ? WHERE F02 = ? AND F03 = ?"))
        {
            pstmt.setBigDecimal(1, F01);
            pstmt.setTimestamp(2, getCurrentTimestamp(connection));
            pstmt.setInt(3, F02);
            pstmt.setString(4, F03.name());
            pstmt.execute();
        }
    }
}
