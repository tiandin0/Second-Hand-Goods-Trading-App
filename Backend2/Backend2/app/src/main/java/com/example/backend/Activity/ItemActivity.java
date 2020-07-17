package com.example.backend.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.backend.Adapter.ItemAdapter;
import com.example.backend.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import DAO.Impl.GeoAddressRepoImpl;
import DAO.Impl.GeolocationComparationImpl;
import Model.Item;

public class ItemActivity extends AppCompatActivity {
    List<Item> itemList = new ArrayList<>();
    private Intent intent;
    private ItemAdapter adapter;
    private DatabaseReference itemRef= FirebaseDatabase.getInstance().getReference("Item");
    private  List<Item> res;
    private String type;
    private Spinner spinnerSort;
    private EditText et_search;
    private Button btn_search;
    private GeolocationComparationImpl geo;
    FusedLocationProviderClient mFusedLocationClient;
    Geocoder gCoder;
    Location curLocation;
    List<Address> addresses;
    String sorttype="Latest Posts First"; // default is sort by latest item posts first
    static final int PERMISSION_ID = 44;
    private  String search;

    
 //  private FirebaseAuth auth=FirebaseAuth.getInstance();
    private void switchType(){
        res=new LinkedList<>();
        itemRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    HashMap<String, String> map = (HashMap<String, String>) item.getValue();
                    String id = map.get("itemId");
                    String tag = map.get("tagId");
                    String sellerId=map.get("sellerId");
                    String buyerId=map.get("buyerId");
                    String title=map.get("title");
                    String productName=map.get("productName");
                    String sellerName=map.get("sellerName");
                    String buyerName=map.get("buyerName");
                    String price=map.get("price");
                    String description=map.get("description");
                    String url=map.get("imageUrl");
                    String address=map.get("address");
                    String status=map.get("status");
                    String postRating=map.get("postRating");
                    String rated=map.get("rated");//"y" or "n"
                    String tt=title.trim().toLowerCase();
                    if(search==null||search.equals("")){
                        if(tag.equals(type)&&status.equals("0")) { // only show onsell items
                            Item i=new Item(id,tag,sellerId,buyerId,sellerName,buyerName,title,productName,price,description,url,address,status,postRating,rated);
                            res.add(i);
                        }
                    }else{
                        if(tag.equals(type)&&status.equals("0")&&tt.contains(search)) { // only show onsell items
                            Item i=new Item(id,tag,sellerId,buyerId,sellerName,buyerName,title,productName,price,description,url,address,status,postRating,rated);
                            res.add(i);
                        }
                    }


                }

                // sort items by specific requirements

               if(sorttype.equals("Latest Posts First")){
                   Collections.reverse(res);

               }
                else if(sorttype.equals("Nearby Posts distance")){
                   // to do  distance sort
                    GeolocationComparationImpl.sortItems(res,curLocation,gCoder);

                }else if(sorttype.equals("Less Expensive Posts First")){
                    Collections.sort(res, new Comparator<Item>() {
                  @Override
                  public int compare(Item o1, Item o2) {
                      return Double.compare(Double.parseDouble(o1.getPrice()),Double.parseDouble(o2.getPrice()));
                  }
              });

                }

                btn_search = (Button)findViewById(R.id.btn_search);
                et_search = (EditText)findViewById(R.id.et_search);
                // search fuction for keywords
                btn_search.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String keywords_search = et_search.getText().toString();
                        search = keywords_search.trim().toLowerCase();
                        switchType();
                    }
                });


                //   all  items of one tag with sort type
                synchronized (this){
                        adapter = new ItemAdapter(res, ItemActivity.this);
                        ListView listview = (ListView) findViewById(R.id.lv_items);
                        listview.setAdapter(adapter);
                        listview.setOnItemClickListener(new ItemListener());
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        intent = getIntent();
         type= intent.getStringExtra("Type").toString();
        geo=new GeolocationComparationImpl();
        addresses=new ArrayList<>();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        gCoder = new Geocoder(this, Locale.getDefault());
        if(ActivityCompat.checkSelfPermission(ItemActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
            getLastLocation();
        }
        else {
            requestPermissions();
        }

        spinnerSort = (Spinner)findViewById(R.id.spinnerSort);
        String[] sortOptions = new String[]{"Latest Posts First","Nearby Posts distance", "Less Expensive Posts First"};
        ArrayAdapter spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sortOptions);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSort.setAdapter(spinnerAdapter);
        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String tobeSelete=parent.getItemAtPosition(position).toString();
                 sorttype = tobeSelete;

                 switchType();
                parent.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                parent.setVisibility(View.VISIBLE);
            }
        });

    }

    private class ItemListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Item clickitem = (Item) adapter.getItem(position);
            // open item detail page for an item chosen
            Intent intent = new Intent(ItemActivity.this, ItemDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("clickitem", (Parcelable) clickitem);
            intent.putExtra("clickitem", bundle);
            ItemActivity.this.startActivity(intent);
        }
    }
    //get the latest location
    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    curLocation=location;

                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    //update the location data
    @SuppressLint("MissingPermission")
    private void requestNewLocationData(){

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
        }
    };
    //check location access is permitted
    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }
    //request permission
    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }
    //check location access is permitted
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }
    //what we do is the location access is enabled
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }


}
