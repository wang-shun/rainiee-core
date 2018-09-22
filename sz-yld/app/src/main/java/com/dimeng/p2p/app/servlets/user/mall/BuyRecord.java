/*
 * 文 件 名:  BuyRecord.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  yuanguangjie
 * 修改时间:  2016年2月23日
 */
package com.dimeng.p2p.app.servlets.user.mall;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.app.servlets.AbstractAppServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.app.servlets.user.domain.mall.PurchaseRecord;
import com.dimeng.p2p.repeater.score.ScoreCommodityManage;
import com.dimeng.p2p.repeater.score.entity.ScoreOrderInfoExt;
import com.dimeng.p2p.repeater.score.entity.T6351Ext;
import com.dimeng.util.parser.IntegerParser;

/**
 * 平台商品详情-购买记录
 * 
 * @author  yuanguangjie
 * @version  [版本号, 2016年2月25日]
 */
public class BuyRecord extends AbstractAppServlet
{
    private static final long serialVersionUID = 647069847939919002L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        ScoreCommodityManage scoreCommodityManage = serviceSession.getService(ScoreCommodityManage.class);
        
        //显示类型：购买记录；商品详情
        final String type = getParameter(request, "type");
        
        //商品Id
        final int commId = IntegerParser.parse(getParameter(request, "id"));
        
        //封装返回给页面信息
        Map<String, Object> commodityMap = new HashMap<String, Object>();
        //购买记录
        if ("gmjl".equals(type))
        {
            // 获取购买记录分页信息
            final int pageIndex = IntegerParser.parse(getParameter(request, "pageIndex"));
            final int pageSize = IntegerParser.parse(getParameter(request, "pageSize"));
            final Paging pageing = getPaging(pageIndex, pageSize);
            
            List<PurchaseRecord> purchaseList = new ArrayList<PurchaseRecord>();
            //根据商品id查询指定商品的购买记录列表
            PagingResult<ScoreOrderInfoExt> list = scoreCommodityManage.getCommodityOrderList(commId, pageing);
            
            if (null != list)
            {
                // 购买总记录数
                final int itemCount = list.getItemCount();
                
                if (itemCount > 0)
                {
                    ScoreOrderInfoExt[] ScoreOrderList = list.getItems();
                    PurchaseRecord purchaseRecord = null;
                    for (ScoreOrderInfoExt bugGoodsList : ScoreOrderList)
                    {
                        purchaseRecord = new PurchaseRecord();
                        // 用户名
                        String name =
                            bugGoodsList.loginName.substring(0, 2)
                                + "******"
                                + bugGoodsList.loginName.substring(bugGoodsList.loginName.length() - 2,
                                    bugGoodsList.loginName.length());
                        purchaseRecord.setLoginName(name);
                        
                        // 数量
                        purchaseRecord.setComdNum(bugGoodsList.comdNum);
                        
                        // 支付方式 ： 积分|余额
                        purchaseRecord.setPayment(bugGoodsList.payment);
                        
                        // 积分价值
                        purchaseRecord.setComdScore(bugGoodsList.comdScore);
                        
                        // 余额价值
                        purchaseRecord.setComdPrices(bugGoodsList.comdPrices);
                        
                        // 订单时间
                        SimpleDateFormat sfm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        purchaseRecord.setOrderTime(sfm.format(bugGoodsList.orderTime));
                        
                        purchaseList.add(purchaseRecord);
                    }
                }
                
                // 购买总记录数
                commodityMap.put("itemCount", itemCount);
                // 购买记录列表
                commodityMap.put("purchaseList", purchaseList);
            }
        }
        else if ("spxq".equals(type))
        {
            //根据商品id查询指定商品对象
            T6351Ext commodityDetails = scoreCommodityManage.getCommodityObject(commId);
            
            //商品详情
            commodityMap.put("commodityItem", getImgContent(commodityDetails.F10));
        }
        
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success！", commodityMap);
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
