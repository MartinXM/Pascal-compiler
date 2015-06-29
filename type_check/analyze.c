#include "global.h"
#include "symtab.h"
#include "analyze.h"

extern int currentNestLevel;
int inTypeCheck = 0;

/*inserts identifiers stores in t into the symble table*/
void insertNode(TreeNode* t) {
	if(t == NULL) 
		return;
	if(t->nodekind == NODE_DECLARE) {
		switch(t->kind.decl) {
			case DECL_CONST:
			{
				while(t != NULL) {
					offset -= OFFSET_INC;
					varListInsert(t->attr.name, t->type, True, currentNestLevel, NULL, t->lineno, 0, offset);
					t = t->sibling;
				}	
				break;
			}
			case DECL_VAR:
			{
				while(t != NULL) {
					TreeNode* pname = t->child[0];
					TreeNode* ptype = t->child[1];
					while(pname != NULL) {
						switch(ptype->kind.type) {
								case TYPE_SIMPLE_ID:
								{
									TypeList l = typeListLookup(ptype->attr.name);
									switch(l->type) {
										case EXPTYPE_ARRAY: 
										{
											offset -= l->size;
											varListInsert(pname->attr.name, l->type, False, currentNestLevel, l->pAttr, pname->lineno, 0, offset);
											break;
										}
										case EXPTYPE_RECORD:
										{
											offset -= l->size;
											varListInsert(pname->attr.name, l->type, False, currentNestLevel, l->pAttr, pname->lineno, 0, offset);
											break;
										}
										default:
										{	
											offset -= OFFSET_INC;	
											varListInsert(pname->attr.name, l->type, False, currentNestLevel, l->pAttr, pname->lineno, 0, offset);
											break;
										}
									}
									break;
								}
								case TYPE_SIMPLE_ENUM: 
								{
									//new一个枚举结构，然后将这个结构的指针存入符号表中
									EnumDef eptr = newEnumDef(ptype->attr.name);
									EnumDef etmp = eptr;
									while(ptype->sibling != NULL) {
										ptype = ptype->sibling;
										etmp = insertEnumDef(etmp, ptype->attr.name);
									}

									offset -= OFFSET_INC;	
									varListInsert(pname->attr.name, EXPTYPE_SIMPLE_ENUM, False,  currentNestLevel, (void*)eptr, pname->lineno, 0, offset);

									break;
								}
								case TYPE_SIMPLE_LIMIT: 
								{
									SubBoundDef sub =  newSubBoundDef(ptype->child[0]->type, &(ptype->child[0]->attr.val), &(ptype->child[1]->attr.val));
									offset -= OFFSET_INC;
									varListInsert(pname->attr.name, EXPTYPE_SIMPLE_LIMIT, False, currentNestLevel, (void*)sub, pname->lineno, 0, offset);
									break;
								}
								case TYPE_SIMPLE_SYS: 
								{
									//简单类型中的系统类型
									offset -= OFFSET_INC;
									varListInsert(pname->attr.name, ptype->type, False,  currentNestLevel, NULL, pname->lineno, 0, offset);
									break;
								}
								case TYPE_ARRAY: 
								{
									int arraySize = 0;
									if(ptype->child[0]->type == EXPTYPE_SIMPLE_LIMIT) {
										arraySize = ptype->child[0]->child[1]->attr.val - ptype->child[0]->child[0]->attr.val + 1; 
										ArrayDef pAttr = newArrayDef(ptype->child[1]->type, ptype->child[0]->child[0]->type, &(ptype->child[0]->child[0]->attr.val), &(ptype->child[0]->child[1]->attr.val));
										offset -= (OFFSET_INC * arraySize);
										varListInsert(pname->attr.name, ptype->type, False, currentNestLevel, (void*)pAttr, pname->lineno, 0, offset);
									}
					    			break;
								}
								case TYPE_RECORD:
								{
									TreeNode* nameNode = ptype->child[0];
									TreeNode* typeNode = ptype->child[1];

									TypeList l = newTypeDef(nameNode->attr.name, typeNode->type, currentNestLevel, NULL, OFFSET_INC);
									offset -= OFFSET_INC;
									TypeList tmp = l;
									while(nameNode->sibling != NULL) {
										nameNode = nameNode->sibling;
										tmp = insertTypeDef(tmp, nameNode->attr.name, typeNode->type, currentNestLevel, NULL, OFFSET_INC);
										offset -= OFFSET_INC;	
									}
								
									while(ptype->sibling != NULL) {
										ptype = ptype->sibling;
										nameNode = ptype->child[0];
										typeNode = ptype->child[1];
										while(nameNode != NULL) {
											tmp = insertTypeDef(tmp, nameNode->attr.name, typeNode->type, currentNestLevel, NULL, OFFSET_INC);	
											offset -= OFFSET_INC;
											nameNode = nameNode->sibling;
										}
									}
									RecordDef r = newAnonyRecord(l); 
									varListInsert(pname->attr.name, EXPTYPE_RECORD, False, currentNestLevel, (void*)r, t->lineno, 0, offset);	

									break;
								}
								default:
									break;						
						}
						pname = pname->sibling;
					}
					t = t->sibling;
				}
				break;
			}
			case DECL_TYPE:
			{
				while(t) {
					switch(t->child[1]->kind.type) {
						case TYPE_SIMPLE_ID: 
						{
							typeListAliaseInsert(t->child[0]->attr.name, t->child[1]->attr.name);	
							break;
						}
						case TYPE_SIMPLE_ENUM: 
						{
							TreeNode* eList = t->child[1]->child[0];
							EnumDef eptr = newEnumDef(eList->attr.name);
							EnumDef etmp = eptr;
							while(eList->sibling != NULL) {
								eList = eList->sibling;
								etmp = insertEnumDef(etmp, eList->attr.name);
							}
							typeListInsert(t->child[0]->attr.name, EXPTYPE_SIMPLE_ENUM, currentNestLevel, (void*)eptr, OFFSET_INC);							
							break;
						}
						case TYPE_SIMPLE_LIMIT: 
						{
							SubBoundDef sub =  newSubBoundDef(t->child[1]->child[0]->type, &(t->child[1]->child[0]->attr.val), &(t->child[1]->child[1]->attr.val));
							typeListInsert(t->child[0]->attr.name, EXPTYPE_SIMPLE_LIMIT, currentNestLevel, (void*)sub, OFFSET_INC);
							break;
						}
						case TYPE_SIMPLE_SYS: 
						{
							typeListInsert(t->child[0]->attr.name, t->child[1]->type, currentNestLevel, NULL, OFFSET_INC);
					    	break;
						}
						case TYPE_ARRAY: 
						{
							int size = 0;
							TreeNode* atype = t->child[1];
							if(atype->child[0]->type == EXPTYPE_SIMPLE_LIMIT) {
							ArrayDef pAttr = newArrayDef(atype->child[1]->type, atype->child[0]->child[0]->type, &(atype->child[0]->child[0]->attr.val), &(atype->child[0]->child[1]->attr.val));
							size = (atype->child[0]->child[1]->attr.val-atype->child[0]->child[0]->attr.val+1)*OFFSET_INC;
							typeListInsert(t->child[0]->attr.name, t->child[1]->type, currentNestLevel, (void*)pAttr, size);
							}							
					    	break;
						}
						case TYPE_RECORD: 
						{
							TreeNode* pname = t->child[0];
							TreeNode* ptype = t->child[1];
							TreeNode* nameNode = ptype->child[0];
							TreeNode* typeNode = ptype->child[1];
							int size = 0;

							TypeList l = newTypeDef(nameNode->attr.name, typeNode->type, currentNestLevel, NULL, OFFSET_INC);
							size ++;
							TypeList tmp = l;
							while(nameNode->sibling != NULL) {
								nameNode = nameNode->sibling;
								tmp = insertTypeDef(tmp, nameNode->attr.name, typeNode->type, currentNestLevel, NULL, OFFSET_INC);
								size ++;	
							}
							
							while(ptype->sibling != NULL) {
								ptype = ptype->sibling;
								nameNode = ptype->child[0];
								typeNode = ptype->child[1];
								while(nameNode != NULL) {
									tmp = insertTypeDef(tmp, nameNode->attr.name, typeNode->type, currentNestLevel, NULL, OFFSET_INC);	
									size ++;
									nameNode = nameNode->sibling;
								}
							}
							RecordDef r = newDefinedRecord(l); 
							printf("size %d",size*OFFSET_INC);
							typeListInsert(pname->attr.name, EXPTYPE_RECORD,  currentNestLevel, (void*)r, size*OFFSET_INC);	

							break;
						}
						default:
							break;						
					}
					t = t->sibling;
				}
				break;
			}
			case DECL_FUNCTION:
			{
				funcListInsert(t->child[0]);
				// enterNewScope(t->child[1]);	
				break;
			} 
			case DECL_PROCEDURE:
			{
				//代码生成时，不要这个，但类型检查时需要，不要会出现变量无法找到的错误
				//如果两个阶段都加上的话，运行程序时要加上sudo，否则有segment fault
				if(inTypeCheck)
					procListInsert(t->child[0]);
				else
					break;
			}
			default:
				break;
		}
		
	}

}

