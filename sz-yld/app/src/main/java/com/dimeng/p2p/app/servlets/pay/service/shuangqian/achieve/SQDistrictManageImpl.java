/**
 * 文 件 名:  SQDistrictManageImpl.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2016年1月11日
 *//*
package com.dimeng.p2p.app.servlets.pay.service.shuangqian.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.S50.entities.T5019;
import com.dimeng.p2p.app.servlets.pay.service.shuangqian.service.SQDistrictManage;
import com.dimeng.p2p.escrow.shuangqian.achieve.AbstractEscrowService;

*//**
 * 双乾托管行政区管理
 * 
 * @author  zhoulantao
 * @version  [版本号, 2016年1月11日]
 *//*
public class SQDistrictManageImpl extends AbstractEscrowService implements SQDistrictManage
{
    
    public SQDistrictManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public T5019[] getSheng()
        throws SQLException
    {
        List<T5019> lists = new ArrayList<>();
        
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F04 FROM S50.T5019_SQ WHERE T5019_SQ.F03 = '0'"))
            {
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        {
                            T5019 t5019 = new T5019();
                            t5019.F01 = resultSet.getInt(1);
                            t5019.F05 = resultSet.getString(2);
                            
                            lists.add(t5019);
                        }
                    }
                }
            }
        }
        
        return lists.toArray(new T5019[lists.size()]);
    }
    
    @Override
    public T5019[] getShi(int provinceId)
        throws SQLException
    {
        List<T5019> lists = new ArrayList<>();
        
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F04 FROM S50.T5019_SQ WHERE T5019_SQ.F02 = ? AND T5019_SQ.F01 <> ? "))
            {
                pstmt.setInt(1, provinceId);
                pstmt.setInt(2, provinceId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        {
                            T5019 t5019 = new T5019();
                            t5019.F01 = resultSet.getInt(1);
                            t5019.F05 = resultSet.getString(2);
                            
                            lists.add(t5019);
                        }
                    }
                }
            }
        }
        
        return lists.toArray(new T5019[lists.size()]);
    }
    
}*/