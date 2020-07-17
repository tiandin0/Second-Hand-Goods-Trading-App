package com.example.backend.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.net.Uri;
import android.os.Bundle;
import com.example.backend.Fragment.buttom_fragment;
import com.example.backend.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

// ChatActivity is consist of two fragment (chat fragment and bottom fragment)
public class ChatActivity extends AppCompatActivity implements buttom_fragment.OnFragmentInteractionListener {

    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void status(String status){
        // get current user
        databaseReference = FirebaseDatabase.getInstance().getReference("User").child(firebaseUser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);

        databaseReference.updateChildren(hashMap);
    }

    @Override
    protected void onStart() {
        super.onStart();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }
}
