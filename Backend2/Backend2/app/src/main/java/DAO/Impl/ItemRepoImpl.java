package DAO.Impl;
import androidx.annotation.NonNull;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import DAO.ItemRepository;
import Model.Item;


/*
   Item table:
            -itemId (self -generated unique)
                        -itemId
                        -tagId
                        -sellerId
                        -buyerId
                        -sellerName
                        -buyerName
                        -title
                        -productName
                        -price
                        -description
                        -imageUrl
                        -address
                        -status  //0:on sell, 1: wait for respond and no more request for buying, 2:sold
                        -postRating
                        -rated("y"/"n")



    -All Table( key is user):
             -Favourite Table
                      -username
                          -item
             -Posted Table
                     -username(sellername)
                          -item
             -Bought Table
                     -username(buyername)
                          -item
             -Sold Table
                     -username(sellerame)
                         -item


 */

public class ItemRepoImpl implements ItemRepository {
    private DatabaseReference itemRef;
    private DatabaseReference userAllRef;
    private FirebaseAuth auth;
    public ItemRepoImpl(){
         userAllRef= FirebaseDatabase.getInstance().getReference("All");
        itemRef= FirebaseDatabase.getInstance().getReference("Item");
        auth=FirebaseAuth.getInstance();

    }


// add to item table and my Posted table
public Item saveToAllTable (Item item){
    String id = itemRef.push().getKey();
    item.setItemId(id);
    itemRef.child(id).setValue(item);
   userAllRef.child("Posted").child(item.getSellerName()).child(id).setValue(item);
   return item;
}
// fu is current user displayname
    // delete from market,  item table and posted table and fav table(not availble) and pic delete deep delete
public void deleteItemByItemId(String itemid){
    String fromUsername=auth.getCurrentUser().getDisplayName();
    itemRef.child(itemid).removeValue();
    userAllRef.child("Posted").child(fromUsername).child(itemid).removeValue();
    StorageReference storageRef=  FirebaseStorage.getInstance().getReference("Pics");
    storageRef.child(itemid).child(itemid+".jpg").delete();

    DatabaseReference ref=userAllRef.child("Favourite");

    ref.addListenerForSingleValueEvent(new ValueEventListener() {

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for(DataSnapshot uu:dataSnapshot.getChildren()){
                if( uu.child(itemid).exists()){
                    ref.child(uu.getKey()).child(itemid).removeValue();

                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });



}
    @Override
// delete one record in user history list
    public void  deleteFromAllTableByUsername(String itemid, String tabletype){
    String name=auth.getCurrentUser().getDisplayName();
    userAllRef.child(tabletype).child(name).child(itemid).removeValue();

    }

    // updated item value by item id
    // type 0: update all value, 1: needs to be add to bought
    // params: updated buyer item
    @Override

public void update(Item item,int type){
        String k=item.getItemId();
        String fu=item.getSellerName();
        String tu=item.getBuyerName();
    itemRef.child(item.getItemId()).setValue(item);
    userAllRef.child("Posted").child(fu).child(k).setValue(item);

    DatabaseReference ref=userAllRef.child("Favourite");
    ref.addListenerForSingleValueEvent(new ValueEventListener() {

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              for(DataSnapshot uu:dataSnapshot.getChildren()){
                 if( uu.child(item.getItemId()).exists()){
                     ref.child(uu.getKey()).child(item.getItemId()).setValue(item);

                 }
              }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });
    if(type==1){
        userAllRef.child("Sold").child(fu).child(k).setValue(item);
        userAllRef.child("Posted").child(fu).child(k).removeValue();
        userAllRef.child("Bought").child(tu).child(k).setValue(item);
        itemRef.child(item.getItemId()).removeValue();


    }
}

}
