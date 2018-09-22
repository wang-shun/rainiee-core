package com.dimeng.p2p.console.servlets.finance.czgl.xsczgl;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6131_F07;
import com.dimeng.p2p.console.servlets.finance.AbstractFinanceServlet;
import com.dimeng.p2p.modules.account.console.service.UserRechargeManage;
import com.dimeng.p2p.modules.account.console.service.entity.UserRecharge;
import com.dimeng.p2p.modules.account.console.service.query.CzglRecordExtendsQuery;
import com.dimeng.util.parser.EnumParser;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.TimestampParser;

@Right(id = "P2P_C_FINANCE_CZGLLIST", moduleId = "P2P_C_FINANCE_CZGL_XSCZGL", order = 0, name = "线上充值管理")
public class CzglList extends AbstractFinanceServlet
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
    {
        UserRechargeManage userRechargeManage = serviceSession.getService(UserRechargeManage.class);
        BigDecimal czze = userRechargeManage.getCzze();
        BigDecimal czsxf = userRechargeManage.getCzsxf();
        CzglRecordExtendsQuery query = new CzglRecordExtendsQuery()
        {
            @Override
            public String getOrderNo()
            {
                return request.getParameter("orderNo");
            }
            
            @Override
            public String getLoginName()
            {
                return request.getParameter("loginName");
            }
            
            @Override
            public String getPayComName()
            {
                return request.getParameter("payComName");
            }
            
            @Override
            public String getPayType()
            {
                return request.getParameter("payType");
            }
            
            @Override
            public Timestamp getStartRechargeTime()
            {
                return TimestampParser.parse(request.getParameter("startTime"));
            }
            
            @Override
            public Timestamp getEndRechargeTime()
            {
                return TimestampParser.parse(request.getParameter("endTime"));
            }
            
            @Override
            public String getSerialNumber()
            {
                return request.getParameter("serialNumber");
            }
            
            @Override
            public String getUsersType()
            {
                return request.getParameter("usersType");
            }
            
            @Override
            public String getChargeStatus()
            {
                return request.getParameter("chargeStatus");
            }
            
            @Override
            public T6131_F07 getStatus()
            {
                return EnumParser.parse(T6131_F07.class, request.getParameter("status"));
            }
            
            @Override
            public T6110_F06 getUserType()
            {
                return EnumParser.parse(T6110_F06.class, request.getParameter("userType"));
            }
            
            @Override
            public Timestamp getFinishEndRechargeTime()
            {
                return TimestampParser.parse(request.getParameter("finishEndTime"));
            }
            
            @Override
            public Timestamp getFinishStartRechargeTime()
            {
                return TimestampParser.parse(request.getParameter("finishStartTime"));
            }
        };
        PagingResult<UserRecharge> result = userRechargeManage.serarch(query, new Paging()
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
        UserRecharge chargeCount = userRechargeManage.chargeRecordCount(query);
        request.setAttribute("czze", czze);
        request.setAttribute("czsxf", czsxf);
        request.setAttribute("result", result);
        request.setAttribute("chargeCount", chargeCount);
        forwardView(request, response, getClass());
    }
}
