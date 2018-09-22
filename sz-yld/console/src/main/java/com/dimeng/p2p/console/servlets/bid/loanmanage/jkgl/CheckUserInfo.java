package com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.S51.enums.T5123_F03;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.entities.T6119;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.S61.enums.T6110_F10;
import com.dimeng.p2p.S61.enums.T6120_F05;
import com.dimeng.p2p.console.servlets.bid.AbstractBidServlet;
import com.dimeng.p2p.modules.account.console.service.EnterpriseManage;
import com.dimeng.p2p.modules.account.console.service.GrManage;
import com.dimeng.p2p.modules.account.console.service.UserManage;
import com.dimeng.p2p.modules.account.console.service.entity.AT6161;
import com.dimeng.p2p.modules.account.console.service.entity.Grxx;
import com.dimeng.p2p.modules.account.console.service.entity.Rzxx;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.p2p.variables.defines.pays.PayVariavle;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BooleanParser;

public class CheckUserInfo extends AbstractBidServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        PrintWriter out = response.getWriter();
        String userName = request.getParameter("userName");
        ResourceProvider resourceProvider = getResourceProvider();
        ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        Boolean tg = BooleanParser.parseObject(configureProvider.getProperty(SystemVariable.SFZJTG));
        String prefix = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
        GrManage personalManage = serviceSession.getService(GrManage.class);
        EnterpriseManage enterpriseManage = serviceSession.getService(EnterpriseManage.class);
        StringBuilder sb = new StringBuilder();
        if (StringHelper.isEmpty(userName))
        {
            sb.append("[{'num':00,'msg':'");
            sb.append("用户名不能为空。" + "'}]");
            out.write(sb.toString());
            return;
        }
        
        UserManage userManage = serviceSession.getService(UserManage.class);
        T6110 t6110 = userManage.getFrontUserByName(userName);
        if (null == t6110)
        {
            sb.append("[{'num':00,'msg':'");
            sb.append("该用户名不存在，请重新输入。" + "'}]");
            out.write(sb.toString());
            return;
        }
        
        int userId = t6110.F01;
        if (T6110_F07.SD.name().equals(t6110.F07.name()))
        {
            sb.append("[{'num':00,'msg':'");
            sb.append("该用户名被锁定，请重新输入。" + "'}]");
            out.write(sb.toString());
            return;
        }
        
        if (T6110_F07.HMD.name().equals(t6110.F07.name()))
        {
            sb.append("[{'num':00,'msg':'");
            sb.append("该用户名被拉黑，请重新输入。" + "'}]");
            out.write(sb.toString());
            return;
        }
        if (tg && !userManage.isTg(userId))
        {
            sb.append("[{'num':00,'msg':'");
            sb.append("该用户没有注册第三方托管账号。" + "'}]");
            out.write(sb.toString());
            return;
        }
        if ("shuangqian".equals(prefix))
        {//如为双乾托管则需校验借款人是否授权
            T6119 t6119 = userManage.selectT6119(userId);
            if (StringHelper.isEmpty(t6119.F04))
            {
                sb.append("[{'num':00,'msg':'");
                sb.append("该用户没有授权二次分配，不能发标借款。'}]");
                out.write(sb.toString());
                return;
            }
        }
        
        T6110_F06 userType = userManage.getUserType(userId);
        T6110_F10 t6110_F10 = userManage.getDb(userId);
        if (userType == T6110_F06.ZRR)
        {
            Grxx user = personalManage.getUser(userId);
            boolean email = false;
            boolean jymm = false;
            if (configureProvider.getProperty(PayVariavle.CHARGE_MUST_EMAIL).equals("true"))
            {
                email = StringHelper.isEmpty(user.email);
            }
            if (configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD).equals("true"))
            {
                jymm = StringHelper.isEmpty(user.jymm);
            }
            if (user == null || StringHelper.isEmpty(user.name) || StringHelper.isEmpty(user.userName)
                || StringHelper.isEmpty(user.phone) || email || StringHelper.isEmpty(user.sfzh) || jymm)
            {
                sb.append("[{'num':00,'msg':'");
                sb.append("该用户的个人资料未填写完整,不能进行发标操作。" + "'}]");
                out.write(sb.toString());
                return;
            }
        }
        else if (userType == T6110_F06.FZRR)
        {
            if (userType == T6110_F06.FZRR && t6110_F10 == T6110_F10.S)
            {
                throw new LogicalException("机构不允许借款。");
            }
            else
            {
                AT6161 t6161 = enterpriseManage.getEnterprise(userId);
                if (t6161 == null || StringHelper.isEmpty(t6161.F02)
                    || ("N".equals(t6161.F20) && StringHelper.isEmpty(t6161.F03)) || StringHelper.isEmpty(t6161.F04)
                    || ("N".equals(t6161.F20) && StringHelper.isEmpty(t6161.F05))
                    || ("N".equals(t6161.F20) && StringHelper.isEmpty(t6161.F06))
                    || ("Y".equals(t6161.F20) && StringHelper.isEmpty(t6161.F19)) || StringHelper.isEmpty(t6161.F13))
                {
                    sb.append("[{'num':00,'msg':'");
                    sb.append("该企业的必要基础信息未填写完整,不能进行发标操作。" + "'}]");
                    out.write(sb.toString());
                    return;
                }
            }
        }
        
        Rzxx[] t6120s = personalManage.getRzxx(userId);
        StringBuffer rzMsg = new StringBuffer("");
        if (t6120s != null && t6120s.length > 0)
        {
            for (Rzxx rzxx : t6120s)
            {
                if (T5123_F03.S.name().equals(rzxx.mustRz.name()) && !T6120_F05.TG.equals(rzxx.status))
                {
                    rzMsg.append("," + rzxx.lxmc);
                }
            }
        }
        //只有自然人需要认证项
        if (userType == T6110_F06.ZRR && !StringHelper.isEmpty(rzMsg.toString()))
        {
            sb.append("[{'num':00,'msg':'");
            sb.append(rzMsg.substring(1) + "认证项没有通过,不能进行发标操作。" + "'}]");
            out.write(sb.toString());
            return;
        }
        
        sb.append("[{'num':01,'msg':'");
        sb.append("sussess" + "'}]");
        out.write(sb.toString());
    }
}