package com.dimeng.p2p.console.servlets.ebq;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.AbstractServlet;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.preservations.ebao.service.PreservationManager;
import com.dimeng.util.parser.IntegerParser;

/**
 * 获取易保全协议保全地址
 * @author dengwenwu
 *
 */
public class ArgeementPreservationView extends AbstractServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        int id = IntegerParser.parse(request.getParameter("id"));
       
		PreservationManager preservationManager = serviceSession.getService(PreservationManager.class);
		String url = preservationManager.getAgreementPreservationUrlById(id, 0);
		if(null != url){
			sendRedirect(request, response, url);
			return;
		}
    }
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
}
