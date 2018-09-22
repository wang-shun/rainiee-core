package com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import com.alibaba.fastjson.JSONObject;
import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.message.email.EmailSender;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S62.entities.T6230;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.console.servlets.bid.AbstractBidServlet;
/*import com.dimeng.p2p.escrow.huanxun.constant.ResultCode;
import com.dimeng.p2p.escrow.huanxun.service.HuanXunBidRegisterManage;*/
import com.dimeng.p2p.modules.account.console.service.GrManage;
import com.dimeng.p2p.modules.bid.console.service.AddBidManage;
import com.dimeng.p2p.modules.bid.console.service.BidManage;
import com.dimeng.p2p.variables.defines.EmailVariavle;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.parser.IntegerParser;

@Right(id = "P2P_C_LOAN_CHECK", name = "审核", moduleId = "P2P_C_BID_JKGL_LOANMANAGE", order = 4)
public class Check extends AbstractBidServlet
{
    /**
     * 标的审核
     */
    private static final long serialVersionUID = -268955794485557959L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        int loanId = IntegerParser.parse(request.getParameter("loanId"));
        int useId = IntegerParser.parse(request.getParameter("useId"));
        ConfigureProvider configureProvider = getResourceProvider().getResource(ConfigureProvider.class);
        String escrow = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
        BidManage bidManage = serviceSession.getService(BidManage.class);
        String state = bidManage.selectBidState(loanId);
        if (T6230_F20.DSH != T6230_F20.parse(state))
        {
            throw new LogicalException("不是待审核状态不能审核通过");
        }
        
        if ("huifu".equals(escrow))
        {
            AddBidManage addBidMange = serviceSession.getService(AddBidManage.class);
            Map<String, String> map = addBidMange.addBidInfo(loanId);
            if ("02".equals(map.get("stat")) || "06".equals(map.get("stat")))
            {
                bidManage.notThrough(loanId, map.get("desc"));
                // 发送邮件
                EmailSender emailSender = serviceSession.getService(EmailSender.class);
                emailSender.send(0,
                    "审核不通过",
                    String.format(EmailVariavle.JKSHBTG_MAIL_STR.getDescription(), map.get("desc"), ""),
                    serviceSession.getService(GrManage.class).findBasicInfo(useId).mailbox);
            }
        }
        
        //环迅托管
        else if ("huanxun".equalsIgnoreCase(escrow))
        {
            /*HuanXunBidRegisterManage bidRegMgr = serviceSession.getService(HuanXunBidRegisterManage.class);
            T6230 t6230 = bidRegMgr.selectT6230(loanId);
            String retStr_bidReg = bidRegMgr.createBidRegister(t6230);
            String retStr_guarnat = bidRegMgr.createBidGuarantorRegister(t6230);
            JSONObject jsonObj_bidReg = JSONObject.parseObject(retStr_bidReg);
            JSONObject jsonObj_guarnat = JSONObject.parseObject(retStr_guarnat);
            if (ResultCode.SCCESS.equals(jsonObj_bidReg.get("resultCode")))
            {
                logger.info("第三方项目登记成功！");
                if (ResultCode.SCCESS.equals(jsonObj_guarnat.get("resultCode")))
                {
                    logger.info("第三方项目担保机构登记成功！");
                }
                else
                {
                    throw new LogicalException("第三方项目担保机构登记失败~" + jsonObj_guarnat.get("resultMsg"));
                }
            }
            else
            {
                throw new LogicalException("第三方项目登记失败~" + jsonObj_bidReg.get("resultMsg"));
            }*/
        }

        //标的审核通过
        bidManage.through(loanId);

        sendRedirect(request, response, getController().getURI(request, LoanList.class));
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        if (throwable instanceof LogicalException || throwable instanceof ParameterException)
        {
            getController().prompt(request, response, PromptLevel.WARRING, throwable.getMessage());
            sendRedirect(request,
                response,
                getController().getURI(request, DetailAnnexWz.class) + "?loanId=" + request.getParameter("loanId")
                    + "&userId=" + request.getParameter("userId") + "&operationJK=SH");
        }
        else
        {
            super.onThrowable(request, response, throwable);
        }
    }
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }

}
