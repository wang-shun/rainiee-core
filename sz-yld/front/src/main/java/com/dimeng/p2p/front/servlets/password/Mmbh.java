package com.dimeng.p2p.front.servlets.password;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.session.Session;
import com.dimeng.framework.http.session.authentication.VerifyCodeAuthentication;
import com.dimeng.framework.resource.ResourceRegister;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.SystemConst;
import com.dimeng.p2p.account.user.service.SafetyManage;
import com.dimeng.p2p.common.EmailTypeUtil;
import com.dimeng.p2p.common.FormToken;
import com.dimeng.p2p.common.PhoneTypeUtil;
import com.dimeng.p2p.variables.defines.MessageVariable;
import com.dimeng.util.StringHelper;

public class Mmbh extends AbstractPasswordServlet
{
    
    private static final long serialVersionUID = 1L;
    
    private final String typeFlagEmail = "0"; //邮箱
    
    private final String typeFlagPhone = "1"; //手机
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        logger.info("密保问题 start");
        String code = request.getParameter("code");
        String phone = request.getParameter("phone");
        String type = request.getParameter("type");
        String questionId = request.getParameter("questionId");
        String answer = request.getParameter("answer");
        String errorCount = request.getParameter("errorCount");// 当前验证错误次数
        String accountId = request.getParameter("accountId");
        String accountType = request.getParameter("accountType");
        String email = request.getParameter("email");
        request.setAttribute("type", type);
        
        StringBuffer log = new StringBuffer("密保问题提交 入参:");
        log.append("code=" + code)
            .append(",phone=" + phone)
            .append(",type=" + type)
            .append(",questionId=" + questionId)
            .append(",accountType=" + accountType)
            .append(",answer=" + answer)
            .append(",errorCount=" + errorCount)
            .append(",accountId=" + accountId);
        logger.info(log.toString());
        
        PrintWriter out = response.getWriter();
        if (typeFlagEmail.equals(type))
        {
            if (!FormToken.verify(serviceSession.getSession(), request.getParameter("tokenEmail")))
            {
                out.write("{'num':1,'msg':'请不要重复提交！'}");
                return;
            }
        }
        else
        {
            if (!FormToken.verify(serviceSession.getSession(), request.getParameter("tokenPhone")))
            {
                out.write("{'num':1,'msg':'请不要重复提交！'}");
                return;
            }
        }
        //生成token
        FormToken.hidden(serviceSession.getSession());
        //获取token的值
        String tokenNew = serviceSession.getSession().getAttribute(SystemConst.TOKEN_NAME);
        ConfigureProvider configureProvider =
            ResourceRegister.getResourceProvider(getServletContext()).getResource(ConfigureProvider.class);
        SafetyManage safetyManage = serviceSession.getService(SafetyManage.class);
        int errors = 0;
        int allowCount =
            Integer.parseInt(configureProvider.getProperty(MessageVariable.PHONE_VERIFYCODE_MAX_ERROR_COUNT));
        if (typeFlagPhone.equals(type) && !StringHelper.isEmpty(errorCount))
        {
            try
            {
                errors = Integer.parseInt(errorCount);
                // 验证次数超过,直接返回
                int ecount = safetyManage.phoneMatchVerifyCodeErrorCount(phone, PhoneTypeUtil.getPhoneType("phonepwd"));
                if (ecount >= allowCount || errors > allowCount)
                {
                    out.write("{'num':1,'msg':'当前手机号码当天匹配验证码错误次数已达上限！','tokenNew':'" + tokenNew + "'}");
                    return;
                }
            }
            catch (Exception e)
            {
                errors = 0;
            }
        }
        else if (typeFlagEmail.equals(type) && !StringHelper.isEmpty(email))
        {// 邮箱
            int ecount = safetyManage.phoneMatchVerifyCodeErrorCount(email, EmailTypeUtil.getEmailType("emailpwd"));
            if (ecount >= allowCount)
            {
                out.write("{'num':1,'msg':'此邮箱当天匹配验证码错误次数已达上限！','tokenNew':'" + tokenNew + "'}");
                return;
            }
        }
        /*boolean isPsdSafe= Boolean.parseBoolean(configureProvider.getProperty(SystemVariable.SFXYMBAQNZ));
        if(isPsdSafe){
        	String inputPwdValue = RSAUtils.decryptStringByJs(answer);
        	inputPwdValue =  UnixCrypt.crypt(inputPwdValue, DigestUtils.sha256Hex(inputPwdValue));
        	String dbValue = safetyManage.getUserAnswerByQuestionId(Integer.parseInt(accountId), Integer.parseInt(questionId));
        	if(!inputPwdValue.equals(dbValue)){
        		//errors ++;
        		out.write("{'num':3,'msg':'输入答案不正确','tokenNew':'"+tokenNew+"'}");
        		return;
        	}
        }*/
        Session session = serviceSession.getSession();
        VerifyCodeAuthentication authentication = new VerifyCodeAuthentication();
        authentication.setVerifyCode(code);
        authentication.setVerifyCodeType(PASSWORD_VERIFY_CODE_TYPE);
        if (typeFlagPhone.equals(type) && !StringHelper.isEmpty(phone))
        {
            try
            {
                session.authenticateVerifyCode(authentication);
            }
            catch (Exception e)
            {
                // 插入手机验证码匹配错误表
                errors++;
                session.setAttribute("ERROR_CHECK_COUNT", String.valueOf(errors));
                if (typeFlagPhone.equals(type) && !StringHelper.isEmpty(phone))
                {
                    session.setAttribute("TEL_PHONE", phone);
                    safetyManage.insertPhoneMatchVerifyCodeError(phone, PhoneTypeUtil.getPhoneType("phonepwd"), code);
                }
                else if (typeFlagEmail.equals(type) && !StringHelper.isEmpty(email))
                {
                    session.setAttribute("EMAIL_PWD", email);
                    safetyManage.insertPhoneMatchVerifyCodeError(email, EmailTypeUtil.getEmailType("emailpwd"), code);
                }
                out.write("{'num':2,'msg':'无效的验证码或验证码已过期.','tokenNew':'" + tokenNew + "'}");
                return;
            }
        }
        //校验成功后，标记校验状态
        session.setAttribute(session.getAttribute("PASSWORD_ACCOUNT_ID"), "CHECK_SUCCESS");
        logger.info("密保问题 end");
        out.write("{'num':0,'msg':'','tokenNew':'" + tokenNew + "'}");
    }
}
