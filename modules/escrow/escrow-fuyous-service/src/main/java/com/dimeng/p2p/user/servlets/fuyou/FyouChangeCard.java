package com.dimeng.p2p.user.servlets.fuyou;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.session.authentication.AuthenticationException;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.AbstractFuyouServlet;
import com.dimeng.p2p.escrow.fuyou.cond.BankCond;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouTypeEnum;
import com.dimeng.p2p.escrow.fuyou.service.BankManage;
import com.dimeng.p2p.escrow.fuyou.util.MchntTxnSsn;
import com.dimeng.p2p.escrow.fuyou.variables.FuyouVariable;
import com.dimeng.util.StringHelper;

/**
 * 
 * 修改银行卡
 * <只支持个人用户，企业用户为线下申请打操作>
 * @author  heshiping
 * @version  [版本号, 2016年1月19日]
 */
public class FyouChangeCard extends AbstractFuyouServlet
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 6340335130640232261L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        logger.info("修改银行卡");
        // 分析从对方传回来的数据
        BankManage manage = serviceSession.getService(BankManage.class);
        final HashMap<String, String> params = manage.getUsrCustInfo();
        if (StringHelper.isEmpty(params.get("usrCustId")))
        {
            getController().prompt(request, response, PromptLevel.ERROR, "用户第三方标识不存在!");
            getController().forwardView(request, response, FyouChangeCard.class);
            return;
        }
        final String mchnt_txn_ssn = MchntTxnSsn.getMts(FuyouTypeEnum.YHKK.name());
        manage.insertT6114Ext(Integer.parseInt(params.get("bankId")), mchnt_txn_ssn);
        final String mchnt_cd = getConfigureProvider().format(FuyouVariable.FUYOU_ACCOUNT_ID);
        
        /**
         * 查询
        BankQueryFace face = new BankQueryFace();
        Map<String, Object> parma =
            face.executeBankQuery(mchnt_cd,
                mchnt_txn_ssn,
                params.get("usrCustId"),
                "",
                getConfigureProvider().format(FuyouVariable.FUYOU_QUERYCHANGECARD_URL),
                serviceSession);
        */
        
        
        Map<String, String> param = manage.bankUpdate(new BankCond()
        {
            
            @Override
            public String mchntTxnSsn()
            {
                return mchnt_txn_ssn;
            }
            
            @Override
            public String mchntCd()
                throws IOException
            {
                return mchnt_cd;
            }
            
            @Override
            public String loginId()
            {
                return params.get("usrCustId");
            }
            
            @Override
            public String pageNotifyUrl()
                throws IOException
            {
                return getConfigureProvider().format(FuyouVariable.FUYOU_BANKCARDRET);
            }
        });
        String formUrl = getConfigureProvider().format(FuyouVariable.FUYOU_CHANGECARD_URL);
        // 向第三方发送请求
        sendHttp(param, formUrl, response, true);
        logger.info("申请更换银行卡发送数据...");
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        getResourceProvider().log(throwable);
        String retUrl = request.getHeader("Referer");
        if (throwable instanceof ParameterException || throwable instanceof SQLException)
        {
            getController().prompt(request, response, PromptLevel.ERROR, "系统繁忙，请您稍后再试!");
            sendRedirect(request, response, retUrl);
        }
        else if (throwable instanceof LogicalException)
        {
            getController().prompt(request, response, PromptLevel.WARRING, throwable.getMessage());
            sendRedirect(request, response, retUrl);
        }
        else if (throwable instanceof AuthenticationException)
        {
            sendRedirect(request, response, retUrl);
        }
        else
        {
            super.onThrowable(request, response, throwable);
        }
    }
}
