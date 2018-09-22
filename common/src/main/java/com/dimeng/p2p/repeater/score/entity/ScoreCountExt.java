package com.dimeng.p2p.repeater.score.entity;

import com.dimeng.p2p.S61.entities.T6105;

public class ScoreCountExt extends T6105
{

    private static final long serialVersionUID = 50133751806614060L;
    
    /**
     * 总积分
     */
    public int SumScore;
    
    /**
     * 已使用总积分
     */
    public int SumUsedScore;
    
    /**
     * 总兑换次数
     */
    public int SumExchangeNum;
    
    /**
     * 总可用积分
     */
    public int SumUnUsedScore;
}
