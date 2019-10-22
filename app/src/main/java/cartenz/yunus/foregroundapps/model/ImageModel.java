package cartenz.yunus.foregroundapps.model;

import android.net.Uri;


import java.util.ArrayList;
import java.util.List;

public class ImageModel {

    public Uri uri;
    public String date;
    public String imageName;
    public String imageCreatedDate;
    public String imageCreatedTime;
    private List<String> images;

    public ImageModel(List<String> images){
        setImages(images);

    }

    public void setImages(List<String> modelList){
        images = new ArrayList<>();
        for (int i=0; i<modelList.size(); i++){
            String image = modelList.get(i);
            images.add(image);
        }
    }

    public List<String> getImages() {
        return images;
    }
}
