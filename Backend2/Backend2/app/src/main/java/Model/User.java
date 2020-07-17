package Model;

public class User {
    private String userId;//auto assign when get auth for firebase
    private String email;//unique
    private String userName;
    // to be delete or upgrade
    private double averageRate;
    private int ratePeopleCount;
    //
    private String imageurl="default";
    private String status="offline";

    public User(){
    super();
}
    public User( String userId, String email, String userName) {
        this.userId = userId;
        this.email = email;
        this.userName = userName;
        this.averageRate = 0.0;
        this.ratePeopleCount=0;
    }

public User(String userId, String email, String userName, double averageRate, int ratePeopleCount) {
        this.userId = userId;
        this.email = email;
        this.userName = userName;
        this.averageRate = averageRate;
        this.ratePeopleCount = ratePeopleCount;
    }

    public User( String userId, String email, String userName,double averageRate, int ratePeopleCount,String url,String status){
        this.userId = userId;
        this.email = email;
        this.userName = userName;
        this.averageRate = averageRate;
        this.ratePeopleCount=ratePeopleCount;
        this.imageurl=url;
        this.status=status;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImageurl() {
        return imageurl;
    }

    public String getStatus() {
        return status;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public double getAverageRate() {
        return averageRate;
    }

    public void setAverageRate(double averageRate) {
        this.averageRate = averageRate;
    }

    public int getRatePeopleCount() {
        return ratePeopleCount;
    }

    public void setRatePeopleCount(int ratePeopleCount) {
        this.ratePeopleCount = ratePeopleCount;
    }
    public  String toString(){
    return "Email:"+email+", rate"+averageRate+",count:"+ratePeopleCount;
    }
}
