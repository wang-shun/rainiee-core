/*
 * 文 件 名:  TransferManageServiceImpl.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  huguangze
 * 修改时间:  2015年11月23日
 */
package com.dimeng.p2p;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.S65.entities.T6517;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.service.AbstractP2PService;

/**
 * 转账管理服务实现
 * 
 * @author  huguangze
 * @version  [版本号, 2015年11月23日]
 */
public class TransferManageServiceImpl extends AbstractP2PService implements ITransferManageService
{
    
    /** <默认构造函数>
     */
    public TransferManageServiceImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    /** {@inheritDoc} */
    
    @Override
    public List<T6517> findTransferOrderList()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            List<T6517> transferOrderList = new ArrayList<>();
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT * from S65.T6517 t1,S65.T6501 t2 where t1.F01 = t2.F01 and t2.F03 = ?"))
            {
                pstmt.setString(1, T6501_F03.DTJ.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        T6517 record = new T6517();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getBigDecimal(2);
                        record.F03 = resultSet.getInt(3);
                        record.F04 = resultSet.getInt(4);
                        record.F05 = resultSet.getString(5);
                        transferOrderList.add(record);
                    }
                }
            }
            return transferOrderList;
        }
    }
}
