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

    Map<String, Object> json;

    public Json(){
        json = new HashMap<>();
    }
    public Json(Map<String, Object> jsonArg){
        json = jsonArg;
    }
    public Json(String jsonArg){
        json = json(jsonArg);
    }        
    public String s(String field){
        return (String)json.get(field);
    }
    public Boolean b(String field){
        return (Boolean)json.get(field);
    }
    public Double d(String field){
        return (Double)json.get(field);
    }        
    public Set<String> keySet(){
        return json.keySet();
    }
    public void put(String field, Object value){
        json.put(field, value);
    }
    public void remove(String field){
        json.remove(field);
    } 
    public boolean containsKey(String key){
        return json.containsKey(key);
    }
    public Map<String, Object> getMap(){
        return json;
    }
    public Object getObject(String key){
        return json.get(key);
    }
    
    @SuppressWarnings("unchecked")
    public Json j(String field){
        var j = json.get(field);
        if (j!=null)
            return new Json((Map<String, Object>)json.get(field));
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
        return (List<String>)json.get(field);  
    }                        

}
