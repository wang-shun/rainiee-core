/*package com.dimeng.p2p.app.servlets.pay.service.fuyou;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.common.enums.FrontLogType;
import com.dimeng.p2p.escrow.fuyou.executor.FYPrepaymentExecutor;
import com.dimeng.p2p.escrow.fuyou.service.PrepaymentManage;
import com.dimeng.p2p.variables.defines.URLVariable;
import com.dimeng.util.parser.IntegerParser;

*//**
 * 
 * 提前还款
 * <富友托管>
 * 
 * @author  suwei
 * @version  [版本号, 2016年03月02日]
 *//*
public class FyouPrepayment extends AbstractFuyouServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        logger.info("APP富友托管-提前还款-开始——IP:" + request.getRemoteAddr());
        int id = IntegerParser.parse(request.getParameter("id"));
        int number = IntegerParser.parse(request.getParameter("number"));
        // TenderPrepaymentManageImpl
        PrepaymentManage manage = serviceSession.getService(PrepaymentManage.class);
        Map<String, String> params = new HashMap<String, String>();
        // 提前还款<本期利息+1，本金，提前还款违约金，提前还款手续费>
        int[] orderIds = manage.prepayment(id, number, params);
        String orderIdsStr = "";
        for (int i = 0; i < orderIds.length; i++)
        {
            if (i == orderIds.length - 1)
            {
                orderIdsStr = orderIdsStr + orderIds[i];
            }
            else
            {
                orderIdsStr = orderIdsStr + orderIds[i] + ",";
            }
        }
        params.put("orderIdsStr", orderIdsStr);
        try
        {
            FYPrepaymentExecutor executor = getResourceProvider().getResource(FYPrepaymentExecutor.class);
            if (orderIds != null && orderIds.length > 0)
            {
                params.put("hint", "succeed");
                logger.info("标ID：" + id + "-提前还款总数：" + orderIds.length);
                int i = 0;
                boolean flag = true;
                for (int orderId : orderIds)
                {
                    i++;
                    logger.info("第" + i + "条提前还款");
                    if ("false".equals(params.get("flag")))
                    {
                        manage.selectT6501(orderId, serviceSession, params);
                        flag = false;
                    }
                    if (flag)
                    {
                        executor.submit(orderId, params);
                        // 如果转账失败，则执行下一个
                        if (!"true".equals(params.get("success")))
                        {
                            logger.info("第" + i + "条提前还款-失败");
                            params.put("hint", "fail");
                            // 失败时将还款记录改回未还
                            //manage.updateT6252(id, number);
                            continue;
                        }
                        else
                        {
                            logger.info("第" + i + "条提前还款-成功");
                        }
                        executor.confirm(orderId, params);
                        logger.info("第" + i + "条提前还款-确认成功");
                    }
                    else
                    {
                        switch (params.get("state"))
                        {
                            case "DTJ":
                                executor.submit(orderId, params);
                                if (!"true".equals(params.get("success")))
                                {
                                    logger.info("第" + i + "条提前还款-失败");
                                    params.put("hint", "fail");
                                    // 失败时将还款记录改回未还
                                    manage.updateT6252(id, number);
                                    break;
                                }
                                logger.info("第" + i + "条提前还款-成功");
                                executor.confirm(orderId, params);
                                break;
                            case "DQR":
                                // 第三方成功，平台未更新
                                executor.confirm(orderId, params);
                                logger.info("第" + i + "条提前还款-确认成功");
                                break;
                            case "CG":
                                logger.info("第" + i + "条提前还款-已确认成功");
                                break;
                            default:
                                executor.confirm(orderId, params);
                                logger.info("第" + i + "条提前还款-确认成功");
                                break;
                        }
                    }
                }
                logger.info("标ID：" + id + "-提前还款-调用结束");
            }
        }
        catch (Throwable e)
        {
            // 失败时将还款记录改回未还
            manage.updateT6252(id, number);
            throw e;
        }
        request.setAttribute("id", id);
        request.setAttribute("number", number);
        request.setAttribute("orderIds", orderIds);
        
        if ("fail".equals(params.get("hint")))
        {
            setReturnMsg(request, response, ExceptionCode.ERROR, "提前还款失败！");
            manage.writeFrontLog(FrontLogType.TQHK.getName(), "提前还款失败-标Id:" + id);
            return;
        }
        else
        {
            setReturnMsg(request, response, ExceptionCode.ERROR, "恭喜您，提前还款成功！");
            manage.writeFrontLog(FrontLogType.TQHK.getName(), "提前还款成功-标Id:" + id);
            return;
        }
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        getResourceProvider().log(throwable.getMessage());
        if (throwable instanceof ParameterException || throwable instanceof SQLException)
        {
            getController().prompt(request, response, PromptLevel.ERROR, "系统繁忙，请您稍后再试");
            sendRedirect(request, response, getConfigureProvider().format(URLVariable.PAY_PERPAYMENT_URL_SECOND));
        }
        else if (throwable instanceof LogicalException)
        {
            getController().prompt(request, response, PromptLevel.WARRING, throwable.getMessage());
            sendRedirect(request, response, getConfigureProvider().format(URLVariable.PAY_PERPAYMENT_URL_SECOND));
        }
        else
        {
            super.onThrowable(request, response, throwable);
        }
    }
}
*/