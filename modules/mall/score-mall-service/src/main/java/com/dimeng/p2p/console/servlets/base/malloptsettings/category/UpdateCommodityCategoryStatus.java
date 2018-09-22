/*
 * 文 件 名:  QyCommodityCategory.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoucl
 * 修改时间:  2015年12月15日
 */
package com.dimeng.p2p.console.servlets.base.malloptsettings.category;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.console.servlets.AbstractMallServlet;
import com.dimeng.p2p.repeater.score.CommodityCategoryManage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2015年12月15日]
 */
@Right(id = "P2P_C_BASE_UPDATECOMMODITYCATEGORYSTATUS", name = "修改商品类别状态", moduleId = "P2P_C_BASE_MALLOPTSETTINGS_CATEGORY")
public class UpdateCommodityCategoryStatus extends AbstractMallServlet
{

    /**
     * 注释内容
     */
    private static final long serialVersionUID = 4580504056495617179L;
    
    @Override
    protected void processGet(final HttpServletRequest request,
            final HttpServletResponse response,
            final ServiceSession serviceSession) throws Throwable {
        CommodityCategoryManage manager = serviceSession.getService(CommodityCategoryManage.class);
        manager.updateT6350_F07(request.getParameter("F01"),request.getParameter("F04"));
        sendRedirect(request, response, getController().getURI(request, CommodityCategory.class));
    }

    @Override
    protected void onThrowable(HttpServletRequest request,
            HttpServletResponse response, Throwable throwable)
            throws ServletException, IOException {
        getResourceProvider().log(throwable.getMessage());
        if (throwable instanceof SQLException) {
            logger.error(throwable, throwable);
            getController().prompt(request, response, PromptLevel.ERROR,
                    "系统繁忙，请您稍后再试");
            sendRedirect(request, response,
                    getController().getURI(request, CommodityCategory.class));

        } else if (throwable instanceof LogicalException
                || throwable instanceof ParameterException) {
            getController().prompt(request, response, PromptLevel.WARRING,
                    throwable.getMessage());
            sendRedirect(request, response,
                    getController().getURI(request, CommodityCategory.class));
        } else {
            super.onThrowable(request, response, throwable);
            sendRedirect(request, response,
                    getController().getURI(request, CommodityCategory.class));
        }
    }
}
