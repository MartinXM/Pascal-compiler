package tree;

/**
 * Created by kehanyang on 15/6/28.
 */
public class DeclTreeNode extends TreeNode {

    private DeclKind declKind;

    public DeclTreeNode() {
    }

    public DeclTreeNode(DeclKind declKind) {
        this.declKind = declKind;
    }

    public DeclKind getDeclKind() {
        return declKind;
    }

    public void setDeclKind(DeclKind declKind) {
        this.declKind = declKind;
    }
}
