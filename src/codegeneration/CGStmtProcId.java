package codegeneration;

import symboltable.ProcDef;
import symboltable.SymbolTable;
import tree.TreeNode;

/**
 * Created by kehanyang on 15/6/28.
 */
public class CGStmtProcId extends CGStmtFuncAndProc {

    CGStmtProcId(CodeGenerator codeGenerator) {
        super(codeGenerator);
    }

    @Override
    void generateCode(TreeNode node) {

        ProcDef judgeVar = SymbolTable.lookupProc((String) node.getAttribute());

        CGPushParam(node.getChildren().get(0));

        codeGenerator.writeCodeLine("push ecx");  // save access link
        codeGenerator.writeCodeLine("mov ecx, esp");

        codeGenerator.writeCodeLine("call " + node.getAttribute());

        codeGenerator.writeCodeLine("pop ecx");

        CGPopParam(node.getChildren().get(0), judgeVar.paraList);
    }
}
