/*package com.dimeng.p2p.app.servlets.pay.service.fuyou.ret;

import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bouncycastle.util.encoders.Base64;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.app.config.Config;
import com.dimeng.p2p.app.servlets.pay.service.fuyou.AbstractFuyouServlet;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouRespCode;
import com.dimeng.p2p.escrow.fuyou.service.BankManage;
import com.google.gson.Gson;

*//**
 * 
 * 申请更换银行卡返回
 * <功能详细描述>
 * 
 * @author  suwei
 * @version  [版本号, 2016年5月5日]
 *//*
public class ChangeCardRet extends AbstractFuyouServlet
{
    *//**
     * 注释内容
     *//*
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        request.setCharacterEncoding("UTF-8");
        Gson gson = new Gson();
        String jsonData = gson.toJson(reversRequest(request));
        logger.info("APP更换银行卡申请：" + jsonData);
        
        @SuppressWarnings("unchecked")
        Map<String, String> params = gson.fromJson(jsonData, Map.class);
        
        BankManage bankManage = serviceSession.getService(BankManage.class);
        String url = getSiteDomain(Config.changeCardRetUrl);
        
        if (FuyouRespCode.JYCG.getRespCode().equals(params.get("resp_code")))
        {
            bankManage.updateT6114Ext(params);
            url += "?code=000000&description=success";
                // "成功提交申请！";
        }
        else
        {
            // "验签失败！";
            url +=
                "?code=000004&description="
                    + URLEncoder.encode(new String(Base64.encode(params.get("resp_desc").getBytes("UTF-8")), "UTF-8"));
        }
        
        // 重定向到用户中心
        sendRedirect(request, response, url);
    }
}
*/