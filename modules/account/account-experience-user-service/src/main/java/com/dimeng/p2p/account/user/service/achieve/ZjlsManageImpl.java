package com.dimeng.p2p.account.user.service.achieve;

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
import java.util.Date;
import java.util.List;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.OrderType;
import com.dimeng.p2p.S61.entities.T6130;
import com.dimeng.p2p.S61.entities.T6131;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S61.enums.T6102_F10;
import com.dimeng.p2p.S61.enums.T6130_F09;
import com.dimeng.p2p.S61.enums.T6131_F07;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.account.user.service.ZjlsManage;
import com.dimeng.p2p.account.user.service.entity.CapitalLs;
import com.dimeng.p2p.account.user.service.entity.Order;
import com.dimeng.util.StringHelper;
import com.dimeng.util.io.CVSWriter;
import com.dimeng.util.parser.DateParser;
import com.dimeng.util.parser.DateTimeParser;
import com.dimeng.util.parser.EnumParser;

public class ZjlsManageImpl extends AbstractAccountService implements ZjlsManage
{
    
    public ZjlsManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public PagingResult<CapitalLs> searchLs(int tradingType, Date startTime, Date endTime, T6101_F03 zhlx, Paging paging)
        throws Throwable
    {
        StringBuilder builder =
            new StringBuilder(
                "SELECT T6102.F01 AS F01, T6102.F02 AS F02, T6102.F03 AS F03, T6102.F04 AS F04, T6102.F05 AS F05, T6102.F06 AS F06, T6102.F07 AS F07, T6102.F08 AS F08, T6102.F09 AS F09, T6102.F10 AS F10, T6102.F11 AS F11, T6101.F02 AS F12, T6102.F12 AS F13, T5122.F02 AS F14 FROM S61.T6102 INNER JOIN S61.T6101 ON T6102.F02 = T6101.F01 INNER JOIN S51.T5122 ON T5122.F01 = T6102.F03 WHERE T6101.F02 = ? ");
        ArrayList<Object> params = new ArrayList<>();
        params.add(serviceResource.getSession().getAccountId());
        {
            if (tradingType > 0)
            {
                builder.append(" AND T6102.F03=?");
                params.add(tradingType);
            }
            if (startTime != null)
            {
                builder.append(" AND DATE(T6102.F05)>=?");
                params.add(DateParser.format(startTime));
            }
            if (endTime != null)
            {
                builder.append(" AND DATE(T6102.F05)<=?");
                params.add(DateParser.format(endTime));
            }
            if (zhlx != null)
            {
                builder.append(" AND T6101.F03=?");
                params.add(zhlx);
            }
        }
        
        builder.append(" ORDER BY T6102.F01 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<CapitalLs>()
            {
                @Override
                public CapitalLs[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<CapitalLs> list = null;
                    while (resultSet.next())
                    {
                        CapitalLs record = new CapitalLs();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getInt(3);
                        record.F04 = resultSet.getInt(4);
                        record.F05 = resultSet.getTimestamp(5);
                        record.F06 = resultSet.getBigDecimal(6);
                        record.F07 = resultSet.getBigDecimal(7);
                        record.F08 = resultSet.getBigDecimal(8);
                        record.F09 = resultSet.getString(9);
                        record.F10 = T6102_F10.parse(resultSet.getString(10));
                        record.F11 = resultSet.getTimestamp(11);
                        record.F12 = resultSet.getInt(12);
                        record.F17 = resultSet.getInt(13);
                        record.tradeType = resultSet.getString(14);
                        record.F18 = DateTimeParser.format(record.F05, "yyyy-MM-dd HH:mm:ss");
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return list == null || list.size() == 0 ? null : list.toArray(new CapitalLs[list.size()]);
                }
            }, paging, builder.toString(), params);
        }
    }
    
