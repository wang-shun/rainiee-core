/*
 * 文 件 名:  Overdue.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoucl
 * 修改时间:  2016年6月6日
 */
package com.dimeng.p2p.console.servlets.statistics.ywtj.yqtj;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S70.entities.T7053;
import com.dimeng.p2p.console.servlets.statistics.AbstractStatisticsServlet;
import com.dimeng.p2p.modules.statistics.console.service.OverdueManage;
import com.dimeng.util.parser.IntegerParser;

/**
 * <逾期数据统计>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2016年6月6日]
 */
@Right(id = "P2P_C_STATISTICS_OVERDUE", name = "逾期数据统计", moduleId = "P2P_C_STATISTICS_YWTJ_YQTJ", order = 1)
public class Overdue extends AbstractStatisticsServlet
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -3816029943735982568L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        int year = IntegerParser.parse(request.getParameter("year"));
        if (year <= 0)
        {
            Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
        }
        OverdueManage manage = serviceSession.getService(OverdueManage.class);
        T7053[] t7053s = manage.getT7053s(year);
        Integer[] options = manage.getT7053Year();
        request.setAttribute("options", options);
        request.setAttribute("t7053s", t7053s);
        request.setAttribute("year", year);
        forwardView(request, response, getClass());
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        getResourceProvider().log(throwable.getMessage());
        if (throwable instanceof SQLException)
        {
            logger.error(throwable, throwable);
            getController().prompt(request, response, PromptLevel.ERROR, "系统繁忙，请您稍后再试");
        }
        else if (throwable instanceof LogicalException || throwable instanceof ParameterException)
        {
            getController().prompt(request, response, PromptLevel.WARRING, throwable.getMessage());
        }
        else
        {
            super.onThrowable(request, response, throwable);
        }
        sendRedirect(request, response, getController().getURI(request, Overdue.class));
    }
}
