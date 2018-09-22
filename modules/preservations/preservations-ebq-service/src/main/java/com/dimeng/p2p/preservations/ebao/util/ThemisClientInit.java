package com.dimeng.p2p.preservations.ebao.util;

import org.mapu.themis.ThemisClient;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.p2p.common.ResourceProviderUtil;
import com.dimeng.p2p.preservations.ebao.variables.EbaoVariable;

/**
 * 保全客户端初始化，这里只作演示，实际项目中可将ThemisClient类配置成spring bean方式
 * @author dengwenwu
 *         Created on 2014/5/5.
 */
public class ThemisClientInit {

	//请填入服务地址（根据环境的不同选择不同的服务地址），沙箱环境，正式环境
	public static final String SERVICE_URL;

	//请填入你的APPKey
	public static final String APP_KEY;

	//请填入你的APPSecret
	public static final String APP_SECRET;


	private static ThemisClient themisClient;

	static{
		ResourceProvider resourceProvider = ResourceProviderUtil.getResourceProvider();
		ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
		SERVICE_URL = configureProvider.getProperty(EbaoVariable.EBAO_SERVICE_URL);
		APP_KEY = configureProvider.getProperty(EbaoVariable.EBAO_APP_KEY);
		APP_SECRET = configureProvider.getProperty(EbaoVariable.EBAO_APP_SECRET);

		themisClient = new ThemisClient(SERVICE_URL,APP_KEY,APP_SECRET);
	}

	public static ThemisClient getClient(){
		return themisClient;
	}

}
