package com.dimeng.p2p.variables.defines;

import java.io.InputStreamReader;

import com.dimeng.framework.config.VariableTypeAnnotation;
import com.dimeng.framework.config.entity.VariableBean;

@VariableTypeAnnotation(id = "URL", name = "链接地址")
public enum URLVariable implements VariableBean {

	/**
	 * 网站首页地址
	 */
	INDEX("网站首页地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}";
		}
	},
	/**
	 * 用户中心地址
	 */
	USER_INDEX("用户中心地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/user/";
		}
	},
	/**
	 * 用户实名认证地址
	 */
	USER_NCIIC("非个人用户实名认证地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/user/account/safetymsgFZRR.html";
		}
	},
	/**
	 * 个人用户实名认证地址
	 */
	USER_ZRR_NCIIC("个人用户实名认证地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/user/account/userBases.html?userBasesFlag=1";
		}
	},
	/**
	 * 个人基础信息地址
	 */
	USER_BASES("个人基础信息地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/user/account/userBases.html";
		}
	},
	/**
	 * 机构/企业安全信息地址
	 */
	COM_FZRR("机构/企业安全信息") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/user/account/safetymsgFZRR.html";
		}
	},
	/**
	 * 站内信链接地址
	 */
	USER_LETTER("站内信链接地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/user/letter/";
		}
	},
	/**
	 * 还款详情地址
	 */
	USER_LOAN_DETAIL("还款详情地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/user/credit/loanDetail.html";
		}
	},
	/**
	 * 用户充值链接地址
	 */
	USER_CHARGE("用户充值链接地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/user/capital/charge.html";
		}
	},
	/**
	 * 用户提现链接地址
	 */
	USER_WITHDRAW("用户提现链接地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/user/capital/withdraw.html";
		}
	},
	/**
	 * 用户充值通知链接地址
	 */
	USER_TRADINGRECORD("用户充值通知链接地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/user/capital/tradingRecord.html";
		}
	},
	/**
	 * 帮助中心充值与提现连接地址
	 */
	FRONT_BZZX_CZYTX("帮助中心充值与提现连接地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/xszy/czytx.html";
		}
	},
	/**
	 * 第三方用户充值链接地址
	 */
	AGENCY_CHARGE("第三方用户充值链接地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/agency/capital/charge.html";
		}
	},
	/**
	 * 提现审核通过，放款
	 */
	WITHDRAW_FK("提现审核通过，放款") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/pay/withdraw.htm";
		}
	},

	/**
	 * 用户资金管理链接地址
	 */
	USER_CAPITAL("用户资金管理链接地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/user/capital/tradingRecord.html";
		}
	},
	/**
	 * 用户投资管理链接地址
	 */
	USER_FINANCING("用户投资管理链接地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/user/financing/wdzq/hszdzq.html";
		}
	},
	/**
	 * 用户贷款管理链接地址
	 */
	USER_CREDIT("用户贷款管理链接地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/user/credit/repaying.html";
		}
	},

	/**
	 * 支付地址入口
	 */
	PAY_INDEX("支付地址入口") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/pay/";
		}
	},
	/**
	 * 充值地址入口
	 */
	PAY_CHARGE("充值地址入口") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/pay/charge.htm";
		}
	},
	/**
	 * 用户中心线下充值地址入口
	 */
	PAY_CHARGE4Line("用户中心线下充值地址入口") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/pay/charge4Line.htm";
		}
	},
	/**
	 * 订单查询入口
	 */
	PAY_CHECK("订单查询入口") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/pay/check.htm";
		}
	},
	/**
	 * 投资入口地址
	 */
	PAY_BID_URL("投资入口地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/pay/bid.htm";
		}
	},
	/**
	 * 在线客服页面地址
	 */
	CUSTOMERS("在线客服页面地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/xszy/zxkf.html";
		}
	},
	/**
	 * 帮助中心页面地址
	 */
	HELP_CENTER("帮助中心页面地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/helpcenter/index.html";
		}
	},
	/**
	 * 我要理财产品介绍
	 */
	FINANCING_CENTER("我要理财产品介绍") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/financing/";
		}
	},
	/**
	 * 中小企理财
	 */
	FINANCING_ZXQ_TB("中小企理财") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/financing/sbtz/tzzq.html";
		}
	},
	/**
	 * 优选理财页面地址
	 */
	FINANCING_YX("优选理财页面地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/financing/yxlc/";
		}
	},
	/**
	 * 散标投资页面地址
	 */
	FINANCING_SBTZ("散标投资页面地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/financing/sbtz/";
		}
	},
	/**
	 * 理财频道产品介绍页面地址
	 */
	FINANCING_LCPD("理财频道产品介绍页面地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/lcpd/index.html";
		}
	},
	/**
	 * 散标投资个人列表地址
	 */
	FINANCING_SBTZ_GR("散标投资个人列表地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/financing/sbtz/grlb.html";
		}
	},
	/**
	 * 债权转让页面地址
	 */
	FINANCING_ZQZR("债权转让页面地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/financing/sbtz/index.html?btype=3";
		}
	},

	/**
	 * 个人标页面地址
	 */
	FINANCING_GRB("个人标页面地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/financing/sbtz/grlb.html";
		}
	},

	/**
	 * 企业标页面地址
	 */
	FINANCING_QYB("企业标页面地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/financing/sbtz/index.html";
		}
	},

	/**
	 * 优选理财页面地址
	 */
	FINANCING_YX_XQ("优选理财详情页面地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/financing/yxlc/index/";
		}
	},
	/**
	 * 散标投资页面地址
	 */
	FINANCING_SBTZ_XQ("散标投资详情页面地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/financing/sbtz/bdxq/";
		}
	},
	/**
	 * 线下债权转让页面地址
	 */
	FINANCING_XXZQ_XQ("线下债权转让详情页面地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/financing/xxzq/bdxq/";
		}
	},
	/**
	 * 债权转让详情页面地址
	 */
	FINANCING_ZQZR_XQ("债权转让详情页面地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/financing/sbtz/zqxq/";
		}
	},
	/**
	 * 还款黑名单页面地址
	 */
	FINANCING_HKHMD("还款黑名单页面地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/financing/hmd/";
		}
	},
	/**
	 * 我要借款产品介绍页面地址
	 */
	CREDIT_CENTER("我要借款产品介绍页面地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/credit/";
		}
	},
	/**
	 * 信用贷页面地址
	 */
	CREDIT_XJD("信用贷页面地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/credit/xjd/";
		}
	},
	/**
	 * 信用贷申请地址
	 */
	CREDIT_XJD_SQ("信用贷申请地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/credit/xjd/xjd.html";
		}
	},
	/**
	 * 担保贷页面地址
	 */
	GUARANTEE_DBD("担保贷页面地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/credit/dbd/";
		}
	},
	/**
	 * 担保贷申请地址
	 */
	GUARANTEE_DBD_SQ("担保贷申请地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/credit/dbd/dbd.html";
		}
	},
	/**
	 * 生意贷页面地址
	 */
	CREDIT_SYD("生意贷页面地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/credit/syd/";
		}
	},
	/**
	 * 生意贷申请页面地址
	 */
	CREDIT_SYD_SQ("生意贷申请页面地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/credit/syd/syd.html";
		}
	},
	/**
	 * 个人贷款意向页面地址
	 */
	CREDIT_DKYX("个人贷款意向页面地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/credit/dkyx.html";
		}
	},
	/**
	 * 企业贷款意向页面地址
	 */
	CREDIT_QYDKYX("企业贷款意向页面地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/credit/qydkyx.html";
		}
	},
	/**
	 * 登录页面地址
	 */
	LOGIN("登录页面地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/user/login.html";
		}
	},
	/**
	 * 用户信息修改地址
	 */
	USER_INFO_UPDATE("用户信息修改地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/user/user/userRevise.html";
		}
	},
	/**
	 * 用户信息查询地址
	 */
	USER_INFO_QUERY("用户信息查询地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/user/userQuery.html";
		}
	},
	/**
	 * 冻结地址
	 */
	FUYOU_FREEZE_PAY_URL("冻结地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/pay/freeze.htm";
		}
	},
	/**
	 * 解冻地址
	 */
	FUYOU_UNFREEZE_PAY_URL("解冻地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/pay/unFreeze.htm";
		}
	},
	/**
	 * 注册页面地址
	 */
	REGISTER("注册页面地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/register.html";
		}
	},
	/**
	 * 微信用户注册地址
	 */
	WX_REGISTER("微信注册页面地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/app/register.jsp";
		}
	},
	/**
	 * 用户注册提交
	 */
	REGISTER_SUBMIT("用户注册提交") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/pay/register.htm";
		}
	},
	/**
	 * 企业注册提交
	 */
	QYREGISTER_SUBMIT("企业注册提交") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/pay/qyRegister.htm";
		}
	},

	/**
	 * 余额查询入口 add by lxl
	 */
	QUERY_BLANCE("余额查询入口") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/pay/userBlance.htm";
		}
	},

	/**
	 * 明细查询入口 add by lxl
	 */
	QUERY_ACCOUNT_DETAIL("明细查询入口") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/pay/userAccountDetail.htm";
		}
	},

	/**
	 * 转账预冻结入口 add by lxl
	 */
	TRANSFER_BMU_FREEZE("转账预冻结入口") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/pay/transferBmuAndFreeze.htm";
		}
	},

	/**
	 * 划拨预冻结入口 add by lxl
	 */
	TRANSFER_BU_FREEZE("划拨预冻结入口") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/pay/transferBuAndFreeze.htm";
		}
	},
	/**
	 * 找回密码页面地址
	 */
	GET_PASSWORD("找回密码页面地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/password/";
		}
	},
	/**
	 * 常见问题页面地址
	 */
	CJ_WT("常见问题页面地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/xszy/cjwt.html";
		}
	},
	/**
	 * 公司简介页面地址
	 */
	GYWM_GSJJ("公司简介页面地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/gywm/index.html?type=GSJJ";
		}
	},
	/**
	 * 信息披露页面地址
	 */
	XXPL_BAXX("信息披露页面地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/xxpl/baxx.html";
		}
	},
    
    /**
     * 信息披露-平台数据展示
     */
    XXPL_BAXX_YYSJ("信息披露-平台数据展示")
    {
        @Override
        public String getValue()
        {
            return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/xxpl/yysj.html";
        }
    },
	/**
	 * 联系我们页面地址
	 */
	GYWM_LXWM("联系我们页面地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/gywm/index.html?type=LXWM";
		}
	},
	/**
	 * 管理团队地址
	 */
	GYWM_GLTD("管理团队地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/gywm/index.html?type=GLTD";
		}
	},
	/**
	 * 专家顾问地址
	 */
	GYWM_ZJGW("专家顾问地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/gywm/index.html?type=ZJGW";
		}
	},
	/**
	 * 招贤纳士页面地址
	 */
	GYWM_ZXNS("招贤纳士页面地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/gywm/index.html?type=ZXNS";
		}
	},
	/**
	 * 媒体报道页面地址
	 */
	ZXDT_MTBD("最新动态-媒体报道页面地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/zxdt/index.html?type=MTBD";
		}
	},
	/**
	 * 社会责任页面地址
	 */
	ZXDT_SHZR("最新动态-社会责任页面地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/zxdt/index.html?type=SHZR";
		}
	},
	/**
	 * 网站公告
	 */
	ZXDT_WZGG("最新动态-网站公告页面地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/zxdt/index.html";
		}
	},
	/**
	 * 网贷行业资讯
	 */
	ZXDT_WDHYZX("最新动态-网贷行业资讯页面地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/zxdt/index.html?type=WDHYZX";
		}
	},
	/**
	 * 互联网金融研究
	 */
	ZXDT_HLWJRYJ("互联网金融研究页面地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/zxdt/index.html?type=HLWJRYJ";
		}
	},
	/**
	 * 散标投资地址
	 */
	TB_SBTZ("散标投资") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/user/bid/sbtz.htm";
		}
	},
	/**
	 * 优选理财地址
	 */
	TB_YXLC("优选理财") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/pay/financialPurchase.htm";
		}
	},
	/**
	 * 债权转让
	 */
	TB_ZQZR("债权转让") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/user/bid/zqzr.htm";
		}
	},
	/**
	 * 线下债权转让
	 */
	TB_XXZQ("线下债权转让") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/user/bid/xxzq.htm";
		}
	},
	/**
	 * 投资中的债权
	 */
	USER_ZQTBZ("我的债权") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/user/financing/wdzq/tbzdzq.html";
		}
	},
	/**
	 * 持有中的优选理财
	 */
	USER_YXLC("优选理财") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/user/financing/yxlc/yxlccyz.html";
		}
	},
	/**
	 * 申请中的优选理财
	 */
	USER_YXLC_SQZ("优选理财申请中") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/user/financing/yxlc/yxlcsqz.html";
		}
	},
	/**
	 * 已转入的债权
	 */
	USER_ZQYZR("已转入的债权") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/user/financing/zqzr/zqyzr.html";
		}
	},
	/**
	 * 借款申请查询
	 */
	USER_JKSQCX("借款申请查询") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/user/credit/apply.htm";
		}
	},
	/**
	 * 等级说明
	 */
	XSZY_DJSM("等级说明") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/xszy/djsm.html";
		}
	},
	/**
	 * 身份证示例
	 */
	SL_SFZZL("身份证示例") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/credit/sl/sfzsl.html";
		}
	},
	/**
	 * 身份证正面头部
	 */
	SL_SFZZL2("身份证正面头部") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/credit/sl/sfzsl2.html";
		}
	},
	/**
	 * 学历样本01
	 */
	SL_XLYB1("学历样本01") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/credit/sl/xlyb1sl.html";
		}
	},
	/**
	 * 学历样本02
	 */
	SL_XLYB2("学历样本02") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/credit/sl/xlyb2sl.html";
		}
	},
	/**
	 * 结婚示例
	 */
	SL_JHSL("结婚示例") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/credit/sl/jhsl.html";
		}
	},
	/**
	 * 收入认证
	 */
	SL_SRSL("收入认证") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/credit/sl/srsl.html";
		}
	},
	/**
	 * 信用报告
	 */
	SL_XYBG("信用报告") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/credit/sl/xybgsl.html";
		}
	},
	/**
	 * 房产示例
	 */
	SL_FCSL("房产示例") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/credit/sl/fcsl.html";
		}
	},
	/**
	 * 车辆行驶证
	 */
	SL_GCSL("车辆行驶证") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/credit/sl/gcsl.html";
		}
	},
	/**
	 * 和车辆合影
	 */
	SL_GCHYSL("和车辆合影") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/credit/sl/gchysl.html";
		}
	},
	/**
	 * 本息保障计划
	 */
	XY_BXBZ("本息保障计划") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/term/BXBZ.html";
		}
	},
	/**
	 * 注册协议
	 */
	XY_ZCXY("注册协议") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/term/ZCXY.html";
		}
	},
	/**
	 * 债权转让及受让协议
	 */
	XY_ZQZRXY("债权转让及受让协议") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/term/ZQZRXY.html";
		}
	},
	/**
	 * 优选理财协议范本
	 */
	XY_YXLCXY("优选理财协议范本") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/term/YXLCXY.html";
		}
	},
	/**
	 * 借款协议(实)
	 */
	XY_SDRZBXY("借款协议(实)") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/term/SDRZBXY.html";
		}
	},
	/**
	 * 借款协议(信)
	 */
	XY_XYRZBXY("借款协议(信)") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/term/XYRZBXY.html";
		}
	},
	/**
	 * 借款协议(担保)
	 */
	XY_DBBXY("借款协议(担保)") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/term/DBBXY.html";
		}
	},
	/**
	 * 借款协议(实、保)
	 */
	JKXYSB("借款协议(实、保)") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/term/JKXYSB.html";
		}
	},
	/**
	 * 债权转让说明书
	 */
	ZQZRSMS("债权转让说明书") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/term/ZQZRSMS.html";
		}
	},
	/**
	 * 理财计算器
	 */
	LC_LCJSQ("理财计算器") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/credit/lcjsq.html";
		}
	},

	/**
	 * 借款计算器
	 */
	JK_JKJSQ("借款计算器") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/credit/jkjsq.html";
		}
	},
	/**
	 * 平台电子协议地址
	 */
	XY_PTDZXY("平台电子协议地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/user/financing/agreement/index.htm";
		}
	},
	/**
	 * 平台后台电子协议地址
	 */
	XY_PTHTDZXY("平台后台电子协议地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/console/bid/htgl/agreement/index.htm";
		}
	},
	/**
	 * 人信用融资意向页面地址
	 */
	CREDIT_GRXYRZ("个人信用融资意向页面地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/credit/rzyx/grxy.html";
		}
	},
	/**
	 * 个人担保融资意向页面地址
	 */
	CREDIT_GRDBRZ("个人担保融资意向页面地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/credit/rzyx/grdb.html";
		}
	},
	/**
	 * 企业信用融资意向页面地址
	 */
	CREDIT_QYXYRZ("企业信用融资意向页面地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/credit/rzyx/qyxy.html";
		}
	},
	/**
	 * 企业贷款意向页面地址
	 */
	CREDIT_QYDBRZ("企业担保融资意向页面地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/credit/rzyx/qydb.html";
		}
	},
	/**
	 * 我要找项目
	 */
	CREDIT_TZYX("我要找项目") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/credit/rzyx/wyzxm.html";
		}
	},
	/**
	 * 新浪微博关注
	 */
	XLWBGZ("新浪微博关注") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}";
		}
	},
	/**
	 * 银行卡管理
	 */
	CARD_MANAGE("银行卡管理") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/user/account/bankcardlist.html";
		}
	},
	/**
	 * 第三方托管，用户注册地址
	 */
	ESCROW_URL_USERREGISTER("第三方托管，用户注册地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/user/capital/fuyouPaySign.htm";
		}
	},
	/**
	 * 第三方托管，提现
	 */
	ESCROW_URL_WITHDRAW("第三方托管，提现") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/pay/withdraw.htm";
		}
	},
	/**
	 * 第三方托管，绑定银行卡
	 */
	ESCROW_URL_BINDCARD("第三方托管，绑定银行卡") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/pay/bindCard.htm";
		}
	},
	/**
	 * 第三方托管，解除银行卡绑定
	 */
	ESCROW_URL_UNBINDCARD("第三方托管，解除银行卡绑定") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/pay/unbindCard.htm";
		}
	},
	/**
	 * 第三方托管，债权转让
	 */
	ESCROW_URL_EXCHANGE("第三方托管，债权转让") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/pay/bidExchange.htm";
		}
	},

	/**
	 * 第三方托管，手机号码修改
	 */
	ESCROW_URL_RESETMOBILE("第三方托管，手机号码修改") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/pay/yeepay/resetMobileServlet.htm";
		}
	},
	/**
	 * 第三发托管，交易密码重置
	 */
	ESCROW_URL_RESETPASSWORD("第三方托管，交易密码重置") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/pay/yeepay/resetPasswordServlet.htm";
		}
	},
	/**
	 * 担保模式页面地址
	 */
	YWMS_URL_DBMS("担保模式页面地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/ywms/dbms.html";
		}
	},
	/**
	 * 融租模式页面地址
	 */
	YWMS_URL_RZMS("融租模式页面地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/ywms/rzms.html";
		}
	},
	/**
	 * 债权模式页面地址
	 */
	YWMS_URL_ZQMS("债权模式页面地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/ywms/zqms.html";
		}
	},
	/**
	 * 公司新浪微博地址
	 */
	COMPANY_SINA_URL("公司新浪微博地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}";
		}
	},
	/**
	 * 公司腾讯微博地址
	 */
	COMPANY_TECENT_URL("公司腾讯微博地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}";
		}
	},
	/**
	 * 公司QQ空间地址
	 */
	COMPANY_QQZONE_URL("公司QQ空间地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}";
		}
	},
	/**
	 * 公司微信地址
	 */
	COMPANY_WECHAT_URL("公司微信地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}";
		}
	},
	/**
	 * 自动投资地址
	 */
	USER_AUTO_BID("自动投资地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/user/financing/zdtb/index.html";
		}
	},
	/**
	 * 理财统计
	 */
	USER_FINANCING_STATISTICS("理财统计") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/user/financing/financingStatistics.html";
		}
	},
	/**
	 * 网站手机端访问地址
	 */
	SITE_PHONE_URL("网站手机端访问地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/";
		}
	},
	/**
	 * 新手引导：利息和费用
	 */
	XSYD_PROFITS_AND_FEES("新手引导：利息和费用") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/xszy/lxhfy.html";
		}
	},

	/**
	 * 新手引导页
	 */
	XSYD_INDEX("新手引导页") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/xszy/";
		}
	},
	/**
	 * 利息和费用
	 */
	XSZY_LXHFY("利息和费用页") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/xszy/lxhfy.html";
		}
	},
	/**
	 * 平台介绍
	 */
	XSZY_PTJS("平台介绍页") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/xszy/ptjs.html";
		}
	},
	/**
	 * 合作机构
	 */
	XSZY_HZJG("合作机构页") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/xszy/hzjg.html";
		}
	},
	/**
	 * 首页购买转让债权地址
	 */
	ZQZR_FOR_INDEX("首页购买转让债权地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/user/bid/zqzrForIndex.htm";
		}
	},
	/**
	 * 新手引导：平台介绍
	 */
	XSYD_PTJS("新手引导：平台介绍") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/xszy/ptjs.html";
		}
	},
	/**
	 * 本息保障
	 */
	AQBZ_BXDB("本息保障") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/aqbz/index.html?type=BXDB";
		}

	},
	/**
	 * 新手专享
	 */
	INDEX_XSZX("新手专享") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/financing/sbtz/xszx.html";
		}
	},
	/**
	 * 协议条款
	 */
	XSYD_XYTK("协议条款") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/xszy/xytk.htm";
		}
	},
	/**
	 * 充值与提现
	 */
	XSYD_CZYTX("充值与提现") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/xszy/czytx.htm";
		}
	},
	/**
	 * 投资与回款
	 */
	XSYD_TZYHK("投资与回款") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/xszy/tzyhk.htm";
		}
	},
	/**
	 * 账户与安全
	 */
	XSYD_ZHYAQ("账户与安全") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/xszy/zhyaq.htm";
		}
	},

	/**
	 * 接口测试地址访问页面 add by lxl
	 */
	TEST_INTERFACE("接口测试地址访问页面") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/test.html";
		}
	},

	/**
	 * 余额查询接口地址成功页面 add by lxl
	 */
	QUERY_BLANCE_SUCC("余额查询接口地址成功页面") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/user/succ.html";
		}
	},

	/**
	 * 新手指引-投资指引
	 */
	XSZY_TZZY("新手指引-投资指引") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/xszy/tzzy.htm";
		}

	},

	/**
	 * 新手指引-融资指引
	 */
	XSZY_RZZY("新手指引-融资指引") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/xszy/rzzy.htm";
		}

	},

	/**
	 * 我的推广
	 */
	WDTG("我的推广") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/user/spread/wdtg.html";
		}

	},

	/**
	 * 我要推广
	 */
	WYTG("我要推广") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/user/spread/wytg.html";
		}

	},

	AQRZ("安全认证页面") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/user/account/safetymsg.html";
		}
	},

	/**
	 * 注册与登录
	 */
	XSYD_ZCYDL("注册与登录") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/xszy/zcydl.htm";
		}
	},
	/**
	 * 用户认证信息地址
	 */
	USER_RZXX("认证信息") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/user/account/userBases.html?userBasesFlag=6";
		}
	},
	/**
	 * APP下载链接
	 */
	APP_DOWNLOAD("APP下载链接") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/app/downloadApp.htm";
		}
	},
	/**
	 * 投资攻略页面地址
	 */
	INDEX_TZGL("投资攻略") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/bzzx/index.html?type=XSZY";
		}
	},
	/**
	 * 我的体验金
	 */
	MY_EXPERIENCE("我的体验金") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/user/spread/wdtyj.html";
		}
	},
	/**
	 * 公益捐赠页面地址
	 */
	FINANCING_GYJZ("公益捐赠页面地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/financing/gyb/gyJz.html";
		}
	},
	/**
	 * 公益标投资入口地址
	 */
	PAY_BID_URL_4_GYB("公益标投资入口地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/pay/donationBid.htm";
		}
	},
	/**
	 * 我的公益标路径（公益标捐赠成功后跳转使用）
	 */
	MYGYB("我的公益标路径（公益标捐赠成功后跳转使用）") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/user/financing/donation/donationList.html";
		}
	},
	/**
	 * 不良资产处理页面地址
	 */
	PTDF_URL("不良资产处理") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/console/finance/zjgl/dfgl/yqddfList.htm";
		}
	},

	/**
	 * 公益标标详情
	 */
	GYB_BDXQ("公益标标详情") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/financing/gyb/gybXq/";
		}
	},
	/**
	 * 验证交易密码地址
	 */
	CHECK_TXPWD("验证交易密码地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/user/bid/checkTxPwd.htm";
		}
	},

	/**
	 * 实名验证引导页
	 */
	REALNAME_GUIDE("实名验证引导页") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/user/guide/realNameAuth.htm";
		}

	},

	/**
	 * QQ登录注册页回调地址
	 */
	REGISTER_QQ_LOGIN("QQ登录注册页回调地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/associatedRegister.html";
		}

	},
	/**
	 * 首页QQ登录回调地址
	 */
	INDEX_QQ_LOGIN("首页QQ登录回调地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/index.html";
		}

	},
	/**
	 * 手动还款地址
	 */
	PAY_PAYMENT_URL("手动还款地址") {
		@Override
		public String getValue() {
			return "/user/credit/payment.htm";
		}

	},
	/**
	 * 还款明细中手动还款地址
	 */
	PAY_PAYMENTONE_URL("还款明细中手动还款地址") {
		@Override
		public String getValue() {
			return "/user/credit/paymentOne.htm";
		}

	},

	PAY_PAYMENT_URL_SECOND("还款明细中手动还款成功跳转地址") {
		@Override
		public String getValue() {
			return "/user/credit/loanDetail.html";
		}
	},
	/**
	 * 提前还款地址
	 */
	PAY_PREPAYMENT_URL("提前还款地址") {
		@Override
		public String getValue() {
			return "/user/credit/prepayment.htm";
		}

	},

	PAY_PERPAYMENT_URL_SECOND("提前还款成功跳转地址") {
		@Override
		public String getValue() {
			return "/user/credit/repaying.html";
		}
	},

	/**
	 * 机构垫付地址
	 */
	PAY_SBDF_URL("机构垫付地址") {
		@Override
		public String getValue() {
			return "/user/bid/sbdf.htm";
		}

	},
	/**
	 * 提现审核地址
	 */
	CHECK_URL("提现审核地址") {
		@Override
		public String getValue() {
			return "/console/finance/txgl/wsh/check.htm";
		}

	},
	/**
	 * 提现审核放款地址
	 */
	CHECKWITHDRAW_URL("提现审核放款地址") {
		@Override
		public String getValue() {
			return "/console/finance/txgl/shtg/checkWithdraw.htm";
		}

	},
	/**
	 * 满标放款地址
	 */
	LOAN_URL("满标放款地址") {
		@Override
		public String getValue() {
			return "/console/finance/zjgl/fksh/loan.htm";
		}

	},
	/**
	 * 满标不放款地址
	 */
	NOTLOAN_URL("满标不放款地址") {
		@Override
		public String getValue() {
			return "/console/finance/zjgl/fksh/notLoan.htm";
		}

	},
	/**
	 * 放款审核列表路径
	 */
	FKSHLIST_URL("放款审核列表路径") {
		@Override
		public String getValue() {
			return "/console/finance/zjgl/fksh/fkshList.htm";
		}

	},
	/**
	 * 线下充值申请地址
	 */
	ADDXXCZ_URL("线下充值申请地址") {
		@Override
		public String getValue() {
			return "/console/finance/czgl/xxczgl/addXxcz.htm";
		}

	},
	/**
	 * 线下充值审核通过地址
	 */

	CHECKYESXXCZ_URL("线下充值审核通过地址") {
		@Override
		public String getValue() {
			return "/console/finance/czgl/xxczgl/xxczshtg.htm";
		}

	},
	/**
	 * 线下充值审核通过地址
	 */

	CHECKNOXXCZ_URL("线下充值审核不通过地址") {
		@Override
		public String getValue() {
			return "/console/finance/czgl/xxczgl/xxczqx.htm";
		}
	},
	AUTO_BID_INDEX("自动投资页面") {
		@Override
		public String getValue() {
			return "/user/financing/zdtb/index.htm";
		}
	},
	AUTO_BID_AUTHORIZED("自动投资授权") {
		@Override
		public String getValue() {
			return "/user/financing/zdtb/zztb.htm";
		}
	},
	/**
	 * 不良资产处理地址
	 */
	PTADVANCE_URL("不良资产处理地址") {
		@Override
		public String getValue() {
			return "/console/finance/zjgl/dfgl/sbdf.htm";
		}

	},

	/**
	 * 线下充值管理地址
	 */
	XXCZGLLIST_URL("线下充值管理地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/console/finance/czgl/xxczgl/xxczglList.htm";
		}
	},
	/**
	 * 第三方账号关联用户提交
	 */
	ASSREGISTER_SUBMIT("第三方账号关联用户注册提交") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/user/associatedRegister.htm";
		}
	},
	/**
	 * 第三方账号关联用户提交
	 */
	ASSACCOUNT_SUBMIT("第三方账号关联用户提交") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/user/associatedAccount.htm";
		}
	},
	/**
	 * 第三方账号关联注册页面
	 */
	ASSOCIATED_REGISTER("第三方账号关联注册页面") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/associatedRegister.html";
		}
	},
	/**
	 * 第三方账号关联用户页面
	 */
	ASSOCIATED_ACCOUNT("第三方账号关联用户页面") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/associatedAccount.html";
		}
	},
	/**
	 * 第三方补充授权地址 默认是空值，需要时才提供初始化值
	 */
	AUTHORIZE_URL("第三方补充授权地址") {
		@Override
		public String getValue() {
			return "/console/account/vipmanage/jgxx/jscl/updateJscl.jsp";
		}
	},
	/**
	 * 积分商城首页地址
	 */
	MALLINDEX_URL("积分商城首页地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/mall/mallIndex.htm";
		}
	},
	/**
	 * 积分商城购物车地址
	 */
	MALLSHOPPINGCAR_URL("积分商城购物车地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/mall/carList.htm";
		}
	},

	/**
	 * 我的积分
	 */
	MYSCORE_URL("我的积分") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/user/mall/myScore.html";
		}
	},
	/**
	 * 我的积分
	 */
	MYORDER_URL("我的订单") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/user/mall/myOrder.htm";
		}
	},
	/**
	 * 积分商城退款地址
	 */

	CHECKMALLREFUND_URL("积分商城退款地址") {
		@Override
		public String getValue() {
			return "/console/finance/zjgl/mall/checkMallRefund.htm";
		}

	},
	/**
	 * 财务管理菜单页面路径
	 */
	CWGL_MENU_PAGE("财务管理菜单页面路径") {
		@Override
		public String getValue() {
			return "/WEB-INF/include/menu/cwgl.jsp";
		}
	},
	/**
	 * 积分商城申请退款
	 */
	SCOREORDER_REFUND_URL("积分商城申请退款") {
		@Override
		public String getValue() {
			return "/console/mall/ddgl/yth/scoreOrderRefund.htm";
		}

	},
	/**
	 * 积分商城退货
	 */
	SCOREORDER_RETURN_URL("积分商城退货") {
		@Override
		public String getValue() {
			return "/console/mall/ddgl/yfh/scoreOrderReturn.htm";
		}

	},
	/**
	 * 积分商城已发送列表修改
	 */
	SCOREORDER_MODIFY_LOGISTICS("积分商城已发送列表修改") {
		@Override
		public String getValue() {
			return "/console/mall/ddgl/yfh/scoreOrderModifyLogistics.htm";
		}

	},
	/**
	 * 后台登录后首页地址
	 */
	CONSOLE_INDEX_URL("后台登录后首页地址") {
		@Override
		public String getValue() {
			return "/console/main.html";
		}

	},
	/**
	 * 后台登录后index地址,main.htm页面使用
	 */
	INDEX_URL("后台登录后index地址") {
		@Override
		public String getValue() {
			return "common/index.htm";
		}

	},
	/**
	 * 用户购买积分商城物品链接地址
	 */
	SCOREORDER_PAYMENT_URL("用户购买积分商城物品链接地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/pay/buy.htm";
		}

	},
	/**
	 * 积分商城详情
	 */
	MALL_DETAIL("积分商城详情") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/mall/ptscXq/";
		}
	},
	/**
	 * 手动执行任务地址
	 */
	EXECUTEJOB_URL("手动执行任务地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/scheduler/executeJob.htm";
		}
	},
	/**
	 * 修改任务地址
	 */
	EDITJOB_URL("手动执行任务地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/scheduler/editJob.htm";
		}
	},

	/**
	 * 后台登录页面地址
	 */
	CONSOLE_LOGIN("后台登录页面地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/console";
		}
	},

	/**
	 * 托管开户引导页面地址
	 */
	OPEN_ESCROW_GUIDE("托管开户引导页面地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/user/capital/openEscrowGuide.htm";// 具体值各托管自行SQL初始化语句指定
		}
	},
	/**
	 * 用户中心债权转让页面地址
	 */
	USER_ZQZR("用户中心债权转让合同页面地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/user/financing/agreement/zqzr.html";
		}
	},
	/**
	 * 用户中心债权转让未保全合同页面地址
	 */
	USER_WBQ_ZQZR("用户中心债权转让未保全合同页面地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/user/financing/agreement/zqzr.html";
		}
	},
	/**
	 * 后台债权转让页面地址
	 */
	CONSOLE_ZQZR("后台债权转让合同页面地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/console/bid/htgl/agreement/zqzr.html";
		}
	},
	/**
	 * 后台债权转让未保全合同页面地址
	 */
	CONSOLE_WBQ_ZQZR("后台债权转让未保全合同页面地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/console/bid/htgl/agreement/zqzr.html";
		}
	},
	/**
	 * 用户中心不良债权转让页面地址
	 */
	USER_BLZQZR_URL("用户中心不良债权转让合同页面地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/user/financing/agreement/blzqzrAgreement.html";
		}
	},
	/**
	 * 用户中心不良债权转让未保全合同页面地址
	 */
	USER_WBQ_BLZQZR_URL("用户中心不良债权转让未保全合同页面地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/user/financing/agreement/blzqzrAgreement.html";
		}
	},
	/**
	 * 后台不良债权转让页面地址
	 */
	CONSOLE_BLZQZR_URL("后台不良债权转让合同页面地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/console/bid/htgl/agreement/blzqzrAgreement.html";
		}
	},
	/**
	 * 后台债权转让未保全合同页面地址
	 */
	CONSOLE_WBQ_BLZQZR_URL("后台不良债权转让未保全合同页面地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/console/bid/htgl/agreement/blzqzrAgreement.html";
		}
	},
	/**
	 * 用户中心不良债权转让页面地址
	 */
	USER_BLZQZR("用户中心不良债权转让页面地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/user/fxbyj/blzqzr.html";
		}
	},

	/**
	 * 用户中心网签地址
	 */
	USER_NETSIGN_URL("用户中心网签地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/user/agreementSign/agreementSignDetail.html";
		}
	},

	/**
	 * 用户中心公益标协议页面地址
	 */
	USER_GYB_URL("用户中心公益标协议页面地址") {
		@Override
		public String getValue() {
			return "/user/financing/agreement/gyb.html";
		}
	},

	/**
	 * 用户中心垫付协议页面地址
	 */
	USER_DF_URL("用户中心垫付协议页面地址") {
		@Override
		public String getValue() {
			return "/user/financing/agreement/dfxy.html";
		}
	},

	/**
	 * 后台垫付协议页面地址
	 */
	CONSOLE_DF_URL("后台垫付协议页面地址") {
		@Override
		public String getValue() {
			return "/console/bid/htgl/agreement/dfxy.html";
		}
	},

	/**
	 * 后台三方协议页面地址
	 */
	CONSOLE_SANFANG_URL("后台三方协议页面地址") {
		@Override
		public String getValue() {
			return "/console/bid/htgl/agreement/xyb.html";
		}
	},

	/**
	 * 用户中心三方协议页面地址
	 */
	USER_SANFANG_URL("用户中心三方协议页面地址") {
		@Override
		public String getValue() {
			return "/user/financing/agreement/xyb.html";
		}
	},

	/**
	 * 后台四方协议页面地址
	 */
	CONSOLE_SIFANG_URL("后台四方协议页面地址") {
		@Override
		public String getValue() {
			return "/console/bid/htgl/agreement/xydb.html";
		}
	},

	/**
	 * 用户中心四方协议页面地址
	 */
	USER_SIFANG_URL("用户中心四方协议页面地址") {
		@Override
		public String getValue() {
			return "/user/financing/agreement/xydb.html";
		}
	},

	/**
	 * 不良债权购买地址
	 */
	PAYBADCLAIM_URL("不良债权购买地址") {
		@Override
		public String getValue() {
			return "/user/fxbyj/payBadClaim.htm";
		}
	},

	/**
	 * 后台合同保全查看地址
	 */
	CONSOLE_CONTRACT_VIEW_URL("后台合同保全查看地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/console/ebq/contractPreservationView.htm";
		}
	},

	/**
	 * 后台协议保全查看地址
	 */
	CONSOLE_AGREEMENT_VIEW_URL("后台协议保全查看地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/console/ebq/argeementPreservationView.htm";
		}
	},

	/**
	 * 用户中心协议保全查看地址
	 */
	USER_AGREEMENT_VIEW_URL("用户中心协议保全查看地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/user/ebq/argeementPreservationView.htm";
		}
	},
	/**
	 * 第三方托管，银行存管标信息补录地址
	 */
	ESCROW_URL_BIDENTERAGAIN("第三方托管，银行存管标信息补录地址") {
		@Override
		public String getValue() {
			return "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/pay/huifu/bidEnterAgain.htm";
		}
	},
	/**
	 * 标申请，审核通过URL
	 */
	BID_CHECK_URL("标申请，审核通过URL") {
		@Override
		public String getValue() {
			return "/console/bid/loanmanage/jkgl/check.htm";
		}
	};

	protected final String key;

	protected final String description;

	URLVariable(String description) {
		StringBuilder builder = new StringBuilder(getType());
		builder.append('.').append(name());
		key = builder.toString();
		this.description = description;
	}

	@Override
	public String getType() {
		return URLVariable.class.getAnnotation(VariableTypeAnnotation.class)
				.id();
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getValue() {
		try (InputStreamReader reader = new InputStreamReader(
				URLVariable.class.getResourceAsStream(getKey()), "UTF-8")) {
			StringBuilder builder = new StringBuilder();
			char[] cbuf = new char[1024];
			int len = reader.read(cbuf);
			while (len > 0) {
				builder.append(cbuf, 0, len);
				len = reader.read(cbuf);
			}
			return builder.toString();
		} catch (Throwable t) {
		}
		return null;
	}

	@Override
	public boolean isInit() {
		return true;
	}
}
