#include "global.h"
#include "util.h"
#include "scan.h"
#include "parse.h"
#include "CG.h"
#include "symtab.h"
#include "analyze.h"
char filename[30] = "test2";
FILE * source ;
FILE * listing ;
int TraceScan = True; 
int lineno=0;
// int error=False;

int main(){
// ningning
    source = fopen(filename,"r");
    listing=stdout;
    TreeNode * syntaxTree;
 //   while(getToken()!=ENDFILE);
    syntaxTree = parse();
    printTree(syntaxTree);  

// type check
    typeCheck(syntaxTree);

// caihua
//  if(!error)
		CG_main(syntaxTree,"out.asm");
//	else
//		printf("Info: compiling exit with errors!");

    return 0;
}
