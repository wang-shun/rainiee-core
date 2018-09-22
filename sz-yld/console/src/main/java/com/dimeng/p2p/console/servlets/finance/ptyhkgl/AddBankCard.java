/*
 * 文 件 名:  AddBankCard.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2015年12月19日
 */
package com.dimeng.p2p.console.servlets.finance.ptyhkgl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S61.enums.T6114_F08;
import com.dimeng.p2p.common.enums.BankCardStatus;
import com.dimeng.p2p.console.servlets.finance.AbstractFinanceServlet;
import com.dimeng.p2p.modules.account.console.service.BankCardManage;
import com.dimeng.p2p.modules.account.console.service.entity.BankCard;
import com.dimeng.p2p.modules.account.console.service.query.BankCardQuery;
import com.dimeng.util.parser.IntegerParser;

/**
 * <平台新增银行卡>
 * <功能详细描述>
 * 
 * @author  xiaoqi
 * @version  [版本号, 2015年12月19日]
 */
@Right(id = "P2P_C_FINANCE_ADDBANKCARD", name = "新增平台银行卡")
public class AddBankCard extends AbstractFinanceServlet
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -7720982839248783174L;
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        BankCardManage bankCardManage = serviceSession.getService(BankCardManage.class);
        String banknumber = request.getParameter("banknumber").replaceAll("\\s", "");
        BankCard bcd = bankCardManage.getBankCar(banknumber);
        
        BankCardQuery query = new BankCardQuery()
        {
            @Override
            public String getSubbranch()
            {
                return request.getParameter("subbranch");
            }
            
            @Override
            public String getStatus()
            {
                return BankCardStatus.QY.name();
            }
            
            @Override
            public String getCity()
            {
                return request.getParameter("xian");
            }
            
            @Override
            public String getBankNumber()
            {
                return request.getParameter("banknumber").replaceAll("\\s", "");
            }
            
            @Override
            public int getBankId()
            {
                return IntegerParser.parse(request.getParameter("bankname"));
            }
            
            @Override
            public int getAcount()
            {
                return serviceSession.getSession().getAccountId();
            }
            
            @Override
            public String getUserName()
            {
                return request.getParameter("userName");
            }
            
            @Override
            public int getType()
            {
                return Integer.parseInt(request.getParameter("type"));
            }
        };
        if (bcd != null)
        {
            //查找平台ID
            int userId = bankCardManage.getPTID();
            //启用银行卡
            int id = bcd.id;
            if (bcd.acount != userId)
            {
                //判断已经存在的银行卡状态，如果为停用，则其他人可以绑定，否则其他人不可以绑定
                if (bcd.status.equalsIgnoreCase(T6114_F08.TY.name()))
                {
                    bankCardManage.updateTY(id, query);
                }
                else
                {
                    getController().prompt(request, response, PromptLevel.ERROR, "当前银行卡号已存在！");
                    getController().forwardView(request, response, getClass());
                    return;
                }
            }
            else
            {
                bankCardManage.update(id, query);
            }
        }
        else
        {
            bankCardManage.AddBankCar(query);
        }
        request.setAttribute("close", "close");
        getController().forwardView(request, response, getClass());
    }
}
