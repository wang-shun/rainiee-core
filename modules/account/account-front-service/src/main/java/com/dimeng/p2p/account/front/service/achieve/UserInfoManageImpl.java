package com.dimeng.p2p.account.front.service.achieve;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.entities.T6118;
import com.dimeng.p2p.S61.entities.T6144;
import com.dimeng.p2p.S61.entities.T6147;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.S61.enums.T6110_F08;
import com.dimeng.p2p.S61.enums.T6110_F10;
import com.dimeng.p2p.S61.enums.T6110_F17;
import com.dimeng.p2p.S61.enums.T6110_F18;
import com.dimeng.p2p.S61.enums.T6118_F02;
import com.dimeng.p2p.S61.enums.T6118_F03;
import com.dimeng.p2p.S61.enums.T6118_F04;
import com.dimeng.p2p.S61.enums.T6118_F05;
import com.dimeng.p2p.S61.enums.T6118_F09;
import com.dimeng.p2p.S61.enums.T6120_F05;
import com.dimeng.p2p.S61.enums.T6141_F03;
import com.dimeng.p2p.S61.enums.T6141_F04;
import com.dimeng.p2p.S61.enums.T6147_F04;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.S62.enums.T6252_F09;
import com.dimeng.p2p.S64.enums.T6410_F07;
import com.dimeng.p2p.account.front.service.UserInfoManage;
import com.dimeng.p2p.account.front.service.entity.UserInfo;
import com.dimeng.p2p.account.front.service.entity.UserRZInfo;
import com.dimeng.p2p.variables.defines.pays.PayVariavle;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BooleanParser;
import com.dimeng.util.parser.IntegerParser;

public class UserInfoManageImpl extends AbstractAccountService implements UserInfoManage
{
    
