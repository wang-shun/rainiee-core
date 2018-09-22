/*
 * 文 件 名:  ConditionSet.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoucl
 * 修改时间:  2015年12月16日
 */
package com.dimeng.p2p.console.servlets.base.malloptsettings.condition;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S63.entities.T6353;
import com.dimeng.p2p.S63.enums.T6353_F05;
import com.dimeng.p2p.console.servlets.AbstractMallServlet;
import com.dimeng.p2p.repeater.score.SetScoreManage;
import com.dimeng.p2p.repeater.score.entity.SetCondition;
import com.dimeng.p2p.variables.defines.MallVariavle;
import com.dimeng.util.parser.BooleanParser;

/**
 * <商城筛选条件设置>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2015年12月16日]
 */
@Right(id = "P2P_C_BASE_VIEWSETCONDITION", isMenu = true, name = "商城筛选条件设置", moduleId = "P2P_C_BASE_MALLOPTSETTINGS_CONDITION")
public class ViewSetCondition extends AbstractMallServlet
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 116961920003769878L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        
        SetScoreManage manage = serviceSession.getService(SetScoreManage.class);
        //积分范围
        List<T6353> scoreRangeList = manage.getT6353List(T6353_F05.score.name());
        request.setAttribute("scoreRangeList", scoreRangeList);
        ResourceProvider resourceProvider = getResourceProvider();
        ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        if (BooleanParser.parse(configureProvider.getProperty(MallVariavle.ALLOWS_THE_BALANCE_TO_BUY)))
        {
            //金额范围
            List<T6353> amountRangeList = manage.getT6353List(T6353_F05.amount.name());
            request.setAttribute("amountRangeList", amountRangeList);
        }
        forwardView(request, response, getClass());
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        SetScoreManage manage = serviceSession.getService(SetScoreManage.class);
        SetCondition setCondition = new SetCondition();
        setCondition.minScore = request.getParameterValues("minScore");
        setCondition.maxScore = request.getParameterValues("maxScore");
        setCondition.minAmount = request.getParameterValues("minAmount");
        setCondition.maxAmount = request.getParameterValues("maxAmount");
        manage.updateT353(setCondition);
        prompt(request, response, PromptLevel.INFO, "保存成功");
        sendRedirect(request, response, getController().getURI(request, ViewSetCondition.class));
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
            sendRedirect(request, response, getController().getURI(request, ViewSetCondition.class));
            
        }
        else if (throwable instanceof LogicalException || throwable instanceof ParameterException)
        {
            getController().prompt(request, response, PromptLevel.WARRING, throwable.getMessage());
            sendRedirect(request, response, getController().getURI(request, ViewSetCondition.class));
        }
        else
        {
            super.onThrowable(request, response, throwable);
            sendRedirect(request, response, getController().getURI(request, ViewSetCondition.class));
        }
    }
    
}
