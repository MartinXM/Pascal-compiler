package codegeneration;

import tree.ExpType;
import tree.OpKind;
import tree.TreeNode;

/**
 * Created by kehanyang on 15/6/28.
 */
public class CGStmtOutput extends Generator {

    CGStmtOutput(CodeGenerator codeGenerator) {
        super(codeGenerator);
    }

    @Override
    void generateCode(TreeNode node) {
        TreeNode child = node.getChildren().get(0);
        while (child != null) {
            codeGenerator.generateCode(child, false);
            codeGenerator.writeCodeLine("pusha");

            if (child.getRunningType() == ExpType.REAL) {
                codeGenerator.writeCodeLine("push eax");
                codeGenerator.writeCodeLine("fld dword ptr [esp]");
                codeGenerator.writeCodeLine("sub esp, 4");
                codeGenerator.writeCodeLine("fstp qword ptr [esp]");
                if (node.getAttribute() == OpKind.WRITELN) {
                    codeGenerator.writeCodeLine("push offset lb_writeln_real");
                } else {
                    codeGenerator.writeCodeLine("push offset lb_write_real");
                }
                codeGenerator.writeCodeLine("call printf");
                codeGenerator.writeCodeLine("add esp, 8");
                codeGenerator.writeCodeLine("pop eax");
            } else if (child.getRunningType() == ExpType.INT) {
                if (node.getAttribute() == OpKind.WRITELN) {
                    codeGenerator.writeCodeLine("invoke printf,offset lb_writeln_int, eax");
                } else {
                    codeGenerator.writeCodeLine("invoke printf,offset lb_write_int, eax");
                }
            } else {
                codeGenerator.error(node.getLineNumber(), "Unknown running type.");
            }

            codeGenerator.writeCodeLine("popa");
            child = child.getSibling();
        }
    }
}
