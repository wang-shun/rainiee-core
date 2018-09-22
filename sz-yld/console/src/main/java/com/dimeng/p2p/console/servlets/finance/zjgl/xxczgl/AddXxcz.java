package com.dimeng.p2p.console.servlets.finance.zjgl.xxczgl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.common.FormToken;
import com.dimeng.p2p.console.servlets.finance.AbstractFinanceServlet;
import com.dimeng.p2p.modules.account.console.service.OfflineChargeManage;
import com.dimeng.util.parser.BigDecimalParser;
import com.dimeng.util.parser.BooleanParser;

@Right(id = "P2P_C_FINANCE_ADDXXCZ", name = "新增", moduleId = "P2P_C_FINANCE_ZJGL_XXCZGL", order = 1)
public class AddXxcz extends AbstractFinanceServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        forwardView(request, response, getClass());
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        
        if (!FormToken.verify(serviceSession.getSession(), request.getParameter("token")))
        {
            request.setAttribute("EMAIL_ERROR", "请不要重复提交请求！");
            forward(request, response, getController().getViewURI(request, AddXxcz.class));
            return;
        }
        //后端校验是否已经绑定手机、实名认证、交易密码
        boolean checkFlag = true;
        String checkMessage = "";
        com.dimeng.p2p.service.UserInfoManage userCommManage =
            serviceSession.getService(com.dimeng.p2p.service.UserInfoManage.class);
        Map<String, String> retMap = userCommManage.checkAccountInfo(request.getParameter("accountName"));
        checkFlag = BooleanParser.parse(retMap.get("checkFlag"));
        checkMessage = retMap.get("checkMessage");
        if (!checkFlag)
        {
            request.setAttribute("EMAIL_ERROR", checkMessage);
            forward(request, response, getController().getViewURI(request, AddXxcz.class));
            return;
        }
        OfflineChargeManage chargeManage = serviceSession.getService(OfflineChargeManage.class);
        chargeManage.add(request.getParameter("accountName"),
            BigDecimalParser.parse(request.getParameter("amount")),
            request.getParameter("remark"));
        sendRedirect(request, response, getController().getURI(request, XxczglList.class));
    }
}
