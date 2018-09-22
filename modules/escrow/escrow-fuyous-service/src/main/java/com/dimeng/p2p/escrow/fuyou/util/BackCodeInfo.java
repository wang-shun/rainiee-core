package com.dimeng.p2p.escrow.fuyou.util;

/**
 * 富友托管返回状态码
 * 
 * @author heshiping
 *
 */
public class BackCodeInfo
{
    
    /**
     * 托管信息反馈
     * @param number
     *  原因码
     * @return String
     */
    public static String info(String resp_code)
    {
        String info = "";
        
        switch (resp_code)
        {
            case "0000":
                info = "交易成功";
                break;
            case "1000":
            case "3120":
            case "1009":
            case "4003":
            case "4006":
            case "4007":
            case "4008":
            case "4012":
            case "5138":
            case "9005":
            case "9006":
            case "9007":
            case "9008":
            case "9009":
            case "9010":
            case "9011":
            case "9012":
            case "9013":
            case "9014":
            case "9015":
            case "9016":
            case "9701":
            case "9801":
            case "9802":
            case "9803":
            case "9804":
            case "9805":
            case "9806":
            case "9901":
            case "9902":
            case "210001":
            case "210002":
            case "9000":
            case "9001":
            case "9002":
                info = "系统异常";
                break;
            case "1001":
                info = "无此用户";
                break;
            case "1002":
                info = "用户未激活";
                break;
            case "1003":
                info = "用户已锁定";
                break;
            case "1004":
            case "1005":
            case "1006":
                info = "用户状态异常";
                break;
            case "1007":
                info = "未知用户";
                break;
            case "1008":
                info = "实名信息不正确";
                break;
            case "1014":
                info = "无效卡号(无此卡号)";
                break;
            case "1042":
                info = "无此账户";
                break;
            case "1051":
                info = "资金不足";
                break;
            case "1101":
                info = "无此商户";
                break;
            case "1102":
                info = "商户已关闭";
                break;
            case "1103":
                info = "商户已锁定";
                break;
            case "2000":
                info = "正常账户";
                break;
            case "2001":
                info = "无此账户";
                break;
            case "2002":
                info = "账户未激活";
                break;
            case "2003":
                info = "账户已锁定";
                break;
            case "2004":
                info = "账户已冻结";
                break;
            case "2005":
            case "2006":
            case "2007":
            case "2008":
            case "2010":
            case "2011":
            case "2012":
            case "2013":
            case "2014":
            case "2015":
            case "2016":
            case "2017":
            case "2018":
                info = "账户状态异常";
                break;
            case "2019":
                info = "交易币种不符";
                break;
            case "2020":
                info = "暂不支持";
                break;
            case "2030":
                info = "贷记账户状态正常";
                break;
            case "2031":
                info = "无此贷记账户";
                break;
            case "2032":
                info = "贷记账户未激活";
                break;
            case "2033":
                info = "贷记账户已锁定";
                break;
            case "2034":
                info = "贷记账户已冻结";
                break;
            case "2035":
                info = "贷记账户已销户";
                break;
            case "2036":
                info = "贷记账户已过期";
                break;
            case "2037":
                info = "贷记账户已挂失";
                break;
            case "2038":
                info = "贷记账户状态不正常";
                break;
            case "2039":
                info = "借贷记账记不属于同一个机构";
                break;
            case "2040":
            case "2041":
            case "2101":
            case "2102":
                info = "怀疑作弊";
                break;
            case "2103":
            case "2104":
            case "2105":
            case "3003":
                info = "安全验证错误";
                break;
            case "2106":
                info = "支付密码错误";
                break;
            case "2107":
                info = "查询密码错误次数超限";
                break;
            case "2108":
                info = "支付密码错误次数超限";
                break;
            case "2109":
                info = "格式错";
                break;
            case "3001":
            case "3002":
            case "3004":
            case "3005":
            case "3006":
            case "3007":
                info = "功能暂不支持";
                break;
            case "3011":
                info = "交易金额太大";
                break;
            case "3012":
                info = "金额无效";
                break;
            case "3013":
            case "3014":
            case "3015":
            case "3016":
            case "3017":
            case "3018":
            case "3020":
            case "3021":
            case "3023":
                info = "余额不足";
                break;
            case "3022":
            case "3019":
                info = "交易金额太小";
                break;
            case "3024":
                info = "金额太大";
                break;
            case "3101":
                info = "找不到原交易";
                break;
            case "3102":
                info = "原交易不成功";
                break;
            case "3103":
                info = "原交易已冲正";
                break;
            case "3104":
                info = "原交易已撤消";
                break;
            case "3105":
                info = "原交易已完成";
                break;
            case "3106":
                info = "原交易已冻结";
                break;
            case "3107":
                info = "原交易已解冻";
                break;
            case "3108":
                info = "原交易金额不符";
                break;
            case "3109":
                info = "原交易账号不符";
                break;
            case "3110":
                info = "找不到原始授权交易";
                break;
            case "3111":
            case "3112":
                info = "原交易状态异常";
                break;
            case "3121":
                info = "商户不匹配";
                break;
            case "3122":
                info = "交易已完成";
                break;
            
            case "3123":
                info = "卡号不匹配";
                break;
            case "3124":
                info = "金额超限";
                break;
            case "3125":
                info = "金额不一致";
                break;
            case "3126":
                info = "终端号不一致";
                break;
            case "3127":
                info = "隔日交易，无法完成";
                break;
            case "3201":
                info = "记账失败";
                break;
            case "3251":
                info = "提现账户未指定";
                break;
            case "3252":
                info = "商户信息不完整";
                break;
            case "3253":
                info = "格式错";
                break;
            case "3271":
            case "3272":
                info = "交易超时";
                break;
            case "4001":
            case "4002":
            case "4004":
            case "4005":
                info = "不允许开此账户";
                break;
            case "4009":
            case "4010":
            case "4011":
                info = "不支持的开户方式";
                break;
            case "4013":
                info = "不可重复开户";
                break;
            case "5001":
                info = "session超时";
                break;
            case "5002":
                info = "验签失败";
                break;
            case "5110":
                info = "用户名或密码错误";
                break;
            case "5017":
                info = "未做任何修改";
                break;
            case "5018":
                info = "根据地区代码和行别找不到对应支行";
                break;
            case "5019":
                info = "数据校验失败";
                break;
            case "5029":
                info = "调用交易查询接口过于频繁";
                break;
            case "5137":
                info = "账户信息不能修改";
                break;
            case "5239":
                info = "商户不存在";
                break;
            case "5343":
            case "5891":
                info = "用户已开户";
                break;
            case "5344":
                info = "账务系统开户失败";
                break;
            case "5345":
                info = "商户流水号重复";
                break;
            case "5346":
                info = "商户流水号不存在";
                break;
            case "5347":
                info = "与商户系统日期不一致";
                break;
            case "5348":
                info = "交易用户不存在";
                break;
            case "5349":
                info = "找不到原交易";
                break;
            case "5350":
                info = "指令提交模式只支持富友余额支付";
                break;
            case "5351":
                info = "商户提现流水号重复";
                break;
            case "5352":
                info = "未找到该商户交易记录";
                break;
            case "5353":
                info = "接收FAS报文出现异常";
                break;
            case "5354":
                info = "FAS报文验签失败";
                break;
            case "5355":
                info = "发送FAS通讯出现异常";
                break;
            case "5356":
                info = "该卡号已认证";
                break;
            case "5357":
                info = "该卡号已经受理且认证通过";
                break;
            case "5358":
                info = "该卡号已经受理,但尚未认证通过";
                break;
            case "5359":
                info = "该卡号尚未签约";
                break;
            case "5460":
                info = "发送日切通知失败";
                break;
            case "5850":
                info = "已经存在相同银行卡号注册的用户";
                break;
            case "5854":
                info = "协议库验证日期超过3天";
                break;
            case "5851":
                info = "已经存在相同证件号注册的用户";
                break;
            case "5852":
                info = "实名验证失败";
                break;
            case "5836":
                info = "不允许信用卡注册";
                break;
            case "5837":
                info = "卡号和行别不一致";
                break;
            case "9003":
                info = "机构已关闭";
                break;
            case "9004":
                info = "机构状态异常";
                break;
            case "10029":
                info = "金额超限";
                break;
            case "100011":
                info = "卡号或者户名不符";
                break;
            case "5839":
                info = "未知错误";
                break;
            default:
                info = "系统忙";
        }
        
        return ("返回码：" + resp_code + "-原因：" + info);
    }
    
}
