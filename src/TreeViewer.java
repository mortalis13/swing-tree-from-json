import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

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
  
  class TreeWorker extends SwingWorker<Void, Integer> implements PropertyChangeListener{

    public Void doInBackground() {
      prevTime=System.currentTimeMillis();
      
      treeData=getTree();
      return null;
    }
    
    public void done(){
      if(treeData!=null){
        DirNode root=new DirNode("root", treeData);
        window.tree.setData(root);
        window.scrollPane.setViewportView(window.tree);
      }
    }
    
    public ArrayList<TreeNode> getTree(){
      ArrayList<TreeNode> tree;
      String json,file;
      
      file="en";
      file="light-test";
      file="light-test-formatted";
      file="3-programming";
//      file="3-programming-formatted";
      
      long time1,time2;
      time1=System.currentTimeMillis();
      json=readJSON(file);
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
    
    public String readJSON(String file){
      String doc = "", path; 
      path="json/"+file+".json";
      
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
    
    // time measure tmeplate
    private void showProcessTime(){
      long time1,time2;
      time1=System.currentTimeMillis();
      // json=readJSON(file);
      time2=System.currentTimeMillis();
      System.out.println("Read time: "+formatTime((int) (time2-time1), "%.2f s") );
    }
  }
  
  public TreeViewer(){
    window=TreeViewerFrame.window;
  }
    
  public void showTree(){
    TreeWorker worker=new TreeWorker();
    worker.addPropertyChangeListener(worker);
    worker.execute();
  }

}
