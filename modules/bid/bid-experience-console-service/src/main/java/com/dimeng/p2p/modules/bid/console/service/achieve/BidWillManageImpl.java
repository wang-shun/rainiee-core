package com.dimeng.p2p.modules.bid.console.service.achieve;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.resource.ResourceNotFoundException;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S62.enums.T6281_F14;
import com.dimeng.p2p.S62.enums.T6281_F18;
import com.dimeng.p2p.S62.enums.T6281_F19;
import com.dimeng.p2p.S62.enums.T6281_F20;
import com.dimeng.p2p.S62.enums.T6282_F11;
import com.dimeng.p2p.S62.enums.T6282_F15;
import com.dimeng.p2p.S62.enums.T6282_F16;
import com.dimeng.p2p.S62.enums.T6282_F17;
import com.dimeng.p2p.modules.bid.console.service.BidWillManage;
import com.dimeng.p2p.modules.bid.console.service.entity.Grjkyx;
import com.dimeng.p2p.modules.bid.console.service.entity.Qyjkyx;
import com.dimeng.p2p.modules.bid.console.service.query.GrIntentionQuery;
import com.dimeng.p2p.modules.bid.console.service.query.QyIntentionQuery;
import com.dimeng.util.Formater;
import com.dimeng.util.StringHelper;
import com.dimeng.util.io.CVSWriter;
import com.dimeng.util.parser.DateTimeParser;
import com.dimeng.util.parser.IntegerParser;

public class BidWillManageImpl extends AbstractBidService implements BidWillManage
{
    
