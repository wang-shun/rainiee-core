package com.dimeng.p2p.user.servlets.letter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.session.authentication.AuthenticationException;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.account.user.service.LetterManage;
import com.dimeng.p2p.user.servlets.capital.AbstractCapitalServlet;
import com.dimeng.util.parser.IntegerParser;

public class Delete extends AbstractCapitalServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        //String[] ids = request.getParameterValues("letterId");
        try
        {
            String letterId = request.getParameter("letterId");
            String[] ids = letterId.split(";");
            if (ids != null && ids.length > 0)
            {
                LetterManage letterManage = serviceSession.getService(LetterManage.class);
                boolean flag = true;
                int[] idArr = new int[ids.length];
                for (int i = 0; i < ids.length; i++)
                {
                    idArr[i] = IntegerParser.parse(ids[i]);
                    if (letterManage.getT6123(idArr[i]) < 1)
                    {//检查站内信是不是属于当前登陆用户，<1则报错
                        flag = false;
                    }
                }
                if (flag)
                {
                    letterManage.delete(idArr);
                    response.getWriter().print("success");
                }
                else
                {
                    response.getWriter().print("非法请求");
                }
            }
            else
            {
                response.getWriter().print("非法请求");
            }
        }
        catch (Exception e)
        {
            logger.info(e.getMessage());
            response.getWriter().print("非法请求");
        }
        
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        PrintWriter out = response.getWriter();
        if (throwable instanceof AuthenticationException)
        {
            out.print("toLogin");
        }
        out.close();
    }
}
