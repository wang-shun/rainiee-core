package com.dimeng.p2p.user.servlets.account;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.session.authentication.AuthenticationException;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6114_F08;
import com.dimeng.p2p.account.user.service.BankCardManage;
import com.dimeng.p2p.account.user.service.TxManage;
import com.dimeng.p2p.account.user.service.UserInfoManage;
import com.dimeng.p2p.account.user.service.entity.BankCard;
import com.dimeng.p2p.account.user.service.entity.BankCardQuery;
import com.dimeng.p2p.common.enums.BankCardStatus;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;

/**
 * 新增银行卡接口
 *
 * @author XiaoLang 2014 © dimeng.net
 * @version v3.0
 * @LastModified Modified 非自然人，银行卡号位数及规则不作限制,by XiaoLang 2014年12月18日
 */
public class Addbankcard extends AbstractAccountServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        BankCardManage bankCardManage = serviceSession.getService(BankCardManage.class);
        BankCard[] card = bankCardManage.getBankCars(BankCardStatus.QY.name());
        ResourceProvider resourceProvider = getResourceProvider();
        ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        int cardNum = IntegerParser.parse(configureProvider.getProperty(SystemVariable.MAX_BANKCARD_COUNT));
        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter out = response.getWriter();
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        if (!serviceSession.getService(TxManage.class).getVerifyStatus()
            || !serviceSession.getService(TxManage.class).getVerifyTradingPsw())
        {
            jsonMap.put("num", "9999");
            jsonMap.put("msg", "未实名认证！");
            out.print(objectMapper.writeValueAsString(jsonMap));
            out.close();
            return;
        }
        if (card.length >= cardNum)
        {
            /*getController().prompt(request, response, PromptLevel.ERROR, "不能添加超过" + cardNum + "张银行卡");
            getController().forwardView(request, response, Addbankcard.class);*/
            jsonMap.put("num", "9999");
            jsonMap.put("msg", "不能添加超过" + cardNum + "张银行卡");
            out.print(objectMapper.writeValueAsString(jsonMap));
            out.close();
            return;
        }
        UserInfoManage userManage = serviceSession.getService(UserInfoManage.class);
        T6110 user = userManage.getUserInfo(serviceSession.getSession().getAccountId());
        String banknumber = request.getParameter("banknumber").replaceAll("\\s", "");
        boolean validCard = StringHelper.isEmpty(banknumber);
        if (user != null && user.F06 == T6110_F06.ZRR)
        {
            String allowBankCards = configureProvider.getProperty(SystemVariable.ALLOW_BANKCARDS);
            if (!StringHelper.isEmpty(allowBankCards))
            {
                String[] allBankCards = StringUtils.split(allowBankCards, ",");
                boolean flag = true;
                for (String allowBankCard : allBankCards)
                {
                    if (allowBankCard.equals(banknumber))
                    {
                        validCard = false;
                        flag = false;
                    }
                }
                if (flag)
                {
                    validCard = !checkBankCard(banknumber);
                }
            }
            else
            {
                validCard = !checkBankCard(banknumber);
            }
        }
        if (validCard)
        {
            /*getController().prompt(request, response, PromptLevel.ERROR, "银行卡错误！");
            getController().forwardView(request, response, Addbankcard.class);*/
            jsonMap.put("num", "0001");
            jsonMap.put("msg", "银行卡错误！");
            out.print(objectMapper.writeValueAsString(jsonMap));
            out.close();
            return;
        }
        BankCard bcd = bankCardManage.getBankCar(banknumber);
        
        if (!serviceSession.getService(UserInfoManage.class).isSmrz())
        {
            //            getController().forwardView(request, response, Addbankcard.class);
            //未实名认证
            jsonMap.put("num", "9999");
            jsonMap.put("msg", "未实名认证！");
            out.print(objectMapper.writeValueAsString(jsonMap));
            out.close();
            return;
        }
        
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
        //添加新的银行卡
        if (bcd == null)
        {
            bankCardManage.AddBankCar(query);
            /*request.setAttribute("close", "close");
            getController().forwardView(request, response, Addbankcard.class);*/
            jsonMap.put("num", "0000");
            jsonMap.put("msg", "添加银行卡成功！");
            out.print(objectMapper.writeValueAsString(jsonMap));
            out.close();
            return;
        }
        
        //添加别人的银行卡
        int userId = serviceSession.getSession().getAccountId();
        if (bcd.acount != userId)
        {
            //判断已经存在的银行卡状态，如果为停用，则其他人可以绑定，否则其他人不可以绑定
            if (bcd.status.equalsIgnoreCase(T6114_F08.TY.name()))
            {
                bankCardManage.updateTY(bcd.id, query, userId);
                /* request.setAttribute("close", "close");
                 getController().forwardView(request, response, Addbankcard.class);*/
                jsonMap.put("num", "0000");
                jsonMap.put("msg", "添加银行卡成功！");
                out.print(objectMapper.writeValueAsString(jsonMap));
                out.close();
                return;
            }
            /*getController().prompt(request, response, PromptLevel.ERROR, "当前银行卡号已存在！");
            getController().forwardView(request, response, Addbankcard.class);*/
            jsonMap.put("num", "0001");
            jsonMap.put("msg", "当前银行卡号已存在！");
            out.print(objectMapper.writeValueAsString(jsonMap));
            out.close();
            return;
        }
        
        //添加自己的银行卡
        //如果该银行卡是启用状态
        if (bcd.status.equalsIgnoreCase(T6114_F08.QY.name()))
        {
            /*getController().prompt(request, response, PromptLevel.ERROR, "当前银行卡号已存在！");
            getController().forwardView(request, response, Addbankcard.class);*/
            jsonMap.put("num", "0001");
            jsonMap.put("msg", "当前银行卡号已存在！");
            out.print(objectMapper.writeValueAsString(jsonMap));
            out.close();
            return;
        }
        //如果该银行卡是停用状态，则改成启用
        bankCardManage.update(bcd.id, query);
        jsonMap.put("num", "0000");
        jsonMap.put("msg", "添加银行卡成功！");
        out.print(objectMapper.writeValueAsString(jsonMap));
        out.close();
    }
    
    /**
     * 校验银行卡卡号
     *
     * @param cardId
     * @return
     */
    protected boolean checkBankCard(String cardId)
    {
        if (cardId == null || cardId.trim().length() < 16)
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
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        getResourceProvider().log(throwable);
        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter out = response.getWriter();
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        if (throwable instanceof SQLException)
        {
            jsonMap.put("num", "9999");
            jsonMap.put("msg", "系统繁忙，请您稍后再试！");
            out.print(objectMapper.writeValueAsString(jsonMap));
            out.close();
            return;
        }
        else if (throwable instanceof ParameterException || throwable instanceof LogicalException)
        {
            jsonMap.put("num", "9999");
            jsonMap.put("msg", throwable.getMessage());
            out.print(objectMapper.writeValueAsString(jsonMap));
            out.close();
            return;
        }
        else if (throwable instanceof AuthenticationException)
        {
            jsonMap.put("num", "0002");
            jsonMap.put("msg", throwable.getMessage());
            out.print(objectMapper.writeValueAsString(jsonMap));
            out.close();
            return;
        }
        else
        {
            jsonMap.put("num", "9999");
            jsonMap.put("msg", throwable.getMessage());
            out.print(objectMapper.writeValueAsString(jsonMap));
            out.close();
            return;
        }
    }
    
}
