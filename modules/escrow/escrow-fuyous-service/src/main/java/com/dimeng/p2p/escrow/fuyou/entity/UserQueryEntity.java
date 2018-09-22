package com.dimeng.p2p.escrow.fuyou.entity;

import java.io.Serializable;

/**
 * 
 * 描述:用户信息查询实体
 * 创建时间：2014年9月12日
 */
public class UserQueryEntity implements Serializable {

    /**
     * 注释内容
     */
    private static final long serialVersionUID = -5202799971284309495L;

    // 商户代码
    private String mchnt_cd;

    // 流水号
    private String mchnt_txn_ssn;

    // 交易日期
    private String mchnt_txn_dt;

    // 待查询的登录帐户列表
    private String user_ids;

    // 签名数据
    private String signature;
    
    public UserQueryEntity(){}
    
    public UserQueryEntity(String mchnt_cd, String mchnt_txn_ssn, 
            String mchnt_txn_dt, String user_ids, String signature){
        this.mchnt_cd = mchnt_cd;
        this.mchnt_txn_ssn = mchnt_txn_ssn;
        this.mchnt_txn_dt = mchnt_txn_dt;
        this.user_ids = user_ids;
        this.signature = signature;
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

    public String getMchnt_txn_dt() {
        return mchnt_txn_dt;
    }

    public void setMchnt_txn_dt(String mchnt_txn_dt) {
        this.mchnt_txn_dt = mchnt_txn_dt;
    }

    public String getUser_ids() {
        return user_ids;
    }

    public void setUser_ids(String user_ids) {
        this.user_ids = user_ids;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
    
}
