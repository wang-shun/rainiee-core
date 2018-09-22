/*
 * 文 件 名:  Dbsh
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
 * 担保审核
 * <功能详细描述>
 *
 * @author heluzhu
 * @version [版本号, 2016/6/14]
 */
@Right(id = "P2P_C_FINANCE_DBSH", name = "审核", moduleId = "P2P_C_FINANCE_ZJGL_YHDBGL", order = 1)
public class Dbsh extends AbstractGuarantorServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        try
        {
            ApplyGuarantorManage manage = serviceSession.getService(ApplyGuarantorManage.class);
            int id = IntegerParser.parse(request.getParameter("id"));
            String status = request.getParameter("status");
            String desc = request.getParameter("desc");
            manage.auditApply(id, status, desc);
            manage.writeLog("操作日志", "用户申请担保审核成功!");
            sendRedirect(request, response, getController().getURI(request, DbList.class));
        }
        catch (Throwable throwable)
        {
            logger.error("申请担保审核", throwable);
            if (throwable instanceof LogicalException || throwable instanceof ParameterException)
            {
                getController().prompt(request, response, PromptLevel.WARRING, throwable.getMessage());
                sendRedirect(request, response, getController().getURI(request, DbList.class));
            }
            else
            {
                super.onThrowable(request, response, throwable);
            }
        }
    }
}
