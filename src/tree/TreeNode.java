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

    public TreeNode(Object kind, int lineNumber) {
        this.kind = kind;
        this.lineNumber = lineNumber;
        if (kind instanceof ExpKind) {
            this.type = ExpType.VOID;
        }
    }


    /**
     * NewOpExpNode.
     * @param first
     * @param second
     * @param op
     * @param lineNumber
     */
    public TreeNode(TreeNode first, TreeNode second, OpKind op, int lineNumber) {
        this.kind = ExpKind.OP;
        this.attribute = op;
        this.lineNumber = lineNumber;
        this.addChild(first);
        this.addChild(second);
    }

    public static TreeNode newOpExpNode(TreeNode first, TreeNode second, OpKind op, int lineNumber) {
        return new TreeNode(first, second, op, lineNumber);
    }

    /**
     * NewFuncSysExpNode
     * @param op
     * @param child
     * @param lineNumber
     */
    public TreeNode(OpKind op, TreeNode child, int lineNumber) {
        this.kind = ExpKind.FUNC_SYS;
        this.addChild(child);
        this.attribute = op;
        this.lineNumber = lineNumber;
    }

    public static TreeNode newFuncSysExpNode(OpKind op, TreeNode child, int lineNumber) {
        return new TreeNode(op, child, lineNumber);
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
    
    public void printTree(TreeNode tree){
        int i;
        while(tree!=null){
           System.out.println("line number "+" kind       "+
        "  attribute "+" type "+" runningType ");
           System.out.print("     "+tree.getLineNumber());
           System.out.print("       "+tree.getKind());
           System.out.print("  "+tree.getAttribute());
           System.out.print("       "+tree.getType());
           System.out.print("     "+tree.getRunningType()+"\n");
                    
            for(i=0;i<tree.getChildren().size();i++){
              if(tree.getChildren().get(i)==null){
            	  System.out.println("children:"+i+" is null "); 
              }else{
            	  System.out.println("children:"+i);
                  printTree(tree.getChildren().get(i));
              }
            }
            tree=tree.getSibling();
        }
    }
}
