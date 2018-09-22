package com.dimeng.p2p.scheduler.tasks;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.config.Envionment;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S62.entities.T6252;
import com.dimeng.p2p.S62.entities.T6290;
import com.dimeng.p2p.S62.enums.T6290_F02;
import com.dimeng.p2p.S62.enums.T6290_F04;
import com.dimeng.p2p.S62.enums.T6290_F06;
import com.dimeng.p2p.common.EmailTypeUtil;
import com.dimeng.p2p.common.PhoneTypeUtil;
import com.dimeng.p2p.common.ResourceProviderUtil;
import com.dimeng.p2p.modules.base.console.service.RepaymentManage;
import com.dimeng.p2p.service.EmailSenderManageExt;
import com.dimeng.p2p.service.SmsSenderManageExt;
import com.dimeng.p2p.variables.defines.EmailVariavle;
import com.dimeng.p2p.variables.defines.LetterVariable;
import com.dimeng.p2p.variables.defines.MsgVariavle;
import com.dimeng.util.StringHelper;

/**
 * 还款提醒
 * <功能详细描述>
 * 
 * @author  heluzhu
 * @version  [版本号, 2015年10月28日]
 */
public class RepayMentWarnTask
{
    protected static final Logger logger = Logger.getLogger(RepayMentWarnTask.class);
    
    public void remind()
    {
        ResourceProvider resourceProvider = ResourceProviderUtil.getResourceProvider();
        logger.info("开始执行【还款提醒定时任务】任务，开始时间：" + new java.util.Date());
        ServiceProvider serviceProvider = resourceProvider.getResource(ServiceProvider.class);
        ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        try (ServiceSession serviceSession = serviceProvider.createServiceSession())
        {
            RepaymentManage repaymentManage = serviceSession.getService(RepaymentManage.class);
            /*T6290 t6290 = repaymentManage.getValidRepaymentSet();
            
            if (t6290 == null)
            { //未设置还款提醒  
                resourceProvider.log("未设置还款提醒  不能发送还款提醒");
                return;
            }*/
            
            int advanceCount = 0;
            String condition = "";
            //还款提醒
            List<T6290> hkTxList = repaymentManage.getT6290List(T6290_F04.HKTX, T6290_F06.QY);
            if (null != hkTxList)
            {
                for (T6290 t6290 : hkTxList)
                {
                    advanceCount = t6290.F03;
                    condition = " DATE(NOW()) = DATE(DATE_SUB(t.F08,INTERVAL " + advanceCount + " DAY))";
                    if (advanceCount == 0)
                    {
                        condition = " DATE(NOW()) = DATE(t.F08)";
                    }
                    T6252[] t6252sAdvance = repaymentManage.selectT6252s(condition);
                    if (t6252sAdvance != null && t6252sAdvance.length > 0)
                    {
                        for (T6252 t6252 : t6252sAdvance)
                        {
                            sendMessage(t6252, t6290, serviceSession, configureProvider, resourceProvider);
                        }
                    }
                    
                }
            }
            else
            {
                resourceProvider.log("未设置还款提醒  不能发送还款提醒");
            }
            
            //逾期提醒
            List<T6290> yqTxList = repaymentManage.getT6290List(T6290_F04.YQTX, T6290_F06.QY);
            if (null != yqTxList)
            {
                for (T6290 t6290 : yqTxList)
                {
                    advanceCount = t6290.F03;
                    if (advanceCount > 0)
                    {
                        //逾期提醒
                        condition = " DATE(DATE_SUB(NOW(),INTERVAL " + advanceCount + " DAY)) = DATE(t.F08) ";
                        T6252[] t6252sPass = repaymentManage.selectT6252s(condition);
                        if (t6252sPass != null && t6252sPass.length > 0)
                        {
                            for (T6252 t6252 : t6252sPass)
                            {
                                sendMessageYQ(t6252, t6290, serviceSession, configureProvider, resourceProvider);
                            }
                        }
                    }
                }
            }
            else
            {
                resourceProvider.log("未设置逾期提醒  不能发送逾期提醒");
            }
            
            /*int advanceCount = 0;
            int passCount = 0;
            advanceCount = t6290.F03;
            passCount = t6290.F04;
            
            //提前预警
            // String condition=" DATE(NOW()) > DATE(DATE_SUB(t.F08,INTERVAL "+advanceCount+" DAY)) AND DATE(t.F08) >= NOW()";
            String condition = " DATE(NOW()) = DATE(DATE_SUB(t.F08,INTERVAL " + advanceCount + " DAY))";
            if (advanceCount == 0)
            {
                condition = " DATE(NOW()) = DATE(t.F08)";
            }
            T6252[] t6252sAdvance = repaymentManage.selectT6252s(condition);
            if (t6252sAdvance != null && t6252sAdvance.length > 0)
            {
                for (T6252 t6252 : t6252sAdvance)
                {
                    sendMessage(t6252, t6290, serviceSession, configureProvider, resourceProvider);
                }
            }
            
            if (passCount > 0)
            {
                
                //过期提醒
                String conditionPass = " DATE(DATE_SUB(NOW(),INTERVAL " + passCount + " DAY)) = DATE(t.F08) ";
                
                //if(passRateCount>0){  逾期频率暂时不做
                
                //DATEDIFF(now(),t.F08)
                //conditionPass+=" AND DATEDIFF(now(),t.F08) % "+passRateCount+" = 0";   //没过一定周期循环一次
                //}
                T6252[] t6252sPass = repaymentManage.selectT6252s(conditionPass);
                if (t6252sPass != null && t6252sPass.length > 0)
                {
                    for (T6252 t6252 : t6252sPass)
                    {
                        sendMessageYQ(t6252, t6290, serviceSession, configureProvider, resourceProvider);
                    }
                }
            }*/
            
        }
        catch (Throwable e)
        {
            resourceProvider.log(e);
            
        }
        
        logger.info("结束执行【还款提醒定时任务】任务，结束时间：" + new java.util.Date());
    }
    
