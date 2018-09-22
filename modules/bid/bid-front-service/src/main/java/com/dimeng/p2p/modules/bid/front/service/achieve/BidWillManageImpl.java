package com.dimeng.p2p.modules.bid.front.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.dimeng.framework.http.upload.UploadFile;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.S62.entities.T6211;
import com.dimeng.p2p.S62.entities.T6281;
import com.dimeng.p2p.S62.entities.T6282;
import com.dimeng.p2p.S62.entities.T6284;
import com.dimeng.p2p.S62.enums.T6211_F03;
import com.dimeng.p2p.S62.enums.T6281_F14;
import com.dimeng.p2p.S62.enums.T6281_F18;
import com.dimeng.p2p.S62.enums.T6281_F19;
import com.dimeng.p2p.S62.enums.T6281_F20;
import com.dimeng.p2p.S62.enums.T6282_F11;
import com.dimeng.p2p.S62.enums.T6282_F15;
import com.dimeng.p2p.S62.enums.T6282_F16;
import com.dimeng.p2p.S62.enums.T6282_F17;
import com.dimeng.p2p.modules.bid.front.service.BidWillManage;

public class BidWillManageImpl extends AbstractBidManage implements
		BidWillManage {

	public BidWillManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	@Override
	public int add(T6281 entity) throws Throwable {
		int userID = entity.F02;
		if (serviceResource.getSession().isAuthenticated()) {
			userID = serviceResource.getSession().getAccountId();
		}
		try (Connection connection = getConnection()) {
			try (PreparedStatement pstmt = connection
					.prepareStatement(
							"INSERT INTO S62.T6281 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?, F10 = ?, F11 = ?, F12 = ?, F13 = ?, F14 = ?, F16 = ?,F18=?,F19=?,F20=?,F22=?",
							PreparedStatement.RETURN_GENERATED_KEYS)) {
				pstmt.setInt(1, userID);
				pstmt.setString(2, entity.F03);
				pstmt.setString(3, entity.F04);
				pstmt.setString(4, entity.F05);
				pstmt.setString(5, entity.F06);
				pstmt.setString(6, entity.F07);
				pstmt.setString(7, entity.F08);
				pstmt.setBigDecimal(8, entity.F09);
				pstmt.setInt(9, entity.F10);
				pstmt.setInt(10, entity.F11);
				pstmt.setString(11, entity.F12);
				pstmt.setString(12, entity.F13);
				pstmt.setString(13, T6281_F14.WCL.name());
				pstmt.setTimestamp(14, getCurrentTimestamp(connection));
				pstmt.setString(15, entity.F18 == null ? T6281_F18.F.name()
						: entity.F18.name());
				pstmt.setString(16, entity.F19 == null ? T6281_F19.F.name()
						: entity.F19.name());
				pstmt.setString(17, entity.F20 == null ? T6281_F20.F.name()
						: entity.F20.name());
				pstmt.setInt(18, entity.F22);
				pstmt.execute();
				try (ResultSet resultSet = pstmt.getGeneratedKeys();) {
					if (resultSet.next()) {
						return resultSet.getInt(1);
					}
					return 0;
				}
			}
		}
	}

	@Override
	public int add(T6282 entity) throws Throwable {
		int userID = entity.F02;
		if (serviceResource.getSession().isAuthenticated()) {
			userID = serviceResource.getSession().getAccountId();
		}
		try (Connection connection = getConnection()) {
			try (PreparedStatement pstmt = connection
					.prepareStatement(
							"INSERT INTO S62.T6282 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?, F10 = ?, F11 = ?, F13 = ? ,F15 = ?,F16=?,F17=?,F19=?",
							PreparedStatement.RETURN_GENERATED_KEYS)) {
				pstmt.setInt(1, userID);
				pstmt.setString(2, entity.F03);
				pstmt.setString(3, entity.F04);
				pstmt.setString(4, entity.F05);
				pstmt.setBigDecimal(5, entity.F06);
				pstmt.setInt(6, entity.F07);
				pstmt.setInt(7, entity.F08);
				pstmt.setString(8, entity.F09);
				pstmt.setString(9, entity.F10);
				pstmt.setString(10, T6282_F11.WCL.name());
				pstmt.setTimestamp(11, getCurrentTimestamp(connection));
				if (entity.F15 == null) {
					pstmt.setString(12, T6282_F15.F.name());
				} else {
					pstmt.setString(12, entity.F15.name());
				}
				if (entity.F16 == null) {
					pstmt.setString(13, T6282_F16.F.name());
				} else {
					pstmt.setString(13, entity.F16.name());
				}
				if (entity.F17 == null) {
					pstmt.setString(14, T6282_F17.F.name());
				} else {
					pstmt.setString(14, entity.F17.name());
				}
				pstmt.setInt(15, entity.F19);
				pstmt.execute();
				try (ResultSet resultSet = pstmt.getGeneratedKeys();) {
					if (resultSet.next()) {
						return resultSet.getInt(1);
					}
					return 0;
				}
			}
		}
	}

	@Override
	public T6211[] getBidTypeAll() throws Throwable {
		try (Connection connection = getConnection()) {
			ArrayList<T6211> list = null;
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F01, F02 FROM S62.T6211 WHERE T6211.F03 = ?")) {
				pstmt.setString(1, T6211_F03.QY.name());
				try (ResultSet resultSet = pstmt.executeQuery()) {
					while (resultSet.next()) {
						T6211 record = new T6211();
						record.F01 = resultSet.getInt(1);
						record.F02 = resultSet.getString(2);
						if (list == null) {
							list = new ArrayList<>();
						}
						list.add(record);
					}
				}
			}
			return ((list == null || list.size() == 0) ? null : list
					.toArray(new T6211[list.size()]));
		}
	}

	@Override
	public void addAnnex(int id, UploadFile file) throws Throwable {
		if (file == null) {
			return;
		}
		
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection.prepareStatement(
					"INSERT INTO S62.T6283 SET F02=?,F03=?",
					PreparedStatement.RETURN_GENERATED_KEYS)) {
				ps.setInt(1, id);
				ps.setBlob(2, file.getInputStream());
				ps.execute();
			}
		}
	}

	@Override
	public int addWyzxm(T6284 entity) throws Throwable {
		try (Connection connection = getConnection()) {
			try (PreparedStatement pstmt = connection
					.prepareStatement(
							"INSERT INTO S62.T6284 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F10 = ?",
							PreparedStatement.RETURN_GENERATED_KEYS)) {
				pstmt.setString(1, entity.F02);
				pstmt.setString(2, entity.F03);
				pstmt.setString(3, entity.F04);
				pstmt.setString(4, entity.F05);
				pstmt.setInt(5, entity.F06);
				pstmt.setString(6, entity.F07);
				pstmt.setTimestamp(7, getCurrentTimestamp(connection));
				pstmt.execute();
				try (ResultSet resultSet = pstmt.getGeneratedKeys();) {
					if (resultSet.next()) {
						return resultSet.getInt(1);
					}
					return 0;
				}
			}
		}
	}
}
