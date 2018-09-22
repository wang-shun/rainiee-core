package com.dimeng.p2p.preservations.ebao.util;

import org.apache.log4j.Logger;
import org.mapu.themis.api.common.PreservationType;
import org.mapu.themis.api.request.contract.ContractFileDownloadUrlRequest;
import org.mapu.themis.api.request.contract.ContractFilePreservationCreateRequest;
import org.mapu.themis.api.request.contract.ContractFileViewUrlRequest;
import org.mapu.themis.api.response.contract.ContractFileDownloadUrlResponse;
import org.mapu.themis.api.response.contract.ContractFileViewUrlResponse;
import org.mapu.themis.api.response.preservation.PreservationCreateResponse;

import com.dimeng.p2p.preservations.ebao.cond.EbaoCond;
import com.dimeng.p2p.preservations.ebao.task.PreservationTesk;
import com.dimeng.util.StringHelper;

/**
 * 保全工具类
 * @author God
 *
 */
public class PreservationTools extends ThemisClientInit {
	private static final Logger logger = Logger.getLogger(PreservationTesk.class);
	/**
	 * 基于上传文件的合同保全 
	 * @param cond
	 * @return
	 */
	public static PreservationCreateResponse contractFilePreservation(EbaoCond cond) {
		logger.info("需要保全的参数：" + cond);
		ContractFilePreservationCreateRequest.Builder builder = new ContractFilePreservationCreateRequest.Builder();//创建保全对象
		builder.withFile(cond.getWithFile());//将保全文件上传到保全平台
		if(!StringHelper.isEmpty(cond.getWithPreservationTitle())){
			builder.withPreservationTitle(cond.getWithPreservationTitle());//保全标题
		}
		builder.withPreservationType(PreservationType.DIGITAL_CONTRACT);//保全分类（默认类型为电子合同，无需更改）
		builder.withIdentifer(cond.getWithIdentifer());//保全主体身份信息
		builder.withSourceRegistryId(cond.getWithSourceRegistryId());//来源企业用户ID(和保全主体对应)
		builder.withContractAmount(cond.getWithContractAmount());//合同金额
		builder.withContractNumber(cond.getWithContractNumber());//合同编号
		if(!StringHelper.isEmpty(cond.getWithPhoneNo())){
			builder.withMobilePhone(cond.getWithPhoneNo());// 用户电话（和邮箱二选一）
		}
		if(!StringHelper.isEmpty(cond.getWithUserEmail())){
			builder.withUserEmail(cond.getWithUserEmail());// 用户邮箱（和电话二选一）
		}
		builder.withComments(cond.getWithComments());//保全备注信息
		builder.withIsNeedSign(true);//设置为true代表使用电子签章功能， false代表不使用该功能
		return getClient().createPreservation(builder.build());
	}
	
	/**
	 * 保全文件查看
	 * @param preservationId 保全ID
	 */
	public static ContractFileViewUrlResponse contractFileViewUrl(long preservationId){
		ContractFileViewUrlRequest request = new ContractFileViewUrlRequest();
		request.setPreservationId(preservationId);
		return getClient().getContactFileViewUrl(request);
	}
	
	/**
	 * 合同文件下载
	 * @param preservationId 保全ID
	 * @return
	 */
	public ContractFileDownloadUrlResponse contractFileDownUrl(long preservationId){
		ContractFileDownloadUrlRequest request = new ContractFileDownloadUrlRequest();
		request.setPreservationId(preservationId);
		return getClient().getContactFileDownloadUrl(request);
	}
}