    /**
     * 发送提醒
     * @throws Throwable
     */
    private void sendMessage(T6252 model, T6290 t6290, ServiceSession serviceSession,
        ConfigureProvider configureProvider, ResourceProvider resourceProvider)
        throws Throwable
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String warnTypes = t6290.F02; //提醒方式
        RepaymentManage repaymentManage = serviceSession.getService(RepaymentManage.class);
        SimpleDateFormat sdfYYMMDD = new SimpleDateFormat("yyyy-MM-dd");
        
        if (model != null)
        {
            
            int userId = model.F03;
            String accountName = model.accountName;
            
            Timestamp now = new Timestamp(System.currentTimeMillis());
            String template = configureProvider.getProperty(LetterVariable.SEND_HKTX_SUCCESS);
            String templateEmail = configureProvider.getProperty(EmailVariavle.SEND_HKTX_MAIL); //邮件模板
            String templateSms = configureProvider.getProperty(MsgVariavle.SEND_HKTX_MSG); //短信模板
            Envionment envionment = configureProvider.createEnvionment();
            envionment.set("name", accountName);
            envionment.set("time", sdfYYMMDD.format(model.F08));
            envionment.set("title", model.bidTitle);
            String money = String.valueOf(model.F07);
            
            envionment.set("money", money);
            
            String title = "发送还款提醒";
            
            if (warnTypes.indexOf(T6290_F02.LETTER.name()) > -1)
            { //发送站内信
                String content = StringHelper.format(template, envionment);
                
                //发送站内消息
                int letterId = repaymentManage.addT6123(userId, title, now);
                repaymentManage.addT6124(letterId, content);
            }
            
            if (warnTypes.indexOf(T6290_F02.EMAIL.name()) > -1)
            { //发送邮件
            
                String content = StringHelper.format(templateEmail, envionment);
                
                EmailSenderManageExt emailSender = serviceSession.getService(EmailSenderManageExt.class);
                emailSender.send(EmailTypeUtil.EMAIL_HKYJ, title, content, model.email);
            }
            
            if (warnTypes.indexOf(T6290_F02.SMS.name()) > -1)
            { //发送短信
            
                String content = StringHelper.format(templateSms, envionment);
                
                String phone = model.phone;
                SmsSenderManageExt smsSender = serviceSession.getService(SmsSenderManageExt.class);
                smsSender.send(PhoneTypeUtil.PHONE_HKYJ, content, null, phone);
            }
            
            logger.info(sdf.format(new Date(System.currentTimeMillis())) + " 发送还款提醒成功(" + accountName + ")");
        }
        
    }
    
    /**
     * 发送逾期提醒
     * @throws Throwable
     */
    private void sendMessageYQ(T6252 model, T6290 t6290, ServiceSession serviceSession,
        ConfigureProvider configureProvider, ResourceProvider resourceProvider)
        throws Throwable
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String warnTypes = t6290.F02; //提醒方式
        RepaymentManage repaymentManage = serviceSession.getService(RepaymentManage.class);
        if (model != null)
        {
            int userId = model.F03;
            String accountName = model.accountName;
            
            Timestamp now = new Timestamp(System.currentTimeMillis());
            String template = configureProvider.getProperty(LetterVariable.SEND_YQTX_SUCCESS);
            String templateEmail = configureProvider.getProperty(EmailVariavle.SEND_QYTX_MAIL); //邮件模板
            String templateSms = configureProvider.getProperty(MsgVariavle.SEND_QYTX_MSG); //短信模板
            Envionment envionment = configureProvider.createEnvionment();
            envionment.set("name", accountName);
            
            envionment.set("title", model.bidTitle);
            
            String title = "发送逾期提醒";
            
            if (warnTypes.indexOf(T6290_F02.LETTER.name()) > -1)
            { //发送站内信
                String content = StringHelper.format(template, envionment);
                
                //发送站内消息
                int letterId = repaymentManage.addT6123(userId, title, now);
                repaymentManage.addT6124(letterId, content);
            }
            
            if (warnTypes.indexOf(T6290_F02.EMAIL.name()) > -1)
            { //发送邮件
            
                String content = StringHelper.format(templateEmail, envionment);
                
                EmailSenderManageExt emailSender = serviceSession.getService(EmailSenderManageExt.class);
                emailSender.send(EmailTypeUtil.EMAIL_HKYJ, title, content, model.email);
            }
            
            if (warnTypes.indexOf(T6290_F02.SMS.name()) > -1)
            { //发送短信
            
                String content = StringHelper.format(templateSms, envionment);
                
                String phone = model.phone;
                SmsSenderManageExt smsSender = serviceSession.getService(SmsSenderManageExt.class);
                smsSender.send(PhoneTypeUtil.PHONE_HKYJ, content, null, phone);
            }
            
            logger.info(sdf.format(new Date(System.currentTimeMillis())) + " 发送逾期提醒成功(" + accountName + ")");
        }
        
    }
}
