/**
 *
 */
package com.dimeng.p2p.modules.bid.console.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.XyType;
import com.dimeng.p2p.S62.entities.T6256;
import com.dimeng.p2p.modules.bid.console.service.AdvanceManage;
import com.dimeng.p2p.service.AbstractP2PService;

/**
 * @author beiweiyuan
 *
 */
public class AdvanceManageImpl extends AbstractP2PService implements AdvanceManage
{
    
    public AdvanceManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public void insertT6256(T6256 t6256)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            insert(connection, "INSERT INTO S62.T6256 SET F01 = ?, F02 = ?", t6256.F01, t6256.F02);
        }
    }
    
    @Override
    public void insertT6256(int dfRecordId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            insert(connection,
                "INSERT INTO S62.T6256 SET F01 = ?, F02 = ?",
                dfRecordId,
                selectLastVersionOfDF(connection));
        }
    }
    
    /**
     * 获取平台最新的垫付协议的协议版本号
     * <功能详细描述>
     * @param connection
     * @return
     * @throws SQLException
     */
    private int selectLastVersionOfDF(Connection connection)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F02 FROM S51.T5125 WHERE T5125.F01 = ? LIMIT 1"))
        {
            pstmt.setInt(1, XyType.DFXY);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    return resultSet.getInt(1);
                }
            }
            return 0;
        }
    }
}
