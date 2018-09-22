package com.dimeng.p2p.console.servlets.account.riskresult.policy;/*
 * 文 件 名:  RiskResultDetail.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  heluzhu
 * 修改时间: 2016/3/10
 */

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.console.servlets.AbstractRankServlet;
import com.dimeng.p2p.repeater.policy.RiskQuesManage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RiskResultDetail extends AbstractRankServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession) throws Throwable {
        processPost(request,response,serviceSession);
    }

    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession) throws Throwable {
        RiskQuesManage manage = serviceSession.getService(RiskQuesManage.class);
        int riskId = Integer.parseInt(request.getParameter("riskId"));
        request.setAttribute("result",manage.queryRiskDetail(riskId));
        forwardView(request,response,getClass());
    }
}
