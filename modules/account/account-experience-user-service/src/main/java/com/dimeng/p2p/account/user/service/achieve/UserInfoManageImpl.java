package com.dimeng.p2p.account.user.service.achieve;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.entities.T6118;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.S61.enums.T6110_F08;
import com.dimeng.p2p.S61.enums.T6110_F10;
import com.dimeng.p2p.S61.enums.T6110_F18;
import com.dimeng.p2p.S61.enums.T6110_F19;
import com.dimeng.p2p.S61.enums.T6118_F02;
import com.dimeng.p2p.S61.enums.T6118_F03;
import com.dimeng.p2p.S61.enums.T6118_F04;
import com.dimeng.p2p.S61.enums.T6118_F05;
import com.dimeng.p2p.S61.enums.T6118_F09;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.S62.enums.T6252_F09;
import com.dimeng.p2p.S64.enums.T6410_F07;
import com.dimeng.p2p.account.user.service.UserInfoManage;
import com.dimeng.p2p.account.user.service.entity.UserInfo;
import com.dimeng.p2p.variables.defines.pays.PayVariavle;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BooleanParser;

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
    
    /**
     * 查询用户风险备用金资金
     * @return
     * @throws Throwable
     */
    @Override
    public T6101 searchFxbyj()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            T6101 record = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06 FROM S61.T6101 WHERE T6101.F02 = ? AND T6101.F03 = ? LIMIT 1"))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                pstmt.setString(2, T6101_F03.FXBZJZH.name());
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
    public UserInfo search(int userId)
        throws Throwable
    {
        if (userId < 0)
        {
            return null;
        }
        UserInfo userInfo = new UserInfo();
        String sql =
            "SELECT T6110.F01 AS F01, T6110.F02 AS F02, T6110.F09 AS F03, T6141.F05 AS F04 FROM S61.T6110 INNER JOIN S61.T6141 ON T6110.F01 = T6141.F01 WHERE T6110.F01 = ? LIMIT 1";
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql))
            {
                ps.setInt(1, userId);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        userInfo.F01 = rs.getInt(1);
                        userInfo.F02 = rs.getString(2);
                        userInfo.F09 = rs.getTimestamp(3);
                        userInfo.F15 = rs.getString(4);
                    }
                }
            }
            userInfo.cyzqsl = cyzqsl(connection, userId);
            userInfo.cylcjhsl = cylcjhsl(connection, userId);
            userInfo.fbjksl = getCreditCount(connection, userId);
            userInfo.cgjksl = getCreditSuccCount(connection, userId);
            userInfo.whqjksl = getWhqCount(connection, userId);
            userInfo.yqje = getYqMoney(connection, userId);
            String sql1 =
                "SELECT T6144.F02 AS F01, T6144.F03 AS F02, T6116.F02 AS F03 FROM S61.T6144 INNER JOIN S61.T6116 ON T6144.F01 = T6116.F01 WHERE T6144.F01 = ? LIMIT 1";
            try (PreparedStatement ps1 = connection.prepareStatement(sql1);)
            {
                ps1.setInt(1, userId);
                try (ResultSet rs = ps1.executeQuery();)
                {
                    if (rs.next())
                    {
                        userInfo.yqcs = rs.getInt(1);
                        userInfo.yzyqcs = rs.getInt(2);
                        userInfo.xyjf = rs.getInt(3);
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
    private int cyzqsl(Connection connection, int userId)
        throws Throwable
    {
        String sql =
            "SELECT COUNT(*) AS F01 FROM S62.T6251 INNER JOIN S62.T6230 ON T6251.F03 = T6230.F01 WHERE T6251.F04 = ? AND T6230.F20 IN (?,?,?)";
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
        return 0;
    }
    
    /**
     * 查询持有优选理财数量
     * 
     * @param userId
     * @return
     * @throws Throwable
     */
    private int cylcjhsl(Connection connection, int userId)
        throws Throwable
    {
        String sql =
            "SELECT COUNT(*) AS F01 FROM S64.T6411 INNER JOIN S64.T6410 ON T6411.F02 = T6410.F01 WHERE T6411.F03 = ? AND T6410.F07 = ?";
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
        return 0;
    }
    
    /**
     * 逾期金额
     * 
     * @param userId
     * @return
     * @throws Throwable
     */
    private BigDecimal getYqMoney(Connection connection, int userId)
        throws Throwable
    {
        if (userId < 0)
        {
            return null;
        }
        BigDecimal yqMoney = new BigDecimal(0);
        try (PreparedStatement ps =
            connection.prepareStatement("SELECT IFNULL(SUM(F07),0) FROM S62.T6252 WHERE F03 = ?  AND F09 = ? AND F10 < ?");)
        {
            ps.setInt(1, userId);
            ps.setString(2, T6252_F09.WH.name());
            ps.setDate(3, getCurrentDate(connection));
            try (ResultSet rs = ps.executeQuery();)
            {
                if (rs.next())
                {
                    yqMoney = rs.getBigDecimal(1);
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
    private int getCreditCount(Connection connection, int userId)
        throws Throwable
    {
        if (userId < 0)
        {
            return 0;
        }
        int jkCount = 0;
        try (PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) FROM S62.T6230 WHERE F02 = ?");)
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
        return jkCount;
    }
    
    /**
     * 借款成功笔数
     * 
     * @param userId
     * @return
     * @throws Throwable
     */
    private int getCreditSuccCount(Connection connection, int userId)
        throws Throwable
    {
        if (userId < 0)
        {
            return 0;
        }
        int succCount = 0;
        try (PreparedStatement ps =
            connection.prepareStatement("SELECT COUNT(*) FROM S62.T6230 WHERE F02 = ? AND F20 IN (?,?,?)");)
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
    private int getWhqCount(Connection connection, int userId)
        throws Throwable
    {
        if (userId < 0)
        {
            return 0;
        }
        int whqCount = 0;
        try (PreparedStatement ps =
            connection.prepareStatement("SELECT COUNT(*) FROM S62.T6230 WHERE F02 = ? AND F20 IN (?,?)");)
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
        return whqCount;
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
    public T6110 getUserInfo(int userId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            T6110 record = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F15, F18, F19 FROM S61.T6110 WHERE T6110.F01 = ? LIMIT 1"))
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
                        record.F15 = resultSet.getTimestamp(11);
                        record.F18 = T6110_F18.parse(resultSet.getString(12));
                        record.F19 = T6110_F19.parse(resultSet.getString(13));
                    }
                }
            }
            return record;
        }
    }
    
    @Override
    public T6110 getUserInfoByAccountName(String accountName)
        throws Throwable
    {
        if (StringHelper.isEmpty(accountName))
        {
            return null;
        }
        try (Connection connection = getConnection())
        {
            T6110 record = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F15, F18, F19 FROM S61.T6110 WHERE T6110.F02 = ? OR T6110.F04 = ? OR T6110.F05 = ? LIMIT 1"))
            {
                pstmt.setString(1, accountName);
                pstmt.setString(2, accountName);
                pstmt.setString(3, accountName);
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
                        record.F15 = resultSet.getTimestamp(11);
                        record.F18 = T6110_F18.parse(resultSet.getString(12));
                        record.F19 = T6110_F19.parse(resultSet.getString(13));
                    }
                }
            }
            return record;
        }
    }
    
    @Override
    public int getUserLoginError(UserInfo userInfo, String ip)
        throws Throwable
    {
        int errorTimes = 0;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT IFNULL(SUM(F03),0) FROM S11._1037 WHERE _1037.F01 = ? OR _1037.F01 = ? OR _1037.F01 = ? AND _1037.F02 = ? "))
            {
                pstmt.setString(1, userInfo.F02);
                pstmt.setString(2, userInfo.F04);
                pstmt.setString(3, userInfo.F05);
                pstmt.setString(4, ip);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        errorTimes = resultSet.getInt(1);
                    }
                }
            }
        }
        return errorTimes;
    }
    
    @Override
    public UserInfo search(String userName)
        throws Throwable
    {
        if (StringHelper.isEmpty(userName))
        {
            return null;
        }
        UserInfo userInfo = new UserInfo();
        String sql =
            "SELECT T6110.F01 AS F01, T6110.F02 AS F02, T6110.F04 AS F03, T6110.F05 AS F04 FROM S61.T6110 WHERE T6110.F02 = ? OR T6110.F04 = ? OR T6110.F05 = ? LIMIT 1";
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql))
            {
                ps.setString(1, userName);
                ps.setString(2, userName);
                ps.setString(3, userName);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        userInfo.F01 = rs.getInt(1);
                        userInfo.F02 = rs.getString(2);
                        userInfo.F04 = rs.getString(3);
                        userInfo.F05 = rs.getString(4);
                    }
                }
            }
        }
        return userInfo;
    }
    
    /**
     * 清除登录错误次数
     * 
     * @param userName
     * @param ip
     */
    @Override
    public void clearErrorCount(String userName, String ip)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            String phoneStr = "";
            String emalStr = "";
            try (PreparedStatement pst =
                connection.prepareStatement("SELECT F02, F04, F05 FROM S61.T6110 WHERE F02 = ? OR F04=? OR F05=? "))
            {
                pst.setString(1, userName);
                pst.setString(2, userName);
                pst.setString(3, userName);
                try (ResultSet rs = pst.executeQuery())
                {
                    if (rs.next())
                    {
                        userName = rs.getString(1);
                        phoneStr = rs.getString(2);
                        emalStr = rs.getString(3);
                    }
                }
            }
            try (PreparedStatement pstmt =
                connection.prepareStatement("UPDATE S11._1037 SET F03= 0 WHERE _1037.F01 IN (?,?,?) AND _1037.F02 = ? "))
            {
                pstmt.setString(1, userName);
                pstmt.setString(2, phoneStr);
                pstmt.setString(3, emalStr);
                pstmt.setString(4, ip);
                pstmt.execute();
            }
        }
        
    }
    
    @Override
    public void udpateT6198F05F07(int userId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            execute(connection,
                "UPDATE S61.T6198 SET F05=F05+1,F07=? WHERE F02 = ?",
                getCurrentTimestamp(connection),
                userId);
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
}
