#ifndef _GLOBALS_H_
#define _GLOBALS_H_

#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>
#include <string.h>

#ifndef YYPARSER
#include "y.tab.h"
#define ENDFILE 0
#endif

#ifndef False
#define False 0
#endif

#ifndef True
#define True 1
#endif

#define MAXRESERVED 8
typedef int TokenType;
extern FILE* source;
extern FILE* listing;
//extern FILE* code;

extern int lineno;

/*
typedef enum{
    // book-keeping
    ENDFILE,ERROR,
    //reserved
    TOKEN_PROGRAM,
    //
    TOKEN_CONST,TOKEN_TYPE,TOKEN_VAR,
    //reserved
    IF,THEN,
    //special symbol
    EQUAL,PLUS,OR,LB,RB,SEMI,SPLIT,DOT,LP,RP,COMMA,COLON,
    //type
    INT,BOOL,TRUE,FALSE,ARRAY,OF,
    //multicharacter tokens
    ID,TOKEN_INT,TOKEN_REAL,TOKEN_CHAR,TOKEN_STRING
}TokenType;
*/

typedef enum{
    NODE_STATEMENT, NODE_EXPRESSION, NODE_DECLARE, NODE_TYPE
}NodeKind;

typedef enum{
    STMT_LABEL,
    STMT_ASSIGN,STMT_GOTO,STMT_IF,STMT_REPEAT,STMT_WHILE,STMT_FOR,STMT_CASE,
    STMT_PROC_ID,STMT_PROC_SYS
}StmtKind;

typedef enum{
    EXP_ID,
    EXP_CONST,
    EXP_OP,EXP_CASE,EXP_FUNC_ID,EXP_FUNC_SYS
}ExpKind;

typedef enum{
    DECL_ROUTINEHEAD,
    DECL_FUNCTION,DECL_FUNCTIONHEAD,
    DECL_PROCEDURE,DECL_PROCEDUREHEAD,
    DECL_CONST,DECL_VAR,DECL_TYPE,
    DECL_VAR_PARA,DECL_VAL_PARA
}DeclKind;

typedef enum{
    TYPE_SIMPLE_SYS,TYPE_SIMPLE_ID, TYPE_SIMPLE_ENUM, TYPE_SIMPLE_LIMIT,
    TYPE_ARRAY, TYPE_RECORD
}TypeKind;

//
typedef enum{
    EXPTYPE_VOID,EXPTYPE_INT,EXPTYPE_REAL,
    EXPTYPE_CHAR,EXPTYPE_STRING,EXPTYPE_BOOL,
    EXPTYPE_ARRAY,EXPTYPE_RECORD,
    EXPTYPE_SIMPLE_ID,EXPTYPE_SIMPLE_ENUM,EXPTYPE_SIMPLE_LIMIT
}ExpType;

#define MAXCHILDREN 4

typedef struct treeNode{
    struct treeNode * child[MAXCHILDREN];
    struct treeNode * sibling;
    NodeKind nodekind;
    int lineno;
    union{ 
        StmtKind stmt;
        ExpKind exp;
        DeclKind decl;
        TypeKind type;
    }kind;
    union{
        TokenType op;
        char * name;
        int val;
        char char_val;
        char * string_val;
        double real_val;
    }attr;
    ExpType type;
    ExpType RuningType;
}TreeNode;

extern int TraceScan;





#endif
