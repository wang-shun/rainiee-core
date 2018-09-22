package com.dimeng.p2p.repeater.score.entity;


import java.sql.Timestamp;

import com.dimeng.p2p.S61.entities.T6105;

public class UserScoreAccountExt extends T6105 {
    
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
     * 注册时间开始
     */
    public Timestamp startTime;
    
    /**
     * 注册时间结束
     */
    public Timestamp endTime;

}
