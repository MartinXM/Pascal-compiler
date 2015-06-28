package Symbol;

import java.util.*;

import tree.*;

public class Symbol {
		
	public final static int ERROR_RETURN = 0xffff;
	public final static int PARA_OFFSET_INC = 4;
	public final static int SHIFT = 4;
	public final static int  ID_MAX_LEN = 10;
	public final static int SIZE = 211;
	public final static int OFFSET_INC = 4;
	
	public static int currentNestLevel = 0;
	public static int base = 0;
	public static int offset = 0;
	public static boolean TraceAnalyze = true;
	public static boolean Error = false;
	
	public static VariableList [] variableHashTable = new VariableList[SIZE];

	/*the hash table of types*/
	public static TypeList [] typeHashTable =new TypeList[SIZE];

	/*the hash table of function*/
	public static FuncList [] funcHashTable = new FuncList[SIZE];

	/*the hash table of procedure*/
	public static ProcList [] procHashTable = new ProcList[SIZE];

	/*record the total offset of each scope*/
	public static int [] totalOffset = new int[SIZE];
	
	
	public static int hash(String key){
		int temp = 0;
		int i = 0;
		while(i < key.length()) {
			temp = ((temp << SHIFT) + key.charAt(i)) % SIZE;
			++i;
		}

		return temp;
	}
	

	
	public static SubBoundDef newSubBoundDef(ExpType type, Object upper, Object lower){
		SubBoundDef newone = new SubBoundDef();
		newone.boundType = type;
		if(type == ExpType.INT) {
			newone.LowerBound = (Integer)lower;
			newone.UpperBound = (Integer)upper;	
		} else if(type == ExpType.CHAR) {
			newone.LowerBound = (Byte)lower;
			newone.UpperBound = (Byte) upper;
		} else if(type == ExpType.SIMPLE_ENUM) {
			newone.LowerBound = (String)lower;
			newone.UpperBound = (String)upper;
		}

		return newone;
		
	}
	
	public static ArrayDef newArrayDef(ExpType arrayType, ExpType boundType, Object upper, Object lower){
		ArrayDef newone = new ArrayDef();
		newone.arrayType = arrayType;
		newone.subBound = newSubBoundDef(boundType, lower, upper);

		return newone;
	}
	public static EnumDef newEnumDef(Object mark){
		EnumDef newone = new EnumDef();
		newone.mark = mark;
		newone.next = null;

		return newone;
		
	}
	public static EnumDef insertEnumDef(EnumDef enumList, Object mark){
		while(enumList.next != null)
			enumList = enumList.next;
		enumList.next = newEnumDef(mark);
		
		return enumList.next; 

		
	}
	public static TypeList newTypeDef(String name, ExpType type, int nestLevel, Object pAttr, int size){
		TypeList newone = new TypeList();
		newone.name = name;
		newone.type = type;
		newone.nestLevel = nestLevel;
		newone.pAttr = pAttr;
		newone.size = size;
		newone.next = null;

		return newone;
		
	}
	public static TypeList insertTypeDef(TypeList typeList, String name, ExpType type, int nestLevel, Object pAttr, int size){
		while(typeList.next != null)
			typeList = typeList.next;
		typeList.next = newTypeDef(name, type, nestLevel, pAttr, size);

		return typeList.next;
		
		
	}
	
	//DEFINED
	//ptr
	
	public static RecordNode newDefinedRecord(TypeList ptr){
		RecordNode newone = new RecordNode();
		newone.type = RecordType.DEFINED;
		newone.ptr = ptr;
		newone.next = null;

		return newone;
		
	}
	//ANONYMOUS
	//PTR
	public static RecordNode newAnonyRecord(TypeList typeList){
		RecordNode newone = new RecordNode();
		newone.type = RecordType.ANONYMOUS;
		newone.ptr = typeList; //转化成Object
		newone.next = null; 

		return newone;
		
	}
	public static SimpleTypeList newSimpleTypeList(String name, ExpType type, boolean isVar){
		SimpleTypeList newone = new SimpleTypeList();
		newone.name = name;
		newone.type = type;
		newone.isVar = isVar;
		
		return newone;
		
	}
	
