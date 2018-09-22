package com.dimeng.p2p.modules.nciic.entity;

import javax.xml.bind.annotation.XmlRootElement;

/*
 * 文 件 名:  RespObject.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  YINKE
 * 修改时间:  2015年10月20日
 */
@XmlRootElement(name = "respObject")
public class RespObject
{
    private String result;
    
    private String message;
    
    private String reserve1;
    
    private String reserve2;
    
    public String getResult()
    {
        return result;
    }
    
    public void setResult(String result)
    {
        this.result = result;
    }
    
    public String getMessage()
    {
        return message;
    }
    
    public void setMessage(String message)
    {
        this.message = message;
    }
    
    public String getReserve1()
    {
        return reserve1;
    }
    
    public void setReserve1(String reserve1)
    {
        this.reserve1 = reserve1;
    }
    
    public String getReserve2()
    {
        return reserve2;
    }
    
    public void setReserve2(String reserve2)
    {
        this.reserve2 = reserve2;
    }
}
