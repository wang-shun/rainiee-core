/*
 * 文 件 名:  AddBidProgres.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修改时间:  2016年3月10日
 */
package com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S62.entities.T6248;
import com.dimeng.p2p.common.FormToken;
import com.dimeng.p2p.console.servlets.bid.AbstractBidServlet;
import com.dimeng.p2p.modules.bid.console.service.BidManage;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;

/**
 * <新增标进展>
 *
 * @version  [版本号, 2016年3月10日]
 */
@Right(id = "P2P_C_LOAN_VIEW_BIDPROGRES", name = "动态管理", moduleId = "P2P_C_BID_JKGL_LOANMANAGE", order = 10)
public class AddBidProgres extends AbstractBidServlet
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 65236221L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        super.processGet(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        
        if (!FormToken.verify(serviceSession.getSession(), request.getParameter(FormToken.parameterName())))
        {
            throw new ParameterException("请不要重复提交请求！");
        }
        
        BidManage bidManage = serviceSession.getService(BidManage.class);
        int loanId = IntegerParser.parse(request.getParameter("loanId"));
        
        try
        {
            T6248 t6248 = new T6248();
            t6248.parse(request);
            t6248.F02 = serviceSession.getSession().getAccountId();
            t6248.F03 = loanId;
            
            if (StringHelper.isEmpty(t6248.F04) || StringHelper.isEmpty(t6248.F06) || null == t6248.F08)
            {
                getController().prompt(request, response, PromptLevel.WARRING, "主题标题,标题时间或介绍不能为空！");
                // 跳转到信息页面
                forwardView(request, response, getClass());
                return;
            }
            //去掉时分秒，再比较。
            /*Date date = new Date(t6242.F13.getTime());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String sDate = sdf.format(date);
            if (t6245.F08.compareTo(sdf.parse(sDate)) < 0)
            {
                getController().prompt(request, response, PromptLevel.WARRING, "标题时间不能小于标的发布时间！");
                // 跳转到信息页面
                forwardView(request, response, getClass());
                return;
            }*/
            bidManage.addBidProgres(t6248);
            // 跳转到倡议书信息页面
            sendRedirect(request, response, getController().getURI(request, ViewBidProgres.class) + "?loanId=" + loanId);
        }
        catch (Throwable throwable)
        {
            logger.error(throwable, throwable);
            if (throwable instanceof ParameterException)
            {
                getController().prompt(request, response, PromptLevel.WARRING, throwable.getMessage());
                processGet(request, response, serviceSession);
            }
            else if (throwable instanceof LogicalException)
            {
                getController().prompt(request, response, PromptLevel.WARRING, throwable.getMessage());
                forwardView(request, response, LoanList.class);
            }
            else
            {
                super.onThrowable(request, response, throwable);
            }
        }
    }
    
}
