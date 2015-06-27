#ifndef _CODEGENERATOR_H_
#define _CODEGENERATOR_H_

#include "CGGlobal.h"
using namespace std;

class CodeGenerator {

private:
	map<NodeKind, map<int, CGFunction *> > functions;
	FileWriter *fileWriter;
	string dataSegment;
	string codeSegment;

	void writeData(string str);
	void writelnData(string str);
	void writeCode(string str);
	void writelnCode(string str);

	void beforeGC();
	void afterGC();

public:
	CodeGenerator();
	~CodeGenerator();

	void generateCode(TreeNode *pnode);
	void generateCode(TreeNode *pnode, string fileName);

	void operator()(TreeNode *pnode);
	void operator()(TreeNode *pnode, string fileName);
};

#endif