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
import com.dimeng.p2p.variables.defines.smses.JianZhouVaribles;
import com.dimeng.p2p.variables.defines.smses.SmsVaribles;
import com.dimeng.util.parser.DateTimeParser;
import com.dimeng.util.parser.IntegerParser;

/**
 * 调度器
 */
public class Scheduler extends Thread {

	protected final ThreadPoolExecutor executor;
	protected transient boolean alive = true;
	protected final ResourceProvider resourceProvider;
	protected final ConfigureProvider configureProvider;
	protected final ServiceProvider serviceProvider;

	public Scheduler(ResourceProvider resourceProvider) {
		this.resourceProvider = resourceProvider;
		configureProvider = resourceProvider
				.getResource(ConfigureProvider.class);
		serviceProvider = resourceProvider.getResource(ServiceProvider.class);
		executor = new ThreadPoolExecutor(10, 50, 30, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>());
	}

	public void halt() {
		alive = false;
		try {
			executor.shutdownNow();
		} finally {
			this.interrupt();
		}

	}

	@Override
	public void run() {
		while (alive) {
			SmsTask[] smsTasks = null;
			try (ServiceSession serviceSession = serviceProvider
					.createServiceSession()) {
				int maxCount = IntegerParser.parse(configureProvider
						.getProperty(SmsVaribles.SMS_MAX_COUNT));
				int expirsMinutes = IntegerParser.parse(configureProvider
						.getProperty(SmsVaribles.SMS_EXPIRES_MINUTES));
				Extracter extracter = serviceSession
						.getService(Extracter.class);
				smsTasks = extracter.extract(maxCount, expirsMinutes);
			} catch (Throwable e) {
				resourceProvider.log(e);
			}
			if (smsTasks != null && smsTasks.length > 0) {
				Runner runner = new Runner(smsTasks);
				executor.submit(runner);
			}
			try {
				sleep(3000);
			} catch (InterruptedException e) {
				alive = false;
				break;
			}
		}
	}

	protected class Runner implements Runnable {
		protected SmsTask[] smsTasks;
		protected final StringBuilder sb = new StringBuilder();
		protected final String encode = "UTF-8";

		public Runner(SmsTask[] smsTasks) {
			this.smsTasks = smsTasks;
		}

		public String send(SmsTask smsTask) {

			int len = smsTask.receivers.length;
			if (len <= 0) {
				return null;
			}
			sb.setLength(0);
			try {
				sb.append("account=");
				sb.append(URLEncoder.encode(configureProvider
						.getProperty(JianZhouVaribles.SMS_USER_ID), encode));
				sb.append("&password=");
				sb.append(URLEncoder.encode(configureProvider
						.getProperty(JianZhouVaribles.SMS_USER_PASSWORD), encode));
				sb.append("&destmobile=");
				for (int i = 0; i < len; i++) {
					if (i > 0) {
						sb.append(";");
					}
					sb.append(smsTask.receivers[i]);
				}
				sb.append("&msgText=");
				sb.append(URLEncoder.encode(
						smsTask.content
								+ configureProvider
										.getProperty(JianZhouVaribles.SMS_SIGN),
						encode));
				URL postUrl = new URL(
						configureProvider.getProperty(JianZhouVaribles.SMS_URI));
				URLConnection connection = postUrl.openConnection();
				connection.setRequestProperty("accept", "*/*");
				connection.setRequestProperty("connection", "Keep-Alive");
				connection.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded");
				connection.setDoOutput(true);
				connection.setDoInput(true);
				try (OutputStreamWriter out = new OutputStreamWriter(
						connection.getOutputStream())) {
					out.write(sb.toString());
					out.flush();
				}
				String result = "";
				try (BufferedReader in = new BufferedReader(
						new InputStreamReader(connection.getInputStream()))) {
					result = in.readLine();
				}
				resourceProvider.log(String.format(
						"短信ID：%s，内容：%s，发送完成，返回状态码：%s,发送时间：%s",
						Long.toString(smsTask.id), smsTask.content, result, DateTimeParser.format(new Date())));
				return result;
			} catch (Exception e) {
				resourceProvider.log(String.format("短信ID：%s，内容：%s，发送失败,发送时间：%s",
						Long.toString(smsTask.id), smsTask.content, DateTimeParser.format(new Date())));
				resourceProvider.log(e);
				return null;
			}
		}

		@Override
		public void run() {
			if (smsTasks == null || smsTasks.length == 0) {
				return;
			}
			try (ServiceSession serviceSession = serviceProvider
					.createServiceSession()) {
				Extracter extracter = serviceSession
						.getService(Extracter.class);
				for (SmsTask smsTask : smsTasks) {
					try
					{
					String code = send(smsTask);
					extracter.mark(smsTask.id, true, code);
					}
					catch(Throwable e)
					{
						resourceProvider.log(e);
					}
				}
			} catch (Throwable e) {
				resourceProvider.log(e);
			}
		}
	}

}
