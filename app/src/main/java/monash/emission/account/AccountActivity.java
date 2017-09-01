package monash.emission.account;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;

import monash.emission.CoffeeRoastingFuncV2.CoffeeSelectionFragment;
import monash.emission.R;

/**
 * Created by Ranger on 2017/9/1.
 */

public class AccountActivity extends Activity{
    protected FragmentManager fragmentManager;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_calculationv2);
        fragmentManager=getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new AccountMainFragment()).commit();



    }
}
