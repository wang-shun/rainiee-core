/*
 * 文 件 名:  AgreementView.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2015年12月4日
 */
package com.dimeng.p2p.app.servlets.bid.publics;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.config.Envionment;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S62.enums.T6230_F11;
import com.dimeng.p2p.S62.enums.T6231_F21;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.modules.bid.user.service.BidManage;
import com.dimeng.p2p.modules.bid.user.service.DzxyManage;
import com.dimeng.p2p.modules.bid.user.service.entity.Bdxq;
import com.dimeng.p2p.modules.bid.user.service.entity.Dzxy;
import com.dimeng.p2p.modules.bid.user.service.entity.DzxyDy;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.Formater;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.DateParser;
import com.dimeng.util.parser.IntegerParser;

/**
 * 查看合同
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年12月4日]
 */
public class AgreementView extends AbstractSecureServlet
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 8481288202536507595L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        // 标ID
        final int creditId = IntegerParser.parse(getParameter(request, "id"));
        
        // 获取标的详情
        BidManage bidManage = serviceSession.getService(BidManage.class);
        Bdxq bdxq = bidManage.get(creditId);
        
        //是否公益标
        String isGyb = request.getParameter("isGyb");
        
        //如果是公益标,跳到公益标页面
        if (!StringHelper.isEmpty(isGyb) && "true".equals(isGyb))
        {
            forwardController(request, response, GybAgreementView.class);
            return;
        }
        
        DzxyManage manage = serviceSession.getService(DzxyManage.class);
        Dzxy dzxy = manage.getBidContent(creditId);
        
        // 获取模板失败
        if (dzxy == null)
        {
            setReturnMsg(request, response, ExceptionCode.UNKNOWN_ERROR, "获取合同失败!");
            return;
        }
        
        Map<String, Object> xydbValue = null;
        // 判断是否是担保标
        if (T6230_F11.S.equals(bdxq.F11.name()))
        {
        	final DzxyDy dzxyDy = manage.getDzxyDy(creditId);
        	
            // 担保标合同
            xydbValue = getXydbValue(serviceSession, dzxyDy);
        }
        else
        {
        	final DzxyDy dzxyDy = manage.getDzxyXy(creditId);
        	
            // 非担保标合同
            xydbValue = getXybValue(serviceSession, dzxyDy);
        }
        
        // 封装模板内容
        final String appTemplate = getTemplate(dzxy.xymc, dzxy.content, xydbValue);
        
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
    protected void processGet(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    /**
     * 获取担保合同参数
     * 
     * @param serviceSession 消息上下文
     * @param dzxyDy 
     * @return
     * @throws IOException
     */
    private Map<String, Object> getXydbValue(final ServiceSession serviceSession, final DzxyDy dzxyDy)
        throws IOException
    {
        final ConfigureProvider config = getConfigureProvider();
        final Envionment envionment = config.createEnvionment();
        
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
        
        valueMap.put("xy_no", dzxyDy.xy_no);
        valueMap.put("jklist", dzxyDy.cjrList);
        
        valueMap.put("yf_loginName", dzxyDy.yf_loginName);
        
        if (null != dzxyDy.t6110)
        {
            if (dzxyDy.t6110.F06.equals(T6110_F06.FZRR))
            {
                valueMap.put("user_info", T6110_F06.FZRR);
                valueMap.put("yf_cardx", dzxyDy.t6161.F03);
                valueMap.put("yf_realName", dzxyDy.t6161.F04);
            }
            if (dzxyDy.t6110.F06.equals(T6110_F06.ZRR) && null != dzxyDy.t6141)
            {
                valueMap.put("user_info", T6110_F06.ZRR);
                valueMap.put("yf_cardx", dzxyDy.t6141.F06);
                valueMap.put("yf_card", dzxyDy.t6141.F07);
                valueMap.put("yf_realName", dzxyDy.t6141.F02);
            }
        }
        
        valueMap.put("bf_companyName", dzxyDy.bf_companyName == null ? "" : dzxyDy.bf_companyName);
        valueMap.put("bf_address", dzxyDy.bf_address == null ? "" : dzxyDy.bf_address);
        
        valueMap.put("df_companyName", dzxyDy.df_companyName == null ? "" : dzxyDy.df_companyName);
        valueMap.put("df_address", dzxyDy.df_address == null ? "" : dzxyDy.df_address);
        
        valueMap.put("yq_rate", dzxyDy.yq_rate);
        valueMap.put("wyj_rate", dzxyDy.wyj_rate);
        
        valueMap.put("jk_jkyt", dzxyDy.t6231.F08 == null ? "" : dzxyDy.t6231.F08);
        valueMap.put("jk_money_xx", dzxyDy.bdxq.F05.subtract(dzxyDy.bdxq.F07));
        valueMap.put("jk_money_dx", dzxyDy.jk_money_dx);
        valueMap.put("jk_rate", Formater.formatRate(dzxyDy.bdxq.F06));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dzxyDy.t6231.F12);
        if (T6231_F21.F == dzxyDy.t6231.F21)
        {
            valueMap.put("jk_jkqx", dzxyDy.bdxq.F09 + "个月");
            calendar.add(Calendar.MONTH, dzxyDy.bdxq.F09);
            valueMap.put("jk_dqr", DateParser.format(calendar.getTime(), "yyyy-MM-dd"));
        }
        else
        {
            valueMap.put("jk_jkqx", dzxyDy.t6231.F22 + "天");
            calendar.add(Calendar.DAY_OF_MONTH, dzxyDy.t6231.F22);
            valueMap.put("jk_dqr", DateParser.format(calendar.getTime(), "yyyy-MM-dd"));
        }
        
        valueMap.put("jk_hkqs", dzxyDy.t6231.F02);
        valueMap.put("jk_hkr", dzxyDy.jk_hkr);
        valueMap.put("jk_ksr", DateParser.format(dzxyDy.t6231.F12, "yyyy-MM-dd"));
        
        valueMap.put("hklist", dzxyDy.hkjh);
        
        valueMap.put("site_name", config.format(SystemVariable.SITE_NAME));
        valueMap.put("site_domain", config.format(SystemVariable.SITE_DOMAIN));
        
        return valueMap;
    }
    
    /**
     * 获取非担保合同参数
     * 
     * @param serviceSession
     * @param dzxyDy
     * @return
     * @throws IOException
     */
    private Map<String, Object> getXybValue(final ServiceSession serviceSession, final DzxyDy dzxyDy)
        throws IOException
    {
        final ConfigureProvider config = getConfigureProvider();
        final Envionment envionment = config.createEnvionment();
        
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
        valueMap.put("xy_no", dzxyDy.xy_no);
        valueMap.put("jklist", dzxyDy.cjrList);
        
        valueMap.put("yf_loginName", dzxyDy.yf_loginName);
        if (null != dzxyDy.t6110)
        {
            if (dzxyDy.t6110.F06.equals(T6110_F06.FZRR))
            {
                valueMap.put("user_info", T6110_F06.FZRR);
                valueMap.put("yf_cardx",dzxyDy.t6161.F20.equals("Y") ? dzxyDy.t6161.F19 : dzxyDy.t6161.F03);
                valueMap.put("yf_realName", dzxyDy.t6161.F04);
            }
            if (dzxyDy.t6110.F06.equals(T6110_F06.ZRR) && null != dzxyDy.t6141)
            {
                valueMap.put("user_info", T6110_F06.ZRR);
                valueMap.put("yf_cardx", dzxyDy.t6141.F06);
                valueMap.put("yf_card", dzxyDy.t6141.F07);
                valueMap.put("yf_realName", dzxyDy.t6141.F02);
            }
        }
        
        valueMap.put("bf_companyName", dzxyDy.bf_companyName == null ? "" : dzxyDy.bf_companyName);
        valueMap.put("bf_address", dzxyDy.bf_address == null ? "" : dzxyDy.bf_address);
        
        valueMap.put("yq_rate", dzxyDy.yq_rate);
        valueMap.put("wyj_rate", dzxyDy.wyj_rate);
        
        valueMap.put("jk_jkyt", dzxyDy.t6231.F08);
        valueMap.put("jk_money_xx", dzxyDy.bdxq.F05.subtract(dzxyDy.bdxq.F07));
        valueMap.put("jk_money_dx", dzxyDy.jk_money_dx);
        valueMap.put("jk_rate", Formater.formatRate(dzxyDy.bdxq.F06));
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dzxyDy.t6231.F12);
        if (T6231_F21.F == dzxyDy.t6231.F21)
        {
            valueMap.put("jk_jkqx", dzxyDy.bdxq.F09 + "个月");
            calendar.add(Calendar.MONTH, dzxyDy.bdxq.F09);
            valueMap.put("jk_dqr", DateParser.format(calendar.getTime(), "yyyy-MM-dd"));
        }
        else
        {
            valueMap.put("jk_jkqx", dzxyDy.t6231.F22 + "天");
            calendar.add(Calendar.DAY_OF_MONTH, dzxyDy.t6231.F22);
            valueMap.put("jk_dqr", DateParser.format(calendar.getTime(), "yyyy-MM-dd"));
        }
        
        valueMap.put("jk_hkqs", dzxyDy.t6231.F02);
        valueMap.put("jk_hkr", dzxyDy.jk_hkr);
        valueMap.put("jk_ksr", DateParser.format(dzxyDy.t6231.F12, "yyyy-MM-dd"));
        
        valueMap.put("hklist", dzxyDy.hkjh);
        valueMap.put("site_name", config.format(SystemVariable.SITE_NAME));
        valueMap.put("site_domain", config.format(SystemVariable.SITE_DOMAIN));
        
        return valueMap;
    }
    
}
