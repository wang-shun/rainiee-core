package com.dimeng.p2p.escrow.fuyou.variables;

import java.io.InputStreamReader;
import java.util.Date;

import com.dimeng.framework.config.VariableTypeAnnotation;
import com.dimeng.framework.config.entity.VariableBean;
import com.dimeng.util.parser.DateParser;

/**
 * 
 * 富友托管的一些常量
 * 
 * @author  heshiping
 * @version  [版本号, 2016年1月19日]
 */
@VariableTypeAnnotation(id = "FUYOU_ESCROW", name = "富友托管")
public enum FuyouVariable implements VariableBean
{
    
    /**
     * 版本号
     */
    FUYOU_VERSION("版本号")
    {
        @Override
        public String getValue()
        {
            return "0.44";
        }
    },
    
    /**
     * 充值手续费
     */
    FUYOU_CHAREFEE_ONOFF("充值手续开关ON-OFF")
    {
        @Override
        public String getValue()
        {
            return "OFF";
        }
    },
    /**
     * 提现手续开关ON-OFF
     */
    FUYOU_WITHDRAW_ONOFF("提现手续开关ON-OFF")
    {
        @Override
        public String getValue()
        {
            return "ON";
        }
    },
    
    /**
     * PC端快速充值手续费率
     */
    FUYOU_CHAREFEE_500001("PC快速充值手续费率")
    {
        @Override
        public String getValue()
        {
            return "0.001";
        }
    },
    
    /**
     * PC端快速充值手续费最低限额
     */
    FUYOU_CHAREMINFEE_500001("PC端快速充值手续费最低限额")
    {
        @Override
        public String getValue()
        {
            return "5";
        }
    },
    
    /**
     * PC端快速充值手续费最高限额
     */
    FUYOU_CHAREMAXFEE_500001("PC端快速充值手续费最高限额")
    {
        @Override
        public String getValue()
        {
            return "80";
        }
    },
    
    /**
     * PC端个人网银充值手续费率
     */
    FUYOU_PERSONFEE_500002("PC个人网银充值手续费率")
    {
        @Override
        public String getValue()
        {
            return "0.0018";
        }
    },
    
    /**
     * PC端企业网银充值手续费
     */
    FUYOU_CORPFEE_500002("PC端企业网银充值手续费")
    {
        @Override
        public String getValue()
        {
            return "15";
        }
    },
    
    /**
     * 快捷充值手续费最低限额
     */
    FUYOU_CHAREMINFEE_500405("快捷充值手续费最低限额")
    {
        @Override
        public String getValue()
        {
            return "2";
        }
    },
    
    /**
     * 快捷充值手续费率
     */
    FUYOU_PERSONFEE_500405("快捷充值手续费率")
    {
        @Override
        public String getValue()
        {
            return "0.0015";
        }
    },
    
    /**
     * APP端快捷支付充值手续费率
     */
    FUYOU_CHAREFEE_APP_500002("APP端快捷支付充值手续费率")
    {
        @Override
        public String getValue()
        {
            return "0.002";
        }
    },
    
    /**
     * APP端快捷支付充值手续费最低限额
     */
    FUYOU_CHAREMINFEE_APP_500002("APP端快捷支付充值手续费最低限额")
    {
        @Override
        public String getValue()
        {
            return "2";
        }
    },
    
    /**
     * 富友托管注册地址
     */
    FUYOU_USREG_URL("富友托管注册地址")
    {
        @Override
        public String getValue()
        {
            return "${FUYOU_ESCROW.FUYOU_URL}/webReg.action";
        }
    },
    /**
     * 富友，企业注册地址
     */
    FUYOU_COMREG_URL("富友，企业注册地址")
    {
        @Override
        public String getValue()
        {
            return "${FUYOU_ESCROW.FUYOU_URL}/webArtifReg.action";
        }
    },
    
    /**
     * p2p平台在富友操作员账号
     */
    FUYOU_P2P_ACCOUNT_NAME("p2p平台在富友操作员账号")
    {
        @Override
        public String getValue()
        {
            return "XXXXXXXXXXX";
        }
    },
    
