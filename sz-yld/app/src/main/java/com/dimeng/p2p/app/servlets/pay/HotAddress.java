package com.dimeng.p2p.app.servlets.pay;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;

/**
 * 
 * 热门开户行城市列表
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年11月12日]
 */
public class HotAddress extends AbstractSecureServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        logger.info("|HotAddress-in|");
        List<String> hotAddress = new ArrayList<String>();
        hotAddress.add("北京");
        hotAddress.add("上海");
        hotAddress.add("广州");
        hotAddress.add("深圳");
        hotAddress.add("珠海");
        hotAddress.add("佛山");
        hotAddress.add("南京");
        hotAddress.add("苏州");
        hotAddress.add("杭州");
        hotAddress.add("济南");
        hotAddress.add("青岛");
        hotAddress.add("郑州");
        hotAddress.add("石家庄");
        hotAddress.add("福州");
        hotAddress.add("厦门");
        hotAddress.add("武汉");
        hotAddress.add("长沙");
        hotAddress.add("成都");
        hotAddress.add("重庆");
        hotAddress.add("太原");
        hotAddress.add("沈阳");
        
        // 封装返回信息
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success!", hotAddress);
        return;
    }
    
}
