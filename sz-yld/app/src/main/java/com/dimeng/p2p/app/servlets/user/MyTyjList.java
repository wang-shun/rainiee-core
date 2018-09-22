/*
 * 文 件 名:  MyTyjList.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2015年11月23日
 */
package com.dimeng.p2p.app.servlets.user;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.enums.T6103_F06;
import com.dimeng.p2p.account.user.service.MyExperienceManage;
import com.dimeng.p2p.account.user.service.entity.MyExperienceRecod;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.app.servlets.user.domain.ExperienceInfo;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.LongParser;

/**
 * 我的体验金列表
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年11月23日]
 */
public class MyTyjList extends AbstractSecureServlet
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -6038142946026843552L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        // 获取请求参数信息
        final int currentPage = IntegerParser.parse(getParameter(request, "pageIndex"));
        final int pageSize = IntegerParser.parse(getParameter(request, "pageSize"));
        
        MyExperienceManage service = serviceSession.getService(MyExperienceManage.class);
        
        // 封装查询参数
        Map<String, Object> params = new HashMap<String, Object>();
        
        // 查询体验金列表
        PagingResult<MyExperienceRecod> result = service.searchAll(params, getPaging(currentPage, pageSize));
        
        List<ExperienceInfo> experienceInfos = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (null != result)
        {
            MyExperienceRecod[] myExperienceRecod = null;
            
            // 判断是否超出记录集
            if (result.getPageCount() >= LongParser.parse(currentPage))
            {
                myExperienceRecod = result.getItems();
            }
            
            if (null != myExperienceRecod && myExperienceRecod.length > 0)
            {
                experienceInfos = new ArrayList<ExperienceInfo>();
                
                for (MyExperienceRecod record : myExperienceRecod)
                {
                    ExperienceInfo info = new ExperienceInfo();
                    // 体验金id
                    info.setExperienceId(record.F01);
                    
                    // 体验金状态
                    info.setStatus(record.F06.name());
                    
                    // 状态描述
                    info.setStatusDes(record.state);
                    
                    // 体验金金额
                    info.setExpAmount(record.expAmount);
                    
                    // 如果体验金未使用或者已过期时，显示过期时间
                    if (T6103_F06.WSY.name().equals(record.F06.name())
                        || T6103_F06.YGQ.name().equals(record.F06.name()))
                    {
                        // 失效时间
                        info.setEndDate(sdf.format(record.F05));
                    }
                    
                    experienceInfos.add(info);
                }
            }
        }
        
        // 封装页面信息
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success", experienceInfos);
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
