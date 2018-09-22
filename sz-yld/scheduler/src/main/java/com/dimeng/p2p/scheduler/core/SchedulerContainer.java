package com.dimeng.p2p.scheduler.core;

import com.dimeng.framework.service.ServiceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S66.entities.T6601;
import com.dimeng.p2p.S66.entities.T6602;
import com.dimeng.p2p.S66.entities.T6603;
import com.dimeng.p2p.S66.enums.T6601_F06;
import com.dimeng.p2p.common.ResourceProviderUtil;
import com.dimeng.p2p.service.TaskManage;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.DateTimeParser;
import org.apache.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * 定时任务管理器
 * <功能详细描述>
 * 
 * @author  heluzhu
 * @version  [版本号, 2015年10月28日]
 */
@WebListener
public class SchedulerContainer implements ServletContextListener
{
    
    protected static final Logger logger = Logger.getLogger(SchedulerContainer.class);
    
    private static Scheduler scheduler;
    
    private static final String GROUP_NAME = "DEFAULT";
    
    private static final HashMap<Integer, JobStatus> jobStatusMap = new HashMap<Integer, JobStatus>();
    
    private static TaskManage taskManage;
    
    private static HashMap<Integer, T6601> objMap = new HashMap<Integer, T6601>();
    
    /**
     * 执行任务
     * 
     * @param context
     * @throws Exception
     */
    void executeJob(JobExecutionContext context)
    {
        JobKey jobKey = context.getJobDetail().getKey();
        String id = jobKey.getName();
        handExecuteJob(Integer.parseInt(id));
    }
    
    /**
     * 手动执行任务
     * @param id
     */
    public static Map<String, String> handExecuteJob(int id)
    {
        
        Map<String, String> result = new HashMap<String, String>();
        try
        {
            T6601 t6601 = objMap.get(id);//taskManage.queryById(id);
            if (t6601 == null)
            {
                result.put("ERROR", "此任务不存在id:+" + id);
                logger.info("此任务不存在id:+" + id);
            }
            else
            {
                if (T6601_F06.ENABLE == t6601.F06)
                {
                    return initJob(t6601);
                }
                else
                {
                    result.put("ERROR", "此任务已被禁用，请先开启");
                    logger.info("此任务已被禁用，请先开启:" + t6601.F02);
                }
            }
        }
        catch (Throwable e)
        {
            result.put("ERROR", "执行任务失败id:" + id);
            logger.error("执行任务失败id:+" + id, e);
        }
        return result;
    }
    
    /**
     * 调度任务
     * @param task
     * @throws Exception
     */
    public static void scheduleJob(T6601 task)
        throws Exception
    {
        if(T6601_F06.DISABLE == task.F06){
            return;
        }
        JobKey jobKey = getJobKey(task.F01);
        TriggerKey triggerKey = getTriggerKey(task.F01);
        
        removeJob(task.F01);
        
        JobDetail jobDetail = JobBuilder.newJob(JobProxy.class).withIdentity(jobKey).build();
        CronTrigger trigger = (CronTrigger)scheduler.getTrigger(triggerKey);
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(task.F05);
        trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
        
        scheduler.scheduleJob(jobDetail, trigger);
        jobStatusMap.put(task.F01, JobStatus.waiting);
        objMap.put(task.F01, task);
    }
    
    static JobKey getJobKey(int id)
    {
        JobKey key = new JobKey(id + "", GROUP_NAME);
        return key;
    }
    
    static TriggerKey getTriggerKey(int id)
    {
        TriggerKey key = new TriggerKey(id + "", GROUP_NAME);
        return key;
    }

    /**
     * 禁用、启用任务
     * @param t6601
     * @return
     */
    public static void updateStatus(T6601 t6601) throws Throwable{
        try {
            if(t6601.F06 == T6601_F06.ENABLE){
                scheduleJob(t6601);
            }else{
                removeJob(t6601.F01);
            }
        } catch (Exception e) {
            logger.error(e,e);
            throw e;
        }
    }

    /**
     * 添加/修改任务
     * @param t6601
     * @return
     */
    public static void editJob(T6601 t6601) throws Throwable{
        try {
            if(t6601.F01 > 0){
                removeJob(t6601.F01);
            }
            scheduleJob(t6601);
        }catch (Exception e) {
            logger.error(e,e);
            throw e;
        }
    }
    /**
     * 删除任务
     * @param id
     * @throws Exception
     */
    private static void removeJob(int id)
        throws Exception
    {
        scheduler.unscheduleJob(getTriggerKey(id));
        scheduler.deleteJob(getJobKey(id));
        jobStatusMap.remove(id);
        objMap.remove(id);
    }
    
