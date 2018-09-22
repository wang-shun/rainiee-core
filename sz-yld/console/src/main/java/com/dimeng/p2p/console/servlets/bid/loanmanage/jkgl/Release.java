package com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.message.email.EmailSender;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.S62.entities.T6230;
import com.dimeng.p2p.S62.enums.T6230_F11;
import com.dimeng.p2p.S62.enums.T6230_F37;
import com.dimeng.p2p.S62.enums.T6230_F38;
import com.dimeng.p2p.console.servlets.bid.AbstractBidServlet;
import com.dimeng.p2p.modules.account.console.service.GrManage;
import com.dimeng.p2p.modules.bid.console.service.AddBidManage;
import com.dimeng.p2p.modules.bid.console.service.BidManage;
import com.dimeng.p2p.variables.defines.EmailVariavle;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.parser.BooleanParser;
import com.dimeng.util.parser.IntegerParser;

@Right(id = "P2P_C_BID_RELEASE", name = "发布[预发布]", moduleId = "P2P_C_BID_JKGL_LOANMANAGE", order = 5)
public class Release extends AbstractBidServlet
{
    private static final long serialVersionUID = -268955794485557959L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        int loanId = IntegerParser.parse(request.getParameter("loanId"));

        BidManage bidManage = serviceSession.getService(BidManage.class);
        ConfigureProvider configureProvider = getResourceProvider().getResource(ConfigureProvider.class);
        String escrow = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
        boolean isDeposit = BooleanParser.parse(configureProvider.getProperty(SystemVariable.IS_OPEN_DEPOSIT));

        if (isDeposit && "huifu".equals(escrow))
        {
            int useId = IntegerParser.parse(request.getParameter("useId"));
            AddBidManage addBidMange = serviceSession.getService(AddBidManage.class);
            Map<String, String> map = addBidMange.checkBidInfo(loanId);
            if ("02".equals(map.get("stat")))
            {
                bidManage.notThrough(loanId, map.get("desc"));
                getController().prompt(request, response, PromptLevel.WARRING, map.get("desc"));
                //发送邮件
                EmailSender emailSender = serviceSession.getService(EmailSender.class);
                emailSender.send(0,
                    "审核不通过",
                    String.format(EmailVariavle.JKSHBTG_MAIL_STR.getDescription(), map.get("desc"), ""),
                    serviceSession.getService(GrManage.class).findBasicInfo(useId).mailbox);
            }
            else if ("00".equals(map.get("stat")))
            {
                bidManage.release(loanId);
            }
            getController().prompt(request, response, PromptLevel.INFO, map.get("desc"));
        }
        
        //环迅托管
        else if ("huanxun".equalsIgnoreCase(escrow))
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
                        bidManage.release(loanId);
                    }
                    else
                    {
                        throw new LogicalException("标的担保机构登记在第三方未登记成功！不能发布……");
                    }
                }
                else
                {
                    //非担保标，不需要登记担保机构，直接发布
                    bidManage.release(loanId);
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
            bidManage.release(loanId);
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
