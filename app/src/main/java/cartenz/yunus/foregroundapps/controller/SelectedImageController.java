package cartenz.yunus.foregroundapps.controller;


import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.io.File;

import cartenz.yunus.foregroundapps.R;
import cartenz.yunus.foregroundapps.activity.SelectedImageActivity;

public class SelectedImageController {

    public static final String IMAGE_POSITION = "IMAGE_POSITION";

    public static final String IMAGE_PATH = "IMAGE_PATH";

    private Activity activity;

    private ImageView imgSelectedDisplay;

    private String imagePath;

    private File filePath;



    public SelectedImageController(AppCompatActivity activity) {
        super();
        this.activity = activity;
        initData();
        initLayout();
    }

    private void initData(){
        Bundle extras = activity.getIntent().getExtras();
        int position;

        if (extras != null){

            if (extras.containsKey(IMAGE_POSITION)){
                position = extras.getInt(IMAGE_POSITION);
            }

            if (extras.containsKey(IMAGE_PATH)){
                imagePath = extras.getString(IMAGE_PATH);
            }

        }

    }

    private void initLayout(){
        imgSelectedDisplay = activity.findViewById(R.id.img_selected_display);
        Picasso.with(activity)
                .load(imagePath)
                .into(imgSelectedDisplay);
    }


}
