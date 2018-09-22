package com.dimeng.p2p.modules.nciic.service;

import com.dimeng.framework.service.Service;

/*
 * 文 件 名:  NciicManage.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  YINKE
 * 修改时间:  2015年10月20日
 */
public interface NciicManage extends Service
{
    /**
     * 实名认证.
     * 
     * @param id
     *            身份证号码
     * @param name
     *            姓名
     * @param terminal 终端类型：PC、APP、WX
     * @return {@code boolean} 是否验证通过
     * @throws Throwable
     */
    boolean check(String id, String name, String terminal)
        throws Throwable;
    
    /**
     * 实名认证.
     * 
     * @param id
     *            身份证号码
     * @param name
     *            姓名
     * @param duplicatedName
     *            是否允许重名
     * @param terminal 终端类型：PC、APP、WX
     * @return {@code boolean} 是否验证通过
     * @throws Throwable
     */
    boolean check(String id, String name, boolean duplicatedName, String terminal)
        throws Throwable;
}
