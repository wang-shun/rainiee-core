package com.dimeng.p2p.user.servlets.account;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.UnixCrypt;

import com.dimeng.framework.http.session.authentication.AuthenticationException;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.account.user.service.SafetyManage;
import com.dimeng.p2p.account.user.service.entity.PwdSafetyQuestionAnswer;
import com.dimeng.p2p.common.FormToken;
import com.dimeng.p2p.common.RSAUtils;
import com.dimeng.util.StringHelper;

public class PwdSafeServlet extends AbstractAccountServlet
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
        String pwdType = request.getParameter("pwdType");
        int accountId = serviceSession.getSession().getAccountId();
        if ("update".equals(pwdType)
            && !"CHECK_SUCCESS".equals(serviceSession.getSession().getAttribute(String.valueOf(accountId))))
        {
            out.write("[{'num':1,'msg':'原密保问题答案验证失败','tokenNew':'" + tokenNew + "'}]");
            return;
        }
        serviceSession.getSession().setAttribute(String.valueOf(accountId), null);
        String phone = request.getParameter("realval");
        if ("find".equals(pwdType)
            && !"true".equals(serviceSession.getSession().getAttribute("securitypwd|" + phone + "is")))
        {
            out.write("[{'num':2,'msg':'手机验证码验证失败','tokenNew':'" + tokenNew + "'}]");
            return;
        }
        serviceSession.getSession().setAttribute("securitypwd|" + phone + "is", null);
        String param = request.getParameter("param");
        logger.info("PwdSafeServlet入参： param " + param);
        if (StringHelper.isEmpty(param))
        {
            out.write("[{'num':3,'msg':'密保问题答案不能为空','tokenNew':'" + tokenNew + "'}]");
            return;
        }
        String[] strs = param.split(",");
        List<PwdSafetyQuestionAnswer> parList = new ArrayList<PwdSafetyQuestionAnswer>();
        for (String str : strs)
        {
            PwdSafetyQuestionAnswer pwdSafetyQuestionAnswer = new PwdSafetyQuestionAnswer();
            String[] temp = str.split(":");
            String value = RSAUtils.decryptStringByJs(temp[1]);
            value = UnixCrypt.crypt(value, DigestUtils.sha256Hex(value));
            pwdSafetyQuestionAnswer.questionId = Integer.valueOf(temp[0]);
            pwdSafetyQuestionAnswer.answer = value;
            parList.add(pwdSafetyQuestionAnswer);
        }
        
        SafetyManage safetyManage = serviceSession.getService(SafetyManage.class);
        boolean result = safetyManage.updatePwdSafeInfo(parList);
        logger.info("PwdSafeServlet 修改密保问题结果 result = " + result);
        out.write("{num:'0000',msg:'修改密保问题成功',tokenNew:'" + tokenNew + "'}");
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
            sb.append("{'num':'101','msg':'未登录或会话超时,请重新登录'}");
        }
        else
        {
            sb.append("{'num':'100','msg':'");
            sb.append(throwable.getMessage() + "'}");
        }
        out.write(sb.toString());
    }
}
