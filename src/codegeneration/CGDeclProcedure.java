package codegeneration;

import symboltable.SymbolTable;
import tree.TreeNode;

/**
 * Created by kehanyang on 15/6/28.
 */
public class CGDeclProcedure extends Generator {

    CGDeclProcedure(CodeGenerator codeGenerator) {
        super(codeGenerator);
    }

    @Override
    void generateCode(TreeNode node) {

        TreeNode child1 = node.getChildren().get(0);
        TreeNode child2 = node.getChildren().get(1);
        SymbolTable.addProcOrFunc(child1);

        child2.setAttribute(child1.getAttribute());
        codeGenerator.generateCode(child2);

        int sizeParam = SymbolTable.leaveScope();
        codeGenerator.writeCodeLine("add esp, " + sizeParam);
        codeGenerator.writeCodeLine("ret");
    }
}
