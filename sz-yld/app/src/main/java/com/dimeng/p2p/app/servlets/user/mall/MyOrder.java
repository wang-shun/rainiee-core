/*
 * 文 件 名:  MyOrders.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2016年1月29日
 */
package com.dimeng.p2p.app.servlets.user.mall;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.upload.FileStore;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S63.enums.T6350_F07;
import com.dimeng.p2p.S63.enums.T6352_F06;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.app.servlets.user.domain.mall.MyOrderExtInfo;
import com.dimeng.p2p.app.servlets.user.domain.mall.MyOrderInfo;
import com.dimeng.p2p.repeater.score.UserCenterScoreManage;
import com.dimeng.p2p.repeater.score.entity.MyOrderInfoExt;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.LongParser;

/**
 * 我的订单
 * 
 * @author  zhoulantao
 * @version  [版本号, 2016年1月29日]
 */
public class MyOrder extends AbstractSecureServlet
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -729835366239716303L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        // 获取页面参数
        final int pageIndex = IntegerParser.parse(getParameter(request, "pageIndex"));
        final int pageSize = IntegerParser.parse(getParameter(request, "pageSize"));
        
        UserCenterScoreManage userCenterScoreManage = serviceSession.getService(UserCenterScoreManage.class);
        // 分页参数
        Paging paging = getPaging(pageIndex, Integer.MAX_VALUE);
        PagingResult<MyOrderInfoExt> myOrderInfo = userCenterScoreManage.queryMyOrderList(paging);
        
        // 查询所有的订单信息
        List<MyOrderInfo> myOrderInfos = null;
        Map<String, List<MyOrderInfoExt>> map = null;
        if (null != myOrderInfo)
        {
            // 判断是否超出记录集
            if (myOrderInfo.getPageCount() < LongParser.parse(pageIndex))
            {
                myOrderInfos = new ArrayList<MyOrderInfo>();
                // 封装返回信息
                setReturnMsg(request, response, ExceptionCode.SUCCESS, "success!", myOrderInfos);
                return;
            }
            
            MyOrderInfoExt[] myOrderInfoExts = myOrderInfo.getItems();
            
            if (null != myOrderInfoExts && myOrderInfoExts.length > 0)
            {
                map = new LinkedHashMap<String, List<MyOrderInfoExt>>();
                
                for (MyOrderInfoExt ext : myOrderInfoExts)
                {
                    if (map.get(ext.orderNum) == null)
                    {
                        List<MyOrderInfoExt> tempList = new ArrayList<MyOrderInfoExt>();
                        tempList.add(ext);
                        map.put(ext.orderNum, tempList);
                    }
                    else
                    {
                        map.get(ext.orderNum).add(ext);
                    }
                }
            }
        }
        
        // 把同一个订单下的商品归纳到一个订单下
        Set<String> keySet = null;
        FileStore fileStore = getResourceProvider().getResource(FileStore.class);
        if (map != null && map.size() > 0)
        {
            myOrderInfos = new ArrayList<MyOrderInfo>();
            keySet = map.keySet();
            
            for (String key : keySet)
            {
                MyOrderInfo orderInfo = new MyOrderInfo();
                // 订单号
                orderInfo.setOrderId(key);
                
                List<MyOrderExtInfo> myOrderExtInfos = null;
                List<MyOrderInfoExt> value = map.get(key);
                
                int count = 0;
                BigDecimal amount = new BigDecimal(0);
                if (value != null && value.size() > 0)
                {
                    myOrderExtInfos = new ArrayList<MyOrderExtInfo>();
                    
                    for (MyOrderInfoExt ext : value)
                    {
                        MyOrderExtInfo extInfo = new MyOrderExtInfo();
                        
                        // 订单ID
                        extInfo.setOrderId(ext.F02);
                        
                        // 商品ID
                        extInfo.setProductId(ext.F03);
                        
                        // 订单状态   运用两个嵌套三目运算符做判断
                        extInfo.setStatus("nopass".equals(ext.F08.name())?"已取消":("pendding".equals(ext.F08.name())?"待确认":ext.F08.getChineseName()));
                        
                        // 商品类型
                        extInfo.setCategory(ext.category.name());
                        
                        // 商品名称
                        extInfo.setComdName(ext.comdName);
                        
                        // 支付方式
                        extInfo.setPayment(ext.payment.name());
                        
                        // 商品图片
                        extInfo.setComdPicture(StringHelper.isEmpty(ext.appComdPicture) ? "" : fileStore.getURL(ext.appComdPicture));
                        
                        // 如果商品类型为话费
                        if (ext.category == T6350_F07.virtual)
                        {
                            // 充值的手机号码
                            extInfo.setPhone(ext.F07);
                        }
                        
                        if (ext.payment.name().equals(T6352_F06.score.name()))
                        {
                            // 积分
                            extInfo.setScore(ext.F04);
                            
                            // 计算此订单下的商品积分总价值
                            amount = amount.add(new BigDecimal(ext.F04).multiply(new BigDecimal(ext.F06)));
                        }
                        else if (ext.payment.name().equals(T6352_F06.balance.name()))
                        {
                            // 购买价格
                            extInfo.setPrice(String.valueOf(ext.F05));
                            
                            // 计算此订单下的商品积分总价值
                            amount = amount.add(ext.F05.multiply(new BigDecimal(ext.F06)));
                        }
                        
                        // 数量
                        extInfo.setNum(ext.F06);
                        count += ext.F06;
                        
                        myOrderExtInfos.add(extInfo);
                    }
                    
                    // 商品总数量
                    orderInfo.setOrderNum(String.valueOf(count));
                    
                    // 商品总价值
                    orderInfo.setOrderSumAmount(String.valueOf(amount));
                    
                    // 支付方式
                    orderInfo.setPayment(value.get(0).payment.name());
                }
                
                // 订单下商品信息
                orderInfo.setMyOrderExtInfos(myOrderExtInfos);
                myOrderInfos.add(orderInfo);
            }
        }
        
        // 返回我的订单详情
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success！", myOrderInfos);
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
