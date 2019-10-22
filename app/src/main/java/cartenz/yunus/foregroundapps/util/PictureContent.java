package cartenz.yunus.foregroundapps.util;

import android.net.Uri;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cartenz.yunus.foregroundapps.model.ImageModel;

public class PictureContent {

    static final List<ImageModel> ITEMS = new ArrayList<>();

    public static void loadSavedImages(File dir) {
        ITEMS.clear();
        if (dir.exists()) {
            File[] files = dir.listFiles();
            for (File file : files) {
                String absolutePath = file.getAbsolutePath();
                String extension = absolutePath.substring(absolutePath.lastIndexOf("."));
                //loadImage(file);
            }
        }
    }

    /*public static void loadImage(File file) {
        ImageModel newItem = new ImageModel();
        newItem.uri = Uri.fromFile(file);
        newItem.date = getDateFromUri(newItem.uri);
        addItem(newItem);
    }*/

    private static void addItem(ImageModel item) {
        ITEMS.add(0, item);
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
