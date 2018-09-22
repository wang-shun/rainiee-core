package com.dimeng.p2p.weixin.dimengapi.msg;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.w3c.dom.Document;

import com.dimeng.util.StringHelper;
import com.google.gson.Gson;

public class JsonMsg extends Msg
{
    private String code;
    
    private String description;
    
    private Object data;
    
    private String callback;
    
    public String getCallback()
    {
        return callback;
    }
    
    public void setCallback(String callback)
    {
        this.callback = callback;
    }
    
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
    
    public void write(OutputStream os)
        throws IOException
    {
        Gson gson = new Gson();
        PrintWriter print = new PrintWriter(os);
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + gson.toJson(this));
        try
        {
            if (StringHelper.isEmpty(callback))
            {
                print.println(gson.toJson(this));
            }
            else
            {
                print.println(callback + "(" + gson.toJson(this) + ")");
            }
        }
        catch (Exception e)
        {
        }
        finally
        {
            print.flush();
            print.close();
        }
    }
    
    @Override
    public void read(Document document)
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void write(Document document)
    {
        // TODO Auto-generated method stub
        
    }
    
    public String getMsgType()
    {
        return MSG_TYPE_JSON;
    }
    
}
