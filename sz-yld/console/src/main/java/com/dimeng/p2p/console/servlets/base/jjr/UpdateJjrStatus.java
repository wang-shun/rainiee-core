package com.dimeng.p2p.console.servlets.base.jjr;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S51.enums.T5128_F05;
import com.dimeng.p2p.console.servlets.base.AbstractBaseServlet;
import com.dimeng.p2p.modules.base.console.service.JjrManage;
import com.dimeng.util.parser.IntegerParser;

//@Right(id = "P2P_C_BASE_UPDATEJJRSTATUS", name = "节假日状态修改")
public class UpdateJjrStatus extends AbstractBaseServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void processGet(HttpServletRequest request,
            HttpServletResponse response, ServiceSession serviceSession)
            throws Throwable {
        processPost(request, response, serviceSession);

    }

    @Override
    protected void processPost(final HttpServletRequest request,
            final HttpServletResponse response,
            final ServiceSession serviceSession) throws Throwable {
        JjrManage manage = serviceSession.getService(JjrManage.class);
        int id = IntegerParser.parse(request.getParameter("F01"));
        T5128_F05 f05 = T5128_F05.parse(request.getParameter("F05"));
        manage.update(id, f05);
        sendRedirect(request, response,
                getController().getURI(request, JjrList.class));
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request,
            HttpServletResponse response, Throwable throwable)
            throws ServletException, IOException {
        getResourceProvider().log(throwable.getMessage());
        if (throwable instanceof SQLException) {
            logger.error(throwable, throwable);
            getController().prompt(request, response, PromptLevel.ERROR,
                    "系统繁忙，请您稍后再试");
            sendRedirect(request, response,
                    getController().getURI(request, UpdateJjrStatus.class));

        } else if (throwable instanceof LogicalException
                || throwable instanceof ParameterException) {
            getController().prompt(request, response, PromptLevel.WARRING,
                    throwable.getMessage());
            sendRedirect(request, response,
                    getController().getURI(request, UpdateJjrStatus.class));
        } else {
            super.onThrowable(request, response, throwable);
            sendRedirect(request, response,
                    getController().getURI(request, UpdateJjrStatus.class));
        }
    }
    
}
