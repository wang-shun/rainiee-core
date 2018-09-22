/*
 * 文 件 名:  CancelGuarantor
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  heluzhu
 * 修改时间: 2016/6/14
 */
package com.dimeng.p2p.console.servlets.finance.zjgl.yhdbgl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.console.servlets.finance.AbstractGuarantorServlet;
import com.dimeng.p2p.repeater.guarantor.ApplyGuarantorManage;
import com.dimeng.util.parser.IntegerParser;

/**
 * 取消申请担保方
 * <功能详细描述>
 *
 * @author heluzhu
 * @version [版本号, 2016/6/14]
 */
@Right(id = "P2P_C_FINANCE_QXSQ", name = "取消担保", moduleId = "P2P_C_FINANCE_ZJGL_YHDBGL", order = 3)
public class CancelGuarantor extends AbstractGuarantorServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        ApplyGuarantorManage manage = serviceSession.getService(ApplyGuarantorManage.class);
        try
        {
            int id = IntegerParser.parse(request.getParameter("id"));
            manage.cancelGuarantor(id);
            manage.writeLog("操作日志", "取消用户担保成功!");
            sendRedirect(request, response, getController().getURI(request, DbList.class));
        }
        catch (Throwable throwable)
        {
            logger.error("取消担保", throwable);
            if (throwable instanceof LogicalException || throwable instanceof ParameterException)
            {
                getController().prompt(request, response, PromptLevel.WARRING, throwable.getMessage());
            }
            else
            {
                getController().prompt(request, response, PromptLevel.ERROR, throwable.getMessage());
            }
            sendRedirect(request, response, getController().getURI(request, DbList.class));
        }
    }
}
