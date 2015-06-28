package codegeneration;

import tree.TreeNode;

/**
 * Created by kehanyang on 15/6/28.
 */
public class CGStmtLabel extends Generator {

    CGStmtLabel(CodeGenerator codeGenerator) {
        super(codeGenerator);
    }

    @Override
    void generateCode(TreeNode node) {
        int value = (Integer)node.getAttribute();
        String label = LabelManager.buildCodeLabel(value);
        codeGenerator.writeCodeLine(label + ":");
        codeGenerator.generateCode(node.getChildren().get(0));  //
    }
}
