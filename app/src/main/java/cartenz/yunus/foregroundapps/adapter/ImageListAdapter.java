package cartenz.yunus.foregroundapps.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import cartenz.yunus.foregroundapps.R;
import cartenz.yunus.foregroundapps.activity.SelectedImageActivity;
import cartenz.yunus.foregroundapps.controller.SelectedImageController;
import cartenz.yunus.foregroundapps.model.ImageModel;

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ViewHolder> {
    private List<ImageModel> imageModelList;
    private Context context;
    private boolean isSelected = false;
    private int selectedPosition;
    private String fileName;

    private File imageFile;

    public ImageListAdapter(Context ctx,List<ImageModel> imgList){
        this.context = ctx;
        this.imageModelList = imgList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public final View view;
        public TextView txtCreatedName;
        public TextView txtCreatedDate;
        public TextView txtCreatedTime;
        public ImageView imgScreenshot;
        public ImageModel imageModel;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            this.imgScreenshot = view.findViewById(R.id.img_screenshot);
            this.txtCreatedDate = view.findViewById(R.id.txt_date_created);

        }
    }

    @NonNull
    @Override
    public ImageListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_list_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //ImageModel model = imageModelList.get(position);

        ImageModel model = imageModelList.get(position);

        holder.imageModel = model;

        holder.txtCreatedDate.setText("14-10-2019");

        ImageView imageScreenShot = holder.imgScreenshot;

        List<String> strImageList = holder.imageModel.getImages();

        for (int i = 0; i < strImageList.size(); i++){
            if (strImageList != null && !strImageList.isEmpty()){

                String displayImage = strImageList.get(i);
                imageFile = new File(displayImage);

                if (!imageFile.exists()) {
                    continue;
                }

                Picasso.with(context)
                        .load(imageFile)
                        .into(imageScreenShot);

                fileName = displayImage.substring(displayImage.lastIndexOf("/")+1);
            }
        }

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSelected = true;
                selectedPosition = position;
                setImageFile(imageFile);

                /*Intent intent = new Intent(context,SelectedImageController.class);
                intent.putExtra(SelectedImageController.IMAGE_POSITION,position);
                intent.putExtra(SelectedImageController.IMAGE_PATH,imageFile);*/

                /*Intent intent = new Intent(context,SelectedImageActivity.class);
                intent.putExtra(SelectedImageController.IMAGE_POSITION,position);
                context.startActivity(intent);*/
                //Toast.makeText(context,"On Click Position "+position,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageModelList.size();
    }

    public boolean isSelected() {
        return isSelected;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public File getImageFile() {
        return imageFile;
    }

    public void setImageFile(File imageFile) {
        this.imageFile = imageFile;
    }

    public String getFileName() {
        return fileName;
    }
}
