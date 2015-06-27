package codegeneration;

import tree.TreeNode;

/**
 * Created by kehanyang on 15/6/28.
 */
public abstract class Generator {

    CodeGenerator codeGenerator;

    Generator(CodeGenerator codeGenerator) {
        this.codeGenerator = codeGenerator;
    }

    abstract void generateCode(TreeNode node, String fileName);

}
