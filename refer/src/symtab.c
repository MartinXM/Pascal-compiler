#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "symtab.h"
#include "analyze.h"

#define ERROR_RETURN 0xffff

/*define size of the hash table*/
#define SIZE 211

/*define parameter offset increase*/
#define PARA_OFFSET_INC 4

/*define SHIFT, use 2^SHIFT as multiplier in hash function*/
#define SHIFT 4

/*define enum type max length*/
#define ID_MAX_LEN 10

int currentNestLevel = 0;

/*the hash function*/
static int hash (char* key) {
	int temp = 0;
	int i = 0;
	while(key[i] != '\0') {
		temp = ((temp << SHIFT) + key[i]) % SIZE;
		++i;
	}

	return temp;
}

/*===============定义符号表，分为变量、类型、函数、过程四个子表================*/



/*the hash table of variables*/
static VariableList variableHashTable[SIZE];

/*the hash table of types*/
static TypeList typeHashTable[SIZE];

/*the hash table of function*/
static FuncList funcHashTable[SIZE];

/*the hash table of procedure*/
static ProcList procHashTable[SIZE];

/*record the total offset of each scope*/
static int totalOffset[50];



/*=========================定义对符号表的插入操作=================================*/

/*build a new sub-bound type definition*/
SubBoundDef newSubBoundDef(ExpType type, void* lower, void* upper) {
	SubBoundDef new = (SubBoundDef) malloc(sizeof(struct SubBoundDefRec));
	new->boundType = type;
	if(type == EXPTYPE_INT) {
		new->LowerBound.i = *(int*)lower;
		new->UpperBound.i = *(int*)upper;	
	} else if(type == EXPTYPE_CHAR) {
		new->LowerBound.c = *(char*)lower;
		new->UpperBound.c = *(char*)upper;
	} else if(type == EXPTYPE_SIMPLE_ENUM) {
		new->LowerBound.m = (char*)lower;
		new->UpperBound.m = (char*)upper;
	}

	return new;
}

/*build a new array definition*/
ArrayDef newArrayDef(ExpType arrayType, ExpType boundType, void* lower, void* upper) {
	ArrayDef new = (ArrayDef) malloc(sizeof(struct ArrayDefRec));
	new->arrayType = arrayType;
	new->subBound = newSubBoundDef(boundType, lower, upper);

	return new;
}

/*build a new enum definition*/
EnumDef newEnumDef(char* mark) {
	EnumDef new = (EnumDef) malloc(sizeof(struct EnumDefRec));
	new->mark = mark;
	new->next = NULL;

	return new;
}

EnumDef insertEnumDef(EnumDef enumList, char* mark) {
	while(enumList->next != NULL)
		enumList = enumList->next;
	enumList->next = newEnumDef(mark);
	
	return enumList->next; 
}

/*build a new type list node*/
TypeList newTypeDef(char* name, ExpType type, int nestLevel, void* pAttr, int size) {
	TypeList new = (TypeList) malloc(sizeof(struct TypeListRec));
	new->name = name;
	new->type = type;
	new->nestLevel = nestLevel;
	new->pAttr = pAttr;
	new->size = size;
	new->next = NULL;

	return new;
}

TypeList insertTypeDef(TypeList typeList, char* name, ExpType type, int nestLevel, void* pAttr, int size) {
	while(typeList->next != NULL)
		typeList = typeList->next;
	typeList->next = newTypeDef(name, type, nestLevel, pAttr, size);

	return typeList->next;
}

/*build a new record definition*/
RecordDef newDefinedRecord(TypeList ptr) {
	RecordDef new = (RecordDef) malloc(sizeof(struct RecordNodeRec));
	new->type = DEFINED;
	new->ptr.pDef = ptr;
	new->next = NULL;

	return new;
}

RecordDef newAnonyRecord(TypeList typeList) {
	RecordDef new = (RecordDef) malloc(sizeof(struct RecordNodeRec));
	new->type = ANONYMOUS;
	new->ptr.pAnony = (void*) typeList;
	new->next = NULL; 

	return new;
}

