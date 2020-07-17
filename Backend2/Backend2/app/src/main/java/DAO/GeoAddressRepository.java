package DAO;
import android.widget.TextView;
import Model.Item;

// get loation and update textview and item  address
public interface GeoAddressRepository {
     void getLastLocation(TextView tv, Item item) ;
}
