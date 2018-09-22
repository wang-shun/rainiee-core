package com.dimeng.p2p.pay.servlets.fuyou.ret;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.AbstractFuyouServlet;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouRespCode;
import com.dimeng.p2p.escrow.fuyou.service.BankManage;
import com.dimeng.p2p.variables.defines.URLVariable;
import com.google.gson.Gson;

/**
 * 
 * 申请更换银行卡返回
 * <功能详细描述>
 * 
 * @author  heshiping
 * @version  [版本号, 2016年1月20日]
 */
public class ChangeCardRet extends AbstractFuyouServlet
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        logger.info("修改银行卡申请返回");
        Gson gson = new Gson();
        String jsonData = gson.toJson(reversRequest(request));
        logger.info("更换银行卡申请：" + jsonData);
        if (jsonData.length() < 5)
        {
            String noticeMessage = "欢迎光临！";
            createNoticeMessagePage(getConfigureProvider().format(URLVariable.INDEX), noticeMessage, response);
            return;
        }
        @SuppressWarnings("unchecked")
        Map<String, String> params = gson.fromJson(jsonData, Map.class);
        
        BankManage bankManage = serviceSession.getService(BankManage.class);
        String noticeMessage = null;
        if (bankManage.bankRetDecode(params) && FuyouRespCode.JYCG.getRespCode().equals(params.get("resp_code")))
        {
            bankManage.updateT6114Ext(params);
                noticeMessage = "成功提交申请！";
        }
        else
        {
            noticeMessage = "验签失败！";
        }
        getController().prompt(request, response, PromptLevel.INFO, noticeMessage);
        sendRedirect(request, response, getConfigureProvider().format(URLVariable.CARD_MANAGE));
    }
    
}
