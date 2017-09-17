package monash.emission.CoffeeRoastingFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by 80576 on 16/08/2017.
 */

public class CoffeePageAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public CoffeePageAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                fuelAnalysis tab1 = new fuelAnalysis();
                return tab1;
            case 1:
                EmissionFactor tab2 = new EmissionFactor();
                return tab2;
            case 2:
                CoffeeInfoFragment tab3 = new CoffeeInfoFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}