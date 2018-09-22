package com.dimeng.p2p.modules.account.console.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.commons.lang3.StringUtils;

import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.http.upload.FileStore;
import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S50.entities.T5020;
import com.dimeng.p2p.S50.enums.T5020_F03;
import com.dimeng.p2p.S51.enums.T5123_F03;
import com.dimeng.p2p.S51.enums.T5123_F04;
import com.dimeng.p2p.S51.enums.T5123_F06;
import com.dimeng.p2p.S61.entities.T6112;
import com.dimeng.p2p.S61.entities.T6113;
import com.dimeng.p2p.S61.entities.T6114;
import com.dimeng.p2p.S61.entities.T6121;
import com.dimeng.p2p.S61.entities.T6122;
import com.dimeng.p2p.S61.entities.T6161;
import com.dimeng.p2p.S61.entities.T6162;
import com.dimeng.p2p.S61.entities.T6163;
import com.dimeng.p2p.S61.entities.T6164;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F17;
import com.dimeng.p2p.S61.enums.T6110_F18;
import com.dimeng.p2p.S61.enums.T6110_F19;
import com.dimeng.p2p.S61.enums.T6114_F08;
import com.dimeng.p2p.S61.enums.T6114_F10;
import com.dimeng.p2p.S61.enums.T6118_F04;
import com.dimeng.p2p.S61.enums.T6120_F05;
import com.dimeng.p2p.S61.enums.T6121_F05;
import com.dimeng.p2p.S61.enums.T6141_F04;
import com.dimeng.p2p.S61.enums.T6141_F09;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.S62.enums.T6231_F21;
import com.dimeng.p2p.modules.account.console.service.QyManage;
import com.dimeng.p2p.modules.account.console.service.entity.Attestation;
import com.dimeng.p2p.modules.account.console.service.entity.Cwzk;
import com.dimeng.p2p.modules.account.console.service.entity.Dbxxmx;
import com.dimeng.p2p.modules.account.console.service.entity.Dfxxmx;
import com.dimeng.p2p.modules.account.console.service.entity.LoanRecord;
import com.dimeng.p2p.modules.account.console.service.entity.Rzxx;
import com.dimeng.p2p.modules.account.console.service.entity.XyrzTotal;
import com.dimeng.p2p.modules.account.console.service.query.DbxxmxQuery;
import com.dimeng.p2p.modules.account.console.service.query.DfxxmxQuery;
import com.dimeng.p2p.modules.account.console.service.query.LoanRecordQuery;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.EnumParser;
import com.dimeng.util.parser.TimestampParser;

public class QyManageImpl extends AbstractUserService implements QyManage
{
    
    public static class QyManageFactory implements ServiceFactory<QyManage>
    {
        
        @Override
        public QyManage newInstance(ServiceResource serviceResource)
        {
            return new QyManageImpl(serviceResource);
        }
    }
    
