package cartenz.yunus.foregroundapps.util;


import com.android.volley.Response;

import org.json.JSONObject;

import java.io.File;

public interface IZeeposAPI {

    void uploadFile(File file, String name, String generateID, Response.Listener<JSONObject> successListener, Response.ErrorListener errorListener);


}
