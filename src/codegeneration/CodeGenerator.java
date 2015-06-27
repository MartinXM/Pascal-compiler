package codegeneration;

import tree.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kehanyang on 15/6/28.
 */
public class CodeGenerator {

    protected static CodeGenerator instance;  // Singleton pattern

    private static Map<Class, Map<Object, Generator> > generators = new HashMap<>();

    protected StringBuilder dataSegment;

    protected StringBuilder codeSegment;

    private CodeGenerator() {

        Map<Object, Generator> stmtGenerators = new HashMap<>();

        Map<Object, Generator> expGenerators = new HashMap<>();
        expGenerators.put(ExpKind.ID, new CGExpId(this));
        expGenerators.put(ExpKind.CONST, new CGExpConst(this));

        Map<Object, Generator> declGenerators = new HashMap<>();

        Map<Object, Generator> typeGenerators = new HashMap<>();

        generators.put(StmtKind.class, stmtGenerators);
        generators.put(ExpKind.class, expGenerators);
        generators.put(DeclKind.class, declGenerators);
        generators.put(TypeKind.class, typeGenerators);
    }

    public static CodeGenerator getCodeGenerator() {  // Singleton pattern
        if (instance == null) {
            instance = new CodeGenerator();
        }
        return instance;
    }

    public void generate(TreeNode node) {
        generateCode(node, "out.asm");
    }

    void writeDataLine(String line) {
        dataSegment.append(line).append('\n');
    }

    void writeCodeLine(String line) {
        codeSegment.append(line).append('\n');
    }

    public void generate(TreeNode node, String fileName) {
        beforeGC();
        generateCode(node, fileName);
        afterGC();
        writeFinalFile(fileName);
    }

    private void beforeGC() {

    }

    private void afterGC() {

    }

    private void writeFinalFile(String fileName) {
        File file = new File(fileName);
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            out.write(dataSegment.toString());
            out.write(codeSegment.toString());
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void generateCode(TreeNode node, String fileName) {
        generators.get(node.getKind().getClass()).get(node.getKind()).generateCode(node, fileName);
    }
}
