package DAO.Impl;

import android.util.Log;
import androidx.annotation.NonNull;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import DAO.FavouriteRepository;
import Model.Favourite;
import Model.Item;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/*
-Favourite table:
     -userid
          -itemId
               -item(as value)

 */

public class FavouriteRepoImpl implements FavouriteRepository {
    private  DatabaseReference favRef;
    public FavouriteRepoImpl(){
        favRef =FirebaseDatabase.getInstance().getReference("All").child("Favourite");
    }

    @Override
    // save a fav to fav database , distinct
    public void save(Favourite favourite) {
        Item t=favourite.getItem();
        String k=t.getItemId();
        favRef.child(favourite.getUserName()).child(k).setValue(t);

    }

  //  @Override
    public void deleteFav(Favourite fav){
        Item t=fav.getItem();
        String k=t.getItemId();
        favRef.child(fav.getUserName()).child(k).removeValue();
    }

  //  @Override
    // get list of favs posts by useremail(key)

    public void getFavouritesByUserId(String userId) {
        List<Item> kk=new LinkedList<>();
        favRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
// this user has favs
                if (snapshot.getValue()!=null) {
                    // list of fav items
                    for (DataSnapshot postsanpshot : snapshot.getChildren()) {
                        HashMap<String, String> map = (HashMap<String, String>) postsanpshot.getValue();
                        String id = map.get("itemId");
                        String tagId = map.get("tagId");
                        String sellerEmail=map.get("sellerEmail");
                        String buyerEmail=map.get("buyerEmail");
                        String title=map.get("title");
                        String productName=map.get("productName");
                        String price=map.get("price");
                        String description=map.get("description");
                        String url=map.get("imageUrl");
                        String address=map.get("address");
                        String status=map.get("status");
                        String postRating=map.get("postRating");
                        String rated=map.get("rated");//"y" or "n"
                        String sellerId=map.get("sellerId");
                        String buyerId=map.get("buyerId");
                        Item t=new Item(id,tagId,sellerId,buyerId,sellerEmail,buyerEmail,title,productName,price,description,url,address,status,postRating,rated);
                       kk.add(t);
                    }


                }else{
                    //frontend show no favorite for this user
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }
}
