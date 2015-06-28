package Symbol;

import tree.ExpType;
import Symbol.TypeListRec;

/*record the definition of each type*/
public class TypeListRec {
	String name;
	AliaseList aliaseSet;
	ExpType type;
	int nestLevel;
	int size;
	Object pAttr; /*pAttr point to the definition detail when type is enum, array or record, otherwise null*/
	TypeListRec next;
}
