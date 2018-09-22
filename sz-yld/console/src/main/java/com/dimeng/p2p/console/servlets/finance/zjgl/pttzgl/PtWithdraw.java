/*
 * 文 件 名:  PtWithdraw.java
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
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BigDecimalParser;
import com.dimeng.util.parser.BooleanParser;

/**
 * 平台提现
 * 
 * @author  xiaoqi
 * @version  [版本号, 2015年11月19日]
 */
@Right(id = "P2P_C_FINANCE_PTWITHDRAW", name = "平台提现", moduleId = "P2P_C_FINANCE_ZJGL_PTTZGL", order = 2)
public class PtWithdraw extends AbstractFinanceServlet
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
        forwardView(request, response, getClass());
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
            String escrowFinance = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
            String ESCROW_PREFIX = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
            if (StringHelper.isEmpty(request.getParameter("amount")))
            {
                throw new LogicalException("提现金额不能为空");
            }
            BigDecimal amount = BigDecimalParser.parse(request.getParameter("amount"));
            String remark = request.getParameter("remark");
            CheckBalanceManage manage = serviceSession.getService(CheckBalanceManage.class);
            String bankCard = "0000000000000000";
            if (tg && !escrowFinance.equalsIgnoreCase("yeepay") && !escrowFinance.equalsIgnoreCase("FUYOU")
                && !escrowFinance.equalsIgnoreCase("HUIFU"))
            {
                Integer bankCardId = Integer.parseInt(request.getParameter("cardId"));
                bankCard = manage.getBankCardById(bankCardId);
                if (!StringHelper.isEmpty(bankCard))
                {
                    bankCard = StringHelper.decode(bankCard);
                }
            }
            int orderId = manage.withdraw(amount, remark, bankCard, tg);
            PlatformCheckBalanceExecutor executor = resourceProvider.getResource(PlatformCheckBalanceExecutor.class);
            if (orderId > 0)
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("type", "withdraw");
                executor.submit(orderId, params);
                if (!tg || "yeepay".equalsIgnoreCase(ESCROW_PREFIX) || "FUYOU".equalsIgnoreCase(ESCROW_PREFIX)
                    || "HUIFU".equalsIgnoreCase(ESCROW_PREFIX))
                {
                    executor.confirm(orderId, params);
                }
            }
            if (tg && !escrowFinance.equalsIgnoreCase("yeepay") && !escrowFinance.equalsIgnoreCase("FUYOU")
                && !"HUIFU".equalsIgnoreCase(ESCROW_PREFIX))
            {
                getController().prompt(request, response, PromptLevel.INFO, "提现申请已经提交!");
            }
            else
            {
                getController().prompt(request, response, PromptLevel.INFO, "提现成功!");
            }
            sendRedirect(request, response, getController().getURI(request, PttzglList.class));
        }
        catch (Throwable throwable)
        {
            if (throwable instanceof LogicalException || throwable instanceof ParameterException)
            {
                getController().prompt(request, response, PromptLevel.WARRING, throwable.getMessage());
                sendRedirect(request, response, getController().getURI(request, PtWithdraw.class));
            }
            else
            {
                super.onThrowable(request, response, throwable);
            }
        }
    }
}
