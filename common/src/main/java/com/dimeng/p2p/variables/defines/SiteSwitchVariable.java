/*
 * 文 件 名:  MallVariavle.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoucl
 * 修改时间:  2015年12月14日
 */
package com.dimeng.p2p.variables.defines;

import java.io.InputStreamReader;

import com.dimeng.framework.config.VariableTypeAnnotation;
import com.dimeng.framework.config.entity.VariableBean;

/**
 * <网站功能开关>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2015年12月14日]
 */
@VariableTypeAnnotation(id = "SITESWITCH", name = "网站功能开关")
public enum SiteSwitchVariable implements VariableBean
{
    /**
     * 网站视频功能开关，true：有视频功能；false：没有视频功能
     */
    SITE_VIDEO_SWITCH("网站视频功能开关，true：有视频功能；false：没有视频功能")
    {
        @Override
        public String getValue()
        {
            return "true";
        }
    },
    /**
     *公益标功能开关，true：有公益标功能；false：没有公益标功能
     */
    DONATION_BID_SWITCH("公益标功能开关，true：有公益标功能；false：没有公益标功能")
    {
        @Override
        public String getValue()
        {
            return "false";
        }
    },
    /**
     *IOS因appstore审核原因需有一个单独的控制公益标功能开闭的开关，true：开通公益标功能；false：关闭公益标功能
     */
    IOS_DONATION_BID_SWITCH("IOS端控制公益标功能开关，true：开通公益标功能；false：关闭公益标功能")
    {
        @Override
        public String getValue()
        {
            return "false";
        }
    },
    /**
     *不良资产处理功能开关，true：有不良资产处理功能；false：没有不良资产处理功能
     */
    PT_ADVANCE_SWITCH("不良资产处理功能开关，true：有不良资产处理功能；false：没有不良资产处理功能")
    {
        @Override
        public String getValue()
        {
            return "false";
        }
    },
    /**
     *红包/加息券功能开关，true：有红包/加息券功能；false：没有红包/加息券功能
     */
    REDPACKET_INTEREST_SWITCH("红包/加息券功能开关，true：有红包/加息券功能；false：没有红包/加息券功能")
    {
        @Override
        public String getValue()
        {
            return "true";
        }
    },
    /**
     *我要吐槽功能开关，true：有我要吐槽功能；false：没有我要吐槽功能
     */
    ADVICE_COMPLAIN_SWITCH("我要吐槽功能开关，true：有我要吐槽功能；false：没有我要吐槽功能")
    {
        @Override
        public String getValue()
        {
            return "true";
        }
    },
    /**
     *担保贷功能开关，true：有担保贷功能；false：没有担保贷功能
     */
    GUARANTEE_DBD_SWITCH("担保贷功能开关，true：有担保贷功能；false：没有担保贷功能")
    {
        @Override
        public String getValue()
        {
            return "false";
        }
    };
    
    protected final String key;
    
    protected final String description;
    
    SiteSwitchVariable(String description)
    {
        StringBuilder builder = new StringBuilder(getType());
        builder.append('.').append(name());
        key = builder.toString();
        this.description = description;
    }
    
    @Override
    public String getType()
    {
        return SiteSwitchVariable.class.getAnnotation(VariableTypeAnnotation.class).id();
    }
    
    @Override
    public String getKey()
    {
        return key;
    }
    
    @Override
    public String getDescription()
    {
        return description;
    }
    
    @Override
    public String getValue()
    {
        try (InputStreamReader reader =
            new InputStreamReader(SiteSwitchVariable.class.getResourceAsStream(getKey()), "UTF-8"))
        {
            StringBuilder builder = new StringBuilder();
            char[] cbuf = new char[1024];
            int len = reader.read(cbuf);
            while (len > 0)
            {
                builder.append(cbuf, 0, len);
                len = reader.read(cbuf);
            }
            return builder.toString();
        }
        catch (Throwable t)
        {
        }
        return null;
    }
    
    @Override
    public boolean isInit()
    {
        return true;
    }
}
