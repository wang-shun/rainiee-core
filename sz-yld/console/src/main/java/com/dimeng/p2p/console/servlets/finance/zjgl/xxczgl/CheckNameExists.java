/*
 * 文 件 名:  CheckNameExists.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  God
 * 修改时间:  2016年2月26日
 */
package com.dimeng.p2p.console.servlets.finance.zjgl.xxczgl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.console.servlets.finance.AbstractFinanceServlet;
import com.dimeng.p2p.modules.account.console.service.OfflineChargeManage;

/**
 * <检查用户名是否存在>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2016年2月26日]
 */
public class CheckNameExists extends AbstractFinanceServlet
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 6055526322616470253L;

    @Override
    protected void processGet(HttpServletRequest request,
            HttpServletResponse response, ServiceSession serviceSession)
            throws Throwable {
        processPost(request, response, serviceSession);
    }

    @Override
    protected void processPost(HttpServletRequest request,
            HttpServletResponse response, ServiceSession serviceSession)
            throws Throwable {
        response.setContentType("text/html;charset="
                + getResourceProvider().getCharset());
        String accountName = request.getParameter("accountName");
        OfflineChargeManage userManager = serviceSession.getService(OfflineChargeManage.class);
        boolean isResult = userManager.isAccountExists(accountName);
        response.getWriter().println(isResult);
        response.getWriter().flush();
    }
}
