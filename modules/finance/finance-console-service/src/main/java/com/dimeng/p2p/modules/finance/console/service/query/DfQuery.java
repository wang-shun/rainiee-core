/*
 * 文 件 名:  DfQuery.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  dingjinqing
 * 修改时间:  2015年3月12日
 */
package com.dimeng.p2p.modules.finance.console.service.query;

import com.dimeng.p2p.S51.enums.T5131_F02;
import com.dimeng.p2p.S62.enums.T6230_F10;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  dingjinqing
 * @version  [版本号, 2015年3月12日]
 */
public abstract interface DfQuery
{
    
    /**
     * 标编号，模糊查询.
     * 
     * @return {@code String}空值无效.
     */
    public abstract String getBidNo();
    
    
    /**
     * 借款标题，模糊查询.
     * 
     * @return {@code String}空值无效.
     */
    public abstract String getLoanTitle();
    
    /**
     * 逾期开始天数，模糊查询.
     * 
     * @return {@code String}空值无效.
     */
    public abstract int getYuqiFromDays();
    
    /**
     * 逾期结束天数，模糊查询.
     * 
     * @return {@code String}空值无效.
     */
    public abstract int getYuqiEndDays();
    
    
    /**
     * 还款方式，模糊查询.
     * 
     * @return {@code String}空值无效.
     */
    public abstract T6230_F10 getHkfs();
    
    /**
     * 垫付方式，模糊查询.
     * 
     * @return {@code String}空值无效.
     */
    public abstract T5131_F02 getDffs();
    
    /**
     * 借款人，模糊查询.
     * 
     * @return {@link String}空值无效.
     */
    public abstract String getLoanName();
}
