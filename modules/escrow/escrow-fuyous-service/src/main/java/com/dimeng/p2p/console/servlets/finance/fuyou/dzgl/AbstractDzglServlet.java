package com.dimeng.p2p.console.servlets.finance.fuyou.dzgl;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.SecureRandom;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.servlet.AbstractServlet;
import com.dimeng.framework.service.ServiceSession;
import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.util.Config;

public abstract class AbstractDzglServlet extends AbstractServlet
{
    
    protected static final Logger logger = Logger.getLogger(AbstractDzglServlet.class);
    
    private static final long serialVersionUID = 1L;
    
    public static final Producer COMMON_KAPTCHA_PRODUCER;
    
    protected static final SecureRandom RANDOM = new SecureRandom();
    
    public static final String PAGING_CURRENT = "paging.current";
    
    static
    {
        ImageIO.setUseCache(false);
        final Properties props = new Properties();
        props.setProperty("kaptcha.image.height", "70");
        props.put("kaptcha.border", "no");
        props.put("kaptcha.textproducer.font.color", "black");
        props.put("kaptcha.textproducer.char.space", "5");
        COMMON_KAPTCHA_PRODUCER = new Config(props).getProducerImpl();
    }
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response, String msg)
        throws ServletException, IOException
    {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try
        {
            response.setContentType("text/html");
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            if ("OK".equals(msg))
            {
                out.write("OK");
            }
            else
            {
                out.write(msg);
            }
        }
        finally
        {
            out.close();
        }
    }
    
    /**
     * 去占位符\空格\换行——HSP
     * @param str
     * @return
     */
    public String trimBlank(String str)
    {
        String dest = "";
        if (str != null)
        {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }
    
    protected ConfigureProvider getConfigureProvider()
    {
        return getResourceProvider().getResource(ConfigureProvider.class);
    }
}
