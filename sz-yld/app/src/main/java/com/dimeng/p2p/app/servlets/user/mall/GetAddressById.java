package com.dimeng.p2p.app.servlets.user.mall;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S63.entities.T6355;
import com.dimeng.p2p.app.service.BusinessManage;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.app.servlets.user.domain.mall.GetAddress;
import com.dimeng.util.parser.IntegerParser;

/**
 * 
 * 根据地址Id查询地址、 
 * 
 * @author  yuanguangjie
 * @version  [版本号, 2016年03月04日]
 */
public class GetAddressById extends AbstractSecureServlet
{
    
    private static final long serialVersionUID = 1L;

    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {

    	BusinessManage businessManage = serviceSession.getService(BusinessManage.class);

    	//地址Id
        final int id = IntegerParser.parse(getParameter(request, "id"));
        T6355 t6 = businessManage.getAddress(id);
        GetAddress address = new GetAddress();
        if (null != t6)
        {
        	//地址id
        	address.setId(t6.F01);
        	
        	//收件人
        	address.setName(t6.F03);
        	
        	//电话
        	address.setPhone(t6.F06);
        	
        	//所在地区
        	address.setRegion(t6.szdq);
        	
        	//区域
        	address.setRegionID(t6.F04);
        	
        	//用户Id
        	address.setUserid(t6.F02);
        	
        	//邮编
        	address.setVo(t6.F07);
        	
        	//是否默认地址
        	address.setYesOrNo(t6.F08);
        	
        	//详细地址
        	address.setAddress(t6.F05);
        }
        // 封装返回信息
        setReturnMsg(request, response, ExceptionCode.SUCCESS,"success!",address);
        
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
