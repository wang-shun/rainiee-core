/*
 * 文 件 名:  OverdueManageImpl.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  God
 * 修改时间:  2016年6月8日
 */
package com.dimeng.p2p.modules.statistics.console.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.S70.entities.T7053;
import com.dimeng.p2p.modules.statistics.console.service.OverdueManage;

/**
 * <逾期数据统计>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2016年6月8日]
 */
public class OverdueManageImpl extends AbstractStatisticsService implements OverdueManage
{
    
    public OverdueManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public T7053[] getT7053s(int year)
        throws Throwable
    {
        T7053[] t7053s = new T7053[12];
        for (int i = 0; i < 12; i++)
        {
            t7053s[i] = new T7053();
        }
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT F01,F02,F03,F04,F05 FROM S70.T7053 WHERE F01=? ORDER BY F02 ASC"))
            {
                ps.setInt(1, year);
                try (ResultSet resultSet = ps.executeQuery())
                {
                    while (resultSet.next())
                    {
                        int yue = resultSet.getInt(2);
                        t7053s[yue - 1].F01 = resultSet.getInt(1);
                        t7053s[yue - 1].F02 = yue;
                        t7053s[yue - 1].F03 = resultSet.getInt(3);
                        t7053s[yue - 1].F04 = resultSet.getBigDecimal(4);
                        t7053s[yue - 1].F05 = resultSet.getTimestamp(5);
                    }
                }
            }
        }
        return t7053s;
    }
    
    @Override
    public Integer[] getT7053Year()
        throws Throwable
    {
        List<Integer> years = new ArrayList<>();
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement("SELECT DISTINCT(F01) FROM S70.T7053"))
            {
                try (ResultSet resultSet = ps.executeQuery())
                {
                    while (resultSet.next())
                    {
                        years.add(resultSet.getInt(1));
                    }
                }
            }
        }
        return years.toArray(new Integer[years.size()]);
    }
    
}