/*build a simple type list*/
SimpleTypeList newSimpleTypeList(char* name, ExpType type, int isVar) {
	SimpleTypeList new = (SimpleTypeList) malloc(sizeof(struct SimpleTypeListRec));
	new->name = name;
	new->type = type;
	new->isVar = isVar;
	
	return new;
}

SimpleTypeList insertSimpleTypeList(SimpleTypeList simpleList, char* name, ExpType type, int isVar) {
	while(simpleList->next != NULL)
		simpleList = simpleList->next;
	simpleList->next = newSimpleTypeList(name, type, isVar);

	return simpleList->next;
}

/*insert line numbers and memory location into the process hash table*/
int procListInsert(TreeNode* procHead) {

	char* name = procHead->attr.name;
	int nestLevel = currentNestLevel;
	int paraNestLevel = nestLevel + 1;
	int offset = 4;

	SimpleTypeList paraList;
	SimpleTypeList tmpList;
	TreeNode* tmpNode;

	if(procHead->child[0] == NULL)
		paraList = NULL;
	else {
	   	if(procHead->child[0]->kind.decl == DECL_VAR_PARA) {
			paraList = newSimpleTypeList(procHead->child[0]->child[0]->attr.name, procHead->child[0]->child[1]->type, True);
		} else {
			paraList = newSimpleTypeList(procHead->child[0]->child[0]->attr.name, procHead->child[0]->child[1]->type, False);
		}

		varListInsert(procHead->child[0]->child[0]->attr.name, procHead->child[0]->child[1]->type, False, paraNestLevel, NULL, procHead->lineno, 0, offset);
		offset = offset + PARA_OFFSET_INC;

		tmpNode = procHead->child[0]->sibling;
		tmpList = paraList;
		while(tmpNode != NULL) {
			if(tmpNode->kind.decl == DECL_VAR_PARA)
				tmpList = insertSimpleTypeList(tmpList, tmpNode->child[0]->attr.name, tmpNode->child[1]->type, True);
			else
				tmpList = insertSimpleTypeList(tmpList, tmpNode->child[0]->attr.name, tmpNode->child[1]->type, False);

			varListInsert(tmpNode->child[0]->attr.name, tmpNode->child[1]->type, False, paraNestLevel, NULL, tmpNode->lineno, 0, offset);
			offset = offset + PARA_OFFSET_INC;
			
			tmpNode = tmpNode->sibling;
		}
	}
	
	int h = hash(name);
	ProcList l = procHashTable[h];
	ProcList tmp = l;
	while((tmp != NULL) && (strcmp(name, tmp->name)))
		tmp = tmp->next;
	if(tmp == NULL || (strcmp(name, tmp->name)==0 && nestLevel>tmp->nestLevel)) { /*process with same nestlevel not yet in the table, insert to the list head*/
		tmp = (ProcList) malloc(sizeof(struct ProcListRec));
		tmp->name = name;
		tmp->paraList = paraList;
		tmp->nestLevel = nestLevel;
		tmp->next = (l == NULL)? NULL:l;
		procHashTable[h] = tmp;
	}

	return offset;
}

