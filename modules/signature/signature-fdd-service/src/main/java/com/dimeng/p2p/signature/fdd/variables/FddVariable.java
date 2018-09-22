package com.dimeng.p2p.signature.fdd.variables;

import java.io.InputStreamReader;

import com.dimeng.framework.config.VariableTypeAnnotation;
import com.dimeng.framework.config.entity.VariableBean;

/**
 * 法大大电子签章常量
 * @author DENGWENWU
 *
 */
@VariableTypeAnnotation(id = "FDD_SIGNATURE", name = "法大大电子签章")
public enum FddVariable implements VariableBean
{
    
    /**
     * 平台编号
     */
    FDD_API_ID("法大大-平台编号")
    {
        @Override
        public String getValue()
        {
            return "400608";
        }
    },
    
    /**
     * 密钥 
     */
    FDD_APP_SECRET("法大大-密钥 ")
    {
        @Override
        public String getValue()
        {
            return "8zSoKeXP3L7YO3UJsVSf1BFI";
        }
    },
    /**
     * 版本
     */
    FDD_V("法大大-版本")
    {
        @Override
        public String getValue()
        {
            return "2.0";
        }
    },
    
    /**
     * 法大大CA证书申请请求
     */
    FDD_CA_SYNCPERSON_URL("法大大-CA证书申请请求")
    {
        @Override
        public String getValue()
        {
            return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/user/fdd/fDDSyncPerson.htm";
        }
    },
    
    /**
     * 法大大手动文档签署请求
     */
    FDD_EXTSIGN_URL("法大大-文档签署请求-手动模式")
    {
        @Override
        public String getValue()
        {
            return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/user/fdd/fDDExtSign.htm";
        }
    },
    
    /**
     * 法大大自动文档签署请求
     */
    FDD_EXTSIGN_AUTO_URL("法大大-文档签署请求-自动模式")
    {
        @Override
        public String getValue()
        {
            return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/console/fdd/fDDExtSignAuto.htm";
        }
    },

    /**
     * 法大大文档签署回调地址-手动模式
     */
    FDD_EXTSIGN_NOTIFY("法大大-文档签署回调地址-手动模式")
    {
        @Override
        public String getValue()
        {
            return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/pay/fdd/ret/fDDExtSignNotify.htm";
        }
    },

    /**
     * 法大大自动签章-扫描多少分钟之前的订单
     */
    FDD_AUTORECON_ORDTIME("法大大-自动签章-扫描多少分钟之前的订单")
    {
        @Override
        public String getValue()
        {
            return "100";
        }
    },
    
    /**
     * 地址
     * 测试地址：http://test.api.fabigbig.com:8888/api/ https://testapi.fadada.com:8443/api/
     */
    FDD_API_URL("法大大-接口请求地址")
    {
        @Override
        public String getValue()
        {
            return "https://testapi.fadada.com:8443/api/";
        }
    },

    
    /**
     * 个人注册，免审核地址
     */
    FDD_URL_SYNCPERSON_AUTO("法大大-用户注册CA证书申请action")
    {
        @Override
        public String getValue()
        {
            return "syncPerson_auto.action";
        }
    },

    /**
     * 客户信息修改接口地址
     */
    FDD_URL_INFO_CHANGE("法大大-客户信息修改action")
    {
        @Override
        public String getValue()
        {
            return "infochange.action";
        }
    },

    /**
     * 上传文档
     */
    FDD_URL_UPLOADDOCS("法大大-文档传输,合同上传action")
    {
        @Override
        public String getValue()
        {
            return "uploaddocs.api";
        }
    },
    
    /**
     * 手动签署地址
     */
    FDD_URL_EXTSIGN("法大大-合同手动签署action")
    {
        @Override
        public String getValue()
        {
            return "extsign.action";
        }
    },
    
    /**
     * 自动签署地址
     */
    FDD_URL_EXTSIGN_AUTO("法大大-合同自动签署action")
    {
        @Override
        public String getValue()
        {
            return "extsign_auto.action";
        }
    },
    /**
     * 签署状态查询接口
     */
    FDD_URL_QUERY_SIGN_STATUS("签署状态查询接口")
    {
        @Override
        public String getValue()
        {
            return "query_signstatus.action";
        }
    },
    /**
    * 合同归档地址
    */
    FDD_URL_CONTRACTFILING("法大大-合同归档action")
    {
        @Override
        public String getValue()
        {
            return "contractFiling.action";
        }
    };
    
    protected final String key;
    
    protected final String description;
    
    FddVariable(String description)
    {
        StringBuilder builder = new StringBuilder(getType());
        builder.append('.').append(name());
        key = builder.toString();
        this.description = description;
    }
    
    @Override
    public String getType()
    {
        return FddVariable.class.getAnnotation(VariableTypeAnnotation.class).id();
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
            new InputStreamReader(FddVariable.class.getResourceAsStream(getKey()), "UTF-8"))
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
