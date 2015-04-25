package tree;
import java.util.ArrayList;

public class DirNode extends TreeNode{
  public ArrayList<TreeNode> children;
  
  public DirNode(String text, ArrayList<TreeNode> children) {
    super(text);
    this.children = children;
  }
  
}
