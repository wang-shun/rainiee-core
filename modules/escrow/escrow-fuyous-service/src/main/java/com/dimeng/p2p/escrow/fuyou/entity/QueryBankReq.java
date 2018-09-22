package com.dimeng.p2p.escrow.fuyou.entity;

import java.io.Serializable;

/**
 * 
 * 查看银行卡进度接收实体类
 * @author  zhongsai
 * @version  [版本号, 2018年4月16日]
 */
public class QueryBankReq implements Serializable
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 6213598088727726926L;
    
    /**
     * 返回码（如：0000）
     */
    private String resp_code;
    
    /**
     * 响应信息（如：成功）
     */
    private String desc_code;
    
    /**
     * 审核状态
     */
    private String examine_st;
    
    /**
     * 签名信息
     */
    private String signature;
    
    /**
     * 备注信息
     */
    private String remark;
    
    public QueryBankReq()
    {
        super();
    }
    
    public QueryBankReq(String resp_code, String desc_code, String examine_st, String signature, String remark)
    {
        super();
        this.resp_code = resp_code;
        this.desc_code = desc_code;
        this.examine_st = examine_st;
        this.signature = signature;
        this.remark = remark;
    }
    
    public String getResp_code()
    {
        return resp_code;
    }
    
    public void setResp_code(String resp_code)
    {
        this.resp_code = resp_code;
    }
    
    public String getDesc_code()
    {
        return desc_code;
    }
    
    public void setDesc_code(String desc_code)
    {
        this.desc_code = desc_code;
    }
    
    public String getExamine_st()
    {
        return examine_st;
    }
    
    public void setExamine_st(String examine_st)
    {
        this.examine_st = examine_st;
    }
    
    public String getSignature()
    {
        return signature;
    }
    
    public void setSignature(String signature)
    {
        this.signature = signature;
    }
    
    public String getRemark()
    {
        return remark;
    }
    
    public void setRemark(String remark)
    {
        this.remark = remark;
    }
    
    @Override
    public String toString()
    {
        return "QueryBankReq [resp_code=" + resp_code + ", desc_code=" + desc_code + ", examine_st=" + examine_st
            + ", signature=" + signature + ", remark=" + remark + "]";
    }
    
}
