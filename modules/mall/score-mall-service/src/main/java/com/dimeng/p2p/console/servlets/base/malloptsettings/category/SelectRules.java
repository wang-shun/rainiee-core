/*
 * 文 件 名:  SelectRules
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  添加商品——根据商品类型查询活动规则
 * 修 改 人:  heluzhu
 * 修改时间: 2016/8/24
 */
package com.dimeng.p2p.console.servlets.base.malloptsettings.category;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.console.servlets.AbstractMallServlet;
import com.dimeng.p2p.repeater.score.ScoreCommodityManage;
import com.dimeng.p2p.repeater.score.entity.ActivityRule;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;

/**
 * 添加商品——根据商品类型查询活动规则
 * <功能详细描述>
 *
 * @author heluzhu
 * @version [版本号, 2016/8/24]
 */
public class SelectRules extends AbstractMallServlet {

    private static final long serialVersionUID = 4978908920522926215L;

    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession) throws Throwable {
        String jlType = request.getParameter("jlType");
        String hdType = request.getParameter("hdType");
        ScoreCommodityManage manage = serviceSession.getService(ScoreCommodityManage.class);
        List<ActivityRule> list = manage.selectRules(jlType, hdType);
        PrintWriter out = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        out.print(objectMapper.writeValueAsString(list));
        out.close();
    }
}
