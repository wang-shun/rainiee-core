/*
 * 文 件 名:  ExplainScoreRule.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoucl
 * 修改时间:  2015年12月14日
 */
package com.dimeng.p2p.console.servlets.base.malloptsettings.scorerule;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S63.entities.T6354;
import com.dimeng.p2p.console.servlets.AbstractMallServlet;
import com.dimeng.p2p.repeater.score.SetScoreManage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <积分规则说明>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2015年12月14日]
 */
@Right(id = "P2P_C_BASE_EXPLAINSCORERULE", isMenu = true, name = "积分规则说明", moduleId = "P2P_C_BASE_MALLOPTSETTINGS_SCORERULE")
public class ExplainScoreRule extends AbstractMallServlet
{

    /**
     * 注释内容
     */
    private static final long serialVersionUID = -1636015212326243385L;
    
    @Override
    protected void processPost(final HttpServletRequest request,
            final HttpServletResponse response,
            final ServiceSession serviceSession) throws Throwable {
        
        try {
            String F02 = request.getParameter("F02");
            String F03 = request.getParameter("F03");
            SetScoreManage manage = serviceSession.getService(SetScoreManage.class);
            T6354 t6354 = manage.getT6354();
            if (t6354 == null) {
                manage.addT6354(F02,F03);
            } else {
                manage.updateT6354(t6354.F01,F02,F03);
            }
            request.setAttribute("F02", F02);
            request.setAttribute("F03", F03);
            prompt(request, response, PromptLevel.INFO, "保存成功");
            forwardView(request, response, getClass());
        } catch (ParameterException | LogicalException e) {
            prompt(request, response, PromptLevel.ERROR, e.getMessage());
            processGet(request, response, serviceSession);
        }

    }

    @Override
    protected void processGet(HttpServletRequest request,
            HttpServletResponse response, ServiceSession serviceSession)
            throws Throwable {
        SetScoreManage manage = serviceSession.getService(SetScoreManage.class);
        T6354 t6354 = manage.getT6354();
        if(null != t6354){
            request.setAttribute("F02", t6354.F02);
            request.setAttribute("F03", t6354.F03);
        }
        forwardView(request, response, getClass());
    }
    
}
