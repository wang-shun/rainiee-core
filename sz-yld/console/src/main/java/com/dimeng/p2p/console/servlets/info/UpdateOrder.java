package com.dimeng.p2p.console.servlets.info;

import java.io.PrintWriter;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.modules.base.console.service.AdvertisementManage;
import com.dimeng.p2p.modules.base.console.service.ArticleManage;
import com.dimeng.p2p.modules.base.console.service.CustomerServiceManage;
import com.dimeng.p2p.modules.base.console.service.FriendlyLinkManage;

@MultipartConfig
@Right(id = "P2P_C_INFO_GYWM_MENU", name = "关于我们",moduleId="P2P_C_INFO_GYWM",order=0)
public class UpdateOrder extends AbstractInformationServlet
{

    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(final HttpServletRequest request,
            HttpServletResponse response, ServiceSession serviceSession)
            throws Throwable {
        PrintWriter out = response.getWriter();
        ArticleManage manage = serviceSession.getService(ArticleManage.class);
        FriendlyLinkManage linkManage = serviceSession.getService(FriendlyLinkManage.class);
        AdvertisementManage advertisementManage = serviceSession.getService(AdvertisementManage.class);
        CustomerServiceManage  customerManage = serviceSession.getService(CustomerServiceManage.class);
        try{
            String ids = request.getParameter("orderId");
            String orderTable = request.getParameter("orderTable"); 
            int order = Integer.parseInt(request.getParameter("order")); 
            if("T5011".equals(orderTable)){
                manage.updateBatchArticleOrder(ids, order);
            }else if("T5014".equals(orderTable)){
                linkManage.updateBatchOrder(ids, order);
            }else if("T5016".equals(orderTable)){
                advertisementManage.updateBatchOrder(ids, order);
            }else if("T5012".equals(orderTable)){
                customerManage.updateBatchOrder(ids, order);
            }else{
                out.write("[{'num':1,'msg':'orderTable参数错误！'}]");
                return;
            }
            out.write("[{'num':0,'msg':'修改成功！'}]");
        }catch(Exception e){
            logger.error(e, e);
            StringBuilder sb = new StringBuilder();
            sb.append("[{'num':1,'msg':'");
            sb.append(e.getMessage() + "'}]");
            out.write(sb.toString());
        }finally{
            out.close();
        }
        
    }
    
}
