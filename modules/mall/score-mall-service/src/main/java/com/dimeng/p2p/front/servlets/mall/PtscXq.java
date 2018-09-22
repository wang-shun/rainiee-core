/*
 * 文 件 名:  MallIndex.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoucl
 * 修改时间:  2015年12月17日
 */
package com.dimeng.p2p.front.servlets.mall;

import com.dimeng.framework.http.servlet.annotation.PagingServlet;
import com.dimeng.framework.http.upload.FileStore;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.repeater.score.ScoreCommodityManage;
import com.dimeng.p2p.repeater.score.entity.ScoreOrderInfoExt;
import com.dimeng.p2p.repeater.score.entity.T6351Ext;
import com.dimeng.util.StringHelper;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;


/**
 * <平台商城首页>
 * <功能详细描述>
 * 
 * @author  pengwei
 * @version  [版本号, 2015年12月17日]
 */
@PagingServlet(itemServlet = PtscXq.class)
public class PtscXq extends AbstractMallServlet
{

    /**
     * 注释内容
     */
    private static final long serialVersionUID = -1332501050421123427L;
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {/*
        String type = request.getParameter("type");
        int commId = Integer.parseInt(request.getParameter("id"));
        PrintWriter out = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        ScoreCommodityManage scoreCommodityManage = serviceSession.getService(ScoreCommodityManage.class);
        if ("spxq".equals(type))
        {
            getSpxq(serviceSession, commId, jsonMap, scoreCommodityManage);
        }
        else if ("gmjl".equals(type))
        {
            getGmjl(request, serviceSession, commId, jsonMap, scoreCommodityManage);
        }
        out.print(objectMapper.writeValueAsString(jsonMap));
        out.close();
    */}
    
    /**
     * 购买记录
     * <功能详细描述>
     * @param request
     * @param serviceSession
     * @param commId
     * @param jsonMap
     * @param scoreCommodityManage
     * @throws Throwable
     */
    private void getGmjl(final HttpServletRequest request, ServiceSession serviceSession, int commId,
        Map<String, Object> jsonMap, ScoreCommodityManage scoreCommodityManage)
        throws Throwable
    {
        PagingResult<ScoreOrderInfoExt> result = scoreCommodityManage.getCommodityOrderList(commId, new Paging()
        {
            
            @Override
            public int getSize()
            {
                return Integer.parseInt(request.getParameter("pageSize"));
            }
            
            @Override
            public int getCurrentPage()
            {
                return Integer.parseInt(request.getParameter("currentPage"));
            }

        });
        jsonMap.put("pageStr", rendPaging(result));
        jsonMap.put("pageCount", result.getPageCount());
        jsonMap.put("ScoreOrderInfoList", result.getItems());
    }

    /**
     * 商品详情
     * <功能详细描述>
     * @param serviceSession
     * @param commId
     * @param jsonMap
     * @param scoreCommodityManage
     */
    private void getSpxq(ServiceSession serviceSession, int commId, Map<String, Object> jsonMap,
        ScoreCommodityManage scoreCommodityManage)
        throws Throwable
    {
        T6351Ext t6351Ext = scoreCommodityManage.getCommodityObject(commId);
        FileStore fileStore  = getResourceProvider().getResource(FileStore.class);
        //转化下文本
        t6351Ext.F10 =  StringHelper.format(t6351Ext.F10, fileStore);
        jsonMap.put("spxqData", t6351Ext);
    }

}
