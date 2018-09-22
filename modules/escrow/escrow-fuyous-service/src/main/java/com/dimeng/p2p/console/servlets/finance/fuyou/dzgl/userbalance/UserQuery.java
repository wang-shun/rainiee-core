package com.dimeng.p2p.console.servlets.finance.fuyou.dzgl.userbalance;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.console.servlets.finance.fuyou.dzgl.AbstractDzglServlet;
import com.dimeng.p2p.escrow.fuyou.entity.UserQueryResponseEntity;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouRespCode;
import com.dimeng.p2p.escrow.fuyou.face.UserinfoQuery;
import com.dimeng.p2p.escrow.fuyou.service.UserManage;
import com.dimeng.p2p.escrow.fuyou.variables.FuyouVariable;

/**
 * 
 * 用户第三方信息查询
 * <功能详细描述>
 * 
 * @author  lingyuanjie
 * @version  [版本号, 2016年06月01日]
 */
@Right(id = "P2P_C_FINANCE_USER_QUERY", moduleId = "P2P_C_FUYOU_USER_BALANCE", order = 1, name = "第三方信息查询")
public class UserQuery extends AbstractDzglServlet
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        logger.info("用户第三方信息查询——IP:" + request.getRemoteAddr());
        String thirdTag = request.getParameter("id");
        String userType = request.getParameter("userType");
        String userTag = request.getParameter("userTag");
        UserManage manage = serviceSession.getService(UserManage.class);
        try
        {
            logger.info(thirdTag + " 查询用户第三方信息开始！");
            //到富友查询用户信息
            UserinfoQuery userinfoQuery = new UserinfoQuery();
            final UserQueryResponseEntity userQueryEntity =
                userinfoQuery.userinfoQuery(manage.userChargeQuery(getConfigureProvider().format(FuyouVariable.FUYOU_ACCOUNT_ID),
                    thirdTag),
                    serviceSession,
                    getConfigureProvider().format(FuyouVariable.FUYOU_QUERYUSERINFS_URL));
            if (!FuyouRespCode.JYCG.getRespCode().equals(userQueryEntity.getResp_code()))
            {
                logger.info(thirdTag + " 查询用户第三方信息失败！");
                throw new LogicalException("查询用户第三方信息失败！");
            }
            
            request.setAttribute("userType", userType);//用户类型
            request.setAttribute("userTag", userTag);//T6110.F10 是否担保机构
            request.setAttribute("thirdTag", thirdTag);//托管帐号
            request.setAttribute("userQueryEntity", userQueryEntity);
            forward(request, response, getController().getViewURI(request, getClass()));
        }
        catch (Exception e)
        {
            logger.info(thirdTag + " 查询用户第三方信息失败！");
            logger.error(e, e);
        }
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request,
            HttpServletResponse response, Throwable throwable)
            throws ServletException, IOException {
        if(throwable instanceof LogicalException){
            this.prompt(request, response, PromptLevel.INFO, throwable.getMessage());
            forward(request, response, getController().getViewURI(request, getClass()));
        }else{
            super.onThrowable(request, response, throwable);
        }
    }
    
}
