package com.dimeng.p2p.account.user.service.achieve;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.dimeng.framework.http.upload.FileStore;
import com.dimeng.framework.http.upload.UploadFile;
import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.S51.entities.T5123;
import com.dimeng.p2p.S51.enums.T5123_F03;
import com.dimeng.p2p.S51.enums.T5123_F04;
import com.dimeng.p2p.S51.enums.T5123_F06;
import com.dimeng.p2p.S51.enums.T5124_F05;
import com.dimeng.p2p.S61.entities.T6120;
import com.dimeng.p2p.S61.enums.T6120_F05;
import com.dimeng.p2p.S61.enums.T6121_F05;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.account.user.service.RzxxManage;
import com.dimeng.p2p.account.user.service.entity.RzxxInfo;
import com.dimeng.p2p.account.user.service.entity.XyInfo;
import com.dimeng.p2p.account.user.service.entity.Xyjl;
import com.dimeng.util.StringHelper;

public class RzxxManageImpl extends AbstractAccountService implements RzxxManage
{
    
    public RzxxManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    public static class RzxxManageFactory implements ServiceFactory<RzxxManage>
    {
        @Override
        public RzxxManage newInstance(ServiceResource serviceResource)
        {
            return new RzxxManageImpl(serviceResource);
        }
    }
    
