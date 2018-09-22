/*
 * 文 件 名:  RiskAssessment.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2016年3月22日
 */
package com.dimeng.p2p.app.servlets.user;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S61.entities.T6147;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.app.servlets.user.domain.UserRisk;
import com.dimeng.p2p.repeater.policy.RiskQuesManage;
import com.dimeng.p2p.repeater.policy.query.RiskAssessmentResult;

/**
 * 风险评估
 * 
 * @author  zhoulantao
 * @version  [版本号, 2016年3月22日]
 */
public class RiskAssessment extends AbstractSecureServlet
{
    private static final long serialVersionUID = 2140166062156529160L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        // 获取题目及对应的答案,答案格式为  题目1Id_题目1答案,题目2Id_题目2答案
        final String answer = getParameter(request, "answer");
        
        // 划分题目
        List<RiskAssessmentResult> results = null;
        if (answer.indexOf(',') != -1)
        {
            final String[] answers = answer.split(",");
            
            results = new ArrayList<RiskAssessmentResult>(answers.length);
            
            for (String wer : answers)
            {
                // 划分题目及答案
                if (wer.indexOf("_") != -1)
                {
                    final String[] wers = wer.split("_");
                    
                    RiskAssessmentResult result = new RiskAssessmentResult();
                    result.quesId = Integer.parseInt(wers[0]);
                    result.answer = wers[1];
                    
                    results.add(result);
                }
            }
        }
        
        if (null != results && results.size() > 0)
        {
            try
            {
                // 计算得分
                final RiskQuesManage manage = serviceSession.getService(RiskQuesManage.class);
                final int count = manage.assessment(results);
                
                if (count > 0)
                {
                    final RiskQuesManage riskQuesManage = serviceSession.getService(RiskQuesManage.class);
                    
                    // 查询用户风估类型
                    final T6147 t6147 = riskQuesManage.getMyRiskResult();
                    UserRisk risk = new UserRisk();
                    if (null != t6147)
                    {
                        // 获取用户积分
                        risk.setScore(t6147.F03);
                        
                        // 获取用户等级
                        risk.setRiskType(t6147.F04.getChineseName());
                    }
                    
                    // 查询用户剩余评估次数
                    final int resultCount = riskQuesManage.leftRiskCount();
                    risk.setRiskTimes(resultCount);
                    
                    // 返回页面信息
                    setReturnMsg(request, response, ExceptionCode.SUCCESS, "success!", risk);
                    return;
                }
            }
            catch (Exception e)
            {
                // 返回页面信息
                setReturnMsg(request, response, ExceptionCode.UNKNOWN_ERROR, e.getMessage());
                return;
            }
        }
        
        // 返回页面信息
        setReturnMsg(request, response, ExceptionCode.UNKNOWN_ERROR, "评估失败");
        return;
    }
    
    @Override
    protected void processGet(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
}
