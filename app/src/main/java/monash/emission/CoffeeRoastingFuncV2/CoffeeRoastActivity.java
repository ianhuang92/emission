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
        //fragment manager
        fragmentManager=getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new CoffeeSelectionFragment()).commit();


    }


}
