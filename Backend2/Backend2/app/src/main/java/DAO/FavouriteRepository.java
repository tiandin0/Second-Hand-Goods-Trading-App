package DAO;
import android.widget.ImageView;

import Model.Favourite;
import Model.Item;

/*
-Favourite table:
     -userid
          -itemId
               -item(as value)

 */
public interface FavouriteRepository {
    // save to Favourite database
    void save(Favourite favourite);
    // list of   my fav items
  // void getFavouritesByUserId(String userId);
   // delete one record from Favourite table
  // void deleteFav(Favourite fav);
}
