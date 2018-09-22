/*
 * 文 件 名:  CheckCategoryExists.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoucl
 * 修改时间:  2016年1月4日
 */
package com.dimeng.p2p.console.servlets.base.malloptsettings.category;

import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S63.entities.T6350;
import com.dimeng.p2p.console.servlets.AbstractMallServlet;
import com.dimeng.p2p.repeater.score.CommodityCategoryManage;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * <检查类别是否唯一>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2016年1月4日]
 */
public class CheckCategoryExists extends AbstractMallServlet
{

    /**
     * 注释内容
     */
    private static final long serialVersionUID = 4978908920522926215L;
    
    @Override
    protected void processGet(HttpServletRequest request,
            HttpServletResponse response, ServiceSession serviceSession)
            throws Throwable {
        processPost(request, response, serviceSession);
    }

    @Override
    protected void processPost(HttpServletRequest request,
            HttpServletResponse response, ServiceSession serviceSession)
            throws Throwable {
        String name = request.getParameter("name");
        CommodityCategoryManage manager = serviceSession.getService(CommodityCategoryManage.class);
        T6350 t6350 = manager.getT6350(name);
        PrintWriter out = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        if(null != t6350){
            jsonMap.put("num", 0);
            jsonMap.put("F01", t6350.F01);
        }else{
            jsonMap.put("num", 1);
            jsonMap.put("msg", "success");
        }
        out.print(objectMapper.writeValueAsString(jsonMap));
        out.close();
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request,
            HttpServletResponse response, Throwable throwable)
            throws ServletException, IOException {
        if (throwable instanceof SQLException
                || throwable instanceof ParameterException
                || throwable instanceof LogicalException) {
            getController().prompt(request, response, PromptLevel.ERROR,
                    throwable.getMessage());
            throwable.getStackTrace();
            PrintWriter out = response.getWriter();
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> jsonMap = new HashMap<String, Object>();
            jsonMap.put("num", 2);
            jsonMap.put("msg", throwable.getMessage());
            out.print(objectMapper.writeValueAsString(jsonMap));
            out.close();
        } else {
            super.onThrowable(request, response, throwable);
        }
    }
}