/*procedure traverse is a generic recursive syntax tree traversal routine*/
void traverse(TreeNode* t) {
	if(t != NULL) {
		int i;
		for(i=0; i<4; i++)
			insertNode(t->child[i]);
	}
}


/*constructs the symbol table by preorder traversal of the syntax tree*/
int buildSymtab(TreeNode* syntaxTree) {
	offset = -4;
	traverse(syntaxTree);
	if(TraceAnalyze) {
		fprintf(listing, "\nSymbol table:\n\n");
		printSymTab(listing);
	}
	return -offset;
}

/* ====================== type check =====================*/

//因为没有复杂类型的嵌套，所以，只有表达式为id时，才可能出现ExpNode的类型为复杂类型的情况
//因为我们存储的是类型的指针，所以可以直接比较两个指针是否指向同一个位置
int compoundTypeEqual(TreeNode* t1,TreeNode* t2){
	VariableList l1,l2;
	l1 = varListLookup(t1->attr.name);
	l2 = varListLookup(t2->attr.name);
	if(l1 == NULL){
		printf("Line %d, Error: %s is not declare.\n",t1->lineno,(t1->child[0])->attr.name);
 		fflush(stdout);
 		typeError();
 		// error = True;
	}
	if(l2 == NULL){
		printf("Line %d, Error: %s is not declare.\n",t2->lineno,(t2->child[0])->attr.name);
 		fflush(stdout);
 		typeError();
 		// error = True;
	}
	return (l1->pAttr == l2->pAttr);
}

