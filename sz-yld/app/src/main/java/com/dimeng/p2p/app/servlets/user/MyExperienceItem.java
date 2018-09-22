/*
 * 文 件 名:  MyExperienceItem.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2015年12月2日
 */
package com.dimeng.p2p.app.servlets.user;

import java.math.BigDecimal;
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
import com.dimeng.p2p.S62.enums.T6231_F21;
import com.dimeng.p2p.account.user.service.entity.MyExperience;
import com.dimeng.p2p.app.service.BusinessManage;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.app.servlets.user.domain.ExperienceInfo;

/**
 * 体验金使用详情(查看使用中、已投资、已结清的使用详情)
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年12月2日]
 */
public class MyExperienceItem extends AbstractSecureServlet
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 5138327578850148772L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        // 体验金ID
        final int expId = Integer.parseInt(getParameter(request, "expId"));
        
        // 体验金状态
        final String status = getParameter(request, "status");
        
        // 设置查询参数
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("expId", expId);
        if (T6103_F06.YWT.name().equals(status))
        {
            params.put("status", "SQZ");
        }
        else if (T6103_F06.YTZ.name().equals(status))
        {
            params.put("status", "CYZ");
        }
        else if (T6103_F06.YJQ.name().equals(status))
        {
            params.put("status", "YJZ");
        }
        else
        {
            setReturnMsg(request, response, ExceptionCode.PARAMETER_ERROR, "体验金状态错误！");
            return;
        }
        
        // 查询指定体验金的投资详情
        BusinessManage service = serviceSession.getService(BusinessManage.class);
        PagingResult<MyExperience> result = service.searMyExperienceById(params, getPaging(1, 0));
        
        List<ExperienceInfo> experienceInfos = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        if (null != result)
        {
            MyExperience[] experiences = result.getItems();
            
            if (null != experiences && experiences.length > 0)
            {
                experienceInfos = new ArrayList<ExperienceInfo>();
                for (MyExperience experience : experiences)
                {
                    ExperienceInfo experienceInfo = new ExperienceInfo();
                    // 借款标题
                    experienceInfo.setBidTitile(experience.F02);
                    
                    // 体验金投资金额
                    experienceInfo.setExpAmount(String.valueOf(experience.F03.setScale(2, BigDecimal.ROUND_HALF_UP)));
                    
                    // 年化利率
                    experienceInfo.setRate(String.valueOf(experience.F04.setScale(4, BigDecimal.ROUND_HALF_UP)));
                    
                    // 体验金收益期
                    experienceInfo.setMonths(experience.F10);
                    
                    // 待收金额
                    experienceInfo.setDueInAmount(String.valueOf(experience.F08.setScale(2, BigDecimal.ROUND_HALF_UP)));
                    
                    // 已收金额
                    experienceInfo.setReceivedAmount(String.valueOf(experience.F05.setScale(2, BigDecimal.ROUND_HALF_UP)));
                    
                    // 结清时间
                    experienceInfo.setReceivedDate(experience.F06 == null ? "" : df.format(experience.F06));
                    
                    // 下个还款日
                    experienceInfo.setNextRepaymentDate(experience.F09 == null ? "" : df.format(experience.F09));
                    
                    if (T6103_F06.YWT.name().equals(status))
                    {
                        String returnPeriod = "";
                        if (T6231_F21.F.name().equals(experience.F11.name()))
                        {
                            if ("false".equals(experience.F15))
                            {
                                returnPeriod =
                                    experience.F10 > experience.F13 * 30 ? experience.F13 * 30 + "天" : experience.F10
                                        + "天";
                            }
                            else
                            {
                                returnPeriod =
                                    experience.F10 > experience.F13 ? experience.F13 + "个月" : experience.F10 + "个月";
                            }
                        }
                        else
                        {
                            if ("false".equals(experience.F15))
                            {
                                returnPeriod =
                                    experience.F10 > experience.F12 ? experience.F12 + "天" : experience.F10 + "天";
                            }
                            else
                            {
                                returnPeriod =
                                    experience.F10 > experience.F12 ? experience.F12 + "个月" : experience.F10 + "个月";
                            }
                        }
                        
                        // 体验收益期
                        experienceInfo.setReturnPeriod(returnPeriod);
                    }
                    
                    experienceInfos.add(experienceInfo);
                }
            }
        }
        
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success！", experienceInfos);
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
