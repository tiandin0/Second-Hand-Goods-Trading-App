package com.example.backend.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.backend.Fragment.buttom_fragment;
import com.example.backend.R;
import com.example.backend.Adapter.HistoryAdapter;
import com.google.firebase.auth.FirebaseAuth;
import java.util.List;
import DAO.Impl.ItemRepoImpl;
import Model.Item;

// transfer to different specific  history page
public class ProfileActivity extends AppCompatActivity implements buttom_fragment.OnFragmentInteractionListener {
    private HistoryAdapter adapter;
    private List<Item>items;
    private ItemRepoImpl itemService=new ItemRepoImpl();
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("profile activity", "oncreate");
        setContentView(R.layout.profile);
        adapter = new HistoryAdapter(this.getBaseContext());
        ListView listveiw = (ListView) findViewById(R.id.Profile_list);
        listveiw.setAdapter(adapter);
        listveiw.setOnItemClickListener(new profileListener());

        //
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //set the list item click listener
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId()){
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent= new Intent(ProfileActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                return true;
        }


        return true;
    }

    private class profileListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String s = (String) adapter.getItem(position);
            Intent intent = new Intent(ProfileActivity.this, HistoryItemActivity.class);
            switch (s){
                case "Post Items":
                    intent.putExtra("Title", "Posted");
                    intent.putExtra("ShowEdit", true);
                    break;

                case "Sold Items":
                    intent.putExtra("Title", "Sold");
                    intent.putExtra("ShowEdit", false);
                    break;

                case "Bought Items":
                    intent.putExtra("Title", "Bought");
                    intent.putExtra("ShowEdit", false);
                    break;

                case "Favorite Items":
                    intent.putExtra("Title", "Favourite");
                    intent.putExtra("ShowEdit", false);
                    break;
            }

            ProfileActivity.this.startActivity(intent);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d("profile activity", "onstart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("profile activity", "onresume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("profile activity", "onrestart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("profile activity", "onpause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("profile activity", "onstop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("profile activity", "ondestroy");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("profile activity", "onsaveINsatnceState");
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d("profile activity", "onRestoreINsatnceState");
    }
}

