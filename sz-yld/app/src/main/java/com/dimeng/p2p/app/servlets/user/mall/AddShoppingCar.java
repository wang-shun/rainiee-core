/*
 * 文 件 名:  AddShoppingCar.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  yuanguangjie
 * 修改时间:  2016年2月26日
 */
package com.dimeng.p2p.app.servlets.user.mall;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.account.front.service.UserInfoManage;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.repeater.score.MallChangeManage;

/**
 * 积分商城-添加到购物车
 * 
 * @author  yuanguangjie
 * @version  [版本号, 2016年2月26日]
 */
public class AddShoppingCar extends AbstractSecureServlet
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 647069847939919002L;
    
    @Override
    protected void processGet(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        // 判断用户是否被拉黑或者锁定
        UserInfoManage userInfoMananage = serviceSession.getService(UserInfoManage.class);
        T6110 t6110 = userInfoMananage.getUserInfo(serviceSession.getSession().getAccountId());
        
        // 用户状态非法
//        if (t6110.F07 == T6110_F07.HMD)
//        {
//            throw new LogicalException("账号异常,请联系客服！");
//        }
        
	    MallChangeManage manage = serviceSession.getService(MallChangeManage.class);
        //获取商品Id 
	    int id = Integer.parseInt(getParameter(request, "id"));
        //获取商品数量
	    int num = Integer.parseInt(getParameter(request, "num"));
        //编辑购物车信息
        manage.editCar(id, num);
        //发送数据
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "添加成功！");
        return;
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        ResourceProvider resourceProvider = getResourceProvider();
        resourceProvider.log(throwable);
        setReturnMsg(request, response, ExceptionCode.ADD_SHOPPING_ERROR, throwable.getMessage());
        return;
        
    }
    
}
