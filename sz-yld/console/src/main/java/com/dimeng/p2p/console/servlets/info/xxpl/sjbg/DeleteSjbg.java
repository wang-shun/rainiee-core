package com.dimeng.p2p.console.servlets.info.xxpl.sjbg;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.console.servlets.info.xxpl.AbstractXxplServlet;
import com.dimeng.p2p.modules.base.console.service.ArticleManage;
import com.dimeng.util.parser.IntegerParser;

/**
 * 
 * <审计报告>
 * <删除>
 * 
 * @author  liulixia
 * @version  [版本号, 2018年2月6日]
 */
@Right(id = "P2P_C_INFO_XXPL_MENU", name = "信息披露", moduleId = "P2P_C_INFO_XXPL", order = 0)
public class DeleteSjbg extends AbstractXxplServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processGet(request, response, serviceSession);
    }
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        ArticleManage manage = serviceSession.getService(ArticleManage.class);
        manage.delete(IntegerParser.parseArray(request.getParameterValues("id")));
        sendRedirect(request, response, getController().getURI(request, SearchSjbg.class));
    }
    
}
