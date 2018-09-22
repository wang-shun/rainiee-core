/*
 * 文 件 名:  GyLoanProgresManageImpl.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  guomianyun
 * 修改时间:  2015年3月9日
 */
package com.dimeng.p2p.modules.bid.donation.service.achieve;

import com.dimeng.framework.resource.ResourceNotFoundException;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S62.entities.T6242;
import com.dimeng.p2p.S62.entities.T6243;
import com.dimeng.p2p.S62.entities.T6245;
import com.dimeng.p2p.S62.enums.T6242_F10;
import com.dimeng.p2p.S62.enums.T6242_F11;
import com.dimeng.p2p.S62.enums.T6245_F05;
import com.dimeng.p2p.repeater.donation.GyLoanProgresManage;
import com.dimeng.p2p.repeater.donation.entity.BidProgres;
import com.dimeng.p2p.repeater.donation.entity.GyLoan;
import com.dimeng.p2p.repeater.donation.query.ProgresQuery;
import com.dimeng.util.StringHelper;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 公益标进展信息
 * <功能详细描述>
 * 
 * @author  guomianyun
 * @version  [版本号, 2015年3月9日]
 */
public class GyLoanProgresManageImpl extends AbstractBidManage implements GyLoanProgresManage
{
    
    public GyLoanProgresManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public int add(T6245 entity)
        throws Throwable
    {
        if (entity == null || entity.F03 <= 0)
        {
            throw new LogicalException("请填写完整信息");
        }
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("INSERT INTO  S62.T6245 SET  F02 = ?,F03 = ?, F04 = ?,F05 = ?, F06 = ?, F07 = CURRENT_TIMESTAMP(), F08 = ?, F09 = ?",
                    PreparedStatement.RETURN_GENERATED_KEYS))
            {
                pstmt.setInt(1, entity.F02);
                pstmt.setInt(2, entity.F03);
                pstmt.setString(3, entity.F04);
                pstmt.setString(4, entity.F05 == null ? T6245_F05.F.name() : entity.F05.name());
                pstmt.setString(5, entity.F06);
                pstmt.setTimestamp(6, entity.F08);
                pstmt.setString(7, entity.F09);
                pstmt.execute();
                try (ResultSet resultSet = pstmt.getGeneratedKeys();)
                {
                    if (resultSet.next())
                    {
                        int id = resultSet.getInt(1);
                        return id;
                    }
                    return 0;
                }
            }
        }
    }
    
    @Override
    public int update(T6245 entity)
        throws Throwable
    {
        if (entity == null || entity.F03 <= 0)
        {
            throw new LogicalException("请填写完整信息");
        }
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("UPDATE  S62.T6245 SET   F04 = ?,F05 = ?, F06 = ?, F07 = CURRENT_TIMESTAMP(), F08 =? , F09 = ? WHERE F01=? AND F03=?"))
            {
                
                pstmt.setString(1, entity.F04);
                pstmt.setString(2, entity.F05 == null ? T6245_F05.F.name() : entity.F05.name());
                pstmt.setString(3, entity.F06);
                pstmt.setTimestamp(4, entity.F08);
                pstmt.setString(5, entity.F09);
                pstmt.setInt(6, entity.F01);
                pstmt.setInt(7, entity.F03);
                return pstmt.executeUpdate();//.execute();
            }
        }
    }
    
    @Override
    public int delete(int F01, int loanId)
        throws Throwable
    {
        if (F01 <= 0 || loanId <= 0)
        {
            throw new LogicalException("参数不能为空,请填写完整");
        }
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt = connection.prepareStatement("DELETE FROM S62.T6245 WHERE F01=? AND F03=?"))
            {
                pstmt.setInt(1, F01);
                pstmt.setInt(2, loanId);
                return pstmt.executeUpdate();//.execute();
            }
        }
    }
    
    @Override
    public T6245 get(int F01)
        throws Throwable
    {
        if (F01 <= 0)
        {
            throw new ParameterException("参数错误");
        }
        try (Connection connection = getConnection())
        {
            T6245 record = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT T6245.F01, T6245.F02, T6245.F03, T6245.F04, T6245.F05, T6245.F06, T6245.F07, T6245.F08, T6245.F09"
                    + "  FROM S62.T6245  WHERE T6245.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, F01);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new T6245();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getInt(3);
                        record.F04 = resultSet.getString(4);
                        record.F05 = T6245_F05.parse(resultSet.getString(5));
                        record.F06 = resultSet.getString(6);
                        record.F07 = resultSet.getTimestamp(7);
                        record.F08 = resultSet.getTimestamp(8);
                        record.F09 = resultSet.getString(9);
                    }
                }
            }
            return record;
        }
    }
    
    @Override
    public PagingResult<BidProgres> search(ProgresQuery query, Paging paging)
        throws Throwable
    {
        StringBuilder sb =
            new StringBuilder(
                "SELECT T6245.F01, T6245.F02, T6245.F03, T6245.F04, T6245.F05, T6245.F06, T6245.F07, T6245.F08,T6245.F09,T7110.F02 AS F10"
                    + "  FROM S62.T6245 INNER JOIN S71.T7110 ON T6245.F02=T7110.F01 INNER JOIN S62.T6242 ON T6245.F03=T6242.F01  WHERE 1=1 ");
        List<Object> parameters = new ArrayList<>();
        
        if (query != null)
        {
            int bid = query.getBidId();
            if (bid > 0)
            {
                sb.append(" AND T6245.F03 =?");
                parameters.add(bid);
            }
            
            Timestamp timestamp = query.getCreateTimeStart();
            if (timestamp != null)
            {
                sb.append(" AND DATE(T6245.F08) >=?");
                parameters.add(timestamp);
            }
            timestamp = query.getCreateTimeEnd();
            if (timestamp != null)
            {
                sb.append(" AND DATE(T6245.F08) <=?");
                parameters.add(timestamp);
            }
            String name = query.getSysName();
            if (!StringHelper.isEmpty(name))
            {
                sb.append(" AND T7110.F02 LIKE ?");
                parameters.add(getSQLConnectionProvider().allMatch(name));
            }
            //  T6242_F11 status = query.getStatus();
            //            if (status != null)
            //            {
            //                sb.append(" AND T6242_F11 =?");
            //                parameters.add(status);
            //            }
        }
        sb.append(" ORDER BY T6245.F07 DESC");
        try(Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<BidProgres>()
            {

                @Override
                public BidProgres[] parse(ResultSet resultSet)
                        throws SQLException
                {

                    List<BidProgres> bids = new ArrayList<BidProgres>();
                    BidProgres record = null;
                    while (resultSet.next())
                    {
                        record = new BidProgres();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getInt(3);
                        record.F04 = resultSet.getString(4);
                        record.F05 = T6245_F05.parse(resultSet.getString(5));
                        record.F06 = resultSet.getString(6);
                        record.F07 = resultSet.getTimestamp(7);
                        record.F08 = resultSet.getTimestamp(8);
                        record.F09 = resultSet.getString(9);
                        record.sysName = resultSet.getString(10);
                        bids.add(record);
                    }
                    return bids.toArray(new BidProgres[bids.size()]);
                }
            }, paging, sb.toString(), parameters);
        }
    }
    
    @Override
    public PagingResult<BidProgres> search4front(ProgresQuery query, Paging paging)
        throws Throwable
    {
        StringBuilder sb =
            new StringBuilder(
                "SELECT T6245.F01, T6245.F02, T6245.F03, T6245.F04, T6245.F05, T6245.F06, T6245.F07, T6245.F08,T6245.F09,T7110.F02 AS F10"
                    + "  FROM S62.T6245 INNER JOIN S71.T7110 ON T6245.F02=T7110.F01 INNER JOIN S62.T6242 ON T6245.F03=T6242.F01  WHERE 1=1 ");
        List<Object> parameters = new ArrayList<>();
        
        if (query != null)
        {
            int bid = query.getBidId();
            if (bid > 0)
            {
                sb.append(" AND T6245.F03 =?");
                parameters.add(bid);
            }
            
            Timestamp timestamp = query.getCreateTimeStart();
            if (timestamp != null)
            {
                sb.append(" AND DATE(T6245.F08) >=?");
                parameters.add(timestamp);
            }
            timestamp = query.getCreateTimeEnd();
            if (timestamp != null)
            {
                sb.append(" AND DATE(T6245.F08) <=?");
                parameters.add(timestamp);
            }
            String name = query.getSysName();
            if (!StringHelper.isEmpty(name))
            {
                sb.append(" AND T7110.F02 LIKE ?");
                parameters.add(getSQLConnectionProvider().allMatch(name));
            }
            //  T6242_F11 status = query.getStatus();
            //            if (status != null)
            //            {
            //                sb.append(" AND T6242_F11 =?");
            //                parameters.add(status);
            //            }
        }
        sb.append(" AND T6245.F05 ='S' ");//查询已发布的进展
        sb.append(" ORDER BY T6245.F08 DESC, T6245.F07 DESC");
        try(Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<BidProgres>()
            {

                @Override
                public BidProgres[] parse(ResultSet resultSet)
                        throws SQLException
                {

                    List<BidProgres> bids = new ArrayList<BidProgres>();
                    BidProgres record = null;
                    while (resultSet.next())
                    {
                        record = new BidProgres();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getInt(3);
                        record.F04 = resultSet.getString(4);
                        record.F05 = T6245_F05.parse(resultSet.getString(5));
                        record.F06 = resultSet.getString(6);
                        record.F07 = resultSet.getTimestamp(7);
                        record.F08 = resultSet.getTimestamp(8);
                        record.F09 = resultSet.getString(9);
                        record.sysName = resultSet.getString(10);
                        bids.add(record);
                    }
                    return bids.toArray(new BidProgres[bids.size()]);
                }
            }, paging, sb.toString(), parameters);
        }
    }
    
    @Override
    public PagingResult<GyLoan> searchList(ProgresQuery query, Paging paging)
        throws Throwable
    {
        StringBuilder sb =
            new StringBuilder(
                "SELECT T6242.F01, T6242.F02, T6242.F03, T6242.F04, T6242.F05, T6242.F06, T6242.F07, T6242.F08, T6242.F09, T6242.F10, T6242.F11, T6242.F12, T6242.F13, T6242.F14, "
                    + "T6242.F15, T6242.F16, T6242.F17, T6242.F18, T6242.F19, T6242.F20, T6242.F21,T6243.F02 AS F22,"
                    + "T6242.F22 AS F23, T6242.F23 AS F24,T7110.F02 AS F25,IFNULL(t2.c,0) AS F26  "
                    + "  FROM S62.T6242 INNER JOIN S62.T6243 ON T6242.F01=T6243.F01 INNER JOIN S71.T7110 ON T6242.F02= T7110.F01  "
                    + "LEFT JOIN (SELECT COUNT(T6245.F01) AS c,T6245.F03 FROM S62.T6245 LEFT JOIN S62.T6242 ON T6245.F03=T6242.F01 GROUP BY T6245.F03) AS t2 ON t2.F03=T6242.F01  WHERE T6242.F11 IN ('JKZ','YJZ') ");
        List<Object> parameters = new ArrayList<>();
        searchListParameter(sb, query, parameters);
        sb.append(" ORDER BY T6242.F15 DESC");
        try(Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<GyLoan>()
            {

                @Override
                public GyLoan[] parse(ResultSet resultSet)
                        throws SQLException
                {
                    List<GyLoan> bids = new ArrayList<GyLoan>();
                    GyLoan gyLoan = null;
                    T6242 record = null;
                    while (resultSet.next())
                    {
                        record = new T6242();
                        gyLoan = new GyLoan();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getString(3);
                        record.F04 = resultSet.getInt(4);
                        record.F05 = resultSet.getBigDecimal(5);
                        record.F06 = resultSet.getBigDecimal(6);
                        record.F07 = resultSet.getBigDecimal(7);
                        record.F08 = resultSet.getInt(8);
                        record.F09 = resultSet.getInt(9);
                        record.F10 = T6242_F10.parse(resultSet.getString(10));
                        record.F11 = T6242_F11.parse(resultSet.getString(11));
                        record.F12 = resultSet.getString(12);
                        record.F13 = resultSet.getTimestamp(13);
                        record.F14 = resultSet.getInt(14);
                        record.F15 = resultSet.getTimestamp(15);
                        record.F16 = resultSet.getTimestamp(16);
                        record.F17 = resultSet.getTimestamp(17);
                        record.F18 = resultSet.getTimestamp(18);
                        record.F19 = resultSet.getTimestamp(19);
                        record.F20 = resultSet.getTimestamp(20);
                        record.F21 = resultSet.getString(21);
                        if (!StringHelper.isEmpty(resultSet.getString(22)))
                        {
                            T6243 t6243 = new T6243();
                            t6243.F01 = resultSet.getInt(1);
                            t6243.F02 = resultSet.getString(22);
                            gyLoan.t6243 = t6243;
                        }
                        record.F22 = resultSet.getString(23);
                        record.F23 = resultSet.getInt(24);
                        gyLoan.sysName = resultSet.getString(25);
                        gyLoan.progres = resultSet.getInt(26);
                        gyLoan.t6242 = record;
                        bids.add(gyLoan);
                    }
                    return bids.toArray(new GyLoan[bids.size()]);
                }

            }, paging, sb.toString(), parameters);
        }
    }
    
    @Override
    public BigDecimal searchListAmount(ProgresQuery query)
        throws Throwable
    {
        StringBuilder sb =
            new StringBuilder(
                "SELECT IFNULL(SUM(T6242.F05),0) AS F01"
                    + " FROM S62.T6242 INNER JOIN S62.T6243 ON T6242.F01=T6243.F01 INNER JOIN S71.T7110 ON T6242.F02= T7110.F01 "
                    + "LEFT JOIN (SELECT COUNT(T6245.F01) AS c,T6245.F03 FROM S62.T6245 LEFT JOIN S62.T6242 ON T6245.F03=T6242.F01 GROUP BY T6245.F03) AS t2 ON t2.F03=T6242.F01  WHERE T6242.F11 IN ('JKZ','YJZ') ");
        ArrayList<Object> parameters = new ArrayList<>();
        searchListParameter(sb, query, parameters);
        try (Connection connection = getConnection())
        {
            return selectBigDecimal(connection, sb.toString(), parameters);
        }
    }

    private void searchListParameter(StringBuilder sb, ProgresQuery query, List<Object> parameters)
        throws ResourceNotFoundException, SQLException
    {
        if (query != null)
        {
            String title = query.getLoanTitle();
            if (!StringHelper.isEmpty(title))
            {
                sb.append(" AND T6242.F03 LIKE ?");
                parameters.add(getSQLConnectionProvider().allMatch(title));
            }
            //申请时间
            Timestamp timestamp = query.getCreateTimeStart();
            if (timestamp != null)
            {
                sb.append(" AND DATE(T6242.F15) >=?");
                parameters.add(timestamp);
            }
            timestamp = query.getCreateTimeEnd();
            if (timestamp != null)
            {
                sb.append(" AND DATE(T6242.F15) <=?");
                parameters.add(timestamp);
            }
            //结束时间
            timestamp = query.getComTimeStart();
            if (timestamp != null)
            {
                sb.append(" AND DATE(T6242.F19) >=?");
                parameters.add(timestamp);
            }
            timestamp = query.getComTimeEnd();
            if (timestamp != null)
            {
                sb.append(" AND DATE(T6242.F19) <=?");
                parameters.add(timestamp);
            }
            //公益方
            String name = query.getGyName();
            if (!StringHelper.isEmpty(name))
            {
                sb.append(" AND T6242.F22 LIKE ?");
                parameters.add(getSQLConnectionProvider().allMatch(name));
            }
            
            String no = query.getBidNo();
            if (!StringHelper.isEmpty(no))
            {
                sb.append(" AND T6242.F21 LIKE ?");
                parameters.add(getSQLConnectionProvider().allMatch(no));
            }
            
            T6242_F11 status = query.getStatus();
            if (null != status)
            {
                sb.append(" AND T6242.F11 = ?");
                parameters.add(status.name());
            }
            
        }
    }
}
