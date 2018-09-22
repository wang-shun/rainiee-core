package com.dimeng.p2p.user.servlets.policy;/*
 * 文 件 名:  RiskAssessment.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  heluzhu
 * 修改时间: 2016/3/9
 */

import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.common.FormToken;
import com.dimeng.p2p.repeater.policy.RiskQuesManage;
import com.dimeng.p2p.repeater.policy.query.QuesQuery;
import com.dimeng.p2p.repeater.policy.query.RiskAssessmentResult;
import com.dimeng.util.StringHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class RiskAssessment extends AbstractURServlet {
    /**
	 * 注释内容
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession) throws Throwable {
        //processPost(request, response, serviceSession);
        RiskQuesManage manage = serviceSession.getService(RiskQuesManage.class);
        List<QuesQuery> list = manage.queryList();
        request.setAttribute("list",list);
        forwardView(request,response,getClass());
    }

    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession) throws Throwable {
        try {
            if(!FormToken.verify(serviceSession.getSession(),
                    request.getParameter(FormToken.parameterName()))) {
                throw new LogicalException("请不要重复提交请求！");
            }
            RiskQuesManage manage = serviceSession.getService(RiskQuesManage.class);
            //提交测试前，判断该用户本年内的剩余评估次数
            if(manage.leftRiskCount()<=0){
                throw new ParameterException("评估失败，评估次数不足");
            }
            List<QuesQuery> list = manage.queryList();
            List<RiskAssessmentResult> results = null;
            for(QuesQuery quesQuery : list){
                RiskAssessmentResult result = new RiskAssessmentResult();
                if(StringHelper.isEmpty(request.getParameter("quesId_"+quesQuery.F01)) || StringHelper.isEmpty(request.getParameter("answer_"+quesQuery.F01))){
                    continue;
                }
                result.quesId = Integer.parseInt(request.getParameter("quesId_"+quesQuery.F01));
                result.answer = request.getParameter("answer_"+quesQuery.F01);
                if(results == null){
                    results = new ArrayList<RiskAssessmentResult>();
                }
                results.add(result);
            }

            if(results == null || results.size() <= 0){
                throw new ParameterException("评估失败");
            }

            int id = manage.assessment(results);
            if(id > 0){
                //getController().sendRedirect(request,response,getController().getURI(request,AssessmentResult.class));
            	prompt(request, response, PromptLevel.INFO,"评估成功");
            	request.setAttribute("t6147", manage.getMyRiskResult());
            	processGet(request, response, serviceSession);
                return;
            }else{
                prompt(request, response, PromptLevel.ERROR, "评估失败");
                forwardView(request,response,getClass());
            }

        }catch (Exception e){
            prompt(request, response, PromptLevel.ERROR, e.getMessage());
            logger.error(e, e);
            processGet(request, response, serviceSession);
        }

    }
}
