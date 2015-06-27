#ifndef _CGEXPID_H_
#define _CGEXPID_H_

#include "CGGlobal.h"
using namespace std;

class CGExpId : public CGFunction {

public:
	CGExpId(CodeGenerator *cg) : CGFunction(cg) {}
	virtual ~CGExpId() {}
	
	virtual void operator()(TreeNode *pnode);
};

void CGExpId::operator()(TreeNode *pnode) {
	
}

#endif