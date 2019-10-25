package cartenz.yunus.foregroundapps.helper;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JSONObjectReader {
    private static final JSONObjectReader ourInstance = new JSONObjectReader();

    public static JSONObjectReader getInstance() {
        return ourInstance;
    }

    private JSONObjectReader() {
    }

    public Integer readInteger(JSONObject jsonObject, String attributeName) {
        try {
            Integer attribute = jsonObject.getInt(attributeName);
            return attribute;
        } catch (JSONException e) {
            Log.i(getClass().getCanonicalName(), e.getLocalizedMessage());
            return 0;
        }
    }

    public Double readDouble(JSONObject jsonObject, String attributeName) {
        try {
            Double attribute = jsonObject.getDouble(attributeName);
            return attribute;
        } catch (JSONException e) {
            Log.i(getClass().getCanonicalName(), e.getLocalizedMessage());
            return null;
        }
    }

    public String readString(JSONObject jsonObject, String attributeName) {
        try {
            String attribute = jsonObject.getString(attributeName);
            if(attribute.equals("null")) {
                return null;
            }
            else {
                return attribute;
            }
        } catch (JSONException e) {
            Log.i(getClass().getCanonicalName(), e.getLocalizedMessage());
            return null;
        }
    }

    public Boolean readBoolean(JSONObject jsonObject, String attributeName) {
        try {
            Boolean attribute = jsonObject.getBoolean(attributeName);
            return attribute;
        } catch (JSONException e) {
            Log.i(getClass().getCanonicalName(), e.getLocalizedMessage());
            return false;
        }
    }

    public JSONArray readJsonArray(JSONObject jsonObject, String attributeName) {
        try {
            return jsonObject.getJSONArray(attributeName);
        } catch (JSONException e) {
            Log.i(getClass().getCanonicalName(), e.getLocalizedMessage());
            return null;
        }
    }

    public JSONObject readJsonObject(JSONObject jsonObject, String attributeName) {
        try {
            return jsonObject.getJSONObject(attributeName);
        } catch (JSONException e) {
            Log.i(getClass().getCanonicalName(), e.getLocalizedMessage());
            return null;
        }
    }

    public Map<String, String> toStringMap(JSONObject object) throws JSONException {
        Map<String, String> map = new HashMap<>();

        Iterator<String> keysItr = object.keys();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            String value = (String) object.get(key);

            map.put(key, value);
        }
        return map;
    }

}