/*insert line numbers and memory location into the function hash table*/
int funcListInsert(TreeNode* funcHead) {

	char* name = funcHead->attr.name;
	int nestLevel = currentNestLevel;
	int paraNestLevel = nestLevel + 1;
	int offset = 4;

	ExpType retType = funcHead->child[1]->type;
	SimpleTypeList paraList;
	SimpleTypeList tmpList;
	TreeNode* tmpNode;


	if(funcHead->child[0] == NULL)
		paraList = NULL;
	else {
	   	if(funcHead->child[0]->kind.decl == DECL_VAR_PARA) {
			//若传递参数为实参
			paraList = newSimpleTypeList(funcHead->child[0]->child[0]->attr.name, funcHead->child[0]->child[1]->type, True);
		} else {
			//若传递参数为形参
			paraList = newSimpleTypeList(funcHead->child[0]->child[0]->attr.name, funcHead->child[0]->child[1]->type, False);
		}
		varListInsert(funcHead->child[0]->child[0]->attr.name,funcHead->child[0]->child[1]->type, False, paraNestLevel, NULL, funcHead->lineno, 0, offset);
		offset = offset + PARA_OFFSET_INC;

		tmpNode = funcHead->child[0]->sibling;
		tmpList = paraList;
		while(tmpNode != NULL) {
			if(tmpNode->kind.decl == DECL_VAR_PARA)
				tmpList = insertSimpleTypeList(tmpList, tmpNode->child[0]->attr.name, tmpNode->child[1]->type, True);
			else
				tmpList = insertSimpleTypeList(tmpList, tmpNode->child[0]->attr.name, tmpNode->child[1]->type, False);

			varListInsert(tmpNode->child[0]->attr.name, tmpNode->child[1]->type, False, paraNestLevel, NULL, tmpNode->lineno, 0, offset);
			offset = offset + PARA_OFFSET_INC;
			
			tmpNode = tmpNode->sibling;
		}
	}

	//符号表插入返回值,与函数同名
	varListInsert(funcHead->attr.name, retType, False, paraNestLevel, NULL, funcHead->lineno, 0, offset);
	offset = offset + PARA_OFFSET_INC;

	int h = hash(name);
	FuncList l = funcHashTable[h];
	FuncList tmp = l;
	while((tmp != NULL) && (strcmp(name, tmp->name)))
		tmp = tmp->next;
	if(tmp == NULL || (strcmp(name, tmp->name)==0 && nestLevel>tmp->nestLevel)) { /*process with same nestlevel not yet in the table, insert to the list head*/
		tmp = (FuncList) malloc(sizeof(struct FuncListRec));
		tmp->name = name;
		tmp->paraList = paraList;
		tmp->retType = retType;
		tmp->nestLevel = nestLevel;
		tmp->next = (l == NULL)? NULL:l;
		funcHashTable[h] = tmp;
	}

	return offset;
}


/*insert line numbers and memory location into the type hash table*/
void typeListAliaseInsert(char* name, char* aliase) {
	int h = hash(name);
	TypeList l = typeHashTable[h];
	while((l != NULL) && (strcmp(name, l->name)))
		l = l->next;
	if(l != NULL) {
		AliaseList t = l->aliaseSet;
		while(t->next != NULL)
			t = t->next;
		t->next = (AliaseList) malloc(sizeof(struct AliaseListRec));
		t->next->aliase = aliase;
		t->next->next = NULL;
	}
}

void typeListInsert(char* name, ExpType type, int nestLevel, void* pAttr, int size) {
	int h = hash(name);
	TypeList l = typeHashTable[h];
	TypeList tmp = l;
	while((tmp != NULL) && (strcmp(name, tmp->name)))
		tmp = tmp->next;
	if(tmp == NULL || (strcmp(name, tmp->name)==0 && nestLevel>tmp->nestLevel)) { /*process with same nestlevel not yet in the table, insert to the list head*/
		tmp = (TypeList) malloc(sizeof(struct TypeListRec));
		tmp->name = name;
		tmp->aliaseSet = NULL;
		tmp->type = type;
		tmp->nestLevel = nestLevel;
		tmp->pAttr = pAttr;
		tmp->size = size;
		tmp->next = (l == NULL)? NULL:l;
		typeHashTable[h] = tmp;
	}

}

