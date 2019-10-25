package cartenz.yunus.foregroundapps.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import androidx.loader.content.CursorLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import cartenz.yunus.foregroundapps.helper.JsonFileHelper;
import cartenz.yunus.foregroundapps.networks.ZeeposAPI;

public class Utils {

    private static Utils mInstance;

    private SimpleDateFormat fileNameDateFormat;

    private static final String appKey = "01234567890abcdefghijklmnopqrstuvwxyz01234567890";

    private Utils(){
        fileNameDateFormat = new SimpleDateFormat("yyyy.MM.dd_HH:mm:ss", Locale.getDefault());
    }

    public static Utils getInstance(){
        if (mInstance == null){
            synchronized (Utils.class){
                mInstance = new Utils();
            }
        }

        return mInstance;
    }

    //for API < 19 (Android 4.4)
    public static String pathLowerAPI19(Context context, Uri contentUri){
        String[] proj = { MediaStore.Images.Media.DATA };
        String result = null;

        CursorLoader cursorLoader = new CursorLoader(
                context,
                contentUri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        if(cursor != null){
            int column_index =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            result = cursor.getString(column_index);
        }
        return result;

    }

    //for API > 19
    public static String pathAboveAPI19(Context context, Uri contentUri){
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        int column_index
                = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);

    }

    private void createDirectory() {
        String path = Environment.getExternalStorageDirectory().toString();
        File dir = new File(path+"/COBA LAGI");
        try{
            if (!dir.exists()){
                dir.mkdirs();
                System.out.println("Directory created");
            } else {
                System.out.println("Directory is not created");
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void takeScreenshot(Activity activity) {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String datafile = Environment.getExternalStorageDirectory().toString() + "/CARTENZ/" + now + ".jpg";

            // create bitmap screen capture
            View v1 = activity.getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(datafile);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            openScreenshot(imageFile,activity);
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }

    private void openScreenshot(File imageFile, Activity activity) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        activity.startActivity(intent);
    }

    public JSONObject getZeeposUrl(Context context) {
        Integer zeeposUrl = Global.URL_JSON;
        try {
            return JsonFileHelper.getInstance().readJsonFromResource(context, zeeposUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public IZeeposAPI getZeeposAPI(Context context){
        return new ZeeposAPI(context);
    }


    public static String generateID(int length){

        String CHAR_UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        String NUMBER = "0123456789";

        String DATA_FOR_RANDOM_STRING = NUMBER + CHAR_UPPER;

        SecureRandom random = new SecureRandom();

        if (length < 1) throw new IllegalArgumentException();

        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {

            int rndCharAt = random.nextInt(DATA_FOR_RANDOM_STRING.length());
            char rndChar = DATA_FOR_RANDOM_STRING.charAt(rndCharAt);

            sb.append(rndChar);
        }

        return sb.toString();
    }

    public String getFileNameDateFormat() {
        return fileNameDateFormat.format(new Date());
    }

    public static String getAppKey() {
        return appKey;
    }

}
