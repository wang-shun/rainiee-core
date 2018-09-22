package com.dimeng.p2p.modules.nciic.entity;

/**
 * 身份证属性
 * @author xiaoyaocai
 *
 */
public class IdentifierAttr
{
    
    private String name;
    
    private String documentNo;
    
    private String result;
    
    private String photo;
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getDocumentNo()
    {
        return documentNo;
    }
    
    public void setDocumentNo(String documentNo)
    {
        this.documentNo = documentNo;
    }
    
    public String getResult()
    {
        return result;
    }
    
    public void setResult(String result)
    {
        this.result = result;
    }
    
    public String getPhoto()
    {
        return photo;
    }
    
    public void setPhoto(String photo)
    {
        this.photo = photo;
    }
    
    @Override
    public String toString()
    {
        return "IdentifierAttr [name=" + name + ", documentNo=" + documentNo + ", result=" + result + ", photo="
            + photo + "]";
    }
}
