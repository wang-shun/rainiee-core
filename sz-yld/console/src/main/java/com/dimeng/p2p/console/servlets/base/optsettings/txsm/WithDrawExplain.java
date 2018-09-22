/*
 * 文 件 名:  WithDrawExplain.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoucl
 * 修改时间:  2015年11月9日
 */
package com.dimeng.p2p.console.servlets.base.optsettings.txsm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.http.upload.FileStore;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S50.entities.T5023;
import com.dimeng.p2p.S50.enums.T5023_F02;
import com.dimeng.p2p.console.servlets.base.AbstractBaseServlet;
import com.dimeng.p2p.modules.base.console.service.FunctionExplainManage;
import com.dimeng.p2p.modules.base.console.service.entity.ExplainInfo;

/**
 * <提现说明管理>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2015年11月9日]
 */
@Right(id = "P2P_C_BASE_WITHDRAWEXPLAIN", isMenu = true, name = "提现说明管理", moduleId = "P2P_C_BASE_OPTSETTINGS_TXSM")
public class WithDrawExplain extends AbstractBaseServlet
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 7678506750746061363L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        
        try
        {
            FunctionExplainManage manage = serviceSession.getService(FunctionExplainManage.class);
            final T5023 t5023 = manage.get(T5023_F02.WITHDRAW);
            ExplainInfo explainInfo = new ExplainInfo()
            {
                
                @Override
                public String getType()
                {
                    return T5023_F02.WITHDRAW.name();
                }
                
                @Override
                public String getContent()
                {
                    return getResourceProvider().getResource(FileStore.class).encode(request.getParameter("content")
                        .replaceAll("&lt;", "<")
                        .replaceAll("&gt;", ">"));
                }
                
                @Override
                public int getId()
                {
                    return t5023 == null ? 0 : t5023.F01;
                }
            };
            
            if (t5023 == null)
            {
                manage.addWithDraw(explainInfo);
            }
            else
            {
                manage.updateWithDraw(explainInfo);
            }
            request.setAttribute("content", request.getParameter("content"));
            prompt(request, response, PromptLevel.INFO, "保存成功");
            forwardView(request, response, getClass());
        }
        catch (ParameterException | LogicalException e)
        {
            prompt(request, response, PromptLevel.ERROR, e.getMessage());
            processGet(request, response, serviceSession);
        }
        
    }
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        FunctionExplainManage manage = serviceSession.getService(FunctionExplainManage.class);
        T5023 t5023 = manage.get(T5023_F02.WITHDRAW);
        request.setAttribute("content", t5023 != null ? t5023.F03 : "");
        forwardView(request, response, getClass());
    }
    
}
