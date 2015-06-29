package symboltable;

import tree.ExpType;


public class LookupRet {
    public int totalOff;
    public int jumpLevel;
    public ExpType type;

    public LookupRet(int totalOff, int jumpLevel, ExpType type) {
        this.totalOff = totalOff;
        this.jumpLevel = jumpLevel;
        this.type = type;
    }
}
