package com.dimeng.p2p.app.servlets.platinfo;

import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.UnixCrypt;

import com.dimeng.framework.http.servlet.Controller;
import com.dimeng.framework.http.session.Session;
import com.dimeng.framework.http.session.SessionManager;
import com.dimeng.framework.http.session.authentication.PasswordAuthentication;
import com.dimeng.framework.http.session.authentication.VerifyCodeAuthentication;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.S61.enums.T6106_F05;
import com.dimeng.p2p.S61.enums.T6110_F08;
import com.dimeng.p2p.S61.enums.T6141_F03;
import com.dimeng.p2p.account.user.service.SafetyManage;
import com.dimeng.p2p.account.user.service.UserThirdPartyRegisterManage;
import com.dimeng.p2p.account.user.service.entity.ThirdUser;
import com.dimeng.p2p.app.service.AppWeixinManage;
import com.dimeng.p2p.app.servlets.AbstractAppServlet;
import com.dimeng.p2p.app.servlets.user.Account;
import com.dimeng.p2p.modules.account.pay.service.UserManage;
import com.dimeng.p2p.modules.account.pay.service.entity.UserInsert;
import com.dimeng.p2p.repeater.score.SetScoreManage;
import com.dimeng.p2p.service.PtAccountManage;
import com.dimeng.p2p.variables.defines.BusinessVariavle;
import com.dimeng.p2p.variables.defines.MallVariavle;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.EnumParser;

/**
 * 
 * 用户注册
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年11月11日]
 */
