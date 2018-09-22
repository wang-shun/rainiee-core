/*
 * 文 件 名:  UpdateDbm.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoucl
 * 修改时间:  2016年8月18日
 */
package com.dimeng.p2p.user.servlets.account;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.repeater.guarantor.ApplyGuarantorManage;
import com.dimeng.util.StringHelper;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2016年8月18日]
 */
public class UpdateDbm extends AbstractAccountServlet
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 3561012155473748436L;
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        try
        {
            PrintWriter out = response.getWriter();
            String dbm = request.getParameter("dbm");
            StringBuilder sb = new StringBuilder();
            if (StringHelper.isEmpty(dbm))
            {
                
                sb.append("[{'num':'02','msg':'");
                sb.append("不能为空！" + "'}]");
                out.write(sb.toString());
                return;
            }
            String mtest = "^[a-zA-Z]([A-Za-z0-9]{6})$";
            dbm = dbm.trim();
            if (!dbm.matches(mtest))
            {
                sb.append("[{'num':'02','msg':'");
                sb.append("请输入合法的担保码！" + "'}]");
                out.write(sb.toString());
                return;
            }
            ApplyGuarantorManage manage = serviceSession.getService(ApplyGuarantorManage.class);
            manage.updateT6125F03(dbm);
            manage.writeLog("操作日志", "用户修改担保码成功!");
            sb.append("[{'num':'01','msg':'");
            sb.append("修改担保码成功！" + "'}]");
            out.write(sb.toString());
            
        }
        catch (Exception e)
        {
            throw e;
        }
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        try
        {
            StringBuilder sb = new StringBuilder();
            PrintWriter out = response.getWriter();
            if (throwable instanceof LogicalException || throwable instanceof ParameterException)
            {
                sb.append("[{'num':'02','msg':'");
                sb.append(throwable.getMessage());
                sb.append("'}]");
                out.write(sb.toString());
            }
            else
            {
                sb.append("[{'num':'03','msg':'");
                sb.append("系统异常，请稍后重试！" + "'}]");
                out.write(sb.toString());
            }
        }
        catch (Exception e)
        {
            logger.error("UpdateDbm.processPost() ", e);
        }
        
    }
}
