package monash.emission.account;


import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import monash.emission.R;
import monash.emission.entity.EmissionRecord;
import monash.emission.entity.UserInfo;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChartFragment extends Fragment {

    View vChart;
    private BarChart mChart;
    private EditText etStart;
    private EditText etEnd;
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private UserInfo currentUser;
    private UserDashboard u;
    private Button but;
    private Date dateStart;
    private Date dateEnd;
    SharedPreferences sharePreference;
    private static final String myPreference = "MySharedPreference";

    public ChartFragment() {
        // Required empty public constructor
    }

    /**
     * display date picker dialog for start date edit text
     */
    protected void showDatePickDlgStart() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(u, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                etStart.setText(String.format("%02d",dayOfMonth) + "-" + String.format("%02d",monthOfYear + 1) + "-" + year);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)-1, calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    /**
     * display date picker dialog for end date edit text
     */
    protected void showDatePickDlgEnd() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(u, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                etEnd.setText(String.format("%02d",dayOfMonth)  + "-" + String.format("%02d",monthOfYear + 1) + "-" + year);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vChart = inflater.inflate(R.layout.fragment_chart, container, false);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        u = (UserDashboard)getActivity();
        sharePreference = getActivity().getSharedPreferences(myPreference, Context.MODE_PRIVATE);
        currentUser = new Gson().fromJson(u.userBundle.getString("userdata"),UserInfo.class);
        //currentUser = new Gson().fromJson(sharePreference.getString(currentUser.getUsername(),null),UserInfo.class);
        etStart=(EditText)vChart.findViewById(R.id.et_startDate_chart);
        etEnd = (EditText)vChart.findViewById(R.id.et_endDate_chart);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        etStart.setText(sdf.format(cal.getTime()));
        etEnd.setText(sdf.format(calendar.getTime()));  //default time as current time
        etStart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    showDatePickDlgStart();
                    return true;
                }
                return false;
            }
        });
        etStart.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePickDlgStart();
                }
            }
        });
        etEnd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    showDatePickDlgEnd();
                    return true;
                }
                return false;
            }
        });
        etEnd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePickDlgEnd();
                }
            }
        });
        but = (Button)vChart.findViewById(R.id.but_run_chart) ;
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate())
                        plot();
            }
        });


        mChart = (BarChart) vChart.findViewById(R.id.barChart);
        //  mChart.setDrawBarShadow(true);
        mChart.setDrawGridBackground(true);
        mChart.getLegend().setEnabled(true);
        //set up the chart data source

        if (currentUser.getEmissionRecords().size() == 0){
            mChart.setNoDataText("You don't have any records to show");
        }else {
            mChart.setNoDataText("Press Run to launch Chart");
        }
        if(validate()){
        plot();}
            return vChart;

    }
    private boolean validate()
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        try{
            dateStart = simpleDateFormat.parse(etStart.getText().toString());
            dateEnd = simpleDateFormat.parse(etEnd.getText().toString());
        }
       catch (ParseException e) {
           return false;
        }
        if (dateStart.compareTo(dateEnd) > 0) { //startDate大于endDate则报错
            return false;
        }

        else

        if (dateEnd.compareTo(Calendar.getInstance().getTime()) > 0) { //endDate大于当前日期则报错
            return false;
        } else

        return true;
    }



    //CALL this method ONLY when you passed validation method above
    private void plot()
    {
        SimpleDateFormat mSdf = new SimpleDateFormat("dd.MM");

        ArrayList<String> labels = new ArrayList<>();
       // List<String> xVals = new ArrayList<String>();
        ArrayList<BarEntry> entryC = new ArrayList<BarEntry>();
        ArrayList<BarEntry> entryS = new ArrayList<BarEntry>();
        int x_indexC = 0;
        int x_indexS = 0;
        ArrayList<EmissionRecord> tempList = currentUser.sortByDate(dateStart,dateEnd);

        for (EmissionRecord emissionRecord: tempList){
            if (emissionRecord.getName().equalsIgnoreCase("CO")) {  /*CO*/
                //Log.i("Type-> CO",emissionRecord.getName());
                entryC.add(new BarEntry((float) emissionRecord.getLevel(),x_indexC));
                labels.add(mSdf.format(emissionRecord.getStartdate()) + " to " + mSdf.format(emissionRecord.getEnddate()));
                x_indexC += 1;
            } else /*SO2*/
            {
               // Log.i("Type-> SO2",emissionRecord.getName());
                entryS.add(new BarEntry((float) emissionRecord.getLevel(),x_indexS));
                //labelsS.add(mSdf.format(emissionRecord.getStartdate()) + " to " + mSdf.format(emissionRecord.getEnddate()));
                x_indexS += 1;
            }
            //xVals.add(emissionRecord.getType());

        }
        BarDataSet barDataSetC = new BarDataSet(entryC,"CO");
        BarDataSet barDataSetS = new BarDataSet(entryS,"SO2");
        Log.i("-----","-------------------");
        Log.i("CO",entryC.size()+"");
        Log.i("SO2",entryS.size()+"");
        Log.i("SO2_index",x_indexS+"");
        Log.i("CO_index",x_indexC+"");
        Log.i("labels",labels.size()+"");
        barDataSetC.setColors(ColorTemplate.LIBERTY_COLORS);
        barDataSetS.setColors(ColorTemplate.COLORFUL_COLORS);
        List<IBarDataSet> barDataSets = new ArrayList<>();
        barDataSets.add(barDataSetC);
        barDataSets.add(barDataSetS);
        BarData barData=new BarData(labels,barDataSets);
        barData.setDrawValues(true);
        barData.setValueTextSize(24);

        XAxis x = mChart.getXAxis();
<<<<<<< HEAD
       // x.setGranularity(1f);
=======
        x.setGranularity(1f);
        
>>>>>>> origin/it33
        //x.setValueFormatter(formater);
        //x.setSpaceMax(15);
        x.setPosition(XAxis.XAxisPosition.BOTTOM);
        x.setLabelRotationAngle(45f);
        mChart.getXAxis().setTextSize(16);
        // barData.setBarWidth(1f);
        mChart.setData(barData);
        // mChart.setFitBars(true);
        mChart.animateY(3000);
        mChart.setScrollContainer(true);
        mChart.setDragEnabled(true);
        mChart.setDragDecelerationEnabled(true);
        mChart.setTouchEnabled(true);
        mChart.setPinchZoom(true);
       // mChart.zoom(1,1,0,0);

    }
}
