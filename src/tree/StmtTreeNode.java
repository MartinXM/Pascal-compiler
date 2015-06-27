package tree;

/**
 * Created by kehanyang on 15/6/28.
 */
public class StmtTreeNode extends TreeNode {

    private StmtKind stmtKind;

    public StmtTreeNode() {
    }

    public StmtTreeNode(StmtKind stmtKind) {
        this.stmtKind = stmtKind;
    }

    public StmtKind getStmtKind() {
        return stmtKind;
    }

    public void setStmtKind(StmtKind stmtKind) {
        this.stmtKind = stmtKind;
    }
}
