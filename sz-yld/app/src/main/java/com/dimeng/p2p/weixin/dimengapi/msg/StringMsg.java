package com.dimeng.p2p.weixin.dimengapi.msg;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.w3c.dom.Document;

import com.dimeng.util.StringHelper;

public class StringMsg extends Msg
{
    private String code;
    
    private String description;
    
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
    
    public void write(OutputStream os)
        throws IOException
    {
        PrintWriter print = new PrintWriter(os);
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " : " + this.description);
        
        try
        {
            if (StringHelper.isEmpty(callback))
            {
                print.println(this.description);
            }
            else
            {
                print.println(callback + "(" + this.description + ")");
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
