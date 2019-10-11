package cartenz.yunus.foregroundapps.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;

import androidx.loader.content.CursorLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

public class Utils {

    private static Utils mInstance;

    private Utils(){

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
}
