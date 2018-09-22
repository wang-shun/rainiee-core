package com.dimeng.p2p.user.servlets.fuyou;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.AbstractFuyouServlet;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.escrow.fuyou.entity.UserQueryResponseEntity;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouRespCode;
import com.dimeng.p2p.escrow.fuyou.face.UserinfoQuery;
import com.dimeng.p2p.escrow.fuyou.service.BankManage;
import com.dimeng.p2p.escrow.fuyou.variables.FuyouVariable;
import com.dimeng.util.StringHelper;

/**
 * 
 * 用户更新银行卡
 * <处理：用户注册第三方成功，但插入银行卡失败>
 * 
 * @author  heshiping
 * @version  [版本号, 2016年1月15日]
 */
public class FyouBank extends AbstractFuyouServlet
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -3908354771681954950L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        logger.info("更新银行卡");
        // 分析从对方传回来的数据
        BankManage manage = serviceSession.getService(BankManage.class);
        String usrCustId = request.getParameter("usrCustId");
        HashMap<String, String> params = manage.getUsrCustInfo();
        if (params == null)
        {
            return;
        }
        if (!usrCustId.equals(params.get("usrCustId")))
        {
            return;
        }
        if (!StringHelper.isEmpty(params.get("bank")))
        {
            return;
        }
        // 至 富友查询用户信息
        UserinfoQuery userinfoQuery = new UserinfoQuery();
        final UserQueryResponseEntity userQuery =
            userinfoQuery.userinfoQuery(manage.userChargeQuery(getConfigureProvider().format(FuyouVariable.FUYOU_ACCOUNT_ID),
                usrCustId),
                serviceSession,
                getConfigureProvider().format(FuyouVariable.FUYOU_QUERYUSERINFS_URL));
        if (!FuyouRespCode.JYCG.getRespCode().equals(userQuery.getResp_code()))
        {
            logger.info("用户信息：查询信息失败");
            throw new LogicalException("查询信息失败！");
        }
        // 姓名
        params.put("cust_nm", userQuery.getCust_nm());
        // 身份证
        params.put("certif_id", userQuery.getCertif_id());
        // 邮箱
        params.put("email", userQuery.getEmail());
        // 开户行地区代码
        params.put("city_id", userQuery.getCity_id());
        // 开户行行别
        params.put("parent_bank_id", userQuery.getParent_bank_id());
        // 开户支行名称
        params.put("bank_nm", userQuery.getBank_nm());
        // 银行卡账号
        params.put("capAcntNo", userQuery.getCapAcntNo());
        boolean flag = true;
        if (!T6110_F06.ZRR.name().equals(params.get("ZRR")))
        {
            flag = false;
        }
        if (manage.updateBank(params, flag))
        {
            processRequest(request, response, "OK");
        }
        
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response, String msg)
        throws ServletException, IOException
    {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try
        {
            response.setContentType("text/html");
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            if ("OK".equals(msg))
            {
                out.write("OK");
            }
            else
            {
                out.write(msg);
            }
        }
        finally
        {
            out.close();
        }
    }
}
