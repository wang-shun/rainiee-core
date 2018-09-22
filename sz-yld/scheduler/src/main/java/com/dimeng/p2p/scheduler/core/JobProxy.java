package com.dimeng.p2p.scheduler.core;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;

/**
 * 任务代理
 * @author heluzhu
 *
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class JobProxy implements Job {
	
	public void execute(JobExecutionContext context) throws JobExecutionException {
		SchedulerContainer sContainer = new SchedulerContainer();
		sContainer.executeJob(context);
	}

}
