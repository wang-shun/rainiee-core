/*
 * 文 件 名:  UpdateYjfk.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  God
 * 修改时间:  2016年4月5日
 */
package com.dimeng.p2p.console.servlets.info.gywm.yjfk;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S61.entities.T6195;
import com.dimeng.p2p.console.servlets.info.AbstractInformationServlet;
import com.dimeng.p2p.modules.base.console.service.TzjyManage;

/**
 * <建议反馈>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2016年4月5日]
 */
@Right(id = "P2P_C_INFO_GYWM_MENU", name = "关于我们",moduleId="P2P_C_INFO_GYWM",order=0)
public class UpdateYjfk extends AbstractInformationServlet
{

    /**
     * 注释内容
     */
    private static final long serialVersionUID = 4328545690028009166L;
    
    @Override
    protected void processGet(HttpServletRequest request,
            HttpServletResponse response, ServiceSession serviceSession)
            throws Throwable {
        processPost(request, response, serviceSession);
    }

    @Override
    protected void processPost(final HttpServletRequest request,
            HttpServletResponse response, ServiceSession serviceSession)
            throws Throwable {
        T6195 t6195 = new T6195();
        t6195.parse(request);
        String doType = request.getParameter("doType");
        TzjyManage manage = serviceSession.getService(TzjyManage.class);
        if("updateStatus".equals(doType)){
            manage.setPublishStatus(t6195);
        }else {
            t6195.F07 = serviceSession.getSession().getAccountId();
            manage.update(t6195);
        }
        sendRedirect(request, response, getController().getURI(request, SearchYjfk.class));
    }
}
