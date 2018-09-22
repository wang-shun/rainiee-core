/*
 * 文 件 名:  MallRefundList.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoucl
 * 修改时间:  2015年12月21日
 */
package com.dimeng.p2p.console.servlets.finance.zjgl.mall;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S63.entities.T6350;
import com.dimeng.p2p.console.servlets.AbstractMallServlet;
import com.dimeng.p2p.repeater.score.CommodityCategoryManage;
import com.dimeng.p2p.repeater.score.ScoreOrderManage;
import com.dimeng.p2p.repeater.score.entity.MallRefund;
import com.dimeng.p2p.repeater.score.query.ScoreOrderQuery;
import com.dimeng.util.parser.IntegerParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <商城退款管理>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2015年12月21日]
 */
@Right(id = "P2P_C_FINANCE_MALLREFUNDLIST", name = "商城退款管理", moduleId="P2P_C_FINANCE_ZJGL_SCTKGL", order = 0)
public class MallRefundList extends AbstractMallServlet
{

    /**
     * 注释内容
     */
    private static final long serialVersionUID = -6759738532728278894L;
    
    @Override
    protected void processGet(HttpServletRequest request,
                              HttpServletResponse response, ServiceSession serviceSession)
            throws Throwable {
        processPost(request, response, serviceSession);
    }

    @Override
    protected void processPost(final HttpServletRequest request,
                               final HttpServletResponse response,
                               final ServiceSession serviceSession) throws Throwable {}
}
