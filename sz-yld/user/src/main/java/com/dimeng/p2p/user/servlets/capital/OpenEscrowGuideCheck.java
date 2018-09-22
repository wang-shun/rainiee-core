package com.dimeng.p2p.user.servlets.capital;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.resource.ResourceRegister;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.account.user.service.SafetyManage;
import com.dimeng.p2p.account.user.service.UserManage;
import com.dimeng.p2p.account.user.service.entity.Safety;
import com.dimeng.p2p.common.CommonUtils;
import com.dimeng.util.StringHelper;

/**
 * 
 * 托管引导提交校验
 * <功能详细描述>
 * 
 * @author  lingyuanjie
 * @version  [版本号, 2016年5月18日]
 */
public class OpenEscrowGuideCheck extends AbstractCapitalServlet
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        PrintWriter out = response.getWriter();
        ConfigureProvider configureProvider =
            ResourceRegister.getResourceProvider(getServletContext()).getResource(ConfigureProvider.class);
        SafetyManage safetyManage = serviceSession.getService(SafetyManage.class);
        String phpne = request.getParameter("mobile");
        String code = request.getParameter("verifyCode");
        String userTag = request.getParameter("userTag");
        
        if (T6110_F06.parse(userTag) == T6110_F06.ZRR)
        {
            String idcard = request.getParameter("identificationNo");
            String name = request.getParameter("realName");
            
            if (StringHelper.isEmpty(name))
            {
                StringBuilder sb = new StringBuilder();
                sb.append("[{'num':03,'type':'realName','msg':'");
                sb.append("姓名不能为空!" + "'}]");
                out.write(sb.toString());
                return;
            }
            String mtest = "^[\u4e00-\u9fa5]{2,}$";
            name = name.trim();
            if (!name.matches(mtest))
            {
                StringBuilder sb = new StringBuilder();
                sb.append("[{'num':03,'type':'realName','msg':'");
                sb.append("请输入合法的姓名!" + "'}]");
                out.write(sb.toString());
                return;
            }
            if (StringHelper.isEmpty(idcard))
            {
                StringBuilder sb = new StringBuilder();
                sb.append("[{'num':00,'type':'idcard','msg':'");
                sb.append("身份证号码不能为空!" + "'}]");
                out.write(sb.toString());
                return;
            }
            
            idcard = idcard.trim();
            if (!CommonUtils.isValidId(idcard))
            {
                StringBuilder sb = new StringBuilder();
                sb.append("[{'num':03,'type':'idcard','msg':'");
                sb.append("无效的身份证号!" + "'}]");
                out.write(sb.toString());
                return;
            }
            
            if (name.length() > 20)
            {
                StringBuilder sb = new StringBuilder();
                sb.append("[{'num':03,'type':'realName','msg':'");
                sb.append("姓名最多为20个字符!" + "'}]");
                out.write(sb.toString());
                return;
            }
            
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            int year = calendar.get(Calendar.YEAR);
            int born = Integer.parseInt(idcard.substring(6, 10));
            if ((year - born) < 18)
            {
                StringBuilder sb = new StringBuilder();
                sb.append("[{'num':03,'type':'idcard','msg':'");
                sb.append("必须年满18周岁!" + "'}]");
                out.write(sb.toString());
                return;
            }
            
            // 判断修改实名认证信息时，该身份证是否正在注册第三方账户
            if (safetyManage.isAuthingUpdate())
            {
                StringBuilder sb = new StringBuilder();
                sb.append("[{'num':03,'type':'idcard','msg':'");
                sb.append("正在第三方注册账户，不能修改实名认证信息!" + "'}]");
                out.write(sb.toString());
                return;
            }
            
            Safety safety = safetyManage.get();
            UserManage userManage = serviceSession.getService(UserManage.class);
            //是否开通富友，开通富友，一般就有实名认证通过
            /* String isFuyouUser = userManage.selectT6119();
             //本地实名认证开通和富友认证通过，才算实名认证通过, guomianyun by 20170306
             if (!StringHelper.isEmpty(safety.isIdCard) && !StringHelper.isEmpty(isFuyouUser))
             {
                 StringBuilder sb = new StringBuilder();
                 sb.append("[{'num':03,'type':'idcard','msg':'");
                 sb.append("您的账号已通过实名认证，请不要重复认证!" + "'}]");
                 out.write(sb.toString());
                 return;
             }*/
            if (safetyManage.isIdcard(idcard, T6110_F06.ZRR))
            {
                StringBuilder sb = new StringBuilder();
                sb.append("[{'num':00,'type':'idcard','msg':'");
                sb.append("该身份证已认证过!" + "'}]");
                out.write(sb.toString());
                return;
            }
        }
        
        if (StringHelper.isEmpty(phpne))
        {
            StringBuilder sb = new StringBuilder();
            sb.append("[{'num':0,'type':'mobile','msg':'");
            sb.append("手机号码错误" + "'}]");
            out.write(sb.toString());
            return;
        }
        else if (StringHelper.isEmpty(code))
        {
            StringBuilder sb = new StringBuilder();
            sb.append("[{'num':0,'type':'verifyCode','msg':'");
            sb.append("手机验证码错误" + "'}]");
            out.write(sb.toString());
            return;
        }
        /*else
        {
            //当日该手机与验证码匹配错误次数
            Integer ecount = safetyManage.phoneMatchVerifyCodeErrorCount(phpne, 1);
            if (ecount >= Integer.parseInt(configureProvider.getProperty(MessageVariable.PHONE_VERIFYCODE_MAX_ERROR_COUNT)))
            {
                StringBuilder sb = new StringBuilder();
                sb.append("[{'num':0,'type':'verifyCode','msg':'");
                sb.append("当前手机号码当天匹配验证码错误次数已达上限！" + "'}]");
                out.write(sb.toString());
                return;
            }
            String codeType = "bind|" + phpne;
            Session session = serviceSession.getSession();
            VerifyCodeAuthentication verfycode = new VerifyCodeAuthentication();
            verfycode.setVerifyCodeType(codeType);
            verfycode.setVerifyCode(code);
            try
            {
                session.authenticateVerifyCode(verfycode);
            }
            catch (AuthenticationException e)
            {
                //绑定手机号码，type为1，见send.java line 110
                safetyManage.insertPhoneMatchVerifyCodeError(phpne, 1, code);
                StringBuilder sb = new StringBuilder();
                sb.append("[{'num':0,'type':'verifyCode','msg':'");
                sb.append("手机验证码错误" + "'}]");
                out.write(sb.toString());
                return;
            }
            
            if (safetyManage.checkPhoneIsExist(phpne))
            {
                StringBuilder sb = new StringBuilder();
                sb.append("[{'num':0,'type':'mobile','msg':'");
                sb.append("手机号码已存在" + "'}]");
                out.write(sb.toString());
                return;
            }
            session.invalidVerifyCode(codeType);
        }*/
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        PrintWriter out = response.getWriter();
        ResourceProvider resourceProvider = getResourceProvider();
        resourceProvider.log(throwable);
        String errorInfo = throwable.getMessage();
        getController().prompt(request, response, PromptLevel.ERROR, errorInfo);
        StringBuilder sb = new StringBuilder();
        sb.append("[{'num':00,'msg':'");
        sb.append(errorInfo);
        sb.append("'}]");
        out.write(sb.toString());
    }
    
}
