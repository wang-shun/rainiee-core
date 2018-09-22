package com.dimeng.p2p.app.servlets.loan;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S50.entities.T5019;
import com.dimeng.p2p.S50.enums.T5019_F11;
import com.dimeng.p2p.app.servlets.AbstractAppServlet;
import com.dimeng.p2p.app.servlets.bid.domain.Prefix;
import com.dimeng.p2p.app.servlets.pay.domain.Address;
import com.dimeng.p2p.app.servlets.pay.service.shuangqian.service.SQDistrictManage;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.modules.base.front.service.DistrictManage;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BooleanParser;

/**
 * 
 * 根据行政区(查省、根据省查市、根据市查县)
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年11月12日]
 */
public class SearchAddress extends AbstractAppServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        // 获取查询类型   省:SHENG 市:SHI 县:XIAN
        final String type = getParameter(request, "type");
        
        T5019[] t5019s = getNomalRegion(type, serviceSession, request);
        
        // 查询结果不为空则返回行政区列表
        if (t5019s != null && t5019s.length > 0)
        {
            List<Address> addresss = new ArrayList<Address>();
            for (T5019 t5019 : t5019s)
            {
                Address a = new Address();
                
                // 设置行政区ID
                a.setId(t5019.F01);
                if (T5019_F11.SHENG.name().equals(type))
                {
                    a.setSheng(t5019.F05);
                }
                // 如果查询类型为市
                else if (T5019_F11.SHI.name().equals(type))
                {
                    a.setShi(t5019.F05);
                }
                // 如果查询类型为县
                else if (T5019_F11.XIAN.name().equals(type))
                {
                    a.setXian(t5019.F05);
                }
                
                addresss.add(a);
            }
            
            // 返回下级行政区列表
            setReturnMsg(request, response, ExceptionCode.SUCCESS, "success!", addresss);
            return;
        }
        else
        {
            // 根据条件没有查询到下级行政区
            setReturnMsg(request, response, ExceptionCode.SUCCESS, "success!");
            return;
        }
    }
    
    /**
     * 获取通用版地区信息
     * 
     * @param type 查询类型
     * @param serviceSession 消息上下文
     * @param request 请求消息
     * @return 地区信息
     * @throws Throwable 异常信息
     */
    private T5019[] getNomalRegion(final String type, final ServiceSession serviceSession,
        final HttpServletRequest request)
        throws Throwable
    {
        DistrictManage disManage = serviceSession.getService(DistrictManage.class);
        T5019[] t5019s = null;
        
        // 如果类型为查询省
        if (T5019_F11.SHENG.name().equals(type))
        {
            // 查询省ID列表
            t5019s = disManage.getSheng();
        }
        // 如果查询类型为市
        else if (T5019_F11.SHI.name().equals(type))
        {
            // 获取省ID
            final int provinceId = Integer.parseInt(getParameter(request, "provinceId"));
            
            // 根据省ID查询市ID列表
            t5019s = disManage.getShi(provinceId);
        }
        // 如果查询类型为县
        else if (T5019_F11.XIAN.name().equals(type))
        {
            // 获取市ID
            final int cityId = Integer.parseInt(getParameter(request, "cityId"));
            
            // 根据市ID查询县ID列表
            t5019s = disManage.getXian(cityId);
        }
        
        return t5019s;
    }
}
