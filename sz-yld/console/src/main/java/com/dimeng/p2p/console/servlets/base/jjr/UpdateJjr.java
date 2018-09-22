package com.dimeng.p2p.console.servlets.base.jjr;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S51.entities.T5128;
import com.dimeng.p2p.console.servlets.base.AbstractBaseServlet;
import com.dimeng.p2p.modules.base.console.service.JjrManage;
import com.dimeng.util.parser.IntegerParser;

//@Right(id = "P2P_C_BASE_UPDATEJJR", name = "节假日信息修改")
public class UpdateJjr extends AbstractBaseServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void processGet(HttpServletRequest request,
            HttpServletResponse response, ServiceSession serviceSession)
            throws Throwable {
        JjrManage jjrManage = serviceSession.getService(JjrManage.class);
        int id = IntegerParser.parse(request.getParameter("id"));
        T5128 t5128 = jjrManage.get(id);
        request.setAttribute("entity", t5128);
        forwardView(request, response, getClass());

    }

    @Override
    protected void processPost(final HttpServletRequest request,
            final HttpServletResponse response,
            final ServiceSession serviceSession) throws Throwable {
        JjrManage manage = serviceSession.getService(JjrManage.class);
        T5128 entity = new T5128();
        entity.parse(request);
        manage.update(entity);
        sendRedirect(request, response,
                getController().getURI(request, JjrList.class));
    }
    
    
}
