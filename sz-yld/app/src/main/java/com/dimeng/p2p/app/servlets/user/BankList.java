package com.dimeng.p2p.app.servlets.user;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.account.user.service.BankCardManage;
import com.dimeng.p2p.account.user.service.entity.Bank;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;

/**
 * 银行列表
 * 
 * @author tanhui
 */
public class BankList extends AbstractSecureServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        
        List<com.dimeng.p2p.app.servlets.user.domain.Bank> bankCarks =
            new ArrayList<com.dimeng.p2p.app.servlets.user.domain.Bank>();
        BankCardManage bankCardManage = serviceSession.getService(BankCardManage.class);
        Bank[] banks = bankCardManage.getBank();
        if (banks != null)
        {
            for (Bank bank : banks)
            {
                com.dimeng.p2p.app.servlets.user.domain.Bank bc = new com.dimeng.p2p.app.servlets.user.domain.Bank();
                bc.setBankName(bank.name);
                bc.setId(bank.id);
                bankCarks.add(bc);
            }
        }

        // 封装返回信息
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success", bankCarks);
        return;
    }
}
