/*
 * 文 件 名:  ImageThread
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  图片加水印和压缩
 * 修 改 人:  heluzhu
 * 修改时间: 2016/8/8
 */
package com.dimeng.p2p;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.upload.FileStore;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.p2p.common.ResourceProviderUtil;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.parser.BooleanParser;
import com.dimeng.util.parser.IntegerParser;

/**
 * 图片加水印和压缩
 * <功能详细描述>
 *
 * @author heluzhu
 * @version [版本号, 2016/8/8]
 */
public class ImageThread implements Runnable {

    private ResourceProvider resourceProvider;

    private ConfigureProvider configureProvider;

    private FileStore fileStore;

    private String code;

    private int size;

    public ImageThread(String code,int size){
        this.resourceProvider = ResourceProviderUtil.getResourceProvider();
        this.configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        fileStore = resourceProvider.getResource(FileStore.class);
        this.code = code;
        this.size = size;
    }
    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p/>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        String url = fileStore.getURL(fileStore.getFileInformation(code));
        if(BooleanParser.parse(configureProvider.getProperty(SystemVariable.IS_OPEN_ZIP_IMAGE))) {
            int limitSize = IntegerParser.parse(configureProvider.getProperty(SystemVariable.IMAGE_NEED_ZIP_SIZE));
            if(size/1024 > limitSize)
            {
                ImageUtil.resizeImage(url, url);
            }
        }
        boolean isOpenWaterImage = BooleanParser.parse(configureProvider.getProperty(SystemVariable.IS_OPEN_WATERIMAGE));
        if(isOpenWaterImage) {
            ImageUtil.pressImage(fileStore.getURL(configureProvider.getProperty(SystemVariable.WATERIMAGE)), url, url);
        }
    }
}
