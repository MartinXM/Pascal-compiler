package codegeneration;

import symboltable.FuncDef;
import symboltable.SymbolTable;
import tree.TreeNode;

/**
 * Created by kehanyang on 15/6/28.
 */
public class CGExpFuncId extends CGStmtFuncAndProc {

    CGExpFuncId(CodeGenerator codeGenerator) {
        super(codeGenerator);
    }

    @Override
    void generateCode(TreeNode node) {
        FuncDef judgeVar = SymbolTable.lookupFunc((String) node.getAttribute());

        codeGenerator.writeCodeLine("push eax");  // push return value

        CGPushParam(node.getChildren().get(0));


        codeGenerator.writeCodeLine("push ecx");  // save access link
        codeGenerator.writeCodeLine("mov ecx, esp");

        codeGenerator.writeCodeLine("call " + node.getAttribute());

        codeGenerator.writeCodeLine("pop ecx");


        CGPopParam(node.getChildren().get(0), judgeVar.paraList);

        codeGenerator.writeCodeLine("pop eax");  // pop return value
    }
}
