package symboltable;

import java.util.List;

/*record the definition of each process*/
public class ProcDef {
	public String name;
	public List<SimpleType> paraList;
	public int nestLevel;

	public ProcDef(String name, List<SimpleType> paraList, int nestLevel) {
		this.name = name;
		this.paraList = paraList;
		this.nestLevel = nestLevel;
	}
}