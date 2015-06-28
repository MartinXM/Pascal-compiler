package Symbol;

import tree.ExpType;
import Symbol.VariableList;

/*record the definition of each variable*/
public class VariableList {
	String name;
	ExpType type;
	boolean isConst;
	int nestLevel;
	Object pAttr; /*pAttr point to the definition detail when type is enum, array or record, otherwise null*/	
	LineList lines;
	MemLoc memloc;
	VariableList next;
}
