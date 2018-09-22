package com.dimeng.p2p.repeater.score.entity;

import java.sql.Timestamp;

import com.dimeng.p2p.S61.entities.T6106;
import com.dimeng.p2p.S61.enums.T6106_F05;

public class UserScoreExt extends T6106 {

    private static final long serialVersionUID = 50133751806614060L;
    
    /**
     * 用户名
     */
    public String loginName;
    
    /**
     * 真是姓名
     */
    public String realName;
    
    /**
     * 获取时间开始
     */
    public Timestamp startTime;
    
    /**
     * 获取时间结束
     */
    public Timestamp endTime;
    
    /**
     * 积分类型
     */
    public T6106_F05 scoreType;

}
