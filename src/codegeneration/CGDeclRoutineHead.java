package codegeneration;

import symbol.Symbol;
import tree.TreeNode;

/**
 * Created by kehanyang on 15/6/28.
 */
public class CGDeclRoutineHead extends Generator {

    CGDeclRoutineHead(CodeGenerator codeGenerator) {
        super(codeGenerator);
    }

    @Override
    void generateCode(TreeNode node) {
        int sizeParam = Symbol.enterNewScope(node);

        if (node.getChildren().get(3) != null)
            codeGenerator.generateCode(node.getChildren().get(3));

        if (!(node.getAttribute()).equals("main")){
            codeGenerator.writeCodeLine(node.getAttribute() + ":");
        }
        else {
            codeGenerator.writeCodeLine("main PROC");
            codeGenerator.writeCodeLine("mov ecx, esp");
        }

        codeGenerator.writeCodeLine("sub esp, " + sizeParam);
    }
}
