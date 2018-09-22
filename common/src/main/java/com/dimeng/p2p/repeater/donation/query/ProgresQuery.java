/*
 * 文 件 名:  ProgresQuery.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  guomianyun
 * 修改时间:  2015年3月9日
 */
package com.dimeng.p2p.repeater.donation.query;

import com.dimeng.p2p.S62.enums.T6242_F11;
import com.dimeng.p2p.common.enums.SignType;

import java.sql.Timestamp;

/**
 * 公益标进展查询
 * <功能详细描述>
 * 
 * @author  guomianyun
 * @version  [版本号, 2015年3月9日]
 */
public interface ProgresQuery
{
    /**
     * 标ID
     * <功能详细描述>
     * @return
     */
    public abstract int getBidId();
    /**
     * 时间,大于等于查询.
     * 
     * @return {@link Timestamp}null无效.
     */
    public abstract Timestamp getCreateTimeStart();

    /**
     * 时间,小于等于查询.
     * 
     * @return {@link Timestamp}null无效.
     */
    public abstract Timestamp getCreateTimeEnd();
    
    /**
     * 完成时间,大于等于查询.
     * 
     * @return {@link Timestamp}null无效.
     */
    public abstract Timestamp getComTimeStart();

    /**
     * 完成时间,小于等于查询.
     * 
     * @return {@link Timestamp}null无效.
     */
    public abstract Timestamp getComTimeEnd();
    
    /**
     * 创建用户
     * <功能详细描述>
     * @return
     */
    public abstract String getSysName();
    
    /**
     * 公益方
     * <功能详细描述>
     * @return
     */
    public abstract String getGyName();
    
  
    
    /**
     * 借款标标题，模糊查询.
     * 
     * @return {@code String}空值无效.
     */
    public abstract String getLoanTitle();

    /**
     * 公益标编号
     * 
     * @return {@link SignType}空值无效.
     */
    public abstract String getBidNo();
    
    /**
     * 状态
     * <功能详细描述>
     * @return
     */
    public abstract T6242_F11 getStatus();
}
