package com.dimeng.p2p.account.user.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.dimeng.framework.http.upload.FileStore;
import com.dimeng.framework.http.upload.UploadFile;
import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.entities.T6112;
import com.dimeng.p2p.S61.entities.T6113;
import com.dimeng.p2p.S61.entities.T6142;
import com.dimeng.p2p.S61.entities.T6143;
import com.dimeng.p2p.S61.enums.T6143_F03;
import com.dimeng.p2p.account.user.service.UserBaseManage;
import com.dimeng.p2p.account.user.service.entity.UserBase;
import com.dimeng.p2p.variables.FileType;
import com.dimeng.util.StringHelper;

public class UserBaseManageImpl extends AbstractAccountService implements
		UserBaseManage {
	public UserBaseManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	public static class BankCarMnageFactory implements
			ServiceFactory<UserBaseManage> {
		@Override
		public UserBaseManage newInstance(ServiceResource serviceResource) {
			return new UserBaseManageImpl(serviceResource);
		}
	}

	@Override
    public UserBase getUserBase()
        throws Throwable
    {
        int acount = serviceResource.getSession().getAccountId();
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT T6110.F02 AS F01, T6110.F04 AS F02, T6110.F05 AS F03, T6141.F02 AS F04, T6141.F06 AS F05, T6141.F05 AS F06,T6141.F07 AS F07,T6141.F08 AS F08 FROM S61.T6110 INNER JOIN S61.T6141 ON T6110.F01 = T6141.F01 WHERE T6141.F01 = ?");
        try (Connection connection = getConnection())
        {
            UserBase user = select(connection, new ItemParser<UserBase>()
            {
                @Override
                public UserBase parse(ResultSet rs)
                    throws SQLException
                {
                    UserBase user = new UserBase();
                    FileStore fileStore = serviceResource.getResource(FileStore.class);
                    if (rs.next())
                    {
                        user.userName = rs.getString(1);
                        user.phoneNumber = rs.getString(2);
                        user.emil = rs.getString(3);
                        user.name = rs.getString(4);
                        user.idCard = rs.getString(5);
                        user.icon = rs.getString(6);
                        user.sfzh = rs.getString(7);
                        user.date = rs.getTimestamp(8);
                        if (!StringHelper.isEmpty(user.icon))
                        {
                            user.userImg = fileStore.getURL(user.icon);
                        }
                        user.sex = getSex(user.sfzh);
                        user.birthday = getBirthday(user.sfzh);
                    }
                    return user;
                }
            }, sb.toString(), acount);
            return user;
        }
    }

	private String getSex(String sfzh)
	{
		if(StringHelper.isEmpty(sfzh))
		{
			return null;
		}

		String sexSfzh = null;
		String sex = null;
		try {
			sexSfzh = StringHelper.decode(sfzh);
			if(Integer.parseInt(sexSfzh.substring(sexSfzh.length() - 2, sexSfzh.length() - 1)) % 2 != 0){
				sex = "男";
			}
			else{
				sex = "女";
			}
		} catch (Throwable throwable) {
			sex = null;
		}
		return sex;
	}

	private String getBirthday(String sfzh)
	{
		if(StringHelper.isEmpty(sfzh))
		{
			return null;
		}

		String birthdaySfzh = null;
		String birthday = null;
		try {
			birthdaySfzh = StringHelper.decode(sfzh);

			StringBuffer tempStr = new StringBuffer("");
			if (birthdaySfzh != null && birthdaySfzh.trim().length() > 0) {
				if (birthdaySfzh.trim().length() == 15) {
					tempStr.append(birthdaySfzh.substring(6, 12));
					tempStr.insert(4, '-');
					tempStr.insert(2, '-');
					tempStr.insert(0, "19");
				} else if (birthdaySfzh.trim().length() == 18) {
					tempStr = new StringBuffer(birthdaySfzh.substring(6, 14));
					tempStr.insert(6, '-');
					tempStr.insert(4, '-');
				}
			}

			birthday = tempStr.toString();

		} catch (Throwable throwable) {
			birthday = null;
		}
		return birthday;
	}

    @Override
    public void upload(UploadFile file)
        throws Throwable
    {
        if (file == null)
        {
            return;
        }
        FileStore fileStore = serviceResource.getResource(FileStore.class);
        try (Connection connection = getConnection())
        {
            execute(connection,
                "UPDATE S61.T6141 SET F05 = ? WHERE F01 = ?",
                fileStore.upload(FileType.USER_IMAGE.ordinal(), file)[0],
                serviceResource.getSession().getAccountId());
        }
    }


	@Override
	public int addXlxx(T6142 entity) throws Throwable {
		try(Connection connection = getConnection()){
			 try (PreparedStatement pstmt = connection.prepareStatement("INSERT INTO S61.T6142 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?",PreparedStatement.RETURN_GENERATED_KEYS)) {
			        pstmt.setInt(1, serviceResource.getSession().getAccountId());
			        pstmt.setString(2, entity.F03);
			        pstmt.setInt(3, entity.F04);
			        pstmt.setString(4, entity.F05);
			        pstmt.setString(5, entity.F06);
			        pstmt.setInt(6, entity.F07);
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
	public T6142 getXlxx(int id) throws Throwable {
		try(Connection connection = getConnection()){
		    int loginId = serviceResource.getSession().getAccountId();
			T6142 record = null;
		    try (PreparedStatement pstmt = connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07 FROM S61.T6142 WHERE T6142.F01 = ? AND T6142.F02 = ? LIMIT 1")) {
		        pstmt.setInt(1, id);
		        pstmt.setInt(2, loginId);
		        try(ResultSet resultSet = pstmt.executeQuery()) {
		            if(resultSet.next()) {
		                record =new T6142();
		                record.F01 = resultSet.getInt(1);
		                record.F02 = resultSet.getInt(2);
		                record.F03 = resultSet.getString(3);
		                record.F04 = resultSet.getInt(4);
		                record.F05 = resultSet.getString(5);
		                record.F06 = resultSet.getString(6);
		                record.F07 = resultSet.getInt(7);
		            }
		        }
		    }
		    return record;
		}
	}


	@Override
	public void updateXlxx(T6142 entity) throws Throwable {
		try(Connection connection = getConnection()){
			try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S61.T6142 SET F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ? WHERE F01 = ? AND F02 = ?")) {
		        pstmt.setString(1,  entity.F03);
		        pstmt.setInt(2,  entity.F04);
		        pstmt.setString(3,  entity.F05);
		        pstmt.setString(4,  entity.F06);
		        pstmt.setInt(5,  entity.F07);
		        pstmt.setInt(6,  entity.F01);
		        pstmt.setInt(7,  serviceResource.getSession().getAccountId());
		        pstmt.execute();
		    }
		}
		
	}


	@Override
    public PagingResult<T6142> seachXlxx(Paging paging)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            return selectPaging(connection,
                new ArrayParser<T6142>()
                {
                    
                    @Override
                    public T6142[] parse(ResultSet resultSet)
                        throws SQLException
                    {
                        ArrayList<T6142> list = null;
                        while (resultSet.next())
                        {
                            T6142 record = new T6142();
                            record.F01 = resultSet.getInt(1);
                            record.F03 = resultSet.getString(2);
                            record.F04 = resultSet.getInt(3);
                            record.F05 = resultSet.getString(4);
                            record.F06 = resultSet.getString(5);
                            record.F06 = resultSet.getString(5);
                            record.F07 = resultSet.getInt(6);
                            if (list == null)
                            {
                                list = new ArrayList<>();
                            }
                            list.add(record);
                        }
                        return ((list == null || list.size() == 0) ? null : list.toArray(new T6142[list.size()]));
                    }
                },
                paging,
                "SELECT F01, F03, F04, F05, F06, F07 FROM S61.T6142 WHERE T6142.F02 = ?",
                serviceResource.getSession().getAccountId());
        }
    }


	@Override
	public int addFcxx(T6112 entity) throws Throwable {
		try(Connection connection = getConnection()){
			 try (PreparedStatement pstmt = connection.prepareStatement("INSERT INTO S61.T6112 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?, F10 = ?, F11 = ?",PreparedStatement.RETURN_GENERATED_KEYS)) {
			        pstmt.setInt(1, serviceResource.getSession().getAccountId());
			        pstmt.setString(2, entity.F03);
			        pstmt.setFloat(3, entity.F04);
			        pstmt.setInt(4, entity.F05);
			        pstmt.setBigDecimal(5, entity.F06);
			        pstmt.setBigDecimal(6, entity.F07);
			        pstmt.setInt(7, entity.F08);
			        pstmt.setString(8, entity.F09);
			        pstmt.setString(9, entity.F10);
			        pstmt.setBigDecimal(10, entity.F11);
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
	public T6112 getFcxx(int id) throws Throwable {
		try(Connection connection = getConnection()){
			T6112 record = null;
			int loginId = serviceResource.getSession().getAccountId();
		    try (PreparedStatement pstmt = connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11 FROM S61.T6112 WHERE T6112.F01 = ? AND T6112.F02 =? LIMIT 1")) {
		        pstmt.setInt(1, id);
		        pstmt.setInt(2, loginId);
		        try(ResultSet resultSet = pstmt.executeQuery()) {
		            if(resultSet.next()) {
		                record = new T6112();
		                record.F01 = resultSet.getInt(1);
		                record.F02 = resultSet.getInt(2);
		                record.F03 = resultSet.getString(3);
		                record.F04 = resultSet.getFloat(4);
		                record.F05 = resultSet.getInt(5);
		                record.F06 = resultSet.getBigDecimal(6);
		                record.F07 = resultSet.getBigDecimal(7);
		                record.F08 = resultSet.getInt(8);
		                record.F09 = resultSet.getString(9);
		                record.F10 = resultSet.getString(10);
		                record.F11 = resultSet.getBigDecimal(11);
		            }
		        }
		    }
		    return record;
		}
	}


	@Override
	public void updateFcxx(T6112 entity) throws Throwable {
		try(Connection connection = getConnection()){
			 try (PreparedStatement pstmt = connection.prepareStatement(
					 "UPDATE S61.T6112 SET F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?, F10 = ?, F11 = ? " +
							 "WHERE F01 = ? AND F02 = ?")) {
			        pstmt.setString(1, entity.F03);
			        pstmt.setFloat(2, entity.F04);
			        pstmt.setInt(3, entity.F05);
			        pstmt.setBigDecimal(4, entity.F06);
			        pstmt.setBigDecimal(5, entity.F07);
			        pstmt.setInt(6, entity.F08);
			        pstmt.setString(7, entity.F09);
			        pstmt.setString(8, entity.F10);
			        pstmt.setBigDecimal(9, entity.F11);
			        pstmt.setInt(10, entity.F01);
			        pstmt.setInt(11, serviceResource.getSession().getAccountId());

			        pstmt.execute();
			    }
		}
		
	}


	@Override
    public PagingResult<T6112> seachFcxx(Paging paging)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            return selectPaging(connection,
                new ArrayParser<T6112>()
                {
                    
                    @Override
                    public T6112[] parse(ResultSet resultSet)
                        throws SQLException
                    {
                        ArrayList<T6112> list = null;
                        while (resultSet.next())
                        {
                            T6112 record = new T6112();
                            record.F01 = resultSet.getInt(1);
                            record.F03 = resultSet.getString(2);
                            record.F04 = resultSet.getFloat(3);
                            record.F05 = resultSet.getInt(4);
                            record.F06 = resultSet.getBigDecimal(5);
                            record.F07 = resultSet.getBigDecimal(6);
                            record.F08 = resultSet.getInt(7);
                            record.F09 = resultSet.getString(8);
                            record.F10 = resultSet.getString(9);
                            record.F11 = resultSet.getBigDecimal(10);
                            if (list == null)
                            {
                                list = new ArrayList<>();
                            }
                            list.add(record);
                        }
                        return ((list == null || list.size() == 0) ? null : list.toArray(new T6112[list.size()]));
                    }
                },
                paging,
                "SELECT F01, F03, F04, F05, F06, F07, F08, F09, F10, F11 FROM S61.T6112 WHERE T6112.F02 = ?",
                serviceResource.getSession().getAccountId());
        }
    }


	@Override
	public int addCcxx(T6113 entity) throws Throwable {
		try(Connection connection = getConnection()){
			 try (PreparedStatement pstmt = connection.prepareStatement("INSERT INTO S61.T6113 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?",PreparedStatement.RETURN_GENERATED_KEYS)) {
			        pstmt.setInt(1, serviceResource.getSession().getAccountId());
			        pstmt.setString(2, entity.F03);
			        pstmt.setString(3, entity.F04);
			        pstmt.setInt(4, entity.F05);
			        pstmt.setBigDecimal(5, entity.F06);
			        pstmt.setBigDecimal(6, entity.F07);
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
	public T6113 getCcxx(int id) throws Throwable {
		try(Connection connection = getConnection()){
			T6113 record = null;
			int loginId = serviceResource.getSession().getAccountId();
		    try (PreparedStatement pstmt = connection.prepareStatement("SELECT F01, F03, F04, F05, F06, F07 FROM S61.T6113 WHERE T6113.F01 = ? AND T6113.F02 = ? LIMIT 1")) {
		        pstmt.setInt(1, id);
		        pstmt.setInt(2, loginId);
		        try(ResultSet resultSet = pstmt.executeQuery()) {
		            if(resultSet.next()) {
		                record = new T6113();
		                record.F01 = resultSet.getInt(1);
		                record.F03 = resultSet.getString(2);
		                record.F04 = resultSet.getString(3);
		                record.F05 = resultSet.getInt(4);
		                record.F06 = resultSet.getBigDecimal(5);
		                record.F07 = resultSet.getBigDecimal(6);
		            }
		        }
		    }
		    return record;
		}
	}


	@Override
	public void updateCcxx(T6113 entity) throws Throwable {
		try(Connection connection = getConnection()){
			 try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S61.T6113 SET F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ? WHERE F01 = ? AND F02 = ?")) {
			        pstmt.setString(1, entity.F03);
			        pstmt.setString(2, entity.F04);
			        pstmt.setInt(3, entity.F05);
			        pstmt.setBigDecimal(4, entity.F06);
			        pstmt.setBigDecimal(5, entity.F07);
			        pstmt.setInt(6, entity.F01);
			        pstmt.setInt(7, serviceResource.getSession().getAccountId());
			        pstmt.execute();
			    }
		}
		
	}


	@Override
    public PagingResult<T6113> seachCcxx(Paging paging)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            return selectPaging(connection,
                new ArrayParser<T6113>()
                {
                    
                    @Override
                    public T6113[] parse(ResultSet resultSet)
                        throws SQLException
                    {
                        ArrayList<T6113> list = null;
                        while (resultSet.next())
                        {
                            T6113 record = new T6113();
                            record.F01 = resultSet.getInt(1);
                            record.F03 = resultSet.getString(2);
                            record.F04 = resultSet.getString(3);
                            record.F05 = resultSet.getInt(4);
                            record.F06 = resultSet.getBigDecimal(5);
                            record.F07 = resultSet.getBigDecimal(6);
                            if (list == null)
                            {
                                list = new ArrayList<>();
                            }
                            list.add(record);
                        }
                        return ((list == null || list.size() == 0) ? null : list.toArray(new T6113[list.size()]));
                    }
                },
                paging,
                "SELECT F01, F03, F04, F05, F06, F07 FROM S61.T6113 WHERE T6113.F02 = ?",
                serviceResource.getSession().getAccountId());
        }
    }


	@Override
	public int addGzxx(T6143 entity) throws Throwable {
		try(Connection connection = getConnection()){
			try (PreparedStatement pstmt = connection.prepareStatement("INSERT INTO S61.T6143 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?, F10 = ?, F11 = ?",PreparedStatement.RETURN_GENERATED_KEYS)) {
		        pstmt.setInt(1, serviceResource.getSession().getAccountId());
		        pstmt.setString(2, entity.F03.name());
		        pstmt.setString(3, entity.F04);
		        pstmt.setString(4, entity.F05);
		        pstmt.setString(5, entity.F06);
		        pstmt.setInt(6, entity.F07);
		        pstmt.setString(7, entity.F08);
		        pstmt.setString(8, entity.F09);
		        pstmt.setString(9, entity.F10);
		        pstmt.setString(10, entity.F11);
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
	public T6143 getGzxx(int id) throws Throwable {
		try(Connection connection = getConnection()){
			 T6143 record = null;
			 int loginId = serviceResource.getSession().getAccountId();
			    try (PreparedStatement pstmt = connection.prepareStatement("SELECT F01, F03, F04, F05, F06, F07, F08, F09, F10, F11 FROM S61.T6143 WHERE T6143.F01 = ? AND T6143.F02 = ? LIMIT 1")) {
			        pstmt.setInt(1, id);
			        pstmt.setInt(2, loginId);
			        try(ResultSet resultSet = pstmt.executeQuery()) {
			            if(resultSet.next()) {
			                record = new T6143();
			                record.F01 = resultSet.getInt(1);
			                record.F03 = T6143_F03.parse(resultSet.getString(2));
			                record.F04 = resultSet.getString(3);
			                record.F05 = resultSet.getString(4);
			                record.F06 = resultSet.getString(5);
			                record.F07 = resultSet.getInt(6);
			                record.F08 = resultSet.getString(7);
			                record.F09 = resultSet.getString(8);
			                record.F10 = resultSet.getString(9);
			                record.F11 = resultSet.getString(10);
			            }
			        }
			    }
			    return record;
		}
	}


	@Override
	public void updateGzxx(T6143 entity) throws Throwable {
		try(Connection connection = getConnection()){
			try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S61.T6143 SET F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?, F10 = ?, F11 = ? WHERE F01 = ? AND F02 = ? ")) {
		        pstmt.setString(1, entity.F03.name());
		        pstmt.setString(2, entity.F04);
		        pstmt.setString(3, entity.F05);
		        pstmt.setString(4, entity.F06);
		        pstmt.setInt(5, entity.F07);
		        pstmt.setString(6, entity.F08);
		        pstmt.setString(7, entity.F09);
		        pstmt.setString(8, entity.F10);
		        pstmt.setString(9, entity.F11);
		        pstmt.setInt(10,  entity.F01);
		        pstmt.setInt(11,  serviceResource.getSession().getAccountId());
		        pstmt.execute();
		    }
		}
		
	}


	@Override
    public PagingResult<T6143> seachGzxx(Paging paging)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            return selectPaging(connection,
                new ArrayParser<T6143>()
                {
                    
                    @Override
                    public T6143[] parse(ResultSet resultSet)
                        throws SQLException
                    {
                        ArrayList<T6143> list = null;
                        while (resultSet.next())
                        {
                            T6143 record = new T6143();
                            record.F01 = resultSet.getInt(1);
                            record.F03 = T6143_F03.parse(resultSet.getString(2));
                            record.F04 = resultSet.getString(3);
                            record.F05 = resultSet.getString(4);
                            record.F06 = resultSet.getString(5);
                            record.F07 = resultSet.getInt(6);
                            record.F08 = resultSet.getString(7);
                            record.F09 = resultSet.getString(8);
                            record.F10 = resultSet.getString(9);
                            record.F11 = resultSet.getString(10);
                            if (list == null)
                            {
                                list = new ArrayList<>();
                            }
                            list.add(record);
                        }
                        return ((list == null || list.size() == 0) ? null : list.toArray(new T6143[list.size()]));
                    }
                },
                paging,
                "SELECT F01, F03, F04, F05, F06, F07, F08, F09, F10, F11 FROM S61.T6143 WHERE T6143.F02 = ?",
                serviceResource.getSession().getAccountId());
        }
    }
	
	 /**
     * 查询区域名称
     *
     * @param id
     * @return
     * @throws SQLException
     */
    @Override
    public String getRegion(int id) throws Throwable {
        if (id <= 0) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        try (Connection connection = getConnection()) {
            try (PreparedStatement ps = connection
                   .prepareStatement("SELECT F06,F07,F08 FROM S50.T5019 WHERE F01=?")) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                   if (rs.next()) {
                       sb.append(rs.getString(1));
                       sb.append(",");
                       sb.append(rs.getString(2));
                       sb.append(",");
                       sb.append(rs.getString(3));
                   }
                }
            }
        }
        return sb.toString();
    }

}
