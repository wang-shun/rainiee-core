package com.dimeng.p2p.console.servlets.mall.jfgl.statistics;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.entities.T6105;
import com.dimeng.p2p.console.servlets.AbstractMallServlet;
import com.dimeng.p2p.repeater.score.UserScoreManage;
import com.dimeng.p2p.repeater.score.entity.ScoreCountExt;
import com.dimeng.p2p.repeater.score.entity.UserScoreAccountExt;
import com.dimeng.util.parser.IntegerParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 积分统计列表
 *
 * @author pengwei
 * @version [版本号, 2015/12/15]
 */
@Right(id = "P2P_C_MALL_JFTJLB", isMenu = true, name = "积分统计列表",moduleId="P2P_C_MALL_JFGL_JFTJLB",order=0)
public class ScoreStatisticsList extends AbstractMallServlet{
    private static final long serialVersionUID = 1L;

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