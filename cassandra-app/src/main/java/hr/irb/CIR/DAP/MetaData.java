package hr.irb.CIR.DAP;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.HashMap;
import java.util.Map;

public class MetaData {
    private Map<String,String> metaData = new HashMap<String,String>();
    public MetaData() {}
    public MetaData(String actionValue) {
        this.metaData.put("action",actionValue);
    }
    public String serialize(){
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonObject = objectMapper.createObjectNode();
        for (String key : metaData.keySet()) {
            String value = this.metaData.get(key);
            jsonObject.put(key, value);
        }
        String jsonString = jsonObject.toString();
        return jsonString;
    }
    public void add(String key, String  value){
        this.metaData.put(key, value);
    }
    public String get(String key){
        return this.metaData.get(key);
    }
}
