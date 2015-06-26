#include "CG.h"
#include "util.h"
#include "analyze.h"
#include "symtab.h"
#define CG_OUTPUT(asm) fprintf(codename, "%s", asm);
#define CG_OUTPUT_DATA(asm) fprintf(dataname, "%s", asm);

FILE* codename = NULL;
FILE* dataname = NULL;
FILE* ff=NULL;

char tmp[200];


void CGExpOp(TreeNode* pnode){

	if (pnode->child[0]!=NULL && pnode->child[1]!=NULL){
		GenerateCode(pnode->child[0]);
		CG_OUTPUT("push eax\n");
	
		GenerateCode(pnode->child[1]);
		CG_OUTPUT("push eax\n");


		if ((pnode->child[0])->RuningType != (pnode->child[1])->RuningType){
 			printf("Line %d, Error: Different data type can not be calculated.\n",pnode->lineno);
 			fflush(stdout);				
 		}
 		
		if ((pnode->child[0])->RuningType==EXPTYPE_REAL && (pnode->child[1])->RuningType==EXPTYPE_REAL)
			pnode->RuningType=EXPTYPE_REAL;
		if ((pnode->child[0])->RuningType==EXPTYPE_INT && (pnode->child[1])->RuningType==EXPTYPE_INT)
			pnode->RuningType=EXPTYPE_INT; 

	}
	else {
		CG_OUTPUT("push 0\n");
		GenerateCode(pnode->child[0]);
		CG_OUTPUT("push eax\n");
		pnode->RuningType=(pnode->child[0])->RuningType;
	}



	if (pnode->RuningType==EXPTYPE_INT){
		CG_OUTPUT("pop ebx\n");
		CG_OUTPUT("pop eax\n");
		switch(pnode->attr.op)
		{
		case TOKEN_PLUS:
			CG_OUTPUT("add eax, ebx\n");
			break;
		case TOKEN_MINUS:
			CG_OUTPUT("sub eax, ebx\n");
			break;
		case TOKEN_MUL:
			CG_OUTPUT("xor edx, edx\nimul ebx\n");
			break;
		case TOKEN_DIV:
			CG_OUTPUT("xor edx, edx\nidiv ebx\n");
			break;
		case TOKEN_MOD:
			CG_OUTPUT("xor edx, edx\nidiv ebx\n");
			CG_OUTPUT("mov eax,edx\n");
			break;
		case TOKEN_LT:
			CG_OUTPUT("cmp eax, ebx\n");
			CG_OUTPUT("mov eax, 0\n");
			CG_OUTPUT("setl al\n");
			break;
		case TOKEN_LE:
			CG_OUTPUT("cmp eax, ebx\n");
			CG_OUTPUT("mov eax, 0\n");
			CG_OUTPUT("setng al\n");
			break;
		case TOKEN_GT:
			CG_OUTPUT("cmp eax, ebx\n");
			CG_OUTPUT("mov eax, 0\n");
			CG_OUTPUT("setg al\n");
			break;
		case TOKEN_GE:
			CG_OUTPUT("cmp eax, ebx\n");
			CG_OUTPUT("mov eax, 0\n");
			CG_OUTPUT("setnl eax\n");
			break;
		case TOKEN_EQUAL:
			CG_OUTPUT("cmp eax, ebx\n");
			CG_OUTPUT("mov eax, 0\n");
			CG_OUTPUT("sete al\n");
			break;
		case TOKEN_UNEQUAL:
			CG_OUTPUT("cmp eax, ebx\n");
			CG_OUTPUT("mov eax, 0\n");
			CG_OUTPUT("setne al\n");
			break;
		}	
	}
	else {
		CG_OUTPUT("fld dword ptr [esp+4]\n");
		CG_OUTPUT("fld dword ptr [esp]\n");
		CG_OUTPUT("pop eax\n");
		CG_OUTPUT("pop eax\n");
		switch(pnode->attr.op){
		case TOKEN_PLUS:
			CG_OUTPUT("fadd\n");
			break;
		case TOKEN_MINUS:
			CG_OUTPUT("fsub\n");
			break;
		case TOKEN_MUL:
			CG_OUTPUT("fmul\n");
			break;
		case TOKEN_DIV:
			CG_OUTPUT("fdiv\n");
			break;
		case TOKEN_LT:
			CG_OUTPUT("fcom\n");
			break;
		case TOKEN_LE:
			CG_OUTPUT("fcom\n");
			break;
		case TOKEN_GT:
			CG_OUTPUT("fcom\n");
			break;
		case TOKEN_GE:
			CG_OUTPUT("fcom\n");
			break;
		case TOKEN_EQUAL:
			CG_OUTPUT("fcom\n");
			break;
		}
		CG_OUTPUT("sub esp,4\n");
		CG_OUTPUT("fstp dword ptr [esp]\n");
		CG_OUTPUT("pop eax\n");
		
	}
}



