package symboltable;

import tree.ExpType;


public class MemLoc {
    public int baseLoc;
    public int offset;

    public MemLoc(int baseLoc, int offset) {
        this.baseLoc = baseLoc;
        this.offset = offset;
    }
}
