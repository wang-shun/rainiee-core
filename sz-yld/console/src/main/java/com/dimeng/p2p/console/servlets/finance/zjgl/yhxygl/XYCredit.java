package com.dimeng.p2p.console.servlets.finance.zjgl.yhxygl;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.console.servlets.finance.AbstractFinanceServlet;
import com.dimeng.p2p.console.servlets.finance.zjgl.yhxygl.XyList;
import com.dimeng.p2p.modules.account.console.service.UserManage;
import com.dimeng.util.parser.BigDecimalParser;
import com.dimeng.util.parser.IntegerParser;

@Right(id = "P2P_C_FINANCE_XYOPERGLLIST", name = "修改信用额度", moduleId="P2P_C_FINANCE_ZJGL_YHXYGL", order = 2)
public class XYCredit extends AbstractFinanceServlet
{
    private static final long serialVersionUID = 1L;
    
    private static final BigDecimal MAX_CREDIT = new BigDecimal("999999999999.99");
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        try
        {
            UserManage userManage = serviceSession.getService(UserManage.class);
            int userId = IntegerParser.parse(request.getParameter("userId"));
            if (userId < 1)
            {
                throw new ParameterException("参数错误");
            }
            BigDecimal creditAmount = BigDecimalParser.parse(request.getParameter("userCredit"));
            if (creditAmount.compareTo(MAX_CREDIT) > 0)
            {
                throw new ParameterException("输入的额度超过限制");
            }
            userManage.updateUserCredit(userId, creditAmount);
            sendRedirect(request, response, getController().getURI(request, XyList.class));
        }
        catch (Throwable throwable)
        {
            if (throwable instanceof ParameterException || throwable instanceof LogicalException)
            {
                getController().prompt(request, response, PromptLevel.WARRING, throwable.getMessage());
                sendRedirect(request, response, getController().getURI(request, XyList.class));
            }
        }
    }

}
