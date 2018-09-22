/*
 * 文 件 名:  CheckUser.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  huqinfu
 * 修改时间:  2016年6月14日
 */
package com.dimeng.p2p.user.servlets.fxbyj;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.util.parser.BooleanParser;
import org.codehaus.jackson.map.ObjectMapper;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.S61.enums.T6110_F19;
import com.dimeng.p2p.repeater.claim.SubscribeBadClaimManage;
import com.dimeng.p2p.service.UserInfoManage;
import com.dimeng.p2p.user.servlets.AbstractUserBadClaimServlet;
import com.dimeng.p2p.variables.defines.URLVariable;

/**
 * <一句话功能简述>
 * 校验用户是否有购买债权的资格
 * 
 * @author  huqinfu
 * @version  [版本号, 2016年6月14日]
 */
public class CheckUser extends AbstractUserBadClaimServlet
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        //是否进行认证
        boolean havaRZTG = true;
        StringBuilder authStr = null;
        int userId = serviceSession.getSession().getAccountId();
        ConfigureProvider configureProvider = getResourceProvider().getResource(ConfigureProvider.class);
        UserInfoManage userInfoManage = serviceSession.getService(UserInfoManage.class);
        T6110 t6110RZ = userInfoManage.getUserInfo(userId);
        T6101 balance = userInfoManage.searchFxbyj();//获取用户余额
        
        BigDecimal money = new BigDecimal(request.getParameter("money"));
        SubscribeBadClaimManage sbManage = serviceSession.getService(SubscribeBadClaimManage.class);

        boolean checkFlag=true;
        String rzUrl = "";

        //验证用户是否实名认证、手机认证等
        UserInfoManage userCommManage = serviceSession.getService(UserInfoManage.class);
        Map<String, String> retMap = userCommManage.checkAccountInfo();
        checkFlag = BooleanParser.parse(retMap.get("checkFlag"));
        if(!checkFlag) {
            rzUrl = retMap.get("rzUrl");
            authStr  = new StringBuilder(retMap.get("checkMessage"));
            havaRZTG = checkFlag;
        }
        else if (!sbManage.isNetSign(userId))
        {
            authStr = new StringBuilder("");
            havaRZTG = false;
            String netSignUrl = configureProvider.format(URLVariable.USER_NETSIGN_URL);
            authStr.append("购买不良债权必须先进行网签合同认证，请您到");
            authStr.append("<a class='blue' href='");
            authStr.append(netSignUrl);
            authStr.append("'>网签合同</a>设置！");
            rzUrl = netSignUrl;
        }
        else if (t6110RZ.F07 == T6110_F07.HMD)
        {
            authStr = new StringBuilder("");
            havaRZTG = false;
            authStr.append("用户已被拉黑，不能购买不良债权！");
            rzUrl = "javascript:closeInfo();";
        }
        else if (t6110RZ.F19 != T6110_F19.S)
        {
            authStr = new StringBuilder("");
            havaRZTG = false;
            authStr.append("您没有购买不良债权的权限！");
            rzUrl = "javascript:closeInfo();";
        }
        else if (money.compareTo(balance.F06) > 0)
        {
            authStr = new StringBuilder("");
            havaRZTG = false;
            authStr.append("风险备用金余额不足！");
            rzUrl = "javascript:closeInfo();";
        }
        
        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter out = response.getWriter();
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("authStr", authStr);
        jsonMap.put("havaRZTG", havaRZTG);
        jsonMap.put("rzUrl", rzUrl);
        out.print(objectMapper.writeValueAsString(jsonMap));
        out.close();
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        super.onThrowable(request, response, throwable);
    }
    
}
