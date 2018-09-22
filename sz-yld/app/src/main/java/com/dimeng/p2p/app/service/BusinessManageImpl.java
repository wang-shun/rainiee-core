/*
 * 文 件 名:  BusinessManageImpl.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2016年3月3日
 */
package com.dimeng.p2p.app.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.entities.T6114;
import com.dimeng.p2p.S61.enums.T6102_F10;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6114_F08;
import com.dimeng.p2p.S62.enums.T6230_F10;
import com.dimeng.p2p.S62.enums.T6230_F11;
import com.dimeng.p2p.S62.enums.T6230_F12;
import com.dimeng.p2p.S62.enums.T6230_F13;
import com.dimeng.p2p.S62.enums.T6230_F14;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.S62.enums.T6230_F27;
import com.dimeng.p2p.S62.enums.T6230_F28;
import com.dimeng.p2p.S62.enums.T6231_F21;
import com.dimeng.p2p.S62.enums.T6231_F27;
import com.dimeng.p2p.S62.enums.T6231_F33;
import com.dimeng.p2p.S62.enums.T6250_F07;
import com.dimeng.p2p.S62.enums.T6252_F09;
import com.dimeng.p2p.S63.entities.T6355;
import com.dimeng.p2p.account.user.service.entity.CapitalLs;
import com.dimeng.p2p.account.user.service.entity.MyExperience;
import com.dimeng.p2p.common.enums.YesOrNo;
import com.dimeng.p2p.modules.bid.front.service.entity.Bdlb;
import com.dimeng.p2p.modules.bid.front.service.entity.BidRecordInfo;
import com.dimeng.p2p.modules.bid.front.service.query.QyBidQuery;
import com.dimeng.util.parser.DateTimeParser;

/**
 * app业务管理
 * 
 * @author  zhoulantao
 * @version  [版本号, 2016年3月3日]
 */
public class BusinessManageImpl extends AbstractAppService implements BusinessManage
{

    public BusinessManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public BidRecordInfo[] getBidRecordList(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            ArrayList<BidRecordInfo> list = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT t6250.F04, t6250.F06, t6141.F02, t6110.F02 FROM S62.T6250 t6250, S61.T6141 t6141, S61.T6110 t6110 WHERE t6250.F02 = ? AND t6250.F07 = ? AND t6250.F03 = t6141.F01 AND t6110.F01 = t6141.F01 ORDER BY t6250.F06 DESC"))
            {
                pstmt.setInt(1, id);
                pstmt.setString(2, T6250_F07.F.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    SimpleDateFormat dateSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    while (resultSet.next())
                    {
                        BidRecordInfo record = new BidRecordInfo();
                        final String userTbr = resultSet.getString(4);
                        record.setAccountName(userTbr.substring(0, 2) + "******"
                            + userTbr.substring(userTbr.length() - 2, userTbr.length()));
                        record.setBidAmount(resultSet.getBigDecimal(1));
                        record.setBidTime(dateSdf.format(resultSet.getTimestamp(2)));
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                }
            }
            return ((list == null || list.size() == 0) ? null : list.toArray(new BidRecordInfo[list.size()]));
        }
    }
    
