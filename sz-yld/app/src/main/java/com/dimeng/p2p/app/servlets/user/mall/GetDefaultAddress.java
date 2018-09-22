/*
 * 文 件 名:  GetDefaultAddress.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2016年1月28日
 */
package com.dimeng.p2p.app.servlets.user.mall;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S63.entities.T6355;
import com.dimeng.p2p.app.service.BusinessManage;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.app.servlets.user.domain.mall.GetAddress;
import com.dimeng.p2p.repeater.score.UserCenterScoreManage;

/**
 * 用户默认收货地址页面
 * 
 * @author  Jason
 * @version  [版本号, 2016年2月29日]
 */
public class GetDefaultAddress extends AbstractSecureServlet
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 647069847939919002L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        UserCenterScoreManage userCenterScoreManage = serviceSession.getService(UserCenterScoreManage.class);
        GetAddress infos = new GetAddress();
        //获取默认收获地址
        T6355 t6355 = userCenterScoreManage.queryDefaultAddress();
        if (null != t6355)
        {
            //收货地址Id
            infos.setId(t6355.F01);
            
            //用户Id
            infos.setUserid(t6355.F02);
            
            //用户姓名
            infos.setName(t6355.F03);
            
            //手机号码
            infos.setPhone(t6355.F06);
            
            //区域
            infos.setRegion(t6355.szdq);
            
            //详细地址
            infos.setAddress(t6355.F05);
            
            //是否默认
            infos.setYesOrNo(t6355.F08);
            
            //邮政编码
            infos.setVo(t6355.F07);
        }
        //没有默认地址 默认显示最新添加的一条记录
        else
        {
            BusinessManage businessManage = serviceSession.getService(BusinessManage.class);
            T6355[] tList = businessManage.queryHarvestAddress();
            if (tList != null && tList.length > 0)
            {
                for (T6355 t63 : tList)
                {
                    //收货地址Id
                    infos.setId(t63.F01);
                    
                    //用户Id
                    infos.setUserid(t63.F02);
                    
                    //用户姓名
                    infos.setName(t63.F03);
                    
                    //手机号码
                    infos.setPhone(t63.F06);
                    
                    //区域Id
                    infos.setRegionID(t63.F04);
                    
                    //区域
                    infos.setRegion(t63.szdq);
                    
                    //详细地址
                    infos.setAddress(t63.F05);
                    
                    //是否默认
                    infos.setYesOrNo(t63.F08);
                    
                    //邮政编码
                    infos.setVo(t63.F07);
                }
            }
        }
        
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success！", infos);
        return;
    }
    
    @Override
    protected void processGet(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
}
