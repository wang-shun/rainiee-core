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
import com.dimeng.p2p.S61.entities.T6161;
import com.dimeng.p2p.S61.entities.T6162;
import com.dimeng.p2p.S61.entities.T6163;
import com.dimeng.p2p.S61.entities.T6164;
import com.dimeng.p2p.account.user.service.QyBaseManage;
import com.dimeng.p2p.account.user.service.RzxxManage;
import com.dimeng.p2p.account.user.service.entity.RzxxInfo;
import com.dimeng.p2p.account.user.service.entity.XyInfo;
import com.dimeng.p2p.account.user.service.entity.Xyjl;
import com.dimeng.util.parser.IntegerParser;

public class QyBases extends AbstractAccountServlet
{
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        QyBaseManage manage = serviceSession.getService(QyBaseManage.class);
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
        
        T6161 qyBases = null;
        T6162 qyJszl = null;
        T6164 qyLxxx = null;
        PagingResult<T6113> resultCcxx = null;
        PagingResult<T6112> resultFcxx = null;
        T6163[] resultCwzl = null;
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        if (1 == qyFlag)
        {
            qyBases = manage.getQyjbxx();
            jsonMap.put("qyBases", qyBases);
        }
        else if (2 == qyFlag)
        {
            qyJszl = manage.getQyjs();
            jsonMap.put("qyJszl", qyJszl);
        }
        else if (4 == qyFlag)
        {
            qyLxxx = manage.getQylxxx();
            jsonMap.put("qyLxxx", qyLxxx);
        }
        else if (5 == qyFlag)
        {
            resultCcxx = manage.getQyccxx(paging);
            jsonMap.put("resultCcxx", resultCcxx.getItems());
            jsonMap.put("pageCount", resultCcxx.getPageCount());
            String pageStr = rendPaging(resultCcxx);
            jsonMap.put("pageStr", pageStr);
            
        }
        else if (6 == qyFlag)
        {
            resultFcxx = manage.getQyfcxx(paging);
            jsonMap.put("resultFcxx", resultFcxx.getItems());
            jsonMap.put("pageCount", resultFcxx.getPageCount());
            String pageStr = rendPaging(resultFcxx);
            jsonMap.put("pageStr", pageStr);
        }
        else if (3 == qyFlag)
        {
            resultCwzl = manage.getQycwzk();
            jsonMap.put("resultCwzl", resultCwzl);
        }
        else if (7 == qyFlag)
        {
            RzxxInfo[] rzxx = rzManage.getQYInfo();
            RzxxInfo[] mustRz = rzManage.getQYMustInfo();
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