void CGExpId(TreeNode* pnode){
	
	int level;
	int offset;
	int lower;
	ExpType cgtype;
	LookupRet st_var;


	VariableList ssvar=varListLookup(pnode->attr.name);
 	if (ssvar==NULL){
 		printf("Line %d, Error: The variable %s is not existed.\n",pnode->lineno,pnode->attr.name);
 		fflush(stdout);
 	}

 	cgtype=ssvar->type;

	if (cgtype==EXPTYPE_ARRAY){  // array of int real bool char

		//st_var=arrayLookup(pnode->attr.name,(pnode->child[0])->attr.val);
		if (ssvar->pAttr==NULL){
 			printf("Line %d, Error: The variable %s is not an array.\n",pnode->lineno,pnode->attr.name);
 			fflush(stdout);
 		}

		lower=(((ArrayDef)ssvar->pAttr)->subBound)->LowerBound.i;
		level=ssvar->memloc.baseLoc;
		offset=ssvar->memloc.offset;
		//offset=st_var.totalOff;

	}
	else if (cgtype==EXPTYPE_RECORD){ // record {int real bool char}
		st_var=recordLookup(pnode->attr.name,(pnode->child[0])->attr.name);
		level=st_var.jumpLevel;
		offset=st_var.totalOff;
	}
	else {	 //int real bool char
		level=ssvar->memloc.baseLoc;
		offset=ssvar->memloc.offset;
	}

	if (cgtype==EXPTYPE_ARRAY){
		CGNodeExpression(pnode->child[0]);
		sprintf(tmp,"mov ebx, %d\n",lower);                 
		CG_OUTPUT(tmp);	

		CG_OUTPUT("sub eax, ebx\n");
		CG_OUTPUT("mov ebx, 4\n");
		CG_OUTPUT("imul eax,ebx\n");
		sprintf(tmp,"mov edi, %d\n",offset);
		CG_OUTPUT(tmp);
		CG_OUTPUT("add edi, eax\n");
	}
	else {
		sprintf(tmp, "mov edi, %d\n", offset);
		CG_OUTPUT(tmp);	
	}

	//printf("%d\n",cgtype);
	if (cgtype==EXPTYPE_ARRAY){ 
		cgtype=((ArrayDef)ssvar->pAttr)->arrayType;
	}
	else if (cgtype==EXPTYPE_RECORD){
		cgtype=st_var.type;	
	}		
	//printf("%d\n",cgtype);
	CG_OUTPUT("mov esi, ecx\n");
	while (level){
		CG_OUTPUT("mov eax, [esi]\n");
		CG_OUTPUT("mov esi, eax\n");
		level=level-1;
	}
	CG_OUTPUT("add esi, edi\n");


	if (cgtype==EXPTYPE_INT || cgtype==EXPTYPE_CHAR || cgtype==EXPTYPE_BOOL){
		CG_OUTPUT("mov eax, [esi]; calculate ExpId \n");
		pnode->RuningType=EXPTYPE_INT;
		//printf("EXPTYPE_INT\n");
	}
	else if (cgtype==EXPTYPE_REAL){
		CG_OUTPUT("mov eax, dword ptr [esi]\n"); 
		pnode->RuningType=EXPTYPE_REAL;
		//printf("EXPTYPE_REAL\n");
	}

}

