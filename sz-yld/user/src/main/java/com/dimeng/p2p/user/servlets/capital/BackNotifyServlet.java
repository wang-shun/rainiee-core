package com.dimeng.p2p.user.servlets.capital;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class BackNotifyServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//resp.getOutputStream().write("不支持GET方式提交".getBytes("utf8"));
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)	throws ServletException, IOException {
			Map<String,String[]> params = req.getParameterMap();
			Set<String> keySet = params.keySet();
			Iterator<String> i = keySet.iterator();
			System.out.println("回调请求："+req.getRequestURL());
			while (i.hasNext()) {
				String key = i.next();
				String[] values =  params.get(key);
				System.out.println("回调参数："+(key+":"+values[0]));
				resp.getOutputStream().write((key+":"+values[0]).getBytes("utf8"));
			}
	
	}
	
	

}
