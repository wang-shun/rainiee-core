/*
 * 文 件 名:  MyharvestAddress.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2016年1月28日
 */
package com.dimeng.p2p.app.servlets.user.mall;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S63.entities.T6355;
import com.dimeng.p2p.app.service.BusinessManage;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.app.servlets.user.domain.mall.GetAddress;
import com.dimeng.p2p.repeater.score.UserCenterScoreManage;
import com.dimeng.util.parser.IntegerParser;

/**
 * 用户收货地址页面
 * 
 * @author  Jason
 * @version  [版本号, 2016年2月28日]
 */
public class MyHarvestAddress extends AbstractSecureServlet
{
    private static final long serialVersionUID = 647069847939919002L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        
        final int pageIndex = IntegerParser.parse(getParameter(request, "pageIndex"));
        final int pageSize = IntegerParser.parse(getParameter(request, "pageSize"));
        // 分页参数
        final Paging paging = getPaging(pageIndex, pageSize);
        
        UserCenterScoreManage userCenterScoreManage = serviceSession.getService(UserCenterScoreManage.class);
        
        BusinessManage businessManage = serviceSession.getService(BusinessManage.class);
        
        //获取收获地址
        PagingResult<T6355> result = businessManage.queryHarvestAddress(paging);
        
        //获取收获地址个数
        int countAddress = userCenterScoreManage.getCountAddress();
        
        // 获取默认收获地址
        T6355 t6355 = userCenterScoreManage.queryDefaultAddress();
        
        List<GetAddress> getAddresses  = new ArrayList<GetAddress>();
            if (null != result && countAddress > 0)
            {
                T6355[] t6355s = result.getItems();
                
                List<T6355> t6355List = new ArrayList<T6355>();
                if (null != t6355s && t6355s.length > 0)
                {
                    t6355List.addAll(Arrays.asList(t6355s));
                    //没有默认地址
                    if(null != t6355){
                    	// 把默认的收货地址从列表中移除
                    	if (null != t6355List && t6355List.size() > 0)
                    	{
                    		for (T6355 t6 : t6355List)
                    		{
                    			if (t6.F01 == t6355.F01)
                    			{
                    				t6355List.remove(t6);
                    				break;
                    			}
                    		}
                    	}
                    	// 添加默认收货地址
                    	getAddresses.add(setAddress(t6355));
                    }
                }
                
                
                if (null != t6355List && t6355List.size() > 0)
                {
                    for (T6355 t6 : t6355List)
                    {
                        getAddresses.add(setAddress(t6));
                    }
                }
            }
        //封装需要发送的数据
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("getAddresses", getAddresses);
        map.put("countAddress", countAddress);
        
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success！", map);
        return;
    }
    
    @Override
    protected void processGet(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    /**
     * 设置收货地址
     * 
     * @param t6 收货地址
     * @return
     */
    private GetAddress setAddress(final T6355 t6)
    {
        final GetAddress info = new GetAddress();
        //用户地址Id
        info.setId(t6.F01);
        
        //用户Id
        info.setUserid(t6.F02);
        
        //用户姓名
        info.setName(t6.F03);
        
        //联系电话
        info.setPhone(t6.F06);
        
        //所在地区
        info.setRegion(t6.szdq);
        
        //所在区域Id
        info.setRegionID(t6.F04);
        
        //详细地址
        info.setAddress(t6.F05);
        
        //时候默认地址
        info.setYesOrNo(t6.F08);
        
        //邮政编码
        info.setVo(t6.F07);
        
        return info;
    }
    
}
