package com.dimeng.p2p.modules.score.mall.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.upload.FileStore;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.p2p.OrderType;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.entities.T6105;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S61.enums.T6103_F06;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6118_F02;
import com.dimeng.p2p.S61.enums.T6118_F05;
import com.dimeng.p2p.S62.enums.T6252_F09;
import com.dimeng.p2p.S63.entities.T6350;
import com.dimeng.p2p.S63.entities.T6351;
import com.dimeng.p2p.S63.entities.T6352;
import com.dimeng.p2p.S63.entities.T6355;
import com.dimeng.p2p.S63.entities.T6358;
import com.dimeng.p2p.S63.entities.T6359;
import com.dimeng.p2p.S63.enums.T6340_F03;
import com.dimeng.p2p.S63.enums.T6340_F04;
import com.dimeng.p2p.S63.enums.T6340_F08;
import com.dimeng.p2p.S63.enums.T6340_F09;
import com.dimeng.p2p.S63.enums.T6342_F04;
import com.dimeng.p2p.S63.enums.T6344_F09;
import com.dimeng.p2p.S63.enums.T6350_F07;
import com.dimeng.p2p.S63.enums.T6351_F11;
import com.dimeng.p2p.S63.enums.T6352_F06;
import com.dimeng.p2p.S63.enums.T6359_F08;
import com.dimeng.p2p.S65.entities.T6501;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.S65.enums.T6501_F07;
import com.dimeng.p2p.common.entities.ActivityInfo;
import com.dimeng.p2p.common.entities.GrBaseInfo;
import com.dimeng.p2p.common.enums.YesOrNo;
import com.dimeng.p2p.repeater.score.MallChangeManage;
import com.dimeng.p2p.repeater.score.entity.AddressResult;
import com.dimeng.p2p.repeater.score.entity.OrderGoods;
import com.dimeng.p2p.repeater.score.entity.ShoppingCarResult;
import com.dimeng.p2p.repeater.score.entity.UserAccount;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.p2p.variables.defines.pays.PayVariavle;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BooleanParser;
import com.dimeng.util.parser.DateParser;
import com.dimeng.util.parser.IntegerParser;

/**
 * 商品兑换
 * <功能详细描述>
 * 
 * @author  heluzhu
 * @version  [版本号, 2015年12月16日]
 */
public class MallChangeManageImpl extends AbstractMallService implements MallChangeManage
{
    
    public MallChangeManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public void editAddress(T6355 t6355)
        throws Throwable
    {}
    
    @Override
    public void delAddress(int id)
        throws Throwable
    {}
    
    @Override
    public AddressResult queryById(int id)
        throws Throwable
    {
		return null;}
    
    @Override
    public AddressResult[] queryByUser()
        throws Throwable
    {
		return null;}
    
    @Override
    public ShoppingCarResult[] queryCar()
        throws Throwable
    {
		return null;}
    
    @Override
    public void editCar(int id, int num)
        throws Throwable
    {}
    
    protected T6358 getExistGoodsInCar(Connection connection, int id)
        throws Throwable
    {
		return null;}
    
    @Override
    public void delCar(List<Integer> ids)
        throws Throwable
    {}
    
    protected int queryBuyCount(Connection connection, int goodId, int userId)
        throws Throwable
    {
		return userId;}
    
    @Override
    public synchronized int toChangeByBalance(List<OrderGoods> goodsList, String type, int addressId)
        throws Throwable
    {
		return addressId;}
    
    @SuppressWarnings("unused")
	private void insertT6555(Connection connection, int F01, int F02, int F03, BigDecimal F04)
        throws SQLException
    {}
    
    @SuppressWarnings("unused")
	private void insertT6556(Connection connection, int F01, int F02, int F03, int F04, String mobile, BigDecimal F07)
        throws SQLException
    {}
    
    @Override
    public synchronized void toChangeByScore(List<OrderGoods> goodsList, String type, int addressId)
        throws Throwable
    {}
    
    @SuppressWarnings("unused")
	private void insertAct(Connection connection, int ruleId, int userId)
        throws Throwable
    {}
    
