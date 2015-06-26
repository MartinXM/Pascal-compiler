#ifndef _ANALYZE_H_
#define _ANALYZE_H_

#define OFFSET_INC 4


/*counter for variable memory location*/
static int base = 0;
static int offset = 0;

/*trace*/
static int TraceAnalyze = 1;

/*error*/
static int Error = 0;

int buildSymtab(TreeNode* t);

#endif
