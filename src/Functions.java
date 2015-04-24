
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

import tree.DirNode;
import tree.FileNode;
import tree.TreeNode;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;


public class Functions {
  
  public ArrayList<TreeNode> getTree(){
    ArrayList<TreeNode> tree;
    String json,file;
    
//    file="en";
    file="light-test";
    
    json=readJSON(file);
    tree=decodeJSON(json);
    return tree;
  }
  
  public String readJSON(String file){
    String doc = "", path;
    BufferedReader br = null;
    int c;

    path="json/"+file+".json";
    
    try {
      br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
      while ((c = br.read()) != -1) {
        doc += (char) c;
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (br != null){
        try {
            br.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    
    return doc;
  }

  public ArrayList decodeJSON(String json){
    ArrayList<TreeNode> tree=new ArrayList<TreeNode>();
    
    JsonParser parser = new JsonParser();
    JsonArray jsonArray = parser.parse(json).getAsJsonArray();
    
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
  
  public ArrayList decodeJSON2(String json){
    ArrayList<TreeNode> tree=new ArrayList<TreeNode>();
    
    Type treeType=new TypeToken<ArrayList<TreeNode>>() {}.getType();
    
    tree=new Gson().fromJson(json, treeType);
    return tree;
  }
  
  public ArrayList decodeJSON1(String json){
    ArrayList tree;
    tree=new Gson().fromJson(json, ArrayList.class);
    return tree;
  }

}