void CGExpConst(TreeNode* pnode){
	char const_real_data[100];	

	switch (pnode->type){
		case EXPTYPE_INT:
			sprintf(tmp, "mov eax, %d; calculate int ExpConst \n", pnode->attr.val);
			CG_OUTPUT(tmp);
			pnode->RuningType=EXPTYPE_INT;
			break;
		case EXPTYPE_CHAR:
			sprintf(tmp, "mov eax, %d; calculate char ExpConst \n", pnode->attr.char_val);
			CG_OUTPUT(tmp);
			pnode->RuningType=EXPTYPE_INT;
			break;
		case EXPTYPE_BOOL:
			sprintf(tmp, "mov eax, %d; calculate bool ExpConst \n", pnode->attr.val);
			CG_OUTPUT(tmp);
			pnode->RuningType=EXPTYPE_INT;
			break;
		case EXPTYPE_REAL:
			strcpy(const_real_data, GetLabel_data());	
			sprintf(tmp, "%s dd %lf\n",const_real_data, pnode->attr.real_val);
		    CG_OUTPUT_DATA(tmp);
        	sprintf(tmp,"mov eax, dword ptr[%s]; calculate real ExpConst \n",const_real_data);
        	CG_OUTPUT(tmp);
        	pnode->RuningType=EXPTYPE_REAL;
			break;			
	}
}


void CGPopParam(TreeNode* pnode,SimpleTypeList judge_var){
	
	if (pnode==NULL || judge_var==NULL)
		return;

	if (judge_var->isVar==1){
		CGNodeExpression(pnode);
		CG_OUTPUT("pop eax\n");
		CG_OUTPUT("mov [esi],eax\n");
	}
	else CG_OUTPUT("pop eax\n");

	CGPopParam(pnode->sibling,judge_var->next);
}

void CGPushParam(TreeNode* pnode){
	if (pnode->sibling!=NULL){
		CGPushParam(pnode->sibling);
	}

	CGNodeExpression(pnode);
	CG_OUTPUT("push eax\n");

}

void CGExpFunc(TreeNode* pnode){

	FuncList judge_var=funcListLookup(pnode->attr.name);

	CG_OUTPUT("push eax\n");  // push return value	

	CGPushParam(pnode->child[0]);


	CG_OUTPUT("push ecx\n");  // save access link
	CG_OUTPUT("mov ecx, esp\n");

	sprintf(tmp, "call %s\n", pnode->attr.name);
	CG_OUTPUT(tmp);

	CG_OUTPUT("pop ecx\n");


	CGPopParam(pnode->child[0],judge_var->paraList);

	CG_OUTPUT("pop eax\n");  // pop return value

}


void CGStmtProc(TreeNode* pnode){

	ProcList judge_var=procListLookup(pnode->attr.name);

	CGPushParam(pnode->child[0]);


	CG_OUTPUT("push ecx\n");  // save access link
	CG_OUTPUT("mov ecx, esp\n");

	sprintf(tmp, "call %s\n", pnode->attr.name);
	CG_OUTPUT(tmp);

	CG_OUTPUT("pop ecx\n");

	CGPopParam(pnode->child[0],judge_var->paraList);
}



void CGStmtAssign(TreeNode* pnode){


	//if  (pnode->attr.op==TOKEN_ID){   // x:=1;
		GenerateCode(pnode->child[1]);
		CG_OUTPUT("push eax\n");
		VariableList ssvar=varListLookup((pnode->child[0])->attr.name);
		if (ssvar->isConst){		
 			printf("Line %d, Error: The const variable %s can not be assigned.\n",pnode->lineno,(pnode->child[0])->attr.name);
 			fflush(stdout);	
		}
		GenerateCode(pnode->child[0]);
		CG_OUTPUT("pop eax\n");
		CG_OUTPUT("mov [esi-0], eax; assign\n");
	/*}
	else if (pnode->attr.op==TOKEN_ARRAY){ //x[1]:=1
		GenerateCode(pnode->child[2]);
		CG_OUTPUT("push eax\n");

		GenerateCode(pnode->child[1]);
		CG_OUTPUT("push eax\n");
		GenerateCode(pnode->child[0]);
		CG_OUTPUT("pop eax\n");
		CG_OUTPUT("mov ebx,4\n");  //int = 4  
		CG_OUTPUT("xor edx, edx\nimul ebx\n");
		CG_OUTPUT("add esi,eax\n");

		CG_OUTPUT("pop eax\n");
		CG_OUTPUT("mov [esi-0], eax; assign\n");		


	}
	else if (pnode->attr.op==TOKEN_RECORD){  //x.x:=1
    	
	}*/
}

