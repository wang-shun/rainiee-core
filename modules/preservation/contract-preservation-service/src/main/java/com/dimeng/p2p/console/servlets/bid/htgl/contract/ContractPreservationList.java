/*
 * 文 件 名:  ContractPreservationList.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2016年6月16日
 */
package com.dimeng.p2p.console.servlets.bid.htgl.contract;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.repeater.preservation.ContractPreservationManage;
import com.dimeng.p2p.repeater.preservation.entity.ContractEntity;
import com.dimeng.p2p.repeater.preservation.query.ContractQuery;
import com.dimeng.util.parser.IntegerParser;

/**
 * <合同保全列表>
 * <功能详细描述>
 * 
 * @author  xiaoqi
 * @version  [7.2.0, 2016年6月16日]
 */
@Right(id = "P2P_C_BID_HTGL_HTBQ_LIST", isMenu = true, name = "合同保全列表", moduleId = "P2P_C_BID_HTGL_HTBQLB", order = 0)
public class ContractPreservationList extends AbstractPreserveServlet
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 5351384457570823438L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        this.processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        ContractPreservationManage contractPreservationManage =
            serviceSession.getService(ContractPreservationManage.class);
        ContractQuery query = new ContractQuery();
        query.parse(request);
        PagingResult<ContractEntity> contractEntitys = contractPreservationManage.search(query, new Paging()
        {
            
            @Override
            public int getSize()
            {
                return 10;
            }
            
            @Override
            public int getCurrentPage()
            {
                return IntegerParser.parse(request.getParameter(PAGING_CURRENT));
            }
        });
        request.setAttribute("result", contractEntitys);
        forwardView(request, response, getClass());
    }
}
