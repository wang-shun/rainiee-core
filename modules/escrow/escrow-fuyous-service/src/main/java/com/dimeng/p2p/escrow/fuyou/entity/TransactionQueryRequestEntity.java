package com.dimeng.p2p.escrow.fuyou.entity;

/**
 * 
 * 交易查询接口请求实本类
 * 
 * @author heshiping
 * @version [版本号, 2015年5月25日]
 */
public class TransactionQueryRequestEntity {
    // 商户代码(必填)
    private String mchnt_cd;
    // 流水号(必填)
    private String mchnt_txn_ssn;
    // 交易类型(必填)
    private String busi_tp;
    // 起始时间(必填)
    private String start_day;
    // 截止时间(必填)
    private String end_day;
    // 交易流水(可选-用于查询某笔交易)
    private String txn_ssn;
    // 交易用户(可选)
    private String cust_no;
    // 交易状态(可选)
    private String txn_st;
    // 交易备注(可选)
    private String remark;
    // 页码(可选)
    private String page_no;
    // 每页条数 (可选)
    private String page_size;
    // 签名数据(必填)
    private String signature;

    public TransactionQueryRequestEntity() {

    }

    public TransactionQueryRequestEntity(String mchnt_cd, String mchnt_txn_ssn,
            String busi_tp, String start_day, String end_day, String txn_ssn,
            String cust_no, String txn_st, String remark, String page_no,
            String page_size) {
        this.mchnt_cd = mchnt_cd;
        this.mchnt_txn_ssn = mchnt_txn_ssn;
        this.busi_tp = busi_tp;
        this.start_day = start_day;
        this.end_day = end_day;
        this.txn_ssn = txn_ssn;
        this.cust_no = cust_no;
        this.txn_st = txn_st;
        this.remark = remark;
        this.page_no = page_no;
        this.page_size = page_size;
    }

    public String getMchnt_cd() {
        return mchnt_cd;
    }

    public void setMchnt_cd(String mchnt_cd) {
        this.mchnt_cd = mchnt_cd;
    }

    public String getMchnt_txn_ssn() {
        return mchnt_txn_ssn;
    }

    public void setMchnt_txn_ssn(String mchnt_txn_ssn) {
        this.mchnt_txn_ssn = mchnt_txn_ssn;
    }

    public String getBusi_tp() {
        return busi_tp;
    }

    public void setBusi_tp(String busi_tp) {
        this.busi_tp = busi_tp;
    }

    public String getStart_day() {
        return start_day;
    }

    public void setStart_day(String start_day) {
        this.start_day = start_day;
    }

    public String getEnd_day() {
        return end_day;
    }

    public void setEnd_day(String end_day) {
        this.end_day = end_day;
    }

    public String getTxn_ssn() {
        return txn_ssn;
    }

    public void setTxn_ssn(String txn_ssn) {
        this.txn_ssn = txn_ssn;
    }

    public String getCust_no() {
        return cust_no;
    }

    public void setCust_no(String cust_no) {
        this.cust_no = cust_no;
    }

    public String getTxn_st() {
        return txn_st;
    }

    public void setTxn_st(String txn_st) {
        this.txn_st = txn_st;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPage_no() {
        return page_no;
    }

    public void setPage_no(String page_no) {
        this.page_no = page_no;
    }

    public String getPage_size() {
        return page_size;
    }

    public void setPage_size(String page_size) {
        this.page_size = page_size;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
