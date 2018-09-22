package com.dimeng.p2p.service;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.S61.entities.T6114;
import com.dimeng.p2p.S61.entities.T6166;

/**
 * 资质文件上传接口方法类 （目前仅新浪存管使用）
 * 
 * @author raoyujun
 * @version [版本号, 2016年8月23日]
 */
public interface AuditFileSftpManage extends Service {

	public T6166 auditFileUpload(String fileUrl, String fileName)
			throws Throwable;

	public int insertT6166(T6166 t6166, T6114 t6114) throws Throwable;

}
