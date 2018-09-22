/*
 * 文 件 名:  SetScore.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoucl
 * 修改时间:  2015年12月11日
 */
package com.dimeng.p2p.console.servlets.base.malloptsettings.scoreset;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.console.servlets.AbstractMallServlet;
import com.dimeng.p2p.repeater.score.SetScoreManage;
import com.dimeng.p2p.repeater.score.entity.SetScore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * <积分设置>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2015年12月11日]
 */
@Right(id = "P2P_C_BASE_VIEWSETSCORE", isMenu = true, name = "积分设置", moduleId = "P2P_C_BASE_MALLOPTSETTINGS_SCORESET")
public class ViewSetScore extends AbstractMallServlet
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 332201206470430690L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        
        SetScoreManage manage = serviceSession.getService(SetScoreManage.class);
        SetScore setScore = manage.getSetScore();
        request.setAttribute("setScore", setScore);
        forwardView(request, response, getClass());
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        
        SetScoreManage manage = serviceSession.getService(SetScoreManage.class);
        SetScore setScore = new SetScore();
        setScore.parse(request);
        manage.updateSetScore(setScore);
        setScore = manage.getSetScore();
        request.setAttribute("setScore", setScore);
        prompt(request, response, PromptLevel.INFO, "保存成功");
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
        sendRedirect(request, response, getController().getURI(request, ViewSetScore.class));
    }
    
}
