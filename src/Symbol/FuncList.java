package Symbol;

import tree.ExpType;
import Symbol.FuncList;

public class FuncList {
	String name;
	SimpleTypeList paraList;
	ExpType retType; /*record the return type*/
	int nestLevel;
	FuncList next;
}
