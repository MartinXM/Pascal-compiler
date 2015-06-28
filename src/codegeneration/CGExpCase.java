package codegeneration;

import tree.TreeNode;

/**
 * Created by kehanyang on 15/6/28.
 */
public class CGExpCase extends Generator {

    CGExpCase(CodeGenerator codeGenerator) {
        super(codeGenerator);
    }

    @Override
    void generateCode(TreeNode node) {
        String nextCaseLabel = LabelManager.createLabel();

        codeGenerator.generateCode(node.getChildren().get(0));

        codeGenerator.writeCodeLine("pop ebx");
        codeGenerator.writeCodeLine("cmp ebx, eax");

        codeGenerator.writeCodeLine("jne " + nextCaseLabel);

        codeGenerator.generateCode(node.getChildren().get(1));

        codeGenerator.writeCodeLine(nextCaseLabel + ":");

        if (node.getSibling() != null) {
            codeGenerator.writeCodeLine("push ebx"); // 在上层(codeGenerator.generateCode())中会对其兄弟进行遍历
        }
    }
}
