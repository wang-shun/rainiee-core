package com.dimeng.p2p.app.servlets;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.bouncycastle.util.encoders.Base64;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.servlet.AbstractServlet;
import com.dimeng.framework.http.session.Session;
import com.dimeng.framework.http.session.SessionManager;
import com.dimeng.framework.http.session.authentication.AccesssDeniedException;
import com.dimeng.framework.http.session.authentication.AuthenticationException;
import com.dimeng.framework.http.session.authentication.OtherLoginException;
import com.dimeng.framework.http.upload.FileStore;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.exception.ServiceNotFoundException;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.S62.enums.T6250_F11;
import com.dimeng.p2p.account.user.service.UserInfoManage;
import com.dimeng.p2p.app.config.AppConst;
import com.dimeng.p2p.app.exception.HMDException;
import com.dimeng.p2p.app.exception.SDException;
import com.dimeng.p2p.app.exception.SessionOutException;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.variables.TerminalType;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.LongParser;
import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.util.Config;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 
 * 系统抽象类
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年12月18日]
 */
public abstract class AbstractAppBaseServlet extends AbstractServlet
{
    /**
     * 序列化唯一ID
     */
    private static final long serialVersionUID = -8885775080072135627L;
    
    /**
     * 日志参数
     */
    protected final Logger logger = Logger.getLogger(getClass());
    
    /**
     * 默认的一页最大显示行数
     */
    protected static final int PAGESIZE = 20;
    
    /**
     * 前端发送的参数名称
     */
    private static final String SIGN = "sign";
    
    /**
     * 参数的加密KEY，android和IOS的需要与后台保持一致
     * DM-SZ-YLD-FUYOU-0822
     */
    private static final String KEY = "1bfb25c009269cdd";
    
    /**
     * 返回验证码
     * 
     * @param producer
     * @param verifyCode
     * @param response
     */
    protected void showKaptcha(String verifyCode, HttpServletResponse response)
    {
        response.setContentType("image/jpeg");
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        try
        {
            // 设置图片样式
            ImageIO.setUseCache(false);
            final Properties props = new Properties();
            props.setProperty("kaptcha.image.height", "70");
            props.put("kaptcha.border", "no");
            props.put("kaptcha.textproducer.font.color", "black");
            props.put("kaptcha.textproducer.char.space", "5");
            
            final Producer producer = new Config(props).getProducerImpl();
            
            // 生成图片
            final BufferedImage bi = producer.createImage(verifyCode);
            ImageIO.write(bi, "jpeg", response.getOutputStream());
        }
        catch (IOException e)
        {
            logger.error("生成验证码图片失败!", e);
        }
    }
    
    /**
     * 是否需要鉴权登录
     */
    @Override
    protected abstract boolean mustAuthenticated();
    
