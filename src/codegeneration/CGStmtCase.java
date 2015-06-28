package codegeneration;

import tree.TreeNode;

/**
 * Created by kehanyang on 15/6/28.
 */
public class CGStmtCase extends Generator {

    CGStmtCase(CodeGenerator codeGenerator) {
        super(codeGenerator);
    }

    @Override
    void generateCode(TreeNode node) {
        codeGenerator.generateCode(node.getChildren().get(0));
        codeGenerator.writeCodeLine("push eax");
        codeGenerator.generateCode(node.getChildren().get(1));
    }
}
