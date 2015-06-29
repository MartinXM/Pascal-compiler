package symboltable;

import java.util.LinkedList;

public class RecordDef {
	public RecordType type;
	public LinkedList<TypeDef> ptr; // Attribute in the record

	public RecordDef(RecordType type, LinkedList<TypeDef> ptr) {
		this.type = type;
		this.ptr = ptr;
	}
}