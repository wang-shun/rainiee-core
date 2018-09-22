package com.dimeng.p2p.modules.systematic.console.service.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import com.dimeng.p2p.common.enums.SkinStatus;

public class Skin implements Serializable {

	private static final long serialVersionUID = 1L;

	public enum Status {
		/**
		 * 是否生效
		 */
		S("是"), F("否");
		protected final String name;

		private Status(String name) {
			this.name = name;
		}

		/**
		 * 获取中文名称.
		 * 
		 * @return {@link String}
		 */
		public String getName() {
			return name;
		}
	}

	/**
	 * 皮肤管理 ID
	 */
	public int id;
	/**
	 * 皮肤主题名称
	 */
	public String themeName;
	/**
	 * SY,首页；GRZX,个人中心
	 */
	public SkinStatus location;
	/**
	 * 图片路径
	 */
	public String pic;
	/**
	 * 发布者
	 */
	public int operator;
	/**
	 * 创建时间
	 */
	public Timestamp createTime;
	/**
	 * 最后修改时间
	 */
	public Timestamp lastUpdateTime;
	/**
	 * 是否生效(是;否)
	 */
	public Status isEffective;
	/**
	 * 管理员
	 */
	public String name;
}
