package com.dimeng.p2p.user.servlets.account;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import com.dimeng.framework.http.session.authentication.AuthenticationException;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.account.user.service.achieve.PwdSafeCacheManage;
import com.dimeng.p2p.account.user.service.entity.PwdSafetyQuestion;
import com.dimeng.util.StringHelper;

public class QueryQuestion extends AbstractAccountServlet {

	private static final long serialVersionUID = 1L;

	protected void processPost(final HttpServletRequest request,
			HttpServletResponse response, final ServiceSession serviceSession)
			throws Throwable {
		PwdSafeCacheManage pwdSafeService = serviceSession.getService(PwdSafeCacheManage.class);
		List<PwdSafetyQuestion> questionList = pwdSafeService.getQuestionList();
		int questionId1 = StringHelper.isEmpty(request.getParameter("quesId1")) ? 0 : Integer.parseInt(request.getParameter("quesId1"));
		int questionId2 = StringHelper.isEmpty(request.getParameter("quesId2")) ? 0 : Integer.parseInt(request.getParameter("quesId2"));
		int questionId3 = StringHelper.isEmpty(request.getParameter("quesId3")) ? 0 : Integer.parseInt(request.getParameter("quesId3"));
		List<PwdSafetyQuestion> result = new ArrayList<PwdSafetyQuestion>();
		for(PwdSafetyQuestion ques : questionList){
			if(questionId1 == ques.id){
				continue;
			}
			if(questionId2 == ques.id){
				continue;
			}
			if(questionId3 == ques.id){
				continue;
			}
			result.add(ques);
		}
		ObjectMapper objectMapper = new ObjectMapper();
		PrintWriter out = response.getWriter();
		out.write(objectMapper.writeValueAsString(result));
		out.close();
	}
	
	@Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
	    
	    logger.error(throwable.getMessage());
        if (throwable instanceof AuthenticationException)
        {
            PrintWriter out = response.getWriter();
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> jsonMap = new HashMap<String, Object>();
            jsonMap.put("loginState", "未登录或会话超时,请重新登录");
            out.print(objectMapper.writeValueAsString(jsonMap));
            out.close();
        }
    }
	
}