    @Override
    protected void doGet(final HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        logger.debug("ComeInAbstractAppBaseServlet->" + request.getServletPath());
        ReturnCode rc = new ReturnCode();
        rc.setCode(ExceptionCode.SUCCESS);
        rc.setDescription("success");
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
            
            // 读取session信息
            final Session session = resourceProvider.getResource(SessionManager.class).getSession(request, response);
            
            // 判断session中登录标识
            if (session != null && session.isAuthenticated() && (!session.isOtherLogin() && !"WEIXIN".equals(getAgentType(request))))
            {
                // 设置用户id
                rc.setAccountId(session.getAccountId());
            }
            
            // 判断是否需要鉴权登录，未登录则抛异常信息
            if (mustAuthenticated() && (session == null || !session.isAuthenticated()))
            {
                // 验证是否登录
                throw new SessionOutException();
            }
            else if (session != null && session.isAuthenticated() && (session.isOtherLogin() && !"WEIXIN".equals(getAgentType(request))))
            {
                throw new OtherLoginException();
            }
            else
            {
                // 校验用户权限
                try (ServiceSession serviceSession =
                    resourceProvider.getResource(ServiceProvider.class).createServiceSession(session,
                        getController().getPrompter(request)))
                {
                    if (mustAuthenticated())
                    {
                        try
                        {
                            // 判断用户状态是否非法
                            final boolean isInvalid = checkUserStatus(request, response, session, serviceSession);
                            
                            if (!isInvalid)
                            {
                                // 黑名单异常
                                throw new AuthenticationException("用户状态错误！");
                            }
                        }
                        catch (SDException sdEx)
                        {
                            // 锁定异常
                            throw new AuthenticationException("您的账号已被锁定，操作有所限制。如有疑问，请联系客服！");
                        }
                    }
                    
                    // 执行接口操作
                    processGet(request, response, serviceSession);
                }
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
    
    @Override
    protected void doPost(final HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        doGet(request, response);
    }
    
    /**
     * 检查用户状态
     * 
     * @param request 请求消息
     * @param response 响应消息
     * @param session 用户session
     * @param serviceSession 上下文session
     * @return 用户状态异常信息
     * @throws AuthenticationException 鉴权失败异常
     * @throws Throwable 异常信息
     */
    private boolean checkUserStatus(final HttpServletRequest request, final HttpServletResponse response,
        Session session, ServiceSession serviceSession)
        throws AuthenticationException, Throwable
    {
        UserInfoManage userInfoMananage = serviceSession.getService(UserInfoManage.class);
        T6110 t6110 = userInfoMananage.getUserInfo(session.getAccountId());
        if (t6110.F07 == T6110_F07.QY || t6110.F07 == T6110_F07.HMD)
        {
            return true;
        }
        else if (t6110.F07 == T6110_F07.SD)
        {
            // 锁定
            session.invalidate(request, response);
            throw new SDException();
        }
        
        // 其他异常
        return false;
    }
    
    /**
     * 请球队参数处理
     * 
     * @param paraName
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     */
    @SuppressWarnings({"unchecked"})
    protected String getParameter(final HttpServletRequest request, final String paraName)
        throws UnsupportedEncodingException
    {
        
        if (paraName == null || request == null)
        {
            return "";
        }
        String sign = request.getParameter(SIGN);
        String value = request.getParameter(paraName);
        
        if (StringHelper.isEmpty(sign) && StringHelper.isEmpty(value))
        {
            return "";
        }
        
        Object obj = null;
        if (!StringHelper.isEmpty(sign))
        {
            
            String json = base64Decrypt(sign, KEY);
            logger.debug("request.para.sign.json:" + json);
            Gson gson = new Gson();
            Map<String, Object> paraMap = gson.fromJson(json, Map.class);
            obj = paraMap.get(paraName);
        }
        
        if (obj != null)
        {
            logger.debug("request.para." + paraName + ":" + obj);
            if (obj instanceof String)
            {
                return String.valueOf(obj);
            }
            else if (obj instanceof Integer)
            {
                return String.valueOf(IntegerParser.parse(obj));
            }
            else if (obj instanceof Long)
            {
                return String.valueOf(LongParser.parse(obj));
            }
            else if (obj instanceof Float)
            {
                return String.valueOf(new Float(String.valueOf(obj)).longValue());
            }
            else if (obj instanceof Double)
            {
                return String.valueOf(new Double(String.valueOf(obj)).longValue());
            }
            else
            {
                return String.valueOf(obj);
            }
            
        }
        else
        {
            logger.debug("request.para." + paraName + ":" + (value == null ? "" : value));
            return (value == null ? "" : value);
        }
        
    }
    
    /**
     * 请球队参数处理
     * 
     * @param paraName
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected String[] getParameterValues(final HttpServletRequest request, final String paraName)
        throws UnsupportedEncodingException
    {
        if (paraName == null || request == null)
        {
            return new String[0];
        }
        String sign = request.getParameter(SIGN);
        String[] resArray = request.getParameterValues(paraName);
        
        if (StringHelper.isEmpty(sign) && (resArray == null || resArray.length == 0))
        {
            return new String[0];
        }
        
        Object obj = null;
        if (!StringHelper.isEmpty(sign))
        {
            
            String json = base64Decrypt(sign, KEY);
            
            Gson gson = new Gson();
            Map<String, Object> paraMap = gson.fromJson(json, Map.class);
            obj = paraMap.get(paraName);
        }
        
        if (obj != null)
        {
            if (obj instanceof ArrayList)
            {
                ArrayList list = (ArrayList)obj;
                String[] array = new String[list.size()];
                return (String[])list.toArray(array);
            }
        }
        else
        {
            return resArray == null ? new String[0] : resArray;
        }
        return new String[0];
        
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
        ReturnCode rc = new ReturnCode();
        if (throwable instanceof JsonSyntaxException)
        {
            rc.setCode(ExceptionCode.SIGN_IS_ERROR);
            rc.setDescription("加密串错误！");
            returnHandle(request, response, rc);
        }
        else if (throwable instanceof SessionOutException)
        {
            rc.setCode(ExceptionCode.NOLOGING_ERROR);
            rc.setDescription(throwable.getMessage());
            returnHandle(request, response, rc);
        }
        else if (throwable instanceof HMDException)
        {
            rc.setCode(ExceptionCode.ACCOUNT_IS_BLACK);
            rc.setDescription(throwable.getMessage());
            returnHandle(request, response, rc);
        }
        else if (throwable instanceof SDException)
        {
            rc.setCode(ExceptionCode.ACCOUNT_IS_LOCK);
            rc.setDescription(throwable.getMessage());
            returnHandle(request, response, rc);
        }
        else if (throwable instanceof AuthenticationException || throwable instanceof AccesssDeniedException)
        {
            rc.setCode(ExceptionCode.AUTHENTICATION_ERROR);
            rc.setDescription(throwable.getMessage());
            returnHandle(request, response, rc);
        }
        else if (throwable instanceof ParameterException || throwable instanceof ServiceNotFoundException
            || throwable instanceof LogicalException)
        {
            rc.setCode(ExceptionCode.AUTHENTICATION_ERROR);
            rc.setDescription(throwable.getMessage());
            returnHandle(request, response, rc);
        }
        else if (throwable instanceof OtherLoginException)
        {
            rc.setCode(ExceptionCode.OTHER_LOGIN_EXCEPTION);
            rc.setDescription(throwable.getMessage());
            returnHandle(request, response, rc);
        }
        else
        {
            rc.setCode(ExceptionCode.UNKNOWN_ERROR);
            rc.setDescription("系统异常请联系,系统工作人员！错误码：" + errorCode);
            returnHandle(request, response, rc);
        }
    }
    
    protected ConfigureProvider getConfigureProvider()
    {
        return getResourceProvider().getResource(ConfigureProvider.class);
    }
    
    /**
     * 传入josn格式，列如：{xxx:xxx}，生成sign=XXX 最终加密串传输sign=XXX 传输
     * 
     * @param data
     * @param key
     * @return
     * @throws UnsupportedEncodingException
     */
    private static String base64Encrypt(final String data)
        throws UnsupportedEncodingException
    {
        if (data == null)
        {
            return "";
        }
        String encode = new String(Base64.encode(data.getBytes("utf-8")));
        encode += KEY;
        byte[] b = encode.getBytes();
        for (int i = 0; i < b.length / 2; i++)
        {
            byte temp = b[i];
            b[i] = b[b.length - 1 - i];
            b[b.length - 1 - i] = temp;
        }
        
        return "?" + SIGN + "=" + new String(b);
        
    }
    
    /**
     * map格式，生成sign=XXX 最终加密串传输sign=XXX 传输
     * 
     * @param data
     * @param key
     * @return
     * @throws UnsupportedEncodingException
     */
    protected String base64Encrypt(final Map<String, String> data)
        throws UnsupportedEncodingException
    {
        
        if (data == null || data.size() == 0)
        {
            return "";
        }
        
        Iterator<Entry<String, String>> ite = data.entrySet().iterator();
        String json = "{";
        while (ite.hasNext())
        {
            Entry<String, String> entry = ite.next();
            String key = entry.getKey();
            String value = entry.getValue();
            json += (key + ":" + value + ",");
        }
        json = json.substring(0, json.length() - 1);
        json += "}";
        logger.debug("Map->json:" + json);
        return base64Encrypt(json);
        
    }
    
    private String base64Decrypt(final String data, final String key)
        throws UnsupportedEncodingException
    {
        if (data == null)
        {
            return "";
        }
        
        byte[] b = data.getBytes("utf-8");
        for (int i = 0; i < b.length / 2; i++)
        {
            byte temp = b[i];
            b[i] = b[b.length - 1 - i];
            b[b.length - 1 - i] = temp;
        }
        String decode = new String(b);
        decode = decode.replaceAll(key, "");
        return new String(Base64.decode(decode), "utf-8");
        
    }
    
    protected void returnHandle(final HttpServletRequest request, final HttpServletResponse response,
        final ReturnCode rc)
        throws IOException
    {
        // 记录出口日志
        if (logger.isDebugEnabled())
        {
            StringBuffer returnlog = new StringBuffer();
            returnlog.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            returnlog.append("|Path:");
            returnlog.append(request.getServletPath());
            if (null != rc)
            {
                if (!StringHelper.isEmpty(String.valueOf(rc.getAccountId())))
                {
                    returnlog.append("|accountId:");
                    returnlog.append(rc.getAccountId());
                }
                
                returnlog.append('|');
                returnlog.append(new Gson().toJson(rc));
            }
            
            logger.debug(returnlog.toString());
        }
        
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
        catch (Exception e)
        {
            logger.error("返回消息出错:" + e.getMessage(), e);
        }
        finally
        {
            print.flush();
            print.close();
        }
    }
    
    /**
     * 获取环境基础路径
     * 
     * @param url 需要访问的接口URL
     * @return 接口绝对路径
     * @throws IOException 异常信息
     */
    protected String getSiteDomain(final String url)
        throws IOException
    {
        // 获取配置文件
        final ConfigureProvider configureProvider = getConfigureProvider();
        
        StringBuffer siteDomain = new StringBuffer();
        siteDomain.append(configureProvider.format(SystemVariable.SITE_REQUEST_PROTOCOL));
        String SITE_DOMAIN = configureProvider.format(SystemVariable.SITE_DOMAIN);
//                String SITE_DOMAIN = "61.145.159.156:5118";
        //        String SITE_DOMAIN = "115.159.150.99";
        //        String SITE_DOMAIN = "www.dimengwx.cc";//微信端测试环境
        siteDomain.append(SITE_DOMAIN);
        
        if (StringHelper.isEmpty(url))
        {
            return siteDomain.toString();
        }
        
        // 判断是否以email开头的路径,邮箱认证地址
        if (url.indexOf("/email") == 0)
        {
            siteDomain.append(url);
            return siteDomain.toString();
        }
        
        // 判断url是否以/开头
        if (url.indexOf("/") != 0)
        {
            siteDomain.append('/');
            
            // 判断url是否以"app/"开头
            String urltmp = AppConst.MODULE.concat("/");
            if (url.indexOf(urltmp) != 0)
            {
                siteDomain.append(urltmp);
                siteDomain.append(url);
            }
            else
            {
                siteDomain.append(url);
            }
        }
        else
        {
            // url以"/"开头，则判断是否以"/app/"开头
            String urltmp = "/".concat(AppConst.MODULE);
            if (url.indexOf(urltmp) != 0)
            {
                siteDomain.append(urltmp);
                siteDomain.append(url);
            }
            else
            {
                siteDomain.append(url);
            }
        }
        
        return siteDomain.toString();
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
    protected void setReturnMsg(final HttpServletRequest request, final HttpServletResponse response,
        final String code, final String desc, final Object data)
        throws IOException
    {
        ReturnCode rc = new ReturnCode();
        rc.setCode(code);
        rc.setDescription(desc);
        rc.setData(data == null ? "" : data);
        returnHandle(request, response, rc);
    }
    
    /**
     * 封装返回消息
     * 
     * @param request 请求消息
     * @param response 响应消息
     * @param code 返回码
     * @param desc 返回描述
     * @throws IOException 异常信息
     */
    protected void setReturnMsg(final HttpServletRequest request, final HttpServletResponse response,
        final String code, final String desc)
        throws IOException
    {
        setReturnMsg(request, response, code, desc, null);
    }
    
    /**
     * 封装页面分页信息
     * 
     * @param pageIndex 当前页数
     * @param pageSize 每页显示行数
     * @return 分页信息
     */
    protected Paging getPaging(final int pageIndex, final int pageSize)
    {
        Paging paging = new Paging()
        {
            @Override
            public int getCurrentPage()
            {
                return pageIndex;
            }
            
            @Override
            public int getSize()
            {
                return pageSize == 0 ? PAGESIZE : pageSize;
            }
        };
        
        return paging;
    }
    
    /**
     * 封装合同模板信息
     * 
     * @param xymc 协议名称
     * @param content 协议内容
     * @param valueMap 协议需要的参数
     * @return 模板内容
     */
    protected String getTemplate(final String xymc, final String content, final Map<String, Object> valueMap)
    {
        StringBuffer appTemplate = new StringBuffer();
        Configuration cfg = new Configuration();
        Template template = null;
        try
        {
            template = new Template(xymc, content, cfg);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage());
            
            // 获取合同失败
            return ExceptionCode.UNKNOWN_ERROR;
        }
        
        Writer writer = null;
        BufferedReader bufferedReader = null;
        File filePath = null;
        long currentTime = System.currentTimeMillis();
        try
        {
            String loadPath = this.getClass().getClassLoader().getResource(".").getPath();
            // 处理高并发时，导致文件数据脏读的问题
            filePath = new File(loadPath + File.separator + xymc + "_" + currentTime + ".txt");
            logger.info("AgreementController.getAppTemplate filePath=" + filePath);
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8"));
            template.process(valueMap, writer);
            if (filePath.isFile() && filePath.exists())
            {
                bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null)
                {
                    appTemplate.append(lineTxt).append("<br/>");
                }
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage());
            
            // 获取合同失败
            return ExceptionCode.UNKNOWN_ERROR;
        }
        finally
        {
            if (bufferedReader != null)
            {
                try
                {
                    bufferedReader.close();
                }
                catch (Exception e)
                {
                    logger.error(e.getMessage());
                    
                    // 获取合同失败
                    return ExceptionCode.UNKNOWN_ERROR;
                }
            }
            if (writer != null)
            {
                try
                {
                    writer.close();
                }
                catch (Exception e)
                {
                    logger.error(e.getMessage());
                    
                    // 获取合同失败
                    return ExceptionCode.UNKNOWN_ERROR;
                }
            }
            if (null != filePath && filePath.isFile() && filePath.exists())
            {
                filePath.delete();
            }
        }
        
        return appTemplate.toString();
    }
    
    /**
     * 获取带图片的文件内容
     * 
     * @param sourceContent 原内容
     * @return 处理过的内容
     * @throws IOException 
     */
    protected String getImgContent(final String sourceContent)
        throws IOException
    {
        // 读取文件服务器处理类
        ResourceProvider resourceProvider = getResourceProvider();
        FileStore fileStore = resourceProvider.getResource(FileStore.class);
        
        // 处理文件内容
        String content = StringHelper.format(sourceContent, fileStore);
        content =
            content.replaceAll("src=\"/.*?/fileStore", "style='width:100%;height:10em' src=\""
                + getSiteDomain("/fileStore"));
        content = content.replace("\t", "").replace("<br/>", "").replace("&nbsp;", "");
        
        return content;
    }
    
    /**
     * 封装提交的form表单
     * 
     * @param param
     * @param formUrl
     * @return
     * @throws Throwable
     */
    protected String createSubmitForm(Map<String, String> param, String formUrl)
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
     * 获取设备来源
     * 
     * @param request 请求消息
     * @return 来源名称
     */
    protected String getAgentType(final HttpServletRequest request)
    {
        // 获取设备类型
        final String userAgent = request.getHeader("user-agent").toLowerCase();
        
        if (userAgent.indexOf("micromessenger") > -1)
        {
            // 微信设备
            return T6250_F11.WEIXIN.name();
        }
        else
        {
            // APP设备
            return T6250_F11.APP.name();
        }
    }
    
    /**
     * 细分终端设备来源
     * @param request
     * @return 终端类型
     */
    protected String getTerminalType(final HttpServletRequest request)
    {
        // 获取设备类型
        final String userAgent = request.getHeader("user-agent").toLowerCase();
        
        if (userAgent.contains("micromessenger"))
        {
            // 微信设备
            return TerminalType.wx.name();
        }
        else if (userAgent.contains("ios"))
        {
            // IOS设备
            return TerminalType.ios.name();
        }
        else if (userAgent.contains("android"))
        {
            // andriod设备
            return TerminalType.ad.name();
        }
        else
        {
            // 其他设备
            return TerminalType.other.name();
        }
    }
    
    /**
     * 校验是否含有中文字符
     * <功能详细描述>
     * @return
     */
    protected boolean isContainChinese(String input)
    {
        boolean flag = false;
        String reg = "[\u2E80-\u9FFF]";
        Pattern pat = Pattern.compile(reg);
        Matcher mat = pat.matcher(convertMD5(input));
        if (mat.find())
        {
            flag = true;
        }
        
        return flag;
    }
    
    public static String convertMD5(String inStr)
    {
        char[] a = inStr.toCharArray();
        for (int i = 0; i < a.length; i++)
        {
            a[i] = (char)(a[i] ^ 't');
        }
        String s = new String(a);
        return s;
    }
    public static void main(String[] args)
        throws UnsupportedEncodingException
    {
        
        // String json = "{i:['1','2','3'],j:'8'}";
        // Gson gson = new Gson();
        // Map<String,Object> map = gson.fromJson(json, Map.class);
        // System.out.println(map.get("i") instanceof ArrayList);
        // System.out.println(base64Encrypt("{\"bidId\":\"66\"}", KEY));
        // System.out.println(base64Encrypt(base64Encrypt("{\"bidId\":\"66\"}",
        // KEY),KEY));
        // System.out.println(base64Decrypt(base64Decrypt("5lkJ0Fs6dKi9to1Gu==QZ5pUahdlUKp1QJZTSqllMJ5GM9U3Rx8Gd5k2SkZzcGBjSrxWN",KEY),KEY));
        // System.out.println("=================1=========================");
        // System.out.println(base64Encrypt("{\"creId\":\"65\"}", KEY));
        // System.out.println(base64Encrypt(base64Encrypt("{\"bidId\":\"66\"}",
        // KEY),KEY));
        // System.out.println(base64Decrypt(base64Decrypt("5lkJ0Fs6dKi9to1Gu==QZ5pUahdlUKp1QJZTSqllMJ5GM9U3Rx8Gd5k2SkZzcGBjSrxWN",KEY),KEY));
        
        // System.out.println(base64Decrypt("5lkJ0Fs6dKi9to1Gu=0nI0YTN0YTN0YTN0YTN0YTN0YTNiojIkJXYDRWaiwiIzOo5riY5iojIl1WYuJye",
        // KEY));
        // //eyJiaWRJZCI6IjY2In0D3%uG1ot9iKd6sF0Jkl5
        // System.out.println(base64Encrypt("{\"name\":\"别想\",\"idCard\":\"564564564564564564\"}"));
        // Map<String,String> map = new HashMap<String, String>();
        // map.put("name", "别想");
        // map.put("idCard", "564564564564564564");
        // System.out.println(base64Encrypt(map));
        // System.out.println(base64Decrypt("5lkJ0Fs6dKi9to1Gu9N7gmvKilrTZtFmbsQjN1QjN1QjN1QjN1QjN1QjN1oDZyF2Qkl2e",
        // KEY));
    }
    
}
