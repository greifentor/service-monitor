package de.ollie.servicemonitor.evaluation.model;

import lombok.Data;
import lombok.Generated;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
@Generated
public class ValueExpression implements ExecutableExpression {

	private Object value;

	@Override
	public Type getType() {
		return Type.VALUE;
	}

}