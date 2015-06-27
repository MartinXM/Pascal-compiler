#ifndef _CGFUNCTION_H_
#define _CGFUNCTION_H_

#include "CGGlobal.h"
using namespace std;

class CodeGenerator;

class CGFunction {

private:
	CodeGenerator *codeGenerator;

public:
	CGFunction(CodeGenerator *cg) : codeGenerator(cg) {}

	virtual void operator()(TreeNode *pnode) = 0;
	virtual ~CGFunction() {}
};

#endif