import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;

import tree.TreeNode;


public class DirectoryTree extends JTree {
  
  public void setData(TreeNode data){
    DirectoryModel model=new DirectoryModel(data);
    super.setModel(model);
    
    getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
    
    Icon nodeIcon = null;
    renderer.setLeafIcon(nodeIcon);
    renderer.setClosedIcon(nodeIcon);
    renderer.setOpenIcon(nodeIcon);
    setCellRenderer(renderer);
  }
  
} 
