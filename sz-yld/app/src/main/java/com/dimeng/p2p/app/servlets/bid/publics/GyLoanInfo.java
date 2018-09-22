/*
 * 文 件 名:  GyLoanList.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2015年11月24日
 */
package com.dimeng.p2p.app.servlets.bid.publics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.upload.FileStore;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S50.entities.T5016;
import com.dimeng.p2p.S50.enums.T5016_F12;
import com.dimeng.p2p.app.servlets.AbstractAppServlet;
import com.dimeng.p2p.app.servlets.domain.Adv;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.modules.base.front.service.AdvertisementManage;
import com.dimeng.p2p.repeater.donation.GyLoanManage;
import com.dimeng.p2p.repeater.donation.entity.GyLoanStatis;
import com.dimeng.util.StringHelper;

/**
 * 公益标信息(广告、统计信息)
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年11月24日]
 */
public class GyLoanInfo extends AbstractAppServlet
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -7616016104794974897L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        // 获取公益标广告页面
        List<Adv> advs = getGyLoanAdv(serviceSession);
        
        // 获取统计信息
        GyLoanManage gyLoanManage = serviceSession.getService(GyLoanManage.class);
        GyLoanStatis gys = gyLoanManage.gyLoanStatistics(0);
        
        // 封装返回给页面信息
        Map<String, Object> loanInfo = new HashMap<String, Object>();
        
        // 广告信息
        loanInfo.put("advs", advs);
        
        // 累计捐赠总金额
        loanInfo.put("donationsAmount", gys.donationsAmount);
        
        // 累计捐赠总笔数
        loanInfo.put("donationsNum", gys.donationsNum);
        
        // 返回公益标信息
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success！", loanInfo);
    }
    
    /**
     * 修改公益标的广告页面
     * 
     * @param serviceSession 上下文session
     * @return 公益标广告页面
     * @throws Throwable 异常信息
     */
    private List<Adv> getGyLoanAdv(final ServiceSession serviceSession)
        throws Throwable
    {
        // 查询广告页面
        AdvertisementManage advertisementManage = serviceSession.getService(AdvertisementManage.class);
        T5016[] advertisements = advertisementManage.getAll_BZB(T5016_F12.APPGYJZ.name());
        
        List<Adv> advs = new ArrayList<Adv>();
        if (advertisements != null && advertisements.length > 0)
        {
            // 到环境的上传文件路径获取广告图片内容
            FileStore fileStore = getResourceProvider().getResource(FileStore.class);
            
            for (T5016 advertisement : advertisements)
            {
                if (advertisement == null)
                {
                    continue;
                }
                Adv adv = new Adv();
                
                // 封装完整图片路径返回给页面显示
                adv.setAdvImg(getSiteDomain(fileStore.getURL(advertisement.F05)));
                
                // 广告的标题信息
                adv.setAdvTitle(advertisement.F03);
                
                // 广告链接内容地址，有则返回
                if (!StringHelper.isEmpty(advertisement.F04))
                {
                    adv.setAdvUrl(advertisement.F04);
                }
                advs.add(adv);
            }
        }
        
        return advs;
    }
    
    @Override
    protected void processGet(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
}