	public static SimpleTypeList insertSimpleTypeList(SimpleTypeList simpleList, String name, ExpType type, boolean isVar){
		while(simpleList.next != null)
			simpleList = simpleList.next;
			simpleList.next = newSimpleTypeList(name, type, isVar);

		return simpleList.next;
		
	}
	//name
	//decl
	public static int procListInsert(TreeNode procHead){
		String name = (String)procHead.getAttribute();
		int nestLevel = currentNestLevel;
		int paraNestLevel = nestLevel + 1;
		int offset = 4;

		SimpleTypeList paraList;
		SimpleTypeList tmpList;
		TreeNode tmpNode;

		if((TreeNode)procHead.getChildren().get(0) == null)
			paraList = null;
		else {
		   	if(procHead.getChildren().get(0).getKind() == DeclKind.VAR_PARA) {
				paraList = newSimpleTypeList((String)procHead.getChildren().get(0).getChildren().get(0).getAttribute(), procHead.getChildren().get(0).getChildren().get(1).getType(), true);
			} else {
				paraList = newSimpleTypeList((String)procHead.getChildren().get(0).getChildren().get(0).getAttribute(), procHead.getChildren().get(0).getChildren().get(1).getType(), false);
			}

			varListInsert((String)procHead.getChildren().get(0).getChildren().get(0).getAttribute(), procHead.getChildren().get(0).getChildren().get(1).getType(), false, paraNestLevel, null, procHead.getLineNumber(), 0, offset);
			offset = offset + PARA_OFFSET_INC;

			tmpNode = procHead.getChildren().get(0).getSibling();
			tmpList = paraList;
			while(tmpNode != null) {
				if(tmpNode.getKind() == DeclKind.VAR_PARA)
					tmpList = insertSimpleTypeList(tmpList, (String)tmpNode.getChildren().get(0).getAttribute(), tmpNode.getChildren().get(1).getType(), true);
				else
					tmpList = insertSimpleTypeList(tmpList, (String)tmpNode.getChildren().get(0).getAttribute(), tmpNode.getChildren().get(1).getType(), true);

				varListInsert((String) tmpNode.getChildren().get(0).getAttribute(), tmpNode.getChildren().get(1).getType(), false, paraNestLevel, null, tmpNode.getLineNumber(), 0, offset);
				offset = offset + PARA_OFFSET_INC;
				
				tmpNode = tmpNode.getSibling();
			}
		}
		
		int h = hash(name);
		ProcList l = procHashTable[h];
		ProcList tmp = l;
		while((tmp != null) && (name.compareTo(tmp.name)!=0))
			tmp = tmp.next;
		if(tmp == null || name.compareTo(tmp.name)==0 && nestLevel>tmp.nestLevel) { /*process with same nestlevel not yet in the table, insert to the list head*/
			tmp = new ProcList();
			tmp.name = name;
			tmp.paraList = paraList;
			tmp.nestLevel = nestLevel;
			tmp.next = (l == null)? null:l;
			procHashTable[h] = tmp;
		}

		return offset;

		
	}
	public static int funcListInsert(TreeNode funcHead){
		String name = (String)funcHead.getAttribute();
		int nestLevel = currentNestLevel;
		int paraNestLevel = nestLevel + 1;
		int offset = 4;

		ExpType retType = funcHead.getChildren().get(1).getType();
		SimpleTypeList paraList;
		SimpleTypeList tmpList;
		TreeNode tmpNode;


		if(funcHead.getChildren().get(0) == null)
			paraList = null;
		else {
		   	if(funcHead.getChildren().get(0).getKind() == DeclKind.VAR_PARA) {
				//若传递参数为实参
				paraList = newSimpleTypeList((String)funcHead.getChildren().get(0).getChildren().get(0).getAttribute(), funcHead.getChildren().get(0).getChildren().get(1).getType(), true);
			} else {
				//若传递参数为形参
				paraList = newSimpleTypeList((String)funcHead.getChildren().get(0).getChildren().get(0).getAttribute(), funcHead.getChildren().get(0).getChildren().get(1).getType(), false);
			}
			varListInsert((String)funcHead.getChildren().get(0).getChildren().get(0).getAttribute(),funcHead.getChildren().get(0).getChildren().get(1).getType(), false, paraNestLevel, null, funcHead.getLineNumber(), 0, offset);
			offset = offset + PARA_OFFSET_INC;

			tmpNode = funcHead.getChildren().get(0).getSibling();
			tmpList = paraList;
			while(tmpNode != null) {
				if(tmpNode.getKind() == DeclKind.VAR_PARA)
					tmpList = insertSimpleTypeList(tmpList, (String)tmpNode.getChildren().get(0).getAttribute(), tmpNode.getChildren().get(1).getType(), true);
				else
					tmpList = insertSimpleTypeList(tmpList, (String)tmpNode.getChildren().get(0).getAttribute(), tmpNode.getChildren().get(1).getType(), false);

				varListInsert((String)tmpNode.getChildren().get(0).getAttribute(), tmpNode.getChildren().get(1).getType(), false, paraNestLevel, null, tmpNode.getLineNumber(), 0, offset);
				offset = offset + PARA_OFFSET_INC;
				
				tmpNode = tmpNode.getSibling();
			}
		}

		//符号表插入返回值,与函数同名
		varListInsert((String)funcHead.getAttribute(), retType, false, paraNestLevel, null, funcHead.getLineNumber(), 0, offset);
		offset = offset + PARA_OFFSET_INC;

		int h = hash(name);
		FuncList l = funcHashTable[h];
		FuncList tmp = l;
		while((tmp != null) && (name.compareTo( tmp.name)!=0))
			tmp = tmp.next;
		if(tmp == null || (name.compareTo(tmp.name)==0 && nestLevel>tmp.nestLevel)) { /*process with same nestlevel not yet in the table, insert to the list head*/
			tmp = new FuncList();
			tmp.name = name;
			tmp.paraList = paraList;
			tmp.retType = retType;
			tmp.nestLevel = nestLevel;
			tmp.next = (l == null)? null:l;
			funcHashTable[h] = tmp;
		}

		return offset;
		
	}
	public static void typeListAliaseInsert(String name, String aliase){
		int h = hash(name);
		TypeList l = typeHashTable[h];
		while((l != null) && (name.compareTo(l.name)!=0))
			l = l.next;
		if(l != null) {
			AliaseList t = l.aliaseSet;
			while(t.next != null)
				t = t.next;
			t.next = new AliaseList();
			t.next.aliase = aliase;
			t.next.next = null;
		}
	}
	public static void typeListInsert(String name, ExpType type, int nestLevel, Object pAttr, int size){
		int h = hash(name);
		TypeList l = typeHashTable[h];
		TypeList tmp = l;
		while((tmp != null) && (name.compareTo(tmp.name)!=0))
			tmp = tmp.next;
		if(tmp == null || (name.compareTo(tmp.name)==0 && nestLevel>tmp.nestLevel)) { /*process with same nestlevel not yet in the table, insert to the list head*/
			tmp = new TypeList();
			tmp.name = name;
			tmp.aliaseSet = null;
			tmp.type = type;
			tmp.nestLevel = nestLevel;
			tmp.pAttr = pAttr;
			tmp.size = size;
			tmp.next = (l == null)? null:l;
			typeHashTable[h] = tmp;
		}

	}
	public static void varListInsert(String name, ExpType type, boolean b, int nestLevel, Object pAttr, int lineno, int baseLoc, int offset){
			int h = hash(name);
			VariableList l = variableHashTable[h];
			VariableList tmp = l;
			while((tmp != null) && (name.compareTo(tmp.name)!=0))
					tmp = tmp.next;
			if(tmp == null || (name.compareTo(tmp.name)==0 && nestLevel>tmp.nestLevel)) { /*process with same nestlevel not yet in the table, insert to the list head*/
				tmp = new VariableList();
				tmp.name = name;
				tmp.type = type;
				tmp.isConst = b; 
				tmp.nestLevel = nestLevel;
				tmp.pAttr = pAttr;
				tmp.lines = new LineList();
				tmp.lines.lineno = lineno;
				tmp.lines.next = null;
				tmp.memloc.baseLoc = baseLoc;
				tmp.memloc.offset = offset;
				tmp.next = (l == null)? null:l;
				variableHashTable[h] = tmp;
			} else { /*find the exact variable*/
				LineList t = tmp.lines;
				while(t.next != null)
					t = t.next;
				t.next = new LineList();
				t.next.lineno = lineno;
				t.next.next = null;
			}
	}


