package com.dimeng.p2p;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.servlet.AbstractServlet;
import com.dimeng.framework.http.servlet.Controller;
import com.dimeng.framework.http.session.authentication.AuthenticationException;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.variables.defines.URLVariable;
import com.dimeng.util.StringHelper;

public abstract class AbstractFuyouServlet extends AbstractServlet
{
    
    private static final long serialVersionUID = 1L;
    
    protected static final String charSet = "utf-8";
    
    public static final Logger logger = Logger.getLogger(AbstractFuyouServlet.class);
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        ResourceProvider resourceProvider = getResourceProvider();
        if (throwable instanceof AuthenticationException)
        {
            Controller controller = getController();
            controller.redirectLogin(request,
                response,
                resourceProvider.getResource(ConfigureProvider.class).format(URLVariable.LOGIN));
        }
        else
        {
            resourceProvider.log(throwable);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
    
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
        return true;
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
        {
        }
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
        logger.info("富友FORM表单请求参数：".concat(builder.toString()));
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
            Pattern pattern = Pattern.compile("\\s*|\t|\r|\n");
            Matcher matcher = pattern.matcher(str);
            dest = matcher.replaceAll("");
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
        else
        {
            amt = "0.0" + amt;
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
    
    protected ConfigureProvider getConfigureProvider()
    {
        return getResourceProvider().getResource(ConfigureProvider.class);
    }
    
    /**
     * http请求
     * @param apiUrl 请求url
     * @param data 数据
     * @param headers 头部信息
     * @param encoding 编码 
     * @return
     */
    public static String sendPOST(String apiUrl, String data, LinkedHashMap<String, String> headers, String encoding)
    {
        StringBuffer strBuffer = null;
        try
        {
            // 建立连接
            URL url = new URL(apiUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            // 需要输出
            httpURLConnection.setDoOutput(true);
            // 需要输入
            httpURLConnection.setDoInput(true);
            // 不允许缓存
            httpURLConnection.setUseCaches(false);
            
            httpURLConnection.setRequestMethod("POST");
            // 设置Headers
            if (null != headers)
            {
                for (String key : headers.keySet())
                {
                    httpURLConnection.setRequestProperty(key, headers.get(key));
                }
            }
            if (StringHelper.isEmpty(encoding))
            {
                encoding = "UTF-8";
            }
            // 连接会话
            httpURLConnection.connect();
            // 建立输入流，向指向的URL传入参数
            DataOutputStream dos = new DataOutputStream(httpURLConnection.getOutputStream());
            // 设置请求参数
            dos.write(data.getBytes(encoding));
            dos.flush();
            dos.close();
            // 获得响应状态
            int http_StatusCode = httpURLConnection.getResponseCode();
            String http_ResponseMessage = httpURLConnection.getResponseMessage();
            if (HttpURLConnection.HTTP_OK == http_StatusCode)
            {
                strBuffer = new StringBuffer();
                String readLine = new String();
                BufferedReader responseReader =
                    new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
                while ((readLine = responseReader.readLine()) != null)
                {
                    strBuffer.append(readLine);
                }
                responseReader.close();
                logger.info("http_StatusCode = " + http_StatusCode + " request_Parameter = " + data);
                return strBuffer.toString();
            }
            else
            {
                logger.error("请求失败！失败原因：" + http_ResponseMessage);
                return "请求失败！失败原因：".concat(http_ResponseMessage);
            }
            
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
