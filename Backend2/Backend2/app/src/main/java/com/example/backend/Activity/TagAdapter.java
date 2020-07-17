package com.example.backend.Activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.backend.R;
import java.util.ArrayList;

public class TagAdapter extends BaseAdapter {
    private String[] TextArray;
    private ArrayList<Integer> ImageList;
    Context context;

    public TagAdapter(Context aContext) {
        context = aContext;

        // Create item list with names and pictures
        TextArray = context.getResources().getStringArray(R.array.ItemCategory);
        ImageList = new ArrayList<>();
        ImageList.add(R.drawable.clothes2);
        ImageList.add(R.drawable.books2);
        ImageList.add(R.drawable.electronics2);
        ImageList.add(R.drawable.furnitures2);

    }
    @Override
    public int getCount() {
        return TextArray.length;
    }

    @Override
    public Object getItem(int position) {
        return TextArray[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        final int index = position;
        if (convertView == null) {  //indicates this is the first time we are creating this row.
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.custom_tag_row, parent, false);
        } else {
            row = convertView;
        }

        // Assign images and tag names to different tags
        ImageView image = (ImageView) row.findViewById(R.id.imageView);
        TextView text = (TextView) row.findViewById(R.id.textView);

        image.setImageResource(ImageList.get(index).intValue());
        text.setText(TextArray[index]);

        return row;
    }
}