char* GetLabel()
{
	static int label_cnt = 0;
	char tmp2[1024];
	static char label[1024];
	strcpy(label, "__CG__label");
	sprintf(tmp2, "%d", label_cnt);
	strcat(label, tmp2);
	label_cnt++;
	return label;
}

char* GetLabel_sys(int val)
{
	char tmp2[1024];
	static char label[1024];
	strcpy(label, "__CG__sys_label");
	sprintf(tmp2, "%d", val);
	strcat(label, tmp2);
	return label;
}

char* GetLabel_data(){
    static int datalabel_cnt = 0;
    char tmp[100];
    static char datalabel[200];
    strcpy(datalabel,"_REALNUM");
    sprintf(tmp,"%d",datalabel_cnt);
    strcat(datalabel,tmp);
    datalabel_cnt++;
    return datalabel;
}


void CGStmtLabel(TreeNode* pnode){
	char sys_label[100];
	strcpy(sys_label, GetLabel_sys(pnode->attr.val));
	sprintf(tmp, "%s:\n", sys_label); 
	CG_OUTPUT(tmp);	
	GenerateCode(pnode->child[0]);
}

void CGStmtGoto(TreeNode* pnode){
	char sys_label[100];
	strcpy(sys_label, GetLabel_sys(pnode->attr.val));
	sprintf(tmp, "jmp %s\n", sys_label); 
	CG_OUTPUT(tmp);		
}


void CGStmtCase(TreeNode* pnode){

	char case_end[100];	
	strcpy(case_end, GetLabel());	

	GenerateCode(pnode->child[0]);
	CG_OUTPUT("push eax\n");
	GenerateCode(pnode->child[1]);

}

void CGExpCase(TreeNode* pnode){

	char case_end[100];	
	strcpy(case_end, GetLabel());	

	GenerateCode(pnode->child[0]);
	
	CG_OUTPUT("pop ebx\n");
	CG_OUTPUT("cmp ebx,eax\n");

	sprintf(tmp, "jne %s\n", case_end);  //unequal
	CG_OUTPUT(tmp);

	GenerateCode(pnode->child[1]);

	sprintf(tmp, "%s:\n", case_end);
	CG_OUTPUT(tmp);
	if (pnode->sibling!=NULL)
		CG_OUTPUT("push ebx\n");
}

void CGStmtWhile(TreeNode* pnode){
	char while_start[100], while_end[100];
	strcpy(while_start, GetLabel());
	strcpy(while_end, GetLabel());	


	sprintf(tmp, "%s:\n", while_start);
	CG_OUTPUT(tmp);

	GenerateCode(pnode->child[0]);

	CG_OUTPUT("cmp eax, 0\n");
	sprintf(tmp,"je %s\n",while_end);
	CG_OUTPUT(tmp);

	GenerateCode(pnode->child[1]);
	sprintf(tmp,"jmp %s\n",while_start);
	CG_OUTPUT(tmp);
	sprintf(tmp, "%s:\n", while_end);
	CG_OUTPUT(tmp);
}


void CGStmtRepeat(TreeNode* pnode){
	char repeat_start[100];
	strcpy(repeat_start, GetLabel());
	
	sprintf(tmp,"%s:\n",repeat_start);
	CG_OUTPUT(tmp);	

	GenerateCode(pnode->child[0]);
	GenerateCode(pnode->child[1]);

	CG_OUTPUT("cmp eax, 0\n");
	sprintf(tmp,"je %s\n",repeat_start);
	CG_OUTPUT(tmp);
	
}




