package com.dimeng.p2p.S71.entities
;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S71.enums.T7122_F03;
import java.sql.Blob;
import java.sql.Timestamp;

/** 
 * 实名认证信息库
 */
public class T7122 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 身份证号
     */
    public String F01;

    /** 
     * 姓名
     */
    public String F02;

    /** 
     * 认证状态,TG:通过;SB:失败;
     */
    public T7122_F03 F03;

    /** 
     * 图像
     */
    public Blob F04;

    /** 
     * 最后更新日期
     */
    public Timestamp F05;

}
