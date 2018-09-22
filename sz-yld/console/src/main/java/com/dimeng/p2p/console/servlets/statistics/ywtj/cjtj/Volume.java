package com.dimeng.p2p.console.servlets.statistics.ywtj.cjtj;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.console.servlets.statistics.AbstractStatisticsServlet;
import com.dimeng.p2p.modules.statistics.console.service.VolumeManage;
import com.dimeng.p2p.modules.statistics.console.service.entity.Profile;
import com.dimeng.p2p.modules.statistics.console.service.entity.VolumeEntity;
import com.dimeng.p2p.modules.statistics.console.service.entity.VolumeRegion;
import com.dimeng.p2p.modules.statistics.console.service.entity.VolumeTimeLimit;
import com.dimeng.p2p.modules.statistics.console.service.entity.VolumeType;
import com.dimeng.util.parser.IntegerParser;

@Right(id = "P2P_C_STATISTICS_VOLUME", name = "成交数据统计", moduleId = "P2P_C_STATISTICS_YWTJ_CJTJ", order = 0)
public class Volume extends AbstractStatisticsServlet
{
    
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
        int year = IntegerParser.parse(request.getParameter("year"));
        VolumeManage manage = serviceSession.getService(VolumeManage.class);
        VolumeEntity[] vEntities = null;
        if (year > 0)
        {
            vEntities = manage.getVolumeEntities(year);
        }
        else
        {
            vEntities = manage.getAllVolumeEntities();
        }
        VolumeEntity[] lvEntities = manage.getLastYearVolumeEntities(year);
        Profile profile = manage.getProfile(year);
        VolumeType[] volumeTypes = manage.getVolumeTypes(year); //按产品类型
        VolumeTimeLimit[] volumeTimeLimits = manage.getVolumeTimeLimits(year); //按期限
        VolumeRegion[] volumeRegions = manage.getVolumeRegions(year); //按地域
        int[] options = manage.getStatisticedYear();
        request.setAttribute("options", options);
        request.setAttribute("vEntities", vEntities);
        request.setAttribute("lvEntities", lvEntities);
        request.setAttribute("profile", profile);
        request.setAttribute("volumeTypes", volumeTypes);
        request.setAttribute("volumeTimeLimits", volumeTimeLimits);
        request.setAttribute("volumeRegions", volumeRegions);
        request.setAttribute("year", year);
        forwardView(request, response, getClass());
    }
}
