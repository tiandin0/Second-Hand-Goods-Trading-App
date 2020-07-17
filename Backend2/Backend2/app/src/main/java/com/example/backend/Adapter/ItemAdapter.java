package com.example.backend.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.backend.R;
import java.util.LinkedList;
import java.util.List;
import Model.Item;

// show  posts as listview for one specific tag
public class ItemAdapter extends BaseAdapter {
    private List<Item> Items=new LinkedList<>();
    private Context context;
String mytag="mytag";
    public ItemAdapter(List<Item> Items, Context context) {
        // Get item list for display for the current tag
        this.Items = Items;
        this.context = context;
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

        if (convertView == null){  //indicates this is the first time we are creating this row.
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.custom_item_row, parent, false);
        }
        else
        {
            row = convertView;
        }

        // Get images stored in the database and its designated item title and price for display
        ImageView itemimage = (ImageView) row.findViewById(R.id.imageView);
        TextView itemtitle = (TextView) row.findViewById(R.id.titleView);
        TextView itemprice = (TextView) row.findViewById(R.id.priceView);
        Item item = Items.get(position);
        Glide.with(context.getApplicationContext()).load(item.getImageUrl()).into(itemimage);
        itemtitle.setText(item.getTitle());
        itemprice.setText("$ "+item.getPrice());
        return row;
    }
}