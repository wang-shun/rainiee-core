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
import com.dimeng.p2p.S51.entities.T5125;
import com.dimeng.p2p.modules.base.console.service.XymbManage;
import com.dimeng.p2p.modules.base.console.service.entity.Xymb;

/**
 * @author guopeng
 * 
 */
public class XymbManageImpl extends AbstractInformationService implements XymbManage
{
    
    public XymbManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public PagingResult<T5125> srarch(Paging paging)
        throws Throwable
    {
        String sql = "SELECT F01, F02, F03,F04 FROM S51.T5125";
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<T5125>()
            {
                
                @Override
                public T5125[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<T5125> list = new ArrayList<>();
                    while (resultSet.next())
                    {
                        T5125 record = new T5125();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getString(3);
                        record.F04 = resultSet.getTimestamp(4);
                        list.add(record);
                    }
                    return list.toArray(new T5125[list.size()]);
                }
            }, paging, sql);
        }
    }
    
    @Override
    public void add(int id, String content)
        throws Throwable
    {
        if (id <= 0)
        {
            return;
        }
        
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                int version = 0;
                try (PreparedStatement pstmt =
                    connection.prepareStatement("SELECT F02 FROM S51.T5125 WHERE T5125.F01 = ? FOR UPDATE"))
                {
                    pstmt.setInt(1, id);
                    try (ResultSet resultSet = pstmt.executeQuery())
                    {
                        if (resultSet.next())
                        {
                            version = resultSet.getInt(1);
                        }
                    }
                }
                try (PreparedStatement pstmt =
                    connection.prepareStatement("UPDATE S51.T5125 SET F02 =F02+1 WHERE F01 = ?"))
                {
                    pstmt.setInt(1, id);
                    pstmt.execute();
                }
                try (PreparedStatement pstmt =
                    connection.prepareStatement("INSERT INTO S51.T5126 SET F01 = ?, F02 = ?, F03 = ?"))
                {
                    pstmt.setInt(1, id);
                    pstmt.setInt(2, version + 1);
                    pstmt.setString(3, rhtml(content));
                    pstmt.execute();
                }
                if (id == 1001)
                {
                    writeLog(connection, "操作日志", "协议模版内容修改(三方)");
                }
                else if (id == 1002)
                {
                    writeLog(connection, "操作日志", "协议模版内容修改(四方)");
                }
                else if (id == 2001)
                {
                    writeLog(connection, "操作日志", "协议模版内容修改(债券转让)");
                }
                else if (id == 2002)
                {
                    writeLog(connection, "操作日志", "协议模版内容修改(不良债券转让)");
                }
                else if (id == 5001)
                {
                    writeLog(connection, "操作日志", "协议模版内容修改(公益捐助)");
                }
                else if (id == 5002)
                {
                    writeLog(connection, "操作日志", "协议模版内容修改(垫付)");
                }
                else
                {
                    writeLog(connection, "操作日志", "协议模版内容修改");
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
    
    private String rhtml(String content)
    {
        String html = content;
        html = html.replace("&amp;", "&");
        html = html.replace("&lt;", "<");
        html = html.replace("&gt;", ">");
        html = html.replace("<!--", "</");
        html = html.replace("-->", ">");
        return html;
    }
    
    @Override
    public Xymb get(int id, int version)
        throws Throwable
    {
        if (id <= 0)
        {
            throw new ParameterException("参数不能为空");
        }
        Xymb record = new Xymb();
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT T5126.F01,T5126.F02,T5126.F03,T5126.F04,T5125.F03 FROM S51.T5126 RIGHT JOIN S51.T5125 ON T5126.F01=T5125.F01 WHERE T5126.F01=? AND T5126.F02=?"))
            {
                pstmt.setInt(1, id);
                pstmt.setInt(2, version);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.text = resultSet.getString(3);
                        record.F04 = resultSet.getTimestamp(4);
                        record.F03 = resultSet.getString(5);
                    }
                }
            }
        }
        return record;
    }
    
    @Override
    public void delete(int id)
        throws Throwable
    {
        if (id <= 0)
        {
            throw new ParameterException("参数不能为空");
        }
        
        try (Connection connection = getConnection())
        {
            execute(connection, "DELETE FROM S51.T5125 WHERE F01 = ?", id);
        }
    }
    
}
