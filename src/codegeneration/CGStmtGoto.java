package codegeneration;

import tree.TreeNode;

/**
 * Created by kehanyang on 15/6/28.
 */
public class CGStmtGoto extends Generator {

    CGStmtGoto(CodeGenerator codeGenerator) {
        super(codeGenerator);
    }

    @Override
    void generateCode(TreeNode node) {
        int value = (Integer)node.getAttribute();
        String label = LabelManager.buildCodeLabel(value);
        codeGenerator.writeCodeLine("jmp " + label);
    }
}
