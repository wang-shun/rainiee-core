package com.dimeng.p2p.common;

import java.math.BigInteger;
import java.security.interfaces.RSAPrivateCrtKey;

import org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters;
import org.bouncycastle.jcajce.provider.asymmetric.rsa.BCRSAPrivateKey;


/**
 * RSA私钥类
 *
 * @author tonglei
 * @version 1.0.0, 2015-05-14
 */
public class DimengRSAPrivateKey extends BCRSAPrivateKey implements RSAPrivateCrtKey
{

    static final long serialVersionUID = 7834723820638524718L;
    
    private final BigInteger publicExponent;
    
    private final BigInteger primeP;
    
    private final BigInteger primeQ;
    
    private final BigInteger primeExponentP;
    
    private final BigInteger primeExponentQ;
    
    private final BigInteger crtCoefficient;
    
    public DimengRSAPrivateKey(RSAPrivateCrtKeyParameters paramRSAPrivateCrtKeyParameters)
    {
        this.modulus = paramRSAPrivateCrtKeyParameters.getModulus();
        this.privateExponent = paramRSAPrivateCrtKeyParameters.getExponent();
        this.publicExponent = paramRSAPrivateCrtKeyParameters.getPublicExponent();
        this.primeP = paramRSAPrivateCrtKeyParameters.getP();
        this.primeQ = paramRSAPrivateCrtKeyParameters.getQ();
        this.primeExponentP = paramRSAPrivateCrtKeyParameters.getDP();
        this.primeExponentQ = paramRSAPrivateCrtKeyParameters.getDQ();
        this.crtCoefficient = paramRSAPrivateCrtKeyParameters.getQInv();
    }
    
    @Override
    public String getFormat()
    {
        return "PKCS#8";
    }
    
    @Override
    public BigInteger getPublicExponent()
    {
        return this.publicExponent;
    }
    
    @Override
    public BigInteger getPrimeP()
    {
        return this.primeP;
    }
    
    @Override
    public BigInteger getPrimeQ()
    {
        return this.primeQ;
    }
    
    @Override
    public BigInteger getPrimeExponentP()
    {
        return this.primeExponentP;
    }
    
    @Override
    public BigInteger getPrimeExponentQ()
    {
        return this.primeExponentQ;
    }
    
    @Override
    public BigInteger getCrtCoefficient()
    {
        return this.crtCoefficient;
    }
    
    @Override
    public int hashCode()
    {
        return (getModulus().hashCode() ^ getPublicExponent().hashCode() ^ getPrivateExponent().hashCode());
    }
    
    @Override
    public boolean equals(Object paramObject)
    {
        if (paramObject == this)
            return true;
        if (!(paramObject instanceof RSAPrivateCrtKey))
            return false;
        RSAPrivateCrtKey localRSAPrivateCrtKey = (RSAPrivateCrtKey)paramObject;
        return ((getModulus().equals(localRSAPrivateCrtKey.getModulus()))
            && (getPublicExponent().equals(localRSAPrivateCrtKey.getPublicExponent()))
            && (getPrivateExponent().equals(localRSAPrivateCrtKey.getPrivateExponent()))
            && (getPrimeP().equals(localRSAPrivateCrtKey.getPrimeP()))
            && (getPrimeQ().equals(localRSAPrivateCrtKey.getPrimeQ()))
            && (getPrimeExponentP().equals(localRSAPrivateCrtKey.getPrimeExponentP()))
            && (getPrimeExponentQ().equals(localRSAPrivateCrtKey.getPrimeExponentQ())) && (getCrtCoefficient().equals(localRSAPrivateCrtKey.getCrtCoefficient())));
    }
    
    @Override
    public String toString()
    {
        StringBuffer localStringBuffer = new StringBuffer();
        String str = System.getProperty("line.separator");
        localStringBuffer.append("RSA Private CRT Key").append(str);
        localStringBuffer.append("            modulus: ").append(getModulus().toString(16)).append(str);
        localStringBuffer.append("    public exponent: ").append(getPublicExponent().toString(16)).append(str);
        localStringBuffer.append("   private exponent: ").append(getPrivateExponent().toString(16)).append(str);
        localStringBuffer.append("             primeP: ").append(getPrimeP().toString(16)).append(str);
        localStringBuffer.append("             primeQ: ").append(getPrimeQ().toString(16)).append(str);
        localStringBuffer.append("     primeExponentP: ").append(getPrimeExponentP().toString(16)).append(str);
        localStringBuffer.append("     primeExponentQ: ").append(getPrimeExponentQ().toString(16)).append(str);
        localStringBuffer.append("     crtCoefficient: ").append(getCrtCoefficient().toString(16)).append(str);
        return localStringBuffer.toString();
    }
    

}
