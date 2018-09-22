package com.dimeng.p2p.S62.entities;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S62.enums.T6233_F10;

/** 
 * 标附件列表(非公开)
 */
public class T6233 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 自增ID
     */
    public int F01;

    /** 
     * 标ID,参考T6230.F01
     */
    public int F02;

    /** 
     * 附件类型,参考T6212.F01
     */
    public int F03;

    /** 
     * 附件名称
     */
    public String F04;

    /** 
     * 附件大小
     */
    public int F05;

    /** 
     * 附件文件编码
     */
    public String F06;

    /** 
     * 附件格式
     */
    public String F07;

    /** 
     * 上传时间
     */
    public Timestamp F08;

    /** 
     * 上传人ID,参考T7110.F01,后台上传时填入
     */
    public int F09;
    /**
     * 是否是封面图标
     */
    public T6233_F10 F10;

}