    @SuppressWarnings("unused")
	private ActivityInfo getActInfo(Connection connection, int ruleId)
        throws Throwable
    {
		return null;}
    
    @SuppressWarnings("unused")
	private void updateT6501F13(Connection connection, BigDecimal F13, int F01)
        throws Throwable
    {}
    
    /*private int insertT6501(Connection connection, int F02, T6501_F03 F03, T6501_F07 F07, int F08)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S65.T6501 SET F02 = ?, F03 = ?, F04 = CURRENT_TIMESTAMP(), F07 = ?, F08 = ?",
                PreparedStatement.RETURN_GENERATED_KEYS))
        {
            pstmt.setInt(1, F02);
            pstmt.setString(2, F03.name());
            pstmt.setString(3, F07.name());
            pstmt.setInt(4, F08);
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
    }*/
    
    @SuppressWarnings("unused")
	private int insertT6352(Connection connection, T6352 t6352)
        throws Throwable
    {
		return 0;}
    
    /**
     * 生成订单编号
     * @return
     * @throws Throwable
     */
    protected String getCrid()
        throws Throwable
    {
		return null;}
    
    protected void updateT6101(Connection connection, BigDecimal F01, int F02)
        throws Throwable
    {}
    
    /**
     * 获取用户总积分
     * <功能详细描述>
     * @param connection
     * @return
     * @throws Throwable
     */
    protected T6105 selectT6105(Connection connection)
        throws Throwable
    {
		return null;}
    
    protected void updateT6105(Connection connection, int F01, int F02, int F03, int F04)
        throws Throwable
    {}
    
    /**
     * 根据商品id查询商品
     * <功能详细描述>
     * @param connection
     * @param id
     * @return
     * @throws Throwable
     */
    private T6351 queryGoodsById(Connection connection, int id)
        throws Throwable
    {
		return null;}
    
    /**
     * 查询用户购买商品次数
     * <功能详细描述>
     * @param id 商品id
     * @return
     * @throws Throwable
     */
    @Override
    public Integer queryBuyGoodsNum(int id)
        throws Throwable
    {
		return null;}
    
    /**
     * 查询用户购买商品次数（即成交次数，审核不通过也算是成交了）
     * <功能详细描述>
     * @param id 商品id
     * @return
     * @throws Throwable
     */
    @Override
    public Integer queryBuyGoodsTimes(int id)
        throws Throwable
    {
		return null;}
    
    private Integer queryBuyGoodsNum(Connection connection, int id)
        throws Throwable
    {
		return null;}
    
    private T6351 queryGoodsByIdForUpdate(Connection connection, int id)
        throws Throwable
    {
		return null;}
    
    public static void main(String[] args)
    {
        String no = "H1511111114";
        String s =
            DateParser.format(new Date(), "yyMMdd")
                + String.format("%05d", Integer.parseInt(no.substring(no.length() - 5)) + 1);
        System.out.println(s);
    }
    
    @Override
    public int queryCarNum()
        throws Throwable
    {
		return 0;}
    
    @Override
    public List<ShoppingCarResult> queryOrderGoods(List<OrderGoods> orders, String type)
        throws Throwable
    {
		return null;}
    
    @Override
    public UserAccount queryAccount()
        throws Throwable
    {
		return null;}
    
    @Override
    public String selectTranPwd()
        throws Throwable
    {
		return null;}
    
    private String isYuqi(int userId, Connection connection)
        throws Throwable
    {
		return null;}
    
    private GrBaseInfo getGrBaseInfo(Connection connection, int F01)
        throws SQLException
    {
		return null;}
    
    /**
     * <判断购物车是否存在>
     * <功能详细描述>
     * @param connection
     * @param F01
     * @return
     * @throws SQLException
     */
    private boolean isExistCart(Connection connection, int F02, int F03)
        throws Throwable
    {
		return false;}
    
    private int getCountAddress(Connection connection, int accountId)
        throws Throwable
    {
		return accountId;}
    
    private boolean isSetBaseDataed(GrBaseInfo grBaseInfo)
    {
		return false;}
}
