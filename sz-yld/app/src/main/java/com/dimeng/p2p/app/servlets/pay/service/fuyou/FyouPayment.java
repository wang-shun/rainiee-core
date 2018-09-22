/*package com.dimeng.p2p.app.servlets.pay.service.fuyou;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.common.enums.FrontLogType;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouTypeEnum;
import com.dimeng.p2p.escrow.fuyou.executor.FYRepaymentExecutor;
import com.dimeng.p2p.escrow.fuyou.service.PublicManage;
import com.dimeng.p2p.modules.bid.user.service.TenderRepaymentManage;
import com.dimeng.p2p.variables.defines.URLVariable;
import com.dimeng.util.parser.IntegerParser;

*//**
 * 
 * 手动还款
 * <富友托管标还款-正常>
 * 
 * @author  suwei
 * @version  [版本号, 2016年03月02日]
 *//*
public class FyouPayment extends AbstractFuyouServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        logger.info("APP富友托管-还款-开始——IP:" + request.getRemoteAddr());
        int id = IntegerParser.parse(request.getParameter("id"));
        int number = IntegerParser.parse(request.getParameter("number"));
        
        PublicManage publicManage = serviceSession.getService(PublicManage.class);
        int[] orderIds = null;
        orderIds = publicManage.selectPayment(id, number);
        TenderRepaymentManage manage = serviceSession.getService(TenderRepaymentManage.class);
        if (orderIds == null)
        {
            orderIds = manage.repayment(id, number);
        }
        Map<String, String> params = new HashMap<String, String>();
        try
        {
            FYRepaymentExecutor executor = getResourceProvider().getResource(FYRepaymentExecutor.class);
            
            if (orderIds != null && orderIds.length > 0)
            {
                publicManage.updtateT6501F10(orderIds, FuyouTypeEnum.SDHK.name());
                logger.info("标ID：" + id + "-还款总数：" + orderIds.length);
                int i = 0;
                params.put("hint", "success");
                for (int orderId : orderIds)
                {
                    i++;
                    publicManage.searchT6501(serviceSession, orderId, params, true);
                    switch (params.get("state"))
                    {
                        case "DTJ":
                            executor.submit(orderId, params);
                            if (!"true".equals(params.get("success")))
                            {
                                logger.info("第" + i + "条还款-失败");
                                params.put("hint", "fail");
                                // 失败时将垫付记录改回未还
                                //manage.updateT6252(id, number);
                                break;
                            }
                            logger.info("第" + i + "条还款-成功");
                            executor.confirm(orderId, params);
                            break;
                        case "DQR":
                            // 第三方成功，平台未更新
                            executor.confirm(orderId, params);
                            logger.info("第" + i + "条还款-确认成功");
                            break;
                        case "CG":
                            logger.info("第" + i + "条还款-已确认成功");
                            break;
                        default:
                            executor.confirm(orderId, params);
                            logger.info("第" + i + "条还款-确认成功");
                            break;
                    }
                }
                logger.info("标ID：" + id + "-第" + --i + "条还款-还款调用结束。");
            }
            manage.writeFrontLog(FrontLogType.SDHK.getName(), " 前台手动还款-标Id:" + id);
        }
        catch (Throwable e)
        {
            manage.updateT6252(id, number);
            throw e;
        }
        request.setAttribute("id", id);
        request.setAttribute("number", number);
        request.setAttribute("orderIds", orderIds);
        String url = getConfigureProvider().format(URLVariable.PAY_PAYMENT_URL_SECOND);
        url = url.concat("?id=").concat(id + "");
        if ("success".equals(params.get("hint")))
        {
            setReturnMsg(request, response, ExceptionCode.SUCCESS, "操作成功");
            return;
        }
        else
        {
            setReturnMsg(request, response, ExceptionCode.ERROR, "操作失败！");
            return;
        }
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        getResourceProvider().log(throwable.getMessage());
        int id = IntegerParser.parse(request.getParameter("id"));
        ConfigureProvider configureProvider = getResourceProvider().getResource(ConfigureProvider.class);
        if (throwable instanceof ParameterException || throwable instanceof SQLException)
        {
            getController().prompt(request, response, PromptLevel.ERROR, "系统繁忙，请您稍后再试");
            sendRedirect(request, response, configureProvider.format(URLVariable.PAY_PAYMENT_URL_SECOND)
                .concat("?id=")
                .concat(id + ""));
        }
        else if (throwable instanceof LogicalException)
        {
            getController().prompt(request, response, PromptLevel.WARRING, throwable.getMessage());
            sendRedirect(request, response, configureProvider.format(URLVariable.PAY_PAYMENT_URL_SECOND)
                .concat("?id=")
                .concat(id + ""));
        }
        else
        {
            super.onThrowable(request, response, throwable);
        }
    }
}
*/