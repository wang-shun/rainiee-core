package com.dimeng.p2p.user.servlets.capital;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.OrderType;
import com.dimeng.p2p.S61.entities.T6125;
import com.dimeng.p2p.S61.enums.T6125_F05;
import com.dimeng.p2p.account.user.service.FxbyjManage;
import com.dimeng.p2p.common.ResourceProviderUtil;
import com.dimeng.p2p.order.BondExecutor;
import com.dimeng.p2p.repeater.guarantor.ApplyGuarantorManage;
import com.dimeng.p2p.user.servlets.Index;
import com.dimeng.p2p.variables.defines.GuarantorVariavle;
import com.dimeng.util.parser.BigDecimalParser;

public class ByjCharge extends AbstractCapitalServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        try
        {
            FxbyjManage fxbyjManage = serviceSession.getService(FxbyjManage.class);
            ApplyGuarantorManage applyGuarantorManage1 = serviceSession.getService(ApplyGuarantorManage.class);
            ConfigureProvider configureProvider =
                ResourceProviderUtil.getResourceProvider().getResource(ConfigureProvider.class);
            T6125 t6125 = applyGuarantorManage1.getGuanterInfo();
            Boolean isHasGuarant =
                Boolean.parseBoolean(configureProvider.getProperty(GuarantorVariavle.IS_HAS_GUARANTOR));
            if (isHasGuarant && (t6125 == null || (t6125.F05 != T6125_F05.SQCG && t6125.F05 != T6125_F05.QXCG)))
            {
                throw new LogicalException("不是担保方，不能转入或转出保证金");
            }
            BigDecimal amount = BigDecimalParser.parse(request.getParameter("amount"));
            if (BigDecimal.ZERO.compareTo(amount) > 0)
            {
                throw new LogicalException("金额必须大于0，且为数字!");
            }
            //在订单表T6501插入订单信息，在保证金充值订单T6513插入充值信息
            int orderId = fxbyjManage.recharge(amount, OrderType.BONDIN.orderType());
            BondExecutor bondExecutor = getResourceProvider().getResource(BondExecutor.class);
            bondExecutor.submit(orderId, null);
            bondExecutor.confirm(orderId, null);
            sendRedirect(request, response, getController().getURI(request, Index.class));
        }
        catch (Throwable throwable)
        {
            if (throwable instanceof LogicalException || throwable instanceof ParameterException)
            {
                getController().prompt(request, response, PromptLevel.WARRING, throwable.getMessage());
                sendRedirect(request, response, getController().getViewURI(request, getClass()));
            }
        }
    }
}
