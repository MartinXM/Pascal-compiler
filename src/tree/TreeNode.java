package tree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kehanyang on 15/6/28.
 */
public class TreeNode {

    private List<TreeNode> children = new ArrayList<>();

    private TreeNode sibling;

    private int lineno;

    private ExpType type;

    private ExpType runningType;

    private Object attribute;

    public TreeNode() {
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

    public int getLineno() {
        return lineno;
    }

    public void setLineno(int lineno) {
        this.lineno = lineno;
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

    public Object getAttribute() {
        return attribute;
    }

    public void setAttribute(Object attribute) {
        this.attribute = attribute;
    }
}
