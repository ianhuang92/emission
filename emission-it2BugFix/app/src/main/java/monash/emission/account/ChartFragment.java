package monash.emission.account;


import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
                etStart.setText(dayOfMonth + "-" + (monthOfYear+1) + "-" + year);
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
                etEnd.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
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
        mChart.getDescription().setEnabled(false);
        //  mChart.setDrawBarShadow(true);
        mChart.setDrawGridBackground(true);
        mChart.getLegend().setEnabled(false);
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

        final ArrayList<String> labels = new ArrayList<>();
        ArrayList<BarEntry> entry = new ArrayList<BarEntry>();
        float x_index = 0;
        ArrayList<EmissionRecord> tempList = currentUser.sortByDate(dateStart,dateEnd);

        for (EmissionRecord emissionRecord: tempList){
            entry.add(new BarEntry(x_index,(float) emissionRecord.getLevel()));
            labels.add(sdf.format(emissionRecord.getStartdate()) + " to " + sdf.format(emissionRecord.getEnddate()));
            x_index += 1;
        }
        BarDataSet barDataSet = new BarDataSet(entry,"");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        BarData barData = new BarData(barDataSet);
        barData.setDrawValues(true);
        barData.setValueTextSize(24);
        XAxis x = mChart.getXAxis();
        x.setGranularity(1f);
        //x.setValueFormatter(formater);
        //x.setSpaceMax(15);
        x.setPosition(XAxis.XAxisPosition.BOTTOM);
        x.setLabelRotationAngle(45f);
        mChart.getXAxis().setTextSize(16);
        // barData.setBarWidth(1f);
        mChart.setData(barData);
        // mChart.setFitBars(true);
        mChart.animateY(3000);
    }
}
