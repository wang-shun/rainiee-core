package com.dimeng.p2p.console.servlets.base.optsettings.region;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.console.servlets.base.AbstractBaseServlet;
import com.dimeng.p2p.modules.base.console.service.DistrictManage;
import com.dimeng.util.parser.IntegerParser;

@Right(id = "P2P_C_BASE_REGION_DISABLE", isMenu = true, name = "区域停用", moduleId = "P2P_C_BASE_OPTSETTINGS_REGION")
public class Disable extends AbstractBaseServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void processGet(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		processPost(request, response, serviceSession);
	}

	@Override
	protected void processPost(final HttpServletRequest request,
			final HttpServletResponse response,
			final ServiceSession serviceSession) throws Throwable {
		DistrictManage districtManage = serviceSession
				.getService(DistrictManage.class);
        String id_str = request.getParameter("id");
        String ids_str = request.getParameter("ids");
        if (id_str != null && !"".equals(id_str))
        {
            int id = IntegerParser.parse(request.getParameter("id"));
            districtManage.disable(id);
        }
        else if (ids_str != null && !"".equals(ids_str))
        {
            String[] ids_strs = ids_str.split(",");
            for (String _id_str : ids_strs)
            {
                int id = IntegerParser.parse(_id_str);
                districtManage.disable(id);
            }
        }
		sendRedirect(request, response,
				getController().getURI(request, RegionList.class));
	}
}
