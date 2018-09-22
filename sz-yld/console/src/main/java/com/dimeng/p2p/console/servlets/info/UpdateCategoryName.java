package com.dimeng.p2p.console.servlets.info;

import java.io.PrintWriter;
import java.util.regex.Pattern;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S50.entities.T5010;
import com.dimeng.p2p.S50.enums.T5010_F04;
import com.dimeng.p2p.modules.base.console.service.ArticleManage;
import com.dimeng.util.StringHelper;

@MultipartConfig
@Right(id = "P2P_C_INFO_GYWM_MENU", name = "关于我们", moduleId = "P2P_C_INFO_GYWM", order = 0)
public class UpdateCategoryName extends AbstractInformationServlet
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        PrintWriter out = response.getWriter();
        ArticleManage manage = serviceSession.getService(ArticleManage.class);
        String name = request.getParameter("articleName");
        T5010_F04 status = T5010_F04.parse(request.getParameter("articleStatus"));
        String type = request.getParameter("type");
        Pattern pattern = Pattern.compile("[<>&]");
        if (StringHelper.isEmpty(name))
        {
            StringBuilder sb = new StringBuilder();
            sb.append("[{'num':1,'msg':'");
            sb.append("标题不能为空！" + "'}]");
            out.write(sb.toString());
            return;
        }
        
        if (pattern.matcher(name).find())
        {
            StringBuilder sb = new StringBuilder();
            sb.append("[{'num':1,'msg':'");
            sb.append("标题中存在非法字符，请重新输入！" + "'}]");
            out.write(sb.toString());
            return;
        }
        
        if (StringHelper.isEmpty(type))
        {
            StringBuilder sb = new StringBuilder();
            sb.append("[{'num':1,'msg':'");
            sb.append("文章类型不能为空！" + "'}]");
            out.write(sb.toString());
            return;
        }
        try
        {
            T5010 t5010 = new T5010();
            t5010.F02 = type;
            t5010.F03 = name;
            t5010.F04 = status;
            manage.updateT5010(t5010);
        }
        catch (ParameterException e)
        {
            logger.error(e, e);
            StringBuilder sb = new StringBuilder();
            sb.append("[{'num':1,'msg':'");
            sb.append(e.getMessage() + "'}]");
            out.write(sb.toString());
            return;
        }
        out.write("[{'num':0,'msg':'设置成功！'}]");
        out.close();
        
    }
    
}
