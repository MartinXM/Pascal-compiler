#ifndef _SYMTAB_H_
#define _SYMTAB_H_

#include "global.h"

/*array/record lookup return type*/
typedef struct LookupRetRec {
	int totalOff;
	int jumpLevel;
	ExpType type;
} LookupRet;

/*record memory location*/
typedef struct MemLocRec {
	int baseLoc;
	int offset;
	ExpType type;
} MemLoc;	

/*the list of line numbers of the source code in which a variable is referenced*/
typedef struct LineListRec {
	int lineno;
	struct LineListRec* next;
}* LineList;

/*the list of alias name of the variable or type*/
typedef struct AliaseListRec {
	char* aliase;
	struct AliaseListRec* next;
}* AliaseList;

/*subBound details*/
typedef union {
	int i;
	char c;
	char* m;
} Bound;

typedef struct SubBoundDefRec {
	ExpType boundType;
	Bound LowerBound;
	Bound UpperBound;
}* SubBoundDef;

/*array details*/
typedef struct ArrayDefRec {
	ExpType arrayType;
	SubBoundDef subBound;
}* ArrayDef;

/*enum detail*/
typedef struct EnumDefRec {
	char* mark; /*point to a constant value*/
	struct EnumDefRec* next;		
}* EnumDef;

/*record detail*/
typedef enum {ANONYMOUS, DEFINED} RecordType;

typedef struct RecordNodeRec {
	RecordType type;
	union {
		struct TypeListRec* pDef; /*pDef point to the definition in TypeList*/
		struct TypeListRec* pAnony; /*pAnonymous point to the anonymous record type*/
	} ptr;
	struct RecordDefRec* next;
}* RecordDef;

/*record the definition of each type*/
typedef struct TypeListRec {
	char* name;
	AliaseList aliaseSet;
	ExpType type;
	int nestLevel;
	int size;
	void* pAttr; /*pAttr point to the definition detail when type is enum, array or record, otherwise null*/
	struct TypeListRec* next;
}* TypeList;

/*record the definition of each variable*/
typedef struct VariableListRec {
	char* name;
	ExpType type;
	int isConst;
	int nestLevel;
	void* pAttr; /*pAttr point to the definition detail when type is enum, array or record, otherwise null*/	
	LineList lines;
	MemLoc memloc;
	struct VariableListRec* next;
}* VariableList;

/*record the definition of each function*/
typedef struct SimpleTypeListRec {
	char* name;
	ExpType type;
	int isVar;
	struct SimpleTypeListRec* next;
}* SimpleTypeList;

typedef struct FuncListRec {
	char* name;
	SimpleTypeList paraList;
	ExpType retType; /*record the return type*/
	int nestLevel;
	struct FuncListRec* next;
}* FuncList;

/*record the definition of each process*/
typedef struct ProcListRec {
	char* name;
	SimpleTypeList paraList;
	int nestLevel;
	struct ProcListRec* next; 
}* ProcList;


static int hash (char* key);
SubBoundDef newSubBoundDef(ExpType type, void* upper, void* lower);
ArrayDef newArrayDef(ExpType arrayType, ExpType boundType, void* upper, void* lower);
EnumDef newEnumDef(char* mark);
EnumDef insertEnumDef(EnumDef enumList, char* mark);
TypeList newTypeDef(char* name, ExpType type, int nestLevel, void* pAttr, int size);
TypeList insertTypeDef(TypeList typeList, char* name, ExpType type, int nestLevel, void* pAttr, int size);
RecordDef newDefinedRecord(TypeList ptr);
RecordDef newAnonyRecord(TypeList typeList);
SimpleTypeList newSimpleTypeList(char* name, ExpType type, int isVar);
SimpleTypeList insertSimpleTypeList(SimpleTypeList simpleList, char* name, ExpType type, int isVar); 

int procListInsert(TreeNode* procHead);
int funcListInsert(TreeNode* funcHead);
void typeListAliaseInsert(char* name, char* aliase);
void typeListInsert(char* name, ExpType type, int nestLevel, void* pAttr, int size);
void varListInsert(char* name, ExpType type, int isConst, int nestLevel, void* pAttr, int lineno, int baseLoc, int offset);


VariableList varListLookup(char* name);
FuncList funcListLookup(char* name);
ProcList procListLookup(char* name);
TypeList typeListLookup(char* name);
LookupRet arrayLookup(char* a, int i);
LookupRet recordLookup(char* rec, char* a);
void initScope();
int enterNewScope(TreeNode* t);
int leaveScope();
void printSymTab(FILE* listing);


#endif
