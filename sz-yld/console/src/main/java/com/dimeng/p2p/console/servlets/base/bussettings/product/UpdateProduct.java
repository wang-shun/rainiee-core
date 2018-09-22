package com.dimeng.p2p.console.servlets.base.bussettings.product;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S62.entities.T6211;
import com.dimeng.p2p.S62.entities.T6216;
import com.dimeng.p2p.S62.enums.T6216_F18;
import com.dimeng.p2p.console.servlets.base.AbstractBaseServlet;
import com.dimeng.p2p.modules.base.console.service.ProductManage;
import com.dimeng.p2p.modules.bid.console.service.BidManage;
import com.dimeng.util.StringHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Right(id = "P2P_C_BASE_UPDATEPRODUCT", name = "修改标产品", moduleId = "P2P_C_BASE_OPTSETTINGS_PRODUCT")
public class UpdateProduct extends AbstractBaseServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        BidManage bidManage = serviceSession.getService(BidManage.class);
        T6211[] t6211s = bidManage.getBidType();
        
        String productId = request.getParameter("id");
        if (StringHelper.isEmpty(productId))
        {
            productId = request.getParameter("F01");
        }
        
        ProductManage productManage = serviceSession.getService(ProductManage.class);
        T6216 model = productManage.get(Integer.parseInt(productId));
        request.setAttribute("model", model);
        request.setAttribute("t6211s", t6211s);
        super.processGet(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        ProductManage productManage = serviceSession.getService(ProductManage.class);
        String name = request.getParameter("F02");
        String[] ways = request.getParameterValues("F11");
        String[] bidTypes = request.getParameterValues("F03");//标类型
        StringBuffer wayStr = new StringBuffer("");
        for (String s : ways)
        {
            wayStr.append("," + s);
        }
        if (!wayStr.toString().equals(""))
        {
            //去掉一地个“，”号
            wayStr = new StringBuffer(wayStr.substring(1));
        }
        
        StringBuffer bidTypeStr = new StringBuffer("");
        for (String s : bidTypes)
        {
            bidTypeStr.append("," + s);
        }
        if (!bidTypeStr.toString().equals(""))
        {
            //去掉一地个“，”号
            bidTypeStr = new StringBuffer(bidTypeStr.substring(1));
        }
        T6216 t6216 = new T6216();
        t6216.parse(request);
        t6216.F11 = wayStr.toString();
        t6216.F03 = bidTypeStr.toString();
        if (name == null)
        {
            getController().prompt(request, response, PromptLevel.WARRING, "标产品名称不能为空");
            processGet(request, response, serviceSession);
            return;
        }
        if (t6216.F15.compareTo(t6216.F05) > 0)
        {
            getController().prompt(request, response, PromptLevel.WARRING, "起投金额不能大于最低借款金额");
            processGet(request, response, serviceSession);
            return;
        }
        /*if (productManage.isProductExists(name)) {
        	getController().prompt(request, response, PromptLevel.WARRING,
        			"标产品名称已经存在");
        	processGet(request, response, serviceSession);
        	return;
        }*/
        if (null == t6216.F18)
        {
            t6216.F18 = T6216_F18.BSX;
        }
        productManage.update(t6216);
        sendRedirect(request, response, getController().getURI(request, SearchProduct.class));
    }
    
}
