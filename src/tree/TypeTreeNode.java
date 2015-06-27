package tree;

/**
 * Created by kehanyang on 15/6/28.
 */
public class TypeTreeNode extends TreeNode {

    TypeKind typeKind;

    public TypeTreeNode() {
    }

    public TypeTreeNode(TypeKind typeKind) {
        this.typeKind = typeKind;
    }

    public TypeKind getTypeKind() {
        return typeKind;
    }

    public void setTypeKind(TypeKind typeKind) {
        this.typeKind = typeKind;
    }
}