    public BidWillManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public PagingResult<Qyjkyx> searchEnterprise(QyIntentionQuery query, Paging paging)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11, F12, F13, F14, F15, F16, F17, F18, F19, F20, F22 FROM S62.T6281 WHERE 1=1 ");
        List<Object> parameters = new ArrayList<>();
        searchEnterpriseParameter(sql, query, parameters);
        sql.append(" ORDER BY T6281.F01 DESC ");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<Qyjkyx>()
            {
                
                @Override
                public Qyjkyx[] parse(ResultSet rs)
                    throws SQLException
                {
                    ArrayList<Qyjkyx> list = null;
                    while (rs.next())
                    {
                        Qyjkyx record = new Qyjkyx();
                        record.F01 = rs.getInt(1);
                        record.F02 = rs.getInt(2);
                        record.F03 = rs.getString(3);
                        record.F04 = rs.getString(4);
                        record.F05 = rs.getString(5);
                        record.F06 = rs.getString(6);
                        record.F07 = rs.getString(7);
                        record.F08 = rs.getString(8);
                        record.F09 = rs.getBigDecimal(9);
                        record.jklxmc = getJklx(rs.getInt(10));
                        record.szcs = getRegion(rs.getInt(11));
                        record.F12 = rs.getString(12);
                        record.F13 = rs.getString(13);
                        record.F14 = T6281_F14.parse(rs.getString(14));
                        record.F15 = rs.getInt(15);
                        record.F16 = rs.getTimestamp(16);
                        record.F17 = rs.getTimestamp(17);
                        record.F18 = T6281_F18.parse(rs.getString(18));
                        record.F19 = T6281_F19.parse(rs.getString(19));
                        record.F20 = T6281_F20.parse(rs.getString(20));
                        record.F22 = rs.getInt(21);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                        
                    }
                    return list == null || list.size() == 0 ? null : list.toArray(new Qyjkyx[list.size()]);
                    
                }
            }, paging, sql.toString(), parameters);
        }
    }
    
    @Override
    public BigDecimal searchEnterpriseAmount(QyIntentionQuery query)
        throws Throwable
    {
        StringBuilder sql = new StringBuilder("SELECT IFNULL(SUM(T6281.F09),0) AS F01 FROM S62.T6281 WHERE 1=1 ");
        ArrayList<Object> parameters = new ArrayList<>();
        searchEnterpriseParameter(sql, query, parameters);
        try (Connection connection = getConnection())
        {
            return selectBigDecimal(connection, sql.toString(), parameters);
        }
    }
    
    private void searchEnterpriseParameter(StringBuilder sql, QyIntentionQuery query, List<Object> parameters)
        throws ResourceNotFoundException, SQLException
    {
        if (query != null)
        {
            SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
            
            String f05 = query.getUserName();
            if (!StringHelper.isEmpty(f05))
            {
                sql.append(" AND T6281.F05 like ? ");
                parameters.add(getSQLConnectionProvider().allMatch(f05));
            }
            int F10 = query.getType();
            if (F10 > 0)
            {
                sql.append("AND T6281.F10 = ? ");
                parameters.add(F10);
            }
            String sheng = query.getSheng();
            String shi = query.getShi();
            String xian = query.getXian();
            if (!StringHelper.isEmpty(sheng) && StringHelper.isEmpty(shi) && StringHelper.isEmpty(xian))
            {
                sql.append(" AND T6281.F11 like ?");
                parameters.add(sqlConnectionProvider.allMatch(sheng.substring(0, 2)));
            }
            else if (!StringHelper.isEmpty(sheng) && !StringHelper.isEmpty(shi) && StringHelper.isEmpty(xian))
            {
                sql.append(" AND T6281.F11 like ?");
                parameters.add(sqlConnectionProvider.allMatch(shi.substring(0, 4)));
            }
            else if (!StringHelper.isEmpty(sheng) && !StringHelper.isEmpty(shi) && !StringHelper.isEmpty(xian))
            {
                sql.append(" AND T6281.F11 like ?");
                parameters.add(sqlConnectionProvider.allMatch(xian));
            }
            T6281_F14 f14 = query.getStatus();
            if (f14 != null)
            {
                sql.append("AND T6281.F14 = ? ");
                parameters.add(f14);
            }
            Timestamp timestamp = query.getCreateTimeStart();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T6281.F16) >= ?");
                parameters.add(timestamp);
            }
            timestamp = query.getCreateTimeEnd();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T6281.F16) <= ?");
                parameters.add(timestamp);
            }
        }
    }
    
    @Override
    public PagingResult<Grjkyx> searchPersonal(GrIntentionQuery query, Paging paging)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11, F12, F13, F14, F15, F16, F17, F19 FROM S62.T6282 WHERE 1=1 ");
        List<Object> parameters = new ArrayList<>();
        searchPersonalParameter(sql, query, parameters);
        sql.append(" ORDER BY T6282.F01 DESC ");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<Grjkyx>()
            {
                
                @Override
                public Grjkyx[] parse(ResultSet rs)
                    throws SQLException
                {
                    ArrayList<Grjkyx> list = null;
                    while (rs.next())
                    {
                        Grjkyx record = new Grjkyx();
                        record.F01 = rs.getInt(1);
                        record.F02 = rs.getInt(2);
                        record.F03 = rs.getString(3);
                        record.F04 = rs.getString(4);
                        record.F05 = rs.getString(5);
                        record.F06 = rs.getBigDecimal(6);
                        record.jklxmc = getJklx(rs.getInt(7));
                        record.szcs = getRegion(rs.getInt(8));
                        record.F09 = rs.getString(9);
                        record.F10 = rs.getString(10);
                        record.F11 = T6282_F11.parse(rs.getString(11));
                        record.F12 = rs.getInt(12);
                        record.F13 = rs.getTimestamp(13);
                        record.F14 = rs.getTimestamp(14);
                        record.F15 = T6282_F15.parse(rs.getString(15));
                        record.F16 = T6282_F16.parse(rs.getString(16));
                        record.F17 = T6282_F17.parse(rs.getString(17));
                        record.F19 = rs.getInt(18);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                        
                    }
                    return list == null || list.size() == 0 ? null : list.toArray(new Grjkyx[list.size()]);
                    
                }
            }, paging, sql.toString(), parameters);
        }
    }
    
    @Override
    public BigDecimal searchPersonalAmount(GrIntentionQuery query)
        throws Throwable
    {
        StringBuilder sql = new StringBuilder("SELECT IFNULL(SUM(T6282.F06),0) AS F01 FROM S62.T6282 WHERE 1=1 ");
        ArrayList<Object> parameters = new ArrayList<>();
        searchPersonalParameter(sql, query, parameters);
        try (Connection connection = getConnection())
        {
            return selectBigDecimal(connection, sql.toString(), parameters);
        }
    }
    
    private void searchPersonalParameter(StringBuilder sql, GrIntentionQuery query, List<Object> parameters)
        throws ResourceNotFoundException, SQLException
    {
        if (query != null)
        {
            SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
            
            String f03 = query.getUserName();
            if (!StringHelper.isEmpty(f03))
            {
                sql.append(" AND T6282.F03 LIKE ? ");
                parameters.add(getSQLConnectionProvider().allMatch(f03));
            }
            int f07 = query.getType();
            if (f07 > 0)
            {
                sql.append("AND T6282.F07 = ? ");
                parameters.add(f07);
            }
            String sheng = query.getSheng();
            String shi = query.getShi();
            String xian = query.getXian();
            if (!StringHelper.isEmpty(sheng) && StringHelper.isEmpty(shi) && StringHelper.isEmpty(xian))
            {
                sql.append(" AND T6282.F08 LIKE ?");
                parameters.add(sqlConnectionProvider.subfixMatch(sheng.substring(0, 2)));
            }
            else if (!StringHelper.isEmpty(sheng) && !StringHelper.isEmpty(shi) && StringHelper.isEmpty(xian))
            {
                sql.append(" AND T6282.F08 LIKE ?");
                parameters.add(sqlConnectionProvider.subfixMatch(shi.substring(0, 4)));
            }
            else if (!StringHelper.isEmpty(sheng) && !StringHelper.isEmpty(shi) && !StringHelper.isEmpty(xian))
            {
                sql.append(" AND T6282.F08 LIKE ?");
                parameters.add(sqlConnectionProvider.subfixMatch(xian));
            }
            T6282_F11 status = query.getStatus();
            if (status != null)
            {
                sql.append("AND T6282.F11 = ? ");
                parameters.add(status);
            }
            Timestamp timestamp = query.getCreateTimeStart();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T6282.F13) >= ?");
                parameters.add(timestamp);
            }
            timestamp = query.getCreateTimeEnd();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T6282.F13) <= ?");
                parameters.add(timestamp);
            }
        }
    }
    
    private String getHyzh(int id)
        throws SQLException
    {
        if (id <= 0)
        {
            return "";
        }
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement("SELECT F02 FROM S61.T6110 WHERE F01=?"))
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
        return "";
    }
    
    /**
     * 查询借款类型
     * 
     * @param id
     * @return
     * @throws SQLException
     */
    private String getJklx(int id)
        throws SQLException
    {
        if (id <= 0)
        {
            return "";
        }
        
        String str = "";
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement("SELECT F02 FROM S62.T6211 WHERE F01=?"))
            {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        str = rs.getString(1);
                    }
                }
            }
        }
        return str;
    }
    
    @Override
    public void export(Grjkyx[] grjkyxs, OutputStream outputStream, String charset)
        throws Throwable
    {
        if (outputStream == null)
        {
            return;
        }
        if (grjkyxs == null)
        {
            return;
        }
        if (StringHelper.isEmpty(charset))
        {
            charset = "GBK";
        }
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(outputStream, charset)))
        {
            CVSWriter writer = new CVSWriter(out);
            writer.write("序号");
            writer.write("联系人");
            writer.write("手机号码");
            writer.write("借款金额(元)");
            writer.write("借款期限");
            writer.write("借款类型");
            writer.write("所在城市");
            writer.write("筹款期限");
            writer.write("借款描述");
            writer.write("提交时间");
            writer.write("状态");
            writer.newLine();
            int index = 1;
            for (Grjkyx grjkyx : grjkyxs)
            {
                writer.write(index++);
                writer.write(grjkyx.F03);
                writer.write(grjkyx.F04 + "\t");
                writer.write(Formater.formatAmount(grjkyx.F06));
                writer.write(grjkyx.F19 + "个月");
                if (grjkyx.F15 == T6282_F15.F && grjkyx.F16 == T6282_F16.F && grjkyx.F17 == T6282_F17.F)
                {
                    writer.write("信用");
                }
                else
                {
                    writer.write(new StringBuilder().append(grjkyx.F15 == T6282_F15.S ? "抵押" : "")
                        .append(grjkyx.F16 == T6282_F16.S ? "实地认证" : "")
                        .append(grjkyx.F17 == T6282_F17.S ? "担保" : "")
                        .toString());
                }
                writer.write(grjkyx.szcs);
                writer.write(grjkyx.F09);
                writer.write(grjkyx.F10);
                writer.write(DateTimeParser.format(grjkyx.F13) + "\t");
                writer.write(grjkyx.F11 != null ? grjkyx.F11.getChineseName() : "");
                writer.newLine();
            }
        }
    }
    
    @Override
    public void export(Qyjkyx[] qyjkyxs, OutputStream outputStream, String charset)
        throws Throwable
    {
        if (outputStream == null)
        {
            return;
        }
        if (qyjkyxs == null)
        {
            return;
        }
        if (StringHelper.isEmpty(charset))
        {
            charset = "GBK";
        }
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(outputStream, charset)))
        {
            CVSWriter writer = new CVSWriter(out);
            writer.write("序号");
            writer.write("企业名称");
            writer.write("注册号");
            writer.write("联系人");
            writer.write("手机号码");
            writer.write("借款金额(元)");
            writer.write("借款期限");
            writer.write("借款类型");
            writer.write("所在城市");
            writer.write("筹款期限");
            writer.write("借款描述");
            writer.write("提交时间");
            writer.write("状态");
            writer.newLine();
            int index = 1;
            for (Qyjkyx qyjkyx : qyjkyxs)
            {
                writer.write(index++);
                writer.write(qyjkyx.F03);
                writer.write(qyjkyx.F04);
                writer.write(qyjkyx.F05);
                writer.write(qyjkyx.F06 + "\t");
                writer.write(Formater.formatAmount(qyjkyx.F09));
                writer.write(qyjkyx.F22 + "个月");
                if (qyjkyx.F18 == T6281_F18.F && qyjkyx.F19 == T6281_F19.F && qyjkyx.F20 == T6281_F20.F)
                {
                    writer.write("信用");
                }
                else
                {
                    writer.write(new StringBuilder().append(qyjkyx.F18 == T6281_F18.S ? "抵押" : "")
                        .append(qyjkyx.F19 == T6281_F19.S ? "实地认证" : "")
                        .append(qyjkyx.F20 == T6281_F20.S ? "担保" : "")
                        .toString());
                }
                writer.write(qyjkyx.szcs);
                writer.write(StringHelper.isEmpty(qyjkyx.F12) ? "无" : qyjkyx.F12);
                writer.write(qyjkyx.F13);
                writer.write(DateTimeParser.format(qyjkyx.F16) + "\t");
                writer.write(qyjkyx.F14 != null ? qyjkyx.F14.getChineseName() : "");
                writer.newLine();
            }
        }
    }
    
    @Override
    public Grjkyx getGrjkyx(int id)
        throws Throwable
    {
        if (id <= 0)
        {
            return null;
        }
        
        try (Connection connection = getConnection())
        {
            Grjkyx record = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11, F12, F13, F14, F15, F16, F17, F18, F19 FROM S62.T6282 WHERE T6282.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new Grjkyx();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getString(3);
                        record.F04 = resultSet.getString(4);
                        record.F05 = resultSet.getString(5);
                        record.F06 = resultSet.getBigDecimal(6);
                        record.jklxmc = getJklx(resultSet.getInt(7));
                        record.szcs = getRegion(resultSet.getInt(8));
                        record.F09 = resultSet.getString(9);
                        record.F10 = resultSet.getString(10);
                        record.F11 = T6282_F11.parse(resultSet.getString(11));
                        record.clr = getCzrmc(resultSet.getInt(12));
                        record.F13 = resultSet.getTimestamp(13);
                        record.F14 = resultSet.getTimestamp(14);
                        record.F15 = T6282_F15.parse(resultSet.getString(15));
                        record.F16 = T6282_F16.parse(resultSet.getString(16));
                        record.F17 = T6282_F17.parse(resultSet.getString(17));
                        record.F18 = resultSet.getString(18);
                        record.F19 = resultSet.getInt(19);
                        record.hyzh = getHyzh(record.F02);
                    }
                }
            }
            return record;
        }
    }
    
    @Override
    public Qyjkyx getQyjkyx(int id)
        throws Throwable
    {
        if (id <= 0)
        {
            return null;
        }
        
        try (Connection connection = getConnection())
        {
            Qyjkyx record = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11, F12, F13, F14, F15, F16, F17, F18, F19, F20, F21, F22 FROM S62.T6281 WHERE T6281.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new Qyjkyx();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getString(3);
                        record.F04 = resultSet.getString(4);
                        record.F05 = resultSet.getString(5);
                        record.F06 = resultSet.getString(6);
                        record.F07 = resultSet.getString(7);
                        record.F08 = resultSet.getString(8);
                        record.F09 = resultSet.getBigDecimal(9);
                        record.jklxmc = getJklx(resultSet.getInt(10));
                        record.szcs = getRegion(resultSet.getInt(11));
                        record.F12 = resultSet.getString(12);
                        record.F13 = resultSet.getString(13);
                        record.F14 = T6281_F14.parse(resultSet.getString(14));
                        record.clr = getCzrmc(resultSet.getInt(15));
                        record.F16 = resultSet.getTimestamp(16);
                        record.F17 = resultSet.getTimestamp(17);
                        record.F18 = T6281_F18.parse(resultSet.getString(18));
                        record.F19 = T6281_F19.parse(resultSet.getString(19));
                        record.F20 = T6281_F20.parse(resultSet.getString(20));
                        record.F21 = resultSet.getString(21);
                        record.F22 = resultSet.getInt(22);
                        record.hyzh = getHyzh(record.F02);
                    }
                }
            }
            return record;
        }
    }
    
    @Override
    public void grjkyxCl(int id, String disposeDesc)
        throws Throwable
    {
        if (id <= 0)
        {
            throw new ParameterException("借款意向不存在");
        }
        if (StringHelper.isEmpty(disposeDesc))
        {
            throw new ParameterException("处理结果不能为空.");
        }
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT T6282.F11 FROM S62.T6282 WHERE T6282.F01 = ? FOR UPDATE"))
            {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        T6282_F11 temp = T6282_F11.parse(rs.getString(1));
                        if (temp != T6282_F11.WCL)
                        {
                            throw new ParameterException("借款意向不为未处理状态.");
                        }
                    }
                    else
                    {
                        throw new ParameterException("借款意向不存在");
                    }
                }
            }
            try (PreparedStatement pstmt =
                connection.prepareStatement("UPDATE S62.T6282 SET T6282.F11 = ?,T6282.F18 = ?,T6282.F12 = ?,T6282.F14 = ? WHERE T6282.F01 = ?"))
            {
                pstmt.setString(1, T6282_F11.YCL.toString());
                pstmt.setString(2, disposeDesc);
                pstmt.setInt(3, serviceResource.getSession().getAccountId());
                pstmt.setTimestamp(4, getCurrentTimestamp(connection));
                pstmt.setInt(5, IntegerParser.parse(id));
                
                pstmt.executeUpdate();
            }
        }
    }
    
    @Override
    public void qyjkyxCl(int id, String disposeDesc)
        throws Throwable
    {
        if (id <= 0)
        {
            throw new ParameterException("借款意向不存在");
        }
        if (StringHelper.isEmpty(disposeDesc))
        {
            throw new ParameterException("处理结果不能为空.");
        }
        try (Connection connection = getConnection())
        {
            
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT T6281.F14 FROM S62.T6281 WHERE T6281.F01 = ? FOR UPDATE"))
            {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        T6281_F14 temp = T6281_F14.parse(rs.getString(1));
                        if (temp != T6281_F14.WCL)
                        {
                            throw new ParameterException("借款意向不为未处理状态.");
                        }
                    }
                    else
                    {
                        throw new ParameterException("借款意向不存在");
                    }
                }
            }
            
            try (PreparedStatement pstmt =
                connection.prepareStatement("UPDATE S62.T6281 SET T6281.F14 = ?,T6281.F21 = ?,T6281.F15 = ?,T6281.F17 = ? WHERE T6281.F01 = ?"))
            {
                pstmt.setString(1, T6282_F11.YCL.toString());
                pstmt.setString(2, disposeDesc);
                pstmt.setInt(3, serviceResource.getSession().getAccountId());
                pstmt.setTimestamp(4, getCurrentTimestamp(connection));
                pstmt.setInt(5, IntegerParser.parse(id));
                
                pstmt.executeUpdate();
            }
        }
    }
    
    /**
     * 查询后操作人名称
     * 
     * @param id
     * @return
     * @throws SQLException
     */
    private String getCzrmc(int id)
        throws SQLException
    {
        if (id <= 0)
        {
            return "";
        }
        
        String str = "";
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement("SELECT F02 FROM S71.T7110 WHERE F01=?"))
            {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        str = rs.getString(1);
                    }
                }
            }
        }
        return str;
    }
}
