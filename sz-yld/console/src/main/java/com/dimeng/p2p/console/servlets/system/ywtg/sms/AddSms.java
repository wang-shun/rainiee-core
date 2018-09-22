package com.dimeng.p2p.console.servlets.system.ywtg.sms;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.message.sms.SmsSender;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S71.enums.T7162_F06;
import com.dimeng.p2p.console.servlets.system.AbstractSystemServlet;
import com.dimeng.p2p.modules.systematic.console.service.SmsManage;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.EnumParser;

@Right(id = "P2P_C_SYS_ADDSMS", name = "新增", moduleId = "P2P_C_SYS_YWTG_DXTG", order = 1)
public class AddSms extends AbstractSystemServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        SmsManage smsManage = serviceSession.getService(SmsManage.class);
        SmsSender smsSender = serviceSession.getService(SmsSender.class);
        T7162_F06 sendType = EnumParser.parse(T7162_F06.class, request.getParameter("sendType"));
        String content = request.getParameter("content");
        ConfigureProvider configureProvider = getResourceProvider().getResource(ConfigureProvider.class);
        /*String siteName = configureProvider.getProperty(SystemVariable.SITE_NAME);
        content = content + " 【" + siteName + "】";*/
        String mobile = request.getParameter("mobile");
        String[] mobiles = new String[0];
        if (!StringHelper.isEmpty(mobile))
        {
            mobiles = mobile.split("\\s");
        }
        
        //如果是指定人发送，则判断指定人手机号
        if (sendType == T7162_F06.ZDR)
        {
            //判断手机号码格式是否正确
            Pattern p = Pattern.compile("^(13|14|15|17|18)\\d{9}$");
            for (String ms : mobiles)
            {
                if (!p.matcher(ms).find())
                {
                    throw new ParameterException("指定的推广手机号码【" + ms + "】格式不正确.");
                }
            }
            
            //去除重复的手机号码
            List<String> mobileLists = Arrays.asList(mobiles);
            Set<String> set = new HashSet<String>(mobileLists);
            mobiles = set.toArray(new String[0]);
            
            //判断手机号码是否存在
            for (String s : mobiles)
            {
                int userId = smsManage.getCheckUserId(s);
                if (userId <= 0)
                {
                    throw new ParameterException("指定的推广手机号码【" + s + "】不存在.");
                }
            }
        }
        smsManage.addSms(sendType, content, mobiles);
        if (T7162_F06.SY == sendType)
        {
            mobiles = smsManage.getUserMobiles();
            smsSender.send(0, content, mobiles);
        }
        else if (T7162_F06.ZDR == sendType)
        {
            smsSender.send(0, content, mobiles);
        }
        sendRedirect(request, response, getController().getURI(request, SmsList.class));
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        if (throwable instanceof LogicalException || throwable instanceof ParameterException)
        {
            getController().prompt(request, response, PromptLevel.ERROR, throwable.getMessage());
            forward(request, response, getController().getViewURI(request, AddSms.class));
        }
        else
        {
            super.onThrowable(request, response, throwable);
        }
    }
}
