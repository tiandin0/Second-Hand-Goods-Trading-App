package DAO;
import android.app.Activity;
// User table service

/*
User table:
        - userId(as key to get user unique)
                -userId
                -email
                -userName
                -averageRate(double)
                -ratePeopleCount(int)
                -imageurl
                -status

 */
// the class to provide user service
public interface UserRepository {
    // add auth to firebase database and save to user table
    void register( String email, String passWord,Activity context);
    // update user's rating
    void updateRate(String id,double rating);
    void login(String email, String passWord, Activity context);
   void getUserById(String uid);
}
