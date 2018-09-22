/*
 * 文 件 名:  ImageUtil
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  图片处理
 * 修 改 人:  heluzhu
 * 修改时间: 2016/8/3
 */
package com.dimeng.p2p;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.resource.ResourceRetention;
import com.dimeng.p2p.common.ResourceProviderUtil;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BooleanParser;
import com.dimeng.util.parser.IntegerParser;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.im4java.core.CompositeCmd;
import org.im4java.core.ConvertCmd;
import org.im4java.core.GMOperation;
import org.im4java.core.IM4JavaException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.UUID;

/**
 * 图片处理
 * <功能详细描述>
 *
 * @author heluzhu
 * @version [版本号, 2016/8/3]
 */
public class ImageUtil {

    private static final Logger logger = Logger.getLogger(ImageUtil.class);

    /**
     * 给图片加水印图（验证码）
     * @param iconPath
     * @param buffImg
     */
    public static BufferedImage pressVerifyImage(String iconPath, BufferedImage buffImg) {
        try {
            Image srcImg = buffImg.getScaledInstance(buffImg.getWidth(),
                    buffImg.getHeight(), BufferedImage.TYPE_INT_RGB);
            // 得到画笔对象
            Graphics2D g = buffImg.createGraphics();
            // 设置对线段的锯齿状边缘处理
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(srcImg.getScaledInstance(srcImg.getWidth(null), srcImg
                    .getHeight(null), Image.SCALE_SMOOTH), 0, 0, null);
            // 水印图象的路径 水印一般为gif或者png的，这样可设置透明度
            ImageIcon imgIcon = new ImageIcon(iconPath);
            // 得到Image对象。
            Image img = imgIcon.getImage();
            float alpha = 0.5f; // 透明度
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
                    alpha));
            // 表示水印图片的位置
            g.drawImage(img, 0, 0, null);

            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));

            g.dispose();
            logger.info("图片完成水印处理");
            return buffImg;
        } catch (Exception e) {
            logger.error(e, e);
        }
        return null;
    }

    /**
     * 给图片加水印图（附件上传）
     * @param iconPath
     * @param srcImgPath
     * @param targerPath
     */
    public final static void pressImage(String iconPath, String srcImgPath,
                                        String targerPath) {
        try {
            if(StringHelper.isEmpty(iconPath) || StringHelper.isEmpty(srcImgPath))
            {
                logger.info("图片路径不能为空");
                return;
            }
            iconPath = rewriteUrl(iconPath);
            srcImgPath = rewriteUrl(srcImgPath);
            targerPath = rewriteUrl(targerPath);
            if(!StringHelper.isEmpty(targerPath))
            {
                String zipUrl = targerPath.substring(0,targerPath.lastIndexOf("."))+"_zip"+targerPath.substring(targerPath.indexOf("."));
                if(new File(zipUrl).exists())
                {
                    srcImgPath = zipUrl;
                    targerPath = targerPath.substring(0,targerPath.lastIndexOf("."))+"_zip_w"+targerPath.substring(targerPath.indexOf("."));
                }else {
                    targerPath = targerPath.substring(0, targerPath.lastIndexOf(".")) + "_w" + targerPath.substring(targerPath.indexOf("."));
                }
            }
            Image srcImg = ImageIO.read(new File(srcImgPath));

            BufferedImage buffImg = new BufferedImage(srcImg.getWidth(null),
                    srcImg.getHeight(null), BufferedImage.TYPE_INT_RGB);

            // 得到画笔对象
            Graphics2D g = buffImg.createGraphics();

            // 设置对线段的锯齿状边缘处理
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            g.drawImage(srcImg.getScaledInstance(srcImg.getWidth(null), srcImg
                    .getHeight(null), Image.SCALE_SMOOTH), 0, 0, null);

            // 水印图象的路径 水印一般为gif或者png的，这样可设置透明度
            ImageIcon imgIcon = new ImageIcon(iconPath);

            // 得到Image对象。
            Image img = imgIcon.getImage();
            // 透明度
            float alpha = 0.5f;
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
                    alpha));
            int interval = IntegerParser.parse(ResourceProviderUtil.getResourceProvider().getResource(ConfigureProvider.class).getProperty(SystemVariable.IMAGE_WATER_INTERVAL));
            // 水印图片的位置
            for (int height = interval + imgIcon.getIconHeight(); height < buffImg
                    .getHeight(); height = height +interval+ imgIcon.getIconHeight()) {
                for (int weight = interval + imgIcon.getIconWidth(); weight < buffImg
                        .getWidth(); weight = weight +interval+ imgIcon.getIconWidth()) {
                    g.drawImage(img, weight - imgIcon.getIconWidth(), height
                            - imgIcon.getIconHeight(), null);
                }
            }

            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));

            g.dispose();

            try(FileOutputStream os = new FileOutputStream(targerPath)){
                // 生成图片
                ImageIO.write(buffImg, "JPG", os);
            }

        } catch (Throwable e) {
            logger.error(e,e);
        }
    }

    /**
     * 重写图片路径
     * @param url
     * @return
     */
    private static String rewriteUrl(String url)
    {
        ResourceProvider resourceProvider = ResourceProviderUtil.getResourceProvider();
        String repStr = resourceProvider.getContextPath();
        if(!StringHelper.isEmpty(url)) {
            url = url.replace(repStr, "");
        }
        String stringHome = resourceProvider.getInitParameter("fileStore.home");
        if(StringHelper.isEmpty(stringHome))
        {
            stringHome = System.getProperty("user.home");
        }
        if(stringHome.indexOf("fileStore") != -1)
        {
            stringHome = stringHome.replace("/fileStore","");
        }
        url = stringHome.replace("\\","/") + url;
        return url;
    }

    /**
     * 根据尺寸缩放图片
     * @param width     缩放后的图片宽度
     * @param height    缩放后的图片高度
     * @param srcPath   源图片路径
     * @param newPath   缩放后图片的路径
     * @param type      压缩方式：1为像素，2为百分比处理，如（像素大小：1024x1024,百分比：50%x50%）
     * @param quality   图片质量
     */
    public static void cutImage(int width, int height, String srcPath, String newPath, int type, double quality) throws Exception{
        srcPath = rewriteUrl(srcPath);
        newPath = rewriteUrl(newPath);
        GMOperation op = new GMOperation();
        op.addImage(srcPath);
        String raw = 1 == type ? width+"x"+height+"!" : width+"%x"+height+"%";
        //压缩大小
        op.addRawArgs("-thumbnail", raw);
        //图片质量
        op.quality(quality);
        op.addImage(newPath);
        //如果使用ImageMagick，设为false,使用GraphicsMagick，就设为true，默认为false
        ConvertCmd convert = new ConvertCmd(true);
        ResourceProvider resourceProvider = ResourceProviderUtil.getResourceProvider();
        String retention = resourceProvider.getInitParameter("retention");
        if(ResourceRetention.DEVELOMENT.name().equals(retention)) {
            //linux下不要设置此值，不然会报错
            convert.setSearchPath("D:\\program files\\graphicsmagick-1.3.18-q8");
        }
        //convert.setSearchPath("D:\\program files\\graphicsmagick-1.3.18-q8");
        convert.run(op);
    }

    public static void main(String[] args) {
        try {
            //cutImage(120,120,"e:/images/1.jpg","e:/images/yzm5.jpg",2,100);
            //addImgText("e:/images/yzm5.jpg");
            //resize("","e:/images/1.jpg", "e:/images/yzm5.jpg", 50, 50);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    /**
     * 给图片加水印
     * @param srcPath   源图片路径
     */
    public static void addImgText(String srcPath) throws Exception {
        GMOperation op = new GMOperation();
        String content = "这是一个水印";
        op.font("宋体").gravity("southeast").pointsize(18).fill("#BCBFC8").draw(new String(content.getBytes(), "utf-8"));
        op.addImage();
        op.addImage();
        ConvertCmd convert = new ConvertCmd(true);
        //linux下不要设置此值，不然会报错
        convert.setSearchPath("D:\\program files\\graphicsmagick-1.3.18-q8");
        convert.run(op,srcPath,srcPath);
    }


    /**
     * 压缩图片
     * @param installPath GraphicsMagick的安装目录（只有windows系统需要配置）
     * @param sourceImage 原图片
     * @param width 压缩后的宽
     * @param height 压缩后的高
     * @return
     */
    public static byte[] resize(String installPath,byte[] sourceImage, Integer width, Integer height)
    {
        try {
            if(width == null || height == null) return sourceImage;
            String tempDir = rewriteUrl("");
            String imageName = UUID.randomUUID().toString();
            File sourceFile = new File(tempDir + "/" + imageName);
            FileUtils.writeByteArrayToFile(sourceFile, sourceImage);
            String sourcePath = sourceFile.getAbsolutePath();
            String targetPath = sourcePath + "_target";
            resize(installPath, sourcePath, targetPath, width, height);
            File targetFile = new File(targetPath);
            byte[] result = FileUtils.readFileToByteArray(targetFile);
            FileUtils.deleteQuietly(sourceFile);
            FileUtils.deleteQuietly(targetFile);
            return result;
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
        return null;
    }

    /**
     * 压缩图片
     * @param installPath GraphicsMagick的安装目录（只有windows系统需要配置）
     * @param sourcePath 源文件路径
     * @param targetPath 缩略图路径
     * @param width 设定宽
     * @param height 设定长
     */
    public static void resize(String installPath,String sourcePath, String targetPath, Integer width, Integer height)
    {
        try {
            boolean isEqualRatio = BooleanParser.parse(ResourceProviderUtil.getResourceProvider().getResource(ConfigureProvider.class).getProperty(SystemVariable.IMAGE_ZIP_TYPE));
            GMOperation  op = new GMOperation();
            if(isEqualRatio){
                op.resize(width, height);
            }else{
                op.resize(width, height,'!');
            }

            op.addImage(sourcePath);
            if(!StringHelper.isEmpty(targetPath))
            {
                targetPath = targetPath.substring(0,targetPath.lastIndexOf("."))+"_zip"+targetPath.substring(targetPath.indexOf("."));
            }
            op.addImage(targetPath);
            ConvertCmd convert = new ConvertCmd(true);
            ResourceProvider resourceProvider = ResourceProviderUtil.getResourceProvider();
            String retention = resourceProvider.getInitParameter("retention");
            if(ResourceRetention.PRE_PRODUCTION.name().equals(retention)) {
                //linux下不要设置此值，不然会报错
                convert.setSearchPath(installPath);
            }
            convert.run(op);
        } catch(Exception e) {
            logger.error(e, e);
        }
    }

    /**
     *
     * @param sourcePath 源文件路径
     * @param targetPath 缩略图路径
     */
    public static void resizeImage(String sourcePath, String targetPath)
    {
        sourcePath = rewriteUrl(sourcePath);
        targetPath = rewriteUrl(targetPath);
        ResourceProvider resourceProvider = ResourceProviderUtil.getResourceProvider();
        ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        String graphicsPath = configureProvider.getProperty(SystemVariable.GRAPHICSMAGICK_INSTALL_PATH);
        String size = configureProvider.getProperty(SystemVariable.IMAGE_ZIP_SIZE);
        int width = 500;
        int height = 500;
        if (!StringHelper.isEmpty(size) && size.indexOf("X") != -1) {
            width = IntegerParser.parse(size.split("X")[0]);
            height = IntegerParser.parse(size.split("X")[1]);
        }
        resize(graphicsPath, sourcePath, targetPath, width, height);
    }

    /**
     * 图片加水印
     *
     * @param sourcePath 源文件路径
     * @param targetPath 修改图路径
     * @param logoPath logo图路径
     */
    public static void watermark(String sourcePath, String targetPath, String logoPath)
    {
        sourcePath = rewriteUrl(sourcePath);
        targetPath = rewriteUrl(targetPath);
        logoPath = rewriteUrl(logoPath);
        GMOperation op = new GMOperation();
        op.addRawArgs("-gravity", "center");
        //op.addRawArgs("-gravity","100");
        op.addImage(logoPath);
        op.addImage(sourcePath);
        op.addImage(targetPath);
        CompositeCmd cmd = new CompositeCmd(true);
        try {
            ResourceProvider resourceProvider = ResourceProviderUtil.getResourceProvider();
            String retention = resourceProvider.getInitParameter("retention");
            if(ResourceRetention.PRE_PRODUCTION.name().equals(retention)) {
                //linux下不要设置此值，不然会报错
                cmd.setSearchPath("D:\\program files\\graphicsmagick-1.3.18-q8");
            }
            cmd.run(op);
        } catch (IOException e) {
            logger.error(e, e);
        } catch (InterruptedException e) {
            logger.error(e, e);
        } catch (IM4JavaException e) {
            logger.error(e, e);
        }
    }


    /**
     * 将输入流转换为byte
     * @param inStream
     * @return
     * @throws IOException
     */
    private static final byte[] input2byte(InputStream inStream)
            throws IOException {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        while ((rc = inStream.read(buff, 0, 100)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        byte[] in2b = swapStream.toByteArray();
        return in2b;
    }

    /**
     * 将byte转换为流
     * @param buf
     * @return
     */
    private static final InputStream byte2Input(byte[] buf) {
        return new ByteArrayInputStream(buf);
    }
}
