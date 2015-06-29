#include "global.h"
#include "util.h"

void printToken(TokenType token, const char * tokenString){
    switch(token){
        case ENDFILE:
            fprintf(listing,"EOF\n");break;
        case ERROR:
            fprintf(listing,"ERROR:%s\n",tokenString);

        case TOKEN_PROGRAM:
        case TOKEN_CONST:
        case TOKEN_TYPE:
        case TOKEN_VAR:
        case TOKEN_PROCEDURE:
        case TOKEN_FUNCTION:
        case TOKEN_OF:
        case TOKEN_BEGIN:
        case TOKEN_END:
        case TOKEN_IF:
        case TOKEN_THEN:
        case TOKEN_ELSE:
            fprintf(listing,"reserved word: %s\n", tokenString);
            break;

        case TOKEN_TO:
            fprintf(listing,"to\n");break;
        case TOKEN_DOWNTO:
            fprintf(listing,"down to\n");break;
        case TOKEN_READ:
            fprintf(listing,"read\n");break;
        case TOKEN_WRITE:
            fprintf(listing,"write\n");break;
        case TOKEN_WRITELN:
            fprintf(listing,"writeln\n");break;
            

        case TOKEN_EQUAL:   fprintf(listing,"=\n",tokenString);break;
        case TOKEN_UNEQUAL:   fprintf(listing,"<>\n",tokenString);break;
        case TOKEN_GE:   fprintf(listing,">=\n",tokenString);break;
        case TOKEN_GT:   fprintf(listing,">\n",tokenString);break;
        case TOKEN_LE:   fprintf(listing,"<=\n",tokenString);break;
        case TOKEN_LT:   fprintf(listing,"<\n",tokenString);break;
        case TOKEN_PLUS:    fprintf(listing,"+\n");break;
        case TOKEN_MINUS:   fprintf(listing,"-\n");break;
        case TOKEN_MUL:     fprintf(listing,"*\n");break;
        case TOKEN_DIV:     fprintf(listing,"\\n");break;
        case TOKEN_OR:      fprintf(listing,"or\n");break;
        case TOKEN_AND:     fprintf(listing,"and\n");break; 
        case TOKEN_MOD:     fprintf(listing,"mod\n");break; 

        case TOKEN_LB:      fprintf(listing,"[\n");break;
        case TOKEN_RB:      fprintf(listing,"]\n");break;
        case TOKEN_SEMI:    fprintf(listing,";\n");break;
        case TOKEN_DOT:     fprintf(listing,".\n");break;
        case TOKEN_DOTDOT:  fprintf(listing,".\n");break;
        case TOKEN_LP:      fprintf(listing,"(\n");break;
        case TOKEN_RP:      fprintf(listing,")\n");break;
        case TOKEN_COMMA:   fprintf(listing,",\n");break;
        case TOKEN_COLON:   fprintf(listing,":\n");break;

        case TOKEN_INTEGER_TYPE:
        case TOKEN_BOOLEAN_TYPE:
        case TOKEN_CHAR_TYPE:
        case TOKEN_REAL_TYPE:
        case TOKEN_ARRAY:
            fprintf(listing,"TYPE, name=%s\n",tokenString);break;

        case TOKEN_TRUE:
        case TOKEN_FALSE:
            fprintf(listing,"BOOLEAN, val=%s\n",tokenString);break;

        case TOKEN_INT: fprintf(listing,"INT, val=%s\n",tokenString);break;
        case TOKEN_REAL: fprintf(listing,"REAL, val=%s\n",tokenString);break;
        case TOKEN_CHAR: fprintf(listing,"CHAR, val=%s\n",tokenString);break;
        case TOKEN_STRING: fprintf(listing,"STRING, val=%s\n",tokenString);break;

        case TOKEN_ID: fprintf(listing,"ID name=%s\n",tokenString);break;

        case TOKEN_ABS: fprintf(listing,"abs\n");break;
        case TOKEN_CHR: fprintf(listing,"chr\n");break;
        case TOKEN_ODD: fprintf(listing,"odd\n");break;
        case TOKEN_ORD: fprintf(listing,"ord\n");break;
        case TOKEN_PRED:fprintf(listing,"pred\n");break;
        case TOKEN_SQR: fprintf(listing,"sqr\n");break;
        case TOKEN_SQRT:fprintf(listing,"sqrt\n");break;
        case TOKEN_SUCC:fprintf(listing,"succ\n");break;
        
        default:
            fprintf(listing,"Unknown token:%s\n",tokenString);
    }
}

