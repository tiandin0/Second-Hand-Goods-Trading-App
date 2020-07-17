package Model;

public class Picture {
    private String itemId;
    private String picturePath;

    public Picture(String itemId, String picturePath) {
        this.itemId = itemId;
        this.picturePath = picturePath;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }
}
