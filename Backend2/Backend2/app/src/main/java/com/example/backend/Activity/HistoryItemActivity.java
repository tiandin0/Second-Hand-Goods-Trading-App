package com.example.backend.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.backend.R;
import com.example.backend.Adapter.HistoryItemAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import Model.Item;

public class HistoryItemActivity extends AppCompatActivity {
    private TextView title;
    private Intent intent;
    private HistoryItemAdapter adapter;
    private DatabaseReference allRef=FirebaseDatabase.getInstance().getReference("All");
    private  List<Item> res;
    private String titletext;
    private FirebaseAuth auth=FirebaseAuth.getInstance();

    private void switchType(String type,String userid){
          res=new LinkedList<>();
      allRef.child(type).child(userid).addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              res=new LinkedList<>();
              for (DataSnapshot item : dataSnapshot.getChildren()) {
                  HashMap<String, String> map = (HashMap<String, String>) item.getValue();
                  String id = map.get("itemId");
                  String tag = map.get("tagId");
                  String sellerId=map.get("sellerId");
                  String buyerId=map.get("buyerId");
                  String sellerName=map.get("sellerName");
                  String buyerName=map.get("buyerName");
                  String title=map.get("title");
                  String productName=map.get("productName");
                  String price=map.get("price");
                  String description=map.get("description");
                  String url=map.get("imageUrl");
                  String address=map.get("address");
                  String status=map.get("status");
                  String postRating=map.get("postRating");
                  String rated=map.get("rated");//"y" or "n"
                  Item i=new Item(id,tag,sellerId,buyerId,sellerName,buyerName,title,productName,price,description,url,address,status,postRating,rated);
                      res.add(i);
              }
              adapter = new HistoryItemAdapter(res,intent.getBooleanExtra("ShowEdit", true), type, HistoryItemActivity.this);//getBaseContext()
              ListView listveiw = (ListView) findViewById(R.id.History_Item_List);
              listveiw.setAdapter(adapter);
              listveiw.setOnItemClickListener(new HistoryItemListener());

              listveiw.setOnScrollListener(new AbsListView.OnScrollListener() {
                  @Override
                  public void onScrollStateChanged(AbsListView view, int scrollState) {

                      switch(scrollState){
                          case SCROLL_STATE_FLING://惯性滚动
                              Glide.with(HistoryItemActivity.this).pauseRequests();//暂停Glide加载图片
                              break;
                          case SCROLL_STATE_IDLE://空闲
                              Glide.with(HistoryItemActivity.this).resumeRequests();//恢复Glide加载图片
                              break;
                      }
                      adapter.notifyDataSetChanged();
                  }

                  @Override
                  public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                  }
              });
          }

          @Override
          public void onCancelled(DatabaseError databaseError) {

          }
      });
}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_lists);
        intent = getIntent();
        String type= intent.getStringExtra("Title").toString();
        titletext =type+" Items";
        title = (TextView) findViewById(R.id.History_Title);
        title.setText("My" + " " + titletext);
        // show user's history
        switchType(type,auth.getCurrentUser().getDisplayName());

    }

    private class HistoryItemListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Item clickitem = (Item) adapter.getItem(position);
            Intent intent = new Intent(HistoryItemActivity.this, ItemDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("clickitem", clickitem);
            intent.putExtra("clickitem", bundle);
            intent.putExtra("history", true);
            intent.putExtra("title", titletext);


            HistoryItemActivity.this.startActivity(intent);
        }
    }

}
