package com.dimeng.p2p.S50.enums;


import com.dimeng.util.StringHelper;

/**
 * 文章类别
 */
public enum T5011_F02 {


    /**
     * 注册与登录
     */
    ZCYDL("注册与登录"),

    /**
     * 充值与提现
     */
    CZYTX("充值与提现"),

    /**
     * 项目风控
     */
    XMFK("项目风控"),

    /**
     * 本息担保
     */
    BXDB("本息担保"),

    /**
     * 账户与安全
     */
    ZHYAQ("账户与安全"),

    /**
     * 投资与回款
     */
    TZYHK("投资与回款"),

    /**
     * 合作伙伴
     */
    HZHB("合作伙伴"),

    /**
     * 备用金垫付文本
     */
    BYJDFWB("备用金垫付文本"),

    /**
     * 过程透明
     */
    GCTM("过程透明"),

    /**
     * 法律合规
     */
    FLHG("法律合规"),

    /**
     * 资金安全
     */
    ZJAQ("资金安全"),

    /**
     * 平台账户
     */
    PTZH("平台账户"),

    /**
     * 我要理财
     */
    WYLC("我要理财"),

    /**
     * 我要借款
     */
    WYJK("我要借款"),

    /**
     * 利息和费用
     */
    LXHFY("利息和费用"),

    /**
     * 平台介绍
     */
    PTJS("平台介绍"),

    /**
     * 名词解释
     */
    MCJS("名词解释"),

    /**
     * 新手指引
     */
    XSZY("新手指引"),

    /**
     * 安全保障
     */
    AQBZ("安全保障"),

    /**
     * 联系我们
     */
    LXWM("联系我们"),

    /**
     * 招贤纳士
     */
    ZXNS("招贤纳士"),

    /**
     * 合作机构
     */
    HZJG("合作机构"),

    /**
     * 专家顾问
     */
    ZJGW("专家顾问"),

    /**
     * 公司简介
     */
    GSJJ("公司简介"),

    /**
     * 管理团队
     */
    GLTD("管理团队"),

    /**
     * 社会责任
     */
    SHZR("社会责任"),

    /**
     * 媒体报道
     */
    MTBD("媒体报道"),

    /**
     * 互联网金融研究
     */
    HLWJRYJ("互联网金融研究"),

    /**
     * 担保模式
     */
    DBMS("担保模式"),

    /**
     * 融租模式
     */
    RZMS("融租模式"),

    /**
     * 债权模式
     */
    ZQMS("债权模式"),

    /**
     * 合作金融机构
     */
    HZJRJG("合作金融机构"),

    /**
     * 信审风控体系
     */
    XSFKTX("信审风控体系"),

    /**
     * CFCA战略合作
     */
    CFCAZLHZ("CFCA战略合作"),

    /**
     * 政策法规保障
     */
    ZCFGBZ("政策法规保障"),

    /**
     * 公司资质
     */
    GSZZ("公司资质"),

    /**
     * 网贷行业资讯
     */
    WDHYZX("网贷行业资讯"),

    /**
     * 公司动态
     */
    GSDT("公司动态"),

    /**
     * 合同确认
     */
    HTQR("合同确认"),

    /**
     * 发标公告
     */
    FBGG("发标公告"),

    /**
     * 取现演示
     */
    QXYS("取现演示"),

    /**
     * 转让操作演示
     */
    ZRCZYS("转让操作演示"),

    /**
     * 常见问题Q&A
     */
    CJWTQA("常见问题Q&A"),
    /**
     * 最新动态
     */
    ZXDT("最新动态"),
    /**
     * 备案信息
     */
    BAXX("备案信息"),

    /**
     * 组织信息
     */
    ZZXX("组织信息"),

    /**
     * 审计报告
     */
    SJBG("审计报告"),

    /**
     * 运营报告
     */
    YYBG("运营报告"),

    /**
     * 收费标准
     */
    SFBZ("收费标准"),
    /**
     * 重大事项
     */
    QTXX("重大事项"),
    /**
     * 其他信息
     */
    ZDSX("其他信息");

    protected final String chineseName;

    private T5011_F02(String chineseName){
        this.chineseName = chineseName;
    }
    /**
     * 获取中文名称.
     *
     * @return {@link String}
     */
    public String getChineseName() {
        return chineseName;
    }
    /**
     * 解析字符串.
     *
     * @return {@link com.dimeng.p2p.S50.enums.T5011_F02}
     */
    public static final T5011_F02 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T5011_F02.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
