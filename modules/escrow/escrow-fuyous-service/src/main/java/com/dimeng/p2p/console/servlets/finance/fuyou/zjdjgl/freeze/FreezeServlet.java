package com.dimeng.p2p.console.servlets.finance.fuyou.zjdjgl.freeze;

import java.io.IOException;
import java.math.BigDecimal;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.servlet.Controller;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.common.FormToken;
import com.dimeng.p2p.console.servlets.finance.fuyou.zjdjgl.AbstractZjdjglServlet;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouRespCode;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouTypeEnum;
import com.dimeng.p2p.escrow.fuyou.entity.freeze.Freeze;
import com.dimeng.p2p.escrow.fuyou.entity.freeze.FreezeRet;
import com.dimeng.p2p.escrow.fuyou.face.FundFreezeFace;
import com.dimeng.p2p.escrow.fuyou.service.freeze.FreezeManage;
import com.dimeng.p2p.escrow.fuyou.util.BackCodeInfo;
import com.dimeng.p2p.escrow.fuyou.util.MchntTxnSsn;
import com.dimeng.p2p.escrow.fuyou.variables.FuyouVariable;
import com.dimeng.util.StringHelper;

/**
 * 资金冻结管理 
 * <功能详细描述>
 * 
 * @author  lingyuanjie
 * @version  [版本号, 2016年6月2日]
 */
/*@Right(id = "P2P_C_FINANCE_FREEZE_SERVLET", name = "资金冻结提交", moduleId = "P2P_C_FUYOU_ZJDJGL_ZJDJ", order = 3)*/
public class FreezeServlet extends AbstractZjdjglServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        //需要冻结的帐号
        String platformUserNo = request.getParameter("platformUserNo");
        if (!FormToken.verify(serviceSession.getSession(), request.getParameter(FormToken.parameterName())))
        {
            getController().prompt(request, response, PromptLevel.WARRING, "请不要重复提交请求！");
            request.setAttribute("name", platformUserNo);
            sendRedirect(request, response, "/console/finance/fuyou/zjdjgl/freeze/freezeView.htm");
            return;
        }
        //余额
        String money = request.getParameter("money");
        //冻结金额
        String amount = request.getParameter("amount");
        
        //解冻时间
        StringBuffer expired = new StringBuffer(request.getParameter("expired"));
        //        String remark = request.getParameter("remark");
        Controller controller = getController();
        FreezeManage freezeManage = serviceSession.getService(FreezeManage.class);
        ConfigureProvider configureProvider = this.getResourceProvider().getResource(ConfigureProvider.class);
        //查询用户账户信息
        T6101 t6101 = freezeManage.selectT6101(platformUserNo, T6101_F03.WLZH);
        BigDecimal amountbig = new BigDecimal(amount);
        
        if (StringHelper.isEmpty(platformUserNo) && StringHelper.isEmpty(amount)
            && StringHelper.isEmpty(expired.toString()))
        {
            getController().prompt(request, response, PromptLevel.INFO, "冻结失败！数值不能为空");
            request.setAttribute("name", platformUserNo);
            forward(request, response, controller.getURI(request, UpdateFreezeView.class));
            return;
        }
        else if (amountbig.compareTo(t6101.F06) == 1)
        {
            getController().prompt(request, response, PromptLevel.INFO, "冻结失败！往来账户余额不足");
            request.setAttribute("name", platformUserNo);
            forward(request, response, controller.getURI(request, UpdateFreezeView.class));
            return;
        }
        else
        {
            BigDecimal moneybig = new BigDecimal(money);
            if (amountbig.compareTo(moneybig) == 1)
            {
                getController().prompt(request, response, PromptLevel.INFO, "冻结失败！冻结金额不能大于余额");
                request.setAttribute("name", platformUserNo);
                forward(request, response, controller.getURI(request, UpdateFreezeView.class));
                return;
            }
            else
            {
                //获取参数
                FundFreezeFace face = new FundFreezeFace();
                String mchntCd = configureProvider.format(FuyouVariable.FUYOU_ACCOUNT_ID);
                String mchntTxnSsn = MchntTxnSsn.getMts(FuyouTypeEnum.ZJDJ.name());
                String custNo = freezeManage.getAccountId(platformUserNo);
                String amt = amount;
                String rem = "";
                String actionUrl = configureProvider.format(FuyouVariable.FUYOU_FUND_FREEZE_URL);
                //请求接口
                FreezeRet freezeRet =
                    face.executeFundFreeze(mchntCd, mchntTxnSsn, custNo, amt, rem, actionUrl, serviceSession);
                //处理请求结果
                if (FuyouRespCode.JYCG.getRespCode().equals(freezeRet.getRespCode()))
                {
                    //更新T6101用户资金
                    Freeze freeze = new Freeze();
                    freeze.setCustNo(platformUserNo);
                    freeze.setMchntTxnSsn(mchntTxnSsn);
                    freeze.setAmt(amount);
                    freeze.setExpired(expired.toString());
                    //                    freeze.setRem(remark);
                    freezeManage.updateT6101(freeze);
                    this.log("资金冻结管理 ---- 冻结成功!");
                }
                else
                {
                    this.log("冻结失败!失败的原因[" + BackCodeInfo.info(freezeRet.getRespCode()) + "]");
                    getController().prompt(request,
                        response,
                        PromptLevel.INFO,
                        "冻结失败!失败的原因[" + BackCodeInfo.info(freezeRet.getRespCode()) + "]");
                    request.setAttribute("name", platformUserNo);
                    forward(request, response, controller.getURI(request, UpdateFreezeView.class));
                    return;
                }
            }
        }
        //        getController().prompt(request, response, PromptLevel.INFO, "冻结成功！");
        controller.sendRedirect(request, response, controller.getURI(request, FreezeView.class));
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        Controller controller = getController();
        controller.prompt(request, response, PromptLevel.INFO, throwable.getMessage());
        sendRedirect(request, response, controller.getViewURI(request, UpdateFreezeView.class));
    }
    
}
