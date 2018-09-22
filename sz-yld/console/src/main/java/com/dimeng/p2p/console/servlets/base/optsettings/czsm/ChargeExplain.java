/*
 * 文 件 名:  ChargeExplain.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoucl
 * 修改时间:  2015年11月9日
 */
package com.dimeng.p2p.console.servlets.base.optsettings.czsm;

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
 * <充值说明管理>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2015年11月9日]
 */
@Right(id = "P2P_C_BASE_CHARGEEXPLAIN", isMenu = true, name = "充值说明管理", moduleId = "P2P_C_BASE_OPTSETTINGS_CZSM")
public class ChargeExplain extends AbstractBaseServlet
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 4048899275175457422L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        
        try
        {
            FunctionExplainManage manage = serviceSession.getService(FunctionExplainManage.class);
            boolean isXxCharge = manage.isXxCharge();
            final T5023 t5023 = manage.get(T5023_F02.CHARGE);
            ExplainInfo explainInfo = new ExplainInfo()
            {
                
                @Override
                public String getType()
                {
                    return T5023_F02.CHARGE.name();
                }
                
                @Override
                public String getContent()
                {
                    return getResourceProvider().getResource(FileStore.class).encode(request.getParameter("xsContent")
                        .replaceAll("&lt;", "<")
                        .replaceAll("&gt;", ">"));
                }
                
                @Override
                public int getId()
                {
                    return t5023 == null ? 0 : t5023.F01;
                }
            };
            request.setAttribute("xsContent", request.getParameter("xsContent"));
            
            final T5023 t5023Line = manage.get(T5023_F02.CHARGELINE);
            ExplainInfo explainInfoLine = null;
            if (isXxCharge)
            {
                explainInfoLine = new ExplainInfo()
                {
                    @Override
                    public String getType()
                    {
                        return T5023_F02.CHARGELINE.name();
                    }
                    
                    @Override
                    public String getContent()
                    {
                        return getResourceProvider().getResource(FileStore.class)
                            .encode(request.getParameter("xxContent").replaceAll("&lt;", "<").replaceAll("&gt;", ">"));
                    }
                    
                    @Override
                    public int getId()
                    {
                        return t5023Line == null ? 0 : t5023Line.F01;
                    }
                };
                request.setAttribute("xxContent", request.getParameter("xxContent"));
            }
            
            if (t5023 == null && (isXxCharge ? t5023Line == null : true))
            {
                manage.addCharge(explainInfo, explainInfoLine);
            }
            else if (t5023 != null && (isXxCharge ? t5023Line != null : true))
            {
                manage.updateCharge(explainInfo, explainInfoLine);
            }
            else
            {
                prompt(request, response, PromptLevel.ERROR, "保存失败");
                forwardView(request, response, getClass());
                return;
            }
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
        T5023 t5023 = manage.get(T5023_F02.CHARGE);
        T5023 t5023Line = manage.get(T5023_F02.CHARGELINE);
        request.setAttribute("xsContent", t5023 != null ? t5023.F03 : "");
        request.setAttribute("xxContent", t5023Line != null ? t5023Line.F03 : "");
        forwardView(request, response, getClass());
    }
    
}
