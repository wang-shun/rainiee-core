package com.dimeng.p2p.order;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.data.sql.SQLConnection;
import com.dimeng.framework.resource.Resource;
import com.dimeng.framework.resource.ResourceAnnotation;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.entities.T6102;
import com.dimeng.p2p.S61.entities.T6105;
import com.dimeng.p2p.S61.entities.T6106;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S61.enums.T6102_F10;
import com.dimeng.p2p.S61.enums.T6106_F05;
import com.dimeng.p2p.S63.entities.T6350;
import com.dimeng.p2p.S63.entities.T6351;
import com.dimeng.p2p.S63.entities.T6352;
import com.dimeng.p2p.S63.entities.T6356;
import com.dimeng.p2p.S63.entities.T6359;
import com.dimeng.p2p.S63.enums.T6350_F07;
import com.dimeng.p2p.S63.enums.T6351_F11;
import com.dimeng.p2p.S63.enums.T6352_F06;
import com.dimeng.p2p.S63.enums.T6356_F03;
import com.dimeng.p2p.S63.enums.T6356_F04;
import com.dimeng.p2p.S63.enums.T6359_F08;
import com.dimeng.p2p.S65.entities.T6555;
import com.dimeng.p2p.S65.entities.T6556;
import com.dimeng.p2p.repeater.score.entity.AddressResult;
import com.dimeng.p2p.variables.defines.MallVariavle;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.DateParser;
import com.dimeng.util.parser.IntegerParser;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
/**
 * 
 * 商品购买执行器
 * <功能详细描述>
 * 
 * @author  heluzhu
 * @version  [版本号, 2015年12月25日]
 */
@ResourceAnnotation
public class MallChangeExecutor extends AbstractOrderExecutor
{
    
    public MallChangeExecutor(ResourceProvider resourceProvider)
    {
        super(resourceProvider);
    }
    
