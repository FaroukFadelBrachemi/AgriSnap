package Models;

public class Image {
    private int imageId;
    private String imageName;

    private String imagePath;
    private String imageDeviceName;
    private String imageTime;
    private String imageDate;
    private String imageLat;
    private String imageLon;

    public Image() {

    }



    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageDeviceName() {
        return imageDeviceName;
    }

    public void setImageDeviceName(String imageDeviceName) {
        this.imageDeviceName = imageDeviceName;
    }

    public String getImageTime() {
        return imageTime;
    }

    public void setImageTime(String imageTime) {
        this.imageTime = imageTime;
    }


    public String getImageLat() {
        return imageLat;
    }

    public void setImageLat(String imageLat) {
        this.imageLat = imageLat;
    }

    public String getImageLon() {
        return imageLon;
    }

    public void setImageLon(String imageLon) {
        this.imageLon = imageLon;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImageDate() {
        return imageDate;
    }

    public void setImageDate(String imageDate) {
        this.imageDate = imageDate;
    }
}

