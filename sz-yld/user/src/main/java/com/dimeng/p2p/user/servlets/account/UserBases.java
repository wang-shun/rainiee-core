package com.dimeng.p2p.user.servlets.account;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.entities.T6112;
import com.dimeng.p2p.S61.entities.T6113;
import com.dimeng.p2p.S61.entities.T6142;
import com.dimeng.p2p.S61.entities.T6143;
import com.dimeng.p2p.account.user.service.RzxxManage;
import com.dimeng.p2p.account.user.service.UserBaseManage;
import com.dimeng.p2p.account.user.service.entity.RzxxInfo;
import com.dimeng.p2p.account.user.service.entity.UserBase;
import com.dimeng.p2p.account.user.service.entity.XyInfo;
import com.dimeng.p2p.account.user.service.entity.Xyjl;
import com.dimeng.util.parser.IntegerParser;

public class UserBases extends AbstractAccountServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        UserBaseManage userManage = serviceSession.getService(UserBaseManage.class);
        RzxxManage rzManage = serviceSession.getService(RzxxManage.class);
        int qyFlag = Integer.parseInt(request.getParameter("qyFlag"));
        
        Paging paging = new Paging()
        {
            
            @Override
            public int getSize()
            {
                return 10;
            }
            
            @Override
            public int getCurrentPage()
            {
                return IntegerParser.parse(request.getParameter("currentPage"));
            }
        };
        
        UserBase userbaseData = null;
        PagingResult<T6142> resultXlxx = null;
        PagingResult<T6143> resultGzxx = null;
        PagingResult<T6112> resultFcxx = null;
        PagingResult<T6113> resultCcxx = null;
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        if (1 == qyFlag)
        {
            userbaseData = userManage.getUserBase();
            jsonMap.put("userbaseData", userbaseData);
        }
        else if (2 == qyFlag)
        {
            resultXlxx = userManage.seachXlxx(paging);
            jsonMap.put("resultXlxx", resultXlxx.getItems());
            jsonMap.put("pageCount", resultXlxx.getPageCount());
            String pageStr = rendPaging(resultXlxx);
            jsonMap.put("pageStr", pageStr);
            
        }
        else if (3 == qyFlag)
        {
            resultGzxx = userManage.seachGzxx(paging);
            jsonMap.put("resultGzxx", resultGzxx.getItems());
            jsonMap.put("pageCount", resultGzxx.getPageCount());
            String pageStr = rendPaging(resultGzxx);
            jsonMap.put("pageStr", pageStr);
        }
        else if (4 == qyFlag)
        {
            resultFcxx = userManage.seachFcxx(paging);
            jsonMap.put("resultFcxx", resultFcxx.getItems());
            jsonMap.put("pageCount", resultFcxx.getPageCount());
            String pageStr = rendPaging(resultFcxx);
            jsonMap.put("pageStr", pageStr);
        }
        else if (5 == qyFlag)
        {
            resultCcxx = userManage.seachCcxx(paging);
            jsonMap.put("resultCcxx", resultCcxx.getItems());
            jsonMap.put("pageCount", resultCcxx.getPageCount());
            String pageStr = rendPaging(resultCcxx);
            jsonMap.put("pageStr", pageStr);
        }
        else if (6 == qyFlag)
        {
            RzxxInfo[] rzxx = rzManage.getGRInfo();
            RzxxInfo[] mustRz = rzManage.getGRMustInfo();
            XyInfo xyInfo = rzManage.getXyInfo();
            Xyjl xyjl = rzManage.getXyjl();
            jsonMap.put("rzxx", rzxx);
            jsonMap.put("mustRz", mustRz);
            jsonMap.put("xyInfo", xyInfo);
            jsonMap.put("xyjl", xyjl);
        }
        PrintWriter out = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        jsonMap.put("qyFlag", qyFlag);
        out.print(objectMapper.writeValueAsString(jsonMap));
        out.close();
    }
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
}
