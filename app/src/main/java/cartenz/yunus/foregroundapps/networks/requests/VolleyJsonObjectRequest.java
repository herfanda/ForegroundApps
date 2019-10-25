package cartenz.yunus.foregroundapps.networks.requests;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cartenz.yunus.foregroundapps.helper.JSONObjectReader;


public class VolleyJsonObjectRequest extends JsonObjectRequest {

    private String token;
    private JSONObject jsonParameters;
    private long requestSize, startTime, endTime;
    private double LOG_THRESHOLD = 3000;


    public VolleyJsonObjectRequest(int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, generateUrl(method, url, jsonRequest), jsonRequest, listener, errorListener);

        this.jsonParameters = jsonRequest;


    }

    private static String generateUrl(int method, String url, JSONObject jsonRequest) {
        if(method == Method.GET) {
            if(jsonRequest != null) {
                String params = "";
                Iterator<String> keys = jsonRequest.keys();

                while (keys.hasNext()) {
                    String key = keys.next();
                    try {
                        String value = jsonRequest.getString(key);
                        params += key + "=" + value;
                        if(keys.hasNext()) {
                            params += "&";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return url + "?" + params;
            }
            else {
                return url;
            }
        }
        else {
            return url;
        }
    }

    @Override
    public Request<?> setRequestQueue(RequestQueue requestQueue) {
        startTime = System.currentTimeMillis();
        requestSize = getBody().length;
        return super.setRequestQueue(requestQueue);
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        logNetworkSpeed(response);

        return super.parseNetworkResponse(response);
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        endTime = System.currentTimeMillis();
        NetworkResponse networkResponse = volleyError.networkResponse;

        if(networkResponse != null) {
            logNetworkSpeed(networkResponse);

            String body = "";
            //get status code here
            String statusCode = String.valueOf(volleyError.networkResponse.statusCode);
            //get response body and parse with appropriate encoding
            if(volleyError.networkResponse.data!=null) {
                try {
                    body = new String(volleyError.networkResponse.data,"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            //do stuff with the body...
            System.out.println(body);
            VolleyError error = new VolleyError(body);
            return error;
        }
        else {
            logNetworkSpeed(volleyError);
        }

        return super.parseNetworkError(volleyError);
    }

    private void logNetworkSpeed(VolleyError error) {
        String requestUrl = getUrl();
        long networkTime = endTime - startTime;

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("RequestUrl", requestUrl);
            jsonObject.put("RequestParameters", jsonParameters);
            jsonObject.put("RequestSize", requestSize + " bytes");
            jsonObject.put("NetworkTime", networkTime + " ms");
            jsonObject.put("ResponseStatus", error.getLocalizedMessage());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //AppConfiguration.getInstance().getMixpanelApi().track("Network Error", jsonObject);
    }

    private void logNetworkSpeed(NetworkResponse response) {
        String requestUrl = getUrl();
        double responseSize = response.data.length;
        double networkTime = response.networkTimeMs;
        int responseStatus = response.statusCode;

        if(networkTime > LOG_THRESHOLD) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("RequestUrl", requestUrl);
                jsonObject.put("RequestParameters", jsonParameters);
                jsonObject.put("RequestSize", requestSize + " bytes");
                jsonObject.put("ResponseSize", responseSize + " bytes");
                jsonObject.put("NetworkTime", networkTime + " ms");
                jsonObject.put("ResponseStatus", responseStatus);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //AppConfiguration.getInstance().getMixpanelApi().track("Network Slow", jsonObject);
        }
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<>();
        if(this.token != null) {
            headers.put("Authorization", "Bearer " + this.token);
        }
        headers.put("Accept", "application/json");
        return headers;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        try {
            Map<String, String> parameters = JSONObjectReader.getInstance().toStringMap(this.jsonParameters);
            return parameters;
        } catch (JSONException e) {
            e.printStackTrace();
            return super.getParams();
        }
    }

}
