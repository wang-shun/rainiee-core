package com.dimeng.p2p.console.servlets;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.session.Session;
import com.dimeng.framework.http.session.SessionManager;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.p2p.common.ResourceProviderUtil;

/**
 * 登录拦截
 * @author heluzhu
 *
 */
public class LoginFilter implements Filter
{
    
    private String encoding;
    
    private String[] unCatch;
    
    @Override
    public void destroy()
    {
        
    }
    
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
        throws IOException, ServletException
    {
        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse)res;
        req.setCharacterEncoding(encoding);
        res.setContentType("text/html;charset=" + encoding);
        ResourceProvider resourceProvider = ResourceProviderUtil.getResourceProvider();
        Session dimengSession = resourceProvider.getResource(SessionManager.class).getSession(request, response, true);
        boolean flag = true;
        for (int i = 0; i < unCatch.length; i++)
        {
            if (request.getRequestURI().contains(unCatch[i]))
            {
                flag = false;
            }
        }
        
        if (flag)
        {
            if (dimengSession == null || !dimengSession.isAuthenticated())
            {
                res.getWriter().print("<script>window.parent.location='/console/login.htm'</script>");
                return;
            }
            
            if (dimengSession != null && dimengSession.isAuthenticated() && dimengSession.isOtherLogin())
            {
                res.getWriter()
                    .print("<script type='text/javascript'>window.parent.location='/console/login.htm?errormsg=other'</script>");
                return;
            }
            
        }
        chain.doFilter(req, res);
    }
    
    @Override
    public void init(FilterConfig config)
        throws ServletException
    {
        encoding = config.getInitParameter("encoding");
        unCatch = config.getInitParameter("unCatch").split(",");
    }
    
}
