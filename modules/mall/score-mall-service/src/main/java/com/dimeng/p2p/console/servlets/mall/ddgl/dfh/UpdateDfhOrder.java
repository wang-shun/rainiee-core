/*
 * 文 件 名:  UpdateDfhOrder.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2016年4月21日
 */
package com.dimeng.p2p.console.servlets.mall.ddgl.dfh;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S63.entities.T6359;
import com.dimeng.p2p.S63.entities.T6360;
import com.dimeng.p2p.S63.enums.T6359_F08;
import com.dimeng.p2p.S63.enums.T6360_F03;
import com.dimeng.p2p.console.servlets.AbstractMallServlet;
import com.dimeng.p2p.console.servlets.mall.ddgl.orderlist.ScoreOrderList;
import com.dimeng.p2p.repeater.score.ScoreOrderManage;
import com.dimeng.util.parser.IntegerParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  xiaoqi
 * @version  [版本号, 2016年4月21日]
 */
@Right(id = "P2P_C_MALL_UPDATEDFHORDER", name = "修改", moduleId = "P2P_C_MALL_DDGL_DFH", order = 1)
public class UpdateDfhOrder extends AbstractMallServlet
{
    private static final long serialVersionUID = 1L;
    
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
}