    @Override
    public RzxxInfo[] getGRInfo()
        throws Throwable
    {
        ArrayList<RzxxInfo> list = null;
        FileStore fileStore = serviceResource.getResource(FileStore.class);
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT T6120.F02 AS F01, T6120.F05 AS F02, T5123.F02 AS F03, T6120.F04 AS F04, T6121.F01 AS F05 FROM S61.T6120 INNER JOIN S51.T5123 ON T6120.F02 = T5123.F01 LEFT JOIN S61.T6121 ON T6121.F01 = T6120.F07 WHERE T6120.F01 = ? AND T5123.F03 = ? AND T5123.F04 = ? AND T5123.F06 = ? "))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                pstmt.setString(2, T5123_F03.F.name());
                pstmt.setString(3, T5123_F04.QY.name());
                pstmt.setString(4, T5123_F06.GR.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        RzxxInfo record = new RzxxInfo();
                        record.id = resultSet.getInt(1);
                        record.type = T6120_F05.parse(resultSet.getString(2));
                        record.name = resultSet.getString(3);
                        record.score = resultSet.getInt(4);
                        record.fileCodes = getFileCodes(resultSet.getInt(5));
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                }
            }
        }
        return ((list == null || list.size() == 0) ? null : list.toArray(new RzxxInfo[list.size()]));
    }
    
    @Override
    public void uploadFile(int rzID, String fileCode, UploadFile file)
        throws Throwable
    {
        
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                int acountID = serviceResource.getSession().getAccountId();
                try (InputStream inputStream = file.getInputStream())
                {
                    int rzjlID =
                        insert(connection,
                            "INSERT INTO S61.T6121 SET F02 = ?, F03 = ?, F05 = ?",
                            acountID,
                            rzID,
                            T6121_F05.DSH.name());
                    insert(connection,
                        "INSERT INTO S61.T6122 SET F01 = ?, F02 = ?, F03 = ?, F04 = ?, F05 = ?",
                        rzjlID,
                        fileCode,
                        inputStream.available(),
                        getCurrentTimestamp(connection),
                        file.getSuffix());
                    execute(connection,
                        "UPDATE S61.T6120 SET  F05 = ?, F07 = ? WHERE F01 = ? AND F02 = ?",
                        T6120_F05.DSH.name(),
                        rzjlID,
                        acountID,
                        rzID);
                    serviceResource.commit(connection);
                }
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    @Override
    public XyInfo getXyInfo()
        throws Throwable
    {
        XyInfo info = new XyInfo();
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F02, F03 FROM S61.T6116 WHERE T6116.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        info.score = resultSet.getInt(1);
                        info.xyed = resultSet.getBigDecimal(2);
                    }
                }
            }
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F02 FROM S51.T5124 WHERE T5124.F03 <= ? AND T5124.F04 >= ? AND T5124.F05 = ? LIMIT 1"))
            {
                pstmt.setInt(1, info.score);
                pstmt.setInt(2, info.score);
                pstmt.setString(3, T5124_F05.QY.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        info.xydj = resultSet.getString(1);
                    }
                }
            }
        }
        return info;
    }
    
    @Override
    public Xyjl getXyjl()
        throws Throwable
    {
        Xyjl info = new Xyjl();
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F02, F03 FROM S61.T6144 WHERE T6144.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        info.yqcs = resultSet.getInt(1);
                        info.yzyqcs = resultSet.getInt(2);
                    }
                }
            }
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT COUNT(*) FROM S62.T6230 WHERE T6230.F02 = ? AND T6230.F20 = ?"))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                pstmt.setString(2, T6230_F20.YJQ.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        info.hxbs = resultSet.getInt(1);
                    }
                }
            }
        }
        return info;
    }
    
    @Override
    public RzxxInfo[] getGRMustInfo()
        throws Throwable
    {
        ArrayList<RzxxInfo> list = null;
        FileStore fileStore = serviceResource.getResource(FileStore.class);
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT T6120.F02 AS F01, T6120.F05 AS F02, T5123.F02 AS F03, T6120.F04 AS F04, T6121.F01 AS F05 FROM S61.T6120 INNER JOIN S51.T5123 ON T6120.F02 = T5123.F01 LEFT JOIN S61.T6121 ON T6121.F01 = T6120.F07 WHERE T6120.F01 = ? AND T5123.F03 = ?  AND T5123.F04 = ? AND T5123.F06 = ? "))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                pstmt.setString(2, T5123_F03.S.name());
                pstmt.setString(3, T5123_F04.QY.name());
                pstmt.setString(4, T5123_F06.GR.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        RzxxInfo record = new RzxxInfo();
                        record.id = resultSet.getInt(1);
                        record.type = T6120_F05.parse(resultSet.getString(2));
                        record.name = resultSet.getString(3);
                        record.score = resultSet.getInt(4);
                        record.fileCodes = getFileCodes(resultSet.getInt(5));
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                }
            }
        }
        return ((list == null || list.size() == 0) ? null : list.toArray(new RzxxInfo[list.size()]));
    }
    
    /** {@inheritDoc} */
    
    @Override
    public T5123[] getT5123(T5123_F03 t5123_F03)
        throws Throwable
    {
        ArrayList<T5123> list = null;
        StringBuffer sql =
            new StringBuffer(
                "SELECT T5123.F01 AS F01, T5123.F02 AS F02, T5123.F03 AS F03, T5123.F04 AS F04, T5123.F05 AS F05 FROM S51.T5123");
        sql.append(" WHERE T5123.F04 = ? ");
        if (t5123_F03 != null)
        {
            sql.append(" AND T5123.F03 = ?");
        }
        sql.append(" ORDER BY T5123.F03");
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt = connection.prepareStatement(sql.toString()))
            {
                pstmt.setString(1, T5123_F04.QY.name());
                if (t5123_F03 != null)
                {
                    pstmt.setString(2, t5123_F03.name());
                }
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        T5123 record = new T5123();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getString(2);
                        record.F03 = T5123_F03.parse(resultSet.getString(3));
                        record.F04 = T5123_F04.parse(resultSet.getString(4));
                        record.F05 = resultSet.getInt(5);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                }
            }
        }
        return ((list == null || list.size() == 0) ? null : list.toArray(new T5123[list.size()]));
    }
    
    @Override
    public RzxxInfo[] getQYInfo()
        throws Throwable
    {
        ArrayList<RzxxInfo> list = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT T6120.F02 AS F01, T6120.F05 AS F02, T5123.F02 AS F03, T6120.F04 AS F04, T6121.F01 AS F05 FROM S61.T6120 INNER JOIN S51.T5123 ON T6120.F02 = T5123.F01 LEFT JOIN S61.T6121 ON T6121.F01 = T6120.F07 WHERE T6120.F01 = ? AND T5123.F03 = ? AND T5123.F04 = ? AND T5123.F06 = ? "))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                pstmt.setString(2, T5123_F03.F.name());
                pstmt.setString(3, T5123_F04.QY.name());
                pstmt.setString(4, T5123_F06.QY.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        RzxxInfo record = new RzxxInfo();
                        record.id = resultSet.getInt(1);
                        record.type = T6120_F05.parse(resultSet.getString(2));
                        record.name = resultSet.getString(3);
                        record.score = resultSet.getInt(4);
                        record.fileCodes = getFileCodes(resultSet.getInt(5));
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                }
            }
        }
        return ((list == null || list.size() == 0) ? null : list.toArray(new RzxxInfo[list.size()]));
    }
    
    private String[] getFileCodes(int rzjlId)
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
    public RzxxInfo[] getQYMustInfo()
        throws Throwable
    {
        ArrayList<RzxxInfo> list = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT T6120.F02 AS F01, T6120.F05 AS F02, T5123.F02 AS F03, T6120.F04 AS F04, T6121.F01 AS F05 FROM S61.T6120 INNER JOIN S51.T5123 ON T6120.F02 = T5123.F01 LEFT JOIN S61.T6121 ON T6121.F01 = T6120.F07 WHERE T6120.F01 = ? AND T5123.F03 = ?  AND T5123.F04 = ? AND T5123.F06 = ? "))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                pstmt.setString(2, T5123_F03.S.name());
                pstmt.setString(3, T5123_F04.QY.name());
                pstmt.setString(4, T5123_F06.QY.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        RzxxInfo record = new RzxxInfo();
                        record.id = resultSet.getInt(1);
                        record.type = T6120_F05.parse(resultSet.getString(2));
                        record.name = resultSet.getString(3);
                        record.score = resultSet.getInt(4);
                        record.fileCodes = getFileCodes(resultSet.getInt(5));
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                }
            }
        }
        return ((list == null || list.size() == 0) ? null : list.toArray(new RzxxInfo[list.size()]));
    }
    
    @Override
    public int insertRzjl(int rzID)
        throws Throwable
    {
        int rzjlID = 0;
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                int acountID = serviceResource.getSession().getAccountId();
                rzjlID =
                    insert(connection,
                        "INSERT INTO S61.T6121 SET F02 = ?, F03 = ?, F05 = ?",
                        acountID,
                        rzID,
                        T6121_F05.DSH.name());
                execute(connection,
                    "UPDATE S61.T6120 SET  F05 = ?, F07 = ? WHERE F01 = ? AND F02 = ?",
                    T6120_F05.DSH.name(),
                    rzjlID,
                    acountID,
                    rzID);
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
        return rzjlID;
    }
    
    @Override
    public void insertFile(int rzjlID, String fileCode, UploadFile file)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                try (InputStream inputStream = file.getInputStream())
                {
                    insert(connection,
                        "INSERT INTO S61.T6122 SET F01 = ?, F02 = ?, F03 = ?, F04 = ?, F05 = ?",
                        rzjlID,
                        fileCode,
                        inputStream.available(),
                        getCurrentTimestamp(connection),
                        file.getSuffix());
                    serviceResource.commit(connection);
                }
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
        
    }
    
    @Override
    public T6120 getRzxx(int rzxID)
        throws Throwable
    {
        T6120 t6120 = new T6120();
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F05, F07 FROM S61.T6120 WHERE T6120.F01 = ? AND T6120.F02 = ? LIMIT 1"))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                pstmt.setInt(2, rzxID);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        t6120.F05 = T6120_F05.parse(resultSet.getString(1));
                        t6120.F07 = resultSet.getInt(2);
                    }
                }
            }
        }
        return t6120;
    }
    
}
