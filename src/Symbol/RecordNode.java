package Symbol;

import Symbol.RecordNode;
import Symbol.TypeListRec;

public class RecordNode {
	public RecordType type;
	public TypeList ptr; /*ptr point to the definition in TypeList*/
	public RecordNode next;
}