    public UserInfoManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public String isYuqi()
        throws Throwable
    {
        String sql = "SELECT DATEDIFF(?,F08) FROM S62.T6252 WHERE F09=? AND F03=? AND F08 < SYSDATE()";
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql))
            {
                ps.setTimestamp(1, getCurrentTimestamp(connection));
                ps.setString(2, T6252_F09.WH.name());
                ps.setInt(3, serviceResource.getSession().getAccountId());
                try (ResultSet rs = ps.executeQuery())
                {
                    while (rs.next())
                    {
                        int days = rs.getInt(1);
                        if (days > 0)
                        {
                            return "Y";
                        }
                    }
                }
            }
        }
        return "N";
    }
    
    @Override
    public T6101 search()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            T6101 record = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06 FROM S61.T6101 WHERE T6101.F02 = ? AND T6101.F03 = ? LIMIT 1"))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                pstmt.setString(2, T6101_F03.WLZH.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new T6101();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = T6101_F03.parse(resultSet.getString(3));
                        record.F04 = resultSet.getString(4);
                        record.F05 = resultSet.getString(5);
                        record.F06 = resultSet.getBigDecimal(6);
                    }
                }
            }
            return record;
        }
    }
    
    @Override
    public UserRZInfo[] getRZInfo(int userId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            ArrayList<UserRZInfo> list = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT T6120.F05 AS F01, T6120.F06 AS F02, T5123.F02 AS F03 FROM S61.T6120 INNER JOIN S51.T5123 ON T6120.F02 = T5123.F01 WHERE T6120.F01 = ?"))
            {
                pstmt.setInt(1, userId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        UserRZInfo record = new UserRZInfo();
                        record.F01 = T6120_F05.parse(resultSet.getString(1));
                        record.F02 = resultSet.getTimestamp(2);
                        record.F03 = resultSet.getString(3);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                }
            }
            return ((list == null || list.size() == 0) ? null : list.toArray(new UserRZInfo[list.size()]));
        }
        
    }
    
    @Override
    public String getUserName(int userID)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F02 FROM S61.T6110 WHERE T6110.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, userID);
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
    public PagingResult<UserInfo> search(Paging paging)
        throws Throwable
    {
        String sql =
            "SELECT T6110.F01 AS F01, T6110.F02 AS F02, T6110.F03 AS F03, T6110.F04 AS F04, T6110.F05 AS F05, T6110.F06 AS F06, T6110.F07 AS F07, T6110.F08 AS F08, T6110.F09 AS F09, T6110.F10 AS F10, T6141.F01 AS F11, T6141.F02 AS F12, T6141.F03 AS F13, T6141.F04 AS F14, T6141.F05 AS F15, T6141.F06 AS F16, T6141.F07 AS F17 FROM S61.T6110 INNER JOIN S61.T6141 ON T6110.F01 = T6141.F01 WHERE T6110.F07 = ?";
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<UserInfo>()
            {
                @Override
                public UserInfo[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<UserInfo> list = null;
                    while (resultSet.next())
                    {
                        UserInfo record = new UserInfo();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getString(2);
                        record.F03 = resultSet.getString(3);
                        record.F04 = resultSet.getString(4);
                        record.F05 = resultSet.getString(5);
                        record.F06 = T6110_F06.parse(resultSet.getString(6));
                        record.F07 = T6110_F07.parse(resultSet.getString(7));
                        record.F08 = T6110_F08.parse(resultSet.getString(8));
                        record.F09 = resultSet.getTimestamp(9);
                        record.F10 = T6110_F10.parse(resultSet.getString(10));
                        record.F11 = resultSet.getInt(11);
                        record.F12 = resultSet.getString(12);
                        record.F13 = T6141_F03.parse(resultSet.getString(13));
                        record.F14 = T6141_F04.parse(resultSet.getString(14));
                        record.F15 = resultSet.getString(15);
                        record.F16 = resultSet.getString(16);
                        try
                        {
                            record.F17 = StringHelper.decode(resultSet.getString(17));
                        }
                        catch (Throwable e)
                        {
                            logger.error("UserInfoManageImpl.search() error", e);
                        }
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return ((list == null || list.size() == 0) ? null : list.toArray(new UserInfo[list.size()]));
                }
            }, paging, sql, T6110_F07.HMD);
        }
    }
    
    @Override
    public UserInfo getAgeSex(String card)
        throws Throwable
    {
        UserInfo userInfo = new UserInfo();
        if (!StringHelper.isEmpty(card))
        {
            
            if (Integer.parseInt(card.substring(card.length() - 2, card.length() - 1)) % 2 != 0)
            {
                userInfo.sex = 1;
            }
            else
            {
                userInfo.sex = 0;
            }
            
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            if (month > IntegerParser.parse(card.substring(10, 12)))
            {
                userInfo.age = year - IntegerParser.parse(card.substring(6, 10));
            }
            else if (month == IntegerParser.parse(card.substring(10, 12))
                && day > IntegerParser.parse(card.substring(12, 14)))
            {
                userInfo.age = year - IntegerParser.parse(card.substring(6, 10));
            }
            else
            {
                userInfo.age = year - IntegerParser.parse(card.substring(6, 10)) - 1;
            }
        }
        return userInfo;
    }
    
    @Override
    public UserInfo search(int userId)
        throws Throwable
    {
        if (userId < 0)
        {
            return null;
        }
        UserInfo userInfo = new UserInfo();
        String sql =
            "SELECT T6110.F01 AS F01, T6110.F02 AS F02, T6110.F09 AS F03, T6141.F05 AS F04,(YEAR(?)-YEAR(T6141.F08)-1) + ( DATE_FORMAT(T6141.F08, '%m%d') <= DATE_FORMAT(?, '%m%d') ) AS age FROM S61.T6110 INNER JOIN S61.T6141 ON T6110.F01 = T6141.F01 WHERE T6110.F01 = ? LIMIT 1";
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql))
            {
                ps.setTimestamp(1, getCurrentTimestamp(connection));
                ps.setTimestamp(2, getCurrentTimestamp(connection));
                ps.setInt(3, userId);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        userInfo.F01 = rs.getInt(1);
                        userInfo.F02 = rs.getString(2);
                        userInfo.F09 = rs.getTimestamp(3);
                        userInfo.F15 = rs.getString(4);
                        userInfo.age = rs.getInt(5);
                    }
                }
            }
            userInfo.cyzqsl = cyzqsl(userId);
            userInfo.cylcjhsl = cylcjhsl(userId);
            userInfo.fbjksl = getCreditCount(userId);
            userInfo.cgjksl = getCreditSuccCount(userId);
            userInfo.whqjksl = getWhqCount(userId);
            userInfo.yqje = getYqMoney(userId);
            String sql1 =
                "SELECT T6144.F02 AS F01, T6144.F03 AS F02, T6116.F02 AS F03, T6116.F03 AS F04 FROM S61.T6144 INNER JOIN S61.T6116 ON T6144.F01 = T6116.F01 WHERE T6144.F01 = ? LIMIT 1";
            try (PreparedStatement ps1 = connection.prepareStatement(sql1))
            {
                ps1.setInt(1, userId);
                try (ResultSet rs = ps1.executeQuery();)
                {
                    if (rs.next())
                    {
                        userInfo.yqcs = rs.getInt(1);
                        userInfo.yzyqcs = rs.getInt(2);
                        userInfo.xyjf = rs.getInt(3);
                        userInfo.xyAmont = rs.getBigDecimal(4);
                    }
                }
            }
        }
        
        return userInfo;
    }
    
    /**
     * 查询持有债权数量
     * 
     * @param userId
     * @return
     * @throws Throwable
     */
    private int cyzqsl(int userId)
        throws Throwable
    {
        String sql =
            "SELECT COUNT(*) AS F01 FROM S62.T6251 INNER JOIN S62.T6230 ON T6251.F03 = T6230.F01 WHERE T6251.F04 = ? AND T6230.F20 IN (?,?,?)";
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql))
            {
                ps.setInt(1, userId);
                ps.setString(2, T6230_F20.TBZ.name());
                ps.setString(3, T6230_F20.DFK.name());
                ps.setString(4, T6230_F20.HKZ.name());
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        return rs.getInt(1);
                    }
                }
            }
        }
        return 0;
    }
    
    /**
     * 查询持有优选理财数量
     * 
     * @param userId
     * @return
     * @throws Throwable
     */
    private int cylcjhsl(int userId)
        throws Throwable
    {
        String sql =
            "SELECT COUNT(*) AS F01 FROM S64.T6411 INNER JOIN S64.T6410 ON T6411.F02 = T6410.F01 WHERE T6411.F03 = ? AND T6410.F07 = ?";
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql))
            {
                ps.setInt(1, userId);
                ps.setString(2, T6410_F07.YSX.name());
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        return rs.getInt(1);
                    }
                }
            }
        }
        return 0;
    }
    
    /**
     * 逾期金额
     * 
     * @param userId
     * @return
     * @throws Throwable
     */
    private BigDecimal getYqMoney(int userId)
        throws Throwable
    {
        if (userId < 0)
        {
            return null;
        }
        BigDecimal yqMoney = new BigDecimal(0);
        try (Connection conn = getConnection())
        {
            try (PreparedStatement ps =
                conn.prepareStatement("SELECT IFNULL(SUM(F07),0) FROM S62.T6252 WHERE F03 = ?  AND F09 = ? AND F10 < ?");)
            {
                ps.setInt(1, userId);
                ps.setString(2, T6252_F09.WH.name());
                ps.setDate(3, getCurrentDate(conn));
                try (ResultSet rs = ps.executeQuery();)
                {
                    if (rs.next())
                    {
                        yqMoney = rs.getBigDecimal(1);
                    }
                }
            }
        }
        return yqMoney;
    }
    
    /**
     * 借款笔数
     * 
     * @param userId
     * @return
     * @throws Throwable
     */
    private int getCreditCount(int userId)
        throws Throwable
    {
        if (userId < 0)
        {
            return 0;
        }
        int jkCount = 0;
        try (Connection conn = getConnection())
        {
            try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM S62.T6230 WHERE F02 = ?");)
            {
                ps.setInt(1, userId);
                try (ResultSet rs = ps.executeQuery();)
                {
                    if (rs.next())
                    {
                        jkCount = rs.getInt(1);
                    }
                }
            }
        }
        return jkCount;
    }
    
    /**
     * 借款成功笔数
     * 
     * @param userId
     * @return
     * @throws Throwable
     */
    private int getCreditSuccCount(int userId)
        throws Throwable
    {
        if (userId < 0)
        {
            return 0;
        }
        int succCount = 0;
        try (Connection conn = getConnection())
        {
            try (PreparedStatement ps =
                conn.prepareStatement("SELECT COUNT(*) FROM S62.T6230 WHERE F02 = ? AND F20 IN (?,?,?)");)
            {
                ps.setInt(1, userId);
                ps.setString(2, T6230_F20.YDF.name());
                ps.setString(3, T6230_F20.HKZ.name());
                ps.setString(4, T6230_F20.YJQ.name());
                try (ResultSet rs = ps.executeQuery();)
                {
                    if (rs.next())
                    {
                        succCount = rs.getInt(1);
                    }
                }
            }
        }
        return succCount;
    }
    
    /**
     * 还清笔数
     * 
     * @param userId
     * @return
     * @throws Throwable
     */
    @SuppressWarnings("unused")
    private int getHqCount(int userId)
        throws Throwable
    {
        if (userId < 0)
        {
            return 0;
        }
        int hqCount = 0;
        try (Connection conn = getConnection())
        {
            try (PreparedStatement ps =
                conn.prepareStatement("SELECT COUNT(*) FROM S62.T6230 WHERE F02 = ? AND F20 = ?");)
            {
                ps.setInt(1, userId);
                ps.setString(2, T6230_F20.YJQ.name());
                try (ResultSet rs = ps.executeQuery();)
                {
                    if (rs.next())
                    {
                        hqCount = rs.getInt(1);
                    }
                }
            }
        }
        return hqCount;
    }
    
    /**
     * 未还清笔数
     * 
     * @param userId
     * @return
     * @throws Throwable
     */
    private int getWhqCount(int userId)
        throws Throwable
    {
        if (userId < 0)
        {
            return 0;
        }
        int whqCount = 0;
        try (Connection conn = getConnection())
        {
            try (PreparedStatement ps =
                conn.prepareStatement("SELECT COUNT(*) FROM S62.T6230 WHERE F02 = ? AND F20 IN (?,?)");)
            {
                ps.setInt(1, userId);
                ps.setString(2, T6230_F20.YDF.name());
                ps.setString(3, T6230_F20.HKZ.name());
                try (ResultSet rs = ps.executeQuery();)
                {
                    if (rs.next())
                    {
                        whqCount = rs.getInt(1);
                    }
                }
            }
        }
        return whqCount;
    }
    
    /**
     * 借款总额
     * 
     * @param userId
     * @return
     * @throws Throwable
     */
    @SuppressWarnings("unused")
    private BigDecimal getJkTotle(int userId)
        throws Throwable
    {
        if (userId < 0)
        {
            return null;
        }
        BigDecimal jkTotle = new BigDecimal(0);
        try (Connection conn = getConnection())
        {
            try (PreparedStatement ps =
                conn.prepareStatement("SELECT IFNULL(SUM(F05),0) FROM S62.T6230 WHERE F02 = ? AND F20 IN (?,?,?)");)
            {
                ps.setInt(1, userId);
                ps.setString(2, T6230_F20.YDF.name());
                ps.setString(3, T6230_F20.HKZ.name());
                ps.setString(4, T6230_F20.YJQ.name());
                try (ResultSet rs = ps.executeQuery();)
                {
                    if (rs.next())
                    {
                        jkTotle = rs.getBigDecimal(1);
                    }
                }
            }
        }
        return jkTotle;
    }
    
    /**
     * 待还本息
     * 
     * @param userId
     * @return
     * @throws Throwable
     */
    @SuppressWarnings("unused")
    private BigDecimal getDhbx(int userId)
        throws Throwable
    {
        if (userId < 0)
        {
            return null;
        }
        BigDecimal dhTotle = new BigDecimal(0);
        try (Connection conn = getConnection())
        {
            try (PreparedStatement ps =
                conn.prepareStatement("SELECT SUM(IFNULL(F07,0)) FROM S62.T6252 WHERE F03 = ? AND F09 = ?");)
            {
                ps.setInt(1, userId);
                ps.setString(2, T6252_F09.WH.name());
                try (ResultSet rs = ps.executeQuery();)
                {
                    if (rs.next())
                    {
                        dhTotle = rs.getBigDecimal(1);
                    }
                }
            }
        }
        return dhTotle;
    }
    
    @Override
    public boolean isSmrz()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F02 FROM S61.T6118 WHERE T6118.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        if (T6118_F02.parse(resultSet.getString(1)) == T6118_F02.TG)
                        {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    
    @Override
    public boolean isTxmm()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F05 FROM S61.T6118 WHERE T6118.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        if (T6118_F05.parse(resultSet.getString(1)) == T6118_F05.YSZ)
                        {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    
    @Override
    public boolean isEmail()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F04 FROM S61.T6118 WHERE T6118.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        if (T6118_F04.parse(resultSet.getString(1)) == T6118_F04.TG)
                        {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    
    @Override
    public boolean isBid()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01 FROM S62.T6230 WHERE T6230.F02 = ? AND T6230.F20 IN (?,?,?) LIMIT 1"))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                pstmt.setString(2, T6230_F20.DSH.name());
                pstmt.setString(3, T6230_F20.DFB.name());
                pstmt.setString(4, T6230_F20.SQZ.name());
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
    
    @Override
    public T6110 getUserInfo(int userId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            T6110 record = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F17, F18 FROM S61.T6110 WHERE T6110.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, userId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new T6110();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getString(2);
                        record.F03 = resultSet.getString(3);
                        record.F04 = resultSet.getString(4);
                        record.F05 = resultSet.getString(5);
                        record.F06 = T6110_F06.parse(resultSet.getString(6));
                        record.F07 = T6110_F07.parse(resultSet.getString(7));
                        record.F08 = T6110_F08.parse(resultSet.getString(8));
                        record.F09 = resultSet.getTimestamp(9);
                        record.F10 = T6110_F10.parse(resultSet.getString(10));
                        record.F17 = T6110_F17.parse(resultSet.getString(11));
                        record.F18 = T6110_F18.parse(resultSet.getString(12));
                    }
                }
            }
            return record;
        }
    }
    
    @Override
    public int getXyjf(int userId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F02 FROM S61.T6116 WHERE T6116.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, userId);
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
    }
    
    @Override
    public String getXyLevel(int userId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F05 FROM S61.T6116 WHERE T6116.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, userId);
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
    }
    
    @Override
    public T6144 getXyjl(int userId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            T6144 record = new T6144();
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04 FROM S61.T6144 WHERE T6144.F01 = ? "))
            {
                pstmt.setInt(1, userId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getInt(3);
                        record.F04 = resultSet.getInt(4);
                    }
                }
            }
            return record;
        }
    }
    
    @Override
    public BigDecimal getDhje(int userId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT SUM(F07) FROM S62.T6252 WHERE T6252.F03 = ? AND T6252.F09 = ? "))
            {
                pstmt.setInt(1, userId);
                pstmt.setString(2, T6252_F09.WH.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return resultSet.getBigDecimal(1);
                    }
                }
            }
            return new BigDecimal(0);
        }
    }
    
    @Override
    public boolean getYhrzxx()
        throws Throwable
    {
        T6118 t6118 = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09 FROM S61.T6118 WHERE T6118.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        t6118 = new T6118();
                        t6118.F01 = resultSet.getInt(1);
                        t6118.F02 = T6118_F02.parse(resultSet.getString(2));
                        t6118.F03 = T6118_F03.parse(resultSet.getString(3));
                        t6118.F04 = T6118_F04.parse(resultSet.getString(4));
                        t6118.F05 = T6118_F05.parse(resultSet.getString(5));
                        t6118.F06 = resultSet.getString(6);
                        t6118.F07 = resultSet.getString(7);
                        t6118.F08 = resultSet.getString(8);
                        t6118.F09 = T6118_F09.parse(resultSet.getString(9));
                    }
                }
            }
        }
        if (t6118 == null || T6118_F03.BTG.name().equals(t6118.F03.name()))
        {
            return false;
        }
        ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
        boolean isOpenWithPsd = BooleanParser.parse(configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD));
        if (isOpenWithPsd)
        {
            //如果需要交易密码，则校验
            if (T6118_F05.WSZ.name().equals(t6118.F05.name()))
            {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public T6118 getT6118(int id)
        throws Throwable
    {
        T6118 t6118 = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09 FROM S61.T6118 WHERE T6118.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        t6118 = new T6118();
                        t6118.F01 = resultSet.getInt(1);
                        t6118.F02 = T6118_F02.parse(resultSet.getString(2));
                        t6118.F03 = T6118_F03.parse(resultSet.getString(3));
                        t6118.F04 = T6118_F04.parse(resultSet.getString(4));
                        t6118.F05 = T6118_F05.parse(resultSet.getString(5));
                        t6118.F06 = resultSet.getString(6);
                        t6118.F07 = resultSet.getString(7);
                        t6118.F08 = resultSet.getString(8);
                        t6118.F09 = T6118_F09.parse(resultSet.getString(9));
                    }
                }
            }
        }
        return t6118;
    }
    
    @Override
    public T6147 getT6147()
        throws Throwable
    {
        T6147 t6147 = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01,F02,F03,F04,F05,F06 FROM S61.T6147 WHERE T6147.F02=? ORDER BY F06 DESC LIMIT 1"))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                try (ResultSet rs = pstmt.executeQuery())
                {
                    if (rs.next())
                    {
                        t6147 = new T6147();
                        t6147.F01 = rs.getInt(1);
                        t6147.F02 = rs.getInt(2);
                        t6147.F03 = rs.getInt(3);
                        t6147.F04 = T6147_F04.parse(rs.getString(4));
                        t6147.F05 = rs.getInt(5);
                        t6147.F06 = rs.getTimestamp(6);
                    }
                }
            }
        }
        return t6147;
    }
    
    /** {@inheritDoc} */
    
    @Override
    public boolean isNetSigned()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            return this.isNetSign(serviceResource.getSession().getAccountId(), connection);
        }
    }
    
}