    public QyManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public T6162 getJscl(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            T6162 record = new T6162();
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05 FROM S61.T6162 WHERE T6162.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getString(2);
                        record.F03 = resultSet.getString(3);
                        record.F04 = resultSet.getString(4);
                        record.F05 = resultSet.getString(5);
                    }
                }
            }
            return record;
        }
    }
    
    @Override
    public void updateJscl(T6162 entity)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("UPDATE S61.T6162 SET F02 = ?, F03 = ?, F04 = ?, F05 = ? WHERE F01 = ?"))
            {
                pstmt.setString(1, entity.F02);
                pstmt.setString(2, entity.F03);
                pstmt.setString(3, entity.F04);
                pstmt.setString(4, entity.F05);
                pstmt.setInt(5, entity.F01);
                pstmt.execute();
            }
        }
        
    }
    
    @Override
    public T6164 getLxxx(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            T6164 record = new T6164();
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07 FROM S61.T6164 WHERE T6164.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getString(3);
                        record.F04 = resultSet.getString(4);
                        record.F05 = resultSet.getString(5);
                        record.F06 = resultSet.getString(6);
                        record.F07 = resultSet.getString(7);
                    }
                }
            }
            return record;
        }
    }
    
    @Override
    public void updateLxxx(T6164 entity, String email)
        throws Throwable
    {
        if (checkMobileExist(entity.F06, entity.F01))
        {
            throw new ParameterException("法人手机号码已经存在！");
        }
        if (!StringHelper.isEmpty(email) && checkEmailExist(email, entity.F01))
        {
            throw new ParameterException("法人邮箱地址已经存在！");
        }
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                try (PreparedStatement pstmt =
                    connection.prepareStatement("UPDATE S61.T6164 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07=? WHERE F01 = ?"))
                {
                    pstmt.setInt(1, entity.F02);
                    pstmt.setString(2, entity.F03);
                    pstmt.setString(3, entity.F04);
                    pstmt.setString(4, entity.F05);
                    pstmt.setString(5, entity.F06);
                    pstmt.setString(6, entity.F07);
                    pstmt.setInt(7, entity.F01);
                    pstmt.execute();
                }
                // 更新信用认证信息
                execute(connection,
                    "UPDATE S61.T6118 SET F04=?,F06=?,F07=? WHERE F01=?",
                    StringHelper.isEmpty(email) ? T6118_F04.BTG : T6118_F04.TG,
                    entity.F06,
                    email,
                    entity.F01);
                // 更新用户信息
                execute(connection, "UPDATE S61.T6110 SET F04=?,F05=? WHERE F01=?", entity.F06, email, entity.F01);
                serviceResource.commit(connection);
            }
            catch (SQLException e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    @Override
    public int addFcxx(T6112 entity)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("INSERT INTO S61.T6112 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?, F10 = ?, F11 = ?",
                    PreparedStatement.RETURN_GENERATED_KEYS))
            {
                pstmt.setInt(1, entity.F02);
                pstmt.setString(2, entity.F03);
                pstmt.setFloat(3, entity.F04);
                pstmt.setInt(4, entity.F05);
                pstmt.setBigDecimal(5, entity.F06);
                pstmt.setBigDecimal(6, entity.F07);
                pstmt.setInt(7, entity.F08);
                pstmt.setString(8, entity.F09);
                pstmt.setString(9, entity.F10);
                pstmt.setBigDecimal(10, entity.F11);
                pstmt.execute();
                try (ResultSet resultSet = pstmt.getGeneratedKeys();)
                {
                    if (resultSet.next())
                    {
                        return resultSet.getInt(1);
                    }
                    return 0;
                }
            }
        }
    }
    
    @Override
    public T6112 getFcxx(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            T6112 record = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11 FROM S61.T6112 WHERE T6112.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new T6112();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getString(3);
                        record.F04 = resultSet.getFloat(4);
                        record.F05 = resultSet.getInt(5);
                        record.F06 = resultSet.getBigDecimal(6);
                        record.F07 = resultSet.getBigDecimal(7);
                        record.F08 = resultSet.getInt(8);
                        record.F09 = resultSet.getString(9);
                        record.F10 = resultSet.getString(10);
                        record.F11 = resultSet.getBigDecimal(11);
                    }
                }
            }
            return record;
        }
    }
    
    @Override
    public void updateFcxx(T6112 entity)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("UPDATE S61.T6112 SET F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?, F10 = ?, F11 = ? WHERE F01 = ?"))
            {
                pstmt.setString(1, entity.F03);
                pstmt.setFloat(2, entity.F04);
                pstmt.setInt(3, entity.F05);
                pstmt.setBigDecimal(4, entity.F06);
                pstmt.setBigDecimal(5, entity.F07);
                pstmt.setInt(6, entity.F08);
                pstmt.setString(7, entity.F09);
                pstmt.setString(8, entity.F10);
                pstmt.setBigDecimal(9, entity.F11);
                pstmt.setInt(10, entity.F01);
                pstmt.execute();
            }
        }
        
    }
    
    @Override
    public PagingResult<T6112> seachFcxx(int id, Paging paging)
        throws Throwable
    {
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
            }, paging, "SELECT F01, F03, F04, F05, F06, F07, F08, F09, F10, F11 FROM S61.T6112 WHERE T6112.F02 = ?", id);
        }
    }
    
    @Override
    public int addCcxx(T6113 entity)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("INSERT INTO S61.T6113 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?",
                    PreparedStatement.RETURN_GENERATED_KEYS))
            {
                pstmt.setInt(1, entity.F02);
                pstmt.setString(2, entity.F03);
                pstmt.setString(3, entity.F04);
                pstmt.setInt(4, entity.F05);
                pstmt.setBigDecimal(5, entity.F06);
                pstmt.setBigDecimal(6, entity.F07);
                pstmt.execute();
                try (ResultSet resultSet = pstmt.getGeneratedKeys();)
                {
                    if (resultSet.next())
                    {
                        return resultSet.getInt(1);
                    }
                    return 0;
                }
            }
        }
    }
    
    @Override
    public T6113 getCcxx(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            T6113 record = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F03, F04, F05, F06, F07 FROM S61.T6113 WHERE T6113.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new T6113();
                        record.F01 = resultSet.getInt(1);
                        record.F03 = resultSet.getString(2);
                        record.F04 = resultSet.getString(3);
                        record.F05 = resultSet.getInt(4);
                        record.F06 = resultSet.getBigDecimal(5);
                        record.F07 = resultSet.getBigDecimal(6);
                    }
                }
            }
            return record;
        }
    }
    
    @Override
    public void updateCcxx(T6113 entity)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("UPDATE S61.T6113 SET F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ? WHERE F01 = ?"))
            {
                pstmt.setString(1, entity.F03);
                pstmt.setString(2, entity.F04);
                pstmt.setInt(3, entity.F05);
                pstmt.setBigDecimal(4, entity.F06);
                pstmt.setBigDecimal(5, entity.F07);
                pstmt.setInt(6, entity.F01);
                pstmt.execute();
            }
        }
        
    }
    
    @Override
    public PagingResult<T6113> seachCcxx(int id, Paging paging)
        throws Throwable
    {
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
            }, paging, "SELECT F01, F03, F04, F05, F06, F07 FROM S61.T6113 WHERE T6113.F02 = ?", id);
        }
    }
    
    @Override
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
    
    @Override
    public String getDbzz(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
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
        }
        return null;
    }
    
    @Override
    public void updateDbzz(String content, int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("UPDATE S61.T6180 SET F02=? WHERE T6180.F01 = ?"))
            {
                pstmt.setString(1, content);
                pstmt.setInt(2, id);
                pstmt.execute();
            }
        }
    }
    
    @Override
    public void updateCwzk(Cwzk cwzk)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("INSERT INTO S61.T6163 (F01, F02, F03, F05, F06, F07, F08) VALUES (?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE F01 = VALUES(F01), F02 = VALUES(F02), F03 = VALUES(F03),  F05 = VALUES(F05), F06 = VALUES(F06), F07 = VALUES(F07), F08 = VALUES(F08)"))
            {
                for (T6163 entity : cwzk.t6163s)
                {
                    pstmt.setInt(1, entity.F01);
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
    
    @Override
    public T6163[] getCwzk(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            T6163[] entitys = new T6163[3];
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08 FROM S61.T6163 WHERE T6163.F01 = ? AND T6163.F02 = ?"))
            {
                pstmt.setInt(1, id);
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
    public T5020[] getBank()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            ArrayList<T5020> list = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02 FROM S50.T5020 WHERE T5020.F03 = ?"))
            {
                pstmt.setString(1, T5020_F03.QY.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        T5020 record = new T5020();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getString(2);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                }
            }
            return ((list == null || list.size() == 0) ? null : list.toArray(new T5020[list.size()]));
        }
    }
    
    @Override
    public T6114 getDgzh(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            T6114 record = new T6114();
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F07 FROM S61.T6114 WHERE T6114.F02 = ? LIMIT 1"))
            {
                pstmt.setInt(1, id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getInt(3);
                        record.F04 = resultSet.getInt(4);
                        record.F05 = resultSet.getString(5);
                        record.F07 = resultSet.getString(6);
                    }
                }
            }
            return record;
        }
    }
    
    @Override
    public void updateDgzh(T6114 entity)
        throws Throwable
    {
        int bankNumId = entity.F01;
        if (isBankNumExist(entity.F07))
        {
            throw new LogicalException("银行卡号已存在");
        }
        try (Connection connection = getConnection())
        {
            if (bankNumId > 0)
            {
                try (PreparedStatement pstmt =
                    connection.prepareStatement("UPDATE S61.T6114 SET F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?, F10 = ? WHERE F01 = ?"))
                {
                    pstmt.setInt(1, entity.F03);
                    pstmt.setInt(2, entity.F04);
                    pstmt.setString(3, entity.F05);
                    pstmt.setString(4, entity.F06);
                    pstmt.setString(5, entity.F07);
                    pstmt.setString(6, T6114_F08.QY.name());
                    pstmt.setTimestamp(7, getCurrentTimestamp(connection));
                    pstmt.setString(8, T6114_F10.BTG.name());
                    pstmt.setInt(9, bankNumId);
                    pstmt.execute();
                }
            }
            else
            {
                try (PreparedStatement pstmt =
                    connection.prepareStatement("INSERT INTO S61.T6114 SET F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?, F10 = ?,F02=?"))
                {
                    pstmt.setInt(1, entity.F03);
                    pstmt.setInt(2, entity.F04);
                    pstmt.setString(3, entity.F05);
                    pstmt.setString(4, entity.F06);
                    pstmt.setString(5, entity.F07);
                    pstmt.setString(6, T6114_F08.QY.name());
                    pstmt.setTimestamp(7, getCurrentTimestamp(connection));
                    pstmt.setString(8, T6114_F10.BTG.name());
                    pstmt.setInt(9, entity.F02);
                    pstmt.execute();
                }
            }
        }
        
    }
    
    @Override
    public String getName(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F04 FROM S61.T6161 WHERE T6161.F01 = ? LIMIT 1"))
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
        }
        return null;
    }
    
    // 判断银行卡号是否存在
    private boolean isBankNumExist(String bankNum)
        throws SQLException
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01 FROM S61.T6114 WHERE T6114.F07 = ? LIMIT 1"))
            {
                pstmt.setString(1, bankNum);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return true;
                    }
                    else
                    {
                        return false;
                    }
                }
            }
        }
    }
    
    @Override
    public T6161 getQyxx(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            T6161 record = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11, F12, F13, F14, F15, F16, F17,F18,F19,F20,F21 FROM S61.T6161 WHERE T6161.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new T6161();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getString(2);
                        record.F03 = resultSet.getString(3);
                        record.F04 = resultSet.getString(4);
                        record.F05 = resultSet.getString(5);
                        record.F06 = resultSet.getString(6);
                        record.F07 = resultSet.getInt(7);
                        record.F08 = resultSet.getBigDecimal(8);
                        record.F09 = resultSet.getString(9);
                        record.F10 = resultSet.getInt(10);
                        record.F11 = resultSet.getString(11);
                        record.F12 = resultSet.getString(12);
                        record.F13 = resultSet.getString(13);
                        record.F14 = resultSet.getBigDecimal(14);
                        record.F15 = resultSet.getBigDecimal(15);
                        record.F16 = resultSet.getString(16);
                        record.F17 = resultSet.getString(17);
                        record.F18 = resultSet.getString(18);
                        record.F19 = resultSet.getString(19);
                        record.F20 = resultSet.getString(20);
                        record.F21 = resultSet.getString(21);
                        record.jgmx = getJgjs(id, connection);
                        record.qkmx = getQkmx(id, connection);
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
    public void updateQyxx(T6161 entity, T6110_F17 t6110_f17, T6110_F19 t6110_f19)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            if (isZchExist(entity.F03, entity.F01, connection))
            {
                throw new ParameterException("营业执照登记注册号已存在");
            }
            if (isNshExist(entity.F05, entity.F01, connection))
            {
                throw new ParameterException("企业纳税号已存在");
            }
            if (isZzjgExist(entity.F06, entity.F01, connection))
            {
                throw new ParameterException("企业组织机构代码已存在");
            }
            if (checkCardExists(StringHelper.encode(entity.F12), T6110_F06.FZRR, entity.F01))
            {
                throw new ParameterException("身份证号不能重复");
            }
            
            if (entity.F19 != null && entity.F19 != "" && isShxydmExist(entity.F19, entity.F01)
                && "Y".equals(entity.F20))
            {
                throw new ParameterException("社会信用代码已存在");
            }
            
            try
            {
                serviceResource.openTransactions(connection);
                try (PreparedStatement pstmt =
                    connection.prepareStatement("UPDATE S61.T6161 SET F03 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?, F10 = ?, F11 = ?, F12 = ?, F13 = ?, F14 = ?, F15 = ?, F16 = ?, F17 = ?,F18=?,F19 = ?, F20 = ?, F21 = ? WHERE F01 = ?"))
                {
                    pstmt.setString(1, entity.F03);
                    pstmt.setString(2, entity.F05);
                    pstmt.setString(3, entity.F06);
                    pstmt.setInt(4, entity.F07);
                    pstmt.setBigDecimal(5, entity.F08);
                    pstmt.setString(6, entity.F09);
                    pstmt.setInt(7, entity.F10);
                    pstmt.setString(8, entity.F11);
                    String sfz = entity.F12;
                    pstmt.setString(9, sfz.substring(0, 2) + "***************");
                    pstmt.setString(10, StringHelper.encode(sfz));
                    pstmt.setBigDecimal(11, entity.F14);
                    pstmt.setBigDecimal(12, entity.F15);
                    pstmt.setString(13, entity.F16);
                    pstmt.setString(14, entity.F17);
                    pstmt.setString(15, entity.F18);
                    pstmt.setString(16, entity.F19);
                    pstmt.setString(17, entity.F20);
                    pstmt.setString(18, entity.F21);
                    pstmt.setInt(19, entity.F01);
                    pstmt.execute();
                }
                Timestamp date = TimestampParser.parse(getBirthday(entity.F12));
                // 更新个人身份证
                execute(connection,
                    "UPDATE S61.T6141 SET F02=?,F04=?,F06=?,F07=?,F08=?,F09=? WHERE F01=?",
                    entity.F11,
                    T6141_F04.TG,
                    entity.F12.substring(0, 2) + "***************",
                    StringHelper.encode(entity.F12),
                    date,
                    getSexNotDecode(entity.F12),
                    entity.F01);
                
                // 更新是否允许投资
                if (t6110_f17 == T6110_F17.S)
                {
                    execute(connection,
                        "UPDATE S61.T6110 SET F17=?,F18=?,F19=? WHERE F01=?",
                        t6110_f17.name(),
                        T6110_F18.S.name(),
                        t6110_f19.name(),
                        entity.F01);
                }
                else
                {
                    execute(connection,
                        "UPDATE S61.T6110 SET F17=?,F19=? WHERE F01=?",
                        t6110_f17.name(),
                        t6110_f19.name(),
                        entity.F01);
                }
                
                execute(connection, "UPDATE S61.T6180 SET F04 = ? WHERE F01 = ?", entity.jgmx, entity.F01);
                if (isQyzh(entity.F01, connection))
                {
                    writeLog(connection, "操作日志", "修改机构信息");
                }
                else
                {
                    writeLog(connection, "操作日志", "修改企业信息");
                }
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    // 企业组织社会信用代码不能重复
    protected boolean isShxydmExist(String F19, int id)
        throws SQLException
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01 FROM S61.T6161 WHERE T6161.F19 = ? AND T6161.F01 <> ? LIMIT 1"))
            {
                pstmt.setString(1, F19);
                pstmt.setInt(2, id);
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
    
    // 身份证号是否重复
    protected boolean checkCardExists(String card, int id)
        throws SQLException
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01 FROM S61.T6141 WHERE T6141.F07 = ? AND  T6141.F01 <> ? LIMIT 1"))
            {
                pstmt.setString(1, card);
                pstmt.setInt(2, id);
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
    
    // 身份证号是否重复
    protected boolean checkCardExists(String card, T6110_F06 t6110_f06, int id)
        throws SQLException
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT T6141.F01 FROM S61.T6141 INNER JOIN S61.T6110 ON T6141.F01=T6110.F01 WHERE T6141.F07 = ? AND T6110.F06=? AND T6141.F01 <> ? LIMIT 1"))
            {
                pstmt.setString(1, card);
                pstmt.setString(2, t6110_f06.name());
                pstmt.setInt(3, id);
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
    
    // 营业执照登记注册号是否重复
    protected boolean isZchExist(String zch, int id, Connection connection)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01 FROM S61.T6161 WHERE T6161.F03 = ? AND  T6161.F01 <> ?  LIMIT 1"))
        {
            pstmt.setString(1, zch);
            pstmt.setInt(2, id);
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
    
    // 企业纳税号是否重复
    protected boolean isNshExist(String F05, int id, Connection connection)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01 FROM S61.T6161 WHERE T6161.F05 = ? AND  T6161.F01 <> ? LIMIT 1"))
        {
            pstmt.setString(1, F05);
            pstmt.setInt(2, id);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    return true;
                }
            }
            return false;
        }
    }
    
    // 企业组织机构代码是否重复
    protected boolean isZzjgExist(String F06, int id, Connection connection)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01 FROM S61.T6161 WHERE T6161.F06 = ? AND  T6161.F01 <> ? LIMIT 1"))
        {
            pstmt.setString(1, F06);
            pstmt.setInt(2, id);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    return true;
                }
            }
            return false;
        }
    }
    
    protected boolean isQyzh(int id, Connection connection)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement(" SELECT F06 , F10 FROM S61.T6110 WHERE T6110.F01 = ? "))
        {
            pstmt.setInt(1, id);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    if ("FZRR".equals(resultSet.getString(1)) && "S".equals(resultSet.getString(2)))
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    @Override
    public void updateQyxxZxq(T6161 entity)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            if (isZchExist(entity.F03, entity.F01, connection))
            {
                throw new ParameterException("营业执照登记注册号已存在");
            }
            if (isNshExist(entity.F05, entity.F01, connection))
            {
                throw new ParameterException("企业纳税号已存在");
            }
            if (isZzjgExist(entity.F06, entity.F01, connection))
            {
                throw new ParameterException("企业组织机构代码已存在");
            }
            
            try (PreparedStatement pstmt =
                connection.prepareStatement("UPDATE S61.T6161 SET F03 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?, F10 = ?, F11 = ?, F12 = ?, F13 = ?, F14 = ?, F15 = ?, F16 = ?, F17 = ?,F18=? WHERE F01 = ?"))
            {
                pstmt.setString(1, entity.F03);
                pstmt.setString(2, entity.F05);
                pstmt.setString(3, entity.F06);
                pstmt.setInt(4, entity.F07);
                pstmt.setBigDecimal(5, entity.F08);
                pstmt.setString(6, entity.F09);
                pstmt.setInt(7, entity.F10);
                pstmt.setString(8, entity.F11);
                String sfz = entity.F12;
                pstmt.setString(9, sfz.substring(0, 2) + "***************");
                pstmt.setString(10, StringHelper.encode(sfz));
                pstmt.setBigDecimal(11, entity.F14);
                pstmt.setBigDecimal(12, entity.F15);
                pstmt.setString(13, entity.F16);
                pstmt.setString(14, entity.F17);
                pstmt.setString(15, entity.F18);
                pstmt.setInt(16, entity.F01);
                pstmt.execute();
            }
            
            // 更新个人身份证
            execute(connection,
                "UPDATE S61.T6141 SET F02=?,F04=?,F06=?,F07=? WHERE F01=?",
                entity.F04,
                T6141_F04.TG,
                entity.F03,
                StringHelper.encode(entity.F03),
                entity.F01);
        }
        
    }
    
    @Override
    public boolean isPhoneExist(String phone, int id)
        throws SQLException
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01 FROM S61.T6164 WHERE T6164.F04 = ? AND F01 != ? LIMIT 1"))
            {
                pstmt.setString(1, phone);
                pstmt.setInt(2, id);
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
    
    /**
     * 校验绑定的邮箱是否已经存在 校验T6110表中的
     */
    private boolean checkEmailExist(String email, int id)
        throws SQLException
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01 FROM S61.T6110 WHERE T6110.F05 = ? AND F01 <> ? LIMIT 1"))
            {
                pstmt.setString(1, email);
                pstmt.setInt(2, id);
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
    
    /**
     * 校验绑定的邮箱是否已经存在 校验T6110表中的
     */
    private boolean checkMobileExist(String mobile, int id)
        throws SQLException
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01 FROM S61.T6110 WHERE T6110.F04 = ? AND F01 <> ? LIMIT 1"))
            {
                pstmt.setString(1, mobile);
                pstmt.setInt(2, id);
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
    public PagingResult<LoanRecord> getJkxx(LoanRecordQuery loanRecordQuery, Paging paging)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT T6230.F01,T6230.F03,T6230.F06, (T6230.F05-T6230.F07) AS F04,T6230.F09,T6230.F24,T6230.F20,T6230.F02,T6231.F21, T6231.F22, T6230.F05,T6230.F25 "
                    + " FROM S62.T6230 INNER JOIN S62.T6231 ON T6230.F01 = T6231.F01 WHERE T6230.F20 in (?,?,?,?,?,?,?) ");
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(T6230_F20.TBZ.name());
        parameters.add(T6230_F20.DFK.name());
        parameters.add(T6230_F20.HKZ.name());
        parameters.add(T6230_F20.YJQ.name());
        parameters.add(T6230_F20.YLB.name());
        parameters.add(T6230_F20.YDF.name());
        parameters.add(T6230_F20.YZR.name());
        
        if (loanRecordQuery.getUserId() > 0)
        {
            sql.append(" AND T6230.F02 = ?");
            parameters.add(loanRecordQuery.getUserId());
        }
        else
        {
            return null;
        }
        
        if (loanRecordQuery != null)
        {
            SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
            
            String string = loanRecordQuery.getLoanRecordTitle();
            if (!StringHelper.isEmpty(string))
            {
                sql.append(" AND T6230.F03 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(string));
            }
            string = loanRecordQuery.getLoanNum();
            if (!StringHelper.isEmpty(string))
            {
                sql.append(" AND T6230.F25 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(string));
            }
            Timestamp timestamp = loanRecordQuery.getCreateTimeStart();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T6230.F24) >= ?");
                parameters.add(timestamp);
            }
            timestamp = loanRecordQuery.getCreateTimeEnd();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T6230.F24) <= ?");
                parameters.add(timestamp);
            }
            T6230_F20 signState = loanRecordQuery.getState();
            if (signState != null)
            {
                sql.append(" AND T6230.F20 = ?");
                parameters.add(signState);
            }
        }
        sql.append(" ORDER BY T6230.F01 DESC");
        try (final Connection connection = getConnection())
        {
            PagingResult<LoanRecord> pagingResult = selectPaging(connection, new ArrayParser<LoanRecord>()
            {
                
                @Override
                public LoanRecord[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<LoanRecord> list = null;
                    while (resultSet.next())
                    {
                        LoanRecord loanRecord = new LoanRecord();
                        loanRecord.loanRecordId = resultSet.getInt(1);
                        loanRecord.loanRecordTitle = resultSet.getString(2);
                        loanRecord.yearRate = resultSet.getDouble(3);
                        loanRecord.amount = resultSet.getBigDecimal(4);
                        
                        loanRecord.loanRecordTime = resultSet.getTimestamp(6);
                        loanRecord.loanRecordState = EnumParser.parse(T6230_F20.class, resultSet.getString(7));
                        loanRecord.dayBorrowFlg = T6231_F21.parse(resultSet.getString(9));
                        if (loanRecord.dayBorrowFlg.equals(T6231_F21.F))
                        {
                            loanRecord.deadline = resultSet.getInt(5);
                        }
                        else
                        {
                            loanRecord.deadline = resultSet.getInt(10);
                        }
                        loanRecord.loanAmount = resultSet.getBigDecimal(11);
                        loanRecord.loanNum = resultSet.getString(12);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(loanRecord);
                    }
                    return list == null ? null : list.toArray(new LoanRecord[list.size()]);
                }
            }, paging, sql.toString(), parameters);
            return pagingResult;
        }
    }
    
    @Override
    public String getEmail(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT F05 FROM S61.T6110 WHERE T6110.F01=? LIMIT 1"))
            {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        return rs.getString(1);
                    }
                }
            }
        }
        return null;
    }
    
    @Override
    public PagingResult<Dbxxmx> seachDbjl(DbxxmxQuery dbxxmxQuery, Paging paging)
        throws Throwable
    {
        
        ArrayList<Object> parameters = new ArrayList<Object>();
        StringBuilder sql =
            new StringBuilder(
                "SELECT T6230.F25 F01,T6230.F03 F02,T6230.F05 F03,T6230.F06 F04,T6230.F09 F05,T6231.F21 F06,T6231.F22 F07,T6230.F24 F08,T6230.F20 F09");
        sql.append(" FROM S62.T6230 INNER JOIN S62.T6236 ON T6230.F01 = T6236.F02 INNER JOIN S62.T6231 ON T6230.F01 = T6231.F01 WHERE T6236.F03 = ? ");
        parameters.add(dbxxmxQuery.getUserId());
        SQLConnectionProvider connectionProvider = getSQLConnectionProvider();
        String param = dbxxmxQuery.getJkbh();
        if (!StringHelper.isEmpty(param))
        {
            sql.append(" AND T6230.F25 LIKE ?");
            parameters.add(connectionProvider.allMatch(param));
        }
        param = dbxxmxQuery.getJkbt();
        if (!StringHelper.isEmpty(param))
        {
            sql.append(" AND T6230.F03 LIKE ?");
            parameters.add(connectionProvider.allMatch(param));
        }
        param = dbxxmxQuery.getCreateTimeStart();
        if (!StringHelper.isEmpty(param))
        {
            sql.append(" AND T6230.F24 >= ?");
            parameters.add(param + " 00:00:00");
        }
        param = dbxxmxQuery.getCreateTimeEnd();
        if (!StringHelper.isEmpty(param))
        {
            sql.append(" AND T6230.F24 <= ?");
            parameters.add(param + " 23:59:59");
        }
        param = dbxxmxQuery.getZt();
        if (!StringHelper.isEmpty(param))
        {
            sql.append(" AND T6230.F20 = ?");
            parameters.add(param);
        }
        sql.append(" ORDER BY T6230.F22 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<Dbxxmx>()
            {
                
                @Override
                public Dbxxmx[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<Dbxxmx> list = null;
                    while (resultSet.next())
                    {
                        Dbxxmx record = new Dbxxmx();
                        record.jkbh = resultSet.getString(1);
                        record.jkbt = resultSet.getString(2);
                        record.jkje = resultSet.getBigDecimal(3);
                        record.nhl = resultSet.getBigDecimal(4);
                        // 2.投资期限
                        String dayOrMonth = resultSet.getInt(5) + "个月";
                        if ("S".equals(resultSet.getString(6)))
                        {
                            dayOrMonth = resultSet.getInt(7) + "天";
                        }
                        record.qx = dayOrMonth;
                        record.jksj = resultSet.getTimestamp(8);
                        record.zt = T6230_F20.parse(resultSet.getString(9)).getChineseName();
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return ((list == null || list.size() == 0) ? null : list.toArray(new Dbxxmx[list.size()]));
                }
            }, paging, sql.toString(), parameters);
        }
    }
    
    @Override
    public PagingResult<Dfxxmx> seachDfjl(DfxxmxQuery dfxxmxQuery, Paging paging)
        throws Throwable
    {
        ArrayList<Object> parameters = new ArrayList<Object>();
        StringBuilder sql =
            new StringBuilder(
                "SELECT T6230.F25 F01,T6110.F02 F02,T6230.F03 F03,T6253.F05 F04,T6253.F06 F05,T6253.F08 F06,T6230.F20 F07");
        sql.append(" FROM S62.T6253 INNER JOIN S62.T6230 ON T6253.F02 = T6230.F01 INNER JOIN S61.T6110 ON T6253.F04=T6110.F01 WHERE T6253.F03 = ? ");
        parameters.add(dfxxmxQuery.getUserId());
        SQLConnectionProvider connectionProvider = getSQLConnectionProvider();
        String param = dfxxmxQuery.getJkbh();
        if (!StringHelper.isEmpty(param))
        {
            sql.append(" AND T6230.F25 LIKE ?");
            parameters.add(connectionProvider.allMatch(param));
        }
        param = dfxxmxQuery.getJkbt();
        if (!StringHelper.isEmpty(param))
        {
            sql.append(" AND T6230.F03 LIKE ?");
            parameters.add(connectionProvider.allMatch(param));
        }
        param = dfxxmxQuery.getZt();
        if (!StringHelper.isEmpty(param))
        {
            sql.append(" AND T6230.F20 = ?");
            parameters.add(param);
        }
        sql.append(" ORDER BY T6253.F07 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<Dfxxmx>()
            {
                
                @Override
                public Dfxxmx[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<Dfxxmx> list = null;
                    while (resultSet.next())
                    {
                        Dfxxmx record = new Dfxxmx();
                        record.jkbh = resultSet.getString(1);
                        record.yhm = resultSet.getString(2);
                        record.jkbt = resultSet.getString(3);
                        record.dfje = resultSet.getBigDecimal(4);
                        record.dffhje = resultSet.getBigDecimal(5);
                        record.yqqs = resultSet.getInt(6);
                        record.zt = T6230_F20.parse(resultSet.getString(7)).getChineseName();
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return ((list == null || list.size() == 0) ? null : list.toArray(new Dfxxmx[list.size()]));
                }
            }, paging, sql.toString(), parameters);
        }
    }
    
    private String getSexNotDecode(String sfzh)
    {
        if (StringHelper.isEmpty(sfzh))
        {
            return null;
        }
        String sex = null;
        try
        {
            if (Integer.parseInt(sfzh.substring(sfzh.length() - 2, sfzh.length() - 1)) % 2 != 0)
            {
                sex = T6141_F09.M.name();
            }
            else
            {
                sex = T6141_F09.F.name();
            }
        }
        catch (Throwable throwable)
        {
            logger.error("SafetyManageImpl.getSexNotDecode error", throwable);
        }
        return sex;
    }
    
    /**
     * 根据身份证得到出生年月
     * 
     * @param cardID
     * @return
     */
    private static String getBirthday(String cardID)
    {
        StringBuffer tempStr = new StringBuffer("");
        if (cardID != null && cardID.trim().length() > 0)
        {
            if (cardID.trim().length() == 15)
            {
                tempStr.append(cardID.substring(6, 12));
                tempStr.insert(4, '-');
                tempStr.insert(2, '-');
                tempStr.insert(0, "19");
            }
            else if (cardID.trim().length() == 18)
            {
                tempStr = new StringBuffer(cardID.substring(6, 14));
                tempStr.insert(6, '-');
                tempStr.insert(4, '-');
            }
        }
        return tempStr.toString();
    }
    
    @Override
    public XyrzTotal getXyrzTotal(int userId)
        throws Throwable
    {
        XyrzTotal xyrzTotal = new XyrzTotal();
        
        if (userId <= 0)
        {
            throw new ParameterException("参数值不能为空！");
        }
        
        try (Connection conn = getConnection())
        {
            try (PreparedStatement ps =
                conn.prepareStatement("SELECT COUNT(T5123.F01) FROM S51.T5123 WHERE T5123.F03=? AND T5123.F04=? AND T5123.F06=?"))
            {
                ps.setString(1, T5123_F03.S.name());
                ps.setString(2, T5123_F04.QY.name());
                ps.setString(3, T5123_F06.QY.name());
                
                try (ResultSet rSet = ps.executeQuery())
                {
                    if (rSet.next())
                    {
                        xyrzTotal.needAttestation = rSet.getInt(1);
                    }
                }
            }
            
            try (PreparedStatement ps =
                conn.prepareStatement("SELECT COUNT(T5123.F01) FROM S51.T5123 WHERE T5123.F03=? AND T5123.F04=? AND T5123.F06=?"))
            {
                ps.setString(1, T5123_F03.F.name());
                ps.setString(2, T5123_F04.QY.name());
                ps.setString(3, T5123_F06.QY.name());
                
                try (ResultSet rSet = ps.executeQuery())
                {
                    if (rSet.next())
                    {
                        xyrzTotal.notNeedAttestation = rSet.getInt(1);
                    }
                }
            }
            
            try (PreparedStatement ps =
                conn.prepareStatement("SELECT COUNT(T6120.F01) FROM S61.T6120,S51.T5123 WHERE T6120.F02=T5123.F01 AND T6120.F01=? AND T5123.F03=? AND T5123.F04=? AND T6120.F05=? AND T5123.F06=?"))
            {
                ps.setInt(1, userId);
                ps.setString(2, T5123_F03.S.name());
                ps.setString(3, T5123_F04.QY.name());
                ps.setString(4, T6120_F05.TG.name());
                ps.setString(5, T5123_F06.QY.name());
                
                try (ResultSet rSet = ps.executeQuery())
                {
                    if (rSet.next())
                    {
                        xyrzTotal.byrztg = rSet.getInt(1);
                    }
                }
            }
            
            try (PreparedStatement ps =
                conn.prepareStatement("SELECT COUNT(T6120.F01) FROM S61.T6120,S51.T5123 WHERE T6120.F02=T5123.F01 AND T6120.F01=? AND T5123.F03=? AND T5123.F04=? AND T6120.F05=? AND T5123.F06=?"))
            {
                ps.setInt(1, userId);
                ps.setString(2, T5123_F03.F.name());
                ps.setString(3, T5123_F04.QY.name());
                ps.setString(4, T6120_F05.TG.name());
                ps.setString(5, T5123_F06.QY.name());
                
                try (ResultSet rSet = ps.executeQuery())
                {
                    if (rSet.next())
                    {
                        xyrzTotal.kxrztg = rSet.getInt(1);
                    }
                }
            }
        }
        return xyrzTotal;
    }
    
    @Override
    public Attestation[] needAttestation(int userId)
        throws Throwable
    {
        if (userId <= 0)
        {
            return null;
        }
        
        ArrayList<Attestation> list = null;
        FileStore fileStore = serviceResource.getResource(FileStore.class);
        try (Connection conn = getConnection())
        {
            try (PreparedStatement ps =
                conn.prepareStatement("SELECT T6120.F07,T5123.F02,T6120.F04,T6120.F05,T6120.F06 FROM S61.T6120 LEFT JOIN S51.T5123 ON T6120.F02=T5123.F01 WHERE T6120.F01=? AND T5123.F03=? AND T5123.F04=? AND T5123.F06=? ORDER BY T6120.F06 DESC "))
            {
                ps.setInt(1, userId);
                ps.setString(2, T5123_F03.S.name());
                ps.setString(3, T5123_F04.QY.name());
                ps.setString(4, T5123_F06.QY.name());
                
                try (ResultSet rs = ps.executeQuery())
                {
                    while (rs.next())
                    {
                        Attestation attestation = new Attestation();
                        attestation.id = rs.getInt(1);
                        attestation.attestationName = rs.getString(2);
                        attestation.creditGrades = rs.getInt(3);
                        attestation.attestationState = EnumParser.parse(T6120_F05.class, rs.getString(4));
                        attestation.attestationTime = rs.getTimestamp(5);
                        
                        try (PreparedStatement ps2 =
                            conn.prepareStatement("SELECT T6122.F01, T6122.F02 FROM S61.T6122 WHERE T6122.F01=?"))
                        {
                            ps2.setInt(1, attestation.id);
                            
                            String ids = "";
                            String fileUrls = "";
                            
                            try (ResultSet rs2 = ps2.executeQuery())
                            {
                                while (rs2.next())
                                {
                                    if (StringHelper.isEmpty(ids))
                                    {
                                        ids = rs2.getInt(1) + "";
                                        fileUrls =
                                            StringHelper.isEmpty(rs2.getString(2)) ? ""
                                                : fileStore.getURL(rs2.getString(2));
                                    }
                                    else
                                    {
                                        ids = ids + "," + rs2.getInt(1);
                                        fileUrls =
                                            fileUrls
                                                + ","
                                                + (StringHelper.isEmpty(rs2.getString(2)) ? ""
                                                    : fileStore.getURL(rs2.getString(2)));
                                    }
                                }
                            }
                            
                            attestation.attachments = StringUtils.split(ids, ",");
                            attestation.fileUrls = StringUtils.split(fileUrls, ",");
                        }
                        
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(attestation);
                    }
                }
            }
        }
        return list == null ? null : list.toArray(new Attestation[list.size()]);
    }
    
    @Override
    public Attestation[] notNeedAttestation(int userId)
        throws Throwable
    {
        if (userId <= 0)
        {
            return null;
        }
        
        ArrayList<Attestation> list = null;
        FileStore fileStore = serviceResource.getResource(FileStore.class);
        try (Connection conn = getConnection())
        {
            try (PreparedStatement ps =
                conn.prepareStatement("SELECT T6120.F07,T5123.F02,T6120.F04,T6120.F05,T6120.F06 FROM S61.T6120 INNER JOIN S51.T5123 ON T6120.F02=T5123.F01 WHERE T6120.F01=? AND T5123.F03=? AND T5123.F04=? AND T5123.F06=? "))
            {
                ps.setInt(1, userId);
                ps.setString(2, T5123_F03.F.name());
                ps.setString(3, T5123_F04.QY.name());
                ps.setString(4, T5123_F06.QY.name());
                
                try (ResultSet rs = ps.executeQuery())
                {
                    while (rs.next())
                    {
                        Attestation attestation = new Attestation();
                        attestation.id = rs.getInt(1);
                        attestation.attestationName = rs.getString(2);
                        attestation.creditGrades = rs.getInt(3);
                        attestation.attestationState = EnumParser.parse(T6120_F05.class, rs.getString(4));
                        attestation.attestationTime = rs.getTimestamp(5);
                        
                        try (PreparedStatement ps2 =
                            conn.prepareStatement("SELECT T6122.F01,T6122.F02 FROM S61.T6122 WHERE T6122.F01=?"))
                        {
                            ps2.setInt(1, attestation.id);
                            
                            String ids = "";
                            String fileUrls = "";
                            try (ResultSet rs2 = ps2.executeQuery())
                            {
                                while (rs2.next())
                                {
                                    if (StringHelper.isEmpty(ids))
                                    {
                                        ids = rs2.getInt(1) + "";
                                        fileUrls =
                                            StringHelper.isEmpty(rs2.getString(2)) ? ""
                                                : fileStore.getURL(rs2.getString(2));
                                    }
                                    else
                                    {
                                        ids = ids + "," + rs2.getInt(1);
                                        fileUrls =
                                            fileUrls
                                                + ","
                                                + (StringHelper.isEmpty(rs2.getString(2)) ? ""
                                                    : fileStore.getURL(rs2.getString(2)));
                                    }
                                }
                            }
                            
                            attestation.attachments = StringUtils.split(ids, ",");
                            attestation.fileUrls = StringUtils.split(fileUrls, ",");
                        }
                        
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(attestation);
                    }
                }
            }
        }
        return list == null ? null : list.toArray(new Attestation[list.size()]);
    }
    
    @Override
    public T6122 getAttachment(int id)
        throws Throwable
    {
        if (id <= 0)
        {
            return null;
        }
        
        T6122 record = null;
        
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F02, F03, F04, F05 FROM S61.T6122 WHERE T6122.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, id);
                try (ResultSet rs = pstmt.executeQuery())
                {
                    if (rs.next())
                    {
                        record = new T6122();
                        record.F02 = rs.getString(1);
                        record.F03 = rs.getInt(2);
                        record.F04 = rs.getTimestamp(3);
                        record.F05 = rs.getString(4);
                    }
                }
            }
            
        }
        return record;
    }
    
    @Override
    public void rztg(int id, String desc)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                Timestamp timestamp = getCurrentTimestamp(connection);
                execute(connection,
                    "UPDATE S61.T6121 SET F04 = ?, F05 = ?, F06 = ?, F07 = ? WHERE F01 = ?",
                    desc,
                    T6121_F05.TG.name(),
                    serviceResource.getSession().getAccountId(),
                    timestamp,
                    id);
                execute(connection,
                    "UPDATE S61.T6120 SET F03 = F03+1, F04 = ?, F05 = ?, F06 =  ? WHERE F07 = ?",
                    getRzfc(id, connection),
                    T6120_F05.TG.name(),
                    timestamp,
                    id);
                writeLog(connection, "操作日志", "认证项审核通过");
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    @Override
    public void rzbtg(int id, String desc)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                Timestamp timestamp = getCurrentTimestamp(connection);
                execute(connection,
                    "UPDATE S61.T6121 SET F04 = ?, F05 = ?, F06 = ?, F07 = ? WHERE F01 = ?",
                    desc,
                    T6121_F05.BTG.name(),
                    serviceResource.getSession().getAccountId(),
                    timestamp,
                    id);
                execute(connection,
                    "UPDATE S61.T6120 SET F03 = F03+1, F05 = ?, F06 =  ? WHERE F07 = ?",
                    T6120_F05.BTG.name(),
                    timestamp,
                    id);
                writeLog(connection, "操作日志", "认证项审核不通过");
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    /**
     * 通过有效认证ID得到认证分数
     * 
     * @param id
     * @return
     * @throws SQLException
     */
    private int getRzfc(int id, Connection connection)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT T5123.F05 AS F01 FROM S51.T5123 INNER JOIN S61.T6120 ON T5123.F01 = T6120.F02 WHERE T6120.F07 = ? LIMIT 1"))
        {
            pstmt.setInt(1, id);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    return resultSet.getInt(1);
                }
            }
        }
        return 0;
    }
    
    @Override
    public Rzxx[] getRzxx(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            ArrayList<Rzxx> list = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT T5123.F02 AS F01, T5123.F03 AS F02, T5123.F05 AS F03, T6120.F03 AS F04, T6120.F05 AS F05, T6120.F06 AS F06, T6120.F07 AS F07,T6120.F04 AS F08,T6120.F02 AS F09,T6121.F01 AS F10 FROM S51.T5123 INNER JOIN S61.T6120 ON T5123.F01 = T6120.F02 LEFT JOIN S61.T6121 ON T6121.F01 = T6120.F07 WHERE T5123.F04 = ? AND T6120.F01 = ? ORDER BY FIELD(T6120.F05,'DSH','WYZ','BTG','TG'),T6120.F06 DESC"))
            {
                pstmt.setString(1, T5123_F04.QY.name());
                pstmt.setInt(2, id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        Rzxx record = new Rzxx();
                        record.lxmc = resultSet.getString(1);
                        record.mustRz = T5123_F03.parse(resultSet.getString(2));
                        record.fs = resultSet.getInt(3);
                        record.rzcs = resultSet.getInt(4);
                        record.status = T6120_F05.parse(resultSet.getString(5));
                        record.time = resultSet.getTimestamp(6);
                        record.yxjlID = resultSet.getInt(7);
                        record.ds = resultSet.getInt(8);
                        record.rzID = resultSet.getInt(9);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                }
                return ((list == null || list.size() == 0) ? null : list.toArray(new Rzxx[list.size()]));
            }
        }
    }
    
    @Override
    public String[] getFileCodes(int rzjlId)
        throws Throwable
    {
        ArrayList<String> lists = null;
        FileStore fileStore = serviceResource.getResource(FileStore.class);
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT T6122.F02 FROM S61.T6122 WHERE T6122.F01 = ?"))
            {
                pstmt.setInt(1, rzjlId);
                try (ResultSet rs = pstmt.executeQuery())
                {
                    while (rs.next())
                    {
                        if (lists == null)
                        {
                            lists = new ArrayList<>();
                        }
                        lists.add(StringHelper.isEmpty(rs.getString(1)) ? "" : fileStore.getURL(rs.getString(1)));
                    }
                }
            }
        }
        return ((lists == null || lists.size() == 0) ? null : lists.toArray(new String[lists.size()]));
        
    }
    
    @Override
    public T6121[] rzjl(int id, int yhId)
        throws Throwable
    {
        ArrayList<T6121> list = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT T6121.F01 ,T6121.F04,T6121.F05,T6121.F06,T6121.F07,T5123.F02 AS F08 FROM S61.T6121 LEFT JOIN S51.T5123  ON S51.T5123.F01 = S61.T6121.F03 WHERE T6121.F02 = ? ORDER BY F07 DESC,F08 ASC "))
            {
                // pstmt.setInt(1, id);
                pstmt.setInt(1, yhId);
                try (ResultSet rs = pstmt.executeQuery())
                {
                    while (rs.next())
                    {
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        T6121 entity = new T6121();
                        entity.F01 = rs.getInt(1);
                        entity.F04 = rs.getString(2);
                        entity.F05 = T6121_F05.parse(rs.getString(3));
                        entity.F06 = rs.getInt(4);
                        entity.F07 = rs.getTimestamp(5);
                        entity.F08 = rs.getString(6);
                        list.add(entity);
                    }
                }
            }
            return ((list == null || list.size() == 0) ? null : list.toArray(new T6121[list.size()]));
        }
        
    }
    
}
