package com.dimeng.p2p.user.servlets.tsjy;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.session.Session;
import com.dimeng.framework.http.session.SessionManager;
import com.dimeng.framework.http.session.authentication.OtherLoginException;
import com.dimeng.framework.http.session.authentication.VerifyCodeAuthentication;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.account.user.service.TzjyManage;
import com.dimeng.p2p.account.user.service.UserInfoManage;
import com.dimeng.p2p.service.SafetymsgViewManage;
import com.dimeng.p2p.user.servlets.letter.AbstractLetterServlet;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.parser.IntegerParser;

public class SaveTsjy extends AbstractLetterServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        PrintWriter out = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        ConfigureProvider configureProvider = getResourceProvider().getResource(ConfigureProvider.class);
        SafetymsgViewManage safeManage = serviceSession.getService(SafetymsgViewManage.class);
        UserInfoManage uManage = serviceSession.getService(UserInfoManage.class);
        TzjyManage manage = serviceSession.getService(TzjyManage.class);
        
        //校验验证码
        try
        {
            
            Session session =
                getResourceProvider().getResource(SessionManager.class).getSession(request, response, true);
            String verifyFlag = configureProvider.getProperty(SystemVariable.SFXYYZM);
            VerifyCodeAuthentication verfycode = new VerifyCodeAuthentication();
            verfycode.setVerifyCodeType("TSJY");
            verfycode.setVerifyCode(request.getParameter("verifyVal"));
            if ("true".equalsIgnoreCase(verifyFlag))
            {
                session.authenticateVerifyCode(verfycode);
            }
        }
        catch (Exception e)
        {
            jsonMap.put("erroMsg", "无效的验证码或验证码已过期");
            out.print(objectMapper.writeValueAsString(jsonMap));
            out.close();
            return;
        }
        
        //判断实名认证
        UserInfoManage mge = serviceSession.getService(UserInfoManage.class);
        if (!mge.isSmrz())
        {
            T6110 t6110 = uManage.getUserInfo(serviceSession.getSession().getAccountId());
            String baseInfoPageName = T6110_F06.ZRR == t6110.F06 ? "个人基础信息" : "安全信息";
            //没有实名认证的不能提交投诉建议
            jsonMap.put("erroMsgDialog",
                "您尚未进行实名认证，不可进行投诉建议。请您到<a href=\"" + configureProvider.format(safeManage.getSafetymsgView())
                    + "\" class=\"blue\">" + baseInfoPageName + "</a>设置。");
            out.print(objectMapper.writeValueAsString(jsonMap));
            out.close();
            return;
        }
        
        //判断已提交的投诉建议数量
        int count = manage.getAdvCount(serviceSession.getSession().getAccountId());
        int ALLWO_ADVICE_TIMES = IntegerParser.parse(configureProvider.getProperty(SystemVariable.ALLWO_ADVICE_TIMES));
        if (count >= ALLWO_ADVICE_TIMES)
        {
            jsonMap.put("erroMsgDialog", "您今天反馈的次数已到达上限，请明天再试");
            out.print(objectMapper.writeValueAsString(jsonMap));
            out.close();
            return;
        }
        
        //保存投诉建议
        String content = request.getParameter("content");
        manage.saveInfo(serviceSession.getSession().getAccountId(), content);
        jsonMap.put("saveMsg", "提交成功，感谢您的反馈");
        out.print(objectMapper.writeValueAsString(jsonMap));
        out.close();
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter out = response.getWriter();
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        if (throwable instanceof OtherLoginException)
        {
            jsonMap.put("msg", throwable.getMessage());
            out.print(objectMapper.writeValueAsString(jsonMap));
        }
        out.close();
    }
    
}
