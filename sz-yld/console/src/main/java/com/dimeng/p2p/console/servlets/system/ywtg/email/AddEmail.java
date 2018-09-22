package com.dimeng.p2p.console.servlets.system.ywtg.email;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.message.email.EmailSender;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S71.enums.T7164_F07;
import com.dimeng.p2p.console.servlets.system.AbstractSystemServlet;
import com.dimeng.p2p.modules.systematic.console.service.EmailManage;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.EnumParser;

@Right(id = "P2P_C_SYS_ADDEMAIL", name = "新增", moduleId = "P2P_C_SYS_YWTG_YJTG", order = 1)
public class AddEmail extends AbstractSystemServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        EmailManage emailManage = serviceSession.getService(EmailManage.class);
        EmailSender emailSender = serviceSession.getService(EmailSender.class);
        T7164_F07 sendType = EnumParser.parse(T7164_F07.class, request.getParameter("sendType"));
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String email = request.getParameter("email");
        String[] emails = new String[0];
        if (!StringHelper.isEmpty(email))
        {
            emails = email.split("\\s");
        }
        
        //如果是指定人发送，则判断指定人邮件
        if (sendType == T7164_F07.ZDR)
        {
            //判断邮箱地址格式是否正确
            Pattern p =
                Pattern.compile("^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,5}$");
            for (String ms : emails)
            {
                if (!p.matcher(ms).find())
                {
                    throw new ParameterException("指定的推广邮箱地址【" + ms + "】格式不正确.");
                }
            }
            
            //去除重复的邮箱地址
            List<String> emailLists = Arrays.asList(emails);
            Set<String> set = new HashSet<String>(emailLists);
            emails = set.toArray(new String[0]);
            
            //判断邮箱地址是否存在
            for (String s : emails)
            {
                int userId = emailManage.getCheckUserId(s);
                if (userId <= 0)
                {
                    throw new ParameterException("指定的推广邮箱地址【" + s + "】不存在.");
                }
            }
        }
        
        emailManage.addEmail(sendType, title, content, emails);
        if (sendType == T7164_F07.SY)
        {
            emails = emailManage.getUserEmails();
            for (String e : emails)
            {
                emailSender.send(0, title, content, e);
            }
        }
        else if (sendType == T7164_F07.ZDR)
        {
            if (emails.length <= 0)
            {
                throw new ParameterException("指定的推广邮箱地址不能为空.");
            }
            for (String e : emails)
            {
                emailSender.send(0, title, content, e);
            }
        }
        sendRedirect(request, response, getController().getURI(request, EmailList.class));
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        if (throwable instanceof LogicalException || throwable instanceof ParameterException)
        {
            getController().prompt(request, response, PromptLevel.ERROR, throwable.getMessage());
            forward(request, response, getController().getViewURI(request, AddEmail.class));
        }
        else
        {
            super.onThrowable(request, response, throwable);
        }
    }
}
