package monash.emission.CoffeeRoastingFragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import monash.emission.R;

/**
 * Created by Ranger on 2017/8/14.
 */

public class EmissionFactor extends Fragment {
    private Spinner spinner;
    private EditText etActivityRate;
    private EditText etEfficiency;
    private View vDisplayUnit;
    private final String[] mItems = {"Batch Roaster with Thermal Oxidiser","Continuous Cooler with Cyclone","Continuous Roaster","Continuous Roaster with Thermal Oxidiser","Green Coffee Bean Screening, Handling, and Storage System with Fabric Filter"};
    private boolean isItemSelected ;
    private int selection;
    private TextView tvDisplay;
    private Button butCalculate;
    private Double activityRate;
    private Double efficiency;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        vDisplayUnit = inflater.inflate(R.layout.fragment_emission_factor, container, false);
        isItemSelected = false;
        tvDisplay = (TextView)vDisplayUnit.findViewById(R.id.emission_factor_tv_display);
        etActivityRate=(EditText) vDisplayUnit.findViewById(R.id.emission_factor_et_ar) ;
        etEfficiency=(EditText)vDisplayUnit.findViewById(R.id.emission_factor_et_ce) ;
        butCalculate = (Button) vDisplayUnit.findViewById(R.id.emission_factor_but_cal) ;
        spinner = (Spinner) vDisplayUnit.findViewById(R.id.emission_factor_spinner);
        ArrayAdapter<String> adapter=new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item, mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner .setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                isItemSelected = true;
                selection = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        butCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isItemSelected)
                {
                    Toast.makeText(getActivity(),"No Item Selected!",Toast.LENGTH_SHORT).show();
                }
                else if(getValue())
                {
                    String output="";
                    switch (selection)
                    {
                        case 0:
                            output = mItems[0]+"\nParticulate Matter: "+activityRate*0.06*(1-efficiency/100)+"\nVOC: "+activityRate*0.024*(1-efficiency/100)+"\nCarbon Monoxide: "+activityRate*0.28*(1-efficiency/100);
                            break;
                        case 1:
                            output = mItems[1]+"\nParticulate Matter:"+activityRate*0.014*(1-efficiency/100)+"\nVOC: not supported.\nCarbon Monoxide: not supported.";
                            break;
                        case 2:
                            output = mItems[2]+"\nParticulate Matter: "+activityRate*0.33*(1-efficiency/100)+"\nVOC: "+activityRate*0.7*(1-efficiency/100)+"\nCarbon Monoxide: "+activityRate*0.75*(1-efficiency/100);
                            break;
                        case 3:
                            output = mItems[3]+"\nParticulate Matter: "+activityRate*0.046*(1-efficiency/100)+"\nVOC: "+activityRate*0.08*(1-efficiency/100)+"\nCarbon Monoxide: "+activityRate*0.049*(1-efficiency/100);
                            break;
                        case 4:
                            output = mItems[4]+"\nParticulate Matter:"+activityRate*0.03*(1-efficiency/100)+"\nVOC: not supported.\nCarbon Monoxide: not supported.";
                            break;
                        default:
                            output="Internal Error: Switch Case not found. See emissionFactor.java line 84";
                    }

                    tvDisplay.setText(output);
                }
                else
                {
                    Toast.makeText(getActivity(),"Input invalid. Number expected.",Toast.LENGTH_SHORT).show();
                }
            }
        });


        return vDisplayUnit;



    }

    private boolean getValue()
    {
        try {
            activityRate = Double.parseDouble(etActivityRate.getText().toString());
            efficiency = Double.parseDouble(etEfficiency.getText().toString());
            return true;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }


}

