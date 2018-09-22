package com.dimeng.p2p.user.servlets.account;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import com.dimeng.framework.http.session.authentication.AuthenticationException;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.account.user.service.BankCardManage;
import com.dimeng.p2p.account.user.service.SafetyManage;
import com.dimeng.p2p.account.user.service.entity.BankCardQuery;
import com.dimeng.p2p.account.user.service.entity.Safety;
import com.dimeng.p2p.common.enums.AttestationState;
import com.dimeng.p2p.common.enums.BankCardStatus;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;

public class Editbankcard extends AbstractAccountServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        BankCardManage bankCardManage = serviceSession.getService(BankCardManage.class);
        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter out = response.getWriter();
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        SafetyManage userManage = serviceSession.getService(SafetyManage.class);
        Safety data = userManage.get();
        if (data.isIdCard.equals(AttestationState.WYZ.name()) || StringHelper.isEmpty(data.txpassword))
        {
            jsonMap.put("num", "9999");
            jsonMap.put("msg", "未实名认证！");
            out.print(objectMapper.writeValueAsString(jsonMap));
            out.close();
            return;
        }
        BankCardQuery query = new BankCardQuery()
        {
            @Override
            public String getSubbranch()
            {
                return request.getParameter("subbranch");
            }
            
            @Override
            public String getStatus()
            {
                return BankCardStatus.QY.name();
            }
            
            @Override
            public String getCity()
            {
                return request.getParameter("xian");
            }
            
            @Override
            public String getBankNumber()
            {
                return null;
            }
            
            @Override
            public int getBankId()
            {
                return IntegerParser.parse(request.getParameter("bankname"));
            }
            
            @Override
            public int getAcount()
            {
                return serviceSession.getSession().getAccountId();
            }
            
            @Override
            public String getUserName()
            {
                return request.getParameter("userName");
            }
            
            @Override
            public int getType()
            {
                return Integer.parseInt(request.getParameter("type"));
            }
        };
        //启用银行卡
        int id = IntegerParser.parse(request.getParameter("id"));
        bankCardManage.update(id, query);
        /*request.setAttribute("close", "close");
        getController().forwardView(request, response, Editbankcard.class);*/
        jsonMap.put("num", "0000");
        jsonMap.put("msg", "修改银行卡成功！");
        out.print(objectMapper.writeValueAsString(jsonMap));
        out.close();
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        getResourceProvider().log(throwable);
        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter out = response.getWriter();
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        if (throwable instanceof SQLException)
        {
            jsonMap.put("num", "9999");
            jsonMap.put("msg", "系统繁忙，请您稍后再试！");
            out.print(objectMapper.writeValueAsString(jsonMap));
            out.close();
            return;
        }
        else if (throwable instanceof ParameterException || throwable instanceof LogicalException)
        {
            jsonMap.put("num", "9999");
            jsonMap.put("msg", throwable.getMessage());
            out.print(objectMapper.writeValueAsString(jsonMap));
            out.close();
            return;
        }
        else if (throwable instanceof AuthenticationException)
        {
            jsonMap.put("num", "0002");
            jsonMap.put("msg", throwable.getMessage());
            out.print(objectMapper.writeValueAsString(jsonMap));
            out.close();
            return;
        }
        else
        {
            jsonMap.put("num", "9999");
            jsonMap.put("msg", throwable.getMessage());
            out.print(objectMapper.writeValueAsString(jsonMap));
            out.close();
            return;
        }
    }
    
}
