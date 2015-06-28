package codegeneration;

import tree.OpKind;
import tree.TreeNode;

/**
 * Created by kehanyang on 15/6/28.
 */
public class CGStmtFor extends Generator {

    CGStmtFor(CodeGenerator codeGenerator) {
        super(codeGenerator);
    }

    @Override
    void generateCode(TreeNode node) {
        if (node.getChildren().size() != 4) {
            codeGenerator.error(node.getLineNumber(), "Wrong for-statement.");
        }

        String startLabel = LabelManager.createLabel();
        String endLabel = LabelManager.createLabel();

        codeGenerator.generateCode(node.getChildren().get(1));  // 计算初值
        codeGenerator.writeCodeLine("push eax");
        codeGenerator.generateCode(node.getChildren().get(0));  // 计算循环变量地址和值（这里有用的只是地址，经过这一步之后，esi中存储了循环变量的地址
        codeGenerator.writeCodeLine("pop eax");
        codeGenerator.writeCodeLine("mov [esi-0], eax");    // 给循环变量赋初值

        OpKind opKind = (OpKind)node.getAttribute();
        codeGenerator.writeCodeLine(startLabel + ":");
        codeGenerator.generateCode(node.getChildren().get(2));  // 计算终值
        codeGenerator.writeCodeLine("push eax");
        codeGenerator.generateCode(node.getChildren().get(0));  // 获取循环变量
        codeGenerator.writeCodeLine("pop ebx");
        codeGenerator.writeCodeLine("cmp eax, ebx");    // eax为循环变量的值，ebx是终值，进行比较
        if (opKind == OpKind.TO) {
            codeGenerator.writeCodeLine("ja " + endLabel);
        } else if (opKind == OpKind.DOWNTO) {
            codeGenerator.writeCodeLine("jb " + endLabel);
        } else {
            codeGenerator.error(node.getLineNumber(), "Unspported for-statement.");
        }

        codeGenerator.generateCode(node.getChildren().get(3));  // 循环体

        // 循环变量自增(for-to)/自减(for-downto)
        codeGenerator.generateCode(node.getChildren().get(0));
        if (opKind == OpKind.TO) {
            codeGenerator.writeCodeLine("inc eax");
        } else {
            // opKind == OpKind.DOWNTO
            codeGenerator.writeCodeLine("dec eax");
        }
        codeGenerator.writeCodeLine("mov [esi-0], eax");    // 将自增/自减之后的值赋值回给循环变量
        codeGenerator.writeCodeLine("jmp " + startLabel);
        codeGenerator.writeCodeLine(endLabel + ":");
    }
}
