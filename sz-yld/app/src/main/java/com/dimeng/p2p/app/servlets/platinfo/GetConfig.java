/*
 * 文 件 名:  GetConfig.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2016年4月6日
 */
package com.dimeng.p2p.app.servlets.platinfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.resource.ResourceRegister;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.app.servlets.AbstractAppServlet;
import com.dimeng.util.StringHelper;

/**
 * 获取配置项信息
 * 
 * @author  zhoulantao
 * @version  [版本号, 2016年4月6日]
 */
public class GetConfig extends AbstractAppServlet
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -8266600198558794950L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        // 配置项名称
        final String configName = getParameter(request, "configName");
        
        // 根据配置项名称获取配置项的值
        final ConfigureProvider configureProvider =
            ResourceRegister.getResourceProvider(getServletContext()).getResource(ConfigureProvider.class);
        String configValue = configureProvider.format(configName);
        configValue = StringHelper.isEmpty(configValue) ? "" : configValue;
        
        // 封装返回信息
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success", configValue);
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
