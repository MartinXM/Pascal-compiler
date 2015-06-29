package symboltable;

import tree.ExpType;

public class TypeDef implements Comparable {
	public String name; // 用在typeHashTable里面的时候，name表示类型的名称，用在RecordNode中的时候，name表示record成员变量的名字
	public ExpType type;
	public int nestLevel;
	public Object pAttr; // pAttr point to the definition detail when type is enum, array or record, otherwise null
	public int size;

	public TypeDef() {
	}

	public TypeDef(String name, ExpType type, int nestLevel, Object pAttr, int size) {
		this.name = name;
		this.type = type;
		this.nestLevel = nestLevel;
		this.pAttr = pAttr;
		this.size = size;
	}

	@Override
	public int compareTo(Object o) {
		if (!(o instanceof TypeDef)) {
			return -1;
		}
		TypeDef another = (TypeDef)o;
		return name.compareTo(another.name);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof TypeDef)) {
			return false;
		}
		TypeDef another = (TypeDef)obj;
		return name.equals(another.name);
	}
}