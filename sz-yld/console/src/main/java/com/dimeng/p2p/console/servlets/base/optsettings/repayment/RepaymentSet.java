package com.dimeng.p2p.console.servlets.base.optsettings.repayment;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S62.entities.T6290;
import com.dimeng.p2p.S62.enums.T6290_F04;
import com.dimeng.p2p.common.entities.BillRemindSet;
import com.dimeng.p2p.console.servlets.base.AbstractBaseServlet;
import com.dimeng.p2p.modules.base.console.service.RepaymentManage;

@Right(id = "P2P_C_BASE_REPAYMENTSET_SET", name = "账单提醒设置", moduleId = "P2P_C_BASE_OPTSETTINGS_REPAYMENT")
public class RepaymentSet extends AbstractBaseServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        RepaymentManage repaymentManage = serviceSession.getService(RepaymentManage.class);
        //        T6290 t6290 = repaymentManage.getRepaymentSet();
        //        request.setAttribute("model", t6290);
        //        super.processGet(request, response, serviceSession);
        
        //还款提醒
        List<T6290> hkTxList = repaymentManage.getT6290List(T6290_F04.HKTX, null);
        //逾期提醒）
        List<T6290> yqTxList = repaymentManage.getT6290List(T6290_F04.YQTX, null);
        request.setAttribute("hkTxList", hkTxList);
        request.setAttribute("yqTxList", yqTxList);
        forwardView(request, response, getClass());
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        
        RepaymentManage repaymentManage = serviceSession.getService(RepaymentManage.class);
        BillRemindSet billRemindSet = new BillRemindSet();
        billRemindSet.hkDay = request.getParameterValues("hkDay");
        billRemindSet.yqDay = request.getParameterValues("yqDay");
        billRemindSet.hktxType = request.getParameterValues("hktxType");
        billRemindSet.hkStatus = request.getParameter("hkStatus");
        billRemindSet.yqtxType = request.getParameterValues("yqtxType");
        billRemindSet.yqStatus = request.getParameter("yqStatus");
        repaymentManage.updateT6290(billRemindSet);
        prompt(request, response, PromptLevel.INFO, "保存成功");
        sendRedirect(request, response, getController().getURI(request, RepaymentSet.class));
        /*String[] ways=request.getParameterValues("F02");
        StringBuffer wayStr = new StringBuffer("");
        for(String s : ways) {
        	wayStr.append(","+s);
        }   
        
        if(!wayStr.toString().equals("")){
        	wayStr=new StringBuffer(wayStr.substring(1));
        }
        
        RepaymentManage repaymentManage = serviceSession.getService(RepaymentManage.class);
        T6290 t6290=new T6290();
        t6290.parse(request);
        t6290.F02=wayStr.toString();
        if(t6290.F01>0){
        	repaymentManage.update(t6290);
        }
        else{
        	repaymentManage.add(t6290);
        }
        request.setAttribute("model", t6290);
        getController().prompt(request,
                response,
                PromptLevel.INFO,
                "设置成功");
        forwardView(request, response, getClass());*/
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        getResourceProvider().log(throwable.getMessage());
        if (throwable instanceof SQLException)
        {
            logger.error(throwable, throwable);
            getController().prompt(request, response, PromptLevel.ERROR, "系统繁忙，请您稍后再试");
        }
        else if (throwable instanceof LogicalException || throwable instanceof ParameterException)
        {
            getController().prompt(request, response, PromptLevel.WARRING, throwable.getMessage());
        }
        else
        {
            super.onThrowable(request, response, throwable);
        }
        sendRedirect(request, response, getController().getURI(request, RepaymentSet.class));
    }
    
}
