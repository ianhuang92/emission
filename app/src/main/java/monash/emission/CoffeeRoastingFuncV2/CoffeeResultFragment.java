package monash.emission.CoffeeRoastingFuncV2;


import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import monash.emission.R;
import monash.emission.entity.CarbonMonoxide;
import monash.emission.entity.EmissionRecord;
import monash.emission.entity.SO2;
import monash.emission.entity.UserInfo;

/**
 * A simple {@link Fragment} subclass.
 */
public class CoffeeResultFragment extends Fragment {

    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private View vResult;
    private CoffeeRoastActivity c;
    private double resultSO2;
    private double resultCO;
    private final String[] mItems = {"Batch Roaster with Thermal Oxidiser","Continuous Cooler with Cyclone","Continuous Roaster","Continuous Roaster with Thermal Oxidiser","Green Coffee Bean Screening, Handling, and Storage System with Fabric Filter"};

    SharedPreferences sharePreference;
    private static final String myPreference = "MySharedPreference";
    private double CO;
    public CoffeeResultFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vResult = inflater.inflate(R.layout.fragment_coffee_result, container, false);
        TextView SO2Text = (TextView)vResult.findViewById(R.id.resultSO2);
        TextView COText = (TextView)vResult.findViewById(R.id.resultCO);
        TextView dateText = (TextView)vResult.findViewById(R.id.dateText);
        TextView recommandation = (TextView)vResult.findViewById(R.id.recommandation);
        sharePreference = getActivity().getSharedPreferences(myPreference, Context.MODE_PRIVATE);
        c = (CoffeeRoastActivity)getActivity();
        int type = c.sharedBundle.getInt("CheckedID");

