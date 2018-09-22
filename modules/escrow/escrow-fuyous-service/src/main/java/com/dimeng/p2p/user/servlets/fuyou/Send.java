package com.dimeng.p2p.user.servlets.fuyou;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.config.Envionment;
import com.dimeng.framework.http.servlet.Controller;
import com.dimeng.framework.http.session.Session;
import com.dimeng.framework.http.session.authentication.AuthenticationException;
import com.dimeng.framework.http.session.authentication.VerifyCodeAuthentication;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.AbstractFuyouServlet;
import com.dimeng.p2p.common.FormToken;
import com.dimeng.p2p.common.PhoneTypeUtil;
import com.dimeng.p2p.escrow.fuyou.service.FYSafetyManage;
import com.dimeng.p2p.service.SmsSenderManageExt;
import com.dimeng.p2p.variables.defines.MessageVariable;
import com.dimeng.p2p.variables.defines.MsgVariavle;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.DateTimeParser;

/**
 * 
 * 发送手机验证码管理
 * <功能详细描述>
 * 
 * @author  lingyuanjie
 * @version  [版本号, 2016年5月26日]
 */
public class Send extends AbstractFuyouServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        String currentTokenValue = request.getParameter("tokenKey");
        if (!FormToken.verify(serviceSession.getSession(), currentTokenValue))
        {
            throw new LogicalException("请不要重复提交请求！");
        }
        String tokenNew = FormToken.hidden(serviceSession.getSession());
        
        PrintWriter out = response.getWriter();
        String phone = request.getParameter("phone");
        String type = request.getParameter("type"); //发送类型
        String verifyType = request.getParameter("verifyType"); //验证码类型
        String verifyCode = request.getParameter("verifyCode"); //验证码
        Session session = serviceSession.getSession();
        
        String utypeTemp1 = "";
        
        VerifyCodeAuthentication verfycode = new VerifyCodeAuthentication();
        verfycode.setVerifyCodeType(verifyType);
        verfycode.setVerifyCode(verifyCode);
        ConfigureProvider configureProvider = getResourceProvider().getResource(ConfigureProvider.class);
        String verifySfxyyzm = configureProvider.getProperty(SystemVariable.SFXYYZM);
        //是否需要输入验证码:false不需要
        try
        {
            if (!"false".equalsIgnoreCase(verifySfxyyzm))
            {
                session.authenticateVerifyCode(verfycode);
            }
        }
        catch (AuthenticationException e)
        {
            out.write("[{'num':2,'msg':'无效的验证码或验证码已过期','tokenNew':'" + tokenNew + "'}]");
            return;
        }
        session.invalidVerifyCode(verifyType); //校验通过后，置为失效
        FYSafetyManage safetyManage = serviceSession.getService(FYSafetyManage.class);
        String senType = "";
        //获取访问者ip地址
        Controller controller = serviceSession.getController();
        String ip = controller.getRemoteAddr(request);
        if (!StringHelper.isEmpty(phone))
        {
            Integer pType = null;
            safetyManage.udpateSendTotal("phone");
            SmsSenderManageExt emailSender = serviceSession.getService(SmsSenderManageExt.class);
            String tem = "";
            
            // 绑定手机号时发送短信和code
            if ("bind".equals(type))
            {
                pType = PhoneTypeUtil.getPhoneType(type);
                //校验当前填写的手机号码是否已存在
                boolean isPhone = safetyManage.checkPhoneIsExist(phone);
                if (isPhone)
                {
                    out.write("[{'num':4,'msg':'手机已存在!','tokenNew':'" + tokenNew + "'}]");
                    return;
                }
                else
                {
                    tem = configureProvider.getProperty(MsgVariavle.RIGISTER_VERIFIY_CODE);
                    senType = type + "|" + phone;
                }
            }
            if (pType != null)
            {
                //获取用户今天已发送手机验证码次数
                Integer ucount = safetyManage.userSendPhoneCount(pType);
                if (ucount >= Integer.parseInt(configureProvider.getProperty(MessageVariable.USER_SEND_MAX_COUNT)))
                {
                    out.write("[{'num':0,'msg':'此功能今天发送手机验证码次数已达上限!','tokenNew':'" + tokenNew + "'}]");
                    return;
                }
                
                //获取IP地址今天已发送手机验证码次数
                Integer ipcount = safetyManage.iPAddrSendSmsCount(ip, pType);
                if (ipcount >= Integer.parseInt(configureProvider.getProperty(MessageVariable.IP_SEND_MAX_COUNT)))
                {
                    out.write("[{'num':0,'msg':'当前IP地址，此功能今天发送手机验证码次数已达上限!','tokenNew':'" + tokenNew + "'}]");
                    return;
                }
            }
            
            Envionment envionment = configureProvider.createEnvionment();
            String code = session.getVerifyCode(senType);
            envionment.set("date", DateTimeParser.format(new Timestamp(System.currentTimeMillis())));
            envionment.set("code", code);
            if(pType != null){
                emailSender.send(pType, StringHelper.format(tem, envionment), ip, phone);
            }
            logger.info(code);
        }
        
        if (!StringHelper.isEmpty(utypeTemp1))
        {
            serviceSession.getSession().removeAttribute(utypeTemp1);
        }
        out.write("[{'num':1,'msg':'sussess','tokenNew':'" + tokenNew + "'}]");
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        
        ResourceProvider resourceProvider = getResourceProvider();
        resourceProvider.log(throwable);
        PrintWriter out = response.getWriter();
        StringBuilder sb = new StringBuilder();
        if (throwable instanceof AuthenticationException)
        {
            sb.append("[{'num':101,'msg':'未登录或会话超时,请重新登录'}]");
        }
        else
        {
            sb.append("[{'num':0,'msg':'");
            sb.append(throwable.getMessage() + "'}]");
        }
        out.write(sb.toString());
    }
    
}
