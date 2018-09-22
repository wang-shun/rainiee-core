package com.dimeng.p2p.escrow.fuyou.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.S61.enums.T6198_F04;
import com.dimeng.p2p.account.user.service.achieve.SafetyManageImpl;
import com.dimeng.p2p.escrow.fuyou.service.FYSafetyManage;

/**
 * 
 *  托管引导页实名认证管理 
 * <功能详细描述>
 * 
 * @author  lingyuanjie
 * @version  [版本号, 2016年5月23日]
 */
public class FYSafetyManageImpl extends SafetyManageImpl implements FYSafetyManage
{
    
    public FYSafetyManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public boolean checkPhoneIsExist(String phone)
        throws Throwable
    {
        boolean isExist = false;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01 FROM S61.T6110 WHERE T6110.F04 = ? LIMIT 1"))
            {
                pstmt.setString(1, phone);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        if (resultSet.getInt(1) != serviceResource.getSession().getAccountId())
                        {
                            isExist = true;
                        }
                    }
                }
            }
        }
        return isExist;
    }
    
    /** {@inheritDoc} */
    
    @Override
    public void updateT6198F06(int userId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            execute(connection,
                "UPDATE  S61.T6198 SET F04=?,F06=? WHERE F02 = ?",
                T6198_F04.PC.name(),
                getCurrentTimestamp(connection),
                userId);
        }
    }
    
}
