package com.dimeng.p2p.app.servlets.pay;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.S61.enums.T6114_F08;
import com.dimeng.p2p.account.user.service.BankCardManage;
import com.dimeng.p2p.account.user.service.UserInfoManage;
import com.dimeng.p2p.account.user.service.entity.BankCard;
import com.dimeng.p2p.account.user.service.entity.BankCardQuery;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.common.enums.BankCardStatus;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;

/**
 * 
 * 绑定银行卡
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年11月11日]
 */
public class BindCard extends AbstractSecureServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        // 判断用户是否被拉黑或者锁定
        UserInfoManage userInfoMananage = serviceSession.getService(UserInfoManage.class);
        T6110 t6110 = userInfoMananage.getUserInfo(serviceSession.getSession().getAccountId());
        
        // 用户状态非法
        if (t6110.F07 == T6110_F07.HMD)
        {
            throw new LogicalException("账号异常,请联系客服！");
        }
        
        BankCardManage bankCardManage = serviceSession.getService(BankCardManage.class);
        
        final String banknumber = getParameter(request, "banknumber");
        final int bankId = IntegerParser.parse(getParameter(request, "bankId"));
        final String subbranch = getParameter(request, "subbranch");
        final String xian = getParameter(request, "xian");
        final String name = getParameter(request, "name");
        
        if (bankId <= 0)
        {
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.BANK_ERROR, "银行错误！");
            return;
        }
        if (StringHelper.isEmpty(banknumber) || !checkBankCard(banknumber))
        {
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.BANK_CARD_NO_ERROR, "银行卡号错误！");
            return;
        }
        
        BankCard bcd = bankCardManage.getBankCar(banknumber);
        
        if (!serviceSession.getService(UserInfoManage.class).isSmrz())
        {
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.NO_IDCARD_NAME, "未实名认证！");
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
                return banknumber;
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
        if (bcd != null)
        {
            int userId = serviceSession.getSession().getAccountId();
            //启用银行卡
            int id = bcd.id;
            if (bcd.acount != userId)
            {
            	//判断已经存在的银行卡状态，如果为停用，则其他人可以绑定，否则其他人不可以绑定
                if(bcd.status.equalsIgnoreCase(T6114_F08.TY.name())){
                    bankCardManage.updateTY(id, query, userId);
                }else
                {
            	    setReturnMsg(request, response, ExceptionCode.BANK_CARD_EXIST, "当前银行卡号已存在！");
                    return;
                }
            }else
            {
            	//停用状态下修改银行卡状态
            	if(bcd.status.equalsIgnoreCase(T6114_F08.TY.name())){
            		bankCardManage.update(id, query);
            	}else
            	{
            		setReturnMsg(request, response, ExceptionCode.BANK_CARD_EXIST, "当前银行卡号已存在！");
                    return;
            	}
            }
        }
        else
        {
            bankCardManage.AddBankCar(query);
        }
        
        // 封装返回信息
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success");
        return;
    }
    
    /**
     * 校验银行卡卡号
     * 
     * @param cardId
     * @return
     */
    protected boolean checkBankCard(String cardId)
    {
        if (cardId.trim().length() < 16)
        {
            return false;
        }
        char bit = getBankCardCheckCode(cardId.substring(0, cardId.length() - 1));
        if (bit == 'N')
        {
            return false;
        }
        return cardId.charAt(cardId.length() - 1) == bit;
    }
    
    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     * 
     * @param nonCheckCodeCardId
     * @return
     */
    protected char getBankCardCheckCode(String nonCheckCodeCardId)
    {
        if (nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0
            || !nonCheckCodeCardId.matches("\\d+"))
        {
            // 如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++)
        {
            int k = chs[i] - '0';
            if (j % 2 == 0)
            {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char)((10 - luhmSum % 10) + '0');
    }
    
}
