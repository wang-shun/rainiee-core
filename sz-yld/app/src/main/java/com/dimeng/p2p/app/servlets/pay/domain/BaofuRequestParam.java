/**
 * 
 */
package com.dimeng.p2p.app.servlets.pay.domain;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author zhoulantao
 *
 */
@XmlRootElement(name="data_content")
public class BaofuRequestParam implements Serializable
{
    private static final long serialVersionUID = -8995075274383374853L;
    
    private String txn_sub_type;
    
    private String biz_type;
    
    private String terminal_id;
    
    private String member_id;
    
    private String pay_code;
    
    private String acc_no;
    
    private String id_card_type;
    
    private String id_card;
    
    private String id_holder;
    
    private String mobile;
    
    private String valid_date;
    
    private String valid_no;
    
    private String trans_id;
    
    private String txn_amt;
    
    private String trade_date;
    
    private String commodity_name;
    
    private String commodity_amount;
    
    private String user_name;
    
    private String page_url;
    
    private String return_url;
    
    private String additional_info;
    
    private String req_reserved;
    
    public String getTxn_sub_type()
    {
        return txn_sub_type;
    }
    
    public void setTxn_sub_type(String txn_sub_type)
    {
        this.txn_sub_type = txn_sub_type;
    }
    
    public String getBiz_type()
    {
        return biz_type;
    }
    
    public void setBiz_type(String biz_type)
    {
        this.biz_type = biz_type;
    }
    
    public String getTerminal_id()
    {
        return terminal_id;
    }
    
    public void setTerminal_id(String terminal_id)
    {
        this.terminal_id = terminal_id;
    }
    
    public String getMember_id()
    {
        return member_id;
    }
    
    public void setMember_id(String member_id)
    {
        this.member_id = member_id;
    }
    
    public String getPay_code()
    {
        return pay_code;
    }
    
    public void setPay_code(String pay_code)
    {
        this.pay_code = pay_code;
    }
    
    public String getAcc_no()
    {
        return acc_no;
    }
    
    public void setAcc_no(String acc_no)
    {
        this.acc_no = acc_no;
    }
    
    public String getId_card_type()
    {
        return id_card_type;
    }
    
    public void setId_card_type(String id_card_type)
    {
        this.id_card_type = id_card_type;
    }
    
    public String getId_card()
    {
        return id_card;
    }
    
    public void setId_card(String id_card)
    {
        this.id_card = id_card;
    }
    
    public String getId_holder()
    {
        return id_holder;
    }
    
    public void setId_holder(String id_holder)
    {
        this.id_holder = id_holder;
    }
    
    public String getMobile()
    {
        return mobile;
    }
    
    public void setMobile(String mobile)
    {
        this.mobile = mobile;
    }
    
    public String getValid_date()
    {
        return valid_date;
    }
    
    public void setValid_date(String valid_date)
    {
        this.valid_date = valid_date;
    }
    
    public String getValid_no()
    {
        return valid_no;
    }
    
    public void setValid_no(String valid_no)
    {
        this.valid_no = valid_no;
    }
    
    public String getTrans_id()
    {
        return trans_id;
    }
    
    public void setTrans_id(String trans_id)
    {
        this.trans_id = trans_id;
    }
    
    public String getTxn_amt()
    {
        return txn_amt;
    }
    
    public void setTxn_amt(String txn_amt)
    {
        this.txn_amt = txn_amt;
    }
    
    public String getTrade_date()
    {
        return trade_date;
    }
    
    public void setTrade_date(String trade_date)
    {
        this.trade_date = trade_date;
    }
    
    public String getCommodity_name()
    {
        return commodity_name;
    }
    
    public void setCommodity_name(String commodity_name)
    {
        this.commodity_name = commodity_name;
    }
    
    public String getCommodity_amount()
    {
        return commodity_amount;
    }
    
    public void setCommodity_amount(String commodity_amount)
    {
        this.commodity_amount = commodity_amount;
    }
    
    public String getUser_name()
    {
        return user_name;
    }
    
    public void setUser_name(String user_name)
    {
        this.user_name = user_name;
    }
    
    public String getPage_url()
    {
        return page_url;
    }
    
    public void setPage_url(String page_url)
    {
        this.page_url = page_url;
    }
    
    public String getReturn_url()
    {
        return return_url;
    }
    
    public void setReturn_url(String return_url)
    {
        this.return_url = return_url;
    }
    
    public String getAdditional_info()
    {
        return additional_info;
    }
    
    public void setAdditional_info(String additional_info)
    {
        this.additional_info = additional_info;
    }
    
    public String getReq_reserved()
    {
        return req_reserved;
    }
    
    public void setReq_reserved(String req_reserved)
    {
        this.req_reserved = req_reserved;
    }

    @Override
    public String toString()
    {
        return "BaofuRequestParam [txn_sub_type=" + txn_sub_type + ", biz_type=" + biz_type + ", terminal_id="
            + terminal_id + ", member_id=" + member_id + ", pay_code=" + pay_code + ", acc_no=" + acc_no
            + ", id_card_type=" + id_card_type + ", id_card=" + id_card + ", id_holder=" + id_holder + ", mobile="
            + mobile + ", valid_date=" + valid_date + ", valid_no=" + valid_no + ", trans_id=" + trans_id
            + ", txn_amt=" + txn_amt + ", trade_date=" + trade_date + ", commodity_name=" + commodity_name
            + ", commodity_amount=" + commodity_amount + ", user_name=" + user_name + ", page_url=" + page_url
            + ", return_url=" + return_url + ", additional_info=" + additional_info + ", req_reserved=" + req_reserved
            + "]";
    }
    
}
