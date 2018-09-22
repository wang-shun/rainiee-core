package com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl;

import java.io.IOException;
import java.sql.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.S62.entities.T6230;
import com.dimeng.p2p.S62.enums.T6230_F11;
import com.dimeng.p2p.S62.enums.T6230_F37;
import com.dimeng.p2p.S62.enums.T6230_F38;
import com.dimeng.p2p.console.servlets.bid.AbstractBidServlet;
import com.dimeng.p2p.modules.bid.console.service.BidManage;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.TimestampParser;

@Right(id = "P2P_C_BID_RELEASE", name = "发布[预发布]", moduleId = "P2P_C_BID_JKGL_LOANMANAGE", order = 5)
public class PreRelease extends AbstractBidServlet
{
    private static final long serialVersionUID = -268955794485557959L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        try
        {
            int loanId = IntegerParser.parse(request.getParameter("loanId"));
            Timestamp releaseTime = TimestampParser.parse(request.getParameter("releaseTime"));
            Timestamp now = new Timestamp(System.currentTimeMillis());
            if (releaseTime.getTime() <= now.getTime())
            {
                throw new LogicalException("预发布时间不能小于当前时间");
            }
            
            BidManage bidManage = serviceSession.getService(BidManage.class);
            ConfigureProvider configureProvider = getResourceProvider().getResource(ConfigureProvider.class);
            String escrow = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
            //环迅托管
            if ("huanxun".equalsIgnoreCase(escrow))
            {
                T6230 t6230 = bidManage.get(loanId);
                if (T6230_F37.S == t6230.F37)
                {
                    //环迅标的登记成功
                    if (T6230_F11.S == t6230.F11)
                    {
                        //担保标，需要判断登记担保机构是否成功
                        if (T6230_F38.S == t6230.F38)
                        {
                            bidManage.preRelease(loanId, releaseTime);
                        }
                        else
                        {
                            throw new LogicalException("标的担保机构登记在第三方未登记成功！不能发布……");
                        }
                    }
                    else
                    {
                        //非担保标，不需要登记担保机构，直接发布
                        bidManage.preRelease(loanId, releaseTime);
                    }
                }
                else
                {
                    //环迅标的登记失败
                    throw new LogicalException("标的登记在第三方未登记成功！不能发布……");
                }
            }
            else
            {
                bidManage.preRelease(loanId, releaseTime);
            }
        }
        catch (Throwable throwable)
        {
            getController().prompt(request, response, PromptLevel.ERROR, throwable.getMessage());
        }
        sendRedirect(request, response, getController().getURI(request, LoanList.class));
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        prompt(request, response, PromptLevel.ERROR, throwable.getMessage());
        sendRedirect(request, response, getController().getURI(request, LoanList.class));
    }

    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }

}
