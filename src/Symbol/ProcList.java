package Symbol;

import Symbol.ProcList;

/*record the definition of each process*/
public class ProcList {
	public String name;
	public SimpleTypeList paraList;
	public int nestLevel;
	public ProcList next; 
}	