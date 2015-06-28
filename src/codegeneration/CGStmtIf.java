package codegeneration;

import tree.TreeNode;

/**
 * Created by kehanyang on 15/6/28.
 */
public class CGStmtIf extends Generator {

    CGStmtIf(CodeGenerator codeGenerator) {
        super(codeGenerator);
    }

    @Override
    void generateCode(TreeNode node) {
        if (node.getChildren().size() < 2 || node.getChildren().size() > 3) {
            codeGenerator.error(node.getLineNumber(), "Wrong if-statement.");
        }
        String elseLabel = LabelManager.createLabel();
        String exitLabel = LabelManager.createLabel();
        codeGenerator.generateCode(node.getChildren().get(0));
        codeGenerator.writeCodeLine("cmp eax, 0");
        codeGenerator.writeCodeLine("je " + elseLabel);
        codeGenerator.generateCode(node.getChildren().get(1));
        codeGenerator.writeCodeLine("jmp " + exitLabel);
        codeGenerator.writeCodeLine(elseLabel + ":");
        if (node.getChildren().size() == 3) {
            codeGenerator.generateCode(node.getChildren().get(2));
        }
        codeGenerator.writeCodeLine(exitLabel + ":");
    }
}
