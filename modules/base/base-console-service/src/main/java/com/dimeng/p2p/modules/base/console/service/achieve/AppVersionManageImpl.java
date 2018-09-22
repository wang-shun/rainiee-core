package com.dimeng.p2p.modules.base.console.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;

import com.dimeng.framework.http.upload.UploadFile;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S71.entities.T7180;
import com.dimeng.p2p.common.FileUploader;
import com.dimeng.p2p.modules.base.console.service.AppVersionManage;
import com.dimeng.p2p.modules.base.console.service.entity.AppVersion;
import com.dimeng.p2p.modules.base.console.service.entity.AppVersionInfo;
import com.dimeng.util.StringHelper;

public class AppVersionManageImpl extends AbstractInformationService implements
		AppVersionManage {

	public static class TermManageFactory implements
			ServiceFactory<AppVersionManage> {

		@Override
		public AppVersionManage newInstance(ServiceResource serviceResource) {
			return new AppVersionManageImpl(serviceResource);
		}

	}

	protected static final ArrayParser<AppVersionInfo> ARRAY_PARSER = new ArrayParser<AppVersionInfo>() {

		@Override
		public AppVersionInfo[] parse(ResultSet rs) throws SQLException {
			ArrayList<AppVersionInfo> list = null;
			while (rs.next()) {
			    AppVersionInfo vinfo = new AppVersionInfo();
				vinfo.id = rs.getInt(1);
				vinfo.verType = rs.getInt(2); 
				vinfo.verNO = rs.getString(3);
				vinfo.isMustUpdate = rs.getInt(4);
				vinfo.mark = rs.getString(5);
				vinfo.file = rs.getString(6); 
				vinfo.status = rs.getInt(7);
				vinfo.publishTime = rs.getTimestamp(8);
				vinfo.publisherId = rs.getInt(9); 
				vinfo.updateTime = rs.getTimestamp(10);
				vinfo.updateId = rs.getInt(11);
				vinfo.publisher = rs.getString(12);
				vinfo.updater = rs.getString(13);
				vinfo.url = rs.getString(14);
				if (list == null) {
					list = new ArrayList<>();
				}
				list.add(vinfo);
			}
			return list == null ? null : list.toArray(new AppVersionInfo[list.size()]);
		}
	};
	protected static final ItemParser<T7180> ITEM_PARSER = new ItemParser<T7180>() {
		@Override
		public T7180 parse(ResultSet rs) throws SQLException {
			T7180 t7180 = null;
			if (rs.next()) {
				t7180 = new T7180();
				t7180.F01 = rs.getInt(1);
				t7180.F02 = rs.getInt(2);
				t7180.F03 = rs.getString(3);
				t7180.F04 = rs.getInt(4);
				t7180.F05 = rs.getString(5);
				t7180.F06 = rs.getString(6);
				t7180.F07 = rs.getInt(7);
				t7180.F08 = rs.getTimestamp(8);
				t7180.F09 = rs.getString(9);
				t7180.F10 = rs.getTimestamp(10);
				t7180.F11 = rs.getString(11); 
				t7180.F12 = rs.getString(12); 
			}
			return t7180;
		}
	};

	public AppVersionManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	@Override
    public int addAppVersion(AppVersion appVer, ResourceProvider rp)
        throws Throwable
    {
        if (StringHelper.isEmpty(appVer.getVerNO()))
        {
            throw new ParameterException("版本号不能为空");
        }
        if (StringHelper.isEmpty(appVer.getMark()))
        {
            throw new ParameterException("升级描述不能为空");
        }
        String fileCode = "";
        FileUploader fileUploader = new FileUploader(rp);
        UploadFile uploadFile = appVer.getFile();
        if (uploadFile != null)
        {// 保存file 
            fileUploader.uploadFile(appVer.getFileName(), uploadFile);
            fileCode = appVer.getFileName();
        }
        
        Timestamp now = new Timestamp(System.currentTimeMillis());
        try (Connection connection = getConnection())
        {
            int id =
                insert(connection,
                    "INSERT INTO S71.T7180 SET F02 = ?,F03 = ?,F04 = ?,F05 = ?,F06 = ?,F07 = ?,F08 = ?,F09 = ?,F10 = ?,F11 = ?,F12 = ?",
                    appVer.getVerType(),
                    appVer.getVerNO(),
                    appVer.getIsMustUpdate(),
                    appVer.getMark(),
                    fileCode,
                    appVer.getStatus(),
                    now,
                    String.valueOf(serviceResource.getSession().getAccountId()),
                    now,
                        String.valueOf(serviceResource.getSession().getAccountId()),
                            appVer.getUrl());
            return id;
        }
    }

	@Override
	public void updateAppVersion(AppVersion appVer, int id , ResourceProvider rp) throws Throwable {

		String fileCode = ""; 
		FileUploader fileUploader = new FileUploader(rp);
		UploadFile uploadFile = appVer.getFile();
		if (uploadFile != null) {// 保存file 
		    fileUploader.uploadFile(appVer.getFileName(), uploadFile);
		    fileCode = appVer.getFileName();
		}
		StringBuffer sql = new StringBuffer("UPDATE S71.T7180 SET ");
		List<Object> paras = new ArrayList<Object>();
		
		sql.append(" F10=? ");
		paras.add(new Timestamp(System.currentTimeMillis()));
		sql.append(" ,F11=? ");
		paras.add(serviceResource.getSession().getAccountId()+"");
		if (appVer.getVerType() > 0) {
            sql.append(" ,F02=? ");
            paras.add(appVer.getVerType());
            
        }
		if (!StringHelper.isEmpty(appVer.getVerNO())) {
            sql.append(" ,F03=? ");
            paras.add(appVer.getVerNO());
            
        }
		if (appVer.getIsMustUpdate()>=0) {
			sql.append(" ,F04=? ");
			paras.add(appVer.getIsMustUpdate());
			
		}
		if (!StringHelper.isEmpty(appVer.getMark())) {
			sql.append(" ,F05=? ");
			paras.add(appVer.getMark());
			
		}
		if (!StringHelper.isEmpty(fileCode)) {
			sql.append(" ,F06=? ");
			paras.add(fileCode);
		} 
		if (appVer.getStatus()>=0) {
			sql.append(" ,F07=? ");
			paras.add(appVer.getStatus());
		}  
        sql.append(" ,F12=? ");
        paras.add(appVer.getUrl());
       
		sql.append(" where F01 = ? ");
		paras.add(id);
	
		try (Connection connection = getConnection()) {

			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F01 FROM S71.T7180 WHERE F01 = ? FOR UPDATE")) {
				pstmt.setInt(1, id);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (!resultSet.next()) {
						throw new LoginException("此APP版本不存在");
					}
				}
			}
			// 修改记录信息
			execute(connection, sql.toString(),paras);
		}

	}

	@Override
    public AppVersionInfo[] searchAppVersion(AppVersion appVer, int id)
        throws Throwable
    {
        
        StringBuffer sql =
            new StringBuffer(
                "SELECT t.F01,t.F02,t.F03,t.F04,t.F05,t.F06,t.F07,t.F08,t.F09,t.F10,t.F11 , u.F02  AS creater , u1.f02 AS updater , t.F12 FROM S71.T7180 t , S71.T7110 u , S71.T7110 u1  WHERE u.F01 = t.F09 AND u1.f01 = t.f11 ");
        List<Object> paras = new ArrayList<Object>();
        
        if (appVer.getVerType() > 0)
        {
            sql.append(" and t.F02=? ");
            paras.add(appVer.getVerType());
            
        }
        
        if (!StringHelper.isEmpty(appVer.getVerNO()))
        {
            sql.append(" and t.F03=? ");
            paras.add(appVer.getVerNO());
            
        }
        if (appVer.getIsMustUpdate() >= 0)
        {
            sql.append(" and t.F04=? ");
            paras.add(appVer.getIsMustUpdate());
            
        }
        if (!StringHelper.isEmpty(appVer.getMark()))
        {
            sql.append(" and t.F05=? ");
            paras.add(appVer.getMark());
            
        }
        if (appVer.getStatus() >= 0)
        {
            sql.append(" and t.F07=? ");
            paras.add(appVer.getStatus());
        }
        if (id > 0)
        {
            sql.append(" and t.F01=? ");
            paras.add(id);
        }
        sql.append(" ORDER BY t.F03 DESC , t.F01 desc ");
        if (appVer.getLimit())
        {
            sql.append(" LIMIT 1 ");
        }
        try (Connection connection = getConnection())
        {
            return selectAll(connection, ARRAY_PARSER, sql.toString(), paras);
        }
    }

	@Override
	public void delAppVersion(int id) throws Throwable {

		StringBuffer sql = new StringBuffer("DELETE FROM S71.T7180 where F01 = ? ");
		List<Object> paras = new ArrayList<Object>();
		paras.add(id);
		try (Connection connection = getConnection()) {
			// 删除记录信息
			execute(connection, sql.toString(), paras);
		}
	}

    /** {@inheritDoc} */
    
    @Override
    public PagingResult<AppVersionInfo> searchAppVersionPaging(AppVersion query, Paging paging)
        throws Throwable
    {
        StringBuffer sql =
            new StringBuffer(
                "SELECT t.F01,t.F02,t.F03,t.F04,t.F05,t.F06,t.F07,t.F08,t.F09,t.F10,t.F11 , u.F02  AS creater , u1.f02 AS updater , t.F12 FROM S71.T7180 t , S71.T7110 u , S71.T7110 u1  WHERE u.F01 = t.F09 AND u1.f01 = t.f11 ");
        List<Object> paras = new ArrayList<Object>();
        if (query.getVerType() > 0)
        {
            sql.append(" and t.F02=? ");
            paras.add(query.getVerType());
            
        }
        
        if (!StringHelper.isEmpty(query.getVerNO()))
        {
            sql.append(" and t.F03 like ? ");
            paras.add(getSQLConnectionProvider().allMatch(query.getVerNO()));
            
        }
        if (query.getIsMustUpdate() >= 0)
        {
            sql.append(" and t.F04=? ");
            paras.add(query.getIsMustUpdate());
            
        }
        if (!StringHelper.isEmpty(query.getPublisher()))
        {
            sql.append(" and u.F02 like ? ");
            paras.add(getSQLConnectionProvider().allMatch(query.getPublisher()));
            
        }
        if (query.getStatus() >= 0)
        {
            sql.append(" and t.F07=? ");
            paras.add(query.getStatus());
        }
        if (query.getStartPublishDate() != null)
        {
            sql.append(" and DATE(t.F08)>=? ");
            paras.add(query.getStartPublishDate());
        }
        if (query.getEndPublishDate() != null)
        {
            sql.append(" and DATE(t.F08)<=? ");
            paras.add(query.getEndPublishDate());
        }
        sql.append(" ORDER BY t.F03 DESC , t.F01 desc ");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<AppVersionInfo>()
            {
                
                @Override
                public AppVersionInfo[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<AppVersionInfo> list = null;
                    while (resultSet.next())
                    {
                        AppVersionInfo record = new AppVersionInfo();
                        record.id = resultSet.getInt(1);
                        record.verType = resultSet.getInt(2);
                        record.verNO = resultSet.getString(3);
                        record.isMustUpdate = resultSet.getInt(4);
                        record.mark = resultSet.getString(5);
                        record.file = resultSet.getString(6);
                        record.status = resultSet.getInt(7);
                        record.publishTime = resultSet.getTimestamp(8);
                        record.publisherId = resultSet.getInt(9);
                        record.updateTime = resultSet.getTimestamp(10);
                        record.updateId = resultSet.getInt(11);
                        record.publisher = resultSet.getString(12);
                        record.updater = resultSet.getString(13);
                        record.url = resultSet.getString(14);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return ((list == null || list.size() == 0) ? null : list.toArray(new AppVersionInfo[list.size()]));
                }
            }, paging, sql.toString(), paras);
        }
    }

}
