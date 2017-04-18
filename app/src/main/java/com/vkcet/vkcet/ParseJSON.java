package com.vkcet.vkcet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by SREE on 03-Mar-16.
 */
public class ParseJSON {

    public static String[] ids;
    public static String[] emails;
    public static String[] roles;

    public static final String JSON_ARRAY = "result";
    public static final String KEY_ID = "id";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_ROLE = "role";

    private JSONArray users = null;

    private String json;

    public ParseJSON(String json){
        this.json = json;
    }

    protected void parseJSON(){
        JSONObject jsonObject=null;
        try {
            jsonObject = new JSONObject(json);
            users = jsonObject.getJSONArray(JSON_ARRAY);

            ids = new String[users.length()];
            emails = new String[users.length()];
            roles = new String[users.length()];

            for(int i=0;i<users.length();i++){
                JSONObject jo = users.getJSONObject(i);
                ids[i] = jo.getString(KEY_ID);
                emails[i] = jo.getString(KEY_EMAIL);
                roles[i] = jo.getString(KEY_ROLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
