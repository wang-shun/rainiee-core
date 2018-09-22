/*
 * 文 件 名:  FreezeRecordView.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  lingyuanjie
 * 修改时间:  2016年6月2日
 */
package com.dimeng.p2p.console.servlets.finance.fuyou.zjdjgl.freeze;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.Controller;
import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.console.servlets.finance.fuyou.zjdjgl.AbstractZjdjglServlet;
import com.dimeng.p2p.escrow.fuyou.entity.freeze.T6170;
import com.dimeng.p2p.escrow.fuyou.service.unfreeze.UnFreezeManage;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;

/**
 * 冻结记录列表查询
 * <功能详细描述>
 * 
 * @author  lingyuanjie
 * @version  [版本号, 2016年6月2日]
 */
@Right(id = "P2P_C_FUYOU_ZJDJGL_ZJDJ_FREEZE_RECORD", isMenu = true, name = "冻结记录查看", moduleId = "P2P_C_FUYOU_ZJDJGL_ZJDJ", order = 4)
public class FreezeRecordView extends AbstractZjdjglServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        String name = request.getParameter("name");//账户名
        if (StringHelper.isEmpty(name))
        {
            name = (String)request.getAttribute("name");
        }
        String serialNumber = request.getParameter("serialNumber");//流水号
        String status = request.getParameter("status");//状态
        UnFreezeManage unFreezeManage = serviceSession.getService(UnFreezeManage.class);
        PagingResult<T6170> list = unFreezeManage.getT6170(name, serialNumber, status, new Paging()
        {
            @Override
            public int getSize()
            {
                return 10;
            }
            
            @Override
            public int getCurrentPage()
            {
                return IntegerParser.parse(request.getParameter(PAGING_CURRENT));
            }
        });
        request.setAttribute("status", status);
        request.setAttribute("name", name);
        request.setAttribute("list", list);
        forwardView(request, response, getClass());
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        Controller controller = getController();
        controller.prompt(request, response, PromptLevel.INFO, throwable.getMessage());
        sendRedirect(request, response, controller.getViewURI(request, getClass()));
    }
}
