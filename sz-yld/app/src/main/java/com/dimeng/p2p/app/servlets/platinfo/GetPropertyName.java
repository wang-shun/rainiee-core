package com.dimeng.p2p.app.servlets.platinfo;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.app.service.BusinessManage;
import com.dimeng.p2p.app.servlets.AbstractAppServlet;

/**
 * 广告详情
 * @author zhoulantao
 *
 */
public class GetPropertyName extends AbstractAppServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        BusinessManage businessManage = serviceSession.getService(BusinessManage.class);
        String mtbdName = businessManage.getT5010Name("MTBD");//媒体报道
        String hyzxName = businessManage.getT5010Name("WDHYZX");//行业资讯
        String xszyName = businessManage.getT5010Name("XSZY");//新手指引
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("mtbdName", mtbdName);
        map.put("hyzxName", hyzxName);
        map.put("xszyName", xszyName);
        // 封装返回信息
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success!", map);
        return;
    }
    
}
