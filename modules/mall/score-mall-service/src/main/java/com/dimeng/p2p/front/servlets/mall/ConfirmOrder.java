package com.dimeng.p2p.front.servlets.mall;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import com.dimeng.framework.http.session.authentication.AuthenticationException;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.repeater.score.MallChangeManage;
import com.dimeng.p2p.repeater.score.entity.OrderGoods;
import com.dimeng.p2p.repeater.score.entity.ShoppingCarResult;

/**
 * 
 * 确认订单
 * <功能详细描述>
 * 
 * @author  heluzhu
 * @version  [版本号, 2015年12月18日]
 */
public class ConfirmOrder extends AbstractMallServlet
{
    private static final long serialVersionUID = -1332501050421123427L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        
        processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {/*
        
        MallChangeManage manage = serviceSession.getService(MallChangeManage.class);
        
        String type = request.getParameter("payType");
        String jsonStr = request.getParameter("goodsList");
        List<OrderGoods> orders = null;
        try
        {
            JSONArray array = JSONArray.fromObject(jsonStr);
            orders = (List<OrderGoods>)array.toCollection(array, OrderGoods.class);
        }
        catch (Exception e)
        {
            getController().prompt(request, response, PromptLevel.WARRING, "参数错误");
            String fromUrl = request.getParameter("fromUrl");
            sendRedirect(request, response, fromUrl + "?id=" + request.getParameter("commodityId"));
            return;
        }
        
        List<ShoppingCarResult> list = manage.queryOrderGoods(orders, type);
        int totalNum = 0;
        BigDecimal totalAmount = new BigDecimal(0);
        int totalScore = 0;
        for (ShoppingCarResult result : list)
        {
            if (result.num <= 0)
            {
                throw new ParameterException("商品购买数量必须大于0");
            }
            totalNum += result.num;
            totalScore += result.score * result.num;
            totalAmount = totalAmount.add(result.amount.multiply(new BigDecimal(result.num)));
        }
        request.setAttribute("list", list);
        request.setAttribute("type", type);
        request.setAttribute("goodsList", jsonStr);
        request.setAttribute("totalNum", totalNum);
        request.setAttribute("totalAmount", totalAmount);
        request.setAttribute("totalScore", totalScore);
        request.setAttribute("source", request.getParameter("source"));
        forwardView(request, response, getClass());
    */}
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        getResourceProvider().log(throwable);
        if (throwable instanceof ParameterException || throwable instanceof AuthenticationException)
        {
            getController().prompt(request, response, PromptLevel.WARRING, throwable.getMessage());
            forwardController(request, response, CarList.class);
        }
        else if (throwable instanceof LogicalException)
        {
            getController().prompt(request, response, PromptLevel.WARRING, throwable.getMessage());
            forwardController(request, response, CarList.class);
        }
        else
        {
            super.onThrowable(request, response, throwable);
            forwardController(request, response, CarList.class);
        }
    }
}
