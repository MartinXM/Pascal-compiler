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
        stmtGenerators.put(StmtKind.IF, new CGStmtIf(this));

        Map<Object, Generator> expGenerators = new HashMap<>();
        expGenerators.put(ExpKind.ID, new CGExpId(this));
        expGenerators.put(ExpKind.CONST, new CGExpConst(this));
        expGenerators.put(ExpKind.OP, new CGExpOp(this));

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
        generate(node, "out.asm");
    }

    void writeDataLine(String line) {
        dataSegment.append(line).append('\n');
    }

    void writeCodeLine(String line) {
        codeSegment.append(line).append('\n');
    }

    public void generate(TreeNode node, String fileName) {
        beforeGC(node);
        generateCode(node);
        afterGC();
        writeFinalFile(fileName);
    }

    private void beforeGC(TreeNode node) {
        writeDataLine(".386");
        writeDataLine(".model flat,stdcall");
        writeDataLine("option casemap:none");
        writeDataLine("include masm32\\include\\msvcrt.inc");
        writeDataLine("includelib msvcrt.lib");

        writeDataLine("printf  proto C:dword,:dword");

        writeDataLine(".data");
        writeDataLine("lb_write_int db '%d',0");
        writeDataLine("lb_writeln_int db '%d',0ah,0dh,0");
        writeDataLine("lb_write_real db '%lf',0");
        writeDataLine("lb_writeln_real db '%lf',0ah,0dh,0");
        writeDataLine("lb_tmp db 0, 0, 0, 0, 0, 0, 0, 0");
        writeDataLine("lb_read_int db '%d',0");
        writeDataLine("lb_read_real db '%f',0");

        writeCodeLine(".code");

//        initScope();
        node.setAttribute("main");
    }

    private void afterGC() {
//        int paraSize = leaveScope();
//        writeCodeLine("add esp, " + paraSize);
        writeCodeLine("ret");
        writeCodeLine("main ENDP");
        writeCodeLine("END main");
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

    protected void generateCode(TreeNode node) {
        if (node == null) {
            warning(node.getLineNumber(), "Null node.");
            return;
        }
        generators.get(node.getKind().getClass()).get(node.getKind()).generateCode(node);
        generateCode(node.getSibling());
    }

    public void error(int lineNumber, String message) {
        System.out.println("Code generation error(line " + lineNumber + "): " + message);
        System.exit(1);
    }

    public void warning(int lineNumber, String message) {
        System.out.println("Code generation warning(line " + lineNumber + "): " + message);
    }
}
