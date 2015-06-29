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

void typeCheck(TreeNode* t);

int compoundTypeEqual(TreeNode* t1,TreeNode* t2);
int nodeTypeEqual(TreeNode* t1,TreeNode* t2);

void checkNodeExpression(TreeNode* pnode);
void checkExpId(TreeNode* pnode);
void checkExpOp(TreeNode* pnode);
void checkExpOp(TreeNode* pnode);
void checkExpConst(TreeNode* pnode);
void checkExpCase(TreeNode* pnode);
void checkExpFunc(TreeNode* pnode);

void checkStmtAssign(TreeNode* pnode);
void checkStmtIf(TreeNode* pnode);
void checkStmtFor(TreeNode* pnode);
void checkStmtWhile(TreeNode* pnode);
void checkStmtRepeat(TreeNode* pnode);
void checkStmtCase(TreeNode* pnode);
void checkStmtGoto(TreeNode* pnode);
void checkStmtLabel(TreeNode* pnode);
void checkStmtProc(TreeNode* pnode);

static void checkNode(TreeNode* pnode);
void typeError();

#endif
