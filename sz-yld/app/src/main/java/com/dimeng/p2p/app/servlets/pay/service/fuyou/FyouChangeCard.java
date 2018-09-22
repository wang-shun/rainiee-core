/*package com.dimeng.p2p.app.servlets.pay.service.fuyou;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bouncycastle.util.encoders.Base64;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.app.config.Config;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.escrow.fuyou.cond.BankCond;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouTypeEnum;
import com.dimeng.p2p.escrow.fuyou.service.BankManage;
import com.dimeng.p2p.escrow.fuyou.util.MchntTxnSsn;
import com.dimeng.p2p.escrow.fuyou.variables.FuyouVariable;
import com.dimeng.util.StringHelper;

*//**
 * 
 * 修改银行卡
 * <只支持个人用户，企业用户为线下申请打操作>
 * @author  suwei
 * @version  [版本号, 2016年5月5日]
 *//*
public class FyouChangeCard extends AbstractFuyouServlet
{
    
    *//**
     * 注释内容
     *//*
    private static final long serialVersionUID = 6340335130640232261L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        logger.info("APP修改银行卡");
        // 分析从对方传回来的数据
        BankManage manage = serviceSession.getService(BankManage.class);
        final HashMap<String, String> params = manage.getUsrCustInfo();
        if (StringHelper.isEmpty(params.get("usrCustId")))
        {
            setReturnMsg(request, response, ExceptionCode.ERROR, "用户第三方标识不存在!");
            return;
        }
        if (StringHelper.isEmpty(params.get("bankId")))
        {
            setReturnMsg(request, response, ExceptionCode.ERROR, "银行卡为空，不能申请更换银行卡！");
            return;
        }
        final String mchnt_txn_ssn = MchntTxnSsn.getMts(FuyouTypeEnum.YHKK.name());
        manage.insertT6114Ext(Integer.parseInt(params.get("bankId")), mchnt_txn_ssn);
        final String mchnt_cd = getConfigureProvider().format(FuyouVariable.FUYOU_ACCOUNT_ID);
        
        *//**
         * 查询
        BankQueryFace face = new BankQueryFace();
        Map<String, Object> parma =
            face.executeBankQuery(mchnt_cd,
                mchnt_txn_ssn,
                params.get("usrCustId"),
                "",
                getConfigureProvider().format(FuyouVariable.FUYOU_QUERYCHANGECARD_URL),
                serviceSession);
        *//*
        
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
                return getSiteDomain("/pay/service/fuyou/ret/changeCardRet.htm");
            }
        });
        String formUrl = trimBlank(getConfigureProvider().format(FuyouVariable.FUYOU_APP_CHANGECARD_URL));
        // 向第三方发送请求
        sendHttp(param, formUrl, response, true);
        logger.info("申请更换银行卡发送数据...");
    }
    
    *//**
     * 将异常消息返回给页面
     *//*
    @SuppressWarnings("deprecation")
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        String enRetUrl = Config.changeCardRetUrl;
        String url =
            enRetUrl + "?" + "code=000004&description="
                + URLEncoder.encode(new String(Base64.encode(throwable.getMessage().getBytes("UTF-8")), "UTF-8"));
        
        response.sendRedirect(url);
        getResourceProvider().log(throwable);
    }
}
*/