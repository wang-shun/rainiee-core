/*
 * 文 件 名:  AbstractMallService.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoucl
 * 修改时间:  2015年12月14日
 */
package com.dimeng.p2p.modules.score.mall.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.resource.ResourceNotFoundException;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.P2PConst;
import com.dimeng.p2p.service.AbstractP2PService;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2015年12月14日]
 */
public abstract class AbstractMallService extends AbstractP2PService
{

    public AbstractMallService(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    protected Connection getConnection() throws ResourceNotFoundException,SQLException {
        return serviceResource.getDataConnectionProvider(SQLConnectionProvider.class, P2PConst.DB_MASTER_PROVIDER).getConnection();
    }

    protected Connection getConnection(String db) throws ResourceNotFoundException, SQLException {
        return serviceResource.getDataConnectionProvider(SQLConnectionProvider.class, P2PConst.DB_MASTER_PROVIDER).getConnection(db);
    }

    @Override
    protected SQLConnectionProvider getSQLConnectionProvider() throws ResourceNotFoundException, SQLException {
        return serviceResource.getDataConnectionProvider(SQLConnectionProvider.class, P2PConst.DB_MASTER_PROVIDER);
    }

    /**
     * 模糊查询拼接方法
     * @param value
     * @return
     */
    protected String allMatch(String value){
        StringBuilder builder = new StringBuilder(value.length() + 2);
        builder.append('%').append(value).append('%');
        return builder.toString();
    }
    
    /**
     * 查询区域名称
     * 
     * @param id
     * @return
     * @throws SQLException
     */
    public String getRegion(int id)
        throws SQLException
    {
        if (id <= 0)
        {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement("SELECT F06,F07,F08 FROM S50.T5019 WHERE F01=?"))
            {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        sb.append(rs.getString(1));
                        sb.append(",");
                        sb.append(rs.getString(2));
                        sb.append(",");
                        sb.append(rs.getString(3));
                    }
                }
            }
        }
        return sb.toString();
    }
}