void CGStmtFor(TreeNode* pnode){

	char for_start[100], for_end[100];
	int i;
	strcpy(for_start, GetLabel());
	strcpy(for_end, GetLabel());

	//chuzhi
	GenerateCode(pnode->child[1]);
	CG_OUTPUT("push eax\n");
	GenerateCode(pnode->child[0]);
	CG_OUTPUT("pop eax\n");
	//CG_OUTPUT("add esi, ecx\n");
	CG_OUTPUT("mov [esi-0],eax  \n");

	
	//xunhuan
	if(pnode->attr.op == TOKEN_TO){  //to
		sprintf(tmp,"%s:\n",for_start);
		CG_OUTPUT(tmp);	
		GenerateCode(pnode->child[2]);
		CG_OUTPUT("push eax\n");
		GenerateCode(pnode->child[0]);
		CG_OUTPUT("pop ebx\n");	
		CG_OUTPUT("cmp eax, ebx\n");
		sprintf(tmp, "ja %s\n", for_end);
		CG_OUTPUT(tmp);	

		GenerateCode(pnode->child[3]);	

		GenerateCode(pnode->child[0]);
		CG_OUTPUT("inc eax\n");
		CG_OUTPUT("mov [esi-0],eax\n");	

		sprintf(tmp,"jmp %s\n",for_start);
		CG_OUTPUT(tmp);	
		sprintf(tmp,"%s:\n",for_end);
		CG_OUTPUT(tmp);	
	}
	 else {
		sprintf(tmp,"%s:\n",for_start);
		CG_OUTPUT(tmp);	
		GenerateCode(pnode->child[2]);
		CG_OUTPUT("push eax\n");
		GenerateCode(pnode->child[0]);
		CG_OUTPUT("pop ebx\n");	
		CG_OUTPUT("cmp eax, ebx\n");
		sprintf(tmp, "jb %s\n", for_end);
		CG_OUTPUT(tmp);	

		GenerateCode(pnode->child[3]);	

		GenerateCode(pnode->child[0]);
		CG_OUTPUT("dec eax\n");
		CG_OUTPUT("mov [esi-0],eax\n");	
		
		sprintf(tmp,"jmp %s\n",for_start);
		CG_OUTPUT(tmp);	
		sprintf(tmp,"%s:\n",for_end);
		CG_OUTPUT(tmp);	
	 } //downto
}



void CGStmtIf(TreeNode* pnode){
	char if_label[100], else_label[100], exit_label[100];
	strcpy(if_label, GetLabel());
	strcpy(else_label, GetLabel());
	strcpy(exit_label, GetLabel());
	
	//panduan
	GenerateCode(pnode->child[0]);
	CG_OUTPUT("cmp eax, 1\n");	
	sprintf(tmp, "je %s \njmp %s\n", if_label, else_label);
	CG_OUTPUT(tmp);	
	
	//if-part
	sprintf(tmp,"%s:\n",if_label);
	CG_OUTPUT(tmp);	
	GenerateCode(pnode->child[1]);

	sprintf(tmp,"jmp %s\n",exit_label);	
	CG_OUTPUT(tmp);	

	sprintf(tmp, "%s: \n", else_label);
  	CG_OUTPUT(tmp);	

	if (pnode->child[2]!=NULL)
		GenerateCode(pnode->child[2]);
	
	sprintf(tmp, "%s: \n", exit_label);
  	CG_OUTPUT(tmp);				
}

void GStmtOutput(TreeNode* pnode){

	TreeNode *tt=NULL;
	

//	if (pnode->child[0]!=NULL)
		tt=pnode->child[0];

	while (tt!=NULL){
		CGNodeExpression(tt);	

		if (tt->RuningType==EXPTYPE_REAL){
			CG_OUTPUT("pusha\n");
			CG_OUTPUT("push eax\n");
			CG_OUTPUT("fld dword ptr [esp]\n");
			CG_OUTPUT("sub esp, 4\n");
			CG_OUTPUT("fstp qword ptr [esp]\n");
			if  (pnode->attr.op==TOKEN_WRITELN){
				CG_OUTPUT("push offset lb_writeln_real\n");
			}
			else {
				CG_OUTPUT("push offset lb_write_real\n");
			}
			CG_OUTPUT("call printf\n");
			CG_OUTPUT("add esp, 8\n");
			CG_OUTPUT("pop eax\n");
			CG_OUTPUT("popa\n");
		}
		else if (tt->RuningType==EXPTYPE_INT){
			CG_OUTPUT("pusha\n");
			if  (pnode->attr.op==TOKEN_WRITELN){
				CG_OUTPUT("invoke printf,offset lb_writeln_int, eax\n");
			}
			else{
				CG_OUTPUT("invoke printf,offset lb_write_int, eax\n");
			}
			CG_OUTPUT("popa\n");
		}
		tt=tt->sibling;
	}
	
}