        double pCs = c.sharedBundle.getDouble("SO2");
        double pCc = c.sharedBundle.getDouble("CO");
        double fU = c.sharedBundle.getDouble("usage");
        final String startDate = c.sharedBundle.getString("StartDate");
        final String endDate = c.sharedBundle.getString("EndDate");
        if (type == 0){ //fuel analysis


            dateText.setText("From " + startDate + " to " + endDate);
            double oH = 0;

            try {
                Date sd = sdf.parse(startDate);
                Date ed = sdf.parse(endDate);
                // Calendar cal1 = Calendar.getInstance();cal1.setTime(sd);
                // Calendar cal2 = Calendar.getInstance();cal2.setTime(ed);
                oH = calculateDays(sd, ed)* (c.sharedBundle.getDouble("AvgHour"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            resultSO2 = fU*0.01*pCs*oH*SO2.MW/ SO2.EW;  //Ekpy, i = Qf * pollutant concentration in fuel * (MWp / EWf) * OpHrs
            resultSO2 = Double.parseDouble(String.format("%.2f", resultSO2));

            resultCO = fU*0.01*pCc*oH* CarbonMonoxide.MW/CarbonMonoxide.EW;  //Ekpy, i = Qf * pollutant concentration in fuel * (MWp / EWf) * OpHrs
            resultCO = Double.parseDouble(String.format("%.2f", resultCO));

            if (resultSO2 > 10000 && resultCO < 10000){
                SO2Text.setTextColor(Color.RED);
                SO2Text.setText("Emission of SO2 via Fuel Analysis: "+ resultSO2+" kg/year.");
                COText.setTextColor(Color.GREEN);
                COText.setText("Emission of CO via Fuel Analysis: "+ resultCO+" kg/year.");
                recommandation.setText("You SO2 is over the threshold, you should lower you SO2 emission, recommendation is PLACEHOLDER. \n You CO is good, keep going");
                new AlertDialog.Builder(getActivity())
                        .setTitle(Html.fromHtml("<font color='#FF0000'>Warning</font>"))
                        .setMessage("You SO2 level is " + resultSO2 + " ,which have exceed the threshold of 'SO2' (10000 tonnes per year)")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, null)
                        .setNegativeButton(android.R.string.no, null).show();
            }else if (resultSO2 < 10000 && resultCO > 10000){
                SO2Text.setTextColor(Color.GREEN);
                SO2Text.setText("Emission of SO2 via Fuel Analysis: "+ resultSO2+" kg/year.");
                COText.setTextColor(Color.RED);
                COText.setText("Emission of CO via Fuel Analysis: "+ resultCO+" kg/year.");
                recommandation.setText("You CO is over the threshold, you should lower you SO2 emission, recommendation is PLACEHOLDER. \n You SO2 is good, keep going");
                new AlertDialog.Builder(getActivity())
                        .setTitle(Html.fromHtml("<font color='#FF0000'>Warning</font>"))
                        .setMessage("You CO level is " + resultCO + " ,which have exceed the threshold of 'CO' (10000 tonnes per year)")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, null)
                        .setNegativeButton(android.R.string.no, null).show();
            }else if (resultSO2 > 10000 && resultCO > 10000){
                SO2Text.setTextColor(Color.RED);
                SO2Text.setText("Emission of SO2 via Fuel Analysis: "+ resultSO2+" kg/year.");
                COText.setTextColor(Color.RED);
                COText.setText("Emission of CO via Fuel Analysis: "+ resultCO+" kg/year.");
                recommandation.setText("Both SO2 and CO is over the threshold, you should lower you SO2 emission, recommendation is PLACEHOLDER. ");
                new AlertDialog.Builder(getActivity())
                        .setTitle(Html.fromHtml("<font color='#FF0000'>Warning</font>"))
                        .setMessage("You SO2 level is " + resultSO2 + " ,which have exceed the threshold of 'SO2' (10000 tonnes per year) \n You CO level is " + resultCO + " ,which have exceed the threshold of 'CO' (10000 tonnes per year)")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, null)
                        .setNegativeButton(android.R.string.no, null).show();
            }else{
                SO2Text.setTextColor(Color.GREEN);
                SO2Text.setText("Emission of SO2 via Fuel Analysis: "+ resultSO2+" kg/year.");
                COText.setTextColor(Color.GREEN);
                COText.setText("Emission of CO via Fuel Analysis: "+ resultCO+" kg/year.");
                recommandation.setText("Both SO2 and CO is good, all under the threshold, keep going. Tips PLACEHOLDER ");

            }
        }else if (type == 1){ //emission factor
            COText.setText("");
            dateText.setText("");
            String output="";
             Double activityRate = c.sharedBundle.getDouble("efrate");
             Double efficiency = c.sharedBundle.getDouble("eff");
            switch (c.sharedBundle.getInt("efindex"))
            {
                case 0://Batch Roaster with Thermal Oxidiser
                    CO = activityRate*0.28*(1-efficiency/100);
                    CO = Double.parseDouble(String.format("%.2f", CO));
                    output = mItems[0]+"\n\nParticulate Matter: "+ String.format("%.2f", activityRate*0.06*(1-efficiency/100))+"\nVOC: "+ String.format("%.2f", activityRate*0.024*(1-efficiency/100))+"\nCarbon Monoxide: "+CO;
                    break;
                case 1://Continuous Cooler with Cyclone

                    output = mItems[1]+"\n\nParticulate Matter:"+ String.format("%.2f", activityRate*0.014*(1-efficiency/100)) +"\nVOC: not supported.\nCarbon Monoxide: not supported.";
                    break;
                case 2://Continuous Roaster
                    CO = activityRate*0.75*(1-efficiency/100);
                    CO = Double.parseDouble(String.format("%.2f", CO));
                    output = mItems[2]+"\n\nParticulate Matter: "+ String.format("%.2f", activityRate*0.33*(1-efficiency/100)) +"\nVOC: "+ String.format("%.2f", activityRate*0.7*(1-efficiency/100)) +"\nCarbon Monoxide: "+CO;
                    break;
                case 3://Continuous Roaster with Thermal Oxidiser
                    CO = activityRate*0.049*(1-efficiency/100);
                    CO = Double.parseDouble(String.format("%.2f", CO));
                    output = mItems[3]+"\n\nParticulate Matter: "+ String.format("%.2f", activityRate*0.046*(1-efficiency/100)) +"\nVOC: "+ String.format("%.2f", activityRate*0.08*(1-efficiency/100)) +"\nCarbon Monoxide: "+CO;
                    break;
                case 4://Green Coffee Bean Screening, Handling, and Storage System with Fabric Filter
                    output = mItems[4]+"\n\nParticulate Matter:"+ String.format("%.2f", activityRate*0.03*(1-efficiency/100)) +"\nVOC: not supported.\nCarbon Monoxide: not supported.";
                    break;
                default:
                    output="Internal Error: Switch Case not found. See emissionFactor.java line 84";
            }
            SO2Text.setText(output);
            recommandation.setText("Recommandation PLACEHOLDER");
            if (CO > 10000){
                recommandation.setText("Recommandation PLACEHOLDER");
                new AlertDialog.Builder(getActivity())
                        .setTitle(Html.fromHtml("<font color='#FF0000'>Warning</font>"))
                        .setMessage("You emission level is " + CO + " ,which have exceed the threshold of '" + "Carbon Monoxide(CO)" + "' (10000 tonnes per year)")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, null)
                        .setNegativeButton(android.R.string.no, null).show();
            }
        }
        Button record = (Button)vResult.findViewById(R.id.recordBtn);
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.dialog);
                dialog.setTitle("Data Saved");
                TextView detail = (TextView)dialog.findViewById(R.id.detailText);
                detail.setText("From " + startDate + " to " + endDate + "\n"
                + "Fuel Type: " + c.sharedBundle.getString("fuel") + "\n" + "SO2 level: " + resultSO2 + "\nCO level: " + resultCO + "\nUser: Guest" );
                try {
                    EmissionRecord record = new EmissionRecord("pollutant","SO2",sdf.parse(startDate),sdf.parse(endDate),sdf.parse(String.valueOf(new Date())),resultSO2);
                    EmissionRecord record2 = new EmissionRecord("pollutant","CO",sdf.parse(startDate),sdf.parse(endDate),sdf.parse(String.valueOf(new Date())),resultCO);
                    ArrayList<EmissionRecord> er = new ArrayList<EmissionRecord>();
                    er.add(record);
                    er.add(record2);

                    if(c.sharedBundle.getInt("login") == 0){
                        String user = sharePreference.getString("guest",null);
                        if (user == null){
                            //create new guest
                            UserInfo guest = new UserInfo("guest","guest","Coffee Roasting",er);
                            SharedPreferences.Editor editor = sharePreference.edit();
                            editor.putString("guest", new Gson().toJson(guest));
                            editor.commit();
                        }else{
                            UserInfo guest = new Gson().fromJson(user,UserInfo.class);
                            guest.addEmission(record);
                            guest.addEmission(record2);
                        }
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Button ok = (Button) dialog.findViewById(R.id.okBTN);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
//                Button dashboard = (Button)dialog.findViewById(R.id.dashboardBTN);
//                dashboard.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                    }
//                });
                dialog.show();
            }
        });
        return vResult;
    }
    /* This method is used to find the no of days between the given dates */
    public static long calculateDays(Date dateEarly, Date dateLater) {
        return (dateLater.getTime() - dateEarly.getTime()) / (24 * 60 * 60 * 1000);
    }

}
