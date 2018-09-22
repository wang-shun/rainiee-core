/*
 * 文 件 名:  CommodityCategory.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoucl
 * 修改时间:  2015年12月15日
 */
package com.dimeng.p2p.console.servlets.base.malloptsettings.category;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S63.entities.T6350;
import com.dimeng.p2p.console.servlets.AbstractMallServlet;
import com.dimeng.p2p.repeater.score.CommodityCategoryManage;
import com.dimeng.p2p.repeater.score.entity.ScoreComdCategoryExt;
import com.dimeng.p2p.repeater.score.query.CommodityCategorySearch;
import com.dimeng.util.parser.IntegerParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <商品类别设置>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2015年12月15日]
 */
@Right(id = "P2P_C_BASE_COMMODITYCATEGORY", isMenu = true, name = "商品类别设置", moduleId = "P2P_C_BASE_MALLOPTSETTINGS_CATEGORY", order = 0)
public class CommodityCategory extends AbstractMallServlet
{

    /**
     * 注释内容
     */
    private static final long serialVersionUID = 9096314181101433188L;
    
    @Override
    protected void processGet(HttpServletRequest request,
            HttpServletResponse response, ServiceSession serviceSession)
            throws Throwable {
        processPost(request, response, serviceSession);
    }

    @Override
    protected void processPost(final HttpServletRequest request,
            final HttpServletResponse response,
            final ServiceSession serviceSession) throws Throwable {
        
        CommodityCategoryManage manager = serviceSession.getService(CommodityCategoryManage.class);
        PagingResult<ScoreComdCategoryExt> result = manager.getScoreComdCategoryExt(new CommodityCategorySearch() {

            @Override
            public String getF02() {
                return request.getParameter("F02");
            }

            @Override
            public String getF04() {
                return request.getParameter("F04");
            }

            /**
             * 商品类别属性
             */
            @Override
            public String getF07() {
                return request.getParameter("F07");
            }

        }, new Paging() {
            
            @Override
            public int getSize() {
                return 10;
            }
            
            @Override
            public int getCurrentPage() {
                return IntegerParser.parse(request
                        .getParameter(AbstractMallServlet.PAGING_CURRENT));
            }
        });
        List<T6350> t6350List = manager.getT6350List(null);
        request.setAttribute("result", result);
        request.setAttribute("t6350List", t6350List);
        forwardView(request, response, getClass());
    }
    
}
