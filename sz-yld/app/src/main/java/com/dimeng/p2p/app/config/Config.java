package com.dimeng.p2p.app.config;

public interface Config
{
    /**
     * 基础路径
     */
    String baseUrl = "/".concat(AppConst.MODULE);
    
    /***第三方处理回调链接**/
    /**充值*/
    String chargeRetUrl = baseUrl.concat("/pay/service/ret/appChargeRet.htm");
    
    /**注册*/
    String registerRetUrl = baseUrl.concat("/pay/service/ret/appRegisterRet.htm");
    
    /**提现*/
    String withdrawRetUrl = baseUrl.concat("/pay/service/ret/appWithdrawRet.htm");
    
    /**购买标*/
    String buyBidRetUrl = baseUrl.concat("/pay/service/ret/appBuyBidRet.htm");
    
    /**购买债权*/
    String buyCreditorRetUrl = baseUrl.concat("/pay/service/ret/appBuyCreditorRet.htm");
    
    /**授权*/
    String authorizeRetUrl = baseUrl.concat("/pay/service/ret/appAuthorizeRet.htm");
    
    /**绑卡*/
    String bindCardRetUrl = baseUrl.concat("/pay/service/ret/appBindCardRet.htm");
    
    /**解绑卡*/
    String unBindCardRetUrl = baseUrl.concat("/pay/service/ret/appUnBindCardRet.htm");
    
    /**公益标捐赠*/
    String buyGyBidRetUrl = baseUrl.concat("/pay/service/ret/appBuyGyBidRet.htm");
    
    /**还款*/
    String repaymentRetUrl = baseUrl.concat("/pay/service/ret/appRepaymentRet.htm");
    
    /**购买商品*/
    String buyGoodRetUrl = baseUrl.concat("/pay/service/ret/appBuyGoodRet.htm");
    
    /**修改银行卡*/
    String changeCardRetUrl = baseUrl.concat("/pay/service/ret/appChangeCardRet.htm");

}
