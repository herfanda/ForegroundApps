package cartenz.yunus.foregroundapps.controller;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLSocketFactory;

import cartenz.yunus.foregroundapps.R;
import cartenz.yunus.foregroundapps.activity.MainActivity;
import cartenz.yunus.foregroundapps.networks.ZeeposAPI;
import cartenz.yunus.foregroundapps.util.Base32;
import cartenz.yunus.foregroundapps.util.Global;
import cartenz.yunus.foregroundapps.util.StorageHelper;
import cartenz.yunus.foregroundapps.util.Utils;
import cartenz.yunus.foregroundapps.util.VolleySingleton;

public class MainActivityController implements MainActivity.ImageHandler {

    private MainActivity activity;

    private static final int threshold = 100000;

    private boolean isSame;

    public float percentage = 0;

    private String firstGenerateID;

    private String secondGenerateID;

    private String generateID;

    private String imgname;

    private String imageString;

    public MainActivityController(MainActivity mainActivity){
        super();
        this.activity = mainActivity;
        initializeVolley();

        if (firstGenerateID == null){
            firstGenerateID = Utils.generateID(Global.LENGTH_VALUE);
            generateID = firstGenerateID;
        } else {
            secondGenerateID = firstGenerateID;
            generateID = secondGenerateID;
        }
    }

    // delete file inside directory
    public void deleteRecursive(File fileDir){

        try {
            FileUtils.deleteDirectory(fileDir);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public float compareImage(File samplePath, File capturePath){

        int samplePixel = Global.DEFAULT_INT;
        int capturePixel = Global.DEFAULT_INT;
        float count = 0;

        int width;
        int height;

        Bitmap sampleBitmap = BitmapFactory.decodeFile(samplePath.getPath());

        Bitmap captureBitmap = BitmapFactory.decodeFile(capturePath.getPath());

        if (sampleBitmap == null|| captureBitmap == null){
            return 0L;
        }


        if (sampleBitmap.getHeight() == captureBitmap.getHeight() ||
        sampleBitmap.getWidth() == captureBitmap.getWidth()){

            isSame = true;

            width = sampleBitmap.getWidth();
            height = sampleBitmap.getHeight();

            float divider = (width * height);

            for (int i = 0; i < width; i++){
                for (int j = 0; j < height; j++){

                    samplePixel = sampleBitmap.getPixel(i,j);

                    capturePixel = captureBitmap.getPixel(i,j);

                    if (samplePixel == capturePixel){
                        count = count + 1;
                    }
                }
            }

            percentage = 100 * count / divider;

            Log.i(activity.TAG,"SAMPLE PIXEL = " +samplePixel);

            Log.i(activity.TAG,"CAPTURE PIXEL = " +capturePixel);

            Log.i(activity.TAG,"PERCENTAGE = " +percentage);

        } else {
            Toast.makeText(activity,"Image size is not same",Toast.LENGTH_SHORT).show();

        }

        return percentage;

    }


    @Override
    public void uploadImage(File file,String name) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        Bitmap uploadImageBitmap = BitmapFactory.decodeFile(file.getPath());

        uploadImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        imageString = Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);

        imgname = name;

        String url = "http://otm.zeepos.com/_api/sc/";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url + generateID,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("     ", "    ");
                Log.i("=== SUCCESS UPLOAD ===", " "+response);
                Log.i("=== IMAGE FILE ===", " "+file.getPath());
                Log.i("=== IMAGE NAME ===", " "+imgname);
                Log.i("     ", "    ");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("FAILED UPLOAD", ""+error.toString());

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> parameters = new HashMap<String, String>();

                parameters.put("file",imgname);
                parameters.put("image",imageString);
                return parameters;
            }
        };

        RequestQueue rQueue = Volley.newRequestQueue(activity);
        rQueue.add(request);



        /*Utils.getInstance().getZeeposAPI(activity).uploadFile(
                file,
                name,
                Utils.getInstance().generateID(Global.LENGTH_VALUE),
                uploadListener(),
                errorListener());*/

    }


    private Response.Listener<JSONObject> uploadListener(){
        return response -> {
            Toast.makeText(activity,"Upload Image Success",Toast.LENGTH_SHORT).show();

            Log.i("SUCCESS UPLOAD", ""+response);

            /*ByteArrayOutputStream stream = new ByteArrayOutputStream();

            Bitmap uploadImageBitmap = BitmapFactory.decodeFile(file.getPath());

            uploadImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

            imageString = Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);

            imgname = name;


            StringRequest request = new StringRequest(Request.Method.POST, getSecureAPIUrl("uploadImage")+generateID, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("SUCCESS UPLOAD", ""+response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("FAILED UPLOAD", ""+error.toString());

                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> parameters = new HashMap<String, String>();

                    parameters.put("file",imgname);
                    parameters.put("image",imageString);
                    return parameters;
                }
            };

            RequestQueue rQueue = Volley.newRequestQueue(context);
            rQueue.add(request);
*/

        };
    }

    private Response.ErrorListener errorListener(){
        return error -> {

        };
    }

    private void initFirebase(){
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>(){

                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()){

                            Toast.makeText(activity,"Task not success",Toast.LENGTH_LONG).show();

                            return;
                        }

                        String token = task.getResult().getToken();
                        //String msg = getString(R.string.fcm_token, token);
                        String msg = activity.getResources().getString(R.string.fcm_token) + token;
                        Log.d(activity.TAG, msg);
                    }
                });

    }

    private void initializeVolley(){

        Network network;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            network = new BasicNetwork(new HurlStack());
        }
        else {
            // Handle legacy Volley stack
            HurlStack hurlStack = new HurlStack(null, new SSLSocketFactory() {
                @Override
                public String[] getDefaultCipherSuites() {
                    return new String[0];
                }

                @Override
                public String[] getSupportedCipherSuites() {
                    return new String[0];
                }

                @Override
                public Socket createSocket(Socket socket, String s, int i, boolean b) throws IOException {
                    return null;
                }

                @Override
                public Socket createSocket(String s, int i) throws IOException, UnknownHostException {
                    return null;
                }

                @Override
                public Socket createSocket(String s, int i, InetAddress inetAddress, int i1) throws IOException, UnknownHostException {
                    return null;
                }

                @Override
                public Socket createSocket(InetAddress inetAddress, int i) throws IOException {
                    return null;
                }

                @Override
                public Socket createSocket(InetAddress inetAddress, int i, InetAddress inetAddress1, int i1) throws IOException {
                    return null;
                }
            });
            network = new BasicNetwork(hurlStack);
        }

        String volleyPath = StorageHelper.getInstance().combinePath(
                new String[] {
                        StorageHelper.getInstance().getCachePath(activity).toString(),
                        "volley"
                }).toString();
        RequestQueue requestQueue = new RequestQueue(new DiskBasedCache(new File(volleyPath)),
                network);
        requestQueue.start();

        VolleySingleton.getInstance().setRequestQueue(requestQueue);

    }

}