TreeNode * newDeclNode(DeclKind kind){
    TreeNode * t = (TreeNode *)malloc(sizeof(TreeNode));
    int i;
    if(t==NULL)
      fprintf(listing,"Out of memory error at line %d\n",lineno);
    else{
        for(i=0;i<MAXCHILDREN;i++)
          t->child[i]=NULL;
        t->sibling=NULL;
        t->nodekind=NODE_DECLARE;  
        t->kind.decl = kind;
        t->lineno=lineno;
    }
    return t;
}


TreeNode * newStmtNode(StmtKind kind){
    TreeNode * t = (TreeNode *)malloc(sizeof(TreeNode));
    int i;
    if(t==NULL)
      fprintf(listing,"Out of memory error at line %d\n",lineno);
    else{
        for(i=0;i<MAXCHILDREN;i++)
          t->child[i]=NULL;
        t->sibling=NULL;
        t->nodekind=NODE_STATEMENT;
        t->kind.stmt=kind;
        t->lineno=lineno;
    }
    return t;
}


TreeNode * newExpNode(ExpKind kind){
    TreeNode * t = (TreeNode *)malloc(sizeof(TreeNode));
    int i;
    if(t==NULL)
      fprintf(listing,"Out of memory error at line %d\n",lineno);
    else{
        for(i=0;i<MAXCHILDREN;i++)
          t->child[i]=NULL;
        t->sibling=NULL;
        t->nodekind=NODE_EXPRESSION;
        t->kind.exp=kind;
        t->lineno=lineno;
        t->type = EXPTYPE_VOID;
    }
    return t;
}
    
TreeNode * newOpExpNode(TreeNode * first, TreeNode * second, TokenType op){
    TreeNode * t = (TreeNode *)malloc(sizeof(TreeNode));
    int i;
    if(t==NULL)
      fprintf(listing,"Out of memory error at line %d\n",lineno);
    else{
        for(i=0;i<MAXCHILDREN;i++)
          t->child[i]=NULL;
        t->sibling=NULL;
        t->nodekind=NODE_EXPRESSION;
        t->kind.exp=EXP_OP;
        t->attr.op=op;
        t->lineno=lineno;
        t->child[0]=first;
        t->child[1]=second;
    }
    return t;

}

TreeNode * newTypeNode(TypeKind type){
    TreeNode * t = (TreeNode *)malloc(sizeof(TreeNode));
    int i;
    if(t==NULL)
      fprintf(listing,"Out of memory error at line %d\n",lineno);
    else{
        for(i=0;i<MAXCHILDREN;i++)
          t->child[i]=NULL;
        t->sibling=NULL;
        t->nodekind=NODE_TYPE;   
        t->kind.type=type;
        t->lineno=lineno;
    }
    return t;
}

TreeNode * newFuncSysExpNode(TokenType op, TreeNode* args){
    TreeNode * t = (TreeNode *)malloc(sizeof(TreeNode));
    int i;
    if(t==NULL)
      fprintf(listing,"Out of memory error at line %d\n",lineno);
    else{
        for(i=0;i<MAXCHILDREN;i++)
          t->child[i]=NULL;
        t->sibling=NULL;
        t->nodekind=NODE_EXPRESSION;
        t->kind.exp=EXP_FUNC_SYS;
        t->child[0]=args;
        t->attr.op=op;
        t->lineno=lineno;
    }
    return t;

}

void freeNode(TreeNode *node){
    int i;
    for(i=0;i<MAXCHILDREN;i++)
      free(node->child[i]);
    free(node->sibling);
    free(node);
}


char* copyString(char *s){
    int n;
    char *t;
    if(s==NULL)
      return NULL;
    n=strlen(s)+1;
    t=malloc(n);
    if(t==NULL)
      fprintf(listing,"Out of memory error at line %d\n",lineno);
    else
      strcpy(t,s);
    return t;
}

//used for printTree
static indentno=0;
#define INDENT indentno+=2
#define UNINDENT indentno-=2

static void printSpaces(void){
    int i;
    for(i=0;i<indentno;i++)
      fprintf(listing," ");
}

