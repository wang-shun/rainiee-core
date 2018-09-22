/*
 * 文 件 名:  CommodityDet.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoucl
 * 修改时间:  2016年5月13日
 */
package com.dimeng.p2p.console.servlets.mall.spgl.goodslist;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.console.servlets.AbstractMallServlet;
import com.dimeng.p2p.repeater.score.ScoreOrderManage;
import com.dimeng.p2p.repeater.score.entity.ScoreOrderDetailRecord;
import com.dimeng.p2p.repeater.score.entity.ScoreOrderMainRecord;
import com.dimeng.p2p.repeater.score.entity.ScoreOrderStatistics;
import com.dimeng.p2p.repeater.score.query.OrderDetQuery;
import com.dimeng.util.parser.IntegerParser;

/**
 * <商品订单详情>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2016年5月13日]
 */
@Right(id = "P2P_C_MALL_COMMODITY_ORDER_DET", name = "查看订单", isMenu = false, moduleId = "P2P_C_MALL_SPGL_SHLB", order = 4)
public class CommodityOrderDet extends AbstractMallServlet
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 3543998174135527279L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {}
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {}
    
}
