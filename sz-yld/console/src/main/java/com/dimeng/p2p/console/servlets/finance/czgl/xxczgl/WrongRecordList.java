/*
 * 文 件 名:  WrongRecordList
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  heluzhu
 * 修改时间: 2016/8/20
 */
package com.dimeng.p2p.console.servlets.finance.czgl.xxczgl;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.console.servlets.spread.AbstractSpreadServlet;
import com.dimeng.p2p.modules.account.console.service.entity.OfflineChargeImportRecord;
import com.dimeng.util.StringHelper;
import net.sf.json.JSONArray;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 批量导入线下充值错误记录
 * <功能详细描述>
 *
 * @author heluzhu
 * @version [版本号, 2016/8/20]
 */
public class WrongRecordList extends AbstractSpreadServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
            throws Throwable {
        String json = request.getParameter("jsonStr");
        if (!StringHelper.isEmpty(json))
        {
            JSONArray array = JSONArray.fromObject(json);
            request.setAttribute("list", array.toCollection(array, OfflineChargeImportRecord.class));
        }
        forwardView(request, response, getClass());
    }

    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
            throws Throwable {
        processPost(request,response,serviceSession);
    }
}