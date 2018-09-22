/**
 * 
 */
package com.dimeng.p2p.service;

import java.math.BigInteger;
import java.security.KeyPair;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.UnixCrypt;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters;
import org.bouncycastle.jcajce.provider.asymmetric.rsa.BCRSAPrivateCrtKey;
import org.bouncycastle.jcajce.provider.asymmetric.rsa.BCRSAPublicKey;

import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.S61.enums.T6110_F08;
import com.dimeng.p2p.S61.enums.T6110_F10;
import com.dimeng.p2p.common.DimengRSAPrivateKey;
import com.dimeng.p2p.common.DimengRSAPulicKey;
import com.dimeng.p2p.common.RSAUtils;

/**
 * @author guopeng
 * 
 */
public class PtAccountManageImpl extends AbstractP2PService implements PtAccountManage
{
    
    public PtAccountManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public void addPtAccount()
        throws Throwable
    {
        try(Connection connection = getConnection())
        {
            try
            {
            	serviceResource.openTransactions(connection);
                try (PreparedStatement pstmt = connection.prepareStatement("SELECT F01 FROM S71.T7101 FOR UPDATE"))
                {
                    try (ResultSet resultSet = pstmt.executeQuery())
                    {
                        if (!resultSet.next())
                        {
                            int userId = addUser(connection);
                            try (PreparedStatement pstmt1 =
                                         connection.prepareStatement("INSERT INTO S71.T7101 SET F01 = ?"))
                            {
                                pstmt1.setInt(1, userId);
                                pstmt1.execute();
                            }
                        }
                    }
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
    
    private int addUser(Connection connection)
        throws Throwable
    {
        String t6110 = "INSERT INTO S61.T6110 SET F02 = ?, F03 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?, F10 = ?";
        /**
        * 资金账户
        */
        String t6101 = "INSERT INTO S61.T6101 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?";
        String password = UnixCrypt.crypt("taojindi", DigestUtils.sha256Hex("taojindi"));
        int userId = getUserByName("平台账号");
        if (userId > 0)
        {
            return userId;
        }
        userId = insert(connection,
            t6110,
            "平台账号",
            password,
            T6110_F06.FZRR.name(),
            T6110_F07.QY.name(),
            T6110_F08.HTTJ.name(),
            getCurrentTimestamp(connection),
            T6110_F10.F.name());
        for (T6101_F03 t : T6101_F03.values())
        {
            execute(connection, t6101, userId, t.name(), getAccount(t.name(), userId), "平台账号");
        }
        return userId;
    }
    
    private int getUserByName(String loginName)
        throws SQLException
    {
        String t6110 = "SELECT F01 FROM S61.T6110 WHERE F02=? FOR UPDATE";
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(t6110))
            {
                ps.setString(1, loginName);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        return rs.getInt(1);
                    }
                }
            }
        }
        return 0;
    }
    
    /**
      * 生成资金账户 账号
      * 
      * @param type
      * @param id
      * @return
      */
    private String getAccount(String type, int id)
    {
        DecimalFormat df = new DecimalFormat("00000000000");
        StringBuilder sb = new StringBuilder();
        if (type == null || type.length() < 1)
        {
            return sb.toString();
        }
        sb.append(type.substring(0, 1));
        sb.append(df.format(id));
        return sb.toString();
    }
    
    @Override
    public void addRSAKey(ResourceProvider resourceProviderParam)
        throws Throwable
    {
        KeyPair keyPair = RSAUtils.getKeyPair(resourceProviderParam);
        BCRSAPrivateCrtKey privateKey = (BCRSAPrivateCrtKey)keyPair.getPrivate();
        BCRSAPublicKey publicKey = (BCRSAPublicKey)keyPair.getPublic();
        try(Connection connection = getConnection())
        {
            try
            {
            	serviceResource.openTransactions(connection);
                try (PreparedStatement pstmt = connection.prepareStatement("SELECT F01 FROM S10._1052 "))
                {
                    try (ResultSet resultSet = pstmt.executeQuery())
                    {
                        if (resultSet.next())
                        {
                            try (PreparedStatement pstmt1 = connection.prepareStatement("DELETE FROM S10._1052"))
                            {
                                pstmt1.execute();
                            }
                        }
                    }
                }
                try (PreparedStatement pstmt =
                             connection.prepareStatement("INSERT INTO S10._1052 SET F01 = ?, F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?,F08 = ?"))
                {
                    pstmt.setString(1, String.valueOf(privateKey.getModulus()));
                    pstmt.setString(2, String.valueOf(privateKey.getPublicExponent()));
                    pstmt.setString(3, String.valueOf(privateKey.getPrivateExponent()));
                    pstmt.setString(4, String.valueOf(privateKey.getPrimeP()));
                    pstmt.setString(5, String.valueOf(privateKey.getPrimeQ()));
                    pstmt.setString(6, String.valueOf(privateKey.getPrimeExponentP()));
                    pstmt.setString(7, String.valueOf(privateKey.getPrimeExponentQ()));
                    pstmt.setString(8, String.valueOf(privateKey.getCrtCoefficient()));
                    pstmt.execute();
                }
                try (PreparedStatement pstmt = connection.prepareStatement("SELECT F01 FROM S10._1053 "))
                {
                    try (ResultSet resultSet = pstmt.executeQuery())
                    {
                        if (resultSet.next())
                        {
                            try (PreparedStatement pstmt1 = connection.prepareStatement("DELETE FROM S10._1053"))
                            {
                                pstmt1.execute();
                            }
                        }
                    }
                }
                try (PreparedStatement pstmt = connection.prepareStatement("INSERT INTO S10._1053 SET F01 = ?, F02 = ?"))
                {
                    pstmt.setString(1, String.valueOf(publicKey.getModulus()));
                    pstmt.setString(2, String.valueOf(publicKey.getPublicExponent()));
                    pstmt.execute();
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
    
    @Override
    public DimengRSAPulicKey getPublicKey()
        throws Exception, SQLException
    {
        String _1053 = "SELECT F01, F02 FROM S10._1053";
        RSAKeyParameters rsaKeyParameters = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(_1053))
            {
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        rsaKeyParameters = new RSAKeyParameters(false, new BigInteger(rs.getString(1)),
                            new BigInteger(rs.getString(2)));
                    }
                }
            }
        }
        if (rsaKeyParameters == null)
        {
            return null;
        }
        return new DimengRSAPulicKey(rsaKeyParameters);
    }
    
    @Override
    public DimengRSAPrivateKey getPrivateKey()
        throws Exception, SQLException
    {
        String _1052 = "SELECT F01, F02, F03, F04, F05, F06, F07, F08 FROM S10._1052";
        RSAPrivateCrtKeyParameters rsaPrivateCrtKeyParameters = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(_1052))
            {
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        rsaPrivateCrtKeyParameters = new RSAPrivateCrtKeyParameters(new BigInteger(rs.getString(1)),
                            new BigInteger(rs.getString(2)), new BigInteger(rs.getString(3)),
                            new BigInteger(rs.getString(4)), new BigInteger(rs.getString(5)),
                            new BigInteger(rs.getString(6)), new BigInteger(rs.getString(7)),
                            new BigInteger(rs.getString(8)));
                    }
                }
            }
        }
        if (rsaPrivateCrtKeyParameters == null)
        {
            return null;
        }
        return new DimengRSAPrivateKey(rsaPrivateCrtKeyParameters);
    }
}
