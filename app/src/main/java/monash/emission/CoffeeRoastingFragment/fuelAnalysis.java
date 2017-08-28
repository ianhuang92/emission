package monash.emission.CoffeeRoastingFragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import monash.emission.R;
import monash.emission.entity.CarbonMonoxide;
import monash.emission.entity.Entity;
import monash.emission.entity.SO2;

/**
 * Created by Ranger on 2017/8/13.
 * fuel analysis
 * using Abstract class entity
 * using child classes of Entity: SO2, CarbonMonoxide
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
    private int oH;  //operate hour
    private int fU;  // fuel usage
    private int pC;   //pollutant concentration
    private double result;
    private boolean percentageFlag;



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vDisplayUnit = inflater.inflate(R.layout.fuel_analysis, container, false); //fuel analysis
        butNext = (Button) vDisplayUnit.findViewById(R.id.fuel_but_cal);
        etFuelFlow = (EditText) vDisplayUnit.findViewById(R.id.fuel_et_fuel_usage);
        etOperatingHour = (EditText) vDisplayUnit.findViewById(R.id.fuel_et_OH);
        etPollutantConcentrate=(EditText) vDisplayUnit.findViewById(R.id.fuel_et_PC);
        tvDisplay = (TextView) vDisplayUnit.findViewById(R.id.fuel_tv_display);
        spinner = (Spinner) vDisplayUnit.findViewById(R.id.spinner);
        ImageView fuelTootip = (ImageView) vDisplayUnit.findViewById(R.id.fuelTooltip);
        fuelTootip.setOnHoverListener(new View.OnHoverListener() {
            @Override
            public boolean onHover(View v, MotionEvent event) {
                return false;
            }
        });
        itemSelected = false;
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
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
                  result = fU*0.01*pC*oH*substance.MW/substance.EW;  //Ekpy, i = Qf * pollutant concentration in fuel * (MWp / EWf) * OpHrs
                 result = Double.parseDouble(String.format("%.2f", result));
                    //where:
                 //Ekpy, i = emissions of pollutant i, kg/yr
                 // Qf = fuel use, kg/hr
                 // MWp = molecular weight of pollutant emitted, kg/kg-mole
                 //  EWf = elemental weight of substance in fuel, kg/kg-mole
                 // OpHrs= operating hours, hr/yr
                    tvDisplay.setText("Emission of "+substance.name+" via Fuel Analysis:\n"+result+" kg/year.");
                    if (result > 10000)  //check whether exceed the threshold 10 tones per year
                    {
                        new AlertDialog.Builder(getActivity())
                                .setTitle(Html.fromHtml("<font color='#FF0000'>Warning</font>"))
                                .setMessage("You emission level is " + result + " ,which have exceed the threshold of '" + substance.name + "' (10000 tonnes per year)")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton(android.R.string.yes, null)
                                .setNegativeButton(android.R.string.no, null).show();
                    }
                }
                else
                {
                    if (!percentageFlag)
                    Toast.makeText(getActivity(),"Input invalid. Integer expected.",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getActivity(),"pollutant concentration entry must lie between 0 and 100.",Toast.LENGTH_SHORT).show();
                }


            }
        });


        return vDisplayUnit;

    }

    /**
     * +    /*
     +    convert text into desired data
     +    data kept in oH,pC,fU
     +    return false if fail to convert
     +    return true if successful
     +     */
    private boolean getValue()
    {
        try {
            this.pC = Integer.parseInt(etPollutantConcentrate.getText().toString());
            this.oH = Integer.parseInt(etOperatingHour.getText().toString());
            this.fU = Integer.parseInt(etFuelFlow.getText().toString());
            if (this.pC<0 || this.pC > 100) {
                this.percentageFlag = true;
                return false;
            }
        }
        catch (NumberFormatException e)
        {
            return false;
        }
        return true;
    }





}
