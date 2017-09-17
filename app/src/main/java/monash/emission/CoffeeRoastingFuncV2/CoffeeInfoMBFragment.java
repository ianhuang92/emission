package monash.emission.CoffeeRoastingFuncV2;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import monash.emission.R;

/**
 * Created by Ranger on 2017/8/29.
 */

public class CoffeeInfoMBFragment extends Fragment {

    private View vDisplayUnit;
    private CoffeeRoastActivity c;
    private Button butBack;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//inflate converts an XML layout file into corresponding ViewGroups and views
        vDisplayUnit = inflater.inflate(R.layout.fragment_coffee_info_mass, container, false);
        c = (CoffeeRoastActivity) getActivity();
        butBack = (Button)vDisplayUnit.findViewById(R.id.but_back_fragment_coffee_info_mass) ;
        butBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c.fragmentManager.beginTransaction().replace(R.id.content_frame, new CoffeeSelectionFragment()).commit();
            }
        });

        return vDisplayUnit;
    }
}
