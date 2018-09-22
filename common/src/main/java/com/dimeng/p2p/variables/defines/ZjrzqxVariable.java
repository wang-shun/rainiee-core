package com.dimeng.p2p.variables.defines;

import java.io.InputStreamReader;

import com.dimeng.framework.config.VariableTypeAnnotation;
import com.dimeng.framework.config.entity.VariableBean;

@VariableTypeAnnotation(id = "ZJRZQX", name = "证件认证期限")
public enum ZjrzqxVariable implements VariableBean
{
    
    /**
     * 身份认证(月)
     */
    SFRZ("身份认证(月)")
    {
        @Override
        public String getValue()
        {
            return "600";
        }
    },
    /**
     * 手机实名认证(月)
     */
    SJSMRZ("手机实名认证(月)")
    {
        @Override
        public String getValue()
        {
            return "600";
        }
    },
    /**
     * 学历认证(月)
     */
    XLRZ("学历认证(月)")
    {
        @Override
        public String getValue()
        {
            return "600";
        }
    },
    /**
     * 居住地证明(月)
     */
    JZDZM("居住地证明(月)")
    {
        @Override
        public String getValue()
        {
            return "6";
        }
    },
    /**
     * 结婚认证(月)
     */
    JHRZ("结婚认证(月)")
    {
        @Override
        public String getValue()
        {
            return "600";
        }
    },
    /**
     * 工作认证(月)
     */
    GZRZ("工作认证(月)")
    {
        @Override
        public String getValue()
        {
            return "6";
        }
    },
    /**
     * 技术职称认证(月)
     */
    JSZCRZ("技术职称认证(月)")
    {
        @Override
        public String getValue()
        {
            return "600";
        }
    },
    /**
     * 收入证明(月)
     */
    SRZM("收入证明(月)")
    {
        @Override
        public String getValue()
        {
            return "6";
        }
    },
    /**
     * 信用报告(月)
     */
    XYBG("信用报告(月)")
    {
        @Override
        public String getValue()
        {
            return "6";
        }
    },
    /**
     * 房产认证(月)
     */
    FCRZ("房产认证(月)")
    {
        @Override
        public String getValue()
        {
            return "600";
        }
    },
    /**
     * 购车证明(月)
     */
    GCRZ("购车证明(月)")
    {
        @Override
        public String getValue()
        {
            return "600";
        }
    },
    /**
     * 微博认证(月)
     */
    WBRZ("微博认证(月)")
    {
        @Override
        public String getValue()
        {
            return "600";
        }
    },
    /**
     * 公司认证(月)
     */
    GSRZ("公司认证(月)")
    {
        @Override
        public String getValue()
        {
            return "600";
        }
    },
    /**
     * 基本信息认证(月)
     */
    JBXXRZ("基本信息认证(月)")
    {
        @Override
        public String getValue()
        {
            return "600";
        }
    };
    
    protected final String key;
    
    protected final String description;
    
    ZjrzqxVariable(String description)
    {
        StringBuilder builder = new StringBuilder(getType());
        builder.append('.').append(name());
        key = builder.toString();
        this.description = description;
    }
    
    @Override
    public String getType()
    {
        return ZjrzqxVariable.class.getAnnotation(VariableTypeAnnotation.class).id();
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
            new InputStreamReader(ZjrzqxVariable.class.getResourceAsStream(getKey()), "UTF-8"))
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
