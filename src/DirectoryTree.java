import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;

import tree.TreeNode;


public class DirectoryTree extends JTree {
  ArrayList<String> iconNames;
  
  public DirectoryTree(ArrayList<String> iconNames){
    this.iconNames=iconNames;
  }
  
  public void setData(TreeNode data){
    DirectoryModel model=new DirectoryModel(data);
    super.setModel(model);
  }
  
  public void setTreeOptions(JTree tree){
    DefaultTreeCellRenderer renderer;
    Icon nodeIcon = null;
    
    getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    
    renderer=new DirectoryRenderer(iconNames);
    setCellRenderer(renderer);
    
    setShowsRootHandles(true);
  }
  
} 
