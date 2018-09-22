package com.dimeng.p2p.console.servlets.base.optsettings.constant;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.entity.VariableBean;
import com.dimeng.framework.config.service.VariableManage;
import com.dimeng.framework.config.service.VariableManage.VariableQuery;
import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.common.HTMLValidator;
import com.dimeng.p2p.console.servlets.system.AbstractSystemServlet;
import com.dimeng.p2p.modules.systematic.console.service.ConstantManage;
import com.dimeng.util.StringHelper;

@Right(id = "P2P_C_BASE_UPDATECONSTANT", name = "平台常量设置编辑", moduleId = "P2P_C_BASE_OPTSETTINGS_CONSTANT")
public class UpdateConstant extends AbstractSystemServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        VariableManage variableManage = serviceSession.getService(VariableManage.class);
        final String key = request.getParameter("key");
        final VariableBean variableBean = variableManage.get(key);
        request.setAttribute("variableBean", variableBean);
        String cur = request.getParameter("cur");
        cur = StringHelper.isEmpty(cur) ? "1" : cur;
        request.setAttribute("cur", cur);
        VariableQuery query = new VariableQuery()
        {
            
            @Override
            public String getValue()
            {
                return null;
            }
            
            @Override
            public String getType()
            {
                String searchType = request.getParameter("searchType");
                searchType = StringHelper.isEmpty(searchType) ? "" : searchType;
                return searchType;
            }
            
            @Override
            public String getKey()
            {
                String searchKey = request.getParameter("searchKey");
                searchKey = StringHelper.isEmpty(searchKey) ? "" : searchKey;
                return searchKey;
            }
            
            @Override
            public String getDescription()
            {
                String searchDes = request.getParameter("searchDes");
                searchDes = StringHelper.isEmpty(searchDes) ? "" : searchDes;
                return searchDes;
            }
        };
        request.setAttribute("variableQuery", query);

        forwardView(request, response, getClass());
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        VariableManage variableManage = serviceSession.getService(VariableManage.class);
        ConstantManage constantManage = serviceSession.getService(ConstantManage.class);
        String key = request.getParameter("key");
        VariableBean variableBean = variableManage.get(key);
        String value = request.getParameter("value");
        // variableManage.synchronize();
        if (HTMLValidator.forbidden(value))
        {
            getController().prompt(request, response, PromptLevel.WARRING, "内容含非法脚本！");
            processGet(request, response, serviceSession);
        }
        else
        {
            try
            {
                value = value.trim();
                variableManage.setProperty(key, value);
                constantManage.addConstantLog(key, variableBean.getDescription(), variableBean.getValue(), value);
                /*sendRedirect(request, response,
                getController().getURI(request, ConstantList.class));*/
                getController().prompt(request, response, PromptLevel.INFO, "常量值修改成功!");
            }
            catch (Throwable t)
            {
                getController().prompt(request, response, PromptLevel.WARRING, t.getMessage());
                //processGet(request, response, serviceSession);
            }
            processGet(request, response, serviceSession);
        }
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        super.onThrowable(request, response, throwable);
        
    }
}
