package com.dimeng.p2p.app.servlets.user.domain;

import java.io.Serializable;

/**
 * 
 * 充值的参数信息
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年12月22日]
 */
public class Chargepinfo  implements Serializable
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 4539963746000779718L;

    /**
     * 充值最低金额(元)
     */
    private String min;
    
    /**
     * 充值最高金额(元)
     */
    private String max;
    
    /**
     * 用户充值手续费率
     */
    private String p;
    
    /**
     * 充值最高手续费（元）
     */
    private String pMax;
    
    /**
     * 是否需要email认证  true: 需要 false: 不需要
     */
    private String isNeedEmail;
    
    /**
     * 是否需要实名认证  true: 需要 false: 不需要
     */
    private String isNeedNciic;
    
    /**
     * 是否需要手机认证  true: 需要 false: 不需要
     */
    private String isNeedPhone;
    
    /**
     * 是否需要交易密码  true: 需要 false: 不需要
     */
    private String isNeedPsd;
    /**
     * 温馨提示
     */
    private String cwxts;
    
    public String getMin()
    {
        return min;
    }
    
    public void setMin(String min)
    {
        this.min = min;
    }
    
    public String getMax()
    {
        return max;
    }
    
    public void setMax(String max)
    {
        this.max = max;
    }
    
    public String getP()
    {
        return p;
    }
    
    public void setP(String p)
    {
        this.p = p;
    }
    
    public String getpMax()
    {
        return pMax;
    }
    
    public void setpMax(String pMax)
    {
        this.pMax = pMax;
    }
    
    public String getIsNeedEmail()
    {
        return isNeedEmail;
    }
    
    public void setIsNeedEmail(String isNeedEmail)
    {
        this.isNeedEmail = isNeedEmail;
    }
    
    public String getIsNeedNciic()
    {
        return isNeedNciic;
    }
    
    public void setIsNeedNciic(String isNeedNciic)
    {
        this.isNeedNciic = isNeedNciic;
    }
    
    public String getIsNeedPhone()
    {
        return isNeedPhone;
    }
    
    public void setIsNeedPhone(String isNeedPhone)
    {
        this.isNeedPhone = isNeedPhone;
    }
    
    public String getIsNeedPsd()
    {
        return isNeedPsd;
    }
    
    public void setIsNeedPsd(String isNeedPsd)
    {
        this.isNeedPsd = isNeedPsd;
    }

	public String getCwxts() {
		return cwxts;
	}

	public void setCwxts(String cwxts) {
		this.cwxts = cwxts;
	}

    /** {@inheritDoc} */
     
    @Override
    public String toString()
    {
        return "Chargepinfo [min=" + min + ", max=" + max + ", p=" + p + ", pMax=" + pMax + ", isNeedEmail="
            + isNeedEmail + ", isNeedNciic=" + isNeedNciic + ", isNeedPhone=" + isNeedPhone + ", isNeedPsd=" + isNeedPsd
            + ", cwxts=" + cwxts + "]";
    }
    
    
    
}
