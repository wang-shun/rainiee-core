package com.dimeng.p2p.S62.entities
;

import com.dimeng.framework.service.AbstractEntity;
import java.sql.Timestamp;

/** 
 * 标附件列表(公开)
 */
public class T6232 extends AbstractEntity{

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
     * 附件文件编码
     */
    public String F04;

    /** 
     * 附件名称
     */
    public String F05;

    /** 
     * 附件大小
     */
    public int F06;

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

}
