package com.dimeng.p2p.user.servlets.fuyou;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.AbstractFuyouServlet;
import com.dimeng.p2p.escrow.fuyou.service.PhoneManage;
import com.dimeng.p2p.escrow.fuyou.variables.FuyouVariable;

/**
 * 
 * 手机号码修改
 * <富友托管>
 * 
 * @author  heshiping
 * @version  [版本号, 2015年12月5日]
 */
public class FyouPhoneUpdate extends AbstractFuyouServlet {	
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession) throws Throwable {
        logger.info("手机号修改-IP:" + request.getRemoteAddr());
        PhoneManage phoneManage = serviceSession.getService(PhoneManage.class);
        
        Map<String, String> params = phoneManage.createPhoneUri();
        
        // 修改手机号地址
        String formUrl = getConfigureProvider().format(FuyouVariable.FUYOU_PC_400101_URL);
        // 向第三方发送请求
        sendHttp(params, formUrl, response, true);
        logger.info("富友托管修改手机号成功发送数据...");
        phoneManage.writeFrontLog("手机号修改", "前台用户修改手机号");
    }
}
