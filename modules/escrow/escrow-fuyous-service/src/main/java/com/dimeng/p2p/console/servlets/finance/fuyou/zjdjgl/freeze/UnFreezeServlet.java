/*
 * 文 件 名:  UnFreezeServlet.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  lingyuanjie
 * 修改时间:  2016年6月3日
 */
package com.dimeng.p2p.console.servlets.finance.fuyou.zjdjgl.freeze;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.servlet.Controller;
import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.common.FormToken;
import com.dimeng.p2p.console.servlets.finance.fuyou.zjdjgl.AbstractZjdjglServlet;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouRespCode;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouTypeEnum;
import com.dimeng.p2p.escrow.fuyou.entity.freeze.T6170;
import com.dimeng.p2p.escrow.fuyou.entity.unfreeze.UnFreezeRet;
import com.dimeng.p2p.escrow.fuyou.face.FundUnFreezeFace;
import com.dimeng.p2p.escrow.fuyou.service.unfreeze.UnFreezeManage;
import com.dimeng.p2p.escrow.fuyou.util.BackCodeInfo;
import com.dimeng.p2p.escrow.fuyou.util.MchntTxnSsn;
import com.dimeng.p2p.escrow.fuyou.variables.FuyouVariable;
import com.dimeng.util.StringHelper;

/**
 * 资金解冻 Servlet
 * <功能详细描述>
 * 
 * @author  lingyuanjie
 * @version  [版本号, 2016年6月2日]
 */
@Right(id = "P2P_C_FINANCE_UNFREEZE_SERVLET", name = "资金解冻操作", moduleId = "P2P_C_FUYOU_ZJDJGL_ZJDJ", order = 5)
public class UnFreezeServlet extends AbstractZjdjglServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        String name = request.getParameter("name"); //用户帐号
        if (!FormToken.verify(serviceSession.getSession(), request.getParameter("token")))
        {
            getController().prompt(request, response, PromptLevel.WARRING, "请不要重复提交请求！");
            request.setAttribute("name", name);
            sendRedirect(request, response, "/console/finance/fuyou/zjdjgl/freeze/freezeRecordView.htm?name=" + name);
            return;
        }
        Controller controller = getController();
        
        String serialNo = request.getParameter("serialNo");//冻结流水号
        
        UnFreezeManage unFreezeManage = serviceSession.getService(UnFreezeManage.class);
        ConfigureProvider configureProvider = this.getResourceProvider().getResource(ConfigureProvider.class);
        
        if (StringHelper.isEmpty(serialNo) || StringHelper.isEmpty(name))
        {
            getController().prompt(request, response, PromptLevel.INFO, "解冻失败！数值不能为空");
            request.setAttribute("name", name);
            forward(request, response, controller.getURI(request, FreezeRecordView.class));
            return;
        }
        else
        {
            //获取参数 
            FundUnFreezeFace face = new FundUnFreezeFace();
            
            //查询资金冻结记录
            T6170 t6170 = unFreezeManage.selectT6170(serialNo);
            
            //商户代码
            String mchntCd = configureProvider.format(FuyouVariable.FUYOU_ACCOUNT_ID);
            
            //解冻流水号
            String mchntTxnSsn = MchntTxnSsn.getMts(FuyouTypeEnum.ZJJD.name());
            this.log("------------- 解冻流水号：" + mchntTxnSsn + "----------------");
            
            //用户第三方帐号
            String custNo = unFreezeManage.getAccountId(name);
            
            //解冻金额
            String amt = t6170.F04.toString();
            
            String rem = "";
            String actionUrl = configureProvider.format(FuyouVariable.FUYOU_FUND_UNFREEZE_URL);
            
            //请求接口
            UnFreezeRet unFreeze =
                face.executeFundUnFreeze(mchntCd, mchntTxnSsn, custNo, amt, rem, actionUrl, serviceSession);
            
            //处理请求结果
            if (FuyouRespCode.JYCG.getRespCode().equals(unFreeze.getRespCode()))
            {
                //登录帐号
                unFreeze.setCustNo(t6170.F03);
                //冻结流水号
                unFreeze.setFreezeSerialNo(serialNo);
                //设定的解冻时间
                unFreeze.setThawDate(t6170.F05);
                //更新T6101用户资金
                unFreezeManage.updateT6101(unFreeze);
                
                this.log("-------------------- 资金解冻成功 ---------------------");
            }
            else
            {
                this.log("解冻失败!失败的原因[" + BackCodeInfo.info(unFreeze.getRespCode()) + "]");
                getController().prompt(request,
                    response,
                    PromptLevel.INFO,
                    "解冻失败!失败的原因[" + BackCodeInfo.info(unFreeze.getRespCode()) + "]");
                request.setAttribute("name", name);
                forward(request, response, controller.getURI(request, FreezeRecordView.class));
                
                return;
            }
        }
        request.setAttribute("name", name);
        controller.prompt(request, response, PromptLevel.INFO, "解冻成功！");
        forward(request, response, controller.getURI(request, FreezeRecordView.class));
        
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        Controller controller = getController();
        controller.prompt(request, response, PromptLevel.INFO, throwable.getMessage());
        sendRedirect(request, response, controller.getViewURI(request, FreezeRecordView.class));
    }
    
}