int nodeTypeEqual(TreeNode* t1,TreeNode* t2){

	if(t1->RunningType == t2->RunningType){
		if(t1->RunningType == EXPTYPE_VOID ||
		   t1->RunningType == EXPTYPE_INT || 
		   t1->RunningType == EXPTYPE_REAL || 
		   t1->RunningType == EXPTYPE_CHAR || 
		   t1->RunningType == EXPTYPE_STRING ||
		   t1->RunningType == EXPTYPE_BOOL)
		   return True;
		else	
			return compoundTypeEqual(t1,t2);
	}
	else
		return False;
}

void checkStmtAssign(TreeNode* pnode){
	//儿子0是一个表达式，所以先检查该表达式是否有语法错误
	//然后获得表达式的类型
	checkNode(pnode->child[1]);
	//儿子1是一个变量，我们通过名字寻找变量，这个变量只会有一个节点
	VariableList ssvar=varListLookup((pnode->child[0])->attr.name);
	if(ssvar == NULL){
		printf("Line %d, Error: The variable %s is not declare.\n",pnode->lineno,(pnode->child[0])->attr.name);
 		fflush(stdout);
 		typeError();
	}
	//首先，这个节点不能够为常量
	if (ssvar->isConst){	
		printf("Line %d, Error: The const variable %s can not be assigned.\n",pnode->lineno,(pnode->child[0])->attr.name);
 		fflush(stdout);
 		typeError();
 		// error = True;
	}
	//左边是一个ExpNode,结构只有三种情况：id,id[]和id.id
	//然后，我们检查这个ExpNode是否有语法错误，并且获取这个ExpNode的类型
	checkNode(pnode->child[0]);

	//如果赋值符号左右两边的类型不相同，则报错
	if(!nodeTypeEqual(pnode->child[0],pnode->child[1])){
		printf("Line %d, Error: The type of %s is different from the right hand.\n",pnode->lineno,(pnode->child[0]->attr.name));
		fflush(stdout);
		typeError();
 		// error = True;
	}
}

