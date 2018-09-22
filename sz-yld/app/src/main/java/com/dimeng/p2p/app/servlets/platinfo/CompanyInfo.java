package com.dimeng.p2p.app.servlets.platinfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S50.entities.T5011;
import com.dimeng.p2p.S50.enums.T5011_F02;
import com.dimeng.p2p.app.servlets.AbstractAppServlet;
import com.dimeng.p2p.modules.base.front.service.ArticleManage;

/**
 * 联系我们
 * @author tanhui
 */
public class CompanyInfo extends AbstractAppServlet
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
        
        String type = getParameter(request, "type");
        ArticleManage articleManage = serviceSession.getService(ArticleManage.class);
        T5011 article = articleManage.get(T5011_F02.parse(type));
        
        Object _data = null;
        if (article != null)
        {
            articleManage.view(article.F01);
            String content = getImgContent(articleManage.getContent(article.F01));
            _data = content;
        }
        else
        {
            _data = "<div style='text-align: center;padding-top:1em'>暂时没有数据！</div>";
        }
        
        // 封装返回信息
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success", _data);
        return;
    }
}
