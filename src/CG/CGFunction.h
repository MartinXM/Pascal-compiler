#ifndef _CGFUNCTION_H_
#define _CGFUNCTION_H_

#include "CGGlobal.h"
using namespace std;

class CodeGenerator;

class CGFunction {

private:
	CodeGenerator *codeGenerator;

protected:
	void writelnData(string str) {
		codeGenerator.writelnData(str);
	}
	void writelnCode(string str) {
		codeGenerator.writelnCode(str);
	}

public:
	CGFunction(CodeGenerator *cg) : codeGenerator(cg) {}

	virtual void operator()(TreeNode *pnode) = 0;
	virtual ~CGFunction() {}
};

#endif