void checkExpId(TreeNode* pnode){
	VariableList ssvar=varListLookup(pnode->attr.name);
	ExpType checkType;
	LookupRet ret;

	if (ssvar==NULL){
 		printf("Line %d, Error: The variable %s is not declared.\n",pnode->lineno,pnode->attr.name);
 		fflush(stdout);
 		typeError();
 		// error = True;
 	}
 	checkType = ssvar->type;
 	switch(checkType){
 		case EXPTYPE_ARRAY:
 			//不检查index是否合理
 			if(pnode->child[0]==NULL){
 				//只是数组
 				pnode->RunningType = checkType;
 			}
 			else{
 				//是数组元素
 				pnode->RunningType = ((ArrayDef)(ssvar->pAttr))->arrayType;
 			}
 			break;
 		case EXPTYPE_RECORD:
 			if(pnode->child[0]==NULL){
 				//只是record
 				pnode->RunningType = checkType;
 			}
 			else{
 				ret = recordLookup(pnode->attr.name,(pnode->child[0])->attr.name);
 				if(ret.type == EXPTYPE_VOID){
 					printf("Line %d, Error: The variable %s.%s is not declare.\n",pnode->lineno,pnode->attr.name,(pnode->child[0])->attr.name);
 					fflush(stdout);
 					typeError();
 					// error = True;
 				}
 				pnode->RunningType = ret.type;
 			}
 			break;
 		case EXPTYPE_BOOL:
 			pnode->RunningType = EXPTYPE_INT;
 		default:
 			pnode->RunningType = checkType;
 			break;
 	}
}

void checkExpOp(TreeNode* pnode){
	if (pnode->child[0]!=NULL && pnode->child[1]!=NULL){
		//检查语法错误，并为RunningType赋值
		checkNode(pnode->child[0]);
		checkNode(pnode->child[1]);


		if ((pnode->child[0])->RunningType != (pnode->child[1])->RunningType){
 			printf("Line %d, Error: Different type can not be calculated.\n",pnode->lineno);
 			fflush(stdout);	
 			typeError();
 			// error = True;			
 		}
 		
		if ((pnode->child[0])->RunningType==EXPTYPE_REAL && (pnode->child[1])->RunningType==EXPTYPE_REAL)
			pnode->RunningType=EXPTYPE_REAL;
		else if((pnode->child[0])->RunningType==EXPTYPE_INT && (pnode->child[1])->RunningType==EXPTYPE_INT)
			pnode->RunningType=EXPTYPE_INT; 
		else if((pnode->child[0])->RunningType==EXPTYPE_CHAR && (pnode->child[1])->RunningType==EXPTYPE_CHAR){
			printf("Line %d, Error: Char can not be calculated.\n",pnode->lineno);
 			fflush(stdout);
 			typeError();
 			// error = True;
		}
		else{
			printf("Line %d, Error: compound type can not be calculated.\n",pnode->lineno);
 			fflush(stdout);
 			typeError();
 			// error = True;
		}

	}
	else {
		checkNode(pnode->child[0]);
		pnode->RunningType=(pnode->child[0])->RunningType;
	}
}

void checkExpConst(TreeNode* pnode){
	if(pnode->type == EXPTYPE_BOOL)
		pnode->RunningType = EXPTYPE_INT;
	else
		pnode->RunningType = pnode->type;
}

