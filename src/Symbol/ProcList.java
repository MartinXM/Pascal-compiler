package Symbol;

import Symbol.ProcList;

/*record the definition of each process*/
public class ProcList {
	String name;
	SimpleTypeList paraList;
	int nestLevel;
	ProcList next; 
}	