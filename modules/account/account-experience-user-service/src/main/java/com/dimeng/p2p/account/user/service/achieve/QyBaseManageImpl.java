package com.dimeng.p2p.account.user.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.entities.T6112;
import com.dimeng.p2p.S61.entities.T6113;
import com.dimeng.p2p.S61.entities.T6161;
import com.dimeng.p2p.S61.entities.T6162;
import com.dimeng.p2p.S61.entities.T6163;
import com.dimeng.p2p.S61.entities.T6164;
import com.dimeng.p2p.account.user.service.QyBaseManage;
import com.dimeng.p2p.account.user.service.entity.Cwzk;
import com.dimeng.p2p.account.user.service.entity.QyContactInfo;
import com.dimeng.util.StringHelper;

public class QyBaseManageImpl extends AbstractAccountService implements QyBaseManage
{
    public QyBaseManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    public static class QyBaseManageFactory implements ServiceFactory<QyBaseManage>
    {
        @Override
        public QyBaseManage newInstance(ServiceResource serviceResource)
        {
            return new QyBaseManageImpl(serviceResource);
        }
    }
    
    @Override
    public T6164 getQylxxx()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            T6164 record = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT IFNULL((SELECT CONCAT(F06,F07,F08) FROM S50.T5019 WHERE T5019.F01 = T6164.F02),'') F02, T6164.F03, T6164.F04, T6164.F05, T6164.F06,T6164.F07,T6110.F05 AS email,T6164.F02 AS regoinId FROM S61.T6164 LEFT JOIN S61.T6110 ON T6110.F01 = T6164.F01 WHERE T6164.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new T6164();
                        record.address = resultSet.getString(1);
                        record.F03 = resultSet.getString(2);
                        record.F04 = resultSet.getString(3);
                        record.F05 = resultSet.getString(4);
                        record.F06 = resultSet.getString(5);
                        record.F07 = resultSet.getString(6);
                        record.email = resultSet.getString(7);
                        record.F02 = resultSet.getInt(8);
                    }
                }
            }
            return record;
        }
    }
    
    @Override
    public T6161 getQyjbxx()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            int userId = serviceResource.getSession().getAccountId();
            T6161 record = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F02, F03, F04, F05, F06, F07, F08, F09, F10, F11, F12, F13,  F14, F15, F16, F17, F18, F19, F20, F21 FROM S61.T6161 WHERE T6161.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new T6161();
                        record.F02 = resultSet.getString(1);
                        record.F03 = resultSet.getString(2);
                        record.F04 = resultSet.getString(3);
                        record.F05 = resultSet.getString(4);
                        record.F06 = resultSet.getString(5);
                        record.F07 = resultSet.getInt(6);
                        record.F08 = resultSet.getBigDecimal(7);
                        record.F09 = resultSet.getString(8);
                        record.F10 = resultSet.getInt(9);
                        record.F11 = resultSet.getString(10);
                        record.F12 = resultSet.getString(11);
                        record.F13 = resultSet.getString(12);
                        record.F14 = resultSet.getBigDecimal(13);
                        record.F15 = resultSet.getBigDecimal(14);
                        record.F16 = resultSet.getString(15);
                        record.F17 = resultSet.getString(16);
                        record.F18 = resultSet.getString(17);
                        record.F19 = resultSet.getString(18);
                        record.F20 = resultSet.getString(19);
                        record.F21 = resultSet.getString(20);
                        record.jgmx = getJgjs(userId, connection);
                        record.qkmx = getQkmx(userId, connection);
                    }
                }
            }
            return record;
        }
    }
    
    // 得到担保机构描述
    private String getJgjs(int id, Connection connection)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F04 FROM S61.T6180 WHERE T6180.F01 = ? LIMIT 1"))
        {
            pstmt.setInt(1, id);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    return resultSet.getString(1);
                }
            }
        }
        return null;
    }
    
    // 得到担保情况描述
    private String getQkmx(int id, Connection connection)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F02 FROM S61.T6180 WHERE T6180.F01 = ? LIMIT 1"))
        {
            pstmt.setInt(1, id);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    return resultSet.getString(1);
                }
            }
        }
        return null;
    }
    
    @Override
    public T6163[] getQycwzk()
        throws Throwable
    {
        int id = serviceResource.getSession().getAccountId();
        try (Connection connection = getConnection())
        {
            T6163[] entitys = new T6163[3];
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08 FROM S61.T6163 WHERE T6163.F01 = ? AND T6163.F02 = ? ORDER BY T6163.F02 DESC"))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                for (int i = 0; i < 3; i++)
                {
                    year--;
                    pstmt.setInt(2, year);
                    try (ResultSet resultSet = pstmt.executeQuery())
                    {
                        if (resultSet.next())
                        {
                            entitys[i] = new T6163();
                            entitys[i].F01 = resultSet.getInt(1);
                            entitys[i].F02 = resultSet.getInt(2);
                            entitys[i].F03 = resultSet.getBigDecimal(3);
                            entitys[i].F04 = resultSet.getBigDecimal(4);
                            entitys[i].F05 = resultSet.getBigDecimal(5);
                            entitys[i].F06 = resultSet.getBigDecimal(6);
                            entitys[i].F07 = resultSet.getBigDecimal(7);
                            entitys[i].F08 = resultSet.getString(8);
                        }
                        else
                        {
                            entitys[i] = new T6163();
                            entitys[i].F01 = id;
                            entitys[i].F02 = year;
                        }
                    }
                }
            }
            return entitys;
        }
    }
    
    @Override
    public T6162 getQyjs()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            T6162 record = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F02, F03, F04, F05 FROM S61.T6162 WHERE T6162.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new T6162();
                        record.F02 = resultSet.getString(1);
                        record.F03 = resultSet.getString(2);
                        record.F04 = resultSet.getString(3);
                        record.F05 = resultSet.getString(4);
                    }
                }
            }
            return record;
        }
        
    }
    
    @Override
    public PagingResult<T6113> getQyccxx(Paging paging)
        throws Throwable
    {
        String sql = "SELECT F01, F03, F04, F05, F06, F07 FROM S61.T6113 WHERE T6113.F02 = ?";
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<T6113>()
            {
                
                @Override
                public T6113[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<T6113> list = null;
                    while (resultSet.next())
                    {
                        T6113 record = new T6113();
                        record.F01 = resultSet.getInt(1);
                        record.F03 = resultSet.getString(2);
                        record.F04 = resultSet.getString(3);
                        record.F05 = resultSet.getInt(4);
                        record.F06 = resultSet.getBigDecimal(5);
                        record.F07 = resultSet.getBigDecimal(6);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return ((list == null || list.size() == 0) ? null : list.toArray(new T6113[list.size()]));
                }
            }, paging, sql, serviceResource.getSession().getAccountId());
        }
    }
    
    @Override
    public PagingResult<T6112> getQyfcxx(Paging paging)
        throws Throwable
    {
        String sql = "SELECT F01, F03, F04, F05, F06, F07, F08, F09, F10, F11 FROM S61.T6112 WHERE T6112.F02 = ?";
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<T6112>()
            {
                
                @Override
                public T6112[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<T6112> list = null;
                    while (resultSet.next())
                    {
                        T6112 record = new T6112();
                        record.F01 = resultSet.getInt(1);
                        record.F03 = resultSet.getString(2);
                        record.F04 = resultSet.getFloat(3);
                        record.F05 = resultSet.getInt(4);
                        record.F06 = resultSet.getBigDecimal(5);
                        record.F07 = resultSet.getBigDecimal(6);
                        record.F08 = resultSet.getInt(7);
                        record.F09 = resultSet.getString(8);
                        record.F10 = resultSet.getString(9);
                        record.F11 = resultSet.getBigDecimal(10);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return ((list == null || list.size() == 0) ? null : list.toArray(new T6112[list.size()]));
                }
            }, paging, sql, serviceResource.getSession().getAccountId());
        }
    }
    
    @Override
    public QyContactInfo getQyContactInfo()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            QyContactInfo record = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT T6164.F02, T6164.F03, T6164.F04, T6164.F05, T6164.F06, T6164.F07, T5019.F06, T5019.F07, T5019.F08 FROM S61.T6164 LEFT JOIN S50.T5019 ON T5019.F01 = T6164.F02 WHERE T6164.F01 = ?  LIMIT 1"))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new QyContactInfo();
                        record.F02 = resultSet.getInt(1);
                        record.F03 = resultSet.getString(2);
                        record.F04 = resultSet.getString(3);
                        record.F05 = resultSet.getString(4);
                        record.F06 = resultSet.getString(5);
                        record.F07 = resultSet.getString(6);
                        record.areaName =
                            new StringBuilder().append(resultSet.getString(7) == null ? "" : resultSet.getString(7))
                                .append(resultSet.getString(8) == null ? "" : resultSet.getString(8))
                                .append(resultSet.getString(9) == null ? "" : resultSet.getString(9))
                                .toString();
                        
                    }
                }
            }
            return record;
        }
    }
    
    @Override
    public void updateQyBases(T6161 entity)
        throws Throwable
    {
        if (entity.F03 != null && entity.F03 != "" && isZchExist(entity.F03))
        {
            throw new ParameterException("营业执照登记注册号重复");
        }
        if (entity.F05 != null && entity.F05 != "" && isNshExist(entity.F05))
        {
            throw new ParameterException("企业纳税号已存在");
        }
        if (entity.F06 != null && entity.F06 != "" && isZzjgExist(entity.F06))
        {
            throw new ParameterException("企业组织机构代码已存在");
        }
        if (entity.F19 != null && entity.F19 != "" && "Y".equals(entity.F20) && isShxydmExist(entity.F19))
        {
            throw new ParameterException("企业社会信用代码已存在");
        }
        int userId = serviceResource.getSession().getAccountId();
        try (Connection connection = getConnection())
        {
            if (!StringHelper.isEmpty(entity.F19))
            {
                try (PreparedStatement pstmt =
                    connection.prepareStatement("UPDATE S61.T6161 SET F07 = ?, F08 = ?, "
                        + "F09 = ?, F10 = ?, F14 = ?, F15 = ?,F16 = ?,F17 = ?,F19= ?,F20= ?,F21= ?,F18 = ? WHERE F01 = ?"))
                {
                    //pstmt.setString(1, entity.F04);
                    pstmt.setInt(1, entity.F07);
                    pstmt.setBigDecimal(2, entity.F08);
                    pstmt.setString(3, entity.F09);
                    pstmt.setInt(4, entity.F10);
                    pstmt.setBigDecimal(5, entity.F14);
                    pstmt.setBigDecimal(6, entity.F15);
                    pstmt.setString(7, entity.F16);
                    pstmt.setString(8, entity.F17);
                    pstmt.setString(9, entity.F19);
                    pstmt.setString(10, entity.F20);
                    pstmt.setString(11, entity.F21);
                    pstmt.setString(12, entity.F18);
                    pstmt.setInt(13, userId);
                    pstmt.execute();
                }
            }
            else
            {
                try (PreparedStatement pstmt =
                    connection.prepareStatement("UPDATE S61.T6161 SET  F03 = ?, F05 = ?, "
                        + "F06 = ?, F07 = ?, F08 = ?, F09 = ?, F10 = ?, F14 = ?, F15 = ?,F16 = ?,F17 = ?,F20 = ?,F21 = ?,F18 = ? WHERE F01 = ?"))
                {
                    pstmt.setString(1, entity.F03);
                    //pstmt.setString(2, entity.F04);
                    pstmt.setString(2, entity.F05);
                    pstmt.setString(3, entity.F06);
                    pstmt.setInt(4, entity.F07);
                    pstmt.setBigDecimal(5, entity.F08);
                    pstmt.setString(6, entity.F09);
                    pstmt.setInt(7, entity.F10);
                    pstmt.setBigDecimal(8, entity.F14);
                    pstmt.setBigDecimal(9, entity.F15);
                    pstmt.setString(10, entity.F16);
                    pstmt.setString(11, entity.F17);
                    pstmt.setString(12, entity.F20);
                    pstmt.setString(13, entity.F21);
                    pstmt.setString(14, entity.F18);
                    pstmt.setInt(15, userId);
                    pstmt.execute();
                }
            }
        }
    }
    
    // 营业执照登记注册号是否重复
    protected boolean isZchExist(String zch)
        throws SQLException
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01 FROM S61.T6161 WHERE T6161.F03 = ? LIMIT 1"))
            {
                pstmt.setString(1, zch);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    // 企业纳税号不能重复
    protected boolean isNshExist(String F05)
        throws SQLException
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01 FROM S61.T6161 WHERE T6161.F05 = ? LIMIT 1"))
            {
                pstmt.setString(1, F05);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return true;
                    }
                }
            }
            return false;
        }
    }
    
    // 企业组织机构代码不能重复
    protected boolean isZzjgExist(String F06)
        throws SQLException
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01 FROM S61.T6161 WHERE T6161.F06 = ? LIMIT 1"))
            {
                pstmt.setString(1, F06);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return true;
                    }
                }
            }
            return false;
        }
    }
    
    // 企业组织社会信用代码不能重复
    protected boolean isShxydmExist(String F19)
        throws SQLException
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01 FROM S61.T6161 WHERE T6161.F19 = ? LIMIT 1"))
            {
                pstmt.setString(1, F19);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return true;
                    }
                }
            }
            return false;
        }
    }
    
    @Override
    public void updateJscl(T6162 entity)
        throws Throwable
    {
        int userId = serviceResource.getSession().getAccountId();
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("UPDATE S61.T6162 SET F02 = ?, F03 = ?, F04 = ?, F05 = ? WHERE F01 = ?"))
            {
                pstmt.setString(1, entity.F02);
                pstmt.setString(2, entity.F03);
                pstmt.setString(3, entity.F04);
                pstmt.setString(4, entity.F05);
                pstmt.setInt(5, userId);
                pstmt.execute();
            }
        }
        
    }
    
    @Override
    public void updateLxxx(T6164 entity)
        throws Throwable
    {
        int userId = serviceResource.getSession().getAccountId();
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("UPDATE S61.T6164 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F07=? WHERE F01 = ?"))
            {
                pstmt.setInt(1, entity.F02);
                pstmt.setString(2, entity.F03);
                pstmt.setString(3, entity.F04);
                pstmt.setString(4, entity.F05);
                pstmt.setString(5, entity.F07);
                pstmt.setInt(6, userId);
                pstmt.execute();
            }
        }
    }
    
    @Override
    public void updateCwzk(Cwzk cwzk)
        throws Throwable
    {
        int userId = serviceResource.getSession().getAccountId();
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("INSERT INTO S61.T6163 (F01, F02, F03, F05, F06, F07, F08) VALUES (?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE F01 = VALUES(F01), F02 = VALUES(F02), F03 = VALUES(F03),  F05 = VALUES(F05), F06 = VALUES(F06), F07 = VALUES(F07), F08 = VALUES(F08)"))
            {
                for (T6163 entity : cwzk.t6163s)
                {
                    pstmt.setInt(1, userId);
                    pstmt.setInt(2, entity.F02);
                    pstmt.setBigDecimal(3, entity.F03);
                    pstmt.setBigDecimal(4, entity.F05);
                    pstmt.setBigDecimal(5, entity.F06);
                    pstmt.setBigDecimal(6, entity.F07);
                    pstmt.setString(7, entity.F08);
                    pstmt.execute();
                }
            }
        }
        
    }
    
}
