package tree;

public class FileNode extends TreeNode{
  public String icon;
  
  public FileNode(String text, String icon) {
    super(text);
    this.icon=icon;
  }
  
}
