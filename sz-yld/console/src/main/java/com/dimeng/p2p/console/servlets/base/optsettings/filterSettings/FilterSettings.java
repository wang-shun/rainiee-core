package com.dimeng.p2p.console.servlets.base.optsettings.filterSettings;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S62.entities.T6299;
import com.dimeng.p2p.S62.enums.T6299_F04;
import com.dimeng.p2p.console.servlets.base.AbstractBaseServlet;
import com.dimeng.p2p.modules.base.console.service.FilterSettingsManage;
import com.dimeng.util.parser.IntegerParser;

@Right(id = "P2P_C_BASE_FILTERSETTINGS_SET", name = "筛选条件设置", moduleId = "P2P_C_BASE_OPTSETTINGS_FILTERSETTINGS")
public class FilterSettings extends AbstractBaseServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        FilterSettingsManage filterSettings = serviceSession.getService(FilterSettingsManage.class);
        T6299 NHSYFirst = filterSettings.getFirst(T6299_F04.NHSY);
        T6299 NHSYLast = filterSettings.getLast(T6299_F04.NHSY);
        T6299[] NHSYFilters = filterSettings.getAddFilter(T6299_F04.NHSY);
        T6299 RZJDFirst = filterSettings.getFirst(T6299_F04.RZJD);
        T6299 RZJDLast = filterSettings.getLast(T6299_F04.RZJD);
        T6299[] RZJDFilters = filterSettings.getAddFilter(T6299_F04.RZJD);
        T6299 JKQXFirst = filterSettings.getFirst(T6299_F04.JKQX);
        T6299 JKQXLast = filterSettings.getLast(T6299_F04.JKQX);
        T6299[] JKQXFilters = filterSettings.getAddFilter(T6299_F04.JKQX);
        request.setAttribute("NHSYFirst", NHSYFirst);
        request.setAttribute("NHSYLast", NHSYLast);
        request.setAttribute("NHSYFilters", NHSYFilters);
        request.setAttribute("RZJDFirst", RZJDFirst);
        request.setAttribute("RZJDLast", RZJDLast);
        request.setAttribute("RZJDFilters", RZJDFilters);
        request.setAttribute("JKQXFirst", JKQXFirst);
        request.setAttribute("JKQXLast", JKQXLast);
        request.setAttribute("JKQXFilters", JKQXFilters);
        forward(request, response, getController().getViewURI(request, FilterSettings.class));
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        FilterSettingsManage filterSettings = serviceSession.getService(FilterSettingsManage.class);
        String[] NHSYids = request.getParameterValues("NHSYids");
        String[] NHSYF02 = request.getParameterValues("NHSYF02");
        String[] NHSYF03 = request.getParameterValues("NHSYF03");
        String[] RZJDids = request.getParameterValues("RZJDids");
        String[] RZJDF02 = request.getParameterValues("RZJDF02");
        String[] RZJDF03 = request.getParameterValues("RZJDF03");
        String[] JKQXids = request.getParameterValues("JKQXids");
        String[] JKQXF02 = request.getParameterValues("JKQXF02");
        String[] JKQXF03 = request.getParameterValues("JKQXF03");
        String[] addNHSYF02 = request.getParameterValues("addNHSYF02");
        String[] addNHSYF03 = request.getParameterValues("addNHSYF03");
        String[] addRZJDF02 = request.getParameterValues("addRZJDF02");
        String[] addRZJDF03 = request.getParameterValues("addRZJDF03");
        String[] addJKQXF02 = request.getParameterValues("addJKQXF02");
        String[] addJKQXF03 = request.getParameterValues("addJKQXF03");
        List<T6299> updateList = new ArrayList<>();
        List<T6299> addList = new ArrayList<>();
        //更新年化利率
        for (int i = 0; i < NHSYids.length; i++)
        {
            T6299 NHSYt6299 = new T6299();
            NHSYt6299.F01 = IntegerParser.parse(NHSYids[i]);
            NHSYt6299.F02 = IntegerParser.parse(NHSYF02[i]);
            NHSYt6299.F03 = IntegerParser.parse(NHSYF03[i]);
            updateList.add(NHSYt6299);
        }
        //更新融资进度
        for (int i = 0; i < RZJDids.length; i++)
        {
            T6299 RZJDt6299 = new T6299();
            RZJDt6299.F01 = IntegerParser.parse(RZJDids[i]);
            RZJDt6299.F02 = IntegerParser.parse(RZJDF02[i]);
            RZJDt6299.F03 = IntegerParser.parse(RZJDF03[i]);
            updateList.add(RZJDt6299);
        }
        //更新借款期限
        for (int i = 0; i < JKQXids.length; i++)
        {
            T6299 JKQXt6299 = new T6299();
            JKQXt6299.F01 = IntegerParser.parse(JKQXids[i]);
            JKQXt6299.F02 = IntegerParser.parse(JKQXF02[i]);
            JKQXt6299.F03 = IntegerParser.parse(JKQXF03[i]);
            updateList.add(JKQXt6299);
        }
        if (null != addNHSYF02)
        {
            //新增年化利率
            for (int j = 0; j < addNHSYF02.length; j++)
            {
                T6299 addNHSYt6299 = new T6299();
                addNHSYt6299.F02 = IntegerParser.parse(addNHSYF02[j]);
                addNHSYt6299.F03 = IntegerParser.parse(addNHSYF03[j]);
                addNHSYt6299.F04 = T6299_F04.NHSY;
                addList.add(addNHSYt6299);
            }
        }
        if (null != addRZJDF02)
        {
            //新增借款期限
            for (int j = 0; j < addRZJDF02.length; j++)
            {
                T6299 addRZJDt6299 = new T6299();
                addRZJDt6299.F02 = IntegerParser.parse(addRZJDF02[j]);
                addRZJDt6299.F03 = IntegerParser.parse(addRZJDF03[j]);
                addRZJDt6299.F04 = T6299_F04.RZJD;
                addList.add(addRZJDt6299);
            }
        }
        if (null != addJKQXF02)
        {
            //新增年化利率
            for (int j = 0; j < addJKQXF02.length; j++)
            {
                T6299 addJKQXt6299 = new T6299();
                addJKQXt6299.F02 = IntegerParser.parse(addJKQXF02[j]);
                addJKQXt6299.F03 = IntegerParser.parse(addJKQXF03[j]);
                addJKQXt6299.F04 = T6299_F04.JKQX;
                addList.add(addJKQXt6299);
            }
        }
        filterSettings.updateFilterSettings(updateList);
        filterSettings.addFilterSettings(addList);
        getController().prompt(request, response, PromptLevel.INFO, "设置成功");
        sendRedirect(request, response, getController().getURI(request, FilterSettings.class));
    }
    
}
