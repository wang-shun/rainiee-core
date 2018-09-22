package com.dimeng.p2p.modules.base.console.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.dimeng.framework.http.upload.FileStore;
import com.dimeng.framework.http.upload.UploadFile;
import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.modules.base.console.service.AppStartFindSetManage;
import com.dimeng.p2p.modules.base.console.service.entity.AdvertisementRecord;
import com.dimeng.p2p.modules.base.console.service.entity.AdvertisementType;
import com.dimeng.p2p.variables.FileType;

public class AppStartFindSetManageImpl extends AbstractInformationService implements
 AppStartFindSetManage
{

    public AppStartFindSetManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
        // TODO Auto-generated constructor stub
    }

    public static class TermManageFactory implements
 ServiceFactory<AppStartFindSetManage>
    {

		@Override
        public AppStartFindSetManage newInstance(ServiceResource serviceResource)
        {
			return new AppStartFindSetManageImpl(serviceResource);
		}

	}

    @Override
    public void updateAppStartFindPic(AdvertisementType advertisement)
        throws Throwable
    {
        if (advertisement == null)
        {
            throw new ParameterException("没有给出启动发现页信息");
        }
        String url = advertisement.getURL();
        UploadFile image = advertisement.getImage();
        String advType = advertisement.getAdvType();
        String title = advertisement.getTitle();
        FileStore fileStore = serviceResource.getResource(FileStore.class);
        String imageCode = "";
        if(image != null){
            imageCode = fileStore.upload(FileType.ADVERTISEMENT_IMAGE.ordinal(), image)[0];
        }

        try (Connection connection = getConnection())
        {
            Timestamp creationTime = getCurrentTimestamp(connection);
            try (PreparedStatement pstmt = connection.prepareStatement("SELECT F01 FROM S50.T5016 WHERE F12 = ?"))
            {
                pstmt.setString(1, advType);
                try (ResultSet rs = pstmt.executeQuery())
                {
                    if (rs.next())
                    {
                        StringBuffer sql = new StringBuffer("UPDATE T5016 SET F04 = ?,");

                        if (image != null)
                        {
                            sql.append("F05=?,");
                        }
                        sql.append("F07 = ?,F08 = ?,F09 = ? WHERE F12 = ? ");

                        if (image != null)
                        {
                            execute(connection,
                                    sql.toString(),
                                    url,
                                    imageCode,
                                    creationTime,
                                    creationTime,
                                    creationTime,
                                    advType);
                        }else {
                            execute(connection,
                                    sql.toString(),
                                    url,
                                    creationTime,
                                    creationTime,
                                    creationTime,
                                    advType);
                        }


                    }
                    else
                    {
                        if (image != null)
                        {
                        insert(connection,
                            "INSERT INTO T5016 SET F03 = ?,F04 = ?,F05 = ?,F06 = ?,F07 = ?,F08 = ?,F09 = ?, F12 = ?",
                            title,
                            url,
                            imageCode,
                            serviceResource.getSession().getAccountId(),
                            creationTime,
                            creationTime,
                            creationTime,
                            advType);
                        }
                    }
                }
            }
        }
    }

    @Override
    public AdvertisementRecord[] search()
        throws Throwable
    {
        ArrayList<AdvertisementRecord> list = new ArrayList<>();
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F03,F04,F05,F12 FROM S50.T5016 WHERE F12 IN ('IOSPIC','FINDPIC','ANDROIDPIC')"))
            {
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        AdvertisementRecord record = new AdvertisementRecord();
                        record.title = resultSet.getString(1);
                        record.url = resultSet.getString(2);
                        record.imageCode = resultSet.getString(3);
                        record.advType = resultSet.getString(4);
                        list.add(record);
                    }
                }
            }
        }
        return list.toArray(new AdvertisementRecord[list.size()]);
    }

    @Override
    public AdvertisementRecord selectPic(String advType)
        throws Throwable
    {
        AdvertisementRecord record = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F03,F04,F05,F12 FROM S50.T5016 WHERE F12 = ?"))
            {
                pstmt.setString(1, advType);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new AdvertisementRecord();
                        record.title = resultSet.getString(1);
                        record.url = resultSet.getString(2);
                        record.imageCode = resultSet.getString(3);
                        record.advType = resultSet.getString(4);
                    }
                }
            }
        }
        return record;
    }

}
