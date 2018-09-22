package com.dimeng.p2p.repeater.score;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.List;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.entities.T6106;
import com.dimeng.p2p.S61.enums.T6106_F05;
import com.dimeng.p2p.S62.entities.T6250;
import com.dimeng.p2p.S62.enums.T6250_F07;
import com.dimeng.p2p.S62.enums.T6250_F08;
import com.dimeng.p2p.S63.entities.T6353;
import com.dimeng.p2p.S63.entities.T6354;
import com.dimeng.p2p.S63.entities.T6357;
import com.dimeng.p2p.S65.entities.T6555;
import com.dimeng.p2p.repeater.score.entity.ScoreCleanZero;
import com.dimeng.p2p.repeater.score.entity.ScoreCleanZeroQuery;
import com.dimeng.p2p.repeater.score.entity.SetCondition;
import com.dimeng.p2p.repeater.score.entity.SetScore;

/**
 * 积分设置
 * @author  zhoucl
 * @date 2015年12月9日
 */
public interface SetScoreManage extends Service
{
    
    /**
     * <获取积分数值设置>
     * @return SetScore
     */
    public abstract SetScore getSetScore()
        throws Throwable;
    
    /**
     * <获取积分范围设置>
     * @param type
     * @return List<T6353>
     */
    public abstract List<T6353> getT6353List(String type)
        throws Throwable;
    
    /**
     * <更新积分设置>
     * @param setScore
     * @param remindType
     * @param minScore
     * @param maxScore
     * @return int
     */
    public abstract void updateSetScore(SetScore setScore)
        throws Throwable;
    
    /**
     * <新增积分规则说明表>
     * @param F02
     * @param F03
     * @return int
     */
    public abstract void addT6354(String F02, String F03)
        throws Throwable;
    
    /**
     * <更新积分规则说明表>
     * @param F01
     * @param F02
     * @param F03
     * @return int
     */
    public abstract void updateT6354(Integer F01, String F02, String F03)
        throws Throwable;
    
    /**
     * <积分规则说明表>
     * @return T6354
     */
    public abstract T6354 getT6354()
        throws Throwable;
    
    /**
     * 
     * <dl>
     * 描述：查询积分清零设置表.
     * </dl>
     * @param query
     * @param paging
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<ScoreCleanZero> searchScoreCleanZero(ScoreCleanZeroQuery query, Paging paging)
        throws Throwable;
    
    /**
     * <清除积分>
     * @param startTime
     * @param endTime
     */
    public abstract void cleanUpScore(String startTime, String endTime)
        throws Throwable;
    
    /**
     * 导出积分清零
     * <功能详细描述>
     * @param scoreCleanZero
     * @param outputStream
     * @param charset
     * @throws Throwable
     */
    public void exportScoreCleanZero(ScoreCleanZero[] scoreCleanZero, OutputStream outputStream, String charset)
        throws Throwable;
    
    /**
     * <更新筛选条件范围表>
     * @param setCondition
     */
    public abstract void updateT353(SetCondition setCondition)
        throws Throwable;
    
    /**
     * <赠送积分，异常不影响其它操作>
     * @param userId 用户Id
     * @param F05 积分类型
     * @param amount 金额
     */
    public abstract int giveScore(Integer userId, T6106_F05 F05, BigDecimal amount);
    
    /**
     * <积分充值>
     * @param userId 用户Id
     * @param amount 金额
     */
    public abstract int chargeScore(Integer userId, int amount)
        throws Throwable;
    
    /**
     * <获取有效投资记录>
     * @param loanId
     * @param F07
     * @param F08
     * @return List<T6250>
     */
    public abstract List<T6250> getT6250List(Integer loanId, T6250_F07 F07, T6250_F08 F08)
        throws Throwable;
    
    /**
     * <积分清零设置表>
     * @return T6357
     */
    public abstract T6357 getT6357()
        throws Throwable;
    
    /**
     * <用户积分获取记录>
     * @return T6106
     */
    public abstract T6106 getT6106()
        throws Throwable;
    
    /**
     * <查询现金购买商品总金额>
     * @return orderId
     * @return T6555
     */
    public abstract T6555 getT6555(int orderId)
        throws Throwable;


    /**
     * <删除筛选条件>
     * @return orderId
     * @return T6555
     */
    public abstract void delT6353(int id) throws Throwable;
}
