package cartenz.yunus.foregroundapps.util;

import android.content.Context;
import android.os.Build;

import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.IOException;

public class StorageHelper {
    private static final StorageHelper ourInstance = new StorageHelper();

    public static StorageHelper getInstance() {
        return ourInstance;
    }

    private StorageHelper() {
    }
    private static File cachePath;
    private static File dataPath;
    private static File tempPath;

    public File getCachePath(Context context) {
        if(cachePath == null) {
            File[] cache = ContextCompat.getExternalCacheDirs(context);
            if(cache != null && cache.length > 0) {
                cachePath = cache[0];
            }

            if(cachePath == null) {
                cachePath = context.getExternalCacheDir();
            }

            if(cachePath == null) {
                cachePath = context.getCacheDir();
            }

            if(!cachePath.exists()) {
                cachePath.mkdirs();
            }
        }

        return cachePath;
    }

    public File getDataPath(Context context) {
        if(dataPath == null) {
            File[] data = ContextCompat.getExternalFilesDirs(context, (String)null);
            if(data != null && data.length > 0) {
                dataPath = data[0];
            }

            if(dataPath == null) {
                dataPath = context.getExternalFilesDir((String)null);
            }

            if(dataPath == null && Build.VERSION.SDK_INT >= 24) {
                dataPath = context.getDataDir();
            }

            if(dataPath == null) {
                dataPath = context.getFilesDir();
            }

            if(dataPath == null) {
                dataPath = new File(context.getApplicationInfo().dataDir);
            }

            if(dataPath == null) {
                dataPath = new File(getCachePath(context) + "/data");
            }

            if(!dataPath.exists()) {
                dataPath.mkdirs();
            }
        }

        return dataPath;
    }

    public File getTempPath(Context context) {
        if(tempPath == null) {
            tempPath = new File(getDataPath(context) + "/temp");
            if(!tempPath.exists()) {
                tempPath.mkdirs();
            }
        }

        return tempPath;
    }

    public CharSequence combinePath(String... params) {
        StringBuilder str = new StringBuilder(params[0]);
        int size = params.length;

        for(int i = 1; i < size; ++i) {
            String item = params[i];
            if(!params[i - 1].endsWith("/")) {
                str.append('/');
            }

            str.append(item);
        }

        return str;
    }

    public File createParentDirectory(File file) {
        File parent = file.getParentFile();
        if(!parent.exists()) {
            parent.mkdirs();
        }

        return file;
    }

    public File createDirectory(File file) {
        file.mkdirs();
        return file;
    }

    public File createFile(File file) throws IOException {
        createParentDirectory(file);
        file.createNewFile();
        return file;
    }

    public boolean deleteIfFileIsTemporary(Context context, File file) {
        return file != null && isTemporaryFile(context, file) && file.delete();
    }

    public boolean isTemporaryFile(Context context, File file) {
        return file.getAbsolutePath().startsWith(getTempPath(context).getAbsolutePath()) || file.getAbsolutePath().startsWith(getCachePath(context).getAbsolutePath());
    }

    public boolean delete(File file) {
        return file != null && file.delete();
    }

    public boolean deleteIf(boolean delete, File file) {
        return delete && file != null && file.delete();
    }

}
