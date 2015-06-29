package codegeneration;

import symboltable.ArrayDef;
import symboltable.LookupRet;
import symboltable.SymbolTable;
import symboltable.VariableDef;
import tree.ExpType;
import tree.TreeNode;

import java.util.LinkedList;

/**
 * Created by kehanyang on 15/6/28.
 */
public class CGExpId extends Generator {

    CGExpId(CodeGenerator codeGenerator) {
        super(codeGenerator);
    }

    @Override
    void generateCode(TreeNode node) {

        int level;
        int offset;
        int lower = 0;
        ExpType type;
        LookupRet recordInfo = null;

        VariableDef varInfo = SymbolTable.lookupVar((String) node.getAttribute());
        if (varInfo == null) {
            codeGenerator.error(node.getLineNumber(), "Variavle " + node.getAttribute() + " not exist.");
        }

        type = varInfo.type;

        if (type == ExpType.ARRAY) {

            if (varInfo.pAttr == null) {
                codeGenerator.error(node.getLineNumber(), "Variable: " + node.getAttribute() + " is not an array.");
            }

            lower = (Integer)(((ArrayDef)varInfo.pAttr).subBound).LowerBound;
            level = varInfo.memloc.baseLoc;
            offset = varInfo.memloc.offset;

        } else if (type == ExpType.RECORD) {
            recordInfo = SymbolTable.lookupRecord((String)node.getAttribute(), (String)(node.getChildren().get(0).getAttribute()));
            level = recordInfo.jumpLevel;
            offset = recordInfo.totalOff;
        } else {
            level = varInfo.memloc.baseLoc;
            offset = varInfo.memloc.offset;
        }

        if (type == ExpType.ARRAY) {
            codeGenerator.generateCode(node.getChildren().get(0), false); 	// 计算数组下标的值
            codeGenerator.writeCodeLine("mov ebx, " + lower);

            codeGenerator.writeCodeLine("sub eax, ebx"); 	// 下标与下界相减，得到相对偏移量
            codeGenerator.writeCodeLine("mov ebx, 4");		// 这里默认一个元素是4个字节，实际如果遇到record的数组会有问题，可以根据符号表中记录信息来计算有几个字节，或者符号表里面直接记录字节数
            codeGenerator.writeCodeLine("imul eax,ebx"); 	// 乘以元素大小，得到真正的相对数组基址的偏移量
            codeGenerator.writeCodeLine("mov edi, " + offset);  // offset中为数组基址相对于函数fp的偏移量
            codeGenerator.writeCodeLine("add edi, eax");	// 想家得到要访问的元素相对于函数fp的偏移量
        }
        else {
            codeGenerator.writeCodeLine("mov edi, " + offset);
        }

        if (type == ExpType.ARRAY) {
            type = ((ArrayDef)varInfo.pAttr).arrayType;
        } else if (type == ExpType.RECORD) {
            type = recordInfo.type;
        }

        codeGenerator.writeCodeLine("mov esi, ecx");
        while (level > 0){
            codeGenerator.writeCodeLine("mov eax, [esi]"); // 根据访问控制链模型向上层查找，[esi]处存放了其对应函数的fp
            codeGenerator.writeCodeLine("mov esi, eax");
            level--;
        }
        codeGenerator.writeCodeLine("add esi, edi");


        if (type == ExpType.INT || type == ExpType.CHAR || type == ExpType.BOOL){
            codeGenerator.writeCodeLine("mov eax, [esi]; calculate ExpId ");
            node.setRunningType(ExpType.INT);
        }
        else if (type == ExpType.REAL){
            codeGenerator.writeCodeLine("mov eax, dword ptr [esi]; calculate ExpId(type: real)");
            node.setRunningType(ExpType.REAL);
        }
    }
}
