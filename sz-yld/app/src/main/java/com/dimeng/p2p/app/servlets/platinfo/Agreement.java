package com.dimeng.p2p.app.servlets.platinfo;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S50.entities.T5017;
import com.dimeng.p2p.app.servlets.AbstractAppServlet;
import com.dimeng.p2p.app.servlets.domain.RegAgreen;
import com.dimeng.p2p.common.enums.TermType;
import com.dimeng.p2p.modules.base.front.service.TermManage;

/**
 * 
 * 协议内容
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年12月28日]
 */
public class Agreement extends AbstractAppServlet
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
        // 获取协议类型
        TermType termType = TermType.parse(getParameter(request, "type"));
        if (termType == null)
        {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        
        TermManage termManage = serviceSession.getService(TermManage.class);
        T5017 term = termManage.get(termType);
        if (term == null)
        {
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.REGISTER_AGREEMENT_NO_EMPTY, "协议内容不存在！");
            return;
        }
        
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        RegAgreen agreen = new RegAgreen();
        
        // 协议内容
        agreen.setContent(getImgContent(term.F02));
        
        // 最后更新时间
        agreen.setUdpateTime(format.format(term.F04));
        
        // 封装返回信息
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success", agreen);
        return;
    }
}
