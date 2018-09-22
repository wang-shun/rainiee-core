/*
 * 文 件 名:  AutoBidCfg.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2015年12月22日
 */
package com.dimeng.p2p.app.servlets.platinfo;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.resource.ResourceRegister;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.app.servlets.AbstractAppServlet;
import com.dimeng.p2p.variables.defines.SystemVariable;

/**
 * 理财计算器配置
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年12月22日]
 */
public class AutoBidCfg extends AbstractAppServlet
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -4142374599206193806L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        final ConfigureProvider configureProvider =
            ResourceRegister.getResourceProvider(getServletContext()).getResource(ConfigureProvider.class);
        
        // 用户自动投资设置: 投资金额的倍数
        final String multAmount = configureProvider.format(SystemVariable.AUTO_BIDING_MULT_AMOUNT);
        
        // 用户自动投资设置: 最低投资金额
        final String minAmount = configureProvider.format(SystemVariable.AUTO_BIDING_MIN_AMOUNT);
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("multAmount", multAmount);
        map.put("minAmount", minAmount);    
        
        // 封装返回信息
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success", map);
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
