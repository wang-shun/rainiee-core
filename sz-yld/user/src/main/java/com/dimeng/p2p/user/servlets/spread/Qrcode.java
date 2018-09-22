/*
 * 文 件 名:  Qrcode.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2016年1月14日
 */
package com.dimeng.p2p.user.servlets.spread;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.resource.ResourceRegister;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.account.user.service.SpreadManage;
import com.dimeng.p2p.user.servlets.qrcode.LogoConfig;
import com.dimeng.p2p.user.servlets.qrcode.ZXingCodeUtil;
import com.dimeng.p2p.user.servlets.qrcode.ZXingConfig;
import com.dimeng.p2p.variables.defines.URLVariable;
import com.google.zxing.WriterException;

/**
 * 实时生成二维码图片，可带logo
 * 
 * @author  xiaoqi
 * @version  [版本号, 2016年1月14日]
 */
public class Qrcode extends AbstractSpreadServlet {

	/**
	 * 注释内容
	 */
	private static final long serialVersionUID = 1L;
	private static final String IMAGETYPE = "JPEG";
	 
	@Override
	protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		SpreadManage manage2 = serviceSession.getService(SpreadManage.class);
	    String tgm = manage2.getMyyqNo();
	    ConfigureProvider configureProvider = ResourceRegister.getResourceProvider(getServletContext()).getResource(ConfigureProvider.class);
	    String content=configureProvider.format(URLVariable.REGISTER)+"?code="+tgm;
	    
	    try(ServletOutputStream stream= response.getOutputStream()){
	    	ZXingCodeUtil util=new ZXingCodeUtil();
		    ZXingConfig zxingconfig = new ZXingConfig();    				// 实例化二维码配置参数
	        zxingconfig.setHints(util.getDecodeHintType());   				// 设置二维码的格式参数
	        zxingconfig.setContent(content);								// 设置二维码生成内容
	        zxingconfig.setHeight(180);										// 设置二维码高度
            zxingconfig.setWidth(180);										// 设置二维码宽度
	        zxingconfig.setLogoPath("D:/logo.jpg"); 						// 设置Logo图片
	        zxingconfig.setLogoConfig(new LogoConfig());    				// Logo图片参数设置   
	        zxingconfig.setLogoFlg(true);   								// 设置生成Logo图片
	        BufferedImage bim = util.getQRCodeBufferedImage(zxingconfig);	// 生成二维码
	        ImageIO.write(bim, IMAGETYPE, stream);
	        stream.flush();
	    }catch (WriterException e) {
            e.printStackTrace();
        }
	    
	}

	 
	@Override
	protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		this.processPost(request, response, serviceSession);
	}

	
}
