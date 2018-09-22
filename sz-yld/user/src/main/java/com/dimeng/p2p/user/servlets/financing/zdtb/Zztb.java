package com.dimeng.p2p.user.servlets.financing.zdtb;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.account.user.service.UserInfoManage;
import com.dimeng.p2p.modules.bid.user.service.ZdtbManage;
import com.dimeng.p2p.modules.bid.user.service.query.AutoBidQuery;
import com.dimeng.p2p.user.servlets.financing.AbstractFinancingServlet;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;

public class Zztb extends AbstractFinancingServlet
{
    
    /**
     * 
     */
    private static final long serialVersionUID = -2258438940746128554L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        ZdtbManage autoUtilFinacingManage = serviceSession.getService(ZdtbManage.class);
        UserInfoManage userInfoManage = serviceSession.getService(UserInfoManage.class);
        T6110 userInfo = userInfoManage.getUserInfo(serviceSession.getSession().getAccountId());
        if (userInfo == null || T6110_F06.FZRR == userInfo.F06)
        {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        int count = autoUtilFinacingManage.autoBidCountQY(null);
        if (count >= 3)
        {
            throw new LogicalException("您已达到新增自动投资规则数量最大值，请删除/关闭已有规则，再重新新增规则");
        }
        autoUtilFinacingManage.save(new AutoBidQuery()
        {
            @Override
            public BigDecimal getTimeMoney()
            {
                return StringHelper.isEmpty(request.getParameter("timeMoney")) ? BigDecimal.ZERO : new BigDecimal(
                    request.getParameter("timeMoney"));
            }
            
            @Override
            public BigDecimal getSaveMoney()
            {
                return StringHelper.isEmpty(request.getParameter("saveMoney")) ? BigDecimal.ZERO : new BigDecimal(
                    request.getParameter("saveMoney"));
            }
            
            @Override
            public BigDecimal getRateStart()
            {
                return new BigDecimal(request.getParameter("rateStart")).divide(new BigDecimal(100));
            }
            
            @Override
            public BigDecimal getRateEnd()
            {
                return new BigDecimal(request.getParameter("rateEnd")).divide(new BigDecimal(100));
            }
            
            @Override
            public int getLevelStart()
            {
                return IntegerParser.parse(request.getParameter("levelStart"));
            }
            
            @Override
            public int getLevelEnd()
            {
                return IntegerParser.parse(request.getParameter("levelEnd"));
            }
            
            @Override
            public int getJkqxStart()
            {
                return IntegerParser.parse(request.getParameter("jkqxStart"));
            }
            
            @Override
            public int getJkqxEnd()
            {
                return IntegerParser.parse(request.getParameter("jkqxEnd"));
            }
            
            @Override
            public int mctbje()
            {
                return StringHelper.isEmpty(request.getParameter("mctbje")) ? 1
                    : Integer.parseInt(request.getParameter("mctbje"));
            }
        });
        
        forward(request, response, getController().getURI(request, Index.class));
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        getResourceProvider().log(throwable);
        if (throwable instanceof SQLException)
        {
            getController().prompt(request, response, PromptLevel.ERROR, "系统繁忙，请您稍后再试");
            sendRedirect(request, response, getController().getViewURI(request, Index.class));
        }
        else if (throwable instanceof LogicalException || throwable instanceof ParameterException)
        {
            getController().prompt(request, response, PromptLevel.WARRING, throwable.getMessage());
            sendRedirect(request, response, getController().getViewURI(request, Index.class));
        }
        else
        {
            super.onThrowable(request, response, throwable);
        }
    }
}
