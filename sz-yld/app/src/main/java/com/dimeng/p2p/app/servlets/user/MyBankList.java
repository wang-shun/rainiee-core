package com.dimeng.p2p.app.servlets.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.account.user.service.TxManage;
import com.dimeng.p2p.account.user.service.entity.BankCard;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.variables.defines.SystemVariable;


/**
 * user银行卡
 * @author tanhui
 *
 */
public class MyBankList extends AbstractSecureServlet
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
        
        TxManage manage = serviceSession.getService(TxManage.class);
        List<com.dimeng.p2p.app.servlets.user.domain.BankCard> myBankList =
            new ArrayList<com.dimeng.p2p.app.servlets.user.domain.BankCard>();
        BankCard[] cards = manage.bankCards();
        if (cards != null)
        {
            for (BankCard card : cards)
            {
                com.dimeng.p2p.app.servlets.user.domain.BankCard bc =
                    new com.dimeng.p2p.app.servlets.user.domain.BankCard();
                bc.setAccount(card.acount);
                bc.setBankID(card.BankID);
                bc.setBankKhhName(card.BankKhhName);
                bc.setBankname(card.Bankname);
                bc.setBankNumber(card.BankNumber != null ? card.BankNumber.replace("**", "*") : "");
                bc.setCity(card.City);
                bc.setId(card.id);
                bc.setStatus(card.status);
                
                myBankList.add(bc);
            }
        }
        String maxbanks = getConfigureProvider().getProperty(SystemVariable.MAX_BANKCARD_COUNT);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("myBankList", myBankList);
        map.put("maxbanks", maxbanks);
        
        // 判断用户是否已申请更换银行卡
/*        BankManage bankManage = serviceSession.getService(BankManage.class);
        
        boolean flag = bankManage.selectT6114Ext();
        map.put("flag", flag);*/

        // 封装返回信息
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success!", map);
        return;
    }
    
}
