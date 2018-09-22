package com.dimeng.p2p.user.servlets.account;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.session.Session;
import com.dimeng.framework.http.session.authentication.AuthenticationException;
import com.dimeng.framework.http.session.authentication.VerifyCodeAuthentication;
import com.dimeng.framework.resource.ResourceRegister;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S61.enums.T6106_F05;
import com.dimeng.p2p.account.user.service.SafetyManage;
import com.dimeng.p2p.repeater.score.SetScoreManage;
import com.dimeng.p2p.variables.defines.MallVariavle;
import com.dimeng.p2p.user.servlets.guide.MailAuth;
import com.dimeng.p2p.user.servlets.guide.TradePwdAuth;
import com.dimeng.p2p.variables.defines.MessageVariable;
import com.dimeng.p2p.variables.defines.pays.PayVariavle;
import com.dimeng.util.StringHelper;

public class BindPhone extends AbstractAccountServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        PrintWriter out = response.getWriter();
        ConfigureProvider configureProvider =
                ResourceRegister.getResourceProvider(getServletContext()).getResource(ConfigureProvider.class);
        SafetyManage safetyManage = serviceSession.getService(SafetyManage.class);
        String phpne = request.getParameter("binphpne");
        String code = request.getParameter("bphoneCode");
        if (StringHelper.isEmpty(phpne))
        {
            StringBuilder sb = new StringBuilder();
            sb.append("[{'num':0,'msg':'");
            sb.append("手机号码错误" + "'}]");
            out.write(sb.toString());
            return;
            /*getController().prompt(request, response, PromptLevel.ERROR,
            		"手机号码错误");
            sendRedirect(request, response,
            		getController().getViewURI(request, Safetymsg.class));
            return;*/
        }
        else if (StringHelper.isEmpty(code))
        {
            /*getController().prompt(request, response, PromptLevel.ERROR, "验证码错误");
            sendRedirect(request, response, getController().getViewURI(request, Safetymsg.class));
            return;*/
            StringBuilder sb = new StringBuilder();
            sb.append("[{'num':0,'msg':'");
            sb.append("手机验证码错误" + "'}]");
            out.write(sb.toString());
            return;
        }
        else
        {
        	//当日该手机与验证码匹配错误次数
        	Integer ecount = safetyManage.phoneMatchVerifyCodeErrorCount(phpne, 1);
        	if (ecount>= Integer.parseInt(configureProvider.getProperty(MessageVariable.PHONE_VERIFYCODE_MAX_ERROR_COUNT))) {
        		 StringBuilder sb = new StringBuilder();
                 sb.append("[{'num':0,'msg':'");
                 sb.append("当前手机号码当天匹配验证码错误次数已达上限！" + "'}]");
                 out.write(sb.toString());
                 return;
			}
            String codeType = "bind|"+phpne;
            Session session = serviceSession.getSession();
            VerifyCodeAuthentication verfycode = new VerifyCodeAuthentication();
            verfycode.setVerifyCodeType(codeType);
            verfycode.setVerifyCode(code);
            try
            {
                session.authenticateVerifyCode(verfycode);
            }
            catch (AuthenticationException e)
            {
                /* getController().prompt(request, response, PromptLevel.ERROR, "验证码错误");
                 sendRedirect(request, response, getController().getViewURI(request, Safetymsg.class));
                 return;*/
            	//绑定手机号码，type为1，见send.java line 110
            	safetyManage.insertPhoneMatchVerifyCodeError(phpne, 1, code);
                StringBuilder sb = new StringBuilder();
                sb.append("[{'num':0,'msg':'");
                sb.append("手机验证码错误" + "'}]");
                out.write(sb.toString());
                return;
            }
            
            if (safetyManage.isPhone(phpne))
            {
                /*  getController().prompt(request, response, PromptLevel.ERROR, "手机号码已存在");
                  sendRedirect(request, response, getController().getViewURI(request, Safetymsg.class));
                  return;*/
                StringBuilder sb = new StringBuilder();
                sb.append("[{'num':0,'msg':'");
                sb.append("手机号码已存在" + "'}]");
                out.write(sb.toString());
                return;
            }
            session.invalidVerifyCode(codeType);
            safetyManage.updatePhone(phpne);
        }
        
        boolean is_mall = Boolean.parseBoolean(configureProvider.getProperty(MallVariavle.IS_MALL));
        if(is_mall){
            //手机认证赠送积分
            SetScoreManage setScoreManage = serviceSession.getService(SetScoreManage.class);
            setScoreManage.giveScore(null,T6106_F05.cellphone, null);
        }
        
		String url = serviceSession.getController().getViewURI(request, TradePwdAuth.class);
        String CHARGE_MUST_WITHDRAWPSD = configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD);
		if(!"true".equalsIgnoreCase(CHARGE_MUST_WITHDRAWPSD)){
			url = serviceSession.getController().getViewURI(request, MailAuth.class);
		}
		
        StringBuilder sb = new StringBuilder();
        sb.append("[{'num':1,'msg':'");
        sb.append("sussess','url':'" + url + "'}]");
        out.write(sb.toString());
        //sendRedirect(request, response, getController().getViewURI(request, Safetymsg.class));
    }
    
}
