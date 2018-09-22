package com.dimeng.p2p.common.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.dimeng.p2p.S62.entities.T6230;
import com.dimeng.p2p.S62.entities.T6231;

/**
 * 电子协议-不良债权转让
 * @author huqinfu
 *
 */
public class DzxyBlzqzr
{
    /**
     * 协议编号
     */
    public String xy_no;
    
    /**
     * 转让人信息（甲方）
     */
    public List<DzxyZrr> jf_zrr;
    
    /**
     * 用户名（乙方）
     */
    public String yf_loginName;
    
    /**
     * 真实姓名(乙方)
     */
    public String yf_realName;
    
    /**
     * 公司名称(乙方)
     */
    public String yf_companyName;
    
    /**
     * 营业执照号/社会信用代码(乙方)
     */
    public String yf_papersNum;
    
    /**
     * 身份证号(乙方)
     */
    public String yf_sfzh;
    
    /**
     * 待还期数
     */
    public int dhPeriods;
    
    /**
     * 总期数
     */
    public int zPeriods;
    
    /**
     * 待还本息
     */
    public BigDecimal dhbx = BigDecimal.ZERO;
    
    /**
     * 债权价值
     */
    public BigDecimal zqjz = BigDecimal.ZERO;
    
    /**
     * 转让价格
     */
    public BigDecimal zrjg = BigDecimal.ZERO;
    
    /**
     * 标的信息
     */
    public T6230 t6230;
    
    /**
     * 标的扩展信息
     */
    public T6231 t6231;
    
    /**
     * 转让时间
     */
    public Timestamp zrTime;
}
