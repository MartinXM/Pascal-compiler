package codegeneration;

import Symbol.ProcList;
import Symbol.Symbol;
import tree.TreeNode;

/**
 * Created by kehanyang on 15/6/28.
 */
public class CGStmtProc extends CGStmtFuncAndProc {

    CGStmtProc(CodeGenerator codeGenerator) {
        super(codeGenerator);
    }

    @Override
    void generateCode(TreeNode node) {

        ProcList judgeVar = Symbol.procListLookup((String) node.getAttribute());

        CGPushParam(node.getChildren().get(0));

        codeGenerator.writeCodeLine("push ecx");  // save access link
        codeGenerator.writeCodeLine("mov ecx, esp");

        codeGenerator.writeCodeLine("call " + node.getAttribute());

        codeGenerator.writeCodeLine("pop ecx");

        CGPopParam(node.getChildren().get(0), judgeVar.paraList);
    }
}