	public static VariableList varListLookup(String name){
		int h = hash(name);
		VariableList l = variableHashTable[h];
		while((l != null) && (name.compareTo( l.name)!=0))
			l = l.next;
		if(l == null)
			return null;
		else {
			l.memloc.baseLoc = currentNestLevel - l.nestLevel; 
			return l;
		}
		
	}
	public static FuncList funcListLookup(String name){
		int h = hash(name);
		FuncList l = funcHashTable[h];
		while((l != null) && (name.compareTo( l.name)!=0))
			l = l.next;
		if(l == null)
			return null;
		else  
			return l;
		
	}
	public static ProcList procListLookup(String name){
		int h = hash(name);
		ProcList l = procHashTable[h];
		while((l != null) && (name.compareTo( l.name)!=0))
			l = l.next;
		if(l == null)
			return null;
		else
			return l;
		
	}
	public static TypeList typeListLookup(String name){
		int h = hash(name);
		TypeList l = typeHashTable[h];
		while((l != null) && (name.compareTo( l.name)!=0))
			l = l.next;
		if(l == null)
			return null;
		else
			return l;
		
	}
	//bound i
	public static LookupRet arrayLookup(String a, int i){
		int lower, upper, size;
		LookupRet ret = null;
		ret.totalOff = ERROR_RETURN;
		ret.jumpLevel = ERROR_RETURN;
		ret.type = ExpType.VOID;
		VariableList l = varListLookup(a);
		if(l.type == ExpType.ARRAY && l.pAttr != null) {
			lower = (int) ((ArrayDef)(l.pAttr)).subBound.LowerBound;
			upper = (int) ((ArrayDef)(l.pAttr)).subBound.UpperBound;
			if(i>=lower && i<=upper) {
				ret.totalOff = l.memloc.offset+OFFSET_INC*(i-lower);
				ret.jumpLevel = currentNestLevel - l.nestLevel;
				ret.type = l.type;
			} 
		}
		return ret;

		
	}
	public static LookupRet recordLookup(String rec, String a){
		VariableList l = varListLookup(rec);
		TypeList plist;
		int size = 0;
		LookupRet ret = null;
		ret.totalOff = ERROR_RETURN;
		ret.jumpLevel = ERROR_RETURN;
		ret.type = ExpType.VOID;
		if(l.type == ExpType.RECORD && l.pAttr != null) {
			if(((RecordNode)(l.pAttr)).type == RecordType.ANONYMOUS) {
				plist = ((RecordNode)(l.pAttr)).ptr; 
			} else {
				plist = ((RecordNode)(l.pAttr)).ptr; 
			}
			while(plist != null && plist.name.compareTo(a)!=0) {
				size += 1;
				plist = plist.next;
			}
			if(plist != null) {
				ret.totalOff = size*OFFSET_INC + l.memloc.offset;
				ret.jumpLevel = currentNestLevel - l.nestLevel;
				ret.type = plist.type;
			}
		}
		return ret;	
		
	}
	public static void initScope(){
		currentNestLevel = -1;
		int i;
		for(i=0; i<SIZE; i++) {
			variableHashTable[i] = null;
			typeHashTable[i] = null;
			procHashTable[i] = null;
			funcHashTable[i] = null;
		}

	}
	public static int enterNewScope(TreeNode t){
		currentNestLevel += 1;
		totalOffset[currentNestLevel] = buildSymtab(t);
		return 	totalOffset[currentNestLevel];
	}
	//free
	public static int leaveScope(){
		int tmp = currentNestLevel;
		int retValue = totalOffset[currentNestLevel];
		Object ptr1 = new Object();
		Object ptr2 = new Object();
		Object ptr3 = new Object();
		currentNestLevel -= 1;
		int i;
		for(i=0; i<SIZE; i++) {
			VariableList vl = variableHashTable[i];
			TypeList tl = typeHashTable[i];
			FuncList fl = funcHashTable[i];
			ProcList pl = procHashTable[i];

			while(vl != null && vl.nestLevel >= tmp) {
				ptr1 = (Object)vl;
				vl = vl.next;
				ptr2 = (Object)(((VariableList)ptr1).lines);
				while((LineList)ptr2 != null) {
					ptr3 = (Object)(((LineList)ptr2).next);
					//free((LineList)ptr2);
					ptr2 = null;
					ptr2 = ptr3;
				}
				//free((VariableList)ptr1);
				ptr1 = null;
			}
			variableHashTable[i] = vl;

			while(tl != null && tl.nestLevel >= tmp) {
				ptr1 = (Object)tl;
				tl = tl.next;
				ptr2 = (Object)(((TypeList)ptr1).aliaseSet);
				while((AliaseList)ptr2 != null) {
					ptr3 = (Object)(((AliaseList)ptr2).next);
					//free((AliaseList)ptr2);
					ptr2 = null;
					ptr2 = ptr3;
				}
				//free((VariableList)ptr1);
				ptr1 = null;
			}
			typeHashTable[i] = tl;
		}
		return retValue;

	}
	
