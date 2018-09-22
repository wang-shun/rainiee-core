package com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S61.entities.T6161;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S62.entities.T6230;
import com.dimeng.p2p.S62.entities.T6235;
import com.dimeng.p2p.S62.enums.T6230_F11;
import com.dimeng.p2p.S62.enums.T6230_F13;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.console.servlets.bid.AbstractBidServlet;
import com.dimeng.p2p.modules.account.console.service.EnterpriseManage;
import com.dimeng.p2p.modules.account.console.service.GrManage;
import com.dimeng.p2p.modules.account.console.service.UserManage;
import com.dimeng.p2p.modules.account.console.service.entity.Grxx;
import com.dimeng.p2p.modules.bid.console.service.BidManage;
import com.dimeng.p2p.modules.bid.console.service.entity.Dbxx;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;

public class SaveLine extends AbstractBidServlet
{
    
    private static final long serialVersionUID = -268955794485557959L;
    
    @Override
    protected void processGet(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        BidManage bidManage = serviceSession.getService(BidManage.class);
        UserManage userManage = serviceSession.getService(UserManage.class);
        //AnnexManage annexManage = serviceSession.getService(AnnexManage.class);
        GrManage personalManage = serviceSession.getService(GrManage.class);
        EnterpriseManage enterpriseManage = serviceSession.getService(EnterpriseManage.class);
        int loanId = IntegerParser.parse(request.getParameter("loanId"));
        int userId = IntegerParser.parse(request.getParameter("userId"));
        T6230 t6230 = bidManage.getBidType(loanId);
        if (t6230 != null && t6230.F20 != T6230_F20.SQZ)
        {
            prompt(request, response, PromptLevel.ERROR, "不是申请中状态");
            sendRedirect(request, response, getController().getURI(request, LoanList.class));
            return;
        }
        try
        {
            if (t6230.F32 <= 0)
            {
                //如果是前台用户提交的信用贷款，则需判断是否有保存产品名称、标的类型
                throw new LogicalException("请选择产品名称并保存");
            }
            
            if (t6230.F13 == T6230_F13.S)
            {
                T6235 dyws = bidManage.finBidDywDate(loanId);
                if (dyws == null)
                {
                    throw new LogicalException("请填写抵押物信息");
                }
            }
            if (t6230.F11 == T6230_F11.S)
            {
                Dbxx[] dbxxs = bidManage.searchBidDb(loanId);
                if (dbxxs == null || dbxxs.length <= 0)
                {
                    throw new LogicalException("您没有填写担保信息");
                }
            }
            // if (!annexManage.isGkExists(loanId)
            // && (t6230.F11 == T6230_F11.S || t6230.F13 == T6230_F13.S ||
            // t6230.F14 == T6230_F14.S)) {
            // throw new LogicalException("请上传马赛克附件");
            // }
            // if (!annexManage.isFgkExists(loanId)
            // && (t6230.F11 == T6230_F11.S || t6230.F13 == T6230_F13.S ||
            // t6230.F14 == T6230_F14.S)) {
            // throw new LogicalException("请上传完整版附件");
            // }
            T6110_F06 userType = userManage.getUserType(userId);
            if (userType == T6110_F06.ZRR)
            {
                Grxx user = personalManage.getUser(userId);
                if (user == null || StringHelper.isEmpty(user.name) || StringHelper.isEmpty(user.userName)
                    || (StringHelper.isEmpty(user.phone) && StringHelper.isEmpty(user.email))
                    || StringHelper.isEmpty(user.sfzh))
                {
                    throw new LogicalException("该用户的个人资料未填写完整,不能进行发标操作");
                }
            }
            else if (userType == T6110_F06.FZRR)
            {
                T6161 t6161 = enterpriseManage.getEnterprise(userId);
                if (t6161 == null || StringHelper.isEmpty(t6161.F02)
                    || ("N".equals(t6161.F20) && StringHelper.isEmpty(t6161.F03)) || StringHelper.isEmpty(t6161.F04)
                    || ("N".equals(t6161.F20) && StringHelper.isEmpty(t6161.F05))
                    || ("N".equals(t6161.F20) && StringHelper.isEmpty(t6161.F06))
                    || ("Y".equals(t6161.F20) && StringHelper.isEmpty(t6161.F19)) || StringHelper.isEmpty(t6161.F13))
                {
                    throw new LogicalException("该企业的必要基础信息未填写完整,不能进行发标操作");
                }
            }
            bidManage.submit(loanId);
            sendRedirect(request, response, getController().getURI(request, LoanList.class));
        }
        catch (Throwable throwable)
        {
            if (throwable instanceof LogicalException || throwable instanceof ParameterException)
            {
                getController().prompt(request, response, PromptLevel.WARRING, throwable.getMessage());
                sendRedirect(request, response, getController().getURI(request, AddAnnexWz.class) + "?loanId=" + loanId
                    + "&userId=" + userId);
            }
            else
            {
                super.onThrowable(request, response, throwable);
            }
        }
    }
}
