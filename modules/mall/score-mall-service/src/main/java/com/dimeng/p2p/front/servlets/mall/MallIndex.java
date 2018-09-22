/*
 * 文 件 名:  MallIndex.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoucl
 * 修改时间:  2015年12月17日
 */
package com.dimeng.p2p.front.servlets.mall;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S63.entities.T6350;
import com.dimeng.p2p.S63.entities.T6351;
import com.dimeng.p2p.S63.entities.T6353;
import com.dimeng.p2p.S63.enums.T6350_F04;
import com.dimeng.p2p.S63.enums.T6353_F05;
import com.dimeng.p2p.repeater.score.CommodityCategoryManage;
import com.dimeng.p2p.repeater.score.MallIndexManage;
import com.dimeng.p2p.repeater.score.SetScoreManage;
import com.dimeng.p2p.repeater.score.entity.SearchGoodsCategory;
import com.dimeng.util.parser.IntegerParser;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <平台商城首页>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2015年12月17日]
 */
public class MallIndex extends AbstractMallServlet
{

    /**
     * 注释内容
     */
    private static final long serialVersionUID = -1332501050421123427L;
    
    @Override
    protected void processGet(HttpServletRequest request,
            HttpServletResponse response, ServiceSession serviceSession)
            throws Throwable {/*
        
        CommodityCategoryManage cmanage = serviceSession.getService(CommodityCategoryManage.class);
        SetScoreManage smanage = serviceSession.getService(SetScoreManage.class);
        MallIndexManage mallIndexManage = serviceSession.getService(MallIndexManage.class);
        //启用的商品类别
        List<T6350> t6350List = cmanage.getT6350List(T6350_F04.on.name());
        //积分范围
        List<T6353> scoreRangeList = smanage.getT6353List(T6353_F05.score.name());
        //金额范围
        List<T6353> amountRangeList = smanage.getT6353List(T6353_F05.amount.name());
        //最近兑换 10条记录
        List<String> newestMallOrderList = mallIndexManage.getNewestMallOrderList(10);
        request.setAttribute("t6350List", t6350List);
        request.setAttribute("scoreRangeList", scoreRangeList);
        request.setAttribute("amountRangeList", amountRangeList);
        request.setAttribute("newestMallOrderList", newestMallOrderList);
        forwardView(request, response, getClass());
    */}

    @Override
    protected void processPost(final HttpServletRequest request,
            HttpServletResponse response, ServiceSession serviceSession)
            throws Throwable {
        MallIndexManage manage = serviceSession.getService(MallIndexManage.class);
        //分页参数
        Paging ajaxPage = new Paging()
        {
            @Override
            public int getSize()
            {
                return IntegerParser.parse(request.getParameter("pageSize"));
            }
            
            @Override
            public int getCurrentPage()
            {
                return IntegerParser.parse(request.getParameter("currentPage"));
            }
        };
        SearchGoodsCategory searchGoodsCategory = new SearchGoodsCategory();
        searchGoodsCategory.parse(request);
        PagingResult<T6351> list = manage.getT6351List(searchGoodsCategory,ajaxPage);
        String pageStr = this.rendPaging(list);
        PrintWriter out = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("t6351List", list.getItems());
        jsonMap.put("pageStr", pageStr);
        jsonMap.put("pageCount", list.getPageCount());
        out.print(objectMapper.writeValueAsString(jsonMap));
        out.close();
    }
}
