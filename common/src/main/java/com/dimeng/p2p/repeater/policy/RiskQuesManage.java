/*
 * 文 件 名:  RiskQuesManage.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  heluzhu
 * 修改时间:  2016年3月9日
 */
package com.dimeng.p2p.repeater.policy;

import java.util.List;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.entities.T6147;
import com.dimeng.p2p.S61.entities.T6149;
import com.dimeng.p2p.repeater.policy.query.QuesQuery;
import com.dimeng.p2p.repeater.policy.query.ResultQuery;
import com.dimeng.p2p.repeater.policy.query.RiskAssessmentResult;
import com.dimeng.p2p.repeater.policy.query.RiskQueryResult;

/**
 * <风险评估问题>
 * <功能详细描述>
 * 
 * @author  heluzhu
 * @version  [版本号, 2016年3月9日]
 */
public abstract interface RiskQuesManage extends Service
{
    
    /**
     * 查询风险评估问题列表
     * @param title
     * @param status
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<QuesQuery> queryAllQues(String title, String status, Paging pag)
        throws Throwable;
    
    /**
     * 新增风险评估问题
     * @param t6149
     * @throws Throwable
     */
    public abstract int addRiskQues(T6149 t6149)
        throws Throwable;
    
    /**
     * 修改风险评估问题
     * @param t6149
     * @throws Throwable
     */
    public abstract void updateRiskQues(T6149 t6149, String oldStatus)
        throws Throwable;
    
    /**
     * 根据ID查询风险评估问题
     * @param id
     * @return
     * @throws Throwable
     */
    public abstract T6149 queryById(int id)
        throws Throwable;
    
    /**
     * 查询当前用户的风险评估能力
     * @return
     * @throws Throwable
     */
    public abstract T6147 getMyRiskResult()
        throws Throwable;
    
    /**
     * 用户中心风险评估问题列表
     * @return
     * @throws Throwable
     */
    public abstract List<QuesQuery> queryList()
        throws Throwable;
    
    /**
     * 风险评估
     * @return
     * @throws Throwable
     */
    public abstract int assessment(List<RiskAssessmentResult> results)
        throws Throwable;
    
    /**
     * 查询所有用户的风险评估结果
     * @param paging
     * @param resultQuery
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<RiskQueryResult> queryAllResult(Paging paging, ResultQuery resultQuery)
        throws Throwable;
    
    /**
     * 风险评估详情
     * @param riskId
     * @return
     * @throws Throwable
     */
    public abstract RiskQueryResult queryRiskDetail(int riskId)
        throws Throwable;
    
    /**
     * 查询自然年内剩余的风险评估次数
     * @return
     * @throws Throwable
     */
    public abstract int leftRiskCount()
        throws Throwable;
    
    /**
     * 查询启用风险评估问题数
     * @return int
     * @throws Throwable
     */
    public abstract int qyRiskCount()
        throws Throwable;
}
