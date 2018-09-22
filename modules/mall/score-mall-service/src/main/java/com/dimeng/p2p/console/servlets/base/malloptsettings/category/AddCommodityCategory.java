/*
 * 文 件 名:  AddCommodityCategory.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoucl
 * 修改时间:  2015年12月15日
 */
package com.dimeng.p2p.console.servlets.base.malloptsettings.category;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.console.servlets.AbstractMallServlet;
import com.dimeng.p2p.repeater.score.CommodityCategoryManage;
import com.dimeng.util.StringHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <新增商品类别>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2015年12月15日]
 */
@Right(id = "P2P_C_BASE_ADDCOMMODITYCATEGORY", name = "新增商品类别", moduleId = "P2P_C_BASE_MALLOPTSETTINGS_CATEGORY")
public class AddCommodityCategory extends AbstractMallServlet
{

    /**
     * 注释内容
     */
    private static final long serialVersionUID = -3042253554989347073L;
    
    @Override
    protected void processGet(HttpServletRequest request,
            HttpServletResponse response, ServiceSession serviceSession)
            throws Throwable {
        sendRedirect(request, response,
            getController().getURI(request, CommodityCategory.class));
    }
    
    @Override
    protected void processPost(final HttpServletRequest request,
            HttpServletResponse response, ServiceSession serviceSession)
            throws Throwable {
        CommodityCategoryManage manager = serviceSession.getService(CommodityCategoryManage.class);
        String F02 = request.getParameter("F02");
        String F03 = request.getParameter("F03");
        String F07 = request.getParameter("F07");
        if (StringHelper.isEmpty(F02)) {
            getController().prompt(request, response, PromptLevel.WARRING,
                    "类别编码不能为空");
            processGet(request, response, serviceSession);
            return;
        }
        if (StringHelper.isEmpty(F03)) {
            getController().prompt(request, response, PromptLevel.WARRING,
                    "商品类别不能为空");
            processGet(request, response, serviceSession);
            return;
        }
        manager.addT6350(F02,F03,F07);
        sendRedirect(request, response,
                getController().getURI(request, CommodityCategory.class));
    }
    
}
