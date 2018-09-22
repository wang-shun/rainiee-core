/*
 * 文 件 名:  ZqzrAgreementView.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2016年1月6日
 */
package com.dimeng.p2p.app.servlets.bid.publics;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.config.Envionment;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S62.enums.T6231_F21;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.modules.bid.user.service.DzxyManage;
import com.dimeng.p2p.modules.bid.user.service.entity.Dzxy;
import com.dimeng.p2p.modules.bid.user.service.entity.DzxyZqzr;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.Formater;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.TimestampParser;

/**
 * 查看债权转让合同
 * 
 * @author  zhoulantao
 * @version  [版本号, 2016年1月6日]
 */
public class ZqzrAgreementView extends AbstractSecureServlet
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 4606025094486425526L;

    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        // 债权ID
        final int zqzrjlId = IntegerParser.parse(getParameter(request, "id"));
        
        DzxyManage manage = serviceSession.getService(DzxyManage.class);
        Dzxy dzxy = manage.getZqzr(zqzrjlId);
        if (dzxy == null) {
            setReturnMsg(request, response, ExceptionCode.UNKNOWN_ERROR, "获取合同失败!");
            return;
        }
        
        DzxyZqzr dzxyZqzr = manage.getDzxyZqzr(zqzrjlId);
        final ConfigureProvider config = getConfigureProvider();
        final Envionment envionment = config.createEnvionment();

        Map<String, Object> valueMap = new HashMap<String, Object>() {

            private static final long serialVersionUID = 1L;

            @Override
            public Object get(Object key) {
                Object object = super.get(key);
                if (object == null) {
                    return envionment == null ? null : envionment.get(key.toString());
                }
                return object;
            }
        };
        valueMap.put("xy_no", dzxyZqzr.xy_no);
        valueMap.put("jf_zsxm", dzxyZqzr.t6141.F02 == null ? "" : dzxyZqzr.t6141.F02);
        valueMap.put("jf_sfzh", dzxyZqzr.t6141.F07 == null ? "" : dzxyZqzr.t6141.F07);
        valueMap.put("jf_yhm", dzxyZqzr.t6110.F02);

        valueMap.put("yf_zsxm", dzxyZqzr.zqzr_yf_realName == null ? "" : dzxyZqzr.zqzr_yf_realName);
        valueMap.put("yf_sfzh", dzxyZqzr.zqzr_yf_sfzh == null ? "" : dzxyZqzr.zqzr_yf_sfzh);
        valueMap.put("yf_yhm", dzxyZqzr.zqzr_yf_loginName);


        valueMap.put("bid_title", dzxyZqzr.bdxq.F03);
        valueMap.put("bid_rate", dzxyZqzr.bdxq.F06);
        valueMap.put("bid_hkfs", dzxyZqzr.bdxq.F10.getChineseName());
        if (T6231_F21.S == dzxyZqzr.t6231.F21) {
            valueMap.put("bid_yzqqx", dzxyZqzr.bdxq.F09 + "天");
        } else {
            valueMap.put("bid_yzqqx", dzxyZqzr.bdxq.F09 + "个月");
        }
        valueMap.put("bid_hkr", TimestampParser.format(dzxyZqzr.t6231.F06, "yyyy-MM-dd"));
        valueMap.put("bid_dhqs", dzxyZqzr.t6262.F10);
        valueMap.put("bid_zqs", dzxyZqzr.t6231.F02);
        if (dzxyZqzr.zqzr_bid_ychbxse.compareTo(BigDecimal.ZERO) > 0) {
            valueMap.put("bid_ychbxse", dzxyZqzr.zqzr_bid_ychbxse);
        } else {
            valueMap.put("bid_ychbxse", "");
        }

        valueMap.put("zqr_bjsr", Formater.formatAmount(dzxyZqzr.t6251.F05));
        valueMap.put("zqr_dsbx", dzxyZqzr.zqzr_zqr_dsbx);
        valueMap.put("zqr_tbsj", TimestampParser.format(dzxyZqzr.t6251.F09, "yyyy-MM-dd"));

        valueMap.put("bid_dqjz", Formater.formatAmount(dzxyZqzr.t6262.F04));

        valueMap.put("zqrz_zrjg", Formater.formatAmount(dzxyZqzr.t6260.F03));
        valueMap.put("zqrz_rate", Formater.formatAmount(dzxyZqzr.t6260.F08.multiply(dzxyZqzr.t6260.F03)));
        valueMap.put("zqrz_zrsj", TimestampParser.format(dzxyZqzr.t6262.F07, "yyyy-MM-dd"));

        valueMap.put("site_name", config.format(SystemVariable.SITE_NAME));
        valueMap.put("site_domain", config.format(SystemVariable.SITE_DOMAIN));
        valueMap.put("company_name", config.format(SystemVariable.COMPANY_NAME));
        
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
    protected void processGet(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
}
