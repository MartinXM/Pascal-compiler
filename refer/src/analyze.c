#include "global.h"
#include "symtab.h"
#include "analyze.h"

extern int currentNestLevel;

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
		//		enterNewScope(t->child[1]);	
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

static void typeError(TreeNode* t, char* message) {
	fprintf(listing, "Type error at line %d: %s\n", t->lineno, message);
	Error = True;
}


