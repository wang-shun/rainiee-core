package com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S61.entities.T6180;
import com.dimeng.p2p.S62.entities.T6230;
import com.dimeng.p2p.S62.entities.T6236;
import com.dimeng.p2p.S62.entities.T6237;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.console.servlets.bid.AbstractBidServlet;
import com.dimeng.p2p.modules.account.console.service.EnterpriseManage;
import com.dimeng.p2p.modules.account.console.service.entity.Jg;
import com.dimeng.p2p.modules.bid.console.service.BidManage;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.parser.BooleanParser;
import com.dimeng.util.parser.IntegerParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Right(id = "P2P_C_BID_ADDPROJECT", name = "新增", moduleId = "P2P_C_BID_JKGL_LOANMANAGE", order = 1)
public class AddGuarantee extends AbstractBidServlet
{
    
    /**
     * 
     */
    private static final long serialVersionUID = -268955794485557959L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        EnterpriseManage enterpriseManage = serviceSession.getService(EnterpriseManage.class);
        int jgId = IntegerParser.parse(request.getParameter("jgId"));
        ConfigureProvider configureProvider = getResourceProvider().getResource(ConfigureProvider.class);
        boolean tg = BooleanParser.parse(configureProvider.getProperty(SystemVariable.SFZJTG));
        String escrow = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
        Jg[] jgs;
        if (tg)
        {
            jgs = enterpriseManage.searchTgJg();
        }
        else
        {
            jgs = enterpriseManage.searchJg();
        }
        //String des = enterpriseManage.getJgDes(jgId);
        T6180 t6180 = enterpriseManage.getJgInfo(jgId);
        request.setAttribute("jgs", jgs);
        request.setAttribute("jgxx", t6180);
        //request.setAttribute("des", des);
        forwardView(request, response, getClass());
    }
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        int loanId = IntegerParser.parse(request.getParameter("loanId"));
        int userId = IntegerParser.parse(request.getParameter("userId"));
        BidManage bidManage = serviceSession.getService(BidManage.class);
        T6230 t6230 = bidManage.get(loanId);
        if (t6230 != null && t6230.F20 != T6230_F20.SQZ)
        {
            prompt(request, response, PromptLevel.ERROR, "不是申请中状态");
            sendRedirect(request, response, getController().getURI(request, LoanList.class));
            return;
        }
        T6236 t6236 = new T6236();
        t6236.parse(request);
        t6236.F02 = loanId;
        t6236.F05 = request.getParameter("dbzz");
        bidManage.addGuarantee(t6236);
        T6237 t6237 = new T6237();
        t6237.F01 = loanId;
        t6237.F02 = request.getParameter("dbzz");
        //t6237.F03 = request.getParameter("fdbqk");
        t6237.F03 = "";
        bidManage.addFx(t6237);
        
        sendRedirect(request, response, getController().getURI(request, AddGuaranteeXq.class) + "?loanId=" + loanId
            + "&userId=" + userId);
    }
}
