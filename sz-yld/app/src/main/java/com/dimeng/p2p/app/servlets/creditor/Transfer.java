/*
 * 文 件 名:  Transfer.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2015年12月11日
 */
package com.dimeng.p2p.app.servlets.creditor;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.UnixCrypt;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.account.user.service.UserInfoManage;
import com.dimeng.p2p.app.service.BusinessManage;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.modules.bid.user.service.ZqzrManage;
import com.dimeng.p2p.modules.bid.user.service.query.addTransfer;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.p2p.variables.defines.pays.PayVariavle;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BigDecimalParser;
import com.dimeng.util.parser.BooleanParser;
import com.dimeng.util.parser.IntegerParser;

/**
 * 转让
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年12月11日]
 */
public class Transfer extends AbstractSecureServlet
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -8098073187529948771L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        // 判断用户是否被拉黑或者锁定
        UserInfoManage userInfoMananage = serviceSession.getService(UserInfoManage.class);
        T6110 t6110 = userInfoMananage.getUserInfo(serviceSession.getSession().getAccountId());
        
        // 用户状态非法
        if (t6110.F07 == T6110_F07.HMD)
        {
            throw new LogicalException("账号异常,请联系客服！");
        }
        
        // 获取是否需要交易密码
        final ConfigureProvider configureProvider = getResourceProvider().getResource(ConfigureProvider.class);
        boolean isOpenWithPsd = BooleanParser.parse(configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD));
        boolean tg = BooleanParser.parse(configureProvider.getProperty(SystemVariable.SFZJTG));
        
        // 获取交易密码
        String tranPwd = getParameter(request, "tranPwd");
        if (isOpenWithPsd)
        {
            tranPwd = UnixCrypt.crypt(tranPwd, DigestUtils.sha256Hex(tranPwd));
        }
        
        final ZqzrManage finacingManage = serviceSession.getService(ZqzrManage.class);
        
        // 债券转出价格
        final BigDecimal salePrice = BigDecimalParser.parse(getParameter(request, "salePrice"));
        
        // 获取债券ID
        final int creditorId = IntegerParser.parse(getParameter(request, "creditorId"));
        
        // 债券价值
        final BigDecimal bidAmount = BigDecimalParser.parse(getParameter(request, "bidAmount"));
        
        BigDecimal zqzrsx = new BigDecimal(0);
        BigDecimal zqzrxx = new BigDecimal(0);
        try
        {
            String zqzrbl = configureProvider.getProperty(SystemVariable.ZQZRBL);
            String[] zqzrblTemp = zqzrbl.split("-");
            if (zqzrblTemp.length == 2)
            {
                zqzrxx = new BigDecimal(zqzrblTemp[0]);
                zqzrsx = new BigDecimal(zqzrblTemp[1]);
            }
        }
        catch (Exception e)
        {
        }
        if ((!zqzrsx.equals(BigDecimal.ZERO)
            && (salePrice.compareTo(zqzrsx.multiply(bidAmount).setScale(2, BigDecimal.ROUND_HALF_UP)) > 0) || (!zqzrsx.equals(BigDecimal.ZERO) && salePrice.compareTo(zqzrxx.multiply(bidAmount)
            .setScale(2, BigDecimal.ROUND_HALF_UP)) < 0)))
        {
            String description = "债权转让的价格区间需在" + zqzrxx.multiply(bidAmount).setScale(2, BigDecimal.ROUND_HALF_UP)
                + "元-" + zqzrsx.multiply(bidAmount).setScale(2, BigDecimal.ROUND_HALF_UP)
                + "元之间";
            setReturnMsg(request, response, ExceptionCode.ZAIQUAN_CY_TS, description);
            return;
        }
        
        // 判断债券是否可以转让
        BusinessManage businessManage = serviceSession.getService(BusinessManage.class);
        
        // 判断债权是否存在逾期
        final String isExpired = businessManage.isExpired(creditorId);
        
        if (!StringHelper.isEmpty(isExpired))
        {
            if (IntegerParser.parse(isExpired) > 0)
            {
                setReturnMsg(request, response, ExceptionCode.UNKNOWN_ERROR, "逾期债权不可以进行转让!");
                return;
            }
        }
        
        final String data = businessManage.getData(String.valueOf(creditorId));
        
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
        Date d = sim.parse(data);
        
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(new Date());
        
        // 债权持有多少天后可转让
        final int cyDay = IntegerParser.parse(getConfigureProvider().getProperty(SystemVariable.ZQZR_CY_DAY));
        
        Calendar createTime = Calendar.getInstance();
        createTime.setTime(d);
        createTime.add(Calendar.DAY_OF_YEAR, cyDay);
        
        if (createTime.compareTo(rightNow) == 1)
        {
            setReturnMsg(request, response, ExceptionCode.ZAIQUAN_CY_TS, "债权持有超过"+ cyDay + "天后才可转让!");
            return;
        }
        
        //查询出下一个还款日
        final String nextData = businessManage.getNextData(String.valueOf(creditorId));
        
        Calendar nextTime = Calendar.getInstance();
        Date nDate = sim.parse(nextData);
        nextTime.setTime(nDate);
        
        int days = ((int)(nextTime.getTime().getTime()/1000) - (int)(rightNow.getTime().getTime()/1000))/3600/24; 
        if (days < 3)
        {
            setReturnMsg(request, response, ExceptionCode.ZAIQUAN_XYGHK_TS, "距离下一个还款日小于3天，不能转让!");
            return;
        }
        
        finacingManage.transfer(new addTransfer()
        {
            @Override
            public BigDecimal getRateMoney()
            {
                return new BigDecimal(configureProvider.getProperty(SystemVariable.ZQZRGLF_RATE));
            }
            
            @Override
            public BigDecimal getTransferValue()
            {
                return salePrice;
            }
            
            @Override
            public int getTransferId()
            {
                return creditorId;
            }
            
            @Override
            public BigDecimal getBidValue()
            {
                return bidAmount;
            }
        }, tranPwd);
        
        // 封装返回信息
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success!");
        return;
    }
    
}
