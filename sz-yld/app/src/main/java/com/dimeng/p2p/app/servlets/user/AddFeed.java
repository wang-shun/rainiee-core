/*
 * 文 件 名:  AddFeed.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2016年3月23日
 */
package com.dimeng.p2p.app.servlets.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.account.user.service.TzjyManage;
import com.dimeng.p2p.account.user.service.UserInfoManage;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.app.tool.EmojiUtil;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;

/**
 * 发布反馈信息
 * 
 * @author  zhoulantao
 * @version  [版本号, 2016年3月23日]
 */
public class AddFeed extends AbstractSecureServlet
{
    private static final long serialVersionUID = 4641439595944138005L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        // 判断实名认证
        UserInfoManage mge = serviceSession.getService(UserInfoManage.class);
        if (!mge.isSmrz())
        {
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.NO_IDCARD_NAME, "您尚未进行实名认证，不可进行投诉建议!");
            return;
        }
        
        // 判断已提交的投诉建议数量
        final TzjyManage manage = serviceSession.getService(TzjyManage.class);
        final int userId = serviceSession.getSession().getAccountId();
        int count = manage.getAdvCount(userId);
        int ALLWO_ADVICE_TIMES =
            IntegerParser.parse(getConfigureProvider().getProperty(SystemVariable.ALLWO_ADVICE_TIMES));
        if (count >= ALLWO_ADVICE_TIMES)
        {
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.UNKNOWN_ERROR, "您今天反馈的次数已到达上限，请明天再试!");
            return;
        }
        
        // 保存投诉建议
        final String content = getParameter(request, "content").trim();
        
        // 对内容做非空判断，StringHelper.isEmpty已经对只包含空格的情况作了处理
        if (StringHelper.isEmpty(content))
        {
            setReturnMsg(request, response, ExceptionCode.UNKNOWN_ERROR, "内容不能为空！");
            return;
        }
        else if (EmojiUtil.getInstance().isContainEmoji(content))
        {
            setReturnMsg(request, response, ExceptionCode.UNKNOWN_ERROR, "请不要输入表情符号！");
            return;
        }
        manage.saveInfo(userId, content);
        
        // 封装返回信息
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success!");
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
