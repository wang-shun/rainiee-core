/*
 * 文 件 名:  ExportScoreCleanZero.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoucl
 * 修改时间:  2015年12月15日
 */
package com.dimeng.p2p.console.servlets.base.malloptsettings.scorecleanset;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.console.servlets.AbstractMallServlet;
import com.dimeng.p2p.repeater.score.SetScoreManage;
import com.dimeng.p2p.repeater.score.entity.ScoreCleanZero;
import com.dimeng.p2p.repeater.score.entity.ScoreCleanZeroQuery;
import com.dimeng.util.parser.IntegerParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;

/**
 * <导出积分清零设置>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2015年12月15日]
 */
@Right(id = "P2P_C_BASE_EXPORTSCORECLEANZERO", name = "导出积分清零", moduleId = "P2P_C_BASE_MALLOPTSETTINGS_SCORECLEANSET")
public class ExportScoreCleanZero extends AbstractMallServlet
{

    /**
     * 注释内容
     */
    private static final long serialVersionUID = 3191408809420488938L;
    
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
        SetScoreManage manager = serviceSession.getService(SetScoreManage.class);
        response.setHeader("Content-disposition", "attachment;filename="
                + new Timestamp(System.currentTimeMillis()).getTime() + ".csv");
        response.setContentType("application/csv");
        PagingResult<ScoreCleanZero> list = manager.searchScoreCleanZero(new ScoreCleanZeroQuery() {

            @Override
            public String getStartTime() {
                return request.getParameter("startTime1");
            }

            @Override
            public String getEndTime() {
                return request.getParameter("endTime1");
            }

        }, new Paging() {
            
            @Override
            public int getSize() {
                return Integer.MAX_VALUE;
            }
            
            @Override
            public int getCurrentPage() {
                return IntegerParser.parse(request
                        .getParameter(AbstractMallServlet.PAGING_CURRENT));
            }
        });
        manager.exportScoreCleanZero(list.getItems(), response.getOutputStream(), "");
    }
}