    /**
     * 富友，企业注册商户后台通知地址
     */
    FUYOU_LEGAL_BACK_NOTIFY_URL("富友，企业注册商户后台通知地址")
    {
        @Override
        public String getValue()
        {
            return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/pay/fuyou/ret/comRegisterRet.htm";
        }
    },
    /**
     * 富友，网银充值地址
     */
    FUYOU_500002_URL("富友，网银充值地址")
    {
        @Override
        public String getValue()
        {
            return "${FUYOU_ESCROW.FUYOU_URL}/500002.action";
        }
    },
    
    /**
     * 富友，快速充值地址
     */
    FUYOU_500001_URL("富友，快速充值地址")
    {
        @Override
        public String getValue()
        {
            return "${FUYOU_ESCROW.FUYOU_URL}/500001.action";
        }
    },
    
    /**
     * 富友，PC快捷充值地址
     */
    FUYOU_500405_URL("富友，PC快捷充值地址")
    {
        @Override
        public String getValue()
        {
            return "${FUYOU_ESCROW.FUYOU_URL}/500405.action";
        }
    },
    
    /**
     * 富友，网银充值通知回调地址
     */
    FUYOU_CHARGENOTICE("富友，网银充值通知回调地址")
    {
        @Override
        public String getValue()
        {
            return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/pay/fuyou/ret/chargeNotice.htm";
        }
    },
    
    /**
    * 富友，快速充值通知回调地址
    */
    FUYOU_CHARGEFAST_NOTIFY("富友，网银充值通知回调地址")
    {
        @Override
        public String getValue()
        {
            return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/pay/fuyou/ret/chargeFastNotify.htm";
        }
    },
    
    /**
     * 富友，充值返回地址
     */
    FUYOU_CHARGE("富友，网银充值返回地址")
    {
        @Override
        public String getValue()
        {
            return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/pay/fuyou/ret/chargeRet.htm";
        }
    },
    
    /**
    * 富友，快速充值返回地址
    */
    FUYOU_CHARGEFAST("富友，充值返回地址")
    {
        @Override
        public String getValue()
        {
            return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/pay/fuyou/ret/chargeFastRet.htm";
        }
    },
    
    /**
     * 富友自动对账 扫描多少分钟之前的订单
     */
    FUYOU_AUTORECON_ORDTIME("扫描多少分钟之前的订单")
    {
        @Override
        public String getValue()
        {
            return "100";
        }
    },
    
    /**
     * 富友自动对账 扫描前多少条订单
     */
    FUYOU_NUMBER("扫描前多少条订单")
    {
        @Override
        public String getValue()
        {
            return "100";
        }
    },
    /**
     * 富友自动转账 失败订单多长时间不再进行转账处理
     */
    FUYOU_TRANSFER_SB_TIME("自动转账失败订单多长时间不再进行转账处理,小时")
    {
        @Override
        public String getValue()
        {
            return "4";
        }
    },

    /**
     * 富友的IP地址和端口
     */
    FUYOU_URL("富友的IP地址和端口")
    {
        @Override
        public String getValue()
        {
            return "https://jzh-test.fuiou.com/jzh/";
        }
    },
    /**
     * 富友，APP用户注册地址
     */
    FUYOU_APP_REG_URL("富友，APP用户注册地址")
    {
        @Override
        public String getValue()
        {
            return "${FUYOU_ESCROW.FUYOU_URL}/app/appWebReg.action";
        }
    },
    /**
     * 富友，APP提现地址
     */
    FUYOU_APP_500003_URL("富友，APP提现地址")
    {
        @Override
        public String getValue()
        {
            return "${FUYOU_ESCROW.FUYOU_URL}/app/500003.action";
        }
    },
    
