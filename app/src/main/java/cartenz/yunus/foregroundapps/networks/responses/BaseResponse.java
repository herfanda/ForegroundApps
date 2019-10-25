package cartenz.yunus.foregroundapps.networks.responses;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BaseResponse {

    protected Integer status;
    protected String message;
    protected String raw;

    public BaseResponse() {

    }

    public BaseResponse(JSONObject responseObject) {
        raw = responseObject.toString();
        if (responseObject.has("statusCode")) {
            status = responseObject.optInt("statusCode");
        } else {
            status = 200;
        }
        message = responseObject.optString("status");
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        if (getStatus() != null) {
            String responseStatusMessage = ResponseStatus.getPrettyMessage(getStatus());

            if(responseStatusMessage != null) {
                return responseStatusMessage;
            }
            else {
                return message;
            }
        } else {
            return "Unavailable";
        }
    }

    public String getPrettyMessage() {
        String responseStatusMessage = ResponseStatus.getPrettyMessage(getStatus());
        return responseStatusMessage;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResponseText() {
        return ResponseStatus.getPrettyMessage(status);
    }

    public boolean isOk() {
        if (status != null) {
            return status >= 200 && status < 300;
        } else {
            return false;
        }
    }

    protected String readString(JSONObject jsonObject, String attributeName) {
        try {
            String attribute = jsonObject.getString(attributeName);
            return attribute;
        } catch (JSONException e) {
            //LogHelper.getInstance().i(getClass().getCanonicalName(), e.getLocalizedMessage());
            return null;
        }
    }

    protected Integer readInteger(JSONObject jsonObject, String attributeName) {
        try {
            Integer attribute = jsonObject.getInt(attributeName);
            return attribute;
        } catch (JSONException e) {
            //LogHelper.getInstance().i(getClass().getCanonicalName(), e.getLocalizedMessage());
            return null;
        }
    }

    protected Double readDouble(JSONObject jsonObject, String attributeName) {
        try {
            Double attribute = jsonObject.getDouble(attributeName);
            return attribute;
        } catch (JSONException e) {
            //LogHelper.getInstance().i(getClass().getCanonicalName(), e.getLocalizedMessage());
            return null;
        }
    }

    protected Boolean readBoolean(JSONObject jsonObject, String attributeName) {
        try {
            Boolean attribute = jsonObject.getBoolean(attributeName);
            return attribute;
        } catch (JSONException e) {
            //LogHelper.getInstance().i(getClass().getCanonicalName(), e.getLocalizedMessage());
            return null;
        }
    }

    protected JSONArray readJsonArray(JSONObject jsonObject, String attributeName) {
        try {
            return jsonObject.getJSONArray(attributeName);
        } catch (JSONException e) {
            //LogHelper.getInstance().i(getClass().getCanonicalName(), e.getLocalizedMessage());
            return null;
        }
    }

    protected List<String> readListOfString(JSONObject jsonObject, String attributeName) {
        JSONArray array;
        List<String> list = new ArrayList<>();
        try {
            array = jsonObject.getJSONArray(attributeName);
        } catch (JSONException e) {
            //LogHelper.getInstance().i(getClass().getCanonicalName(), e.getLocalizedMessage());
            return null;
        }
        for(int i=0; i<array.length(); i++){
            try {
                list.add(array.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }

    public VolleyError getError() {
        return new VolleyError(getMessage());
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.putOpt("statusCode", getStatus());
            jsonObject.putOpt("status", getMessage());
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return super.toString();
    }


}
