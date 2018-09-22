package com.dimeng.p2p.console.servlets.base.optsettings.constant;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.entity.VariableBean;
import com.dimeng.framework.config.service.VariableManage;
import com.dimeng.framework.config.service.VariableManage.VariableQuery;
import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.console.servlets.system.AbstractSystemServlet;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;

@Right(id = "P2P_C_BASE_CONSTANTLIST", isMenu = true, name = "平台常量管理", moduleId = "P2P_C_BASE_OPTSETTINGS_CONSTANT", order = 0)
public class ConstantList extends AbstractSystemServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        VariableManage variableManage = serviceSession.getService(VariableManage.class);
        variableManage.synchronize();
        final int decode = IntegerParser.parse(request.getParameter("decode"), 0);
        PagingResult<VariableBean> result = variableManage.search(new VariableQuery()
        {
            
            @Override
            public String getValue()
            {
                return null;
            }
            
            @Override
            public String getType()
            {
                return request.getParameter("type");
            }
            
            @Override
            public String getKey()
            {
                return request.getParameter("key");
            }
            
            @Override
            public String getDescription()
            {
                String des = request.getParameter("des");
                if (decode > 0 && !StringHelper.isEmpty(des))
                {
                    try
                    {
                        des = URLDecoder.decode(des.replace("^", "%"), getResourceProvider().getCharset());
                    }
                    catch (UnsupportedEncodingException e)
                    {
                        logger.error(e, e);
                    }
                }
                return des;
            }
        }, new Paging()
        {
            
            @Override
            public int getSize()
            {
                return 10;
            }
            
            @Override
            public int getCurrentPage()
            {
                return IntegerParser.parse(request.getParameter(PAGING_CURRENT));
            }
        });
        request.setAttribute("result", result);
        forwardView(request, response, ConstantList.class);
    }
}
