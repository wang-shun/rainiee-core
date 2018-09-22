package com.dimeng.p2p.modules.nciic.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@SuppressWarnings("serial")
@XStreamAlias("policeCheckInfo")
public class PoliceCheckInfo implements java.io.Serializable
{
    @XStreamAlias("message")
    private Message message;
    
    @XStreamAlias("name")
    private String name;
    
    @XStreamAlias("identitycard")
    private String identitycard;//���֤��
    
    @XStreamAlias("compStatus")
    private String compStatus;//�Ա�״̬
    
    @XStreamAlias("compResult")
    private String compResult;//�ȶԽ��
    
    @XStreamAlias("no")
    private String no;
    
    public Message getMessage()
    {
        return message;
    }
    
    public void setMessage(Message message)
    {
        this.message = message;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getIdentitycard()
    {
        return identitycard;
    }
    
    public void setIdentitycard(String identitycard)
    {
        this.identitycard = identitycard;
    }
    
    public String getCompStatus()
    {
        return compStatus;
    }
    
    public void setCompStatus(String compStatus)
    {
        this.compStatus = compStatus;
    }
    
    public String getCompResult()
    {
        return compResult;
    }
    
    public void setCompResult(String compResult)
    {
        this.compResult = compResult;
    }
    
    public String getNo()
    {
        return no;
    }
    
    public void setNo(String no)
    {
        this.no = no;
    }
    
}
