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
import com.dimeng.p2p.account.user.service.entity.BankCard;
import com.dimeng.util.parser.IntegerParser;

/**
 * 根据id获取用户银行卡信息
 * 
 * @author  huqinfu
 * @version  [版本号, 2016年7月20日]
 */
public class BankcardById extends AbstractAccountServlet
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
        int id = IntegerParser.parse(request.getParameter("id"));
        BankCard bcd = bankCardManage.getBankCar(id);
        jsonMap.put("num", "0000");
        jsonMap.put("bankCard", bcd);
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
            jsonMap.put("num", "0001");
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
