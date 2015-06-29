package symbol;

import tree.ExpType;

public class TypeList {
	public String name; // 用在typeHashTable里面的时候，name表示类型的名称，用在RecordNode中的时候，name表示record成员变量的名字
	public AliaseList aliaseSet;
	public ExpType type;
	public int nestLevel;
	public int size;
	public Object pAttr; /*pAttr point to the definition detail when type is enum, array or record, otherwise null*/
	public TypeList next;
}