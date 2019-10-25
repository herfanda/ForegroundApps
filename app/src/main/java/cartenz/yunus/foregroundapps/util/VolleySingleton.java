package cartenz.yunus.foregroundapps.util;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.Map;

import static android.content.ContentValues.TAG;

public class VolleySingleton {

    private static final int TIMEOUT = 15000;

    private static VolleySingleton ourInstance = new VolleySingleton();
    private RequestQueue requestQueue;

    private RequestQueue mRequestQueue;

    public static VolleySingleton getInstance() {
        return ourInstance;
    }

    private VolleySingleton() {
    }

    public RequestQueue getRequestQueue(final Context context) {
        if(requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            this.requestQueue = Volley.newRequestQueue(context);
        }
        return requestQueue;
    }

    public RequestQueue getRequestQueue() {
        if(requestQueue == null) {
            throw new NullPointerException("Please set the request queue before using the Volley Singleton!");
        }
        return requestQueue;
    }

    public void setRequestQueue(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    /*public RequestQueue getLegacyRequestQueue(final Context context) {
        HurlStack hurlStack = new HurlStack(null, new SSLSocketFactoryCompat());

        return Volley.newRequestQueue(context, hurlStack);
    }*/

    public <T> void addToRequestQueue(Request<T> req, Context context) {
        addToRequestQueueCustomTimeout(req, context, TIMEOUT);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        addToRequestQueueCustomTimeout(req, TIMEOUT);
    }

    public <T> void addToRequestQueueCustomTimeout(Request<T> req, Context context, int customTimeout) {

        Log.i("Request URL " ,req.getUrl());
        try {
            for (Map.Entry<String, String> entry : req.getHeaders().entrySet()) {

                Log.i("Request Header " +entry.getKey() + ": ",entry.getValue());
            }

        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }

        DefaultRetryPolicy currentPolicy = (DefaultRetryPolicy)req.getRetryPolicy();
        DefaultRetryPolicy policy = new DefaultRetryPolicy(customTimeout,
                currentPolicy.getCurrentRetryCount(),
                currentPolicy.getBackoffMultiplier());
        req.setRetryPolicy(policy);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getRequestQueue(context).add(req);
        }
        else {
            //getLegacyRequestQueue(context).add(req);
        }
    }

    public <T> void addToRequestQueueCustomTimeout(Request<T> req, int customTimeout) {
        DefaultRetryPolicy currentPolicy = (DefaultRetryPolicy) req.getRetryPolicy();
        DefaultRetryPolicy policy = new DefaultRetryPolicy(customTimeout,
                currentPolicy.getCurrentRetryCount(),
                currentPolicy.getBackoffMultiplier());
        req.setRetryPolicy(policy);

        getRequestQueue().add(req);
    }

    //androidhive

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    /*public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }*/

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }



}
