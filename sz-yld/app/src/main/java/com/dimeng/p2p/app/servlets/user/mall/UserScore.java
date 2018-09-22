/*
 * 文 件 名:  UserScore.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2016年1月28日
 */
package com.dimeng.p2p.app.servlets.user.mall;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S61.enums.T6106_F05;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.repeater.score.SetScoreManage;
import com.dimeng.p2p.repeater.score.UserCenterScoreManage;
import com.dimeng.p2p.variables.defines.MallVariavle;

/**
 * 用户积分(含签到状态)
 * 
 * @author  zhoulantao
 * @version  [版本号, 2016年1月28日]
 */
public class UserScore extends AbstractSecureServlet
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 118251632395494743L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        boolean isSigned = false;
        ConfigureProvider configureProvider = getConfigureProvider();
        int giveScore = 0;
        boolean is_mall = Boolean.parseBoolean(configureProvider.getProperty(MallVariavle.IS_MALL));
        if (is_mall)
        {
            UserCenterScoreManage userCenterScoreManage = serviceSession.getService(UserCenterScoreManage.class);
            isSigned = userCenterScoreManage.updateUserSigned();
            if (isSigned)
            {
                //签到赠送积分
                SetScoreManage setScoreManage = serviceSession.getService(SetScoreManage.class);
                giveScore = setScoreManage.giveScore(null, T6106_F05.sign, null);
            }
        }
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        // 签到状态
        jsonMap.put("isSigned", isSigned);
        
        // 获得的积分
        jsonMap.put("giveScore", giveScore);
        
        // 用户剩余积分
        UserCenterScoreManage userCenterScore = serviceSession.getService(UserCenterScoreManage.class);
        int usableScore = userCenterScore.getUsableScore();
        jsonMap.put("usableScore", usableScore);

        // 封装返回信息
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success！", jsonMap);
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
