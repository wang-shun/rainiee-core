package com.dimeng.p2p.app.servlets.platinfo;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.upload.FileStore;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S50.entities.T5016;
import com.dimeng.p2p.app.servlets.AbstractAppServlet;
import com.dimeng.p2p.app.servlets.domain.Adv;
import com.dimeng.p2p.modules.base.front.service.AdvertisementManage;
import com.dimeng.util.StringHelper;

/**
 * 广告详情
 * @author zhoulantao
 *
 */
public class AdvServlet extends AbstractAppServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected boolean mustAuthenticated()
    {
        return false;
    }
    
    @Override
    protected void processGet(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        AdvertisementManage advertisementManage = serviceSession.getService(AdvertisementManage.class);
        T5016[] advertisements = advertisementManage.getAll_BZB("APP");
        List<Adv> advs = new ArrayList<Adv>();
        if (advertisements != null && advertisements.length > 0)
        {
            FileStore fileStore = getResourceProvider().getResource(FileStore.class);
            for (T5016 advertisement : advertisements)
            {
                if (advertisement == null)
                {
                    continue;
                }
                Adv adv = new Adv();
                adv.setAdvImg(getSiteDomain(fileStore.getURL(advertisement.F05)));
                adv.setAdvTitle(advertisement.F03);
                if (!StringHelper.isEmpty(advertisement.F04))
                {
                    adv.setAdvUrl(advertisement.F04);
                }
                advs.add(adv);
            }
        }
        
        // 封装返回信息
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success!", advs);
        return;
    }
    
}