	public static void printSymTab( ){
		int i;
		//fprintf(listing, "Variable Name		NestLevel 	Location 	Line Number\n");
		//fprintf(listing, "------------- 	---------	-------- 	-----------\n");
		for(i=0; i<SIZE; i++) {
			if(variableHashTable[i] != null) {
				VariableList l = variableHashTable[i];
				while(l != null) {
					LineList t = l.lines;
//					fprintf(listing, "%-14s ", l.name);
//					fprintf(listing, "%-8d", l.nestLevel);
//					fprintf(listing, "%-8d ", l.memloc.offset);
					while(t != null) {
						//fprintf(listing, "%4d ", t.lineno);
						t = t.next;
					}
					//fprintf(listing, "\n");
					l = l.next;
				}
			}
		}

//		fprintf(listing, "Function Name		NestLevel 	Return Type 	Parameter\n");
//		fprintf(listing, "------------- 	---------	-------- 	-----------\n");
		for(i=0; i<SIZE; i++) {
			if(funcHashTable[i] != null) {
				FuncList l = funcHashTable[i];
				while(l != null) {
					SimpleTypeList t = l.paraList;
//					fprintf(listing, "%-14s ", l.name);
//					fprintf(listing, "%-8d", l.nestLevel);
//					fprintf(listing, "%-8d ", l.retType);
					while(t != null) {
//						fprintf(listing, "%-14s ", t.name);
						t = t.next;
					}
				//	fprintf(listing, "\n");
					l = l.next;
				}
			}
		}	
		

	}
	
