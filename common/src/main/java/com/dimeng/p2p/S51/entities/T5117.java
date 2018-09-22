package com.dimeng.p2p.S51.entities
;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S51.enums.T5117_F01;
import java.sql.Timestamp;

/** 
 * 协议条款
 */
public class T5117 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 协议类型,BXBZ:本息保障计划;ZCXY:注册协议;DBBXY:担保标协议(借款协议(担保));XYRZBXY:信用认证标协议(借款协议(信));SDRZBXY:实地认证标协议(借款协议(实));YXLCXY;优选理财协议范本;ZQZRXY:债权转让协议(债权转让及受让协议);JKXYSB:借款协议(实/保);ZQZRSMS:债权转让说明书;GRDYDBBXY:个人抵押担保标协议;GRXYBXY:个人信用标协议;QYDYDBBXY:企业抵押担保标协议;QYXYBXY:企业信用标协议;XXZQZRXY:线下债权转让协议;XSZQZRXY:线上债权转让协议;
     */
    public T5117_F01 F01;

    /** 
     * 浏览次数
     */
    public int F02;

    /** 
     * 协议内容
     */
    public String F03;

    /** 
     * 创建者,参考T7011.F01
     */
    public int F04;

    /** 
     * 创建时间
     */
    public Timestamp F05;

    /** 
     * 最后修改时间
     */
    public Timestamp F06;

}
