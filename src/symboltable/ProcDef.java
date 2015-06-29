package symboltable;

import java.util.LinkedList;

/*record the definition of each process*/
public class ProcDef {
	public String name;
	public LinkedList<SimpleType> paraList;
	public int nestLevel;

	public ProcDef(String name, LinkedList<SimpleType> paraList, int nestLevel) {
		this.name = name;
		this.paraList = paraList;
		this.nestLevel = nestLevel;
	}
}