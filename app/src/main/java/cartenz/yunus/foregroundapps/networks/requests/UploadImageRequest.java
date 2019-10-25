package cartenz.yunus.foregroundapps.networks.requests;

import com.android.volley.Request;

import java.io.File;

public class UploadImageRequest extends BaseRequest {

    private File file;
    private String image;
    private String generateID;

    @Override
    protected String getAction() {
        return null;
    }


    @Override
    protected void populateSignatureParameters() {

        signatureParameters.put("file",getFile());
        signatureParameters.put("image",getImage());

    }

    public UploadImageRequest(){
        setMethod(Request.Method.POST);
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
