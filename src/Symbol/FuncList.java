package Symbol;

import tree.ExpType;
import Symbol.FuncList;

public class FuncList {
	public String name;
	public SimpleTypeList paraList;
	public ExpType retType; /*record the return type*/
	public int nestLevel;
	public FuncList next;
}
