package com.example.backend.Activity;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.example.backend.Fragment.buttom_fragment;
import com.example.backend.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// four category list page
public class MainPageActivity extends AppCompatActivity implements buttom_fragment.OnFragmentInteractionListener {
    private List<Tag> tagList = new ArrayList<>();
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        Log.d("activity", "oncreate");
        //render tags and its images to be chosen by the user later
        final TagAdapter adapter = new TagAdapter(MainPageActivity.this);
        ListView listView = (ListView)findViewById(R.id.lv_tags);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String s = (String) adapter.getItem(i);
                // open specific tag page containing all items under such tag
                Intent intent = new Intent(MainPageActivity.this, ItemActivity.class);
                switch (s) {
                    case "Clothes":
                        intent.putExtra("Title", "Clothes");
                        intent.putExtra("Type", "0");
                        break;

                    case "Books":
                        intent.putExtra("Title", "Books");
                        intent.putExtra("Type", "1");
                        break;

                    case "Electronics":
                        intent.putExtra("Title", "Electronics");
                        intent.putExtra("Type", "2");
                        break;

                    case "Furnitures":
                        intent.putExtra("Title", "Furnitures");
                        intent.putExtra("Type", "3");
                        break;

                }
                startActivity(intent);

            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("activity", "onstart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("activity", "onstop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("activity", "ondestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("activity", "onpause");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("activity", "onrestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("activity", "onresume");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d("activity", "onnewIntent");
    }

    public void onFragmentInteraction(Uri uri) {

    }
}
