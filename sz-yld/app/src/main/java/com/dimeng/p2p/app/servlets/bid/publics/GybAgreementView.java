/*
 * 文 件 名:  GybAgreementView.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2015年12月8日
 */
package com.dimeng.p2p.app.servlets.bid.publics;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.config.Envionment;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.repeater.donation.GyLoanManage;
import com.dimeng.p2p.common.entities.Dzxy;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.parser.IntegerParser;

/**
 * 公益标模板
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年12月8日]
 */
public class GybAgreementView extends AbstractSecureServlet
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -705863803101628668L;
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        // 获取公益标ID
        final int creditId = IntegerParser.parse(getParameter(request, "id"));
        
        GyLoanManage manage = serviceSession.getService(GyLoanManage.class);
        
        //公益标协议模板内容
        Dzxy dzxy = manage.getBidContent(creditId);
        
        //标的进展信息
        final ConfigureProvider configureProvider = getConfigureProvider();
        final Envionment envionment = configureProvider.createEnvionment();
        
        Map<String, Object> valueMap = new HashMap<String, Object>()
        {
            private static final long serialVersionUID = 1L;
            
            @Override
            public Object get(Object key)
            {
                Object object = super.get(key);
                if (object == null)
                {
                    return envionment == null ? null : envionment.get(key.toString());
                }
                return object;
            }
        };
        valueMap.put("site_name", configureProvider.format(SystemVariable.SITE_NAME));
        valueMap.put("site_domain", configureProvider.format(SystemVariable.SITE_DOMAIN));
        
        // 封装模板内容
        final String appTemplate = getTemplate(dzxy.xymc, dzxy.content, valueMap);
        
        if (ExceptionCode.UNKNOWN_ERROR.equals(appTemplate))
        {
            // 获取合同失败
            setReturnMsg(request, response, ExceptionCode.UNKNOWN_ERROR, "获取合同失败!");
            return;
        }
        
        // 替换模板中的值
        String appTemplateHtml = appTemplate.toString().replace("\t", "").replace("<br/>", "").replace("&nbsp;", "");
        Map<String, String> map = new HashMap<String, String>();
        map.put("agreementView", appTemplateHtml);
        
        // 返回合同内容
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success!", map);
        return;
    }
    
    @Override
    protected void processGet(final HttpServletRequest request, HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
}
