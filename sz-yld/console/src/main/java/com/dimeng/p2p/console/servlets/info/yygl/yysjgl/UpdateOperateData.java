/*
 * 文 件 名:  UpdateOperateData.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  God
 * 修改时间:  2016年3月10日
 */
package com.dimeng.p2p.console.servlets.info.yygl.yysjgl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S61.entities.T6196;
import com.dimeng.p2p.S61.entities.T6197;
import com.dimeng.p2p.console.servlets.info.AbstractInformationServlet;
import com.dimeng.p2p.repeater.policy.OperateDataManage;

/**
 * <运营数据管理>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2016年3月10日]
 */
@Right(id = "P2P_C_INFO_YYGL_MENU", name = "运营管理",moduleId="P2P_C_INFO_YYGL",order=0)
public class UpdateOperateData extends AbstractInformationServlet
{

    /**
     * 注释内容
     */
    private static final long serialVersionUID = -8939700734129640077L;
    
    @Override
    protected void processGet(HttpServletRequest request,
            HttpServletResponse response, ServiceSession serviceSession)
            throws Throwable {
        OperateDataManage manage = serviceSession.getService(OperateDataManage.class);
        T6196 t6196 = manage.getT6196();
        List<T6197> t6197List = manage.getT6197List();
        request.setAttribute("t6196", t6196);
        request.setAttribute("t6197List", t6197List);
        forwardView(request, response, getClass());
    }

    @Override
    protected void processPost(final HttpServletRequest request,
            final HttpServletResponse response,
            final ServiceSession serviceSession) throws Throwable {
        
        OperateDataManage manage = serviceSession.getService(OperateDataManage.class);
        T6196 t6196 = new T6196();
        t6196.parse(request);
        String[] amounts = request.getParameterValues("amounts");
        manage.updateOperateData(t6196,amounts);
        prompt(request, response, PromptLevel.INFO, "保存成功");
        sendRedirect(request, response, getController().getURI(request, UpdateOperateData.class));
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request,
            HttpServletResponse response, Throwable throwable)
            throws ServletException, IOException {
        getResourceProvider().log(throwable.getMessage());
        logger.error(throwable,throwable);
        if (throwable instanceof SQLException) {
            getController().prompt(request, response, PromptLevel.ERROR,
                    "系统繁忙，请您稍后再试");
        } else if (throwable instanceof LogicalException
                || throwable instanceof ParameterException) {
            getController().prompt(request, response, PromptLevel.WARRING,
                    throwable.getMessage());
        } else {
            super.onThrowable(request, response, throwable);
        }
        sendRedirect(request, response,
            getController().getURI(request, UpdateOperateData.class));
    }
    
}
