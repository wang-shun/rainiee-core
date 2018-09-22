package com.dimeng.p2p.console.servlets.menu;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.session.Session;
import com.dimeng.framework.http.session.SessionManager;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S50.entities.T5010;
import com.dimeng.p2p.common.ResourceProviderUtil;
import com.dimeng.p2p.console.servlets.AbstractConsoleServlet;
import com.dimeng.p2p.modules.base.console.service.ArticleManage;
import com.dimeng.p2p.variables.defines.SiteSwitchVariable;
import com.dimeng.util.parser.BooleanParser;
import org.codehaus.jackson.map.ObjectMapper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 功能详细描述
 *
 * @author zengzhihua
 * @version [版本号, 2016/3/3]
 */
public class Xcgl extends AbstractConsoleServlet{
    private static final long serialVersionUID = 1L;

    @Override
    protected void processPost(final HttpServletRequest request,
                               HttpServletResponse response, ServiceSession serviceSession)
            throws Throwable {
        ArticleManage xcglmanage = serviceSession.getService(ArticleManage.class);
        String code = request.getParameter("code");
        String CURRENT_SUB_CATEGORY = request.getParameter("CURRENT_SUB_CATEGORY");
        T5010[] aqbzs = xcglmanage.getArticleCategoryAll(Integer.valueOf(code));
        ResourceProvider resourceProvider = ResourceProviderUtil.getResourceProvider();
        SessionManager sessionManager = resourceProvider.getResource(SessionManager.class);
        Session dimengSession = sessionManager.getSession(request, response, true);

        //开关判断，为false，则不显示我要吐槽
        ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        boolean advice_complain_switch = BooleanParser.parse(configureProvider.getProperty(SiteSwitchVariable.ADVICE_COMPLAIN_SWITCH));

        String liStr = "";
        for (T5010 t5010 : aqbzs) {
            if (!advice_complain_switch && "YJFK".equals(t5010.F02)) {
                continue;
            }
            if (dimengSession.isAccessableResource(t5010.F07)) {
                liStr += "<li><a class=\"click-link";
                if (t5010.F02.equals(CURRENT_SUB_CATEGORY)) {
                    liStr += " select-a ";
                }
                liStr += "\" href=\"" + t5010.F06 + "\" target=\"mainFrame\" id=\"" + t5010.F02 + "\">" + t5010.F03 + "</a></li>";
            } else {
                liStr += "<li><a href=\"javascript:void(0)\" class=\"disabled\">" + t5010.F03 + "</a></li>";
            }
        }

        PrintWriter out = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("liStr", liStr);
        out.print(objectMapper.writeValueAsString(jsonMap));
        out.close();
    }
}