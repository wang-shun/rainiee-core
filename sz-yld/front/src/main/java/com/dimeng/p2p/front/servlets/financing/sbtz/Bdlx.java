/*
 * 文 件 名:  Bdlx.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2015年12月31日
 */
package com.dimeng.p2p.front.servlets.financing.sbtz;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S62.entities.T6211;
import com.dimeng.p2p.S62.entities.T6216;
import com.dimeng.p2p.front.servlets.financing.AbstractFinancingServlet;
import com.dimeng.p2p.modules.bid.front.service.BidManage;
import com.dimeng.util.StringHelper;

/**
 * 根据产品名称获取标的类型
 * 
 * @author  xiaoqi
 * @version  [版本号, 2015年12月31日]
 */
public class Bdlx extends AbstractFinancingServlet {

	/**
	 * 注释内容
	 */
	private static final long serialVersionUID = -2958808792708342432L;

	 
	@Override
	protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		BidManage bidManage = serviceSession.getService(BidManage.class);
		PrintWriter out = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        if(StringHelper.isEmpty(request.getParameter("id"))){
        	return ;
        }
        Integer id=Integer.parseInt(request.getParameter("id"));
        T6216 t6216=bidManage.getProductById(id);
        T6211[] t6211=bidManage.getBidTypeByCondition(t6216.F03);
        out.print(objectMapper.writeValueAsString(t6211));
        out.close();
	}

}
