package com.dimeng.p2p.console.servlets.base.optsettings.task;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S66.entities.T6601;
import com.dimeng.p2p.S66.enums.T6601_F06;
import com.dimeng.p2p.console.config.util.HttpClientUtil;
import com.dimeng.p2p.console.servlets.base.AbstractBaseServlet;
import com.dimeng.p2p.service.TaskManage;
import com.dimeng.p2p.variables.defines.URLVariable;
import com.dimeng.util.StringHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author heluzhu
 *
 */
@Right(id = "P2P_C_BASE_TASK_UPDATE", name = "修改任务", moduleId = "P2P_C_BASE_OPTSETTINGS_TASK")
public class UpdateStatus extends AbstractBaseServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        sendRedirect(request, response,
            getController().getURI(request, TaskList.class));
    }
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        
        try
        {
            int id = StringHelper.isEmpty(request.getParameter("id"))? 0 : Integer.parseInt(request.getParameter("id"));
            TaskManage manage = serviceSession.getService(TaskManage.class);
            if (id <= 0)
            {
                throw new ParameterException("该任务不存在");
            }
            String status = request.getParameter("status");
            T6601 t6601 = manage.queryById(id);
            t6601.F06 = T6601_F06.parse(status);
            logger.info("===========开始修改任务状态ID:" + id + "===========" + status);
            manage.updateInfo(t6601);
            Map<String,String> param = new HashMap<String,String>();
            param.put("id", id + "");
            param.put("method", "updateStatus");
            final ConfigureProvider configureProvider = getResourceProvider().getResource(ConfigureProvider.class);
            String url = configureProvider.format(URLVariable.EDITJOB_URL);
            HttpClientUtil.doPost(url, param, "utf-8");
            logger.info("===========结束修改任务状态ID:"+id+"==========="+status);

            sendRedirect(request, response,
                getController().getURI(request, TaskList.class));
        }
        catch (Throwable throwable)
        {
            logger.error(throwable, throwable);
            if (throwable instanceof ParameterException || throwable instanceof LogicalException)
            {
                getController().prompt(request, response, PromptLevel.WARRING, throwable.getMessage());
                processGet(request, response, serviceSession);
            }
            else
            {
                getController().prompt(request, response, PromptLevel.ERROR, throwable.getMessage());
                processGet(request, response, serviceSession);
            }
        }
        
    }
}
