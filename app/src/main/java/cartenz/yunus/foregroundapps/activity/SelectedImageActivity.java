package cartenz.yunus.foregroundapps.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import cartenz.yunus.foregroundapps.R;
import cartenz.yunus.foregroundapps.controller.SelectedImageController;

public class SelectedImageActivity extends AppCompatActivity {

    private SelectedImageController selectedImageController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selected_image_layout);
        selectedImageController = new SelectedImageController(this);
    }
}
