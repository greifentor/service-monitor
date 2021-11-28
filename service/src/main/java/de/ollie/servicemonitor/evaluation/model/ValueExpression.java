package de.ollie.servicemonitor.evaluation.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class ValueExpression implements ExecutableExpression {

	private Object value;

	@Override
	public Type getType() {
		return Type.VALUE;
	}

}