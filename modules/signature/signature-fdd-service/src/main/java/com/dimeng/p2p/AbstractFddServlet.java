package com.dimeng.p2p;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.servlet.AbstractServlet;
import com.dimeng.framework.service.ServiceSession;

public abstract class AbstractFddServlet extends AbstractServlet
{
    
    private static final long serialVersionUID = 1L;
    
    protected static final String charSet = "utf-8";
    
    public static final Logger logger = Logger.getLogger(AbstractFddServlet.class);
    
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    public static final String PAGING_CURRENT = "paging.current";

    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    protected void createNoticeMessagePage(String retUrl, String noticeMessage, HttpServletResponse response)
        throws Throwable
    {
        StringBuilder builder = new StringBuilder();
        builder.append("<html>");
        builder.append("<head>");
        builder.append("<script type=\"text/javascript\">");
        builder.append("    function openURL(){");
        builder.append("    alert(\"");
        builder.append(noticeMessage);
        builder.append("\");");
        builder.append("    location.replace(\"");
        builder.append(retUrl);
        builder.append("\"  );");
        builder.append("    }");
        builder.append("</script>");
        builder.append("</head>");
        builder.append("<body onload=\"openURL();\">");
        builder.append("</body>");
        builder.append("</html>");
        doPrintWriter(response, builder.toString(), false);
        
    }
    
    public void doPrintWriter(HttpServletResponse response, String message)
        throws Throwable
    {
        StringBuilder builder = new StringBuilder();
        builder.append("<html>");
        builder.append("<head>");
        builder.append("<script type=\"text/javascript\">");
        builder.append("    function openURL(){");
        builder.append("    alert(\"");
        builder.append(message);
        builder.append("\");");
        builder.append("    window.close();");
        builder.append("    }");
        builder.append("</script>");
        builder.append("</head>");
        builder.append("<body onload=\"openURL();\">");
        builder.append("</body>");
        builder.append("</html>");
        PrintWriter writer = response.getWriter();
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        writer.print(builder.toString());
        writer.flush();
        writer.close();
    }
    
    protected void doPrintWriter(HttpServletResponse response, String location, boolean printTag)
        throws Throwable
    {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        if (printTag)
        {
            logger.info(String.format("%s,发送请求参数：%s", getSystemDateTime(), location));
        }
        try
        {
            writer.print(location);
        }
        catch (Exception e)
        {
            logger.error(e.toString());
        }
        finally
        {
            writer.flush();
            writer.close();
        }
    }
    
    protected String getSystemDateTime()
    {
        return sdf.format(System.currentTimeMillis());
    }

    /**
     * {@inheritDoc}
     * 验证会话是否超时
     */
    @Override
    protected boolean mustAuthenticated()
    {
        return true;
    }
    
    
    protected ConfigureProvider getConfigureProvider()
    {
        return getResourceProvider().getResource(ConfigureProvider.class);
    }
}
