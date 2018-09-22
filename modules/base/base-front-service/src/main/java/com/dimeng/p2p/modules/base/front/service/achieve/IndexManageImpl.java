package com.dimeng.p2p.modules.base.front.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.S62.enums.T6252_F09;
import com.dimeng.p2p.modules.base.front.service.IndexManage;
import com.dimeng.p2p.modules.base.front.service.entity.LcInfo;
import com.dimeng.p2p.modules.base.front.service.entity.PtTzTotal;
import com.dimeng.p2p.modules.base.front.service.entity.PtlcTotal;
import com.dimeng.p2p.modules.base.front.service.entity.RankList;

public class IndexManageImpl extends AbstractBaseService implements IndexManage {

	public IndexManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	@Override
	public RankList[] getLcList() throws Throwable {
		try(Connection connection = getConnection()){
			 ArrayList<RankList> list = null;
			    try (PreparedStatement pstmt = connection.prepareStatement("SELECT F01, F03 FROM S61.T6115 ORDER BY F03 DESC LIMIT 5")) {
			        try(ResultSet resultSet = pstmt.executeQuery()) {
			            while(resultSet.next()) {
			            	RankList record = new RankList();
			                record.userName = getUserName(resultSet.getInt(1));
			                record.money = resultSet.getBigDecimal(2);
			                if(list == null) {
			                    list = new ArrayList<>();
			                }
			                list.add(record);
			            }
			        }
			    }
			    return ((list == null || list.size() == 0) ? null: list.toArray(new RankList[list.size()]));
		}
	}
	
	//根据用户ID得到用户名
	private String getUserName(int userId) throws SQLException{
		try(Connection connection = getConnection()){
			  try (PreparedStatement pstmt = connection.prepareStatement("SELECT F02 FROM S61.T6110 WHERE T6110.F01 = ? ")) {
			        pstmt.setInt(1, userId);
			        try(ResultSet resultSet = pstmt.executeQuery()) {
			            if(resultSet.next()) {
			                return resultSet.getString(1);
			            }
			        }
			    }
		}
		return null;
	}

	@Override
	public PtlcTotal getPtlcTotal() throws Throwable {
		int month = Calendar.getInstance().get(Calendar.MONTH)+1;
		PtlcTotal total = new PtlcTotal();
		total.month = month;
		try(Connection connection = getConnection()){
			//投资人数
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT COUNT(1) FROM (SELECT 1 FROM S62.T6250 WHERE T6250.F07 = 'F' AND MONTH(T6250.F06) = ? GROUP BY T6250.F02, T6250.F03) AS A "))
            {
		        pstmt.setInt(1, month);
		        try(ResultSet resultSet = pstmt.executeQuery()) {
		            if(resultSet.next()) {
		                total.tcNum = resultSet.getInt(1);
		            }
		        }
		    }
			//收回本金利息人数
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT COUNT(1) FROM (SELECT 1 FROM S62.T6252 WHERE T6252.F05 = '7001' AND T6252.F09 = ? AND MONTH(T6252.F10) = ? GROUP BY T6252.F02, T6252.F04) AS A "))
            {
		        pstmt.setString(1, T6252_F09.YH.name());
		        pstmt.setInt(2, month);
		        try(ResultSet resultSet = pstmt.executeQuery()) {
		            if(resultSet.next()) {
		            	total.shNum = resultSet.getInt(1);
		            }
		        }
		    }
			//成功转让债权人数
			try (PreparedStatement pstmt = connection.prepareStatement("SELECT COUNT(F01) FROM S62.T6262 WHERE MONTH(T6262.F07) = ? ")) {
				pstmt.setInt(1, month);
		        try(ResultSet resultSet = pstmt.executeQuery()) {
		            if(resultSet.next()) {
		            	total.zrNum = resultSet.getInt(1);
		            }
		        }
		    }
			
			
		}
		
		
		return total;
	}

	@Override
	public PtTzTotal getPtTzTotal() throws Throwable {
		PtTzTotal pt =  new PtTzTotal();
		try(Connection connection = getConnection()){
			//累计投资总额
			try (PreparedStatement pstmt = connection.prepareStatement("SELECT SUM(F03) FROM S61.T6115")) {
		        try(ResultSet resultSet = pstmt.executeQuery()) {
		            if(resultSet.next()) {
		                pt.tzze = resultSet.getBigDecimal(1);
		            }
		        }
		    }
			//累计投资收益
			try (PreparedStatement pstmt = connection.prepareStatement("SELECT F09 FROM S70.T7010 ")) {
		        try(ResultSet resultSet = pstmt.executeQuery()) {
		            if(resultSet.next()) {
		                pt.tzsy = resultSet.getBigDecimal(1);
		            }
		        }
		    }
			
			//累计投资人数
			try (PreparedStatement pstmt = connection.prepareStatement("SELECT COUNT(F01) FROM S61.T6115 WHERE T6115.F03 > 0")) {
		        try(ResultSet resultSet = pstmt.executeQuery()) {
		            if(resultSet.next()) {
		                pt.personTotle = resultSet.getInt(1);
		            }
		        }
		    }
		}
		return pt;
	}

	@Override
	public LcInfo[] getlcInfos() throws Throwable {
		try(Connection connection = getConnection()){
			 ArrayList<LcInfo> list = null;
			    try (PreparedStatement pstmt = connection.prepareStatement("SELECT  F03,F04,F06 FROM S62.T6250 ORDER BY F06 DESC LIMIT 3")) {
			        try(ResultSet resultSet = pstmt.executeQuery()) {
			            while(resultSet.next()) {
			            	LcInfo record = new LcInfo();
			                record.userName = getUserName(resultSet.getInt(1));
			                record.money = resultSet.getBigDecimal(2);
			                record.time = resultSet.getTimestamp(3);
			                if(list == null) {
			                    list = new ArrayList<>();
			                }
			                list.add(record);
			            }
			        }
			    }
			    return ((list == null || list.size() == 0) ? null: list.toArray(new LcInfo[list.size()]));
		}
	}

	@Override
	public int getPersonTotal() throws Throwable {
		try(Connection connection = getConnection()){
			  try (PreparedStatement pstmt = connection.prepareStatement("SELECT COUNT(F01) FROM S61.T6110")) {
			        try(ResultSet resultSet = pstmt.executeQuery()) {
			            if(resultSet.next()) {
			                return resultSet.getInt(1);
			            }
			        }
			    }
		}
		return 0;
	}
	
}
