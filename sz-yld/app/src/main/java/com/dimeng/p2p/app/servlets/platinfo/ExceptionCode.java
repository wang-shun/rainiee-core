package com.dimeng.p2p.app.servlets.platinfo;

public interface ExceptionCode
{
    /**成功*/
    String SUCCESS = "000000";
    
    /**参数传递错误*/
    String PARAMETER_ERROR = "000001";
    
    /**未登录*/
    String NOLOGING_ERROR = "000002";
    
    /**用户名或者密码错误，登录失败*/
    String LOGING_FAIL_ERROR = "000003";
    
    /**未知错误*/
    String UNKNOWN_ERROR = "000004";
    
    /**两次密码不一样*/
    String PASSWORD_DIFFERENT_ERROR = "000005";
    
    /**验证码输入错误*/
    String VERIFY_CODE_ERROR = "000006";
    
    /**注册用户必须先退出登录*/
    String REGISTER_MUST_LOGINOUT_ERROR = "000007";
    
    /**没有查出数据*/
    String NO_DATA_ERROR = "000008";
    
    /**账户不存在*/
    String ACCOUNT_EXISTS_ERROR = "000009";
    
    /**不能和登录密码相同*/
    String SAME_LOGINPASSWORD_ERROR = "000010";
    
    /**交易密码已经存在*/
    String TRANPASSWORD_EXIST_ERROR = "000011";
    
    /**密码不能为空*/
    String PASSWORD_ISEMPTY_ERROR = "000012";
    
    /**俩次密码输入不一*/
    String PASSWORD_NOSAME_ERROR = "000013";
    
    /**交易密码错误*/
    String TRANPASSWORD_ERROR = "000014";
    
    /**未设置交易密码错误*/
    String NO_TRANPASSWORD_ERROR = "000015";
    
    /**自动登录sing信息错误*/
    String AUTO_LOGIN_SIGN_ERRROR = "000016";
    
    /**自动登录time核对错误*/
    String AUTO_LOGIN_TIME_ERRROR = "000017";
    
    /**手机号码错误*/
    String PHONE_NUM_ERRROR = "000018";
    
    /**手机号码已经存在*/
    String PHONE_EXIST_ERRROR = "000019";
    
    /**手机验证码错误*/
    String PHONE_VERIFY_ERRROR = "000020";
    
    /**实名认证失败*/
    String IDCARD_NAME_ERRROR = "000021";
    
    /**实名认证已经存在*/
    String IDCARD_NAME_EXIST_ERRROR = "000022";
    
    /**邮箱地址错误*/
    String EMAIL_ERRROR = "000023";
    
    /**邮箱验证码错误*/
    String EMAIL_VERIFY_ERRROR = "000024";
    
    /**邮箱已经存在*/
    String EMAIL_EXIST_ERRROR = "000025";
    
    /**手机号码不存在*/
    String PHONE_NUMBER_NOEXIST_ERRROR = "000026";
    
    /**验证码类型不能为空*/
    String VERIFY_TYPE_NO_EMPTY_ERRROR = "000027";
    
    /**验证码类型错误*/
    String VERIFY_TYPE_ERRROR = "000028";
    
    /**验证码生成错误*/
    String PRODUCE_VERIFY_ERRROR = "000029";
    
    /**不能和交易密码相同*/
    String SAME_TRANPASSWORD_ERROR = "000030";
    
    /**密码错误 */
    String PASSWORD_ERROR = "000031";
    
    /**充值类型错误！*/
    String CHARGE_TYPE_ERROR = "000032";
    
    /**用户银行卡数量超出最大*/
    String BANK_CARD_COUNT_OVER = "000033";
    
    /**银行卡号错误*/
    String BANK_CARD_NO_ERROR = "000034";
    
    /**银行卡号不存在*/
    String BANK_CARD_NO_EXIST = "000035";
    
    /**银行卡号存在*/
    String BANK_CARD_EXIST = "000036";
    
    /**银行错误*/
    String BANK_ERROR = "000037";
    
    /**未实名认证*/
    String NO_IDCARD_NAME = "000038";
    
    /**金额输入错误*/
    String AMOUNT_ERROR = "000039";
    
    /**银行卡解除绑定失败*/
    String BANK_CARD_UNBIND_FAIL = "000040";
    
    /**企业和机构用户不能登录*/
    String ZRR_LOGIN_FAIL = "000041";
    
    /**找回密码手机号码不能为空*/
    String FIND_PASS_MOBILE_NOTNULL = "000042";
    
    /**找回密码手机号码未注册*/
    String FIND_PASS_MOBILE_NOTREGISTER = "000043";
    
    /**身份验证错误 AuthenticationException*/
    String AUTHENTICATION_ERROR = "000044";
    
    /**注册协议不存在*/
    String REGISTER_AGREEMENT_NO_EMPTY = "000045";
    
    /**您有逾期未还的贷款，还完才能进行投资操作*/
    String YU_QI_EXCEPTION = "000046";
    
    /**您尚未授权还款转账与二次分配转账，不能投资！**/
    String UNAUTHORIZED_EXCEPTION = "000047";
    
    /**账号被锁定**/
    String ACCOUNT_IS_LOCK = "000048";
    
    /**账号被拉黑**/
    String ACCOUNT_IS_BLACK = "000049";
    
    /**加密串错误**/
    String SIGN_IS_ERROR = "000050";
    
    /**不能和原来密码相同*/
    String SAME_PASSWORD_ERROR = "000051";
    
    /**未注册第三方支付*/
    String NO_REGISTER_OTHER_PAY = "000201";
    
    /**第三方支付注册用户url生成错误*/
    String URL_CREATE_ERROR_PAY = "000202";
    
    /**第三方支付充值金额输入错误*/
    String AMOUNT_ERROR_PAY = "000203";
    
    /**手机、邮箱验证码匹配错误最大次数*/
    String PHONE_VERIFYCODE_MAX_ERROR_COUNT = "000052";
    
    /**
     * 排行榜类型错误
     */
    String RANK_TYPE_ERROR = "000053";
    
    /**正在申请贷款*/
    String ALREADY_LOANED_ERROR = "000054";
    
    /**借款期限超出有效天数*/
    String LOAN_OUT_DATE = "000055";
    
    /**借款期限超出有效月数*/
    String LOAN_OUT_MONTH = "000056";
    
    /**添加购物车失败*/
    String ADD_SHOPPING_ERROR = "000057";
    
    /**提现金额不足*/
    String AMOUNT_ERROR_NO_EMPTY = "000204";
    
    /**
     * 托管前缀未配置
     */
    String PREFIX_IS_EMPTY = "000205";
    
    /**
     * 债权持有天数
     */
    String ZAIQUAN_CY_TS = "000206";
    /**
     * 距离最近一个还款日
     */
    String ZAIQUAN_XYGHK_TS = "000207";
    
    /**
     * 微信验签失败
     */
    String WEIXIN_AUTH_ERROR = "000058";
    
    /**
     * 微信账号未绑定平台账号
     */
    String WEIXIN_NO_REGISTER = "000059";
    
    /**
     * 用户名不能和密码相同
     */
    String ACCOUNT_PASSWORD_ERROR = "000060";
    
    /**
     * 另一个账号登录异常
     */
    String OTHER_LOGIN_EXCEPTION = "000061";
    
    /**失败*/
    String ERROR = "000062";
    
    /**法人注册*/
    String COM_REGISTER_ERROR = "000063";

    /**
     * 用户当天超过实名认证错误次数
     */
    String SM_RZCS_EXCEPTION = "000070";
}
