package cartenz.yunus.foregroundapps.model;

import android.net.Uri;


import java.util.ArrayList;
import java.util.List;

public class ImageModel {

    public Uri uri;
    public String imageName;
    public String imageCreatedDate;
    public String imageCreatedTime;
    private List<String> images;

    public ImageModel(){

    }

    public void setImages(List<ImageModel> modelList){
        images = new ArrayList<>();
        for (int i=0; i<modelList.size(); i++){
            String image = modelList.get(i).toString();
            images.add(image);
        }
    }

    public List<String> getImages() {
        return images;
    }
}
