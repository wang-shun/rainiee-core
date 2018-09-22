package com.dimeng.p2p.user.servlets.fuyou;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.AbstractFuyouServlet;
import com.dimeng.p2p.S61.entities.T6114_EXT;
import com.dimeng.p2p.escrow.fuyou.service.BankManage;
import com.dimeng.p2p.escrow.fuyou.variables.FuyouVariable;

/**
 * 
 * 更换银行卡查询
 * 
 * @author  heshiping
 * @version  [版本号, 2016年2月24日]
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
    {
        logger.info("更换银行卡查询");
        String mchnt_txn_ssn = request.getParameter("mchnt_txn_ssn");
        BankManage manage = serviceSession.getService(BankManage.class);
        T6114_EXT t6114_EXT = manage.selectT6114Ext(mchnt_txn_ssn);
        if (t6114_EXT == null)
        {
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
                        getConfigureProvider().format(FuyouVariable.FUYOU_QUERYCHANGECARD_URL),
                        true);
                break;
            
            case "DTJ":
                msg =
                    manage.queryFuyou(serviceSession,
                        t6114_EXT.F08,
                        getConfigureProvider().format(FuyouVariable.FUYOU_ACCOUNT_ID),
                        getConfigureProvider().format(FuyouVariable.FUYOU_QUERYCHANGECARD_URL),
                        false);
                
                break;
            case "SB":
                
                break;
            
            case "CG":
                
                break;
            
            default:
                break;
        }
        
        processRequest(request, response, msg);
        
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
                out.write(msg != null ? msg : "未提交申请到第三方");
            }
        }
        finally
        {
            out.close();
        }
    }
}
