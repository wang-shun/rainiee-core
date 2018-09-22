package com.dimeng.p2p.modules.nciic.entity;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 实名认证返回对像
 * @author xiaoyaocai
 *
 */
@XmlType
//@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class PoliceCheckInfo
{
    
    @XmlAttribute(name = "subReportType")
    public String subReportType;
    
    @XmlAttribute(name = "subReportTypeCost")
    public String subReportTypeCost;
    
    @XmlAttribute(name = "treatResult")
    public String treatResult;
    
    @XmlAttribute(name = "errorMessage")
    public String errorMessage;
    
    @XmlElement
    public List<IdentifierAttr> item;
    
    @Override
    public String toString()
    {
        return "PoliceCheckInfo [subReportType=" + subReportType + ", subReportTypeCost=" + subReportTypeCost
            + ", treatResult=" + treatResult + ", errorMessage=" + errorMessage + ", item=" + item + "]";
    }
    
}
