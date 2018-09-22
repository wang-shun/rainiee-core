package com.dimeng.p2p.app.servlets.user.mall;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S63.entities.T6355;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.app.tool.EmojiUtil;
import com.dimeng.p2p.common.enums.YesOrNo;
import com.dimeng.p2p.repeater.score.UserCenterScoreManage;
import com.dimeng.util.parser.IntegerParser;

/**
 * 
 * 添加收件地址
 * 
 * @author  zhangyoufu
 * @version  [版本号, 2016年02月25日]
 */
public class AddAddress extends AbstractSecureServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        final String name = getParameter(request, "name");//收件人
        final String phone = getParameter(request, "phone");//联系电话
        final String xian = getParameter(request, "xian");
        final String address = getParameter(request, "address");//详细地址
        final String VA = getParameter(request, "va");//邮编
        final String yesOrNo = getParameter(request, "yesOrNo");
        
        if (EmojiUtil.getInstance().isContainEmoji(address))
        {
            setReturnMsg(request, response, ExceptionCode.UNKNOWN_ERROR, "请不要输入表情符号！");
            return;
        }
        
        UserCenterScoreManage userCenterScoreManage = serviceSession.getService(UserCenterScoreManage.class);
        T6355 t6355 = new T6355();
        t6355.F03 = name;
        t6355.F06 = phone;
        t6355.F05 = address;
        t6355.F07 = VA;
        t6355.F08 = YesOrNo.parse(yesOrNo);
        t6355.F04 = IntegerParser.parse(xian);
        if (t6355.F08 == null)
        {
            t6355.F08 = YesOrNo.no;
        }
        int num = userCenterScoreManage.addAddress(t6355);
        if (num != 0)
        {
            setReturnMsg(request, response, ExceptionCode.SUCCESS, "success");
        }
        else
        {
            setReturnMsg(request, response, ExceptionCode.UNKNOWN_ERROR, "failed");
        }
        
        // 封装返回信息
        
        return;
    }
    
    @Override
    protected void processGet(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
}