    @Override
    protected void doConfirm(SQLConnection connection, int orderId, Map<String, String> params)
        throws Throwable
    {
        T6555 t6555 = selectT6555(connection, orderId);
        if (t6555 == null)
        {
            throw new LogicalException("订单详细不存在！");
        }
        List<T6556> t6556s = selectT6556s(connection, orderId);
        T6352 t6352 = new T6352();
        t6352.F02 = t6555.F02;
        t6352.F03 = getCrid(connection);
        t6352.F05 = "PC";
        t6352.F06 = T6352_F06.balance;
        int buyId = insertT6352(connection, t6352);
        for (T6556 t6556 : t6556s)
        {
            T6351 t6351 = queryGoodsByIdForUpdate(connection, t6556.F02);
            if (t6351 == null)
            {
                throw new LogicalException("所购买的商品不存在或已下架!");
            }

            if (t6556.F04 > t6351.F06)
            {
                throw new LogicalException("商品的库存不足");
            }
            
            T6101 wlzh = selectT6101(connection, t6555.F02, T6101_F03.WLZH, true);
            if (wlzh == null)
            {
                throw new LogicalException("您购买账号不存在");
            }
            if (wlzh.F06.compareTo(t6351.F15.multiply(new BigDecimal(t6556.F04))) < 0)
            {
                throw new LogicalException("您的账户余额不足");
            }
            
            int pid = getPTID(connection);
            if (pid <= 0)
            {
                throw new LogicalException("平台账号不存在");
            }
            
            // 平台往来账户信息
            T6101 ptwlzh = selectT6101(connection, pid, T6101_F03.WLZH, true);
            if (ptwlzh == null)
            {
                throw new LogicalException("平台往来账户不存在");
            }
            
            BigDecimal amount = t6556.F07.multiply(new BigDecimal(t6556.F04));
            
            {
                wlzh.F06 = wlzh.F06.subtract(amount);
                updateT6101(connection, wlzh.F06, wlzh.F01);
                // 资金流水
                T6102 t6102 = new T6102();
                t6102.F02 = wlzh.F01;
                t6102.F03 = FeeCode.MALL_BUY;
                t6102.F04 = ptwlzh.F01;
                t6102.F07 = amount;
                t6102.F08 = wlzh.F06;
                t6102.F09 = String.format("订单号:%s，商品名称：%s", t6352.F03, t6351.F03);
                insertT6102(connection, t6102);
            }
            {
                // 增加入账账户金额
                ptwlzh.F06 = ptwlzh.F06.add(amount);
                updateT6101(connection, ptwlzh.F06, ptwlzh.F01);
                T6102 t6102 = new T6102();
                t6102.F02 = ptwlzh.F01;
                t6102.F03 = FeeCode.MALL_BUY;
                t6102.F04 = wlzh.F01;
                t6102.F06 = amount;
                t6102.F08 = ptwlzh.F06;
                t6102.F09 = String.format("订单号:%s，商品名称：%s", t6352.F03, t6351.F03);
                insertT6102(connection, t6102);
            }
            
            T6359 t6359 = new T6359();
            t6359.F02 = buyId;
            t6359.F03 = t6556.F02;
            t6359.F04 = 0;
            t6359.F05 = amount;
            t6359.F06 = t6556.F04;
            t6359.F07 = t6351.t6350.F07 == T6350_F07.virtual ? t6556.F06 : "";
            t6359.F08 = T6359_F08.pendding;
            if (t6351.t6350.F07 == T6350_F07.kind)
            {
                AddressResult address = queryById(connection, t6555.F03, t6555.F02);
                if (address != null)
                {
                    t6359.F13 = address.receiverName;
                    t6359.F14 = address.region;
                    t6359.F15 = address.address;
                    t6359.F16 = address.telephone;
                    t6359.F17 = address.code;
                }
            }
            int buyCount = queryBuyCount(connection, t6556.F02, t6555.F02);
            if (t6351.F18 > 0 && (t6556.F04 + buyCount) > t6351.F18)
            {
                throw new LogicalException("不能超过限购数：" + t6351.F18 + "<br/>已购买数量："+ buyCount);
            }
            insertT6359(connection, t6359);
            updateT6351(connection, t6556.F04, t6351.F01);
            if (t6556.F05 > 0)
            {
                deleteT6358(connection, t6556.F05);
            }
            try (PreparedStatement ps = connection.prepareStatement("UPDATE S65.T6555 SET F05 = ? WHERE F01 = ?"))
            {
                ps.setInt(1, buyId);
                ps.setInt(2, t6555.F01);
                ps.executeUpdate();
            }
        }
        
        //赠送积分异常不影响购买
        try
        {
            ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
            boolean is_mall = Boolean.parseBoolean(configureProvider.getProperty(MallVariavle.IS_MALL));
            if(is_mall){
                //现金购买商品赠送积分
                giveScore(connection, t6555.F02,t6555.F04,getCurrentTimestamp(connection));
            }
        }
        catch (Exception e)
        {
            logger.error(e, e);
        }
        
    }
    
