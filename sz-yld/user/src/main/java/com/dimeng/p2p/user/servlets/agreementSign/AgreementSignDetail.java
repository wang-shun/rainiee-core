package com.dimeng.p2p.user.servlets.agreementSign;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.modules.bid.user.service.AgreementSignManage;
import com.dimeng.p2p.modules.bid.user.service.entity.AgreementSign;
import com.dimeng.p2p.user.servlets.AbstractUserServlet;

public class AgreementSignDetail extends AbstractUserServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        AgreementSignManage manage = serviceSession.getService(AgreementSignManage.class);
        AgreementSign[] agreementSaveLists = manage.getAgreementSaveList();
        //判断是否网签
        boolean isNetSign = manage.isNetSign();
        //判断是否保全
        boolean isSaveAgreement = manage.isSaveAgreement();
        PrintWriter out = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("isNetSign", isNetSign);
        jsonMap.put("isSaveAgreement", isSaveAgreement);
        jsonMap.put("agreementSaveList", agreementSaveLists);
        out.print(objectMapper.writeValueAsString(jsonMap));
        out.close();
    }
}
