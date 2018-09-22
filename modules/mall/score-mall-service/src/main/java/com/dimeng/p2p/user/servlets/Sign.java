package com.dimeng.p2p.user.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.session.authentication.OtherLoginException;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S61.enums.T6106_F05;
import com.dimeng.p2p.repeater.score.SetScoreManage;
import com.dimeng.p2p.repeater.score.UserCenterScoreManage;
import com.dimeng.p2p.variables.defines.MallVariavle;

public class Sign extends AbstractMallServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        
        boolean isSigned = false;
        ResourceProvider resourceProvider = getResourceProvider();
        ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        int giveScore = 0;
        boolean is_mall = Boolean.parseBoolean(configureProvider.getProperty(MallVariavle.IS_MALL));
        if (is_mall)
        {
            UserCenterScoreManage userCenterScoreManage = serviceSession.getService(UserCenterScoreManage.class);
            isSigned = userCenterScoreManage.updateUserSigned();
            if (isSigned)
            {
                //签到赠送积分
                SetScoreManage setScoreManage = serviceSession.getService(SetScoreManage.class);
                giveScore = setScoreManage.giveScore(null, T6106_F05.sign, null);
            }
        }
        PrintWriter out = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("isSigned", isSigned);
        jsonMap.put("giveScore", giveScore);
        UserCenterScoreManage userCenterScore = serviceSession.getService(UserCenterScoreManage.class);
        int usableScore = userCenterScore.getUsableScore();
        jsonMap.put("usableScore", usableScore);
        out.print(objectMapper.writeValueAsString(jsonMap));
        out.close();
    }
    
    @Override
    protected boolean mustAuthenticated()
    {
        return true;
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter out = response.getWriter();
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        if (throwable instanceof OtherLoginException || throwable instanceof ParameterException)
        {
            jsonMap.put("msg", throwable.getMessage());
            out.print(objectMapper.writeValueAsString(jsonMap));
        }
        out.close();
    }
}
