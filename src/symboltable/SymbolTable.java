package symboltable;

import tree.DeclKind;
import tree.ExpType;
import tree.TreeNode;
import tree.TypeKind;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by kehanyang on 15/6/30.
 */
public class SymbolTable {

    private final static int ERROR_RETURN = 0xffff;
    private final static int OFFSET_INC = 4;
    private final static int MAXSIZE = 256;

    private static int currentNestLevel = 0;
    private static int offset = 0;

    private static HashMap<String, LinkedList<VariableDef> > variableDefHashMap = new HashMap<>();

    private static HashMap<String, LinkedList<TypeDef> > typeDefHashMap = new HashMap<>();

    private static HashMap<String, LinkedList<FuncDef> > funcDefHashMap = new HashMap<>();

    private static HashMap<String, LinkedList<ProcDef> > procDefHashMap = new HashMap<>();

    private static int[] totalOffset = new int[MAXSIZE];

    public static int addProcOrFunc(TreeNode headNode) {
        boolean isFunc = (headNode.getKind() == DeclKind.FUNCTIONHEAD);
        String name = (String)headNode.getAttribute();
        int nestLevel = currentNestLevel;
        int paraNestLevel = nestLevel + 1;
        int offset = 4;

        LinkedList<SimpleType> paraList = new LinkedList<>();
        TreeNode paraDeclListNode = headNode.getChildren().get(0);

        while (paraDeclListNode != null) {
            TreeNode nameListNode = paraDeclListNode.getChildren().get(0);
            TreeNode typeNode = paraDeclListNode.getChildren().get(1);
            boolean isVar;
            if (paraDeclListNode.getKind() == DeclKind.VAR_PARA) { // 是传递实参还是形参
                isVar = true;
            } else {
                isVar = false;
            }
            while (nameListNode != null) {
                SimpleType para = new SimpleType((String)nameListNode.getAttribute(), typeNode.getType(), isVar);
                paraList.add(para);
                insertVariable(new VariableDef(para.name, para.type, false, paraNestLevel, null, offset), headNode.getLineNumber());
                offset += OFFSET_INC;
                nameListNode = nameListNode.getSibling();
            }
            paraDeclListNode = paraDeclListNode.getSibling();
        }
        if (paraList.size() == 0) {
            paraList = null;
        }

        if (isFunc) {
            // 插入函数返回值
            ExpType retType = headNode.getChildren().get(1).getType();
            insertVariable(new VariableDef((String)headNode.getAttribute(), retType, false, paraNestLevel, null, offset), headNode.getLineNumber());
            offset += OFFSET_INC;

            // 插入函数
            LinkedList<FuncDef> funcDefs = funcDefHashMap.get(name);
            if (funcDefs == null) {
                funcDefs = new LinkedList<>();
                funcDefHashMap.put(name, funcDefs);
            }
            funcDefs.addFirst(new FuncDef(name, paraList, retType, nestLevel));

        } else {
            LinkedList<ProcDef> procDefs = procDefHashMap.get(name);
            if (procDefs == null) {
                procDefs = new LinkedList<>();
                procDefHashMap.put(name, procDefs);
            }
            procDefs.addFirst(new ProcDef(name, paraList, nestLevel));
        }

        return offset;
    }

    private static void insertVariable(VariableDef variableDef, int lineNumber) {
        String name = variableDef.name;
        LinkedList<VariableDef> definedVar = variableDefHashMap.get(name);
        if (definedVar == null) {
            definedVar = new LinkedList<>();
            variableDefHashMap.put(name, definedVar);
        }
        int nestLevel = variableDef.nestLevel;
        if (definedVar.size() == 0 || nestLevel > definedVar.getFirst().nestLevel) {
            definedVar.addFirst(variableDef);
        } else {
            System.err.println("Redefine variable: " + name + " in line " + lineNumber);
        }
    }

    private static void insertType(TypeDef typeDef, int lineNumber) {
        String name = typeDef.name;
        LinkedList<TypeDef> definedType = typeDefHashMap.get(name);
        if (definedType == null) {
            definedType = new LinkedList<>();
            typeDefHashMap.put(name, definedType);
        }
        int nestLevel = typeDef.nestLevel;
        if (definedType.size() == 0 || nestLevel > definedType.getFirst().nestLevel) {
            definedType.add(typeDef);
        } else {
            System.err.println("Redefine type: " + name + " in line " + lineNumber);
        }
    }

