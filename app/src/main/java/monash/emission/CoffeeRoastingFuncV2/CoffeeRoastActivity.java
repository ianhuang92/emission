package monash.emission.CoffeeRoastingFuncV2;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;

import monash.emission.R;

/**
 * Created by Ranger on 2017/8/25.
 */

public class CoffeeRoastActivity extends Activity {
    //protected ArrayList<Bundle> sharedBundles;
    protected Bundle sharedBundle;
    protected FragmentManager fragmentManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coffee);
        //sharedBundles = new ArrayList<>();


        sharedBundle = new Bundle();
        //About this bundle
        //Used Key -- Type -- Meaning
        //CheckedName -- String -- Name of methodology selected
        //CheckedID  -- int -- ID of radio button selected in group, starting from 0
        //StartDate -- String -- start date selected, yyyy-MM-dd
        //EndDate -- String -- end date selected, yyyy-MM-dd
        //Duration -- int --number of days between end date and start date.
        //AvgHour -- Double -- average running hour per day


        //fragment manager
        fragmentManager=getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new CoffeeSelectionFragment()).commit();


    }


}
