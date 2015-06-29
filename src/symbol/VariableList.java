package symbol;

import tree.ExpType;

/*record the definition of each variable*/
public class VariableList {
	public String name;
	public ExpType type;
	public boolean isConst;
	public int nestLevel;
	public Object pAttr; /*pAttr point to the definition detail when type is enum, array or record, otherwise null*/	
	public LineList lines;
	public MemLoc memloc=new MemLoc();
	public VariableList next;
}
