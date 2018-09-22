package com.dimeng.p2p.front.servlets.password;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.config.Envionment;
import com.dimeng.framework.http.servlet.Controller;
import com.dimeng.framework.http.session.Session;
import com.dimeng.framework.resource.ResourceRegister;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.account.front.service.PasswordManage;
import com.dimeng.p2p.account.user.service.SafetyManage;
import com.dimeng.p2p.common.PhoneTypeUtil;
import com.dimeng.p2p.service.SmsSenderManageExt;
import com.dimeng.p2p.variables.defines.MessageVariable;
import com.dimeng.p2p.variables.defines.MsgVariavle;
import com.dimeng.util.StringHelper;

public class SendCheckCode extends AbstractPasswordServlet {

	private static final long serialVersionUID = 1L;

	@Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable {
		logger.info("sendCheckCode 获取验证码  start");
		String phone = request.getParameter("phone");
		String type = request.getParameter("type");
        String accountType = request.getParameter("accountType");
		request.setAttribute("type", type);
		String tokenNew ="";
		PrintWriter out = response.getWriter();
		Session session = serviceSession.getSession();
        PasswordManage passwordManage = serviceSession.getService(PasswordManage.class);
        SafetyManage safetyManage = serviceSession.getService(SafetyManage.class);
        ConfigureProvider configureProvider = ResourceRegister.getResourceProvider(getServletContext()).getResource(ConfigureProvider.class);
        
        if (StringHelper.isEmpty(phone)){
            out.write("{'errorCode': 1 , 'errorMsg':'手机号码不能为空','tokenNew':'"+tokenNew+"'}");
            return;
        }
        int id = passwordManage.phoneExist(phone, accountType);
        if (id <= 0) {
            out.write("{'errorCode': 2 , 'errorMsg':'手机号码不存在或不匹配.','tokenNew':'"+tokenNew+"'}");
            return;
        }
        session.setAttribute("TEL_PHONE", phone);
        session.setAttribute("RESET_FLAG", "1");//0:表示邮箱 1:表示通过手机
        
        //获取此手机今天已发送验证码次数
        Integer count = safetyManage.bindPhoneCount(phone, PhoneTypeUtil.getPhoneType("phonepwd"));
        if (count >= Integer.parseInt(configureProvider.getProperty(MessageVariable.PHONE_MAX_COUNT))){
            out.write("{'errorCode': 3 , 'errorMsg':'此手机号码当天获取验证码次数已达上限！','tokenNew':'"+tokenNew+"'}");
            return;
        }
        //获取访问者ip地址
        Controller controller = serviceSession.getController();
        String ip = controller.getRemoteAddr(request);
        //获取IP地址今天已发送手机验证码次数
        Integer ipcount = safetyManage.iPAddrSendSmsCount(ip, PhoneTypeUtil.getPhoneType("phonepwd"));
        if (ipcount >= Integer.parseInt(configureProvider.getProperty(MessageVariable.IP_SEND_MAX_COUNT))){
            out.write("{'errorCode': 4 , 'errorMsg':'当前IP地址，此功能今天获取手机验证码次数已达上限！','tokenNew':'"+tokenNew+"'}");
            return;
        }
        //当日该手机与验证码匹配错误次数
        Integer pcount = safetyManage.phoneMatchVerifyCodeErrorCount(phone, PhoneTypeUtil.getPhoneType("phonepwd"));
        
        if (pcount >= Integer.parseInt(configureProvider.getProperty(MessageVariable.PHONE_VERIFYCODE_MAX_ERROR_COUNT))){
            out.write("{'errorCode': 5 , 'errorMsg':'此手机号码当天匹配验证码错误次数已达上限！','tokenNew':'"+tokenNew+"'}");
            return;
        }
        
        SmsSenderManageExt smsSender = serviceSession.getService(SmsSenderManageExt.class);

        String tem = configureProvider.getProperty(MsgVariavle.FIND_PASSWORD);
        Envionment envionment = configureProvider.createEnvionment();
        String code = session.getVerifyCode(PASSWORD_VERIFY_CODE_TYPE);
        envionment.set("code", code);
        smsSender.send(PhoneTypeUtil.getPhoneType("phonepwd"), StringHelper.format(tem, envionment), ip, phone);
        session.setAttribute("PASSWORD_ACCOUNT_ID", Integer.toString(id));
        out.write("{'errorCode': 0 , 'errorMsg':'','tokenNew':'"+tokenNew+"'}");
		logger.info("sendCheckCode 获取验证码  end code=" + code);
	}
}
