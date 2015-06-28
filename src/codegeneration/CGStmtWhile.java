package codegeneration;

import tree.TreeNode;

/**
 * Created by kehanyang on 15/6/28.
 */
public class CGStmtWhile extends Generator {

    CGStmtWhile(CodeGenerator codeGenerator) {
        super(codeGenerator);
    }

    @Override
    void generateCode(TreeNode node) {
        String startLabel = LabelManager.createLabel();
        String endLabel = LabelManager.createLabel();

        codeGenerator.writeCodeLine(startLabel + ":");

        codeGenerator.generateCode(node.getChildren().get(0));  // while条件

        codeGenerator.writeCodeLine("cmp eax, 0");
        codeGenerator.writeCodeLine("je " + endLabel);

        codeGenerator.generateCode(node.getChildren().get(1));  // 循环体
        codeGenerator.writeCodeLine("jmp " + startLabel);
        codeGenerator.writeCodeLine(endLabel + ":");
    }
}
