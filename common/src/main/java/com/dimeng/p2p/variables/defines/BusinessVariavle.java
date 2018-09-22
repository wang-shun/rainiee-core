package com.dimeng.p2p.variables.defines;

import java.io.InputStreamReader;

import com.dimeng.framework.config.VariableTypeAnnotation;
import com.dimeng.framework.config.entity.VariableBean;

@VariableTypeAnnotation(id = "BUSINESS", name = "业务员功能模版")
public enum BusinessVariavle implements VariableBean {

    /**
     * 是否有业务员功能
     */
    IS_BUSINESS("是否有业务员功能, true(有),false(没有)")
    {
        @Override
        public String getValue()
        {
            return "false";
        }
    };

	protected final String key;
	protected final String description;

	BusinessVariavle(String description) {
		StringBuilder builder = new StringBuilder(getType());
		builder.append('.').append(name());
		key = builder.toString();
		this.description = description;
	}

	@Override
	public String getType() {
		return BusinessVariavle.class.getAnnotation(VariableTypeAnnotation.class)
				.id();
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getValue() {
		try (InputStreamReader reader = new InputStreamReader(
				BusinessVariavle.class.getResourceAsStream(getKey()), "UTF-8")) {
			StringBuilder builder = new StringBuilder();
			char[] cbuf = new char[1024];
			int len = reader.read(cbuf);
			while (len > 0) {
				builder.append(cbuf, 0, len);
				len = reader.read(cbuf);
			}
			return builder.toString();
		} catch (Throwable t) {
		}
		return null;
	}
    
    @Override
    public boolean isInit()
    {
        return true;
    }
}