/*
 * 文 件 名:  ScoreRules.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2016年1月29日
 */
package com.dimeng.p2p.app.servlets.user.mall;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S63.entities.T6354;
import com.dimeng.p2p.app.servlets.AbstractAppServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.app.servlets.user.domain.mall.ScoreRuleInfo;
import com.dimeng.p2p.repeater.score.SetScoreManage;

/**
 * 积分规则
 * 
 * @author  zhoulantao
 * @version  [版本号, 2016年1月29日]
 */
public class ScoreRules extends AbstractAppServlet
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 6122595665488840371L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        SetScoreManage manage = serviceSession.getService(SetScoreManage.class);
        
        T6354 t6354 = manage.getT6354();
        ScoreRuleInfo info = new ScoreRuleInfo();
        if (null != t6354)
        {
            info.setScoreDesc(getImgContent(t6354.F02));
            info.setScoreRule(getImgContent(t6354.F03));
        }
        
        // 封装返回信息
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success！", info);
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
