package monash.emission.CoffeeRoastingFuncV2;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import monash.emission.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CoffeeEFFragment extends Fragment {
    private View vEF;
    private EditText etActivityRate;
    private CoffeeRoastActivity c;
    private EditText etEfficiency;
    private Double activityRate;
    private final String[] mItems = {"Batch Roaster with Thermal Oxidiser","Continuous Cooler with Cyclone","Continuous Roaster","Continuous Roaster with Thermal Oxidiser","Green Coffee Bean Screening, Handling, and Storage System with Fabric Filter"};

    private double CO;
    private Double efficiency;
    private boolean percentageFlag;
    public CoffeeEFFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vEF = inflater.inflate(R.layout.fragment_coffee_ef, container, false);
        etActivityRate = (EditText)vEF.findViewById(R.id.rateinput);
        etEfficiency = (EditText)vEF.findViewById(R.id.efinput);
        Button cal = (Button)vEF.findViewById(R.id.calculateefBTN);
        c = (CoffeeRoastActivity) getActivity();
        final RadioGroup rdg = (RadioGroup) vEF.findViewById(R.id.radioGroup);
        cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = rdg.getCheckedRadioButtonId();
                RadioButton fuelBtn = (RadioButton) vEF.findViewById(id);
                if (fuelBtn.getText().toString().startsWith("G") || fuelBtn.getText().toString().startsWith("B")
                        || fuelBtn.getText().toString().startsWith("C") ){
                    c.sharedBundle.putString("ef",fuelBtn.getText().toString());
                    if(getValue()){
                        c.sharedBundle.putInt("efindex",rdg.indexOfChild(vEF.findViewById(rdg.getCheckedRadioButtonId())));
                        FragmentTransaction ft = c.fragmentManager.beginTransaction();
                        ft.replace(R.id.content_frame, new CoffeeResultFragment());
                        ft.addToBackStack("CoffeeSlt2Duration");
                        ft.commit();
                    }else if(!percentageFlag)
                    {
                        Toast.makeText(getActivity(),"Input invalid. Number expected.",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getActivity(),"Overall control efficiency entry must lie between 0 to 100.",Toast.LENGTH_SHORT).show();
                    }


                }else{
                    Toast.makeText(getActivity(),"You must select a operation",Toast.LENGTH_SHORT).show();
                }
            }
        });
        ImageView helpRate = (ImageView)vEF.findViewById(R.id.helpRate);
        helpRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle(Html.fromHtml("<font color='#0000ff'>About Activity Rate</font>"))
                        .setMessage("You can enter how many green coffee beans been processed during a period of time .(in tonnes) ")
                        .setIcon(android.R.drawable.btn_star_big_on)
                        .setPositiveButton("Learn More About Different Fuels", null)
                        .setNegativeButton(android.R.string.no, null).show();

            }
        });
        return vEF;
    }
    private boolean getValue()
    {
        try {
            activityRate = Double.parseDouble(etActivityRate.getText().toString());
            efficiency = Double.parseDouble(etEfficiency.getText().toString());
            if(efficiency<0 || efficiency>100)
            {this.percentageFlag = true;
                return false;
            }
            c.sharedBundle.putDouble("efrate",activityRate);
            c.sharedBundle.putDouble("eff",efficiency);
            return true;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }
}
