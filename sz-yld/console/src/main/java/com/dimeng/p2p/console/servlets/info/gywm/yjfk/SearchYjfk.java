/*
 * 文 件 名:  UpdateYjfk.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  God
 * 修改时间:  2016年4月5日
 */
package com.dimeng.p2p.console.servlets.info.gywm.yjfk;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.entities.T6195_EXT;
import com.dimeng.p2p.console.servlets.info.AbstractInformationServlet;
import com.dimeng.p2p.modules.base.console.service.TzjyManage;
import com.dimeng.p2p.modules.base.console.service.query.TzjyQuery;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.TimestampParser;

/**
 * <意见反馈>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2016年4月5日]
 */
@Right(id = "P2P_C_INFO_GYWM_MENU", name = "关于我们",moduleId="P2P_C_INFO_GYWM",order=0)
public class SearchYjfk extends AbstractInformationServlet
{

    /**
     * 注释内容
     */
    private static final long serialVersionUID = 4328545690028009166L;
    
    @Override
    protected void processPost(HttpServletRequest request,
            HttpServletResponse response, ServiceSession serviceSession)
            throws Throwable {
        processGet(request, response, serviceSession);
    }

    @Override
    protected void processGet(final HttpServletRequest request,
            HttpServletResponse response, ServiceSession serviceSession)
            throws Throwable {

        TzjyManage manage = serviceSession.getService(TzjyManage.class);
        final Paging paging = new Paging() {

            @Override
            public int getSize() {

                return 10;
            }

            @Override
            public int getCurrentPage() {
                return IntegerParser.parse(request.getParameter(PAGING_CURRENT));
            }
        };
        TzjyQuery query = new TzjyQuery() {

            @Override
            public Timestamp getCreateTimeEnd() {
                return TimestampParser.parse(request
                        .getParameter("createTimeEnd"));
            }

            @Override
            public Timestamp getCreateTimeStart() {
                return TimestampParser.parse(request
                        .getParameter("createTimeStart"));
            }

            @Override
            public String getReplyStatus() {
                return request.getParameter("replyStatus");
            }

            @Override
            public String getPublishStatus() {
                return request.getParameter("publishStatus");
            }

            @Override
            public Timestamp getPublishTimeEnd() {
                return TimestampParser.parse(request
                        .getParameter("publishTimeEnd"));
            }

            @Override
            public Timestamp getPublishTimeStart() {
                return TimestampParser.parse(request
                        .getParameter("publishTimeStart"));
            }

            @Override
            public int getId() {
                return 0;
            }

        };
        PagingResult<T6195_EXT> result = manage.search(query, paging);
        request.setAttribute("result", result);
        forwardView(request, response, getClass());

    }
}