    private static Map<String, String> initJob(T6601 t6601)
    {
        Map<String, String> result = new HashMap<String, String>();
        try
        {
            if (t6601 != null)
            {
                String jobParam = t6601.F10;
                if (!StringHelper.isEmpty(jobParam))
                {
                    HashMap<String, String> map = new HashMap<String, String>();
                    String[] values = jobParam.split(",");
                    for (String v : values)
                    {
                        String[] pair = v.split(":");
                        if (pair.length == 2)
                        {
                            String key = pair[0];
                            String value = pair[1];
                            map.put(key, value);
                        }
                    }
                    JobContext.setParamMap(map);
                }
                Class<?> clazz = Class.forName(t6601.F03);
                Method method = clazz.getDeclaredMethod(t6601.F04);
                Object obj = clazz.newInstance();
                
                if (obj == null)
                {
                    
                    result.put("ERROR", "实体bean未初始化");
                    logger.info("实体bean未初始化:" + clazz.getName());
                    return result;
                }
                jobStatusMap.put(t6601.F01, JobStatus.runing);
                t6601.F07 = new Timestamp(System.currentTimeMillis());
                String executeTime = "0";
                try
                {
                    Thread currentThread = Thread.currentThread();
					long statTime=System.currentTimeMillis();
					logger.info("StartJob_,ThreadId:"+currentThread.getId()+",Method:"+clazz.getName()+"."+method.getName()+" ,startTime"+DateTimeParser.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
					method.invoke(obj);
					long endTime =System.currentTimeMillis();					
					logger.info("EndJob_,ThreadId:"+currentThread.getId()+",Method:"+clazz.getName()+"."+method.getName()+" ,startTime"+DateTimeParser.format(new Date(), "yyyy-MM-dd HH:mm:ss")+",execute:"+((endTime-statTime)/1000)+" s");
                    executeTime = ((endTime-statTime)/1000)+"";
                }
                catch (Exception e)
                {
                    logger.error(e,e);
                    T6602 t6602 = new T6602();
                    t6602.F02 = t6601.F01;
                    t6602.F03 = e.getMessage();
                    try
                    {
                        taskManage.insertT6602(t6602);
                    }
                    catch (Throwable e1)
                    {
                        logger.error(e1.getMessage());
                    }
                    result.put("ERROR", "运行失败");
                    logger.error("任务运行失败:" + clazz.getName() + "." + method.getName(), e);
                }
                t6601.F08 = new Timestamp(System.currentTimeMillis());
                taskManage.updateExcuteTime(t6601);
                /*T6603 t6603 = new T6603();
                t6603.F02 = t6601.F01;
                t6603.F03 = executeTime;
                t6603.F04 = t6601.F07;
                t6603.F05 = t6601.F08;
                taskManage.inserT6603(t6603);*/
                result.put("SUCCESS", "任务运行成功");
            }
            
        }
        catch (Throwable e)
        {
            T6602 t6602 = new T6602();
            t6602.F02 = t6601.F01;
            t6602.F03 = e.getMessage();
            try
            {
                taskManage.insertT6602(t6602);
            }
            catch (Throwable e1) {
                logger.error(e1,e1);
            }
            result.put("ERROR", "运行失败");
        }
        finally
        {
        	if(t6601 != null){
        		jobStatusMap.put(t6601.F01, JobStatus.waiting);
        	}
        }
        return result;
    }

    /**
     * Receives notification that the web application initialization
     * process is starting.
     * <p/>
     * <p>All ServletContextListeners are notified of context
     * initialization before any filters or servlets in the web
     * application are initialized.
     *
     * @param sce the ServletContextEvent containing the ServletContext
     *            that is being initialized
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        if (scheduler == null)
        {
            try
            {

                ServiceProvider serviceProvider = ResourceProviderUtil.getResourceProvider().getResource(ServiceProvider.class);

                try (ServiceSession serviceSession = serviceProvider.createServiceSession())
                {
                    taskManage = serviceSession.getService(TaskManage.class);
                    //Thread.sleep(1000);
                    scheduler = new StdSchedulerFactory("quartz.properties").getScheduler();
                    scheduler.start();
                    List<T6601> list = taskManage.queryAllInfo(null, null);
                    if (list != null && list.size() > 0)
                    {
                        for (T6601 t6601 : list)
                        {
                            try
                            {
                                if (t6601.F06 == T6601_F06.ENABLE)
                                {
                                    scheduleJob(t6601);
                                }
                            }
                            catch (Exception e)
                            {
                                logger.error("执行异常：scheduler name is +" + t6601.F02,e);
                            }
                        }
                    }
                }
                catch (Throwable e)
                {
                    logger.error(e,e);
                }

            }
            catch (Exception e)
            {
                logger.error("scheduler start error", e);
            }
        }
    }

    /**
     * Receives notification that the ServletContext is about to be
     * shut down.
     * <p/>
     * <p>All servlets and filters will have been destroyed before any
     * ServletContextListeners are notified of context
     * destruction.
     *
     * @param sce the ServletContextEvent containing the ServletContext
     *            that is being destroyed
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try
        {
            if(scheduler!=null){
                scheduler.shutdown();
            }
            jobStatusMap.clear();
            objMap.clear();
            Thread.currentThread().interrupt();
        }
        catch (SchedulerException e)
        {
            logger.error("scheduler shutdown error",e);
        }
    }
}
