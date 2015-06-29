package symboltable;

import tree.ExpType;

public class SimpleType {
	public String name;
	public ExpType type;
	public boolean isVar;

	public SimpleType() {
	}

	public SimpleType(String name, ExpType type, boolean isVar) {
		this.name = name;
		this.type = type;
		this.isVar = isVar;
	}
}

