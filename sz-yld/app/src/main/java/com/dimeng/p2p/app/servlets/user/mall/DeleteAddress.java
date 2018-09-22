package com.dimeng.p2p.app.servlets.user.mall;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.repeater.score.UserCenterScoreManage;
import com.dimeng.util.parser.IntegerParser;

/**
 * 
 * 删除收件地址
 * 
 * @author  jsaon
 * @version  [版本号, 2016年02月25日]
 */
public class DeleteAddress extends AbstractSecureServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        final String id = getParameter(request, "id");//ID
      
        UserCenterScoreManage userCenterScoreManage = serviceSession.getService(UserCenterScoreManage.class);
        
        //根据Id删除指定的地址
        final int num = userCenterScoreManage.deleteAddress(IntegerParser.parse(id));
        if (num != 0)
        {
        	setReturnMsg(request, response, ExceptionCode.SUCCESS, "success");
        }
        else
        {
        	setReturnMsg(request, response, ExceptionCode.UNKNOWN_ERROR, "failed");
        }
        setReturnMsg(request, response, ExceptionCode.UNKNOWN_ERROR, "failed");
        
        // 封装返回信息
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
