package symboltable;

import tree.ExpType;

import java.util.LinkedList;

public class FuncDef {
	public String name;
	public LinkedList<SimpleType> paraList;
	public ExpType retType; /*record the return type*/
	public int nestLevel;

	public FuncDef(String name, LinkedList<SimpleType> paraList, ExpType retType, int nestLevel) {
		this.name = name;
		this.paraList = paraList;
		this.retType = retType;
		this.nestLevel = nestLevel;
	}
}
