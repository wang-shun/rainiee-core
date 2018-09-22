package com.dimeng.p2p.pay.service.fuyou.varibles;

import com.dimeng.framework.config.VariableTypeAnnotation;
import com.dimeng.framework.config.entity.VariableBean;

@VariableTypeAnnotation(id = "FUYOUPAY", name = "第三方支付-富友支付")
public enum FuyouPayVaribles implements VariableBean
{
    
    ISTEST("是否测试环境", "true"),
    
    MCHNT_VER("富友网关版本号", "1.0.1"),
    
    MCHNT_KEY("富友网关商户秘钥", "vau6p7ldawpezyaugc0kopdrrwm4gkpu"),
    
    MCHNT_CD("富友网关 商户代码 富友分配给各合作商户的唯一识别码", "0002900F0006944"),
    
    MCHNT_CD_H5("富友网关 商户代码 富友分配给各合作商户的唯一识别码-H5", "0001000F0358674"),
    
    MCHNT_KEY_H5("富友网关商户秘钥_H5", "d8n0dh23w2yzrnez52ocqb4ckzp7t0fs"),
    
    QUICK_CHARGE_H5_URL("富友网关快捷支付H5请求地址", "http://www-1.fuiou.com:18670/mobile_pay/h5pay/payAction.pay"),
    
    CHARGE_URL("富友网关网银充值请求地址", "http://www-1.fuiou.com:8888/wg1_run/smpGate.do"),
    
    CHARGE_CHECK_URL("富友网关网银充值订单查询请求地址", "http://www-1.fuiou.com:8888/wg1_run/smpAQueryGate.do"),
    
    QUICK_CHARGE_URL("富友网关快捷支付请求地址", "http://www-1.fuiou.com:8888/wg1_run/dirPayGate.do"),
    
    QUICK_CHARGE_CHECK_URL("富友网关快捷支付订单查询请求地址", "http://www-1.fuiou.com:8888/wg1_run/smpDirPayQueryGate.do"),
    
    QUICK_CHARGE_APP_CHECK_URL("富友网关APP快捷支付订单查询请求地址",
            "http://www-1.fuiou.com:18670/mobile_pay/checkInfo/checkResult.pay"),
    
    PUBLIC_KEY("富友网关快捷支付商户验签公钥",
        "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCn26E6VU4mVfUlsaScZDuPyYTSszoFCS7ctk6K6kO4y6xZrrVSoGhrd6ej1PXa421uqvDEpmrrnZBaJO0y7S/6niWNzwZQ5ajWo929ZH0HrqsD4DENUEyBTj8U9etnxx7J1wZFtPzgHd3FrUSj1RVjpy5QA40ls29KD2DhJU/oFwIDAQAB"),
        
    PRIVATE_KEY("富友网关快捷支付商户私钥",
        "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAJMr8NnRV7ve7Y5FEBium/TsU0fK5NvzvFpsYxPAQhBXY+EN0Bi2JEg790C1njk9Q3U36u2JBDHAiDIomlgh6wWkJsFn7dghV/fCWSX1VVJ+dRINZy1432fRaJ8GqspvMneBpeLjBe94IwlWKpN+AOR+BNX8QL/uHmfCPlVQXos9AgMBAAECgYAzqbMs434m50UBMmFKKNF6kxNRGnpodBFktLO7FTybu/HF6TFp21a1PMe5IYhfk5AAsBZ6OCUOygWFhhdYZN+5W+dweF3kp1rLE4y5CjwqNlk/g22TAndf9znh/ltHFLvITToqu/eh/34tE1gyNxRbsi1olw/1wv8ZRjM3vtM9QQJBANvNwFq+CJHUyFzkXQB7+ycQFnY8wDq8Uw2Hv9ZMjgIntH7FSlJtdu5mAYPPo6f74slO5tFUMNP7EVppqsjYaNkCQQCraD6iKHo+OIlvvYIKiMXatJGD7N1GNhq5CrhUNPWLHwv/Ih2D3JJdF8IUZOPIJfUxTfM2fZYI+EVdsv6s4RcFAkAGjNYbnighOGcUJZYD6q3sVxVkRqEv3ubWs2HrH/Lna4l8caKqXCq8JfwLkod8/QugFiLYwBqIZqX4vMdjHtfZAkBsAl9dbWZCaPvpxp/4JWGPxDLhz9NLV/KU4bVvkoObq++yUHwKyGYOdVcd5MlIKOsNq5Hzp0Vw14lWVuF2bMxFAkBuNrZksvUULNIaWDKd4rQ6GVzUxXuIZW0ZE6atHYDiXPB4jVAjKRtLxZAV1qH9cr1zNJlcg+RbGYUdF9t4A9n5"),
        
    DAIFU_MERID("富友代付 商户代码 富友分配给各合作商户的唯一识别码", "0002900F0345178"),
    
    DAIFU_KEY("富友代付商户秘钥", "123456"),
    
    DAIFU_URL("富友代付请求地址", "http://www-1.fuiou.com:8992/fuMer/req.do"),
    
    CHECK_CARD_URL("富友银行卡信息验证请求地址", "http://www-1.fuiou.com:18670/mobile_pay/checkCard/checkCard01.pay"),
    
    MCHNT_KEY_CHECK_CARD("富友银行卡信息验证商户秘钥", "5old71wihg2tqjug9kkpxnhx9hiujoqj"),
    
    MCHNT_CD_CHECK_CARD("富友网关 富友银行卡信息验证 富友分配给各合作商户的唯一识别码", "0002900F0096235");
    
    private String key;
    
    private String description;
    
    private String value;
    
    FuyouPayVaribles(String description, String value)
    {
        StringBuffer sb = new StringBuffer(this.getType());
        sb.append(".");
        sb.append(this.name());
        this.key = sb.toString();
        this.description = description;
        this.value = value;
    }
    
    @Override
    public String getType()
    {
        return this.getClass().getAnnotation(VariableTypeAnnotation.class).id();
    }
    
    @Override
    public String getKey()
    {
        return this.key;
    }
    
    @Override
    public String getValue()
    {
        return this.value;
    }
    
    @Override
    public String getDescription()
    {
        return this.description;
    }
    
    @Override
    public boolean isInit()
    {
        // TODO Auto-generated method stub
        return true;
    }
    
}
