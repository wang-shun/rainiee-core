/*
 * 文 件 名:  PageEhCacheFilter.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  huanggang
 * 修改时间:  2015年10月30日
 */
package com.dimeng.p2p.front.servlets;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.ehcache.constructs.web.AlreadyCommittedException;
import net.sf.ehcache.constructs.web.AlreadyGzippedException;
import net.sf.ehcache.constructs.web.filter.FilterNonReentrantException;
import net.sf.ehcache.constructs.web.filter.SimplePageCachingFilter;

import org.apache.commons.lang.StringUtils;

/**
 * 页面缓存EhCahce实现
 * <功能详细描述>
 * 
 * @author  huanggang
 * @version  [版本号, 2015年10月30日]
 */
public class PageEhCacheFilter extends SimplePageCachingFilter
{
    
    private final static String FILTER_URL_PATTERNS = "patterns";
    
    private static String[] cacheURLs;
    
    private void init()
        throws Exception
    {
        String patterns = filterConfig.getInitParameter(FILTER_URL_PATTERNS);
        cacheURLs = StringUtils.split(patterns, ",");
    }
    
    @Override
    protected void doFilter(final HttpServletRequest request, final HttpServletResponse response,
        final FilterChain chain)
        throws AlreadyGzippedException, AlreadyCommittedException, FilterNonReentrantException, Exception
    {
        if (cacheURLs == null)
        {
            init();
        }
        
        String url = request.getRequestURI();
        boolean flag = false;
        if (null != cacheURLs && cacheURLs.length > 0)
        {
            for (String cacheURL : cacheURLs)
            {
                if (url.contains(cacheURL.trim()))
                {
                    flag = true;
                    break;
                }
            }
        }
        // 如果包含我们要缓存的url 就缓存该页面，否则执行正常的页面转向 
        if (flag)
        {
            String query = request.getQueryString();
            if (query != null)
            {
                query = "?" + query;
            }
            System.out.println("当前请求被缓存：" + url + query);
            super.doFilter(request, response, chain);
        }
        else
        {
            chain.doFilter(request, response);
        }
    }
    
    //    private boolean headerContains(final HttpServletRequest request, final String header, final String value)
    //    {
    //        logRequestHeaders(request);
    //        Enumeration accepted = request.getHeaders(header);
    //        try
    //        {
    //            while (accepted.hasMoreElements())
    //            {
    //                final String headerValue = (String)accepted.nextElement();
    //                if (headerValue.indexOf(value) != -1)
    //                {
    //                    return true;
    //                }
    //            }
    //        }
    //        catch (NoSuchElementException ee)
    //        {
    //            return false;
    //        }
    //        return false;
    //    }
    //    
    //    /** 
    //     * @see net.sf.ehcache.constructs.web.filter.Filter#acceptsGzipEncoding(javax.servlet.http.HttpServletRequest) 
    //     * <b>function:</b> 兼容ie6/7 gzip压缩 
    //     * @author hoojo 
    //     * @createDate 2012-7-4 上午11:07:11 
    //     */
    //    @Override
    //    protected boolean acceptsGzipEncoding(HttpServletRequest request)
    //    {
    //        boolean ie6 = headerContains(request, "User-Agent", "MSIE 6.0");
    //        boolean ie7 = headerContains(request, "User-Agent", "MSIE 7.0");
    //        return acceptsEncoding(request, "gzip") || ie6 || ie7;
    //    }
}
