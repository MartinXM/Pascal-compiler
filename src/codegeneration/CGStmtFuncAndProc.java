package codegeneration;

import symbol.SimpleTypeList;
import tree.TreeNode;

/**
 * Created by kehanyang on 15/6/28.
 */
public abstract class CGStmtFuncAndProc extends Generator {

    CGStmtFuncAndProc(CodeGenerator codeGenerator) {
        super(codeGenerator);
    }

    protected void CGPushParam(TreeNode node) {
        if (node.getSibling() != null) {
            CGPushParam(node.getSibling());
        }

        codeGenerator.generateCode(node, false);
        codeGenerator.writeCodeLine("push eax");
    }

    protected void CGPopParam(TreeNode node, SimpleTypeList paraList) {

        if (node == null || paraList == null)
            return;

        if (paraList.isVar){
            codeGenerator.generateCode(node, false);
            codeGenerator.writeCodeLine("pop eax");
            codeGenerator.writeCodeLine("mov [esi],eax");
        }
        else {
            codeGenerator.writeCodeLine("pop eax");
        }

        CGPopParam(node.getSibling(), paraList.next);
    }
}
