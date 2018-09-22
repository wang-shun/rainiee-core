package com.dimeng.p2p.app.servlets.pay;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.S61.enums.T6114_F08;
import com.dimeng.p2p.account.front.service.UserInfoManage;
import com.dimeng.p2p.account.user.service.BankCardManage;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;

/**
 * 
 * 删除已绑定银行卡
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年11月12日]
 */
public class UnbindCard extends AbstractSecureServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        logger.info("|UnbindCard-in|");
        
        // 判断用户是否被拉黑或者锁定
        UserInfoManage userInfoMananage = serviceSession.getService(UserInfoManage.class);
        T6110 t6110 = userInfoMananage.getUserInfo(serviceSession.getSession().getAccountId());
        
        // 用户状态非法
        if (t6110.F07 == T6110_F07.HMD)
        {
            throw new LogicalException("账号异常,请联系客服！");
        }
        
        BankCardManage bankCardManage = serviceSession.getService(BankCardManage.class);
        String id = getParameter(request, "id");
        
        if (StringHelper.isEmpty(id))
        {
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.PARAMETER_ERROR, "参数错误！");
            return;
        }
        bankCardManage.delete(IntegerParser.parse(id), T6114_F08.TY.name());
        
        // 封装返回信息
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success！");
        return;
    }
    
}
