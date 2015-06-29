package codegeneration;

import tree.ExpType;
import tree.OpKind;
import tree.TreeNode;

/**
 * Created by kehanyang on 15/6/28.
 */
public class CGExpOp extends Generator {

    CGExpOp(CodeGenerator codeGenerator) {
        super(codeGenerator);
    }

    @Override
    void generateCode(TreeNode node) {

        if (node.getChildren().get(0) != null && node.getChildren().get(1) != null) {

            TreeNode child1 = node.getChildren().get(0);
            TreeNode child2 = node.getChildren().get(1);

            codeGenerator.generateCode(child1);  // 计算左操作数
            codeGenerator.writeCodeLine("push eax");
            codeGenerator.generateCode(child2);  // 计算右操作数
            codeGenerator.writeCodeLine("push eax");

            if (child1.getRunningType() != child2.getRunningType()) {
                codeGenerator.error(node.getLineNumber(), "Different running type on the both sides of operator.");
            }

            node.setRunningType(child1.getRunningType());

        } else if (node.getChildren().get(0) != null) {
            // 单目运算符，目前只支持正负号，左操作数为0
            codeGenerator.writeCodeLine("push 0");
            codeGenerator.generateCode(node.getChildren().get(0));
            codeGenerator.writeCodeLine("push eax");

            node.setRunningType(node.getChildren().get(0).getRunningType());

        } else {
            codeGenerator.error(node.getLineNumber(), "Operator with unsupported number of operand.");
        }

        OpKind opKind = (OpKind)node.getAttribute();
        if (node.getRunningType() != ExpType.REAL) {
            codeGenerator.writeCodeLine("pop ebx");
            codeGenerator.writeCodeLine("pop eax");
            switch (opKind) {
                case PLUS:
                    codeGenerator.writeCodeLine("add eax, ebx");
                    break;
                case MINUS:
                    codeGenerator.writeCodeLine("sub eax, ebx");
                    break;
                case MUL:
                    codeGenerator.writeCodeLine("xor edx, edx");
                    codeGenerator.writeCodeLine("imul ebx");
                    break;
                case DIV:
                    codeGenerator.writeCodeLine("xor edx, edx");
                    codeGenerator.writeCodeLine("idiv ebx");
                    break;
                case MOD:
                    codeGenerator.writeCodeLine("xor edx, edx");
                    codeGenerator.writeCodeLine("idiv ebx");
                    codeGenerator.writeCodeLine("mov eax,edx");
                    break;
                case LT:
                    codeGenerator.writeCodeLine("cmp eax, ebx");
                    codeGenerator.writeCodeLine("mov eax, 0");
                    codeGenerator.writeCodeLine("setl al");		// 因为这个时候要传回表达式的值，因此不能直接置flag寄存器，而是要将布尔表达式的值存入eax
                    break;
                case LE:
                    codeGenerator.writeCodeLine("cmp eax, ebx");
                    codeGenerator.writeCodeLine("mov eax, 0");
                    codeGenerator.writeCodeLine("setng al");
                    break;
                case GT:
                    codeGenerator.writeCodeLine("cmp eax, ebx");
                    codeGenerator.writeCodeLine("mov eax, 0");
                    codeGenerator.writeCodeLine("setg al");
                    break;
                case GE:
                    codeGenerator.writeCodeLine("cmp eax, ebx");
                    codeGenerator.writeCodeLine("mov eax, 0");
                    codeGenerator.writeCodeLine("setnl eax");
                    break;
                case EQUAL:
                    codeGenerator.writeCodeLine("cmp eax, ebx");
                    codeGenerator.writeCodeLine("mov eax, 0");
                    codeGenerator.writeCodeLine("sete al");
                    break;
                case UNEQUAL:
                    codeGenerator.writeCodeLine("cmp eax, ebx");
                    codeGenerator.writeCodeLine("mov eax, 0");
                    codeGenerator.writeCodeLine("setne al");
                    break;
            }
        } else {
            // real类型运算
            if (opKind == OpKind.PLUS || opKind == OpKind.MINUS || opKind == OpKind.MUL || opKind == OpKind.DIV) {
                // 算术运算的时候要倒过来压栈，因为无操作数版的算术指令是ST(1)作为第一个操作数，ST(0)作为第二个操作数
                codeGenerator.writeCodeLine("fld dword ptr [esp+4]");
                codeGenerator.writeCodeLine("fld dword ptr [esp]");
            }
            else {
                codeGenerator.writeCodeLine("fld dword ptr [esp]");
                codeGenerator.writeCodeLine("fld dword ptr [esp+4]");
            }
            codeGenerator.writeCodeLine("pop eax");
            codeGenerator.writeCodeLine("pop eax");
            switch(opKind){
                case PLUS:
                    codeGenerator.writeCodeLine("fadd");
                    break;
                case MINUS:
                    codeGenerator.writeCodeLine("fsub");
                    break;
                case MUL:
                    codeGenerator.writeCodeLine("fmul");
                    break;
                case DIV:
                    codeGenerator.writeCodeLine("fdiv");
                    break;
                case LT:
                    codeGenerator.writeCodeLine("fcomi st(0), st(1)");
                    codeGenerator.writeCodeLine("mov eax, 0");
                    codeGenerator.writeCodeLine("setb al");
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
                case LE:
                    codeGenerator.writeCodeLine("fcomi st(0), st(1)");
                    codeGenerator.writeCodeLine("mov eax, 0");
                    codeGenerator.writeCodeLine("setna al");
                    break;
                case GT:
                    codeGenerator.writeCodeLine("fcomi st(0), st(1)");
                    codeGenerator.writeCodeLine("mov eax, 0");
                    codeGenerator.writeCodeLine("seta al");
                    break;
                case GE:
                    codeGenerator.writeCodeLine("fcomi st(0), st(1)");
                    codeGenerator.writeCodeLine("mov eax, 0");
                    codeGenerator.writeCodeLine("setnb eax");
                    break;
                case EQUAL:
                    codeGenerator.writeCodeLine("fcomi st(0), st(1)");
                    codeGenerator.writeCodeLine("mov eax, 0");
                    codeGenerator.writeCodeLine("sete al");
                    break;
                case UNEQUAL:
                    codeGenerator.writeCodeLine("fcomi st(0), st(1)");
                    codeGenerator.writeCodeLine("mov eax, 0");
                    codeGenerator.writeCodeLine("setne al");
                    break;
            }
            if (opKind == OpKind.PLUS || opKind == OpKind.MINUS || opKind == OpKind.MUL || opKind == OpKind.DIV) {
                // 当执行的是算术指令时，将结果从浮点寄存器栈弹出到eax
                codeGenerator.writeCodeLine("sub esp,4");
                codeGenerator.writeCodeLine("fstp dword ptr [esp]");
                codeGenerator.writeCodeLine("pop eax");
            }
        }
    }
}