void GStmtInput(TreeNode* pnode){

	TreeNode *tt=NULL;

	char output[100];
	char output_all[100];
	strcpy(output, "invoke crt_scanf, addr lb_read_");//	int_write,eax\n");

//	if (pnode->child[0]!=NULL)
	tt=pnode->child[0];

	while (tt!=NULL){
		CGNodeExpression(tt);
		
		output_all[0]=0;
		strcpy(output_all,output);

		if (tt->RuningType==EXPTYPE_INT)
			strcat(output_all,"int, addr lb_tmp\n");
		else if (tt->RuningType==EXPTYPE_REAL)
			strcat(output_all,"real, addr lb_tmp\n");

		CG_OUTPUT("pusha\n");
		CG_OUTPUT(output_all);
		CG_OUTPUT("popa\n");
		CG_OUTPUT("mov eax, dword ptr lb_tmp\n");
		CG_OUTPUT("mov [esi], eax\n");
		tt=tt->sibling;
	}
	
}



void CGNodeExpression(TreeNode* pnode){
	switch (pnode->kind.exp){
				case EXP_ID:
				//printf("EXP_ID\n");
				//fflush(stdout);
					CGExpId(pnode);
					break;
				case EXP_OP:
				//printf("EXP_OP\n");
				//fflush(stdout);
					CGExpOp(pnode);
					break;
				case EXP_CONST:
					CGExpConst(pnode);
					break;
				case EXP_CASE:
					CGExpCase(pnode);
					break;
				case EXP_FUNC_ID:
					CGExpFunc(pnode);
					break;
			}
}


void GenerateCode(TreeNode* pnode){
	int size_param;
	switch (pnode->nodekind){
		case NODE_STATEMENT:
			//printf("NODE_STATEMENT\n");
			//fflush(stdout);
			switch (pnode->kind.stmt){
				case STMT_ASSIGN:
					//printf("STMT_ASSIGN\n");
					//fflush(stdout);
					CGStmtAssign(pnode);
					break;
				case STMT_IF:
					CGStmtIf(pnode);
					break;
				case STMT_FOR:
					CGStmtFor(pnode);
					break;
				case STMT_WHILE:
					CGStmtWhile(pnode);
					break;
				case STMT_REPEAT:
					CGStmtRepeat(pnode);
					break;	
				case STMT_CASE:
					CGStmtCase(pnode);
					break;	
				case STMT_GOTO:
					CGStmtGoto(pnode);
					break;	
				case STMT_LABEL:
					CGStmtLabel(pnode);
					break;	
				case STMT_PROC_ID:
					CGStmtProc(pnode);
					break;
				case STMT_PROC_SYS:
					switch(pnode->attr.op)
					{
						case TOKEN_WRITE:
							GStmtOutput(pnode);
							break;
						case TOKEN_WRITELN:
							GStmtOutput(pnode);
							break;
						case TOKEN_READ:
							GStmtInput(pnode);
							break;
						default: break;
					}
				break;
				
			}
			break;
		case NODE_EXPRESSION:
				//printf("NODE_EXPRESSION\n");
				//fflush(stdout);
				CGNodeExpression(pnode);
			break;
		case NODE_DECLARE:
			switch (pnode->kind.decl){
				case DECL_ROUTINEHEAD:
					//printf("DECL_ROUTINEHEAD\n");
					//fflush(stdout);

					size_param=enterNewScope(pnode);

					if (pnode->child[3]!=NULL)
  						GenerateCode(pnode->child[3]);
								
					if (strcmp(pnode->attr.name,"main")){
						sprintf(tmp, "%s: \n",pnode->attr.name);
						CG_OUTPUT(tmp);
					}
					else {
						CG_OUTPUT("main PROC\n");
						CG_OUTPUT("mov ecx, esp\n");
					}

  					sprintf(tmp, "sub esp, %d\n", size_param);
  					CG_OUTPUT(tmp);		


  					break;
				case DECL_FUNCTION:

					//printf("DECL_FUNCTION\n");
					//fflush(stdout);
					
					funcListInsert(pnode->child[0]);  // give congjie function_head!!!!!!!!!!!!!

					(pnode->child[1])->attr.name=(pnode->child[0])->attr.name;
					GenerateCode(pnode->child[1]); //routine_head 
	
					
					size_param=leaveScope();
					sprintf(tmp, "add esp, %d\n", size_param);
  					CG_OUTPUT(tmp);	
					CG_OUTPUT("ret\n");	

					break;
    			case DECL_PROCEDURE:
	
					//printf("DECL_PROCEDURE\n");
					fflush(stdout);
					
					procListInsert(pnode->child[0]);  // give congjie function_head!!!!!!!!!!!!!

					(pnode->child[1])->attr.name=(pnode->child[0])->attr.name;
					GenerateCode(pnode->child[1]); //routine_head 
				
					size_param=leaveScope();
					sprintf(tmp, "add esp, %d\n", size_param);
  					CG_OUTPUT(tmp);	
					CG_OUTPUT("ret\n");	

					break;
			}
			break;
		case NODE_TYPE:
			break;
		default: printf("pnode->nodekind Default");
	}
	if(pnode->sibling!=NULL){
		GenerateCode(pnode->sibling);	
	}
	
}