	//analyze
	
	public static int buildSymtab(TreeNode syntaxTree){
		offset = -4;
		traverse(syntaxTree);
		if(TraceAnalyze) {
//			fprintf(listing, "\nSymbol table:\n\n");
//			printSymTab(listing);
		}
		return -offset;
	}
	
	public static void traverse(TreeNode t) {
		if(t != null) {
			int i;
			for(i=0; i<4; i++)
				insertNode(t.getChildren().get(i));
		}
	}
	
	public static void typeError(TreeNode t, String message) {
		//fprintf(listing, "Type error at line %d: %s\n", t->lineno, message);
		Error = true;
	}
	//nodekind
	//node_declare
	public static void insertNode(TreeNode t) {
		if(t == null) 
			return;
		if(t.getKind() instanceof DeclKind) {
			switch((DeclKind)t.getKind()) {
				case CONST:
				{
					while(t != null) {
						offset -= OFFSET_INC;
						varListInsert((String)t.getAttribute(), t.getType(), true, currentNestLevel, null, t.getLineNumber(), 0, offset);
						t = t.getSibling();
					}	
					break;
				}
				case VAR:
				{
					while(t != null) {
						TreeNode pname = t.getChildren().get(0);
						TreeNode ptype = t.getChildren().get(1);
						while(pname != null) {
							switch((TypeKind)ptype.getKind()) {
									case SIMPLE_ID:
									{
										TypeList l = typeListLookup((String)ptype.getAttribute());
										switch(l.type) {
											case ARRAY: 
											{
												offset -= l.size;
												varListInsert((String)pname.getAttribute(), l.type, false, currentNestLevel, l.pAttr, pname.getLineNumber(), 0, offset);
												break;
											}
											case RECORD:
											{
												offset -= l.size;
												varListInsert((String)pname.getAttribute(), l.type, false, currentNestLevel, l.pAttr, pname.getLineNumber(), 0, offset);
												break;
											}
											default:
											{	
												offset -= OFFSET_INC;	
												varListInsert((String)pname.getAttribute(), l.type, false, currentNestLevel, l.pAttr, pname.getLineNumber(), 0, offset);
												break;
											}
										}
										break;
									}
									case SIMPLE_ENUM: 
									{
										EnumDef eptr = newEnumDef((String)ptype.getAttribute());
										EnumDef etmp = eptr;
										while(ptype.getSibling() != null) {
											ptype = ptype.getSibling();
											etmp = insertEnumDef(etmp, (String)ptype.getAttribute());
										}

										offset -= OFFSET_INC;	
										varListInsert((String)pname.getAttribute(), ExpType.SIMPLE_ENUM, false,  currentNestLevel, (Object)eptr, pname.getLineNumber(), 0, offset);

										break;
									}
									case SIMPLE_LIMIT: 
									{
										SubBoundDef sub =  newSubBoundDef(ptype.getChildren().get(0).getType(), (Integer)(ptype.getChildren().get(0).getAttribute()), (Integer)ptype.getChildren().get(1).getAttribute());
										offset -= OFFSET_INC;
										varListInsert((String)pname.getAttribute(), ExpType.SIMPLE_LIMIT, false, currentNestLevel, (Object)sub, pname.getLineNumber(), 0, offset);
										break;
									}
									case SIMPLE_SYS: 
									{
										offset -= OFFSET_INC;
										varListInsert((String)pname.getAttribute(), ptype.getType(), false,  currentNestLevel, null, pname.getLineNumber(), 0, offset);
										break;
									}
									case ARRAY: 
									{
										int arraySize = 0;
										if(ptype.getChildren().get(0).getType() == ExpType.SIMPLE_LIMIT) {
											arraySize = (Integer)ptype.getChildren().get(0).getChildren().get(1).getAttribute() - (Integer)ptype.getChildren().get(0).getChildren().get(0).getAttribute() + 1; 
											ArrayDef pAttr = newArrayDef(ptype.getChildren().get(1).getType(), ptype.getChildren().get(0).getChildren().get(0).getType(), (Integer)ptype.getChildren().get(0).getChildren().get(0).getAttribute(), (Integer)ptype.getChildren().get(0).getChildren().get(1).getAttribute());
											offset -= (OFFSET_INC * arraySize);
											varListInsert((String)pname.getAttribute(), ptype.getType(), false, currentNestLevel, (Object)pAttr, pname.getLineNumber(), 0, offset);
										}
						    			break;
									}
									case RECORD:
									{
										TreeNode nameNode = ptype.getChildren().get(0);
										TreeNode typeNode = ptype.getChildren().get(1);

										TypeList l = newTypeDef((String)nameNode.getAttribute(), typeNode.getType(), currentNestLevel, null, OFFSET_INC);
										offset -= OFFSET_INC;
										TypeList tmp = l;
										while(nameNode.getSibling() != null) {
											nameNode = nameNode.getSibling();
											tmp = insertTypeDef(tmp, (String)nameNode.getAttribute(),typeNode.getType(), currentNestLevel, null, OFFSET_INC);
											offset -= OFFSET_INC;	
										}
									
										while(ptype.getSibling() != null) {
											ptype = ptype.getSibling();
											nameNode = ptype.getChildren().get(0);
											typeNode = ptype.getChildren().get(1);
											while(nameNode != null) {
												tmp = insertTypeDef(tmp, (String)nameNode.getAttribute(), typeNode.getType(), currentNestLevel, null, OFFSET_INC);	
												offset -= OFFSET_INC;
												nameNode = nameNode.getSibling();
											}
										}
										RecordNode r = newAnonyRecord(l); 
										varListInsert((String)pname.getAttribute(), ExpType.RECORD, false, currentNestLevel, (Object)r, t.getLineNumber(), 0, offset);	

										break;
									}
									default:
										break;						
							}
							pname = pname.getSibling();
						}
						t = t.getSibling();
					}
					break;
				}
				case TYPE:
				{
					while(t != null) {
						switch((TypeKind)t.getChildren().get(1).getKind()) {
							case SIMPLE_ID: 
							{
								typeListAliaseInsert((String)t.getChildren().get(0).getAttribute(), (String)t.getChildren().get(1).getAttribute());	
								break;
							}
							case SIMPLE_ENUM: 
							{
								TreeNode eList = t.getChildren().get(1).getChildren().get(0);
								EnumDef eptr = newEnumDef((String)eList.getAttribute());
								EnumDef etmp = eptr;
								while(eList.getSibling() != null) {
									eList = eList.getSibling();
									etmp = insertEnumDef(etmp, (String)eList.getAttribute());
								}
								typeListInsert((String)t.getChildren().get(0).getAttribute(), ExpType.SIMPLE_ENUM, currentNestLevel, (Object)eptr, OFFSET_INC);							
								break;
							}
							case SIMPLE_LIMIT: 
							{
								SubBoundDef sub =  newSubBoundDef(t.getChildren().get(1).getChildren().get(0).getType(),(Integer)t.getChildren().get(1).getChildren().get(0).getAttribute(), (Integer)t.getChildren().get(1).getChildren().get(1).getAttribute());
								typeListInsert((String)t.getChildren().get(0).getAttribute(), ExpType.SIMPLE_LIMIT, currentNestLevel, (Object)sub, OFFSET_INC);
								break;
							}
							case SIMPLE_SYS: 
							{
								typeListInsert((String)t.getChildren().get(0).getAttribute(), t.getChildren().get(1).getType(), currentNestLevel, null, OFFSET_INC);
						    	break;
							}
							case ARRAY: 
							{
								int size = 0;
								TreeNode atype = t.getChildren().get(1);
								if(atype.getChildren().get(0).getType() == ExpType.SIMPLE_LIMIT) {
								ArrayDef pAttr = newArrayDef(atype.getChildren().get(1).getType(), atype.getChildren().get(0).getChildren().get(0).getType(), (Integer)atype.getChildren().get(0).getChildren().get(0).getAttribute(),(Integer)atype.getChildren().get(0).getChildren().get(1).getAttribute());
								size =((Integer)(atype.getChildren().get(0).getChildren().get(1).getAttribute())-(Integer)atype.getChildren().get(0).getChildren().get(0).getAttribute()+1)*OFFSET_INC;
								typeListInsert((String)t.getChildren().get(0).getAttribute(), t.getChildren().get(1).getType(), currentNestLevel, (Object)pAttr, size);
								}							
						    	break;
							}
							case RECORD: 
							{
								TreeNode pname = t.getChildren().get(0);
								TreeNode ptype = t.getChildren().get(1);
								TreeNode nameNode = ptype.getChildren().get(0);
								TreeNode typeNode = ptype.getChildren().get(1);
								int size = 0;

								TypeList l = newTypeDef((String)nameNode.getAttribute(), typeNode.getType(), currentNestLevel, null, OFFSET_INC);
								size ++;
								TypeList tmp = l;
								while(nameNode.getSibling() != null) {
									nameNode = nameNode.getSibling();
									tmp = insertTypeDef(tmp, (String)nameNode.getAttribute(), typeNode.getType(), currentNestLevel, null, OFFSET_INC);
									size ++;	
								}
								
								while(ptype.getSibling() != null) {
									ptype = ptype.getSibling();
									nameNode = ptype.getChildren().get(0);
									typeNode = ptype.getChildren().get(1);
									while(nameNode != null) {
										tmp = insertTypeDef(tmp, (String)nameNode.getAttribute(), typeNode.getType(), currentNestLevel, null, OFFSET_INC);	
										size ++;
										nameNode = nameNode.getSibling();
									}
								}
								RecordNode r = newDefinedRecord(l); 
								//printf("size %d",size*OFFSET_INC);
								typeListInsert((String)pname.getAttribute(), ExpType.RECORD,  currentNestLevel, (Object)r, size*OFFSET_INC);	

								break;
							}
							default:
								break;						
						}
						t = t.getSibling();
					}
					break;
				}
				case FUNCTION:
				{
					funcListInsert(t.getChildren().get(0));
			//		enterNewScope(t.getChildren().get(1));	
					break;
				} 
				default:
					break;
			}
			
		}

	}

	
	
	
	
	
}
