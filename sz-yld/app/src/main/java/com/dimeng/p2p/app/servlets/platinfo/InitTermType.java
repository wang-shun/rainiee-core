/*
 * 文 件 名:  InitResister.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2015年12月28日
 */
package com.dimeng.p2p.app.servlets.platinfo;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S50.entities.T5017;
import com.dimeng.p2p.S62.enums.T6230_F11;
import com.dimeng.p2p.app.servlets.AbstractAppServlet;
import com.dimeng.p2p.common.enums.TermType;
import com.dimeng.p2p.modules.base.front.service.TermManage;

/**
 * 初始化协议类型
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年12月28日]
 */
public class InitTermType extends AbstractAppServlet
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -2807064250281767362L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        // 获取需要查询的协议类型    ZC:注册|JK:借款|GYB:公益捐赠|ZQ:债权转让|GEXX:个人信息采集授权条款|QYXX:企业信息采集授权条款
        final String type = getParameter(request, "type");
        
        TermType termType = null;
        // 注册
        if ("ZC".equals(type))
        {
            termType = TermType.ZCXY;
        }
        // 借款
        else if ("JK".equals(type))
        {
            // 判断是否为担保标 
            final String isDB = getParameter(request, "isDB");
            
            // 如果是担保标的话需要读四方协议
            if (T6230_F11.S.name().equals(isDB))
            {
                termType = TermType.FFJKXY;
            }
            else
            {
                termType = TermType.TFJKXY;
            }
        }
        // 公益捐赠
        else if ("GYB".equals(type))
        {
            termType = TermType.GYJZXY;
        }
        // 债权转让
        else if ("ZQ".equals(type))
        {
            termType = TermType.ZQZRXY;
        }
        //个人信息采集授权条款
        else if ("GRXX".equals(type))
        {
        	 termType = TermType.GRXXCQSQTK;
        }
        //企业信息采集授权条款
        else if ("QYXX".equals(type))
        {
        	 termType = TermType.QYXXCJSQTK;
        }
        else
        {
            // 类型不符合，则直接返回成功。不提示错误
            setReturnMsg(request, response, ExceptionCode.UNKNOWN_ERROR, "未知的协议类型!");
            return;
        }
        
        // 获取协议信息
        TermManage termManage = serviceSession.getService(TermManage.class);
        T5017 term = termManage.get(termType);
        
        // 如果注册协议不为空，则显示注册协议的名称
        if (term != null)
        {
            // 返回注册协议的名称
            Map<String, String> params = new HashMap<String, String>();
            
            // 协议类型
            params.put("agreementType", termType.name());
            
            // 协议名称
            params.put("agreementName", term.F01.getName());
            
            setReturnMsg(request, response, ExceptionCode.SUCCESS, "success！", params);
            return;
        }
        
        setReturnMsg(request, response, ExceptionCode.UNKNOWN_ERROR, "未知的协议类型!");
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
