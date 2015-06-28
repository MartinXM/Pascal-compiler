package Symbol;

import tree.ExpType;
import Symbol.TypeList;

public class TypeList {
	public String name;
	public AliaseList aliaseSet;
	public ExpType type;
	public int nestLevel;
	public int size;
	public Object pAttr; /*pAttr point to the definition detail when type is enum, array or record, otherwise null*/
	public TypeList next;
}