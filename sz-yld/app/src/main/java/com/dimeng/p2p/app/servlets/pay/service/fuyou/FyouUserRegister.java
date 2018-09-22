/*package com.dimeng.p2p.app.servlets.pay.service.fuyou;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bouncycastle.util.encoders.Base64;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.app.config.Config;
import com.dimeng.p2p.app.servlets.pay.service.fuyou.service.UserManage;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.escrow.fuyou.cond.UserRegisterCond;
import com.dimeng.p2p.escrow.fuyou.entity.UserRegisterEntity;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouTypeEnum;
import com.dimeng.p2p.escrow.fuyou.util.MchntTxnSsn;
import com.dimeng.p2p.escrow.fuyou.variables.FuyouVariable;
import com.dimeng.util.StringHelper;

*//**
 * 
 * 富友托管注册
 * <第三方托管注册>
 * 
 * @author  suwei
 * @version  [版本号, 2015年03月02日]
 *//*
public class FyouUserRegister extends AbstractFuyouServlet
{
    *//**
     * 注释内容
     *//*
    private static final long serialVersionUID = -4656239724729249471L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        logger.info("APP富友托管注册开始——IP:" + request.getRemoteAddr());
        UserManage userManage = serviceSession.getService(UserManage.class);
        
        //在认证页用户可以修改手机号传过来
        final String phone = getParameter(request, "ts");
        
        // 判断法人与自然人
        if (!userManage.isZrr())
        {
            // 法人注册
            //            forwardController(request, response, FyouComRegister.class);
            setReturnMsg(request, response, ExceptionCode.COM_REGISTER_ERROR, "企业或机构账号注册用户请在PC端注册第三方账号");
            return;
        }
        // 查询用户基本信息
        final UserRegisterEntity userEntity = userManage.selectUserInfo();
        
        // 创建注册第三方请求信息
        Map<String, String> userRegisterMap = userManage.createRegisterUri(new UserRegisterCond()
        {
            
            @Override
            public String userIdFrom()
            {
                return trimBlank(userEntity.getUser_id_from());
            }
            
            @Override
            public String parentBankId()
            {
                return trimBlank(userEntity.getParent_bank_id());
            }
            
            @Override
            public String pageNotifyUrl()
                throws Throwable
            {
                return getSiteDomain("/pay/service/fuyou/ret/userRegisterRet.htm");
            }
            
            @Override
            public String mobileNo()
            {
            	if(StringHelper.isEmpty(phone)){
            		return trimBlank(phone);
            	}else{
            		return trimBlank(userEntity.getMobile_no());
            	}
            }
            
            @Override
            public String mchntTxnSsn()
            {
                return MchntTxnSsn.getMts(FuyouTypeEnum.YHZC.name());
            }
            
            @Override
            public String mchntCd()
                throws Throwable
            {
                return trimBlank(getConfigureProvider().format(FuyouVariable.FUYOU_ACCOUNT_ID));
            }
            
            @Override
            public String email()
            {
                return trimBlank(userEntity.getEmail());
            }
            
            @Override
            public String custNm()
            {
                return trimBlank(userEntity.getCust_nm());
            }
            
            @Override
            public String cityId()
            {
                return trimBlank(userEntity.getCity_id());
            }
            
            @Override
            public String certifId()
            {
                return trimBlank(userEntity.getCertif_id().contains("x") ? userEntity.getCertif_id().replace("x", "X")
                    : userEntity.getCertif_id());
            }
            
            @Override
            public String capAcntNo()
            {
                return trimBlank(userEntity.getCapAcntNo());
            }
            
            @Override
            public String bankNm()
            {
                return trimBlank(userEntity.getBank_nm());
            }
            
            @Override
            public String backNotifyUrl()
                throws Throwable
            {
                return getSiteDomain("/pay/service/fuyou/ret/userRegisterRet.htm");
            }
        });
        // 注册提交地址
        //        String formUrl = "https://jzh-test.fuiou.com/jzh/app/appWebReg.action";
        String formUrl = getConfigureProvider().format(FuyouVariable.FUYOU_APP_REG_URL);
        // 向第三方发送请求
        sendHttp(userRegisterMap, formUrl, response, true);
        logger.info("富友托管注册成功发送数据...");
    }
    
    @SuppressWarnings("deprecation")
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        String enRetUrl = Config.registerRetUrl;
        String url =
            enRetUrl + "?" + "code=000004&description="
                + URLEncoder.encode(new String(Base64.encode(throwable.getMessage().getBytes("UTF-8")), "UTF-8"));
        
        response.sendRedirect(url);
        getResourceProvider().log(throwable);
    }
}
*/