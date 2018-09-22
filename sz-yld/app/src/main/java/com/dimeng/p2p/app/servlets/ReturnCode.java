package com.dimeng.p2p.app.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.p2p.app.servlets.user.domain.BankCard;
import com.dimeng.util.StringHelper;
import com.google.gson.Gson;

public class ReturnCode
{
    private String code;
    
    private String description;
    
    private Object data;
    
    private Integer accountId;
    
    public String getCode()
    {
        return code;
    }
    
    public void setCode(String code)
    {
        this.code = code;
    }
    
    public String getDescription()
    {
        return description;
    }
    
    public void setDescription(String description)
    {
        this.description = description;
    }
    
    public Object getData()
    {
        return data;
    }
    
    public void setData(Object data)
    {
        this.data = data;
    }
    
    public Integer getAccountId()
    {
        return accountId;
    }
    
    public void setAccountId(Integer accountId)
    {
        this.accountId = accountId;
    }
    
    public void handle(HttpServletRequest request, HttpServletResponse response)
        throws IOException
    {
        
        Gson gson = new Gson();
        PrintWriter print = response.getWriter();
        try
        {
            String callback = request.getParameter("callback");
            if (StringHelper.isEmpty(callback))
            {
                print.println(gson.toJson(this));
            }
            else
            {
                print.println(callback + "(" + gson.toJson(this) + ")");
            }
        }
        finally
        {
            print.flush();
            print.close();
        }
        
    }
    
    public static void main(String[] args)
    {
        BankCard bc = new BankCard();
        Gson gson = new Gson();
        System.out.println("json:" + gson.toJson(bc));
    }
    
}
