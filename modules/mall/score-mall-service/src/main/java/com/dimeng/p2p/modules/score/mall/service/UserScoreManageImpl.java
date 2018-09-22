/*
 * 文 件 名:  ScoreMallIndexManageImpl.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoucl
 * 修改时间:  2015年12月9日
 */
package com.dimeng.p2p.modules.score.mall.service;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.entities.T6105;
import com.dimeng.p2p.S61.entities.T6106;
import com.dimeng.p2p.S61.enums.T6106_F05;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S63.enums.T6352_F06;
import com.dimeng.p2p.common.enums.YesOrNo;
import com.dimeng.p2p.repeater.score.UserScoreManage;
import com.dimeng.p2p.repeater.score.entity.ScoreCountExt;
import com.dimeng.p2p.repeater.score.entity.ScoreExchangeExt;
import com.dimeng.p2p.repeater.score.entity.ScoreOrderInfoExt;
import com.dimeng.p2p.repeater.score.entity.UsedScoreExchangeExt;
import com.dimeng.p2p.repeater.score.entity.UserScoreAccountExt;
import com.dimeng.p2p.repeater.score.entity.UserScoreExt;
import com.dimeng.util.StringHelper;
import com.dimeng.util.io.CVSWriter;
import com.dimeng.util.parser.DateTimeParser;

/**
 * <积分商城首页>
 * <功能详细描述>
 * 
 * @author  pengwei
 * @version  [版本号, 2015年12月14日]
 */
public class UserScoreManageImpl extends AbstractMallService implements UserScoreManage
{
    
    public UserScoreManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public PagingResult<T6105> queryUserScoreAccountList(UserScoreAccountExt userScoreAccountExt, Paging page)
        throws Throwable
    {
		return null;}
    
    @Override
    public PagingResult<ScoreOrderInfoExt> queryUsedUScoreList(int userId, Paging page)
        throws Throwable
    {
		return null;}
    
    @Override
    public PagingResult<T6106> getUserScoreList(int userId, Paging page)
        throws Throwable
    {
		return null;}
    
    @Override
    public PagingResult<T6106> queryScoreList(UserScoreExt userScoreExt, Paging page)
        throws Throwable
    {
		return null;}
    
    @Override
    public PagingResult<ScoreOrderInfoExt> queryScoreExchangeList(ScoreExchangeExt scoreExchangeExt, Paging page)
        throws Throwable
    {
		return null;}
    
    @Override
    public ScoreCountExt getSumScore(UserScoreAccountExt userScoreAccountExt)
        throws Throwable
    {
		return null;}
    
    @Override
    public int getSumScore(int userId)
        throws Throwable
    {
		return 0;}
    
    @Override
    public UsedScoreExchangeExt getSumScoreExchange(int userId)
        throws Throwable
    {
		return null;}
    
    @Override
    public int queryGetSumScore(UserScoreExt userScoreExt)
        throws Throwable
    {
		return 0;}
    
    @Override
    public UsedScoreExchangeExt getSumScoreExchange(ScoreExchangeExt scoreExchangeExt)
        throws Throwable
    {
		return null;}
    
    @Override
    public void exportScoreStatisticsList(T6105[] t6105s, OutputStream outputStream, String charset)
        throws Throwable
    {}
    
    @Override
    public void exportScoreGetList(T6106[] t6106s, OutputStream outputStream, String charset)
        throws Throwable
    {}
    
    @Override
    public void exportScoreExchangeList(ScoreOrderInfoExt[] scoreOrderInfoExtS, OutputStream outputStream,
        String charset)
        throws Throwable
    {}
    
}