void printTree(TreeNode * tree){
    int i;
    INDENT;
    while(tree!=NULL){
        printSpaces();
        switch(tree->nodekind){
            case(NODE_STATEMENT):
                switch(tree->kind.stmt){
                    case STMT_LABEL:    fprintf(listing,"Stmt labelno%d\n",tree->attr.val);break;
                    case STMT_ASSIGN:   fprintf(listing,"Stmt Assign\n");break;
                    case STMT_GOTO:     fprintf(listing,"goto %d\n",tree->attr.val);break;
                    case STMT_IF:       fprintf(listing,"if\n");break;
                    case STMT_REPEAT:   fprintf(listing,"repeat\n");break;
                    case STMT_WHILE:    fprintf(listing,"while\n");break;
                    case STMT_CASE:     fprintf(listing,"case\n");break;
                    case STMT_FOR:      fprintf(listing,"for direction:");printToken(tree->attr.op,"\0");break;
                    case STMT_PROC_SYS:  fprintf(listing,"system procedure:");printToken(tree->attr.op,"\0");break;
                    case STMT_PROC_ID:   fprintf(listing,"procedure:");break;
                    default: fprintf(listing,"Unknown Statement type\n");break;
                }
            break;
        
            case(NODE_EXPRESSION):
               switch(tree->kind.exp){
                    case EXP_OP:
                        fprintf(listing,"EXP op: "); printToken(tree->attr.op,"\0");break;
                    case EXP_CONST:
                        switch(tree->type){
                            case EXPTYPE_INT: fprintf(listing,"const int:%d\n",tree->attr.val); break;
                            case EXPTYPE_REAL: fprintf(listing,"const real:%lf\n",tree->attr.real_val); break;
                            case EXPTYPE_CHAR: fprintf(listing,"const char:%c\n",tree->attr.char_val); break;
                            case EXPTYPE_STRING: fprintf(listing,"const string:%s\n",tree->attr.string_val); break;
                        }
                        break;
                    case EXP_ID: fprintf(listing,"Exp ID: %s\n",tree->attr.name); break;
                    case EXP_CASE: fprintf(listing,"Exp case:");break;
                    case EXP_FUNC_ID: fprintf(listing,"Func:");break;
                    case EXP_FUNC_SYS: fprintf(listing,"Sys func:");printToken(tree->attr.op,"\0");break;
                    default: fprintf(listing,"Unknown Expression type\n");
                }
            break;
        
            case(NODE_DECLARE):
                switch(tree->kind.decl){
                    case DECL_ROUTINEHEAD: fprintf(listing,"Routine Head\n");break;
                    case DECL_PROCEDURE: fprintf(listing,"Declare Procedure\n");break;
                    case DECL_PROCEDUREHEAD: fprintf(listing,"Procedure Head %s\n",tree->attr.name);break;
                    case DECL_CONST: fprintf(listing,"Const Declare:\n");break;
                    case DECL_TYPE: fprintf(listing,"Type Declare:\n");break;
                    case DECL_VAR: fprintf(listing,"Var Declare:\n");break;
                    case DECL_FUNCTION: fprintf(listing,"Declare Function \n");break;
                    case DECL_FUNCTIONHEAD:fprintf(listing,"Function Head:%s\n",tree->attr.name);break;
                    case DECL_VAR_PARA:fprintf(listing,"var parameter:\n");break;
                    case DECL_VAL_PARA:fprintf(listing,"val parameters:\n");break;
                }
            break;
            
            case(NODE_TYPE):
                switch(tree->kind.type){
                    case TYPE_SIMPLE_SYS:
                        switch(tree->type){
                            case EXPTYPE_INT:fprintf(listing,"type integer\n");break;
                            case EXPTYPE_REAL:fprintf(listing,"type real\n");break;
                            case EXPTYPE_CHAR:fprintf(listing,"type char\n");break;
                            case EXPTYPE_BOOL:fprintf(listing,"type boolean\n");break;
                        }
                    break;
                    case TYPE_SIMPLE_ID: fprintf(listing,"type id\n"); break;
                    case TYPE_SIMPLE_ENUM: fprintf(listing,"type enum\n"); break;
                    case TYPE_SIMPLE_LIMIT: fprintf(listing,"type limit\n"); break;
                    case TYPE_ARRAY: fprintf(listing,"type array\n");break;
                    case TYPE_RECORD: fprintf(listing,"type record\n");break;
                    default: fprintf(listing,"Unknown type\n");break;
                }
            break;

            default: fprintf(listing,"Unknown node kind\n"); break;
        }

        for(i=0;i<MAXCHILDREN;i++)
          printTree(tree->child[i]);
        tree=tree->sibling;
    }
    UNINDENT;
}

















