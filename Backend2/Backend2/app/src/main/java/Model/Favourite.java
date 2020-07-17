package Model;

public class Favourite {
    //Every Favourite record is unique
    private String userName;
    private Item item;

    public Favourite(String userName, Item item) {
        this.userName = userName;
        this.item = item;

    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String toString(){
        return "username: "+userName+", item: "+item;
    }
}
