package com.dimeng.p2p.modules.statistics.console.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.modules.statistics.console.service.UserOnlineManage;
import com.dimeng.p2p.modules.statistics.console.service.entity.UserOnlineData;
import com.dimeng.util.parser.DateParser;

public class UserOnlineManageImpl extends AbstractStatisticsService implements
		UserOnlineManage {

	public UserOnlineManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	@Override
	public UserOnlineData[] getUserOnlineDatas(Date date) throws Throwable {
		List<UserOnlineData> onlineDatas = new ArrayList<>();
		if(date != null){
			try(Connection connection = getConnection()){
				try(PreparedStatement ps = connection.prepareStatement("SELECT F02,F03 FROM S70.T7036 WHERE F01=?")){
					ps.setString(1, DateParser.format(date));
					try(ResultSet resultSet = ps.executeQuery()){
						while(resultSet.next()){
							UserOnlineData onlineData = new UserOnlineData();
							onlineData.partTime = resultSet.getInt(1);
							onlineData.count = resultSet.getInt(2);
							onlineDatas.add(onlineData);
						}
					}
				}
			}
		}
		return onlineDatas.toArray(new UserOnlineData[onlineDatas.size()]);
	}
	
	public static class UserOnlineManageFactory implements ServiceFactory<UserOnlineManage>{

		@Override
		public UserOnlineManage newInstance(ServiceResource serviceResource) {
			return new UserOnlineManageImpl(serviceResource);
		}
		
	}
}
