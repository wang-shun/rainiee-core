package com.dimeng.p2p.console.servlets.account;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import com.dimeng.framework.http.session.authentication.AuthenticationException;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S50.entities.T5019;
import com.dimeng.p2p.modules.base.console.service.DistrictManage;

/**
 * 通过ajax获取省市县信息
 * 
 * @author raoyujun
 * @version [版本号, 2016年8月23日]
 */
public class GetAddrForAjax extends AbstractAccountServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void processPost(final HttpServletRequest request,
			HttpServletResponse response, final ServiceSession serviceSession)
			throws Throwable {
		DistrictManage districtManage = serviceSession
				.getService(DistrictManage.class);
		int id = Integer.parseInt(request.getParameter("id"));
		String type = request.getParameter("type");

		ObjectMapper objectMapper = new ObjectMapper();
		PrintWriter out = response.getWriter();
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		if (type.equals("sheng")) {
			T5019[] shis = districtManage.getShi(id);
			jsonMap.put("shis", shis);
		} else {
			T5019[] xians = districtManage.getXian(id);
			jsonMap.put("xians", xians);
		}
		jsonMap.put("num", "0000");
		out.print(objectMapper.writeValueAsString(jsonMap));
		out.close();
	}

	@Override
	protected void onThrowable(HttpServletRequest request,
			HttpServletResponse response, Throwable throwable)
			throws ServletException, IOException {
		getResourceProvider().log(throwable);
		ObjectMapper objectMapper = new ObjectMapper();
		PrintWriter out = response.getWriter();
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		if (throwable instanceof SQLException) {
			jsonMap.put("num", "9999");
			jsonMap.put("msg", "系统繁忙，请您稍后再试！");
			out.print(objectMapper.writeValueAsString(jsonMap));
			out.close();
			return;
		} else if (throwable instanceof ParameterException
				|| throwable instanceof LogicalException) {
			jsonMap.put("num", "0001");
			jsonMap.put("msg", throwable.getMessage());
			out.print(objectMapper.writeValueAsString(jsonMap));
			out.close();
			return;
		} else if (throwable instanceof AuthenticationException) {
			jsonMap.put("num", "0002");
			jsonMap.put("msg", throwable.getMessage());
			out.print(objectMapper.writeValueAsString(jsonMap));
			out.close();
			return;
		} else {
			jsonMap.put("num", "9999");
			jsonMap.put("msg", throwable.getMessage());
			out.print(objectMapper.writeValueAsString(jsonMap));
			out.close();
			return;
		}
	}

}