    /**
     * 富友，APP快捷充值地址
     */
    FUYOU_APP_500002_URL("富友，APP快捷充值地址")
    {
        @Override
        public String getValue()
        {
            return "${FUYOU_ESCROW.FUYOU_URL}/app/500002.action";
        }
    },
    /**
     * 富友，APP快速充值地址
     */
    FUYOU_APP_500001_URL("富友，APP快速充值地址")
    {
        @Override
        public String getValue()
        {
            return "${FUYOU_ESCROW.FUYOU_URL}/app/500001.action";
        }
    },
    
    /**
     * 富友，APP用户更换银行卡接口地址
     */
    FUYOU_APP_CHANGECARD_URL("富友，APP用户更换银行卡接口地址")
    {
        @Override
        public String getValue()
        {
            return "${FUYOU_ESCROW.FUYOU_URL}/app/appChangeCard.action";
        }
    },
    /**
     * 预授权接口地址
     */
    FUYOU_PREAUTH_URL("预授权接口地址")
    {
        @Override
        public String getValue()
        {
            return "${FUYOU_ESCROW.FUYOU_URL}/preAuth.action";
        }
    },
    /**
     * 个人APP端更换手机号
     */
    FUYOU_APP_400101_URL("个人APP端更换手机号")
    {
        @Override
        public String getValue()
        {
            return "${FUYOU_ESCROW.FUYOU_URL}/app/400101.action";
        }
    },
    /**
     * 个人PC端更换手机号
     */
    FUYOU_PC_400101_URL("个人PC端更换手机号")
    {
        @Override
        public String getValue()
        {
            return "${FUYOU_ESCROW.FUYOU_URL}/400101.action";
        }
    },
    /**
     * 预授权撤销接口地址
     */
    FUYOU_PREAUTHCANCEL_URL("预授权撤销接口地址")
    {
        @Override
        public String getValue()
        {
            return "${FUYOU_ESCROW.FUYOU_URL}/preAuthCancel.action";
        }
    },
    /**
     * 转账接口地址(商户与个人)
     */
    FUYOU_TRANSFERBMU_URL("转账接口地址")
    {
        @Override
        public String getValue()
        {
            return "${FUYOU_ESCROW.FUYOU_URL}/transferBmu.action";
        }
    },
    
    /**
     * 富友，划拨接口地址(个人与个人)
     */
    FUYOU_TRANSFERBU_URL("富友，划拨接口地址")
    {
        @Override
        public String getValue()
        {
            return "${FUYOU_ESCROW.FUYOU_URL}/transferBu.action";
        }
    },
    /**
     * 富友提现接口（免登陆)
     */
    FUYOU_500003_URL("富友免登录提现接口")
    {
        @Override
        public String getValue()
        {
            return "${FUYOU_ESCROW.FUYOU_URL}/500003.action";
        }
    },
    
    /**
     * 设置用户提现方式（T+0 T+1）
     */
    FUYOU_TXFS_URL("设置用户提现模式")
    {
        @Override
        public String getValue()
        {
            return "${FUYOU_ESCROW.FUYOU_URL}/cashWithSetReq.action";
        }
    },
    
    /**
     * 充值、提现单笔查询
     */
    FUYOU_QUERYCZTX_URL("充值、提现单笔查询")
    {
        @Override
        public String getValue()
        {
            return "${FUYOU_ESCROW.FUYOU_URL}/querycztx.action";
        }
    },
    /**
     * 富友提现后台通知地址
     */
    FUYOU_WITHDRANOTICE_URL("富友提现后台通知地址")
    {
        @Override
        public String getValue()
        {
            return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/pay/fuyou/ret/withdrawNotice.htm";
        }
    },
    /**
     * 富友提现页面返回地址
     */
    FUYOU_WITHDRAWRET_URL("富友提现第三方跳转地址")
    {
        @Override
        public String getValue()
        {
            return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/pay/fuyou/ret/withdrawRet.htm";
        }
    },
    /**
     * 富友，余额查询 add by lxl
     */
    FUYOU_QUERY_BLANCE("富友，余额查询接口地址")
    {
        @Override
        public String getValue()
        {
            return "${FUYOU_ESCROW.FUYOU_URL}/BalanceAction.action";
        }
    },
    
