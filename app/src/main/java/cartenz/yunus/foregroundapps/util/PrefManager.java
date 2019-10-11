package cartenz.yunus.foregroundapps.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {

        private static PrefManager instance;
        private static SharedPreferences sharedPreferences;
        private static SharedPreferences.Editor editor;
        private static final String ZEEPOS      = "ZEEPOS";

        public PrefManager(Context context) {
            sharedPreferences = context.getSharedPreferences(ZEEPOS,Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
        }

        public static PrefManager getInstance (Context context){
            if(null == instance){
                instance = new PrefManager(context);
            }
            return instance;
        }

        public void putString(String key,String value){
            editor.putString(key,value);
            editor.commit();
        }

        public String getString(String key, String defValue){
            return sharedPreferences.getString(key,defValue);
        }

        public void putInt(String key, int value){
            editor.putInt(key,value);
            editor.commit();
        }

        public int getInt(String key, int defValue){
            return sharedPreferences.getInt(key,defValue);
        }

        public void putLong(String key, long value){
            editor.putLong(key,value);
            editor.commit();
        }

        public long getLong(String key, long defValue){
            return sharedPreferences.getLong(key,defValue);
        }

        public void putBoolean(String key, boolean value){
            editor.putBoolean(key,value);
            editor.commit();
        }

        public boolean getBoolean(String key, boolean defValue){
            return sharedPreferences.getBoolean(key,defValue);
        }

        public static SharedPreferences.Editor getEditor() {
            return editor;
        }

}
