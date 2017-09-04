package monash.emission.CoffeeRoastingFuncV2;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import monash.emission.MainActivity;
import monash.emission.R;
import monash.emission.account.AccountActivity;

/**
 * Created by Ranger on 2017/8/25.
 */

public class CoffeeSelectionFragment extends Fragment {
    private RadioGroup rGroup;
    private int checkedID;
    private String checkedMsg;
    private Button butNext;
    private CoffeeRoastActivity c;
    private Button butHome;

private View vDisplayUnit;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//inflate converts an XML layout file into corresponding ViewGroups and views
        vDisplayUnit = inflater.inflate(R.layout.fragment_coffee_selection, container, false);
        c=(CoffeeRoastActivity)getActivity();
        butNext = (Button)vDisplayUnit.findViewById(R.id.but_coffeeSelection_Next) ;
        butHome = (Button)vDisplayUnit.findViewById(R.id.but_coffeeSelection_Home) ;;


//init radio group and set listener
        rGroup = (RadioGroup)vDisplayUnit.findViewById(R.id.frag_coffee_calMethod);

        butHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(c.getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });

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
        c.sharedBundle.putInt("login",0);
        //c.sharedBundles.add(bundle);
        c.sharedBundle = bundle;
        FragmentTransaction ft = c.fragmentManager.beginTransaction();
        //fragment transaction
        if (checkedID ==0 ) {
            ft.replace(R.id.content_frame, new CoffeeDurationFragment());
            ft.addToBackStack("CoffeeSlt2Duration");
            ft.commit();
        }else if ( checkedID == 1){
            ft.replace(R.id.content_frame, new CoffeeDurationFragment());
            ft.addToBackStack("CoffeeSlt2Duration");
            ft.commit();
        }else if (checkedID == 3){
            c.fragmentManager.beginTransaction().replace(R.id.content_frame, new CoffeeInfoMBFragment()).commit();
        }
    else
        //navigate to your following fragments
        //modify following statement if needed
        //TODO
            c.fragmentManager.beginTransaction().replace(R.id.content_frame, new CoffeeInfoDMFragment()).commit();
    }

        });


        ImageView helpFA = (ImageView)vDisplayUnit.findViewById(R.id.helpFuel);
        ImageView helpEF = (ImageView)vDisplayUnit.findViewById(R.id.helpEF);
        ImageView helpDM = (ImageView)vDisplayUnit.findViewById(R.id.helpDM);
        ImageView helpMB = (ImageView)vDisplayUnit.findViewById(R.id.helpMB);
        helpFA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle(Html.fromHtml("<font color='#0000ff'>About Fuel Analysis</font>"))
                        .setMessage("Fuel analysis is an example of an engineering calculation and can be used to predict SO2 " +
                                "and metal emissions based on application of conservation laws, if fuel rate is measured.\n\n" +
                                "The presence of certain elements in fuels may be used to predict their presence in emission " +
                                "streams.\n\n This includes elements such as sulfur, which may be converted into other " +
                                "compounds during the combustion process. ")
                        .setIcon(android.R.drawable.btn_star_big_on)
                        .setPositiveButton("Learn More from NPI website", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://npi.gov.au/substances/fact-sheets"));
                                startActivity(browserIntent);
                            }
                        })

                        .setNegativeButton(android.R.string.no, null).show();
            }
        });
        helpEF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle(Html.fromHtml("<font color='#0000ff'>About Emission Factor</font>"))
                        .setMessage("An emission factor is a tool that is used to estimate emissions to the environment. \nIn this " +
                                "case, it relates the quantity of substances emitted from a source to some common " +
                                "activity associated with those emissions. \n\nEmission factors are obtained from US, European, " +
                                "and Australian sources and are usually expressed as the weight of a substance emitted " +
                                "divided by the unit weight, volume, distance, or duration of the activity emitting the " +
                                "substance (eg. kilograms of sulfur dioxide emitted per tonne of air-dried unbleached pulp " +
                                "produced).")
                        .setIcon(android.R.drawable.btn_star_big_on)
                        .setPositiveButton("Learn More from NPI website", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://npi.gov.au/substances/fact-sheets"));
                                startActivity(browserIntent);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });
        helpDM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle(Html.fromHtml("<font color='#0000ff'>About Direct Measurement</font>"))
                        .setMessage("You may wish to undertake direct measurement in order to report to the NPI, particularly " +
                                "if you already do so in order to meet other regulatory requirements. However, the NPI" +
                                "does not require you to undertake additional sampling and measurement.\n\n For the " +
                                "sampling data to be adequate and able to be used for NPI reporting purposes, it would " +
                                "need to be collected over a period of time, and to be representative of operations for the " +
                                "whole year.")
                        .setIcon(android.R.drawable.btn_star_big_on)
                        .setPositiveButton("Learn More from NPI website", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://npi.gov.au/substances/fact-sheets"));
                                startActivity(browserIntent);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });
        helpMB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle(Html.fromHtml("<font color='#0000ff'>About Mass Balance</font>"))
                        .setMessage("A mass balance identifies the quantity of substance going in and out of an entire facility, " +
                                "process, or piece of equipment.\n\n Emissions can be calculated as the difference between " +
                                "input and output of each listed substance. \n\nAccumulation or depletion of the substance " +
                                "within the equipment should be accounted for in your calculation.")
                        .setIcon(android.R.drawable.btn_star_big_on)
                        .setPositiveButton("Learn More from NPI website", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://npi.gov.au/substances/fact-sheets"));
                                startActivity(browserIntent);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });




        return vDisplayUnit;
    }



}
