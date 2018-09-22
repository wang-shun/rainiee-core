/*
 * 文 件 名:  BuyRecord.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  yuanguangjie
 * 修改时间:  2016年2月23日
 */
package com.dimeng.p2p.app.servlets.user.mall;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;



import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.account.front.service.UserInfoManage;
import com.dimeng.p2p.account.user.service.TxManage;
import com.dimeng.p2p.app.servlets.AbstractAppServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.order.MallChangeExecutor;
import com.dimeng.p2p.repeater.score.MallChangeManage;
import com.dimeng.p2p.repeater.score.entity.OrderGoods;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.p2p.variables.defines.pays.PayVariavle;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BooleanParser;

/**
 * 平台商品详情-购买记录
 * 
 * @author  yuanguangjie
 * @version  [版本号, 2016年2月25日]
 */
public class BuyGoods extends AbstractAppServlet
{
    private static final long serialVersionUID = 647069847939919002L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        // 判断用户是否被拉黑或者锁定
        UserInfoManage userInfoMananage = serviceSession.getService(UserInfoManage.class);
        T6110 t6110 = userInfoMananage.getUserInfo(serviceSession.getSession().getAccountId());
        
        // 用户状态非法
        if (t6110.F07 == T6110_F07.HMD)
        {
            throw new LogicalException("账号异常,请联系客服！");
        }
        
    	MallChangeManage manage = serviceSession.getService(MallChangeManage.class);
    	
    	//兑换方式 ：积分-金额
        final String type =  getParameter(request, "payType");
        //商品信息
        final String jsonStr = getParameter(request, "goodsList");
        JSONArray array = JSONArray.fromObject(jsonStr); 
        List<OrderGoods> orders = (List<OrderGoods>)array.toCollection(array, OrderGoods.class);
        
        //收货地址
        final String address =  getParameter(request, "addressId");
        final int addressId = StringHelper.isEmpty(address) ? 0 : Integer.parseInt(address);
        //交易密码
        final String tranPwd = getParameter(request, "tranPwd");
        final ConfigureProvider configureProvider = getResourceProvider().getResource(ConfigureProvider.class);
        //系统是否需要交易密码
        final boolean isOpenWithPsd = BooleanParser.parse(configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD));
        //是否托管
        final boolean isTg = BooleanParser.parse(configureProvider.getProperty(SystemVariable.SFZJTG));
        
        if(isOpenWithPsd)
        {
        	// 交易密码处理
            TxManage txManage = serviceSession.getService(TxManage.class);
            if (!txManage.checkWithdrawPassword(tranPwd))
            {
                // 封装返回信息
                setReturnMsg(request, response, ExceptionCode.TRANPASSWORD_ERROR, "请输入正确的交易密码！");
                return;
            }
        }
        String msg = "";
        //积分兑换
        if("score".equals(type))
        {
            manage.toChangeByScore(orders, type, addressId);
            msg = "兑换成功！";
        }
        //余额购买
        else
        {
            MallChangeExecutor executor = getResourceProvider().getResource(MallChangeExecutor.class);
            int orderId = manage.toChangeByBalance(orders, type, addressId);
            executor.submit(orderId, null);
            executor.confirm(orderId, null);
            msg = "购买成功！";
        }
        setReturnMsg(request, response, ExceptionCode.SUCCESS, msg);
        return;
    }
    
    @Override
    protected void processGet(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
}
