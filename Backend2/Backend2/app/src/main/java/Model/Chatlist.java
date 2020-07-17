package Model;

// this class just to save the history of chat of certain user
public class Chatlist {
    private String id;

    public Chatlist(String id){
        this.id = id;
    }

    public Chatlist(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
