package com.example.backend.Adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.backend.R;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    private String[] TextArray;
    private ArrayList<Integer> ImageList;
    Context context;
    public CustomAdapter(Context aContext) {
        context = aContext;
        ImageList = new ArrayList<>();
        ImageList.add(R.drawable.clothes);
        ImageList.add(R.drawable.books);
        ImageList.add(R.drawable.electronics);
        ImageList.add(R.drawable.furnitures);
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

        if (convertView == null){  //indicates this is the first time we are creating this row.
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.custom_row, parent, false);
        }
        else
        {
            row = convertView;
        }

        ImageView image = (ImageView) row.findViewById(R.id.leftimage);
        TextView text = (TextView) row.findViewById(R.id.Item_Text);
        image.setImageResource(ImageList.get(index).intValue());
        text.setText(TextArray[index]);
        return row;
    }
}