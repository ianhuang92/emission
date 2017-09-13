package monash.emission.account;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import monash.emission.R;
import monash.emission.entity.EmissionRecord;
import monash.emission.entity.UserInfo;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChartFragment extends Fragment {

    View vChart;
    private BarChart mChart;
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private UserInfo currentUser;
    private UserDashboard u;
    SharedPreferences sharePreference;
    private static final String myPreference = "MySharedPreference";

    public ChartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vChart = inflater.inflate(R.layout.fragment_chart, container, false);

        u = (UserDashboard)getActivity();
        sharePreference = getActivity().getSharedPreferences(myPreference, Context.MODE_PRIVATE);
        currentUser = new Gson().fromJson(u.userBundle.getString("userdata"),UserInfo.class);
        currentUser = new Gson().fromJson(sharePreference.getString(currentUser.getUsername(),null),UserInfo.class);
        mChart = (BarChart) vChart.findViewById(R.id.barChart);
        mChart.getDescription().setEnabled(false);
        //  mChart.setDrawBarShadow(true);
        mChart.setDrawGridBackground(true);
        mChart.getLegend().setEnabled(false);
        //set up the chart data source
        if (currentUser.getEmissionRecords().size() == 0){
            mChart.setNoDataText("You don't have any records to show");
        }else{
            mChart.setNoDataText("Loading Chart.....");
            final ArrayList<String> labels = new ArrayList<>();
            ArrayList<BarEntry> entry = new ArrayList<BarEntry>();
            float x_index = 0;

            for (EmissionRecord emissionRecord: currentUser.getEmissionRecords()){
                entry.add(new BarEntry(x_index,(float) emissionRecord.getLevel()));
                labels.add(sdf.format(emissionRecord.getStartDate()) + " to " + sdf.format(emissionRecord.getEndDate()));
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


        return vChart;
    }

}
