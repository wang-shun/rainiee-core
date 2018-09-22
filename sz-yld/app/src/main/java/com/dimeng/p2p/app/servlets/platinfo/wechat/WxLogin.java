/*
 * 文 件 名:  WxLogin.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  suwei
 * 修改时间:  2016年6月14日
 */
package com.dimeng.p2p.app.servlets.platinfo.wechat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.Controller;
import com.dimeng.framework.http.session.Session;
import com.dimeng.framework.http.session.authentication.AuthenticationException;
import com.dimeng.framework.http.session.authentication.PasswordAuthentication;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S63.enums.T6340_F03;
import com.dimeng.p2p.S63.enums.T6340_F04;
import com.dimeng.p2p.account.user.service.UserInfoManage;
import com.dimeng.p2p.account.user.service.UserThirdPartyLoginManage;
import com.dimeng.p2p.account.user.service.entity.ThirdUser;
import com.dimeng.p2p.app.servlets.AbstractAppServlet;
import com.dimeng.p2p.app.servlets.platinfo.AssociatedAccount;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.app.servlets.user.Account;
import com.dimeng.p2p.service.ActivityCommon;
import com.dimeng.p2p.variables.TerminalType;
import com.dimeng.util.StringHelper;

/**
* <微信二维码登录回调处理>
* @author  suwei
* @version  [版本号, 2016年6月14日]
*/

public class WxLogin extends AbstractAppServlet
{
    /**
    * 注释内容
    */
    private static final long serialVersionUID = 1L;
    
    @Override
    protected boolean mustAuthenticated()
    {
        return false;
    }
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        Controller controller = getController();
        PasswordAuthentication authentication = new PasswordAuthentication();
        authentication.setVerifyCodeType("LOGIN");
        authentication.setTerminal(TerminalType.wx.name());
        
        Session session = serviceSession.getSession();
        try
        {
            String openId = getParameter(request, "openId");
            // 如果openID存在
            if (!StringHelper.isEmpty(openId))
            {
                UserThirdPartyLoginManage userThirdPartyLoginManage =
                    serviceSession.getService(UserThirdPartyLoginManage.class);
                ThirdUser thirdUser = userThirdPartyLoginManage.getUserInfoByThirdID(openId);
                if (null == thirdUser)
                {
                    request.setAttribute("loginType", "weixin");
                    this.forwardController(request, response, AssociatedAccount.class);
                    return;
                }
                
                /*设置登录信息*/
                authentication.setAccountName(thirdUser.F02);
                authentication.setPassword(thirdUser.F10);
                authentication.setVerifyCode(session.getVerifyCode("LOGIN"));
                //更新登录时间
                userThirdPartyLoginManage.updateUserLoginTime(openId, "wx");
                
                UserInfoManage uManage1 = serviceSession.getService(UserInfoManage.class);
                try
                {
                    session.invalidate(request, response);
                    session.checkIn(request, response, authentication);
                    T6110 user1 = uManage1.getUserInfo(session.getAccountId());
                    //生日登录送红包和加息券
                    ActivityCommon activityCommon = serviceSession.getService(ActivityCommon.class);
                    activityCommon.sendActivity(user1.F01, T6340_F03.redpacket.name(), T6340_F04.birthday.name());
                    activityCommon.sendActivity(user1.F01, T6340_F03.interest.name(), T6340_F04.birthday.name());
                    
                    //更新累计登陆次数和最后登陆时间
                    uManage1.udpateT6198F05F07(user1.F01);
                }
                catch (AuthenticationException e)
                {
                    getResourceProvider().log(e.getMessage());
                    setReturnMsg(request, response, ExceptionCode.LOGING_FAIL_ERROR, e.getMessage());
                    return;
                }
                
                // 跳转到用户信息页面
                controller.forward(request, response, controller.getURI(request, Account.class));
            }
            else
            {
                // 如果未获取到OpenID,跳转到登录页面
                setReturnMsg(request, response, ExceptionCode.LOGING_FAIL_ERROR, "操作失败");
                return;
            }
        }
        catch (Exception e)
        {
            logger.info(e);
            setReturnMsg(request, response, ExceptionCode.LOGING_FAIL_ERROR, "操作失败");
            return;
        }
    }
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        this.processPost(request, response, serviceSession);
    }
    
}
