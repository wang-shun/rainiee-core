package com.dimeng.p2p.repeater.policy.query;/*
 * 文 件 名:  RiskQueryResult.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  heluzhu
 * 修改时间: 2016/3/10
 */

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class RiskQueryResult {
    /**
     * 风险评估ID
     */
    public int riskId;
    /**
     * 用户名
     */
    public String userName;

    /**
     * 真实姓名
     */
    public String realName;

    /**
     * 评估分值
     */
    public int score;

    /**
     * 评估类型
     */
    public String riskType;

    /**
     * 评估时间
     */
    public Timestamp time;

    /**
     * 评估问题list
     */
    public List<AnswerQuery> answerList = new ArrayList<AnswerQuery>();
}
