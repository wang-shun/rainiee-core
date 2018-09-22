/*
 * 文 件 名:  ShoppingCar.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  yuanguangjie
 * 修改时间:  2016年2月25日
 */
package com.dimeng.p2p.app.servlets.user.mall;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.app.servlets.user.domain.mall.ShoppingCarInfo;
import com.dimeng.p2p.repeater.score.MallChangeManage;
import com.dimeng.p2p.repeater.score.entity.ShoppingCarResult;
import com.dimeng.p2p.repeater.score.entity.UserAccount;
import com.dimeng.p2p.variables.defines.MallVariavle;
import com.dimeng.util.parser.BooleanParser;

/**
 * 积分商城-购物车
 * 
 * @author  yuanguangjie
 * @version  [版本号, 2016年2月25日]
 */
public class ShoppingCar extends AbstractSecureServlet
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 647069847939919002L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
    	logger.info("|ShoppingCar-in|");
        
        MallChangeManage manage = serviceSession.getService(MallChangeManage.class);
        List<ShoppingCarInfo> scoreMallList = new ArrayList<ShoppingCarInfo>();
        
        //查询当前用户的购物车
        ShoppingCarResult[] shoppingCar =  manage.queryCar();
        ShoppingCarInfo sInfo = null;
        
        if (shoppingCar != null && shoppingCar.length > 0 )
        {
        	for (ShoppingCarResult car : shoppingCar)
        	{
        		sInfo = new ShoppingCarInfo();
        		//购物车Id
        		sInfo.setCarId(car.id);
        		
        		//商品Id
        		sInfo.setId(car.goodsId);
        		
        		//商品名称
        		sInfo.setName(car.goodsName);
        		
        		//商品图片
        		sInfo.setImage(car.goodsImg);
        		
        		//兑换积分
        		sInfo.setScore(car.score);
        		
        		//购买金额
        		sInfo.setAmount(car.amount);
        		
        		//商品库存数量
        		sInfo.setStock(car.goodsCount);
        		
        		//购买数量
        		sInfo.setNum(car.num);
        		
        		//是否支持金额购买
        		sInfo.setIsCanMoney(car.isBuyCash);
        		
        		//是否支持积分购买
        		sInfo.setIsCanScore(car.isBuyScore);
        		
        		//单用户限购数量
        		sInfo.setPurchase(car.singleCount);
        		
        		//已购买数量
        		sInfo.setYgCount(car.ygCount);
        		
        		scoreMallList.add(sInfo);
        	}
        }
        
        //查询用户的积分和可用余额
        UserAccount user = manage.queryAccount();
        //用户可用积分
        final int userScore = user.totalScore;
        //用户可用余额
        BigDecimal userBalance = user.balance;
        //获取平台产量：是否支持余额购买
        final ConfigureProvider configureProvider = getConfigureProvider();
        final boolean allowsBalance = BooleanParser.parse(configureProvider.getProperty(MallVariavle.ALLOWS_THE_BALANCE_TO_BUY));
        
        // 封装返回给页面信息
        Map<String, Object> scoreMap = new HashMap<String, Object>();
        scoreMap.put("scoreMallList", scoreMallList);
        scoreMap.put("userScore", userScore);
        scoreMap.put("userBalance", userBalance);
        scoreMap.put("allowsBalance", allowsBalance);
        //购物车商品列表
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success！", scoreMap);
        return;
    }
    
    @Override
    protected void processGet(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
}
