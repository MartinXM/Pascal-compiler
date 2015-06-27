package tree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kehanyang on 15/6/28.
 */
public class TreeNode {

    private List<TreeNode> children = new ArrayList<>();

    private TreeNode sibling;

    private int lineNumber;

    private Object kind;

    private Object attribute;

    private ExpType type;

    private ExpType runningType;

    public TreeNode() {
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void addChild(TreeNode node) {
        children.add(node);
    }

    public void removeChild(int index) {
        children.remove(index);
    }

    public TreeNode getSibling() {
        return sibling;
    }

    public void setSibling(TreeNode sibling) {
        this.sibling = sibling;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public Object getKind() {
        return kind;
    }

    public void setKind(Object kind) {
        this.kind = kind;
    }

    public Object getAttribute() {
        return attribute;
    }

    public void setAttribute(Object attribute) {
        this.attribute = attribute;
    }

    public ExpType getType() {
        return type;
    }

    public void setType(ExpType type) {
        this.type = type;
    }

    public ExpType getRunningType() {
        return runningType;
    }

    public void setRunningType(ExpType runningType) {
        this.runningType = runningType;
    }
}
