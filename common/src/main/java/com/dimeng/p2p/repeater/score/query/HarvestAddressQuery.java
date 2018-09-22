package com.dimeng.p2p.repeater.score.query;

import com.dimeng.p2p.common.enums.YesOrNo;

/**
 * 用户收货信息
 * @author Administrator
 *
 */
public interface HarvestAddressQuery
{
    
    /**
    * 收货人
    * @return
    */
    public abstract String getHarvestName();

    /**
     * 区域
     * @return
     */
    public abstract int getCity();

    /**
     * 详细地址
     * @return
     */
    public abstract String getFullAddress();

    /**
     * 联系电话
     * @return
     */
    public abstract String getPhone();
    
    /**
    * 邮编
    * @return
    */
    public abstract String getPostcode();
    
    /**
    * 是否为默认地址（yes：是，no：否）
    * @return
    */
    public abstract YesOrNo getStatus();
}
