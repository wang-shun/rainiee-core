/*
 * 文 件 名:  XssFilter.java
 * 版    权:  © 2014 DM. All rights reserved.
 * 描    述:  <描述>
 * 修 改 人:  linxiaolin
 * 修改时间:  2015/2/10
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.dimeng.p2p.user.servlets;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.session.Session;
import com.dimeng.framework.http.session.SessionManager;
import com.dimeng.p2p.common.ResourceProviderUtil;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.StringHelper;

/**
 * 跨站脚本漏洞修复
 * @author linxiaolin
 * @version [1.0, 2015/2/10]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@WebFilter(filterName = "/XssFilter", urlPatterns = "/*", initParams = {
    @WebInitParam(name = "encoding", value = "UTF-8"),
    @WebInitParam(name = "illegalChars", value = "|,',\",\\',\\\",&lt;,>,(,),+,\\\",\",\\,%,and,select")})
public class XssFilter implements Filter
{
    private String encoding;
    
    private String[] illegalChars;
    
    @Override
    public void init(FilterConfig config)
        throws ServletException
    {
        encoding = config.getInitParameter("encoding");
        illegalChars = config.getInitParameter("illegalChars").split(",");
    }
    
    @Override
    public void destroy()
    {
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
        throws IOException, ServletException
    {
        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse)res;
        req.setCharacterEncoding(encoding);
        res.setContentType("text/html;charset=" + encoding);
        String referer = request.getHeader("Referer");
        ConfigureProvider configureProvider =
            ResourceProviderUtil.getResourceProvider().getResource(ConfigureProvider.class);
        String domain = configureProvider.getProperty(SystemVariable.SITE_DOMAIN);
        String thirdDomain = configureProvider.getProperty(SystemVariable.THIRD_DOMAIN);
        String thirdUrl[] = thirdDomain.split(",");
        boolean isHas = false;
        Session dimengSession =
            ResourceProviderUtil.getResourceProvider()
                .getResource(SessionManager.class)
                .getSession(request, response, true);
        
        if (dimengSession != null && dimengSession.isAuthenticated() && dimengSession.isOtherLogin())
        {
            //判断是否ajax请求
            if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With")))
            {
                res.getWriter().print("您的帐号已在另一地点登录，您被迫下线");
            }
            else
            {
                res.getWriter()
                    .print("<script>window.alert('您的帐号已在另一地点登录，您被迫下线');window.location.href='/user/login.html'</script>");
            }
            return;
        }
        
        if (!StringHelper.isEmpty(referer) && dimengSession != null && dimengSession.isAuthenticated())
        {
            for (String url : thirdUrl)
            {
                if (referer.indexOf(url) >= 0)
                {
                    isHas = true;
                    break;
                }
            }
            if (!isHas && referer.indexOf(domain) < 0)
            {
                response.sendRedirect(request.getRequestURI());
                return;
            }
        }
        boolean flag = true;
        String retUrl = configureProvider.getProperty(SystemVariable.UNCATCH_URL);
        if (!StringHelper.isEmpty(retUrl))
        {
            String[] unCatch = retUrl.split(",");
            for (int i = 0; i < unCatch.length; i++)
            {
                if (request.getRequestURI().contains(unCatch[i].trim()))
                {
                    flag = false;
                }
            }
        }
        Enumeration<String> params = req.getParameterNames();
        boolean result = false;
        if (flag)
        {
            for (Enumeration e = params; params.hasMoreElements();)
            {
                
                String thisName = e.nextElement().toString();
                String thisValue = request.getParameter(thisName);
                for (int j = 0; j < illegalChars.length; j++)
                {
                    String illegalChar = illegalChars[j].replaceAll("&lt;", "<");
                    if (thisValue.indexOf(illegalChar) != -1)
                    {
                        result = true;
                    }
                }
            }
        }
        
        if (result)
        {
            res.getWriter().print("<script>window.alert('当前请求中存在非法字符，请重新输入');window.history.go(-1);</script>");
            return;
        }
        chain.doFilter(req, res);
    }
    
}
