package com.dimeng.p2p.console.servlets.system.ywtg.letter;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S71.enums.T7160_F07;
import com.dimeng.p2p.console.servlets.system.AbstractSystemServlet;
import com.dimeng.p2p.modules.systematic.console.service.LetterManage;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.EnumParser;

@Right(id = "P2P_C_SYS_ADDLETTER", name = "新增", moduleId = "P2P_C_SYS_YWTG_ZNXTG", order = 1)
public class AddLetter extends AbstractSystemServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        LetterManage letterManage = serviceSession.getService(LetterManage.class);
        T7160_F07 sendType = EnumParser.parse(T7160_F07.class, request.getParameter("sendType"));
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String userName = request.getParameter("userName");
        String[] userNames = new String[0];
        if (!StringHelper.isEmpty(userName))
        {
            userNames = userName.split("\\s+");
        }
        
        List<String> list = Arrays.asList(userNames);
        Set<String> set = new HashSet<String>(list);
        userNames = set.toArray(new String[0]);
        
        if (T7160_F07.ZDR == sendType)
        {
            for (String s : userNames)
            {
                int userId = letterManage.getCheckUserId(s);
                if (userId <= 0)
                {
                    throw new ParameterException("指定的推广用户名【" + s + "】不存在.");
                }
            }
        }
        letterManage.addLetter(sendType, title, content, userNames);
        sendRedirect(request, response, getController().getURI(request, LetterList.class));
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        StringBuffer sb = new StringBuffer(throwable.getMessage());
        if (throwable.getMessage().indexOf("Data too long for column 'F03' at row 1") > -1)
        {
            sb = new StringBuffer();
            sb.append("站内信内容超过表字段最大长度");
        }
        if (throwable instanceof LogicalException || throwable instanceof ParameterException
            || throwable instanceof SQLException)
        {
            getController().prompt(request, response, PromptLevel.ERROR, sb.toString());
            forward(request, response, getController().getViewURI(request, AddLetter.class));
        }
        else
        {
            super.onThrowable(request, response, throwable);
        }
    }
}
