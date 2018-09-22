package com.dimeng.p2p.modules.base.pay.service.achieve;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.OrderType;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S61.enums.T6147_F04;
import com.dimeng.p2p.S62.entities.T6230;
import com.dimeng.p2p.S62.entities.T6231;
import com.dimeng.p2p.S62.entities.T6238;
import com.dimeng.p2p.S62.entities.T6252;
import com.dimeng.p2p.S62.entities.T6280;
import com.dimeng.p2p.S62.enums.T6216_F18;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.S62.enums.T6230_F28;
import com.dimeng.p2p.S62.enums.T6231_F19;
import com.dimeng.p2p.S62.enums.T6231_F21;
import com.dimeng.p2p.S62.enums.T6231_F33;
import com.dimeng.p2p.S62.enums.T6252_F09;
import com.dimeng.p2p.S62.enums.T6280_F10;
import com.dimeng.p2p.S65.entities.T6501;
import com.dimeng.p2p.S65.entities.T6504;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.S65.enums.T6501_F07;
import com.dimeng.p2p.S65.enums.T6504_F06;
import com.dimeng.p2p.common.RiskLevelCompareUtil;
import com.dimeng.p2p.common.enums.FrontLogType;
import com.dimeng.p2p.modules.base.pay.service.DefaultInterestManage;
import com.dimeng.p2p.variables.defines.RegulatoryPolicyVariavle;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.DateHelper;
import com.dimeng.util.parser.BigDecimalParser;
import com.dimeng.util.parser.BooleanParser;

public class DefaultInterestManageImpl extends AbstractBaseManage implements DefaultInterestManage
{
    
