/*
 * 文 件 名:  ScoreMallIndexManageImpl.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoucl
 * 修改时间:  2015年12月9日
 */
package com.dimeng.p2p.modules.score.mall.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.entities.T6106;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.enums.T6106_F05;
import com.dimeng.p2p.S63.entities.T6355;
import com.dimeng.p2p.S63.enums.T6350_F07;
import com.dimeng.p2p.S63.enums.T6352_F06;
import com.dimeng.p2p.S63.enums.T6359_F08;
import com.dimeng.p2p.common.enums.YesOrNo;
import com.dimeng.p2p.repeater.score.UserCenterScoreManage;
import com.dimeng.p2p.repeater.score.entity.MyOrderInfoExt;
import com.dimeng.p2p.repeater.score.entity.ScoreCountExt;
import com.dimeng.p2p.repeater.score.entity.ScoreOrderInfoExt;
import com.dimeng.p2p.repeater.score.query.HarvestAddressQuery;
import com.dimeng.util.parser.DateTimeParser;

/**
 * <用户中心相关积分操作方法>
 * <功能详细描述>
 * 
 * @author  pengwei
 * @version  [版本号, 2015年12月18日]
 */
public class UserCenterScoreManageImpl extends AbstractMallService implements UserCenterScoreManage
{
    
    public UserCenterScoreManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public boolean updateUserSigned()
        throws Throwable
    {
		return false;}
    
    @SuppressWarnings("static-access")
    private String getYesterday(Timestamp now)
    {
		return null;}
    
    @Override
    public int getUsableScore()
        throws Throwable
    {
		return 0;}
    
    @Override
    public ScoreCountExt getSumScore()
        throws Throwable
    {
		return null;}
    
    @Override
    public PagingResult<T6106> getUserScoreList(Paging page)
        throws Throwable
    {
		return null;}
    
    @Override
    public PagingResult<ScoreOrderInfoExt> queryUsedUScoreList(Paging page)
        throws Throwable
    {
		return null;}
    
    @Override
    public PagingResult<MyOrderInfoExt> queryMyOrderList(Paging page)
        throws Throwable
    {
		return null;}
    
    @Override
    public PagingResult<T6355> queryHarvestAddress(Paging page)
        throws Throwable
    {
		return null;}
    
    @Override
    public int getCountAddress()
        throws Throwable
    {
		return 0;}
    
    @Override
    public int addAddress(T6355 t6355)
        throws Throwable
    {
		return 0;}
    
    @Override
    public int deleteAddress(int id)
        throws Throwable
    {
		return 0;}
    
    @Override
    public void updateAddress(int id, HarvestAddressQuery query)
        throws Throwable
    {}
    
    @Override
    public T6355 getAddress(int id)
        throws Throwable
    {
		return null;}
    
    @Override
    public PagingResult<MyOrderInfoExt> queryMyOrderById(final String orderId, Paging page)
        throws Throwable
    {
		return null;}
    
    @Override
    public T6355 queryDefaultAddress()
        throws Throwable
    {
		return null;}
    
    private int getCountAddress(Connection connection, int accountId)
        throws Throwable
    {
		return 0;}
    
}
