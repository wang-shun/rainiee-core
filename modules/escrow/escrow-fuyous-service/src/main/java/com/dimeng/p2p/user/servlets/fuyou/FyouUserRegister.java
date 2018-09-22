package com.dimeng.p2p.user.servlets.fuyou;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.session.authentication.AuthenticationException;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.AbstractFuyouServlet;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.escrow.fuyou.cond.UserRegisterCond;
import com.dimeng.p2p.escrow.fuyou.entity.UserRegisterEntity;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouTypeEnum;
import com.dimeng.p2p.escrow.fuyou.service.UserManage;
import com.dimeng.p2p.escrow.fuyou.util.MchntTxnSsn;
import com.dimeng.p2p.escrow.fuyou.variables.FuyouVariable;
import com.dimeng.util.StringHelper;

/**
 * 
 * 富友托管注册
 * <第三方托管注册>
 * 
 * @author  heshiping
 * @version  [版本号, 2015年11月10日]
 */
public class FyouUserRegister extends AbstractFuyouServlet
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -4656239724729249471L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        logger.info("富友托管注册开始——IP:" + request.getRemoteAddr());
        final String mobile = request.getParameter("mobile");
        final String identificationNo = request.getParameter("identificationNo");
        final String realName = request.getParameter("realName");
        
        UserManage userManage = serviceSession.getService(UserManage.class);
        //判断是否绑定邮箱和账户
        /*       
        if (!userManage.isBandPhoneAndEmail())
        {
         getController().prompt(request, response, PromptLevel.ERROR, "请先绑定手机号和邮箱!");
         if (!userManage.isZrr())
         {
             sendRedirect(request, response, getConfigureProvider().format(URLVariable.COM_FZRR));
         }
         else
         {
             sendRedirect(request, response, getConfigureProvider().format(URLVariable.USER_ZRR_NCIIC));
         }
         return;
         }*/
        
        if (T6110_F06.ZRR == userManage.getUserType())
        {
            if (StringHelper.isEmpty(identificationNo))
            {
                getController().prompt(request, response, PromptLevel.WARRING, "请先填写身份证信息！");
                return;
            }
            if (StringHelper.isEmpty(realName))
            {
                getController().prompt(request, response, PromptLevel.WARRING, "请先填写姓名！");
                return;
            }
        }
        
        if (StringHelper.isEmpty(mobile))
        {
            getController().prompt(request, response, PromptLevel.WARRING, "请先填写手机号！");
            return;
        }
        
        // 判断法人与自然人
        if (!userManage.isZrr())
        {
            // 法人注册
            forwardController(request, response, FyouComRegister.class);
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
                return trimBlank(getConfigureProvider().format(FuyouVariable.FUYOU_REGISTER));
            }
            
            @Override
            public String mobileNo()
            {
                return trimBlank(mobile);
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
                return trimBlank(realName);
            }
            
            @Override
            public String cityId()
            {
                return trimBlank(userEntity.getCity_id());
            }
            
            @Override
            public String certifId()
            {
                return trimBlank(identificationNo);
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
                return "";
            }
        });
        // 注册提交地址
        String formUrl = getConfigureProvider().format(FuyouVariable.FUYOU_USREG_URL);
        // 向第三方发送请求
        sendHttp(userRegisterMap, formUrl, response, true);
        logger.info("富友托管注册成功发送数据...");
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        getResourceProvider().log(throwable);
        String retUrl = request.getHeader("Referer");
        if (throwable instanceof ParameterException || throwable instanceof SQLException)
        {
            getController().prompt(request, response, PromptLevel.ERROR, "系统繁忙，请您稍后再试!");
            sendRedirect(request, response, retUrl);
        }
        else if (throwable instanceof LogicalException)
        {
            getController().prompt(request, response, PromptLevel.WARRING, throwable.getMessage());
            sendRedirect(request, response, retUrl);
        }
        else if (throwable instanceof AuthenticationException)
        {
            sendRedirect(request, response, retUrl);
        }
        else
        {
            super.onThrowable(request, response, throwable);
        }
    }
}
