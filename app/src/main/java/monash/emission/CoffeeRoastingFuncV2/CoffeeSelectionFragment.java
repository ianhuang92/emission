package monash.emission.CoffeeRoastingFuncV2;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import monash.emission.R;

/**
 * Created by Ranger on 2017/8/25.
 */

public class CoffeeSelectionFragment extends Fragment {
    private RadioGroup rGroup;
    private int checkedID;
    private String checkedMsg;
    private Button butNext;
    private CoffeeRoastActivity c;

private View vDisplayUnit;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//inflate converts an XML layout file into corresponding ViewGroups and views
        vDisplayUnit = inflater.inflate(R.layout.fragment_coffee_selection, container, false);
        c=(CoffeeRoastActivity)getActivity();
        butNext = (Button)vDisplayUnit.findViewById(R.id.but_coffeeSelection_Next) ;


//init radio group and set listener
        rGroup = (RadioGroup)vDisplayUnit.findViewById(R.id.frag_coffee_calMethod);

    butNext.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        //read selected radio button data
        for(int i=0;i<rGroup.getChildCount();i++)
        {
            RadioButton radioButton = (RadioButton)rGroup.getChildAt(i);
            if (radioButton.isChecked())
            {
                Log.i("tag", "You selected: "+radioButton.getText()+"  ID: "+checkedID);
                checkedID = i;
                checkedMsg = radioButton.getText().toString();
                }
        }
        //save into bundle and upload to activity bundle arraylist
        Bundle bundle = new Bundle();
        bundle.putString("CheckedName",checkedMsg);
        bundle.putInt("CheckedID",checkedID);
        //c.sharedBundles.add(bundle);
        c.sharedBundle = bundle;
        FragmentTransaction ft = c.fragmentManager.beginTransaction();
        //fragment transaction
        if (checkedID ==0 || checkedID == 1) {
            ft.replace(R.id.content_frame, new CoffeeDurationFragment());
            ft.addToBackStack("CoffeeSlt2Duration");
            ft.commit();
        }
    else
        //navigate to your following fragments
        //modify following statement if needed
        //TODO
        if(checkedID == 3)
            c.fragmentManager.beginTransaction().replace(R.id.content_frame, new CoffeeInfoMBFragment()).commit();
        else
            c.fragmentManager.beginTransaction().replace(R.id.content_frame, new CoffeeInfoDMFragment()).commit();
            }

        });










        return vDisplayUnit;
    }



}
