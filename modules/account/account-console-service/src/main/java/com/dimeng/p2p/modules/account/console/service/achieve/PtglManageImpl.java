package com.dimeng.p2p.modules.account.console.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.upload.FileStore;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S71.entities.T7101;
import com.dimeng.p2p.modules.account.console.service.PtglManage;
import com.dimeng.p2p.modules.account.console.service.entity.Ptgl;
import com.dimeng.p2p.variables.LogoFileType;
import com.dimeng.p2p.variables.defines.SystemVariable;

public class PtglManageImpl extends AbstractUserService implements PtglManage
{
    
    public PtglManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public T7101 search()
        throws Throwable
    {
        T7101 record = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11, F12 FROM S71.T7101 LIMIT 1"))
            {
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new T7101();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getString(2);
                        record.F03 = resultSet.getString(3);
                        record.F04 = resultSet.getString(4);
                        record.F05 = resultSet.getString(5);
                        record.F06 = resultSet.getString(6);
                        record.F07 = resultSet.getString(7);
                        record.F08 = resultSet.getString(8);
                        record.F09 = resultSet.getString(9);
                        record.F10 = resultSet.getString(10);
                        record.F11 = resultSet.getString(11);
                        record.F12 = resultSet.getString(12);
                    }
                }
            }
        }
        return record;
    }
    
    @Override
    public void update(Ptgl ptgl)
        throws Throwable
    {
        String imageCode = null;
        FileStore fileStore = serviceResource.getResource(FileStore.class);
        ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                if (ptgl.qtlg != null)
                {// 保存封面图片
                    imageCode = fileStore.upload(LogoFileType.LOGO_IMAGE.ordinal(), ptgl.qtlg)[0];
                    checkImag(imageCode);
                    execute(connection, "UPDATE S71.T7101 SET F02 = ? WHERE F01 = ?", imageCode, ptgl.F01);
                    configureProvider.set(SystemVariable.QTLOGO.getKey(), imageCode);
                }
                
                if (ptgl.qtywlg != null)
                {// 保存封面图片
                    imageCode = fileStore.upload(LogoFileType.LOGO_IMAGE.ordinal(), ptgl.qtywlg)[0];
                    checkImag(imageCode);
                    execute(connection, "UPDATE S71.T7101 SET F12 = ? WHERE F01 = ?", imageCode, ptgl.F01);
                    configureProvider.set(SystemVariable.QTYWLOGO.getKey(), imageCode);
                }
                
                if (ptgl.htdl != null)
                {// 保存封面图片
                    imageCode = fileStore.upload(LogoFileType.LOGO_IMAGE.ordinal(), ptgl.htdl)[0];
                    checkImag(imageCode);
                    execute(connection, "UPDATE S71.T7101 SET F03 = ? WHERE F01 = ?", imageCode, ptgl.F01);
                    configureProvider.set(SystemVariable.HTDLLOGO.getKey(), imageCode);
                }
                
                if (ptgl.htsy != null)
                {// 保存封面图片
                    imageCode = fileStore.upload(LogoFileType.LOGO_IMAGE.ordinal(), ptgl.htsy)[0];
                    checkImag(imageCode);
                    execute(connection, "UPDATE S71.T7101 SET F04 = ? WHERE F01 = ?", imageCode, ptgl.F01);
                    configureProvider.set(SystemVariable.HTSYLOGO.getKey(), imageCode);
                }
                
                if (ptgl.qtwx != null)
                {// 保存微信二维码
                    imageCode = fileStore.upload(LogoFileType.LOGO_IMAGE.ordinal(), ptgl.qtwx)[0];
                    checkImag(imageCode);
                    execute(connection, "UPDATE S71.T7101 SET F05 = ? WHERE F01 = ?", imageCode, ptgl.F01);
                    configureProvider.set(SystemVariable.QTWXEWM.getKey(), imageCode);
                }
                if (ptgl.qtwb != null)
                {// 保存微信二维码
                    imageCode = fileStore.upload(LogoFileType.LOGO_IMAGE.ordinal(), ptgl.qtwb)[0];
                    checkImag(imageCode);
                    execute(connection, "UPDATE S71.T7101 SET F06 = ? WHERE F01 = ?", imageCode, ptgl.F01);
                    configureProvider.set(SystemVariable.QTWBEWM.getKey(), imageCode);
                }
                if (ptgl.qtapp != null)
                {// 保存微信二维码
                    imageCode = fileStore.upload(LogoFileType.LOGO_IMAGE.ordinal(), ptgl.qtapp)[0];
                    checkImag(imageCode);
                    execute(connection, "UPDATE S71.T7101 SET F07 = ? WHERE F01 = ?", imageCode, ptgl.F01);
                    configureProvider.set(SystemVariable.QTSJKHD.getKey(), imageCode);
                }
                if (ptgl.qtbd != null)
                {// 保存微信二维码
                    imageCode = fileStore.upload(LogoFileType.LOGO_IMAGE.ordinal(), ptgl.qtbd)[0];
                    checkImag(imageCode);
                    execute(connection, "UPDATE S71.T7101 SET F08 = ? WHERE F01 = ?", imageCode, ptgl.F01);
                    configureProvider.set(SystemVariable.BDMRTB.getKey(), imageCode);
                }
                if (ptgl.sytb != null)
                {// 保存平台水印图标
                    imageCode = fileStore.upload(LogoFileType.LOGO_IMAGE.ordinal(), ptgl.sytb)[0];
                    checkImag(imageCode);
                    execute(connection, "UPDATE S71.T7101 SET F09 = ? WHERE F01 = ?", imageCode, ptgl.F01);
                    configureProvider.set(SystemVariable.WATERIMAGE.getKey(), imageCode);
                }
                if (ptgl.txnantb != null)
                {// 用户中心头像图标（男）
                    imageCode = fileStore.upload(LogoFileType.LOGO_IMAGE.ordinal(), ptgl.txnantb)[0];
                    checkImag(imageCode);
                    execute(connection, "UPDATE S71.T7101 SET F10 = ? WHERE F01 = ?", imageCode, ptgl.F01);
                    configureProvider.set(SystemVariable.TXNANTB.getKey(), imageCode);
                }
                if (ptgl.txnvtb != null)
                {// 用户中心头像图标（女）
                    imageCode = fileStore.upload(LogoFileType.LOGO_IMAGE.ordinal(), ptgl.txnvtb)[0];
                    checkImag(imageCode);
                    execute(connection, "UPDATE S71.T7101 SET F11 = ? WHERE F01 = ?", imageCode, ptgl.F01);
                    configureProvider.set(SystemVariable.TXNVTB.getKey(), imageCode);
                }
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    /**
     * <检验图片格式>
     * <功能详细描述>
     * @param fileName
     * @throws Throwable
     */
    private void checkImag(String fileName)
        throws Throwable
    {
        fileName = fileName.toLowerCase();
        if (!fileName.endsWith(".png") && !fileName.endsWith(".jpg") && !fileName.endsWith(".jpeg"))
        {
            throw new ParameterException("只支持png、jpg、jpeg格式图片。");
        }
    }
    
    @Override
    public T7101 get(int id)
        throws Throwable
    {
        if (id <= 0)
        {
            throw new ParameterException("参数不能为空.");
        }
        
        T7101 record = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04 FROM S71.T7101 WHERE F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, id);
                
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new T7101();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getString(2);
                        record.F03 = resultSet.getString(3);
                        record.F04 = resultSet.getString(4);
                    }
                }
            }
        }
        return record;
    }
}
