package cartenz.yunus.foregroundapps.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import cartenz.yunus.foregroundapps.R;
import cartenz.yunus.foregroundapps.model.ImageModel;

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ViewHolder> {
    private List<ImageModel> imageModelList;
    private Context context;

    public ImageListAdapter(Context ctx,List<ImageModel> imgList){
        context = ctx;
        imageModelList = imgList;
    }

    @NonNull
    @Override
    public ImageListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_list_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ImageModel model = imageModelList.get(position);

        holder.txtCreatedDate.setText("8-10-2019");

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"On Click",Toast.LENGTH_SHORT).show();
            }
        });

        List<String> strImageList = model.getImages();

        for (int i = 0; i < strImageList.size(); i++){
            if (strImageList != null && !strImageList.isEmpty()){
                Picasso.with(context)
                        .load(strImageList.get(i))
                        .fit()
                        .centerCrop()
                        .into(holder.imgScreenshot);
            }
        }

    }

    @Override
    public int getItemCount() {
        return imageModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public final View view;
        public TextView txtCreatedName;
        public TextView txtCreatedDate;
        public TextView txtCreatedTime;
        public ImageView imgScreenshot;
        public ImageModel imageModel;

        //public List<ImageModel>modelList;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            this.imgScreenshot = view.findViewById(R.id.item_image_view);
            this.txtCreatedDate = view.findViewById(R.id.txt_date_created);


        }
    }
}
