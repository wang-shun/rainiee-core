package com.dimeng.p2p.app.servlets.user.mall;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.app.tool.EmojiUtil;
import com.dimeng.p2p.common.enums.YesOrNo;
import com.dimeng.p2p.repeater.score.UserCenterScoreManage;
import com.dimeng.p2p.repeater.score.query.HarvestAddressQuery;
import com.dimeng.util.parser.IntegerParser;

/**
 * 
 * 修改收件地址
 * 
 * @author  zhangyoufu
 * @version  [版本号, 2016年02月25日]
 */
public class UpdateAddress extends AbstractSecureServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        final int id = IntegerParser.parse(getParameter(request, "id"));//ID
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
        if (id <= 0)
        {
            return;
        }
        HarvestAddressQuery query = new HarvestAddressQuery()
        {
            
            @Override
            public YesOrNo getStatus()
            {
                if (yesOrNo == null || yesOrNo.equals(""))
                {
                    return YesOrNo.no;
                }
                return YesOrNo.yes;
            }
            
            @Override
            public String getPostcode()
            {
                return VA;
            }
            
            @Override
            public String getPhone()
            {
                return phone;
            }
            
            @Override
            public String getHarvestName()
            {
                return name;
            }
            
            @Override
            public String getFullAddress()
            {
                return address;
            }
            
            @Override
            public int getCity()
            {
                return IntegerParser.parse(xian);
            }
        };
        userCenterScoreManage.updateAddress(id, query);
        
        // 封装返回信息
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success");
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
