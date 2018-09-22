/**
 * 文 件 名:  UserRegisterServlet.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2016年3月5日
 *//*
package com.dimeng.p2p.app.servlets.pay.service.shuangqian;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.entities.T6141;
import com.dimeng.p2p.S61.enums.T6198_F04;
import com.dimeng.p2p.app.config.Config;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.escrow.shuangqian.entity.UserRegisterParamEntity;
import com.dimeng.p2p.escrow.shuangqian.service.UserManage;
import com.dimeng.util.StringHelper;

*//**
 * 第三方用户注册(双乾)
 * 
 * @author  zhoulantao
 * @version  [版本号, 2016年3月5日]
 *//*
public class UserRegister extends AbstractSecureServlet
{
    *//**
     * 注释内容
     *//*
    private static final long serialVersionUID = 3569139825396199088L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        
        final String retUrl = Config.registerRetUrl;
        UserManage manage = serviceSession.getService(UserManage.class);
        final T6141 t6141 = manage.selectT6141();
        final T6110 t6110 = manage.selectT6110();
        UserRegisterParamEntity entity = new UserRegisterParamEntity();
        entity.setRealName(t6141.F02);
        entity.setIdentificationNo(StringHelper.decode(t6141.F07));
        entity.setMobile(t6110.F04);
        entity.setTerminal(T6198_F04.APP.name());
        String returnUrl = getSiteDomain("/pay/service/shuangqian/ret/userRegisterRet.htm" + "?retUrl=" + retUrl);
        entity.setReturnUrl(returnUrl);
        String form = manage.createUserRegisterForm(entity);
        logger.info("PayUserRegisterProxy rsp = " + form);
        PrintWriter writer = response.getWriter();
        if (form == "")
        {
            form = retUrl;
        }
        
        writer.print(form);
        writer.flush();
        writer.close();
    }
}*/