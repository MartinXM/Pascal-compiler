package Symbol;

import Symbol.RecordNode;
import Symbol.TypeListRec;

public class RecordNode {
	RecordType type;
	TypeList ptr; /*ptr point to the definition in TypeList*/
	RecordNode next;
}