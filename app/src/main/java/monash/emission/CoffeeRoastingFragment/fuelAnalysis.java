package monash.emission.CoffeeRoastingFragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import monash.emission.R;
import monash.emission.entity.CarbonMonoxide;
import monash.emission.entity.Entity;
import monash.emission.entity.SO2;

/**
 * Created by Ranger on 2017/8/13.
 */

public class fuelAnalysis extends Fragment {
    private Button butNext;
    private EditText etFuelFlow;
    private EditText etOperatingHour;
    private EditText etPollutantConcentrate;
    private TextView tvDisplay;
    private Spinner spinner;
    private View vDisplayUnit;
    private Entity substance;
    private boolean itemSelected;
    private int oH;
    private int fU;
    private int pC;
    private double result;



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vDisplayUnit = inflater.inflate(R.layout.fuel_analysis, container, false); //fuel analysis
        butNext = (Button) vDisplayUnit.findViewById(R.id.fuel_but_cal);
        etFuelFlow = (EditText) vDisplayUnit.findViewById(R.id.fuel_et_fuel_usage);
        etOperatingHour = (EditText) vDisplayUnit.findViewById(R.id.fuel_et_OH);
        etPollutantConcentrate=(EditText) vDisplayUnit.findViewById(R.id.fuel_et_PC);
        tvDisplay = (TextView) vDisplayUnit.findViewById(R.id.fuel_tv_display);
        spinner = (Spinner) vDisplayUnit.findViewById(R.id.spinner);
        itemSelected = false;
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                Entity[] pollutant = {new SO2(), new CarbonMonoxide()};
                substance = pollutant[pos];
                itemSelected = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // itemSelected = false;
            }

            ;
        });
        butNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!itemSelected )
                {
                    Toast.makeText(getActivity(),"No Item Selected!",Toast.LENGTH_SHORT).show();

                }
                else if(getValue())
                {
                  result = fU*0.01*pC*oH*substance.MW/substance.EW;
                    tvDisplay.setText("Emission of "+substance.name+" via Fuel Analysis:\n"+result+" kg/year.");
                    if (result > 10000)  //10 tones per year
                    {
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Warning")
                                .setMessage("You emission level is " + result + " ,which have exceed the threshold of '" + substance.name + "' (10000 tonnes per year)")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton(android.R.string.yes, null)
                                .setNegativeButton(android.R.string.no, null).show();
                    }
                }
                else
                {
                    Toast.makeText(getActivity(),"Input invalid. Integer expected.",Toast.LENGTH_SHORT).show();
                }


            }
        });


        return vDisplayUnit;

    }

    private boolean getValue()
    {
        try {
            this.pC = Integer.parseInt(etPollutantConcentrate.getText().toString());
            this.oH = Integer.parseInt(etOperatingHour.getText().toString());
            this.fU = Integer.parseInt(etFuelFlow.getText().toString());
        }
        catch (NumberFormatException e)
        {
            return false;
        }
        return true;
    }





}
