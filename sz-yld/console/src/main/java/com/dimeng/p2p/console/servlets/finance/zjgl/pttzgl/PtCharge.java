/*
 * 文 件 名:  PtCharge.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2015年11月19日
 */
package com.dimeng.p2p.console.servlets.finance.zjgl.pttzgl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.common.FormToken;
import com.dimeng.p2p.console.servlets.finance.AbstractFinanceServlet;
import com.dimeng.p2p.modules.account.console.service.CheckBalanceManage;
import com.dimeng.p2p.order.PlatformCheckBalanceExecutor;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.parser.BigDecimalParser;
import com.dimeng.util.parser.BooleanParser;

/**
 * 平台充值
 * 
 * @author  xiaoqi
 * @version  [版本号, 2015年11月19日]
 */
@Right(id = "P2P_C_FINANCE_PTCHARGE", name = "平台充值", moduleId = "P2P_C_FINANCE_ZJGL_PTTZGL", order = 1)
public class PtCharge extends AbstractFinanceServlet
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        CheckBalanceManage manage = serviceSession.getService(CheckBalanceManage.class);
        ResourceProvider resourceProvider = getResourceProvider();
        ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        Boolean tg = BooleanParser.parseObject(configureProvider.getProperty(SystemVariable.SFZJTG));
        BigDecimal balance = manage.getPTZHBalance(tg);
        request.setAttribute("balance", balance.toString());
        forwardView(request, response, PtCharge.class);
    }
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        try
        {
            if (!FormToken.verify(serviceSession.getSession(), request.getParameter(FormToken.parameterName())))
            {
                getController().prompt(request, response, PromptLevel.WARRING, "请不要重复提交请求！");
                sendRedirect(request, response, getController().getURI(request, PttzglList.class));
                return;
            }
            ResourceProvider resourceProvider = getResourceProvider();
            ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
            Boolean tg = BooleanParser.parseObject(configureProvider.getProperty(SystemVariable.SFZJTG));
            String ESCROW_PREFIX = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
            BigDecimal amount = BigDecimalParser.parse(request.getParameter("amount"));
            String remark = request.getParameter("remark");
            CheckBalanceManage manage = serviceSession.getService(CheckBalanceManage.class);
            int orderId = manage.recharge(amount, remark, tg);
            PlatformCheckBalanceExecutor executor = resourceProvider.getResource(PlatformCheckBalanceExecutor.class);
            if (orderId > 0)
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("type", "charge");
                executor.submit(orderId, params);
                
                if (!tg || "yeepay".equalsIgnoreCase(ESCROW_PREFIX) || "FUYOU".equalsIgnoreCase(ESCROW_PREFIX)
                    || "HUIFU".equalsIgnoreCase(ESCROW_PREFIX))
                {
                    executor.confirm(orderId, params);
                }
            }
            getController().prompt(request, response, PromptLevel.INFO, "充值成功!");
            sendRedirect(request, response, getController().getURI(request, PttzglList.class));
        }
        catch (Throwable throwable)
        {
            if (throwable instanceof LogicalException || throwable instanceof ParameterException)
            {
                getController().prompt(request, response, PromptLevel.WARRING, throwable.getMessage());
                sendRedirect(request, response, getController().getURI(request, PtCharge.class));
            }
            else
            {
                super.onThrowable(request, response, throwable);
            }
        }
    }
    
}
