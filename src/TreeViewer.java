import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.SwingWorker;

import tree.DirNode;
import tree.FileNode;
import tree.TreeNode;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class TreeViewer{
  
  private ArrayList<TreeNode> treeData;
  private TreeViewerFrame window;
  
  private long prevTime=0;
  private int totalTime=0;
  
  String rootName;
  String path;
  
  public ArrayList<String> iconNames;
  
  class TreeWorker extends SwingWorker<Void, Integer> implements PropertyChangeListener{

    public Void doInBackground() {
      treeData=getTree();
      return null;
    }
    
    public void done(){
      loadTreeIntoWindow();
    }
    
    public ArrayList<TreeNode> getTree(){
      ArrayList<TreeNode> tree;
      String json,file;
      
      long time1,time2;
      time1=System.currentTimeMillis();
      json=readJSON();
      time2=System.currentTimeMillis();
      System.out.println("Read time: "+formatTime((int) (time2-time1), "%.2f s") );
      
      time1=System.currentTimeMillis();
      tree=decodeJSON(json);
      time2=System.currentTimeMillis();
      System.out.println("Decode time: "+formatTime((int) (time2-time1), "%.2f s") );
      
//      json=readJSON(file);
//      tree=decodeJSON(json);
      
      return tree;
    }
    
    public String readJSON(){
      String doc = "";
      
      try {
        Path filePath = Paths.get(path);
        Charset charset = Charset.forName("UTF-8");
        
        byte[] data = Files.readAllBytes(filePath);
        doc=new String(data, charset);
      } catch (IOException e) {
        e.printStackTrace();
      }
      
      return doc;
    }
    
    public ArrayList<TreeNode> decodeJSON(String json){
      ArrayList<TreeNode> tree;
      JsonArray jsonArray = new JsonParser().parse(json).getAsJsonArray();
      tree=convertJsonToTree(jsonArray);
      return tree;
    }
    
    public ArrayList<TreeNode> convertJsonToTree(JsonArray jsonArray){
      ArrayList<TreeNode> tree=new ArrayList<TreeNode>();
      Gson gson=new Gson();
      
      for(JsonElement jsonElem:jsonArray){
        JsonObject json=(JsonObject) jsonElem;
        boolean isDir=json.has("children");
        
        if(isDir){
          DirNode dir=gson.fromJson(json, DirNode.class);
          tree.add(dir);
          
          JsonArray children=(JsonArray) json.get("children");
          dir.children=convertJsonToTree(children);
        }
        else{
          FileNode file=gson.fromJson(json, FileNode.class);
          tree.add(file);
          iconNames.add(getIconFromNode(file.icon));
        }
      }
      
      return tree;
    }
    
    
    protected void process(List<Integer> list){
    }
    
    public void propertyChange(PropertyChangeEvent evt) {
    }
    
    private void logStats(String currentDir, int progress){
      long currentTime=System.currentTimeMillis();
      int time=(int) (currentTime-prevTime);
      prevTime=currentTime;
      
      String timeString=formatTime(time, "Time: %.2f s");
      totalTime+=time;
    }
    
    private void updateStatusBar(String type, String currentDir){
    }
    
    private String formatTime(int time, String format){
      Formatter timeFormat=new Formatter();
      timeFormat.format(format, (float) time/1000);
      return timeFormat.toString();
    }
    
    // time measure template
    private void showProcessTime(){
      long time1,time2;
      time1=System.currentTimeMillis();
      // json=readJSON(file);
      time2=System.currentTimeMillis();
      System.out.println("Read time: "+formatTime((int) (time2-time1), "%.2f s") );
    }
  }
  
// ----------------------------------------- TreeViewer class methods -----------------------------------------  
  
  public TreeViewer(){
    window=TreeViewerFrame.window;
    iconNames=new ArrayList<String>();
  }
    
  public void showTree(){
    path=window.tfPath.getText();
    TreeWorker worker=new TreeWorker();
    worker.addPropertyChangeListener(worker);
    worker.execute();
  }
  
  public void loadTreeIntoWindow(){
    if(treeData!=null){
      DirectoryTree tree=window.tree;
      
      rootName=getNameFromPath();
      DirNode root=new DirNode(rootName, treeData);
      
      tree=new DirectoryTree(iconNames);
      tree.setData(root);
      tree.setTreeOptions(tree);
      
      window.scrollPane.setViewportView(tree);
    }
  }
  
  public static String getIconFromNode(String iconPath){
    String icon=null;
    
    Pattern pat=Pattern.compile("/([^/]+\\.[^/.]+)$");
    Matcher mat=pat.matcher(iconPath);
    
    if(mat.find()){
      icon=mat.group(1);                                      
    }
    else{
      icon="file.png";
    }
    
    return icon;
  }
  
  public static String formatPath(String path) {
    path=path.replace('\\', '/');
    path=path.trim();
    
    int last=path.length()-1;
    if(path.substring(last).equals("/"))
      path=path.substring(0,last);
    
    return path;
  }
  
  public String getNameFromPath(){
    String name="";
    
    Pattern pat=Pattern.compile("/([^/]+)\\.[^/.]+$");
    Matcher mat=pat.matcher(path);
    
    if(mat.find())
      name=mat.group(1);
    
    return name;
  }
  
  public static String getExt(String file){
    String ext;
    ext=regexFind("\\.([^.]+)$", file, 1);
    ext=ext.toLowerCase();
    return ext;
  }
  
  public static String regexFind(String pattern, String text, int group){
    String result="";
    
    Pattern pat=Pattern.compile(pattern);
    Matcher mat=pat.matcher(text);
    
    if(mat.find())
      result=mat.group(group);
    
    return result;
  }
  
}