/*insert line numbers and memory location into the variable hash table*/
void varListInsert(char* name, ExpType type, int isConst, int nestLevel, void* pAttr, int lineno, int baseLoc, int offset) { 
	int h = hash(name);
	VariableList l = variableHashTable[h];
	VariableList tmp = l;
	while((tmp != NULL) && (strcmp(name, tmp->name)))
			tmp = tmp->next;
	if(tmp == NULL || (strcmp(name, tmp->name)==0 && nestLevel>tmp->nestLevel)) { /*process with same nestlevel not yet in the table, insert to the list head*/
		tmp = (VariableList) malloc(sizeof(struct VariableListRec));
		tmp->name = name;
		tmp->type = type;
		tmp->isConst = isConst; 
		tmp->nestLevel = nestLevel;
		tmp->pAttr = pAttr;
		tmp->lines = (LineList) malloc(sizeof(struct LineListRec));
		tmp->lines->lineno = lineno;
		tmp->lines->next = NULL;
		tmp->memloc.baseLoc = baseLoc;
		tmp->memloc.offset = offset;
		tmp->next = (l == NULL)? NULL:l;
		variableHashTable[h] = tmp;
	} else { /*find the exact variable*/
		LineList t = tmp->lines;
		while(t->next != NULL)
			t = t->next;
		t->next = (LineList) malloc(sizeof(struct LineListRec));
		t->next->lineno = lineno;
		t->next->next = NULL;
	}
}



/*=======================定义对符号表的查找操作=================================*/

/*varListLookup returns the VariableList or null if not found*/
VariableList varListLookup(char* name) {
	int h = hash(name);
	VariableList l = variableHashTable[h];
	while((l != NULL) && (strcmp(name, l->name)))
		l = l->next;
	if(l == NULL)
		return NULL;
	else {
		l->memloc.baseLoc = currentNestLevel - l->nestLevel; 
		return l;
	}
}

/*funcListLookup returns the FuncList of that name or null if not found*/
FuncList funcListLookup(char* name) {
	int h = hash(name);
	FuncList l = funcHashTable[h];
	while((l != NULL) && (strcmp(name, l->name)))
		l = l->next;
	if(l == NULL)
		return NULL;
	else  
		return l;
}

/*procListLookup returns the ProcList of that name or null if not found*/
ProcList procListLookup(char* name) {
	int h = hash(name);
	ProcList l = procHashTable[h];
	while((l != NULL) && (strcmp(name, l->name)))
		l = l->next;
	if(l == NULL)
		return NULL;
	else
		return l;
}

/*typeListLookup returns the TypeList of that name or null if not found*/
TypeList typeListLookup(char* name) {
	int h = hash(name);
	TypeList l = typeHashTable[h];
	while((l != NULL) && (strcmp(name, l->name)))
		l = l->next;
	if(l == NULL)
		return NULL;
	else
		return l;
}

/*array Lookup*/
LookupRet arrayLookup(char* a, int i) {
	int lower, upper, size;
	LookupRet ret;
	ret.totalOff = ERROR_RETURN;
	ret.jumpLevel = ERROR_RETURN;
	ret.type = EXPTYPE_VOID;
	VariableList l = varListLookup(a);
	if(l->type == EXPTYPE_ARRAY && l->pAttr != NULL) {
		lower = ((ArrayDef)(l->pAttr))->subBound->LowerBound.i;
		upper = ((ArrayDef)(l->pAttr))->subBound->UpperBound.i;
		if(i>=lower && i<=upper) {
			ret.totalOff = l->memloc.offset+OFFSET_INC*(i-lower);
			ret.jumpLevel = currentNestLevel - l->nestLevel;
			ret.type = l->type;
		} 
	}
	return ret;
}

/*recordLookup*/
LookupRet recordLookup(char* rec, char* a) {
	VariableList l = varListLookup(rec);
	TypeList plist;
	int size = 0;
	LookupRet ret;
	ret.totalOff = ERROR_RETURN;
	ret.jumpLevel = ERROR_RETURN;
	ret.type = EXPTYPE_VOID;
	if(l->type == EXPTYPE_RECORD && l->pAttr != NULL) {
		if(((RecordDef)(l->pAttr))->type == ANONYMOUS) {
			plist = ((RecordDef)(l->pAttr))->ptr.pAnony; 
		} else {
			plist = ((RecordDef)(l->pAttr))->ptr.pDef; 
		}
		while(plist != NULL && strcmp(plist->name, a)) {
			size += 1;
			plist = plist->next;
		}
		if(plist != NULL) {
			ret.totalOff = size*OFFSET_INC + l->memloc.offset;
			ret.jumpLevel = currentNestLevel - l->nestLevel;
			ret.type = plist->type;
		}
	}
	return ret;	
}