void checkExpCase(TreeNode* pnode){
	checkNode(pnode->child[0]);	
	checkNode(pnode->child[1]);

	pnode->RunningType = pnode->child[0]->RunningType;
}

void checkExpFunc(TreeNode* pnode){
	FuncList judge_var=funcListLookup(pnode->attr.name);
 	if(judge_var == NULL){
 		printf("Line %d, Error: Function %s dose not declare.\n",pnode->lineno,pnode->attr.name);
 		fflush(stdout);
 		typeError();
 		// error = True;
 	}
	TreeNode* tmpChild = pnode->child[0];
	SimpleTypeList tmpPara = judge_var->paraList;

	if(tmpChild != NULL)		//可能不存在参数
		checkNode(tmpChild);
	while(tmpChild!=NULL && tmpPara!=NULL){
		//只是简单地判断类型名是否相同
		if(tmpChild->RunningType != tmpPara->type){
			printf("Line %d, Error: parameter type does not match.\n",pnode->lineno);
 			fflush(stdout);
 			typeError();
 			// error = True;
		}
		tmpChild = tmpChild->sibling;
		tmpPara = tmpPara->next;
	}
	if(tmpChild != NULL || tmpPara!=NULL){
		printf("Line %d, Error: number of parameters does not match.\n",pnode->lineno);
 		fflush(stdout);
 		typeError();
 		// error = True;
	}

	//假设返回值正确
	pnode->RunningType = judge_var->retType;

	// 暂时无法比较返回值
	// checkNode(pnode->child[1]);
	// if(pnode->child[1]->RunningType != judge_var->retType){
	// 	printf("Line %d, Error: return data type does not match.\n",pnode->lineno);
 // 		fflush(stdout);
 // 		error = True;
	// }

}


void checkNodeExpression(TreeNode* pnode){
	switch (pnode->kind.exp){
				case EXP_ID:
				//这里面包括id,id.id,id[id]的情况
					checkExpId(pnode);
					break;
				case EXP_OP:
				//printf("EXP_OP\n");
				//fflush(stdout);
					checkExpOp(pnode);
					break;
				case EXP_CONST:
					checkExpConst(pnode);
					break;
				case EXP_CASE:
					checkExpCase(pnode);
					break;
				case EXP_FUNC_ID:
					checkExpFunc(pnode);
					break;
			}
}

void checkStmtIf(TreeNode* pnode){
	ExpType checkType;
	//语法分析会保证if至少有两段

	//panduan
	checkNode(pnode->child[0]);
	checkType = pnode->child[0]->RunningType;
	if(checkType != EXPTYPE_INT &&
	   checkType != EXPTYPE_REAL){
		printf("Line %d, Error: If condition is not illegal.\n",pnode->lineno);
 		fflush(stdout);
 		typeError();
 		// error = True;
	}

	//if-part
	checkNode(pnode->child[1]);

	if (pnode->child[2]!=NULL)
		checkNode(pnode->child[2]);
}

void checkStmtFor(TreeNode* pnode){

	//初始值
	//语法分析保证for至少四段
	checkNode(pnode->child[0]);
	checkNode(pnode->child[1]);
	checkNode(pnode->child[2]);
	checkNode(pnode->child[3]);

	if(pnode->child[0]->RunningType != EXPTYPE_INT ||
	   pnode->child[1]->RunningType != EXPTYPE_INT ||
	   pnode->child[2]->RunningType != EXPTYPE_INT){
		printf("Line %d, Error: Index of for statement must be int.\n",pnode->lineno);
 		fflush(stdout);
 		typeError();
 		// error = True;
	}
}

void checkStmtWhile(TreeNode* pnode){
	ExpType checkType;

	//语法分析保证while至少两个儿子
	checkNode(pnode->child[0]);
	checkNode(pnode->child[1]);

	checkType = pnode->child[0]->RunningType;
	if(checkType != EXPTYPE_INT &&
	   checkType != EXPTYPE_REAL){
		printf("Line %d, Error: While condition is not illegal.\n",pnode->lineno);
 		fflush(stdout);
 		typeError();
 		// error = True;
	}
}

