/*
 * 文 件 名:  ExperienceQuery.java
 * 版    权:  © 2014 DM. All rights reserved.
 * 描    述:  <描述>
 * 修 改 人:  linxiaolin
 * 修改时间:  2014/9/25
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */

package com.dimeng.p2p.modules.account.console.experience.service.query;


import java.sql.Timestamp;

import com.dimeng.p2p.S61.enums.T6103_F06;

/**
 * 体验金列表查询条件
 */
public interface ExperienceDetailQuery {
    /**
     * 用户名
     * @return String
     */
    public String userName();

    /**
     * 标编号
     * @return
     */
    public String bid();

    /**
     * 失效开始时间
     */
    public Timestamp invalidStartTime();
    /**
     * 失效结束时间
     */
    public Timestamp invalidEndTime();

    /**
     * 利息生效开始时间
     */
    public Timestamp lixiStartTime();
    /**
     * 利息生效结束时间
     */
    public Timestamp lixiEndTime();

    /**
     * 体检金状态
     * @return
     */
    public T6103_F06 status();

}
