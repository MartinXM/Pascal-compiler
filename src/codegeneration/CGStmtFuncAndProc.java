package codegeneration;

import symboltable.SimpleType;
import tree.TreeNode;

import java.util.LinkedList;

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

    protected void CGPopParam(TreeNode node, LinkedList<SimpleType> paraList) {

        if (node == null || paraList == null || paraList.size() == 0)
            return;

        for (SimpleType para : paraList) {
            if (node == null) {
                break;
            }
            if (para.isVar) {
                codeGenerator.generateCode(node, false);
                codeGenerator.writeCodeLine("pop eax");
                codeGenerator.writeCodeLine("mov [esi],eax");
            }
            else {
                codeGenerator.writeCodeLine("pop eax");
            }
            node = node.getSibling();
        }

    }
}
