package com.dimeng.p2p.console.servlets.base.optsettings.task;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.console.config.util.HttpClientUtil;
import com.dimeng.p2p.console.servlets.base.AbstractBaseServlet;
import com.dimeng.p2p.variables.defines.URLVariable;
import com.dimeng.util.StringHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 手动执行定时任务
 * <功能详细描述>
 * 
 * @author  heluzhu
 * @version  [版本号, 2015年10月28日]
 */
//@Right(id = "P2P_C_BASE_TASK_EXECUTE", name = "手动执行任务", moduleId = "P2P_C_BASE_OPTSETTINGS_TASK")
public class ExecuteTask extends AbstractBaseServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request,response,serviceSession);
    }
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        PrintWriter out = response.getWriter();
        try
        {
            int id = StringHelper.isEmpty(request.getParameter("id"))? 0 : Integer.parseInt(request.getParameter("id"));
            if (id <= 0)
            {
                throw new ParameterException("该任务不存在");
            }
            Map<String,String> param = new HashMap<String,String>();
            param.put("id",id+"");
            final ConfigureProvider configureProvider = getResourceProvider().getResource(ConfigureProvider.class);
            String url = configureProvider.format(URLVariable.EXECUTEJOB_URL);
            String result = HttpClientUtil.doPost(url, param, "utf-8");
            out.write(result);
        }
        catch (Throwable throwable)
        {
            logger.error(throwable, throwable);
            if (throwable instanceof ParameterException || throwable instanceof LogicalException)
            {
                out.write("{code:1,msg:'"+throwable.getMessage()+"'}");
            }
            else
            {
                out.write("{code:1,msg:'" + throwable.getMessage() + "'}");
            }
        }
        out.close();
        return;
    }
}