    public static VariableDef lookupVar(String name) {
        LinkedList<VariableDef> varInfos = variableDefHashMap.get(name);
        if (varInfos == null || varInfos.size() == 0) {
            return null;
        } else {
            VariableDef ret = varInfos.getFirst();
            ret.memloc.baseLoc = currentNestLevel - ret.nestLevel;
            return ret;
        }
    }

    public static TypeDef lookupType(String name) {
        LinkedList<TypeDef> typeDefs = typeDefHashMap.get(name);
        if (typeDefs == null || typeDefs.size() == 0) {
            return null;
        } else {
            return typeDefs.getFirst();
        }
    }

    public static ProcDef lookupProc(String name) {
        LinkedList<ProcDef> procDefs = procDefHashMap.get(name);
        if (procDefs == null || procDefs.size() == 0) {
            return null;
        } else {
            return procDefs.getFirst();
        }
    }

    public static FuncDef lookupFunc(String name) {
        LinkedList<FuncDef> funcDefs = funcDefHashMap.get(name);
        if (funcDefs == null || funcDefs.size() == 0) {
            return null;
        } else {
            return funcDefs.getFirst();
        }
    }

    public static LookupRet lookupRecord(String rec, String a) {
        VariableDef l = lookupVar(rec);
        LinkedList<TypeDef> members;
        LookupRet ret = new LookupRet(ERROR_RETURN, ERROR_RETURN, ExpType.VOID);
        if (l.type != ExpType.RECORD || l.pAttr == null) {
            return ret;
        }
        members = ((RecordDef)l.pAttr).ptr;
        int size = members.indexOf(new TypeDef(a));
        if (size >= 0) {
            ret.totalOff = size * OFFSET_INC + l.memloc.offset;
            ret.jumpLevel = currentNestLevel - l.nestLevel;
            ret.type = members.get(size).type;
        }
        return ret;
    }

    public static void initScope() {
        currentNestLevel = -1;
        variableDefHashMap.clear();
        typeDefHashMap.clear();
        funcDefHashMap.clear();
        procDefHashMap.clear();
    }

    public static int enterNewScope(TreeNode t) {
        currentNestLevel += 1;
        totalOffset[currentNestLevel] = buildSymtab(t);
        return totalOffset[currentNestLevel];
    }

    public static int leaveScope() {
        int retValue = totalOffset[currentNestLevel];
        currentNestLevel--;

        for (Iterator<Map.Entry<String, LinkedList<VariableDef> > >  it = variableDefHashMap.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, LinkedList<VariableDef> > entry = it.next();
            LinkedList<VariableDef> variableDefs = entry.getValue();
            while (variableDefs.size() != 0 && variableDefs.getFirst().nestLevel > currentNestLevel) {
                variableDefs.removeFirst();
            }
            if (variableDefs.size() == 0) {
                it.remove();
            }
        }

        for (Iterator<Map.Entry<String, LinkedList<TypeDef> > >  it = typeDefHashMap.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, LinkedList<TypeDef> > entry = it.next();
            LinkedList<TypeDef> typeDefs = entry.getValue();
            while (typeDefs.size() != 0 && typeDefs.getFirst().nestLevel > currentNestLevel) {
                typeDefs.removeFirst();
            }
            if (typeDefs.size() == 0) {
                it.remove();
            }
        }

        return retValue;
    }

    private static int buildSymtab(TreeNode syntaxTree) {
        offset = -4;
        traverse(syntaxTree);
        return -offset;
    }

    private static void traverse(TreeNode t) {
        if (t != null && t.getChildren().size() >= 4) {
            for (int i = 0; i < 4; i++) {
                insertNode(t.getChildren().get(i));
            }
        }
    }

