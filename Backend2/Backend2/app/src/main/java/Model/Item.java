package Model;
import android.os.Parcel;
import android.os.Parcelable;
public class Item implements Parcelable{
    private String itemId="null";//unique and auto
    private String tagId="";//0 1 2 3
    private String sellerId="";
    private String buyerId="";
    private String sellerName="";
    private String buyerName="";
    private String title="";
    private String productName="";
    private String price="";
    private String description="";
    private String imageUrl="";
    private String address="";//auto getting
    private String status="0";//0:on sell, 1: sold
    // to be delete or upgrade
    private String postRating="Null";
    private String rated="n";//"y" or "n"
    public Item(){


    }
    public Item(String itemId, String tagId, String sellerId, String buyerId,String sellerName,String buyerName, String title, String productName, String price, String description,String url, String address, String status, String postRating, String rated) {
        this.itemId = itemId;
        this.tagId = tagId;
        this.sellerId = sellerId;
        this.buyerId = buyerId;
        this.title = title;
        this.productName = productName;
        this.price = price;
        this.description = description;
        this.imageUrl=url;
        this.address = address;
        this.status = status;
        this.postRating = postRating;
        this.rated = rated;
        this.sellerName=sellerName;
        this.buyerName=buyerName;
    }

    // implement the Parcelable in order to pass thie object from an activity to another activity
    protected Item(Parcel in) {
        itemId = in.readString();
        tagId = in.readString();
        sellerId = in.readString();
        buyerId = in.readString();
        sellerName=in.readString();
        buyerName=in.readString();
        title = in.readString();
        productName = in.readString();
        price = in.readString();
        description = in.readString();
        imageUrl = in.readString();
        address = in.readString();
        status = in.readString();
        postRating = in.readString();
        rated = in.readString();
    }
    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getSellerName() {
        return sellerName;
    }

    public String getBuyerName() {
        return buyerName;
    }
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPostRating() {
        return postRating;
    }

    public void setPostRating(String postRating) {
        if(this.rated.equals("y")) return;
        this.postRating = postRating;
        setRated("y");
    }

    public String getRated() {
        return rated;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.itemId);
        dest.writeString(this.tagId);
        dest.writeString(this.sellerId);
        dest.writeString(this.buyerId);
        dest.writeString(this.sellerName);
        dest.writeString(this.buyerName);
        dest.writeString(this.title);
        dest.writeString(this.productName);
        dest.writeString(this.price);
        dest.writeString(this.description);
        dest.writeString(this.imageUrl);
        dest.writeString(this.address);
        dest.writeString(this.status);
        dest.writeString(this.postRating);
        dest.writeString(this.rated);
    }

    public void setRated(String rated) {
        this.rated = rated;

    }
    public  String toString(){
        return "itemid: "+itemId+", tagid:"+tagId+" sellerName:"+sellerName+",buyerName: "+buyerName+", price:"+price+"+ rated:"+rated+", rating:"+postRating;
    }
}
