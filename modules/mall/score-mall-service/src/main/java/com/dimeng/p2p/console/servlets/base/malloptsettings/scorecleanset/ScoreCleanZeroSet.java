/*
 * 文 件 名:  CleanZeroSet.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoucl
 * 修改时间:  2015年12月14日
 */
package com.dimeng.p2p.console.servlets.base.malloptsettings.scorecleanset;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S61.entities.T6106;
import com.dimeng.p2p.S63.entities.T6357;
import com.dimeng.p2p.console.servlets.AbstractMallServlet;
import com.dimeng.p2p.repeater.score.SetScoreManage;
import com.dimeng.util.parser.DateTimeParser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <积分清零设置>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2015年12月14日]
 */
@Right(id = "P2P_C_BASE_SCORECLEANZEROSET", isMenu = true, name = "积分清零设置", moduleId = "P2P_C_BASE_MALLOPTSETTINGS_SCORECLEANSET", order = 0)
public class ScoreCleanZeroSet extends AbstractMallServlet
{

    /**
     * 注释内容
     */
    private static final long serialVersionUID = -3102170769739481658L;
    
    @Override
    protected void processGet(HttpServletRequest request,
            HttpServletResponse response, ServiceSession serviceSession)
            throws Throwable {
        SetScoreManage manager = serviceSession.getService(SetScoreManage.class);
        String startTime = getStartTime(manager);
        request.setAttribute("startTime", startTime);
        forwardView(request, response, getClass());
    }

    @Override
    protected void processPost(final HttpServletRequest request,
            final HttpServletResponse response,
            final ServiceSession serviceSession) throws Throwable {
        SetScoreManage manager = serviceSession.getService(SetScoreManage.class);
        String startTime = getStartTime(manager);
        String type = request.getParameter("type");
        if("cleanZero".equals(type)){
            manager.cleanUpScore(startTime, request.getParameter("endTime"));
        }
        processGet(request, response, serviceSession);
    }

    private String getStartTime(SetScoreManage manager) throws Throwable{
        String startTime = "";
        T6357 t6357 = manager.getT6357();
        if(null == t6357){
            T6106 t6106 = manager.getT6106();
            if(null != t6106){
                startTime =  DateTimeParser.format(t6106.F04,"yyyy-MM-dd");
            }
        }else{
            startTime =  DateTimeParser.format(t6357.F04,"yyyy-MM-dd");
        }
        return startTime;
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        prompt(request, response, PromptLevel.ERROR, throwable.getMessage());
        forwardView(request, response, getClass());
    }
    
}
