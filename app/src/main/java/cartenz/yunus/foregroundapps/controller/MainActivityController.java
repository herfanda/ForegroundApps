package cartenz.yunus.foregroundapps.controller;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;

import cartenz.yunus.foregroundapps.R;
import cartenz.yunus.foregroundapps.activity.MainActivity;
import cartenz.yunus.foregroundapps.util.Base32;
import cartenz.yunus.foregroundapps.util.Global;

public class MainActivityController implements MainActivity.ImageHandler {

    private MainActivity activity;

    private static final int threshold = 100000;

    private boolean isSame;

    public float percentage = 0;

    public MainActivityController(MainActivity mainActivity){
        super();
        this.activity = mainActivity;
    }

    // delete file inside directory
    public void deleteRecursive(File fileDir){

        try {
            FileUtils.deleteDirectory(fileDir);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String generateRandomID(int length){
        SecureRandom secureRandom = new SecureRandom();
        byte[] token = new byte[length];
        secureRandom.nextBytes(token);
        return new Base32().encode(token);
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

            Log.i(activity.TAG,"WIDTH = " +width);
            Log.i(activity.TAG,"HEIGHT = " +height);

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

            Log.i(activity.TAG,"COUNT = " +count);

            Log.i(activity.TAG,"DIVIDER = " +divider);

            Log.i(activity.TAG,"SAMPLE PIXEL = " +samplePixel);

            Log.i(activity.TAG,"CAPTURE PIXEL = " +capturePixel);

            Log.i(activity.TAG,"PERCENTAGE = " +percentage);

        } else {
            Toast.makeText(activity,"Image size is not same",Toast.LENGTH_SHORT).show();

        }

        return percentage;

    }

    private static double pixelDiff(int samplePixel, int capturePixel){

        double redSample = Color.red(samplePixel);
        double greenSample = Color.green(samplePixel);
        double blueSample = Color.blue(samplePixel);

        double redCapture = Color.red(capturePixel);
        double greenCapture = Color.green(capturePixel);
        double blueCapture = Color.blue(capturePixel);


        return Math.abs(redCapture - redSample) + (greenCapture - greenSample )+( blueCapture + blueSample);
    }


    @Override
    public void uploadImage(File file) {

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

    private static String getDateFromUri(Uri uri){
        String[] split = uri.getPath().split("/");
        String fileName = split[split.length - 1];
        String fileNameNoExt = fileName.split("\\.")[0];
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = format.format(new Date(Long.parseLong(fileNameNoExt)));
        return dateString;
    }
}
