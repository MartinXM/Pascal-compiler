#include "CodeGenerator.h"
using namespace std;

CodeGenerator::CodeGenerator() {
	fileWriter = NULL;

	// functions[NODE_STATEMENT][STMT_LABEL] = new CGLabel(this);
	// functions[NODE_STATEMENT][STMT_ASSIGN] = new CGAssign(this);
	// functions[NODE_STATEMENT][STMT_GOTO] = new CGGoto(this);
	// functions[NODE_STATEMENT][STMT_IF] = new CGIf(this);
	// functions[NODE_STATEMENT][STMT_REPEAT] = new CGRepeat(this);
	// functions[NODE_STATEMENT][STMT_WHILE] = new CGWhile(this);
	// functions[NODE_STATEMENT][STMT_FOR] = new CGFor(this);
	// functions[NODE_STATEMENT][STMT_CASE] = new CGCase(this);
	// functions[NODE_STATEMENT][STMT_PROC_ID] = new CGProcId(this);
	// functions[NODE_STATEMENT][STMT_PROC_SYS] = new CGProcSys(this);

	// functions[NODE_EXPRESSION][EXP_ID] = new CGExpId(this);
	// functions[NODE_EXPRESSION][EXP_CONST] = new CGExpConst(this);
	// functions[NODE_EXPRESSION][EXP_OP] = new CGExpOp(this);
	// functions[NODE_EXPRESSION][EXP_CASE] = new CGExpCase(this);
	// functions[NODE_EXPRESSION][EXP_FUNC_ID] = new CGExpFuncId(this);
	// functions[NODE_EXPRESSION][EXP_FUNC_SYS] = new CGExpFuncSys(this);
	
	// functions[NODE_DECLARE][DECL_ROUTINEHEAD] = new CGDeclRoutinehead(this);
	// functions[NODE_DECLARE][DECL_FUNCTION] = new CGDeclFunction(this);
	// functions[NODE_DECLARE][DECL_FUNCTIONHEAD] = new CGDeclFunctionHead(this);
	// functions[NODE_DECLARE][DECL_PROCEDURE] = new CGDeclProcedure(this);
	// functions[NODE_DECLARE][DECL_PROCEDUREHEAD] = new CGDeclProcedureHead(this);
	// functions[NODE_DECLARE][DECL_CONST] = new CGDeclConst(this);
	// functions[NODE_DECLARE][DECL_VAR] = new CGDeclVar(this);
	// functions[NODE_DECLARE][DECL_TYPE] = new CGDeclType(this);
	// functions[NODE_DECLARE][DECL_VAR_PARA] = new CGDeclVarPara(this);
	// functions[NODE_DECLARE][DECL_VAL_PARA] = new CGDeclValPara(this);

	// functions[NODE_TYPE][TYPE_SIMPLE_SYS] = new CGTypeSimpleSys(this);
	// functions[NODE_TYPE][TYPE_SIMPLE_ID] = new CGTypeSimpleId(this);
	// functions[NODE_TYPE][TYPE_SIMPLE_ENUM] = new CGTypeSimpleEnum(this);
	// functions[NODE_TYPE][TYPE_SIMPLE_LIMIT] = new CGTypeSimpleLimit(this);
	// functions[NODE_TYPE][TYPE_SIMPLE_ARRAY] = new CGTypeSimpleArray(this);
	// functions[NODE_TYPE][TYPE_SIMPLE_RECORD] = new CGTypeSimpleRecord(this);
}

CodeGenerator::~CodeGenerator() {
	for (auto it1 = functions.begin(); it1 != functions.end(); it1++) {
		for (auto it2 = it1->second.begin(); it2 != it1->second.end(); it2++) {
			delete it2->second;
		}
	}
	if (fileWriter != NULL) {
		delete fileWriter;
	}
}

void CodeGenerator::generateCode(TreeNode *pnode) {
	generateCode(pnode, "out.asm");
}

void CodeGenerator::generateCode(TreeNode *pnode, string fileName) {
	beforeGC();
	(*functions[pnode->nodekind][pnode->kind.stmt])(pnode);
	afterGC();
	fileWriter = new FileWriter(fileName);
}

void CodeGenerator::operator()(TreeNode *pnode) {
	generateCode(pnode);
}

void CodeGenerator::operator()(TreeNode *pnode, string fileName) {
	generateCode(pnode, fileName);
}

void CodeGenerator::writeData(string str) {
	dataSegment += str;
}

void CodeGenerator::writelnData(string str) {
	dataSegment += str + "\n";
}

void CodeGenerator::writeCode(string str) {
	codeSegment += str;
}

void CodeGenerator::writelnCode(string str) {
	codeSegment += str + "\n";
}

void CodeGenerator::beforeGC() {

}

void CodeGenerator::afterGC() {

}