void checkStmtRepeat(TreeNode* pnode){
	ExpType checkType;

	checkNode(pnode->child[0]);
	checkNode(pnode->child[1]);	

	checkType = pnode->child[1]->RunningType;
	if(checkType != EXPTYPE_INT &&
	   checkType != EXPTYPE_REAL){
		printf("Line %d, Error: Util condition is not illegal.\n",pnode->lineno);
 		fflush(stdout);
 		typeError();
 		// error = True;
	}
}

void checkStmtCase(TreeNode* pnode){
	ExpType checkType;
	TreeNode* tmp;

	//语法分析保证2个儿子
	checkNode(pnode->child[0]);
	checkNode(pnode->child[1]);

	checkType = pnode->child[0]->RunningType;
	tmp = pnode->child[1];
	while(tmp!=NULL){
		if(checkType != tmp->RunningType){
			printf("Line %d, Error: data type dose not match case type.\n",pnode->lineno);
 			fflush(stdout);
 			typeError();
 			// error = True;
		}
		tmp = tmp->sibling;
	}	
}

void checkStmtGoto(TreeNode* pnode){
    int i=0;
}

void checkStmtLabel(TreeNode* pnode){
	checkNode(pnode->child[0]);
}

void checkStmtProc(TreeNode* pnode){
	if(pnode->child[0] != NULL)
		checkNode(pnode->child[0]);
}

static void checkNode(TreeNode* pnode){
	int size_param;
	switch(pnode->nodekind){
		case NODE_STATEMENT:
			switch (pnode->kind.stmt){
				case STMT_ASSIGN:
					//printf("STMT_ASSIGN\n");
					//fflush(stdout);
					checkStmtAssign(pnode);
					break;
				case STMT_IF:
					checkStmtIf(pnode);
					break;
				case STMT_FOR:
					checkStmtFor(pnode);
					break;
				case STMT_WHILE:
					checkStmtWhile(pnode);
					break;
				case STMT_REPEAT:
					checkStmtRepeat(pnode);
					break;	
				case STMT_CASE:
					checkStmtCase(pnode);
					break;	
				case STMT_GOTO:
					checkStmtGoto(pnode);
					break;	
				case STMT_LABEL:
					checkStmtLabel(pnode);
					break;	
				case STMT_PROC_ID:
					checkStmtProc(pnode);
					break;
				default: 
					break;				
			}
			break;
		case NODE_EXPRESSION:
			checkNodeExpression(pnode);
			break;
		case NODE_DECLARE:
			switch (pnode->kind.decl){
				case DECL_ROUTINEHEAD:
					size_param=enterNewScope(pnode);
					if (pnode->child[3]!=NULL)
  						checkNode(pnode->child[3]);
  					break;
				case DECL_FUNCTION:					
					//funcListInsert(pnode->child[0]);  // give congjie function_head!!!!!!!!!!!!!

					(pnode->child[1])->attr.name=(pnode->child[0])->attr.name;
					checkNode(pnode->child[1]); //routine_head 
	
					
					size_param=leaveScope();
					break;
    			case DECL_PROCEDURE:
					fflush(stdout);
					
					//procListInsert(pnode->child[0]);  // give congjie function_head!!!!!!!!!!!!!

					(pnode->child[1])->attr.name=(pnode->child[0])->attr.name;
					checkNode(pnode->child[1]); //routine_head 
				
					size_param=leaveScope();
					break;
			}
			break;
		case NODE_TYPE:
			break;
		default: printf("pnode->nodekind Default");
	}
	if(pnode->sibling!=NULL){
		checkNode(pnode->sibling);	
	}
}

//一边检查语法错误，一边给每个节点的type赋值
void typeCheck(TreeNode* syntaxTree){
	inTypeCheck = 1;
	initScope();
	checkNode(syntaxTree);
	leaveScope();
	inTypeCheck = 0;
}

void typeError(){
	printf("Exit with error\n");
	exit(1);
}

