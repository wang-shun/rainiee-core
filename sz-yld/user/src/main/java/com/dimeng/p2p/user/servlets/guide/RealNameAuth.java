/*
 * 文 件 名:  RealNameAuth.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoucl
 * 修改时间:  2015年11月12日
 */
package com.dimeng.p2p.user.servlets.guide;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.account.user.service.SafetyManage;
import com.dimeng.p2p.account.user.service.UserInfoManage;
import com.dimeng.p2p.account.user.service.entity.Safety;
import com.dimeng.p2p.variables.defines.pays.PayVariavle;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BooleanParser;

/**
 * <实名认证引导页> 
 * @author zhoucl
 * @version [版本号, 2015年11月12日]
 */
public class RealNameAuth extends AbstractGuideServlet {

    /**
     * 注释内容
     */
    private static final long serialVersionUID = -2052736675348427214L;

    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
            ServiceSession serviceSession) throws Throwable {
        ResourceProvider resourceProvider = getResourceProvider();
        final ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        String mRealName = configureProvider.getProperty(PayVariavle.CHARGE_MUST_NCIIC);
        SafetyManage userManage = serviceSession.getService(SafetyManage.class);
        Safety safety = userManage.get();
        UserInfoManage uManage = serviceSession.getService(UserInfoManage.class);
        T6110 t6110 = uManage.getUserInfo(serviceSession.getSession().getAccountId());
        if (BooleanParser.parse(mRealName) && StringHelper.isEmpty(safety.name) && T6110_F07.HMD != t6110.F07 && t6110.F06 == T6110_F06.ZRR) {
            sendRedirect(request, response, getController().getViewURI(request, getClass()));
        } else {
            //手机认证
            sendRedirect(request, response, serviceSession.getController().getViewURI(request,PhoneAuth.class));
        }
    }

    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response,
            ServiceSession serviceSession) throws Throwable {
        this.processPost(request, response, serviceSession);
    }

}
