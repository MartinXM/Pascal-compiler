package codegeneration;

import symboltable.SymbolTable;
import symboltable.VariableDef;
import tree.TreeNode;

/**
 * Created by kehanyang on 15/6/28.
 */
public class CGStmtAssign extends Generator {

    CGStmtAssign(CodeGenerator codeGenerator) {
        super(codeGenerator);
    }

    @Override
    void generateCode(TreeNode node) {
        codeGenerator.generateCode(node.getChildren().get(1)); // 计算表达式右边的值
        codeGenerator.writeCodeLine("push eax");
        VariableDef varInfo = SymbolTable.lookupVar((String) node.getChildren().get(0).getAttribute());
        if (varInfo == null) {
            codeGenerator.error(node.getLineNumber(), "undefined variable: " + node.getChildren().get(0).getAttribute());
        }
        if (varInfo.isConst){
            codeGenerator.error(node.getLineNumber(), "Attempt to assign a const variable.");
        }
        codeGenerator.generateCode(node.getChildren().get(0));
        codeGenerator.writeCodeLine("pop eax");
        codeGenerator.writeCodeLine("mov [esi-0], eax; assign");
        node.getChildren().get(0).setRunningType(node.getChildren().get(1).getRunningType());
    }
}
