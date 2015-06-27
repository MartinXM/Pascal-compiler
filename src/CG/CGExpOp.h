#ifndef _CGEXPOP_H_
#define _CGEXPOP_H_

#include "CGGlobal.h"
using namespace std;

class CGExpOp : public CGFunction {

public:
	CGExpOp(CodeGenerator *cg) : CGFunction(cg) {}
	virtual ~CGExpOp() {}
	
	virtual void operator()(TreeNode *pnode);
};

void CGExpOp::operator()(TreeNode *pnode) {
	if (pnode->child[0] != NULL && pnode->child[1] != NULL) {
		codeGenerator->generateCode(pnode->child[0]);	// 计算左操作数
		writelnCode("push eax");
		codeGenerator->generateCode(pnode->child[1]);
		writelnCode("push eax");
	}
	else if (pnode->child[0] != NULL) {
		// 单目运算符，目前只支持正负号，左操作数为0
		writelnCode("push 0");
		codeGenerator->generateCode(pnode->child[0]);
		writelnCode("push eax");
	}
	else {
		cgerror("Operator with no operand.");
	}

	if (pnode->type != EXPTYPE_REAL) {
		writelnCode("pop ebx");
		writelnCode("pop eax");
		switch(pnode->attr.op)
		{
			case TOKEN_PLUS:
				writelnCode("add eax, ebx");
				break;
			case TOKEN_MINUS:
				writelnCode("sub eax, ebx");
				break;
			case TOKEN_MUL:
				writelnCode("xor edx, edx");
				writelnCode("imul ebx");
				break;
			case TOKEN_DIV:
				writelnCode("xor edx, edx");
				writelnCode("idiv ebx");
				break;
			case TOKEN_MOD:
				writelnCode("xor edx, edx");
				writelnCode("idiv ebx");
				writelnCode("mov eax,edx");
				break;
			case TOKEN_LT:
				writelnCode("cmp eax, ebx");
				writelnCode("mov eax, 0");
				writelnCode("setl al");		// 因为这个时候要传回表达式的值，因此不能直接置flag寄存器，而是要将布尔表达式的值存入eax
				break;
			case TOKEN_LE:
				writelnCode("cmp eax, ebx");
				writelnCode("mov eax, 0");
				writelnCode("setng al");
				break;
			case TOKEN_GT:
				writelnCode("cmp eax, ebx");
				writelnCode("mov eax, 0");
				writelnCode("setg al");
				break;
			case TOKEN_GE:
				writelnCode("cmp eax, ebx");
				writelnCode("mov eax, 0");
				writelnCode("setnl eax");
				break;
			case TOKEN_EQUAL:
				writelnCode("cmp eax, ebx");
				writelnCode("mov eax, 0");
				writelnCode("sete al");
				break;
			case TOKEN_UNEQUAL:
				writelnCode("cmp eax, ebx");
				writelnCode("mov eax, 0");
				writelnCode("setne al");
				break;
		}
	}
	else {
		// real类型
		if (pnode->attr.op == TOKEN_PLUS || pnode->attr.op == TOKEN_MINUS || pnode->attr.op == TOKEN_MUL || pnode->attr.op == TOKEN_DIV) {
			// 算术运算的时候要倒过来压栈，因为无操作数版的算术指令是ST(1)作为第一个操作数，ST(0)作为第二个操作数
			writelnCode("fld dword ptr [esp+4]");
			writelnCode("fld dword ptr [esp]");
		}
		else {
			writelnCode("fld dword ptr [esp]");
			writelnCode("fld dword ptr [esp+4]");
		}
		writelnCode("pop eax");
		writelnCode("pop eax");
		switch(pnode->attr.op){
		case TOKEN_PLUS:
			writelnCode("fadd");
			break;
		case TOKEN_MINUS:
			writelnCode("fsub");
			break;
		case TOKEN_MUL:
			writelnCode("fmul");
			break;
		case TOKEN_DIV:
			writelnCode("fdiv");
			break;
		case TOKEN_LT:
			writelnCode("fcomi st(0), st(1)");
			writelnCode("mov eax, 0");
			writelnCode("setb al");
			break;
			/*
			 * ja label 相当于
			 * if(CF == 0 && ZF == 0)goto label;
			 * 
			 * jg label 相当于
			 * if(ZF == 0 && SF == OF)goto label;
			 *
			 * seta和setg同理，所以这里要使用set above / set below 这一系列的，不能用setg/setl
			 */
		case TOKEN_LE:
			writelnCode("fcomi st(0), st(1)");
			writelnCode("mov eax, 0");
			writelnCode("setna al");
			break;
		case TOKEN_GT:
			writelnCode("fcomi st(0), st(1)");
			writelnCode("mov eax, 0");
			writelnCode("seta al");
			break;
		case TOKEN_GE:
			writelnCode("fcomi st(0), st(1)");
			writelnCode("mov eax, 0");
			writelnCode("setnb eax");
			break;
		case TOKEN_EQUAL:
			writelnCode("fcomi st(0), st(1)");
			writelnCode("mov eax, 0");
			writelnCode("sete al");
			break;
		case TOKEN_UNEQUAL:
			writelnCode("fcomi st(0), st(1)");
			writelnCode("mov eax, 0");
			writelnCode("setne al");
			break;
		}
		if (pnode->attr.op == TOKEN_PLUS || pnode->attr.op == TOKEN_MINUS || pnode->attr.op == TOKEN_MUL || pnode->attr.op == TOKEN_DIV) {
			// 当执行的是算术指令时，将结果从浮点寄存器栈弹出到eax
			writelnCode("sub esp,4");
			writelnCode("fstp dword ptr [esp]");
			writelnCode("pop eax");
		}
	}
}

#endif