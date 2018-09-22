/*
 * 文 件 名:  AbstractPdfFormationExecutor.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2016年6月20日
 */
package com.dimeng.p2p.order;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Map;

import org.apache.log4j.Logger;

import com.dimeng.framework.data.sql.SQLConnection;
import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.resource.Resource;
import com.dimeng.framework.resource.ResourceNotFoundException;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.p2p.P2PConst;
import com.dimeng.util.StringHelper;

/**
* <一句话功能简述> 生成pdf抽象类
* <功能详细描述>
* 
* @author  xiaoqi
* @version  [7.2.0, 2016年6月20日]
*/
public abstract class AbstractPdfFormationExecutor extends Resource
{
    
    private File home = null;
    
    protected final char separatorChar = File.separatorChar;;
    
    protected static final int DECIMAL_SCALE = 9;
    
    protected final Logger logger = Logger.getLogger(getClass());
    
    /** 
     * <默认构造函数>
     */
    public AbstractPdfFormationExecutor(ResourceProvider resourceProvider)
    {
        super(resourceProvider);
    }
    
    /**
     * <一句话功能简述> 将HTML文件转换为PDF
     * <功能详细描述>
     * @param htmlPath 待转换的HTML路径
     * @param contextPath  HTML引入的CSS路径
     * @param charsetName 字符编码格式
     * @throws Throwable
     */
    public abstract String convertHtml2Pdf(String htmlPath, final String contextPath, final String charsetName)
        throws Throwable;
    
    /**
     * <一句话功能简述>  根据freemarker模版和valueMap生成临时的HTML文件
     * <功能详细描述>
     * @param valueMap  
     * @param name      模版名称
     * @param content   模版内容
     * @param charset   字符集
     * @param xyNo      协议编号   
     * @return
     * @throws Throwable
     */
    public abstract String createHTML(Map<String, Object> valueMap, String fileType, String name, String content,
        String charset, String xyNo)
        throws Throwable;
    
    /**
     * <一句话功能简述> 生成文件路径
     * <功能详细描述>
     * @param fileType  文件类型
     * @param subffix   文件后缀
     * @param xyNo      协议编号
     * @return
     */
    public abstract File getFile(String fileType, String subffix, String xyNo)
        throws Throwable;
    
    /**
     * <一句话功能简述> 生成PDF存放路径
     * <功能详细描述>
     * @return
     */
    protected synchronized File getHome()
    {
        if (home == null)
        {
            String stringHome = resourceProvider.getInitParameter("fileStore.home");
            if (StringHelper.isEmpty(stringHome))
            {
                home = new File(System.getProperty("user.home"), "fileStore");
            }
            else
            {
                File file = new File(stringHome);
                if (file.isAbsolute())
                {
                    home = file;
                }
                else
                {
                    String contextHome = resourceProvider.getHome();
                    home = new File(contextHome, stringHome);
                }
            }
            home.mkdirs();
        }
        return home;
    }
    
    protected SQLConnection getConnection()
        throws SQLException
    {
        try
        {
            SQLConnectionProvider connectionProvider =
                resourceProvider.getDataConnectionProvider(SQLConnectionProvider.class, P2PConst.DB_MASTER_PROVIDER);
            return connectionProvider.getConnection();
        }
        catch (ResourceNotFoundException e)
        {
            logger.error(e, e);
            throw e;
        }
    }
    
    protected Timestamp getCurrentTimestamp(Connection connection)
        throws Throwable
    {
        try
        {
            try (PreparedStatement pstmt = connection.prepareStatement("SELECT CURRENT_TIMESTAMP()"))
            {
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return resultSet.getTimestamp(1);
                    }
                }
            }
        }
        catch (Exception e)
        {
            logger.error(e, e);
            throw e;
        }
        return null;
    }
    
    @Override
    public void initilize(Connection connection)
        throws Throwable
    {
        // TODO Auto-generated method stub
        
    }
}
