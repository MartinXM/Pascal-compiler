package codegeneration;

import tree.ExpType;
import tree.TreeNode;

/**
 * Created by kehanyang on 15/6/28.
 */
public class CGExpConst extends Generator {

    CGExpConst(CodeGenerator codeGenerator) {
        super(codeGenerator);
    }

    @Override
    void generateCode(TreeNode node) {

        if (node.getType() != ExpType.REAL) {
            int value;
            if (node.getType() == ExpType.CHAR) {
                value = (Character)node.getKind();
            } else {
                value = (Integer)node.getAttribute();
            }
            codeGenerator.writeCodeLine("mov eax, " + value);
        } else {
            double value = (Double)node.getAttribute();
            String realDataLabel = LabelManager.getDataLabel(value, codeGenerator);
            codeGenerator.writeCodeLine("mov eax, " + realDataLabel);
        }
    }
}
