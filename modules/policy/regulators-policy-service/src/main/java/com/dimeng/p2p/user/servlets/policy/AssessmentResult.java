package com.dimeng.p2p.user.servlets.policy;/*
 * 文 件 名:  RiskAssessment.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  heluzhu
 * 修改时间: 2016/3/9
 */

import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.repeater.policy.RiskQuesManage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AssessmentResult extends AbstractURServlet {
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession) throws Throwable {
        processPost(request, response, serviceSession);
    }

    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession) throws Throwable {
        try {
            RiskQuesManage manage = serviceSession.getService(RiskQuesManage.class);
            request.setAttribute("t6147", manage.getMyRiskResult());
            forwardView(request, response, getClass());
        }catch (Exception e){
            prompt(request, response, PromptLevel.ERROR, e.getMessage());
            logger.error(e, e);
            processGet(request, response, serviceSession);
        }

    }
}
