/*
 * 文 件 名:  ExperienceTotalList.java
 * 版    权:  © 2014 DM. All rights reserved.
 * 描    述:  <描述>
 * 修 改 人:  linxiaolin
 * 修改时间:  2014/9/25
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.dimeng.p2p.modules.account.console.experience.service.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.p2p.S61.enums.T6103_F06;
import com.dimeng.p2p.S61.enums.T6103_F08;
import com.dimeng.p2p.S62.enums.T6231_F21;

/**
 * 功能详细描述
 *
 * @author linxiaolin
 * @version [版本号, 2014/9/25]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class ExperienceTotalList {
    //用户ID
    public int userId;

    //体验金ID
    public int experienceId;

    //用户名
    public String userName;

    //用户真实姓名
    public String userRealName;

    //来源
    public T6103_F08 source;

    //体验金派出的金额
    public BigDecimal experienceMoney = new BigDecimal(0);

    //生效时间
    public Timestamp validTime;

    //失效时间
    public Timestamp invalidTime;

    //体验金的状态
    public T6103_F06 status;

    //体验金可得利息金额
    public BigDecimal experienceInterest =  new BigDecimal(0);

    //体验金利息生成时间
    public Timestamp interestTime;

    //借款人用户名
    public String jkName;

    //借款人真实姓名
    public String jkRealName;

    //借款项目名字
    public String jkTitle;

    //借款周期(月)
    public int jkTime;

    //借款周期(日)
    public int jkDay;

    //借款年化利率
    public BigDecimal jkNlv =  new BigDecimal(0);

    //投资时间
    public Timestamp tbTime;

    //是否为按天借款,S:是;F:否
    public T6231_F21 borMethod;

    //标编号
    public String bid;

    //投资金额
    public BigDecimal investmoney =  new BigDecimal(0);

    //标ID
    public String id;

    //收益期
    public String expectedTerm;

}