public class Register extends AbstractAppServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        // 获取session信息
        Session session = serviceSession.getSession();
        if (session == null)
        {
            session = getResourceProvider().getResource(SessionManager.class).getSession(request, response, true);
        }
        
        // 获取验证码
        final String verifyCode = getParameter(request, "verifyCode");
        final String phone = getParameter(request, "phone");
        
        //获取访问者ip地址
        Controller controller = getController();
        String agent = controller.getRemoteAgent(request);
        if (StringHelper.isEmpty(agent))
        {
            throw new LogicalException("非法请求");
        }
        String ip = controller.getRemoteAddr(request);
        UserManage userManager = serviceSession.getService(UserManage.class);
        //查询该ip当天注册账号数量，true：允许注册，false：不允许注册
        if (!userManager.ifAllowRegister(ip))
        {
            logger.info(ip + ",当天注册账号数量达到上限");
            throw new LogicalException("您当天注册账号数量达到上限，请明天再注册");
        }
        
        // 获取填写的用户名、密码
        final String accountName = getParameter(request, "accountName");
        final String password = getParameter(request, "password");
        String newPassword = getParameter(request, "newPassword");
        
        // 判断用户名是否已存在
        boolean isResult = userManager.isAccountExists(accountName);
        
        if (isResult)
        {
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.ACCOUNT_EXISTS_ERROR, "该用户名已存在，请输入其他用户名！");
            return;
        }
        if (accountName.equals(password))
        {
            setReturnMsg(request, response, ExceptionCode.ACCOUNT_PASSWORD_ERROR, "用户名与密码不能相同,请重新输入！");
            return;
        }
        
        // 调用平台注册用户接口
        com.dimeng.p2p.modules.account.pay.service.UserManage userManage =
            serviceSession.getService(com.dimeng.p2p.modules.account.pay.service.UserManage.class);
        
        // 用户类型  LC:理财 JK:借款  APP端默认为LC
        final String type = getParameter(request, "type");
        
        // 邀请码
        final String code = getParameter(request, "code");
        
        // 推广人账号(目前已不使用)
        final String name = getParameter(request, "name");
        
        // 第三方登录账号信息
        final String openId = getParameter(request, "openId");
        
        // QQ登录token
        final String qqToken = getParameter(request, "accessToken");
        
        // 微信登录用户唯一标识
        final String weixinId = getParameter(request, "weixinId");
        
        final String codeVer = session.getVerifyCode("LOGIN");
        
        final String loginType = getParameter(request, "loginType");
        
        // 来源
        final T6110_F08 source = getSource(request);
        
        // 判断手机号码是否被抢注
        SafetyManage safetyManage = serviceSession.getService(SafetyManage.class);
        if (safetyManage.isPhone(phone))
        {
            // 增加判断，防止手机号码被抢注后，重复注册！
            setReturnMsg(request, response, ExceptionCode.PHONE_EXIST_ERRROR, "手机号码已注册，您可以用它直接登录！");
            return;
        }
        
        UserInsert user = new UserInsert()
        {
            
            @Override
            public T6141_F03 getType()
            {
                return EnumParser.parse(T6141_F03.class, type);
            }
            
            @Override
            public String getPassword()
            {
                return password;
            }
            
            @Override
            public String getCode()
            {
                return code;
            }
            
            @Override
            public String getAccountName()
            {
                return accountName;
            }
            
            @Override
            public String getPhone()
            {
                
                return phone;
            }
            
            @Override
            public String getName()
            {
                
                return name;
            }
            
            @Override
            public String getNum()
            {
                return null;
            }

            @Override
            public T6110_F08 getRegisterType()
            {
                return source;
            }
        };
        
        // 注册前判断平台账号是否存在，否，则增加平台账号
        PtAccountManage ptAccountManage = serviceSession.getService(PtAccountManage.class);
        ptAccountManage.addPtAccount();
        
        // 判断邀请码是否存在
        if (!StringHelper.isEmpty(code))
        {
            com.dimeng.p2p.account.front.service.UserManage um =
                serviceSession.getService(com.dimeng.p2p.account.front.service.UserManage.class);
            final int resultCode = um.checkCodeExist(code);
            
            final boolean is_business = Boolean.parseBoolean(getConfigureProvider().getProperty(BusinessVariavle.IS_BUSINESS));
            
            if (resultCode == -1)
            {
                // 封装返回信息
                setReturnMsg(request, response, ExceptionCode.UNKNOWN_ERROR, "邀请码/推广人手机号" + (is_business ? "/业务员工号" : "") + "不存在，请重新输入");
                return;
            }
            else if (resultCode == -2)
            {
                // 封装返回信息
                setReturnMsg(request, response, ExceptionCode.UNKNOWN_ERROR, "邀请码不属于个人用户，请重新输入");
                return;
            }
        }
        
        // 封装验证验证码的对象
        VerifyCodeAuthentication authentication = new VerifyCodeAuthentication();
        // 说明:此处是根据在获取验证码时的类型来校验验证码，type不要填错了
        authentication.setVerifyCodeType("PRZ" + "|" + phone);
        authentication.setVerifyCode(verifyCode);
        
        // 校验验证码是否正确
        session.authenticateVerifyCode(authentication);
        
        int userId = 0;
        try
        {
            userId = userManage.addUser(user, serviceSession);
        }
        catch (Exception e)
        {
            setReturnMsg(request, response, ExceptionCode.UNKNOWN_ERROR, "注册失败，请重新注册");
            return;
        }
        if (userId > 0)
        {
            if (!StringHelper.isEmpty(openId))
            {
                UserThirdPartyRegisterManage userThirdPartyRegisterManage =
                    serviceSession.getService(UserThirdPartyRegisterManage.class);
                ThirdUser thireUser = new ThirdUser();
                thireUser.F01 = userId;
                thireUser.openId = openId;
                //注册则是登录，所以添加注册时间
                thireUser.loginDate = new Timestamp(new Date().getTime());
                thireUser.token = qqToken;
                thireUser.tokenTime = System.currentTimeMillis();
                
                if (!StringHelper.isEmpty(qqToken))
                {
                    thireUser.qqTen = "Y";
                    
                    Long newDateTimes = new Date().getTime();
                    
                    session.setAttribute("qqTokenTime", newDateTimes.toString());
                    session.setAttribute("qqTokens", newDateTimes.toString());
                }
                else
                {
                    thireUser.qqTen = "N";
                    session.setAttribute("qqTokenTime", null);
                }
                if (!StringHelper.isEmpty(loginType) && "weixin".equals(loginType))
                {
                    thireUser.wxAuth = "Y";
                }
                else
                {
                    thireUser.wxAuth = "N";
                }
                userThirdPartyRegisterManage.addT6171(thireUser);
                session.setAttribute("qqToken", null);
            }
            
            if (!StringHelper.isEmpty(weixinId))
            {
                final AppWeixinManage appWeixinManage = serviceSession.getService(AppWeixinManage.class);
                appWeixinManage.updateAppWeixin(weixinId, userId);
            }
            
            // 设置绑定手机号码
            if (!StringHelper.isEmpty(phone))
            {
                safetyManage.updatePhoneById(phone, userId);
            }
            
            // 记录注册信息
            userManage.logRegisterInfo(userId, ip);
            if (session != null)
            {
                session.invalidate(request, response);
            }
            
            // 执行登录操作，保存用户登录信息，跳转到用户账户页面
            PasswordAuthentication passwordAuthentication = new PasswordAuthentication();
            passwordAuthentication.setAccountName(accountName);
            passwordAuthentication.setPassword(UnixCrypt.crypt(newPassword, DigestUtils.sha256Hex(newPassword)));
            passwordAuthentication.setVerifyCodeType("LOGIN");
            passwordAuthentication.setVerifyCode(codeVer);
            
            session.checkIn(request, response, passwordAuthentication);
            
            // 判断注册是否需要送积分
            boolean is_mall = Boolean.parseBoolean(getConfigureProvider().getProperty(MallVariavle.IS_MALL));
            if (is_mall)
            {
                //注册赠送积分
                SetScoreManage setScoreManage = serviceSession.getService(SetScoreManage.class);
                setScoreManage.giveScore(null, T6106_F05.register, null);
            }
            
            controller.forward(request, response, controller.getURI(request, Account.class));
        }
    }
    
    /**
     * 获取设备来源
     * 
     * @param request 请求消息
     * @return 来源名称
     */
    private T6110_F08 getSource(final HttpServletRequest request)
    {
        // 获取设备类型
        final String userAgent = request.getHeader("user-agent").toLowerCase();
        
        if (userAgent.indexOf("micromessenger") > -1)
        {
            // 微信设备
            return T6110_F08.WXZC;
        }
        else
        {
            // APP设备
            return T6110_F08.APPZC;
        }
    }
}
