package com.dimeng.p2p.app.servlets.user;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.resource.ResourceRegister;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S50.entities.T5023;
import com.dimeng.p2p.S50.enums.T5023_F02;
import com.dimeng.p2p.account.user.service.TxManage;
import com.dimeng.p2p.app.servlets.AbstractAppServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.app.servlets.user.domain.BaseInfo;
import com.dimeng.p2p.app.servlets.user.domain.Chargepinfo;
import com.dimeng.p2p.app.servlets.user.domain.SQFee;
import com.dimeng.p2p.app.servlets.user.domain.Withdrawpinfo;
//import com.dimeng.p2p.escrow.shuangqian.variables.ShuangQianVariable;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.p2p.variables.defines.pays.PayVariavle;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BooleanParser;
import com.dimeng.util.parser.DoubleParser;

/**
 * 提现手续费和充值手续费
 * 
 * @author luoxiaoyan
 */
public class Fee extends AbstractAppServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        final ConfigureProvider configureProvider =
            ResourceRegister.getResourceProvider(getServletContext()).getResource(ConfigureProvider.class);
        
        Map<String, Object> map = new HashMap<String, Object>();
        
        TxManage txManage = serviceSession.getService(TxManage.class);
        T5023 t5023 = txManage.getT5023(T5023_F02.WITHDRAW);
        String twxts = "";
        if (t5023 != null)
        {
            twxts = getImgContent(t5023.F03);
        }
        // 提现手续费
        final String way = configureProvider.getProperty(SystemVariable.WITHDRAW_POUNDAGE_WAY);
        final String proportion = configureProvider.getProperty(SystemVariable.WITHDRAW_POUNDAGE_PROPORTION);
        final String p1 = configureProvider.getProperty(SystemVariable.WITHDRAW_POUNDAGE_1_5);
        final String p2 = configureProvider.getProperty(SystemVariable.WITHDRAW_POUNDAGE_5_20);
        final String min = configureProvider.getProperty(SystemVariable.WITHDRAW_MIN_FUNDS);
        final String max = configureProvider.getProperty(SystemVariable.WITHDRAW_MAX_FUNDS);
        final String withdrawLimitFund = configureProvider.getProperty(SystemVariable.WITHDRAW_LIMIT_FUNDS);
        
        String kcfs = configureProvider.getProperty(SystemVariable.TXSXF_KCFS);
        Withdrawpinfo w = new Withdrawpinfo();
        w.setWay(way);
        w.setProportion(proportion);
        w.setPoundage1(p1);
        w.setPoundage2(p2);
        w.setMax(max);
        w.setMin(min);
        //w.setTgPoundage(tgPoundage);
        w.setTxkfType(kcfs);
        w.setTwxts(twxts);
        w.setWithdrawLimitFund(withdrawLimitFund);
        
        t5023 = txManage.getT5023(T5023_F02.CHARGE);
        String cwxts = "";
        if (t5023 != null)
        {
            cwxts = getImgContent(t5023.F03);
        }
        // 充值手续费
        final String cmin = configureProvider.getProperty(PayVariavle.CHARGE_MIN_AMOUNT);
        final String cmax = configureProvider.getProperty(PayVariavle.CHARGE_MAX_AMOUNT);
        final String p = configureProvider.getProperty(PayVariavle.CHARGE_RATE);
        final String pMax = configureProvider.getProperty(PayVariavle.CHARGE_MAX_POUNDAGE);
        final String isNeedEmail = configureProvider.getProperty(PayVariavle.CHARGE_MUST_EMAIL);
        final String isNeedNciic = configureProvider.getProperty(PayVariavle.CHARGE_MUST_NCIIC);
        final String isNeedPhone = configureProvider.getProperty(PayVariavle.CHARGE_MUST_PHONE);
        final String isNeedPsd = configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD);
        
        // 托管前缀
        final String prefix = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
        
        // 判断是否是托管项目
        boolean tg = BooleanParser.parseObject(configureProvider.getProperty(SystemVariable.SFZJTG));
        if (tg)
        {
            // 双乾托管费率,默认为shuangqian
            /*if (!StringHelper.isEmpty(prefix) && "shuangqian".equals(prefix.toLowerCase(Locale.ENGLISH)))
            {
                String kjFeeType = configureProvider.getProperty(ShuangQianVariable.SQ_KJFEETYPE);
                double withdrawRate =
                    DoubleParser.parse(configureProvider.getProperty(ShuangQianVariable.SQ_SQWITHDRAW_RATE));
                DecimalFormat df = new DecimalFormat("#0.00");
                double kuaiJieRate =
                    DoubleParser.parse(df.format((("3".equals(kjFeeType) || "4".equals(kjFeeType)) ? 0.001
                        : withdrawRate + 0.001) * 100));
                
                SQFee sqFee = new SQFee();
                sqFee.setKuaiJieRate(kuaiJieRate);
                sqFee.setWithdrawRate(withdrawRate);
                sqFee.setKjFeeType(kjFeeType);
                
                map.put("sqFee", sqFee);
            }*/
        }
        
        Chargepinfo c = new Chargepinfo();
        c.setMax(cmax);
        c.setMin(cmin);
        c.setP(p);
        c.setpMax(pMax);
        c.setIsNeedEmail(isNeedEmail);
        c.setIsNeedNciic(isNeedNciic);
        c.setIsNeedPhone(isNeedPhone);
        c.setIsNeedPsd(isNeedPsd);
        c.setCwxts(cwxts);
        
        // 是否需要邮箱认证
//        final String isNeedEmailRZ = configureProvider.getProperty(SystemVariable.SFBXYXRZ);
        
        BaseInfo baseInfo = new BaseInfo();
        baseInfo.setIsNeedEmailRZ("true");
        baseInfo.setPrefix(prefix);
        
        map.put("chargep", c);
        map.put("withdrawp", w);
        map.put("baseInfo", baseInfo);
        
        // 封装返回信息
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success", map);
        return;
    }
    
}
