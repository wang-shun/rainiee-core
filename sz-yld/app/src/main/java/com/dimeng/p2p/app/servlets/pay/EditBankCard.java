package com.dimeng.p2p.app.servlets.pay;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.account.user.service.BankCardManage;
import com.dimeng.p2p.account.user.service.entity.BankCardQuery;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.common.enums.BankCardStatus;
import com.dimeng.util.parser.IntegerParser;

/**
 * 
 * 修改绑定的银行卡
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年11月12日]
 */
public class EditBankCard extends AbstractSecureServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        logger.info("|EditBankCard-in|");
        final String subbranch = getParameter(request, "subbranch");
        final int bankId = IntegerParser.parse(getParameter(request, "bankId"));
        final String xian = getParameter(request, "xian");
        final String name = getParameter(request, "name");
        
        BankCardManage bankCardManage = serviceSession.getService(BankCardManage.class);
        if (bankId <= 0)
        {
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.BANK_ERROR, "银行错误！");
            return;
        }
        
        BankCardQuery query = new BankCardQuery()
        {
            @Override
            public String getSubbranch()
            {
                return subbranch;
            }
            
            @Override
            public String getStatus()
            {
                return BankCardStatus.QY.name();
            }
            
            @Override
            public String getCity()
            {
                return xian;
            }
            
            @Override
            public String getBankNumber()
            {
                return null;
            }
            
            @Override
            public int getBankId()
            {
                return bankId;
            }
            
            @Override
            public int getAcount()
            {
                return serviceSession.getSession().getAccountId();
            }
            
            @Override
            public int getType()
            {
                return 1;
            }
            
            @Override
            public String getUserName()
            {
                return name;
            }
        };
        //启用银行卡
        int id = IntegerParser.parse(getParameter(request, "id"));
        bankCardManage.update(id, query);
        
        // 封装返回信息
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success!");
        return;
    }
}
