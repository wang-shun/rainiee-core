/*
 * 文 件 名:  MyOrderInfo.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2016年2月24日
 */
package com.dimeng.p2p.app.servlets.user.mall;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.upload.FileStore;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S63.enums.T6350_F07;
import com.dimeng.p2p.S63.enums.T6352_F06;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.app.servlets.user.domain.mall.MyOrderExtInfo;
import com.dimeng.p2p.app.servlets.user.domain.mall.MyOrderInfo;
import com.dimeng.p2p.app.servlets.user.domain.mall.MyOrderItems;
import com.dimeng.p2p.repeater.score.UserCenterScoreManage;
import com.dimeng.p2p.repeater.score.entity.MyOrderInfoExt;
import com.dimeng.util.StringHelper;

/**
 * 订单详情
 * 
 * @author  zhoulantao
 * @version  [版本号, 2016年2月24日]
 */
public class MyOrderItem extends AbstractSecureServlet
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -459880322339796800L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        // 获取订单编号
        final String orderId = getParameter(request, "orderId");
        
        // 根据订单编号查询订单详情
        UserCenterScoreManage userCenterScoreManage = serviceSession.getService(UserCenterScoreManage.class);
        PagingResult<MyOrderInfoExt> myOrderInfo = userCenterScoreManage.queryMyOrderById(orderId, getPaging(1, 0));
        
        MyOrderItems myOrderItem = null;
        Map<String, List<MyOrderInfoExt>> map = null;
        if (null != myOrderInfo)
        {
            MyOrderInfoExt[] myOrderInfoExts = myOrderInfo.getItems();
            
            if (null != myOrderInfoExts && myOrderInfoExts.length > 0)
            {
                myOrderItem = new MyOrderItems();
                
                // 商品为实物
                if (myOrderInfoExts[0].category == T6350_F07.virtual)
                {
                    // 收货人联系方式
                    myOrderItem.setTelephone(myOrderInfoExts[0].phoneNum);
                }
                else
                {
                    // 收货人姓名
                    myOrderItem.setBuyer(StringHelper.isEmpty(myOrderInfoExts[0].F13) ? "" : myOrderInfoExts[0].F13);
                    
                    // 收货人联系方式
                    myOrderItem.setTelephone(myOrderInfoExts[0].F16);
                    
                    // 收货人地址信息
                    String region = StringHelper.isEmpty(myOrderInfoExts[0].region) ? "" : myOrderInfoExts[0].region;
                    myOrderItem.setAddress(region.concat(StringHelper.isEmpty(myOrderInfoExts[0].F15) ? ""
                        : myOrderInfoExts[0].F15));
                }
                
                // 根据订单号将订单分类
                List<MyOrderInfo> myOrderInfos = null;
                map = new LinkedHashMap<String, List<MyOrderInfoExt>>();
                for (MyOrderInfoExt ext : myOrderInfoExts)
                {
                    // 物流单号为空，还未发货
                    if (StringHelper.isEmpty(ext.F12))
                    {
                        if (map.get("a") == null)
                        {
                            List<MyOrderInfoExt> tempList = new ArrayList<MyOrderInfoExt>();
                            tempList.add(ext);
                            map.put("a", tempList);
                        }
                        else
                        {
                            map.get("a").add(ext);
                        }
                    }
                    else
                    {
                        if (map.get(ext.F12) == null)
                        {
                            List<MyOrderInfoExt> tempList = new ArrayList<MyOrderInfoExt>();
                            tempList.add(ext);
                            map.put(ext.F12, tempList);
                        }
                        else
                        {
                            map.get(ext.F12).add(ext);
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
                    
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    for (String key : keySet)
                    {
                        MyOrderInfo orderInfo = new MyOrderInfo();
                        List<MyOrderExtInfo> myOrderExtInfos = null;
                        List<MyOrderInfoExt> value = map.get(key);
                        
                        int count = 0;
                        BigDecimal amount = new BigDecimal(0);
                        if (value != null && value.size() > 0)
                        {
                            myOrderExtInfos = new ArrayList<MyOrderExtInfo>();
                            
                            MyOrderInfoExt infoExt = value.get(0);
                            
                            // 订单号
                            orderInfo.setOrderId(infoExt.orderNum);
                            
                            // 订单时间
                            orderInfo.setOrderTime(sdf.format(infoExt.orderTime));
                            
                            orderInfo.setDispatchDate(null == infoExt.F10 ? "" : sdf.format(infoExt.F10));
                            // 如果物流信息不为空
                            if (!"a".equals(key))
                            {
                                // 发货时间
//                                orderInfo.setDispatchDate(sdf.format(infoExt.F10));
                                
                                // 商品为实物
                                if (infoExt.category == T6350_F07.kind)
                                {
                                    // 物流方
                                    orderInfo.setLogistics(infoExt.F11);
                                    
                                    // 物流订单号
                                    orderInfo.setLogisticsOrderNum(infoExt.F12);
                                }
                            }
//                            else if ("a".equals(key) && infoExt.category == T6350_F07.virtual)
//                            {
//                                // 发货时间
//                                orderInfo.setDispatchDate(null == infoExt.F10 ? "" : sdf.format(infoExt.F10));
//                            }
                            
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
                                
                                // 商品图片
                                extInfo.setComdPicture(StringHelper.isEmpty(ext.appComdPicture) ? ""
                                    : fileStore.getURL(ext.appComdPicture));
                                
                                // 支付方式
                                extInfo.setPayment(ext.payment.name());
                                
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
                            orderInfo.setPayment(infoExt.payment.name());
                        }
                        
                        // 订单下商品信息
                        orderInfo.setMyOrderExtInfos(myOrderExtInfos);
                        myOrderInfos.add(orderInfo);
                    }
                }
                
                // 订单详情
                myOrderItem.setMyOrderInfos(myOrderInfos);
            }
        }
        
        // 返回我的订单详情
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success！", myOrderItem);
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
