package symboltable;

import tree.ExpType;

public class ArrayDef {
	public ExpType arrayType;
	public SubBoundDef subBound;

	public ArrayDef() {
	}

	public ArrayDef(ExpType arrayType, ExpType boundType, Object upper, Object lower) {
		this.arrayType = arrayType;
		this.subBound = new SubBoundDef(boundType, upper, lower);
	}
}
