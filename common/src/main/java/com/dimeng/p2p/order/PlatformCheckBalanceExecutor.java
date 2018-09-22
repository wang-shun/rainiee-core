/*
 * 文 件 名:  PlatformCheckBalanceExceutor.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2015年11月22日
 */
package com.dimeng.p2p.order;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.dimeng.framework.data.sql.SQLConnection;
import com.dimeng.framework.resource.Resource;
import com.dimeng.framework.resource.ResourceAnnotation;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.entities.T6102;
import com.dimeng.p2p.S61.entities.T6104;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S61.enums.T6104_F07;

/**
 * 平台调账信息执行器
 * 
 * @author  xiaoqi
 * @version  [版本号, 2015年11月22日]
 */
@ResourceAnnotation
public class PlatformCheckBalanceExecutor extends AbstractOrderExecutor
{
    
    public PlatformCheckBalanceExecutor(ResourceProvider resourceProvider)
    {
        super(resourceProvider);
    }
    
    @Override
    protected void doConfirm(SQLConnection connection, int orderId, Map<String, String> params)
        throws Throwable
    {
        //锁定平台调账信息记录
        T6104 t6104 = getT6104(connection, orderId, T6104_F07.DTJ.name());
        if (t6104 == null)
        {
            throw new LogicalException("平台调账信息不存在");
        }
        //锁定平台往来资金账户
        T6101 t6101 = selectT6101(connection, t6104.F02, T6101_F03.WLZH, true);
        if (t6101 == null)
        {
            throw new LogicalException("平台往来资金账户不存在");
        }
        
        //插入资金流水
        T6102 t6102 = new T6102();
        t6102.F02 = getPTID(connection);
        if ("charge".equals(params.get("type")))
        {
            t6102.F03 = FeeCode.PTCZ;
            t6102.F06 = t6104.F05;
            t6102.F07 = new BigDecimal(0);
            t6102.F08 = t6101.F06.add(t6104.F05);
        }
        else
        {
            t6102.F03 = FeeCode.PTTX;
            t6102.F06 = new BigDecimal(0);
            t6102.F07 = t6104.F06;
            t6102.F08 = t6101.F06.subtract(t6104.F06);
        }
        t6102.F04 = getPTID(connection);
        t6102.F05 = getCurrentTimestamp(connection);
        t6102.F09 = t6104.F10;
        insertT6102(connection, t6102);
        
        //更新平台往来账户余额
        if ("charge".equals(params.get("type")))
        {
            updateT6101(connection, t6101.F06.add(t6104.F05), t6101.F01);
        }
        else
        {
            updateT6101(connection, t6101.F06.subtract(t6104.F06), t6101.F01);
        }
        //更新平台调账信息状态
        updateT6104(connection, orderId, T6104_F07.CG.name());
    }
    
    /**
    * 更新平台账户余额
    * <功能详细描述>
    * @param connection
    * @param F06
    * @param F01
    * @throws Throwable
    */
    protected void updateT6101(Connection connection, BigDecimal F06, int F01)
        throws Throwable
    {
        // 更新用户账号信息
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S61.T6101 SET F06 =?, F07 = ? WHERE F01 = ?"))
        {
            pstmt.setBigDecimal(1, F06);
            pstmt.setTimestamp(2, getCurrentTimestamp(connection));
            pstmt.setInt(3, F01);
            pstmt.executeUpdate();
        }
    }
    
    @Override
    public Class<? extends Resource> getIdentifiedType()
    {
        return PlatformCheckBalanceExecutor.class;
    }
    
    /** {@inheritDoc} */
    
    @Override
    protected void doSubmit(SQLConnection connection, int orderId, Map<String, String> params)
        throws Throwable
    {
        
    }
    
    protected T6104 getT6104(Connection connection, int orderId, String status)
        throws Throwable
    {
        try (PreparedStatement ps =
            connection.prepareStatement("SELECT F01,F02,F03,F04,F05,F06,F07,F08,F09,F10 FROM S61.T6104 WHERE T6104.F03=? AND T6104.F07=? LIMIT 1 FOR UPDATE"))
        {
            ps.setInt(1, orderId);
            ps.setString(2, status);
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    T6104 t6104 = new T6104();
                    t6104.F01 = rs.getInt(1);
                    t6104.F02 = rs.getInt(2);
                    t6104.F03 = rs.getInt(3);
                    t6104.F04 = rs.getInt(4);
                    t6104.F05 = rs.getBigDecimal(5);
                    t6104.F06 = rs.getBigDecimal(6);
                    t6104.F07 = T6104_F07.parse(rs.getString(7));
                    t6104.F08 = rs.getTimestamp(8);
                    t6104.F09 = rs.getInt(9);
                    t6104.F10 = rs.getString(10);
                    return t6104;
                }
            }
        }
        return null;
    }
    
    protected void updateT6104(Connection connection, int orderId, String status)
        throws Throwable
    {
        try (PreparedStatement ps = connection.prepareStatement("UPDATE S61.T6104 SET F07=? WHERE F03=? "))
        {
            ps.setString(1, status);
            ps.setInt(2, orderId);
            ps.execute();
        }
    }
}