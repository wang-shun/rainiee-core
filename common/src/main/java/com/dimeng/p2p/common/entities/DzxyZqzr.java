/*
 * 文 件 名:  DzxyZqzr.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  God
 * 修改时间:  2016年6月29日
 */
package com.dimeng.p2p.common.entities;

import java.math.BigDecimal;

import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.entities.T6141;
import com.dimeng.p2p.S61.entities.T6161;
import com.dimeng.p2p.S62.entities.T6231;
import com.dimeng.p2p.S62.entities.T6251;
import com.dimeng.p2p.S62.entities.T6260;
import com.dimeng.p2p.S62.entities.T6262;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  God
 * @version  [版本号, 2016年6月29日]
 */
public class DzxyZqzr
{
    /**
     * 协议编号
     */
    public String xy_no;
    
    /**
     * 用户名（乙方）
     */
    public String zqzr_yf_loginName;
    
    /**
     * 用户类型（乙方）
     */
    public String zqzr_yf_userType;
    
    /**
     * 真实姓名(乙方)
     */
    public String zqzr_yf_realName;
    
    /**
     * 公司名称(乙方)
     */
    public String zqzr_yf_companyName;
    
    /**
     * 营业执照号/社会信用代码(乙方)
     */
    public String zqzr_yf_papersNum;
    
    /**
     * 身份证号(乙方)
     */
    public String zqzr_yf_sfzh;
    
    /**
     * 标的主信息
     */
    public Bdxq bdxq;
    
    /**
     * 标的扩展信息
     */
    public T6231 t6231;
    
    /**
     * 债权用户信息
     */
    public T6110 t6110;
    
    /**
     * 债权购买用户信息
     */
    public T6110 buyT6110;
    
    /**
     * 债权用户基本信息
     */
    public T6141 t6141;
    
    /**
     * 债权用户公司信息
     */
    public T6161 t6161;
    
    /**
     * 债权购买用户公司信息
     */
    public T6161 buyT6161;
    
    /**
     * 线上债权转让申请
     */
    public T6260 t6260;
    
    /**
     * 线上债权转让记录
     */
    public T6262 t6262;
    
    /**
     * 标债权记录
     */
    public T6251 t6251;
    
    /**
     * 月还本息数额
     */
    public BigDecimal zqzr_bid_ychbxse = BigDecimal.ZERO;
    
    /**
     * 债权人待收本息
     */
    public BigDecimal zqzr_zqr_dsbx = BigDecimal.ZERO;
}
