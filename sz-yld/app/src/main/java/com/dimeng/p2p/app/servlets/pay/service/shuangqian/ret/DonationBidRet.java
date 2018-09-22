/**
 * 文 件 名:  DonationBidRet.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2016年1月12日
 *//*
package com.dimeng.p2p.app.servlets.pay.service.shuangqian.ret;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bouncycastle.util.encoders.Base64;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.escrow.shuangqian.entity.BidEntity;
import com.dimeng.p2p.escrow.shuangqian.service.BidManage;

*//**
 * 公益标捐赠页面回调接口
 * 
 * @author  zhoulantao
 * @version  [版本号, 2016年1月12日]
 *//*
public class DonationBidRet extends AbstractShuangqianServlet
{
    *//**
     * 注释内容
     *//*
    private static final long serialVersionUID = 8662262722412140334L;
    
    @SuppressWarnings("deprecation")
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        BidManage manage = serviceSession.getService(BidManage.class);
        BidEntity entity = manage.bidReturnDecoder(request);
        
        String enRetUrl = request.getParameter("retUrl");
        String url = enRetUrl + "?";
        if ("88".equals(entity.resultCode))
        {
            url += "code=000000&description=success";
        }
        else
        {
            url +=
                "code=000004&description="
                    + URLEncoder.encode(new String(Base64.encode(entity.message.getBytes("UTF-8")), "UTF-8"));
        }
        response.sendRedirect(url);
    }
}*/