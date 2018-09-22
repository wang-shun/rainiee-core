package com.dimeng.p2p.user.servlets.capital;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.account.user.service.TxManage;
import com.dimeng.p2p.account.user.service.UserInfoManage;
import com.dimeng.p2p.common.FormToken;
import com.dimeng.p2p.common.RSAUtils;
import com.dimeng.p2p.common.enums.FrontLogType;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.p2p.variables.defines.pays.PayVariavle;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BooleanParser;
import com.dimeng.util.parser.IntegerParser;

public class Withdraw extends AbstractCapitalServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        
        if (!FormToken.verify(serviceSession.getSession(), request.getParameter(FormToken.parameterName())))
        {
            throw new LogicalException("请不要重复提交请求！");
        }
        UserInfoManage uManage = serviceSession.getService(UserInfoManage.class);
        T6110 t6110 = uManage.getUserInfo(serviceSession.getSession().getAccountId());
        if (T6110_F07.HMD == t6110.F07)
        {
            throw new LogicalException("账户已被拉黑，不能进行提现！");
        }
        //getController().prompt(request, response, PromptLevel.WARRING, request.getParameter("amount"));
        String withdrawPsd = request.getParameter("withdrawPsd");
        if (StringHelper.isEmpty(request.getParameter("amount")))
        {
            throw new LogicalException("提现金额不能为空");
        }
        ConfigureProvider configureProvider = getResourceProvider().getResource(ConfigureProvider.class);
        boolean isOpenWithPsd = BooleanParser.parse(configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD));
        if (StringHelper.isEmpty(withdrawPsd) && isOpenWithPsd)
        {
            throw new LogicalException("交易密码不能为空");
        }
        int cardId = IntegerParser.parse(request.getParameter("cardId"));
        BigDecimal amount = new BigDecimal(request.getParameter("amount"));
        if (isOpenWithPsd)
        {
            withdrawPsd = RSAUtils.decryptStringByJs(withdrawPsd);
        }
        
        //getController().clearPrompts(request, response, PromptLevel.WARRING);
        
        boolean txkcfs = Boolean.parseBoolean(getConfigureProvider().getProperty(SystemVariable.TXSXF_KCFS));
        BigDecimal poundage = null;
        String pundageWay = getConfigureProvider().getProperty(SystemVariable.WITHDRAW_POUNDAGE_WAY);
        if ("BL".equals(pundageWay))
        {
            // 按比例计算
            String _proportion = getConfigureProvider().getProperty(SystemVariable.WITHDRAW_POUNDAGE_PROPORTION);
            if (StringHelper.isEmpty(_proportion) || _proportion.contains("-"))
            {
                throw new LogicalException("系统繁忙，请稍后再试！");
            }
            BigDecimal proportion = new BigDecimal(_proportion);
            poundage = amount.multiply(proportion).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        else
        {
            // 按额度计算[1-50000), [50000, ]
            if (amount.compareTo(new BigDecimal(50000)) < 0)
            {
                poundage = new BigDecimal(getConfigureProvider().getProperty(SystemVariable.WITHDRAW_POUNDAGE_1_5));
            }
            else
            {
                poundage = new BigDecimal(getConfigureProvider().getProperty(SystemVariable.WITHDRAW_POUNDAGE_5_20));
            }
        }
        TxManage manage = serviceSession.getService(TxManage.class);
        manage.withdraw(amount, withdrawPsd, cardId, T6101_F03.WLZH, poundage, txkcfs);
        BigDecimal money = new BigDecimal(getConfigureProvider().format(SystemVariable.WITHDRAW_LIMIT_FUNDS));//BigDecimal.ZERO;
        String escrow = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
        /*if (!txkcfs)
        {//如果是外扣方式,则审核金额需要+手续费.
        	amount = amount.add(poundage);
        }*/
        //提现金额小于提现审核所限制金额或所限金额为0就自动提现，不需人工审核
        manage.writeFrontLog(FrontLogType.TX.getName(), "前台提现");
        //如果是通联代付的话，必须要审核
        if (amount.doubleValue() > money.doubleValue() || money.doubleValue() <= 0 || escrow.equals("ALLINPAY"))
        {
            getController().prompt(request, response, PromptLevel.INFO, "您的提现申请已提交，预计T+1工作日到账，具体以实际到账日期为准！");
        }
        else
        {
            getController().prompt(request, response, PromptLevel.INFO, "提现成功！");
        }
        sendRedirect(request, response, getController().getViewURI(request, Withdraw.class));
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        logger.error(throwable, throwable);
        if (throwable instanceof NumberFormatException)
        {
            sendRedirect(request, response, getController().getViewURI(request, Withdraw.class));
        }
        if (throwable instanceof LogicalException)
        {
            getController().prompt(request, response, PromptLevel.INFO, throwable.getMessage());
            forwardView(request, response, Withdraw.class);
            //sendRedirect(request, response, getController().getViewURI(request, Withdraw.class));
        }
        if (throwable instanceof SQLException)
        {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