    private static void insertNode(TreeNode t) {
        if (t == null)
            return;
        if (!(t.getKind() instanceof DeclKind)) {
            return;
        }
        int lineNumber = t.getLineNumber();
        switch ((DeclKind)t.getKind()) {
            case CONST: {
                while (t != null) {
                    offset -= OFFSET_INC;
                    insertVariable(new VariableDef((String)t.getAttribute(), t.getType(), true, currentNestLevel, null, offset), lineNumber);
                    t = t.getSibling();
                }
                break;
            }
            case VAR: {
                while (t != null) {
                    TreeNode pname = t.getChildren().get(0);
                    TreeNode ptype = t.getChildren().get(1);
                    while (pname != null) {
                        switch ((TypeKind)ptype.getKind()) {
                            case SIMPLE_ID: {
                                TypeDef l = lookupType((String) ptype.getAttribute());
                                switch (l.type) {
                                    case ARRAY:
                                    case RECORD: {
                                        offset -= l.size;
                                        break;
                                    }
                                    default: {
                                        offset -= OFFSET_INC;
                                        break;
                                    }
                                }
                                insertVariable(new VariableDef((String)pname.getAttribute(), l.type, false, currentNestLevel, l.pAttr, offset), lineNumber);
                                break;
                            }
                            case SIMPLE_ENUM: {
                                LinkedList eptr = new LinkedList();
                                while (ptype != null) {
                                    eptr.add(ptype.getAttribute());
                                    ptype = ptype.getSibling();
                                }
                                offset -= OFFSET_INC;
                                insertVariable(new VariableDef((String)pname.getAttribute(), ExpType.SIMPLE_ENUM, false, currentNestLevel, eptr, offset), lineNumber);

                                break;
                            }
                            case SIMPLE_LIMIT: {
                                SubBoundDef sub = new SubBoundDef(ptype.getChildren().get(0).getType(), ptype.getChildren().get(0).getAttribute(), ptype.getChildren().get(1).getAttribute());
                                offset -= OFFSET_INC;
                                insertVariable(new VariableDef((String)pname.getAttribute(), ExpType.SIMPLE_LIMIT, false, currentNestLevel, sub, offset), lineNumber);
                                break;
                            }
                            case SIMPLE_SYS: {
                                offset -= OFFSET_INC;
                                insertVariable(new VariableDef((String)pname.getAttribute(), ptype.getType(), false, currentNestLevel, null, offset), lineNumber);
                                break;
                            }
                            case ARRAY: {
                                int arraySize = 0;
                                if (ptype.getChildren().get(0).getType() == ExpType.SIMPLE_LIMIT) { // 目前只能处理数组下标为子界的情况
                                    arraySize = (Integer) ptype.getChildren().get(0).getChildren().get(1).getAttribute() - (Integer) ptype.getChildren().get(0).getChildren().get(0).getAttribute() + 1;
                                    ArrayDef pAttr = new ArrayDef(ptype.getChildren().get(1).getType(), ptype.getChildren().get(0).getChildren().get(0).getType(), ptype.getChildren().get(0).getChildren().get(0).getAttribute(), ptype.getChildren().get(0).getChildren().get(1).getAttribute());
                                    offset -= (OFFSET_INC * arraySize);
                                    insertVariable(new VariableDef((String) pname.getAttribute(), ptype.getType(), false, currentNestLevel, pAttr, offset), lineNumber);
                                }
                                break;
                            }
                            case RECORD: { // 暂不支持嵌套record
                                LinkedList<TypeDef> typeDefList = new LinkedList<>();
                                while (ptype != null) {
                                    TreeNode nameNode = ptype.getChildren().get(0);
                                    TreeNode typeNode = ptype.getChildren().get(1);
                                    while (nameNode != null) {
                                        offset -= OFFSET_INC;
                                        typeDefList.add(new TypeDef((String)nameNode.getAttribute(), typeNode.getType(), currentNestLevel, null, OFFSET_INC));
                                        nameNode = nameNode.getSibling();
                                    }
                                }
                                RecordDef r = new RecordDef(RecordType.ANONYMOUS, typeDefList);
                                insertVariable(new VariableDef((String)pname.getAttribute(), ExpType.RECORD, false, currentNestLevel, r, offset), lineNumber);
                                break;
                            }
                            default:
                                break;
                        }
                        pname = pname.getSibling();
                    }
                    t = t.getSibling();
                }
                break;
            }
            case TYPE: {
                while (t != null) {
                    switch ((TypeKind) t.getChildren().get(1).getKind()) {
                        case SIMPLE_ID: {
                            String definedTypeName = (String)t.getChildren().get(1).getAttribute();
                            TypeDef definedType = lookupType(definedTypeName);
                            if (definedType == null) {
                                System.err.println("No such type: " + definedTypeName + " in line " + lineNumber);
                                System.exit(1);
                            }
                            insertType(new TypeDef((String)t.getChildren().get(0).getAttribute(), definedType.type, currentNestLevel, definedType.pAttr, OFFSET_INC), lineNumber);
                            break;
                        }
                        case SIMPLE_ENUM: {
                            TreeNode eList = t.getChildren().get(1).getChildren().get(0);
                            LinkedList eptr = new LinkedList();
                            while (eList != null) {
                                eptr.add(eList.getAttribute());
                                eList = eList.getSibling();
                            }
                            offset -= OFFSET_INC;
                            insertType(new TypeDef((String)t.getChildren().get(0).getAttribute(), ExpType.SIMPLE_ENUM, currentNestLevel, eptr, OFFSET_INC), lineNumber);
                            // (String) t.getChildren().get(0).getAttribute()是这个枚举类型的名字
                            break;
                        }
                        case SIMPLE_LIMIT: {
                            SubBoundDef sub = new SubBoundDef(t.getChildren().get(1).getChildren().get(0).getType(), t.getChildren().get(1).getChildren().get(0).getAttribute(), t.getChildren().get(1).getChildren().get(1).getAttribute());
                            insertType(new TypeDef((String)t.getChildren().get(0).getAttribute(), ExpType.SIMPLE_LIMIT, currentNestLevel, sub, OFFSET_INC), lineNumber);
                            // (String)t.getChildren().get(0).getAttribute()是这个类型的名字
                            break;
                        }
                        case SIMPLE_SYS: {
                            insertType(new TypeDef((String)t.getChildren().get(0).getAttribute(), t.getChildren().get(1).getType(), currentNestLevel, null, OFFSET_INC), lineNumber);
                            break;
                        }
                        case ARRAY: {
                            TreeNode atype = t.getChildren().get(1);
                            if (atype.getChildren().get(0).getType() == ExpType.SIMPLE_LIMIT) { // 目前只能处理数组下标为子界的情况
                                ArrayDef pAttr = new ArrayDef(atype.getChildren().get(1).getType(), atype.getChildren().get(0).getChildren().get(0).getType(), atype.getChildren().get(0).getChildren().get(0).getAttribute(), atype.getChildren().get(0).getChildren().get(1).getAttribute());
                                int size = ((Integer)(atype.getChildren().get(0).getChildren().get(1).getAttribute()) - (Integer) atype.getChildren().get(0).getChildren().get(0).getAttribute() + 1) * OFFSET_INC;
                                insertType(new TypeDef((String)t.getChildren().get(0).getAttribute(), t.getChildren().get(1).getType(), currentNestLevel, pAttr, size), lineNumber);
                            }
                            break;
                        }
                        case RECORD: { // 暂不支持嵌套record
                            TreeNode pname = t.getChildren().get(0);
                            TreeNode ptype = t.getChildren().get(1);
                            LinkedList<TypeDef> typeDefList = new LinkedList<>();
                            int size = 0;
                            while (ptype != null) {
                                TreeNode nameNode = ptype.getChildren().get(0);
                                TreeNode typeNode = ptype.getChildren().get(1);
                                while (nameNode != null) {
                                    size++;
                                    typeDefList.add(new TypeDef((String)nameNode.getAttribute(), typeNode.getType(), currentNestLevel, null, OFFSET_INC));
                                    nameNode = nameNode.getSibling();
                                }
                                ptype = ptype.getSibling();
                            }
                            RecordDef r = new RecordDef(RecordType.DEFINED, typeDefList);
                            insertType(new TypeDef((String)pname.getAttribute(), ExpType.RECORD, currentNestLevel, r, size * OFFSET_INC), lineNumber);
                            break;
                        }
                        default:
                            break;
                    }
                    t = t.getSibling();
                }
                break;
            }
            // function和procedure在代码生成进入到相应节点的时候再插入，不然如果同级的不同函数之间有相同的变量的话，这种冲突是解决不了的
            default:
                break;
        }
    }

}
