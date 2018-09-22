/*
 * 文 件 名:  StartFindServlet.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  God
 * 修改时间:  2016年6月1日
 */
package com.dimeng.p2p.app.servlets.platinfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.upload.FileStore;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.app.servlets.AbstractAppServlet;
import com.dimeng.p2p.app.servlets.domain.Adv;
import com.dimeng.p2p.modules.base.console.service.AppStartFindSetManage;
import com.dimeng.p2p.modules.base.console.service.entity.AdvertisementRecord;
import com.dimeng.util.StringHelper;

/**
 * APP查询启动页图片接口
 * 
 * @author  suwei
 * @version  [版本号, 2016年6月1日]
 */
public class StartFindServlet extends AbstractAppServlet
{
    private static final long serialVersionUID = 1L;
    
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
        // 获取前端传过来的启动发现页图片图片类型
        String advType = getParameter(request, "advType");
        if (StringHelper.isEmpty(advType))
        {
            setReturnMsg(request, response, ExceptionCode.PARAMETER_ERROR, "请求参数错误");
            return;
        }
        
        AppStartFindSetManage manage = serviceSession.getService(AppStartFindSetManage.class);
        
        AdvertisementRecord record = manage.selectPic(advType);
        
        Adv adv = new Adv();
        FileStore fileStore = getResourceProvider().getResource(FileStore.class);
        adv.setAdvImg(record == null || StringHelper.isEmpty(record.imageCode) ? ""
            : getSiteDomain(fileStore.getURL(record.imageCode)));
        adv.setAdvTitle(record == null ? "" : record.title);
        adv.setAdvUrl(record == null ? "" : StringHelper.isEmpty(record.url) ? "" : record.url);
        
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success", adv);
    }
}