    @Override
    public BigDecimal getZhje(T6101_F03 f03)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F06 FROM S61.T6101 WHERE T6101.F02 = ? AND T6101.F03 = ? LIMIT 1"))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                pstmt.setString(2, f03.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return resultSet.getBigDecimal(1);
                    }
                }
            }
        }
        return new BigDecimal(0);
    }
    
    @Override
    public BigDecimal getCz()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT IFNULL(SUM(T6102.F06),0) AS F01 FROM S61.T6102 INNER JOIN S61.T6101 ON T6102.F02 = T6101.F01 WHERE T6101.F03 = 'WLZH' AND (T6102.F03 = ? OR T6102.F03= ?) AND T6101.F02 = ?"))
            {
                pstmt.setInt(1, FeeCode.CZ);
                pstmt.setInt(2, FeeCode.CZ_XX);
                pstmt.setInt(3, serviceResource.getSession().getAccountId());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return resultSet.getBigDecimal(1);
                    }
                }
            }
        }
        return new BigDecimal(0);
    }
    
    @Override
    public BigDecimal getTx()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT IFNULL(SUM(F04),0) AS F01 FROM S61.T6130 WHERE T6130.F02 = ? AND T6130.F09 = ?"))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                pstmt.setString(2, T6130_F09.YFK.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return resultSet.getBigDecimal(1);
                    }
                }
            }
        }
        return new BigDecimal(0);
    }
    
    @Override
    public BigDecimal getTGTx() throws Throwable {
    	try (Connection connection = getConnection()){
            try (PreparedStatement pstmt = connection.prepareStatement("SELECT IFNULL(SUM(T6503.F03),0) AS F01 FROM S65.T6501 INNER JOIN S65.T6503 ON T6501.F01=T6503.F01 WHERE T6503.F02=? AND T6501.F02=? AND T6501.F03=? ")){
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                pstmt.setInt(2, OrderType.WITHDRAW.orderType());
                pstmt.setString(3, T6501_F03.CG.name());
                try (ResultSet resultSet = pstmt.executeQuery()){
                    if (resultSet.next()){
                        return resultSet.getBigDecimal(1);
                    }
                }
            }
        }
    	return new BigDecimal(0);
    }
    
    
    @Override
    public void export(CapitalLs[] records, OutputStream outputStream, String charset)
        throws Throwable
    {
        if (outputStream == null)
        {
            return;
        }
        if (records == null)
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
            writer.write("时间");
            writer.write("类型明细");
            writer.write("收入(元)");
            writer.write("支出(元)");
            writer.write("结余(元)");
            writer.write("备注");
            writer.newLine();
            int i = 1;
            for (CapitalLs record : records)
            {
                writer.write(i);
                writer.write(DateTimeParser.format(record.F05) + "\t");
                writer.write(record.tradeType);
                writer.write(record.F06.toString());
                writer.write(record.F07.toString());
                writer.write(record.F08.toString());
                writer.write(record.F09);
                writer.newLine();
                i++;
            }
        }
        
    }
    
    private String get(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F02 FROM S51.T5122 WHERE T5122.F01 = ? LIMIT 1"))
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
        return "";
    }
    
    @Override
    public PagingResult<T6131> searchCz(T6131_F07 stauts, Timestamp startTime, Timestamp endTime, Paging paging)
        throws Throwable
    {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT F03, F06, F07, F08 FROM S61.T6131 WHERE T6131.F02 = ? ");
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(serviceResource.getSession().getAccountId());
        if (stauts != null)
        {
            sql.append(" AND F07 = ?");
            parameters.add(stauts.name());
        }
        if (startTime != null)
        {
            sql.append(" AND DATE(F06) >= ?");
            parameters.add(startTime);
        }
        if (endTime != null)
        {
            sql.append(" AND DATE(F06) <= ?");
            parameters.add(endTime);
        }
        sql.append(" ORDER BY F01");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<T6131>()
            {
                
                @Override
                public T6131[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<T6131> list = null;
                    while (resultSet.next())
                    {
                        T6131 record = new T6131();
                        record.F03 = resultSet.getBigDecimal(1);
                        record.F06 = resultSet.getTimestamp(2);
                        record.F07 = T6131_F07.parse(resultSet.getString(3));
                        record.F08 = resultSet.getString(4);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return ((list == null || list.size() == 0) ? null : list.toArray(new T6131[list.size()]));
                }
            }, paging, sql.toString(), parameters);
        }
    }
    
    @Override
    public PagingResult<T6130> searchTx(T6130_F09 stauts, Timestamp startTime, Timestamp endTime, Paging paging)
        throws Throwable
    {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT F03, F04, F06, F07, F08, F09, F12 FROM S61.T6130 WHERE T6130.F02 = ? ");
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(serviceResource.getSession().getAccountId());
        if (stauts != null)
        {
            sql.append(" AND F09 = ?");
            parameters.add(stauts.name());
        }
        if (startTime != null)
        {
            sql.append(" AND DATE(F08) >= ?");
            parameters.add(startTime);
        }
        if (endTime != null)
        {
            sql.append(" AND DATE(F08) <= ?");
            parameters.add(endTime);
        }
        sql.append(" ORDER BY F01");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<T6130>()
            {
                
                @Override
                public T6130[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<T6130> list = null;
                    while (resultSet.next())
                    {
                        T6130 record = new T6130();
                        record.F03 = resultSet.getInt(1);
                        record.F04 = resultSet.getBigDecimal(2);
                        record.F06 = resultSet.getBigDecimal(3);
                        record.F07 = resultSet.getBigDecimal(4);
                        record.F08 = resultSet.getTimestamp(5);
                        record.F09 = T6130_F09.parse(resultSet.getString(6));
                        record.F12 = resultSet.getString(7);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return ((list == null || list.size() == 0) ? null : list.toArray(new T6130[list.size()]));
                }
            }, paging, sql.toString(), parameters);
        }
    }
    
    /**
     * 手机版的交易流水
     * @param type 0全部，1支出，2收入
     * @param tradingType
     * @param startTime
     * @param endTime
     * @param zhlx
     * @param paging
     * @return
     * @throws Throwable
     */
    @Override
    public PagingResult<CapitalLs> mobileSearchLs(int type, int tradingType, Date startTime, Date endTime,
        T6101_F03 zhlx, Paging paging)
        throws Throwable
    {
        StringBuilder builder =
            new StringBuilder(
            		"SELECT T6102.F01 AS F01, T6102.F02 AS F02, T6102.F03 AS F03, T6102.F04 AS F04, T6102.F05 AS F05, T6102.F06 AS F06, T6102.F07 AS F07, T6102.F08 AS F08, T6102.F09 AS F09, T6102.F10 AS F10, T6102.F11 AS F11, T6101.F02 AS F12, T6102.F12 AS F13, T5122.F02 AS F14 FROM S61.T6102 INNER JOIN S61.T6101 ON T6102.F02 = T6101.F01 INNER JOIN S51.T5122 ON T5122.F01 = T6102.F03 WHERE T6101.F02 = ?");
        ArrayList<Object> params = new ArrayList<>();
        params.add(serviceResource.getSession().getAccountId());
        {
            if (type > 0 && type == 1)
            {
                builder.append(" AND T6102.F07>0");
            }
            else if (type > 0 && type == 2)
            {
                builder.append(" AND T6102.F06>0");
            }
            if (tradingType > 0)
            {
                builder.append(" AND T6102.F03=?");
                params.add(tradingType);
            }
            if (startTime != null)
            {
                builder.append(" AND DATE(T6102.F05)>=?");
                params.add(DateParser.format(startTime));
            }
            if (endTime != null)
            {
                builder.append(" AND DATE(T6102.F05)<=?");
                params.add(DateParser.format(endTime));
            }
            if (zhlx != null)
            {
                builder.append(" AND T6101.F03=?");
                params.add(zhlx);
            }
        }
        
        builder.append(" ORDER BY T6102.F01 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<CapitalLs>()
            {
                @Override
                public CapitalLs[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<CapitalLs> list = null;
                    while (resultSet.next())
                    {
                        CapitalLs record = new CapitalLs();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getInt(3);
                        record.F04 = resultSet.getInt(4);
                        record.F05 = resultSet.getTimestamp(5);
                        record.F06 = resultSet.getBigDecimal(6);
                        record.F07 = resultSet.getBigDecimal(7);
                        record.F08 = resultSet.getBigDecimal(8);
                        record.F09 = resultSet.getString(9);
                        record.F10 = T6102_F10.parse(resultSet.getString(10));
                        record.F11 = resultSet.getTimestamp(11);
                        record.F12 = resultSet.getInt(12);
                        record.F17 = resultSet.getInt(13);
                        record.tradeType = resultSet.getString(14);
                        record.F18 = DateTimeParser.format(record.F05, "yyyy-MM-dd HH:mm:ss");
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return list == null || list.size() == 0 ? null : list.toArray(new CapitalLs[list.size()]);
                }
            }, paging, builder.toString(), params);
        }
    }
    
    /**
    * 用户充值订单查询
    * @param paging
    * @return
    * @throws Throwable
    */
    @Override
    public PagingResult<Order> searchCZ(List<T6501_F03> stauts, String typeCode, Timestamp startTime,
        Timestamp endTime, Paging paging)
        throws Throwable
    {
        
        StringBuffer sql =
            new StringBuffer(
                "SELECT T6502.F01 AS F01, T6502.F03 AS F02, T6501.F03 AS F03, T6501.F04 AS F04, T6502.F07 AS F05 FROM S65.T6502 INNER JOIN S65.T6501 ON T6502.F01 = T6501.F01 WHERE T6502.F02 = ? ");
        ArrayList<Object> params = new ArrayList<>();
        params.add(serviceResource.getSession().getAccountId());
        {
            
            if (startTime != null)
            {
                sql.append(" AND DATE(T6501.F04)>=?");
                params.add(DateParser.format(startTime));
            }
            if (endTime != null)
            {
                sql.append(" AND DATE(T6501.F04)<=?");
                params.add(DateParser.format(endTime));
            }
            if (!StringHelper.isEmpty(typeCode))
            {
                sql.append(" AND T6501.F02=?");
                params.add(typeCode);
            }
            if (stauts != null && stauts.size() > 0)
            {
                sql.append(" AND (");
                for (int i = 0; i < stauts.size(); i++)
                {
                    if (i == 0)
                    {
                        sql.append(" T6501.F03=? ");
                    }
                    else
                    {
                        sql.append(" OR T6501.F03=? ");
                    }
                    params.add(stauts.get(i).name());
                }
                sql.append(" )");
            }
            
        }
        sql.append(" ORDER BY T6501.F04 DESC ");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<Order>()
            {
                @Override
                public Order[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    List<Order> orders = null;
                    while (resultSet.next())
                    {
                        if (orders == null)
                        {
                            orders = new ArrayList<>();
                        }
                        Order order = new Order();
                        order.id = resultSet.getInt(1);
                        order.amount = resultSet.getBigDecimal(2);
                        order.status = EnumParser.parse(T6501_F03.class, resultSet.getString(3));
                        order.orderTime = resultSet.getTimestamp(4);
                        order.payType = resultSet.getString(5);
                        orders.add(order);
                    }
                    return orders == null ? null : orders.toArray(new Order[orders.size()]);
                }
            }, paging, sql.toString(), params);
        }
    }
    
}
