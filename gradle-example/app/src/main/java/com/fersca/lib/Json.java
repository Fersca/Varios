package com.fersca.lib;

import static com.fersca.lib.HttpCli.json;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Fernando.Scasserra
 */

public class Json {

    Map<String, Object> internalJson;

    public Json(){
        internalJson = new HashMap<>();
    }
    public Json(Map<String, Object> jsonArg){
        internalJson = jsonArg;
    }
    public Json(String jsonArg){
        internalJson = json(jsonArg);
    }        
    public String s(String field){
        return (String)internalJson.get(field);
    }
    public Boolean b(String field){
        return (Boolean)internalJson.get(field);
    }
    public Double d(String field){
        return (Double)internalJson.get(field);
    }        
    public Set<String> keySet(){
        return internalJson.keySet();
    }
    public void put(String field, Object value){
        internalJson.put(field, value);
    }
    public void remove(String field){
        internalJson.remove(field);
    } 
    public boolean containsKey(String key){
        return internalJson.containsKey(key);
    }
    public Map<String, Object> getMap(){
        return internalJson;
    }
    public Object getObject(String key){
        return internalJson.get(key);
    }
    
    @SuppressWarnings("unchecked")
    public Json j(String field){
        var j = internalJson.get(field);
        if (j!=null)
            return new Json((Map<String, Object>)internalJson.get(field));
        else 
            return null;
    }    

    /*
    public ArrayList<Json> ja(String field){
        
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> list = (List<Map<String, Object>>)json.get(field);  
        
        ArrayList<Json> l = new ArrayList<>();
        
        for (Map<String, Object> e : list){
            Json j = new Json(e);
            l.add(j);
        }
        
        return l;
    }                        
    */
    @SuppressWarnings("unchecked")
    public List<String> sa(String field){        
        return (List<String>)internalJson.get(field);  
    }                        

}
