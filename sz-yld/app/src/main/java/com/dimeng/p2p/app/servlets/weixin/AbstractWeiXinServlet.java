package com.dimeng.p2p.app.servlets.weixin;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.servlet.AbstractServlet;
import com.dimeng.framework.http.session.authentication.AccesssDeniedException;
import com.dimeng.framework.http.session.authentication.AuthenticationException;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.exception.ServiceNotFoundException;
import com.dimeng.p2p.app.exception.HMDException;
import com.dimeng.p2p.app.exception.SDException;
import com.dimeng.p2p.app.exception.SessionOutException;
import com.dimeng.p2p.app.servlets.ReturnCode;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.weixin.dimengapi.msg.Msg;
import com.dimeng.p2p.weixin.dimengapi.msg.StringMsg;
import com.dimeng.util.StringHelper;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * 
 * 微信
 * 
 * @author  zhoulantao
 * @version  [版本号, 2016年3月31日]
 */
public abstract class AbstractWeiXinServlet extends AbstractServlet
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -730184451905947982L;
    
    protected final Logger logger = Logger.getLogger(getClass());
    
    /**
     * 处理微信服务器验证
     * 
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        logger.info("ComeInAbstractWeiXinServlet-> doGet ->" + request.getServletPath());
        
        try
        {
            // 读取资源文件
            ResourceProvider resourceProvider = getResourceProvider();
            final ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
            if (configureProvider != null)
            {
                // 设置请求参数字符集
                request.setCharacterEncoding(resourceProvider.getCharset());
                
                // 设置响应消息消息头类型及字符集
                response.setContentType("application/json;charset=" + getResourceProvider().getCharset());
            }
            
            // 校验用户权限
            try (ServiceSession serviceSession =
                resourceProvider.getResource(ServiceProvider.class).createServiceSession(null,
                    getController().getPrompter(request)))
            {
                // 执行接口操作
                processGet(request, response, serviceSession);
            }
        }
        catch (final Throwable throwable)
        {
            // 记录异常日志
            logger.error("系统异常", throwable);
            
            // 返回异常信息
            onThrowable(request, response, throwable);
        }
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        logger.info("ComeInAbstractWeiXinServlet-> doPost ->" + request.getServletPath());
        
        try
        {
            // 读取资源文件
            ResourceProvider resourceProvider = getResourceProvider();
            final ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
            if (configureProvider != null)
            {
                // 设置请求参数字符集
                request.setCharacterEncoding(resourceProvider.getCharset());
                
                // 设置响应消息消息头类型及字符集
                response.setContentType("application/json;charset=" + getResourceProvider().getCharset());
            }
            
            // 校验用户权限
            try (ServiceSession serviceSession =
                resourceProvider.getResource(ServiceProvider.class).createServiceSession(null,
                    getController().getPrompter(request)))
            {
                // 执行接口操作
                processPost(request, response, serviceSession);
            }
        }
        catch (final Throwable throwable)
        {
            // 记录异常日志
            logger.error("系统异常", throwable);
            
            // 返回异常信息
            onThrowable(request, response, throwable);
        }
    }
    
    protected boolean mustAuthenticated()
    {
        return false;
    }
    
    @Override
    protected void onThrowable(final HttpServletRequest request, final HttpServletResponse response,
        final Throwable throwable)
        throws ServletException, IOException
    {
        String errorCode =
            RandomStringUtils.random(4, new char[] {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'O', 'B',
                'Q', 'I', 'S', 'U', 'V', 'W', 'X', 'Y', 'Z'})
                + RandomUtils.nextInt(100000);
        logger.error(errorCode + throwable);
        StringMsg rc = new StringMsg();
        if (throwable instanceof JsonSyntaxException)
        {
            rc.setCode(ExceptionCode.SIGN_IS_ERROR);
            rc.setDescription("加密串错误！");
            returnHandle(response, rc);
        }
        else if (throwable instanceof SessionOutException)
        {
            rc.setCode(ExceptionCode.NOLOGING_ERROR);
            rc.setDescription(throwable.getMessage());
            returnHandle(response, rc);
        }
        else if (throwable instanceof HMDException)
        {
            rc.setCode(ExceptionCode.ACCOUNT_IS_BLACK);
            rc.setDescription(throwable.getMessage());
            returnHandle(response, rc);
        }
        else if (throwable instanceof SDException)
        {
            rc.setCode(ExceptionCode.ACCOUNT_IS_LOCK);
            rc.setDescription(throwable.getMessage());
            returnHandle(response, rc);
        }
        else if (throwable instanceof AuthenticationException || throwable instanceof AccesssDeniedException)
        {
            rc.setCode(ExceptionCode.AUTHENTICATION_ERROR);
            rc.setDescription(throwable.getMessage());
            returnHandle(response, rc);
        }
        else if (throwable instanceof ParameterException || throwable instanceof ServiceNotFoundException
            || throwable instanceof LogicalException)
        {
            rc.setCode(ExceptionCode.AUTHENTICATION_ERROR);
            rc.setDescription(throwable.getMessage());
            returnHandle(response, rc);
        }
        else
        {
            rc.setCode(ExceptionCode.UNKNOWN_ERROR);
            rc.setDescription("系统异常请联系,系统工作人员！错误码：" + errorCode);
            returnHandle(response, rc);
        }
        throwable.printStackTrace();
    }
    
    /**
     * 回传消息给微信服务器
     * 
     * @param response 响应消息
     * @param msg 回调消息的内容
     * @throws IOException 异常信息
     */
    protected void returnHandle(HttpServletResponse response, final Msg msg)
        throws IOException
    {
        OutputStream os = null;
        OutputStreamWriter write = null;
        try
        {
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            final TransformerFactory tffactory = TransformerFactory.newInstance();
            
            final DocumentBuilder builder = factory.newDocumentBuilder();
            final Document document = builder.newDocument();
            msg.write(document);
            
            os = response.getOutputStream();
            write = new OutputStreamWriter(os, "utf-8");
            
            final Transformer transformer = tffactory.newTransformer();
            transformer.transform(new DOMSource(document), new StreamResult(write));
        }
        catch (Exception e)
        {
            logger.error("回传消息给微信服务器出现异常:", e);
        }
        finally
        {
            if (null != os)
            {
                os.flush();
                os.close();
            }
            
            if (null != write)
            {
                write.close();
            }
        }
    }
    
    private void returnHandle(final HttpServletRequest request, HttpServletResponse response,
        final ReturnCode rc)
        throws IOException
    {
        
        logger.info(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "|accountId:" + rc.getAccountId()
            + "|Path:" + request.getServletPath() + "|" + new Gson().toJson(rc));
        
        Gson gson = new Gson();
        PrintWriter print = response.getWriter();
        try
        {
            String callback = request.getParameter("callback");
            if (StringHelper.isEmpty(callback))
            {
                print.println(gson.toJson(rc));
            }
            else
            {
                print.println(callback + "(" + gson.toJson(rc) + ")");
            }
        }
        finally
        {
            print.flush();
            print.close();
        }
    }
    
    /**
     * 封装返回消息
     * 
     * @param request 请求消息
     * @param response 响应消息
     * @param code 返回码
     * @param desc 返回描述
     * @param data 返回数据
     * @throws IOException 异常信息
     */
    protected void setReturnMsg(final HttpServletRequest request, HttpServletResponse response,
        final String code, final String desc, final Object data)
        throws IOException
    {
        ReturnCode rc = new ReturnCode();
        rc.setCode(code);
        rc.setDescription(desc);
        rc.setData(data == null ? "" : data);
        returnHandle(request, response, rc);
    }
    
    protected ConfigureProvider getConfigureProvider()
    {
        return getResourceProvider().getResource(ConfigureProvider.class);
    }
    
}
