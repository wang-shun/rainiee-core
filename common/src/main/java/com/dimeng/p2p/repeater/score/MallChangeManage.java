package com.dimeng.p2p.repeater.score;
import java.util.List;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.S63.entities.T6355;
import com.dimeng.p2p.repeater.score.entity.AddressResult;
import com.dimeng.p2p.repeater.score.entity.OrderGoods;
import com.dimeng.p2p.repeater.score.entity.ShoppingCarResult;
import com.dimeng.p2p.repeater.score.entity.UserAccount;

/**
 * 
 * 商品兑换
 * <功能详细描述>
 * 
 * @author  heluzhu
 * @version  [版本号, 2015年12月14日]
 */
public abstract interface MallChangeManage extends Service
{
    /**
     * 增加收货地址
     * <功能详细描述>
     * @param t6355
     * @throws Throwable
     */
    public void editAddress(T6355 t6355) throws Throwable;
    
    /**
     * 删除收货地址
     * <功能详细描述>
     * @param id
     * @throws Throwable
     */
    public void delAddress(int id) throws Throwable;
    
    /**
     * 根据ID查询收货信息
     * <功能详细描述>
     * @param id
     * @return
     * @throws Throwable
     */
    public AddressResult queryById(int id) throws Throwable;
    
    /**
     * 查询用户的收货地址
     * <功能详细描述>
     * @return
     * @throws Throwable
     */
    public AddressResult[] queryByUser() throws Throwable;
    
    /**
     * 查询当前用户的购物车
     * <功能详细描述>
     * @return
     * @throws Throwable
     */
    public ShoppingCarResult[] queryCar() throws Throwable;
    /**
     * 编辑购物车信息
     * <功能详细描述>
     * @param t6358
     * @throws Throwable
     */
    public void editCar(int id,int num) throws Throwable;
    /**
     * 删除购物车商品
     * <功能详细描述>
     * @param ids
     * @throws Throwable
     */
    public void delCar(List<Integer> ids) throws Throwable;
    
    /**
     * 余额购买
     * <功能详细描述>
     * @throws Throwable
     */
    public int toChangeByBalance(List<OrderGoods> goodsList,String type,int addressId) throws Throwable;
    
    /**
     * 积分兑换
     * <功能详细描述>
     * @throws Throwable
     */
    public void toChangeByScore(List<OrderGoods> goodsList,String type,int addressId) throws Throwable;
    
    /**
     * 查询购物车商品数量
     * <功能详细描述>
     * @return
     * @throws Throwable
     */
    public int queryCarNum() throws Throwable;
    
    /**
     * 查询订单中的商品信息
     * <功能详细描述>
     * @param orders
     * @return
     * @throws Throwable
     */
    public List<ShoppingCarResult> queryOrderGoods(List<OrderGoods> orders,String type) throws Throwable;
    
    /**
     * 查询用户的积分和可用余额
     * <功能详细描述>
     * @return
     * @throws Throwable
     */
    public UserAccount queryAccount() throws Throwable;
    
    /**
     * 查询交易密码
     * <功能详细描述>
     * @return
     * @throws Throwable
     */
    public String selectTranPwd() throws Throwable;
    
    /**
     * 查询用户购买商品数量
     * <功能详细描述>
     * @param id 商品id
     * @return
     * @throws Throwable
     */
    public Integer queryBuyGoodsNum(int id)
        throws Throwable;
    
    /**
     * 查询用户购买商品次数（即成交次数，审核不通过也算是成交了）
     * <功能详细描述>
     * @param id 商品id
     * @return
     * @throws Throwable
     */
    public Integer queryBuyGoodsTimes(int id)
        throws Throwable;
}
