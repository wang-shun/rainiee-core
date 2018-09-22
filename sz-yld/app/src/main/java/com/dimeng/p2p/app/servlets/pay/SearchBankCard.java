package com.dimeng.p2p.app.servlets.pay;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S50.entities.T5019;
import com.dimeng.p2p.account.user.service.BankCardManage;
import com.dimeng.p2p.account.user.service.entity.Bank;
import com.dimeng.p2p.account.user.service.entity.BankCard;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.modules.base.front.service.DistrictManage;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;

/**
 * 
 * 查找已绑定银行卡
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年11月12日]
 */
public class SearchBankCard extends AbstractSecureServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        logger.info("|SearchBankCard-in|");
        int bankCardId = IntegerParser.parse(getParameter(request, "bankCardId"));
        if (bankCardId <= 0)
        {
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.PARAMETER_ERROR, "银行卡id错误！");
            return;
        }
        BankCardManage manage = serviceSession.getService(BankCardManage.class);
        
        BankCard bankCard = manage.getBankCar(bankCardId);
        
        int areaId = 0;
        if (null != bankCard && !StringHelper.isEmpty(bankCard.City))
        {
            areaId = IntegerParser.parse(bankCard.City);
        }
        
        if (bankCard == null || areaId <= 0)
        {
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.BANK_CARD_NO_EXIST, "银行卡不存在！");
            return;
        }
        DistrictManage letterManage = serviceSession.getService(DistrictManage.class);
        T5019 t5019 = letterManage.getArea(areaId);
        String bankName = "";
        Bank[] banks = manage.getBank();
        if (banks != null)
        {
            for (Bank bank : banks)
            {
                if (bank.id == bankCard.BankID)
                {
                    bankName = bank.name;
                    break;
                }
            }
        }
        com.dimeng.p2p.app.servlets.user.domain.BankCard bc = new com.dimeng.p2p.app.servlets.user.domain.BankCard();
        bc.setBankID(bankCard.BankID);
        bc.setBankKhhName(bankCard.BankKhhName);
        bc.setBankname(bankName);
        bc.setBankNumber(bankCard.BankNumber);
        bc.setCity(t5019.F06 + t5019.F07 + t5019.F08);
        bc.setCityId(t5019.F01);
        bc.setId(bankCard.id);
        
        // 封装返回信息
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success!", bc);
        return;
    }
    
}
