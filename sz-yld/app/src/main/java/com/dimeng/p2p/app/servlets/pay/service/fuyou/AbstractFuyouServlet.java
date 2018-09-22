package com.dimeng.p2p.app.servlets.pay.service.fuyou;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.app.servlets.AbstractAppBaseServlet;

public abstract class AbstractFuyouServlet extends AbstractAppBaseServlet
{
    
    private static final long serialVersionUID = 1L;
    
    protected static final String charSet = "utf-8";
    
    public static Logger logger = Logger.getLogger(AbstractFuyouServlet.class);
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    /**
     * {@inheritDoc}
     * 验证会话是否超时
     */
    @Override
    protected boolean mustAuthenticated()
    {
        return false;
    }
    
    /**
     * 向第三方平台发送请求
     * <From表表单>
     * @param param 参数
     * @param formUrl 请求地址
     * @param response
     * @param printTag  是否打印
     * @throws Throwable
     */
    public void sendHttp(Map<String, String> param, String formUrl, HttpServletResponse response, boolean printTag)
        throws Throwable
    {
        //构建表单元素
        String formStr = createUrl(param, formUrl);
        if (printTag)
            logger.info("发送富友请求参数：" + formStr);
        response.setContentType("text/html");
        response.setCharacterEncoding(charSet);
        response.getWriter().write(formStr);
        response.getWriter().close();
    }
    
    /**
     * 构建表单提交元素
     * @param param 元素
     * @param formUrl 请求地址
     * @return
     * @throws Throwable
     */
    public String createUrl(Map<String, String> param, String formUrl)
        throws Throwable
    {
        StringBuilder builder = new StringBuilder();
        builder.append("<form action='");
        builder.append(formUrl);
        builder.append("' method=\"post\">");
        for (String key : param.keySet())
        {
            builder.append("<input type=\"hidden\" name=\"" + key + "\" value='");
            builder.append(param.get(key));
            builder.append("' />");
        }
        builder.append("</form>");
        builder.append("<script type=\"text/javascript\">");
        builder.append("document.forms[0].submit();");
        builder.append("</script>");
        return builder.toString();
    }
    
    /**
     * 提示信息
     * <功能详细描述>
     * @param retUrl 
     * @param noticeMessage
     * @param response
     * @throws Throwable
     */
    protected void createNoticeMessagePage(String retUrl, String noticeMessage, HttpServletResponse response)
        throws Throwable
    {
        StringBuilder builder = new StringBuilder();
        builder.append("<html>");
        builder.append("<head>");
        builder.append("<script type=\"text/javascript\">");
        builder.append("    function openURL(){");
        builder.append("    alert(\"" + noticeMessage + "\");");
        builder.append("    location.replace(\"");
        builder.append(retUrl);
        builder.append("\"  );");
        builder.append("    }");
        builder.append("</script>");
        builder.append("</head>");
        builder.append("<body onload=\"openURL();\">");
        builder.append("</body>");
        builder.append("</html>");
        response.setContentType("text/html");
        response.setCharacterEncoding(charSet);
        response.getWriter().write(builder.toString());
        response.getWriter().close();
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
    
    /**
     * 金额格式化——HSP
     * <单位分>
     * @param money
     * @return
     */
    public String getAmt(BigDecimal money)
    {
        DecimalFormat format = new DecimalFormat("0.00");
        String amt = format.format(money);
        if ("0.00".equals(amt))
        {
            return "0";
        }
        return formatAmt(amt);
    }
    
    /**
     * 金额格式化（单位：分）
     * 
     * @作者：何石平 （20151012）
     * @param amt
     *            金额
     * @return String 格式化后
     */
    protected String formatAmt(String amt)
    {
        int i = amt.indexOf(".");
        if (i > 0)
        {
            String z = amt.substring(0, i);
            String f = amt.substring(i + 1);
            if (f.length() > 2)
            {
                f = f.substring(0, 2);
            }
            else if (f.length() == 0)
            {
                f = f + "00";
            }
            else if (f.length() == 1)
            {
                f = f + "0";
            }
            amt = z + f;
        }
        else
        {
            amt = amt + "00";
        }
        return amt;
    }
    
    /**
     * 金额格式化（单位：元）
     * 
     * @param amt 金额
     * @return String 格式化后
     */
    protected String formatAmtRet(String amt)
    {
        int i = amt.length();
        if (i > 1)
        {
            String z = amt.substring(0, i - 2);
            String f = amt.substring(i - 2);
            
            if (i == 2)
            {
                amt = "0." + f;
            }
            else
            {
                amt = z + "." + f;
            }
        }
        return amt;
    }
    
    /**
     * 接收from表单参数
     * <功能详细描述>
     * @param request
     * @return
     */
    public Map<String, String> reversRequest(HttpServletRequest request)
    {
        try
        {
            request.setCharacterEncoding(charSet);
        }
        catch (UnsupportedEncodingException e)
        {
            logger.error(e);
        }
        Map<String, String> map = new HashMap<String, String>();
        Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements())
        {
            String name = names.nextElement();
            String value = request.getParameter(name);
            map.put(name, value);
        }
        return map;
    }
}
