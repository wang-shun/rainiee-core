/*package com.dimeng.p2p.app.servlets.pay.service.fuyou;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bouncycastle.util.encoders.Base64;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S65.enums.T6504_F07;
import com.dimeng.p2p.app.config.Config;
import com.dimeng.p2p.common.enums.FrontLogType;
import com.dimeng.p2p.escrow.fuyou.executor.FYTenderOrderExecutor;
import com.dimeng.p2p.escrow.fuyou.service.BidManage;
import com.dimeng.p2p.modules.bid.pay.service.TenderManage;
import com.dimeng.p2p.variables.defines.URLVariable;
import com.dimeng.util.parser.BigDecimalParser;
import com.dimeng.util.parser.IntegerParser;

*//**
 * 
 * 手动投资
 * <功能详细描述>
 * 
 * @author  suwei
 * @version  [版本号, 2016年03月02日]
 *//*
public class FyouBid extends AbstractFuyouServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        logger.info("APP手动投标——IP:" + request.getRemoteAddr());
        
        // BidManage 继承 TenderManage - 另重写 其方法
        BidManage tenderManage = serviceSession.getService(BidManage.class);
        // 标ID
        int bidId = IntegerParser.parse(request.getParameter("bidId"));
        // 体验金
        String userReward = request.getParameter("userReward");
        // 投资金额
        final BigDecimal amount = BigDecimalParser.parse(request.getParameter("amount"));
        // 交易密码
        String tranPwd = request.getParameter("tranPwd");
//        tranPwd = UnixCrypt.crypt(tranPwd, DigestUtils.sha256Hex(tranPwd));
        // 我的体验金
        String myRewardType = request.getParameter("myRewardType");
        // 红包
        String hbRule = request.getParameter("hbRule");
        // 加息卷
        String jxqRule = request.getParameter("jxqRule");
        // 投资来源
        final String source = getAgentType(request);
        //活动使用方式（ALL：组合使用,SINGLE：单一使用,NONE：不使用）
        final String useType = "SINGLE";
        String usedExp = "no";
        if ("experience".equals(myRewardType))
        {
            usedExp = "yes";
        }
        Map<String, String> prams = new HashMap<String, String>();
        prams.put("source", source);
        // 生成相应订单<保存在map内>
        Map<String, String> rtnMap =
            tenderManage.bid(bidId, amount, userReward, tranPwd, myRewardType, hbRule, jxqRule, prams);
        rtnMap.put("userReward", userReward);
        rtnMap.put("myRewardType", myRewardType);
        // 红包
        rtnMap.put("hbRule", hbRule);
        // 加息卷
        rtnMap.put("jxqRule", jxqRule);
        
           // 业务订单执行
           FYTenderOrderExecutor executor = getResourceProvider().getResource(FYTenderOrderExecutor.class);
           //余额投标订单
           int orderId = IntegerParser.parse(rtnMap.get("orderId"));
           //tenderManage.updateT6501(orderId, MchntTxnSsn.getMts(FuyouTypeEnum.SDTB.name()));
           // 提交订单
           executor.submit(orderId, rtnMap);
           logger.info("投标成功—富友预授权成功-订单：" + orderId + " -合同号：" + rtnMap.get("contract_no"));
           //确认订单
           executor.confirm(orderId, rtnMap);
           tenderManage.writeFrontLog(FrontLogType.SDTB.getName(), " 手动投资-标Id:" + bidId + "-金额：" + amount);
           
           String url = getSiteDomain(Config.buyBidRetUrl) + "?code=000000&description=success";
           sendRedirect(request, response, url);
           //        setReturnMsg(request, response, ExceptionCode.SUCCESS, "恭喜您，投资成功");
           return;
        
        normalBid(request,
            response,
            serviceSession,
            bidId,
            userReward,
            useType,
            hbRule,
            jxqRule,
            usedExp,
            amount,
            tranPwd,
            myRewardType);
        return;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    protected void onThrowable(final HttpServletRequest request, final HttpServletResponse response,
        final Throwable throwable)
        throws ServletException, IOException
    {
        String enRetUrl = Config.buyBidRetUrl;
        String url =
            enRetUrl + "?" + "code=000004&description="
                + URLEncoder.encode(new String(Base64.encode(throwable.getMessage().getBytes("UTF-8")), "UTF-8"));
        
        response.sendRedirect(url);
        getResourceProvider().log(throwable);
    }
    
    protected String getURL(int loanId)
        throws IOException
    {
        ResourceProvider resourceProvider = getResourceProvider();
        final ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        StringBuilder url = new StringBuilder(configureProvider.format(URLVariable.FINANCING_SBTZ_XQ));
        url.append(Integer.toString(loanId)).append(resourceProvider.getSystemDefine().getRewriter().getViewSuffix());
        return url.toString();
    }
    
    private void normalBid(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession,
        int loanId, String userReward, String useType, String hbRule, String jxqRule, String usedExp,
        BigDecimal amount, String tranPwd, String myRewardType)
        throws Throwable
    {
        TenderManage tenderManage = serviceSession.getService(TenderManage.class);
         final ConfigureProvider configureProvider = getResourceProvider().getResource(ConfigureProvider.class);
         boolean isOpenWithPsd = BooleanParser.parse(configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD));
         if (isOpenWithPsd)
         {
             if (StringHelper.isEmpty(tranPwd))
             {
                 throw new LogicalException("交易密码不能为空");
             }
             tranPwd = RSAUtils.decryptStringByJs(tranPwd);
             tranPwd = UnixCrypt.crypt(tranPwd, DigestUtils.sha256Hex(tranPwd));
         }
        
        if ("ALL".equals(useType))
        {
            userReward = "isUsed";
            myRewardType = "ALL";
        }
        Map<String, String> parMap = new HashMap<String, String>();
        parMap.put("source", T6504_F07.PC.name());
        parMap.put("usedExp", usedExp);
        Map<String, String> rtnMap =
            tenderManage.bid(loanId, amount, userReward, tranPwd, myRewardType, hbRule, jxqRule, parMap);
        rtnMap.put("userReward", userReward);
        rtnMap.put("myRewardType", myRewardType);
        rtnMap.put("hbRule", hbRule);
        rtnMap.put("jxqRule", jxqRule);
        rtnMap.put("usedExp", usedExp);
        
        // 业务订单执行
        FYTenderOrderExecutor executor = getResourceProvider().getResource(FYTenderOrderExecutor.class);
        //余额投标订单
        int orderId = IntegerParser.parse(rtnMap.get("orderId"));
        // 提交订单
        executor.submit(orderId, rtnMap);
        //确认订单
        executor.confirm(orderId, rtnMap);
        
        logger.info("投标成功—富友预授权成功-订单：" + orderId + " -合同号：" + rtnMap.get("contract_no"));
        //确认订单
        executor.confirm(orderId, rtnMap);
        tenderManage.writeFrontLog(FrontLogType.SDTB.getName(), " 手动投资-标Id:" + loanId + "-金额：" + amount);
        
        String url = getSiteDomain(Config.buyBidRetUrl) + "?code=000000&description=success";
        sendRedirect(request, response, url);
        
    }

}
*/