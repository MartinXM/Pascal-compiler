package codegeneration;

import tree.ExpType;
import tree.TreeNode;

/**
 * Created by kehanyang on 15/6/28.
 */
public class CGStmtInput extends Generator {

    CGStmtInput(CodeGenerator codeGenerator) {
        super(codeGenerator);
    }

    @Override
    void generateCode(TreeNode node) {
        if (node.getChildren().size() != 1) {
            codeGenerator.error(node.getLineNumber(), "Wrong input-statement.");
        }
        TreeNode child = node.getChildren().get(0);
        while (child != null) {
            codeGenerator.generateCode(child, false);   // 获取目标变量的地址

            codeGenerator.writeCodeLine("pusha");
            if (child.getType() != ExpType.REAL) {
                codeGenerator.writeCodeLine("invoke crt_scanf, addr lb_read_int, addr lb_tmp");
            } else {
                codeGenerator.writeCodeLine("invoke crt_scanf, addr lb_read_real, addr lb_tmp");
            }
            codeGenerator.writeCodeLine("popa");

            codeGenerator.writeCodeLine("mov eax, dword ptr lb_tmp");
            codeGenerator.writeCodeLine("mov [esi], eax");
            child = child.getSibling();
        }
    }
}
