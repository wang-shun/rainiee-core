/*
 * 文 件 名:  Prepayment.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2015年12月14日
*/ 
package com.dimeng.p2p.app.servlets.user;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.modules.bid.user.service.TenderPrepaymentManage;
import com.dimeng.p2p.order.TenderPrepaymentExecutor;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.parser.BooleanParser;
import com.dimeng.util.parser.IntegerParser;

/**
 * 提前还款
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年12月14日]
 */
public class Prepayment extends AbstractSecureServlet
{
    
	/**
     * 注释内容
     */
    private static final long serialVersionUID = 8967279058042949929L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        // 标ID
        int loanId = IntegerParser.parse(getParameter(request, "loanId"));
        
        // 当前期数
        int currentTerm = IntegerParser.parse(getParameter(request, "currentTerm"));
        
        // 判断是否托管
        Boolean tg = BooleanParser.parseObject(getConfigureProvider().getProperty(SystemVariable.SFZJTG));
        
        TenderPrepaymentManage manage = serviceSession.getService(TenderPrepaymentManage.class);
        if (tg)
        {
            String location =
                getSiteDomain("/user/prepaymentProxy.htm?loanId=" + loanId + "&currentTerm=" + currentTerm);
            Map<String, String> map = new HashMap<String, String>();
            map.put("url", location);
            
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.SUCCESS, "success!", map);
            return;
        }
        else
        {
            int[] orderIds = manage.prepayment(loanId, currentTerm);
            StringBuffer orderIdsStrs = new StringBuffer();
            if (null != orderIds && orderIds.length > 0)
            {
                final int idLen = orderIds.length;
                for (int i = 0; i < idLen; i++)
                {
                    orderIdsStrs.append(orderIds[i]);
                    orderIdsStrs.append(',');
                }
            }
            
            String orderIdsStr = orderIdsStrs.toString();
            if (orderIdsStr.lastIndexOf(",") != -1)
            {
                orderIdsStr = orderIdsStr.substring(0, orderIdsStr.length() - 1);
            }
            
            Map<String, String> params = new HashMap<>();
            params.put("orderIdsStr", orderIdsStr);
            try
            {
                TenderPrepaymentExecutor executor = getResourceProvider().getResource(TenderPrepaymentExecutor.class);
                if (orderIds != null && orderIds.length > 0)
                {
                    int index = 1;
                    for (int orderId : orderIds)
                    {
                        if (index == orderIds.length)
                        {
                            params.put("isLast", "true");
                        }
                        else
                        {
                            params.put("isLast", "false");
                        }
                        executor.submit(orderId, null);
                        if (!tg)
                        {
                            executor.confirm(orderId, params);
                        }
                        index++;
                    }
                }
            }
            catch (Throwable e)
            {
                // 记录失败信息
                logger.error("提前还款失败!", e);
                throw e;
            }
            
            // 封装页面信息
            setReturnMsg(request, response, ExceptionCode.SUCCESS, "success!");
            return;
        }
    }
    
    @Override
    protected void processGet(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
}