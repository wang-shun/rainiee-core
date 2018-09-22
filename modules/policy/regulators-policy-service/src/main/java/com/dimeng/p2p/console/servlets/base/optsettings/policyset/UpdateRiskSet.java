/*
 * 文 件 名:  UpdateRiskValue.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  God
 * 修改时间:  2016年3月9日
 */
package com.dimeng.p2p.console.servlets.base.optsettings.policyset;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S61.entities.T6148;
import com.dimeng.p2p.console.servlets.AbstractRankServlet;
import com.dimeng.p2p.repeater.policy.RiskAssessManage;
import com.dimeng.p2p.repeater.policy.entity.RiskSetCondition;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * <风险评估设置>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2016年3月9日]
 */
@Right(id = "P2P_C_BASE_UPDATERISKSET", isMenu = true , name = "风险评估设置", moduleId = "P2P_C_BASE_OPTSETTINGS_POLICYSET")
public class UpdateRiskSet extends AbstractRankServlet
{

    /**
     * 注释内容
     */
    private static final long serialVersionUID = -1535955061648423112L;
    
    @Override
    protected void processGet(HttpServletRequest request,
            HttpServletResponse response, ServiceSession serviceSession)
            throws Throwable {
        RiskAssessManage manage = serviceSession.getService(RiskAssessManage.class);
        List<T6148> t6148List = manage.getT6148List();
        request.setAttribute("t6148List", t6148List);
        forwardView(request, response, getClass());
    }

    @Override
    protected void processPost(final HttpServletRequest request,
            final HttpServletResponse response,
            final ServiceSession serviceSession) throws Throwable {
        
        RiskAssessManage manage = serviceSession.getService(RiskAssessManage.class);
        RiskSetCondition riskSetCondition = new RiskSetCondition();
        riskSetCondition.parse(request);
        manage.updateT6148s(riskSetCondition);
        prompt(request, response, PromptLevel.INFO, "保存成功");
        sendRedirect(request, response, getController().getURI(request, UpdateRiskSet.class));
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request,
            HttpServletResponse response, Throwable throwable)
            throws ServletException, IOException {
        getResourceProvider().log(throwable.getMessage());
        if (throwable instanceof SQLException) {
            logger.error(throwable, throwable);
            getController().prompt(request, response, PromptLevel.ERROR,
                    "系统繁忙，请您稍后再试");
            sendRedirect(request, response,
                    getController().getURI(request, UpdateRiskSet.class));

        } else if (throwable instanceof LogicalException
                || throwable instanceof ParameterException) {
            getController().prompt(request, response, PromptLevel.WARRING,
                    throwable.getMessage());
            sendRedirect(request, response,
                    getController().getURI(request, UpdateRiskSet.class));
        } else {
            super.onThrowable(request, response, throwable);
            sendRedirect(request, response,
                    getController().getURI(request, UpdateRiskSet.class));
        }
    }
    
}
