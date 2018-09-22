/*
 * 文 件 名:  ScoreMallList.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  yuanguangjie
 * 修改时间:  2016年2月23日
 */
package com.dimeng.p2p.app.servlets.user.mall;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.upload.FileStore;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S63.entities.T6350;
import com.dimeng.p2p.S63.entities.T6351;
import com.dimeng.p2p.S63.entities.T6353;
import com.dimeng.p2p.S63.enums.T6350_F04;
import com.dimeng.p2p.S63.enums.T6353_F05;
import com.dimeng.p2p.app.servlets.AbstractAppServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.app.servlets.user.domain.mall.ScoreAmountRange;
import com.dimeng.p2p.app.servlets.user.domain.mall.ScoreMall;
import com.dimeng.p2p.app.servlets.user.domain.mall.ScoreMallType;
import com.dimeng.p2p.app.servlets.user.domain.mall.ScoreRange;
import com.dimeng.p2p.repeater.score.CommodityCategoryManage;
import com.dimeng.p2p.repeater.score.MallIndexManage;
import com.dimeng.p2p.repeater.score.SetScoreManage;
import com.dimeng.p2p.repeater.score.entity.SearchGoodsCategory;
import com.dimeng.p2p.variables.defines.MallVariavle;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BooleanParser;
import com.dimeng.util.parser.IntegerParser;

/**
 * 积分商城产品列表
 * 
 * @author  yuanguangjie
 * @version  [版本号, 2016年2月23日]
 */
public class ScoreMallList extends AbstractAppServlet
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
        logger.info("|ScoreMallList-in|");
        
        /** 获取分页信息 */
        final int pageIndex = IntegerParser.parse(getParameter(request, "pageIndex"));
        final int pageSize = IntegerParser.parse(getParameter(request, "pageSize"));
        final Paging pageing = getPaging(pageIndex, pageSize);
        
        CommodityCategoryManage cmanage = serviceSession.getService(CommodityCategoryManage.class);
        SetScoreManage smanage = serviceSession.getService(SetScoreManage.class);
        MallIndexManage manage = serviceSession.getService(MallIndexManage.class);
        
        /** 查询条件：启用的商品类别 */
        List<T6350> typeList = cmanage.getT6350List(T6350_F04.on.name());
        List<ScoreMallType> mallTypeList = new ArrayList<ScoreMallType>();
        ScoreMallType scoreMallType = null;
        if (typeList != null && typeList.size() > 0)
        {
            for (T6350 t6350 : typeList)
            {
                scoreMallType = new ScoreMallType();
                //类别Id
                scoreMallType.setId(t6350.F01);
                //类别名称
                scoreMallType.setName(t6350.F03);
                
                mallTypeList.add(scoreMallType);
            }
        }
        
        /** 查询条件：积分范围 */
        List<T6353> scoreList = smanage.getT6353List(T6353_F05.score.name());
        List<ScoreRange> scoreRangeList = new ArrayList<ScoreRange>();
        ScoreRange scoreRange = null;
        if (scoreList != null && scoreList.size() > 0)
        {
            for (T6353 t6353 : scoreList)
            {
                scoreRange = new ScoreRange();
                //积分范围Id
                scoreRange.setId(t6353.F01);
                //积分最小值
                scoreRange.setMinScore(t6353.F02);
                //积分最大值
                scoreRange.setMaxScore(t6353.F03);
                
                scoreRangeList.add(scoreRange);
            }
        }
        
        /** 查询条件：金额范围 */
        List<T6353> amountList = smanage.getT6353List(T6353_F05.amount.name());
        List<ScoreAmountRange> amountRangeList = new ArrayList<ScoreAmountRange>();
        ScoreAmountRange amount = null;
        if (amountList != null && amountList.size() > 0)
        {
            for (T6353 t6353 : amountList)
            {
                amount = new ScoreAmountRange();
                //金额范围Id
                amount.setId(t6353.F01);
                //金额最小值
                amount.setMinScore(t6353.F02);
                //金额最大值
                amount.setMaxScore(t6353.F03);
                
                amountRangeList.add(amount);
            }
        }
        
        /** 获取平台商品 */
        SearchGoodsCategory searchGoodsCategory = new SearchGoodsCategory();
        
        // 查询条件-商品类别
        searchGoodsCategory.goodsCategory = getParameter(request, "goodsCategory");
        // 查询条件-积分范围
        searchGoodsCategory.scoreRange = getParameter(request, "scoreRange");
        // 查询条件-金额范围
        searchGoodsCategory.amountRange = getParameter(request, "amountRange");
        // 查询条件-排序条件
        searchGoodsCategory.sortWay = getParameter(request, "sortWay");
        // 查询条件-排序方式
        searchGoodsCategory.orderBy = getParameter(request, "orderBy");
        
        // 查询商品并分页
        PagingResult<T6351> t6351List = manage.getT6351List(searchGoodsCategory, pageing);
        
        // 封装返回给页面信息
        Map<String, Object> scoreMap = new HashMap<String, Object>();
        
        List<ScoreMall> scoreMallList = new ArrayList<ScoreMall>();
        // 判断是否有商品
        if (t6351List != null)
        {
            // 商品总条数，用于判断数据是否加载完。
            int itemCount = t6351List.getItemCount();
            
            // 商品总条数-用于判断数据是否加载完
            scoreMap.put("itemCount", itemCount);
            
            FileStore fileStore = getResourceProvider().getResource(FileStore.class);
            T6351[] mallList = t6351List.getItems();
            ScoreMall score = null;
            if (mallList != null && mallList.length > 0)
            {
                for (T6351 t6351 : mallList)
                {
                    score = new ScoreMall();
                    // 商品Id
                    score.setId(t6351.F01);
                    
                    // 商品名称
                    score.setName(t6351.F03);
                    
                    // 商品图片
                    score.setImage(StringHelper.isEmpty(t6351.F09) ? "" : fileStore.getURL(t6351.F09));
                    
                    // 商品积分
                    score.setScore(t6351.F05);
                    
                    // 商品价格
                    score.setAmount(t6351.F15);
                    
                    // 商品成交笔数
                    score.setDealNum(t6351.F07);
                    
                    // 是否支持积分购买
                    score.setIsCanScore(t6351.F16);
                    
                    // 是否支持余额购买
                    score.setIsCanMoney(t6351.F17);
                    
                    // 市场价格
                    score.setMarketPrice(t6351.F19);
                    
                    scoreMallList.add(score);
                }
            }
        }
        
        // 获取平台产量：是否支持余额购买
        final ConfigureProvider configureProvider = getConfigureProvider();
        boolean allowsBalance =
            BooleanParser.parse(configureProvider.getProperty(MallVariavle.ALLOWS_THE_BALANCE_TO_BUY));
        
        // 查询条件-商品类别
        scoreMap.put("mallTypeList", mallTypeList);
        // 查询条件-积分范围
        scoreMap.put("scoreRangeList", scoreRangeList);
        // 查询条件-金额范围
        scoreMap.put("amountRangeList", amountRangeList);
        // 商品列表
        scoreMap.put("scoreMallList", scoreMallList);
        // 是否支持余额购买
        scoreMap.put("allowsBalance", allowsBalance);
        
        // 发送数据
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
