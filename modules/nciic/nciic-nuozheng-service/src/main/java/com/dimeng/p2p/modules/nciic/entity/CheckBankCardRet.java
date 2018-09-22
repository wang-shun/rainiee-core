package com.dimeng.p2p.modules.nciic.entity;


/*
 * 文 件 名:  CheckBankCardRet.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  YINKE
 * 修改时间:  2016年3月8日
 */
public class CheckBankCardRet
{
    private DataRet data;
    
    private String status;
    
    public DataRet getData()
    {
        return data;
    }
    
    public void setData(DataRet data)
    {
        this.data = data;
    }
    
    public String getStatus()
    {
        return status;
    }
    
    public void setStatus(String status)
    {
        this.status = status;
    }
}
