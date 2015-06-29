package symboltable;

import tree.ExpType;

/*record the definition of each variable*/
public class VariableDef {
	public String name;
	public ExpType type;
	public boolean isConst;
	public int nestLevel;
	public Object pAttr; // pAttr point to the definition detail when type is enum, array or record, otherwise null
	public MemLoc memloc;

	public VariableDef(String name, ExpType type, boolean isConst, int nestLevel, Object pAttr, int offset) {
		this.name = name;
		this.type = type;
		this.isConst = isConst;
		this.nestLevel = nestLevel;
		this.pAttr = pAttr;
		this.memloc = new MemLoc(0, offset);
	}
}
