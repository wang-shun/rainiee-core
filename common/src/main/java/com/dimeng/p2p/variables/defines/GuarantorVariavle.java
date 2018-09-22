package com.dimeng.p2p.variables.defines;

import com.dimeng.framework.config.VariableTypeAnnotation;
import com.dimeng.framework.config.entity.VariableBean;

import java.io.InputStreamReader;

@VariableTypeAnnotation(id = "GUARANTOR", name = "申请担保方功能模版")
public enum GuarantorVariavle implements VariableBean {

    /**
     * 是否申请担保方功能
     */
    IS_HAS_GUARANTOR("是否申请担保方功能, true(有),false(没有)")
    {
        @Override
        public String getValue()
        {
            return "true";
        }
    };

	protected final String key;
	protected final String description;

	GuarantorVariavle(String description) {
		StringBuilder builder = new StringBuilder(getType());
		builder.append('.').append(name());
		key = builder.toString();
		this.description = description;
	}

	@Override
	public String getType() {
		return GuarantorVariavle.class.getAnnotation(VariableTypeAnnotation.class)
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
				GuarantorVariavle.class.getResourceAsStream(getKey()), "UTF-8")) {
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