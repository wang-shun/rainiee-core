/*
 * 文 件 名:  CommondityDetails.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  yuanguangjie
 * 修改时间:  2016年2月23日
 */
package com.dimeng.p2p.app.servlets.user.mall;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.session.Session;
import com.dimeng.framework.http.session.SessionManager;
import com.dimeng.framework.http.upload.FileStore;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.app.servlets.AbstractAppServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.app.servlets.user.domain.mall.ScoreMall;
import com.dimeng.p2p.repeater.score.MallChangeManage;
import com.dimeng.p2p.repeater.score.ScoreCommodityManage;
import com.dimeng.p2p.repeater.score.entity.T6351Ext;
import com.dimeng.p2p.repeater.score.entity.UserAccount;
import com.dimeng.p2p.variables.defines.MallVariavle;
import com.dimeng.util.parser.BooleanParser;

/**
 * 平台商品详情
 * 
 * @author  yuanguangjie
 * @version  [版本号, 2016年2月23日]
 */
public class CommodityDetails extends AbstractAppServlet
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
       	ScoreCommodityManage scoreCommodityManage = serviceSession.getService(ScoreCommodityManage.class);
       	MallChangeManage manage = serviceSession.getService(MallChangeManage.class);
       	
    	//平台商品ID
        final int commId = Integer.parseInt(getParameter(request, "id"));
        
        //根据商品id查询指定商品对象
        T6351Ext commodityDetails = scoreCommodityManage.getCommodityObject(commId);
        
        ScoreMall scoreMall = new ScoreMall();
        
        if (null != commodityDetails)
        {
        	//商品Id
        	scoreMall.setId(commodityDetails.F01);
        	
        	//商品名称
        	scoreMall.setName(commodityDetails.F03);
        	
        	if( commodityDetails.F08 != null && !commodityDetails.F08.equals(""))
        	{
        		//APP商品图片
        		FileStore fileStore = getResourceProvider().getResource(FileStore.class);
        		scoreMall.setImage(fileStore.getURL(commodityDetails.F08));
        	}
        	
        	//商品类型
        	scoreMall.setType(commodityDetails.commTypeName);
        	
        	//兑换商品所需积分
        	scoreMall.setScore(commodityDetails.F05);
        	
        	//购买商品所需价格
        	scoreMall.setAmount(commodityDetails.F15);
        	
        	//商品成交笔数
        	final int dealNum = manage.queryBuyGoodsTimes(commId);
        	scoreMall.setDealNum(dealNum);
        	
        	//商品库存
        	scoreMall.setStock(commodityDetails.F06);
        	
        	//每个用户限购数量
        	scoreMall.setPurchase(commodityDetails.F18);
        	
        	//商品类别|用于判断实物还是话费
        	scoreMall.setCommTypeEnum(commodityDetails.commTypeEnum);
        	
        	//是否可以积分兑换
        	scoreMall.setIsCanScore(commodityDetails.F16);
        	
        	//是否可以余额兑换
        	scoreMall.setIsCanMoney(commodityDetails.F17);
        	
        	// 市场价格
        	scoreMall.setMarketPrice(commodityDetails.F19);
        }
        
        //获取平台产量：是否支持余额购买
        final ConfigureProvider configureProvider = getConfigureProvider();
        boolean allowsBalance = BooleanParser.parse(configureProvider.getProperty(MallVariavle.ALLOWS_THE_BALANCE_TO_BUY));
        
        // 封装返回给页面信息
        Map<String, Object> scoreMap = new HashMap<String, Object>();
        
        // 读取资源文件
        ResourceProvider resourceProvider = getResourceProvider();
        
        // 读取session信息
        final Session session = resourceProvider.getResource(SessionManager.class).getSession(request, response);
        
        // 判断session中登录标识 |登录之后可查看用户可用积分、余额
        if (session != null && session.isAuthenticated())
        {
        	//查询用户的积分和可用余额
        	UserAccount user = manage.queryAccount();
        	//用户可用积分
        	final int userScore = user.totalScore;
        	//用户可用余额
        	final BigDecimal userBalance = user.balance;
        	//获取用户购物车中商品个数
        	final int num = manage.queryCarNum();
        	
        	//查询用户购买商品次数
        	final int ygCount = manage.queryBuyGoodsTimes(session.getAccountId());
        	
        	
        	scoreMap.put("num", num);
        	scoreMap.put("ygCount", ygCount);
        	scoreMap.put("userScore", userScore);
        	scoreMap.put("userBalance", userBalance);
        }
        
        scoreMap.put("scoreMall", scoreMall);
        //是否支持余额购买
        scoreMap.put("allowsBalance", allowsBalance);
        
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
