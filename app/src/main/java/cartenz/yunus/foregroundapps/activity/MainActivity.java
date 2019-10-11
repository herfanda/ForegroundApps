package cartenz.yunus.foregroundapps.activity;


import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cartenz.yunus.foregroundapps.adapter.ImageListAdapter;
import cartenz.yunus.foregroundapps.model.ImageModel;
import cartenz.yunus.foregroundapps.service.ForegroundService;
import cartenz.yunus.foregroundapps.R;
import cartenz.yunus.foregroundapps.service.RestarterBroadCastReceiver;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    private Button btnStartService;
    private Button btnStopService;

    private static final int REQUEST_CODE = 100;
    private static String STORE_DIRECTORY;
    private static final String SCREENCAP_NAME = "SCREENCAPTURE";
    private static final int VIRTUAL_DISPLAY_FLAGS = DisplayManager.VIRTUAL_DISPLAY_FLAG_OWN_CONTENT_ONLY | DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC;
    private static MediaProjection sMediaProjection;

    private MediaProjectionManager mProjectionManager;
    private static int IMAGES_PRODUCED;

    private ImageReader mImageReader;
    private Display mDisplay;
    private VirtualDisplay mVirtualDisplay;
    private Handler mHandler;
    private int mDensity;
    private int mWidth;
    private int mHeight;
    private int mRotation;
    private OrientationChangeCallback mOrientationChangeCallback;
    private ForegroundService foregroundService;
    private boolean isActiveAgain;

    private RecyclerView rvImage;

    private File[] listFile;

    private ArrayList imgList;

    private FileOutputStream outputStream;

    private String imageName;

    private List<ImageModel> imageModelList;


    Context ctx;

    public Context context() {
        return ctx;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // create directory
        STORE_DIRECTORY =  Environment.getExternalStorageDirectory().toString() + "/CARTENZ/";

        initLayout();
        initEvent();

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},00);
        // call for the projection manager
        mProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);


        startService();

        if (foregroundService == null){
            foregroundService = new ForegroundService(this);
        }


        initData();

    }

    private void initFirebase(){
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>(){

                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()){

                            Toast.makeText(getApplicationContext(),"Task not success",Toast.LENGTH_LONG).show();

                            return;
                        }

                        String token = task.getResult().getToken();
                        //String msg = getString(R.string.fcm_token, token);
                        String msg = getResources().getString(R.string.fcm_token) + token;
                        Log.d(TAG, msg);
                    }
                });

    }

    private void initLayout(){
        ctx = this;
        mHandler = new Handler();

        isActiveAgain = getIntent().getBooleanExtra("ACTIVE_AGAIN",false);

        /*btnStartService = findViewById(R.id.buttonStartService);
        btnStopService = findViewById(R.id.buttonStopService);*/

        rvImage = findViewById(R.id.img_recyclerview);
        rvImage.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rvImage.setLayoutManager(layoutManager);


        String alarm = Context.ALARM_SERVICE;
        AlarmManager alarmManager = (AlarmManager) getSystemService(alarm);

        Intent intent = new Intent("REFRESH_THIS");

        PendingIntent pi = PendingIntent.getBroadcast(this, 123456789, intent, 0);

        int type = AlarmManager.RTC_WAKEUP;
        long interval = 1000 * 50;

        alarmManager.setInexactRepeating(type, System.currentTimeMillis(), interval, pi);
    }

    private void initEvent(){
       /* btnStartService.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {

                startService();
            }
        });

        btnStopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService();
            }
        });*/

    }

    private void initData(){
        prepareData();

        if (prepareData() != null){
            ImageListAdapter adapter = new ImageListAdapter(this,prepareData());
            adapter.notifyDataSetChanged();
            rvImage.setAdapter(adapter);
        }
    }

    private ArrayList prepareData(){
        ArrayList imagelist = new ArrayList();

        if (STORE_DIRECTORY != null){
            File file = new File(STORE_DIRECTORY);
            ImageModel imageModel = new ImageModel();
            if (file.isDirectory() && file.exists()){
                listFile = file.listFiles();
                for (int i = 0; i < listFile.length; i++){
                    imagelist.add(listFile[i].getAbsolutePath());
                    //imageModel.setImages(imagelist);
                }
            }
        }

        return imagelist;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void startService(){
        // start capture handling thread

        Toast.makeText(getApplicationContext(),"MyServiceIsRunning",Toast.LENGTH_SHORT).show();

        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                mHandler = new Handler();
                Looper.loop();

            }
        }.start();

        Intent startIntent = new Intent(this, ForegroundService.class);
        startIntent.setAction("AKTIF");

        ContextCompat.startForegroundService(this, startIntent);

        startActivityForResult(mProjectionManager.createScreenCaptureIntent(), REQUEST_CODE);

    }

    private void stopService(){

        mHandler.post(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                if (sMediaProjection != null) {
                    sMediaProjection.stop();
                }
            }
        });


        Intent stopIntent = new Intent(this, ForegroundService.class);
        stopService(stopIntent);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == REQUEST_CODE){
            sMediaProjection = mProjectionManager.getMediaProjection(resultCode, data);

            DisplayMetrics metrics = getResources().getDisplayMetrics();
            mDensity = metrics.densityDpi;
            mDisplay = getWindowManager().getDefaultDisplay();

            // create virtual display depending on device width / height
            createVirtualDisplay();

            // register orientation change callback
            mOrientationChangeCallback = new OrientationChangeCallback(this);
            if (mOrientationChangeCallback.canDetectOrientation()) {
                mOrientationChangeCallback.enable();
            }

            // register media projection stop callback
            sMediaProjection.registerCallback(new MediaProjectionStopCallback(), mHandler);

        }


        super.onActivityResult(requestCode, resultCode, data);
    }


    /****************************************** Factoring Virtual Display creation ****************/

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void createVirtualDisplay(){

        // get Width and Height
        Point size = new Point();
        mDisplay.getSize(size);

        mWidth = size.x;
        mHeight = size.y;

        // start capture reader
        mImageReader = ImageReader.newInstance(mWidth, mHeight, PixelFormat.RGBA_8888, 2);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mVirtualDisplay = sMediaProjection.createVirtualDisplay(SCREENCAP_NAME, mWidth, mHeight, mDensity, VIRTUAL_DISPLAY_FLAGS, mImageReader.getSurface(), null, mHandler);
        }
        mImageReader.setOnImageAvailableListener(new ImageAvailableListener(), mHandler);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private class ImageAvailableListener implements ImageReader.OnImageAvailableListener{

        //private FileOutputStream outputStream;
        private Bitmap bitmap;
        private Image image;

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onImageAvailable(final ImageReader reader) {

            Handler handler = new Handler();
            int delay = 1000; //milisecond;

            if (IMAGES_PRODUCED < 100){

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            image = reader.acquireLatestImage();
                            if (image != null){
                                Image.Plane[] planes = image.getPlanes();
                                ByteBuffer buffer = planes[0].getBuffer();
                                int pixelStride = planes[0].getPixelStride();
                                int rowStride = planes[0].getRowStride();
                                int rowPadding = rowStride - pixelStride * mWidth;


                                View view = getWindow().getDecorView().getRootView();
                                view.setDrawingCacheEnabled(true);

                                // create bitmap
                                bitmap = Bitmap.createBitmap(mWidth + rowPadding / pixelStride, mHeight, Bitmap.Config.ARGB_8888);
                                bitmap.copyPixelsFromBuffer(buffer);
                                view.setDrawingCacheEnabled(false);

                                outputStream = new FileOutputStream(STORE_DIRECTORY + "/CAPTURE_" + IMAGES_PRODUCED + ".png");
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                                outputStream.flush();
                                outputStream.close();

                                imageName = outputStream.toString();
                                Log.i(TAG, "IMAGE NAME " + imageName);

                                IMAGES_PRODUCED++;

                                Log.e(TAG, "captured image: " + IMAGES_PRODUCED);
                            }


                        }catch (Exception e){
                            e.printStackTrace();
                        }finally {
                            if (outputStream != null) {
                                try {
                                    outputStream.close();
                                } catch (IOException ioe) {
                                    ioe.printStackTrace();
                                }
                            }

                            if (bitmap != null) {
                                bitmap.recycle();
                            }

                            if (image != null) {
                                image.close();
                            }
                        }
                    }
                },delay);

            } else {
                IMAGES_PRODUCED = 0;
                createVirtualDisplay();
            }

        }
    }


    private class OrientationChangeCallback extends OrientationEventListener {

        OrientationChangeCallback(Context context) {
            super(context);
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onOrientationChanged(int orientation) {
            final int rotation = mDisplay.getRotation();
            if (rotation != mRotation) {
                mRotation = rotation;
                try {
                    // clean up
                    if (mVirtualDisplay != null) mVirtualDisplay.release();
                    if (mImageReader != null) mImageReader.setOnImageAvailableListener(null, null);

                    // re-create virtual display depending on device width / height
                    createVirtualDisplay();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private class MediaProjectionStopCallback extends MediaProjection.Callback {
        @Override
        public void onStop() {
            Log.e("ScreenCapture", "stopping projection.");
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mVirtualDisplay != null) mVirtualDisplay.release();
                    if (mImageReader != null) mImageReader.setOnImageAvailableListener(null, null);
                    if (mOrientationChangeCallback != null) mOrientationChangeCallback.disable();
                    sMediaProjection.unregisterCallback(MediaProjectionStopCallback.this);
                }
            });

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent broadcastIntent = new Intent(this, RestarterBroadCastReceiver.class);
        sendBroadcast(broadcastIntent);
        Log.i("MAINACT", "READY STEADY!");
        Log.i("MAINACT", "onDestroy!");
        stopService();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }

    @Override
    protected void onStop() {
        super.onStop();

        //stopService();
    }

    private static String getDateFromUri(Uri uri){
        String[] split = uri.getPath().split("/");
        String fileName = split[split.length - 1];
        String fileNameNoExt = fileName.split("\\.")[0];
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = format.format(new Date(Long.parseLong(fileNameNoExt)));
        return dateString;
    }


}