    public DefaultInterestManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public void calculate()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                Date currentDate = getCurrentDate(connection);
                T6252[] t6252s = selectAllT6252(connection, currentDate);
                if (t6252s == null || t6252s.length == 0)
                {
                    logger.info("自动计算罚息T6252表没有相关数据");
                    serviceResource.rollback(connection);
                    return;
                }
                Map<Integer, T6238> cache = new HashMap<>();
                T6252 defaultInterest = new T6252();
                defaultInterest.F05 = FeeCode.TZ_FX;
                BigDecimal zero = new BigDecimal(0);
                Map<String, String> map = new HashMap<String, String>();
                for (T6252 t6252 : t6252s)
                {
                    T6238 t6238 = selectT6238(connection, cache, t6252.F02);
                    if (t6238 == null || t6238.F04.compareTo(zero) <= 0)
                    {
                        continue;
                    }
                    int days =
                        (int)Math.floor((currentDate.getTime() - t6252.F08.getTime()) / DateHelper.DAY_IN_MILLISECONDS);
                    defaultInterest.F02 = t6252.F02;
                    defaultInterest.F03 = t6252.F03;
                    defaultInterest.F04 = t6252.F04;
                    defaultInterest.F06 = t6252.F06;
                    defaultInterest.F07 = t6252.F07.multiply(t6238.F04).multiply(new BigDecimal(days));
                    defaultInterest.F08 = t6252.F08;
                    defaultInterest.F09 = t6252.F09;
                    defaultInterest.F11 = t6252.F11;
                    int id = 0;
                    try (PreparedStatement ps =
                        connection.prepareStatement("SELECT F01 FROM S62.T6252 WHERE F05=? AND F06=? AND F11=?"))
                    {
                        ps.setInt(1, FeeCode.TZ_FX);
                        ps.setInt(2, t6252.F06);
                        ps.setInt(3, t6252.F11);
                        try (ResultSet rs = ps.executeQuery())
                        {
                            if (rs.next())
                            {
                                id = rs.getInt(1);
                            }
                        }
                    }
                    if (id <= 0)
                    {
                        insertT6252(connection, defaultInterest);
                    }
                    else
                    {
                        updateT6252(connection, defaultInterest.F07, id);
                    }
                    if (days > 0)
                    {
                        T6231_F19 t6231_F19 = T6231_F19.F;
                        if (days <= 30)
                        {
                            t6231_F19 = T6231_F19.S;
                        }
                        else if (days > 30)
                        {
                            t6231_F19 = T6231_F19.YZYQ;
                        }
                        try (PreparedStatement pstmt =
                            connection.prepareStatement("UPDATE S62.T6231 SET F19 = ? WHERE F01 = ?"))
                        {
                            pstmt.setString(1, t6231_F19.name());
                            pstmt.setInt(2, t6252.F02);
                            pstmt.execute();
                        }
                    }
                    
                    String keyStr =
                        String.valueOf(t6252.F02) + String.valueOf(t6252.F03) + String.valueOf(t6252.F05)
                            + String.valueOf(t6252.F06);
                    if (map.get(keyStr) == null)
                    {
                        // 更新用户信用档案
                        int t6146Id = 0;
                        try (PreparedStatement ps =
                            connection.prepareStatement("SELECT F01 FROM S61.T6146 WHERE F02=? AND F04=? AND F05=?"))
                        {
                            ps.setInt(1, t6252.F03);
                            ps.setInt(2, t6252.F06);
                            ps.setInt(3, t6252.F11);
                            try (ResultSet rs = ps.executeQuery())
                            {
                                if (rs.next())
                                {
                                    t6146Id = rs.getInt(1);
                                }
                            }
                        }
                        
                        if (t6146Id <= 0)
                        {
                            insertT6146(connection, t6252, days);
                        }
                        else
                        {
                            updateT6146(connection, t6252, days);
                        }
                        
                        Map<String, Integer> maps = getYqsj(connection, t6252.F03);
                        try (PreparedStatement ps =
                            connection.prepareStatement("UPDATE S61.T6144 SET F02 = ?,F03 = ?,F04 = ?,F05 = ? WHERE F01 = ?"))
                        {
                            ps.setInt(1, maps.get("yqcs"));
                            ps.setInt(2, maps.get("yzyqcs"));
                            ps.setInt(3, maps.get("zcyqts"));
                            ps.setTimestamp(4, getCurrentTimestamp(connection));
                            ps.setInt(5, t6252.F03);
                            ps.executeUpdate();
                        }
                        map.put(keyStr, "1");
                    }
                }
                map.clear();
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    private T6252[] selectAllT6252(Connection connection, Date currentDate)
        throws SQLException
    {
        ArrayList<T6252> list = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, SUM(F07), F08, F09, F11 FROM S62.T6252 WHERE (T6252.F05 = ? OR T6252.F05=?) AND T6252.F08 < ?  AND (T6252.F09 = 'WH' OR T6252.F09='HKZ') GROUP BY F02,F04,F06,F11  FOR UPDATE"))
        {
            pstmt.setInt(1, FeeCode.TZ_BJ);
            pstmt.setInt(2, FeeCode.TZ_LX);
            pstmt.setDate(3, currentDate);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                while (resultSet.next())
                {
                    T6252 record = new T6252();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getInt(3);
                    record.F04 = resultSet.getInt(4);
                    record.F05 = resultSet.getInt(5);
                    record.F06 = resultSet.getInt(6);
                    record.F07 = resultSet.getBigDecimal(7);
                    record.F08 = resultSet.getDate(8);
                    record.F09 = T6252_F09.parse(resultSet.getString(9));
                    record.F11 = resultSet.getInt(10);
                    if (list == null)
                    {
                        list = new ArrayList<>();
                    }
                    list.add(record);
                }
            }
        }
        return ((list == null || list.size() == 0) ? null : list.toArray(new T6252[list.size()]));
    }
    
    private T6238 selectT6238(Connection connection, Map<Integer, T6238> cache, int F01)
        throws SQLException
    {
        Integer key = Integer.valueOf(F01);
        T6238 record = cache.get(key);
        if (record != null)
        {
            return record;
        }
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04 FROM S62.T6238 WHERE T6238.F01 = ? "))
        {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6238();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getBigDecimal(2);
                    record.F03 = resultSet.getBigDecimal(3);
                    record.F04 = resultSet.getBigDecimal(4);
                    cache.put(key, record);
                }
            }
        }
        return record;
    }
    
    private int insertT6252(Connection connection, T6252 entity)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S62.T6252 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?, F11 = ?",
                PreparedStatement.RETURN_GENERATED_KEYS))
        {
            pstmt.setInt(1, entity.F02);
            pstmt.setInt(2, entity.F03);
            pstmt.setInt(3, entity.F04);
            pstmt.setInt(4, entity.F05);
            pstmt.setInt(5, entity.F06);
            pstmt.setBigDecimal(6, entity.F07);
            pstmt.setDate(7, entity.F08);
            pstmt.setString(8, entity.F09.name());
            pstmt.setInt(9, entity.F11);
            pstmt.execute();
            try (ResultSet resultSet = pstmt.getGeneratedKeys();)
            {
                if (resultSet.next())
                {
                    return resultSet.getInt(1);
                }
                return 0;
            }
        }
    }
    
    private void updateT6252(Connection connection, BigDecimal amount, int id)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S62.T6252 SET F07 = ? WHERE F01=? AND (F09='WH' OR F09='HKZ')",
                PreparedStatement.RETURN_GENERATED_KEYS))
        {
            pstmt.setBigDecimal(1, amount);
            pstmt.setInt(2, id);
            pstmt.execute();
        }
    }
    
    @Override
    public List<Integer> autoBid()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
                boolean zjb =
                    BooleanParser.parse(serviceResource.getResource(ConfigureProvider.class)
                        .getProperty(SystemVariable.BID_SFZJKT));
                //是否开启风险承受能力评估
                boolean isOpenRiskAccess =
                    Boolean.parseBoolean(configureProvider.getProperty(RegulatoryPolicyVariavle.IS_OPEN_RISK_ASSESS));
                //标产品是否增加投资限制
                boolean isInvestLimit =
                    Boolean.parseBoolean(configureProvider.getProperty(RegulatoryPolicyVariavle.IS_INVEST_LIMIT));
                List<Integer> orderIds = new ArrayList<>();
                List<Integer> accountIds = new ArrayList<>();
                Queue<T6230> bidIds = getBid(connection);
                if (bidIds.isEmpty())
                {
                    serviceResource.rollback(connection);
                    return orderIds;
                }
                // 用户账户余额map
                Map<Integer, BigDecimal> maps = new HashMap<>();
                while (!bidIds.isEmpty())
                {
                    T6230 t6230 = bidIds.poll();
                    Queue<T6280> t6280s = selectAllT6280(connection, t6230);
                    if (t6280s.isEmpty())
                        continue;
                    // 剩余可投金额
                    BigDecimal syktje = t6230.F07;
                    // 当前用户账户余额
                    BigDecimal amount = BigDecimal.ZERO;
                    //产品风险等级
                    T6216_F18 productRiskLevel = getProductRiskLevel(connection, t6230.F32);
                    
                    while (!t6280s.isEmpty())
                    {
                        T6280 t6280 = t6280s.poll();
                        
                        //获取标的担保方ID（购买人不能购买自己担保的标）
                        int assureId = selectT6236(connection, t6230.F01);
                        if (t6280.F12 == assureId)
                        {
                            logger.warn("不可投资自己担保的标");
                            continue;
                        }
                        
                        if (!zjb && t6280.F12 == t6230.F02)
                        {
                            logger.warn("标：" + t6230.F01 + "不能投自己所发的标");
                            continue;
                        }
                        if (isExists(connection, t6280.F12, t6230.F01))
                        {
                            continue;
                        }
                        /*用户风险等级与产品风向等级是否匹配*/
                        T6147_F04 userRiskLevel = getUserRiskLevel(connection, t6280.F12);
                        if (isOpenRiskAccess && isInvestLimit
                            && !RiskLevelCompareUtil.compareRiskLevel(userRiskLevel, productRiskLevel.name()))
                        {
                            logger.info("用户ID:" + t6280.F12 + ",风险承受等级:"
                                + (userRiskLevel == null ? "没设置" : userRiskLevel.getChineseName()) + ",低于产品的风险承受等级:"
                                + productRiskLevel.getChineseName());
                            continue;
                        }
                        
                        if (maps.containsKey(t6280.F12))
                        {
                            amount = maps.get(t6280.F12);
                        }
                        else
                        {
                            amount = getUserAccount(connection, t6280.F12);
                        }
                        
                        // 投资金额
                        BigDecimal tbAmount = BigDecimal.ZERO;
                        BigDecimal bfbAmount = BigDecimal.ZERO;
                        
                        if (t6280.F13 == 1)
                        {
                            tbAmount = t6280.F02;
                        }
                        else
                        {
                            //tbAmount = new BigDecimal(100*amount.divide(new BigDecimal(100)).intValue());//取100的整数倍
                            tbAmount = amount.setScale(0, BigDecimal.ROUND_FLOOR);//取整数
                        }
                        if (tbAmount.compareTo(t6230.F28.F25) < 0)
                        {
                            continue;
                        }
                        if (t6230.F28.F33 == T6231_F33.APP)
                        {
                            logger.warn("投资端：" + t6230.F28.F33.name() + ",此标仅供APP端投资");
                            continue;
                        }
                        amount = amount.subtract(tbAmount);
                        if (t6280.F13 == 1 && amount.compareTo(t6280.F09) < 0)
                        {
                            continue;
                        }
                        
                        {
                            // 借款总额的百分比
                            BigDecimal bfb =
                                BigDecimalParser.parse(configureProvider.getProperty(SystemVariable.AUTO_BIDING_MAX_PERCENT));
                            bfbAmount = t6230.F05.multiply(bfb);
                            // 单笔投资金额若超过该标借款总额的20.0%，则按照该标借款总额的20.0%金额投资。
                            if (tbAmount.compareTo(bfbAmount) > 0)
                            {
                                tbAmount = bfbAmount.setScale(0, BigDecimal.ROUND_FLOOR);
                            }
                            
                        }
                        {
                            // 投资进度达到百分比停止投资
                            BigDecimal tbjd =
                                BigDecimalParser.parse(configureProvider.getProperty(SystemVariable.AUTO_BIDING_END_PROGRESS));
                            // 已投资金额
                            BigDecimal ytbAoumt = t6230.F05.subtract(syktje);
                            // 投资进度达到95.0%时停止自动投资
                            if (ytbAoumt.divide(t6230.F05, BigDecimal.ROUND_DOWN).compareTo(tbjd) >= 0)
                            {
                                continue;
                            }
                            // 自动投资后的金额
                            BigDecimal tbhAmount = ytbAoumt.add(tbAmount);
                            // 若投资后投资进度超过95.0%，则投达到95.0%的剩余金额.
                            if (tbhAmount.divide(t6230.F05, BigDecimal.ROUND_DOWN).compareTo(tbjd) > 0)
                            {
                                tbAmount = t6230.F05.multiply(tbjd).subtract(ytbAoumt);
                            }
                            if (t6230.F28 != null && (tbAmount.doubleValue() < t6230.F28.F25.doubleValue()))
                            {
                                continue;
                            }
                            
                            // 最大投资额
                            BigDecimal zdtzje = selectYtje(connection, t6280.F12, t6230.F01);
                            if (t6230.F28 != null && tbAmount.compareTo(t6230.F28.F26.subtract(zdtzje)) > 0)
                            {
                                logger.info("您该项目的投资总金额已超过最大投资金额" + t6230.F28.F26 + "元;当前可投金额为"
                                    + t6230.F28.F26.subtract(zdtzje) + "元");
                                continue;
                            }
                            
                            syktje = t6230.F07.subtract(tbAmount);
                            if (t6230.F28 != null && syktje.compareTo(new BigDecimal(0)) > 0
                                && syktje.compareTo(t6230.F28.F25) < 0)
                            {
                                logger.info("剩余可投金额不能低于最低起投金额,剩余可投金额:" + syktje);
                                continue;
                            }
                            logger.info("======自动投资条件满足==============");
                            T6504 t6504 = new T6504();
                            t6504.F02 = t6280.F12;
                            t6504.F03 = t6230.F01;
                            t6504.F04 = tbAmount;
                            t6504.F06 = T6504_F06.S;
                            orderIds.add(addOrder(connection, t6504));
                            accountIds.add(t6280.F12);
                            updateRule(t6280.F01, connection);
                        }
                        // 剩余可投金额
                        syktje = syktje.subtract(tbAmount);
                        maps.put(t6280.F12, amount);
                        
                    }
                }
                serviceResource.commit(connection);
                for (int accountId : accountIds)
                {
                    writeFrontLog(FrontLogType.ZDTB.getName(), "前台自动投资", accountId);
                }
                return orderIds;
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    /**
     * 查询担保机构
     * @param connection
     * @param F02
     * @return
     * @throws SQLException
     */
    private int selectT6236(Connection connection, int F02)
        throws SQLException
    {
        int F03 = 0;
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT F03 FROM S62.T6236 WHERE T6236.F02 = ?"))
        {
            pstmt.setInt(1, F02);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    F03 = resultSet.getInt(1);
                }
            }
        }
        return F03;
    }
    
    /**
     * 获取产品风险等级
     * @param connection
     * @param id
     * @return
     * @throws Throwable
     */
    private T6216_F18 getProductRiskLevel(Connection connection, int id)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F18 FROM S62.T6216 WHERE T6216.F01=? LIMIT 1"))
        {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery())
            {
                if (rs.next())
                {
                    return T6216_F18.parse(rs.getString(1));
                }
            }
        }
        return T6216_F18.BSX;
    }
    
    /**
     * 查选当前用户的风险承受等级及风险评估分数
     * @param id
     * @return
     * @throws Throwable
     */
    private T6147_F04 getUserRiskLevel(Connection connection, int id)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F04 FROM S61.T6147 WHERE T6147.F02=? ORDER BY F06 DESC LIMIT 1"))
        {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery())
            {
                if (rs.next())
                {
                    return T6147_F04.parse(rs.getString(1));
                }
            }
        }
        return null;
    }
    
    /**
     * 查询某用户对标的的投资金额之和
     * 
     * @param connection
     * @param userId
     * @param bidId
     * @return
     * @throws SQLException
     */
    private BigDecimal selectYtje(Connection connection, int userId, int bidId)
        throws SQLException
    {
        BigDecimal ytje = BigDecimal.ZERO;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT SUM(t6504.F04) FROM S65.T6504 t6504 LEFT JOIN S65.T6501 t6501 ON t6504.F01 = t6501.F01 WHERE t6504.F02 = ? AND t6504.F03 = ? AND t6501.F03 = ?"))
        {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, bidId);
            pstmt.setString(3, T6501_F03.CG.name());
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    ytje = resultSet.getBigDecimal(1);
                }
            }
        }
        return ytje;
    }
    
    public boolean isYuqi(int accountId, Connection connection)
        throws Throwable
    {
        String sql = "SELECT DATEDIFF(?,F08) FROM S62.T6252 WHERE F09=? AND F03=? AND F08 < SYSDATE()";
        try (PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setTimestamp(1, getCurrentTimestamp(connection));
            ps.setString(2, T6252_F09.WH.name());
            ps.setInt(3, accountId);
            try (ResultSet rs = ps.executeQuery())
            {
                while (rs.next())
                {
                    int days = rs.getInt(1);
                    if (days > 0)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    protected boolean isExists(Connection connection, int userId, int loanId)
        throws Throwable
    {
        try (PreparedStatement ps =
            connection.prepareStatement("SELECT F01 FROM S65.T6504 WHERE F02=? AND F03=? AND F06 = ?"))
        {
            ps.setInt(1, userId);
            ps.setInt(2, loanId);
            ps.setString(3, T6504_F06.S.name());
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * 查询符号条件的标
     */
    /*protected List<T6230> getBid(Connection connection, T6280 t6280)
    		throws Throwable {
    	List<T6230> lists = new ArrayList<>();
    	try (PreparedStatement pstmt = connection
    			.prepareStatement("SELECT T6230.F01,T6230.F05,T6230.F07,T6230.F28 FROM S62.T6230 LEFT JOIN S62.T6231 ON T6230.F01 = T6231.F01 WHERE T6230.F06 >= ? AND T6230.F06<=? AND ((T6230.F09>=? AND T6230.F09<=? AND T6231.F21 = 'F') OR (T6231.F22 >= ?*30 AND T6231.F22 <= ?*30 AND T6231.F21 = 'S')) AND T6230.F20=?")) {
    		pstmt.setBigDecimal(1, t6280.F03);
    		pstmt.setBigDecimal(2, t6280.F04);
    		pstmt.setInt(3, t6280.F05);
    		pstmt.setInt(4, t6280.F06);
    		pstmt.setInt(5, t6280.F05);
            pstmt.setInt(6, t6280.F06);
    		pstmt.setString(7, T6230_F20.TBZ.name());
    		try (ResultSet resultSet = pstmt.executeQuery()) {
    			while (resultSet.next()) {
    				T6230 t6230 = new T6230();
    				t6230.F01 = resultSet.getInt(1);
    				t6230.F05 = resultSet.getBigDecimal(2);
    				t6230.F07 = resultSet.getBigDecimal(3);
                    t6230.xsb = T6230_F28.parse(resultSet.getString(4));
    				lists.add(t6230);
    			}
    		}
    	}
    	return lists;
    }*/
    
    protected Queue<T6230> getBid(Connection connection)
        throws SQLException
    {
        Queue<T6230> bidQueues = new LinkedList<T6230>();
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT T6230.F01,T6230.F05,T6230.F07,T6230.F28,T6231.F21,T6231.F22,T6231.F25,T6231.F26,T6230.F06,T6230.F09,T6230.F32,T6231.F33 FROM S62.T6230 LEFT JOIN S62.T6231 ON T6230.F01 = T6231.F01 WHERE T6230.F20=? AND T6230.F07 > 0 AND DATE_ADD( T6230.F22, INTERVAL T6230.F08 DAY ) >= CURRENT_DATE () ORDER BY T6230.F22 ASC "))
        {
            pstmt.setString(1, T6230_F20.TBZ.name());
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                while (resultSet.next())
                {
                    T6230 t6230 = new T6230();
                    t6230.F01 = resultSet.getInt(1);
                    t6230.F05 = resultSet.getBigDecimal(2);
                    t6230.F07 = resultSet.getBigDecimal(3);
                    t6230.xsb = T6230_F28.parse(resultSet.getString(4));
                    t6230.F28 = new T6231();
                    t6230.F28.F21 = T6231_F21.parse(resultSet.getString(5));
                    t6230.F28.F22 = resultSet.getInt(6);
                    t6230.F28.F25 = resultSet.getBigDecimal(7);
                    t6230.F28.F26 = resultSet.getBigDecimal(8);
                    t6230.F06 = resultSet.getBigDecimal(9);
                    t6230.F09 = resultSet.getInt(10);
                    t6230.F32 = resultSet.getInt(11);
                    t6230.F28.F33 = T6231_F33.parse(resultSet.getString(12));
                    bidQueues.offer(t6230);
                }
            }
        }
        return bidQueues;
    }
    
    /**
     * 查询用户账户余额
     * 
     * @param userId
     * @return
     * @throws Throwable
     */
    protected BigDecimal getUserAccount(Connection connection, int userId)
        throws Throwable
    {
        BigDecimal amount = new BigDecimal(0);
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F06 FROM S61.T6101 WHERE T6101.F02 = ? AND T6101.F03 = ? LIMIT 1"))
        {
            pstmt.setInt(1, userId);
            pstmt.setString(2, T6101_F03.WLZH.name());
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    amount = resultSet.getBigDecimal(1);
                }
            }
        }
        return amount;
    }
    
    protected int addOrder(Connection connection, T6504 t6504)
        throws Throwable
    {
        /*int orderId = 0;
        try (PreparedStatement pstmt = connection
        		.prepareStatement(
        				"INSERT INTO S65.T6501 SET F02 = ?,F03 = ?, F04 = ?, F07 = ?, F08 = ?",
        				PreparedStatement.RETURN_GENERATED_KEYS)) {
        	pstmt.setInt(1, OrderType.BID.orderType());
        	pstmt.setString(2, T6501_F03.DTJ.name());
        	pstmt.setTimestamp(3, getCurrentTimestamp(connection));
        	pstmt.setString(4, T6501_F07.HT.name());
        	pstmt.setInt(5, t6504.F02);
        	pstmt.execute();
        	try (ResultSet resultSet = pstmt.getGeneratedKeys();) {
        		if (resultSet.next()) {
        			orderId = resultSet.getInt(1);
        		}
        	}
        }*/
        T6501 t6501 = new T6501();
        t6501.F02 = OrderType.BID.orderType();
        t6501.F03 = T6501_F03.DTJ;
        t6501.F04 = getCurrentTimestamp(connection);
        t6501.F07 = T6501_F07.HT;
        t6501.F08 = t6504.F02;
        t6501.F13 = t6504.F04;
        int orderId = insertT6501(connection, t6501);
        
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S65.T6504 SET F01 = ?, F02 = ?, F03 = ?, F04 = ?, F06 = ?"))
        {
            pstmt.setInt(1, orderId);
            pstmt.setInt(2, t6504.F02);
            pstmt.setInt(3, t6504.F03);
            pstmt.setBigDecimal(4, t6504.F04);
            pstmt.setString(5, t6504.F06.name());
            pstmt.execute();
        }
        return orderId;
    }
    
    protected void updateRule(int id, Connection conneciton)
        throws Throwable
    {
        execute(conneciton,
            "UPDATE S62.T6280 SET F11 = ?,F14 = ? WHERE F01 = ? ",
            getCurrentTimestamp(conneciton),
            System.currentTimeMillis(),
            id);
    }
    
    /**
     * 
     * 查询自动投资设置
     * 
     * @param connection
     * @return
     * @throws SQLException
     */
    /*protected T6280[] selectAllT6280(Connection connection) throws SQLException {
    	ArrayList<T6280> list = null;
    	try (PreparedStatement pstmt = connection
    			.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11 FROM S62.T6280 WHERE F10=? ORDER BY F11")) {
    		pstmt.setString(1, T6280_F10.QY.name());
    		try (ResultSet resultSet = pstmt.executeQuery()) {
    			while (resultSet.next()) {
    				T6280 record = new T6280();
    				record.F01 = resultSet.getInt(1);
    				record.F02 = resultSet.getBigDecimal(2);
    				record.F03 = resultSet.getBigDecimal(3);
    				record.F04 = resultSet.getBigDecimal(4);
    				record.F05 = resultSet.getInt(5);
    				record.F06 = resultSet.getInt(6);
    				record.F07 = resultSet.getInt(7);
    				record.F08 = resultSet.getInt(8);
    				record.F09 = resultSet.getBigDecimal(9);
    				record.F10 = T6280_F10.parse(resultSet.getString(10));
    				record.F11 = resultSet.getTimestamp(11);
    				if (list == null) {
    					list = new ArrayList<>();
    				}
    				list.add(record);
    			}
    		}
    	}
    	return ((list == null || list.size() == 0) ? null : list
    			.toArray(new T6280[list.size()]));
    }*/
    
    protected Queue<T6280> selectAllT6280(Connection connection, T6230 t6230)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT T6280.F01, T6280.F02, T6280.F03, T6280.F04, T6280.F05, T6280.F06, T6280.F07, T6280.F08, T6280.F09, T6280.F10, T6280.F11, T6280.F12, T6280.F13 FROM S62.T6280 LEFT JOIN S61.T6110 ON T6110.F01 = T6280.F12 WHERE T6110.F07 = 'QY' AND T6110.F06 = 'ZRR' AND T6280.F10=? ");
        if (t6230.F06 != null && t6230.F06.doubleValue() > 0)
        {
            sql.append(" AND T6280.F03 <= ? AND T6280.F04 >=?");
        }
        sql.append(" ORDER BY T6280.F11 ASC,T6280.F14 ASC ");
        ArrayList<T6280> list = null;
        try (PreparedStatement pstmt = connection.prepareStatement(sql.toString()))
        {
            pstmt.setString(1, T6280_F10.QY.name());
            if (t6230.F06 != null && t6230.F06.doubleValue() > 0)
            {
                pstmt.setBigDecimal(2, t6230.F06);
                pstmt.setBigDecimal(3, t6230.F06);
            }
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                while (resultSet.next())
                {
                    T6280 record = new T6280();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getBigDecimal(2);
                    record.F03 = resultSet.getBigDecimal(3);
                    record.F04 = resultSet.getBigDecimal(4);
                    record.F05 = resultSet.getInt(5);
                    record.F06 = resultSet.getInt(6);
                    record.F07 = resultSet.getInt(7);
                    record.F08 = resultSet.getInt(8);
                    record.F09 = resultSet.getBigDecimal(9);
                    record.F10 = T6280_F10.parse(resultSet.getString(10));
                    record.F11 = resultSet.getTimestamp(11);
                    record.F12 = resultSet.getInt(12);
                    record.F13 = resultSet.getInt(13);
                    if (list == null)
                    {
                        list = new ArrayList<>();
                    }
                    list.add(record);
                }
            }
        }
        Queue<T6280> queueRules = new LinkedList<T6280>();
        if (list != null)
        {
            for (T6280 t6280 : list)
            {
                if (isExists(connection, t6280.F12, t6230.F01))
                {
                    continue;
                }
                //是否有逾期
                if (isYuqi(t6280.F12, connection))
                {
                    continue;
                }
                //有投资、购买债权记录的用户不能投资新手标
                if (T6230_F28.S.equals(t6230.xsb) && (getXsbCount(t6280.F12) > 0 || getZqzrCount(t6280.F12) > 0))
                {
                    continue;
                }
                if (t6280.F13 == 1
                    && t6230.F28 != null
                    && (t6280.F02.doubleValue() < t6230.F28.F25.doubleValue() || t6280.F02.doubleValue() > t6230.F28.F26.doubleValue()))
                {
                    continue;
                }
                if (t6230.F28 != null && T6231_F21.S.name().equals(t6230.F28.F21.name()))
                {
                    int startDay = t6280.F05 * 30;
                    int endDay = t6280.F06 * 30;
                    if (startDay <= t6230.F28.F22 && endDay >= t6230.F28.F22)
                    {
                        queueRules.offer(t6280);
                    }
                }
                else if (t6230.F28 != null && T6231_F21.F.name().equals(t6230.F28.F21.name()))
                {
                    if (t6230.F09 >= t6280.F05 && t6230.F09 <= t6280.F06)
                    {
                        queueRules.offer(t6280);
                    }
                }
            }
        }
        return queueRules;
    }
    
    /**
     * 获取某用户的债权转让数量
     * @param userId
     * @return
     * @throws Throwable
     */
    protected int getZqzrCount(int userId)
        throws SQLException
    {
        try (Connection connection = getConnection())
        {
            int count = 0;
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT count(*) FROM S62.T6262  WHERE T6262.F03 = ? "))
            {
                ps.setInt(1, userId);
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
    
    /**
     * 已投新手标数量
     * <功能详细描述>
     * @param userId
     * @return
     * @throws Throwable
     */
    protected int getXsbCount(int userId)
        throws SQLException
    {
        try (Connection connection = getConnection())
        {
            int count = 0;
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT count(*) FROM S62.T6250  WHERE T6250.F03 = ? "))
            {
                ps.setInt(1, userId);
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
    
    private int insertT6146(Connection connection, T6252 entity, int day)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S61.T6146 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?",
                PreparedStatement.RETURN_GENERATED_KEYS))
        {
            pstmt.setInt(1, entity.F03);
            pstmt.setInt(2, day);
            pstmt.setInt(3, entity.F06);
            pstmt.setInt(4, entity.F11);
            pstmt.execute();
            try (ResultSet resultSet = pstmt.getGeneratedKeys();)
            {
                if (resultSet.next())
                {
                    return resultSet.getInt(1);
                }
                return 0;
            }
        }
    }
    
    private void updateT6146(Connection connection, T6252 entity, int day)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S61.T6146 SET F03 = ? WHERE F02=? AND F04=? AND F05=?",
                PreparedStatement.RETURN_GENERATED_KEYS))
        {
            pstmt.setInt(1, day);
            pstmt.setInt(2, entity.F03);
            pstmt.setInt(3, entity.F06);
            pstmt.setInt(4, entity.F11);
            pstmt.execute();
        }
    }
    
    private Map<String, Integer> getYqsj(Connection connection, int userId)
        throws Throwable
    {
        Map<String, Integer> map = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT a.F01,b.F01,c.F01 FROM (SELECT COUNT(t.F01) F01 FROM (SELECT COUNT(T6146.F03) F01 FROM S61.T6146 LEFT JOIN S62.T6251 ON T6251.F01 = T6146.F05 WHERE T6146.F02=? AND T6146.F03<31 GROUP BY T6146.F02,T6146.F04,T6251.F03) t ) a,"
                + "(SELECT COUNT(F03) F01 FROM S61.T6146 WHERE F02=? AND F03>30) b,"
                + "(SELECT IFNULL(MAX(F03),0) F01 FROM S61.T6146 WHERE F02=?) c"))
        {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, userId);
            pstmt.setInt(3, userId);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    map = new HashMap<String, Integer>();
                    map.put("yqcs", resultSet.getInt(1));
                    map.put("yzyqcs", resultSet.getInt(2));
                    map.put("zcyqts", resultSet.getInt(3));
                }
            }
        }
        return map;
    }
    
}
