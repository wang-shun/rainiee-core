/*
 * 文 件 名:  RiskQuestions.java
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
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.app.servlets.user.domain.Questions;
import com.dimeng.p2p.repeater.policy.RiskQuesManage;
import com.dimeng.p2p.repeater.policy.query.QuesQuery;

/**
 * 风估问题
 * 
 * @author  zhoulantao
 * @version  [版本号, 2016年3月22日]
 */
public class RiskQuestions extends AbstractSecureServlet
{
    private static final long serialVersionUID = -7394914977611849724L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        final RiskQuesManage riskQuesManage = serviceSession.getService(RiskQuesManage.class);
        final List<QuesQuery> questions = riskQuesManage.queryList();
        
        // 获取问题列表
        List<Questions> qs = null;
        if (null != questions && questions.size() > 0)
        {
            qs = new ArrayList<Questions>();
            
            for (QuesQuery query : questions)
            {
                Questions question = new Questions();
                question.setQuestionId(query.F01);
                question.setQuestion(query.F02);
                question.setAnswerA(query.F03);
                question.setAnswerB(query.F04);
                question.setAnswerC(query.F05);
                question.setAnswerD(query.F06);
                
                qs.add(question);
            }
        }
        
        // 返回问题列表
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success!", qs);
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
