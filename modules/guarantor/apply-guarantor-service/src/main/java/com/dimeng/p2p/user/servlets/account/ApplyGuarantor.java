/*
 * 文 件 名:  ApplyGuarantor
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  heluzhu
 * 修改时间: 2016/6/13
 */
package com.dimeng.p2p.user.servlets.account;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.account.user.service.UserInfoManage;
import com.dimeng.p2p.repeater.guarantor.ApplyGuarantorManage;
import com.dimeng.p2p.user.servlets.AbstractGuarantorServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 申请担保方
 * <功能详细描述>
 *
 * @author heluzhu
 * @version [版本号, 2016/6/13]
 */
public class ApplyGuarantor extends AbstractGuarantorServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
                               ServiceSession serviceSession)
            throws Throwable
    {
        UserInfoManage userInfoManage = serviceSession.getService(UserInfoManage.class);
        ApplyGuarantorManage manage = serviceSession.getService(ApplyGuarantorManage.class);
        String result = "";
        try {
            if ("Y".equals(userInfoManage.isYuqi()))
            {
                result = "{code:1002,msg:'账户逾期借款未还，无法申请'}";
            }
            else
            {
                manage.applyGuarantor();
                result = "{code:1000,msg:'申请成功'}";
            }
        }catch (Throwable throwable){
            logger.error("申请担保方",throwable);
            if(throwable instanceof LogicalException) {
                result = "{code:1001,msg:'"+throwable.getMessage()+"'}";
            }else{
                result = "{code:1001,msg:'申请失败'}";
            }
        }
        try (PrintWriter out = response.getWriter())
        {
            out.print(result);
        }

    }
}