    protected int insertT6352(Connection connection, T6352 t6352)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S63.T6352 SET F02 = ?, F03 = ?, F04 = NOW(), F05 = ?, F06 = ? ",
                PreparedStatement.RETURN_GENERATED_KEYS))
        {
            pstmt.setInt(1, t6352.F02);
            pstmt.setString(2, t6352.F03);
            pstmt.setString(3, t6352.F05);
            pstmt.setString(4, t6352.F06.name());
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
    
    /**
     * 生成订单编号
     * @return
     * @throws Throwable
     */
    protected String getCrid(Connection connection)
        throws Throwable
    {
        String serNo = "";
        try (PreparedStatement pstmt =
            connection.prepareStatement(" SELECT MAX(F03) FROM S63.T6352 WHERE date_format(T6352.F04,'%Y-%m-%d')=date_format(CURDATE(),'%Y-%m-%d')"))
        {
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    String no = resultSet.getString(1);
                    if (!StringHelper.isEmpty(no))
                    {
                        serNo +=
                            DateParser.format(new Date(), "yyMMdd")
                                + String.format("%05d", Integer.parseInt(no.substring(no.length() - 5)) + 1);
                    }
                    else
                    {
                        serNo += DateParser.format(new Date(), "yyMMdd") + "00001";
                    }
                }
            }
        }
        return serNo;
    }
    
    protected T6351 queryGoodsByIdForUpdate(Connection connection, int id)
        throws Throwable
    {
        T6351 t6351 = null;
        try (PreparedStatement statement =
            connection.prepareStatement("SELECT T6351.F01 AS F01, T6351.F06 AS F02, T6351.F18 AS F03,T6351.F11 AS F04,T6351.F07 AS F05,T6350.F07 AS F06,T6351.F05 AS F07,T6351.F15 AS F08,T6351.F03 AS F09 FROM S63.T6351,S63.T6350 WHERE T6351.F04 = T6350.F01 AND T6351.F11 = 'sold' AND T6351.F01 = ? FOR UPDATE "))
        {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery())
            {
                if (rs.next())
                {
                    t6351 = new T6351();
                    t6351.F01 = rs.getInt(1);
                    t6351.F06 = rs.getInt(2);
                    t6351.F18 = rs.getInt(3);
                    t6351.F11 = T6351_F11.parse(rs.getString(4));
                    t6351.F07 = rs.getInt(5);
                    if (t6351.t6350 == null)
                    {
                        t6351.t6350 = new T6350();
                    }
                    t6351.t6350.F07 = T6350_F07.valueOf(rs.getString(6));
                    t6351.F05 = rs.getInt(7);
                    t6351.F15 = rs.getBigDecimal(8);
                    t6351.F03 = rs.getString(9);
                }
            }
        }
        return t6351;
    }
    
    protected int queryBuyCount(Connection connection, int goodId, int userId)
        throws Throwable
    {
        try (PreparedStatement statement =
            connection.prepareStatement("SELECT SUM(T6359.F06) FROM S63.T6359 LEFT JOIN S63.T6352 ON T6359.F02 = T6352.F01 WHERE T6352.F02 = ? AND T6359.F03 = ? "))
        {
            statement.setInt(1, userId);
            statement.setInt(2, goodId);
            try (ResultSet rs = statement.executeQuery())
            {
                if (rs.next())
                {
                    return rs.getInt(1);
                }
                return 0;
            }
        }
    }
    
    protected int insertT6359(Connection connection, T6359 t6359)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S63.T6359 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F13 = ?, F14 = ?, F15 = ?, F16 = ?, F17 = ? ",
                PreparedStatement.RETURN_GENERATED_KEYS))
        {
            pstmt.setInt(1, t6359.F02);
            pstmt.setInt(2, t6359.F03);
            pstmt.setInt(3, t6359.F04);
            pstmt.setBigDecimal(4, t6359.F05);
            pstmt.setInt(5, t6359.F06);
            pstmt.setString(6, t6359.F07);
            pstmt.setString(7, t6359.F08.name());
            pstmt.setString(8, t6359.F13);
            pstmt.setInt(9, t6359.F14);
            pstmt.setString(10, t6359.F15);
            pstmt.setString(11, t6359.F16);
            pstmt.setString(12, t6359.F17);
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
    
    protected void updateT6101(Connection connection, BigDecimal F01, int F02)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S61.T6101 SET F06 = ?, F07 = now()  WHERE F01 = ?"))
        {
            pstmt.setBigDecimal(1, F01);
            pstmt.setInt(2, F02);
            pstmt.execute();
        }
    }
    
    protected void updateT6351(Connection connection, int F01, int F03)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S63.T6351 SET F06 = F06-?,F07 = F07+1 WHERE F01 = ?"))
        {
            pstmt.setInt(1, F01);
            pstmt.setInt(2, F03);
            pstmt.execute();
        }
    }
    
    protected void deleteT6358(Connection connection, int F01)
        throws Throwable
    {
        try (PreparedStatement pstmt = connection.prepareStatement("DELETE FROM S63.T6358 WHERE F01 = ?"))
        {
            pstmt.setInt(1, F01);
            pstmt.execute();
        }
    }
    
    @Override
    public Class<? extends Resource> getIdentifiedType()
    {
        return MallChangeExecutor.class;
    }
    
    protected T6555 selectT6555(Connection connection, int orderId)
        throws Throwable
    {
        T6555 record = null;
        try (PreparedStatement statement =
            connection.prepareStatement("SELECT F01,F02,F03,F04,F05 FROM S65.T6555 WHERE F01=?"))
        {
            statement.setInt(1, orderId);
            try (ResultSet resultSet = statement.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6555();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getInt(3);
                    record.F04 = resultSet.getBigDecimal(4);
                    record.F05 = resultSet.getInt(5);
                }
            }
        }
        return record;
    }
    
    protected AddressResult queryById(Connection connection, int id, int userId)
        throws Throwable
    {
        AddressResult result = null;
        try (PreparedStatement statement =
            connection.prepareStatement("SELECT F01,F02,F03,F04,F05,F06,F07,F08 FROM S63.T6355 WHERE F01 = ? AND F02 = ? "))
        {
            statement.setInt(1, id);
            statement.setInt(2, userId);
            try (ResultSet rs = statement.executeQuery())
            {
                if (rs.next())
                {
                    result = new AddressResult();
                    result.id = rs.getInt(1);
                    result.region = rs.getInt(4);
                    result.receiverName = rs.getString(3);
                    result.address = rs.getString(5);
                    result.telephone = rs.getString(6);
                    result.code = rs.getString(7);
                    result.isDefault = rs.getString(8);
                }
            }
        }
        return result;
    }
    
    protected List<T6556> selectT6556s(Connection connection, int orderId)
        throws Throwable
    {
        List<T6556> list = null;
        try (PreparedStatement statement =
            connection.prepareStatement("SELECT F01,F02,F03,F04,F05,F06,F07 FROM S65.T6556 WHERE F03=?"))
        {
            statement.setInt(1, orderId);
            try (ResultSet resultSet = statement.executeQuery())
            {
                while (resultSet.next())
                {
                    T6556 record = new T6556();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getInt(3);
                    record.F04 = resultSet.getInt(4);
                    record.F05 = resultSet.getInt(5);
                    record.F06 = resultSet.getString(6);
                    record.F07 = resultSet.getBigDecimal(7);
                    if (list == null)
                    {
                        list = new ArrayList<T6556>();
                    }
                    list.add(record);
                }
            }
        }
        return list;
    }
    
    protected void giveScore(Connection connection, Integer userId,BigDecimal amount,Timestamp now){
        try
        {
            T6106 t6106 = new T6106();
            t6106.F02 = userId;
            T6356 t6356 = getT6356(connection);
            if(null == t6356){
                throw new ParameterException("积分规则不存在");
            }
            String score = t6356.F02;
            if(StringHelper.isEmpty(score) || "0".equals(score)){
                throw new ParameterException("积分值错误："+score);
            }
            t6106.F05 = T6106_F05.buygoods;
            if(null == amount){
                throw new ParameterException("金额为空");
            }
            setT6106F03(t6106,amount,score);
            t6106.F04 = now;
            t6106.F07 = now;
            //保存用户积分
            saveT6106(t6106,connection);
            //更新用户积分账户
            updateT6105(t6106,connection,now);
        }
        catch (Exception e)
        {
            logger.error(e, e);
        }
        catch (Throwable e)
        {
            logger.error(e, e);
        }
    }
    
    /**
     * <积分规则设置表>
     * @return T6356
     * @throws SQLException 
     */
    private T6356 getT6356(Connection connection) throws SQLException
    {
        
        try (PreparedStatement pstmt = connection
            .prepareStatement("SELECT F01,F02,F03,F04,F05,F06 FROM S63.T6356 WHERE F03=? AND F04=? LIMIT 1")) {
            pstmt.setString(1, T6106_F05.buygoods.name());
            pstmt.setString(2, T6356_F04.on.name());
            try (ResultSet rs = pstmt.executeQuery()) {
                T6356 t6356 = null;
                if (rs.next()) {
                    t6356 = new T6356();
                    t6356.F01 = rs.getInt(1);
                    t6356.F02 = rs.getString(2);
                    t6356.F03 = T6356_F03.parse(rs.getString(3));
                    t6356.F04 = T6356_F04.parse(rs.getString(4));
                    t6356.F05 = rs.getTimestamp(5);
                    t6356.F06 = rs.getTimestamp(5);
                }
                return t6356;
            }
        }
    }
    
    /**
     * <设置现金购买商品积分类型的积分值>
     * <功能详细描述>
     * @param t6106
     * @param amount
     * @param score
     */
    private void setT6106F03(T6106 t6106,BigDecimal amount,String score) throws Throwable{
        int amountInt = amount.intValue();
        String[] scores = score.split(",");
        int amountMark = IntegerParser.parse(scores[0]);
        if(amountMark>amountInt){
            throw new ParameterException("不符合赠送积分规则,赠送类型:"+t6106.F05.name()+",amount:"+amount);
        }
        
        t6106.F03 = amountInt*IntegerParser.parse(scores[1])/amountMark;
    }
    
    private void saveT6106(T6106 t6106,Connection connection) throws Throwable{
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S61.T6106 SET F02=?,F03=?,F04=?,F05=?,F07=?",
                PreparedStatement.RETURN_GENERATED_KEYS))
        {
            pstmt.setInt(1, t6106.F02);
            pstmt.setInt(2, t6106.F03);
            pstmt.setTimestamp(3, t6106.F04);
            pstmt.setString(4, t6106.F05.name());
            pstmt.setTimestamp(5, t6106.F07);
            pstmt.execute();
        }
    }
    
    private void updateT6105(T6106 t6106,Connection connection,Timestamp now)
        throws Throwable
    {
        if (t6106.F02 <= 0)
        {
            throw new ParameterException("赠送积分用户不存在");
        }
        addScoreAccount(connection,t6106.F02,now);
        T6105 t6105 = getT6105(t6106.F02,connection);
        if(null == t6105){
            throw new ParameterException("用户积分账户不存在");
        }
        try (PreparedStatement pstmt = connection
            .prepareStatement("UPDATE S61.T6105 SET F03=F03+?,F07=? WHERE F02 = ?")) {
            pstmt.setInt(1, t6106.F03);
            pstmt.setTimestamp(2, now);
            pstmt.setInt(3, t6106.F02);
            pstmt.execute();
        }
    }
    
    /**
     * <用户积分账户>
     * @return T6105
     */
    private void addScoreAccount(Connection connection,int F02,Timestamp now)
        throws Throwable
    {
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT F01 FROM S61.T6105 WHERE F02=?"))
        {
            pstmt.setInt(1, F02);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (!resultSet.next())
                {
                    try (PreparedStatement pstmt1 =
                                 connection.prepareStatement("INSERT INTO S61.T6105 SET F02 = ?, F06 = ?, F07 = ?"))
                    {
                        pstmt1.setInt(1, F02);
                        pstmt1.setTimestamp(2, now);
                        pstmt1.setTimestamp(3, now);
                        pstmt1.execute();
                    }
                }
            }
        }
    }
    
    /**
     * <用户积分账户>
     * @return T6105
     * @throws SQLException 
     */
    private T6105 getT6105(int F02,Connection connection) throws SQLException
    {
        
        try (PreparedStatement pstmt = connection
            .prepareStatement("SELECT F01,F02,F03,F04,F05,F06,F07 FROM S61.T6105 WHERE F01=(SELECT F01 FROM S61.T6105 WHERE F02=? LIMIT 1) FOR UPDATE")) {
            pstmt.setInt(1, F02);
            try (ResultSet rs = pstmt.executeQuery()) {
                T6105 t6105 = null;
                if (rs.next()) {
                    t6105 = new T6105();
                    t6105.F01 = rs.getInt(1);
                    t6105.F02 = rs.getInt(2);
                    t6105.F03 = rs.getInt(3);
                    t6105.F04 = rs.getInt(4);
                    t6105.F05 = rs.getInt(5);
                    t6105.F06 = rs.getTimestamp(6);
                    t6105.F07 = rs.getTimestamp(7);
                }
                return t6105;
            }
        }
    }
    
}