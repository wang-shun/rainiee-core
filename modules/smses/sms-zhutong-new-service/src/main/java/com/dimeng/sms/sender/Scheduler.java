package com.dimeng.sms.sender;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.message.sms.Extracter;
import com.dimeng.framework.message.sms.entity.SmsTask;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.variables.defines.smses.SmsVaribles;
import com.dimeng.p2p.variables.defines.smses.ZhuTongVariables;
import com.dimeng.util.parser.DateTimeParser;
import com.dimeng.util.parser.IntegerParser;

/**
 * 调度器
 */
public class Scheduler extends Thread
{
    
    protected final ThreadPoolExecutor executor;
    
    protected transient boolean alive = true;
    
    protected final ResourceProvider resourceProvider;
    
    protected final ConfigureProvider configureProvider;
    
    protected final ServiceProvider serviceProvider;
    
    public Scheduler(ResourceProvider resourceProvider)
    {
        this.resourceProvider = resourceProvider;
        configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        serviceProvider = resourceProvider.getResource(ServiceProvider.class);
        executor = new ThreadPoolExecutor(10, 50, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
    }
    
    public void halt()
    {
        alive = false;
        try
        {
            executor.shutdownNow();
        }
        finally
        {
            this.interrupt();
        }
        
    }
    
    @Override
    public void run()
    {
        while (alive)
        {
            SmsTask[] smsTasks = null;
            try (ServiceSession serviceSession = serviceProvider.createServiceSession())
            {
                //				serviceSession.openTransactions();
                int maxCount = IntegerParser.parse(configureProvider.getProperty(SmsVaribles.SMS_MAX_COUNT));
                int expirsMinutes = IntegerParser.parse(configureProvider.getProperty(SmsVaribles.SMS_EXPIRES_MINUTES));
                Extracter extracter = serviceSession.getService(Extracter.class);
                smsTasks = extracter.extract(maxCount, expirsMinutes);
                //				serviceSession.commit();
            }
            catch (Throwable e)
            {
                resourceProvider.log(e);
            }
            if (smsTasks != null && smsTasks.length > 0)
            {
                Runner runner = new Runner(smsTasks);
                executor.submit(runner);
            }
            try
            {
                sleep(3000);
            }
            catch (InterruptedException e)
            {
                alive = false;
                break;
            }
        }
    }
    
    protected class Runner implements Runnable
    {
        protected SmsTask[] smsTasks;
        
        protected final StringBuilder sb = new StringBuilder();
        
        protected final String encode = "utf-8";
        
        public Runner(SmsTask[] smsTasks)
        {
            this.smsTasks = smsTasks;
        }
        
        public String send(SmsTask smsTask)
        {
            
            int len = smsTask.receivers.length;
            if (len <= 0)
            {
                return null;
            }
            sb.setLength(0);
           //助通行业短信平台对接  add by 胥耀 - 2017/11/25
            try
            {
            	//当前时间
            	String tkey=TimeUtil.getNowTime("yyyyMMddHHmmss");
            	//url
            	sb.append("url=");
                sb.append(configureProvider.getProperty(ZhuTongVariables.SMS_URI));
                //用户名
                sb.append("&username=");
                sb.append(URLEncoder.encode(configureProvider.getProperty(ZhuTongVariables.SMS_USER_ID), encode));
                //密码
                sb.append("&password=");
                sb.append(MD5Update.getMD5(MD5Update.getMD5(configureProvider.getProperty(ZhuTongVariables.SMS_USER_PASSWORD))+tkey));
                //明文
                // sb.append(URLEncoder.encode(configureProvider.getProperty(ZhuTongVariables.SMS_USER_PASSWORD), encode));
                //加密
                // MD5 md5 = new MD5();
                // md5.getMD5ofStr(configureProvider.getProperty(ZhuTongVariables.SMS_USER_PASSWORD));
                //当前时间
                sb.append("&tkey=");        
                sb.append(tkey);    
                //手机号
                sb.append("&mobile=");
                for (int i = 0; i < len; i++)
                {
                    if (i > 0)
                    {
                        sb.append(",");
                    }
                    sb.append(smsTask.receivers[i]);
                }                                               
                //签名及短信内容
                sb.append("&content=");
                String sign = configureProvider.getProperty(ZhuTongVariables.SMS_SIGN);
                sb.append(URLEncoder.encode(smsTask.content + sign, encode));
                //扩展的小号
                sb.append("&xh=");           
                
                resourceProvider.log(sb.toString());
          //    sb.append("&dstime="); //定时时间，为空时表示立即发送   
                //产品ID
         /*     sb.append("&productid=");
                sb.append(URLEncoder.encode(configureProvider.getProperty(ZhuTongVariables.SMS_PRODUCT_ID), encode));*/
                
               
                
                //提交页面
               /* URL postUrl = new URL(configureProvider.getProperty(ZhuTongVariables.SMS_URI));
                URLConnection connection = postUrl.openConnection();*/
              //  connection.setRequestProperty("accept", "*/*");
              /*  connection.setRequestProperty("connection", "Keep-Alive");
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setDoOutput(true);
                connection.setDoInput(true);
                try (OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream()))
                {
                    out.write(sb.toString());
                    out.flush();
                }
                String result = "";
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), encode)))
                {
                    result = in.readLine();
                }*/
                
                //定时信息只可为post方式提交
                String ret=HttpReq.sendPost(configureProvider.getProperty(ZhuTongVariables.SMS_URI),sb.toString());
                //新增短信返回码所对应的描述  add by zhangxu - 20150907
                resourceProvider.log(String.format("短信ID：%s，内容：%s，发送完成，返回状态码：%s，返回描述：%s,发送时间：%s ",
                    Long.toString(smsTask.id),
                    smsTask.content,
                    ret,
                    this.getResultExplain(ret),
                    DateTimeParser.format(new Date())));
                return ret;
            }
            catch (Exception e)
            {
                resourceProvider.log(String.format("短信ID：%s，内容：%s，发送失败,发送时间：%s",
                    Long.toString(smsTask.id),
                    smsTask.content,
                    DateTimeParser.format(new Date())));
                resourceProvider.log(e);
                return null;
            }
        }
        
        /**
         * 处理发送短信返回结果
         * @param result
         * @return
         */
        private String getResultExplain(String result)
        {
            
            String explain = "";
            if (result != null)
            {
                if (result.contains(","))
                {
                    String resultDo = result.substring(0, result.indexOf(","));
                    switch (resultDo)
                    {
                        case "0":
                            explain = "短信发送失败";
                            break;
                        case "1":
                            explain = "短信发送成功";
                            break;
                        case "-1":
                            explain = "用户名或者密码不正确或用户禁用";
                            break;
                        case "2":
                            explain = "余额不够或扣费错误";
                            break;
                        case "3":
                            explain = "扣费失败异常（请联系客服）";
                            break;
                        case "6":
                            explain = "有效号码为空，手机号码有错";
                            break;
                        case "7":
                            explain = "短信内容为空（消息长度错）";
                            break;
                        case "107":
                            explain = "包含错误的手机号码";
                            break;
                        case "8":
                            explain = "无签名";
                            break;
                        case "9":
                            explain = "没有Url提交权限";
                            break;
                        case "10":
                            explain = "发送号码过多,最多支持2000个号码";
                            break;
                        case "11":
                            explain = "产品ID异常或产品禁用";
                            break;
                        case "12":
                            explain = "参数异常";
                            break;
                        case "13":
                            explain = "30分种重复提交30分种重复提交";
                            break;
                        case "14":
                            explain = "用户名或密码不正确，产品余额为0，禁止提交，联系客服";
                            break;
                        case "15":
                            explain = "Ip验证失败";
                            break;
                        case "16":
                            explain = "短信内容过长，最多支持500个";
                            break;
                    }
                }
            }
            else
            {
                explain = "短信发送返回码为空，请检查服务器公网IP";
            }
            
            return explain;
        }
        
        @Override
        public void run()
        {
            if (smsTasks == null || smsTasks.length == 0)
            {
                return;
            }
            try (ServiceSession serviceSession = serviceProvider.createServiceSession())
            {
                Extracter extracter = serviceSession.getService(Extracter.class);
                for (SmsTask smsTask : smsTasks)
                {
                    //捕获，避免异常不导致后面的循环中断
                    try
                    {
                        //						serviceSession.openTransactions();
                        String code = send(smsTask);
                        extracter.mark(smsTask.id, "1".equals(code.split(",")[0]), code);
                        //						serviceSession.commit();
                    }
                    catch (Throwable e)
                    {
                        resourceProvider.log(e);
                    }
                }
            }
            catch (Throwable e)
            {
                resourceProvider.log(e);
            }
        }
    }
    
}
