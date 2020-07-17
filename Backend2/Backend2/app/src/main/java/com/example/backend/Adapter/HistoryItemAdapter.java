package com.example.backend.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.backend.Activity.HistoryItemActivity;
import com.example.backend.Activity.ItemDetailActivity;
import com.example.backend.Activity.PostActivity;
import com.example.backend.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import DAO.Impl.ItemRepoImpl;
import Model.Item;
import Model.User;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class HistoryItemAdapter extends BaseAdapter {
    private List<Item> Items;
    private Context context;
    private boolean show;
    private String category;
    private ItemRepoImpl itemService=new ItemRepoImpl();
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;

    public HistoryItemAdapter(List<Item> res, boolean show, String category, Context context) {
        this.Items = res;
        this.context = context;
        this.show = show;
        this.category = category;

    }
    public HistoryItemAdapter(Context context, boolean show){
        Items = new ArrayList<>();
        this.context = context;
        this.show = show;
    }

    @Override
    public int getCount() {
        return Items.size();
    }

    @Override
    public Object getItem(int position) {
        return Items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row;
        ViewHolder viewholder;
        if (convertView == null){  //indicates this is the first time we are creating this row.
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.item_list_row, parent, false);

            viewholder = new ViewHolder();
            viewholder.userimage = (CircleImageView) row.findViewById(R.id.Histroy_UserImage);
            viewholder.itemimage = (ImageView) row.findViewById(R.id.History_Item_Image);
            viewholder.username = (TextView) row.findViewById(R.id.History_UserName);
            viewholder.imageTitle = (TextView) row.findViewById(R.id.History_Item_Title);
            viewholder.edit = (Button) row.findViewById(R.id.History_Edit);
            viewholder.delete = (Button) row.findViewById(R.id.History_Delete);
            viewholder.price = (TextView) row.findViewById(R.id.History_Item_price);
            row.setTag(viewholder);

        }
        else
        {
            row = convertView;
            viewholder = (ViewHolder) row.getTag();
        }

        if(!show){
            viewholder.edit.setVisibility(View.GONE);
        }
        if(Items.size() > 0){

            Item item = Items.get(position);
            //update  the image
            Glide.with(context).load(item.getImageUrl()).into(viewholder.itemimage);

            switch (category){
                case "Posted":
                    viewholder.username.setText(item.getSellerName());
                    setImage(item.getSellerId(), viewholder.userimage);
                    break;

                case "Sold":
                    viewholder.username.setText(item.getBuyerName());
                    setImage(item.getBuyerId(), viewholder.userimage);
                    break;

                case "Bought":
                    viewholder.username.setText(item.getSellerName());
                    setImage(item.getSellerId(), viewholder.userimage);
                    break;

                case "Favourite":
                    viewholder.username.setText(item.getSellerName());
                    setImage(item.getSellerId(), viewholder.userimage);
                    break;
            }
            viewholder.imageTitle.setText(item.getTitle());
            viewholder.price.setText("$"+item.getPrice());
        }

        viewholder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder bld= new AlertDialog.Builder(context);
                bld. setTitle("Alert");
                bld.setMessage("Are you sure to delete this record?");
                bld.setCancelable(true);
                bld.setPositiveButton("Yes, I want to delete.",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog,int which) {
                        Item remove_item = Items.remove(position);
                        String itemid = remove_item.getItemId();
                        if(category.equals("Posted")){
                            // delete from market and deep delete from posted and fav and pic
                            itemService.deleteItemByItemId(itemid);

                        }else{
                            // only delete my record , not affecting other
                            itemService.deleteFromAllTableByUsername(itemid,category);

                        }
                        notifyDataSetChanged();

                    }
                });
                bld.setNegativeButton("No",new DialogInterface.OnClickListener(){

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                bld.show();
            }
        });

        viewholder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Item clickitem = (Item) getItem(position);
                Intent intent = new Intent(context, PostActivity.class);
                //Bundle bundle = new Bundle();
                intent.putExtra("edititem", clickitem);
                //intent.putExtra("edititem", bundle);
                //intent.putExtra("edit", true);
                intent.putExtra("edit", true);
                intent.setFlags( FLAG_ACTIVITY_NEW_TASK);
                //intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        return row;
    }

    private void setImage(String userID, ImageView userimage){
        databaseReference = FirebaseDatabase.getInstance().getReference("User").child(userID);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                String imageurl = user.getImageurl();
                if (user.getImageurl().equals("default")){
                    userimage.setImageResource(R.drawable.icon);
                }else {

                    Glide.with(context.getApplicationContext()).load(user.getImageurl()).into(userimage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private static class ViewHolder{
        CircleImageView  userimage;
        ImageView itemimage;
        TextView username;
        TextView imageTitle;
        Button edit;
        Button delete;
        TextView price;
    }



}