    @Override
    public String getTxPwdByPhone(String phoneNumber)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<String>()
            {
                @Override
                public String parse(ResultSet rs)
                    throws SQLException
                {
                    if (rs.next())
                    {
                        return rs.getString(1);
                    }
                    return "";
                }
            }, "select T6118.F08 from S61.T6110, S61.T6118 where T6110.F01 = T6118.F01 AND T6110.F04 = ?", phoneNumber);
        }
    }
    
    @Override
    public T6355 getAddress(int id)
        throws Throwable
    {
        if (id <= 0)
        {
            throw new ParameterException("参数错误");
        }
        try (Connection connection = getConnection())
        {
            T6355 record = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT T6355.F01, T6355.F02, T6355.F03, T6355.F04, T6355.F05, T6355.F06, T6355.F07, T6355.F08 FROM S63.T6355 WHERE T6355.F01 = ? AND T6355.F02 = ? LIMIT 1"))
            {
                pstmt.setInt(1, id);
                pstmt.setInt(2, serviceResource.getSession().getAccountId());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new T6355();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getString(3);
                        record.F04 = resultSet.getInt(4);
                        record.szdq = getRegion(resultSet.getInt(4));
                        record.F05 = resultSet.getString(5);
                        record.F06 = resultSet.getString(6);
                        record.F07 = resultSet.getString(7);
                        record.F08 = YesOrNo.parse(resultSet.getString(8));
                    }
                }
            }
            return record;
        }
    }
    
    /**
     * 查询区域名称
     * 
     * @param id
     * @return
     * @throws SQLException
     */
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
    public T6355[] queryHarvestAddress()
        throws Throwable
    {
        StringBuilder sql = new StringBuilder();
        ArrayList<Object> parameters = new ArrayList<>();
        sql.append("SELECT * FROM S63.T6355 WHERE T6355.F02 = ? ORDER BY F01 DESC LIMIT 1");
        parameters.add(serviceResource.getSession().getAccountId());
        try (Connection connection = getConnection())
        {
            return selectAll(connection, new ArrayParser<T6355>()
            {
                @Override
                public T6355[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<T6355> list = null;
                    while (resultSet.next())
                    {
                        T6355 t6355 = new T6355();
                        t6355.F01 = resultSet.getInt(1);
                        t6355.F02 = resultSet.getInt(2);
                        t6355.F03 = resultSet.getString(3);
                        t6355.szdq = getRegion(resultSet.getInt(4));
                        t6355.F05 = resultSet.getString(5);
                        t6355.F06 = resultSet.getString(6);
                        t6355.F07 = resultSet.getString(7);
                        t6355.F08 = YesOrNo.parse(resultSet.getString(8));
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(t6355);
                    }
                    return ((list == null || list.size() == 0) ? null : list.toArray(new T6355[list.size()]));
                }
            }, sql.toString(), parameters);
        }
    }
    
    @Override
    public PagingResult<T6355> queryHarvestAddress(Paging page)
        throws Throwable
    {
        StringBuilder sql = new StringBuilder();
        ArrayList<Object> parameters = new ArrayList<>();
        sql.append("SELECT * FROM S63.T6355 WHERE T6355.F02 = ? ORDER BY F01 DESC");
        parameters.add(serviceResource.getSession().getAccountId());
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<T6355>()
            {
                @Override
                public T6355[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<T6355> list = null;
                    while (resultSet.next())
                    {
                        T6355 t6355 = new T6355();
                        t6355.F01 = resultSet.getInt(1);
                        t6355.F02 = resultSet.getInt(2);
                        t6355.F03 = resultSet.getString(3);
                        t6355.F04 = resultSet.getInt(4);
                        t6355.szdq = getRegion(resultSet.getInt(4));
                        t6355.F05 = resultSet.getString(5);
                        t6355.F06 = resultSet.getString(6);
                        t6355.F07 = resultSet.getString(7);
                        t6355.F08 = YesOrNo.parse(resultSet.getString(8));
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(t6355);
                    }
                    return ((list == null || list.size() == 0) ? null : list.toArray(new T6355[list.size()]));
                }
            }, page, sql.toString(), parameters);
        }
    }
    
    @Override
    public PagingResult<MyExperience> searMyExperienceById(Map<String, Object> params, Paging paging)
        throws Throwable
    {
        ArrayList<Object> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT T6230.F03 AS F01,T6103.F03 AS F02,T6230.F06 AS F03,T6103.F07 AS F04,T6230.F09 AS F05,T6231.F21 AS F06,T6231.F22 AS F07,");
        sql.append("(SELECT SUM(T6285.F07) FROM S62.T6285 WHERE T6285.F02 = T6230.F01 AND T6285.F12 = T6103.F01 AND T6285.F09 IN ('WFH','FHZ')) AS F08,");
        sql.append("(SELECT SUM(T6285.F07) FROM S62.T6285 WHERE T6285.F02 = T6230.F01 AND T6285.F12 = T6103.F01 AND T6285.F09 = 'YFH') AS F09,");
        sql.append("(SELECT MIN(T6285.F08) FROM S62.T6285 WHERE T6285.F02 = T6230.F01 AND T6285.F12 = T6103.F01 AND T6285.F09 IN ('WFH','FHZ')) AS F10,");
        sql.append("(SELECT MAX(T6285.F08) FROM S62.T6285 WHERE T6285.F02 = T6230.F01 AND T6285.F12 = T6103.F01 AND T6285.F09 = 'YFH') AS F11,");
        sql.append("(SELECT T6286.F05 FROM S62.T6286 WHERE T6286.F02 = T6230.F01 AND T6286.F10 = T6103.F01) AS F12,T6231.F12 AS F13, T6231.F13 AS F14, T6103.F16 AS F15 ");
        sql.append("FROM S62.T6230 INNER JOIN S61.T6103 ON T6230.F01 = T6103.F13 INNER JOIN S62.T6231 ON T6230.F01 = T6231.F01 WHERE T6103.F02 = ? ");
        list.add(serviceResource.getSession().getAccountId());
        sql.append(" AND T6103.F01 = ? ");
        list.add(String.valueOf(params.get("expId")));
        
        String queryType = String.valueOf(params.get("status"));
        if ("SQZ".equals(queryType))
        {
            sql.append("AND T6230.F20 IN ('TBZ','DFK') ");
            sql.append("ORDER BY F12 DESC");
        }
        else if ("CYZ".equals(queryType))
        {
            sql.append("AND (SELECT COUNT(1) FROM S62.T6285 T6285_CY WHERE T6285_CY.F09 IN ('WFH','FHZ') AND T6285_CY.F04 = ? AND T6285_CY.F02 = T6230.F01) > 0 ");
            list.add(serviceResource.getSession().getAccountId());
            sql.append("ORDER BY F13 DESC");
        }
        else if ("YJZ".equals(queryType))
        {
            sql.append("AND (SELECT COUNT(1) FROM S62.T6286 WHERE T6286.F10 = T6103.F01 AND T6286.F07 = 'S') > 0 ");
            sql.append("AND (SELECT COUNT(1) FROM S62.T6285 T6285_CY WHERE T6285_CY.F09 IN ('WFH','FHZ') AND T6285_CY.F04 = ? AND T6285_CY.F02 = T6230.F01) = 0 ");
            list.add(serviceResource.getSession().getAccountId());
            sql.append("ORDER BY F11 DESC");
        }
        /* sql.append("AND TEMP.F08 < 0 AND TEMP.F09 > 0 "); */
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<MyExperience>()
            {
                @Override
                public MyExperience[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<MyExperience> list = null;
                    while (resultSet.next())
                    {
                        MyExperience myExperience = new MyExperience();
                        myExperience.F02 = resultSet.getString(1);
                        myExperience.F03 = resultSet.getBigDecimal(2);
                        myExperience.F04 = resultSet.getBigDecimal(3);
                        myExperience.F10 = resultSet.getInt(4);
                        myExperience.F13 = resultSet.getInt(5);
                        myExperience.F11 = T6231_F21.parse(resultSet.getString(6));
                        myExperience.F12 = resultSet.getInt(7);
                        myExperience.F08 = resultSet.getBigDecimal(8);
                        myExperience.F05 = resultSet.getBigDecimal(9);
                        myExperience.F09 = resultSet.getTimestamp(10);
                        myExperience.F06 = resultSet.getTimestamp(11);
                        myExperience.F14 = resultSet.getTimestamp(14);
                        myExperience.F15 = resultSet.getString(15);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(myExperience);
                    }
                    return ((list == null || list.size() == 0) ? null : list.toArray(new MyExperience[list.size()]));
                }
            }, paging, sql.toString(), list);
        }
    }
    
    @Override
    public String getData(String creditorId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<String>()
            {
                @Override
                public String parse(ResultSet rs)
                    throws SQLException
                {
                    if (rs.next())
                    {
                        return rs.getString(1);
                    }
                    return "";
                }
            }, "SELECT F09 FROM S62.T6251 WHERE F01=?", creditorId);
        }
    }
    
    @Override
    public String isExpired(int creditorId)
        throws Throwable
    {
        ArrayList<Object> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) FROM S62.T6252 WHERE T6252.F08 < CURDATE() AND T6252.F09 = ? AND T6252.F11 = ? ");
        list.add(T6252_F09.WH.name());
        list.add(creditorId);
        
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<String>()
            {
                @Override
                public String parse(ResultSet rs)
                    throws SQLException
                {
                    if (rs.next())
                    {
                        return rs.getString(1);
                    }
                    return "";
                }
            }, sql.toString(), list);
        }
    }
    
    /**
     * 已投新手标数量 
     * 
     * @param userId
     * @return
     * @throws Throwable
     */
    @Override
    public int getXsbCount(int userId) throws SQLException {
        try (Connection connection = getConnection()) {
            int count = 0;
            try (PreparedStatement ps = connection
                    .prepareStatement("SELECT count(*) FROM S62.T6250  WHERE T6250.F03 = ? ")) {
                ps.setInt(1, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        count = rs.getInt(1);
                    }
                }
            }
            return count;
        }
    }
    
   /**
    * 获取某用户的债权转让数量
    * 
    * @param userId
    * @return
    * @throws Throwable
    */
    @Override
    public int getZqzrCount(int userId) throws SQLException {
       try (Connection connection = getConnection()) {
           int count = 0;
           try (PreparedStatement ps = connection
                   .prepareStatement("SELECT count(*) FROM S62.T6262  WHERE T6262.F03 = ? ")) {
               ps.setInt(1, userId);
               try (ResultSet rs = ps.executeQuery()) {
                   if (rs.next()) {
                       count = rs.getInt(1);
                   }
               }
           }
           return count;
       }
   }
    
    @Override
    public String getNextData(String creditorId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<String>()
            {
                @Override
                public String parse(ResultSet rs)
                    throws SQLException
                {
                    if (rs.next())
                    {
                        return rs.getString(1);
                    }
                    return "";
                }
            }, "SELECT T6231.F06 FROM S62.T6251,S62.T6231 WHERE T6251.F01 = ? AND T6231.F01 = T6251.F03", creditorId);
        }
    }
    
    /**
     * 手机版的交易流水
     * 
     * @param paging
     * @return
     * @throws Throwable
     */
    @Override
    public PagingResult<CapitalLs> mobileSearchLs(Paging paging)
        throws Throwable
    {
        StringBuilder builder =
            new StringBuilder(
                    "SELECT T6102.F01 AS F01, T6102.F02 AS F02, T6102.F03 AS F03, T6102.F04 AS F04, T6102.F05 AS F05, T6102.F06 AS F06, T6102.F07 AS F07, T6102.F08 AS F08, T6102.F09 AS F09, T6102.F10 AS F10, T6102.F11 AS F11, T6101.F02 AS F12, T6102.F12 AS F13, T5122.F02 AS F14 FROM S61.T6102 INNER JOIN S61.T6101 ON T6102.F02 = T6101.F01 INNER JOIN S51.T5122 ON T5122.F01 = T6102.F03 WHERE T6101.F02 = ?");
        ArrayList<Object> params = new ArrayList<>();
        params.add(serviceResource.getSession().getAccountId());
        builder.append(" AND T6101.F03= 'WLZH'");
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
    public void updateT6198F06(String source)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            execute(connection,
                "UPDATE  S61.T6198 SET F04=?,F06=? WHERE F02 = ?",
                source,
                getCurrentTimestamp(connection),
                serviceResource.getSession().getAccountId());
        }
    }

    @Override
    public int getZrzZqCount()
        throws SQLException
    {
        try (Connection connection = getConnection()) 
        {
            int count = 0;
            try (PreparedStatement ps = connection
                    .prepareStatement("SELECT count(*) FROM S62.T6260 INNER JOIN S62.T6251 ON T6260.F02 = T6251.F01 WHERE T6260.F07 IN ('ZRZ','DSH') AND T6251.F04 = ? ")) 
            {
                ps.setInt(1, serviceResource.getSession().getAccountId());
                try (ResultSet rs = ps.executeQuery()) 
                {
                    if (rs.next()) 
                    {
                        count = rs.getInt(1);
                    }
                }
            }
            return count;
        }
    }

    @Override
    public int getYzcZqCount()
        throws SQLException
    {
        try (Connection connection = getConnection()) 
        {
            int count = 0;
            try (PreparedStatement ps = connection
                    .prepareStatement("SELECT COUNT(*) FROM S62.T6251 INNER JOIN S62.T6260 ON T6260.F02 = T6251.F01 INNER JOIN S62.T6262 ON T6260.F01 = T6262.F02 WHERE T6251.F04 = ? ")) 
            {
                ps.setInt(1, serviceResource.getSession().getAccountId());
                try (ResultSet rs = ps.executeQuery()) 
                {
                    if (rs.next()) 
                    {
                        count = rs.getInt(1);
                    }
                }
            }
            return count;
        }
    }

    @Override
    public int getYzrZqCount()
        throws SQLException
    {
        try (Connection connection = getConnection()) 
        {
            int count = 0;
            try (PreparedStatement ps = connection
                    .prepareStatement("SELECT COUNT(*) FROM S62.T6251 INNER JOIN S62.T6260 ON T6260.F02 = T6251.F01 INNER JOIN S62.T6262 ON T6260.F01 = T6262.F02 WHERE T6262.F03 = ?")) 
            {
                ps.setInt(1, serviceResource.getSession().getAccountId());
                try (ResultSet rs = ps.executeQuery()) 
                {
                    if (rs.next()) 
                    {
                        count = rs.getInt(1);
                    }
                }
            }
            return count;
        }
    }
     
    @Override
    public int getHkzZqCount()
        throws SQLException
    {
        try (Connection connection = getConnection()) 
        {
            int count = 0;
            try (PreparedStatement ps = connection
                    .prepareStatement("SELECT COUNT(*) FROM S62.T6251 INNER JOIN S62.T6230 ON T6251.F03 = T6230.F01 WHERE T6230.F20 IN ('HKZ') AND T6251.F04 = ? AND T6251.F07 > 0")) 
            {
                ps.setInt(1, serviceResource.getSession().getAccountId());
                try (ResultSet rs = ps.executeQuery()) 
                {
                    if (rs.next()) 
                    {
                        count = rs.getInt(1);
                    }
                }
            }
            return count;
        }
    }
     
    @Override
    public int getYjqZqCount()
        throws SQLException
    {
        try (Connection connection = getConnection()) 
        {
            int count = 0;
            try (PreparedStatement ps = connection
                    .prepareStatement("SELECT COUNT(*) FROM S62.T6251 INNER JOIN S62.T6230 ON T6251.F03 = T6230.F01 WHERE T6230.F20 IN ('YDF','YJQ') AND T6251.F04 = ?")) 
            {
                ps.setInt(1, serviceResource.getSession().getAccountId());
                try (ResultSet rs = ps.executeQuery()) 
                {
                    if (rs.next()) 
                    {
                        count = rs.getInt(1);
                    }
                }
            }
            return count;
        }
    }
     
    @Override
    public int getTzzZqCount()
        throws SQLException
    {
        try (Connection connection = getConnection()) 
        {
            int count = 0;
            try (PreparedStatement ps = connection
                    .prepareStatement("SELECT COUNT(*) FROM S62.T6230 INNER JOIN S62.T6250 ON T6230.F01 = T6250.F02 WHERE T6230.F20 IN ('TBZ','DFK') AND T6250.F03 = ?")) 
            {
                ps.setInt(1, serviceResource.getSession().getAccountId());
                try (ResultSet rs = ps.executeQuery()) 
                {
                    if (rs.next()) 
                    {
                        count = rs.getInt(1);
                    }
                }
            }
            return count;
        }
    }

    @Override
    public int getOrderCount()
        throws SQLException
    {
        try (Connection connection = getConnection()) 
        {
            int count = 0;
            try (PreparedStatement ps = connection
                    .prepareStatement("SELECT COUNT(DISTINCT T6352.F03) FROM S63.T6352 INNER JOIN S63.T6359 ON T6359.F02 = T6352.F01 INNER JOIN S63.T6351 ON T6359.F03 = T6351.F01 LEFT JOIN S63.T6350 ON T6351.F04 = T6350.F01 WHERE T6352.F02 = ?")) 
            {
                ps.setInt(1, serviceResource.getSession().getAccountId());
                try (ResultSet rs = ps.executeQuery()) 
                {
                    if (rs.next()) 
                    {
                        count = rs.getInt(1);
                    }
                }
            }
            return count;
        }
    }

	
	 
	@Override
	public PagingResult<Bdlb> searchAll(QyBidQuery query, T6230_F27 t6230_F27,
			T6110_F06 t6110_F06, Paging paging) throws Throwable {
		      StringBuilder sql =
		      new StringBuilder(
		          
		          "SELECT T6211.F02 AS F01, T6230.F01 AS F02, T6230.F02 AS F03, T6230.F03 AS F04, T6230.F04 AS F05, T6230.F05 AS F06, T6230.F06 AS F07, T6230.F07 AS F08, T6230.F08 AS F09, T6230.F09 AS F10, T6230.F20 AS F11, T6230.F21 AS F12, T6230.F22 AS F13, T6230.F23 AS F14, T5124.F02 AS F15,T6230.F11 AS F16,T6230.F13 AS F17,T6230.F14 AS F18, (SELECT T6161.F18 FROM S61.T6161 WHERE T6161.F01 = T6236.F03) AS F19, T6231.F21 AS F20, T6231.F22 AS F21, T6230.F10 AS F22,T6230.F19 AS F23,T6230.F12 AS F24,T6110.F06 AS F25,T6230.F21 AS F26, case WHEN T6230.F20='HKZ' or  T6230.F20='YJQ' or  T6230.F20='YDF' THEN ( T6230.F05- T6230.F07) ELSE T6230.F05 END AS F27, CASE WHEN T6231.F21='S' THEN (T6231.F22/DAYOFMONTH(last_day(T6230.F24))) ELSE T6230.F09 END AS F28, case WHEN T6230.F20='HKZ' or  T6230.F20='YJQ' or  T6230.F20='YDF' THEN 0 ELSE T6230.F07 END AS F29 ,case WHEN T6230.F20='HKZ' or  T6230.F20='YJQ' or  T6230.F20='YDF' THEN 100 ELSE case WHEN T6230.F20='YFB' THEN -1 ELSE((T6230.F05-T6230.F07)/T6230.F05)*100 END END AS F30, ( SELECT T6161.F04 FROM S61.T6161 WHERE T6161.F01 = T6236.F03) AS F31, T6230.F28 AS F32,T6231.F27 AS F33,T6231.F28 AS F34,T6231.F29 AS F35,T6231.F30 AS F36, ");
		  sql.append("(CASE WHEN (T6231.F29 = 'S' AND T6230.F20 IN ('YFB', 'TBZ')) THEN 1 WHEN (T6230.F28 = 'S' AND T6230.F20 IN ('YFB', 'TBZ') AND T6231.F29 = 'F') THEN 2 WHEN (T6231.F27 = 'S' AND T6230.F20 IN ('YFB', 'TBZ') AND T6231.F29 = 'F') THEN 3 ELSE 4 END) AS ORD, T6231.F33 AS F38 ");
		  sql.append(" FROM S62.T6211 INNER JOIN S62.T6230 ON T6211.F01 = T6230.F04 INNER JOIN S62.T6231 ON T6231.F01 = T6230.F01 LEFT JOIN S51.T5124 ON T6230.F23 = T5124.F01  INNER JOIN S61.T6110 ON T6110.F01 = T6230.F02 LEFT JOIN S62.T6236 ON T6236.F02 = T6230.F01 AND T6236.F04 = 'S' ");
		  ArrayList<Object> parameters = new ArrayList<Object>();
		  sql.append(" WHERE CASE WHEN T6230.F20 IN ('TBZ','YFB') AND T6231.F33 IN ('PC_APP','APP') THEN 1 WHEN T6230.F20 IN ('YJQ','YDF','DFK','HKZ') AND T6231.F33 IN ('PC_APP','APP','PC') THEN 1 ELSE 0 END=1 ");
		  if (t6230_F27 != null)
		  {
		      sql.append(" AND T6230.F27 = ?");
		      parameters.add(t6230_F27);
		  }
		  if (t6110_F06 != null)
		  {
		      sql.append(" AND T6110.F06 = ?");
		      parameters.add(t6110_F06);
		  }
		  try (Connection connection = getConnection())
		  {
		      if (query != null)
		      {
		          if (query.getProductId() > 0)
		          {
		              sql.append(" AND T6230.F32 = ?");
		              parameters.add(query.getProductId());
		          }
		          if (query.bidType() > 0)
		          {
		              sql.append(" AND T6230.F04 = ?");
		              parameters.add(query.bidType());
		          }
		          int rate = query.getRate();
		          if (rate > 0)
		          {
		              try (PreparedStatement pstmt =
		                  connection.prepareStatement("SELECT F01, F02, F03, F04, F05 FROM S62.T6299 WHERE F01 = ?"))
		              {
		                  pstmt.setInt(1, rate);
		                  try (ResultSet rs = pstmt.executeQuery())
		                  {
		                      if (rs.next())
		                      {
		                          if (rs.getInt(2) <= 0)
		                          {
		                              sql.append(" AND T6230.F06<?");
		                              parameters.add(new BigDecimal(rs.getInt(3)).divide(new BigDecimal(100)));
		                          }
		                          else if (rs.getInt(3) <= 0)
		                          {
		                              sql.append(" AND T6230.F06>?");
		                              parameters.add(new BigDecimal(rs.getInt(2)).divide(new BigDecimal(100)));
		                          }
		                          else
		                          {
		                              sql.append(" AND T6230.F06>=? AND T6230.F06<=?");
		                              parameters.add(new BigDecimal(rs.getInt(2)).divide(new BigDecimal(100)));
		                              parameters.add(new BigDecimal(rs.getInt(3)).divide(new BigDecimal(100)));
		                          }
		                      }
		                  }
		              }
		          }
		          
		          int getProgress = query.getJd();
		          if (getProgress > 0)
		          {
		              try (PreparedStatement pstmt =
		                  connection.prepareStatement("SELECT F01, F02, F03, F04, F05 FROM S62.T6299 WHERE F01 = ?"))
		              {
		                  pstmt.setInt(1, getProgress);
		                  try (ResultSet rs = pstmt.executeQuery())
		                  {
		                      if (rs.next())
		                      {
		                          if (rs.getInt(2) <= 0)
		                          {
		                              sql.append(" AND (T6230.F05-T6230.F07)/T6230.F05*100<? AND T6230.F20 NOT IN ('HKZ','YJQ','YDF') ");
		                              parameters.add(new BigDecimal(rs.getInt(3)));
		                          }
		                          else if (rs.getInt(3) <= 0)
		                          {
		                              sql.append(" AND ((T6230.F05-T6230.F07)/T6230.F05*100>? OR (T6230.F20 IN ('HKZ','YJQ','YDF'))) ");
		                              parameters.add(new BigDecimal(rs.getInt(2)));
		                          }
		                          else
		                          {
		                              sql.append(" AND (T6230.F05-T6230.F07)/T6230.F05*100>=? AND (T6230.F05-T6230.F07)/T6230.F05*100<=? AND T6230.F20 NOT IN ('HKZ','YJQ','YDF') ");
		                              parameters.add(new BigDecimal(rs.getInt(2)));
		                              parameters.add(new BigDecimal(rs.getInt(3)));
		                          }
		                      }
		                  }
		              }
		          }
		          T6230_F20 creditStatus = query.getStatus();
		          if (creditStatus != null)
		          {
		              if (creditStatus == T6230_F20.YJQ)
		              {
		                  sql.append(" AND (T6230.F20 = ? OR T6230.F20=?)");
		                  parameters.add(creditStatus);
		                  parameters.add("YDF");
		              }
		              else
		              {
		                  sql.append(" AND T6230.F20 = ?");
		                  parameters.add(creditStatus);
		              }
		          }
		          sql.append(" GROUP BY T6230.F01 ");
		          int order = query.getOrder();
		          if (order == 0 || order == 3)
		          {
		              sql.append(" ORDER BY ORD,T6231.F30 DESC,T6230.F20,T6230.F22 DESC");
		          }
		          else if (order == 11)
		          {
		              sql.append(" ORDER BY T6230.F06 DESC, ORD,T6231.F30 DESC,T6230.F20,T6230.F22 DESC");
		          }
		          else if (order == 12)
		          {
		              sql.append(" ORDER BY T6230.F06 ASC, ORD,T6231.F30 DESC,T6230.F20,T6230.F22 DESC");
		          }
		          else if (order == 41)
		          {
		              sql.append(" ORDER BY F27 DESC, ORD,T6231.F30 DESC,T6230.F20,T6230.F22 DESC");
		          }
		          else if (order == 42)
		          {
		              sql.append(" ORDER BY F27 ASC, ORD,T6231.F30 DESC,T6230.F20,T6230.F22 DESC");
		          }
		          else if (order == 51)
		          {
		              sql.append(" ORDER BY F28 DESC, ORD,T6231.F30 DESC,T6230.F20,T6230.F22 DESC");
		          }
		          else if (order == 52)
		          {
		              sql.append(" ORDER BY F28 ASC, ORD,T6231.F30 DESC,T6230.F20,T6230.F22 DESC");
		          }
		          else if (order == 61)
		          {
		              sql.append(" ORDER BY F29 DESC, ORD,T6231.F30 DESC,T6230.F20,T6230.F22 DESC");
		          }
		          else if (order == 62)
		          {
		              sql.append(" ORDER BY F29 ASC, ORD,T6231.F30 DESC,T6230.F20,T6230.F22 DESC");
		          }
		          else if (order == 71)
		          {
		              sql.append(" ORDER BY F30 DESC, ORD,T6231.F30 DESC,T6230.F20,T6230.F22 DESC");
		          }
		          else if (order == 72)
		          {
		              sql.append(" ORDER BY F30 ASC, ORD,T6231.F30 DESC,T6230.F20,T6230.F22 DESC");
		          }
		          
		      }
		      else
		      {
		          sql.append(" ORDER BY ORD,T6231.F30 DESC,T6230.F20,T6230.F22 DESC");
		      }
		      
		      return selectPaging(connection, new ArrayParser<Bdlb>()
		      {
		          @Override
		          public Bdlb[] parse(ResultSet resultSet)
		              throws SQLException
		          {
		              ArrayList<Bdlb> list = null;
		              while (resultSet.next())
		              {
		                  Bdlb record = new Bdlb();
		                  record.F01 = resultSet.getString(1);
		                  record.F02 = resultSet.getInt(2);
		                  record.F03 = resultSet.getInt(3);
		                  record.F04 = resultSet.getString(4);
		                  record.F05 = resultSet.getInt(5);
		                  record.F06 = resultSet.getBigDecimal(6);
		                  record.F07 = resultSet.getBigDecimal(7);
		                  record.F08 = resultSet.getBigDecimal(8);
		                  record.F09 = resultSet.getInt(9);
		                  record.F10 = resultSet.getInt(10);
		                  record.F11 = T6230_F20.parse(resultSet.getString(11));
		                  record.F12 = resultSet.getString(12);
		                  record.F13 = resultSet.getTimestamp(13);
		                  record.F14 = resultSet.getInt(14);
		                  record.F15 = resultSet.getString(15);
		                  record.F16 = T6230_F11.parse(resultSet.getString(16));
		                  record.F17 = T6230_F13.parse(resultSet.getString(17));
		                  record.F18 = T6230_F14.parse(resultSet.getString(18));
		                  record.jgjc = resultSet.getString(19);
		                  record.proess =
		                      (record.F06.doubleValue() - record.F08.doubleValue()) / record.F06.doubleValue();
		                  record.F19 = T6231_F21.parse(resultSet.getString(20));
		                  record.F20 = resultSet.getInt(21);
		                  record.F21 = T6230_F10.parse(resultSet.getString(22));
		                  record.jxfs = resultSet.getInt(23);
		                  record.F22 = T6230_F12.parse(resultSet.getString(24));
		                  record.F23 = T6110_F06.parse(resultSet.getString(25));
		                  record.image = resultSet.getString(26);
		                  record.jgqc = resultSet.getString(31);
		                  record.F28 = T6230_F28.parse(resultSet.getString(32));
		                  record.F29 = T6231_F27.parse(resultSet.getString(33));
		                  record.F30 = resultSet.getBigDecimal(34);
		                  record.F31 = T6231_F33.parse(resultSet.getString(38));
		                  if (list == null)
		                  {
		                      list = new ArrayList<>();
		                  }
		                  list.add(record);
		              }
		              return ((list == null || list.size() == 0) ? null : list.toArray(new Bdlb[list.size()]));
		          }
		      },
		          paging,
		          sql.toString(),
		          parameters);
		  }
	}

    @Override
    public String getUserNameByPhone(String phone)
        throws Throwable
    {
        String username = "";
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement("SELECT F02 FROM S61.T6110 WHERE F04=?"))
            {
                ps.setString(1, phone);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        username = rs.getString(1);
                    }
                }
            }
        }
        return username;
    }

    /**
     * 获取用户绑定的银行卡信息
     *
     * @param userId
     * @return
     * @throws Throwable
     */
    @Override
    public T6114 getBankCard(int userId)
            throws SQLException
    {
        try (Connection connection = getConnection())
        {
            T6114 t6114 = null;
            try (PreparedStatement ps =
                         connection.prepareStatement("SELECT F03,F07 FROM S61.T6114 WHERE F02=? AND F08=?"))
            {
                ps.setInt(1, userId);
                ps.setString(2, T6114_F08.QY.name());
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        t6114 = new T6114();
                        t6114.F03 = rs.getInt(1);
                        t6114.F07 = rs.getString(2);
                    }
                }
            }
            return t6114;
        }
    }

    /**
     * 获取用户绑定银行卡的银行编码
     *
     * @param userId
     * @return50140244
     * @throws Throwable
     */
    @Override
    public String getBankCode(int bankId)
            throws SQLException
    {
        try (Connection connection = getConnection())
        {
            String bankCode = "";
            try (PreparedStatement ps = connection.prepareStatement("SELECT F04 FROM S50.T5020 WHERE F01=?"))
            {
                ps.setInt(1, bankId);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        bankCode = rs.getString(1);
                    }
                }
            }
            return bankCode;
        }
    }
    
    @Override
    public String getT5010Name(String type)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            String name = "";
            try (PreparedStatement ps = connection.prepareStatement("SELECT F03 FROM S50.T5010 WHERE F02 = ?"))
            {
                ps.setString(1, type);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        name = rs.getString(1);
                    }
                }
            }
            return name;
        }
    }
}
