package com.dimeng.p2p.common;

import java.math.BigInteger;
import java.security.interfaces.RSAPublicKey;

import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.jcajce.provider.asymmetric.util.KeyUtil;


/**
 * RSA公钥类
 *
 * @author tonglei
 * @version 1.0.0, 2015-05-14
 */
public class DimengRSAPulicKey implements RSAPublicKey
{

    static final long serialVersionUID = 2675817738516720772L;
    
    private final BigInteger modulus;
    
    private final BigInteger publicExponent;
    
    public DimengRSAPulicKey(RSAKeyParameters paramRSAKeyParameters)
    {
        this.modulus = paramRSAKeyParameters.getModulus();
        this.publicExponent = paramRSAKeyParameters.getExponent();
    }

    @Override
    public BigInteger getModulus()
    {
        return this.modulus;
    }
    
    @Override
    public BigInteger getPublicExponent()
    {
        return this.publicExponent;
    }

    @Override
    public String getAlgorithm()
    {
        return "RSA";
    }
    
    @Override
    public String getFormat()
    {
        return "X.509";
    }
    
    @Override
    public byte[] getEncoded()
    {
        return KeyUtil.getEncodedSubjectPublicKeyInfo(new AlgorithmIdentifier(PKCSObjectIdentifiers.rsaEncryption,
            new DERNull()), new org.bouncycastle.asn1.pkcs.RSAPublicKey(getModulus(), getPublicExponent()));
    }
    
    @Override
    public int hashCode()
    {
        return (getModulus().hashCode() ^ getPublicExponent().hashCode());
    }
    
    @Override
    public boolean equals(Object paramObject)
    {
        if (paramObject == this)
            return true;
        if (!(paramObject instanceof java.security.interfaces.RSAPublicKey))
            return false;
        java.security.interfaces.RSAPublicKey localRSAPublicKey = (java.security.interfaces.RSAPublicKey)paramObject;
        return ((getModulus().equals(localRSAPublicKey.getModulus())) && (getPublicExponent().equals(localRSAPublicKey.getPublicExponent())));
    }
    
    @Override
    public String toString()
    {
        StringBuffer localStringBuffer = new StringBuffer();
        String str = System.getProperty("line.separator");
        localStringBuffer.append("RSA Public Key").append(str);
        localStringBuffer.append("            modulus: ").append(getModulus().toString(16)).append(str);
        localStringBuffer.append("    public exponent: ").append(getPublicExponent().toString(16)).append(str);
        return localStringBuffer.toString();
    }

}