/*===================定义进出函数或过程时对符号表的更新==========================*/

/*initialize*/
void initScope() {
	currentNestLevel = -1;
	int i;
	for(i=0; i<SIZE; i++) {
		variableHashTable[i] = NULL;
		typeHashTable[i] = NULL;
		procHashTable[i] = NULL;
		funcHashTable[i] = NULL;
	}
}

/*enter new function or process scope*/
int enterNewScope(TreeNode* t) {
	currentNestLevel += 1;
	totalOffset[currentNestLevel] = buildSymtab(t);
	return 	totalOffset[currentNestLevel];
}

/*quit function or process scope*/
int leaveScope() {
	int tmp = currentNestLevel;
	int retValue = totalOffset[currentNestLevel];
	void* ptr1, *ptr2, *ptr3;
	currentNestLevel -= 1;
	int i;
	for(i=0; i<SIZE; i++) {
		VariableList vl = variableHashTable[i];
		TypeList tl = typeHashTable[i];
		FuncList fl = funcHashTable[i];
		ProcList pl = procHashTable[i];

		while(vl != NULL && vl->nestLevel >= tmp) {
			ptr1 = (void*)vl;
			vl = vl->next;
			ptr2 = (void*)(((VariableList)ptr1)->lines);
			while((LineList)ptr2 != NULL) {
				ptr3 = (void*)(((LineList)ptr2)->next);
				free((LineList)ptr2);
				ptr2 = ptr3;
			}
			free((VariableList)ptr1);
		}
		variableHashTable[i] = vl;

		while(tl != NULL && tl->nestLevel >= tmp) {
			ptr1 = (void*)tl;
			tl = tl->next;
			ptr2 = (void*)(((TypeList)ptr1)->aliaseSet);
			while((AliaseList)ptr2 != NULL) {
				ptr3 = (void*)(((AliaseList)ptr2)->next);
				free((AliaseList)ptr2);
				ptr2 = ptr3;
			}
			free((VariableList)ptr1);
		}
		typeHashTable[i] = tl;
	}
	return retValue;
}


/*=======================定义对符号表的打印操作=================================*/

/*print symbol table*/
void printSymTab(FILE* listing) {
	int i;
	fprintf(listing, "Variable Name		NestLevel 	Location 	Line Number\n");
	fprintf(listing, "------------- 	---------	-------- 	-----------\n");
	for(i=0; i<SIZE; i++) {
		if(variableHashTable[i] != NULL) {
			VariableList l = variableHashTable[i];
			while(l != NULL) {
				LineList t = l->lines;
				fprintf(listing, "%-14s ", l->name);
				fprintf(listing, "%-8d", l->nestLevel);
				fprintf(listing, "%-8d ", l->memloc.offset);
				while(t != NULL) {
					fprintf(listing, "%4d ", t->lineno);
					t = t->next;
				}
				fprintf(listing, "\n");
				l = l->next;
			}
		}
	}

	fprintf(listing, "Function Name		NestLevel 	Return Type 	Parameter\n");
	fprintf(listing, "------------- 	---------	-------- 	-----------\n");
	for(i=0; i<SIZE; i++) {
		if(funcHashTable[i] != NULL) {
			FuncList l = funcHashTable[i];
			while(l != NULL) {
				SimpleTypeList t = l->paraList;
				fprintf(listing, "%-14s ", l->name);
				fprintf(listing, "%-8d", l->nestLevel);
				fprintf(listing, "%-8d ", l->retType);
				while(t != NULL) {
					fprintf(listing, "%-14s ", t->name);
					t = t->next;
				}
				fprintf(listing, "\n");
				l = l->next;
			}
		}
	}	
	
}
