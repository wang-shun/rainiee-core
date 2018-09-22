package com.dimeng.p2p.app.servlets.pay;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;

/**
 * 
 * 修改银行卡类
 * @author  zhongsai
 * @version  [V7.0, 2018年4月18日]
 */
public class ChangeCard extends AbstractSecureServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        String location =
            getSiteDomain("/pay/service/fuyou/fyouChangeCard.htm");
        Map<String, String> map = new HashMap<String, String>();
        map.put("url", location);
        
        // 封装返回信息
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success!", map);
        return;
    }
}