int CG_main(TreeNode* pnode,char * ffname){
	int size_param;
	char buf[10000];

	codename = fopen("code_part.asm", "w");
	dataname = fopen("data_part.asm", "w");
/*	
CG_OUTPUT("section .data\n");
CG_OUTPUT("\tintformat db \"%d\",0xa,0\n");     
CG_OUTPUT("section .text\n");
CG_OUTPUT("\tglobal main\n");
CG_OUTPUT("main:\n");
*/
	
	CG_OUTPUT_DATA(".386\n");
	CG_OUTPUT_DATA(".model flat,stdcall\n");
	CG_OUTPUT_DATA("option casemap:none\n");
	CG_OUTPUT_DATA("include masm32\\include\\msvcrt.inc\n");
	CG_OUTPUT_DATA("includelib msvcrt.lib\n");

	CG_OUTPUT_DATA("printf  proto C:dword,:dword\n");

	CG_OUTPUT_DATA(".data\n");
	CG_OUTPUT_DATA("lb_write_int db '%d',0\n");
	CG_OUTPUT_DATA("lb_writeln_int db '%d',0ah,0dh,0\n");
	CG_OUTPUT_DATA("lb_write_real db '%lf',0\n");
	CG_OUTPUT_DATA("lb_writeln_real db '%lf',0ah,0dh,0\n");
	CG_OUTPUT_DATA("lb_tmp db 0, 0, 0, 0, 0, 0, 0, 0\n");
	CG_OUTPUT_DATA("lb_read_int db '%d',0\n");
	CG_OUTPUT_DATA("lb_read_real db '%f',0\n");

	CG_OUTPUT(".code\n");	
	
	initScope();
	pnode->attr.name="main";
	GenerateCode(pnode);
	

	size_param=leaveScope();  //li kai zhu yu shi
	sprintf(tmp, "add esp, %d\n", size_param);
  	CG_OUTPUT(tmp);	
	CG_OUTPUT("main ENDP\n");
	CG_OUTPUT("END main\n");
	fclose(codename);
	fclose(dataname);
	

	ff = fopen(ffname, "w");
	codename = fopen("code_part.asm", "r");
	dataname = fopen("data_part.asm", "r");	
	while(fgets(buf,BUFSIZ,dataname)!=NULL){
		fputs(buf,ff);
	}
	while(fgets(buf,BUFSIZ,codename)!=NULL){
		fputs(buf,ff);
	}	
	fclose(codename);
	fclose(dataname);
	fclose(ff);	
}
