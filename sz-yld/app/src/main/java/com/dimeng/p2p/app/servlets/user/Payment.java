package com.dimeng.p2p.app.servlets.user;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.modules.bid.user.service.TenderRepaymentManage;
import com.dimeng.p2p.order.TenderRepaymentExecutor;
import com.dimeng.util.parser.IntegerParser;

/**
 * 手工还款
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年12月14日]
 */
public class Payment extends AbstractSecureServlet
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -9043714174462748459L;
    
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
        //Boolean tg = BooleanParser.parseObject(getConfigureProvider().getProperty(SystemVariable.SFZJTG));
        
        // 查询订单号
        TenderRepaymentManage manage = serviceSession.getService(TenderRepaymentManage.class);
        int[] orderIds = manage.repayment(loanId, currentTerm);
        try
        {
            TenderRepaymentExecutor executor = getResourceProvider().getResource(TenderRepaymentExecutor.class);
            if (orderIds != null && orderIds.length > 0)
            {
                Map<String, String> map = new HashMap<String, String>();
                int index = 1;
                for (int orderId : orderIds)
                {
                    if (index == orderIds.length)
                    {
                        map.put("isLast", "true");
                    }
                    else
                    {
                        map.put("isLast", "false");
                    }
                    // 提交还款订单
                    executor.submit(orderId, null);
                    
                    // 非托管模式下，确认订单
                    executor.confirm(orderId, map);
                    index++;
                }
            }
        }
        catch (Throwable e)
        {
            // 还款出现异常时，将还款计划更新为未还
            manage.updateT6252(loanId, currentTerm);
            throw e;
        }
        
        // 封装页面信息
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success!");
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