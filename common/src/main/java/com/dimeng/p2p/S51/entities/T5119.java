package com.dimeng.p2p.S51.entities
;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S51.enums.T5119_F11;
import com.dimeng.p2p.S51.enums.T5119_F13;

/** 
 * 中国行政区划
 */
public class T5119 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * ID,6位数字
     */
    public int F01;

    /** 
     * 省级ID
     */
    public short F02;

    /** 
     * 市级ID
     */
    public short F03;

    /** 
     * 县级ID
     */
    public short F04;

    /** 
     * 名称,最多15个字符
     */
    public String F05;

    /** 
     * 省级名称,最多15个字符
     */
    public String F06;

    /** 
     * 市级名称,最多15个字符
     */
    public String F07;

    /** 
     * 县级名称,最多15个字符
     */
    public String F08;

    /** 
     * 电话区号,最多4个字符
     */
    public String F09;

    /** 
     * 邮政编码,最多5个字符
     */
    public String F10;

    /** 
     * 级别,SHENG:省级;SHI:市级;XIAN:县级
     */
    public T5119_F11 F11;

    /** 
     * 简拼,最多15个字符
     */
    public String F12;

    /** 
     * 启用状态,QY:启用;TY:停用;
     */
    public T5119_F13 F13;

}