    /**
     * 私钥文件路径
     */
    FUYOU_PRIVATEKEY_PATH("私钥文件路径")
    {
        @Override
        public String getValue()
        {
            return "C:/fuyou/prkey.key";
        }
    },
    /**
     * 公钥文件路径
     */
    FUYOU_PUBLICKEY_PATH("公钥文件路径")
    {
        @Override
        public String getValue()
        {
            return "C:/fuyou/pbkey.key";
        }
    },
    
    /**
     * 富友，商户注册后台通知地址
     */
    FUYOU_REGISTER("富友，用户注册通知地址")
    {
        @Override
        public String getValue()
        {
            return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/pay/fuyou/ret/userRegisterRet.htm";
        }
    },
    
    /**
     * 富友，手机号修改响应地址
     */
    FUYOU_PHONERET("富友，手机号修改响应地址")
    {
        @Override
        public String getValue()
        {
            return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/pay/fuyou/ret/phoneRet.htm";
        }
    },
    
    /**
     * 富友，银行卡申修改响应地址
     */
    FUYOU_BANKCARDRET("富友，银行卡申修改响应地址")
    {
        @Override
        public String getValue()
        {
            return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/pay/fuyou/ret/changeCardRet.htm";
        }
    },
    
    /**
     * 富友，银行卡申修改申请地址
     */
    FUYOU_BANKCARD("富友，银行卡申修改响应地址")
    {
        @Override
        public String getValue()
        {
            return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/user/fuyou/fyouChangeCard.htm";
        }
    },
    
    /**
     * 用户信息查询地址
     */
    FUYOU_QUERYUSERINFS_URL("用户信息查询地址")
    {
        @Override
        public String getValue()
        {
            return "${FUYOU_ESCROW.FUYOU_URL}/queryUserInfs.action";
        }
    },
    
    /**
     * 交易查询接口地址
     */
    FUYOU_TRADINGQUERY_URL("交易查询接口地址")
    {
        @Override
        public String getValue()
        {
            return "${FUYOU_ESCROW.FUYOU_URL}/queryTxn.action";
        }
    },
    
    /**
     * 富友，用户更换银行卡接口地址
     */
    FUYOU_CHANGECARD_URL("富友，用户更换银行卡接口地址")
    {
        @Override
        public String getValue()
        {
            return "${FUYOU_ESCROW.FUYOU_URL}/changeCard2.action";
        }
    },
    
    /**
     * 富 友，用户更换银行卡查询地址
     */
    FUYOU_QUERYCHANGECARD_URL("富 友，用户更换银行卡查询地址")
    {
        @Override
        public String getValue()
        {
            return "${FUYOU_ESCROW.FUYOU_URL}/queryChangeCard.action";
        }
    },
    
    /**
     * 富友分配给用友的商户代码
     */
    FUYOU_ACCOUNT_ID("富友分配的商户代码")
    {
        @Override
        public String getValue()
        {
            return "XXXXXXXXXXXXXX";
        }
    },
    /**
     * 富友，资金冻结请求地址
     */
    FUYOU_FUND_FREEZE_URL("富友，资金冻结请求地址")
    {
        @Override
        public String getValue()
        {
            return "${FUYOU_ESCROW.FUYOU_URL}/freeze.action";
        }
    },
    /**
     * 富友，资金解冻请求地址
     */
    FUYOU_FUND_UNFREEZE_URL("富友，资金解冻请求地址")
    {
        @Override
        public String getValue()
        {
            return "${FUYOU_ESCROW.FUYOU_URL}/unFreeze.action";
        }
    };
    
    protected final String key;
    
    protected final String description;
    
    FuyouVariable(String description)
    {
        StringBuilder builder = new StringBuilder(getType());
        builder.append('.').append(name());
        key = builder.toString();
        this.description = description;
    }
    
    @Override
    public String getType()
    {
        return FuyouVariable.class.getAnnotation(VariableTypeAnnotation.class).id();
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
            new InputStreamReader(FuyouVariable.class.getResourceAsStream(getKey()), "UTF-8"))
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
