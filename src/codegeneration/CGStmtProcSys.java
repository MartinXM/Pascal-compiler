package codegeneration;

import tree.OpKind;
import tree.TreeNode;

/**
 * Created by kehanyang on 15/6/28.
 */
public class CGStmtProcSys extends Generator {

    private static CGStmtInput inputGenerator;

    private static CGStmtOutput outputGenerator;

    CGStmtProcSys(CodeGenerator codeGenerator) {
        super(codeGenerator);
        if (inputGenerator == null) {
            inputGenerator = new CGStmtInput(codeGenerator);
        }
        if (outputGenerator == null) {
            outputGenerator = new CGStmtOutput(codeGenerator);
        }
    }

    @Override
    void generateCode(TreeNode node) {
        if (!(node.getAttribute() instanceof  OpKind)) {
            codeGenerator.error(node.getLineNumber(), "Node type error, expected OpKind, found " + node.getAttribute().getClass() + ".");
        }
        if (node.getAttribute() == OpKind.READ) {
            inputGenerator.generateCode(node);
        } else {
            outputGenerator.generateCode(node);
        }
    }
}
