package cartenz.yunus.foregroundapps.networks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


import cartenz.yunus.foregroundapps.helper.JsonFileHelper;
import cartenz.yunus.foregroundapps.networks.requests.UploadImageRequest;
import cartenz.yunus.foregroundapps.networks.requests.VolleyJsonObjectRequest;
import cartenz.yunus.foregroundapps.util.IZeeposAPI;
import cartenz.yunus.foregroundapps.util.Utils;
import cartenz.yunus.foregroundapps.util.VolleySingleton;

public class ZeeposAPI implements IZeeposAPI {

    private Context context;


    public ZeeposAPI(Context context){
        this.context = context;

    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getSecureAPIUrl(String functionName) {
        JSONObject listOfUrls = Utils.getInstance().getZeeposUrl(getContext());

        return JsonFileHelper.getInstance().readString(listOfUrls, functionName);
    }

    @Override
    public void uploadFile(File file,String imageName, String generateID,Response.Listener<JSONObject> successListener, Response.ErrorListener errorListener) {
        UploadImageRequest uploadRequest = new UploadImageRequest();
        String url = getSecureAPIUrl("uploadImage")+generateID;

        uploadRequest.setFile(file);
        uploadRequest.setImage(imageName);

        JSONObject objectParameter = uploadRequest.generateJsonParameter();
        VolleyJsonObjectRequest request = new VolleyJsonObjectRequest(uploadRequest.getMethod(),url,objectParameter,successListener,errorListener);
        VolleySingleton.getInstance().addToRequestQueue(request);

        Log.i("REQUEST UPLOAD FILE ","URL ID VALUE "+url);

    }

}
