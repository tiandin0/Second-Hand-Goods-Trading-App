package DAO.Impl;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import Model.Item;
/*
The class is for comparing the distance
 */
public class GeolocationComparationImpl {
    public static void sortItems(List<Item>res,Location location,Geocoder gCoder){
        Collections.sort(res,new SortPlaces(location,gCoder));
    }
}
//the comparator of distance
class SortPlaces implements Comparator<Item> {
    Location currentLoc;
    Geocoder gCoder;
    static String mytag="mytag";
    public SortPlaces(Location location,Geocoder gCoder){
        this.gCoder=gCoder;
        currentLoc=location;
    }
    @Override
    public int compare(Item place1, Item place2) {
        double lat1 = 0;
        double lon1 = 0;
        double lat2 = 0;
        double lon2 = 0;
        try {
            List<Address> res=gCoder.getFromLocationName(place1.getAddress(),5);
            if(res!=null&&res.size()>0){
                lat1 = res.get(0).getLatitude();
                lon1 = res.get(0).getLongitude();
            }
            res=gCoder.getFromLocationName(place2.getAddress(),5);
            if(res!=null&&res.size()>0){
                lat2=res.get(0).getLatitude();
                lon2=res.get(0).getLongitude();
            }
        } catch (IOException e) {

            e.printStackTrace();
        }
        Log.i(mytag,""+place1.getTitle()+lat1+lon1+place1.getAddress());
        Log.i(mytag,""+place2.getTitle()+lat2+lon2+place2.getAddress());
        double distanceToPlace1 = distance(currentLoc.getLatitude(), currentLoc.getLongitude(), lat1, lon1);
        double distanceToPlace2 = distance(currentLoc.getLatitude(), currentLoc.getLongitude(), lat2, lon2);
        return (int) (distanceToPlace1 - distanceToPlace2);
    }
    //calculate the distance between two locations using latitude and longitude
    public double distance(double fromLat, double fromLon, double toLat, double toLon) {
        double radius = 6378137;   // approximate Earth radius, *in meters*
        double deltaLat = toLat - fromLat;
        double deltaLon = toLon - fromLon;
        double angle = 2 * Math.asin( Math.sqrt(
                Math.pow(Math.sin(deltaLat/2), 2) +
                        Math.cos(fromLat) * Math.cos(toLat) *
                                Math.pow(Math.sin(deltaLon/2), 2) ) );
        return radius * angle;
    }
}

