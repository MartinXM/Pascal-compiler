package codegeneration;

import tree.TreeNode;

/**
 * Created by kehanyang on 15/6/28.
 */
public class CGStmtRepeat extends Generator {

    CGStmtRepeat(CodeGenerator codeGenerator) {
        super(codeGenerator);
    }

    @Override
    void generateCode(TreeNode node) {
        String startLabel = LabelManager.createLabel();
        codeGenerator.writeCodeLine(startLabel + ":");

        codeGenerator.generateCode(node.getChildren().get(0));
        codeGenerator.generateCode(node.getChildren().get(1));

        codeGenerator.writeCodeLine("cmp eax, 0");
        codeGenerator.writeCodeLine("je " + startLabel);
    }
}
