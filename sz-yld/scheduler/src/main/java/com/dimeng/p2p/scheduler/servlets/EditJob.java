package com.dimeng.p2p.scheduler.servlets;/*
 * 文 件 名:  EditJob.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  heluzhu
 * 修改时间: 2016/3/24
 */

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S66.entities.T6601;
import com.dimeng.p2p.scheduler.core.SchedulerContainer;
import com.dimeng.p2p.service.TaskManage;
import com.dimeng.util.parser.IntegerParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class EditJob extends AbstractSchedulerServlet {

    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession) throws Throwable {
        PrintWriter out = response.getWriter();
        TaskManage manage = serviceSession.getService(TaskManage.class);
        try{
            int id = IntegerParser.parse(request.getParameter("id"));
            T6601 t6601 = manage.queryById(id);
            if("updateStatus".equals(request.getParameter("method"))){
                SchedulerContainer.updateStatus(t6601);
            }else{
                SchedulerContainer.editJob(t6601);
            }
            out.print("{code:0,msg:'success'}");
        }catch (Throwable e){
            logger.error(e, e);
            out.print("{code:1,msg:'fail'}");
        }finally {
            out.close();
        }

    }
}
