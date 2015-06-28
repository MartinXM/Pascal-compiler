package Symbol;

import tree.ExpType;
import Symbol.TypeList;

public class TypeList {
	String name;
	AliaseList aliaseSet;
	ExpType type;
	int nestLevel;
	int size;
	Object pAttr; /*pAttr point to the definition detail when type is enum, array or record, otherwise null*/
	TypeList next;
}