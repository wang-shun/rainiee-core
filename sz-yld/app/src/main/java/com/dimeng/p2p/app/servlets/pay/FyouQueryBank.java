package com.dimeng.p2p.app.servlets.pay;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S61.entities.T6114_EXT;
import com.dimeng.p2p.app.servlets.pay.service.fuyou.AbstractFuyouServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
/*import com.dimeng.p2p.escrow.fuyou.service.BankManage;
import com.dimeng.p2p.escrow.fuyou.variables.FuyouVariable;*/

/**
 * 
 * 更换银行卡查询
 * 
 * @author  zhongsai
 * @version  [版本号, 2018年04月20日]
 */
public class FyouQueryBank extends AbstractFuyouServlet
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {/*
        logger.info("APP更换银行卡查询");
        String mchnt_txn_ssn = request.getParameter("mchnt_txn_ssn");
       
        T6114_EXT t6114_EXT = manage.selectT6114Ext(mchnt_txn_ssn);
        if (t6114_EXT == null)
        {
            setReturnMsg(request, response, ExceptionCode.SUCCESS, "该用户无更换银行卡的申请", "该用户无更换银行卡的申请");
            return;
        }
        String msg = null;
        switch (t6114_EXT.F04.name())
        {
            case "YTJ":
                msg =
                    manage.queryFuyou(serviceSession,
                        t6114_EXT.F08,
                        getConfigureProvider().format(FuyouVariable.FUYOU_ACCOUNT_ID),
                        getConfigureProvider().format(FuyouVariable.FUYOU_QUERYCHANGECARD_URL), true);
                break;
            
            case "DTJ":
                msg =
                    manage.queryFuyou(serviceSession,
                        t6114_EXT.F08,
                        getConfigureProvider().format(FuyouVariable.FUYOU_ACCOUNT_ID),
                        getConfigureProvider().format(FuyouVariable.FUYOU_QUERYCHANGECARD_URL), false);
                break;
                
            case "SB":
                msg = "更换银行卡的申请失败";
                break;
            
            case "CG":
                msg = "更换银行卡的申请成功";
                break;
            
            default:
                break;
        }
        
        if (msg == "OK")
        {
            msg = "银行卡更换成功";
        }
        else if(msg == "SHSB")
        {
            msg = "审核未通过,启动原银行卡";
        }
        
        setReturnMsg(request, response, ExceptionCode.SUCCESS, msg, msg);
        return;
    */}
}
