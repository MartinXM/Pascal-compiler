package symboltable;

import tree.ExpType;

import java.util.ArrayList;
import java.util.List;

public class FuncDef {
	public String name;
	public List<SimpleType> paraList;
	public ExpType retType; /*record the return type*/
	public int nestLevel;

	public FuncDef(String name, List<SimpleType> paraList, ExpType retType, int nestLevel) {
		this.name = name;
		this.paraList = paraList;
		this.retType = retType;
		this.nestLevel = nestLevel;
	}
}
