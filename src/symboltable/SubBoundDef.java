package symboltable;

import tree.ExpType;

public class SubBoundDef {
	public ExpType boundType;
	public Object LowerBound;
	public Object UpperBound;

	public SubBoundDef() {
	}

	public SubBoundDef(ExpType boundType, Object lowerBound, Object upperBound) {
		this.boundType = boundType;
		LowerBound = lowerBound;
		UpperBound = upperBound;
	}
}