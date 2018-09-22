package com.dimeng.p2p.modules.base.console.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S51.entities.T5128;
import com.dimeng.p2p.S51.enums.T5128_F05;
import com.dimeng.p2p.modules.base.console.service.JjrManage;

/**
 * @author tonglei
 * 
 */
public class JjrManageImpl extends AbstractInformationService implements JjrManage {

    public JjrManageImpl(ServiceResource serviceResource) {
        super(serviceResource);
    }

    @Override
    public PagingResult<T5128> srarch(Paging paging)
        throws Throwable
    {
        String sql = "SELECT F01, F02, F03, F04, F05, F06 FROM S51.T5128";
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<T5128>()
            {
                @Override
                public T5128[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<T5128> list = new ArrayList<>();
                    while (resultSet.next())
                    {
                        T5128 record = new T5128();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getString(2);
                        record.F03 = resultSet.getTimestamp(3);
                        record.F04 = resultSet.getInt(4);
                        record.F05 = T5128_F05.parse(resultSet.getString(5));
                        record.F06 = resultSet.getTimestamp(6);
                        list.add(record);
                    }
                    return list.toArray(new T5128[list.size()]);
                }
            }, paging, sql);
        }
    }

    @Override
    public void add(T5128 t5128) throws Throwable {
        T5128 t5128_exist = getHolidayByDate(t5128);
        if(t5128_exist != null){
            throw new ParameterException("节假日已经存在，请重新选择！");
        }
        try (Connection connection = getConnection()) {
            try (PreparedStatement pstmt = connection
                    .prepareStatement("INSERT INTO S51.T5128 SET T5128.F02 = ?, T5128.F03 = ?, T5128.F04 = ?, T5128.F05 = ?, T5128.F06 = ? ")) {
                pstmt.setString(1, t5128.F02);
                pstmt.setTimestamp(2, t5128.F03);
                pstmt.setInt(3, t5128.F04);
                pstmt.setString(4, T5128_F05.QY.name());
                pstmt.setTimestamp(5, getCurrentTimestamp(connection));
                pstmt.execute();
            }
        }
    }
    
    @Override
    public T5128 get(int id) throws Throwable {
        if (id <= 0) {
            throw new ParameterException("参数不能为空");
        }
        T5128 record = new T5128();
        try (Connection connection = getConnection()) {
            try (PreparedStatement pstmt = connection
                    .prepareStatement("SELECT T5128.F01,T5128.F02,T5128.F03,T5128.F04,T5128.F05,T5128.F06 FROM S51.T5128 WHERE T5128.F01 = ?")) {
                pstmt.setInt(1, id);
                try (ResultSet resultSet = pstmt.executeQuery()) {
                    if (resultSet.next()) {
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getString(2);
                        record.F03 = resultSet.getTimestamp(3);
                        record.F04 = resultSet.getInt(4);
                        record.F05 = T5128_F05.parse(resultSet.getString(5));
                        record.F06 = resultSet.getTimestamp(6);
                    }
                }
            }
        }
        return record;
    }

    @Override
    public void update(T5128 t5128)
        throws Throwable
    {
        try (Connection connection = getConnection()) {
            try (PreparedStatement pstmt = connection
                    .prepareStatement("UPDATE S51.T5128 SET T5128.F02 = ?, T5128.F03 = ?, T5128.F04 = ?, T5128.F05 = ?, T5128.F06 = ? WHERE T5128.F01 = ? ")) {
                pstmt.setString(1, t5128.F02);
                pstmt.setTimestamp(2, t5128.F03);
                pstmt.setInt(3, t5128.F04);
                pstmt.setString(4, T5128_F05.QY.name());
                pstmt.setTimestamp(5, getCurrentTimestamp(connection));
                pstmt.setInt(6, t5128.F01);
                pstmt.execute();
            }
        }
        
    }

    @Override
    public void update(int id, T5128_F05 f05)
        throws Throwable
    {
        try (Connection connection = getConnection()) {
            try (PreparedStatement pstmt = connection
                    .prepareStatement("UPDATE S51.T5128 SET T5128.F05 = ?, T5128.F06 = ? WHERE T5128.F01 = ? ")) {
                pstmt.setString(1, f05.name());
                pstmt.setTimestamp(2, getCurrentTimestamp(connection));
                pstmt.setInt(3, id);
                pstmt.execute();
            }
        }
    }

    @Override
    public T5128 getHolidayByDate(T5128 t5128) throws Throwable{
        T5128 record = null;
        try (Connection connection = getConnection()) {
            try (PreparedStatement pstmt = connection
                    .prepareStatement("SELECT T5128.F01,T5128.F02,T5128.F03,T5128.F04,T5128.F05,T5128.F06 FROM S51.T5128 WHERE T5128.F03 = ?")) {
                pstmt.setTimestamp(1, t5128.F03);
                try (ResultSet resultSet = pstmt.executeQuery()) {
                    if (resultSet.next()) {
                        record = new T5128();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getString(2);
                        record.F03 = resultSet.getTimestamp(3);
                        record.F04 = resultSet.getInt(4);
                        record.F05 = T5128_F05.parse(resultSet.getString(5));
                        record.F06 = resultSet.getTimestamp(6);
                    }
                }
            }
        }
        return record;
    }
}
