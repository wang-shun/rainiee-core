/*
 * 文 件 名:  MsgServlet.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2016年4月11日
 */
package com.dimeng.p2p.app.servlets.weixin;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.app.service.AppWeixinManage;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.p2p.weixin.config.Config;
import com.dimeng.p2p.weixin.dimengapi.msg.EventMsg;
import com.dimeng.p2p.weixin.dimengapi.msg.HeadMsg;
import com.dimeng.p2p.weixin.dimengapi.msg.Msg;
import com.dimeng.p2p.weixin.dimengapi.msg.StringMsg;
import com.dimeng.p2p.weixin.dimengapi.msg.TextMsg;
import com.dimeng.util.StringHelper;

/**
 * 消息处理接口
 * 
 * @author  zhoulantao
 * @version  [版本号, 2016年4月11日]
 */
public class MsgServlet extends AbstractWeiXinServlet
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -2397474288008269105L;
    
    /**
     * 处理微信服务器验证
     */
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        // 获取回调地址
        final String callback = request.getParameter("callback");
        
        // 获取输入流
        final InputStream is = request.getInputStream();
        
        // 解析请求消息
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder builder = factory.newDocumentBuilder();
        final Document document = builder.parse(is);
        
        // 获取头部消息体
        HeadMsg head = new HeadMsg();
        head.read(document);
        
        // 获取消息类型
        String type = head.getMsgType();
        
        try
        {
            /**
             * 注：此处仅为样例，后续有版本需要增加不同的消息处理
             *    请在此处添加，并添加处理类
             *    此注释请勿删除！！！
             */
            if (Msg.MSG_TYPE_TEXT.equals(type))
            {
                // 文本消息
                TextMsg msg = new TextMsg(head);
                msg.read(document);
                
                // 处理文本消息
                onTextMsg(response, msg);
            }
            else if (Msg.MSG_TYPE_EVENT.equals(type))
            {
                // 事件推送
                EventMsg msg = new EventMsg(head);
                msg.read(document);
                
                // 处理事件信息
                onEventMsg(msg, request, response, serviceSession);
            }
            else
            {
                // 返回给微信服务器
                StringMsg rc = new StringMsg();
                rc.setDescription("该功能尚未开通!");
                rc.setCallback(callback);
                returnHandle(response, rc);
            }
        }
        catch (Exception e)
        {
            logger.error("处理微信消息失败:", e);
            
            // 返回给微信服务器
            StringMsg rc = new StringMsg();
            rc.setDescription("请求事件处理错误");
            rc.setCallback(callback);
            returnHandle(response, rc);
        }
    }
    
    /**
     * 处理微信服务器验证
     */
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    /**
     * 文本消息处理
     * 
     * @param response 对微信服务器的响应消息
     * @param msg 文本消息
     * @throws IOException 异常消息
     */
    private void onTextMsg(HttpServletResponse response, final TextMsg msg)
        throws IOException
    {
        TextMsg reMsg = new TextMsg();
        reMsg.setFromUserName(msg.getToUserName());
        reMsg.setToUserName(msg.getFromUserName());
        reMsg.setCreateTime(msg.getCreateTime());
        reMsg.setContent("感谢您的反馈!");
        
        returnHandle(response, reMsg);
    }
    
    /**
     * 事件消息处理
     * 
     * @param msg 事件消息
     * @param response 对微信服务器的响应消息
     * @throws Throwable 异常消息
     */
    private void onEventMsg(final EventMsg msg, final HttpServletRequest request, HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        // 事件消息类型
        String eventType = msg.getEvent();
        
        // 自定义消息类型
        String eventKey = msg.getEventKey();
        
        // 获取系统配置信息
        final ConfigureProvider configureProvider = getConfigureProvider();
        
        // 获取应用名称
        String siteName = configureProvider.format(SystemVariable.SITE_NAME);
        if (EventMsg.SUBSCRIBE.equals(eventType))
        {
            // 订阅
            TextMsg reMsg = msg.getReplayMsg(TextMsg.class);
            reMsg.setContent("感谢您关注" + siteName);
            
            // 回传消息
            returnHandle(response, reMsg);
        }
        else if (EventMsg.UNSUBSCRIBE.equals(eventType))
        {
            // 取消订阅
            AppWeixinManage appWeixinManage = serviceSession.getService(AppWeixinManage.class);
            appWeixinManage.deleteAppWeixin(msg.getFromUserName());
        }
        else if (EventMsg.CLICK.equals(eventType))
        {
            // 点击事件
            if ("bind".equals(eventKey))
            {
                // 绑定用户
                bindUser(msg, serviceSession, request, configureProvider, response);
            }
            else if ("unbind".equals(eventKey))
            {
                // 解绑用户
                unBindUser(msg, serviceSession, request, configureProvider, response);
            }
            else if ("userInfo".equals(eventKey))
            {
                // 查看用户信息
                getUserInfo(msg, serviceSession, request, configureProvider, response);
            }
            else
            {
                TextMsg reMsg = msg.getReplayMsg(TextMsg.class);
                reMsg.setContent("该功能尚未开通！");
                returnHandle(response, reMsg);
            }
        }
    }
    
    /**
     * 绑定用户
     * 
     * @param msg 事件消息对象
     * @param serviceSession 消息上下文
     * @param request 请求消息
     * @param configureProvider 配置文件
     * @param response 响应消息
     * @throws Throwable 异常消息
     */
    private void bindUser(final EventMsg msg, final ServiceSession serviceSession, final HttpServletRequest request,
        final ConfigureProvider configureProvider, HttpServletResponse response)
        throws Throwable
    {
        // 判断此用户是否已经绑定微信公众号
        AppWeixinManage appWeixinManage = serviceSession.getService(AppWeixinManage.class);
        int accountId = appWeixinManage.searchAppWeixin(msg.getFromUserName());
        
        // 封装返回消息
        TextMsg reMsg = msg.getReplayMsg(TextMsg.class);
        StringBuffer content = new StringBuffer();
        
        // 获取应用名称
        String siteName = configureProvider.format(SystemVariable.SITE_NAME);
        
        // 如果accountId不等于0，则表示已经绑定平台账号。提示用户无需再次绑定
        if (accountId > 0)
        {
            // 查询账号信息
            Map<String, String> accountInfo = appWeixinManage.getWeixinAccountInfo(accountId);
            
            // 封装提示信息
            content.append("尊敬的" + accountInfo.get("accountName") + ", 您已绑定过" + siteName + "账号！");
        }
        else
        {
            // 微信鉴权页面请求URL
            final String auth_url = getAuthUrl(request, configureProvider);
            
            // 未绑定账号，发送推送消息绑定账号
            content.append("<a href=\"" + auth_url + "\">请点击绑定您的" + siteName + "账号!</a>\r\n");
        }
        
        reMsg.setContent(content.toString());
        
        // 回传消息
        returnHandle(response, reMsg);
    }
    
    /**
     * 解绑用户
     * 
     * @param msg 事件消息对象
     * @param serviceSession 消息上下文
     * @param request 请求消息
     * @param configureProvider 配置文件
     * @param response 响应消息
     * @throws Throwable 异常消息
     */
    private void unBindUser(final EventMsg msg, final ServiceSession serviceSession, final HttpServletRequest request,
        final ConfigureProvider configureProvider, HttpServletResponse response)
        throws Throwable
    {
        // 判断此用户是否已经绑定微信公众号
        AppWeixinManage appWeixinManage = serviceSession.getService(AppWeixinManage.class);
        int accountId = appWeixinManage.searchAppWeixin(msg.getFromUserName());
        
        // 封装返回消息
        TextMsg reMsg = msg.getReplayMsg(TextMsg.class);
        StringBuffer content = new StringBuffer();
        
        // 获取应用名称
        String siteName = configureProvider.format(SystemVariable.SITE_NAME);
        
        // 微信鉴权页面请求URL
        final String auth_url = getAuthUrl(request, configureProvider);
        
        // 如果accountId不等于0，则表示已经绑定平台账号。
        if (accountId > 0)
        {
            // 删除回话
            appWeixinManage.goOnAccount(accountId);
            
            // 删除绑定账号信息
            String accountName = appWeixinManage.deleteAppWeixin(msg.getFromUserName());
            
            if (!StringHelper.isEmpty(accountName))
            {
                content.append("您成功解绑了账号:" + accountName + "!<a href=\"" + auth_url + "\">可以继续绑定您的其它" + siteName
                    + "账号!</a>\r\n");
            }
            else
            {
                // 发送推送消息
                content.append("您尚未绑定账号!<a href=\"" + auth_url + "\">请点击绑定您的" + siteName + "账号!</a>\r\n");
            }
        }
        else
        {
            // 发送推送消息
            content.append("您尚未绑定账号!<a href=\"" + auth_url + "\">请点击绑定您的" + siteName + "账号!</a>\r\n");
        }
        
        reMsg.setContent(content.toString());
        
        // 回传消息
        returnHandle(response, reMsg);
    }
    
    /**
     * 获取微信鉴权页面请求URL
     * 
     * @param request 请求消息
     * @param configureProvider 配置信息
     * @return 微信鉴权页面请求URL
     * @throws IOException 异常信息
     */
    private String getAuthUrl(final HttpServletRequest request, final ConfigureProvider configureProvider)
        throws IOException
    {
        final String url = Config.getResourceUrl(request.getRequestURL().toString(), request.getContextPath());
        
        // 授权请求URL
        String auth_url =
            "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
        
        final String appId = configureProvider.format(SystemVariable.WEIXIN_APP_ID);
        // 替换url中的值
        auth_url =
            auth_url.replace("APPID", appId)
                .replace("REDIRECT_URI", URLEncoder.encode(url + "#user/personal/binLogin", "UTF-8"))
                .replace("SCOPE", "snsapi_userinfo")
                .replace("STATE", "123");
        
        return auth_url;
    }
    
    /**
     * 获取用户的基础信息
     * 
     * @param msg 事件信息
     * @param serviceSession 上下文session
     * @param request 请求消息
     * @param configureProvider 配置文件信息
     * @param response 响应消息
     * @throws Throwable 
     */
    private void getUserInfo(final EventMsg msg, final ServiceSession serviceSession, final HttpServletRequest request,
        final ConfigureProvider configureProvider, HttpServletResponse response)
        throws Throwable
    {
        // 判断此用户是否已经绑定微信公众号
        AppWeixinManage appWeixinManage = serviceSession.getService(AppWeixinManage.class);
        int accountId = appWeixinManage.searchAppWeixin(msg.getFromUserName());
        
        // 封装返回消息
        TextMsg reMsg = msg.getReplayMsg(TextMsg.class);
        StringBuffer content = new StringBuffer();
        
        if (accountId > 0)
        {
            // 查询账号信息
            Map<String, String> accountInfo = appWeixinManage.getAccountInfo(accountId);
            final String accountName = accountInfo.get("accountName");
            
            // 已赚总收益
            final String earnAmount = accountInfo.get("yzzje");
            
            // 冻结金额
            final String freezeFunds = accountInfo.get("freezeFunds");
            
            // 借款负债
            final BigDecimal loanAmount = appWeixinManage.getLoanAmount(accountId);
            
            // 投资资产
            final BigDecimal tzzc =
                new BigDecimal(accountInfo.get("yxzc")).add(new BigDecimal(accountInfo.get("sbzc")));
            
            // 账户金额 = 冻结金额 + 账户余额
            final BigDecimal zhje = new BigDecimal(freezeFunds).add(new BigDecimal(accountInfo.get("balance")));
            
            // 账户净资产 = 账户金额 + 理财资产 - 借款负债
            final String merelyAmount = String.valueOf(zhje.add(tzzc).subtract(loanAmount));
            
            // 请求的url
            final String url = Config.getResourceUrl(request.getRequestURL().toString(), request.getContextPath());
            
            content.append("尊敬的" + accountName + ",您的<a href=\"" + url + "#user/personal/userInfo/" + accountId
                + "\">账户信息</a>如下:\r\n");
            content.append("    投资总收益:￥" + earnAmount + "\r\n");
            content.append("    总    资    产:￥" + merelyAmount + "\r\n");
            content.append("    冻 结 金 额 :￥" + freezeFunds + "\r\n");
            content.append("    借 款 负 债 :￥" + loanAmount + "\r\n");
        }
        else
        {
            // 微信鉴权页面请求URL
            final String auth_url = getAuthUrl(request, configureProvider);
            
            // 获取应用名称
            String siteName = configureProvider.format(SystemVariable.SITE_NAME);
            
            // 发送推送消息
            content.append("您尚未绑定账号!<a href=\"" + auth_url + "\">请点击绑定您的" + siteName + "账号!</a>\r\n");
        }
        
        reMsg.setContent(content.toString());
        
        // 回传消息
        returnHandle(response, reMsg);
    }
}
