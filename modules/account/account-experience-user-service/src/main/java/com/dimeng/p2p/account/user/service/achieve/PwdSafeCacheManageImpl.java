package com.dimeng.p2p.account.user.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.account.user.service.entity.PwdSafetyQuestion;

public class PwdSafeCacheManageImpl extends AbstractAccountService implements PwdSafeCacheManage {

	private static Map<Integer, PwdSafetyQuestion> passwordQuestionMap = new HashMap<Integer, PwdSafetyQuestion>();
	private static Map<String, List<PwdSafetyQuestion>> passwordQuestionTypeMap = new HashMap<String, List<PwdSafetyQuestion>>();
	private static List<PwdSafetyQuestion> questionList = new ArrayList<PwdSafetyQuestion>();
	private static boolean initFlag = false;
	
	public PwdSafeCacheManageImpl(ServiceResource serviceResource) {
	        super(serviceResource);
	}
	    
	  public static class PwdSafeCacheManageFactory implements ServiceFactory<PwdSafeCacheManage> {
	        @Override
	        public PwdSafeCacheManage newInstance(ServiceResource serviceResource){
	            return new PwdSafeCacheManageImpl(serviceResource);
	        }
	  }
	    
	/**
	 * 装载支付通道
	 */
	private void loadPasswordQuestionMap() throws Throwable {
		logger.info("密保问题初始化 start!");
		List<PwdSafetyQuestion> questions = new ArrayList<PwdSafetyQuestion>();
		try (Connection connection = getConnection();
			 PreparedStatement pstmt =connection.prepareStatement("SELECT F01,F02,F03 FROM S61.T6193 ORDER BY F03")){
		     try (ResultSet resultSet = pstmt.executeQuery()) {
		         while (resultSet.next()){
		        	 PwdSafetyQuestion safetyQuestion = new PwdSafetyQuestion();
		        	 safetyQuestion.id = resultSet.getInt(1);
		        	 //safetyQuestion.type= resultSet.getString(2);
		        	 safetyQuestion.descr = resultSet.getString(2);
		        	 safetyQuestion.displayOrder = resultSet.getInt(3);
		        	 questions.add(safetyQuestion);
		          }
		    }
		 }
		questionList = questions;
		passwordQuestionMap.clear();
		passwordQuestionTypeMap.clear();
		for (PwdSafetyQuestion question : questions) {
			passwordQuestionMap.put(question.id, question);
			List<PwdSafetyQuestion> list = passwordQuestionTypeMap.get(question.type);
			if (list == null) {
				list = new ArrayList<PwdSafetyQuestion>();
				passwordQuestionTypeMap.put(question.type, list);
			}
			list.add(question);
		}
		initFlag = true;
		logger.info("密保问题初始化 end!");
	}
	
	
	public Map<Integer, PwdSafetyQuestion> getPasswordQuestionMap(){
		if(!initFlag){
			try {
				loadPasswordQuestionMap();
			}catch (Throwable e) {
				logger.error("密保问题初始化异常", e);
			}
		}
		return passwordQuestionMap;
	}
	
	public Map<String, List<PwdSafetyQuestion>> getPasswordQuestionTypeMap(){
		if(!initFlag){
			try {
				loadPasswordQuestionMap();
			}catch (Throwable e) {
				logger.error("密保问题初始化异常", e);
			}
		}
		return passwordQuestionTypeMap;
	}
	
	public List<PwdSafetyQuestion> getQuestionList(){
		if(!initFlag){
			try {
				loadPasswordQuestionMap();
			}catch (Throwable e) {
				logger.error("密保问题初始化异常", e);
			}
		}
		return questionList;
	}
	
	
	public List<PwdSafetyQuestion> getPasswordQuestionByUser(int userId) throws Throwable{
		if(!initFlag){
			try {
				loadPasswordQuestionMap();
			}catch (Throwable e) {
				logger.error("密保问题初始化异常",e);
			}
		}
		List<PwdSafetyQuestion> result = new ArrayList<PwdSafetyQuestion>();
		try (Connection connection = getConnection()){
			 try(PreparedStatement pstmt =connection.prepareStatement("SELECT F03 FROM S61.T6194 WHERE F02 = ?")){
				 pstmt.setInt(1, userId);
			     try (ResultSet resultSet = pstmt.executeQuery()) {
			         while (resultSet.next()){
			        	 result.add(passwordQuestionMap.get(resultSet.getInt(1)));
			          }
			     }
			 }
		 }
		return result;
	}
	
}
