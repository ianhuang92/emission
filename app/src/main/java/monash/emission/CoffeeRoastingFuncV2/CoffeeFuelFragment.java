package monash.emission.CoffeeRoastingFuncV2;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
public class CoffeeFuelFragment extends Fragment {

    private View vFuel;
    private CoffeeRoastActivity c;

    public CoffeeFuelFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vFuel = inflater.inflate(R.layout.fragment_coffee_fuel, container, false);
        c = (CoffeeRoastActivity) getActivity();
        final EditText usage = (EditText)vFuel.findViewById(R.id.fuelUsageInput);
        final RadioGroup rdg = (RadioGroup) vFuel.findViewById(R.id.fuelRadio);

        Button btn = (Button) vFuel.findViewById(R.id.button6);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = c.fragmentManager.beginTransaction();
                ft.replace(R.id.content_frame, new CoffeeSelectionFragment());
                ft.addToBackStack("CoffeeSlt2Duration");
                ft.commit();
            }
        });
        final double[] concentrationRateCO = {0};
        final double[] concentrationRateSO2 = {0};
        //final RadioButton fuelBtn = (RadioButton) vFuel.findViewById(id);
        Button cal = (Button) vFuel.findViewById(R.id.calculateBtn);
        cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = rdg.getCheckedRadioButtonId();
                RadioButton fuelBtn = (RadioButton) vFuel.findViewById(id);
                if (fuelBtn.getText().toString().startsWith("G")){
                    concentrationRateCO[0] = 2.34;
                    concentrationRateSO2[0] = 1.17;
                    c.sharedBundle.putString("fuel","Gasoline");
                }else if(fuelBtn.getText().toString().startsWith("F")){
                    concentrationRateCO[0] = 3.45;
                    c.sharedBundle.putString("fuel","Fossil fuel");
                    concentrationRateSO2[0] = 3.2;
                }else if(fuelBtn.getText().toString().startsWith("D")){
                    concentrationRateCO[0] = 6;
                    c.sharedBundle.putString("fuel","Diesel");
                    concentrationRateSO2[0] = 0.001;
                }
                if (concentrationRateCO[0] == 0){
                    Toast.makeText(getActivity(),"You must select a fuel",Toast.LENGTH_SHORT).show();
                }else{
                    c.sharedBundle.putDouble("SO2", concentrationRateSO2[0]);
                    c.sharedBundle.putDouble("CO", concentrationRateCO[0]);
                    try{
                        double input = Double.parseDouble(usage.getText().toString());
                        if (input > 0){
                            c.sharedBundle.putDouble("usage",input);

                            FragmentTransaction ft = c.fragmentManager.beginTransaction();
                            ft.replace(R.id.content_frame, new CoffeeResultFragment());
                            ft.addToBackStack("CoffeeSlt2Duration");
                            ft.commit();

                        }else{
                            Toast.makeText(getActivity(),"Fuel usage should be positive",Toast.LENGTH_SHORT).show();
                        }
                    }catch (NumberFormatException e){
                        Toast.makeText(getActivity(),"Fuel usage should only be numbers",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        ImageView helpIcon = (ImageView)vFuel.findViewById(R.id.helpIcon);
        helpIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle(Html.fromHtml("<font color='#0000ff'>Tips</font>"))
                        .setMessage("Choose which fuel is used in your machine. ")
                        .setIcon(android.R.drawable.btn_star_big_on)
                        .setPositiveButton("Learn More About Different Fuels", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://en.wikipedia.org/wiki/Fuel#Chemical"));
                                startActivity(browserIntent);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });
        return vFuel;
    }

}
