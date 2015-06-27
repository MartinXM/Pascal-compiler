package tree;

/**
 * Created by kehanyang on 15/6/28.
 */
public class ExpTreeNode extends TreeNode {

    private ExpKind expKind;

    public ExpTreeNode() {
    }

    public ExpTreeNode(ExpKind expKind) {
        this.expKind = expKind;
    }

    public ExpKind getExpKind() {
        return expKind;
    }

    public void setExpKind(ExpKind expKind) {
        this.expKind = expKind;
    }
}
