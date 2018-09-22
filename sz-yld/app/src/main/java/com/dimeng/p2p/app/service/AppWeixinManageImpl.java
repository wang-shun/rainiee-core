package com.dimeng.p2p.app.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S61.enums.T6123_F05;
import com.dimeng.p2p.S62.enums.T6252_F09;
import com.dimeng.p2p.common.enums.BankCardStatus;

public class AppWeixinManageImpl extends AbstractAppService implements AppWeixinManage
{
    public static class TermManageFactory implements ServiceFactory<AppWeixinManage>
    {
        
        @Override
        public AppWeixinManage newInstance(ServiceResource serviceResource)
        {
            return new AppWeixinManageImpl(serviceResource);
        }
    }
    
    public AppWeixinManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    /**
     * 修改app版本
     * @throws Throwable
     */
    @Override
    public void updateAppWeixin(String weixinAccount, int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                
                execute(connection, "DELETE FROM S71.T7181 WHERE F01=? OR F02 = ?", id, weixinAccount);
                
                execute(connection, "INSERT INTO S71.T7181 SET F01 = ?,F02 = ?", id, weixinAccount);
                
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
     * 修改app版本
     * @throws Throwable
     */
    @Override
    public String deleteAppWeixin(String weixinAccount)
        throws Throwable
    {
        
        String result = null;
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                try (PreparedStatement pstmt =
                    connection.prepareStatement("SELECT T6110.F02 FROM S71.T7181 T7181,S61.T6110 T6110 WHERE T7181.F01=T6110.F01 and T7181.F02=?"))
                {
                    pstmt.setString(1, weixinAccount);
                    try (ResultSet resultSet = pstmt.executeQuery())
                    {
                        if (resultSet.next())
                        {
                            result = resultSet.getString(1);
                            execute(connection, "DELETE FROM S71.T7181 WHERE F02 = ? ", weixinAccount);
                        }
                    }
                }
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
        return result;
    }
    
    /**
     * 查询微信关联账号
     * @return 
     * @throws Throwable
     */
    @Override
    public int searchAppWeixin(String weixinAccount)
        throws Throwable
    {
        int accountId = 0;
        try (Connection connection = getConnection())
        {
            
            try (PreparedStatement pstmt = connection.prepareStatement("SELECT F01 FROM S71.T7181 WHERE F02 = ?"))
            {
                pstmt.setString(1, weixinAccount);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        accountId = resultSet.getInt(1);
                    }
                }
            }
        }
        return accountId;
    }
    
    @Override
    public Map<String, String> getWeixinAccountInfo(int accountId)
        throws Throwable
    {
        Map<String, String> map = new HashMap<String, String>();
        
        try (Connection connection = getConnection())
        {
            // 查询账户名称
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT F02, F03 FROM S61.T6110 WHERE T6110.F01 = ?"))
            {
                ps.setInt(1, accountId);
                try (ResultSet resultSet = ps.executeQuery())
                {
                    if (resultSet.next())
                    {
                        //查询账户名称
                        map.put("accountName", resultSet.getString(1));
                        map.put("password", resultSet.getString(2));
                    }
                }
            }
        }
        
        return map;
    }
    
    @Override
    public void goOnAccount(int accountId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S11._1030 SET F04 = NULL WHERE F04 = ?"))
            {
                pstmt.setInt(1, accountId);
                pstmt.executeUpdate();
            }
        }
    }
    
    @Override
    public Map<String, String> getAccountInfo(int accountId)
        throws Throwable
    {
        Map<String, String> map = new HashMap<String, String>();
        
        //散标已赚金额
        BigDecimal sbyz = BigDecimal.ZERO;
        //
        BigDecimal yxyz = BigDecimal.ZERO;
        
        // 账户余额
        map.put("balance", getDecimal(getZhje(T6101_F03.WLZH, accountId)));
        
        // 冻结资金
        map.put("freezeFunds", getDecimal(getZhje(T6101_F03.SDZH, accountId)));
        
        try (Connection connection = getConnection())
        {
            // 优选理财账户数据
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT F02, F03, F04, F05 FROM S64.T6413 WHERE T6413.F01 = ? LIMIT 1"))
            {
                ps.setInt(1, accountId);
                try (ResultSet resultSet = ps.executeQuery())
                {
                    if (resultSet.next())
                    {
                        //优选理财已经赚金额
                        yxyz = resultSet.getBigDecimal(1);
                        map.put("yxyz", getDecimal(resultSet.getBigDecimal(1)));
                        
                        //优选理财账户资产
                        map.put("yxzc", getDecimal(resultSet.getBigDecimal(2)));
                        
                        //优选理财平均收益率
                        map.put("yxsyl", getDecimal(resultSet.getBigDecimal(3)));
                        
                        //优选理财持有量
                        map.put("yxcyl", getDecimal(resultSet.getBigDecimal(4)));
                    }
                }
            }
            // 散标投资账户资产(未还的本金+利息+罚息)
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT IFNULL(SUM(F07),0) FROM S62.T6252 WHERE T6252.F04 = ? AND F09=?"))
            {
                ps.setInt(1, accountId);
                ps.setString(2, T6252_F09.WH.name());
                try (ResultSet resultSet = ps.executeQuery())
                {
                    if (resultSet.next())
                    {
                        //散标账户资产
                        map.put("sbzc", getDecimal(resultSet.getBigDecimal(1)));
                    }
                }
            }
            // 查询账户名称
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT F02, F03 FROM S61.T6110 WHERE T6110.F01 = ?"))
            {
                ps.setInt(1, accountId);
                try (ResultSet resultSet = ps.executeQuery())
                {
                    if (resultSet.next())
                    {
                        // 查询账户名称
                        map.put("accountName", resultSet.getString(1));
                        map.put("password", resultSet.getString(2));
                    }
                }
            }
            
            // 散标已挣总金额
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT IFNULL(SUM(T6102.F06),0) from S61.T6102 WHERE T6102.F03 IN (?, ?, ?) AND "
                    + "T6102.F02 = (SELECT T6101.F01 FROM S61.T6101 WHERE T6101.F02 = ? AND T6101.F03 = ?)"))
            {
                ps.setInt(1, FeeCode.TZ_LX);
                ps.setInt(2, FeeCode.TZ_FX);
                ps.setInt(3, FeeCode.TZ_WYJ);
                ps.setInt(4, accountId);
                ps.setString(5, T6101_F03.WLZH.name());
                try (ResultSet resultSet = ps.executeQuery())
                {
                    if (resultSet.next())
                    {
                        sbyz =
                            resultSet.getBigDecimal(1)
                                .subtract(yxlcLx(connection, accountId))
                                .setScale(2, BigDecimal.ROUND_HALF_UP);
                        //散标已赚金额
                        map.put("sbyz", String.valueOf(sbyz));
                    }
                }
            }
            
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F02 FROM S62.T6263 WHERE T6263.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, accountId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        //散标已赚金额
                        map.put("sbyz",
                            String.valueOf(sbyz.add(resultSet.getBigDecimal(1)).setScale(2, BigDecimal.ROUND_HALF_UP)));
                        sbyz = sbyz.add(resultSet.getBigDecimal(1));
                    }
                }
            }
            
            // 散标持有量
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT COUNT(DISTINCT F01) FROM S62.T6251 WHERE T6251.F04 = ? AND T6251.F07 >0"))
            {
                ps.setInt(1, accountId);
                try (ResultSet resultSet = ps.executeQuery())
                {
                    if (resultSet.next())
                    {
                        //散标持有量
                        map.put("sbcyl", String.valueOf(resultSet.getInt(1)));
                    }
                }
            }
            
            // 查询平均年化利率
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT SUM(T6230.F06)/COUNT(1) FROM S62.T6230 INNER JOIN S62.T6251 ON T6230.F01 = T6251.F03"
                    + " WHERE T6251.F04 = ? AND T6251.F07 > 0"))
            {
                ps.setInt(1, accountId);
                try (ResultSet resultSet = ps.executeQuery())
                {
                    if (resultSet.next())
                    {
                        //查询平均年化利率
                        map.put("sbsyl",
                            String.valueOf(sbyz.add(resultSet.getBigDecimal(1)).setScale(2, BigDecimal.ROUND_HALF_UP)));
                    }
                }
            }
            //待收本金
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT SUM(F07) FROM S62.T6252 WHERE T6252.F04 = ? AND T6252.F05 = ? AND T6252.F09 = ? "))
            {
                pstmt.setInt(1, accountId);
                pstmt.setInt(2, FeeCode.TZ_BJ);
                pstmt.setString(3, T6252_F09.WH.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        map.put("dsbj",
                            String.valueOf(resultSet.getBigDecimal(1).setScale(2, BigDecimal.ROUND_HALF_UP)));
                    }
                }
            }
            //待收利息
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT SUM(F07) FROM S62.T6252 WHERE T6252.F04 = ? AND T6252.F05 = ? AND T6252.F09 = ? "))
            {
                pstmt.setInt(1, accountId);
                pstmt.setInt(2, FeeCode.TZ_LX);
                pstmt.setString(3, T6252_F09.WH.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        map.put("dslx",
                            String.valueOf(resultSet.getBigDecimal(1).setScale(2, BigDecimal.ROUND_HALF_UP)));
                    }
                }
            }
            //获取贷款负债
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT IFNULL(SUM(F07),0) FROM S62.T6252 WHERE T6252.F03 = ? AND T6252.F09 = ?"))
            {
                ps.setInt(1, accountId);
                ps.setString(2, T6252_F09.WH.name());
                try (ResultSet resultSet = ps.executeQuery())
                {
                    if (resultSet.next())
                    {
                        map.put("loanAmount",
                            String.valueOf(resultSet.getBigDecimal(1).setScale(2, BigDecimal.ROUND_HALF_UP)));
                    }
                }
            }
            
            //未读信息
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT COUNT(F01) FROM S61.T6123 WHERE T6123.F02 = ? AND T6123.F05 = ? LIMIT 1"))
            {
                pstmt.setInt(1, accountId);
                pstmt.setString(2, T6123_F05.WD.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        map.put("letterCount", String.valueOf(resultSet.getInt(1)));
                    }
                }
            }
            //银行卡数量
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT COUNT(*) FROM S61.T6114 INNER JOIN S50.T5020 ON T6114.F03 = T5020.F01 WHERE T6114.F02 = ? AND T6114.F08 = ?"))
            {
                pstmt.setInt(1, accountId);
                pstmt.setString(2, BankCardStatus.QY.toString());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        map.put("bankCount", String.valueOf(resultSet.getInt(1)));
                    }
                }
            }
        }
        map.put("yzzje", String.valueOf(yxyz.add(sbyz)));
        
        return map;
    }
    
    /**
     * 根据资金账户查询金额
     * 
     * @param f03
     * @param accountId
     * @return
     * @throws Throwable
     */
    private BigDecimal getZhje(T6101_F03 f03, int accountId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F06 FROM S61.T6101 WHERE T6101.F02 = ? AND T6101.F03 = ? LIMIT 1"))
            {
                pstmt.setInt(1, accountId);
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
    
    /**
     * 优选理财利息加罚息
     * 
     * @param connection
     * @param accountId
     * @return
     * @throws SQLException
     */
    private BigDecimal yxlcLx(Connection connection, int accountId)
        throws SQLException
    {
        BigDecimal lx = new BigDecimal(0);
        try (PreparedStatement ps =
            connection.prepareStatement("SELECT IFNULL(SUM(F07),0)   FROM S64.T6412 WHERE F09 = 'YH' AND F04=? AND F05=7002");)
        {
            ps.setInt(1, accountId);
            try (ResultSet rs = ps.executeQuery();)
            {
                if (rs.next())
                {
                    lx = rs.getBigDecimal(1);
                }
            }
        }
        return lx;
    }
    
    @Override
    public BigDecimal getLoanAmount(int accountId) throws Throwable {
        try (Connection connection = getConnection()) {
            try (PreparedStatement ps = connection
                    .prepareStatement("SELECT IFNULL(SUM(F07),0) FROM S62.T6252 WHERE T6252.F03 = ? AND T6252.F09 = ?")) {
                ps.setInt(1, accountId);
                ps.setString(2, T6252_F09.WH.name());
                try (ResultSet resultSet = ps.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getBigDecimal(1);
                    }
                }
            }
        }
        return new BigDecimal(0);
    }
    
    private String getDecimal(final BigDecimal value)
    {
        return String.valueOf(value.setScale(2, BigDecimal.ROUND_HALF_UP));
    }
}
